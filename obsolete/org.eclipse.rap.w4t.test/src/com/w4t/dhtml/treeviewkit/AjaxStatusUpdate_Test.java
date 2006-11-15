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

import junit.framework.TestCase;
import com.w4t.*;
import com.w4t.IWindowManager.IWindow;
import com.w4t.dhtml.*;
import com.w4t.dhtml.treenodekit.TreeNodeRenderer;
import com.w4t.engine.W4TModelUtil;
import com.w4t.engine.lifecycle.LifeCycle;
import com.w4t.engine.requests.RequestParams;
import com.w4t.engine.util.FormManager;
import com.w4t.engine.util.WindowManager;
import com.w4t.internal.adaptable.IFormAdapter;
import com.w4t.util.browser.Ie6;


public class AjaxStatusUpdate_Test extends TestCase {
  
  protected void setUp() throws Exception {
    Fixture.setUp();
    Fixture.createContext( false );
  }
  
  protected void tearDown() throws Exception {
    Fixture.tearDown();
    Fixture.removeContext();
  }
  
  public void testExpandWithoutDynLoading() throws Exception {
    // Construct test form 
    WebForm form = Fixture.getEmptyWebFormInstance();
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
    Fixture.fakeResponseWriter();
    W4TModelUtil.initModel();
    Fixture.fakeBrowser( new Ie6( true, true ) );
    prepareInitialRequest( form );
    Fixture.forceAjaxRendering( form );
    // ... and run request
    LifeCycle lifeCycle = ( LifeCycle )W4TContext.getLifeCycle();
    lifeCycle.execute();
    // ensure the treenode is initially rendered hidden
    String expected 
      = "name=\""
      + TreeNodeRenderer.TREE_NODE_TOGGLE_STATE_INFO
      + node.getUniqueID()
      + "\" value=\"vis_hide\"";
    assertTrue( Fixture.getAllMarkup().indexOf( expected ) != -1 );
    // simulate second request, wich sends along the info that the 
    // treenode is expanded now
    fakeExpandClick( node );
    Fixture.fakeResponseWriter();
    Fixture.fakeRequestParam( RequestParams.IS_AJAX_REQUEST, "true" );
    incRequestCounter( form );
    lifeCycle = ( LifeCycle )W4TContext.getLifeCycle();
    lifeCycle.execute();
    // treenode must be marked as expanded but not rendered 
    assertEquals( true, node.isExpanded() );
    assertEquals( TreeNode.DYNLOAD_NEVER, node.getDynLoading() );
    String nodeIdAttribute = "id=\"" + node.getUniqueID() + "\"";
    assertTrue( Fixture.getAllMarkup().indexOf( nodeIdAttribute ) == -1 );
  }

  private void fakeExpandClick( TreeNode node ) {
    String name = TreeNodeRenderer.TREE_NODE_TOGGLE_STATE_INFO
                + node.getUniqueID();
    Fixture.fakeRequestParam( name, "vis_show" );
  }

  private static void prepareInitialRequest( final WebForm form ) 
    throws Exception 
  {
    IWindow window = WindowManager.getInstance().create( form );
    IFormAdapter adapter = Fixture.getFormAdapter( form );
    adapter.increase();
    adapter.showInNewWindow( false );
    adapter.refreshWindow( false );
    String formId = form.getUniqueID();
    String requestCounter = String.valueOf( adapter.getRequestCounter() - 1 );
    Fixture.fakeFormRequestParams( requestCounter, window.getId(), formId );
    Fixture.fakeRequestParam( RequestParams.IS_AJAX_REQUEST, "false" );
  }
  
  private static void incRequestCounter( final WebForm form ) {
    IFormAdapter adapter = Fixture.getFormAdapter( form );
    String requestCounter = String.valueOf( adapter.getRequestCounter() - 1 );
    Fixture.fakeRequestParam( RequestParams.REQUEST_COUNTER, requestCounter );
  }
}
