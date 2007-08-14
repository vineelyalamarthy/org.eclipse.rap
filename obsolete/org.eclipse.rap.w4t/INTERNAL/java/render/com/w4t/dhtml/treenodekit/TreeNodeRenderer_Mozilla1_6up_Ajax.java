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
import com.w4t.LifeCycleHelper;
import com.w4t.WebComponent;
import com.w4t.ajax.AjaxStatusUtil;
import com.w4t.dhtml.*;
import com.w4t.dhtml.renderinfo.TreeNodeInfo;


/**
 * <p>The renderer for <code>org.eclipse.rap.dhtml.TreeNode</code> on Mozilla 1.6 and 
 * later with AJaX support enabled.</p>
 */
//TODO [rh] unite! copied from corresponding Ie5 renderer
public class TreeNodeRenderer_Mozilla1_6up_Ajax
  extends TreeNodeRenderer_Mozilla1_6up_Script
{
  
  public void render( final WebComponent component ) throws IOException {
    TreeNode treeNode = ( TreeNode )component;
    ItemUtil.checkMarkState( treeNode );
    if( AjaxStatusUtil.mustRender( component ) ) {
      super.render( component );
    } else {
      updateToggleLoadingListener( treeNode );
      TreeNodeInfo info = TreeNodeRendererUtil.getInfo( treeNode );
      TreeNodeShift shift = info.getTreeNodeShift();
      prepare( treeNode );
      for( int i = 0; i < info.getNodeCount(); i++ ) {
        TreeNode childNode = info.getNode( i );
        shift.setLastChild(    i == info.getNodeCount() - 1 
                            && info.getLeafCount() == 0 );
        if( isScheduled( childNode ) ) {
          LifeCycleHelper.render( childNode );
        }
      }
      for( int i = 0; i < info.getLeafCount(); i++ ) {
        TreeLeaf leaf = info.getLeaf( i );
        shift.setLastChild( i == info.getLeafCount() - 1 );
        if( isScheduled( leaf ) ) {
          LifeCycleHelper.render( leaf );
        }
      }
      postpare( treeNode );
    }
    ItemUtil.bufferMarkedState( treeNode );
  }
  
  void createStateInfoField( final TreeNode treeNode ) throws IOException {
    if( AjaxStatusUtil.mustRender( treeNode ) ) {
      super.createStateInfoField( treeNode );
    }
  }
  
  private TreeNodeInfo updateToggleLoadingListener( final TreeNode treeNode ) {
    TreeNodeInfo info = TreeNodeRendererUtil.getInfo( treeNode );
    info.removeToggleLoadingListener();
    if( renderPlaceHolder( treeNode ) ) {
      info.addToggleLoadingListener();
    }
    return info;
  }
}
