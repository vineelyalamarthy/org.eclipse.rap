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

import org.eclipse.rwt.internal.lifecycle.HtmlResponseWriter;
import org.eclipse.rwt.internal.util.HTML;

import com.w4t.dhtml.TreeView;

/**
 * <p>
 * The renderer for org.eclipse.rap.dhtml.TreeView on Mozilla 1.6 and later.
 * </p>
 */
public class TreeViewRenderer_Mozilla1_6up_Script
  extends TreeViewRenderer_DOM_Script
{
  
  private static final String DIV_CSS 
    = "display:block;font-size:0px;" 
    + "text-align:left;vertical-align:top;" 
    + "-moz-user-select:none"; // prevent text selection while drag-drop

  protected void useJSLibrary() throws IOException {
    TreeViewUtil.userDoubleClickLibrary();
    TreeViewUtil.useJSLibrary( TreeViewUtil.TREEVIEW_IE_GECKO );
  }
  
  void createOuterDivOpen( final TreeView treeView ) throws IOException {
    HtmlResponseWriter out = getResponseWriter();
    out.startElement( HTML.DIV, null );
    out.writeAttribute( HTML.ID, treeView.getUniqueID(), null );
    out.writeAttribute( HTML.CLASS, out.registerCssClass( DIV_CSS ), null );
  }
}