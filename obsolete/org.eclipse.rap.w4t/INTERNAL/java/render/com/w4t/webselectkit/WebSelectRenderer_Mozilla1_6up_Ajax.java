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
package com.w4t.webselectkit;

import java.io.IOException;

import org.eclipse.rwt.internal.lifecycle.HtmlResponseWriter;
import org.eclipse.rwt.internal.util.HTML;

import com.w4t.*;
import com.w4t.ajax.AjaxStatusUtil;
import com.w4t.event.WebItemEvent;


/** <p>The default Ajax renderer for org.eclipse.rap.WebSelect on Mozilla browsers.</p>
  */
public class WebSelectRenderer_Mozilla1_6up_Ajax extends Renderer {

  public void readData( final WebComponent component ) {
    ReadDataUtil.applyValue( component );
  }
  
  public void processAction( final WebComponent component ) {
    ProcessActionUtil.processFocusGained( component );
    ProcessActionUtil.processItemStateChangedScript( component );
  }
  
  public void render( final WebComponent component ) throws IOException {
    if( AjaxStatusUtil.mustRender( component ) ) {
      doRender( getResponseWriter(), ( WebSelect )component );
    }
  }

  private static void doRender( final HtmlResponseWriter out, 
                                final WebSelect wsl ) 
    throws IOException 
  {
    out.startElement( HTML.SELECT, null );
    out.writeAttribute( HTML.ID, wsl.getUniqueID(), null );
    out.writeAttribute( HTML.NAME, wsl.getUniqueID(), null );
    writeItemEventHandler( out, wsl );
    WebSelectUtil.writeFocusEventHandler( wsl );
    RenderUtil.writeDisabled( wsl );
    out.writeAttribute( HTML.SIZE, String.valueOf( wsl.getSize() ), null );
    RenderUtil.writeUniversalAttributes( wsl );
    WebSelectUtil.appendItemEntries( wsl );
    out.endElement( HTML.SELECT );
  }

  private static void writeItemEventHandler( final HtmlResponseWriter out, 
                                             final WebSelect wsl ) 
    throws IOException 
  {
    if( wsl.isEnabled() && WebSelectUtil.needsItemHandler( wsl ) ) {
      out.writeAttribute( HTML.ON_CHANGE, WebItemEvent.JS_EVENT_CALL, null );
    }
  }
  
}