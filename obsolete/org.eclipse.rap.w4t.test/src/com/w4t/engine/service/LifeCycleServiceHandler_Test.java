/*******************************************************************************
 * Copyright (c) 2002-2006 Innoopract Informationssysteme GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Innoopract Informationssysteme GmbH - initial API and implementation
 ******************************************************************************/
package com.w4t.engine.service;

import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpSession;

import junit.framework.TestCase;

import com.w4t.*;
import com.w4t.Fixture.*;
import com.w4t.IWindowManager.IWindow;
import com.w4t.engine.lifecycle.*;
import com.w4t.engine.requests.RequestParams;
import com.w4t.engine.util.WindowManager;
import com.w4t.internal.adaptable.IFormAdapter;
import com.w4t.util.browser.Default;
import com.w4t.util.browser.Ie6;


public class LifeCycleServiceHandler_Test extends TestCase {
  
  private final class ThreadController implements PhaseListener {

    private final List order;
    private final Thread worker1;
    private final Thread worker2;

    private static final long serialVersionUID = 1L;

    private ThreadController( final List order, 
                              final Thread worker1, 
                              final Thread worker2 )
    {
      this.order = order;
      this.worker1 = worker1;
      this.worker2 = worker2;
    }

    public void beforePhase( final PhaseEvent event ) {
      Thread currentThread = Thread.currentThread();
      if( currentThread == worker1 ) {
        try {
          worker2.start();
          Thread.sleep( 250 );
        } catch( final InterruptedException ex ) {
          throw new RuntimeException( ex );
        }    
      }
    }

    public void afterPhase( final PhaseEvent event ) {
      order.add( Thread.currentThread() );
    }

    public PhaseId getPhaseId() {
      return PhaseId.RENDER;
    }
  }

  private class Worker implements Runnable {
    
    private ServiceContext context;
    private Throwable throwable;
    private final WebForm form;
    
    private Worker( final HttpSession session, final WebForm form ) {
      this.form = form;
      TestRequest request = new TestRequest();
      request.setSession( session );
      TestResponse response = new TestResponse();
      response.setOutputStream( new TestServletOutputStream() );
      context = new ServiceContext( request, response );
      context.setStateInfo( new ServiceStateInfo() );
    }
    
    public void run() {
      try {
        prepareRequest();      
        ServiceManager.getHandler().service();
      } catch( final Throwable thr ) {
        this.throwable = thr;
      }
    }

    private void prepareRequest() {
      ContextProvider.setContext( context );
      Fixture.fakeRequestParam( RequestParams.STARTUP, null );

      if( W4TContext.getBrowser().isAjaxEnabled() ) {
        Fixture.fakeRequestParam( RequestParams.IS_AJAX_REQUEST, "true" );
      } else {
        Fixture.fakeRequestParam( RequestParams.IS_AJAX_REQUEST, "false" );
      } 
      
      IWindow window = WindowManager.getInstance().findWindow( form );
      String formId = form.getUniqueID();
      IFormAdapter adapter = Fixture.getFormAdapter( form );
      int requestCounter = adapter.getRequestCounter();
      String reqCounterValue = String.valueOf( requestCounter - 1 );
      Fixture.fakeFormRequestParams( reqCounterValue, window.getId(), formId );
    }
  }
  
  protected void setUp() throws Exception {
    Fixture.setUp();
    Fixture.createContext( false );
  }
  
  protected void tearDown() throws Exception {
    Fixture.tearDown();
    Fixture.removeContext();
  }
  
