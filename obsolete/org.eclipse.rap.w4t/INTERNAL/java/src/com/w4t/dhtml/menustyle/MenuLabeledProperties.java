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

import com.w4t.types.WebColor;


/** <p>the superclass of all style objects for menu components with labels. 
  * This encapsulates some font properties in addition to those properties 
  * encapsulated in the superclass.</p>
  */
public abstract class MenuLabeledProperties extends MenuProperties {

  /** sets the style attribute color */
  public void setColor( WebColor color ) {
    style.setColor( color );
  }
  
  /** gets the style attribute color */
  public WebColor getColor() {
    return style.getColor();
  }  
  
  /** <p>sets the style attribute font-family.</p> */
  public void setFontFamily( String fontFamily ) {
    style.setFontFamily( fontFamily );
  }

  /** <p>returns the style attribute font-family.</p> */
  public String getFontFamily() {
    return style.getFontFamily();
  }

  /** <p>sets the style attribute font-size.</p> */  
  public void setFontSize( int fontSize ) {
    style.setFontSize( fontSize );
  }
  
  /** <p>returns the style attribute font-size.</p> */  
  public int getFontSize() {
    return style.getFontSize();
  }
  
  /** <p>sets the style attribute font-weight.</p> */    
  public void setFontWeight( String fontWeight ) {
    style.setFontWeight( fontWeight );
  }
  
  /** <p>returns the style attribute font-weight.</p> */    
  public String getFontWeight() {
    return style.getFontWeight();
  }

  /** <p>sets the style attribute font-style.</p> */
  public void setFontStyle( String fontStyle ) {
    style.setFontStyle( fontStyle );
  }
  
  /** <p>returns the style attribute font-style.</p> */
  public String getFontStyle() {
    return style.getFontStyle();
  }  
}
