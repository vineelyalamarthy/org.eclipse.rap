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
package com.w4t.webcheckboxkit;

import java.io.IOException;

import org.eclipse.rwt.internal.lifecycle.HtmlResponseWriter;
import org.eclipse.rwt.internal.util.HTML;

import com.w4t.*;
import com.w4t.event.WebFocusGainedEvent;
import com.w4t.event.WebItemEvent;


/** <p>The default renderer for org.eclipse.rap.WebCheckBox.</p>
  *
  * <p>The default renderer is non-browser-specific and implements 
  * functionality in a way that runs on the most commonly used browsers.</p>
  */
public class WebCheckBoxRenderer_Default_Script extends WebCheckBoxRenderer {
  
  public static final String JS_SET_FOCUS_ID 
    = "eventHandler.setFocusID(this)";

  public static final String JS_SET_FOCUS_ID_SEMICOLON 
    = "eventHandler.setFocusID(this);";

  public void readData( final WebComponent component ) {
    WebCheckBoxReadDataUtil.applyValue( ( WebCheckBox )component );
  }
  
  public void processAction( final WebComponent component ) {
    ProcessActionUtil.processFocusGained( component );
    ProcessActionUtil.processItemStateChangedScript( component );
  }
  
  void createEventHandler( final WebCheckBox wcb ) throws IOException {
    HtmlResponseWriter out = getResponseWriter();
    if( wcb.isEnabled() ) {
      if( WebItemEvent.hasListener( wcb ) ) {
        createMouseEvents();
      }
      if( WebFocusGainedEvent.hasListener( wcb ) ) {
        StringBuffer buffer = new StringBuffer();
        buffer.append( JS_SET_FOCUS_ID_SEMICOLON );
        buffer.append( WebFocusGainedEvent.JS_HANDLER_CALL );
        out.writeAttribute( HTML.ON_FOCUS, buffer, null );
      } else {
        out.writeAttribute( HTML.ON_FOCUS, JS_SET_FOCUS_ID, null );
      }
    }
  }

  private void createMouseEvents() throws IOException {
    HtmlResponseWriter out = getResponseWriter();
    out.writeAttribute( HTML.ON_MOUSE_DOWN, 
                        "eventHandler.setClickedCheckBox(this)", 
                        null );
    out.writeAttribute( HTML.ON_MOUSE_UP, 
                        "eventHandler.triggerClickedCheckBox(this)", 
                        null );
    out.writeAttribute( HTML.ON_KEY_DOWN, 
                        "eventHandler.keyOnCheckBox(this)", 
                        null );
  }
  
  void createSubmitter( final WebCheckBox wcb ) {
    // nothing to do here in script mode
  }
}