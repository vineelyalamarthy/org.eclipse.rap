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
package com.w4t.engine.requests;

import javax.servlet.http.HttpServletRequest;
import com.w4t.engine.service.ContextProvider;

/**<p>Utility class to construct URL's.</p> */
public final class URLHelper {
  
  public static final String EQUAL = "=";
  public static final String AMPERSAND = "&";
  public static final String QUESTION_MARK = "?";

  
  private URLHelper() {
    // no instance creation
  }

  /** returns the servlets URL of the current W4Toolkit installation. */
  public static String getURLString( final boolean addEncodingDummy ) {
    String requestURL = getRequestURL();
    HttpServletRequest request = ContextProvider.getRequest();
    String servletPath = request.getServletPath();
    int pos = requestURL.indexOf( servletPath );
    String result = requestURL.substring( 0, pos + servletPath.length() );
    
    ///////////////////////////////////////////////////////////////////////
    // add a dummy parameter to create a complete session id encoding
    // if cookies are disabled
    if( addEncodingDummy ) {
      result += QUESTION_MARK + RequestParams.ENCODING_DUMMY + "=no";
    }
    ///////////////////////////////////////////////////////////////////////
    return result;
  }

  /** returns the url to the webapps context root of the current W4Toolkit 
   *  installation. */
  public static String getContextURLString() {
    String requestURL = getRequestURL();
    HttpServletRequest request = ContextProvider.getRequest();
    String servletPath = request.getServletPath();
    int pos = requestURL.indexOf( servletPath );
    return requestURL.substring( 0, pos );
  }

  /** returns the servlets URL of current W4Toolkit installation with
   *  session id encoding if cookies are not accepted by the browser. */  
  public static String getEncodedURLString() {
    String result = getURLString( true );
    return ContextProvider.getResponse().encodeURL( result );
  }

  
  //////////////////
  // helping methods

  private static String getRequestURL() {
    HttpServletRequest request = ContextProvider.getRequest();

    ///////////////////////////////////////////////////////////////////////
    // use the following workaround to keep servlet 2.2 spec. compatibility
    String port = URLHelper.createPortPattern( request );
    StringBuffer result = new StringBuffer();
    String serverName = request.getServerName();
    if( serverName != null && !"".equals( serverName ) ) {
      result.append( request.getScheme() );
      result.append( "://" );
      result.append( serverName );
      result.append( port );
    }
    result.append( request.getRequestURI() );
    ///////////////////////////////////////////////////////////////////////
    
    return result.toString();
  }
  
  private static String createPortPattern( final HttpServletRequest request ) {
    String result = String.valueOf( request.getServerPort() );
    if( result != null && !result.equals( "" ) ) {
      StringBuffer buffer = new StringBuffer();
      buffer.append( ":" );
      buffer.append( result );
      result = buffer.toString(); 
    } else {
      result = "";
    }
    return result;
  }

  /**
   * <p>Appends the given <code>key</code> and <code>value</code> to the given 
   * buffer by prepending a question mark and separating key and value with an 
   * equals sign.</p>
   */
  public static void appendFirstParam( final StringBuffer buffer, 
                                       final String key, 
                                       final String value ) 
  {
    buffer.append( QUESTION_MARK );
    buffer.append( key );
    buffer.append( EQUAL );
    buffer.append( value );
  }
  
  /**
   * <p>Appends the given <code>key</code> and <code>value</code> to the given 
   * buffer by prepending an ampersand and separating key and value with an 
   * equals sign.</p>
   */
  public static void appendParam( final StringBuffer buffer, 
                                     final String key, 
                                     final String value ) 
  {
    buffer.append( AMPERSAND );
    buffer.append( key );
    buffer.append( EQUAL );
    buffer.append( value );
  }
}
