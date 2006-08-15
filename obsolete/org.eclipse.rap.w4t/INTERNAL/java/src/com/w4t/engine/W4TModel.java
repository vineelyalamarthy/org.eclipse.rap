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

import java.net.URLClassLoader;
import javax.servlet.http.HttpServletRequest;
import com.w4t.Adaptable;
import com.w4t.WebComponentStatistics;
import com.w4t.engine.classloader.ClassLoaderCache;
import com.w4t.engine.classloader.DelegateClassLoader;
import com.w4t.util.*;


/** <p>The model of the Delegate-Model architecture of the W4T platform.</p>
  * <p>The model of the W4T plattform loads the WebForm classes,
  * which are requested. It feeds the components with the values actually
  * passed in the request, processes events, and provides the Delegate with
  * the newly created contents of the page.</p>
  */
public class W4TModel implements Adaptable {

  /** identifier of the W4TModel instance in the session object */
  public final static String ID_W4T_MODEL = "com_w4t_model";
  /** the fully qualified classname of the W4TModels core implementation */
  public final static String CORE_CLASS = "com.w4t.engine.W4TModelCore";
  /** 
   * Identifier used in preload mode for identification of the 
   * preloaded session wrapper instance which is added to the
   * real session for session event notification reasons.
   */
  public final static String W4T_SESSION_WRAPPER = "W4T_Session_Wrapper";
  
  /** the worker of this sessions model */
  private W4TModel core = null;


  /** Creates a new instance of <code>W4TModel</code>. */
  // NOTE: [fappel] This needs to be public as long as the classloader
  //                architecture is still alive...
  public W4TModel() {
    init();
  }  
   
  private boolean usePreloaderBuffer() {
    IConfiguration configuration = ConfigurationReader.getConfiguration();
    IPreloaderBuffer preloaderBuffer = configuration.getPreloaderBuffer();
    return preloaderBuffer.isUsePreloaderBuffer();
  }
  
  private W4TModel createCore() {
    ClassLoader parentLoader = getClass().getClassLoader();
    W4TModel result = null;
    if( parentLoader instanceof URLClassLoader ) {
      URLClassLoader parent = ( URLClassLoader )parentLoader;
      DelegateClassLoader loader 
        = new DelegateClassLoader( parent.getURLs(), parent, false );
      result = loadCore( loader );
    } else {
      result = loadCore( parentLoader );
    }
    return result;
  }
  
  private W4TModel loadCore( final ClassLoader loader ) {
    try {
      return ( W4TModel )loader.loadClass( CORE_CLASS ).newInstance();
    } catch( final Exception ex ) {
      throw new RuntimeException( ex );
    }
  }
  
  /** initialisations of the sessions model instance */
  protected void init() {
    if( usePreloaderBuffer() ) {
      core = ClassLoaderCache.getInstance().get();
    } else {
      core = createCore();
    }  
  }

  /** gets the javascript code for setting focus on form or a WebComponent. */
  protected String getFocus( final HttpServletRequest request ) {
    return core.getFocus( request );
  }
  
  /** returns a WebComponentStatistics with information about the components,
    * uptime etc. of the session. */
  public WebComponentStatistics getStatistics() {
    return core.getStatistics();
  }
  
  /** free the expired WebComponents, and throw the closing
   *  event on closed WebForms. */  
  public void cleanup() {
    core.cleanup();
  }

  public Object getAdapter( final Class adapter ) {
    return core.getAdapter( adapter );
  }
}