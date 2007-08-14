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

import java.io.IOException;

import javax.servlet.ServletException;

import org.eclipse.rwt.Adaptable;
import org.eclipse.rwt.SessionSingletonBase;
import org.eclipse.rwt.internal.lifecycle.LifeCycle;
import org.eclipse.rwt.internal.service.IServiceAdapter;

import com.w4t.W4TContext;
import com.w4t.WebComponentStatistics;
import com.w4t.util.WebComponentRegistry;


/** <p>The model of the Delegate-Model architecture of the W4T platform.</p>
  * <p>The model of the W4T plattform loads the WebForm classes,
  * which are requested. It feeds the components with the values actually
  * passed in the request, processes events, and provides the Delegate with
  * the newly created contents of the page.</p>
  */
public class W4TModel extends SessionSingletonBase implements Adaptable {

  /** 
   * Identifier used in preload mode for identification of the 
   * preloaded session wrapper instance which is added to the
   * real session for session event notification reasons.
   */
  public final static String W4T_SESSION_WRAPPER = "W4T_Session_Wrapper";
  
  private WebComponentRegistry componentRegistry;
  private IServiceAdapter serviceAdapter;
  
  private final class ServiceAdapter implements IServiceAdapter {
    public void execute() throws ServletException, IOException {
      prepareRegistry();
      LifeCycle lifeCycle = ( LifeCycle )W4TContext.getLifeCycle();
      lifeCycle.execute();   
    }
  }
  

  private W4TModel() {
    // prevent instance creation
  }
  
  
  public static W4TModel getInstance() {
    return ( W4TModel )getInstance( W4TModel.class );
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

  public Object getAdapter( final Class adapter ) {
    if( IServiceAdapter.class == adapter ) {
      serviceAdapter = new ServiceAdapter();
    }
    return serviceAdapter;
  }
  
  
  //////////////////
  // helping methods
  
  private void prepareRegistry() {
    componentRegistry = WebComponentRegistry.getInstance();
  }
}