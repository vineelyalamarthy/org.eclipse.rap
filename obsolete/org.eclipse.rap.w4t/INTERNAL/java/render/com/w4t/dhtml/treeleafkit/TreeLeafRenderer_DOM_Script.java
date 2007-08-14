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

import com.w4t.WebComponent;
import com.w4t.dhtml.*;


/** <p>The superclass for all renderers for org.eclipse.rap.dhtml.TreeLeaf on 
  * more recent browsers that support javascript and restructuring of html 
  * documents via DOM.</p>
  * 
  * <p>Note that this is not an actual renderer but encapsulates common 
  * functionality for browser-specific renderers.</p>
  */
public abstract class TreeLeafRenderer_DOM_Script 
  extends TreeLeafRenderer_Base_Script
{
  // we pass these as bit keys to the eventhandling js
  private static final int EVT_TYPE_ACTION = 1;
  private static final int EVT_TYPE_DRAGDROP = 2;

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
    if(    isIconImage( imageName ) 
        && (    TreeLeafUtil.isActionActive( treeLeaf ) 
             || TreeLeafUtil.isDblClickActive( treeLeaf ) ) ) 
    {
      HtmlResponseWriter out = getResponseWriter();
      out.startElement( HTML.SPAN, null );
      writeMouseEventHandler( treeLeaf );
      TreeLeafUtil.writeClickHandler( out, treeLeaf );
      TreeLeafUtil.writeDoubleClickHandler( out, treeLeaf );
    }
  }

  private void createIconHandlerClose( final TreeLeaf treeLeaf,
                                       final String imageName ) 
    throws IOException
  {
    HtmlResponseWriter out = getResponseWriter();
    if(    isIconImage( imageName ) 
        && (    TreeLeafUtil.isActionActive( treeLeaf ) 
             || TreeLeafUtil.isDblClickActive( treeLeaf ) ) ) 
    {
      out.endElement( HTML.SPAN ); 
    }
  }
  
  private boolean isIconImage( final String imageName ) {
    return imageName.indexOf( "_LeafIcon" ) != -1;
  }

  private void createLabelContent( final TreeLeaf treeLeaf ) 
    throws IOException 
  {
    HtmlResponseWriter out = getResponseWriter();
    String cursorBuffer = getStyle( treeLeaf ).getCursor();
    getStyle( treeLeaf ).setCursor( "default" );
    if(     TreeLeafUtil.isActionActive( treeLeaf ) 
        && !TreeLeafUtil.isDragDropActive( treeLeaf ) ) 
    {
      out.startElement( HTML.SPAN, null );
      createUniversalAttributes( treeLeaf );
      out.writeNBSP();
      out.writeNBSP();
      out.endElement( HTML.SPAN );
      out.startElement( HTML.SPAN, null );
      createUniversalAttributes( treeLeaf );
      out.writeAttribute( "unselectable", "on", null );
      TreeLeafUtil.writeClickHandler( out, treeLeaf );
      TreeLeafUtil.writeDoubleClickHandler( out, treeLeaf );
      out.writeText( getLabel( treeLeaf ), null );
      out.endElement( HTML.SPAN );
    } else if( TreeLeafUtil.isDragDropActive( treeLeaf ) ) {
      out.startElement( HTML.SPAN, null );
      createUniversalAttributes( treeLeaf );
      TreeLeafUtil.writeClickHandler( out, treeLeaf );
      TreeLeafUtil.writeDoubleClickHandler( out, treeLeaf );
      StringBuffer buffer = new StringBuffer();
      buffer.append( "dragDropHandler.mouseDown( '" );
      buffer.append( treeLeaf.getUniqueID() );
      buffer.append( "' )" );
      out.writeAttribute( HTML.ON_MOUSE_DOWN, buffer, null );
      buffer.setLength( 0 );
      buffer.append( "dragDropHandler.mouseUp( '" );
      buffer.append( treeLeaf.getUniqueID() );
      buffer.append( "', " );
      buffer.append( createDDEventType( treeLeaf ) );
      buffer.append( ")" );
      out.writeAttribute( HTML.ON_MOUSE_UP, buffer, null );
      out.writeNBSP();
      out.writeNBSP();
      out.writeText( getLabel( treeLeaf ), null );
      out.endElement( HTML.SPAN );
    } else {
      out.startElement( HTML.SPAN, null );
      createUniversalAttributes( treeLeaf );
      out.writeAttribute( "unselectable", "on", null );
      TreeLeafUtil.writeDoubleClickHandler( out, treeLeaf );
      out.writeNBSP();
      out.writeNBSP();
      out.writeText( getLabel( treeLeaf ), null );
      out.endElement( HTML.SPAN );
    }
    getStyle( treeLeaf ).setCursor( cursorBuffer );
  }
  
  private String createDDEventType( final TreeLeaf treeLeaf ) {
    return   !TreeLeafUtil.hasActionListener( treeLeaf )
           ? String.valueOf( EVT_TYPE_DRAGDROP )
           : String.valueOf(   EVT_TYPE_DRAGDROP + EVT_TYPE_ACTION );
  }

  /** 
   * returns mouse specific eventhandling code to be rendered into the
   * event handler of this Item. 
   */
  protected void writeMouseEventHandler( final TreeLeaf treeLeaf ) 
    throws IOException 
  {
    String mouseOut = "eventHandler.resumeSubmit();";
    HtmlResponseWriter out = getResponseWriter();
    StringBuffer buffer = new StringBuffer();
    buffer.append( "eventHandler.suspendSubmit();" );
    buffer.append( "dragDropHandler.mouseDown( '" );
    buffer.append( treeLeaf.getUniqueID() );
    buffer.append( "' )" );
    out.writeAttribute( HTML.ON_MOUSE_DOWN, buffer, null );
    out.writeAttribute( HTML.ON_MOUSE_OUT, mouseOut, null );
    out.writeAttribute( HTML.ON_MOUSE_UP, mouseOut, null );
  }  
  
}