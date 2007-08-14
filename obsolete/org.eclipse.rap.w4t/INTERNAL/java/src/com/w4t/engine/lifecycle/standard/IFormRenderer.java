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

import java.io.IOException;
import com.w4t.WebComponent;

/** 
 * <p>The <code>IFormRenderer</code> interface provides interaction with a 
 * WebForm renderer.</p>
 * <p>In order to work with the standard <code>ILifeCycle</code> implementation,
 * a WebForm-<code>Renderer</code> must implement this interface.</p> 
 */
// TODO [rh] JavaDoc revise this
public interface IFormRenderer {

  /** 
   * <p>Returns whether the {@link org.eclipse.rwt.event.WebRenderEvent render events}
   * should be fired.</p>
   */
  boolean fireRenderEvents();
    
  /**
   * <p>Render the markup for the form. The name-match with 
   * <code>Renderer.render(WebComponent)</code> is intentional.</p>
   * @see org.eclipse.rwt.Renderer#render(WebComponent)
   */
  void render( WebComponent component ) throws IOException;

  /** 
   * <p>Prepare the WebForm-renderer for the rendering process.</p> 
   */
  void prepare();
}
