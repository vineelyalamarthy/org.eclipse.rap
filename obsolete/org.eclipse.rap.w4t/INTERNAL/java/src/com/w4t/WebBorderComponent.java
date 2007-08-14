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

import com.w4t.types.WebColor;
import com.w4t.util.DefaultColorScheme;

/** <p>A WebBorderComponent contains a WebComponent as content, which is 
  * displayed with borders around it.</p>
  * <p>The WebBorderComponent employs the <i>Decorator</i> design pattern.
  * </p>
  * @see org.eclipse.rwt.Decorator Decorator
  */
public class WebBorderComponent extends Decorator {
  
  public final static int INSET = 1;
  public final static int OUTSET = 2;
  public final static int RIDGE = 3;
  public final static int GROOVE = 4;
  public final static int HOVER = 5;
  
  private WebColor bgColor;
  private WebColor lightColor;
  private WebColor darkColor;

  /** the width of this BorderPanel */
  private String width = "100%";
  /** the height of this BorderPanel */
  private String height = "100%";
  /** the horizontal alignment of the content component */
  private String align = "center";
  /** the vertical alignment of the content component */
  private String vAlign = "middle";
  /** padding of the content cell */ 
  private int padding = 0;
  /** default content WebComponent */
  private WebPanel wplContent = null;
  
  private int borderType = INSET;
  

  /** Creates a new instance of <code>WebBorderComponent</code>. */
  public WebBorderComponent() {
    super();
    bgColor = createColor( DefaultColorScheme.BORDER_COMPONENT_BG );
    lightColor = createColor( DefaultColorScheme.BORDER_COMPONENT_LIGHT );
    darkColor = createColor( DefaultColorScheme.BORDER_COMPONENT_LIGHT );
    try {
      wplContent = new WebPanel();
      ( ( WebFlowLayout )wplContent.getWebLayout() ).setWidth( "" );
      setContent( wplContent );
    } catch ( Exception ex ) {
    }
  }
  
  public Object clone() throws CloneNotSupportedException {
    WebBorderComponent result = ( WebBorderComponent )super.clone();
    result.setBorderType( borderType );
    result.lightColor = new WebColor( this.getLightColor().toString() );
    result.darkColor = new WebColor( this.getDarkColor().toString() );
    result.bgColor = new WebColor( this.getBgColor().toString() );
    try {
      result.wplContent = new WebPanel();
      ( ( WebFlowLayout )result.wplContent.getWebLayout() ).setWidth( "" );
      result.setContent( result.wplContent );
    } catch( Exception e ) {
      // shouldn't happen
    }
    return result;
  }

  /////////////////////
  // getter and setters
  
  /** sets the css border-style attribute of this border component. The 
   *  following style parameters are currently supported:<br>
   *  - INSET ,OUTSET, RIDGE, GROOVE and HOVER <br>
   * Note: HOVER is not supported on all browsers.  
   */ 
  public void setBorderType( final int borderType ) {
    switch ( borderType ) {
      case INSET:
        this.borderType = INSET;
      break;
      case OUTSET:
        this.borderType = OUTSET;
      break;
      case RIDGE:
        this.borderType = RIDGE;
      break;
      case GROOVE:
        this.borderType = GROOVE;
      break;
      case HOVER:
        this.borderType = HOVER;
      break;
    }
  }

  /** returns the css border-style attribute of this border component. The 
   *  following style parameters are currently supported:<br>
   *  - INSET ,OUTSET, RIDGE, GROOVE and HOVER <br>
   * Note: HOVER is not supported on all browsers.  
   */   
  public int getBorderType() {
    return borderType;
  }
  
  /** sets the horizontal alignment of the content component */
  public void setAlign( final String align ) {
    this.align = align;
  }
  
  /** gets the horizontal alignment of the content component */  
  public String getAlign() {
    return align;
  }
  
  /** sets the vertical alignment of the content component */
  public void setVAlign( final String vAlign ) {
    this.vAlign = vAlign;
  }
  
  /** sets the vertical alignment of the content component */
  public String getVAlign() {
    return vAlign;
  }
    
  /** gets the width of this BorderPanel */
  public String getWidth() {
    return width;
  }
  /** sets the width of this BorderPanel */
  public void setWidth( final String width ) {
    this.width = width;
  }

  /** gets the height of this BorderPanel */
  public String getHeight() {
    return height;
  }
  /** sets the height of this BorderPanel */
  public void setHeight( final String height ) {
    this.height = height;
  }
  
  public void setLightColor( final WebColor lightColor ) {
    this.lightColor = lightColor;
  }
  
  public WebColor getLightColor() {
    return lightColor;
  }
  
  public void setDarkColor( final WebColor darkColor ) {
    this.darkColor = darkColor;
  }
  
  public WebColor getDarkColor() {
    return darkColor;
  }  
  
  public void setBgColor( final WebColor bgColor ) {
    this.bgColor = bgColor;
  }
  
  public WebColor getBgColor() {
    return bgColor;
  }
  
  /** sets the padding of the WebBorderComponents content cell */
  public void setPadding( final int padding ) {
    if( padding >= 0 ) {
      this.padding = padding;
    }
  }
  
  /** gets the padding of the WebBorderComponents content cell */
  public int getPadding() {
    return padding;
  }
  
  /** <p>returns a path to an image that represents this WebComponent
   * (widget icon).</p> */
  public static String retrieveIconName() {
    return "resources/images/icons/borderpanel.gif";
  }
  
  
  // helping methods
  //////////////////
  
  private WebColor createColor( final String key ) {
    return new WebColor( DefaultColorScheme.get( key ) );
  } 
}