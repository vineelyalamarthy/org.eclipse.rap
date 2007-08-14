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
package com.w4t.dhtml.treeviewkit;

import java.io.IOException;

import org.eclipse.rwt.internal.lifecycle.HtmlResponseWriter;
import org.eclipse.rwt.internal.util.HTML;

import com.w4t.*;
import com.w4t.dhtml.TreeView;
import com.w4t.dhtml.event.DragDropEvent;
import com.w4t.dhtml.renderinfo.TreeViewInfo;
import com.w4t.event.WebRenderEvent;


/** 
 * <p>The renderer for {@link org.eclipse.rwt.dhtml.TreeView TreeView} on 
 * Microsoft Internet Explorer 6 and later.</p>
 */
public class TreeViewRenderer_Ie5up_Script extends TreeViewRenderer_DOM_Script {

  private static final int PIXEL_HEIGHT_OF_ITEM = 18;
  private static final String HEIGHT_HELPER_POSTFIX = "_HeightHelper";
  
  
  private final class TreeFormMarkupAppender extends FormMarkupAppender {

    private TreeFormMarkupAppender( final WebComponent component ) {
      super( component );
    }

    protected void doAfterRender( final WebRenderEvent evt ) 
      throws IOException 
    {
      HtmlResponseWriter out = getResponseWriter();
      StringBuffer code = new StringBuffer();
      code.append( "try {" );
      code.append( "document.body.onmouseup = dragDropHandler.clearDragDrop;" );
      code.append( "var jtree=document.getElementById(\"" );
      code.append( component.getUniqueID() );
      code.append( "\");" );
      code.append( "jtree.style.height=\"0px\";" );
      code.append( "} catch( e ) { }" );
      RenderUtil.writeJavaScriptInline( out, code.toString() );
    }
  }
  
  public void render( final WebComponent component ) throws IOException {
    super.render( component );
    TreeView treeView = ( TreeView )component;
    TreeFormMarkupAppender adapter = new TreeFormMarkupAppender( treeView );
    treeView.getWebForm().addWebRenderListener( adapter );
  }

  protected void useJSLibrary() throws IOException {
    TreeViewUtil.userDoubleClickLibrary();
    TreeViewUtil.useJSLibrary( TreeViewUtil.TREEVIEW_IE_GECKO );
  }
  
  void createOuterDivOpen( final TreeView treeView ) throws IOException {
    TreeViewInfo info = getInfo( treeView );
    int treeHeight = info.getNodeCount();
    treeHeight *= PIXEL_HEIGHT_OF_ITEM;
    HtmlResponseWriter out = getResponseWriter();
    out.startElement( HTML.DIV, null );
    out.writeAttribute( HTML.ID, treeView.getUniqueID(), null );
    out.writeAttribute( HTML.NAME, getTreeHeightHelperId( treeView ), null );
    out.writeAttribute( HTML.ALIGN, HTML.LEFT, null );
    StringBuffer buffer = new StringBuffer();
    buffer.append( "height:" );
    buffer.append( String.valueOf( treeHeight ) );
    buffer.append( "px" );
    out.writeAttribute( HTML.STYLE, buffer, null );
    out.writeAttribute( HTML.VALIGN, HTML.TOP, null );
    if( DragDropEvent.hasListener( treeView ) ) { 
      out.writeAttribute( "onselectstart", "return false", null );
    }
    out.writeAttribute( HTML.CLASS, 
                        out.registerCssClass( "font-size: 0px;" ), 
                        null );
  }

  private String getTreeHeightHelperId( final WebComponent component ) {
    return component.getUniqueID() + HEIGHT_HELPER_POSTFIX;
  }
}