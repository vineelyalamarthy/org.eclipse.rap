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

import com.w4t.dhtml.*;


/** <p>contains information that is needed in the Renderer for TreeViews, 
  * TreeNode in common.</p>
  *
  * <p>Note that render info objects should <b>always</b> be 
  * <b>readonly</b>.</p>
  *
  * see org.eclipse.rap.dhtml.treeviewkit.TreeViewRenderer, TreeView.createRenderInfo()
  * see org.eclipse.rap.dhtml.treenodekit.TreeNodeRenderer, TreeNode.createRenderInfo()
  */
public class TreeInfo {

  /** a buffer for hidden field elements that are created in rendering 
    * (needed for expanded/collapsed events). */
  HtmlResponseWriter tbStateInfoFields;
  /** the list of nodes. */
  List nodeList;
  /** the list of leaves. */
  List leafList;
  /** helping data structure that is needed for rendering. */
  TreeNodeShift treeNodeShift;
  
  
  /** <p>constructs a new TreeInfo with the specified information.</p> */
  public TreeInfo( final HtmlResponseWriter tbStateInfoFields, 
                   final List nodeList, 
                   final List leafList,
                   final TreeNodeShift treeNodeShift )
  {
    this.tbStateInfoFields = tbStateInfoFields;
    this.nodeList = nodeList;
    this.leafList = leafList;
    this.treeNodeShift = treeNodeShift;
  }
  
  
  // accessors
  ////////////
  
  /** <p>clears the state info fields buffer of the TreeView.</p> */
  public void clearStateInfoFields() {
    tbStateInfoFields.clearBody();
  }
  
  /** <p>appends the state info fields buffer of the TreeView to the 
    * specified HtmlResponseWriter.</p> */
  public void appendStateInfoFields( final HtmlResponseWriter out ) {
    out.append( tbStateInfoFields );
  }
  
  /** <p>returns the number of child nodes of the TreeView.</p> */
  public int getNodeCount() {
    return nodeList.size();
  }

  /** <p>returns the number of child leaves of the TreeView.</p> */  
  public int getLeafCount() {
    return leafList.size();
  }
  
  /** <p>returns the node of the TreeView at the specified index.</p> */
  public TreeNode getNode( final int index ) {
    return ( TreeNode )nodeList.get( index );
  }
  
  /** <p>returns the leaf of the TreeView at the specified index.</p> */
  public TreeLeaf getLeaf( final int index ) {
    return ( TreeLeaf )leafList.get( index );
  }
  
  public TreeNodeShift getTreeNodeShift() {
    return treeNodeShift;
  }  
}