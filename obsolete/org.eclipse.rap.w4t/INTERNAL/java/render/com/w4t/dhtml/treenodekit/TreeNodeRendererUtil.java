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
import java.text.MessageFormat;

import org.eclipse.rwt.internal.lifecycle.HtmlResponseWriter;
import org.eclipse.rwt.internal.util.HTML;

import com.w4t.LifeCycleHelper;
import com.w4t.W4TContext;
import com.w4t.ajax.AjaxStatusUtil;
import com.w4t.dhtml.TreeNode;
import com.w4t.dhtml.event.DoubleClickEvent;
import com.w4t.dhtml.event.DragDropEvent;
import com.w4t.dhtml.renderinfo.TreeNodeInfo;
import com.w4t.engine.lifecycle.standard.IRenderingSchedule;
import com.w4t.event.WebActionEvent;
import com.w4t.internal.adaptable.IRenderInfoAdapter;


final class TreeNodeRendererUtil {
  
  private static final String TREE_ITEM_CLICKED 
    = "treeItemClicked(''{0}'');";
  private static final String TREE_ITEM_DBL_CLICKED 
    = "treeItemDblClicked(''{0}'');";

  private TreeNodeRendererUtil() {
    // prevent instantiation
  }
  
  public static void scheduleToggleModeRendering( final TreeNode node ) {
    if( node.isExpanded() ) {
      for( int i = 0; i < node.getItemCount(); i++ ) {
        if( node.getItem( i ).isVisible() ) {
          getRenderingSchedule().schedule( node.getItem( i ) );
        }
      }
    }
  }
  
  static void createStateInfoField( final TreeNode treeNode ) {
    TreeNodeInfo info = getInfo( treeNode );
    StringBuffer hiddenField = new StringBuffer();
    hiddenField.append( "<input type=\"hidden\" " );
    hiddenField.append( "name=\"" );
    hiddenField.append( getStateInfoId( treeNode.getUniqueID() ) );
    hiddenField.append( "\" value=\"" );
    hiddenField.append( info.getExpansion() );
    hiddenField.append( "\" />" );
    // need this because of bug in IExplorer (must not be newline here!)
    String absoluteNecessaryBlank = " ";
    hiddenField.append( absoluteNecessaryBlank );
    info.appendStateInfoField( hiddenField.toString() );
  }

  static TreeNodeInfo getInfo( final TreeNode treeNode ) {
    IRenderInfoAdapter adapter 
      = ( IRenderInfoAdapter )treeNode.getAdapter( IRenderInfoAdapter.class );
    return ( TreeNodeInfo )adapter.getInfo();
  }

  static String getStateInfoId( final String uniqueId ) {
    StringBuffer result 
      = new StringBuffer( TreeNodeRenderer.TREE_NODE_TOGGLE_STATE_INFO );
    return result.append( uniqueId ).toString();
  }

  static void overrideAjaxHashCode( final TreeNode treeNode ) {
    // check whether the request is only updating server-side state that
    // is already reflected on the client-side
    if(    W4TContext.getBrowser().isAjaxEnabled() 
        && TreeNode.DYNLOAD_NEVER.equals( treeNode.getDynLoading() ) ) 
    {
      AjaxStatusUtil.updateHashCode( treeNode );
    }
  }

  static boolean isActionActive( final TreeNode treeNode ) {
    return treeNode.isEnabled() && hasActionListener( treeNode );
  }

  static boolean isDragDropActive( final TreeNode treeNode ) {
    return    treeNode.isEnabled() 
           && DragDropEvent.hasListener( treeNode );
  }

  static boolean isDblClickActive( final TreeNode treeNode ) {
    return treeNode.isEnabled() && DoubleClickEvent.hasListener( treeNode );
  }

  static boolean hasActionListener( final TreeNode treeNode ) {
    return WebActionEvent.hasListener( treeNode );
  }

  static void writeClickHandler( final HtmlResponseWriter out, 
                                 final TreeNode treeNode ) 
    throws IOException 
  {
    if( isActionActive( treeNode ) ) {
      Object[] id = new Object[] { treeNode.getUniqueID() };
      String handler = MessageFormat.format( TREE_ITEM_CLICKED, id );
      out.writeAttribute( HTML.ON_CLICK, handler, null );
    }
  }
  
  static void writeDoubleClickHandler( final HtmlResponseWriter out, 
                                       final TreeNode treeNode ) 
    throws IOException 
  {
    if( treeNode.isEnabled() && DoubleClickEvent.hasListener( treeNode ) ) {
      Object[] id = new Object[] { treeNode.getUniqueID() };
      String handler = MessageFormat.format( TREE_ITEM_DBL_CLICKED, id );
      out.writeAttribute( HTML.ON_DBL_CLICK, handler, null );
    }
  }

  private static IRenderingSchedule getRenderingSchedule() {
    return LifeCycleHelper.getSchedule();
  }
}
