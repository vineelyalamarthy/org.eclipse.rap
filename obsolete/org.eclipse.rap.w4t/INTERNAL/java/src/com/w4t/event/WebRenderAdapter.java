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
  * described by the <code>WebRenderListener</code> interface.
  * <p>Classes that wish to deal with <code>WebFormEvent</code>s can extend 
  * this class and override only the methods which they are interested in.</p>
  * @see org.eclipse.rwt.event.WebFormListener
  * @see org.eclipse.rwt.event.WebFormEvent
  */
public class WebRenderAdapter implements WebRenderListener {

  /** 
   * <p>Invoked before the the WebComponent, to which this
   * <code>WebRenderListener</code> is added, is rendered. The default behavior 
   * is to do nothing.</p>
   */
  public void beforeRender( final WebRenderEvent e ) {
  }

  /**
   * <p>Invoked after the the WebComponent, to which this WebRenderListener is
   * added, has been rendered. The default behavior is to do nothing.</p>
   */
  public void afterRender( final WebRenderEvent e ) {
  }
}
