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
package com.w4t.dhtml.menuitemkit;

import java.io.IOException;

import org.eclipse.rwt.internal.lifecycle.HtmlResponseWriter;
import org.eclipse.rwt.internal.util.HTML;

import com.w4t.RenderUtil;
import com.w4t.dhtml.MenuBar;
import com.w4t.dhtml.MenuItem;
import com.w4t.dhtml.menustyle.MenuItemDisabledStyle;
import com.w4t.dhtml.menustyle.MenuItemEnabledStyle;
import com.w4t.event.WebActionEvent;


/**
 * <p>Utility methods that assist in rendering <code>MenuItem</code>s.</p> 
 */
public final class MenuItemUtil {
  
  public static final String CSS_CLASS_NAME = "menuItem";

  private MenuItemUtil() {
    // prevent instantiation
  }
  
  ///////////////////
  // Rendering helper methods

  static void renderActiveItem( final HtmlResponseWriter out, 
                                final MenuItem menuItem ) 
    throws IOException 
  {
    out.startElement( HTML.A, null );
    out.writeAttribute( HTML.ID, menuItem.getUniqueID(), null );
    StringBuffer href;
    href = new StringBuffer( "javascript:menuBarHandler.resetActiveButton();" );
    href.append( RenderUtil.webActionPerformed( menuItem.getUniqueID() ) );
    out.writeAttribute( HTML.HREF, href.toString(), null );
    renderTitle( out, menuItem );
    out.writeAttribute( HTML.CLASS, CSS_CLASS_NAME, null );
    out.writeText( getLabel( menuItem ), null ); 
    out.endElement( HTML.A );
  }
  
  static void renderInactiveItem( final HtmlResponseWriter out, 
                                  final MenuItem menuItem ) 
    throws IOException 
  {
    out.startElement( HTML.A, null );
    out.writeAttribute( HTML.ID, menuItem.getUniqueID(), null );
    renderTitle( out, menuItem );
    out.writeAttribute( HTML.CLASS, CSS_CLASS_NAME, null );
    out.writeAttribute( HTML.STYLE, getMIDStyle( menuItem ).toString(), null );
    out.writeText( getLabel( menuItem ), null ); 
    out.endElement( HTML.A );
  }
  
  ///////////////////
  // Methods to reveal MenuItem properties 
  
  static boolean isActive( final MenuItem menuItem ) {
    return    menuItem.isEnabled()
           && WebActionEvent.hasListener( menuItem );
  }
  
  static MenuItemEnabledStyle getMIEStyle( final MenuItem menuItem ) {
    return getMenuBar( menuItem ).getItemEnabledStyle();
  }

  static MenuItemDisabledStyle getMIDStyle( final MenuItem menuItem ) {
    return getMenuBar( menuItem ).getItemDisabledStyle();
  }
  
  static String getLabel( final MenuItem menuItem ) {
    return RenderUtil.resolve( menuItem.getLabel() );    
  }

  static String getTitle( final MenuItem menuItem ) {
    return RenderUtil.resolve( menuItem.getTitle() );    
  }

  static MenuBar getMenuBar( final MenuItem menuItem ) {
    return ( MenuBar )menuItem.getParentNode().getParentNode();
  }

  ///////////////////
  // Private helper methods

  private static void renderTitle( final HtmlResponseWriter out,
                                   final MenuItem menuItem ) 
    throws IOException
  {
    String title = getTitle( menuItem );
    if( title != null && !"".equals( title ) ) {
      out.writeAttribute( HTML.TITLE, title, null );
    }
  }
}
