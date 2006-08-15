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


/** <p>Implementations of IReadDataAdapter are used to read status
 *  information of WebComponents from the currently processed request.
 *  This takes place in the <em>ReadData phase</em> of the requests life cycle
 *  handling.</p>
 */
public interface IReadDataAdapter {
  
  /** <p>Reads parameter values which belong to the given <code>component</code> 
   *  from the current request and sets these values to the component.</p> 
   *  @param component the WebComponent whose status information should
   *                   be read from the request.
   */
  void readData( WebComponent component );
  
}
