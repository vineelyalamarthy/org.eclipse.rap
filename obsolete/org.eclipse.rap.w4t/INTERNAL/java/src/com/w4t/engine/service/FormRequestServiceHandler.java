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
import javax.servlet.ServletException;
import com.w4t.Browser;
import com.w4t.HtmlResponseWriter;
import com.w4t.engine.W4TModel;
import com.w4t.engine.W4TModelUtil;
import com.w4t.engine.requests.RequestCancelledException;
import com.w4t.engine.requests.RequestParams;
import com.w4t.util.browser.BrowserLoader;


class FormRequestServiceHandler extends AbstractServiceHandler {
  
  public void service() throws IOException, ServletException {
//    logRequestParams();
    // start point of process time    
    long startTime = System.currentTimeMillis();    
    initializeStateInfo();
    try {
      detectBrowser();
      if( isBrowserDetected() ) {
        W4TModelUtil.initModel();
        getServiceAdapter( W4TModel.getInstance() ).execute();
      } else {
        BrowserSurvey.sendBrowserSurvey();
      }
      if( !ServiceManager.isTimeStampTrigger() ) {
        appendProcessTime( startTime );
//System.out.println( "time to process: " + ( System.currentTimeMillis() - startTime ) );
        writeOutput();
      }
    } catch( final RequestCancelledException ignore ) {
      // then we do nothing (no content must be written to the out stream)
    }
  }
  

  //////////////////
  // helping methods
  
  private static boolean isBrowserDetected() {
    return getBrowser() != null;
  }

  private static Browser getBrowser() {
    String id = ServiceContext.DETECTED_SESSION_BROWSER;
    return ( Browser )getRequest().getSession().getAttribute( id );
  }
  
  private static void detectBrowser() {
    if( !isBrowserDetected() ) {
      if(    getRequest().getParameter( RequestParams.SCRIPT ) != null 
          && getRequest().getParameter( RequestParams.AJAX_ENABLED ) != null )
      {
        Browser browser = BrowserLoader.load();
        String id = ServiceContext.DETECTED_SESSION_BROWSER;
        getRequest().getSession().setAttribute( id, browser );
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
//System.out.print( content.getHeadToken( i ) );
      }
  
      // send the body to the client
      for( int i = 0; i < content.getBodySize(); i++ ) {
        out.print( content.getBodyToken( i ) );
//System.out.print( content.getBodyToken( i ) );
      }
  
      // send the foot to the client
      for( int i = 0; i < content.getFootSize(); i++ ) {
        out.print( content.getFootToken( i ) );
//System.out.print( content.getFootToken( i ) );
//System.out.println();
      }
    } finally {
      out.close();
    }
  }

  private static IServiceStateInfo getStateInfo() {
    return ContextProvider.getStateInfo();
  }
}