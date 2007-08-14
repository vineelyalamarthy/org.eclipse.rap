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

import com.w4t.dhtml.TreeNodeShift;


/** <p>contains the information that is needed in the Renderer for 
  * TreeLeaves.</p>
  *
  * <p>Note that render info objects should <b>always</b> be 
  * <b>readonly</b>.</p>
  *
  * see org.eclipse.rap.dhtml.treeleafkit.TreeLeafRenderer, TreeLeaf.createRenderInfo()
  */
public class TreeLeafInfo {

  /** whether the TreeLeaf is the currently marked item in a treeview 
    * structure. */
  private boolean marked;

  private TreeNodeShift treeNodeShift;
  
  
  /** <p>constructs a new TreeLeafInfo with the specified information.</p> */
  public TreeLeafInfo( final boolean marked, 
                       final TreeNodeShift treeNodeShift ) 
  {
    this.marked = marked;
    this.treeNodeShift = treeNodeShift;
  }
  
  
  // attribute getters
  ////////////////////

  /** <p>returns whether the TreeLeaf is the currently marked item in 
    * a treeview structure.</p> */
  public boolean isMarked() {
    return marked;
  }

  public TreeNodeShift getTreeNodeShift() {
    return treeNodeShift;
  }
}