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
package com.w4t.dhtml;

import com.w4t.WebComponent;
import com.w4t.dhtml.renderinfo.MenuButtonInfo;
import com.w4t.internal.adaptable.IRenderInfoAdapter;
import com.w4t.internal.adaptable.RenderInfoAdapter;


/** <p>This is used internally only. Use {@link org.eclipse.rwt.dhtml.MenuBar MenuBar}, 
  * {@link org.eclipse.rwt.custom.CMenu CMenu} 
  * and {@link org.eclipse.rwt.dhtml.MenuItem MenuItem}
  * to build menus.</p>
  * 
  * <p>encapsulates the part of a menu which is displayed on the MenuBar
  * and shows or hides the popup area of the menu on user click.</p>
  */
public class MenuButton extends WebComponent {
  
  private Menu menu;
  
  private boolean active = false;

  private Object renderInfoAdapter;
  
  /** <p>constructs a new MenuButton.</p> */
  public MenuButton() {
    // need a parameterless constructor here
  }
  
  /** <p>constructs a new MenuButton for the specified CMenu.</p> */
  public MenuButton( final Menu menu ) {
    this.menu = menu;
  }
  
  public Object getAdapter( final Class adapter ) {
    Object result;
    if( adapter == IRenderInfoAdapter.class ) {
      result = getRenderInfoAdapter();
    } else {
      result = super.getAdapter( adapter );      
    }
    return result;
  }

  private Object getRenderInfoAdapter() {
    if( renderInfoAdapter == null ) {
      renderInfoAdapter = new RenderInfoAdapter() {
        private MenuButtonInfo renderInfo;
        public Object getInfo() {
          return renderInfo;
        }
        public void createInfo() {
          renderInfo = new MenuButtonInfo( menu );
        }
      };
    }
    return renderInfoAdapter;
  }
  
  void setActive( final boolean active ) {
    this.active = active;
  }
  
  boolean isActive() {
    return active;
  }
  
  Menu getMenu() {
    return menu;
  }
}
