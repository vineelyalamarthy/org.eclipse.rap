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
import com.w4t.dhtml.*;

/** <p>Tests functionalities in com.w4tWebComponent.</p>
  */
public class WebComponent_Test extends TestCase {

  public WebComponent_Test( final String name ) {
    super( name );
  }

  protected void setUp() throws Exception {
    Fixture.setUp();
    Fixture.createContext();
  }
  
  protected void tearDown() throws Exception {
    Fixture.tearDown();
    Fixture.removeContext();
  }

  // testing methods
  //////////////////

  public void testSetName() throws Exception {
    WebButton target = new WebButton();
    assertEquals( target.getName(), "" );
    
    target.setName( "Test1" );
    assertEquals( target.getName(), "Test1" );
    
    target.setName( "test" );
    assertEquals( target.getName(), "test" );
    
    // empty String is not accepted
    target.setName( "" );
    assertEquals( target.getName(), "test" );
    
    // no string starting with digit
    target.setName( "1Test" );
    assertEquals( target.getName(), "test" );

    // no string containing special chars
    target.setName( "te!st" );
    assertEquals( target.getName(), "test" );
   
    // no string with blanks
    target.setName( "Test it" );
    assertEquals( target.getName(), "test" );
   
    // one-char string 
    target.setName( "x" );
    assertEquals( target.getName(), "x" );
  }
  
  public void testGetWebForm() throws Exception {
    WebForm form = Fixture.getEmptyWebFormInstance();
    assertSame( form, form.getWebForm() );
    
    WebButton button = new WebButton();
    assertNull( button.getWebForm() );
    
    form.add( button, WebBorderLayout.NORTH );
    assertSame( form, button.getWebForm() );
    
    WebContainer container = new WebPanel();
    assertNull( container.getWebForm() );
    
    form.add( container, WebBorderLayout.NORTH );
    assertSame( form, container.getWebForm() );
    
    WebText text = new WebText();
    assertNull( text.getWebForm() );
    
    container.add( text );
    assertSame( form, text.getWebForm() );
    
    WebLabel label = new WebLabel();
    assertNull( label.getWebForm() );
    
    Decorator decorator = new Decorator( label ) {};
    assertNull( label.getWebForm() );
    assertNull( decorator.getWebForm() );
    
    container.add( decorator );
    assertSame( form, decorator.getWebForm() );
    assertSame( form, label.getWebForm() );
    
    TreeView treeView = new TreeView();
    assertNull( treeView.getWebForm() );
    
    container.add( treeView );
    assertSame( form, treeView.getWebForm() );
    
    TreeNode treeNode1 = new TreeNode();
    assertNull( treeNode1.getWebForm() );
    
    treeView.addItem( treeNode1 );
    assertSame( form, treeNode1.getWebForm() );
    
    TreeNode treeNode2 = new TreeNode();
    assertNull( treeNode2.getWebForm() );
    
    treeNode1.addItem( treeNode2 );
    assertSame( form, treeNode2.getWebForm() );
    
    TreeLeaf treeLeaf = new TreeLeaf();
    assertNull( treeLeaf.getWebForm() );
    
    treeNode2.addItem( treeLeaf );
    assertSame( form, treeLeaf.getWebForm() );
    
    treeLeaf.remove();
    assertNull( treeLeaf.getWebForm() );
    treeNode1.remove();
    assertNull( treeNode1.getWebForm() );
    assertNull( treeNode2.getWebForm() );
    treeView.remove();
    assertNull( treeView.getWebForm() );
    decorator.remove();
    assertNull( decorator.getWebForm() );
    assertNull( label.getWebForm() );
    text.remove();
    assertNull( text.getWebForm() );
    container.remove();
    assertNull( container.getWebForm() );
    button.remove();
    assertNull( button.getWebForm() );
  }
  
