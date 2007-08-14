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
import com.w4t.dhtml.event.WebTreeNodeCollapsedEvent;
import com.w4t.dhtml.event.WebTreeNodeExpandedEvent;
import com.w4t.dhtml.renderinfo.TreeNodeInfo;
import com.w4t.util.image.*;


/** 
 * <p>The base renderer for <code>org.eclipse.rap.dhtml.TreeNode</code> on 
 * browsers without javascript support.</p>
 */
public class TreeNodeRenderer_Base_Noscript extends TreeNodeRenderer {
  
  public void processAction( final WebComponent component ) {
    TreeNode node = ( TreeNode )component;
    DHTMLProcessActionUtil.processTreeNodeExpandedNoScript( node );
    DHTMLProcessActionUtil.processTreeNodeCollapsedNoScript( node );
    DHTMLProcessActionUtil.processDragDropNoScript( node );
    DHTMLProcessActionUtil.processDoubleClickNoScript( node );
    ProcessActionUtil.processActionPerformedNoScript( node );
  }

  public void scheduleRendering( final WebComponent component ) {
    TreeNodeRendererUtil.scheduleToggleModeRendering( ( TreeNode )component );
  }
  
  public void render( final WebComponent component ) throws IOException {
    TreeNode treeNode = ( TreeNode )component;
    fillBuffers( treeNode );  
    HtmlResponseWriter out = getResponseWriter();
    createNodeContent( treeNode );
    createBranchOpen( treeNode );
    prepare( treeNode );

    if( treeNode.isExpanded() ) {
      createChildren( treeNode );
    }
    out.endElement( HTML.DIV );
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
  }

  private void createNodeContent( final TreeNode treeNode ) throws IOException {
    HtmlResponseWriter out = getResponseWriter();
    out.startElement( HTML.DIV, null );
    out.writeAttribute( HTML.CLASS, 
                        out.registerCssClass( ItemUtil.STYLE_CONTENT ), 
                        null );
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

  /** build the actual label entry string for the label entry if no DnD 
   * listener is set. </p> 
   * @throws IOException */
  private void createLabelContent( final TreeNode treeNode ) throws IOException {
    if( TreeNodeRendererUtil.isActionActive( treeNode ) ) {
      createActionSubmitter( treeNode );
    } else {
      createLabelEntry( treeNode );
    }
    if( TreeNodeRendererUtil.isDragDropActive( treeNode ) ) {
      createDragDropSubmitter( treeNode );      
    }
    if( TreeNodeRendererUtil.isDblClickActive( treeNode ) ) {
      createDoubleClickSubmitter( treeNode );
    }
  }

  private void createLabelEntry( final TreeNode treeNode ) throws IOException {
    HtmlResponseWriter out = getResponseWriter();
    out.startElement( HTML.SPAN, null );
    createUniversalAttributes( treeNode ); 
    out.writeNBSP();
    out.writeNBSP();
    out.writeText( getLabel( treeNode ), null );
    out.endElement( HTML.SPAN );
  }
  
  /** workaround for the dragdrop functionality in scripting mode 
   * @throws IOException */
  private void createDragDropSubmitter( final TreeNode treeNode ) 
    throws IOException 
  {
    RenderUtil.writeDragDropSubmitter( treeNode.getUniqueID() );
  }

  private void createDoubleClickSubmitter( final TreeNode treeNode ) 
    throws IOException 
  {
    RenderUtil.writeDoubleClickSubmitter( treeNode.getUniqueID() );
  }

  private void createActionSubmitter( final TreeNode treeNode ) {    
    try {
      ImageDescriptor imgDesc 
        = ImageDescriptorFactory.create( getLabel( treeNode ), 
                                         getStyle( treeNode ).getColor(),
                                         getStyle( treeNode ).getBgColor(),
                                         getStyle( treeNode ).getFontFamily(),
                                         getStyle( treeNode ).getFontSize(),
                                         getStyle( treeNode ).getFontWeight(),
                                         getStyle( treeNode ).getFontStyle() );
      ImageCache cache = ImageCache.getInstance();
      String imageName = cache.getImageName( imgDesc );
      if( !cache.isStandardSubmitterImage( imageName ) ) {
        RenderUtil.writeActionSubmitter( imageName, 
                                         treeNode.getUniqueID(),
                                         getLabel( treeNode ), 
                                         "" );
      } else {
        createLabelEntry( treeNode );
      }
    } catch( Exception e ) {
      // TODO [rh] exception handling
      System.out.println( "\nException creating submitter image:\n" + e );
      e.printStackTrace();
    }
  }

  void createImageTag( final TreeNode treeNode, final String imageName ) 
    throws IOException
  {
    if( isToggleable( treeNode, imageName ) ) {
      RenderUtil.writeSubmitter( imageName, 
                                 getPrefixedID( treeNode ), 
                                 getAlt( treeNode ), 
                                 "" );
    } else {
      HtmlResponseWriter out = getResponseWriter();
      out.startElement( HTML.IMG, null );
      out.writeAttribute( HTML.SRC, imageName, null );
      out.writeAttribute( HTML.BORDER, "0", null );
    }
    createIconImage( treeNode, imageName );
  }
  
  private String getPrefixedID( final TreeNode treeNode ) {
    String prefix = treeNode.isExpanded() ? WebTreeNodeCollapsedEvent.PREFIX 
                                          : WebTreeNodeExpandedEvent.PREFIX;
    return prefix + treeNode.getUniqueID();
  }

  private static String getAlt( final TreeNode treeNode ) {
    return treeNode.isExpanded() ? "Collapse" : "Expand";
  }

  private void createIconImage( final TreeNode treeNode,
                                final String imageName ) 
    throws IOException
  {
    if( !isShiftImage( imageName ) ) {
      String name = createIconName( treeNode );
      if( TreeNodeRendererUtil.isActionActive( treeNode ) ) {
        RenderUtil.writeActionSubmitter( name, 
                                         treeNode.getUniqueID(), 
                                         "", 
                                         "" );
      } else {
        HtmlResponseWriter out = getResponseWriter();
        out.startElement( HTML.IMG, null );
        createID( treeNode, imageName, "ico" );
        out.writeAttribute( HTML.SRC, name, null );
        out.writeAttribute( HTML.ALT, " ", null );
        out.writeAttribute( HTML.BORDER, "0", null );
      }
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

  private void createID( final TreeNode treeNode,
                         final String imageName,
                         final String prefix ) 
    throws IOException
  {
    if( isToggleable( treeNode, imageName ) ) {
      HtmlResponseWriter out = getResponseWriter();
      StringBuffer buffer = new StringBuffer();
      buffer.append( prefix );
      buffer.append( treeNode.getUniqueID() );
      out.writeAttribute( HTML.ID, buffer, null );
    }
  }
  
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
  
  String getVerticalAlign() {
    return HTML.TOP;
  }  
}