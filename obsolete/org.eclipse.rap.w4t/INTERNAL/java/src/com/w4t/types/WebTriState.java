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

/** <p>A WebTriState is the specific data type for attributes with values
  * NO, YES or DEFAULT.</p>
  * 
  * <p>This value type is used in situations where settings can include 'yes'
  * or 'no' values or can be left empty. In the latter case the empty value 
  * is typically recognized as meaning 'use default'. Depending on the
  * evaluation of the attribute, the default can then be one of 'yes' or 'no'.
  * </p>
  * 
  * <p>For example, some {@link org.eclipse.rwt.WindowProperties window properties} 
  * use a WebTriState when the browser may recognize 'yes' or 'no' but will
  * also accept an empty value, which it then interprets as one of 'yes' or 
  * 'no'.</p>
  */
public class WebTriState extends WebPropertyBase {

  /** <p>Value definition for using the default settings.</p> */
  public final static String DEFAULT = "";
  /** <p>Value definition for enabling a setting.</p> */
  public final static String YES = "yes";
  /** <p>Value definition for disabling a setting.</p> */
  public final static String NO = "no";
  
  /** <p>constructs a new WebTriState with the specified value.</p> */
  public WebTriState( final String value ) {
    super( value );
  }
  
  public int hashCode() {
    return value.hashCode();
  }
}

