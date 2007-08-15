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
package com.w4t;

import java.io.IOException;

import org.eclipse.rwt.internal.events.EventAdapter;
import org.eclipse.rwt.internal.events.IEventAdapter;

import com.w4t.event.WebActionEvent;
import com.w4t.event.WebActionListener;
import com.w4t.internal.adaptable.IRenderInfoAdapter;
import com.w4t.internal.adaptable.RenderInfoAdapter;
import com.w4t.renderinfo.CardLayoutRenderInfo;
import com.w4t.types.TabConfig;
import com.w4t.types.WebColor;
import com.w4t.util.DefaultColorScheme;


/** <p>A WebCardLayout treats each WebComponent in the WebContainer to
  * which this layout belongs as a card.</p>
  * <p>Only one card is visible at a time, and the container acts as
  * a stack of cards. The ordering of cards is determined by the
  * order WebComponents added to the WebContainer. Adding a
  * WebComponent to the WebContainer the Constraint to use
  * is a String which specifies the name of the card (this name
  * has to be unique).</p>
  * <p>A selection bar which contains an tab for each card
  * is shown at the top (default), left, right or bottom of the
  * WebContainer.</p>
  */
public class WebCardLayout extends WebTable implements WebLayout {

  /** selection bar at the top with alignment left */
  public final static int H_TOP_ALIGN_LEFT      = 0;
  /** selection bar at the top with alignment center */
  public final static int H_TOP_ALIGN_CENTER    = 1;
  /** selection bar at the top with alignment right */
  public final static int H_TOP_ALIGN_RIGHT     = 2;
  /** selection bar at the bottom with alignment left */
  public final static int H_BOTTOM_ALIGN_LEFT   = 3;
  /** selection bar at the bottom with alignment center */
  public final static int H_BOTTOM_ALIGN_CENTER = 4;
  /** selection bar at the bottom, with alignment right */
  public final static int H_BOTTOM_ALIGN_RIGHT  = 5;
  /** selection bar at the left with vertical alignment top */
  public final static int V_LEFT_ALIGN_TOP      = 6;
  /** selection bar at the left with vertical alignment middle */
  public final static int V_LEFT_ALIGN_MIDDLE   = 7;
  /** selection bar at the left with vertical alignment bottom */
  public final static int V_LEFT_ALIGN_BOTTOM   = 8;
  /** selection bar at the right with vertical alignment top */
  public final static int V_RIGHT_ALIGN_TOP     = 9;
  /** selection bar at the right with vertical alignment middle */
  public final static int V_RIGHT_ALIGN_MIDDLE  = 10;
  /** selection bar at the right with vertical alignment bottom */
  public final static int V_RIGHT_ALIGN_BOTTOM  = 11;

    // TODO: replace the position member by more differenced settings
  /** used for the positioning of the selection bar
   * 
   * @deprecated
   */
  private int position = H_TOP_ALIGN_LEFT;
  
  
  /** contains configuration info such as where the tabs are placed 
   *  (left, right, top, bottom), how they are aligned etc. */
  private TabConfig tabConfig;

  /** the card which is in front */
  private String displayCard = "";

  /** the html color of the displayed card */
  private WebColor cardColor;
  /** style editor of the displayed card */
  private Style cardStyle = new Style();
  /** style editor of the active tab */
  private Style activeTabStyle = new Style();
  /** style editor of the active tab link */
  private Style activeTabLinkStyle = new Style();

  /** the html color of the inactiv tabs */
  private WebColor inactiveTabColor;
  /** style editor of the inactive tabs */
  private Style inactiveTabStyle = new Style();
  /** style editor of the inactive tab link */
  private Style inactiveTabLinkStyle = new Style();

  /** <p>the color of the stylistic border line. This applies only if the
   *  TabConfig's type attribute is set to TabConfig.MODERN.</p> */
  private WebColor borderColor;
  /** <p>the width of the stylistic border line. This applies only if the
   *  TabConfig's type attribute is set to TabConfig.MODERN.</p> */
  private int borderWidth;

  /** tells, if the WebActionEvent of the tabs are en-/disabled */
  private boolean enabled = true;

  private boolean cardEnabledByComponent;
  
