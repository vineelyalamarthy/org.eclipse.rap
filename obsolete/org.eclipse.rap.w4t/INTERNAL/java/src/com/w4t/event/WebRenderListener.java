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

/** <p>A render listener is notified before and after rendering of a 
 * <code>WebComponent</code> takes place.</p>
 */
public interface WebRenderListener extends EventListener {

  /** 
   * <p>Invoked before the the WebComponent, to which this
   * <code>WebRenderListener</code> is added, is rendered.</p>
   * <p>Note that WebComponents are not rendered if the visible attribute is 
   * set to <code>false</code>; therefore no render event occurs.
   * </p> 
   */
  void beforeRender( WebRenderEvent e );

  /**
   * <p>Invoked after the the WebComponent, to which this WebRenderListener is
   * added, has been rendered.</p>
   * <p>Note that WebComponents are not rendered if the visible attribute is 
   * set to <code>false</code>; therefore no render event occurs.</p>
   */
  void afterRender( WebRenderEvent e );
}

 