/*******************************************************************************
 * Copyright (c) 2002, 2010 Innoopract Informationssysteme GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Innoopract Informationssysteme GmbH - initial API and implementation
 *     EclipseSource - ongoing development
 ******************************************************************************/
package org.eclipse.swt.widgets;

import java.util.*;
import java.util.List;

import junit.framework.TestCase;

import org.eclipse.rwt.Fixture;
import org.eclipse.rwt.graphics.Graphics;
import org.eclipse.rwt.lifecycle.PhaseId;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.internal.widgets.ITreeAdapter;
import org.eclipse.swt.internal.widgets.ItemHolder;

public class Tree_Test extends TestCase {

  public void testGetItemsAndGetItemCount() {
    Display display = new Display();
    Composite shell = new Shell( display, SWT.NONE );
    Tree tree = new Tree( shell, SWT.NONE );
    assertEquals( 0, tree.getItemCount() );
    assertEquals( 0, tree.getItems().length );
    TreeItem item = new TreeItem( tree, SWT.NONE );
    assertEquals( 1, tree.getItemCount() );
    assertEquals( 1, tree.getItems().length );
    assertSame( item, tree.getItems()[ 0 ] );
  }

  public void testImage() {
    Display display = new Display();
    Composite shell = new Shell( display, SWT.NONE );
    Tree tree = new Tree( shell, SWT.NONE );
    TreeItem item1 = new TreeItem( tree, SWT.NONE );
    item1.setImage( Graphics.getImage( Fixture.IMAGE1 ) );
    assertSame( Graphics.getImage( Fixture.IMAGE1 ), item1.getImage() );
    TreeItem item2 = new TreeItem( tree, SWT.NONE );
    item2.setImage( Graphics.getImage( Fixture.IMAGE2 ) );
    assertSame( Graphics.getImage( Fixture.IMAGE2 ), item2.getImage() );
  }

  public void testStyle() {
    Display display = new Display();
    Composite shell = new Shell( display, SWT.NONE );
    Tree tree1 = new Tree( shell, SWT.NONE );
    assertTrue( ( tree1.getStyle() & SWT.SINGLE ) != 0 );
    assertTrue( ( tree1.getStyle() & SWT.H_SCROLL ) != 0 );
    assertTrue( ( tree1.getStyle() & SWT.V_SCROLL ) != 0 );
    Tree tree2 = new Tree( shell, SWT.SINGLE );
    assertTrue( ( tree2.getStyle() & SWT.SINGLE ) != 0 );
    assertTrue( ( tree2.getStyle() & SWT.H_SCROLL ) != 0 );
    assertTrue( ( tree2.getStyle() & SWT.V_SCROLL ) != 0 );
    Tree tree3 = new Tree( shell, SWT.MULTI );
    assertTrue( ( tree3.getStyle() & SWT.MULTI ) != 0 );
    assertTrue( ( tree3.getStyle() & SWT.H_SCROLL ) != 0 );
    assertTrue( ( tree3.getStyle() & SWT.V_SCROLL ) != 0 );
    Tree tree4 = new Tree( shell, SWT.SINGLE | SWT.MULTI );
    assertTrue( ( tree4.getStyle() & SWT.SINGLE ) != 0 );
    assertTrue( ( tree4.getStyle() & SWT.H_SCROLL ) != 0 );
    assertTrue( ( tree4.getStyle() & SWT.V_SCROLL ) != 0 );
  }

  public void testItemHierarchy() {
    Display display = new Display();
    Composite shell = new Shell( display, SWT.NONE );
    Tree tree = new Tree( shell, SWT.NONE );
    TreeItem item = new TreeItem( tree, SWT.NONE );
    TreeItem subItem = new TreeItem( item, SWT.NONE );
    assertEquals( 1, item.getItems().length );
    assertEquals( null, item.getParentItem() );
    assertEquals( tree, item.getParent() );
    assertEquals( subItem, item.getItems()[ 0 ] );
    assertEquals( tree, subItem.getParent() );
    assertEquals( item, subItem.getParentItem() );
    assertEquals( 0, subItem.getItems().length );
  }

  public void testDispose() {
    Display display = new Display();
    Composite shell = new Shell( display, SWT.NONE );
    Tree tree = new Tree( shell, SWT.NONE );
    TreeItem item = new TreeItem( tree, SWT.NONE );
    TreeItem subItem = new TreeItem( item, SWT.NONE );
    TreeColumn column = new TreeColumn( tree, SWT.NONE );
    tree.dispose();
    assertEquals( true, item.isDisposed() );
    assertEquals( true, subItem.isDisposed() );
    assertEquals( true, column.isDisposed() );
    assertEquals( 0, ItemHolder.getItems( tree ).length );
    assertEquals( 0, ItemHolder.getItems( item ).length );
    assertEquals( 0, ItemHolder.getItems( subItem ).length );
  }

  public void testDisposeSelected() {
    Display display = new Display();
    Composite shell = new Shell( display, SWT.NONE );
    Tree tree = new Tree( shell, SWT.MULTI );
    TreeItem item = new TreeItem( tree, SWT.NONE );
    TreeItem subItem = new TreeItem( item, SWT.NONE );
    TreeItem otherItem = new TreeItem( tree, SWT.NONE );
    tree.setSelection( new TreeItem[]{
      subItem, otherItem
    } );
    item.dispose();
    assertEquals( true, item.isDisposed() );
    assertEquals( true, subItem.isDisposed() );
    assertEquals( false, otherItem.isDisposed() );
    assertEquals( 1, tree.getSelectionCount() );
    assertSame( otherItem, tree.getSelection()[ 0 ] );
  }

  public void testIndexOf() {
    Display display = new Display();
    Composite shell = new Shell( display, SWT.NONE );
    Tree tree = new Tree( shell, SWT.NONE );
    TreeItem item = new TreeItem( tree, SWT.NONE );
    assertEquals( 0, tree.indexOf( item ) );
    item.dispose();
    try {
      tree.indexOf( item );
      fail( "Must not allow to call indexOf for disposed item" );
    } catch( IllegalArgumentException e ) {
      // expected
    }
    try {
      tree.indexOf( ( TreeItem )null );
      fail( "Must not allow to call indexOf for null" );
    } catch( IllegalArgumentException iae ) {
      // expected
    }
    Tree anotherTree = new Tree( shell, SWT.NONE );
    TreeItem anotherItem = new TreeItem( anotherTree, SWT.NONE );
    assertEquals( -1, tree.indexOf( anotherItem ) );
  }

