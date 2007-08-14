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

import org.eclipse.rwt.internal.lifecycle.HtmlResponseWriter;

import com.w4t.Style;
import com.w4t.dhtml.event.*;
import com.w4t.dhtml.renderinfo.TreeNodeInfo;
import com.w4t.event.*;
import com.w4t.internal.adaptable.IRenderInfoAdapter;
import com.w4t.internal.adaptable.RenderInfoAdapter;
import com.w4t.types.WebColor;
import com.w4t.util.DefaultColorScheme;


/** 
 * <p>A Node for the TreeView.</p>
 */
public class TreeNode extends Node {

  private static final ToggleLoadingListener TOGGLE_LOADING_LISTENER 
    = new ToggleLoadingListener();
  private static final String ITEM_MARKED_FONT 
    = DefaultColorScheme.get( DefaultColorScheme.ITEM_MARKED_FONT );
  private static final String ITEM_MARKED_BG
    = DefaultColorScheme.get( DefaultColorScheme.ITEM_MARKED_BG );
  private static final WebColor ITEM_MARKED_FONT_COLOR 
    = new WebColor( ITEM_MARKED_FONT );
  private static final WebColor ITEM_MARKED_BG_COLOR 
    = new WebColor( ITEM_MARKED_BG );
  private static final WebColor ITEM_FONT_COLOR 
    = new WebColor( DefaultColorScheme.get( DefaultColorScheme.ITEM_FONT ) );
  
  /** <p>constant value for a mode of dynamic reloading of this treeNode.</p>
    * <p><i>Never</i> loading means, that this TreeNode is always completely
    * rendered.</p>
    * <p>This is recommended for small trees which are not supposed to change
    * at runtime.</p> */
  public static final String DYNLOAD_NEVER   = "never";
  /** <p>constant value for a mode of dynamic reloading of this treeNode.</p>
    * <p><i>Dynamic</i> loading means, that this TreeNode is not completely
    * rendered, but only rendered on demand, if a certain (configurable )
    * number of child nodes is exceeded (see setMinChildsDynLoad() ).</p> */
  public static final String DYNLOAD_DYNAMIC = "dynamic";
  /** <p>constant value for a mode of dynamic reloading of this treeNode.</p>
    * <p><i>Always</i> loading means, that this TreeNode is not completely
    * rendered, but only rendered on demand, independent of the number of
    * child nodes.</p> */
  public static final String DYNLOAD_ALWAYS  = "always";

  /** <p>the image set name that is set by default on tree nodes.</p> */
  public static final String DEFAULT_IMAGE_SET 
    = "resources/images/treeview/modern/Modern";

  /** helping list for render(): contains the html code for the hidden
    * input fields which are used to store for every TreeNode the information
    * whether it is expanded or collapsed. */
  protected HtmlResponseWriter tbStateInfoFields;

  /** <p>the number of child nodes and leaves added to this TreeNode.</p> */
  protected int childCount = 0;
  /** <p>if dynamic loading for the branch starting with this TreeNode is
    * enabled, and this minimum number of children is exceeded, the branch is
    * only rendered if it is to be shown.</p> */
  protected String dynLoading = DYNLOAD_DYNAMIC;
  /** <p>the minimal child node number for dynamic loading to this
    * TreeNode.</p> */
  protected int minChildsDynLoad = 50;
  /** <p>the name (as pathname) of the images which are displayed as
    * representation of this TreeNode in expanded and collapsed state.</p>
    * <p>Images must be located at the specified path.</p>
    * <p>Currently only gif images are supported.</p> */
  protected String imageSetName = "";

  /** tells, whether this TreeNode to expanded state, i.e. the childs
    * on the level immediately below this node are visible. */
  private boolean expanded = false;

  /** helping variable for rendering: the String to be rendered into the layers
    * for visibility. */
  protected String expansion = "vis_hide";
  
  /** the background color this TreeNode has when it is marked. */
  private WebColor markedBgColor;
  /** the color this TreeNode has when it is marked. */  
  private WebColor markedColor;
  /** the css attribute encapsulation for this TreeNode */
  private Style style;
  /** tooltip displayed on mouseover with this TreeNode */
  private String title = "";
  private Object renderInfoAdapter;

