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


/** <p>encapsulates the attributes of the button which belongs to a menu 
  * and is displayed in the menu bar, when the mouse pointer is moved 
  * over it (not supported by all browsers).</p>
  */
public class MenuButtonHoverStyle extends MenuProperties {

  /** <p>constructs a new MenuButtonHoverStyle with some 
    * default settings.</p> */
  public MenuButtonHoverStyle() {
    setBorder( 1 );
    setBorderTopColor( createColor( DefaultColorScheme.MENU_BORDER ) );
    setBorderBottomColor( createColor( DefaultColorScheme.MENU_BORDER ) );
    setBorderLeftColor( createColor( DefaultColorScheme.MENU_BORDER ) );
    setBorderRightColor( createColor( DefaultColorScheme.MENU_BORDER ) );
    setColor( createColor( DefaultColorScheme.MENU_DEFAULT_FONT ) );
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
