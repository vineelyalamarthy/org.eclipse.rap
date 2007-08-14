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
package com.w4t.dhtml.treenodekit;

import java.io.IOException;

import org.eclipse.rwt.internal.lifecycle.HtmlResponseWriter;
import org.eclipse.rwt.internal.util.HTML;

import com.w4t.*;
import com.w4t.dhtml.TreeNode;
import com.w4t.dhtml.TreeNodeShift;
import com.w4t.dhtml.renderinfo.TreeNodeInfo;
import com.w4t.internal.adaptable.IRenderInfoAdapter;
import com.w4t.types.WebColor;
import com.w4t.util.DefaultColorScheme;


/** <p>the superclass of all Renderers that render org.eclipse.rap.dhtml.TreeNode.</p>
  */
public abstract class TreeNodeRenderer extends Renderer {

  public static final String TREE_NODE_TOGGLE_STATE_INFO 
    = "treeNodeToggleStateInfo";
  private static final String BG_COLOR_BUFFER = "bgColorBuffer";
  private static final String COLOR_BUFFER = "colorBuffer";
  private static final String VERTICAL_ALIGN_BUFFER = "verticalAlignBuffer";
  private static final String LAST_CHILD_BUFFER = "lastChildBuffer";
  
  public void readData( final WebComponent component ) {
    TreeNode treeNode = ( TreeNode )component;
    String uniqueID = component.getUniqueID();
    if( W4TContext.getBrowser().isScriptEnabled() ) {
      String fieldName = TreeNodeRendererUtil.getStateInfoId( uniqueID );
      String value = ReadDataUtil.findValue( fieldName );
      boolean oldExpandedState = treeNode.isExpanded();
      if( value != null ) {
        treeNode.setExpansion( value );
        if( oldExpandedState != treeNode.isExpanded() ) {
          TreeNodeRendererUtil.overrideAjaxHashCode( treeNode );
        }
      }
    } else {
      String collapsedName = NoscriptUtil.addCollapsedPrefix( uniqueID );
      collapsedName = NoscriptUtil.addSuffix( collapsedName );
      String expandedName = NoscriptUtil.addExpandedPrefix( uniqueID );
      expandedName = NoscriptUtil.addSuffix( expandedName );
      if( ReadDataUtil.findValue( collapsedName ) != null ) {
        treeNode.setExpanded( false );
      } else if( ReadDataUtil.findValue( expandedName ) != null ) {
        treeNode.setExpanded( true );
      }
    }
  }
  
  void setBgColorBuffer( final TreeNode treeNode, 
                         final WebColor bgColorBuffer )
  {
    IRenderInfoAdapter adapter = getRenderInfoAdapter( treeNode );
    adapter.addRenderState( BG_COLOR_BUFFER, bgColorBuffer );
  }
  
  WebColor getBgColorBuffer( final TreeNode treeNode ) {
    IRenderInfoAdapter adapter = getRenderInfoAdapter( treeNode );
    return ( WebColor )adapter.getRenderState( BG_COLOR_BUFFER );
  }
  
  void setColorBuffer( final TreeNode treeNode, final WebColor colorBuffer ) {
    IRenderInfoAdapter adapter = getRenderInfoAdapter( treeNode );
    adapter.addRenderState( COLOR_BUFFER, colorBuffer );  
  }
  
  WebColor getColorBuffer( final TreeNode treeNode ) {
    IRenderInfoAdapter adapter = getRenderInfoAdapter( treeNode );
    return ( WebColor )adapter.getRenderState( COLOR_BUFFER );
  }
  
  void setVerticalAlignBuffer( final TreeNode treeNode, 
                               final String verticalAlignBuffer )
  {
    IRenderInfoAdapter adapter = getRenderInfoAdapter( treeNode );
    adapter.addRenderState( VERTICAL_ALIGN_BUFFER, verticalAlignBuffer );      
  }
  
  String getVerticalAlignBuffer( final TreeNode treeNode ) {
    IRenderInfoAdapter adapter = getRenderInfoAdapter( treeNode );
    return ( String )adapter.getRenderState( VERTICAL_ALIGN_BUFFER );    
  }
  
