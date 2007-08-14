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

import org.eclipse.rwt.internal.browser.Default;
import org.eclipse.rwt.internal.browser.Ie5_5up;

import junit.framework.TestCase;
import com.w4t.dhtml.*;
import com.w4t.dhtml.treenodekit.TreeNodeRenderer;
import com.w4t.engine.lifecycle.standard.ILifeCycleAdapter;
import com.w4t.webcheckboxkit.WebCheckBoxRenderer;


public class ReadData_Test extends TestCase {
  
  private static final String DATA_BOUND_DATA = "testData1";

  protected void setUp() throws Exception {
    W4TFixture.setUp();
    W4TFixture.createContext();
  }
  
  protected void tearDown() throws Exception {
    W4TFixture.tearDown();
    W4TFixture.removeContext();
  }
  
  public void testReadScrollPaneScript() throws Exception {
    WebForm form = new WebForm() {
      protected void setWebComponents() throws Exception {
      }
    };
    W4TFixture.fakeBrowser( new Ie5_5up( true ) );
    
    WebScrollPane scrollPane = new WebScrollPane();
    form.add( scrollPane, WebBorderLayout.NORTH );
    assertEquals( 0, scrollPane.getScrollX() );
    assertEquals( 0, scrollPane.getScrollY() );
    String paramX = "scrollPaneX_" + scrollPane.getUniqueID() + "_scrollDiv";
    String paramY = "scrollPaneY_" + scrollPane.getUniqueID() + "_scrollDiv";
    W4TFixture.fakeRequestParam( paramX, "10" );
    W4TFixture.fakeRequestParam( paramY, "20" );
    ILifeCycleAdapter adapter = getLifeCycleAdapter( form );
    adapter.readData();
    assertEquals( 10, scrollPane.getScrollX() );
    assertEquals( 20, scrollPane.getScrollY() );
  }
  
  public void testReadTreeNodeScript() throws Exception {
    WebForm form = new WebForm() {
      protected void setWebComponents() throws Exception {
      }
    };
    W4TFixture.fakeBrowser( new Default( true ) );
    
    TreeView treeView = new TreeView();
    form.add( treeView, WebBorderLayout.NORTH );
    TreeNode treeNode = new TreeNode();
    treeView.addItem( treeNode );
    String id 
      = TreeNodeRenderer.TREE_NODE_TOGGLE_STATE_INFO +  treeNode.getUniqueID();
    W4TFixture.fakeRequestParam( id, "vis_show" );
    
    assertFalse( treeNode.isExpanded() );
    ILifeCycleAdapter adapter = getLifeCycleAdapter( form );
    adapter.readData();
    assertTrue( treeNode.isExpanded() );
    
    W4TFixture.fakeRequestParam( id, "vis_hide" );
    adapter.readData();
    assertFalse( treeNode.isExpanded() );
  }

  private ILifeCycleAdapter getLifeCycleAdapter( final WebForm form ) {
    return ( ILifeCycleAdapter )form.getAdapter( ILifeCycleAdapter.class );
  }
  
  public void testReadTreeNodeNoScript() throws Exception {
    WebForm form = new WebForm() {
      protected void setWebComponents() throws Exception {
      }
    };
    W4TFixture.fakeBrowser( new Default( false ) );
    
    TreeView treeView = new TreeView();
    form.add( treeView, WebBorderLayout.NORTH );
    TreeNode treeNode = new TreeNode();
    treeView.addItem( treeNode );
    String uniqueID = treeNode.getUniqueID();
    
    String expandedName = NoscriptUtil.addExpandedPrefix( uniqueID );
    expandedName = NoscriptUtil.addSuffix( expandedName );
    W4TFixture.fakeRequestParam( expandedName, "true" );
    assertFalse( treeNode.isExpanded() );
    ILifeCycleAdapter adapter = getLifeCycleAdapter( form );
    adapter.readData();
    assertTrue( treeNode.isExpanded() );
    
    String collapsedName = NoscriptUtil.addCollapsedPrefix( uniqueID );
    collapsedName = NoscriptUtil.addSuffix( collapsedName );
    W4TFixture.fakeRequestParam( collapsedName, "true" );
    adapter.readData();
    assertFalse( treeNode.isExpanded() );
  }
  
