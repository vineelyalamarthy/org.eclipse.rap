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
package com.w4t.dhtml;

import com.w4t.dhtml.event.*;
import com.w4t.dhtml.renderinfo.TreeViewInfo;
import com.w4t.event.WebActionListener;
import com.w4t.event.WebRenderListener;
import com.w4t.internal.adaptable.IRenderInfoAdapter;
import com.w4t.internal.adaptable.RenderInfoAdapter;

/**
  * <p>The root class for TreeView components in the W4Toolkit. The root node
  * itself is invisible, a tree is created by adding TreeNodes or TreeLeaves
  * to the Treeview or to TreeNodes which are already added to the tree.</p>
  * <p>Trees may be created by adding TreeNode and TreeLeaf objects to an
  * instance of TreeView. Alternatively, a subclass of TreeDataSource may
  * be used to build a tree from a database table containing the necessary
  * information about the tree structure.</p>
  */
public class TreeView extends TreeNode {

  /* image paths for the treeview gifs */

  /** <p>the image name suffix for the image displayed on an expanded node
    * of the tree.</p> */
  public final static String EXPANDED_IMG = "Exp.gif";
  /** <p>the image name suffix for the image displayed on an collapsed node
    * of the tree.</p> */
  public final static String COLLAPSED_IMG = "Col.gif";
  /** <p>the image name and path for the image displayed on leaf of
    * the tree.</p> */
  public final static String LEAF_IMG
    = "resources/images/treeview/document.gif";

  private TreeNodeShift treeNodeShift; 
  private Object renderInfoAdapter;

  
  /** <p>constructs a new TreeView.</p> */
  public TreeView() {
    init();
  }
  
  private void init() {
    setImageSetName( DEFAULT_IMAGE_SET );
  }
  
  /** <p>returns a copy of TreeView.</p> */
  public Object clone() throws CloneNotSupportedException {
    TreeView result = ( TreeView )super.clone();
    result.init();
    return result;  
  }  

  public Object getAdapter( final Class adapter ) {
    Object result;
    if( adapter == IRenderInfoAdapter.class ) {
      result = getRenderInfoAdapter();
    } else {
      result = super.getAdapter( adapter );      
    }
    return result;
  }

  private Object getRenderInfoAdapter() {
    if( renderInfoAdapter == null ) {
      renderInfoAdapter = new RenderInfoAdapter() {
        private TreeViewInfo renderInfo;
        public Object getInfo() {
          return renderInfo;
        }
        public void createInfo() {
          renderInfo = new TreeViewInfo( tbStateInfoFields, 
                                         nodeList, 
                                         leafList,
                                         treeNodeShift );
        }
      };
    }
    return renderInfoAdapter;
  }

  
  // event handling methods
  /////////////////////////

  /** <p>adds the specified WebTreeNodeExpandedListener to all TreeNodes
    * which are added to this TreeView.</p>
    * <p>Note: all listeners on the TreeView react to events on all TreeNodes
    * which are added to the TreeView. The behaviour defined in the listener
    * will therefore be shared between all nodes of the tree. To achieve
    * special behavior of single TreeNodes on events add the listener to
    * the TreeNode itself.</p> */
  public void addWebTreeNodeExpandedListener( 
    final WebTreeNodeExpandedListener listener ) 
  {
    super.addWebTreeNodeExpandedListener( listener );
    for( int i = 0; i < nodeList.size(); i++ ) {
      TreeNode childNode = ( TreeNode )nodeList.get ( i );
      childNode.addWebTreeNodeExpandedListenerRecursively( listener );
    }
  }

