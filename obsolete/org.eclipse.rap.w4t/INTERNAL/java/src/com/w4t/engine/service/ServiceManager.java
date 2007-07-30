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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;
import com.w4t.IResourceManager;
import com.w4t.engine.requests.RequestParams;
import com.w4t.engine.util.ResourceManager;


/** <p>provides the appropriate HttpServlet request service handler for the
 *  given runtime mode.</p> 
 */
// TODO [rh] Could implement resource/timestamp request handler as regular
//      IServiceHandler
// TODO [rh] access to customHandlers Map is unsynchronized and may cause
//      trouble in case of unproper use
public class ServiceManager {
  
  private static final String SERVICEHANDLER_XML = "servicehandler.xml";
  private static IServiceHandler lifeCycleRequestHandler;
  private static IServiceHandler resourceRequestHandler;
  private static IServiceHandler triggerFormHandler;
  private static HandlerDispatcher handlerDispatcher;
  private static Map customHandlers = new HashMap();
  
  static {
    try {
      IResourceManager manager = ResourceManager.getInstance();
      if( manager != null ) {
        Enumeration resources = manager.getResources( SERVICEHANDLER_XML );
        while( resources != null && resources.hasMoreElements() ) {
          URL url = ( URL )resources.nextElement();
          DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
          DocumentBuilder builder = factory.newDocumentBuilder();
          URLConnection con = url.openConnection();
          con.setUseCaches( false );
          Document document;
          InputStream is = con.getInputStream();
          try {
            document = builder.parse( is );
          } finally {
            is.close();
          }
          NodeList handlers = document.getElementsByTagName( "handler" );
          int count = handlers.getLength();
          for( int i = 0; i < count; i++ ) {
            Node handler = handlers.item( i );
            NamedNodeMap attributes = handler.getAttributes();
            String name = attributes.getNamedItem( "class" ).getNodeValue();
            String param = "requestparameter";
            String id = attributes.getNamedItem( param ).getNodeValue();
            Class clazz = ServiceManager.class.getClassLoader().loadClass( name );
            Object handlerInstance = clazz.newInstance();
            registerServiceHandler( id, ( IServiceHandler )handlerInstance );
          }
        }
      }
    } catch( final Throwable thr ) {
      System.err.println( "Could not load custom service handlers." );
      thr.printStackTrace();
    }
  }
  
  public static void registerServiceHandler( final String id, 
                                             final IServiceHandler handler ) 
  {
    customHandlers.put( id, handler );
  }

  public static void unregisterServiceHandler( final String id ) {
    customHandlers.remove( id );
  }
  
  private static final class HandlerDispatcher implements IServiceHandler {
    
    public void service() throws ServletException, IOException {
      if( isCustomHandler() ) {
        IServiceHandler customHandler
          = ( IServiceHandler )customHandlers.get( getCustomHandlerId() );
        customHandler.service();
      } else if( isResourceRequest() ) {
        getResourceRequestHandler().service();
      } else if( isTimeStampTrigger() ) {
        getTriggerFormRequestHandler().service();
      } else {
        getLifeCycleRequestHandler().service();
      }
    }
  }
  
  
  /** <p>returns the appropriate service handler.</p> */
  public static IServiceHandler getHandler() {
    if( handlerDispatcher == null ) {
      handlerDispatcher = new HandlerDispatcher();
    }
    return handlerDispatcher;
  }
  
  static boolean isTimeStampTrigger() {
    HttpServletRequest request = ContextProvider.getRequest();
    return request.getParameter( RequestParams.REQUEST_TIMESTAMP_NAME ) != null;
  }
  
  static boolean isResourceRequest() {
    HttpServletRequest request = ContextProvider.getRequest();
    String resource = request.getParameter( RequestParams.RESOURCE );
    return resource != null && !resource.equals( "" );
  }
    
  
  //////////////////
  // helping methods
  
  private static boolean isCustomHandler() {
    return customHandlers.containsKey( getCustomHandlerId() );
  }

  private static String getCustomHandlerId() {
    HttpServletRequest request = ContextProvider.getRequest();
    return request.getParameter( IServiceHandler.REQUEST_PARAM );
  }
  
  private static IServiceHandler getLifeCycleRequestHandler() {
    if( lifeCycleRequestHandler == null ) {
      lifeCycleRequestHandler = new LifeCycleServiceHandler();
    }
    return lifeCycleRequestHandler;
  }
  
  private synchronized static IServiceHandler getResourceRequestHandler() {
    if( resourceRequestHandler == null ) {
      resourceRequestHandler = new ResourceRequestServiceHandler();
    }
    return resourceRequestHandler;
  }
  
  private synchronized static IServiceHandler getTriggerFormRequestHandler() {
    if( triggerFormHandler == null ) {
      triggerFormHandler = new TimestampRequestServiceHandler();
    }
    return triggerFormHandler;
  }
}