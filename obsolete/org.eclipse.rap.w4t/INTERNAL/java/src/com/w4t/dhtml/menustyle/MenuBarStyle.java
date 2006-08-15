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
package com.w4t.dhtml.menustyle;

import com.w4t.util.DefaultColorScheme;


/** <p>encapsulates the style attributes of the MenuBar (as border 
  * settings etc.)</p>
  */
public class MenuBarStyle extends MenuProperties {

  /** <p>constructs a new MenuBarStyle with some default values.</p> */
  public MenuBarStyle() {
    setBorder( 0 );
    setBgColor( createColor( DefaultColorScheme.MENU_BG ) );
    setPadding( 4 );
    style.setFontSize( 1 );
    style.setTextAlign( "left" );
    style.setWidth( "100%" );        
    style.setColor( createColor( DefaultColorScheme.MENU_DEFAULT_FONT ) );
  }

  /** sets the style attribute width */
  public void setWidth( final String width ) {
    style.setWidth( width );
  }

  /** gets the style attribute width */
  public String getWidth() {
    return style.getWidth();
  }    
}