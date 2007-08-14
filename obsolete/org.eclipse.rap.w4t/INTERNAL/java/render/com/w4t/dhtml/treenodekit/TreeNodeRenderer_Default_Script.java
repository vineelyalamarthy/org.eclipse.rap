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
import com.w4t.dhtml.*;
import com.w4t.dhtml.event.*;
import com.w4t.dhtml.renderinfo.TreeNodeInfo;


/** <p>The default renderer for org.eclipse.rap.dhtml.TreeNode.</p> */
// TODO [rh] missing test case for this renderer
public class TreeNodeRenderer_Default_Script extends TreeNodeRenderer {
  
  public void processAction( final WebComponent component ) {
    TreeNode node = ( TreeNode )component;
    DHTMLProcessActionUtil.processTreeNodeExpandedScript( node );
    DHTMLProcessActionUtil.processTreeNodeCollapsedScript( node );
    DHTMLProcessActionUtil.processDragDropScript( node );
    DHTMLProcessActionUtil.processDoubleClickScript( node );
    ProcessActionUtil.processActionPerformedScript( node );
  }
  
  public void scheduleRendering( WebComponent component ) {
    TreeNodeRendererUtil.scheduleToggleModeRendering( ( TreeNode )component );
  }
  
  public void render( final WebComponent component ) throws IOException {
    TreeNode treeNode = ( TreeNode )component;
    fillBuffers( treeNode );
    createNodeContent( treeNode );
    createBranchOpen( treeNode );
    prepare( treeNode );
    if( treeNode.isExpanded() ) {
      createChildren( treeNode );
    }        
    createBranchClose();
    postpare( treeNode );
    restoreFromBuffers( treeNode );
  }

  private void createChildren( final TreeNode treeNode ) throws IOException {
    TreeNodeInfo info = TreeNodeRendererUtil.getInfo( treeNode );
    for( int i = 0; i < info.getNodeCount(); i++ ) {
      TreeNode childNode = info.getNode( i );
      getShift( treeNode ).setLastChild(    i == info.getNodeCount() - 1 
                                         && info.getLeafCount() == 0 );
      LifeCycleHelper.render( childNode );
    }

    for( int i = 0; i < info.getLeafCount(); i++ ) {
      TreeLeaf leaf = info.getLeaf( i );
      getShift( treeNode ).setLastChild( i == info.getLeafCount() - 1 );
      LifeCycleHelper.render( leaf );
    }
  }
  
  private void prepare( final TreeNode treeNode ) {
    TreeNodeRendererUtil.getInfo( treeNode ).clearStateInfoFields();
    TreeNodeRendererUtil.createStateInfoField( treeNode );
    if( hasChildren( treeNode ) ) {
      if( isLastChildBuffer( treeNode ) ) {
        addEmptyImage( treeNode );
      } else {
        addLineImage( treeNode );
      }
    }
  }

  private void postpare( final TreeNode treeNode ) {
    if( hasChildren( treeNode ) ) {
      getShift( treeNode ).remove();
    }
    TreeNodeRendererUtil.getInfo( treeNode ).appendStateInfoFields();
  }
  
