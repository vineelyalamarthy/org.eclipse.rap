/*******************************************************************************
 * Copyright (c) 2002, 2009 Innoopract Informationssysteme GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Innoopract Informationssysteme GmbH - initial API and implementation
 *     EclipseSource - ongoing development
 ******************************************************************************/
package org.eclipse.rwt.internal.lifecycle;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.rwt.internal.AdapterFactoryRegistry;
import org.eclipse.rwt.internal.lifecycle.IPhase.IInterruptible;
import org.eclipse.rwt.internal.lifecycle.UIThread.UIThreadTerminatedError;
import org.eclipse.rwt.internal.service.*;
import org.eclipse.rwt.internal.util.ParamCheck;
import org.eclipse.rwt.lifecycle.*;
import org.eclipse.rwt.service.ISessionStore;
import org.eclipse.swt.internal.graphics.TextSizeDetermination;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

/**
 * TODO: [fappel] comment
 * <p></p>
 */
public class RWTLifeCycle extends LifeCycle {

  public static final String UI_THREAD
    = RWTLifeCycle.class.getName() + ".uiThread";
  private static final Integer ZERO = new Integer( 0 );

  private static final String ATTR_SESSION_DISPLAY
    = RWTLifeCycle.class.getName() + "#sessionDisplay";
  private static final String ATTR_REDRAW_CONTROLS
    = RWTLifeCycle.class.getName() + "#redrawControls";
  private static final String INITIALIZED
    = RWTLifeCycle.class.getName() + "Initialized";
  private static final String CURRENT_PHASE
    = RWTLifeCycle.class.getName() + ".currentPhase";
  private static final String PHASE_ORDER
    = RWTLifeCycle.class.getName() + ".phaseOrder";
  private static final String UI_THREAD_THROWABLE
    = UIThreadController.class.getName() + "#UIThreadThrowable";
  private static final String REQUEST_THREAD_RUNNABLE
    = RWTLifeCycle.class.getName() + "#requestThreadRunnable";

  private final static IPhase[] PHASES = new IPhase[] {
    new PrepareUIRoot(),
    new ReadData(),
    new ProcessAction(),
    new Render()
  };

  private static final IPhase[] PHASE_ORDER_STARTUP = new IPhase[] {
    new IInterruptible() {
      public PhaseId execute() throws IOException {
        return null;
      }
      public PhaseId getPhaseID() {
        return PhaseId.PREPARE_UI_ROOT;
      }
    },
    new Render()
  };

  private static final IPhase[] PHASE_ORDER_SUBSEQUENT = new IPhase[] {
    new IPhase() {
      public PhaseId execute() throws IOException {
        return null;
      }
      public PhaseId getPhaseID() {
        return PhaseId.PREPARE_UI_ROOT;
      }
    },
    new ReadData(),
    new IInterruptible() {
      public PhaseId execute() throws IOException {
        new ProcessAction().execute();
        return null;
      }
      public PhaseId getPhaseID() {
        return PhaseId.PROCESS_ACTION;
      }
    },
    new Render()
  };

  private static final class PhaseExecutionError extends ThreadDeath {
    private static final long serialVersionUID = 1L;
    public PhaseExecutionError( final Throwable cause ) {
      initCause( cause );
    }
  }

  private final class UIThreadController implements Runnable {
    public void run() {
      IUIThreadHolder uiThread = ( IUIThreadHolder )Thread.currentThread();
      // [rh] sync exception handling and switchThread (see bug 316676)
      synchronized( uiThread.getLock() ) {
        try {
          try {
            uiThread.updateServiceContext();
            UICallBackManager.getInstance().notifyUIThreadStart();
            continueLifeCycle();
            createUI();
            continueLifeCycle();
            UICallBackManager.getInstance().notifyUIThreadEnd();
          } catch( UIThreadTerminatedError thr ) {
            throw thr;
          } catch( Throwable thr ) {
            IServiceStateInfo stateInfo = ContextProvider.getStateInfo();
            stateInfo.setAttribute( UI_THREAD_THROWABLE, thr );
          }
          // In any case: wait for the thread to be terminated by session timeout
          uiThread.switchThread();
        } catch( UIThreadTerminatedError e ) {
          // If we get here, the session is being invalidated, see
          // UIThread#terminateThread()
          ( ( ISessionShutdownAdapter )uiThread ).processShutdown();
        }
      }
    }
  }