  /** creates a new instance of TreeNode */
  public TreeNode() {
    markedBgColor = ITEM_MARKED_BG_COLOR;
    markedColor = ITEM_MARKED_FONT_COLOR;
    tbStateInfoFields = new HtmlResponseWriter();
  }
  
  private void initStyle() {
    style = new Style();
    style.setTextDecoration( "none" );
    style.setColor( ITEM_FONT_COLOR );
    style.setWhiteSpace( "nowrap" );    
  }
  
  /** <p>returns a copy of Node.</p> */
  public Object clone() throws CloneNotSupportedException {
    TreeNode result = ( TreeNode )super.clone();
    if( style != null ) {
       result.initStyle();
    }
    return result;
  }  
  
  private TreeNodeInfo doCreateInfo() {
    this.removeWebTreeNodeExpandedListener( TOGGLE_LOADING_LISTENER );
    boolean marked = tdItems.isMarked( itemID );
    TreeNodeShift treeNodeShift = this.findRoot().getTreeNodeShift();
    
    return new TreeNodeInfo( tbStateInfoFields, 
                             nodeList, 
                             leafList,
                             this,
                             marked,
                             TOGGLE_LOADING_LISTENER,
                             expansion,
                             treeNodeShift );
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
        private TreeNodeInfo renderInfo;
        public Object getInfo() {
          return renderInfo;
        }
        public void createInfo() {
          renderInfo = doCreateInfo();
        }
      };
    }
    return renderInfoAdapter;
  }

  
  /** <p>adds the specified WebTreeNodeExpandedListener to this
    * TreeNode.</p> */
  public void addWebTreeNodeExpandedListener( 
    final WebTreeNodeExpandedListener listener )
  {
    WebTreeNodeExpandedEvent.addListener( this, listener );
  }

  /** <p>Removes the specified <code>WebTreeNodeExpandedListener</code> from
   * this TreeNode.</p> 
   * <p>This method performs no function, nor does it throw an exception, if 
   * the given <code>listener</code> was not previously added to this component.
   * If listener is <code>null</code>, no exception is thrown and no action 
   * is performed.</p>
   * @param listener the <code>WebTreeNodeExpandedListener</code> to be removed.
   */
  public void removeWebTreeNodeExpandedListener(
    final WebTreeNodeExpandedListener listener ) 
  {
    WebTreeNodeExpandedEvent.removeListener( this, listener );
  }

  /** <p>Adds the specified WebTreeNodeCollapsedListener to this
    * TreeNode.</p> */
  public void addWebTreeNodeCollapsedListener(
    final WebTreeNodeCollapsedListener listener )
  {
    WebTreeNodeCollapsedEvent.addListener( this, listener );
  }

  /** 
   * <p>Removes the specified <code>WebTreeNodeCollapsedListener</code> from
   * this TreeNode.</p>
   * <p>This method performs no function, nor does it throw an exception, if 
   * the given <code>listener</code> was not previously added to this component.
   * If listener is <code>null</code>, no exception is thrown and no action 
   * is performed.</p>
   * @param listener the <code>WebTreeNodeCollapsedListener</code> to be 
   * removed.
   * */
  public void removeWebTreeNodeCollapsedListener(
    final WebTreeNodeCollapsedListener listener ) 
  {
    WebTreeNodeCollapsedEvent.removeListener( this, listener );
  }

  /** <p>adds the specified WebTreeNodeExpandedListener to this
    * TreeNode and recursively to all of its child nodes.</p> */
  protected void addWebTreeNodeExpandedListenerRecursively(
    final WebTreeNodeExpandedListener listener ) 
  {
    this.addWebTreeNodeExpandedListener( listener );
    int size = nodeList.size();
    for( int i = 0; i < size; i++ ) {
      TreeNode childNode = ( TreeNode )nodeList.get ( i );
      childNode.addWebTreeNodeExpandedListenerRecursively( listener );
    }
  }

  /** <p>removes the specified WebTreeNodeExpandedListener from
    * this TreeNode and from all of its child nodes.</p> */
  protected void removeWebTreeNodeExpandedListenerRecursively(
    final WebTreeNodeExpandedListener listener )
  {
    this.removeWebTreeNodeExpandedListener( listener );
    int size = nodeList.size();
    for( int i = 0; i < size; i++ ) {
      TreeNode childNode = ( TreeNode )nodeList.get ( i );
      childNode.removeWebTreeNodeExpandedListenerRecursively( listener );
    }
  }

  /** <p>Adds the specified WebTreeNodeCollapsedListener to this
    * TreeNode and to all of its child nodes.</p> */
  protected void addWebTreeNodeCollapsedListenerRecursively(
    final WebTreeNodeCollapsedListener listener )
  {
    this.addWebTreeNodeCollapsedListener( listener );
    int size = nodeList.size();
    for( int i = 0; i < size; i++ ) {
      TreeNode childNode = ( TreeNode )nodeList.get ( i );
      childNode.addWebTreeNodeCollapsedListenerRecursively( listener );
    }
  }

  /** <p>Removes the specified WebTreeNodeCollapsedListener from
    * this TreeNode and from all of its child nodes.</p> */
  protected void removeWebTreeNodeCollapsedListenerRecursively(
    final WebTreeNodeCollapsedListener listener )
  {
    this.removeWebTreeNodeCollapsedListener( listener );
    int size = nodeList.size();
    for( int i = 0; i < size; i++ ) {
      TreeNode childNode = ( TreeNode )nodeList.get ( i );
      childNode.removeWebTreeNodeCollapsedListenerRecursively( listener );
    }
  }

  /** <p>Adds the specified DragDropListener to this TreeNode.</p> */
  public void addDragDropListener( final DragDropListener listener ) {
    DragDropEvent.addListener( this, listener );
  }

  /** 
   * <p>Removes the specified <code>DragDropListener</code> from this TreeNode.
   * </p> 
   * <p>This method performs no function, nor does it throw an exception, if 
   * the given <code>listener</code> was not previously added to this component.
   * If listener is <code>null</code>, no exception is thrown and no action 
   * is performed.</p>
   * @param listener the <code>DragDropListener</code> to be removed.
   */
  public void removeDragDropListener( final DragDropListener listener ) {
    DragDropEvent.removeListener( this, listener );
  }

  /** 
   * <p>Adds the specified <code>DoubeClickListener</code> to receive double-
   * click events from this item. These events occur when a user double-clicks
   * this item.</p>
   * @param listener the <code>DoubleClickListener</code> to be added. Must
   * not be <code>null</code>.
   */
  public void addDoubleClickListener( final DoubleClickListener listener ) {
    DoubleClickEvent.addListener( this, listener );
  }
  
  /** 
   * <p>Removes the specified <code>DoubleClickListener</code> so that it no 
   * longer receives double-click events from this item.</p>
   * <p>This method performs no function, nor does it throw an exception, if 
   * the given <code>listener</code> was not previously added to this component.
   * If listener is <code>null</code>, no exception is thrown and no action 
   * is performed.</p>
   * @param listener the <code>DoubleClickListener</code> to be removed.
   */
  public void removeDoubleClickListener( final DoubleClickListener listener ) {
    DoubleClickEvent.removeListener( this , listener );
  }
  
  /** <p>Adds the specified <code>DoubleClickListener</code> to this TreeNode 
   * and to all of its child nodes.</p> */
  protected void addDoubleClickListenerRecursively( 
    final DoubleClickListener listener ) 
  {
    DoubleClickEvent.addListener( this, listener );
    for( int i = 0; i < nodeList.size(); i++ ) {
      TreeNode childNode = ( TreeNode )nodeList.get ( i );
      childNode.addDoubleClickListenerRecursively( listener );
    }
    for( int i = 0; i < leafList.size(); i++ ) {
      TreeLeaf leaf = ( TreeLeaf )leafList.get( i );
      leaf.addDoubleClickListener( listener );
    }
  }
  
  /** <p>Removes the specified <code>DoubleClickListener</code> from
   * this TreeNode and from all of its child nodes.</p> */
  protected void removeDoubleClickListenerRecursively(
    final DoubleClickListener listener )
  {
    DoubleClickEvent.removeListener( this, listener );
    for( int i = 0; i < nodeList.size(); i++ ) {
      TreeNode childNode = ( TreeNode )nodeList.get ( i );
      childNode.removeDoubleClickListenerRecursively( listener );
    }
    for( int i = 0; i < leafList.size(); i++ ) {
      TreeLeaf leaf = ( TreeLeaf )leafList.get( i );
      leaf.removeDoubleClickListener( listener );
    }
  }

  
  /** <p>Adds the specified DragDropListener to this TreeNode and to
    * all of its child nodes.</p> */
  protected void addDragDropListenerRecursively( 
    final DragDropListener listener )
  {
    this.addDragDropListener( listener );
    int size = nodeList.size();
    for( int i = 0; i < size; i++ ) {
      TreeNode childNode = ( TreeNode )nodeList.get ( i );
      childNode.addDragDropListenerRecursively( listener );
    }
  }

  /** <p>Removes the specified DragDropListener from this TreeNode and
    * from all of its child nodes.</p> */
  protected void removeDragDropListenerRecursively( 
    final DragDropListener listener )
  {
    this.removeDragDropListener( listener );
    int size = nodeList.size();
    for( int i = 0; i < size; i++ ) {
      TreeNode childNode = ( TreeNode )nodeList.get ( i );
      childNode.removeDragDropListenerRecursively( listener );
    }
  }

  /** <p>Sets this TreeNode to expanded state, i.e. the childs on the level
    * immediately below this node are visible.</p> */
  public void setExpanded( final boolean expanded ) {
    this.expanded  = expanded;

    if( expanded ) {
      // set all parents up to root to expanded = true
      if( parentNode != null ) {
        ( ( TreeNode )parentNode ).setExpanded( true );
      }
    } else {
      // set all children to expanded =  false
      int size = nodeList.size();
      for( int i = 0; i < size; i++ ) {
        TreeNode child = ( TreeNode )nodeList.get( i );
        child.setExpanded( false );
      }
    }
    expansion = expanded ? "vis_show" : "vis_hide";
  }

  /** <p>returns, whether this TreeNode to expanded state, i.e. the childs
    * on the level immediately below this node are visible.</p> */
  public boolean isExpanded() {
    return expanded;
  }

  /** <p>Sets the specified number as minimal child node number for
    * dynamic loading to this TreeNode.</p>
    * <p>Note: the minimal child node number for dynamic loading can be
    * set on the TreeView, to which this TreeNode is added; this will apply it
    * to all TreeNodes added to the TreeView. If you set the number on this
    * TreeNode, it applies only to this TreeNode.</p> */
  public void setMinChildsDynLoad( final int minChildsDynLoad ) {
    this.minChildsDynLoad = minChildsDynLoad;
  }

  /** <p>Returns the minimal child node number for dynamic loading
    * which is set on this TreeNode.</p>
    * <p>Note: the minimal child node number for dynamic loading can be
    * set on the TreeView, to which this TreeNode is added; this will apply it
    * to all TreeNodes added to the TreeView. If you set the number on this
    * TreeNode only, it applies only to this TreeNode.</p> */
  public int getMinChildsDynLoad(  ) {
    return this.minChildsDynLoad;
  }

  /** <p>Sets the specified dynamic loading mode identifier for dynamic
    * loading to this TreeNode. This must be one of TreeNode.DYNLOAD_NEVER,
    * TreeNode.DYNLOAD_ALWAYS and TreeNode.DYNLOAD_DYNAMIC.</p>
    * <p>Note: the dynamic loading mode can be set on the TreeView, to which
    * this TreeNode is added; this will apply it to all TreeNodes added to
    * the TreeView. If you set the mode on this TreeNode, it applies
    * only to this TreeNode.</p> */
  public void setDynLoading( final String dynLoading ) {
    if(    !dynLoading.equals( DYNLOAD_NEVER )
        && !dynLoading.equals( DYNLOAD_ALWAYS )
        && !dynLoading.equals( DYNLOAD_DYNAMIC ) )
    {
      String msg = "No valid paramater for dynamic loading.";
      throw new IllegalArgumentException( msg );
    }
    this.dynLoading = dynLoading;
  }

  /** <p>Returns the dynamic loading mode identifier for dynamic
    * loading to this TreeNode. This is one of TreeNode.DYNLOAD_NEVER,
    * TreeNode.DYNLOAD_ALWAYS and TreeNode.DYNLOAD_DYNAMIC.</p>
    * <p>Note: the dynamic loading mode can be set on the TreeView, to which
    * this TreeNode is added; this will apply it to all TreeNodes added to
    * the TreeView. If you set the mode on this TreeNode, it applies
    * only to this TreeNode.</p> */
  public String getDynLoading(  ) {
    return this.dynLoading;
  }

  /** <p>Returns the topmost parent node of this TreeNode, i.e. the parent or 
    * parent of a parent of this TreeNode, which has no more parent above.</p>
    * <p>Note that the return value of this is not necessarily of type 
    * TreeView.</p>
    * <p>If there is no parent node above this TreeNode, a reference to this 
    * TreeNode itself is returned.</p> */
  public TreeNode getRootNode() {
    TreeNode result = this;
    TreeNode temp = this;
    while( temp != null ) {
      result = temp;
      temp = ( TreeNode )result.getParentNode();
    }
    return result;
  }
  
  /** <p>Determines and returns the count of children which are added to
    * this TreeNode (recursively, i.e. all children and children of 
    * children etc. are counted).</p> */
  public int getChildCount() {
    this.childCount = 0;

    int size = nodeList.size();
    for( int i = 0; i < size; i++ ) {
      TreeNode child = ( TreeNode )nodeList.get( i );
      this.childCount += child.getChildCount() + 1;
    }
    this.childCount += leafList.size();

    return this.childCount;
  }


  // inherited and overriden methods
  //////////////////////////////////

  /** 
   * <p>Adds the specified item as child to this TreeNode.</p>
   * <p>Note that all event listeners which are set to the root node of this 
   * TreeNode (which is either a {@link org.eclipse.rwt.dhtml.TreeView TreeView} or the
   * root node of a branch of TreeNodes) are added to item. This applies 
   * also to dynloading settings (see {@link #setMinChildsDynLoad(int) 
   * setMinChildsDynLoad(int)} and {@link #setDynLoading(String) 
   * setDynLoading(String)}).If you want to set a special dynloading for
   * item, apply it after you added it.</p> */
  public void addItem( final Item item ) {
    super.addItem( item );

    TreeView root = findRoot();
    if( root != null ) {
      // must add the action listener of root, but cannot use
      // addWebActionListener of root, which is recursive
      Object[] actionListeners = WebActionEvent.getListeners( root );
      for( int i = 0; i < actionListeners.length; i++ ) {
        item.addWebActionListener( ( WebActionListener )actionListeners[ i ] );
      }
      
      Object[] dblClickListeners = DoubleClickEvent.getListeners( root );
      for( int i = 0; i < dblClickListeners.length; i++ ) {
        DoubleClickListener dblClickListener
          = ( DoubleClickListener )dblClickListeners[ i ];
        DoubleClickEvent.addListener( item, dblClickListener );
      }
      
      Object[] renderListeners = WebRenderEvent.getListeners( root );
      for( int i = 0; i < renderListeners.length; i++ ) {
        item.addWebRenderListener( ( WebRenderListener )renderListeners[ i ] );
      }
      
      // with TreeNode specific listeners dito
      if( item instanceof TreeNode ) {
        TreeNode treeNode = ( TreeNode )item;
        
        Object[] dragDropListeners = DragDropEvent.getListeners( root );
        for( int i = 0; i < dragDropListeners.length; i++ ) {
          DragDropListener dragDropListener
            = ( DragDropListener )dragDropListeners[ i ];
          treeNode.addDragDropListener( dragDropListener );
        }
        
        Object[] expandedListeners 
          = WebTreeNodeExpandedEvent.getListeners( root );
        for( int i = 0; i < expandedListeners.length; i++ ) {
          WebTreeNodeExpandedListener expandedListener
            = ( WebTreeNodeExpandedListener )expandedListeners[ i ];
          treeNode.addWebTreeNodeExpandedListener( expandedListener );
        }
        
        Object[] collapsedListeners
          = WebTreeNodeCollapsedEvent.getListeners( root );
        for( int i = 0; i < collapsedListeners.length; i++ ) {
          WebTreeNodeCollapsedListener collapsedListener
            = ( WebTreeNodeCollapsedListener )collapsedListeners[ i ];
          treeNode.addWebTreeNodeCollapsedListener( collapsedListener );
        }
        
        treeNode.setDynLoading( this.getDynLoading() );
        treeNode.setMinChildsDynLoad( this.getMinChildsDynLoad() );
      }
    }
  }

  /** <p>Removes the specified item from this TreeNode, removing also all
    * listeners from it which were set to it from the root of this tree.</p>
    */
  public void removeItem( final Item item ) {
    super.removeItem( item );

    TreeView root = findRoot();
    if( root != null ) {
      // must remove the action listener of root, but cannot use
      // removeWebActionListener of root, which is recursive
      Object[] actionListeners = WebActionEvent.getListeners( root );
      for( int i = 0; i < actionListeners.length; i++ ) {
        WebActionListener actionListener
          = ( WebActionListener )actionListeners[ i ];
        item.removeWebActionListener( actionListener );
      }

      Object[] dblClickListeners = DoubleClickEvent.getListeners( root );
      for( int i = 0; i < dblClickListeners.length; i++ ) {
        DoubleClickListener dblClickListener
          = ( DoubleClickListener )dblClickListeners[ i ];
        DoubleClickEvent.removeListener( item, dblClickListener );
      }
      
      Object[] renderListeners = WebRenderEvent.getListeners( root );
      for( int i = 0; i < renderListeners.length; i++ ) {
        WebRenderListener renderListener 
          = ( WebRenderListener )renderListeners[ i ];
        item.removeWebRenderListener( renderListener );
      }
      
      // with TreeNode specific listeners dito
      if( item instanceof TreeNode ) {
        TreeNode treeNode = ( TreeNode )item;
        
        Object[] dragDropListeners = DragDropEvent.getListeners( root );
        for( int i = 0; i < dragDropListeners.length; i++ ) {
          DragDropListener dragDropListener
            = ( DragDropListener )dragDropListeners[ i ];
          treeNode.removeDragDropListener( dragDropListener );
        }
        
        Object[] expandedListeners
          = WebTreeNodeExpandedEvent.getListeners( root );
        for( int i = 0; i < expandedListeners.length; i++ ) {
          WebTreeNodeExpandedListener expandedListener
            = ( WebTreeNodeExpandedListener )expandedListeners[ i ];
          treeNode.removeWebTreeNodeExpandedListener( expandedListener );
        }
        
        Object[] collapsedListeners
          = WebTreeNodeCollapsedEvent.getListeners( root );
        for( int i = 0; i < collapsedListeners.length; i++ ) {
          WebTreeNodeCollapsedListener collapsedListener
            = ( WebTreeNodeCollapsedListener )collapsedListeners[ i ];
          treeNode.removeWebTreeNodeCollapsedListener( collapsedListener );
        }
      }
    }
  }


  // attribute getters and setters
  ////////////////////////////////
  
  /** Sets the tooltip displayed on mouseover with this TreeNode */
  public void setTitle( final String title ) {
    this.title = title;
  }
  
  /** Returns the tooltip displayed on mouseover with this TreeNode */
  public String getTitle() {
    return title;
  }
  
  /** Sets the css attribute encapsulation for this TreeNode */
  public void setStyle( final Style style ) {
    this.style = style;
  }
  
  /** Returns the css attribute encapsulation for this TreeNode */
  public Style getStyle() {
    if( style == null ) {
      initStyle();
    }
    return style;
  }  

  /** <p>Sets the color this TreeNode has when it is marked.</p> */  
  public void setMarkedColor( final WebColor markedColor ) {
    this.markedColor = markedColor;
  }
  
  /** <p>Returns the color this TreeNode has when it is marked.</p> */
  public WebColor getMarkedColor() {
    return markedColor;
  }

  /** <p>Sets the background color this TreeNode has when it is marked.</p> */  
  public void setMarkedBgColor( final WebColor markedBgColor ) {
    this.markedBgColor = markedBgColor;
  }  
  
  /** <p>Returns the background color this TreeNode has when it is 
    * marked.</p> */  
  public WebColor getMarkedBgColor() {
    return markedBgColor;
  }  
  
  /** <p>Sets the name (as pathname) of the images which are displayed as
    * representation of this TreeNode in expanded and collapsed state.</p>
    * <p>If no imageset is set on this TreeNode, the imageSetName of the 
    * parent node is used instead.</p>
    * <p>Currently only gif images are supported.</p> */
  public void setImageSetName( final String imageSetName ) {
    this.imageSetName = imageSetName;
  }

  /** <p>Returns the name (as pathname) of the images which are displayed as
    * representation of this TreeNode in expanded and collapsed state.</p>
    * <p>If no imageset is set on this TreeNode, the imageSetName of the 
    * parent node is used instead.</p>
    * <p>Currently only gif images are supported.</p> */
  public String getImageSetName() {
    String result = imageSetName;
    if( result.equals( "" ) && getParentNode() != null ) {
      result = ( ( TreeNode )getParentNode() ).getImageSetName();
    }
    return result;
  }

  /** <p>Returns, whether this TreeNode is enabled (reacts on user input)
    * or not. A WebComponent is also not enabled if it is added to a
    * WebContainer and the WebContainer to which it is added is
    * disabled.</p> */
  public boolean isEnabled() {
    boolean result =  super.isEnabled();    
    if( getParentNode() != null ) {
      TreeNode parentNode = ( TreeNode )getParentNode();
      result = result && parentNode.isEnabled();
    }
    return result;
  }

  /** helping method: sets the String to be rendered into the layers
    * for visibility. */
  public void setExpansion( final String expansion ) {
    this.expansion = expansion;
    this.expanded = expansion.equals( "vis_show" ) ? true : false;
  }
  
  
  // package private helping methods
  //////////////////////////////////

  /** helping method for rendering: adds html code for the hidden input
    * fields which are used to store for every TreeNode the information
    * whether it is expanded or collapsed. */
  public void addStateInfo( final HtmlResponseWriter stateInfo ) {
    tbStateInfoFields.append( stateInfo );
  }

  /** helping method for rendering: adds html code for the hidden input
    * fields which are used to store for every TreeNode the information
    * whether it is expanded or collapsed. */
  public void addStateInfo( final String stateInfo ) {
    tbStateInfoFields.append( stateInfo );
  }

  /** helping method; returns the root node of the tree to which this
    * TreeNode is added, if any, or null else. */
  TreeView findRoot() {
    Node result = this;
    while( result != null && !( result instanceof TreeView ) ) {
      result = result.getParentNode();
    }
    // cast is valid here, for result can be either null or instanceof TreeView
    return ( TreeView )result;
  }

  /** sets the specified number as minimal child node number for
    * dynamic loading to this TreeNode and to all of its child nodes. */
  void setMinChildsDynLoadRecursively( final int minChildsDynLoad ) {
    this.minChildsDynLoad = minChildsDynLoad;
    int size = nodeList.size();
    for( int i = 0; i < size; i++ ) {
      TreeNode childNode = ( TreeNode )nodeList.get ( i );
      childNode.setMinChildsDynLoadRecursively( minChildsDynLoad );
    }
  }

  /** sets the specified mode of dynamic loading to this TreeNode and
    * to all of its child nodes. */
  void setDynLoadingRecursively( final String dynLoading ) {
    if(    !dynLoading.equals( DYNLOAD_NEVER )
        && !dynLoading.equals( DYNLOAD_ALWAYS )
        && !dynLoading.equals( DYNLOAD_DYNAMIC ) )
    {
      String msg = "No valid paramater for dynamic loading.";
      throw new IllegalArgumentException( msg );
    }
    this.dynLoading = dynLoading;
    int size = nodeList.size();
    for( int i = 0; i < size; i++ ) {
      TreeNode childNode = ( TreeNode )nodeList.get ( i );
      childNode.setDynLoadingRecursively( dynLoading );
    }
  }

  // inner classes
  ////////////////

  /** a helping listener class which just causes a reload on the
    * branch below this TreeNode. */
  static class ToggleLoadingListener implements WebTreeNodeExpandedListener {
    public void webTreeNodeExpanded( final WebTreeNodeExpandedEvent evt ) {
      // there is nothing to do, since the event itself causes the
      // tree to be rendered again
    }
  }
}