  public void testIndexOfColumns() {
    Display display = new Display();
    Composite shell = new Shell( display, SWT.NONE );
    Tree tree = new Tree( shell, SWT.NONE );
    TreeColumn col = new TreeColumn( tree, SWT.NONE );
    assertEquals( 0, tree.indexOf( col ) );
    col.dispose();
    try {
      tree.indexOf( col );
      fail( "Must not allow to call indexOf for disposed item" );
    } catch( IllegalArgumentException e ) {
      // expected
    }
    try {
      tree.indexOf( ( TreeColumn )null );
      fail( "Must not allow to call indexOf for null" );
    } catch( IllegalArgumentException iae ) {
      // expected
    }
  }

  public void testExpandCollapse() {
    Fixture.fakePhase( PhaseId.PROCESS_ACTION );
    final StringBuffer log = new StringBuffer();
    Display display = new Display();
    Composite shell = new Shell( display, SWT.NONE );
    Tree tree = new Tree( shell, SWT.NONE );
    tree.addTreeListener( new TreeListener() {
      public void treeCollapsed( final TreeEvent e ) {
        log.append( "collapsed" );
      }
      public void treeExpanded( final TreeEvent e ) {
        log.append( "expanded" );
      }
    } );
    TreeItem item = new TreeItem( tree, SWT.NONE );
    TreeItem subItem = new TreeItem( item, SWT.NONE );
    TreeItem childlessItem = new TreeItem( tree, SWT.NONE );
    // ensure initial state
    assertEquals( false, item.getExpanded() );
    // ensure changing works
    item.setExpanded( true );
    assertEquals( true, item.getExpanded() );
    // test that items without sub-items cannot be expanded
    childlessItem.setExpanded( true );
    assertEquals( false, childlessItem.getExpanded() );
    // test that an item is still expanded when it has no more sub items
    item.setExpanded( true );
    subItem.dispose();
    assertEquals( true, item.getExpanded() );
    // ensure that calling setExpanded does not raise any events
    assertEquals( "", log.toString() );
  }

  public void testSelectionForSingle() {
    Display display = new Display();
    Composite shell = new Shell( display, SWT.NONE );
    Tree tree = new Tree( shell, SWT.SINGLE );
    // Verify that an empty tree has an empty selection
    assertEquals( 0, tree.getItemCount() );
    assertTrue( Arrays.equals( new TreeItem[ 0 ], tree.getSelection() ) );
    assertEquals( 0, tree.getSelectionCount() );
    // Verify that adding a TreeItem does not change the current selection
    TreeItem treeItem1 = new TreeItem( tree, SWT.NONE );
    assertEquals( 1, tree.getItemCount() );
    assertTrue( Arrays.equals( new TreeItem[ 0 ], tree.getSelection() ) );
    assertEquals( 0, tree.getSelectionCount() );
    // Test selecting a single treItem
    tree.setSelection( treeItem1 );
    assertEquals( 1, tree.getSelectionCount() );
    TreeItem[] expected = new TreeItem[]{
      treeItem1
    };
    assertTrue( Arrays.equals( expected, tree.getSelection() ) );
    // Verify that getSelection returns a safe copy
    tree.setSelection( treeItem1 );
    TreeItem[] selection = tree.getSelection();
    selection[ 0 ] = null;
    assertSame( treeItem1, tree.getSelection()[ 0 ] );
    // Test that null-argument leads to an exception
    try {
      tree.setSelection( ( TreeItem )null );
      fail( "must not allow setSelection( null )" );
    } catch( IllegalArgumentException iae ) {
      // expected
    }
    // Test de-selecting all items
    tree.setSelection( new TreeItem[ 0 ] );
    assertEquals( 0, tree.getSelectionCount() );
    assertEquals( 0, tree.getSelection().length );
    // Test selecting a single treeItem with setSelection(TreeItem[])
    tree.setSelection( new TreeItem[]{
      treeItem1
    } );
    assertEquals( 1, tree.getSelectionCount() );
    assertSame( treeItem1, tree.getSelection()[ 0 ] );
    // Test that setSelection(TreeItem[]) copies the argument
    TreeItem[] newSelection = new TreeItem[]{
      treeItem1
    };
    tree.setSelection( newSelection );
    newSelection[ 0 ] = null;
    assertSame( treeItem1, tree.getSelection()[ 0 ] );
    // Test that calling setSelection(TreeItem[]) with a null-argument leads
    // to an exception
    try {
      tree.setSelection( ( org.eclipse.swt.widgets.TreeItem[] )null );
      fail( "must not allow setSelection( null )" );
    } catch( IllegalArgumentException iae ) {
      // expected
    }
    // Test calling setSelection(TreeItem[]) with more than one element
    TreeItem treeItem2 = new TreeItem( tree, SWT.NONE );
    tree.setSelection( new TreeItem[]{
      treeItem2, treeItem1, null
    } );
    assertEquals( 0, tree.getSelectionCount() );
    assertTrue( Arrays.equals( new TreeItem[ 0 ], tree.getSelection() ) );
    // Test calling setSelection(TreeItem[]) with one null-element
    // -> must not change current selection
    tree.setSelection( new TreeItem[]{
      treeItem1
    } );
    tree.setSelection( new TreeItem[]{
      null
    } );
    assertEquals( 1, tree.getSelectionCount() );
    assertSame( treeItem1, tree.getSelection()[ 0 ] );
    // Verify that selecting a disposed of item throws an exception
    try {
      tree.setSelection( treeItem1 );
      TreeItem disposedItem = new TreeItem( tree, SWT.NONE );
      disposedItem.dispose();
      tree.setSelection( disposedItem );
      fail( "Must not allow to select diposed of tree item." );
    } catch( IllegalArgumentException e ) {
      // ensure that the previously set selection is not changed
      assertSame( treeItem1, tree.getSelection()[ 0 ] );
    }
  }