  Runnable uiRunnable;
  private final Set listeners;

  public RWTLifeCycle() {
    listeners = new HashSet();
    listeners.addAll( Arrays.asList( PhaseListenerRegistry.get() ) );
    uiRunnable = new UIThreadController();
  }

  public void execute() throws IOException {
    initialize();
    if( getEntryPoint() != null ) {
      setPhaseOrder( PHASE_ORDER_STARTUP );
    } else {
      setPhaseOrder( PHASE_ORDER_SUBSEQUENT );
    }
    Runnable runnable = null;
    do {
      setRequestThreadRunnable( null );
      executeUIThread();
      runnable = getRequestThreadRunnable();
      if( runnable != null ) {
        runnable.run();
      }
    } while( runnable != null );
  }

  public void addPhaseListener( final PhaseListener listener ) {
    ParamCheck.notNull( listener, "listener" );
    synchronized( listeners ) {
      listeners.add( listener );
    }
  }

  public void removePhaseListener( final PhaseListener listener ) {
    ParamCheck.notNull( listener, "listener" );
    synchronized( listeners ) {
      listeners.remove( listener );
    }
  }

  public Scope getScope() {
    return Scope.APPLICATION;
  }

  public static void requestThreadExec( final Runnable runnable ) {
    setRequestThreadRunnable( runnable );
    switchThread();
  }

  private static void setRequestThreadRunnable( final Runnable runnable ) {
    IServiceStateInfo stateInfo = ContextProvider.getStateInfo();
    stateInfo.setAttribute( REQUEST_THREAD_RUNNABLE, runnable );
  }
  
  private static Runnable getRequestThreadRunnable() {
    IServiceStateInfo stateInfo = ContextProvider.getStateInfo();
    return ( Runnable )stateInfo.getAttribute( REQUEST_THREAD_RUNNABLE );
  }


  //////////////////////////
  // readAndDispatch & sleep

  void continueLifeCycle() {
    int start = 0;
    IPhase[] phaseOrder = getPhaseOrder();
    if( phaseOrder != null ) {
      Integer currentPhase = getCurrentPhase();
      if( currentPhase != null ) {
        int phaseIndex = currentPhase.intValue();
        // A non-null currentPhase indicates that an IInterruptible phase
        // was executed before. In this case we now need to execute the
        // AfterPhase events
        afterPhaseExecution( phaseOrder[ phaseIndex ].getPhaseID() );
        start = currentPhase.intValue() + 1;
      }
      boolean interrupted = false;
      for( int i = start; !interrupted && i < phaseOrder.length; i++ ) {
        IPhase phase = phaseOrder[ i ];
        beforePhaseExecution( phase.getPhaseID() );
        if( phase instanceof IInterruptible ) {
          // IInterruptible phases return control to the user code, thus
          // they don't call Phase#execute()
          IServiceStateInfo stateInfo = ContextProvider.getStateInfo();
          stateInfo.setAttribute( CURRENT_PHASE, new Integer( i ) );
          interrupted = true;
        } else {
          try {
            phase.execute();
          } catch( Throwable e ) {
            // Wrap exception in a ThreadDeath-derived error to break out of
            // the application call stack
            throw new PhaseExecutionError( e );
          }
          afterPhaseExecution( phase.getPhaseID() );
        }
      }
      if( !interrupted ) {
        ContextProvider.getStateInfo().setAttribute( CURRENT_PHASE, null );
      }
    }
  }


  static int createUI() {
    int result = -1;
    if( ZERO.equals( getCurrentPhase() ) ) {
      String startup = getEntryPoint();
      if( startup != null ) {
        TextSizeDetermination.readStartupProbes();
        result = EntryPointManager.createUI( startup );
      }
    }
    return result;
  }

