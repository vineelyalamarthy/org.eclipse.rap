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

import com.w4t.event.*;


/** <p>A Menu belongs to a dropdown menu and contains of a popup area that 
  * is visible when the user clicks the menu.</p>
  */
public class Menu extends Node {

  /** <p>whether this Menu is expanded.</p> */
  private boolean expanded;
  /** <p>contains the absolute position for this Menu, which can be set 
    * per browser</p> */
  private MenuPopupPosition popupPosition;
  /** <p>the part of the menu that reacts on user input to show or hide 
    * this Menu.</p> */
  private MenuButton menuButton;
  

  /** <p>constructs a new Menu.</p> */
  public Menu() {
    init();
  } 

  /** <p>constructs a new Menu with the specified text displayed on it.</p> */
  public Menu( final String label ) {
    this();
    this.label = label;
  }
  
  private void init() {
    menuButton = new MenuButton( this );
    popupPosition = new MenuPopupPosition();
    
    this.addWebActionListener( new WebActionListener() {
      public void webActionPerformed( final WebActionEvent evt ) {
        Menu menu = ( Menu )evt.getSource();
        if( !menu.getMenuButton().isActive() ) {
          menu.setExpanded( true );
        } else {
          menu.setExpanded( false );
        }
      }
    } );

    this.addWebRenderListener( new WebRenderAdapter() {      
      public void beforeRender( final WebRenderEvent evt ) {
        if( menuButton.isActive() ) {
          menuButton.getMenu().setExpanded( false );
        }
      }
      public void afterRender( final WebRenderEvent evt ) {
        Menu menu = ( Menu )evt.getSource();        
        if( menu.isExpanded() ) {
          menu.getMenuButton().setActive( true );
        } else {
          menu.getMenuButton().setActive( false );
        }
      }
    } );
  }  
  
  /** returns a copy of Menu. */
  public Object clone() throws CloneNotSupportedException {
    Menu result = ( Menu )super.clone();
    result.init();
    return result;
  }
  

  MenuButton getMenuButton() {
    return menuButton;
  }
  
  // attribute getters and setters
  ////////////////////////////////
  
  /** <p>sets whether this Menu is expanded (whether sub-menus are 
    * shown).</p> */
  public void setExpanded( final boolean expanded ) {
    this.expanded = expanded;
  }

  /** <p>returns whether this Menu is expanded (whether sub-menus are 
    * shown).</p> */
  public boolean isExpanded() {
    return expanded;
  }

  /** <p>gets, whether this menu is enabled (reacts on user input)
    * or not. A menu is also not enabled if it is added to a
    * other node or a container and the node/container to which it is added is
    * disabled.</p> */
  public boolean isEnabled() {
    boolean isEnabled = false;

    Node parentNode = getParentNode();
    if( parentNode == null ) {
      isEnabled = super.isEnabled();
    } else {
      if( super.isEnabled() ) {
        isEnabled = parentNode.isEnabled();
      }
    }
    return isEnabled;
  }

  /** <p>adds the passed position  as absolute position of this Menu for the 
    * specified browser. Absolute positions are specified for each browser 
    * type separately (as most browsers handle them differently).</p>
    *
    * <p>Note that it is not always (especially on more recent browser 
    * versions, i.e. Internet Explorer 5 and higher, Netscape 6 and higher)
    * necessary to set an absolute position for a Menu.</p>
    *
    * @param   browserName   the name of the browser for which the absolute
    *                        position is to be set. Must be one of the 
    *                        browser name constants of org.eclipse.rap.Browser.
    * @param   position      the absolute position to be set for the 
    *                        specified browser.
    */
  public void addAbsolutePosition( final String browserName, 
                                   final Point position ) {
    popupPosition.add( browserName, position );
  }

  /** <p>removes the absolute position which was set on this Menu for 
    * the specified browser. Absolute positions are specified for 
    * each browser type separately (as most browsers handle them 
    * differently).</p>
    *
    * <p>Note that it is not always (especially on more recent browser 
    * versions, i.e. Internet Explorer 5 and higher, Netscape 6 and higher)
    * necessary to set an absolute position for a Menu.</p>
    *
    * @param   browserName   the name of the browser for which the absolute
    *                        position is removed. Must be one of the 
    *                        browser name constants of org.eclipse.rap.Browser.
    */
  public void removeAbsolutePosition( final String browserName ) {
    popupPosition.remove( browserName );
  }
  
  /** <p>returns the absolute position of this Menu which was set for the 
    * specified browser. Absolute positions are specified for each browser 
    * type separately (as most browsers handle them differently).</p>
    *
    * <p>Note that it is not always (especially on more recent browser 
    * versions, i.e. Internet Explorer 5 and higher, Netscape 6 and higher)
    * necessary to set an absolute position for a Menu.</p>
    *
    * @param   browserName   the name of the browser for which the absolute
    *                        position is retrieved. Must be one of the 
    *                        browser name constants of org.eclipse.rap.Browser.
    * @return                a Point object which contains the absolute 
    *                        position for the specified browser, or null if
    *                        no position for that browser was set.
    */
  public Point getAbsolutePosition( final String browserName ) {
    return popupPosition.get( browserName );
  }
  
  /** adds a new {@link MenuItemSeparator MenuItemSeparator} to this Menu.
    * This is a convenience method and equivalent to 
    * addItem( new MenuItemSeparator() ). */
  public void addSeparator() {
    addItem( new MenuItemSeparator() );
  }
}