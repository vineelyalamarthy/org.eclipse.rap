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
package com.w4t.webbuttonkit;

import java.io.IOException;

import org.eclipse.rwt.internal.lifecycle.HtmlResponseWriter;
import org.eclipse.rwt.internal.service.ContextProvider;
import org.eclipse.rwt.internal.util.HTML;

import com.w4t.*;
import com.w4t.event.WebActionEvent;
import com.w4t.event.WebFocusGainedEvent;


final class WebButtonRendererUtil {
  
  public static final String WEB_ACTION_PERFORMED_START
    = "eventHandler.webActionPerformed('";
  public static final String WEB_ACTION_PERFORMED_END = "')";
  public static final String JS_SET_FOCUS_ID = "eventHandler.setFocusID('";
  public static final String JS_FOCUS_GAINED = "eventHandler.webFocusGained('";
  public static final String END = "')";
  public static final String END_SEMICOLON = "');";
  public static final String RESET_FORM = "eventHandler.resetForm();";

  private WebButtonRendererUtil() {
  }
  
  private static HtmlResponseWriter getResponseWriter() {
    return ContextProvider.getStateInfo().getResponseWriter();
  }

  static void createEventHandler( final WebButton wbt )
    throws IOException
  {
    HtmlResponseWriter out = RenderUtilLinkScript.getResponseWriter();
    if( WebFocusGainedEvent.hasListener( wbt ) ) {
      StringBuffer focusHandler = new StringBuffer();
      focusHandler.append( WebButtonRendererUtil.JS_SET_FOCUS_ID );
      focusHandler.append( wbt.getUniqueID() );
      focusHandler.append( WebButtonRendererUtil.END_SEMICOLON );
      focusHandler.append( WebButtonRendererUtil.JS_FOCUS_GAINED );
      focusHandler.append( wbt.getUniqueID() );
      focusHandler.append( WebButtonRendererUtil.END );
      out.writeAttribute( HTML.ON_FOCUS, focusHandler.toString(), null ); 
    } else {
      StringBuffer focusHandler = new StringBuffer();
      focusHandler.append( WebButtonRendererUtil.JS_SET_FOCUS_ID );
      focusHandler.append( wbt.getUniqueID() );
      focusHandler.append( WebButtonRendererUtil.END );
      out.writeAttribute( HTML.ON_FOCUS, focusHandler.toString(), null );
    }
  
    if( WebActionEvent.hasListener( wbt ) ) {
      writeMouseEvents();
    }
  }
  
  static void writeMouseEvents() throws IOException {
    HtmlResponseWriter out = getResponseWriter();
    out.writeAttribute( HTML.ON_MOUSE_DOWN, 
                        JavaScriptUtil.JS_SUSPEND_SUBMIT, 
                        null );
    out.writeAttribute( HTML.ON_MOUSE_OUT, 
                        JavaScriptUtil.JS_RESUME_SUBMIT, 
                        null );
    out.writeAttribute( HTML.ON_MOUSE_UP, 
                        JavaScriptUtil.JS_RESUME_SUBMIT,
                        null );
  }

  static void createAnchor(  final WebButton wbt ) 
    throws IOException
  {
    HtmlResponseWriter out = RenderUtilImageScript.getResponseWriter();
    WebButtonRenderer.createNonBreakingSpace( wbt );
    out.startElement( HTML.A, null );
    out.writeAttribute( HTML.ID, wbt.getUniqueID(), null );
    StringBuffer href = new StringBuffer( "javascript:" );
    if ( wbt.isReset() ) {
      href.append( WebButtonRendererUtil.RESET_FORM );
    }
    href.append( RenderUtil.webActionPerformed( wbt.getUniqueID() ) );
    out.writeAttribute( HTML.HREF, href.toString(), null );
    createEventHandler( wbt );
    RenderUtil.writeUniversalAttributes( wbt );
    WebButtonRenderer.createImageOrLabel( wbt );
    out.endElement( HTML.A );
    WebButtonRenderer.createNonBreakingSpace( wbt );
  }

  static void createDisabledLink( final WebButton wbt )
    throws IOException
  {
    HtmlResponseWriter out = RenderUtilImageScript.getResponseWriter();
    out.startElement( HTML.SPAN, null );
    out.writeAttribute( HTML.ID, wbt.getUniqueID(), null );
    RenderUtil.writeUniversalAttributes( wbt );
    WebButtonRenderer.createImageOrLabel( wbt );
    out.endElement( HTML.SPAN );
  }
}
