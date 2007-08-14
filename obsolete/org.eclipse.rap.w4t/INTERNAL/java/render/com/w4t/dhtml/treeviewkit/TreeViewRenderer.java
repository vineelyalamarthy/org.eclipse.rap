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

import com.w4t.Renderer;
import com.w4t.WebComponent;
import com.w4t.dhtml.TreeView;
import com.w4t.dhtml.renderinfo.TreeViewInfo;
import com.w4t.internal.adaptable.IRenderInfoAdapter;


/** <p>The superclass of all Renderers that render org.eclipse.rap.dhtml.TreeView.</p>
  */
public abstract class TreeViewRenderer extends Renderer {
  
  public void scheduleRendering( final WebComponent component ) {
    TreeView treeView = ( TreeView )component;
    for( int i = 0; i < treeView.getItemCount(); i++ ) {
      if( treeView.getItem( i ).isVisible() ) {
        getRenderingSchedule().schedule( treeView.getItem( i ) );
      }
    }
  }
  
  TreeViewInfo getInfo( final TreeView treeView ) {
    IRenderInfoAdapter adapter 
      = ( IRenderInfoAdapter )treeView.getAdapter( IRenderInfoAdapter.class );
    return ( TreeViewInfo )adapter.getInfo();
  }

  void createOuterDivOpen( final TreeView treeView ) 
    throws IOException 
  {
    HtmlResponseWriter out = getResponseWriter();
    out.startElement( HTML.DIV, null );
    out.writeAttribute( HTML.ALIGN, HTML.LEFT, null );
    out.writeAttribute( HTML.CLASS, 
                        out.registerCssClass( "display:block;font-size:0px;" ), 
                        null );
  }

  void createOuterDivClose() throws IOException {
    getResponseWriter().endElement( HTML.DIV );
  } 
}