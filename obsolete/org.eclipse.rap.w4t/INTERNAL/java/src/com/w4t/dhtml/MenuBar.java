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

import com.w4t.dhtml.menustyle.*;
import com.w4t.dhtml.renderinfo.MenuBarInfo;
import com.w4t.internal.adaptable.IRenderInfoAdapter;
import com.w4t.internal.adaptable.RenderInfoAdapter;


/** <p>This is an implementation of a menu bar similar to
  * other gui frameworks. To construct a menu control add Menu objects into the
  * MenuBar object. Each menu object itself contains MenuItem objects. In the
  * MenuBar the Menu is represented as a hover button (hover effect is
  * not supported by all browsers), which can be selected via mouse click.
  * If so the selected menu pops up and the menus items are displayed.</p>
  */
public class MenuBar extends Node {

  /** the menu bar properties */
  private MenuBarStyle menuBarStyle;
  /** the properties of a selected menu representation button */
  private MenuButtonActiveStyle buttonActiveStyle;
  /** the properties of a disabled menu representation button */
  private MenuButtonDisabledStyle buttonDisabledStyle;
  /** the properties of a enabled menu representation button */  
  private MenuButtonEnabledStyle buttonEnabledStyle;
  /** the properties of a menu representation button on mouseover */  
  private MenuButtonHoverStyle buttonHoverStyle;
  /** the properties of a menuItem on mouseover */  
  private MenuItemHoverStyle itemHoverStyle;
  /** the properties of an enabled menuItem */
  private MenuItemEnabledStyle itemEnabledStyle;
  /** the properties of a disabled menuItem */
  private MenuItemDisabledStyle itemDisabledStyle;
  
  /** the properties of a popup menu */
  private MenuPopupStyle menuPopupStyle;
  private Object renderInfoAdapter;
  
  
  /** <p>constructs a new MenuBar.</p> */
  public MenuBar() {
    super();
    init();
  }
  
  private void init() {
    menuBarStyle = new MenuBarStyle();
    buttonActiveStyle = new MenuButtonActiveStyle();
    buttonDisabledStyle = new MenuButtonDisabledStyle();
    buttonEnabledStyle = new MenuButtonEnabledStyle();
    buttonHoverStyle = new MenuButtonHoverStyle();
    itemHoverStyle = new MenuItemHoverStyle();
    itemEnabledStyle = new MenuItemEnabledStyle();  
    itemDisabledStyle = new MenuItemDisabledStyle();  
    menuPopupStyle = new MenuPopupStyle();
  }
  
  /** returns a copy of MenuBar. */
  public Object clone() throws CloneNotSupportedException {
    MenuBar result = ( MenuBar )super.clone();
    result.init();
    return result;
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
        private MenuBarInfo renderInfo;
        public Object getInfo() {
          return renderInfo;
        }
        public void createInfo() {
          Node[] menus = getNodes();
          MenuButton[] menuButtons = new MenuButton[ menus.length ];
          for( int i = 0; i < menus.length; i++ ) {
            menuButtons[ i ] = ( ( Menu )menus[ i ] ).getMenuButton();
          }
          renderInfo = new MenuBarInfo( menuButtons );
        }
      };
    }
    return renderInfoAdapter;
  }
  
  /** <p>adds the specified item to this MenuBar.</p> */
  public void addItem( final Item item ) {
    if( !( item instanceof Menu )  ) {
      String msg = "Cannot add a component which isn't a Menu to the MenuBar.";
      throw new IllegalArgumentException( msg );
    }
    super.addItem( item );
  }  
  
  /** sets the menu bar properties */
  public void setMenuBarStyle( final MenuBarStyle menuBarStyle ) {
    this.menuBarStyle = menuBarStyle;
  }
  
  /** returns the menu bar properties */  
  public MenuBarStyle getMenuBarStyle() {
    return menuBarStyle;
  }
   
  /** sets the properties of a selected menu representation button */
  public void setButtonActiveStyle( 
                               final MenuButtonActiveStyle buttonActiveStyle ) {
    this.buttonActiveStyle = buttonActiveStyle;
  }
  
  /** returns the properties of a selected menu representation button */  
  public MenuButtonActiveStyle getButtonActiveStyle() {
    return buttonActiveStyle;
  }
  
  /** sets the properties of a disabled menu representation button */
  public void setButtonDisabledStyle( 
                           final MenuButtonDisabledStyle buttonDisabledStyle ) {
    this.buttonDisabledStyle = buttonDisabledStyle;
  }
  
  /** returns the properties of a disabled menu representation button */    
  public MenuButtonDisabledStyle getButtonDisabledStyle() {
    return buttonDisabledStyle;
  }
  
  /** sets the properties of a enabled menu representation button */  
  public void setButtonEnabledStyle( 
                             final MenuButtonEnabledStyle buttonEnabledStyle ) {
    this.buttonEnabledStyle = buttonEnabledStyle;
  }
  
  /** returns the properties of a enabled menu representation button */  
  public MenuButtonEnabledStyle getButtonEnabledStyle() {  
    return buttonEnabledStyle;
  }
  
  /** sets the properties of a menu representation button on mouseover */  
  public void setButtonHoverStyle( 
                                 final MenuButtonHoverStyle buttonHoverStyle ) {
    this.buttonHoverStyle = buttonHoverStyle;
  }
  
  /** returns the properties of a menu representation button on mouseover */  
  public MenuButtonHoverStyle getButtonHoverStyle() {
    return buttonHoverStyle;
  }
  
  /** sets the properties of a menuItem on mouseover */  
  public void setItemHoverStyle( 
                                     final MenuItemHoverStyle itemHoverStyle ) {
    this.itemHoverStyle = itemHoverStyle;
  }
  
  /** returns the properties of a menuItem on mouseover */  
  public MenuItemHoverStyle getItemHoverStyle() {
    return itemHoverStyle;
  }
  
  /** sets the properties of an enabled menuItem */
  public void setItemEnabledStyle( 
                                 final MenuItemEnabledStyle itemEnabledStyle ) {
    this.itemEnabledStyle = itemEnabledStyle;
  }
  
  /** returns the properties of an enabled menuItem */
  public MenuItemEnabledStyle getItemEnabledStyle() {
    return itemEnabledStyle;
  }

  /** sets the properties of a disabled menuItem */
  public void setItemDisabledStyle( 
                               final MenuItemDisabledStyle itemDisabledStyle ) {
    this.itemDisabledStyle = itemDisabledStyle;
  }
  
  /** returns the properties of a disabled menuItem */
  public MenuItemDisabledStyle getItemDisabledStyle() {
    return itemDisabledStyle;
  }
  
  
  /** sets the properties of a popup menu */  
  public void setMenuPopupStyle( final MenuPopupStyle menuPopupStyle ) {
    this.menuPopupStyle = menuPopupStyle;
  }
  
  /** returns the properties of a popup menu */  
  public MenuPopupStyle getMenuPopupStyle() {
    return menuPopupStyle;
  }
  
  public static String retrieveIconName() {
    return "resources/images/icons/menubar.gif";
  }  
}