  /** <p>removes the specified WebTreeNodeExpandedListener from all TreeNodes
    * which are added to this TreeView.</p>
    * <p>Note: all listeners on the TreeView react to events on all TreeNodes
    * which are added to the TreeView. The behaviour defined in the listener
    * will therefore be shared between all nodes of the tree. To achieve
    * special behavior of single TreeNodes on events add the listener to
    * the TreeNode itself.</p> */
  public void removeWebTreeNodeExpandedListener(
    final WebTreeNodeExpandedListener listener ) 
  {
    super.removeWebTreeNodeExpandedListener( listener );
    for( int i = 0; i < nodeList.size(); i++ ) {
      TreeNode childNode = ( TreeNode )nodeList.get ( i );
      childNode.removeWebTreeNodeExpandedListenerRecursively( listener );
    }
  }

  /** <p>adds the specified WebTreeNodeCollapsedListener to all TreeNodes
    * which are added to this TreeView.</p>
    * <p>Note: all listeners on the TreeView react to events on all TreeNodes
    * which are added to the TreeView. The behaviour defined in the listener
    * will therefore be shared between all nodes of the tree. To achieve
    * special behavior of single TreeNodes on events add the listener to
    * the TreeNode itself.</p> */
  public void addWebTreeNodeCollapsedListener(
    final WebTreeNodeCollapsedListener listener )
  {
    super.addWebTreeNodeCollapsedListener( listener );
    for( int i = 0; i < nodeList.size(); i++ ) {
      TreeNode childNode = ( TreeNode )nodeList.get ( i );
      childNode.addWebTreeNodeCollapsedListenerRecursively( listener );
    }
  }

  /** <p>removes the specified WebTreeNodeCollapsedListener from all TreeNodes
    * which are added to this TreeView.</p>
    * <p>Note: all listeners on the TreeView react to events on all TreeNodes
    * which are added to the TreeView. The behaviour defined in the listener
    * will therefore be shared between all nodes of the tree.To achieve
    * special behavior of single TreeNodes on events add the listener to
    * the TreeNode itself.</p> */
  public void removeWebTreeNodeCollapsedListener(
    final WebTreeNodeCollapsedListener listener )
  {
    super.removeWebTreeNodeCollapsedListener( listener );
    for( int i = 0; i < nodeList.size(); i++ ) {
      TreeNode childNode = ( TreeNode )nodeList.get ( i );
      childNode.removeWebTreeNodeCollapsedListenerRecursively( listener );
    }
  }

  /** <p>adds the specified DragDropListener to all TreeNodes which are
    * added to this TreeView.</p>
    * <p>Note: all listeners on the TreeView react to events on all TreeNodes
    * which are added to the TreeView. The behaviour defined in the listener
    * will therefore be shared between all nodes of the tree. To achieve
    * special behavior of single TreeNodes on events add the listener to
    * the TreeNode itself.</p> */
  public void addDragDropListener( final DragDropListener listener ) {
    super.addDragDropListener( listener );
    int size = nodeList.size();
    for( int i = 0; i < size; i++ ) {
      TreeNode childNode = ( TreeNode )nodeList.get ( i );
      childNode.addDragDropListenerRecursively( listener );
    }
  }

  /** <p>removes the specified DragDropListener from all TreeNodes which
    * are added to this TreeView.</p>
    * <p>Note: all listeners on the TreeView react to events on all TreeNodes
    * which are added to the TreeView. The behaviour defined in the listener
    * will therefore be shared between all nodes of the tree.To achieve
    * special behavior of single TreeNodes on events add the listener to
    * the TreeNode itself.</p> */
  public void removeDragDropListener( final DragDropListener listener ) {
    super.removeDragDropListener( listener );
    int size = nodeList.size();
    for( int i = 0; i < size; i++ ) {
      TreeNode childNode = ( TreeNode )nodeList.get ( i );
      childNode.removeDragDropListenerRecursively( listener );
    }
  }

