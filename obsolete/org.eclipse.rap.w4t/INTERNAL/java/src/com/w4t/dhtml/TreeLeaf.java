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

import com.w4t.Style;
import com.w4t.dhtml.event.DoubleClickEvent;
import com.w4t.dhtml.event.DoubleClickListener;
import com.w4t.dhtml.renderinfo.TreeLeafInfo;
import com.w4t.internal.adaptable.IRenderInfoAdapter;
import com.w4t.internal.adaptable.RenderInfoAdapter;
import com.w4t.types.WebColor;
import com.w4t.util.DefaultColorScheme;

/** <p>A Leaf for the TreeView.</p>
  */
public class TreeLeaf extends Leaf {

  private static final String ITEM_MARKED_BG 
    = DefaultColorScheme.ITEM_MARKED_BG;
  private static final String ITEM_MARKED_FONT
    = DefaultColorScheme.ITEM_MARKED_FONT;
  private static final WebColor ITEM_MARKED_FONT_COLOR 
    = new WebColor( DefaultColorScheme.get( ITEM_MARKED_FONT ) );
  private static final WebColor ITEM_MARKED_BG_COLOR 
    = new WebColor( DefaultColorScheme.get( ITEM_MARKED_BG ) );
  private static final WebColor ITEM_FONT_COLOR 
    = new WebColor( DefaultColorScheme.get( DefaultColorScheme.ITEM_FONT ) );
  /** the background color this TreeLeaf has when it is marked. */
  private WebColor markedBgColor;
  /** the color this TreeLeaf has when it is marked. */  
  private WebColor markedColor;
  /** the css attribute encapsulation for this TreeLeaf */
  private Style style;
  /** tooltip displayed on mouseover with this TreeLeaf */
  private String title = "";
  /** <p>the name (as pathname) of the images which are displayed as
    * representation of this TreeLeaf in expanded and collapsed state.</p>
    * <p>Images must be located at the specified path.</p> 
    * <p>Currently only gif images are supported.</p> */
  protected String imageSetName = "";
  private Object renderInfoAdapter;

  
  public TreeLeaf() {
    markedBgColor = ITEM_MARKED_BG_COLOR;
    markedColor = ITEM_MARKED_FONT_COLOR;        
  }
  
  private void initStyle() {
    style = new Style();
    style.setTextDecoration( "none" );
    style.setColor( ITEM_FONT_COLOR );
    style.setWhiteSpace( "nowrap" );    
  }
  
  /** <p>returns a copy of TreeLeaf.</p> */
  public Object clone() throws CloneNotSupportedException {
    TreeLeaf result = ( TreeLeaf )super.clone();
    result.style = null;
    if( style != null ) {
      result.initStyle();
    }
    return result;
  }
  
  private TreeLeafInfo doCreateInfo() {
    boolean marked = tdItems.isMarked( itemID );
    TreeView root = ( ( TreeNode )getParentNode() ).findRoot();
    TreeNodeShift treeNodeShift = root.getTreeNodeShift();
    return new TreeLeafInfo( marked, treeNodeShift );
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
        private TreeLeafInfo renderInfo;
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
  
  
  // attribute setters and getters
  ////////////////////////////////
  
  /** <p>returns, whether this TreeLeaf is enabled (reacts on user input)
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

  /** <p>sets the color this TreeLeaf has when it is marked.</p> */  
  public void setMarkedColor( final WebColor markedColor ) {
    this.markedColor = markedColor;
  }
  
  /** <p>returns the color this TreeLeaf has when it is marked.</p> */
  public WebColor getMarkedColor() {
    return markedColor;
  }

  /** <p>sets the background color this TreeLeaf has when it is marked.</p> */  
  public void setMarkedBgColor( final WebColor markedBgColor ) {
    this.markedBgColor = markedBgColor;
  }  
  
  /** <p>returns the background color this TreeLeaf has when it is 
    * marked.</p> */  
  public WebColor getMarkedBgColor() {
    return markedBgColor;
  }

  /** sets the tooltip displayed on mouseover with this TreeLeaf */
  public void setTitle( final String title ) {
    this.title = title;
  }
  
  /** returns the tooltip displayed on mouseover with this TreeLeaf */
  public String getTitle() {
    return title;
  }
  
  /** sets the css attribute encapsulation for this TreeLeaf */
  public void setStyle( final Style style ) {
    this.style = style;
  }
  
  /** returns the css attribute encapsulation for this TreeLeaf */
  public Style getStyle() {
    if( style == null ) {
      initStyle();
    }
    return style;
  }
  
  /** <p>sets the name (as pathname) of the images which are displayed as
    * representation of this TreeLeaf.</p>
    * <p>If no imageset is set on this TreeLeaf, the imageSetName of the 
    * parent node is used instead.</p>
    * <p>Currently only gif images are supported.</p> */
  public void setImageSetName( final String imageSetName ) {
    this.imageSetName = imageSetName;
  }

  /** <p>returns the name (as pathname) of the images which are displayed as
    * representation of this TreeLeaf.</p>
    * <p>If no imageset is set on this TreeLeaf, the imageSetName of the 
    * parent node is used instead.</p>
    * <p>Currently only gif images are supported.</p> */
  public String getImageSetName() {
    String result = imageSetName;
    if( result.equals( "" ) && getParentNode() != null ) {
      result = ( ( TreeNode )getParentNode() ).getImageSetName();
    }
    return result;
  }

  /** TODO:[fappel] comment */
  public void addDoubleClickListener( final DoubleClickListener listener ) {
    DoubleClickEvent.addListener( this, listener );
  }

  /** TODO:[fappel] comment */
  public void removeDoubleClickListener( final DoubleClickListener listener ) {
    DoubleClickEvent.removeListener( this, listener );
  }
}