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

/** <p>encapsulates the attributes of the button which belongs to a menu 
  * and is displayed in the menu bar.</p>
  */
public class MenuButtonEnabledStyle extends MenuLabeledProperties {

  /** the margin of a displayed Button*/
  public static final int BUTTON_MARGIN = 1;
  
  /** <p>constructs a new MenuButtonEnabledStyle with some 
    * default settings.</p> */
  public MenuButtonEnabledStyle() {
    setBorder( 1 );
    setPadding( 2 );
    setColor( createColor( DefaultColorScheme.MENU_DEFAULT_FONT ) );
    setBorderTopColor( createColor( DefaultColorScheme.MENU_BG ) );
    setBorderLeftColor( createColor( DefaultColorScheme.MENU_BG ) );
    setBorderBottomColor( createColor( DefaultColorScheme.MENU_BG ) );
    setBorderRightColor( createColor( DefaultColorScheme.MENU_BG ) );
    
    style.setMargin( BUTTON_MARGIN + "px" );
    style.setPosition( "relative" );
    style.setTextDecoration( "none" );    
    style.setCursor( "default" );    
  }
  
  
  // attribute getters and setters
  ////////////////////////////////
  
  /** sets the style attribute padding */
  public void setPadding( final int padding ) {
    String topOrBottom = String.valueOf( padding ) + "px";
    String leftOrRight = String.valueOf( padding * 3 ) + "px";
    style.setPadding( topOrBottom + " "
                    + leftOrRight + " " 
                    + topOrBottom + " "
                    + leftOrRight );
  }
}