  public void testSelectionForMulti() {
    Display display = new Display();
    Composite shell = new Shell( display, SWT.NONE );
    Tree tree = new Tree( shell, SWT.MULTI );
    // Verify that an empty tree has an empty selection
    assertEquals( 0, tree.getItemCount() );
    assertTrue( Arrays.equals( new TreeItem[ 0 ], tree.getSelection() ) );
    assertEquals( 0, tree.getSelectionCount() );
    // Verify that adding a TreeItem does not change the current selection
    TreeItem treeItem1 = new TreeItem( tree, SWT.NONE );
    assertEquals( 1, tree.getItemCount() );
    assertTrue( Arrays.equals( new TreeItem[ 0 ], tree.getSelection() ) );
    assertEquals( 0, tree.getSelectionCount() );
    // Test de-selecting all items
    tree.setSelection( new TreeItem[ 0 ] );
    assertEquals( 0, tree.getSelectionCount() );
    assertEquals( 0, tree.getSelection().length );
    // Ensure that setSelection( item ) does the same as
    // setSelection( new TreeItem[] { item } )
    tree.setSelection( treeItem1 );
    TreeItem selected1 = tree.getSelection()[ 0 ];
    tree.setSelection( new TreeItem[]{
      treeItem1
    } );
    TreeItem selected2 = tree.getSelection()[ 0 ];
    assertSame( selected1, selected2 );
    // Select two treeItems
    TreeItem treeItem2 = new TreeItem( tree, SWT.NONE );
    tree.setSelection( new TreeItem[]{
      treeItem1, treeItem2
    } );
    assertEquals( 2, tree.getSelectionCount() );
    TreeItem[] expected = new TreeItem[]{
      treeItem1, treeItem2
    };
    assertTrue( Arrays.equals( expected, tree.getSelection() ) );
    // Test calling setSelection(TreeItem[]) with one null-element
    // -> must not change current selection
    tree.setSelection( new TreeItem[]{
      treeItem1
    } );
    tree.setSelection( new TreeItem[]{
      null
    } );
    assertEquals( 1, tree.getSelectionCount() );
    assertSame( treeItem1, tree.getSelection()[ 0 ] );
    // Test calling setSelection(TreeItem[]) with only null-elements
    // -> must not change current selection
    tree.setSelection( new TreeItem[]{
      treeItem1
    } );
    tree.setSelection( new TreeItem[]{
      null, null
    } );
    assertEquals( 1, tree.getSelectionCount() );
    assertSame( treeItem1, tree.getSelection()[ 0 ] );
    // Select two treeItems, with some null-elements in between
    tree.setSelection( new TreeItem[ 0 ] );
    tree.setSelection( new TreeItem[]{
      null, treeItem1, null, treeItem2
    } );
    assertEquals( 2, tree.getSelectionCount() );
    expected = new TreeItem[]{
      treeItem1, treeItem2
    };
    assertTrue( Arrays.equals( expected, tree.getSelection() ) );
    // Verify that selecting a disposed of item throws an exception
    try {
      tree.setSelection( treeItem1 );
      TreeItem disposedItem = new TreeItem( tree, SWT.NONE );
      disposedItem.dispose();
      tree.setSelection( new TreeItem[]{
        treeItem1, disposedItem
      } );
      fail( "Must not allow to select diposed of tree item(s)." );
    } catch( IllegalArgumentException e ) {
      // ensure that the previously set selection is not changed
      assertSame( treeItem1, tree.getSelection()[ 0 ] );
    }
  }

  public void testSelectAllForSingle() {
    Display display = new Display();
    Composite shell = new Shell( display, SWT.NONE );
    Tree tree = new Tree( shell, SWT.SINGLE );
    TreeItem item1 = new TreeItem( tree, SWT.NONE );
    new TreeItem( item1, SWT.NONE );
    TreeItem item2 = new TreeItem( tree, SWT.NONE );
    tree.setSelection( item2 );
    tree.selectAll();
    assertSame( item2, tree.getSelection()[ 0 ] );
  }

  public void testSelectAllForMulti() {
    Display display = new Display();
    Composite shell = new Shell( display, SWT.NONE );
    Tree tree = new Tree( shell, SWT.MULTI );
    TreeItem item1 = new TreeItem( tree, SWT.NONE );
    TreeItem item11 = new TreeItem( item1, SWT.NONE );
    TreeItem item2 = new TreeItem( tree, SWT.NONE );
    tree.selectAll();
    assertTrue( contains( tree.getSelection(), item1 ) );
    assertTrue( contains( tree.getSelection(), item11 ) );
    assertTrue( contains( tree.getSelection(), item2 ) );
  }

  public void testDeselectAll() {
    Display display = new Display();
    Composite shell = new Shell( display, SWT.NONE );
    Tree tree = new Tree( shell, SWT.MULTI );
    TreeItem treeItem1 = new TreeItem( tree, SWT.NONE );
    TreeItem treeItem2 = new TreeItem( tree, SWT.NONE );
    tree.setSelection( new TreeItem[]{
      treeItem1, treeItem2
    } );
    tree.deselectAll();
    assertEquals( 0, tree.getSelectionCount() );
    assertEquals( 0, tree.getSelection().length );
  }

  public void testSelectForSingle() {
    Display display = new Display();
    Composite shell = new Shell( display, SWT.NONE );
    Tree tree = new Tree( shell, SWT.SINGLE );
    TreeItem item1 = new TreeItem( tree, SWT.NONE );
    new TreeItem( item1, SWT.NONE );
    TreeItem item2 = new TreeItem( tree, SWT.NONE );
    tree.setSelection( item2 );
    tree.select( item1 );
    assertTrue( contains( tree.getSelection(), item1 ) );
    assertFalse( contains( tree.getSelection(), item2 ) );
  }

  public void testSelectForMulti() {
    Display display = new Display();
    Composite shell = new Shell( display, SWT.NONE );
    Tree tree = new Tree( shell, SWT.MULTI );
    TreeItem item1 = new TreeItem( tree, SWT.NONE );
    TreeItem item11 = new TreeItem( item1, SWT.NONE );
    TreeItem item2 = new TreeItem( tree, SWT.NONE );
    tree.select( item1 );
    tree.select( item2 );
    assertTrue( contains( tree.getSelection(), item1 ) );
    assertFalse( contains( tree.getSelection(), item11 ) );
    assertTrue( contains( tree.getSelection(), item2 ) );
  }

