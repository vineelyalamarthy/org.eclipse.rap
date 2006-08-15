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
import com.w4t.types.WebTriState;

/** <p>WindowProperties encapsulates information about browser windows, 
  * like size, position etc.. Used for specifying browser window settings 
  * for WebForms that open in a new window.</p>
  */
public class WindowProperties extends WebComponentProperties {
  
  /** <p>popupPosition value for automatically positioning 
   *  a popup form into the desktops center area.</p> */
  public final static String POSITION_CENTER = "center";
  /** <p>popupPosition value for use the absolute positioning of 
   *  the screenX and screenY attributes.</p> */
  public final static String POSITION_ABSOLUTE = "absolute";

  /** <p>the available width of the clients desktop in pixel</p> */
  public static int availWidth = 0;
  /** <p>the available height of the clients desktop in pixel</p> */
  public static int availHeight = 0;

  /** <p>determines the strategie of positioning a popup WebForm. Might be
    *  one of those POSITION_ABSOLUTE (default), POSITION_CENTER.</p>*/
  private String popupPosition = POSITION_ABSOLUTE;
  private String dependent = "";
  private String directories = "";
  private String height = "600";
  private String hotkeys = "";
  private String innerheight = "";
  private String innerwidth = "";
  private WebTriState location = new WebTriState( "no" );
  private WebTriState menubar = new WebTriState( "no" );
  private WebTriState resizable = new WebTriState( "yes" );
  private WebTriState status = new WebTriState( "no" );
  private String screenX = "";
  private String screenY = "";
  private WebTriState scrollbars = new WebTriState( "" );
  private WebTriState titlebar = new WebTriState( "" );
  private WebTriState toolbar = new WebTriState( "" );
  private String width = "600";
  private String fullScreen = "no";


  /** builds the String for the new window's properties depending on
    * the attribute settings. */
  public String toString() {
    computePosition();
    StringBuffer windowProps = new StringBuffer();
    createAttribute( windowProps, "dependent", dependent );
    createAttribute( windowProps, "directories", directories );
    createAttribute( windowProps, "height", height );
    createAttribute( windowProps, "hotkeys", hotkeys );
    createAttribute( windowProps, "innerheight", innerheight );
    createAttribute( windowProps, "innerwidth", innerwidth );
    createAttribute( windowProps, "location", location.toString() );
    createAttribute( windowProps, "menubar", menubar.toString() );
    createAttribute( windowProps, "resizable", resizable.toString() );
    createAttribute( windowProps, "status", status.toString() );
    createAttribute( windowProps, "left", screenX );
    createAttribute( windowProps, "top", screenY );    
    createAttribute( windowProps, "scrollbars", scrollbars.toString() );
    createAttribute( windowProps, "titlebar", titlebar.toString() );
    createAttribute( windowProps, "toolbar", toolbar.toString() );
    createAttribute( windowProps, "width", width );
    createAttribute( windowProps, "fullscreen", fullScreen ); 
    return windowProps.toString();
  }

  private void computePosition() {
    if( popupPosition.equals( POSITION_CENTER ) ) {
      try {
        int currWidth = Integer.parseInt( width );
        int currHeight = Integer.parseInt( height );
        screenX = String.valueOf( ( int )( ( availWidth - currWidth ) / 2 ) );
        screenY = String.valueOf( ( int )( ( availHeight - currHeight ) ) / 2 );
      } catch( NumberFormatException ignore ) {
        // since not both window size values are valid, 
        // autopositioning is canceled 
      }
    }
  }
  
  private void createAttribute( final StringBuffer sb, 
                                final String key, 
                                final String value ) {
    if( !value.equals( "" ) ) {
      sb.append( key );
      sb.append( "=" );
      sb.append( value );
      sb.append( "," );
    }
  }

  /** <p>Sets whether the popup window depends on the parent window, which
    * means that the window is closed when the parent window gets closed.</p>
    * 
    * <p>Possible values:
    * <ul>
    *   <li>yes - window is closed when the parent window gets closed</li>
    *   <li>no [default] - window remains open when the parent window gets 
    *                     closed</li>
    * </ul></p> */
  public void setDependent( final String dependent ) {
    this.dependent = dependent;
  }

  /** <p>returns whether the popup window depends on the parent window, which
    * means that the window is closed, when the parent window gets closed.</p>
    * 
    * <p>Possible values:
    * <ul>
    *   <li>yes - window is closed when the parent window gets closed</li>
    *   <li>no [default] - window remains open when the parent window gets
    *                     closed</li>
    * </ul></p> */
  public String getDependent() {
    return dependent;
  }

  /** <p>sets whether extra bars like 'Links' or self-defined bars like
    * search engine bars are displayed.</p> 
    * <ul>Possible values: 
    *   <li>yes - extra bars will be displayed</li>
    *   <li>no [default] - extra bars will not be displayed</li>
    * </ul> */
  public void setDirectories( final String directories ) {
    this.directories = directories;
  }

  /** <p>returns whether extra bars like 'Links' or self-defined bars like
    * search engine bars are displayed.</p> 
    * <ul>Possible values: 
    *   <li>yes - extra bars will be displayed</li>
    *   <li>no [default] - extra bars will not be displayed</li>
    * </ul> */
  public String getDirectories() {
    return directories;
  }

