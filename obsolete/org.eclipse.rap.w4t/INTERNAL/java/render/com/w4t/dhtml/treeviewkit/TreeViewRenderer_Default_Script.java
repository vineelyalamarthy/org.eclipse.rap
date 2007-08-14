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
package com.w4t.dhtml.treeviewkit;

import java.io.IOException;
import com.w4t.RenderUtil;
import com.w4t.WebComponent;



/** <p>The default renderer for org.eclipse.rap.dhtml.TreeView.</p>
  *
  * <p>The default renderer is non-browser-specific and implements 
  * functionality in a way that runs on the most commonly used browsers.</p>
  */
public class TreeViewRenderer_Default_Script 
  extends TreeViewRenderer_Base_Script
{

  public void render( final WebComponent component ) throws IOException {
    String script = "document.body.onmouseup = dragDropHandler.clearDragDrop;";
    RenderUtil.writeJavaScriptInline( getResponseWriter(), script );
    super.render( component );
  }
  
  protected void useJSLibrary() throws IOException {
    TreeViewUtil.userDoubleClickLibrary();
    TreeViewUtil.useJSLibrary( TreeViewUtil.TREEVIEW_DEFAULT );
  }
}