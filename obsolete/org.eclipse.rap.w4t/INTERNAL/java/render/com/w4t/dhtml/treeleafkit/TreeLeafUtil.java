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
import java.text.MessageFormat;

import org.eclipse.rwt.internal.lifecycle.HtmlResponseWriter;
import org.eclipse.rwt.internal.util.HTML;

import com.w4t.dhtml.TreeLeaf;
import com.w4t.dhtml.event.DoubleClickEvent;
import com.w4t.dhtml.event.DragDropEvent;
import com.w4t.event.WebActionEvent;


final class TreeLeafUtil {

  private static final String TREE_ITEM_CLICKED 
    = "treeItemClicked(''{0}'');";
  private static final String TREE_ITEM_DBL_CLICKED 
    = "treeItemDblClicked(''{0}'');";

  static boolean isDragDropActive( final TreeLeaf treeLeaf ) {
    return    treeLeaf.isEnabled() 
           && DragDropEvent.hasListener( treeLeaf.getParentNode() );
  }

  static boolean isActionActive( final TreeLeaf treeLeaf ) {
    return treeLeaf.isEnabled() && hasActionListener( treeLeaf );
  }
  
  static boolean isDblClickActive( final TreeLeaf treeLeaf ) {
    return treeLeaf.isEnabled() && DoubleClickEvent.hasListener( treeLeaf );
  }

  static boolean hasActionListener( final TreeLeaf treeLeaf ) {
    return WebActionEvent.hasListener( treeLeaf );
  }

  static void writeClickHandler( final HtmlResponseWriter out, 
                                 final TreeLeaf treeLeaf ) 
    throws IOException 
  {
    if( isActionActive( treeLeaf ) ) {
      Object[] id = new Object[] { treeLeaf.getUniqueID() };
      String handler = MessageFormat.format( TREE_ITEM_CLICKED, id );
      out.writeAttribute( HTML.ON_CLICK, handler, null );
    }
  }
  
  static void writeDoubleClickHandler( final HtmlResponseWriter out, 
                                       final TreeLeaf treeLeaf ) 
    throws IOException 
  {
    if( isDblClickActive( treeLeaf ) ) {
      Object[] id = new Object[] { treeLeaf.getUniqueID() };
      String handler = MessageFormat.format( TREE_ITEM_DBL_CLICKED, id );
      out.writeAttribute( HTML.ON_DBL_CLICK, handler, null );
    }
  }
}
