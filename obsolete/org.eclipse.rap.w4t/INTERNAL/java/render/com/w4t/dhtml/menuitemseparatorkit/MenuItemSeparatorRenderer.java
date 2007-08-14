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
package com.w4t.dhtml.menuitemseparatorkit;

import java.io.IOException;

import org.eclipse.rwt.internal.lifecycle.HtmlResponseWriter;

import com.w4t.Renderer;


/** <p>the superclass of all Renderers that render 
  * {@link org.eclipse.rwt.dhtml.MenuItemSeparator org.eclipse.rap.dhtml.MenuItemSeparator}.
  * </p>
  */
public abstract class MenuItemSeparatorRenderer extends Renderer {

  protected void renderVerticalSeparator() throws IOException {
    HtmlResponseWriter out = getResponseWriter();
    out.writeNBSP();
    out.writeText( "|", null );
    out.writeNBSP();
  }
}
  

