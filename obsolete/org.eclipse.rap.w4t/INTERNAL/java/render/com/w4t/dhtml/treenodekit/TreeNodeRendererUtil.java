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

import com.w4t.W4TContext;
import com.w4t.ajax.AjaxStatusUtil;
import com.w4t.dhtml.TreeNode;
import com.w4t.dhtml.renderinfo.TreeNodeInfo;
import com.w4t.engine.lifecycle.standard.IRenderingSchedule;
import com.w4t.engine.service.ContextProvider;
import com.w4t.internal.adaptable.IRenderInfoAdapter;


final class TreeNodeRendererUtil {
  
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
  
  private static IRenderingSchedule getRenderingSchedule() {
    return ContextProvider.getStateInfo().getRenderingSchedule();
  }

  public static void createStateInfoField( final TreeNode treeNode ) {
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
}
