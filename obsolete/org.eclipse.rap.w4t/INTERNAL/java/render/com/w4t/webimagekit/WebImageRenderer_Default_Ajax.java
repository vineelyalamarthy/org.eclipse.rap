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
import com.w4t.ajax.AjaxStatusUtil;


/** <p>The default renderer for org.eclipse.rap.WebImage.</p>
  *
  * <p>The default renderer is non-browser-specific and implements 
  * functionality in a way that runs on the most commonly used browsers.</p>
  */  
public class WebImageRenderer_Default_Ajax extends Renderer {
  
  public void render( final WebComponent component ) throws IOException {
    if( AjaxStatusUtil.mustRender( component ) ) {
      doRender( getResponseWriter(), ( WebImage )component );   
    }
  }

  private static void doRender( final HtmlResponseWriter out, 
                                final WebImage wim ) 
    throws IOException 
  {
    renderImg( out, wim );
  }
    
  private static void renderImg( final HtmlResponseWriter out, 
                                 final WebImage wim ) 
    throws IOException 
  {
    out.startElement( HTML.IMG, wim );
    out.writeAttribute( HTML.ID, wim.getUniqueID(), null );
    String path = WebImageUtil.createImagePath( wim );
    out.writeAttribute( HTML.SRC, RenderUtil.resolve( path ), null );
    out.writeAttribute( HTML.ALT, RenderUtil.resolve( wim.getAlt() ), null );
    out.writeAttribute( HTML.BORDER, wim.getBorder(), null );
    WebImageUtil.renderWidth( wim );
    WebImageUtil.renderHeight( wim );
    RenderUtil.writeUniversalAttributes( wim );
    out.endElement( HTML.IMG );

  }
  
}