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
import com.w4t.event.WebItemEvent;


/** <p>The default noscript renderer for org.eclipse.rap.WebSelect.</p>
  *
  * <p>The default noscript renderer is non-browser-specific and implements 
  * functionality in a way that runs on browsers that do not implement or 
  * permit javascript execution.</p>
  */
public class WebSelectRenderer_Default_Noscript extends Renderer {

  public void readData( final WebComponent component ) {
    ReadDataUtil.applyValue( component );
  }
  
  public void processAction( final WebComponent component ) {
    ProcessActionUtil.processItemStateChangedNoScript( component );
  }

  public void render( final WebComponent component ) throws IOException {
    WebSelect wsl = ( WebSelect )component;
    HtmlResponseWriter out = getResponseWriter();
    out.startElement( HTML.SELECT, null );
    out.writeAttribute( HTML.NAME, wsl.getUniqueID(), null );
    RenderUtil.writeDisabled( component );
    out.writeAttribute( HTML.SIZE, String.valueOf( wsl.getSize() ), null );
    RenderUtil.writeUniversalAttributes( wsl );
    WebSelectUtil.appendItemEntries( wsl );
    out.endElement( HTML.SELECT );
    createSubmitter( wsl );
  }

  private static void createSubmitter( final WebSelect wsl ) 
    throws IOException 
  {
    if( WebItemEvent.hasListener( wsl ) && wsl.isEnabled() ) {
      RenderUtil.writeItemSubmitter( wsl.getUniqueID() );
    }
  }
}