  /** <p>adds the specified WebActionListener to all TreeNodes
    * which are added to this TreeView.</p>
    * <p>Note: all listeners on the TreeView react to events on all TreeNodes
    * which are added to the TreeView. The behaviour defined in the listener
    * will therefore be shared between all nodes of the tree. To achieve
    * special behavior of single TreeNodes on events add the listener to
    * the TreeNode itself.</p> */
  public void addWebActionListener( final WebActionListener listener ) {
    super.addWebActionListener( listener );
    for( int i = 0; i < nodeList.size(); i++ ) {
      TreeNode childNode = ( TreeNode )nodeList.get ( i );
      childNode.addWebActionListenerRecursively( listener );
    }
    for( int i = 0; i < leafList.size(); i++ ) {
      TreeLeaf leaf = ( TreeLeaf )leafList.get( i );
      leaf.addWebActionListener( listener );
    }
  }

  /** <p>removes the specified WebActionListener from all TreeNodes
    * which are added to this TreeView.</p>
    * <p>Note: all listeners on the TreeView react to events on all TreeNodes
    * which are added to the TreeView. The behaviour defined in the listener
    * will therefore be shared between all nodes of the tree.To achieve
    * special behavior of single TreeNodes on events add the listener to
    * the TreeNode itself.</p> */
  public void removeWebActionListener( final WebActionListener listener ) {
    super.removeWebActionListener( listener );
    for( int i = 0; i < nodeList.size(); i++ ) {
      TreeNode childNode = ( TreeNode )nodeList.get ( i );
      childNode.removeWebActionListenerRecursively( listener );
    }
    for( int i = 0; i < leafList.size(); i++ ) {
      TreeLeaf leaf = ( TreeLeaf )leafList.get( i );
      leaf.removeWebActionListener( listener );
    }
  }

  /** <p>adds the specified WebRenderListener to all TreeNodes
    * which are added to this TreeView.</p>
    * <p>Note: all listeners on the TreeView react to events on all TreeNodes
    * which are added to the TreeView. The behaviour defined in the listener
    * will therefore be shared between all nodes of the tree. To achieve
    * special behavior of single TreeNodes on events add the listener to
    * the TreeNode itself.</p> */
  public void addWebRenderListener( final WebRenderListener listener ) {
    super.addWebRenderListener( listener );
    for( int i = 0; i < nodeList.size(); i++ ) {
      TreeNode childNode = ( TreeNode )nodeList.get ( i );
      childNode.addWebRenderListenerRecursively( listener );
    }
    for( int i = 0; i < leafList.size(); i++ ) {
      TreeLeaf leaf = ( TreeLeaf )leafList.get( i );
      leaf.addWebRenderListener( listener );
    }
  }

  /** <p>removes the specified WebRenderListener from all TreeNodes
    * which are added to this TreeView.</p>
    * <p>Note: all listeners on the TreeView react to events on all TreeNodes
    * which are added to the TreeView. The behaviour defined in the listener
    * will therefore be shared between all nodes of the tree.To achieve
    * special behavior of single TreeNodes on events add the listener to
    * the TreeNode itself.</p> */
  public void removeWebRenderListener( final WebRenderListener listener ) {
    super.removeWebRenderListener( listener );
    for( int i = 0; i < nodeList.size(); i++ ) {
      TreeNode childNode = ( TreeNode )nodeList.get ( i );
      childNode.removeWebRenderListenerRecursively( listener );
    }
    for( int i = 0; i < leafList.size(); i++ ) {
      TreeLeaf leaf = ( TreeLeaf )leafList.get( i );
      leaf.removeWebRenderListener( listener );
    }
  }

  /** TODO:[fappel] comment */
  public void addDoubleClickListener( final DoubleClickListener listener ) {
    addDoubleClickListenerRecursively( listener );
  }

  /** TODO:[fappel] comment */
  public void removeDoubleClickListener( DoubleClickListener listener ) {
    removeDoubleClickListenerRecursively( listener );
  }

  TreeNodeShift getTreeNodeShift() {
    return treeNodeShift;
  }
  
  // attribute getters and setters
  ////////////////////////////////

