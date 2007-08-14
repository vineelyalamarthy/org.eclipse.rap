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
package com.w4t.webtextareakit;

import java.io.IOException;

import org.eclipse.rwt.internal.lifecycle.HtmlResponseWriter;
import org.eclipse.rwt.internal.util.HTML;

import com.w4t.*;
import com.w4t.event.WebItemEvent;



/** <p>The default noscript renderer for org.eclipse.rap.WebTextArea.</p>
  *
  * <p>The default noscript renderer is non-browser-specific and implements 
  * functionality in a way that runs on browsers that do not implement or 
  * permit javascript execution.</p>
  */
public class WebTextAreaRenderer_Default_Noscript extends Renderer {
 
  public void readData( final WebComponent component ) {
    ReadDataUtil.applyValue( component );
  }
  
  public void processAction( final WebComponent component ) {
    ProcessActionUtil.processItemStateChangedNoScript( component );
  }
  
  public void render( final WebComponent component ) throws IOException {
    WebTextArea wta = ( WebTextArea )component;
    HtmlResponseWriter out = getResponseWriter();
    out.startElement( HTML.TEXTAREA, null );
    out.writeAttribute( HTML.NAME, wta.getUniqueID(), null );
    out.writeAttribute( HTML.ROWS, String.valueOf( wta.getRows() ), null );
    out.writeAttribute( HTML.COLS, String.valueOf( wta.getCols() ), null );
    out.writeAttribute( HTML.WRAP, wta.getWrap(), null );
    RenderUtil.writeUniversalAttributes( wta );
    RenderUtil.writeReadOnly( wta );
    RenderUtil.writeDisabled( wta );
    out.writeText( WebTextAreaUtil.getValue( wta ), null );
    out.endElement( HTML.TEXTAREA );
    createSubmitter( wta );
  }
  
  // helping methods
  //////////////////

  private static void createSubmitter( final WebTextArea wta ) 
    throws IOException 
  {
    if( WebItemEvent.hasListener( wta ) && wta.isEnabled() ) {
      RenderUtil.writeItemSubmitter( wta.getUniqueID() );
    }
  }
}
