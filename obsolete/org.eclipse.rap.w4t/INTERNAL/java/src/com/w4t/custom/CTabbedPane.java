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
package com.w4t.custom;

import java.util.Vector;

import com.w4t.*;
import com.w4t.event.*;
import com.w4t.types.TabConfig;
import com.w4t.types.WebColor;

/** <p>A component that lets the user switch between a group of components 
 *  by clicking on a tab with a given title.</p>
 * 
 *  <p>CTable is based on the WebContainer component with the
 *  WebCardLayout as layout manager.</p>
 */
public class CTabbedPane extends WebContainer implements ICustomContainer {
  
  private int selectionIndex = -1;
  
  /** <p>Creates a new instance of CTabbedPane.</p> */
  public CTabbedPane() {
    super();
    final WebCardLayout layout = new WebCardLayout();
    setWebLayout( layout );
    addWebContainerListener( new WebContainerAdapter() {
      public void webComponentAdded( final WebContainerEvent evt ) {
        if( selectionIndex >= 0 && selectionIndex < getWebComponentCount() ) {
          String constraint = ( String )evt.getConstraint();
          if( constraint.equals( getConstraint( selectionIndex ) ) ) {
            layout.setDisplayCard( constraint );
          }
        }
      }
    } );
  }
  
  /** <p>returns a path to an image that represents this WebComponent
   *  (widget icon).</p> */
  public static String retrieveIconName() {
    return "resources/images/icons/tabbedpane.gif";
  }
 
  /** <p>sets the background color of this CTabbedPane.</p> */
  public void setBgColor( final WebColor bgColor ) {
    getLayout().setBgColor( bgColor );
  }

  /** <p>returns the background color of this CTabbedPane.</p> */
  public WebColor getBgColor() {
    return getLayout().getBgColor();
  } 
  
  /** <p>en-/disables the WebActionEvent on the tabs of this WebCardLayout 
   *  which is responsible for switching between the cards.</p> */
  public void setEnabled( final boolean enabled ) {
    getLayout().setEnabled( enabled );
  }

  /** <p>returns if the WebActionEvent of the tabs which is responsible 
   *  for switching between the cards is en-/disabled.</p> */
  public boolean isEnabled() {
    return getLayout().isEnabled();
  }
  
  /** <p>sets the background color of inactive tabs.</p> */
  public void setInactiveTabColor( final WebColor inactiveTabColor ) {
    getLayout().setInactiveTabColor( inactiveTabColor );
  }

  /** <p>returns the background color of inactive tabs.</p>*/
  public WebColor getInactiveTabColor() {
    return getLayout().getInactiveTabColor();
  }

  /** <p>sets the background color of the active tab and the 
   *  displayed card.</p>*/
  public void setCardColor( final WebColor cardColor ) {
    getLayout().setCardColor( cardColor );
  }

  /** <p>returns the html color of the displayed card.</p> */
  public WebColor getCardColor() {
    return getLayout().getCardColor();
  }

  /** <p>sets the style component of the inactive tabs.<p> */
  public void setInactiveTabStyle( final Style inactiveTabStyle ) {
    getLayout().setInactiveTabStyle( inactiveTabStyle );
  }

  /** <p>returns the style component of the inactive tabs.<p> */
  public Style getInactiveTabStyle() {
    return getLayout().getInactiveTabStyle();
  }

  /** <p>sets the style component of the active tab.</p> */
  public void setActiveTabStyle( final Style activeTabStyle ) {
    getLayout().setActiveTabStyle( activeTabStyle );
  }

  /** <p>returns the style component of the active tab.</p> */
  public Style getActiveTabStyle() {
    return getLayout().getActiveTabStyle();
  }

  /** <p>sets the style component of the displayed card.</p> */
  public void setCardStyle( final Style cardStyle ) {
    getLayout().setCardStyle( cardStyle );
  }

  /** <p>returns the style component of the active card.</p> */
  public Style getCardStyle() {
    return getLayout().getCardStyle();
  }

  /** <p>sets the style component of the active tab link.</p>*/
  public void setActiveTabLinkStyle( final Style style ) {
    getLayout().setActiveTabLinkStyle( style );
  }

  /** <p>returns the style component of the active tab link.</p> */
  public Style getActiveTabLinkStyle() {
    return getLayout().getActiveTabLinkStyle();
  }

  /** <p>sets the style component of the inactive tab link.></p> */
  public void setInactiveTabLinkStyle( final Style style ) {
    getLayout().setInactiveTabLinkStyle( style );
  }

  /** <p>returns the style component of the inactive tab link.</p> */
  public Style getInactiveTabLinkStyle() {
    return getLayout().getInactiveTabLinkStyle();
  }

