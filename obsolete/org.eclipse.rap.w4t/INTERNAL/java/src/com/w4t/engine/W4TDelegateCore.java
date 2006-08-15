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

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import org.apache.commons.fileupload.FileUpload;
import com.w4t.engine.classloader.GlobalClasses;
import com.w4t.engine.requests.FileUploadRequest;
import com.w4t.engine.service.*;
import com.w4t.engine.util.IEngineConfig;
import com.w4t.util.*;


/** This class implements the core functionality of the W4TDelegate servlet.
 *  The W4TDelegateCore class is loaded by its own class loader to get an
 *  abstraction layer between the namespace of the servlet engines
 *  webapplication classloader and the namespaces of the sessions content
 *  classes which are also loaded by their own classloader per session.
 */
public class W4TDelegateCore extends HttpServlet {
  
  private static final long serialVersionUID = 1L;
  
  /** referrence to the global cleanup thread */
  private RegistrySkimmer registrySkimmer;

  
  public void init( final ServletConfig config ) throws ServletException {
    super.init( config );
    try {
      loadGlobalClasses();      
      createResourceManagerInstance();
      createImageCacheInstance();
    } catch( Exception e ) {
      throw new ServletException( e.toString() );
    }
    startRegistrySkimmer();
  }  
  
  private void startRegistrySkimmer() {
    registrySkimmer = new RegistrySkimmer();
    registrySkimmer.setDaemon( true );
    registrySkimmer.start();    
  }    
  
  /** <p>do the cleanup.</p> */
  public void destroy() {
    RegistrySkimmer.shutdown();
  }
 
  /**
    * Process the delegated HTTP request of the W4TDelegate servlet
    * @param request encapsulates the information of a http request
    * from a client browser
    * @param response encapsulates the information of a http response
    * to a client browser
    * @throw IOException error accessing the response output stream
    * @throw ServletException
    */
  public void service( final HttpServletRequest request,
                       final HttpServletResponse response ) 
    throws ServletException, IOException 
  {
    request.setCharacterEncoding( "UTF-8" );
    HttpServletRequest wrappedRequest = getWrappedRequest( request );
    try {
      ServiceContext context = new ServiceContext( wrappedRequest, response );
      ContextProvider.setContext( context );
      ServiceManager.getHandler().service( );
    } finally {
      ContextProvider.disposeContext();
    }                                                         
  }
  
  
  // helping methods
  //////////////////
  
  private void loadGlobalClasses() throws Exception {
    String fileName = getInitializationProps().getGlobalClassesList();
    new GlobalClasses( fileName ).preload();
  }

  private IInitialization getInitializationProps() {
    IConfiguration configuration = getConfiguration();
    return configuration.getInitialization();
  }

  private IConfiguration getConfiguration() {
    return ConfigurationReader.getConfiguration();
  }
  
  private void createResourceManagerInstance() throws Exception {
    String resources = getInitializationProps().getResources();
    String resourceManager = "com.w4t.engine.util.ResourceManager";
    createSingletonInstance( resourceManager, resources );
  }

  private void createImageCacheInstance() throws Exception {
    String nsSubmitters = getInitializationProps().getNoscriptSubmitters();
    String imageCache = "com.w4t.util.image.ImageCache";
    createSingletonInstance( imageCache, nsSubmitters );
  }
  
  private void createSingletonInstance( final String className, 
                                        final String param ) throws Exception {
    Class cacheClass = Class.forName( className );
    Class[] paramTypes = new Class[]{ String.class, String.class };
    Method creator = cacheClass.getMethod( "createInstance", paramTypes );
    IEngineConfig engineConfig = ConfigurationReader.getEngineConfig();
    File webAppBase = engineConfig.getServerContextDir();
    Object[] params = new Object[]{ webAppBase.toString(), param };
    creator.invoke( null, params );    
  }
  
  /** This method detects if the request is multipart or not. In both cases it
   *  returns a request transparent to the developer. This means you can use the
   *  same getters and setter for different types of requests. */
  private HttpServletRequest getWrappedRequest( final HttpServletRequest req ) 
    throws ServletException 
  {
    HttpServletRequest result = req;
    if( FileUpload.isMultipartContent( req ) ) {
      result = new FileUploadRequest( req );
    }
    return result;
  }
}