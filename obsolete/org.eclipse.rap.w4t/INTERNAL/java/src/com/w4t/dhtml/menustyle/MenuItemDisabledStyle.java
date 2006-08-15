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

/** <p>Encapsulates the attributes of the disabled menu item.</p>
  */
public class MenuItemDisabledStyle extends MenuLabeledProperties {

  
  /** <p>constructs a new MenuItemDisabledStyle with some 
    * default settings.</p> */
  public MenuItemDisabledStyle() {   
    setColor( createColor( DefaultColorScheme.MENU_DISABLED_FONT ) );
  }
}