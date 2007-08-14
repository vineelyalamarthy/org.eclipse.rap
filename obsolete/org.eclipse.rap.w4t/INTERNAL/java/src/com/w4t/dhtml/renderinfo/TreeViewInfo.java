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
package com.w4t.dhtml.renderinfo;

import java.util.List;

import org.eclipse.rwt.internal.lifecycle.HtmlResponseWriter;

import com.w4t.dhtml.TreeNodeShift;


/** <p>contains the information that is needed in the Renderer for 
  * TreeViews.</p>
  *
  * <p>Note that render info objects should <b>always</b> be 
  * <b>readonly</b>.</p>
  *
  * see org.eclipse.rap.dhtml.treeviewkit.TreeViewRenderer, TreeView.createRenderInfo()
  */
public class TreeViewInfo extends TreeInfo {

  
  /** <p>constructs a new TreeViewInfo with the specified information.</p> */
  public TreeViewInfo( final HtmlResponseWriter tbStateInfoFields, 
                       final List vecNodeList, 
                       final List vecLeafList,
                       final TreeNodeShift treeNodeShift ) {
    super( tbStateInfoFields, 
           vecNodeList, 
           vecLeafList, 
           treeNodeShift );
  }
}