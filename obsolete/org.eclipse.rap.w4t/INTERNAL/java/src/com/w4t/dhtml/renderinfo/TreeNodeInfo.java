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

import com.w4t.dhtml.TreeNode;
import com.w4t.dhtml.TreeNodeShift;
import com.w4t.dhtml.event.WebTreeNodeExpandedListener;



/** <p>contains the information that is needed in the Renderer for 
  * TreeNodes.</p>
  *
  * <p>Note that render info objects should <b>always</b> be 
  * <b>readonly</b>.</p>
  */
public class TreeNodeInfo extends TreeInfo {

  private TreeNode treeNode;
  /** whether the TreeNode is the currently marked item in a treeview 
    * structure. */
  private boolean marked;
  private WebTreeNodeExpandedListener toggleLoadingListener;
  private String expansion;  
  
  /** <p>constructs a new TreeNodeInfo with the specified information.</p> */
  public TreeNodeInfo( final HtmlResponseWriter tbStateInfoFields, 
                       final List nodeList, 
                       final List leafList,
                       final TreeNode treeNode,
                       final boolean marked,
                       final WebTreeNodeExpandedListener toggleLoadingListener,
                       final String expansion,
                       final TreeNodeShift treeNodeShift ) {
    super( tbStateInfoFields, 
           nodeList, 
           leafList, 
           treeNodeShift );
    this.treeNode = treeNode;
    this.marked = marked;
    this.toggleLoadingListener = toggleLoadingListener;
    this.expansion = expansion;
  }
  
  
  // accessors
  ////////////
  
  /** <p>returns whether the TreeNode is the currently marked item in 
    * a treeview structure.</p> */
  public boolean isMarked() {
    return marked;
  }
  
  public void removeToggleLoadingListener() {
    treeNode.removeWebTreeNodeExpandedListener( toggleLoadingListener );
  }
  
  public void addToggleLoadingListener() {
    treeNode.addWebTreeNodeExpandedListener( toggleLoadingListener );
  }
  
  public String getExpansion() {
    return expansion;
  }
  
  public void appendStateInfoFields() {
    ( ( TreeNode )treeNode.getParentNode() ).addStateInfo( tbStateInfoFields );
  }
  
  public void appendStateInfoField( final String hiddenField ) {
    ( ( TreeNode )treeNode.getParentNode() ).addStateInfo( hiddenField );    
  }  
}