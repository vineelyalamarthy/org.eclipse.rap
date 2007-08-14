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
package com.w4t.dhtml.menukit;

import java.io.IOException;

import org.eclipse.rwt.internal.lifecycle.HtmlResponseWriter;
import org.eclipse.rwt.internal.util.HTML;

import com.w4t.*;
import com.w4t.dhtml.*;
import com.w4t.dhtml.menustyle.MenuBarStyle;
import com.w4t.dhtml.menustyle.MenuButtonActiveStyle;


/** <p>The renderer for org.eclipse.rap.dhtml.CMenu on browsers without
  * javascript support.</p>
  */
public class MenuRenderer_Default_Noscript extends MenuRenderer {
  
  
  public void processAction( final WebComponent component ) {
    ProcessActionUtil.processActionPerformedNoScript( component );
  }

  public void render( final WebComponent component ) throws IOException {
    Menu menu = ( Menu )component;
    if( menu.isExpanded() ) {
      createSeparatorLine( menu );
      HtmlResponseWriter out = getResponseWriter();
      int padding = getMenuBar( menu ).getMenuBarStyle().getPadding();
      out.startElement( HTML.TABLE, null );
      out.writeAttribute( HTML.BORDER, "0", null );
      out.writeAttribute( HTML.CELLSPACING, "0", null );
      out.writeAttribute( HTML.CELLPADDING, String.valueOf( padding ), null );
      out.endElement( HTML.TR );
      
      Leaf[] leaves = menu.getLeaves();
      for( int i = 0; i < leaves.length; i++ ) {
        Leaf leaf = leaves[ i ];
        out.startElement( HTML.TD, null );
        LifeCycleHelper.render( leaf );
        out.endElement( HTML.TD );
      }
      out.endElement( HTML.TR );
      out.endElement( HTML.TABLE );
    }
  }  
  
  void createSeparatorLine( final Menu menu ) throws IOException {
    HtmlResponseWriter out = getResponseWriter();
    MenuButtonActiveStyle buttonActiveStyle
      = getMenuBar( menu ).getButtonActiveStyle();
    out.endElement( HTML.TD );
    out.endElement( HTML.TR );
    out.startElement( HTML.TR, null );
    out.startElement( HTML.TD, null );
    out.writeAttribute( HTML.BGCOLOR, 
                        buttonActiveStyle.getBgColor().toString(), 
                        null );
    out.startElement( HTML.IMG, null );
    out.writeAttribute( HTML.BORDER, "0", null );
    out.writeAttribute( HTML.SRC, ItemUtil.IMG_TRANSPARENT, null );
    out.writeAttribute( HTML.WIDTH, "1", null );
    out.writeAttribute( HTML.HEIGHT, "1", null );
    out.endElement( HTML.TD );
    out.endElement( HTML.TR );
    out.startElement( HTML.TR, null);
    out.startElement( HTML.TD, null);
    out.writeAttribute( HTML.BGCOLOR, 
                        getStyle( menu ).getBgColor().toString(), 
                        null );
  }
  
  static MenuBar getMenuBar( final Menu menu ) {
    return ( MenuBar )menu.getParentNode();    
  }
  
  private MenuBarStyle getStyle( final Menu menu ) {
    return getMenuBar( menu ).getMenuBarStyle();
  }
}