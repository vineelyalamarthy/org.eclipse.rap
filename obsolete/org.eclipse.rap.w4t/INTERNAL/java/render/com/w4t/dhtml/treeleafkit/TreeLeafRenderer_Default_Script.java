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


/** <p>The default renderer for org.eclipse.rap.dhtml.TreeLeaf.</p>
  *
  * <p>The default renderer is non-browser-specific and implements 
  * functionality in a way that runs on the most commonly used browsers.</p>
  */
//TODO [rh] missing test case for this renderer
public class TreeLeafRenderer_Default_Script 
   extends TreeLeafRenderer
{
  
  public void processAction( final WebComponent component ) {
    TreeLeaf treeLeaf = ( TreeLeaf )component;
    DHTMLProcessActionUtil.processDragDropScript( treeLeaf );
    DHTMLProcessActionUtil.processDoubleClickScript( treeLeaf );
    ProcessActionUtil.processActionPerformedScript( treeLeaf );
  }

  public void render( final WebComponent component ) throws IOException {
    TreeLeaf treeLeaf = ( TreeLeaf )component;
    prepare( treeLeaf );
    createLeafContent( treeLeaf );
    postpare( treeLeaf );
  }

  private void createLeafContent( final TreeLeaf treeLeaf ) throws IOException {
    TreeNodeShift shift = getInfo( treeLeaf ).getTreeNodeShift();
    createDivOpen( "font-size:0px;", treeLeaf );
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
    HtmlResponseWriter out = getResponseWriter();
    out.endElement( HTML.DIV );
  }

  private void createImageTag( final TreeLeaf treeLeaf,
                                 final String imageName ) throws IOException
  {
    HtmlResponseWriter out = getResponseWriter();
    createIconHandlerOpen( treeLeaf, imageName );
    out.startElement( HTML.IMG, null );
    out.writeAttribute( HTML.SRC, imageName, null );
    out.writeAttribute( HTML.BORDER, "0", null );
    createIconHandlerClose( treeLeaf, imageName );
  }

  private void createIconHandlerOpen( final TreeLeaf treeLeaf,
                                      final String imageName ) 
    throws IOException
  {
    if( isIconImage( imageName ) && TreeLeafUtil.isActionActive( treeLeaf ) ) {
      HtmlResponseWriter out = getResponseWriter();
      out.startElement( HTML.A, null );
      String href = RenderUtil.jsWebActionPerformed( treeLeaf.getUniqueID() );
      out.writeAttribute( HTML.HREF, href, null );
      getMouseEventHandler( treeLeaf );
    }
  }

  private void createIconHandlerClose( final TreeLeaf treeLeaf,
                                       final String imageName ) 
    throws IOException
  {
    HtmlResponseWriter out = getResponseWriter();
    if( isIconImage( imageName ) && TreeLeafUtil.isActionActive( treeLeaf ) ) {
      out.endElement( HTML.A ); 
    }
  }
  
  
  private boolean isIconImage( final String imageName ) {
    return imageName.indexOf( "_LeafIcon" ) != -1;
  }

  
  /** <p>Build the actual label entry string for the label entry.</p> */
  private void createLabelContent( final TreeLeaf treeLeaf ) 
    throws IOException 
  {
    HtmlResponseWriter out = getResponseWriter();
    
    if( TreeLeafUtil.isActionActive( treeLeaf ) ) {
      out.startElement( HTML.SPAN, null );
      createUniversalAttributes( treeLeaf );
      out.writeNBSP();
      out.writeNBSP();
      out.endElement( HTML.SPAN );
      out.startElement( HTML.A, null );
      String href = RenderUtil.jsWebActionPerformed( treeLeaf.getUniqueID() );
      out.writeAttribute( HTML.HREF, href, null );
      createUniversalAttributes( treeLeaf );
      TreeLeafUtil.writeDoubleClickHandler( out, treeLeaf );
      out.writeText( getLabel( treeLeaf ), null );
      out.endElement( HTML.A );
    } else {
      out.startElement( HTML.SPAN, null );
      createUniversalAttributes( treeLeaf );
      TreeLeafUtil.writeDoubleClickHandler( out, treeLeaf );
      out.writeNBSP();
      out.writeNBSP();
      out.writeText( getLabel( treeLeaf ), null );
      out.endElement( HTML.SPAN );
    }
    if( TreeLeafUtil.isDragDropActive( treeLeaf ) ) {
      out.startElement( HTML.A, null );
      String href = RenderUtil.jsDoDragDrop( treeLeaf );
      out.writeAttribute( HTML.HREF, href, null );
      createUniversalAttributes( treeLeaf );
      out.startElement( HTML.IMG, null );
      out.writeAttribute( HTML.SRC, RenderUtil.DRAG_DROP_IMAGE, null );
      out.writeAttribute( HTML.BORDER, "0", null );
      out.endElement( HTML.A );
    }
  }

  /** Returns mouse specific eventhandling code to be rendered into the
   * event handler of this Item. */
  protected void getMouseEventHandler( final TreeLeaf treeLeaf ) 
    throws IOException 
  {
    HtmlResponseWriter out = getResponseWriter();
    StringBuffer mouseDown = new StringBuffer();
    mouseDown.append( "eventHandler.suspendSubmit();" );
    mouseDown.append( "dragDropHandler.mouseDown( '" );
    mouseDown.append( treeLeaf.getUniqueID() );
    mouseDown.append( "' )" );
    out.writeAttribute( HTML.ON_MOUSE_DOWN, mouseDown, null );
    String resumeSubmit = "eventHandler.resumeSubmit();";
    out.writeAttribute( HTML.ON_MOUSE_OUT, resumeSubmit, null );
    out.writeAttribute( HTML.ON_MOUSE_UP, resumeSubmit, null );
  }  
  
  
  // helping methods
  //////////////////
  
  String getVerticalAlign() {
    return "top";
  }
}

