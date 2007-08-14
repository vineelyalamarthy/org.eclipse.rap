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
package com.w4t.webimagekit;

import java.io.IOException;

import org.eclipse.rwt.internal.lifecycle.HtmlResponseWriter;
import org.eclipse.rwt.internal.util.HTML;

import com.w4t.*;


/** <p>The default noscript renderer for org.eclipse.rap.WebImage.</p>
  *
  * <p>The default noscript renderer is non-browser-specific and implements 
  * functionality in a way that runs on browsers that do not implement or 
  * permit javascript execution.</p>
  */
public class WebImageRenderer_Default_Noscript extends Renderer {

  // TODO: textfield for databound image is ignored so far (does it make 
  // sense for noscript version?
  public void render( final WebComponent component ) throws IOException {
    
    WebImage wim = ( WebImage )component;
    HtmlResponseWriter out = getResponseWriter();
    
    out.startElement( HTML.IMG, wim );
    String path = WebImageUtil.createImagePath( wim );
    out.writeAttribute( HTML.SRC, RenderUtil.resolve( path ), null );
    out.writeAttribute( HTML.ALT, RenderUtil.resolve( wim.getAlt() ), null );
    out.writeAttribute( HTML.BORDER, wim.getBorder(), null );
    WebImageUtil.renderHeight( wim );
    WebImageUtil.renderWidth( wim );
    RenderUtil.writeUniversalAttributes( wim );
    out.endElement( HTML.IMG );
  }

}