  /** <p>sets the "height" of the popup window in pixels.</p> */
  public void setHeight( final String height ) {
    this.height = height;
  }

  /** <p>returns the "height" of the popup window in pixels.</p> */
  public String getHeight() {
    return height;
  }

  /** <p>sets whether the key control of the browser is activated.</p>
    * 
    * <p>Note: This attribute is supported only by Netscape 4 and higher.</p> 
    * 
    * <ul>Possible values: 
    *   <li>yes [default] - 'hotkeys' will be activated</li>
    *   <li>no - 'hotkeys' will be disabled</li>
    * </ul> */
  public void setHotkeys( final String hotkeys ) {
    this.hotkeys = hotkeys;
  }

  /** <p>returns whether the key control of the browser is activated.</p>
    * 
    * <p>Note: This attribute is supported only by Netscape 4 and higher.</p>
    *  
    * <ul>Possible values: 
    *   <li>yes [default] - 'hotkeys' will be activated</li>
    *   <li>no - 'hotkeys' will be disabled</li>
    * </ul>
    */
  public String getHotkeys() {
    return hotkeys;
  }

  /** <p>sets the height of the client area for the new window in pixels. 
    * The difference to "height" is: this function sets the 
    * visible area, without the bars etc.
    *  
    * <p>Note: This attribute is supported only by Netscape 4 and higher.</p>
    */
  public void setInnerheight( final String innerheight ) {
    this.innerheight = innerheight;
  }

  /** <p>returns the height of the client area for the new window in pixels. 
    * The difference to "height" is: this function sets the 
    * visible area, without the bars etc.
    *  
    * <p>Note: This attribute is supported only by Netscape 4 and higher.</p>
    */
  public String getInnerheight() {
    return innerheight;
  }

  /** <p>sets the width of the client area for the new window in pixels. The 
    * difference to "width" is: this function sets the visible area, without 
    * the borders of the window.</p>
    *
    * <p>Note: This attribute is supported only by Netscape 4 and higher.</p>
    */
  public void setInnerwidth( final String innerwidth ) {
    this.innerwidth = innerwidth;
  }

  /** <p>returns the width of the client area for the new window in pixels. The 
    * difference to "width" is: this function sets the visible area, without 
    * the borders of the window.</p>
    *
    * <p>Note: This attribute is supported only by Netscape 4 and higher.</p>
    */
  public String getInnerwidth() {
    return innerwidth;
  }

  /** <p>sets whether the address bar is shown in the popup window.</p>
    * 
    * <ul>Possible values: 
    *   <li>yes - the adress bar will be displayed</li>
    *   <li>no [default] - the adress bar will not be displayed</li>
    * </ul> */  
  public void setLocation( final WebTriState location ) {
    this.location = location;
  }

  /** <p>returns whether the address bar is shown in the popup window.</p>
    * 
    * <ul>Possible values: 
    *   <li>yes - the adress bar will be displayed</li>
    *   <li>no [default] - the adress bar will not be displayed</li>
    * </ul> */  
  public WebTriState getLocation() {
    return location;
  }

  /** <p>sets whether the menu bar is shown in the popup window.</p>
    * 
    * <ul>Possible values: 
    *   <li>yes - the menu bar will be displayed</li>
    *   <li>no [default] - the menu bar will not be displayed</li>
    * </ul>
    */
  public void setMenubar( final WebTriState menubar ) {
    this.menubar = menubar;
  }

  /** <p>returns whether the menu bar is shown in the popup window.</p>
    * 
    * <ul>Possible values: 
    *   <li>yes - the menu bar will be displayed</li>
    *   <li>no [default] - the menu bar will not be displayed</li>
    * </ul>
    */
  public WebTriState getMenubar() {
    return menubar;
  }

  /** <p>sets whether the popup window can be resized.</p>
    * 
    * <ul>Possible values: 
    *   <li>yes [default] - the popup window can be resized</li>
    *   <li>no - the popup window cannot be resized</li>
    * </ul> */
   public void setResizable( final WebTriState resizable ) {
    this.resizable = resizable;
  }

  /** <p>returns whether the popup window can be resized.</p>
    * 
    * <ul>Possible values: 
    *   <li>yes [default] - the popup window can be resized</li>
    *   <li>no - the popup window cannot be resized</li>
    * </ul> */
  public WebTriState getResizable() {
    return resizable;
  }

  /** <p>sets whether the status bar of the popup window is displayed.</p>
    * 
    * <ul>Possible values: 
    *   <li>yes [default] - the status bar will be displayed</li>
    *   <li>no - the status bar will not be displayed</li>
    * </ul> */
  public void setStatus( final WebTriState status ) {
    this.status = status;
  }

  /** <p>returns whether the status bar of the popup window is displayed.</p>
    * 
    * <ul>Possible values: 
    *   <li>yes [default] - the status bar will be displayed</li>
    *   <li>no - the status bar will not be displayed</li>
    * </ul> */
  public WebTriState getStatus() {
    return status;
  }

