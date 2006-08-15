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

import com.w4t.WebComponentProperties;

/** <p>WebPropertyBase is the superclass for the specific data types.</p>
 */
public class WebPropertyBase extends WebComponentProperties {

  String value;

  /** constructs a new WebPropertyBase with the specified value. */
  public WebPropertyBase( final String value ) {
    this.value = value;
  }

  /** returns a string representation of this WebProperty. */
  public String toString() {
    return value;
  }
}
