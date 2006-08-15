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

import com.w4t.types.WebTriState;
import com.w4t.util.DefaultColorScheme;


/** <p>encapsulates the attributes of the area of the menu on which the 
  * menu items are placed.</p>
  */
public class MenuPopupStyle extends MenuProperties {
  
  /** <p>constructs a new MenuPopupStyle with some default settings.</p> */
  public MenuPopupStyle() {
    setBgColor( createColor( DefaultColorScheme.MENU_BG ) );
    setBorder( 1 );
    setBorderTopColor( createColor( DefaultColorScheme.MENU_BORDER ) );
    setBorderLeftColor( createColor( DefaultColorScheme.MENU_BORDER ) );
    setBorderBottomColor( createColor( DefaultColorScheme.MENU_BORDER ) );
    setBorderRightColor( createColor( DefaultColorScheme.MENU_BORDER ) );
    setPadding( 0 );
    
    style.setPosition( "absolute" );
    style.setTextAlign( "left" );    
    style.setVisibility( new WebTriState( "hidden" ) );  
    style.setZIndex( "99999" );
  }

  
  public void setVisibility( WebTriState visibility ) {
    style.setVisibility( visibility );
  }
  
  public WebTriState getVisibility() {
    return style.getVisibility();
  }
}
