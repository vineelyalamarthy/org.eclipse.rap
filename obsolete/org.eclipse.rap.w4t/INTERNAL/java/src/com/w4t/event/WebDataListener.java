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
package com.w4t.event;

import java.util.EventListener;

/** 
 * <p><code>WebDataListener</code>s observe value changes of
 * <code>IInputValueHolder</code>s that are caused by user input.</p>
 */
public interface WebDataListener extends EventListener {

  /**
   * <p>If the value of a databound component was changed by user input
   * the <code>webValueChanged</code> event handler method is called at
   * the end of the read-data-phase of the request lifecycle. </p> 
   */
  void webValueChanged( WebDataEvent e );

}

 