  public void testDeselectForSingle() {
    Display display = new Display();
    Composite shell = new Shell( display, SWT.NONE );
    Tree tree = new Tree( shell, SWT.SINGLE );
    TreeItem item1 = new TreeItem( tree, SWT.NONE );
    new TreeItem( item1, SWT.NONE );
    TreeItem item2 = new TreeItem( tree, SWT.NONE );
    tree.select( item2 );
    assertTrue( contains( tree.getSelection(), item2 ) );
    tree.deselect( item2 );
    assertFalse( contains( tree.getSelection(), item2 ) );
  }

  public void testDeselectForMulti() {
    Display display = new Display();
    Composite shell = new Shell( display, SWT.NONE );
    Tree tree = new Tree( shell, SWT.MULTI );
    TreeItem item1 = new TreeItem( tree, SWT.NONE );
    TreeItem item11 = new TreeItem( item1, SWT.NONE );
    TreeItem item2 = new TreeItem( tree, SWT.NONE );
    tree.selectAll();
    tree.deselect( item11 );
    assertTrue( contains( tree.getSelection(), item1 ) );
    assertFalse( contains( tree.getSelection(), item11 ) );
    assertTrue( contains( tree.getSelection(), item2 ) );
  }

  public void testRemoveAll() {
    Display display = new Display();
    Composite shell = new Shell( display, SWT.NONE );
    Tree tree = new Tree( shell, SWT.MULTI );
    // Test removeAll on empty tree
    tree.removeAll();
    assertEquals( 0, tree.getItemCount() );
    assertEquals( 0, tree.getSelection().length );
    // Test removeAll on populated tree
    new TreeItem( tree, SWT.NONE );
    TreeItem treeItem = new TreeItem( tree, SWT.NONE );
    TreeItem subTreeItem = new TreeItem( treeItem, SWT.NONE );
    tree.setSelection( treeItem );
    tree.removeAll();
    assertEquals( 0, tree.getItemCount() );
    assertEquals( 0, tree.getSelection().length );
    assertEquals( true, treeItem.isDisposed() );
    assertEquals( true, subTreeItem.isDisposed() );
  }

  public void testInitialGetTopItemIndex() {
    Display display = new Display();
    Shell shell = new Shell( display, SWT.NONE );
    Tree tree = new Tree( shell, SWT.NONE );
    ITreeAdapter adapter = ( ITreeAdapter )tree.getAdapter( ITreeAdapter.class );
    assertEquals( 0, adapter.getTopItemIndex() );
    display.dispose();
  }
  
  public void testShowItemFlat() {
    Display display = new Display();
    Shell shell = new Shell( display, SWT.NONE );
    Tree tree = new Tree( shell, SWT.NONE );
    tree.setBounds( 0, 0, 200, 200 );
    for( int i = 0; i < 100; i++ ) {
      new TreeItem( tree, SWT.None );
    }
    ITreeAdapter adapter = ( ITreeAdapter )tree.getAdapter( ITreeAdapter.class );
    assertEquals( 0, adapter.getTopItemIndex() );
    tree.showItem( tree.getItem( 70 ) );
    assertEquals( 59, adapter.getTopItemIndex() );
    display.dispose();
  }

  public void testGetParentItem() {
    Display display = new Display();
    Shell shell = new Shell( display, SWT.NONE );
    Tree tree = new Tree( shell, SWT.SINGLE );
    assertNull( tree.getParentItem() );
  }

  protected void setUp() throws Exception {
    Fixture.setUp();
    Fixture.fakePhase( PhaseId.RENDER );
  }

  protected void tearDown() throws Exception {
    Fixture.tearDown();
  }

  public void testGetColumnCount() {
    Display display = new Display();
    Composite shell = new Shell( display, SWT.NONE );
    Tree tree = new Tree( shell, SWT.SINGLE );
    assertEquals( 0, tree.getColumnCount() );
    TreeColumn column0 = new TreeColumn( tree, SWT.NONE );
    assertEquals( 1, tree.getColumnCount() );
    TreeColumn column1 = new TreeColumn( tree, SWT.NONE );
    assertEquals( 2, tree.getColumnCount() );
    TreeColumn column2 = new TreeColumn( tree, SWT.NONE );
    assertEquals( 3, tree.getColumnCount() );
    column0.dispose();
    assertEquals( 2, tree.getColumnCount() );
    column1.dispose();
    assertEquals( 1, tree.getColumnCount() );
    column2.dispose();
    assertEquals( 0, tree.getColumnCount() );
  }

  public void testGetColumnI() {
    Display display = new Display();
    Composite shell = new Shell( display, SWT.NONE );
    Tree tree = new Tree( shell, SWT.SINGLE );
    try {
      tree.getColumn( 0 );
      fail( "No exception thrown for index out of range" );
    } catch( IllegalArgumentException e ) {
    }
    TreeColumn column0 = new TreeColumn( tree, SWT.LEFT );
    try {
      tree.getColumn( 1 );
      fail( "No exception thrown for index out of range" );
    } catch( IllegalArgumentException e ) {
    }
    assertEquals( column0, tree.getColumn( 0 ) );
    TreeColumn column1 = new TreeColumn( tree, SWT.LEFT );
    assertEquals( column1, tree.getColumn( 1 ) );
    column1.dispose();
    try {
      tree.getColumn( 1 );
      fail( "No exception thrown for index out of range" );
    } catch( IllegalArgumentException e ) {
    }
    column0.dispose();
    try {
      tree.getColumn( 0 );
      fail( "No exception thrown for index out of range" );
    } catch( IllegalArgumentException e ) {
    }
  }

