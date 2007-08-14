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


class RenderUtilButtonScript {
  
  private static final Object WEB_ACTION_PERFORMED 
    = "eventHandler.webActionPerformed(this)";
  
  private RenderUtilButtonScript() {
  }

  private static HtmlResponseWriter out() {
    return ContextProvider.getStateInfo().getResponseWriter();
  }
  
  
  public static void render( final WebButton wbt ) 
    throws IOException
  {
    String button;
    if (wbt.isReset() ) {
      button = HTML.RESET;
    } else {
      button = HTML.BUTTON;
    }
    HtmlResponseWriter out = out();
    out.startElement( HTML.INPUT, null );
    out.writeAttribute( HTML.ID, wbt.getUniqueID(), null );
    out.writeAttribute( HTML.TYPE, button, null );
    out.writeAttribute( HTML.NAME, wbt.getUniqueID(), null );
    out.writeAttribute( HTML.VALUE, WebButtonRenderer.getLabel( wbt ), null );
    RenderUtil.writeDisabled( wbt );
    RenderUtil.writeUniversalAttributes( wbt );
    createEventHandler( wbt );
    out.endElement( HTML.INPUT );

  }
  
  private static void createEventHandler( final WebButton wbt ) 
    throws IOException 
  {
    HtmlResponseWriter out = out();
    if( WebActionEvent.hasListener( wbt ) ) {
      out.writeAttribute( HTML.ON_CLICK, WEB_ACTION_PERFORMED, null );
    }
    
    if( WebFocusGainedEvent.hasListener( wbt ) ) {
      out.writeAttribute( WebFocusGainedEvent.JS_HANDLER, 
                          WebFocusGainedEvent.JS_HANDLER_CALL, 
                          null );
    }

    out.writeAttribute( HTML.ON_FOCUS, JavaScriptUtil.JS_SET_FOCUS_ID, null );

    if( WebActionEvent.hasListener( wbt ) ) {
      WebButtonRendererUtil.writeMouseEvents();
    }
  }
}
