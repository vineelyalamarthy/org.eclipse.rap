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
package com.w4t.engine.requests;

/** <p>this is thrown to indicate that a request is stopped and gets 
  * no further render output etc. Currently this is used in the adapter 
  * only.</p>
  */
public class RequestCancelledException extends Exception {

  private static final long serialVersionUID = 1L;

  public RequestCancelledException() {
    super();
  }
}
