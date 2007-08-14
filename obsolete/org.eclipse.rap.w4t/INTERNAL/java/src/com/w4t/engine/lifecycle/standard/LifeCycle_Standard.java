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
package com.w4t.engine.lifecycle.standard;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletException;

import org.eclipse.rwt.Adaptable;
import org.eclipse.rwt.internal.AdapterManager;
import org.eclipse.rwt.internal.lifecycle.LifeCycle;
import org.eclipse.rwt.internal.lifecycle.Scope;
import org.eclipse.rwt.internal.util.ParamCheck;
import org.eclipse.rwt.lifecycle.*;

import com.w4t.*;
import com.w4t.ajax.AjaxStatusAdapterFactory;
import com.w4t.internal.adaptable.RenderInfoAdapterFactory;


/** <p>A lifecycle implementation for the standard W4Toolkit lifecycle of 
  * a request.</p>
  */
public class LifeCycle_Standard extends LifeCycle {

  static final String PACKAGE = "org.eclipse.rap.engine.lifecycle.standard.";  
  
  private final Phase[] phases;
  private final Set listeners;
  
  
  public LifeCycle_Standard() {
    phases = new Phase[] {
      new AccessForm(),
      new ReadData(),
      new ProcessAction(),
      new Render()
    };
    listeners = new HashSet();
    AdapterManager adapterManager = W4TContext.getAdapterManager();
    adapterManager.registerAdapters( new LifeCycleAdapterFactory(), 
                                     WebForm.class );
    adapterManager.registerAdapters( new AjaxStatusAdapterFactory(),
                                     WebComponent.class );
    adapterManager.registerAdapters( new RenderInfoAdapterFactory(),
                                     Adaptable.class );
  }

  public Scope getScope() {
    return Scope.SESSION;
  }
  
  public void execute() throws ServletException {
    Thread.currentThread().setContextClassLoader( getClass().getClassLoader() );
    PhaseId current = PhaseId.PREPARE_UI_ROOT;
    while( current != null ) {
      PhaseId next;
      try {
        beforePhaseExecution( current );
        next = getPhase( current ).execute();
      } finally {
        afterPhaseExecution( current );        
      }
      current = next;
    }
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
  
  
  //////////////////
  // helping methods
  

  private void afterPhaseExecution( final PhaseId current ) {
    PhaseListener[] phaseListeners = getPhaseListeners();
    PhaseEvent evt = new PhaseEvent( this, current );
    for( int i = 0; i < phaseListeners.length; i++ ) {
      PhaseId listenerId = phaseListeners[ i ].getPhaseId();
      if(    listenerId == PhaseId.ANY 
          || listenerId == getPhase( current ).getPhaseID() ) 
      {
        // TODO: [fappel] exception handling?
        phaseListeners[ i ].afterPhase( evt );
      }
    }
  }

  private void beforePhaseExecution( final PhaseId current ) {
    PhaseListener[] phaseListeners = getPhaseListeners();
    PhaseEvent evt = new PhaseEvent( this, current );
    for( int i = 0; i < phaseListeners.length; i++ ) {
      PhaseId listenerId = phaseListeners[ i ].getPhaseId();
      if(    listenerId == PhaseId.ANY 
          || listenerId == getPhase( current ).getPhaseID() ) 
      {
        // TODO: [fappel] exception handling?
        phaseListeners[ i ].beforePhase( evt );
      }
    }
  }

  private Phase getPhase( final PhaseId current ) {
    return phases[ current.getOrdinal() - 1 ];
  }

  private PhaseListener[] getPhaseListeners() {
    PhaseListener[] result;
    synchronized( listeners ) {
      result = new PhaseListener[ listeners.size() ];
      listeners.toArray( result );
    }
    return result;
  }
}