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
import org.eclipse.rwt.internal.service.ContextProvider;

import com.w4t.*;
import com.w4t.dhtml.*;
import com.w4t.dhtml.renderinfo.TreeViewInfo;


/** <p>The superclass for all renderers for org.eclipse.rap.dhtml.TreeView on 
  * more recent browsers that support javascript.</p>
  * 
  * <p>Note that this is not an actual renderer but encapsulates common 
  * functionality for browser-specific renderers.</p>
  */
abstract class TreeViewRenderer_Base_Script extends TreeViewRenderer {
  
  public void processAction( final WebComponent component ) {
    TreeView treeView = ( TreeView )component;
    DHTMLProcessActionUtil.processTreeNodeExpandedScript( treeView );
    DHTMLProcessActionUtil.processTreeNodeCollapsedScript( treeView );
    DHTMLProcessActionUtil.processDragDropScript( treeView );
    ProcessActionUtil.processActionPerformedScript( treeView );
  }

  public void render( final WebComponent component ) throws IOException {
    useJSLibrary();
    TreeView treeView = ( TreeView )component;
    createOuterDivOpen( treeView );
    
    TreeViewInfo info = getInfo( treeView );
    TreeNodeShift shift = info.getTreeNodeShift();
    
    // Note: We switch the HtmlResponseWriter of the request lifecycle
    // to a temporary one, to retrieve state info that has to
    // be added as hidden fields before the markup that is created
    // by the nodes and leafs of the tree:
    // Clear the old hidden fields code with the TreeNode states.
    // After that create the render content of the tree nodes and leafs
    // into a seperate buffer. Then append the state info hidden fields 
    // to the main buffer and finally also append the markup of the nodes
    // and leafs to the main buffer.
    info.clearStateInfoFields();
    HtmlResponseWriter out = getResponseWriter();
    HtmlResponseWriter tbTree = new HtmlResponseWriter();
    ContextProvider.getStateInfo().setResponseWriter( tbTree );
    // TODO: revise this: this is a hack (exchanging response writer)
    out.writeText( "", null );
    try {
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
    } finally {
      ContextProvider.getStateInfo().setResponseWriter( out );
    }
    info.appendStateInfoFields( out );
    out.mergeRegisteredCssClasses( tbTree.getCssClasses() );
    out.append( tbTree );
    createOuterDivClose();
  }

  protected abstract void useJSLibrary() throws IOException;
  
}
