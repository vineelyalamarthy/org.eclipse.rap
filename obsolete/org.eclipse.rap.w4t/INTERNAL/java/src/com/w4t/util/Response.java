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

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;


class Response implements HttpServletResponse {

  public void addCookie( final Cookie arg0 ) {
  }

  public boolean containsHeader( final String arg0 ) {
    return false;
  }

  public String encodeURL( final String arg0 ) {
    return null;
  }

  public String encodeRedirectURL( final String arg0 ) {
    return null;
  }

  public String encodeUrl( final String arg0 ) {
    return null;
  }

  public String encodeRedirectUrl( final String arg0 ) {
    return null;
  }

  public void sendError( final int arg0, final String arg1 )
    throws IOException
  {
  }

  public void sendError( final int arg0 ) throws IOException {
  }

  public void sendRedirect( final String arg0 ) throws IOException {
  }

  public void setDateHeader( final String arg0, final long arg1 ) {
  }

  public void addDateHeader( final String arg0, final long arg1 ) {
  }

  public void setHeader( final String arg0, final String arg1 ) {
  }

  public void addHeader( final String arg0, final String arg1 ) {
  }

  public void setIntHeader( final String arg0, final int arg1 ) {
  }

  public void addIntHeader( final String arg0, final int arg1 ) {
  }

  public void setStatus( final int arg0 ) {
  }

  public void setStatus( final int arg0, final String arg1 ) {
  }

  public String getCharacterEncoding() {
    return null;
  }

  public ServletOutputStream getOutputStream() throws IOException {
    return null;
  }

  public PrintWriter getWriter() throws IOException {
    return null;
  }

  public void setContentLength( final int arg0 ) {
  }

  public void setContentType( final String arg0 ) {
  }

  public void setBufferSize( final int arg0 ) {
  }

  public int getBufferSize() {
    return 0;
  }

  public void flushBuffer() throws IOException {
  }

  public void resetBuffer() {
  }

  public boolean isCommitted() {
    return false;
  }

  public void reset() {
  }

  public void setLocale( final Locale arg0 ) {
  }

  public Locale getLocale() {
    return null;
  }

  public String getContentType() {
    throw new UnsupportedOperationException();
  }

  public void setCharacterEncoding( String charset ) {
    throw new UnsupportedOperationException();
  }
}