  public void testSameFormSynchLock() throws Exception {
    WebForm form = newForm();
    HttpSession session = ContextProvider.getRequest().getSession();
    Fixture.fakeBrowser( new Ie6( true, true ) );
    
    Worker worker1 = new Worker( session, form );
    final Thread thread1 = newThread( worker1, "worker1" );
    Worker worker2 = new Worker( session, form );
    final Thread thread2 = newThread( worker2, "worker2" );
    final List executionOrder = new Vector();
    
    PhaseListener listener 
      = new ThreadController( executionOrder, thread1, thread2 );
    W4TContext.getLifeCycle().addPhaseListener( listener );
    
    thread1.start();
    thread1.join();
    thread2.join();

    assertNotNull( worker1.context );
    assertNull( worker1.throwable );
    assertNotNull( worker2.context );
    assertNull( worker2.throwable );
    
    assertFalse( getStateInfo( worker1 ).isExpired() );
    assertTrue( getStateInfo( worker2 ).isExpired() );
    assertEquals( 2, executionOrder.size() );
    assertSame( executionOrder.get( 0 ), thread1 );
    assertSame( executionOrder.get( 1 ), thread2 );
  }
  
  public void testDifferentFormsSynchLock() throws Exception {
    WebForm form1 = newForm();
    WebForm form2 = newForm();
    HttpSession session = ContextProvider.getRequest().getSession();
    Fixture.fakeBrowser( new Ie6( true, true ) );
    
    Worker worker1 = new Worker( session, form1 );
    final Thread thread1 = newThread( worker1, "worker1" );
    Worker worker2 = new Worker( session, form2 );
    final Thread thread2 = newThread( worker2, "worker2" );
    final List executionOrder = new Vector();
    
    PhaseListener listener 
      = new ThreadController( executionOrder, thread1, thread2 );
    W4TContext.getLifeCycle().addPhaseListener( listener );
    
    thread1.start();
    thread1.join();
    thread2.join();
    
    assertNotNull( worker1.context );
    assertNull( worker1.throwable );
    assertNotNull( worker2.context );
    assertNull( worker2.throwable );
    
    assertFalse( getStateInfo( worker1 ).isExpired() );
    assertFalse( getStateInfo( worker2 ).isExpired() );
    assertEquals( 2, executionOrder.size() );
    assertSame( executionOrder.get( 0 ), thread1 );
    assertSame( executionOrder.get( 1 ), thread2 );
  }
  
  /**
   * <p>Simulates a parameterless request within an existing session.</p>
   * <p>Expected behaviour is to reset the session and as a consequence thereof
   * render the internal browser survey.</p>
   */
  public void testEmptyRequest() throws Exception {
    newForm();
    ISessionStore session = ContextProvider.getSession();
    session.setAttribute( "test-attribute", "test-attribute-value" );
    TestResponse response = ( TestResponse )ContextProvider.getResponse();
    response.setOutputStream( new TestServletOutputStream() );
    
    ServiceManager.getHandler().service();
    HttpSession httpSession = ContextProvider.getRequest().getSession();
    httpSession.setAttribute( SessionStoreImpl.ID_SESSION_STORE, session );
    assertEquals( null, session.getAttribute( "test-attribute" ) );
    String markup = Fixture.getAllMarkup();
    assertTrue( markup.indexOf( "Startup Page" ) > -1 );
  }
  
  public void testStartupRequestInExistingSession() throws Exception {
    newForm();
    Fixture.fakeBrowser( new Default( true, true ) );
    ISessionStore session = ContextProvider.getSession();
    session.setAttribute( "test-attribute", "test-attribute-value" );
    TestResponse response = ( TestResponse )ContextProvider.getResponse();
    response.setOutputStream( new TestServletOutputStream() );
    
    ServiceManager.getHandler().service();
    HttpSession httpSession = ContextProvider.getRequest().getSession();
    httpSession.setAttribute( SessionStoreImpl.ID_SESSION_STORE, session );
    assertEquals( null, session.getAttribute( "test-attribute" ) );
    String markup = Fixture.getAllMarkup();
    assertTrue( markup.indexOf( "Startup Page" ) > -1 );
  }

  private static IServiceStateInfo getStateInfo( final Worker worker ) {
    return worker.context.getStateInfo();
  }
  
  private static WebForm newForm() {
    WebForm result = Fixture.loadStartupForm();
    WindowManager.getInstance().create( result );
    return result;
  }

  private static Thread newThread( final Worker worker, final String name ) {
    Thread result = new Thread( worker, name );
    result.setDaemon( true );
    return result;
  }
}