  public void testGetColumns() {
    Display display = new Display();
    Composite shell = new Shell( display, SWT.NONE );
    Tree tree = new Tree( shell, SWT.SINGLE );
    assertEquals( 0, tree.getColumns().length );
    TreeColumn column0 = new TreeColumn( tree, SWT.LEFT );
    TreeColumn[] columns = tree.getColumns();
    assertEquals( 1, columns.length );
    assertEquals( column0, columns[ 0 ] );
    column0.dispose();
    assertEquals( 0, tree.getColumns().length );
    column0 = new TreeColumn( tree, SWT.LEFT );
    TreeColumn column1 = new TreeColumn( tree, SWT.RIGHT, 1 );
    columns = tree.getColumns();
    assertEquals( 2, columns.length );
    assertEquals( column0, columns[ 0 ] );
    assertEquals( column1, columns[ 1 ] );
    column0.dispose();
    columns = tree.getColumns();
    assertEquals( 1, columns.length );
    assertEquals( column1, columns[ 0 ] );
    column1.dispose();
    assertEquals( 0, tree.getColumns().length );
  }

  public void testSetHeaderVisible() {
    Display display = new Display();
    Composite shell = new Shell( display, SWT.NONE );
    Tree tree = new Tree( shell, SWT.SINGLE );
    assertFalse( tree.getHeaderVisible() );
    tree.setHeaderVisible( true );
    assertTrue( tree.getHeaderVisible() );
    tree.setHeaderVisible( false );
    assertFalse( tree.getHeaderVisible() );
  }

  public void testGetHeaderHeight() {
    Display display = new Display();
    Composite shell = new Shell( display, SWT.NONE );
    Tree tree = new Tree( shell, SWT.SINGLE );
    assertEquals( 0, tree.getHeaderHeight() );
    tree.setHeaderVisible( true );
    assertTrue( tree.getHeaderHeight() > 0 );
    tree.setHeaderVisible( false );
    assertEquals( 0, tree.getHeaderHeight() );
  }

  public void testSetSortColumn() {
    Display display = new Display();
    Composite shell = new Shell( display, SWT.NONE );
    Tree tree = new Tree( shell, SWT.SINGLE );
    TreeColumn column = new TreeColumn( tree, SWT.NONE );
    assertEquals( null, tree.getSortColumn() );
    tree.setSortColumn( column );
    assertEquals( column, tree.getSortColumn() );
    tree.setSortColumn( null );
    assertEquals( null, tree.getSortColumn() );
  }

  public void testSetSortDirection() {
    Display display = new Display();
    Composite shell = new Shell( display, SWT.NONE );
    Tree tree = new Tree( shell, SWT.SINGLE );
    assertEquals( SWT.NONE, tree.getSortDirection() );
    tree.setSortDirection( SWT.UP );
    assertEquals( SWT.UP, tree.getSortDirection() );
    tree.setSortDirection( SWT.DOWN );
    assertEquals( SWT.DOWN, tree.getSortDirection() );
  }

  public void testShowSelection() {
    Display display = new Display();
    Composite shell = new Shell( display, SWT.NONE );
    Tree tree = new Tree( shell, SWT.SINGLE );
    TreeItem item;
    tree.showSelection();
    item = new TreeItem( tree, 0 );
    tree.setSelection( new TreeItem[]{
      item
    } );
    tree.showSelection();
  }

  public void testResizeListener() {
    Fixture.fakePhase( PhaseId.PROCESS_ACTION );
    Display display = new Display();
    final Shell shell = new Shell( display );
    final Tree tree = new Tree( shell, SWT.VIRTUAL | SWT.BORDER );
    final List log = new ArrayList();
    tree.addControlListener( new ControlAdapter() {
      
      public void controlResized( ControlEvent event ) {
        log.add( event );
      }
    } );
    tree.setSize( 100, 160 );
    assertEquals( 1, log.size() );
  }

  public void testUpdateScrollBarOnColumnChange() {
    Display display = new Display();
    Shell shell = new Shell( display );
    Tree tree = new Tree( shell, SWT.NONE );
    tree.setSize( 20, 20 );
    assertFalse( tree.hasHScrollBar() );
    TreeColumn column = new TreeColumn( tree, SWT.LEFT );
    column.setWidth( 25 );
    assertTrue( tree.hasHScrollBar() );
    column.pack();
    assertFalse( tree.hasHScrollBar() );
    column.setWidth( 25 );
    assertTrue( tree.hasHScrollBar() );
    column.dispose();
    assertFalse( tree.hasHScrollBar() );
  }

  public void testUpdateScrollBarOnItemsChange() {
    Display display = new Display();
    Shell shell = new Shell( display );
    Tree tree = new Tree( shell, SWT.NONE );
    tree.setSize( 20, 20 );
    assertFalse( tree.hasVScrollBar() );
    for( int i = 0; i < 20; i++ ) {
      new TreeItem( tree, SWT.NONE );
    }
    assertTrue( tree.hasVScrollBar() );
    tree.removeAll();
    assertFalse( tree.hasVScrollBar() );
  }

  public void testUpdateScrollBarOnItemExpand() {
    Display display = new Display();
    Shell shell = new Shell( display );
    Tree tree = new Tree( shell, SWT.NONE );
    tree.setSize( 50, 20 );
    assertFalse( tree.hasVScrollBar() );
    TreeItem item = new TreeItem( tree, SWT.NONE );
    new TreeItem( item, SWT.NONE );
    assertFalse( tree.hasVScrollBar() );
    item.setExpanded( true );
    assertTrue( tree.hasVScrollBar() );
    item.setExpanded( false );
    assertFalse( tree.hasVScrollBar() );
  }

  public void testUpdateScrollBarOnResize() {
    Fixture.fakePhase( PhaseId.PROCESS_ACTION );
    Display display = new Display();
    Shell shell = new Shell( display );
    Tree tree = new Tree( shell, SWT.NONE );
    tree.setSize( 20, 20 );
    TreeColumn column = new TreeColumn( tree, SWT.LEFT );
    column.setWidth( 25 );
    assertTrue( tree.hasHScrollBar() );
    tree.setSize( 30, 30 );
    assertFalse( tree.hasHScrollBar() );
  }

