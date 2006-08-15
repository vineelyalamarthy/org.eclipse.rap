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
  * and is displayed in the menu bar, when it is pressed down (when  
  * the corresponding menu is expanded).</p>
  */
public class MenuButtonActiveStyle extends MenuLabeledProperties {

  /** the margin of a displayed Button*/
  public static final int BUTTON_MARGIN = 1;

  /** <p>constructs a new MenuButtonActiveStyle with some 
    * default settings.</p> */
  public MenuButtonActiveStyle() {
    setBorder( 1 );
    setPadding( 2 );
    setBgColor( createColor( DefaultColorScheme.MENU_BG ) );
    setColor( createColor( DefaultColorScheme.MENU_HOVER_FONT ) );
    setBorderBottomColor( createColor( DefaultColorScheme.MENU_BORDER ) );
    setBorderTopColor( createColor( DefaultColorScheme.MENU_BORDER ) );
    setBorderLeftColor( createColor( DefaultColorScheme.MENU_BORDER ) );
    setBorderRightColor( createColor( DefaultColorScheme.MENU_BORDER ) );
        
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
