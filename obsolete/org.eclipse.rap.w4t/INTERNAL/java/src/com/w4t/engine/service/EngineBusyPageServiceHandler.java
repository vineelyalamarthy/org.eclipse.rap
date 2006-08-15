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

import java.io.*;
import com.w4t.engine.util.IEngineConfig;
import com.w4t.util.ConfigurationReader;


class EngineBusyPageServiceHandler extends AbstractServiceHandler {

  /** name of the html page which is shown if the server load is too heavy. */
  private final static String BUSY_PAGE = "busy.html";
  
  /** contains the page content shown if a EngineBusyExcepton occurs. */
  private String busyPage;  
  

  public void service() throws IOException {
    PrintWriter writer = getOutputWriter();
    try {
      writer.print( retrieveBusyPage() );
    } finally {
      writer.close();
    }
    ContextProvider.getSession().invalidate();
  }
  
  private String retrieveBusyPage() {
    if( busyPage == null ) {      
      try {
        IEngineConfig engineConfig = ConfigurationReader.getEngineConfig();
        File serverDir = engineConfig.getServerContextDir();
        String name = serverDir.toString() + File.separator + BUSY_PAGE;
        FileInputStream is = new FileInputStream( name );      
        byte[] bytes = new byte[ is.available() ];
        is.read( bytes, 0, is.available() );
        StringBuffer lines = new StringBuffer();
        for( int i = 0; i < bytes.length; i++ ) {
          lines.append( ( char )bytes[ i ] );
        }
        busyPage = lines.toString();
      } catch( IOException ioe ) {
        busyPage =  
            "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">"
          + "<html><head><title>W4 Toolkit - System Busy </title></head>"
          + "<body>System busy, please try again later.</body></html>";
      }
    }
    return busyPage;
  }
}