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
package com.w4t.weblabelkit;

import java.io.IOException;

import org.eclipse.rwt.internal.lifecycle.HtmlResponseWriter;
import org.eclipse.rwt.internal.util.HTML;

import com.w4t.*;



/** <p>The default noscript renderer for org.eclipse.rap.WebLabel.</p>
  *
  * <p>The default noscript renderer is non-browser-specific and implements 
  * functionality in a way that runs on browsers that do not implement or 
  * permit javascript execution.</p>
  */
public class WebLabelRenderer_Default_Noscript extends Renderer {
  
  public void render( final WebComponent component ) throws IOException {
    WebLabel wlb = ( WebLabel )component;
    HtmlResponseWriter out = getResponseWriter();
    out.startElement( HTML.SPAN, wlb );
    RenderUtil.writeUniversalAttributes( wlb );
    String value = WebLabelUtil.getValue( wlb );
    String[] splitLineBreak = RenderUtil.splitLineBreak( value );
    for( int i = 0; i < splitLineBreak.length; i++ ) {
      if( splitLineBreak[ i ].equals( "\n" ) ) {
        out.write( "<br>" );
      } else {
        out.writeText( splitLineBreak[ i ], null );
      }      
    }
    out.endElement( HTML.SPAN );
  }
}
