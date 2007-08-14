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
package com.w4t.dhtml.menubuttonkit;

import java.io.IOException;

import org.eclipse.rwt.internal.lifecycle.HtmlResponseWriter;
import org.eclipse.rwt.internal.util.HTML;

import com.w4t.dhtml.MenuButton;



/** <p>The renderer for org.eclipse.rap.dhtml.MenuButton on browsers with
  * javascript support.</p>
  */
public class MenuButtonRenderer_Default_Script extends MenuButtonRenderer {
  
  void createActiveButton( final MenuButton menuButton ) throws IOException {
    HtmlResponseWriter out = getResponseWriter();
    String button = "menuButton";
    StringBuffer buffer = new StringBuffer();
    buffer.append( "return menuBarHandler.buttonClick(this, '" );
    buffer.append( getMenu( menuButton ).getUniqueID() );
    buffer.append( "', '" );
    buffer.append( getMenuBar( menuButton ).getUniqueID() );
    buffer.append( "');" );
    out.startElement( HTML.A, null );
    out.writeAttribute( HTML.CLASS, button, null );
    out.writeAttribute( HTML.HREF, "", null );
    out.writeAttribute( HTML.ON_CLICK, buffer, null );
    buffer = new StringBuffer();
    buffer.append( "menuBarHandler.buttonMouseover(this, '" );
    buffer.append( getMenu( menuButton ).getUniqueID() );
    buffer.append( "', '" );
    buffer.append( getMenuBar( menuButton ).getUniqueID() );
    buffer.append( "');" );
    out.writeAttribute( HTML.ON_MOUSE_OVER, buffer, null );
    out.writeText( getLabel( menuButton ), null );
    out.endElement( HTML.A );
  }
  
  void createInactiveButton( final MenuButton menuButton ) throws IOException {
    HtmlResponseWriter out = getResponseWriter();
    String clas = "disabledMenuButton";
    out.startElement( HTML.A, null );
    out.writeAttribute( HTML.CLASS, clas, null );
    out.writeText( getLabel( menuButton ), null );
    out.endElement( HTML.A );    
  }
}