  public void testUpdateScrollBarOnItemWidthChange() {
    Display display = new Display();
    Shell shell = new Shell( display );
    Tree tree = new Tree( shell, SWT.NONE );
    tree.setSize( 60, 60 );
    TreeItem item = new TreeItem( tree, SWT.NONE );
    assertFalse( tree.hasHScrollBar() );
    item.setText( "Very long long long long long long long long text" );
    assertTrue( tree.hasHScrollBar() );
    item.setText( "" );
    assertFalse( tree.hasHScrollBar() );
    Image image = Graphics.getImage( Fixture.IMAGE_100x50 );
    item.setImage( image );
    assertTrue( tree.hasHScrollBar() );
    item.setImage( ( Image )null );
    assertFalse( tree.hasHScrollBar() );
    item.setText( "Very long long long long long long long long text" );
    item.setImage( image );
    tree.clearAll( true );
    assertFalse( tree.hasHScrollBar() );
  }

  public void testUpdateScrollBarOnHeaderVisibleChange() {
    Fixture.fakePhase( PhaseId.PROCESS_ACTION );
    Display display = new Display();
    Shell shell = new Shell( display );
    Tree tree = new Tree( shell, SWT.NONE );
    int itemCount = 5;
    for( int i = 0; i < itemCount ; i++ ) {
      TreeItem item = new TreeItem( tree, SWT.NONE );
      item.setText( "Item" );
    }
    int itemHeight = 16;
    tree.setSize( 100, itemCount * itemHeight + 4 );
    assertFalse( tree.hasVScrollBar() );
    tree.setHeaderVisible( true );
    assertTrue( tree.hasVScrollBar() );
  }

  public void testUpdateScrollBarOnVirtualItemCountChange() {
    Fixture.fakePhase( PhaseId.PROCESS_ACTION );
    Display display = new Display();
    Shell shell = new Shell( display );
    Tree tree = new Tree( shell, SWT.VIRTUAL );
    int itemCount = 5;
    int itemHeight = 16;
    tree.setSize( 100, itemCount * itemHeight + 4 );
    tree.setItemCount( itemCount );
    assertFalse( tree.hasVScrollBar() );
    tree.setItemCount( itemCount * 2 );
    assertTrue( tree.hasVScrollBar() );
  }

  public void testUpdateScrollBarItemWidthChangeWithColumn() {
    Display display = new Display();
    Shell shell = new Shell( display );
    Tree tree = new Tree( shell, SWT.NONE );
    tree.setSize( 20, 20 );
    TreeColumn column = new TreeColumn( tree, SWT.LEFT );
    column.setWidth( 10 );
    TreeItem item = new TreeItem( tree, SWT.NONE );
    assertFalse( tree.hasHScrollBar() );
    item.setText( "Very long long long long long long long long text" );
    assertFalse( tree.hasHScrollBar() );
    Image image = Graphics.getImage( Fixture.IMAGE_100x50 );
    item.setImage( image );
    assertFalse( tree.hasHScrollBar() );
  }

  public void testUpdateScrollBarWithInterDependencyHFirst() {
    Fixture.fakePhase( PhaseId.PROCESS_ACTION );
    Display display = new Display();
    Shell shell = new Shell( display );
    Tree tree = new Tree( shell, SWT.NONE );
    TreeColumn column = new TreeColumn( tree, SWT.LEFT );
    column.setWidth( 20 );
    new TreeItem( tree, SWT.NONE );
    int itemHeight = 16;
    tree.setSize( 30, itemHeight + 4 );
    assertFalse( tree.needsVScrollBar() );
    assertFalse( tree.needsHScrollBar() );
    assertFalse( tree.hasHScrollBar() );
    assertFalse( tree.hasVScrollBar() );
    column.setWidth( 40 );
    assertTrue( tree.hasHScrollBar() );
    assertTrue( tree.hasVScrollBar() );
  }

  public void testUpdateScrollBarWithInterDependencyVFirst() {
    Fixture.fakePhase( PhaseId.PROCESS_ACTION );
    Display display = new Display();
    Shell shell = new Shell( display );
    Tree tree = new Tree( shell, SWT.NONE );
    TreeColumn column = new TreeColumn( tree, SWT.LEFT );
    column.setWidth( 26 );
    tree.setSize( 30, 30 );
    assertFalse( tree.hasHScrollBar() );
    assertFalse( tree.hasVScrollBar() );
    for( int i = 0; i < 10; i++ ) {
      new TreeItem( tree, SWT.NONE );
    }
    assertTrue( tree.hasHScrollBar() );
    assertTrue( tree.hasVScrollBar() );
  }
  
  public void testComputeSizeWithColumns() {
    Fixture.fakePhase( PhaseId.PROCESS_ACTION );
    Display display = new Display();
    Composite shell = new Shell( display, SWT.NONE );
    Tree tree = new Tree( shell, SWT.NONE );
    Point expected = new Point( 80, 80 );
    assertEquals( expected, tree.computeSize( SWT.DEFAULT, SWT.DEFAULT ) );
    for( int i = 0; i < 10; i++ ) {
      new TreeItem( tree, SWT.NONE ).setText( "Item " + i );
    }
    new TreeItem( tree, SWT.NONE ).setText( "Long long item 100" );
    expected = new Point( 121, 192 );
    assertEquals( expected, tree.computeSize( SWT.DEFAULT, SWT.DEFAULT ) );
    tree = new Tree( shell, SWT.BORDER );
    for( int i = 0; i < 10; i++ ) {
      new TreeItem( tree, SWT.NONE ).setText( "Item " + i );
    }
    new TreeItem( tree, SWT.NONE ).setText( "Long long item 100" );
    expected = new Point( 125, 196 );
    assertEquals( 2, tree.getBorderWidth() );
    assertEquals( expected, tree.computeSize( SWT.DEFAULT, SWT.DEFAULT ) );
    tree.setHeaderVisible( true );
    assertEquals( 15, tree.getHeaderHeight() );
    expected = new Point( 125, 211 );
    assertEquals( expected, tree.computeSize( SWT.DEFAULT, SWT.DEFAULT ) );
    TreeColumn col1 = new TreeColumn( tree, SWT.NONE );
    col1.setText( "Col 1" );
    TreeColumn col2 = new TreeColumn( tree, SWT.NONE );
    col2.setText( "Column 2" );
    TreeColumn col3 = new TreeColumn( tree, SWT.NONE );
    col3.setText( "Wider Column" );
    expected = new Point( 84, 211 );
    assertEquals( expected, tree.computeSize( SWT.DEFAULT, SWT.DEFAULT ) );
    col1.pack();
    col2.pack();
    col3.pack();
    expected = new Point( 238, 211 );
    assertEquals( expected, tree.computeSize( SWT.DEFAULT, SWT.DEFAULT ) );
    col1.setWidth( 10 );
    col2.setWidth( 10 );
    assertEquals( 67, col3.getWidth() );
    expected = new Point( 107, 211 );
    assertEquals( expected, tree.computeSize( SWT.DEFAULT, SWT.DEFAULT ) );
    tree = new Tree( shell, SWT.CHECK );
    for( int i = 0; i < 10; i++ ) {
      new TreeItem( tree, SWT.NONE ).setText( "Item " + i );
    }
    TreeItem item = new TreeItem( tree, SWT.NONE );
    item.setText( "Long long item 100" );
    TreeItem subitem = new TreeItem( item, SWT.NONE );
    subitem.setText( "Subitem 1" );
    expected = new Point( 136, 192 );
    assertEquals( expected, tree.computeSize( SWT.DEFAULT, SWT.DEFAULT ) );
    item.setExpanded( true );
    expected = new Point( 136, 208 );
    assertEquals( expected, tree.computeSize( SWT.DEFAULT, SWT.DEFAULT ) );
    expected = new Point( 316, 316 );
    assertEquals( expected, tree.computeSize( 300, 300 ) );
  }