  /** <p>returns the color of the stylistic border line. This applies only 
   *  if the TabConfig's type attribute is set to TabConfig.MODERN.</p> */
  public WebColor getBorderColor() {
    return getLayout().getBorderColor();
  }

  /** <p>sets the color of the stylistic border line. This applies only 
   *  when if TabConfig's type attribute is set to TabConfig.MODERN.</p> */
  public void setBorderColor( final WebColor borderColor ) {
    getLayout().setBorderColor( borderColor );
  }

  /** <p>returns the width of the stylistic border line. This applies only 
   *  if the TabConfig's type attribute is set to TabConfig.MODERN.</p> */
  public int getBorderWidth() {
    return getLayout().getBorderWidth();
  }

  /** <p>sets the width of the stylistic border line. This applies only 
   *  if the TabConfig's type attribute is set to TabConfig.MODERN.</p> */
  public void setBorderWidth( final int borderWidth ) {
    getLayout().setBorderWidth( borderWidth );
  }

  /** <p>sets the configuration object for alignment and style of the
   *  tabs.</p> */
  public void setTabConfig( final TabConfig tabConfig ) {
    getLayout().setTabConfig( tabConfig );
  }

  /** <p>returns the configuration object for alignment and style of the
   *  tabs.</p> */
  public TabConfig getTabConfig() {
    return getLayout().getTabConfig();
  }
  
  /** <p>sets the style type for this WebCardLayout. Must be one of
   *  the constants TabConfig.STYLE_MODERN or TabConfig.STYLE_CLASSIC.</p> */
  public void setType( final int type ) {
    getLayout().getTabConfig().setType( type );
  }
  
  /** <p>returns the default style type for this WebCardLayout.</p> */
  public int getType() {
    return getLayout().getTabConfig().getType();
  }
  
  
  /** <p>returns the zero based index of the active tab or -1 if
   *  this CTabbedPane does not contain a card.</p>*/
  public int getSelectionIndex() {
    for( int i = 0; i < getWebComponentCount(); i++ ) {
      String name = ( String )getConstraint( i );
      if( name.equals( getLayout().getDisplayCard() ) ) {
        selectionIndex = i;
      }
    } 
    return selectionIndex;
  }

  /** <p>sets the zero based index of the active which should get the
   *  active tab.</p>*/  
  public void setSelectionIndex( final int selectionIndex ) {
    if( selectionIndex >= 0 && selectionIndex < getWebComponentCount() ) {
      String newCardName = ( String )getConstraint( selectionIndex );
      getLayout().setDisplayCard( newCardName );
    }
    this.selectionIndex = selectionIndex;
  }

  // TODO [rh] JavaDoc necessary?
  public String getActiveTabLabel() {
    return getLayout().getDisplayCard();
  }
  
  // TODO [rh] JavaDoc necessary?
  public void setActiveTabLabel( final String activeTabLabel ) {
    if( activeTabLabel != null && !activeTabLabel.equals( "" ) ) {
      int index = getSelectionIndex();
      Vector componentBuffer = new Vector();
      Vector constraintBuffer = new Vector();
      for( int i = 0; i < getWebComponentCount(); i++ ) {
        componentBuffer.add( get( i ) );
        constraintBuffer.add( getConstraint( i ) );
      }
      removeAll();
      for( int i = 0; i < componentBuffer.size(); i++ ) {
        String name = ( String )constraintBuffer.get( i );
        if( index == i ) {
          name = activeTabLabel;
        }
        add( ( WebComponent )componentBuffer.get( i ), name );
      }
      setSelectionIndex( index );      
    }
  }
  
  public boolean isCardEnabledByComponent() {
    return getLayout().isCardEnabledByComponent();
  }
  
  public void setCardEnabledByComponent( final boolean cardEnabledByComponent ){
    getLayout().setCardEnabledByComponent( cardEnabledByComponent );
  }


  // event handling
  //////////////////

  /** <p>Adds the specified WebActionListener to receive action events from
   *  the selected tab. Action events occur when a user presses and releases
   *  the mouse over a tab.</p> */
  public void addWebActionListener( final WebActionListener l ) {
    getLayout().addWebActionListener( l );
  }

  /** <p>Removes the specified WebActionListener so that it no longer
   * receives action events from the selected tab. Action events occur
   * when a user presses and releases the mouse over a tab.</p> */
  public void removeWebActionListener( final WebActionListener l ){
    getLayout().removeWebActionListener( l );
  }
  

  /////////////////
  // helping method
  
  private WebCardLayout getLayout() {
    return ( WebCardLayout )getWebLayout();
  }
}