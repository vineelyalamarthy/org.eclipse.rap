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

import com.w4t.*;
import com.w4t.dhtml.*;
import com.w4t.dhtml.renderinfo.TreeViewInfo;


/** <p>The superclass for all renderers for 
  * {@link org.eclipse.rwt.dhtml.TreeView TreeView} on browsers without 
  * javascript support.</p>
  * 
  * <p>Note that this is not an actual renderer but encapsulates common 
  * functionality for browser-specific renderers.</p>
  */
public abstract class TreeViewRenderer_Base_Noscript extends TreeViewRenderer {
  
  public void processAction( final WebComponent component ) {
    TreeView treeView = ( TreeView )component;
    DHTMLProcessActionUtil.processTreeNodeExpandedNoScript( treeView );
    DHTMLProcessActionUtil.processTreeNodeCollapsedNoScript( treeView );
    DHTMLProcessActionUtil.processDragDropNoScript( treeView );
    ProcessActionUtil.processActionPerformedNoScript( treeView );
  }

  public void render( final WebComponent component ) throws IOException {
    TreeView treeView = ( TreeView )component; 
    createOuterDivOpen( treeView );
    createTree( treeView );
    createOuterDivClose();
  }

  private void createTree( final TreeView treeView ) throws IOException {
    HtmlResponseWriter tbTree = new HtmlResponseWriter();
    TreeViewInfo info = getInfo( treeView );
    TreeNodeShift shift = info.getTreeNodeShift();
    
    for( int i = 0; i < info.getNodeCount(); i++ ) {
      TreeNode childNode = info.getNode( i );
      shift.setLastChild(    i == info.getNodeCount() - 1 
                             && info.getLeafCount() == 0 );
      LifeCycleHelper.render( childNode );
    }
    for( int i = 0; i < info.getLeafCount(); i++ ) {
      TreeLeaf leaf = info.getLeaf( i );
      shift.setLastChild( i == info.getLeafCount() - 1 );
      LifeCycleHelper.render( leaf );
    }
    HtmlResponseWriter out = getResponseWriter();
    out.mergeRegisteredCssClasses( tbTree.getCssClasses() );
    // TODO: revise this: this is a hack (exchanging response writer)
    out.append( tbTree ); 
  }
}