  /** <p>sets the horizontal screen offset of the new window 
    * in pixels. This specifies the popup position 
    * of the new window. 
    * 
    * <p>This uses the same value as the 'window.left' parameter to the 
    * javascipt 'window.open()' function.</p>
    */
  public void setScreenX( final String screenX ) {
    this.screenX = screenX;
  }

  /** <p>returns the horizontal screen offset of the new window 
    * in pixels. This specifies the popup position 
    * of the new window. 
    * 
    * <p>This uses the same value as the 'window.left' parameter to the 
    * javascipt 'window.open()' function.</p>
    */
  public String getScreenX() {
    return screenX;
  }

  /** <p>sets the vertical screen offset of the new window in
    * pixels. This specifies the popup position of
    * the new window.</p>
    * 
    * <p>This uses the same value as the 'window.top' parameter to the 
    * javascipt 'window.open()' function.</p>
    */
  public void setScreenY( final String screenY ) {
    this.screenY = screenY;
  }

  /** <p>returns the vertical screen offset of the new window in
    * pixels. This specifies the popup position of
    * the new window.</p>
    * 
    * <p>This uses the same value as the 'window.top' parameter to the 
    * javascipt 'window.open()' function.</p>
    */
  public String getScreenY() {
    return screenY;
  }

  /** <p>sets whether the scrollbars of the popup window will be displayed.</p>
    * 
    * <ul>Possible values: 
    *   <li>yes - the scrollbars will be displayed</li>
    *   <li>no [default] - the scrollbars will be displayed</li>
    * </ul> */
  public void setScrollbars( final WebTriState scrollbars ) {
    this.scrollbars = scrollbars;
  }

  /** <p>returns whether the scrollbars of the popup window will be 
    * displayed.</p>
    * 
    * <ul>Possible values: 
    *   <li>yes - the scrollbars will be displayed</li>
    *   <li>no [default] - the scrollbars will be displayed</li>
    * </ul> */
  public WebTriState getScrollbars() {
    return scrollbars;
  }

  /** <p>sets the "titlebar" property for the new window.</p> */
  public void setTitlebar( final WebTriState titlebar ) {
    this.titlebar = titlebar;
  }

  /** <p>returns the "titlebar" property for the new window.</p> */
  public WebTriState getTitlebar() {
    return titlebar;
  }

  /** <p>sets whether the tool bar (browser navigation, refresh, home etc.) 
    * of the popup window will be displayed.</p>
    * 
    * <ul>Possible values: 
    *   <li>yes - the tool bar will be displayed</li>
    *   <li>no [default] - the tool bar will be displayed</li>
    * </ul> */
  public void setToolbar( final WebTriState toolbar ) {
    this.toolbar = toolbar;
  }

  /** <p>returns whether the tool bar (browser navigation, refresh, home etc.) 
    * of the popup window will be displayed.</p>
    * 
    * <ul>Possible values: 
    *   <li>yes - the tool bar will be displayed</li>
    *   <li>no [default] - the tool bar will be displayed</li>
    * </ul> */
  public WebTriState getToolbar() {
    return toolbar;
  }

  /** <p>sets the width of the new window in pixels. The distinction to 
    * "innerwidth" is: this function sets the size of the whole window, 
    * including the borders of the window.</p> */
  public void setWidth( final String width ) {
    this.width = width;
  }

  /** <p>returns the width of the new window in pixels. The distinction to 
    * "innerwidth" is: this function sets the size of the whole window, 
    * including the borders of the window.</p> */
  public String getWidth() {
    return width;
  }

  /** <p>sets whether the popup window should fill the whole screen.</p>
    * 
    * <ul>Possible values: 
    *   <li>yes - the window will fill the available screen</li>
    *   <li>no [default] - the window will not fill the available screen</li>
    * </ul> */
  public void setFullScreen( final String fullScreen ) {
    this.fullScreen = fullScreen;
  }

  /** <p>returns whether the popup window should fill the whole screen.</p>
    * 
    * <ul>Possible values: 
    *   <li>yes - the window will fill the available screen</li>
    *   <li>no [default] - the window will not fill the available screen</li>
    * </ul> */
  public String getFullScreen() {
    return fullScreen;
  }
  
  /** <p>Returns the strategy for positioning a popup WebForm. Might be
    *  one of those: POSITION_ABSOLUTE (default), POSITION_CENTER.</p>*/  
  public String getPopupPosition() {
    return popupPosition;
  }

  /** <p>Sets the strategy for positioning a popup WebForm. Might be
    *  one of those: POSITION_ABSOLUTE (default), POSITION_CENTER.</p>*/  
  public void setPopupPosition( final String popupPosition ) {
    this.popupPosition = popupPosition;
  }
  
  /** <p>Sets the available height of the clients desktop. Automatically 
    * invoked by the system.</p> */
  public static void setAvailHeight( final int availHeight ) {
    WindowProperties.availHeight = availHeight;
  }

  /** <p>Sets the available width of the clients desktop. Automatically 
   *  invoked by the system.</p> */
  public static void setAvailWidth( final int availWidth ) {
    WindowProperties.availWidth = availWidth;
  }
}