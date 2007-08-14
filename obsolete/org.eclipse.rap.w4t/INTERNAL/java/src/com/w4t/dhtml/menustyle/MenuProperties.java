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

import com.w4t.Style;
import com.w4t.WebComponentProperties;
import com.w4t.types.WebColor;
import com.w4t.util.DefaultColorScheme;


/** <p>the superclass of all style objects for menu components. Styles 
  * for menu components are like org.eclipse.rap.Styles but reduced in functionality
  * extend (not all style settings are safe for menus).</p>
  */
public abstract class MenuProperties extends WebComponentProperties {

  /** <p>the internal data structure for this MenuStyle. Subclasses 
    * redirect all attributes they want to use to this internal data 
    * structure, all other attributes remain unused.</p> */
  Style style;  
  
  
  /** <p>creates a new instance of MenuProperties.</p> */
  public MenuProperties() {
    style = new Style();
  }
  
  /** <p>returns a string representation of this MenuStyle.</p> */
  public String toString() {
    return style.toString();
  }

  
  // attribute getters and setters
  ////////////////////////////////
  
  /** sets the style attribute border */
  public void setBorder( final int border ) {
    style.setBorder( String.valueOf( border ) + "px solid" );
  }
  
  /** gets the style attribute border */
  public int getBorder() {
    return parsePixel( style.getBorder() );
  }  
  
  /** sets top border color of the css-style attribute border-color.
   *  Note: This only takes effect if all borders attributes are set */
  public void setBorderTopColor( final WebColor borderTopColor ) {
    style.setBorderTopColor( borderTopColor );
  }

  /** gets top border color of the css-style attribute border-color.
   *  Note: This only takes effect if all borders attributes are set */
  public WebColor getBorderTopColor() {
    return style.getBorderTopColor();
  }

  /** sets bottom border color of the css-style attribute border-color.
   *  Note: This only takes effect if all borders attributes are set */
  public void setBorderBottomColor( final WebColor borderBottomColor ) {
    style.setBorderBottomColor( borderBottomColor );
  }

  /** gets bottom border color of the css-style attribute border-color.
   *  Note: This only takes effect if all borders attributes are set */
  public WebColor getBorderBottomColor() {
    return style.getBorderBottomColor();
  }

  /** sets right border color of the css-style attribute border-color.
   *  Note: This only takes effect if all borders attributes are set */
  public void setBorderRightColor( final WebColor borderRightColor ) {
    style.setBorderRightColor( borderRightColor );
  }

  /** gets right border color of the css-style attribute border-color.
   *  Note: This only takes effect if all borders attributes are set */
  public WebColor getBorderRightColor() {
    return style.getBorderRightColor();
  }

  /** sets left border color of the css-style attribute border-color.
   *  Note: This only takes effect if all borders attributes are set */
  public void setBorderLeftColor( final WebColor borderLeftColor ) {
    style.setBorderLeftColor( borderLeftColor );
  }
  
  /** gets left border color of the css-style attribute border-color.
   *  Note: This only takes effect if all borders attributes are set */
  public WebColor getBorderLeftColor() {
    return style.getBorderLeftColor();
  }

  /** sets the style attribute padding */
  public void setPadding( final int padding ) {
    style.setPadding( String.valueOf( padding ) + "px" );
  }

  /** gets the style attribute padding */
  public int getPadding() {
    return parsePixel( style.getPadding() );
  }
  
  /** sets the style attribute background-color */
  public void setBgColor( final WebColor bgColor ) {
    style.setBgColor( bgColor );
  }
  
  /** gets the style attribute background-color */
  public WebColor getBgColor() {
    return style.getBgColor();
  }

  
  // helping methods
  //////////////////
  
  int parsePixel( final String toParse ) {
    int result = 0;
    try {
      int index = toParse.indexOf( "px" );
      result = Integer.parseInt( toParse.substring( 0, index ) );
    } catch( Exception ignored ) {
      // no exception handling here, we return just a default value 
      // (should not happen anyway, as the border can only be set with 
      // setBorder( int )
    }
    return result;
  }
  
  WebColor createColor( final String key ) {
    return new WebColor( DefaultColorScheme.get( key ) );
  }
}