  public void testComputeSizeWithIndention() {
    Fixture.fakePhase( PhaseId.PROCESS_ACTION );
    Display display = new Display();
    Composite shell = new Shell( display, SWT.NONE );
    Tree tree = new Tree( shell, SWT.NONE );
    Point expected = new Point( 80, 80 );
    assertEquals( expected, tree.computeSize( SWT.DEFAULT, SWT.DEFAULT ) );
    TreeItem item1 = new TreeItem( tree, SWT.NONE );
    TreeItem item2 = new TreeItem( item1, SWT.NONE );
    TreeItem item3 = new TreeItem( item2, SWT.NONE );
    TreeItem item4 = new TreeItem( item3, SWT.NONE );
    item1.setText( "Item 1" );
    item2.setText( "Item 2" );
    item3.setText( "Item 3" );
    item4.setText( "Item 4" );
    item1.setExpanded( true );
    item2.setExpanded( true );
    item3.setExpanded( true );
    expected = new Point( 106, 80 );
    assertEquals( expected, tree.computeSize( SWT.DEFAULT, SWT.DEFAULT ) );
  }
  
  public void testShowColumn() {
    Fixture.fakePhase( PhaseId.PROCESS_ACTION );
    Display display = new Display();
    Shell shell = new Shell( display );
    shell.setSize( 800, 600 );
    Tree tree = new Tree( shell, SWT.NONE );
    tree.setSize( 300, 100 );
    for( int i = 0; i < 10; i++ ) {
      TreeColumn column = new TreeColumn( tree, SWT.NONE );
      column.setWidth( 50 );
    }
    for( int i = 0; i < 10; i++ ) {
      new TreeItem( tree, SWT.NONE );
    }
    ITreeAdapter adapter
      = ( ITreeAdapter )tree.getAdapter( ITreeAdapter.class );
    assertEquals( 0, adapter.getScrollLeft() );
    tree.showColumn( tree.getColumn( 8 ) );
    assertEquals( 166, adapter.getScrollLeft() );
    tree.showColumn( tree.getColumn( 1 ) );
    assertEquals( 50, adapter.getScrollLeft() );
    tree.showColumn( tree.getColumn( 3 ) );
    assertEquals( 50, adapter.getScrollLeft() );
    try {
      tree.showColumn( null );
      fail( "Null argument not allowed" );
    } catch( IllegalArgumentException e ) {
    }
    TreeColumn column = tree.getColumn( 3 );
    column.dispose();
    try {
      tree.showColumn( column );
      fail( "Disposed column not allowed as argument" );
    } catch( IllegalArgumentException e ) {
    }
    Tree tree1 = new Tree( shell, SWT.NONE );
    column = new TreeColumn( tree1, SWT.NONE );
    tree.showColumn( column );
    assertEquals( 50, adapter.getScrollLeft() );
    tree.setColumnOrder( new int[] { 8, 7, 0, 1, 2, 3, 6, 5, 4 } );
    tree.showColumn( tree.getColumn( 8 ) );
    assertEquals( 0, adapter.getScrollLeft() );
    tree.showColumn( tree.getColumn( 5 ) );
    assertEquals( 116, adapter.getScrollLeft() );
  }  
  
  //////////
  // VIRTUAL

  public void testVirtualGetItemOutOfBounds() {
    Display display = new Display();
    Shell shell = new Shell( display );
    Tree tree = new Tree( shell, SWT.VIRTUAL );
    tree.setItemCount( 10 );
    try {
      tree.getItem( 10 );
      fail();
    } catch( IllegalArgumentException ex ) {
      // expected
    }
  }
  
  public void testVirtualInitalSetDataEvents() {
    Fixture.fakePhase( PhaseId.PROCESS_ACTION );
    Display display = new Display();
    final Shell shell = new Shell( display );
    final Tree tree = new Tree( shell, SWT.VIRTUAL | SWT.BORDER );
    final List log = new ArrayList();
    tree.addListener( SWT.SetData, new Listener() {
      public void handleEvent( final Event event ) {
        TreeItem item = ( TreeItem )event.item;
        String text = null;
        TreeItem parentItem = item.getParentItem();
        if( parentItem == null ) {
          text = "node " + tree.indexOf( item );
        } else {
          text = parentItem.getText() + " - " + parentItem.indexOf( item );
        }
        item.setText( text );
        item.setItemCount( 10 );
        log.add( item );
      }
    } );
    tree.setItemCount( 20 );
    assertEquals( 0, log.size() );
    // TODO [tb] : doesn't work if called before setItemCount. Use fakeRedraw?
    tree.setSize( 100, 160 ); 
    assertTrue( log.contains( tree.getItem( 0 ) ) );
    assertTrue( log.contains( tree.getItem( 1 ) ) );
    assertFalse( log.contains( tree.getItem( 19 ) ) );
  }
  