  /** the WebContainer which is layouted by this WebBorderLayout. This is 
    * only temporarily set during rendering. */
  private WebContainer parent = null;
  private Object renderInfoAdapter;
  private IEventAdapter eventAdapter;
  
  
  /** Constructor */
  public WebCardLayout() {
    setBgColor( createColor( DefaultColorScheme.WEB_OBJECT_BG ) );
    String itColor = DefaultColorScheme.CARD_LAYOUT_INACTIVE_TAB;
    setInactiveTabColor( createColor( itColor ) );
    setCardColor( createColor( DefaultColorScheme.WEB_OBJECT_BG ) );
    String atlColor = DefaultColorScheme.CARD_LAYOUT_ACTIVE_TAB_LINK;
    getActiveTabLinkStyle().setColor( createColor( atlColor ) );
    String itlColor = DefaultColorScheme.CARD_LAYOUT_INACTIVE_TAB_LINK;
    getInactiveTabLinkStyle().setColor( createColor( itlColor ) );
    getActiveTabLinkStyle().setTextDecoration( "none" );
    getActiveTabLinkStyle().setFontWeight( "bold" );
    getActiveTabLinkStyle().setLineHeight( "20px" );
    getInactiveTabLinkStyle().setTextDecoration( "none" );

    // defaults for bordered style
    borderWidth = 1;
    borderColor = createColor( DefaultColorScheme.CARD_LAYOUT_BORDER );
    
    tabConfig = new TabConfig();
  }

  /** <p>checks if the constraint parameter in which was use in the
    * {@link org.eclipse.rwt.WebContainer#add(WebComponent,Object) 
    * add(WebComponent,Object)} method of a WebContainer has 
    * the correct type.</p>
    */
  public boolean checkConstraint( final Object constraint ) {
    return constraint instanceof String;
  }

  /** <p>returns a clone of this WebCardayout.</p>
    * <p>Cloning a WebLayout involves a copy of all settings and inits, but no
    * cloning or copying added components
    * ( see @link WebContainer.clone() ).</p> */
  public Object clone() throws CloneNotSupportedException {
    WebCardLayout clone = ( WebCardLayout )super.clone();

    // inits
    clone.cardStyle = ( Style )this.cardStyle.clone();
    clone.cardColor = ( WebColor )this.cardColor.clone();

    clone.activeTabStyle = ( Style )this.activeTabStyle.clone();
    clone.activeTabLinkStyle = ( Style )this.activeTabLinkStyle.clone();

    clone.inactiveTabColor = ( WebColor )this.inactiveTabColor.clone();
    clone.inactiveTabStyle = ( Style )this.inactiveTabStyle.clone();
    clone.inactiveTabLinkStyle = ( Style )this.inactiveTabLinkStyle.clone();

    return clone;
  }

  public Object getAdapter( final Class adapter ) {
    Object result;
    if( adapter == IRenderInfoAdapter.class ) {
      result = getRenderInfoAdapter();
    } else if( adapter == IEventAdapter.class ) {
      if( eventAdapter == null ) {
        eventAdapter = new EventAdapter();
      }
      result = eventAdapter;
    } else {
      result = super.getAdapter( adapter );      
    }
    return result;
  }

  private Object getRenderInfoAdapter() {
    if( renderInfoAdapter == null ) {
      renderInfoAdapter = new RenderInfoAdapter() {
        private CardLayoutRenderInfo renderInfo;
        public Object getInfo() {
          return renderInfo;
        }
        public void createInfo() {
          renderInfo = new CardLayoutRenderInfo( parent );
        }
      };
    }
    return renderInfoAdapter;
  }
  
  /** <p>creates the layout for the WebContainer.</p>
    *
    * @param parent the WebContainer which uses this layout manager */
  public void layoutWebContainer( final WebContainer parent ) throws IOException {
    LifeCycleHelper.render( this, parent );
  }
  
  
  // attribute getters and setters
  ////////////////////////////////
  
  /** <p>returns the area specified in the constraints object. Used e.g. for
    * setting the format of the Area.</p>
    * @param constraints specifies the Area
    */
  public Area getArea( final Object constraints ) {
    return null;
  }

  /**
    * gets the region specified in the
    * constraints object. used e.g. for setting the format
    * of the region
    * @param constraints specifies the region
    * @deprecated replaced by {@link #getArea(Object)}
    */
  public WebTableCell getRegion( final Object constraints ) {
    return null;
  }