  void executeUIThread() throws IOException {
    ServiceContext context = ContextProvider.getContext();
    ISessionStore session = ContextProvider.getSession();
    IUIThreadHolder uiThread = getUIThreadHolder();
    if( uiThread == null ) {
      uiThread = createUIThread();
      // The serviceContext MUST be set before thread.start() is called
      uiThread.setServiceContext( context );
      synchronized( uiThread.getLock() ) {
        uiThread.getThread().start();
        uiThread.switchThread();
      }
    } else {
      uiThread.setServiceContext( context );
      uiThread.switchThread();
    }
    // TODO [rh] consider moving this to UIThreadController#run
    if( !uiThread.getThread().isAlive() ) {
      session.setAttribute( UI_THREAD, null );
    }
    handleUIThreadException();
  }

  private static void handleUIThreadException() throws IOException {
    IServiceStateInfo stateInfo = ContextProvider.getStateInfo();
    Throwable throwable
      = ( Throwable )stateInfo.getAttribute( UI_THREAD_THROWABLE );
    if( throwable != null ) {
      if( throwable instanceof PhaseExecutionError ) {
        throwable = throwable.getCause();
      }
      if( throwable instanceof IOException ) {
        throw ( IOException )throwable;
      } else if( throwable instanceof RuntimeException ) {
        throw ( RuntimeException )throwable;
      } else if( throwable instanceof Error ) {
        throw ( Error )throwable;
      } else {
        throw new RuntimeException( throwable );
      }
    }
  }

  public void sleep() {
    continueLifeCycle();
    IUIThreadHolder uiThread = getUIThreadHolder();
    UICallBackManager.getInstance().notifyUIThreadEnd();
    uiThread.switchThread();
    uiThread.updateServiceContext();
    UICallBackManager.getInstance().notifyUIThreadStart();
    continueLifeCycle();
  }

  private IUIThreadHolder createUIThread() {
    ISessionStore session = ContextProvider.getSession();
    IUIThreadHolder result = new UIThread( uiRunnable );
    result.getThread().setDaemon( true );
    result.getThread().setName( "UIThread [" + session.getId() + "]" );
    session.setAttribute( UI_THREAD, result );
    setShutdownAdapter( ( ISessionShutdownAdapter )result );
    return result;
  }

  private static void switchThread() {
    ISessionStore session = ContextProvider.getSession();
    IUIThreadHolder uiThreadHolder
      = ( IUIThreadHolder )session.getAttribute( UI_THREAD );
    uiThreadHolder.switchThread();
  }
  
  private static Integer getCurrentPhase() {
    IServiceStateInfo stateInfo = ContextProvider.getStateInfo();
    return ( Integer )stateInfo.getAttribute( CURRENT_PHASE );
  }

  private static String getEntryPoint() {
    String result = null;
    HttpServletRequest request = ContextProvider.getRequest();
    String startup = request.getParameter( RequestParams.STARTUP );
    if( startup != null ) {
      result = startup;
    } else if( getSessionDisplay() == null ) {
      result = EntryPointManager.DEFAULT;
    }
    return result;
  }

  private static void setShutdownAdapter(
    final ISessionShutdownAdapter adapter )
  {
    ISessionStore sessionStore = ContextProvider.getSession();
    SessionStoreImpl sessionStoreImpl = ( SessionStoreImpl )sessionStore;
    sessionStoreImpl.setShutdownAdapter( adapter );
  }

  private void beforePhaseExecution( final PhaseId current ) {
    PhaseListener[] phaseListeners = getPhaseListeners();
    PhaseEvent evt = new PhaseEvent( this, current );
    for( int i = 0; i < phaseListeners.length; i++ ) {
      PhaseId listenerId = phaseListeners[ i ].getPhaseId();
      if( mustNotify( current, listenerId ) ) {
        try {
          phaseListeners[ i ].beforePhase( evt );
        } catch( final Throwable thr ) {
          String text
            = "Could not execute PhaseListener before phase ''{0}''.";
          String msg = MessageFormat.format( text, new Object[] { current } );
          ServletLog.log( msg, thr );
        }
      }
    }
  }

