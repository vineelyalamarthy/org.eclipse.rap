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


/** <p>classes that implement this can be asked to validate their internals 
  * (for instance the value of a Webcomponent) against a 
  * {@link Validator Validator} which can be set.</p>
  */
public interface ValidatableComponent extends Validatable {
  
  /** <p>Sets the Validator against which this ValidatableComponent validates 
    * its internals.</p> */
  void setValidator( Validator validator );

  /** <p>Returns the Validator against which this ValidatableComponent 
    * validates its internals.</p> */
  Validator getValidator();
}
