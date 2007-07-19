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
import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.commons.fileupload.FileUpload;
import com.w4t.engine.requests.FileUploadRequest;
import com.w4t.engine.service.*;
import com.w4t.engine.util.*;
import com.w4t.util.ConfigurationReader;
import com.w4t.util.IInitialization;
import com.w4t.util.image.ImageCache;


/** <p>The Delegate servlet of the W4T plattform handles HTTP requests and 
  * provides the W4TModel with the corresponding HTTP session.</p>
  *
  * <p>It uses a reference to the W4TModel, which is placed in the session,
  * to create the page content. The W4TModel is initialized by the 
  * Delegate.</p>
  */
public class W4TDelegate extends HttpServlet {

  private static final long serialVersionUID = 1L;
  private RegistrySkimmer registrySkimmer;

  /** Initialize global variables */
  public void init( final ServletConfig config ) throws ServletException {  
    super.init( config );
    IEngineConfig engineConfig = getEngineConfig();
    try {
      ConfigurationReader.setEngineConfig( engineConfig );
      createResourceManagerInstance();
      createImageCacheInstance();
    } catch( Exception e ) {
      throw new ServletException( e );
    }
    startRegistrySkimmer();
  }

  /** <p>do the cleanup.</p> */
  public void destroy() {
    RegistrySkimmer.shutdown();
    super.destroy();
  }
  
  /**
    * Process the HTTP Post request
    * @param request encapsulates the information of a http request
    * from a client browser
    * @param response encapsulates the information of a http response
    * to a client browser
    * @throw IOException error accessing the response output stream
    * @throw ServletException
    */
  public void doPost( final HttpServletRequest request,
                      final HttpServletResponse response )
    throws ServletException, IOException {
    request.setCharacterEncoding( "UTF-8" );
    HttpServletRequest wrappedRequest = getWrappedRequest( request );
    try {
      ServiceContext context = new ServiceContext( wrappedRequest, response );
      ContextProvider.setContext( context );
      // TODO [fappel]: move this into a method ContextProvider#ensureSession().
      //                Ensure that there is exactly one ISessionStore per 
      //                session created
      synchronized( registrySkimmer ) {
        ContextProvider.getSession();
      }
      ServiceManager.getHandler().service( );
    } finally {
      ContextProvider.disposeContext();
    }                                                         
  }

  /**
    * Process the http get request, which is used for the first access to a
    * web application
    * @param request encapsulates the information of a http request
    * from a client browser
    * @param response encapsulates the information of a http response
    * to a client browser
    * @throw IOException error accessing the response output stream
    * @throw ServletException
    */
  public void doGet( final HttpServletRequest request,
                     final HttpServletResponse response )
    throws ServletException, IOException {
    doPost( request, response );
  }

  public String getServletInfo() {
    return "com.w4t.engine.W4TDelegate Information";
  }
  

  // helping methods
  //////////////////
  
  private IEngineConfig getEngineConfig() {
    String name = IEngineConfig.class.getName();
    ServletContext sc = getServletContext();
    IEngineConfig result = ( IEngineConfig )sc.getAttribute( name );
    if( result == null ) {
      result = new EngineConfig( sc.getRealPath( "/" ) );
      sc.setAttribute( name, result );
    }
    return result;
  }
  
  private void startRegistrySkimmer() {
    registrySkimmer = new RegistrySkimmer();
    registrySkimmer.setDaemon( true );
    registrySkimmer.start();    
  }
  
  private void createResourceManagerInstance() throws Exception {
    String resources = getInitializationProps().getResources();
    ResourceManager.createInstance( getWebAppBase().toString(), resources );
  }

  private void createImageCacheInstance() throws Exception {
    String nsSubmitters = getInitializationProps().getNoscriptSubmitters();
    ImageCache.createInstance( getWebAppBase().toString(), nsSubmitters );
  }

  private File getWebAppBase() {
    IEngineConfig engineConfig = ConfigurationReader.getEngineConfig();
    return engineConfig.getServerContextDir();
  }
  
  private IInitialization getInitializationProps() {
    return ConfigurationReader.getConfiguration().getInitialization();
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