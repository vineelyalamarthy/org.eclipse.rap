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
 * <p>Classes which implement this interface provide methods that deal with 
 * changes in item state; e.g. a <code>WebCheckBox</code> informs about being
 * checked or unchecked via this event type.</p>
 * @see org.eclipse.rwt.event.WebItemEvent
 */
public interface WebItemListener extends EventListener {

  /** 
   * <p>Invoked when an item state change event occurs.</p>
   */
  void webItemStateChanged( WebItemEvent e );

}

 