  private void createNodeContent( final TreeNode treeNode ) 
    throws IOException 
  {
    HtmlResponseWriter out = getResponseWriter();
    String string = "font-size: 0px;";
    out.startElement( HTML.DIV, null );
    out.writeAttribute( HTML.STYLE, string, null );
    int size = getShift( treeNode ).getSize();
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

  /** build the actual label entry string for the label entry. </p> 
   * @throws IOException */
  private void createLabelContent( final TreeNode treeNode ) 
    throws IOException 
  {
    HtmlResponseWriter out = getResponseWriter();
    if( isActionActive( treeNode ) ) {
      out.startElement( HTML.A, null );
      String href = RenderUtil.jsWebActionPerformed( treeNode.getUniqueID() );
      out.writeAttribute( HTML.HREF, href, null );
      createUniversalAttributes( treeNode );
      out.writeNBSP();
      out.writeNBSP();
      out.writeText( getLabel( treeNode ), null );
      out.endElement( HTML.A );
    } else {
      createSpan( treeNode );
    }
    if( isDragDropActive( treeNode ) ) {
      out.startElement( HTML.A, null );
      String href = RenderUtil.jsDoDragDrop( treeNode );
      out.writeAttribute( HTML.HREF, href, null );
      createUniversalAttributes( treeNode );
      out.startElement( HTML.IMG, null );
      out.writeAttribute( HTML.SRC, RenderUtil.DRAG_DROP_IMAGE, null );
      out.writeAttribute( HTML.BORDER, "0", null );
      out.endElement( HTML.A );
    } 
  }  
  
  private void createSpan( final TreeNode treeNode ) throws IOException {
    HtmlResponseWriter out = getResponseWriter();
    out.startElement( HTML.SPAN, null );
    createUniversalAttributes( treeNode );
    out.writeNBSP();
    out.writeNBSP();
    out.writeText( getLabel( treeNode ), null );
    out.endElement( HTML.SPAN );
  }

  void createImageTag( final TreeNode treeNode, final String imageName ) 
    throws IOException
  {
    createToggleHandlerOpen( treeNode, imageName );
    createToggleImage( treeNode, imageName );
    createToggleHandlerClose( treeNode, imageName );
    createIconImage( treeNode, imageName );
  }

  private void createIconHandlerOpen( final TreeNode treeNode ) 
    throws IOException 
  {
    if( isActionActive( treeNode ) ) {
      HtmlResponseWriter out = getResponseWriter();
      out.startElement( HTML.A, null );
      String href = RenderUtil.jsWebActionPerformed( treeNode.getUniqueID() );
      out.writeAttribute( HTML.HREF, href, null );
    }
  }
  
  private void createIconHandlerClose( final TreeNode treeNode )
    throws IOException
  {
    if( isActionActive( treeNode ) ) {
      HtmlResponseWriter out = getResponseWriter();
      out.endElement( HTML.A );
    }
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
      createIconHandlerOpen( treeNode );
      out.startElement( HTML.IMG, null );
      createID( treeNode, imageName, "ico" );
      out.writeAttribute( HTML.SRC, name, null );
      out.writeAttribute( HTML.BORDER, "0", null );
      createIconHandlerClose( treeNode );
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
      lsnr += WebTreeNodeExpandedEvent.hasListener( treeNode ) ? 2 : 0;
      lsnr += WebTreeNodeCollapsedEvent.hasListener( treeNode ) ? 1 : 0;
      String hasEvent = String.valueOf( lsnr );
      HtmlResponseWriter out = getResponseWriter();
      StringBuffer buffer = new StringBuffer();
      buffer.append( "javascript: treeViewHandler.toggle( '" );
      buffer.append( treeNode.getUniqueID() );
      buffer.append( "', " );
      buffer.append( hasEvent );
      buffer.append( ", '" );
      buffer.append( treeNode.getImageSetName() );
      buffer.append( "', " ); 
      buffer.append( String.valueOf( isLastChildBuffer( treeNode ) ) );
      buffer.append( " )" );
      out.startElement( HTML.A, null );
      out.writeAttribute( HTML.HREF, buffer, null );
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
    if( isToggleable( treeNode, imageName ) ) {
      HtmlResponseWriter out = getResponseWriter();  
      StringBuffer buffer = new StringBuffer();
      buffer.append( prefix );
      buffer.append( treeNode.getUniqueID() );
      out.writeAttribute( HTML.ID, buffer, null );
    }
  }
  
  // helping methods
  //////////////////
  
  private boolean isToggleable( final TreeNode treeNode,
                                final String imageName )
  {
    return treeNode.isEnabled() && isToggleImage( imageName );
  }
  
  private boolean isToggleImage( final String imageName ) {
    return imageName.indexOf( "_Minus" ) != -1
        || imageName.indexOf( "_Plus" ) != -1;
  }
  
  private boolean isShiftImage( final String imageName ) {
    return imageName.indexOf( "_Empty" ) != -1
        || imageName.indexOf( "_Line" ) != -1;
  }
  
  private boolean isActionActive( final TreeNode treeNode ) {
    return    treeNode.isEnabled() 
           && TreeNodeRendererUtil.hasActionListener( treeNode );
  }
  
  private boolean isDragDropActive( final TreeNode treeNode ) {
    return    treeNode.isEnabled() 
           && DragDropEvent.hasListener( treeNode );
  }
  
  String getVerticalAlign() {
    return HTML.TOP;
  }
}