  /** <p>returns the TreeNode which is marked on this TreeView.</p>
    * @deprecated use getMarkedItem() */
  public TreeNode getMarkedNode() {
    TreeNode markedNode = null;
    if( tdItems != null ) {
      markedNode = ( TreeNode )tdItems.getMarkedItem();
    }
    return markedNode;
  }

  /** <p>returns the Item which is marked on this TreeView.</p> */
  public Item getMarkedItem() {
    Item result = null;
    if( tdItems != null ) {
      result = tdItems.getMarkedItem();
    }
    return result;
  }
  
  
  /** <p>sets the specified number as minimal child node number for
    * dynamic loading to all TreeNodes added to this TreeView.</p>
    * <p>Note: You may also set the minimal child node number for
    * dynamic loading on a single TreeNode, which overrides this setting
    * for that TreeNode, if is set later.</p> */
  public void setMinChildsDynLoad( final int minChildsDynLoad ) {
    this.minChildsDynLoad = minChildsDynLoad;
    for( int i = 0; i < nodeList.size(); i++ ) {
      TreeNode childNode = ( TreeNode )nodeList.get ( i );
      childNode.setMinChildsDynLoadRecursively( minChildsDynLoad );
    }
  }

  /** <p>returns the minimal child node number for dynamic loading which
    * is set all TreeNodes added to this TreeView.</p>
    * <p>Note: You may also set the minimal child node number for
    * dynamic loading on a single TreeNode, which overrides this setting
    * for that TreeNode, if is set later. Get this with getMinChildsDynLoad()
    * in org.eclipse.rap.dhtml.TreeNode .</p> */
  public int getMinChildsDynLoad(  ) {
    return this.minChildsDynLoad;
  }

  /** <p>sets the specified dynamic loading mode identifier for dynamic
    * loading to all TreeNodes added to this TreeView. This must be one
    * of TreeNode.DYNLOAD_NEVER, TreeNode.DYNLOAD_ALWAYS and
    * TreeNode.DYNLOAD_DYNAMIC.</p>
    * <p>Note: You may also set the dynamic loading mode  on a single
    * TreeNode, which overrides this setting for that TreeNode, if is set
    * later.</p> */
  public void setDynLoading( final String dynLoading ) {
    super.setDynLoading( dynLoading );
    for( int i = 0; i < nodeList.size(); i++ ) {
      TreeNode childNode = ( TreeNode )nodeList.get ( i );
      childNode.setDynLoadingRecursively( dynLoading );
    }
  }

  /** <p>returns the dynamic loading mode identifier for dynamic
    * loading which is set all TreeNodes added to this TreeView. This is
    * one of TreeNode.DYNLOAD_NEVER, TreeNode.DYNLOAD_ALWAYS and
    * TreeNode.DYNLOAD_DYNAMIC.</p>
    * <p>Note: You may also set the dynamic loading mode on a single TreeNode,
    * which overrides this setting for that TreeNode, if is set later. Get
    * this with getDynLoading() in org.eclipse.rap.dhtml.TreeNode .</p> */
  public String getDynLoading(  ) {
    return this.dynLoading;
  }

  /** <p>returns the items belonging (i.e. added to) this TreeView and all 
   * of its child nodes. Note that the invisible TreeView itself counts 
   * into the returned number as well.</p> */
  public int getTreeItemCount() {
    return tdItems.getItemCount();
  }
  
  /** <p>sets the image set name for this TreeView. Note that the TreeView
    * component itself is not visible. The name set here applies to all nodes
    * added to this TreeView.</p>
    *
    * <p>Currently only gif images are supported.</p> 
    */
  public void setImageSetName( final String imageSetName ) {
    super.setImageSetName( imageSetName );
    treeNodeShift = new TreeNodeShift( imageSetName );
  }
  
  public static String retrieveIconName() {
    return "resources/images/icons/treeview.gif";
  }
}