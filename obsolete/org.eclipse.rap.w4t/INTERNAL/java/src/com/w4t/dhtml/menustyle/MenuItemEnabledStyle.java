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

/** <p>encapsulates the attributes of the enabled menu item.</p>
  */
public class MenuItemEnabledStyle extends MenuLabeledProperties {

  
  /** <p>constructs a new MenuItemEnabledStyle with some 
    * default settings.</p> */
  public MenuItemEnabledStyle() {   
    setColor( createColor( DefaultColorScheme.MENU_DEFAULT_FONT ) );
    setPadding( 2 );
    style.setDisplay( "block" );
    style.setMargin( "0px" );    
    style.setTextDecoration( "none" );
    style.setWhiteSpace( "nowrap" );
    style.setCursor( "default" );
  }
  
  /** sets the style attribute padding */
  public void setPadding( final int padding ) {
    String topOrBottom = String.valueOf( padding ) + "px";
    String leftOrRight = String.valueOf( padding * 6 ) + "px";
    style.setPadding( topOrBottom + " "
                    + leftOrRight + " " 
                    + topOrBottom + " "
                    + leftOrRight );
  }
}