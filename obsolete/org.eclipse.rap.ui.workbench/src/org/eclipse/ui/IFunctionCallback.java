/*******************************************************************************
 * Copyright (c) 2002-2007 Innoopract Informationssysteme GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Innoopract Informationssysteme GmbH - initial API and implementation
 ******************************************************************************/

package org.eclipse.ui;


/**
 * Used to notify that a function is finished and to propagate its
 * return code to the caller.
 * 
 * This interface has been introduced with RAP.
 */
public interface IFunctionCallback {

	  /**
	   * Gets called when the function returns.
	   * 
	   * @param returnValue
	   * 
	   */
	  public void functionReturned( Object returnValue );
	
}