  /** sets the name of the card which will be displayed next */
  public void setDisplayCard( final String displayCard ) {
    this.displayCard = displayCard;
  }

  /** <p>gets the name of the active card</p> */
  public String getDisplayCard() {
    return displayCard;
  }

  /** <p>en-/disables the WebActionEvent on the tabs of this 
   * WebCardLayout.</p> */
  public void setEnabled( final boolean enabled ) {
    this.enabled = enabled;
  }

  /** <p>returns if the WebActionEvent of the tabs are en-/disabled.</p> */
  public boolean isEnabled() {
    return enabled;
  }


  // event handling
  //////////////////

  /** Adds the specified WebActionListener to receive action events from
    * the selected tab. Action events occur when a user presses and releases
    * the mouse over a tab.
    * @param listener the WebActionListener
    */
  public void addWebActionListener( final WebActionListener listener ) {
    WebActionEvent.addListener( this, listener );
  }

  /** Removes the specified WebActionListener so that it no longer
    * receives action events from the selected tab. Action events occur
    * when a user presses and releases the mouse over a tab.
    * @param listener the WebActionListener
    */
  public void removeWebActionListener( final WebActionListener listener ) {
    WebActionEvent.removeListener( this, listener );
  }


  // styles
  /////////

  /** sets the html color of the inactive tabs */
  public void setInactiveTabColor( final WebColor inactiveTabColor ) {
    this.inactiveTabColor = inactiveTabColor;
  }

  /** gets the html color of the inactive tabs */
  public WebColor getInactiveTabColor() {
    return inactiveTabColor;
  }

  /** sets the html color of the displayed card */
  public void setCardColor( final WebColor cardColor ) {
    this.cardColor = cardColor;
  }

  /**gets the html color of the displayed card */
  public WebColor getCardColor() {
    return cardColor;
  }

  /** sets the style editor of the inactive tabs */
  public void setInactiveTabStyle( final Style inactiveTabStyle ) {
    this.inactiveTabStyle = inactiveTabStyle;
  }

  /** gets the style editor of the inactive tabs */
  public Style getInactiveTabStyle() {
    return inactiveTabStyle;
  }

  /** sets the style editor of the active tab */
  public void setActiveTabStyle( final Style activeTabStyle ) {
    this.activeTabStyle = activeTabStyle;
  }

  /** gets the style editor of the active tab */
  public Style getActiveTabStyle() {
    return activeTabStyle;
  }

  /** sets the style editor of the displayed card */
  public void setCardStyle( final Style cardStyle ) {
    this.cardStyle = cardStyle;
  }

  /** gets the style editor of the active card */
  public Style getCardStyle() {
    return cardStyle;
  }

  /** sets the style editor of the active tab link */
  public void setActiveTabLinkStyle( final Style style ) {
    this.activeTabLinkStyle = style;
  }

  /** gets the style editor of the active tab link */
  public Style getActiveTabLinkStyle() {
    return activeTabLinkStyle;
  }

  /** sets the style editor of the inactive tab link */
  public void setInactiveTabLinkStyle( final Style style ) {
    this.inactiveTabLinkStyle = style;
  }

  /** gets the style editor of the inactive tab link */
  public Style getInactiveTabLinkStyle() {
    return inactiveTabLinkStyle;
  }

  /** <p>returns the color of the stylistic border line. This applies only 
   *  if the TabConfig's type attribute is set to TabConfig.MODERN.</p> */
  public WebColor getBorderColor() {
    return borderColor;
  }

  /** <p>sets the color of the stylistic border line. This applies only 
   *  when if TabConfig's type attribute is set to TabConfig.MODERN.</p> */
  public void setBorderColor( final WebColor borderColor ) {
    this.borderColor = borderColor;
  }

  /** <p>returns the width of the stylistic border line. This applies only 
   *  if the TabConfig's type attribute is set to TabConfig.MODERN.</p> */
  public int getBorderWidth() {
    return borderWidth;
  }

  /** <p>sets the width of the stylistic border line. This applies only 
   *  if the TabConfig's type attribute is set to TabConfig.MODERN.</p> */
  public void setBorderWidth( final int borderWidth ) {
    this.borderWidth = borderWidth;
  }
  
