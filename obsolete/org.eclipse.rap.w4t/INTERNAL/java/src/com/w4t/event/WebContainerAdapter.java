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


/** 
 * <p>This adapter class provides default implementations for the methods 
 * described by the <code>WebContainerListener</code> interface.</p>
 * <p>Classes that wish to deal with <code>WebContainerEvent</code>s which 
 * can extend this class and override only the methods which they are 
 * interested in.</p>
 * @see org.eclipse.rwt.event.WebContainerListener
 * @see org.eclipse.rwt.event.WebContainerEvent
  */
public class WebContainerAdapter implements WebContainerListener {

  /** 
   * <p>Invoked when a <code>WebComponent</code> was added to a
   * <code>WebContainer</code>. The default behavior is to do nothing.</p> 
   */
  public void webComponentAdded( final WebContainerEvent evt ) {
  }

  /** 
   * <p>Invoked when a <code>WebComponent</code> was removed from a
   * <code>WebContainer</code>. The default behavior is to do nothing.</p>
   */
  public void webComponentRemoved( final WebContainerEvent evt ) {
  }

  /** 
   * <p>Invoked when <code>WebContainer</code>s <code>WebLayout</code> was 
   * changed. The default behavior is to do nothing.</p> 
   */
  public void webLayoutChanged( final WebContainerEvent evt ) {
  }
}

