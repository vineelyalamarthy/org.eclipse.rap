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
package com.w4t.engine.lifecycle.standard;

import com.w4t.engine.util.ServletExceptionAdapter;

/** <p>Adapter used for exceptions which occur during the execute method of 
 *  a Phase instance. PhaseExceptions are used instead of the ServletExceptions 
 *  in order to keep the inner exception information.</p> 
 */
public class PhaseException extends ServletExceptionAdapter {
  
  private static final long serialVersionUID = 1L;
  
  public PhaseException( final Throwable cause ) {
    super( cause );
  }
}