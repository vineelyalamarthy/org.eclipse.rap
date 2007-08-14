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
 * <p>An action listener is informed about action events such as a 
 * <code>WebButton</code> that was pressed/clicked.</p>
 * @see org.eclipse.rwt.event.WebActionEvent
 */
public interface WebActionListener extends EventListener {

  /** 
   * <p>Invoked when a user action occurs.</p> 
   */
  void webActionPerformed( WebActionEvent e );

}

