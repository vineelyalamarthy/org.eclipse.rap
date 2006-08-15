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
package com.w4t.engine;

import java.util.Enumeration;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;
import com.w4t.W4TContext;
import com.w4t.WebComponentStatistics;
import com.w4t.engine.classloader.HttpSessionImpl;
import com.w4t.engine.lifecycle.LifeCycle;
import com.w4t.engine.requests.RequestCancelledException;
import com.w4t.engine.service.ContextProvider;
import com.w4t.internal.adaptable.IServiceAdapter;
import com.w4t.util.WebComponentRegistry;

/** 
 * <p>This class is not intended to be used by clients.</p>
 */
// NOTE: [fappel] This needs to be public as long as the classloader
//                architecture is still alive...
public class W4TModelCore extends W4TModel {

  /**
   * Identifier that is used to add the <code>W4TModelCore</code>
   * to the session as attribute.
   */
  private final static String ID_W4T_MODEL_CORE = ID_W4T_MODEL + "_core";
  /** 
   * Keep the session for thread processing between requests.  
   */
  private IServiceAdapter serviceAdapter;
  private WebComponentRegistry componentRegistry;
  private HttpSession session;

  private final class ServiceAdapter implements IServiceAdapter {
    public void execute() throws RequestCancelledException, ServletException {
      prepareSession();
      prepareRegistry();
      LifeCycle lifeCycle = ( LifeCycle )W4TContext.getLifeCycle();
      lifeCycle.execute();   
    }
  }
  
  // NOTE: [fappel] This needs to be public as long as the classloader
  //                architecture is still alive...
  public W4TModelCore()  {
    ContextProvider.getSession().setAttribute( ID_W4T_MODEL_CORE, this );
  }
  
  public static W4TModelCore getInstance() {
    String id = ID_W4T_MODEL_CORE;
    return ( W4TModelCore )ContextProvider.getSession().getAttribute( id );
  }

  protected void init() {
    // Do not remove this because it is implicitly called by W4TModel 
    // constructor and would cause an endless recursion - see W4TModel#init()
  } 
  
  /** returns a WebComponentStatistics with information about the components,
    * uptime etc. of the session. */
  public WebComponentStatistics getStatistics() {
    WebComponentStatistics result = null;
    if( componentRegistry != null ) {
      result = componentRegistry.getStatistics();
    }
    return result;
  }
  
  /** free the expired WebComponents, and throw the closing
   *  event on closed WebForms. */  
  public void cleanup() {
    if( componentRegistry != null ) {
      componentRegistry.cleanup();
    }
  }

  
  // interface methods
  ////////////////////

  public Object getAdapter( final Class adapterClass ) {
    if( IServiceAdapter.class == adapterClass ) {
      serviceAdapter = new ServiceAdapter();
    }
    return serviceAdapter;
  }
  
  // helping methods
  //////////////////
  
  private void prepareSession() {
    HttpSession contextSession = ContextProvider.getSession();
    if( this.session == null ) {
      this.session = contextSession;
    } else {
      if( this.session instanceof HttpSessionImpl ) {
        Object wrapper = contextSession.getAttribute( W4T_SESSION_WRAPPER );
        if( wrapper == null ) {
          contextSession.setAttribute( W4T_SESSION_WRAPPER, this.session );
        }
        Enumeration attributeNames = this.session.getAttributeNames();
        while( attributeNames.hasMoreElements() ) {
          String key = ( String )attributeNames.nextElement();
          contextSession.setAttribute( key, this.session.getAttribute( key ) );
        }
      }
    }
  }
  
  private void prepareRegistry() {
    componentRegistry = WebComponentRegistry.getInstance();
  }  
}