  public boolean isCardEnabledByComponent() {
    return cardEnabledByComponent;
  }
  
  public void setCardEnabledByComponent( final boolean cardEnabledByComponent ){
    this.cardEnabledByComponent = cardEnabledByComponent;
  }

  
  // configuration settings
  /////////////////////////

  public void setTabConfig( final TabConfig tabConfig ) {
    this.tabConfig = tabConfig;
  }

  public TabConfig getTabConfig() {
    return tabConfig;
  }


  // the following are old and deprecated

  public TabConfig updateConfig( final int position ) {
    String pos   = TabConfig.POSITION_TOP;
    String align = TabConfig.ALIGN_LEFT ;
    switch( position ) {    
      case H_TOP_ALIGN_LEFT: 
        pos = TabConfig.POSITION_TOP;
        align = TabConfig.ALIGN_LEFT; 
        break;
      case H_TOP_ALIGN_CENTER:
        pos = TabConfig.POSITION_TOP;
        align = TabConfig.ALIGN_CENTER; 
        break;    
      case H_TOP_ALIGN_RIGHT:
        pos = TabConfig.POSITION_TOP;
        align = TabConfig.ALIGN_RIGHT; 
        break;    
      case H_BOTTOM_ALIGN_LEFT:
        pos = TabConfig.POSITION_BOTTOM;
        align = TabConfig.ALIGN_LEFT; 
        break;    
      case H_BOTTOM_ALIGN_CENTER:
        pos = TabConfig.POSITION_BOTTOM;
        align = TabConfig.ALIGN_CENTER; 
        break;    
      case H_BOTTOM_ALIGN_RIGHT:
        pos = TabConfig.POSITION_BOTTOM;
        align = TabConfig.ALIGN_RIGHT; 
        break;    
      case V_LEFT_ALIGN_TOP:
        pos = TabConfig.POSITION_LEFT;
        align = TabConfig.ALIGN_TOP; 
        break;    
      case V_LEFT_ALIGN_MIDDLE:
        pos = TabConfig.POSITION_LEFT;
        align = TabConfig.ALIGN_MIDDLE; 
        break;    
      case V_LEFT_ALIGN_BOTTOM:
        pos = TabConfig.POSITION_LEFT;
        align = TabConfig.ALIGN_BOTTOM; 
        break;    
      case V_RIGHT_ALIGN_TOP:
        pos = TabConfig.POSITION_RIGHT;
        align = TabConfig.ALIGN_TOP; 
        break;    
      case V_RIGHT_ALIGN_MIDDLE:
        pos = TabConfig.POSITION_RIGHT;
        align = TabConfig.ALIGN_MIDDLE; 
        break;    
      case V_RIGHT_ALIGN_BOTTOM:
        pos = TabConfig.POSITION_RIGHT;
        align = TabConfig.ALIGN_BOTTOM; 
    }    
    return new TabConfig( tabConfig.getType(), pos, align );
  }

  /** <p>sets the alignment of the selection bar of this WebCardLayout by
    * using one of the constants defined above.</p>
    * 
    * @deprecated TODO: tell why
    */
  public void setPosition( final int position ) {
    // TODO: replace the position member by more differenced settings
    // for backwards compatibility, we translate the old position setting
    // to the new settings
    this.position = position;
    this.tabConfig = updateConfig( position );
  }

  /** <p>sets the alignment of the selection bar of this WebCardLayout by
    * using one of the constants defined above.</p>
    * 
    * @deprecated TODO: tell why
    */
  public int getPosition() {
    // TODO: replace the position member by more differenced settingss    
    return position;
  }

  // temporary delegation for the renderers
  /** <p>sets the style type for this WebCardLayout. Must be one of
    * the constants TabConfig.STYLE_MODERN or TabConfig.STYLE_CLASSIC.</p> */
  public void setType( final int type ) {
    tabConfig.setType( type );
  }
  
  /** <p>returns the default style type for this WebCardLayout.</p> */
  public int getType() {
    return tabConfig.getType();
  }
  
  
  // helping methods
  //////////////////
  
  private WebColor createColor( final String key ) {
    return new WebColor( DefaultColorScheme.get( key ) );
  }
}