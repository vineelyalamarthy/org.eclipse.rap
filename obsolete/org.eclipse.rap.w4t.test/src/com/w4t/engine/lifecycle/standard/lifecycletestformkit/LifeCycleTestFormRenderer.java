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
package com.w4t.engine.lifecycle.standard.lifecycletestformkit;

import com.w4t.Renderer;
import com.w4t.engine.lifecycle.standard.IFormRenderer;


public class LifeCycleTestFormRenderer 
  extends Renderer
  implements IFormRenderer
{
  

  public static final String PROCESS_EVENT_NOSCRIPT = "processEventNoscript";
  public static final String READ_DATA_NOSCRIPT = "readDataNoscript";
  public static final String RENDER_NOSCRIPT = "renderNoScript";
  
  protected void performRendering() throws Exception {
  }

  public boolean fireRenderEvents() {
    return false;
  }

  public void prepare() {
  }
}
