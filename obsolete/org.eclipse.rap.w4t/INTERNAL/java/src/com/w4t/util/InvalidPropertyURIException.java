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
package com.w4t.util;

/** <p>An InvalidPropertyURIException is thrown when an invalid URI to
  * a property in a resource bundle has been specified.</p>
  */
public class InvalidPropertyURIException extends Exception {

  private static final long serialVersionUID = 1L;

  /** <p>Constructs a new InvalidPropertyURIException with the specified 
    * message.</p> */
  public InvalidPropertyURIException( final String msg ) {
    super( msg );
  }
}