  public void testReadDataBoundsNoScript() throws Exception {
    WebForm form = new WebForm() {
      protected void setWebComponents() throws Exception {
      }
    };
    W4TFixture.fakeBrowser( new Default( false ) );
    
    WebText text = new WebText();
    W4TFixture.fakeRequestParam( text.getUniqueID(), DATA_BOUND_DATA );
    form.add( text, WebBorderLayout.NORTH );
    assertEquals( "", text.getValue() );
    
    WebTextArea textArea = new WebTextArea();
    W4TFixture.fakeRequestParam( textArea.getUniqueID(), DATA_BOUND_DATA );
    form.add( textArea, WebBorderLayout.NORTH );
    assertEquals( "", textArea.getValue() );
    
    WebSelect select = new WebSelect();
    select.setItem( new String[] { DATA_BOUND_DATA } );
    W4TFixture.fakeRequestParam( select.getUniqueID(), DATA_BOUND_DATA );
    form.add( select, WebBorderLayout.NORTH );
    assertEquals( DATA_BOUND_DATA, select.getValue() );
    select.setUseEmptyItem( true );
    assertEquals( "", select.getValue() );
    
    WebRadioButtonGroup radioGroup = new WebRadioButtonGroup();
    WebRadioButton radioButton = new WebRadioButton();
    radioGroup.add( radioButton );
    radioButton.setValue( DATA_BOUND_DATA );
    W4TFixture.fakeRequestParam( radioGroup.getUniqueID(), DATA_BOUND_DATA );
    form.add( radioGroup, WebBorderLayout.NORTH );
    assertFalse( radioButton.isSelected() );
    
    WebCheckBox checkBox = new WebCheckBox();
    String id = checkBox.getUniqueID();
    W4TFixture.fakeRequestParam( id, checkBox.getValCheck() );
    W4TFixture.fakeRequestParam( WebCheckBoxRenderer.PREFIX + id,
                              checkBox.getValUnCheck() );
    form.add( checkBox, WebBorderLayout.NORTH );
    assertEquals( checkBox.getValUnCheck(), checkBox.getValue() );
    
    ILifeCycleAdapter adapter = getLifeCycleAdapter( form );
    adapter.readData();
    assertEquals( DATA_BOUND_DATA, text.getValue() );
    assertEquals( DATA_BOUND_DATA, textArea.getValue() );
    assertEquals( DATA_BOUND_DATA, select.getValue() );
    assertTrue( radioButton.isSelected() );    
    assertEquals( checkBox.getValCheck(), checkBox.getValue() );
  }
  
  public void testReadDataBoundsScript() throws Exception {
    WebForm form = new WebForm() {
      protected void setWebComponents() throws Exception {
      }
    };
    W4TFixture.fakeBrowser( new Default( true ) );
    
    WebText text = new WebText();
    W4TFixture.fakeRequestParam( text.getUniqueID(), DATA_BOUND_DATA );
    form.add( text, WebBorderLayout.NORTH );
    assertEquals( "", text.getValue() );
    
    WebTextArea textArea = new WebTextArea();
    W4TFixture.fakeRequestParam( textArea.getUniqueID(), DATA_BOUND_DATA );
    form.add( textArea, WebBorderLayout.NORTH );
    assertEquals( "", textArea.getValue() );
    
    WebSelect select = new WebSelect();
    select.setItem( new String[] { DATA_BOUND_DATA } );
    W4TFixture.fakeRequestParam( select.getUniqueID(), DATA_BOUND_DATA );
    form.add( select, WebBorderLayout.NORTH );
    assertEquals( DATA_BOUND_DATA, select.getValue() );
    select.setUseEmptyItem( true );
    assertEquals( "", select.getValue() );
    
    WebRadioButtonGroup radioGroup = new WebRadioButtonGroup();
    WebRadioButton radioButton = new WebRadioButton();
    radioGroup.add( radioButton );
    radioButton.setValue( DATA_BOUND_DATA );
    W4TFixture.fakeRequestParam( radioGroup.getUniqueID(), DATA_BOUND_DATA );
    form.add( radioGroup, WebBorderLayout.SOUTH );
    radioGroup.setValue( "other-value" );
    assertFalse( radioButton.isSelected() );
    
    WebCheckBox checkBox = new WebCheckBox();
    String id = checkBox.getUniqueID();
    W4TFixture.fakeRequestParam( id, checkBox.getValCheck() );
    W4TFixture.fakeRequestParam( WebCheckBoxRenderer.PREFIX + id,
                              checkBox.getValUnCheck() );
    form.add( checkBox, WebBorderLayout.NORTH );
    assertEquals( checkBox.getValUnCheck(), checkBox.getValue() );
    
    ILifeCycleAdapter adapter = getLifeCycleAdapter( form );
    adapter.readData();
    assertEquals( DATA_BOUND_DATA, text.getValue() );
    assertEquals( DATA_BOUND_DATA, textArea.getValue() );
    assertEquals( DATA_BOUND_DATA, select.getValue() );
    assertTrue( radioButton.isSelected() );    
    assertEquals( checkBox.getValCheck(), checkBox.getValue() );
  }
}