  void setLastChildBuffer( final TreeNode treeNode, 
                           final boolean lastChildBuffer )
  {
    IRenderInfoAdapter adapter = getRenderInfoAdapter( treeNode );
    Boolean value = Boolean.valueOf( lastChildBuffer );
    adapter.addRenderState( LAST_CHILD_BUFFER, value );  
  }
  
  boolean isLastChildBuffer( final TreeNode treeNode ) {
    IRenderInfoAdapter adapter = getRenderInfoAdapter( treeNode );
    Object renderState = adapter.getRenderState( LAST_CHILD_BUFFER );
    boolean result = false;
    if( renderState != null ) {
      result = ( ( Boolean )renderState ).booleanValue();      
    }
    return result;
  }
  
  static IRenderInfoAdapter getRenderInfoAdapter( final TreeNode treeNode ) {
    Class clazz = IRenderInfoAdapter.class;
    return ( IRenderInfoAdapter )treeNode.getAdapter( clazz );
  }
  
  static boolean hasChildren( final TreeNode treeNode ) {
    TreeNodeInfo info = TreeNodeRendererUtil.getInfo( treeNode );
    return info.getNodeCount() > 0 || info.getLeafCount() > 0;
  }

  void addShiftImages( final TreeNode treeNode ) {
    if( hasChildren( treeNode ) ) {
      if( isLastChildBuffer( treeNode ) ) {
        addEmptyImage( treeNode );
      } else {
        addLineImage( treeNode );
      }
    }
  }

  static Style getStyle( final TreeNode treeNode ) {
    return treeNode.getStyle();
  }

  void createUniversalAttributes( final TreeNode treeNode ) throws IOException {
    HtmlResponseWriter out = getResponseWriter();
    String style = getStyle( treeNode ).toString();
    if( !"".equals( style ) ) {
      out.writeAttribute( HTML.CLASS,
                          out.registerCssClass( style ),
                          null );
    }
    out.writeAttribute( HTML.TITLE, getTitle( treeNode ), null );
  }
    
  static String getLabel( final TreeNode treeNode ) {
    return RenderUtil.resolve( treeNode.getLabel() );
  }
  
  static String getTitle( final TreeNode treeNode ) {
    return RenderUtil.resolve( treeNode.getTitle() );
  }
  
  static String getCollapsedIcon( final TreeNode treeNode ) {
    return getShift( treeNode ).getCollapsedIcon( treeNode.getImageSetName() );
  }
  
  String getExpandedWithChildrenIcon( final TreeNode treeNode ) {
    TreeNodeShift shift = getShift( treeNode );
    return shift.getExpandedWithChildrenIcon( treeNode.getImageSetName() );
  }
  
  String getExpandedWithoutChildrenIcon( final TreeNode treeNode ) {
    String name = treeNode.getImageSetName();
    return getShift( treeNode ).getExpandedWithoutChildrenIcon( name );
  }
  
  String getMinusLast( final TreeNode treeNode ) {
    return getShift( treeNode ).getMinusLast( treeNode.getImageSetName() );
  }

  String getMinusInner( final TreeNode treeNode ) {
    return getShift( treeNode ).getMinusInner( treeNode.getImageSetName() );
  }
  
  String getLast( final TreeNode treeNode ) {
    return getShift( treeNode ).getLast( treeNode.getImageSetName() );
  }
  
  String getInner( final TreeNode treeNode ) {
    return getShift( treeNode ).getInner( treeNode.getImageSetName() );
  }
  
  String getPlusLast( final TreeNode treeNode ) {
    return getShift( treeNode ).getPlusLast( treeNode.getImageSetName() );
  }

  String getPlusInner( final TreeNode treeNode ) {
    return getShift( treeNode ).getPlusInner( treeNode.getImageSetName() );
  }
  
  void addEmptyImage( final TreeNode treeNode ) {
    getShift( treeNode ).addEmptyImage( treeNode.getImageSetName() );
  }
  
  void addLineImage( final TreeNode treeNode ) {
    getShift( treeNode ).addLineImage( treeNode.getImageSetName() );
  }
  
