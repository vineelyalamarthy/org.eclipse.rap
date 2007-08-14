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

import com.w4t.LifeCycleHelper;
import com.w4t.WebComponent;
import com.w4t.dhtml.*;
import com.w4t.dhtml.event.WebTreeNodeCollapsedEvent;
import com.w4t.dhtml.event.WebTreeNodeExpandedEvent;
import com.w4t.dhtml.renderinfo.TreeNodeInfo;


/**
 * <p> The superclass for all renderers for org.eclipse.rap.dhtml.TreeNode on more
 * recent browsers that support javascript and restructuring of html documents
 * via DOM. </p>
 * <p> Note that this is not an actual renderer but encapsulates common
 * functionality for browser-specific renderers. </p>
 */
public abstract class TreeNodeRenderer_DOM_Script 
  extends TreeNodeRenderer_Base_Script
{
  
  private static final String PLACEHOLDER_TEXT = "Loading...";
  
  // we pass these as bit keys to the eventhandling js
  private static final int EVT_TYPE_ACTION = 1;
  private static final int EVT_TYPE_DRAGDROP = 2;

  public void scheduleRendering( final WebComponent component ) {
    TreeNode treeNode = ( TreeNode )component;
    if( !renderPlaceHolder( treeNode ) ) {
      for( int i = 0; i < treeNode.getItemCount(); i++ ) {
        if( treeNode.getItem( i ).isVisible() ) {
          getRenderingSchedule().schedule( treeNode.getItem( i ) );
        }
      }
    }    
  }

  public void render( final WebComponent component ) throws IOException {
    TreeNode treeNode = ( TreeNode )component;
    fillBuffers( treeNode );
    HtmlResponseWriter out = getResponseWriter();
    out.startElement( HTML.DIV, null );
    out.writeAttribute( HTML.ID, treeNode.getUniqueID(), null );
    createNodeContent( treeNode );
    createBranchOpen( treeNode );
    prepare( treeNode );
    if( renderPlaceHolder( treeNode ) ) {
      createPlaceHolder( treeNode );
    } else {
      createChildren( treeNode );
    }    
    createBranchClose();
    postpare( treeNode );
    createBranchClose();
    restoreFromBuffers( treeNode );
  }

  private void createChildren( final TreeNode treeNode ) throws IOException {
    TreeNodeInfo info = TreeNodeRendererUtil.getInfo( treeNode );
    for( int i = 0; i < info.getNodeCount(); i++ ) {
      TreeNode childNode = info.getNode( i );
      getShift( treeNode ).setLastChild(    i == info.getNodeCount() - 1 
                                         && info.getLeafCount() == 0 );
      if( isScheduled( childNode ) ) {
        LifeCycleHelper.render( childNode );
      }
    }

    for( int i = 0; i < info.getLeafCount(); i++ ) {
      TreeLeaf leaf = info.getLeaf( i );
      getShift( treeNode ).setLastChild( i == info.getLeafCount() - 1 );
      if( isScheduled( leaf ) ) {
        LifeCycleHelper.render( leaf );
      }
    }
  }
  
  static boolean isScheduled( final WebComponent component ) {
    return getRenderingSchedule().isScheduled( component );
  }

  private void createPlaceHolder( final TreeNode treeNode ) throws IOException {
    TreeLeaf placeHolder = new TreeLeaf();
    placeHolder.setLabel( PLACEHOLDER_TEXT );
    getShift( treeNode ).setLastChild( true );
    treeNode.addItem( placeHolder );
    LifeCycleHelper.render( placeHolder );
    placeHolder.remove();
  }
  
  void fillBuffers( final TreeNode treeNode ) {
    super.fillBuffers( treeNode );
    TreeNodeInfo info = TreeNodeRendererUtil.getInfo( treeNode );
    info.removeToggleLoadingListener();
    if( renderPlaceHolder( treeNode ) ) {
      info.addToggleLoadingListener();
    }
  }
  
  void prepare( final TreeNode treeNode ) throws IOException {
    TreeNodeRendererUtil.getInfo( treeNode ).clearStateInfoFields();
    createStateInfoField( treeNode );
    if( hasChildren( treeNode ) ) {
      if( isLastChildBuffer( treeNode ) ) {
        addEmptyImage( treeNode );
      } else {
        addLineImage( treeNode );
      }
    }
  }

  /** <p>helping method for performRendering();</p>
   * <p> There must be a hidden input field for this TreeNode, which contains 
   * info about whether this TreeNode is expanded or collapsed; this is rendered 
   * by the root, we tell it what to render.</p> */
  void createStateInfoField( final TreeNode treeNode ) throws IOException {
    // need this because of bug in IExplorer (must not be newline here!)
    String absoluteNecessaryBlank = " ";
    TreeNodeInfo info = TreeNodeRendererUtil.getInfo( treeNode );
    StringBuffer hiddenField = new StringBuffer();
    hiddenField.append( "<input type=\"hidden\" " );
    hiddenField.append( "name=\"" );
    String name = TreeNodeRendererUtil.getStateInfoId( treeNode.getUniqueID() );
    hiddenField.append( name );
    hiddenField.append( "\" value=\"" );
    hiddenField.append( info.getExpansion() );
    hiddenField.append( "\" />" );
    hiddenField.append( absoluteNecessaryBlank );
    info.appendStateInfoField( hiddenField.toString() );
  }
 
  void postpare( final TreeNode treeNode ) {
    if( hasChildren( treeNode ) ) {
      getShift( treeNode ).remove();
    }
    TreeNodeRendererUtil.getInfo( treeNode ).appendStateInfoFields();
  }

  private void createNodeContent( final TreeNode treeNode ) throws IOException {
    HtmlResponseWriter out = getResponseWriter();
    int size = getShift( treeNode ).getSize();
    createOpenNodeContent( treeNode );
    for( int i = 0; i < size; i++ ) {
      String imageName = getShift( treeNode ).getImageName( i );
      createImageTag( treeNode, imageName );
    }
    if( treeNode.isExpanded() ) {
      createExpandedNode( treeNode );
    } else {
      createCollapsedNode( treeNode );
    }
    createLabelContent( treeNode );
    out.endElement( HTML.DIV );
  }

  void createOpenNodeContent( final TreeNode treeNode ) throws IOException {
    StringBuffer buffer = new StringBuffer();
    buffer.append( "node_" );
    buffer.append( treeNode.getUniqueID() );
    HtmlResponseWriter out = getResponseWriter();
    out.startElement( HTML.DIV, null );
    out.writeAttribute( HTML.CLASS, 
                        out.registerCssClass( ItemUtil.STYLE_CONTENT ), 
                        null );
    out.writeAttribute( HTML.ID, buffer, null );
  }

  private void createLabelContent( final TreeNode treeNode ) 
    throws IOException 
  {
    if( !TreeNodeRendererUtil.isDragDropActive( treeNode ) ) {
      createLabelContentWithoutDD( treeNode );
    } else {
      createLabelContentWithDD( treeNode );    
    } 
  }

  /** build the actual label entry string for the label entry if no DnD 
   * listener is set. </p> 
   * @throws IOException */
  private void createLabelContentWithoutDD( final TreeNode treeNode ) 
    throws IOException 
  {
    HtmlResponseWriter out = getResponseWriter();
    String cursorBuffer = getStyle( treeNode ).getCursor();
    getStyle( treeNode ).setCursor( "default" );
    out.startElement( HTML.SPAN, null );
    TreeNodeRendererUtil.writeClickHandler( out, treeNode );
    TreeNodeRendererUtil.writeDoubleClickHandler( out, treeNode );
    createUniversalAttributes( treeNode );
    out.writeAttribute( "unselectable", "on", null );
    out.writeNBSP();
    out.writeNBSP();
    out.writeText( getLabel( treeNode ), null );
    out.endElement( HTML.SPAN );
    getStyle( treeNode ).setCursor( cursorBuffer );
  }

  private void createLabelContentWithDD( final TreeNode treeNode ) 
    throws IOException 
  {
    String cursorBuffer = getStyle( treeNode ).getCursor();
    String compID = treeNode.getUniqueID();
    String evtType = createDDEventType( treeNode );
    getStyle( treeNode ).setCursor( "default" );
    HtmlResponseWriter out = getResponseWriter(); 
    createSpan( treeNode );
    out.startElement( HTML.SPAN, null );
    TreeNodeRendererUtil.writeClickHandler( out, treeNode );
    TreeNodeRendererUtil.writeDoubleClickHandler( out, treeNode );
    StringBuffer buffer = new StringBuffer();
    buffer.append( "dragDropHandler.mouseDown( '" );
    buffer.append( compID );
    buffer.append( "', " );
    buffer.append( evtType );
    buffer.append( " );" );
    out.writeAttribute( HTML.ON_MOUSE_DOWN, buffer, null );
    buffer.setLength( 0 );
    buffer.append( "dragDropHandler.mouseUp( '" );
    buffer.append( compID );
    buffer.append( "', " );
    buffer.append( evtType );
    buffer.append( "  );" );
    out.writeAttribute( HTML.ON_MOUSE_UP, buffer, null );
    createUniversalAttributes( treeNode );
    out.writeText( getLabel( treeNode ), null );
    out.endElement( HTML.SPAN );
    getStyle( treeNode ).setCursor( cursorBuffer );
  }

  private void createSpan( final TreeNode treeNode ) throws IOException {
    HtmlResponseWriter out = getResponseWriter();
    out.startElement( HTML.SPAN, null );
    createUniversalAttributes( treeNode );
    out.writeNBSP();
    out.writeNBSP();
    out.endElement( HTML.SPAN );
  }
  
  private String createDDEventType( final TreeNode treeNode ) {
    return   !TreeNodeRendererUtil.hasActionListener( treeNode )
           ? String.valueOf( EVT_TYPE_DRAGDROP )
           : String.valueOf( EVT_TYPE_DRAGDROP + EVT_TYPE_ACTION );
  }

  void createImageTag( final TreeNode treeNode, final String imageName ) 
    throws IOException 
  {
    createToggleHandlerOpen( treeNode, imageName );
    createToggleImage( treeNode, imageName );
    createToggleHandlerClose( treeNode, imageName );
    createIconImage( treeNode, imageName );
  }
  
  private void createToggleImage( final TreeNode treeNode,
                                  final String imageName ) throws IOException
  {
    HtmlResponseWriter out = getResponseWriter();
    out.startElement( HTML.IMG, null );
    createID( treeNode, imageName, "tog" );
    out.writeAttribute( HTML.SRC, imageName, null );
    out.writeAttribute( HTML.BORDER, "0", null );
  }

  private void createIconImage( final TreeNode treeNode,
                                final String imageName ) throws IOException
  {
    if( !isShiftImage( imageName ) ) {
      String name = createIconName( treeNode );
      HtmlResponseWriter out = getResponseWriter();
      out.startElement( HTML.IMG, null );
      TreeNodeRendererUtil.writeClickHandler( out, treeNode );
      TreeNodeRendererUtil.writeDoubleClickHandler( out, treeNode );
      createID( treeNode, imageName, "ico" );
      out.writeAttribute( HTML.SRC, name, null );
      out.writeAttribute( HTML.BORDER, "0", null );
    }
  }
  
  private String createIconName( final TreeNode treeNode ) {
    String result = getCollapsedIcon( treeNode );
    if( treeNode.isExpanded() ) {
      result =   hasChildren( treeNode )
               ? getExpandedWithChildrenIcon( treeNode ) 
               : getExpandedWithoutChildrenIcon( treeNode );
    }
    return result;
  }

  private void createToggleHandlerOpen( final TreeNode treeNode,
                                        final String imageName ) 
    throws IOException
  {
    if( isToggleable( treeNode, imageName ) ) {
      int lsnr = 0;
      lsnr += (    WebTreeNodeExpandedEvent.hasListener( treeNode )
          || renderPlaceHolder( treeNode ) ) ? 2 : 0;
      lsnr += WebTreeNodeCollapsedEvent.hasListener( treeNode ) ? 1 : 0;
      String hasEvent = String.valueOf( lsnr );
      HtmlResponseWriter out = getResponseWriter();
      StringBuffer buffer = new StringBuffer();
      buffer.append( "treeViewHandler.toggle( '" );
      buffer.append( treeNode.getUniqueID() );
      buffer.append( "', " );
      buffer.append( hasEvent );
      buffer.append( ", '" );
      buffer.append( treeNode.getImageSetName() );
      buffer.append( "', " ); 
      buffer.append( String.valueOf( isLastChildBuffer( treeNode ) ) );
      buffer.append( " )" );
      out.startElement( HTML.A, null );
      out.writeAttribute( HTML.ON_CLICK, buffer, null );
    }
  }

  private void createToggleHandlerClose( final TreeNode treeNode,
                                         final String imageName ) 
    throws IOException
  {
    if( isToggleable( treeNode, imageName ) ){
      HtmlResponseWriter out = getResponseWriter();
      out.endElement( HTML.A );
    }
  }

  private void createID( final TreeNode treeNode,
                         final String imageName, 
                         final String prefix ) throws IOException
  {
    HtmlResponseWriter out = getResponseWriter();    
    if( isToggleable( treeNode, imageName ) ) {
      StringBuffer buffer = new StringBuffer();
      buffer.append( prefix );
      buffer.append( treeNode.getUniqueID() );
      out.writeAttribute( HTML.ID, buffer, null );
    }
  }
  
  // helping methods
  //////////////////
  
  private static boolean isToggleable( final TreeNode treeNode,
                                       final String imageName )
  {
    return treeNode.isEnabled() && isToggleImage( imageName );
  }
  
  private static boolean isToggleImage( final String imageName ) {
    return imageName.indexOf( "_Minus" ) != -1
        || imageName.indexOf( "_Plus" ) != -1;
  }
  
  private static boolean isShiftImage( final String imageName ) {
    return imageName.indexOf( "_Empty" ) != -1
        || imageName.indexOf( "_Line" ) != -1;
  }
  
  static boolean renderPlaceHolder( final TreeNode treeNode ) {
    return    !treeNode.isExpanded()
            && ( reloadAlways( treeNode ) || loadDynamic( treeNode ) );
  }
  
  private static boolean reloadAlways( final TreeNode treeNode ) {
    return    treeNode.getDynLoading().equals( TreeNode.DYNLOAD_ALWAYS );
  }
  
  private static boolean loadDynamic( final TreeNode treeNode ) {
    return    treeNode.getDynLoading().equals( TreeNode.DYNLOAD_DYNAMIC )
           && treeNode.getChildCount() >= treeNode.getMinChildsDynLoad();
  }
}