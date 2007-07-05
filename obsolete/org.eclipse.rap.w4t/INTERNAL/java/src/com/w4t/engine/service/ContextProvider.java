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

import javax.servlet.http.*;

import com.w4t.ParamCheck;


/** 
 * <p>This enables application wide access to the context of the
 * currently processed request during the service handler execution.</p>
 */ 
public class ContextProvider {
  
  private final static ThreadLocal CONTEXT_HOLDER = new ThreadLocal();

  /** 
   * Sets the {@link ServiceContext} associated with the currently
   * processed request.  
   */
  public static void setContext( final ServiceContext context ) {
    ParamCheck.notNull( context, "context" );
    if( getContextInternal() != null ) {
      String msg = "Current thread has already buffered a context instance.";
      throw new IllegalStateException( msg );
    }
    CONTEXT_HOLDER.set( context );
  }

  /** 
   * Returns the {@link ServiceContext} associated with the currently
   * processed request.  
   */
  public static ServiceContext getContext() {
    ServiceContext result = getContextInternal();
    if( result == null ) {
      String msg =   "No context available outside of the request "
                   + "service lifecycle.";
      throw new IllegalStateException( msg );
    }
    return result;
  }
  
  /**
   * Returns the <code>HttpServletRequest</code> that is currently
   * processed. This is a convenience method that delegates to
   * <code>ContextProvider.getContext().getRequest()</code>;  
   */
  public static HttpServletRequest getRequest() {
    return getContext().getRequest();
  }
  
  /**
   * Returns the <code>HttpServletResponse</code> that is associated
   * with the currently processed request. This is a convenience method
   * that delegates to <code>ContextProvider.getContext().getResponse()</code>;  
   */
  public static HttpServletResponse getResponse() {
    return getContext().getResponse();
  }

  /**
   * Returns the <code>ISessionStore</code> of the <code>HttpSession</code>
   * to which the currently processed request belongs.
   */
  public static ISessionStore getSession() {
    ISessionStore result = getContext().getSessionStore();
    if( result == null ) {
      HttpSession httpSession = getRequest().getSession();
      String id = SessionStoreImpl.ID_SESSION_STORE;
      result = ( ISessionStore )httpSession.getAttribute( id );
      if( result == null ) { 
        result = new SessionStoreImpl( httpSession );
      }
      getContext().setSessionStore( result );
    }
    return result;
  }

  /**
   * Returns the {@link IServiceStateInfo} that is associated
   * with the currently processed request. This is a convenience method 
   * that delegates to <code>ContextProvider.getContext().getStateInfo()</code>;  
   */
  public static IServiceStateInfo getStateInfo() {
    return getContext().getStateInfo();
  }

  /** 
   * Releases the currently buffered context instance. Note that this is
   * automatically called by the library to end the context's lifecycle.
   * A premature call will cause failure of the currently processed
   * request lifecycle.
   */
  public static void disposeContext() {
    ServiceContext context = getContextInternal();
    if( context != null ) {
      context.dispose();
      CONTEXT_HOLDER.set( null );
    }
  }
 
  /**
   * Returns whether the current thread has an associated service context.
   */
  public static boolean hasContext() {
    return CONTEXT_HOLDER.get() != null;    
  }
  
  
  //////////////////
  // helping methods

  private static ServiceContext getContextInternal() {
    return ( ServiceContext )CONTEXT_HOLDER.get();
  }
}
