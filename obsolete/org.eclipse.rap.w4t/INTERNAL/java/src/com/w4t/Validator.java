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


/** <p>Classes that implement this interface know how to validate a given input 
 * string.</p>
 * <p>Validators are used by classes that implement {@link 
 * org.eclipse.rwt.ValidatableComponent ValidatableComponent} to validate their 
 * values.</p>
 */
public interface Validator {
  
  /** <p>Validates the passed value against some implementation-specific 
    * validation algorithm.</p> */
  boolean validate( String value );
  
  /** <p>Sets an information that may be used as comment for
    * what this Validator is like or as error message.</p> */
  void setInfo( String info ) ;

  /** <p>Returns the information that may be used as comment for what this
    * Validator is like or as error message.</p> */
  String getInfo();
}
