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
package com.w4t.internal.adaptable;

import org.eclipse.rwt.internal.lifecycle.HtmlResponseWriter;

public interface IFormAdapter {
  
  void postRender();
  HtmlResponseWriter getRenderBuffer();
  void increase();
  long getTimeStamp();
  void refreshTimeStamp();
  int getRequestCounter();
  void updateRequestCounter( int requestCounter );
  void refreshWindow( boolean refreshWindow );
  void showInNewWindow( boolean showInNewWindow );
  void addWindowOpenerBuffer( String windowOpenerBuffer );
  String getWindowOpenerBuffer();
  void addWindowRefresherBuffer( String windowRefresherBuffer );
  String getWindowRefresherBuffer();
  void addWindowCloserBuffer( String windowCloserBuffer );
  String getWindowCloserBuffer();
  void setActive( boolean active );
  boolean isActive();
}
