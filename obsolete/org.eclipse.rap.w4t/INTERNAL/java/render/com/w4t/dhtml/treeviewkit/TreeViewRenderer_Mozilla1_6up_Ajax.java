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
import com.w4t.LifeCycleHelper;
import com.w4t.WebComponent;
import com.w4t.ajax.AjaxStatusUtil;
import com.w4t.dhtml.*;
import com.w4t.dhtml.renderinfo.TreeViewInfo;

/**
 * <p>The renderer for {@link org.eclipse.rwt.dhtml.TreeView <code>TreeView</code>} on 
  * Mozilla 1.6 and later with AJaX support enabled.</p>
 */
// TODO [rh] unite! copied from corresponding default renderer
public class TreeViewRenderer_Mozilla1_6up_Ajax
  extends TreeViewRenderer_Mozilla1_6up_Script
{
  
  public void render( final WebComponent component ) throws IOException {
    if( AjaxStatusUtil.mustRender( component ) ) {
      super.render( component );
    } else {
      TreeView treeView = ( TreeView )component;
      TreeViewInfo info = getInfo( treeView  );
      TreeNodeShift shift = info.getTreeNodeShift();
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
    }
  }
  
  static boolean isScheduled( final WebComponent component ) {
    return getRenderingSchedule().isScheduled( component );
  }
}
