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
import java.io.PrintWriter;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.w4t.Browser;
import com.w4t.HtmlResponseWriter;
import com.w4t.engine.W4TModel;
import com.w4t.engine.W4TModelUtil;
import com.w4t.engine.requests.RequestParams;
import com.w4t.util.browser.BrowserLoader;


class LifeCycleServiceHandler extends AbstractServiceHandler {

  // TODO [rh] Should we have a separate class that contains all logger names?
  //      e.g. com.w4t.util.LogerNames?
  public static final String LOG_REQUEST_PARAMS 
    = LifeCycleServiceHandler.class.getName() + ".requestParams"; 
  public static final String LOG_REQUEST_HEADER 
    = LifeCycleServiceHandler.class.getName() + ".requestHeaders"; 
  public static final String LOG_RESPONSE_CONTENT
    = LifeCycleServiceHandler.class.getName() + ".responseContent"; 
  
  // The log level used by all loggers thoughout this class
  private static final Level LOG_LEVEL = Level.FINE;
  private static final String PARAM_BUFFER
    = "org.eclipse.rap.w4t.startupRequestParameterBuffer:-)";
  
  private static Logger requestParamsLogger 
    = Logger.getLogger( LOG_REQUEST_PARAMS );
  private static Logger requestHeaderLogger 
    = Logger.getLogger( LOG_REQUEST_HEADER );
  private static Logger responseContentLogger
    = Logger.getLogger( LOG_RESPONSE_CONTENT );
  
  public void service() throws IOException, ServletException {
    logRequestHeader();
    logRequestParams();
    synchronized( ContextProvider.getSession() ) {
      HttpSession session = ContextProvider.getSession().getHttpSession();
      // start point of process time
      long startTime = System.currentTimeMillis();
      initializeStateInfo();
      checkRequest( session );
      detectBrowser();
      if( isBrowserDetected() ) {
        W4TModelUtil.initModel();
        wrapStartupRequest( session );
        getServiceAdapter( W4TModel.getInstance() ).execute();
      } else {
        bufferStartupRequestParams( session );
        BrowserSurvey.sendBrowserSurvey();
      }
      appendProcessTime( startTime );
      writeOutput();
    }
  }

  //////////////////
  // helping methods
  
  private static boolean isBrowserDetected() {
    return getBrowser() != null;
  }

  private static Browser getBrowser() {
    String id = ServiceContext.DETECTED_SESSION_BROWSER;
    return ( Browser )ContextProvider.getSession().getAttribute( id );
  }
  
  private static void detectBrowser() {
    if( !isBrowserDetected() ) {
      if(    getRequest().getParameter( RequestParams.SCRIPT ) != null 
          && getRequest().getParameter( RequestParams.AJAX_ENABLED ) != null )
      {
        Browser browser = BrowserLoader.load();
        String id = ServiceContext.DETECTED_SESSION_BROWSER;
        ContextProvider.getSession().setAttribute( id, browser );
      }
    }
    if ( isBrowserDetected() ) {
      ContextProvider.getStateInfo().setDetectedBrowser( getBrowser() );
    }
  }
  
  private static void initializeStateInfo() {
    HtmlResponseWriter htmlResponseWriter = new HtmlResponseWriter();
    if( getStateInfo() == null ) {
      IServiceStateInfo stateInfo;
      stateInfo = new ServiceStateInfo();
      ContextProvider.getContext().setStateInfo( stateInfo );
    }
    getStateInfo().setResponseWriter( htmlResponseWriter );
  }

  private void bufferStartupRequestParams( HttpSession session ) {
    Map parameters = getRequest().getParameterMap();
    session.setAttribute( PARAM_BUFFER, new HashMap( parameters ) );
  }

  private static void wrapStartupRequest( final HttpSession session ) {
    Map params = ( Map )session.getAttribute( PARAM_BUFFER );
    if( params != null ) {
      ServiceContext context = ContextProvider.getContext();
      context.setRequest( new StartupRequest( getRequest(), params ) );
    }
    session.removeAttribute( PARAM_BUFFER );
  }

