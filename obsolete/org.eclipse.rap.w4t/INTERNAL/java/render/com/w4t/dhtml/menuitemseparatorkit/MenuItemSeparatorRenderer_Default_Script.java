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
package com.w4t.dhtml.menuitemseparatorkit;

import java.io.IOException;

import org.eclipse.rwt.internal.lifecycle.HtmlResponseWriter;
import org.eclipse.rwt.internal.util.HTML;

import com.w4t.WebComponent;


/** <p>The renderer for org.eclipse.rap.dhtml.MenuItemSeparator on browsers with
  * javascript support.</p>
  */
public class MenuItemSeparatorRenderer_Default_Script 
  extends MenuItemSeparatorRenderer
{

  private static final String STYLE = "border-bottom: 1px solid #f0f0f0;"
                                    + "border-top: 1px solid #808080;"
                                    + "margin: 3px 4px 3px 4px;";

  public void render( final WebComponent component ) throws IOException {
    renderHorizontalSeparator();
  }
  
  void renderHorizontalSeparator() throws IOException {
    HtmlResponseWriter out = getResponseWriter();
    out.startElement( HTML.DIV, null );
    out.writeAttribute( HTML.CLASS, out.registerCssClass( STYLE ), null );
    out.endElement( HTML.DIV );
  }
}
