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

import com.w4t.event.ValidationListener;


/** <p>Classes that implement this are validatable, which means that they
  * can be asked to validate their internals from outside. Validation may
  * for instance be implemented on WebComponents (which validate their 
  * values) or on WebContainers (which then call the validation method of 
  * the components added to it, provided they are validatable as well).</p>
  */
public interface Validatable {
  
  /** <p>Validates the internals of this Validatable.</p> */
  boolean validate();

  /** <p>Sets, whether the validation on this Validatable is active  
    * (whether validation is actually performed on 
    * {@link #validate() validate()} call).</p> */
  void setValidationActive( boolean validationActive );

  /** <p>Returns, whether the validation on this Validatable is active
    * (whether validation is actually performed on 
    * {@link #validate() validate()} call).</p> */
  boolean isValidationActive();
  
  /** <p>Adds the specified listener to this Validatable. The listener is
    * notified about validations which take place on this Validatable.</p>*/
  void addValidationListener( ValidationListener validationListener );

  /** <p>Removes the specified listener to this Validatable. The listener is
    * no longer notified about validations which take place on this 
    * Validatable.</p>*/
  void removeValidationListener( ValidationListener validationListener );
}