  public void testGetParent() throws Exception {
    WebForm form = Fixture.getEmptyWebFormInstance();
    assertNull( form.getParent() );
    
    WebButton button = new WebButton();
    assertNull( button.getParent() );
    
    form.add( button, WebBorderLayout.NORTH );
    assertSame( form, button.getParent() );
    
    WebContainer container = new WebPanel();
    assertNull( container.getParent() );
    
    form.add( container, WebBorderLayout.NORTH );
    assertSame( form, container.getParent() );
    
    WebText text = new WebText();
    assertNull( text.getParent() );
    
    container.add( text );
    assertSame( container, text.getParent() );
    
    WebLabel label = new WebLabel();
    assertNull( label.getParent() );
    
    Decorator decorator = new Decorator( label ) {};
    assertNull( label.getParent() );
    assertNull( decorator.getParent() );
    
    container.add( decorator );
    assertSame( container, decorator.getParent() );
    assertSame( container, label.getParent() );
    
    TreeView treeView = new TreeView();
    assertNull( treeView.getParent() );
    
    container.add( treeView );
    assertSame( container, treeView.getParent() );
    
    TreeNode treeNode1 = new TreeNode();
    assertNull( treeNode1.getParent() );
    
    treeView.addItem( treeNode1 );
    assertSame( container, treeNode1.getParent() );
    
    TreeNode treeNode2 = new TreeNode();
    assertNull( treeNode2.getParent() );
    
    treeNode1.addItem( treeNode2 );
    assertSame( container, treeNode2.getParent() );
    
    TreeLeaf treeLeaf = new TreeLeaf();
    assertNull( treeLeaf.getParent() );
    
    treeNode2.addItem( treeLeaf );
    assertSame( container, treeLeaf.getParent() );
    
    treeLeaf.remove();
    assertNull( treeLeaf.getParent() );
    treeNode1.remove();
    assertNull( treeNode1.getParent() );
    assertNull( treeNode2.getParent() );
    treeView.remove();
    assertNull( treeView.getParent() );
    decorator.remove();
    assertNull( decorator.getParent() );
    assertNull( label.getParent() );
    text.remove();
    assertNull( text.getParent() );
    container.remove();
    assertNull( container.getParent() );
    button.remove();
    assertNull( button.getParent() );
    
    treeNode2.addItem( treeLeaf );
    container.add( treeView );
    treeView.addItem( treeNode1 );
    assertSame( container, treeView.getParent() );
    assertSame( container, treeNode1.getParent() );
    assertSame( container, treeNode2.getParent() );
    assertSame( container, treeLeaf.getParent() );
    
    container.add( decorator );
    assertSame( container, decorator.getParent() );
    assertSame( container, label.getParent() );
    
    container.remove( decorator );
    assertNull( decorator.getParent() );
    assertNull( label.getParent() );
    
    container.remove( treeView );
    assertNull( treeView.getParent() );
    assertNull( treeNode1.getParent() );
    assertNull( treeNode2.getParent() );
    assertNull( treeLeaf.getParent() );
    
    container.add( treeView );
    treeView.addItem( treeNode1 );
    treeView.removeAllItems();
    assertSame( container, treeView.getParent() );
  }
  
  public void testEnablement() throws Exception {
    WebContainer container = new WebPanel();
    assertTrue( container.isEnabled() );
    
    WebButton button = new WebButton();
    assertTrue( button.isEnabled() );
    
    container.add( button );
    container.setEnabled( false );
    assertFalse( container.isEnabled() );
    assertFalse( button.isEnabled() );
    
    button.remove();
    assertTrue( button.isEnabled() );
    
    Decorator decorator = new Decorator( button ) {};
    assertTrue( decorator.isEnabled() );
    assertTrue( button.isEnabled() );

    container.add( decorator );
    assertFalse( decorator.isEnabled() );
    assertFalse( button.isEnabled() );
    
    TreeView treeView = new TreeView();
    TreeNode treeNode1 = new TreeNode();
    treeView.addItem( treeNode1 );
    TreeNode treeNode2 = new TreeNode();
    treeNode1.addItem( treeNode2 );
    TreeLeaf treeLeaf = new TreeLeaf();
    treeNode2.addItem( treeLeaf );
    
    assertTrue( treeView.isEnabled() );
    assertTrue( treeNode1.isEnabled() );
    assertTrue( treeNode2.isEnabled() );
    assertTrue( treeLeaf.isEnabled() );
    
    treeView.setEnabled( false );
    assertFalse( treeView.isEnabled() );
    assertFalse( treeNode1.isEnabled() );
    assertFalse( treeNode2.isEnabled() );
    assertFalse( treeLeaf.isEnabled() );
    
    treeView.setEnabled( true );
    container.add( treeView );
    assertFalse( treeView.isEnabled() );
    assertFalse( treeNode1.isEnabled() );
    assertFalse( treeNode2.isEnabled() );
    assertFalse( treeLeaf.isEnabled() );    
  }
  
  public void testVisibility() throws Exception {
    WebContainer container = new WebPanel();
    assertTrue( container.isVisible() );
    
    WebButton button = new WebButton();
    assertTrue( button.isVisible() );
    
    container.add( button );
    container.setVisible( false );
    assertFalse( container.isVisible() );
    assertFalse( button.isVisible() );
    
    button.remove();
    assertTrue( button.isVisible() );
    
    Decorator decorator = new Decorator( button ) {};
    assertTrue( decorator.isVisible() );
    assertTrue( button.isVisible() );

    container.add( decorator );
    assertFalse( decorator.isVisible() );
    assertFalse( button.isVisible() );
    
    TreeView treeView = new TreeView();
    TreeNode treeNode1 = new TreeNode();
    treeView.addItem( treeNode1 );
    TreeNode treeNode2 = new TreeNode();
    treeNode1.addItem( treeNode2 );
    TreeLeaf treeLeaf = new TreeLeaf();
    treeNode2.addItem( treeLeaf );
    
    assertTrue( treeView.isVisible() );
    assertTrue( treeNode1.isVisible() );
    assertTrue( treeNode2.isVisible() );
    assertTrue( treeLeaf.isVisible() );
    
    treeView.setVisible( false );
    assertFalse( treeView.isVisible() );
    assertFalse( treeNode1.isVisible() );
    assertFalse( treeNode2.isVisible() );
    assertFalse( treeLeaf.isVisible() );
    
    treeView.setVisible( true );
    container.add( treeView );
    assertFalse( treeView.isVisible() );
    assertFalse( treeNode1.isVisible() );
    assertFalse( treeNode2.isVisible() );
    assertFalse( treeLeaf.isVisible() );    
  }
}