  void afterPhaseExecution( final PhaseId current ) {
    PhaseListener[] phaseListeners = getPhaseListeners();
    PhaseEvent evt = new PhaseEvent( this, current );
    for( int i = 0; i < phaseListeners.length; i++ ) {
      PhaseId listenerId = phaseListeners[ i ].getPhaseId();
      if( mustNotify( current, listenerId ) ) {
        try {
          phaseListeners[ i ].afterPhase( evt );
        } catch( final Throwable thr ) {
          String text
            = "Could not execute PhaseListener after phase ''{0}''.";
          String msg = MessageFormat.format( text, new Object[] { current } );
          ServletLog.log( msg, thr );
        }
      }
    }
    if( current == PhaseId.PROCESS_ACTION ) {
      doRedrawFake();
    }
  }

  private static void initialize() {
    ISessionStore session = ContextProvider.getSession();
    if( session.getAttribute( INITIALIZED ) == null ) {
      AdapterFactoryRegistry.register();
      session.setAttribute( INITIALIZED, Boolean.TRUE );
    }
  }

  private static boolean mustNotify( final PhaseId currentId,
                                     final PhaseId listenerId )
  {
    return    listenerId == PhaseId.ANY
           || listenerId == PHASES[ currentId.getOrdinal() - 1 ].getPhaseID();
  }

  private PhaseListener[] getPhaseListeners() {
    synchronized( listeners ) {
      PhaseListener[] result = new PhaseListener[ listeners.size() ];
      listeners.toArray( result );
      return result;
    }
  }

  public static void fakeRedraw( final Control control,
                                 final boolean redraw )
  {
    IServiceStateInfo stateInfo = ContextProvider.getStateInfo();
    Set set = ( Set )stateInfo.getAttribute( ATTR_REDRAW_CONTROLS );
    if( set == null ) {
      set = new HashSet();
      stateInfo.setAttribute( ATTR_REDRAW_CONTROLS, set );
    }
    if( redraw ) {
      set.add( control );
    } else {
      set.remove( control );
    }
  }
  
  public static boolean needsFakeRedraw( final Control control ) {
    IServiceStateInfo stateInfo = ContextProvider.getStateInfo();
    Set set = ( Set )stateInfo.getAttribute( ATTR_REDRAW_CONTROLS );
    return set != null && set.contains( control );
  }

  private static void doRedrawFake() {
    IServiceStateInfo stateInfo = ContextProvider.getStateInfo();
    Set set = ( Set )stateInfo.getAttribute( ATTR_REDRAW_CONTROLS );
    if( set != null ) {
      Object[] controls = set.toArray();
      for( int i = 0; i < controls.length; i++ ) {
        Control control = ( Control )controls[ i ];
        WidgetUtil.getLCA( control ).doRedrawFake( control );
      }
    }
  }

  public void setPhaseOrder( final IPhase[] phaseOrder ) {
    IServiceStateInfo stateInfo = ContextProvider.getContext().getStateInfo();
    stateInfo.setAttribute( PHASE_ORDER, phaseOrder );
  }

  IPhase[] getPhaseOrder() {
    IServiceStateInfo stateInfo = ContextProvider.getContext().getStateInfo();
    return ( IPhase[] )stateInfo.getAttribute( PHASE_ORDER );
  }

  public static IUIThreadHolder getUIThreadHolder() {
    ISessionStore session = ContextProvider.getSession();
    return ( IUIThreadHolder )session.getAttribute( UI_THREAD );
  }

  public static void setSessionDisplay( final Display display ) {
    ContextProvider.getSession().setAttribute( ATTR_SESSION_DISPLAY, display );
  }

  public static Display getSessionDisplay() {
    Display result = null;
    if( ContextProvider.hasContext() ) {
      ISessionStore sessionStore = ContextProvider.getSession();
      result = ( Display )sessionStore.getAttribute( ATTR_SESSION_DISPLAY );
    }
    return result;
  }
}