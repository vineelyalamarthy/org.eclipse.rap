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
package com.w4t.webfileuploadkit;

import java.io.IOException;

import org.eclipse.rwt.internal.lifecycle.HtmlResponseWriter;
import org.eclipse.rwt.internal.util.HTML;

import com.w4t.WebFileUpload;
import com.w4t.event.WebFocusGainedEvent;

public class WebFileUploadRenderer_Default_Script extends WebFileUploadRenderer
{
  private static final Object JS_SET_FOCUS_ID 
    = "eventHandler.setFocusID(this)";
  private static final Object JS_SET_FOCUS_ID_SEMICOLON 
    = "eventHandler.setFocusID(this);";

  void appendFocusHandling( final WebFileUpload wfu ) throws IOException {
    HtmlResponseWriter out = getResponseWriter();
    if( wfu.isDesignTime() ) {
      StringBuffer buffer = new StringBuffer();
      buffer.append( JS_SET_FOCUS_ID_SEMICOLON );
      buffer.append( WebFocusGainedEvent.JS_HANDLER_CALL );
      out.writeAttribute( HTML.ON_FOCUS, buffer, null );
    } else {
      out.writeAttribute( HTML.ON_FOCUS, JS_SET_FOCUS_ID, null );
    }
  }
}