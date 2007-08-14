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
package com.w4t.dhtml.renderinfo;

import com.w4t.dhtml.Menu;


/** <p>contains the information that is needed in the Renderer for 
  * MenuButton.</p>
  * <p>Note that render info objects should <strong>always</strong> be 
  * <strong>readonly</strong>.</p>
  * @see org.eclipse.rwt.dhtml.menubuttonkit.MenuButtonRenderer MenuButtonRenderer
  */
public class MenuButtonInfo {

  private Menu menu;
  
  
  public MenuButtonInfo( final Menu menu ) {
    this.menu = menu;
  }
  
  public Menu getMenu() {
    return menu;
  }
}