  void fillBuffers( final TreeNode treeNode ) {
    setLastChildBuffer( treeNode, getShift( treeNode ).isLastChild() );  
    // check if the current item is marked
    setBgColorBuffer( treeNode, getStyle( treeNode ).getBgColor() );
    setColorBuffer( treeNode, getStyle( treeNode ).getColor() );
    setVerticalAlignBuffer( treeNode, getStyle( treeNode ).getVerticalAlign() );
    getStyle( treeNode ).setVerticalAlign( getVerticalAlign() );
    if( TreeNodeRendererUtil.getInfo( treeNode ).isMarked() ) {
      getStyle( treeNode ).setColor( treeNode.getMarkedColor() );
      getStyle( treeNode ).setBgColor( treeNode.getMarkedBgColor() );
    }
    if( !treeNode.isEnabled() ) {
      String color 
        = DefaultColorScheme.get( DefaultColorScheme.ITEM_DISABLED_FONT );
      getStyle( treeNode ).setColor( new WebColor( color ) );
    }
  }
  
  abstract String getVerticalAlign();

  void restoreFromBuffers( final TreeNode treeNode ) {
    boolean lastChildBuffer = isLastChildBuffer( treeNode );
    TreeNodeRendererUtil.getInfo( treeNode ).getTreeNodeShift().setLastChild( lastChildBuffer );
    getStyle( treeNode ).setColor( getColorBuffer( treeNode ) );
    getStyle( treeNode ).setBgColor( getBgColorBuffer( treeNode ) ); 
    getStyle( treeNode ).setVerticalAlign( getVerticalAlignBuffer( treeNode ) );
  }

  void createCollapsedNode( final TreeNode treeNode ) throws IOException {
    if( hasChildren( treeNode ) ) {
      if( isLastChildBuffer( treeNode ) ) {
        createImageTag( treeNode, getPlusLast( treeNode ) );
      } else {
        createImageTag( treeNode, getPlusInner( treeNode ) );
      }
    } else {
      if( isLastChildBuffer( treeNode ) ) {
        createImageTag( treeNode, getLast( treeNode ) );
      } else {
        createImageTag( treeNode, getInner( treeNode ) );
      }
    }
  }

  void createExpandedNode( final TreeNode treeNode ) throws IOException 
  {
    if( hasChildren( treeNode ) ) {
      if( isLastChildBuffer( treeNode ) ) {
        createImageTag( treeNode, getMinusLast( treeNode ) );
      } else {
        createImageTag( treeNode, getMinusInner( treeNode ) );
      }
    } else {
      // should not happen
      if( isLastChildBuffer( treeNode ) ) {
        createImageTag( treeNode, getLast( treeNode ) );
      } else {
        createImageTag( treeNode, getInner( treeNode ) );
      }
    }
  }

  abstract void createImageTag( TreeNode treeNode, String img ) 
    throws IOException;

  static TreeNodeShift getShift( final TreeNode treeNode ) {
    return TreeNodeRendererUtil.getInfo( treeNode ).getTreeNodeShift();
  }

  void createBranchOpen( final TreeNode treeNode ) throws IOException {
    String exp = treeNode.isExpanded() ? "block" : "none" ;
    StringBuffer buffer = new StringBuffer();
    buffer.append( "display:" );
    buffer.append( exp );
    buffer.append( ";font-size: 0px;" );
    HtmlResponseWriter out = getResponseWriter();
    out.startElement( HTML.DIV, null );
    createDivID( treeNode );
    out.writeAttribute( HTML.STYLE, buffer, null );
    out.closeElementIfStarted();
  }

  void createBranchClose() throws IOException {
    HtmlResponseWriter out = getResponseWriter();
    out.endElement( HTML.DIV );
  }

  void createDivID( final TreeNode treeNode ) throws IOException {
    HtmlResponseWriter out = getResponseWriter();
    StringBuffer buffer = new StringBuffer();
    buffer.append( HTML.DIV );
    buffer.append( treeNode.getUniqueID() );
    out.writeAttribute( HTML.ID, buffer, null );
  }
}