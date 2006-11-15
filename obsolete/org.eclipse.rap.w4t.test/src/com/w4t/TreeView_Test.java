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
package com.w4t;

import junit.framework.TestCase;
import com.w4t.dhtml.TreeNode;
import com.w4t.dhtml.TreeView;


public class TreeView_Test extends TestCase {
  
  public void testRemoveAll() throws Exception {
    // create form
    WebForm form = Fixture.getEmptyWebFormInstance();
    // create tree 
    TreeView tree = new TreeView();
    form.add( tree, WebBorderLayout.CENTER );
    // populate tree
    tree.addItem( new TreeNode() );
    tree.removeAllItems();
    assertEquals( "tree must not have nodes after removeAllItems", 
                  0, tree.getChildCount() );
    assertNotNull( "removeAllItems changed parent of tree", tree.getWebForm() );
  }
  
  protected void setUp() throws Exception {
    Fixture.setUp();
    Fixture.createContext();
  }
  
  protected void tearDown() throws Exception {
    Fixture.tearDown();
    Fixture.removeContext();
  }
  
}
