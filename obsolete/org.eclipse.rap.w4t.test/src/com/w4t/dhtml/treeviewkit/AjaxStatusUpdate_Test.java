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

import org.eclipse.rwt.internal.browser.Ie6;
import org.eclipse.rwt.internal.lifecycle.LifeCycle;
import org.eclipse.rwt.internal.service.RequestParams;

import junit.framework.TestCase;

import com.w4t.*;
import com.w4t.IWindowManager.IWindow;
import com.w4t.dhtml.*;
import com.w4t.dhtml.treenodekit.TreeNodeRenderer;
import com.w4t.engine.W4TModelUtil;
import com.w4t.engine.util.FormManager;
import com.w4t.engine.util.WindowManager;
import com.w4t.internal.adaptable.IFormAdapter;


public class AjaxStatusUpdate_Test extends TestCase {
  
  protected void setUp() throws Exception {
    W4TFixture.setUp();
    W4TFixture.createContext( false );
  }
  
  protected void tearDown() throws Exception {
    W4TFixture.tearDown();
    W4TFixture.removeContext();
  }
  
  public void testExpandWithoutDynLoading() throws Exception {
    // Construct test form 
    WebForm form = W4TFixture.getEmptyWebFormInstance();
    FormManager.add( form );
    TreeView tree = new TreeView();
    form.add( tree, WebBorderLayout.CENTER );
    TreeNode node = new TreeNode();
    node.setLabel( "node" );
    tree.addItem( node );
    TreeLeaf leaf = new TreeLeaf();
    leaf.setLabel( "leaf" );
    node.addItem( leaf );
    tree.setDynLoading( TreeNode.DYNLOAD_NEVER );
    // Fake "execution" environment
    W4TFixture.fakeResponseWriter();
    W4TModelUtil.initModel();
    W4TFixture.fakeBrowser( new Ie6( true, true ) );
    prepareInitialRequest( form );
    W4TFixture.forceAjaxRendering( form );
    // ... and run request
    LifeCycle lifeCycle = ( LifeCycle )W4TContext.getLifeCycle();
    lifeCycle.execute();
    // ensure the treenode is initially rendered hidden
    String expected 
      = "name=\""
      + TreeNodeRenderer.TREE_NODE_TOGGLE_STATE_INFO
      + node.getUniqueID()
      + "\" value=\"vis_hide\"";
    assertTrue( W4TFixture.getAllMarkup().indexOf( expected ) != -1 );
    // simulate second request, wich sends along the info that the 
    // treenode is expanded now
    fakeExpandClick( node );
    W4TFixture.fakeResponseWriter();
    W4TFixture.fakeRequestParam( RequestParams.IS_AJAX_REQUEST, "true" );
    incRequestCounter( form );
    lifeCycle = ( LifeCycle )W4TContext.getLifeCycle();
    lifeCycle.execute();
    // treenode must be marked as expanded but not rendered 
    assertEquals( true, node.isExpanded() );
    assertEquals( TreeNode.DYNLOAD_NEVER, node.getDynLoading() );
    String nodeIdAttribute = "id=\"" + node.getUniqueID() + "\"";
    assertTrue( W4TFixture.getAllMarkup().indexOf( nodeIdAttribute ) == -1 );
  }

  private void fakeExpandClick( TreeNode node ) {
    String name = TreeNodeRenderer.TREE_NODE_TOGGLE_STATE_INFO
                + node.getUniqueID();
    W4TFixture.fakeRequestParam( name, "vis_show" );
  }

  private static void prepareInitialRequest( final WebForm form ) 
    throws Exception 
  {
    IWindow window = WindowManager.getInstance().create( form );
    IFormAdapter adapter = W4TFixture.getFormAdapter( form );
    adapter.increase();
    adapter.showInNewWindow( false );
    adapter.refreshWindow( false );
    String formId = form.getUniqueID();
    String requestCounter = String.valueOf( adapter.getRequestCounter() - 1 );
    W4TFixture.fakeFormRequestParams( requestCounter, window.getId(), formId );
    W4TFixture.fakeRequestParam( RequestParams.IS_AJAX_REQUEST, "false" );
  }
  
  private static void incRequestCounter( final WebForm form ) {
    IFormAdapter adapter = W4TFixture.getFormAdapter( form );
    String requestCounter = String.valueOf( adapter.getRequestCounter() - 1 );
    W4TFixture.fakeRequestParam( RequestParams.REQUEST_COUNTER, requestCounter );
  }
}