  public void testVirtualNoSetDataEventForCollapsedItems() {
    Fixture.fakePhase( PhaseId.PROCESS_ACTION );
    Display display = new Display();
    final Shell shell = new Shell( display );
    final Tree tree = new Tree( shell, SWT.VIRTUAL );
    final List log = new ArrayList();
    tree.addListener( SWT.SetData, new Listener() {
      public void handleEvent( final Event event ) {
        log.add( event );
      }
    } );
    tree.setItemCount( 1 );
    TreeItem item = tree.getItem( 0 );
    item.setItemCount( 10 );
    tree.setSize( 100, 160 ); 
    assertFalse( item.getItems()[ 0 ].isCached() );
    assertEquals( 1, log.size() );
  }
  
  public void testVirtualClear() {
    Display display = new Display();
    final Shell shell = new Shell( display );
    final Tree tree = new Tree( shell, SWT.VIRTUAL | SWT.BORDER );
    tree.setSize( 100, 160 );
    tree.addListener( SWT.SetData, new Listener() {
      public void handleEvent( final Event event ) {
        TreeItem item = ( TreeItem )event.item;
        String text = null;
        TreeItem parentItem = item.getParentItem();
        if( parentItem == null ) {
          text = "node " + tree.indexOf( item );
        } else {
          text = parentItem.getText() + " - " + parentItem.indexOf( item );
        }
        item.setText( text );
        item.setItemCount( 10 );
      }
    } );
    Fixture.fakePhase( PhaseId.PROCESS_ACTION );
    tree.setItemCount( 1 );
    Fixture.readDataAndProcessAction( tree );
    assertEquals( "node 0", tree.getItem( 0 ).getText() );
    tree.clearAll( true );
    assertEquals( "node 0", tree.getItem( 0 ).getText() );
    tree.clear( 0, false );
    assertEquals( "node 0", tree.getItem( 0 ).getText() );
    TreeItem root = tree.getItem( 0 );
    root.clear( 0, false );
    assertEquals( "node 0 - 0", root.getItem( 0 ).getText() );
    root.clearAll( true );
    assertEquals( "node 0 - 0", root.getItem( 0 ).getText() );
    assertEquals( "node 0 - 1", root.getItem( 1 ).getText() );
  }
  
  public void testVirtualComputeSize() {
    Fixture.fakePhase( PhaseId.PROCESS_ACTION );
    Display display = new Display();
    Composite shell = new Shell( display, SWT.NONE );
    Tree tree = new Tree( shell, SWT.BORDER | SWT.VIRTUAL );
    tree.setItemCount( 10 );
    tree.addListener( SWT.SetData, new Listener() {

      public void handleEvent( final Event event ) {
        TreeItem item = ( TreeItem )event.item;
        int treeIndex = item.getParent().indexOf( item );
        item.setText( "Item " + treeIndex );
      }
    } );
    // DEFAULT_WIDTH + scrollbar (16) + 2 * border (2)
    Point expected = new Point( 84, 180 );
    assertEquals( expected, tree.computeSize( SWT.DEFAULT, SWT.DEFAULT ) );
    tree.setHeaderVisible( true );
    assertEquals( 15, tree.getHeaderHeight() );
    expected = new Point( 84, 195 );
    assertEquals( expected, tree.computeSize( SWT.DEFAULT, SWT.DEFAULT ) );
    TreeColumn col1 = new TreeColumn( tree, SWT.NONE );
    col1.setText( "Col 1" );
    TreeColumn col2 = new TreeColumn( tree, SWT.NONE );
    col2.setText( "Column 2" );
    TreeColumn col3 = new TreeColumn( tree, SWT.NONE );
    col3.setText( "Wider Column" );
    expected = new Point( 84, 195 );
    assertEquals( expected, tree.computeSize( SWT.DEFAULT, SWT.DEFAULT ) );
    col1.pack();
    col2.pack();
    col3.pack();
    expected = new Point( 163, 195 );
    assertEquals( expected, tree.computeSize( SWT.DEFAULT, SWT.DEFAULT ) );
    col1.setWidth( 10 );
    col2.setWidth( 10 );
    assertEquals( 67, col3.getWidth() );
    expected = new Point( 107, 195 );
    assertEquals( expected, tree.computeSize( SWT.DEFAULT, SWT.DEFAULT ) );
    expected = new Point( 320, 320 );
    assertEquals( expected, tree.computeSize( 300, 300 ) );
  }
  
  public void testVirtualScrollThrowsSetData() {
    Fixture.fakePhase( PhaseId.PROCESS_ACTION );
    final LoggingListener log = new LoggingListener();
    final Display display = new Display();
    Shell shell = new Shell( display );
    Tree tree = new Tree( shell, SWT.VIRTUAL );
    tree.setItemCount( 100 );
    tree.setSize( 100, 100 );
    tree.addListener( SWT.SetData, log );
    log.clear();
    ITreeAdapter adapter = ( ITreeAdapter )tree.getAdapter( ITreeAdapter.class );
    adapter.setTopItemIndex( 30 );
    assertSame( tree.getItem( 30 ), log.get( 0 ).item );
    assertSame( tree.getItem( 31 ), log.get( 1 ).item );
  }
  
  public void testVirtualShowItemThrowsSetData() {
    Fixture.fakePhase( PhaseId.PROCESS_ACTION );
    final LoggingListener log = new LoggingListener();
    final Display display = new Display();
    Shell shell = new Shell( display );
    Tree tree = new Tree( shell, SWT.VIRTUAL );
    tree.setItemCount( 100 );
    tree.setSize( 100, 100 );
    tree.addListener( SWT.SetData, log );
    log.clear();
    tree.showItem( tree.getItem( 30 ) );
    assertTrue( log.getItems().contains( tree.getItem( 30 ) ) );
  }

  
  /////////
  // HELPER

  private static boolean contains( final TreeItem[] items, final TreeItem item )
  {
    boolean result = false;
    for( int i = 0; !result && i < items.length; i++ ) {
      if( item == items[ i ] ) {
        result = true;
      }
    }
    return result;
  }
}
