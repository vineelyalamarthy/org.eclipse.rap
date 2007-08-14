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

import org.eclipse.rwt.internal.browser.Browser;

import com.w4t.WebComponent;
import com.w4t.dhtml.Menu;
import com.w4t.dhtml.MenuItemSeparator;


/** <p>The renderer for 
  * {@link org.eclipse.rwt.dhtml.MenuItemSeparator MenuItemSeparator} on Netscape 
  * Navigator 6 and higher without javascript support.</p>
  */
public class MenuItemSeparatorRenderer_Mozilla1_6up_Noscript 
  extends MenuItemSeparatorRenderer_Default_Script
{

  public void render( final WebComponent component ) throws IOException {
    MenuItemSeparator mis = ( MenuItemSeparator )component;
    if( hasAbsolutePosition( mis ) ) {
      renderHorizontalSeparator();
    } else {
      renderVerticalSeparator();
    }
  }
                       
  private boolean hasAbsolutePosition( final MenuItemSeparator mis ) {
    Menu menu = ( Menu )mis.getParentNode();
    return menu.getAbsolutePosition( Browser.NAVIGATOR_6_UP ) != null;
  }
}