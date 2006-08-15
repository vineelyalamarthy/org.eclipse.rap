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
package com.w4t.renderinfo;

import com.w4t.WebContainer;

/** The LayoutRenderInfo contains information which all WebLayoutRenderer need
  * to perform the rendering of a WebContainer.
  */
public class LayoutRenderInfo {
  
  private WebContainer parent;  
  
  /** Creates a new instance of LayoutRenderInfo */
  public LayoutRenderInfo( final WebContainer parent ) {
    this.parent = parent;
  }
  
  /** returns the WebLayouts WebContainer which has to be rendered */
  public WebContainer getParent() {
    return parent;
  }
}
