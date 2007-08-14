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
  * <p>A container listener is notified about added and removed 
  * child-components and layout-changes of the respective
  * <code>WebContainer</code>.</p>
  * @see org.eclipse.rwt.event.WebContainerAdapter
  * @see org.eclipse.rwt.event.WebContainerEvent
  */
public interface WebContainerListener extends EventListener {

  /** 
   * <p>Invoked when a <code>WebComponent</code> was added to a 
   * <code>WebContainer</code>.</p> 
   */
  void webComponentAdded( WebContainerEvent evt );

  /**
   * <p>Invoked when a <code>WebComponent</code> was removed from a 
   * <code>WebContainer</code>.</p> 
   */
  void webComponentRemoved( WebContainerEvent evt );

  /**
   * <p>Invoked when a <code>WebLayout</code> was set to a 
   * <code>WebContainer</code>.</p> 
   */
  void webLayoutChanged( WebContainerEvent evt );
}

