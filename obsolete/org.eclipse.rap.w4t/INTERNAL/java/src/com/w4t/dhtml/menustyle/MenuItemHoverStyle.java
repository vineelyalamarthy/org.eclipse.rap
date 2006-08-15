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

import com.w4t.Style;
import com.w4t.types.WebColor;
import com.w4t.util.DefaultColorScheme;

/** <p>encapsulates the attributes of the menu item, when the mouse 
  * pointer is moved over it (not supported by all browsers).</p>
  */
public class MenuItemHoverStyle extends MenuProperties {
  
  /** <p>constructs a new MenutemHoverStyle with some 
    * default settings.</p> */
  public MenuItemHoverStyle() {   
    setBgColor( createColor( DefaultColorScheme.MENU_BG ) );
    setColor( createColor( DefaultColorScheme.MENU_HOVER_FONT ) );
    style.setFontSize( Style.NOT_USED );
    style.setFontFamily( "" );    
  }
    
  /** sets the style attribute color */
  public void setColor( WebColor color ) {
    style.setColor( color );
  }
  
  /** gets the style attribute color */
  public WebColor getColor() {
    return style.getColor();
  }  
}
