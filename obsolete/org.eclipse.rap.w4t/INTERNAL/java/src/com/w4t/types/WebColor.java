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
package com.w4t.types;

import java.util.Vector;
import com.w4t.util.ColorMapper;


/** <p>A WebColor is the specific data type for color objects.</p>
  */
public class WebColor extends WebPropertyBase {

  static Vector netscapeColors;
  
  public Object clone() throws CloneNotSupportedException {
    return super.clone();
  }
  
  /** constructs an new WebColor with the specified value. */
  public WebColor( final String value ) {
    super( value );
    this.value = mapColor( value );
  }

  /** <p>translates the passed color into an rgb hex string, if it is one of 
    * the standard html color or old netscape color (i.e. "red", "blue" 
    * etc.).</p> */
  private String mapColor( final String colorName ) {
    return ColorMapper.getHexRGB( colorName );
  }

  /** <p>indicates whether some other WebColor is "equal to" this one.</p>
    * 
    * <p>This returns generally false if the passed Object is no WebColor, 
    * and returns true if the passed WebColor has the same value 
    * (represents the same html color hex string) as this WebColor.</p> */
  public boolean equals( final Object obj ) {
    boolean result = false;
    if( obj instanceof WebColor ) {
      WebColor color = ( WebColor )obj;
      result = this.value.equals( color.toString() );
    }
    return result;
  }

  public int hashCode() {
    return value.hashCode();
  }
}