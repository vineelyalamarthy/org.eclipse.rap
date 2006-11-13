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
package com.w4t.util;

import java.io.*;
import java.security.Principal;
import java.util.*;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.http.*;


class Request implements HttpServletRequest {
  
  private final HttpSession session;
  
  Request( final HttpSession session ) {
    this.session = session;
  }

  public String getAuthType() {
    return null;
  }

  public Cookie[] getCookies() {
    return null;
  }

  public long getDateHeader( final String arg0 ) {
    return 0;
  }

  public String getHeader( final String arg0 ) {
    return null;
  }

  public Enumeration getHeaders( final String arg0 ) {
    return null;
  }

  public Enumeration getHeaderNames() {
    return null;
  }

  public int getIntHeader( final String arg0 ) {
    return 0;
  }

  public String getMethod() {
    return null;
  }

  public String getPathInfo() {
    return null;
  }

  public String getPathTranslated() {
    return null;
  }

  public String getContextPath() {
    return null;
  }

  public String getQueryString() {
    return null;
  }

  public String getRemoteUser() {
    return null;
  }

  public boolean isUserInRole( final String arg0 ) {
    return false;
  }

  public Principal getUserPrincipal() {
    return null;
  }

  public String getRequestedSessionId() {
    return null;
  }

  public String getRequestURI() {
    return null;
  }

  public StringBuffer getRequestURL() {
    return null;
  }

  public String getServletPath() {
    return null;
  }

  public HttpSession getSession( final boolean arg0 ) {
    return session;
  }

  public HttpSession getSession() {
    return session;
  }

  public boolean isRequestedSessionIdValid() {
    return false;
  }

  public boolean isRequestedSessionIdFromCookie() {
    return false;
  }

  public boolean isRequestedSessionIdFromURL() {
    return false;
  }

  public boolean isRequestedSessionIdFromUrl() {
    return false;
  }

  public Object getAttribute( final String arg0 ) {
    return null;
  }

  public Enumeration getAttributeNames() {
    return null;
  }

  public String getCharacterEncoding() {
    return null;
  }

  public void setCharacterEncoding( final String arg0 )
    throws UnsupportedEncodingException
  {
  }

  public int getContentLength() {
    return 0;
  }

  public String getContentType() {
    return null;
  }

  public ServletInputStream getInputStream() throws IOException {
    return null;
  }

  public String getParameter( final String arg0 ) {
    return null;
  }

  public Enumeration getParameterNames() {
    return null;
  }

  public String[] getParameterValues( final String arg0 ) {
    return null;
  }

  public Map getParameterMap() {
    return null;
  }

  public String getProtocol() {
    return null;
  }

  public String getScheme() {
    return null;
  }

  public String getServerName() {
    return null;
  }

  public int getServerPort() {
    return 0;
  }

  public BufferedReader getReader() throws IOException {
    return null;
  }

  public String getRemoteAddr() {
    return null;
  }

  public String getRemoteHost() {
    return null;
  }

  public void setAttribute( final String arg0, final Object arg1 ) {
  }

  public void removeAttribute( final String arg0 ) {
  }

  public Locale getLocale() {
    return null;
  }

  public Enumeration getLocales() {
    return null;
  }

  public boolean isSecure() {
    return false;
  }

  public RequestDispatcher getRequestDispatcher( final String arg0 ) {
    return null;
  }

  public String getRealPath( final String arg0 ) {
    return null;
  }

  public String getLocalAddr() {
    throw new UnsupportedOperationException();
  }

  public String getLocalName() {
    throw new UnsupportedOperationException();
  }

  public int getLocalPort() {
    throw new UnsupportedOperationException();
  }

  public int getRemotePort() {
    throw new UnsupportedOperationException();
  }
}
