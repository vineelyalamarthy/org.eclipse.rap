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


/** <p>The default renderer for org.eclipse.rap.WebSelect.</p>
  *
  * <p>The default renderer is non-browser-specific and implements 
  * functionality in a way that runs on the most commonly used browsers.</p>
  */
public class WebSelectRenderer_Default_Script extends Renderer {

  public void readData( final WebComponent component ) {
    ReadDataUtil.applyValue( component );
  }
  
  public void processAction( final WebComponent component ) {
    ProcessActionUtil.processFocusGained( component );
    ProcessActionUtil.processItemStateChangedScript( component );
  }

  public void render( final WebComponent component ) throws IOException {
    WebSelect wsl = ( WebSelect )component;
    HtmlResponseWriter out = getResponseWriter();
    out.startElement( HTML.SELECT, null );
    out.writeAttribute( HTML.NAME, wsl.getUniqueID(), null );
    EventUtil.createItemAndFocusHandler( wsl );
    RenderUtil.writeDisabled( component );
    out.writeAttribute( HTML.SIZE, String.valueOf( wsl.getSize() ), null );
    RenderUtil.writeUniversalAttributes( wsl );
    WebSelectUtil.appendItemEntries( wsl );
    out.endElement( HTML.SELECT );
  }
}