  private static void checkRequest( final HttpSession session ) {
    boolean startup 
      = getRequest().getParameter( RequestParams.STARTUP ) != null;
    String uiRoot = getRequest().getParameter( RequestParams.UIROOT );
    if(    !session.isNew() && !startup && uiRoot == null 
        || startup && isBrowserDetected() ) 
    {
      Enumeration keys = session.getAttributeNames();
      List keyBuffer = new ArrayList();
      while( keys.hasMoreElements() ) {
        keyBuffer.add( keys.nextElement() );
      }
      Object[] attributeNames = keyBuffer.toArray();
      for( int i = 0; i < attributeNames.length; i++ ) {
        session.removeAttribute( ( String )attributeNames[ i ] );
      }
    }
  }
  
  private static void appendProcessTime( final long startTime ) {
    if( getInitProps().isProcessTime() ) {
      // end point of process time
      long finish = System.currentTimeMillis();
      HtmlResponseWriter content = getStateInfo().getResponseWriter();
      content.appendFoot( getProcessTime( startTime, finish ) );
    }
  }
  
  private static StringBuffer getProcessTime( final long start, 
                                              final long finish )
  {
    long processTime = finish - start;
    StringBuffer result = new StringBuffer();
    result.append( "\nTime to process: " );
    result.append( processTime );
    result.append( " ms" );
    return result;
  }

  private static void writeOutput() throws IOException {
    HtmlResponseWriter content = getStateInfo().getResponseWriter();
    PrintWriter out = getOutputWriter();
    try {
      // send the head to the client
      for( int i = 0; i < content.getHeadSize(); i++ ) {
        out.print( content.getHeadToken( i ) );
      }
      // send the body to the client
      for( int i = 0; i < content.getBodySize(); i++ ) {
        out.print( content.getBodyToken( i ) );
      }
      // send the foot to the client
      for( int i = 0; i < content.getFootSize(); i++ ) {
        out.print( content.getFootToken( i ) );
      }
    } finally {
      out.close();
    }
    logResponseContent();
  }

  private static IServiceStateInfo getStateInfo() {
    return ContextProvider.getStateInfo();
  }

  //////////////////
  // Logging methods
  
  private static void logRequestHeader() {
    if( requestHeaderLogger.isLoggable( LOG_LEVEL ) ) {
      HttpServletRequest request = ContextProvider.getRequest();
      Enumeration headerNames = request.getHeaderNames();
      StringBuffer msg = new StringBuffer();
      msg.append( "Request header:\n" );
      msg.append( "(method):" );
      msg.append( request.getMethod() );
      while( headerNames.hasMoreElements() ) {
        String headerName = ( String )headerNames.nextElement();
        msg.append( headerName );
        msg.append( ": " );
        msg.append( request.getHeader( headerName ) );
      }
      requestHeaderLogger.log( LOG_LEVEL, msg.toString() );
    }    
  }
  
  private static void logRequestParams() {
    if( requestParamsLogger.isLoggable( LOG_LEVEL ) ) {
      StringBuffer msg = new StringBuffer();
      msg.append( "Request parameters:\n" );
      HttpServletRequest request = ContextProvider.getRequest();
      Enumeration parameterNames = request.getParameterNames();
      while( parameterNames.hasMoreElements() ) {
        String parameterName = ( String )parameterNames.nextElement();
        String parameterValue = request.getParameter( parameterName );
        msg.append( parameterName );
        msg.append( "=" );
        msg.append( parameterValue );      
        msg.append( "\n" );      
      }
      requestParamsLogger.log( LOG_LEVEL, msg.toString() );    
    }
  }
  
  private static void logResponseContent() {
    if( responseContentLogger.isLoggable( LOG_LEVEL ) ) {
      HtmlResponseWriter content = getStateInfo().getResponseWriter();
      StringBuffer msg = new StringBuffer();
      for( int i = 0; i < content.getHeadSize(); i ++ ) {
        msg.append( content.getHeadToken( i ) );
      }
      for( int i = 0; i < content.getBodySize(); i ++ ) {
        msg.append( content.getBodyToken( i ) );
      }
      for( int i = 0; i < content.getFootSize(); i ++ ) {
        msg.append( content.getFootToken( i ) );
      }
      responseContentLogger.log( LOG_LEVEL, msg.toString() );
    }
  }
}