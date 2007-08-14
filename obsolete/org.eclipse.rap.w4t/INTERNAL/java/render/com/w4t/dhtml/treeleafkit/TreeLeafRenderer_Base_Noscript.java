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
package com.w4t.dhtml.treeleafkit;

import java.io.IOException;

import org.eclipse.rwt.internal.lifecycle.HtmlResponseWriter;
import org.eclipse.rwt.internal.util.HTML;

import com.w4t.*;
import com.w4t.dhtml.*;
import com.w4t.util.image.*;


/** 
 * <p>The base renderer for <code>org.eclipse.rap.dhtml.TreeLeaf</code> on browsers 
 * without javascript support.</p>
 */
public class TreeLeafRenderer_Base_Noscript extends TreeLeafRenderer {

  public void processAction( final WebComponent component ) {
    TreeLeaf treeLeaf = ( TreeLeaf )component;
    DHTMLProcessActionUtil.processDragDropNoScript( treeLeaf );
    DHTMLProcessActionUtil.processDoubleClickNoScript( treeLeaf );
    ProcessActionUtil.processActionPerformedNoScript( treeLeaf );
  }

  public void render( final WebComponent component ) throws IOException {
    TreeLeaf treeLeaf = ( TreeLeaf )component;
    prepare( treeLeaf );
    createLeafContent( treeLeaf );
    postpare( treeLeaf );
  }
  
  private void createLeafContent( final TreeLeaf treeLeaf ) throws IOException {
    TreeNodeShift shift = getInfo( treeLeaf ).getTreeNodeShift();
    HtmlResponseWriter out = getResponseWriter();
    
    createDivOpen( ItemUtil.STYLE_CONTENT, treeLeaf );
    for( int i = 0; i < shift.getSize(); i++ ) {
      String imageName = shift.getImageName( i );
      createImageTag( treeLeaf, imageName );
    }
    String imageName = shift.isLastChild() 
                     ? getLast( treeLeaf )
                     : getInner( treeLeaf );
    createImageTag( treeLeaf, imageName );
    createImageTag( treeLeaf, getLeafIcon( treeLeaf ) );
    createLabelContent( treeLeaf );
    out.endElement( HTML.DIV );
  }

  private void createImageTag( final TreeLeaf treeLeaf,
                               final String imageName ) throws IOException 
                                 
  {
    HtmlResponseWriter out = getResponseWriter();
    if( isIconImage( imageName ) && TreeLeafUtil.isActionActive( treeLeaf ) ) {
      RenderUtil.writeActionSubmitter( imageName, 
                                           treeLeaf.getUniqueID(), 
                                           " ", "" );
    } else {
      out.startElement( HTML.IMG, null );
      out.writeAttribute( HTML.SRC, imageName, null );
      out.writeAttribute( HTML.ALT, " ", null );
      out.writeAttribute( HTML.BORDER, "0", null );
    }
  }
  
  private boolean isIconImage( final String imageName ) {
    return imageName.indexOf( "_LeafIcon" ) != -1;
  }
  
  private void createLabelContent( final TreeLeaf treeLeaf ) 
    throws IOException 
  {
    if( TreeLeafUtil.isActionActive( treeLeaf ) ) {
      createActionSubmitter( treeLeaf );
    } else {
      createLabelEntry( treeLeaf );
    }
    if( TreeLeafUtil.isDragDropActive( treeLeaf ) ) {
      createDragDropSubmitter( treeLeaf );
    }
    if( TreeLeafUtil.isDblClickActive( treeLeaf ) ) {
      createDoubleClickSubmitter( treeLeaf );
    }
  }

  private void createLabelEntry( final TreeLeaf treeLeaf ) 
    throws IOException 
  {
    HtmlResponseWriter out = getResponseWriter();
    out.startElement( HTML.SPAN, null );
    createUniversalAttributes( treeLeaf );
    out.writeNBSP();
    out.writeNBSP();
    out.writeText( getLabel( treeLeaf ), null );
    out.endElement( HTML.SPAN );
  }

  /** workaround for the dragdrop functionality in scripting mode 
   * @throws IOException */
  private void createDragDropSubmitter( final TreeLeaf treeLeaf ) 
    throws IOException 
  {
    RenderUtil.writeDragDropSubmitter( treeLeaf.getUniqueID() );
  }

  private void createDoubleClickSubmitter( final TreeLeaf treeLeaf ) 
    throws IOException 
  {
    RenderUtil.writeDoubleClickSubmitter( treeLeaf.getUniqueID() );
  }

  private void createActionSubmitter( final TreeLeaf treeLeaf ) {
    try {        
      ImageDescriptor imgDesc 
      = ImageDescriptorFactory.create( getLabel( treeLeaf ), 
                                       getStyle( treeLeaf ).getColor(),
                                       getStyle( treeLeaf ).getBgColor(),
                                       getStyle( treeLeaf ).getFontFamily(),
                                       getStyle( treeLeaf ).getFontSize(),
                                       getStyle( treeLeaf ).getFontWeight(),
                                       getStyle( treeLeaf ).getFontStyle() );
      ImageCache cache = ImageCache.getInstance();
      String imageName = cache.getImageName( imgDesc );
      if( !cache.isStandardSubmitterImage( imageName ) ) {
        RenderUtil.writeActionSubmitter( imageName, 
                                             treeLeaf.getUniqueID(),
                                             getLabel( treeLeaf ), "" );
      } else {
        createLabelEntry( treeLeaf );
      }
    } catch( Exception e ) {
      // TODO [rh] exception handling
      System.out.println( "\nException creating submitter image:\n" + e );
      e.printStackTrace();
    }
  }
  
    
  // helping methods
  //////////////////
  
  String getVerticalAlign() {
    return "top";
  }
}