/*******************************************************************************
 * Copyright (c) 2002, 2009 Innoopract Informationssysteme GmbH.
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

import junit.framework.TestCase;

import org.eclipse.rwt.Fixture;
import org.eclipse.rwt.graphics.Graphics;
import org.eclipse.rwt.internal.lifecycle.RWTLifeCycle;
import org.eclipse.rwt.lifecycle.PhaseId;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.widgets.ITableAdapter;
import org.eclipse.swt.internal.widgets.ItemHolder;
import org.eclipse.swt.layout.FillLayout;


public class Table_Test extends TestCase {

  protected void setUp() throws Exception {
    Fixture.setUp();
  }

  protected void tearDown() throws Exception {
    Fixture.tearDown();
  }

  public void testInitialValues() {
    Display display = new Display();
    Shell shell = new Shell( display, SWT.NONE );
    Table table = new Table( shell, SWT.NONE );

    assertEquals( false, table.getHeaderVisible() );
    assertEquals( false, table.getLinesVisible() );
    assertEquals( 0, table.getSelectionCount() );
    assertEquals( 0, table.getSelectionIndices().length );
    assertEquals( 0, table.getSelection().length );
    assertEquals( 0, table.getTopIndex() );
    assertNull( table.getSortColumn() );
    assertEquals( SWT.NONE, table.getSortDirection() );
  }

  public void testStyle() {
    Display display = new Display();
    Shell shell = new Shell( display, SWT.NONE );
    Table table = new Table( shell, SWT.NONE );
    assertTrue( ( table.getStyle() & SWT.H_SCROLL ) != 0 );
    assertTrue( ( table.getStyle() & SWT.V_SCROLL ) != 0 );
    assertTrue( ( table.getStyle() & SWT.SINGLE ) != 0 );

    table = new Table( shell, SWT.NO_SCROLL );
    assertTrue( ( table.getStyle() & SWT.NO_SCROLL ) != 0 );
    assertTrue( ( table.getStyle() & SWT.V_SCROLL ) == 0 );
    assertTrue( ( table.getStyle() & SWT.H_SCROLL ) == 0 );

    table = new Table( shell, SWT.SINGLE | SWT.MULTI );
    assertTrue( ( table.getStyle() & SWT.SINGLE ) != 0 );

    table = new Table( shell, SWT.SINGLE | SWT.SINGLE );
    assertTrue( ( table.getStyle() & SWT.SINGLE ) != 0 );

    table = new Table( shell, SWT.SINGLE | SWT.MULTI );
    assertTrue( ( table.getStyle() & SWT.SINGLE ) != 0 );
  }

  public void testTableCreation() {
    Display display = new Display();
    Shell shell = new Shell( display, SWT.NONE );
    Table table = new Table( shell, SWT.NONE );
    assertEquals( 0, table.getItemCount() );
    assertEquals( 0, table.getItems().length );
    TableItem item0 = new TableItem( table, SWT.NONE );
    assertEquals( 1, table.getItemCount() );
    assertEquals( 1, table.getItems().length );
    assertEquals( item0, table.getItem( 0 ) );
    assertEquals( item0, table.getItems()[ 0 ] );
    try {
      table.getItem( 4 );
      fail( "Index out of bounds" );
    } catch( final IllegalArgumentException iae ) {
      // expected
    }
    assertSame( display, item0.getDisplay() );
    item0.dispose();
    assertEquals( 0, table.getItemCount() );
    assertEquals( 0, table.getItems().length );
    item0 = new TableItem( table, SWT.NONE );
    assertEquals( 1, table.getItemCount() );
    assertEquals( 0, table.getColumnCount() );
    assertEquals( 0, table.getColumns().length );
    TableColumn column0 = new TableColumn( table, SWT.NONE );
    assertEquals( 1, table.getColumnCount() );
    assertEquals( 1, table.getColumns().length );
    assertEquals( column0, table.getColumn( 0 ) );
    assertEquals( column0, table.getColumns()[ 0 ] );
    try {
      table.getColumn( 4 );
      fail( "Index out of bounds" );
    } catch( final IllegalArgumentException iae ) {
      // expected
    }
    assertSame( display, column0.getDisplay() );
    column0.dispose();
    assertEquals( 0, table.getColumnCount() );
    assertEquals( 0, table.getColumns().length );

    // search operation indexOf
    column0 = new TableColumn( table, SWT.NONE );
    TableColumn tableColumn1 = new TableColumn( table, SWT.NONE );
    assertEquals( 1, table.indexOf( tableColumn1 ) );
    TableItem tableItem1 = new TableItem( table, SWT.NONE );
    assertEquals( 1, table.indexOf( tableItem1 ) );

    // column width property
    assertEquals( 0, column0.getWidth() );
    column0.setWidth( 100 );
    assertEquals( 100, column0.getWidth() );
  }

  public void testHeaderHeight() {
    Display display = new Display();
    Shell shell = new Shell( display , SWT.NONE );
    Table table = new Table( shell, SWT.NONE );
    new TableColumn( table, SWT.NONE );
    assertEquals( 0, table.getHeaderHeight() );
    table.setHeaderVisible( true );
    assertTrue( table.getHeaderHeight() > 0 );
  }

  public void testTableItemTexts() {
    Display display = new Display();
    Shell shell = new Shell( display , SWT.NONE );
    Table table = new Table( shell, SWT.NONE );
    TableItem item = new TableItem( table, SWT.NONE );
    String text0 = "text0";
    String text1 = "text1";
    String text2 = "text2";
    String text3 = "text3";
    String text4 = "text4";
    String text5 = "text5";

    // test text for first column, the same as setText(String)
    item.setText( text0 );
    assertEquals( text0, item.getText() );
    assertEquals( text0, item.getText( 0 ) );
    item.setText( 0, text1 );
    assertEquals( text1, item.getText() );
    assertEquals( text1, item.getText( 0 ) );
    try {
      item.setText( 0, null );
      fail( "Parameter index must not be null." );
    } catch( final IllegalArgumentException iae ) {
      // expected
    }

    // test text setting if no table column exists
    item.setText( 1, text1 );
    assertEquals( "", item.getText( 1 ) );
    item.setText( 2, text1 );
    assertEquals( "", item.getText( 2 ) );
    // test text setting if table column exists
    TableColumn column0 = new TableColumn( table, SWT.NONE );
    new TableColumn( table, SWT.NONE );
    TableColumn column2 = new TableColumn( table, SWT.NONE );
    item.setText( 2, text2 );
    assertEquals( text2, item.getText( 2 ) );

    // test text retrievment after last column was disposed
    column2.dispose();
    assertSame( "", item.getText( 2 ) );
    column2 = new TableColumn( table, SWT.NONE );
    assertSame( "", item.getText( 2 ) );
    new TableColumn( table, SWT.NONE );
    item.setText( 3, text3 );
    assertSame( text3, item.getText( 3 ) );
    new TableColumn( table, SWT.NONE );
    assertSame( "", item.getText( 4 ) );
    String[] texts = new String[]{
      text0, text1, text2, text3, text4, text5
    };

    // test setting multiple texts at once
    for( int i = 0; i < texts.length; i++ ) {
      item.setText( i, texts[ i ] );
    }
    assertEquals( text0, item.getText( 0 ) );
    assertEquals( text1, item.getText( 1 ) );
    assertEquals( text2, item.getText( 2 ) );
    assertEquals( text3, item.getText( 3 ) );
    assertEquals( text4, item.getText( 4 ) );
    assertEquals( "", item.getText( 5 ) );

    // test disposal of column that is not the last one
    column2.dispose();
    assertEquals( text0, item.getText( 0 ) );
    assertEquals( text1, item.getText( 1 ) );
    assertEquals( text3, item.getText( 2 ) );
    assertEquals( text4, item.getText( 3 ) );
    assertEquals( "", item.getText( 4 ) );
    column0.dispose();
    assertEquals( text1, item.getText( 0 ) );
    assertEquals( text3, item.getText( 1 ) );
    assertEquals( text4, item.getText( 2 ) );
    assertEquals( "", item.getText( 3 ) );
  }

  public void testTopIndex() {
    Display display = new Display();
    Shell shell = new Shell( display , SWT.NONE );
    Table table = new Table( shell, SWT.NONE );
    new TableItem( table, SWT.NONE );
    TableItem lastItem = new TableItem( table, SWT.NONE );

    // Set a value which is out of bounds
    int previousTopIndex = table.getTopIndex();
    table.setTopIndex( 10000 );
    assertEquals( previousTopIndex, table.getTopIndex() );

    // Set topIndex to the second item
    table.setTopIndex( 1 );
    assertEquals( 1, table.getTopIndex() );

    // Remove last item (whose index equals topIndex) -> must adjust topIndex
    table.setTopIndex( table.indexOf( lastItem ) );
    lastItem.dispose();
    assertEquals( 0, table.getTopIndex() );

    // Ensure that topIndex stays at least 0 even if all items are removed
    table.removeAll();
    TableItem soleItem = new TableItem( table, SWT.NONE );
    soleItem.dispose();
    assertEquals( 0, table.getTopIndex() );

    // Ensure that topIndex stays 0 if no vertical scrollbar needed.
    table.setSize( 100, 100 );
    for( int i = 0; i < 10; i++ ) {
      new TableItem( table, SWT.NONE );
    }
    assertEquals( 0, table.getTopIndex() );
    table.setTopIndex( 9 );
    assertEquals( 9, table.getTopIndex() );
    for( int i = 3; i < 10; i++ ) {
      table.getItem( 3 ).dispose();
    }
    assertEquals( 0, table.getTopIndex() );

    // Ensure that topIndex is adjusted if it is bigger than item count
    table.removeAll();
    for( int i = 0; i < 20; i++ ) {
      new TableItem( table, SWT.NONE );
    }
    table.setTopIndex( 14 );
    for( int i = 10; i < 20; i++ ) {
      table.getItem( 10 ).dispose();
    }
    int itemCount = table.getItemCount();
    int visibleItemCount = table.getVisibleItemCount( false );
    assertEquals( itemCount - visibleItemCount - 1, table.getTopIndex() );
  }

  public void testDispose() {
    Display display = new Display();
    Shell shell = new Shell( display );
    Table table = new Table( shell, SWT.SINGLE );
    TableColumn column = new TableColumn( table, SWT.NONE );
    TableItem item = new TableItem( table, SWT.NONE );
    table.dispose();
    assertTrue( table.isDisposed() );
    assertTrue( column.isDisposed() );
    assertTrue( item.isDisposed() );
  }

  public void testDisposeSingleSelectedItem() {
    Display display = new Display();
    Shell shell = new Shell( display );
    Table table = new Table( shell, SWT.SINGLE );
    new TableColumn( table, SWT.NONE );
    TableItem item = new TableItem( table, SWT.NONE );

    table.setSelection( new TableItem[] { item } );
    item.dispose();
    assertEquals( -1, table.getSelectionIndex() );
    assertEquals( 0, table.getItemCount() );
    assertEquals( 0, table.getSelectionCount() );
    assertEquals( 0, table.getSelection().length );
  }

  public void testDisposeMultiSelectedItem() {
    Display display = new Display();
    Shell shell = new Shell( display );
    Table table = new Table( shell, SWT.MULTI );
    new TableColumn( table, SWT.NONE );
    TableItem item0 = new TableItem( table, SWT.NONE );
    TableItem item1 = new TableItem( table, SWT.NONE );
    new TableItem( table, SWT.NONE );

    table.setSelection( new TableItem[] { item0, item1 } );
    item0.dispose();
    assertEquals( table.indexOf( item1 ), table.getSelectionIndex() );
    assertEquals( 1, table.getSelectionIndices().length );
    assertEquals( 0, table.getSelectionIndices()[ 0 ] );
    assertEquals( 2, table.getItemCount() );
    assertEquals( 1, table.getSelectionCount() );

    table.removeAll();
    item0 = new TableItem( table, SWT.NONE );
    item1 = new TableItem( table, SWT.NONE );
    TableItem item2 = new TableItem( table, SWT.NONE );
    table.selectAll();
    table.remove( 1 );
    assertEquals( 2, table.getSelectionIndices().length );
    assertTrue( find( table.indexOf( item0 ), table.getSelectionIndices() ) );
    assertTrue( find( table.indexOf( item2 ), table.getSelectionIndices() ) );
  }

  public void testDisposeInSetData() {
    Fixture.fakePhase( PhaseId.PROCESS_ACTION );
    Display display = new Display();
    Shell shell = new Shell( display );
    Table table = new Table( shell, SWT.VIRTUAL );
    table.setItemCount( 100 );
    table.addListener( SWT.SetData, new Listener() {
      public void handleEvent( final Event event ) {
        event.item.dispose();
      }
    } );
    try {
      // if dispose() is called while processing a SetData event
      // and the event was caused by a call to one of the getXXX methods
      // of TableItem an SWT.ERROR_WIDGET_DISPOSED is thrown
      table.getItem( 40 ).getTextBounds( 0 );
      fail( "disposing while in SetData must not be allowd" );
    } catch( SWTException e ) {
      assertEquals( SWT.ERROR_WIDGET_DISPOSED, e.code );
    }
  }

  public void testReduceSetItemCountWithSelection() {
    // Create a table that is populated with setItemCount with all selected
    Fixture.fakePhase( PhaseId.PROCESS_ACTION );
    Display display = new Display();
    Shell shell = new Shell( display );
    shell.setLayout( new FillLayout() );
    Table table = new Table( shell, SWT.MULTI );
    new TableColumn( table, SWT.NONE );
    shell.layout();
    shell.open();
    table.setItemCount( 4 );
    table.setSelection( 0, table.getItemCount() - 1 );
    // Name items to ease debugging
    for( int i = 0; i < table.getItemCount(); i++ ) {
      table.getItem( i ).setText( "Item " + i );
    }
    // reduce the number of items by half
    table.setItemCount( table.getItemCount() / 2 );
    // Ensure that the selection contains all the remaining items and all
    // indices returned are valid
    int[] selectionIndices = table.getSelectionIndices();
    for( int i = 0; i < selectionIndices.length; i++ ) {
      assertTrue( selectionIndices[ i ] >= 0 );
      assertTrue( selectionIndices[ i ] < table.getItemCount() );
    }
    assertEquals( table.getItemCount(), selectionIndices.length );
  }

  public void testReduceSetItemCountWithSelectionVirtual() {
    // Create a table that is populated with setItemCount with all selected
    Fixture.fakePhase( PhaseId.PROCESS_ACTION );
    Display display = new Display();
    Shell shell = new Shell( display );
    shell.setSize( 800, 800 );
    shell.setLayout( new FillLayout() );
    Table table = new Table( shell, SWT.MULTI | SWT.VIRTUAL );
    new TableColumn( table, SWT.NONE );
    table.addListener( SWT.SetData, new Listener() {
      public void handleEvent( final Event event ) {
        Item item = ( Item )event.item;
        item.setText( "Item " + event.index );
      }
    } );
    shell.layout();
    shell.open();
    table.setItemCount( 4 );
    table.setSelection( 0, table.getItemCount() - 1 );
    // reduce the number of items by half
    table.setItemCount( table.getItemCount() / 2 );
    // Ensure that the selection contains all the remaining items and all
    // indices returned are valid
    int[] selectionIndices = table.getSelectionIndices();
    for( int i = 0; i < selectionIndices.length; i++ ) {
      assertTrue( selectionIndices[ i ] >= 0 );
      assertTrue( selectionIndices[ i ] < table.getItemCount() );
    }
    assertEquals( table.getItemCount(), selectionIndices.length );
  }

  public void testFocusIndex() {
    Display display = new Display();
    Shell shell = new Shell( display );
    Table table = new Table( shell, SWT.NONE );
    new TableColumn( table, SWT.NONE );
    TableItem item0 = new TableItem( table, SWT.NONE );
    TableItem item1 = new TableItem( table, SWT.NONE );
    TableItem item2 = new TableItem( table, SWT.NONE );
    Object adapter = table.getAdapter( ITableAdapter.class );
    ITableAdapter tableAdapter = ( ITableAdapter )adapter;

    // Test initial value
    assertEquals( -1, tableAdapter.getFocusIndex() );

    // setSelection changes the focusIndex to the selected item
    table.setSelection( item0 );
    assertEquals( 0, tableAdapter.getFocusIndex() );

    table.setSelection( item1 );
    table.setSelection( new TableItem[] { item0 } );
    assertEquals( 0, tableAdapter.getFocusIndex() );

    // calling setSelection with an out-of-range value clears the selection but
    // does not affect the focusIndex
    table.setSelection( 0 );
    table.setSelection( -2 );
    assertEquals( 0, tableAdapter.getFocusIndex() );
    table.setSelection( 0 );
    table.setSelection( table.getItemCount() + 10 );
    assertEquals( 0, tableAdapter.getFocusIndex() );

    // Resetting the selection does not affect the focusIndex
    table.setSelection( item0 );
    table.deselectAll();
    assertEquals( 0, table.getSelectionCount() );
    assertEquals( 0, tableAdapter.getFocusIndex() );

    // Ensure that select does not change the focusIndex
    table.setSelection( item0 );
    table.select( table.indexOf( item1 ) );
    assertEquals( 0, tableAdapter.getFocusIndex() );

    // Disposing of the focused item also resets the focusIndex
    table.setSelection( item0 );
    item0.dispose();
    assertEquals( -1, tableAdapter.getFocusIndex() );

    // Disposing of the focused but un-selected item moves focus to the
    // selected item
    table.setSelection( item1 );
    table.select( table.indexOf( item2 ) );
    item1.dispose();
    assertEquals( table.indexOf( item2 ), tableAdapter.getFocusIndex() );

    // Insert an item before the focused one an verify that focus moves on
    table.removeAll();
    new TableItem( table, SWT.NONE );
    table.setSelection( 0 );
    assertEquals( 0, tableAdapter.getFocusIndex() ); // ensure precondition
    new TableItem( table, SWT.NONE, 0 );
    assertEquals( 1, table.getSelectionIndex() );
    assertEquals( 1, tableAdapter.getFocusIndex() );
  }

  public void testFocusIndexVirtual() {
    Fixture.fakePhase( PhaseId.PROCESS_ACTION );
    Display display = new Display();
    Shell shell = new Shell( display );
    Table table = new Table( shell, SWT.VIRTUAL );
    table.setSize( 500, 500 );
    Object adapter = table.getAdapter( ITableAdapter.class );
    ITableAdapter tableAdapter = ( ITableAdapter )adapter;

    table.setItemCount( 100 );
    table.setSelection( 99 );
    table.setSize( 501, 501 );
    table.setItemCount( 98 );
    assertEquals( 0, table.getSelectionCount() );
    assertEquals( -1, tableAdapter.getFocusIndex() );
  }

  public void testRemoveAll() {
    Display display = new Display();
    Shell shell = new Shell( display );
    Table table = new Table( shell, SWT.NONE );
    new TableColumn( table, SWT.NONE );
    TableItem preDisposedItem = new TableItem( table, SWT.NONE );
    TableItem item0 = new TableItem( table, SWT.NONE );
    TableItem item1 = new TableItem( table, SWT.NONE );

    preDisposedItem.dispose();
    table.setSelection( 1 );
    table.setTopIndex( 1 );
    table.removeAll();
    assertEquals( -1, table.getSelectionIndex() );
    assertEquals( 0, table.getItemCount() );
    assertEquals( 0, table.getTopIndex() );
    assertTrue( item0.isDisposed() );
    assertTrue( item1.isDisposed() );
  }

  public void testRemoveAllVirtual() {
    Fixture.fakePhase( PhaseId.PROCESS_ACTION );
    Display display = new Display();
    Shell shell = new Shell( display );
    shell.setSize( 100, 100 );
    shell.setLayout( new FillLayout() );
    Table table = new Table( shell, SWT.MULTI | SWT.VIRTUAL );
    new TableColumn( table, SWT.NONE );
    table.addListener( SWT.SetData, new Listener() {
      public void handleEvent( final Event event ) {
        Item item = ( Item )event.item;
        item.setText( "Item " + event.index );
      }
    } );
    shell.layout();
    shell.open();
    table.setItemCount( 10 );
    table.removeAll();
    assertEquals( 0, table.getItemCount() );
  }

  public void testRemoveRange() {
    Display display = new Display();
    Shell shell = new Shell( display );
    Table table = new Table( shell, SWT.NONE );
    int number = 5;
    TableItem[] items = new TableItem[ number ];
    for( int i = 0; i < number; i++ ) {
      items[ i ] = new TableItem( table, 0 );
    }
    try {
      table.remove( -number, number + 100 );
      fail( "No exception thrown for illegal index range" );
    } catch( IllegalArgumentException e ) {
    }
    table = new Table( shell, SWT.NONE );
    items = new TableItem[ number ];
    for( int i = 0; i < number; i++ ) {
      items[ i ] = new TableItem( table, 0 );
    }
    table.remove( 2, 3 );
    assertTrue( items[ 2 ].isDisposed() );
    assertTrue( items[ 3 ].isDisposed() );
    assertEquals( table.getItemCount(), 3 );
  }

  public void testRemoveRangeVirtual() {
    Fixture.fakePhase( PhaseId.PROCESS_ACTION );
    Display display = new Display();
    Shell shell = new Shell( display );
    shell.setSize( 100, 100 );
    shell.setLayout( new FillLayout() );
    Table table = new Table( shell, SWT.MULTI | SWT.VIRTUAL );
    new TableColumn( table, SWT.NONE );
    table.addListener( SWT.SetData, new Listener() {
      public void handleEvent( final Event event ) {
        Item item = ( Item )event.item;
        item.setText( "Item " + event.index );
      }
    } );
    shell.layout();
    shell.open();
    table.setItemCount( 10 );
    table.remove( 0, 9 );
    assertEquals( 0, table.getItemCount() );
  }

  public void testRemove() {
    Display display = new Display();
    Shell shell = new Shell( display );
    Table table = new Table( shell, SWT.NONE );
    int number = 5;
    TableItem[] items = new TableItem[ number ];
    for( int i = 0; i < number; i++ ) {
      items[ i ] = new TableItem( table, 0 );
    }
    table.remove( 1 );
    assertTrue( items[ 1 ].isDisposed() );
    assertEquals( table.getItemCount(), 4 );
  }

  public void testRemoveVirtual() {
    Fixture.fakePhase( PhaseId.PROCESS_ACTION );
    Display display = new Display();
    Shell shell = new Shell( display );
    Table table = new Table( shell, SWT.MULTI | SWT.VIRTUAL );
    table.setSize( 100, 100 );
    table.setItemCount( 10 );
    table.remove( 0 );
    assertEquals( 9, table.getItemCount() );
    TableItem item5 = table.getItem( 5 );
    table.remove( 2 );
    assertSame( item5, table.getItem( 4 ) );
    assertEquals( 8, table.getItemCount() );
  }

  public void testRemoveArrayVirtual() {
    Fixture.fakePhase( PhaseId.PROCESS_ACTION );
    Display display = new Display();
    Shell shell = new Shell( display );
    shell.setSize( 100, 100 );
    shell.setLayout( new FillLayout() );
    Table table = new Table( shell, SWT.MULTI | SWT.VIRTUAL );
    new TableColumn( table, SWT.NONE );
    table.addListener( SWT.SetData, new Listener() {
      public void handleEvent( final Event event ) {
        Item item = ( Item )event.item;
        item.setText( "Item " + event.index );
      }
    } );
    shell.layout();
    shell.open();
    table.setItemCount( 10 );
    table.remove( new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 } );
    assertEquals( 0, table.getItemCount() );
  }

  public void testRemoveWithSelectionListener() {
    // ensure that no selection event is fired if a selected item is removed
    final boolean eventFired[] = { false };
    Display display = new Display();
    Shell shell = new Shell( display );
    Table table = new Table( shell, SWT.NONE );
    for( int i = 0; i < 5; i++ ) {
      new TableItem( table, SWT.NONE );
    }
    table.addSelectionListener( new SelectionAdapter() {
      public void widgetSelected( final SelectionEvent event ) {
        eventFired[ 0 ] = true;
      }
    } );
    table.select( 1 );
    table.remove( 1 );
    assertFalse( eventFired[ 0 ] );
  }

  public void testRemoveArray() {
    Display display = new Display();
    Shell shell = new Shell( display );
    Table table = new Table( shell, SWT.NONE );
    int number = 15;
    TableItem[] items = new TableItem[ number ];
    for( int i = 0; i < number; i++ ) {
      items[ i ] = new TableItem( table, 0 );
    }
    try {
      table.remove( null );
      fail( "No exception thrown for tableItems == null" );
    } catch( IllegalArgumentException iae ) {
    	// expected
    }
    try {
      table.remove( new int[]{
        2, 1, 0, -100, 5, 5, 2, 1, 0, 0, 0
      } );
      fail( "No exception thrown for illegal index arguments" );
    } catch( IllegalArgumentException e ) {
    }
    try {
      table.remove( new int[]{
        2, 1, 0, number, 5, 5, 2, 1, 0, 0, 0
      } );
      fail( "No exception thrown for illegal index arguments" );
    } catch( IllegalArgumentException e ) {
    }
    table.remove( new int[]{} );
    table = new Table( shell, SWT.NONE );
    for( int i = 0; i < number; i++ ) {
      items[ i ] = new TableItem( table, 0 );
    }
    assertTrue( ":a:", !items[ 2 ].isDisposed() );
    table.remove( new int[]{ 2 } );
    assertTrue( ":b:", items[ 2 ].isDisposed() );
    assertEquals( number - 1, table.getItemCount() );
    assertTrue( ":c:", !items[ number - 1 ].isDisposed() );
    table.remove( new int[]{ number - 2 } );
    assertTrue( ":d:", items[ number - 1 ].isDisposed() );
    assertEquals( number - 2, table.getItemCount() );
    assertTrue( ":e:", !items[ 3 ].isDisposed() );
    table.remove( new int[]{ 2 } );
    assertTrue( ":f:", items[ 3 ].isDisposed() );
    assertEquals( number - 3, table.getItemCount() );
    assertTrue( ":g:", !items[ 0 ].isDisposed() );
    table.remove( new int[]{ 0 } );
    assertTrue( ":h:", items[ 0 ].isDisposed() );
    assertEquals( number - 4, table.getItemCount() );
  }

  public void testSingleSelection() {
    Display display = new Display();
    Shell shell = new Shell( display );
    Table table = new Table( shell, SWT.SINGLE );
    TableItem item1 = new TableItem( table, SWT.NONE );
    new TableItem( table, SWT.NONE );
    new TableItem( table, SWT.NONE );

    // Test setSelection(int)
    table.deselectAll();
    table.setSelection( 0 );
    assertEquals( true, table.isSelected( 0 ) );

    table.setSelection( table.getItemCount() + 20 );
    assertEquals( 0, table.getSelectionCount() );

    // Test setSelection(int,int)
    table.deselectAll();
    table.setSelection( 0, 0 );
    assertEquals( 1, table.getSelectionCount() );
    assertEquals( true, table.isSelected( 0 ) );

    table.deselectAll();
    table.setSelection( 0 );
    table.setSelection( 0, 2 );
    assertEquals( 0, table.getSelectionCount() );

    // Test setSelection(int[])
    table.deselectAll();
    table.setSelection( new int[]{ 0 } );
    assertEquals( 1, table.getSelectionCount() );
    assertEquals( true, table.isSelected( 0 ) );

    table.deselectAll();
    table.setSelection( 2 );
    table.setSelection( new int[]{ 0, 1 } );
    assertEquals( 0, table.getSelectionCount() );

    table.deselectAll();
    table.setSelection( 2 );
    table.setSelection( new int[]{ 777 } );
    assertEquals( 0, table.getSelectionCount() );

    // Test setSelection(TableItem)
    table.deselectAll();
    table.setSelection( item1 );
    assertEquals( 1, table.getSelectionCount() );
    assertEquals( item1, table.getSelection()[ 0 ] );

    try {
      table.deselectAll();
      table.setSelection( item1 );
      table.setSelection( ( TableItem )null );
    } catch( IllegalArgumentException e ) {
      // expected
      assertEquals( 1, table.getSelectionCount() );
      assertEquals( item1, table.getSelection()[ 0 ] );
    }

    table.deselectAll();
    table.setSelection( item1 );
    Table anotherTable = new Table( shell, SWT.NONE );
    TableItem anotherItem = new TableItem( anotherTable, SWT.NONE );
    table.setSelection( anotherItem );
    assertEquals( 0, table.getSelectionCount() );

    // Test select(int)
    table.deselectAll();
    table.select( 0 );
    assertEquals( true, table.isSelected( 0 ) );
    table.select( 1 );
    assertEquals( false, table.isSelected( 0 ) );
    assertEquals( true, table.isSelected( 1 ) );

    table.deselectAll();
    table.select( 0 );
    table.select( table.getItemCount() + 20 );
    assertEquals( true, table.isSelected( 0 ) );

    // Test select(int,int)
    table.deselectAll();
    table.select( 0, 0 );
    assertEquals( 1, table.getSelectionCount() );
    assertEquals( true, table.isSelected( 0 ) );

    table.deselectAll();
    table.setSelection( 2 );
    table.select( 0, 1 );
    assertEquals( 1, table.getSelectionCount() );
    assertEquals( true, table.isSelected( 2 ) );

    // Test select(int[])
    table.deselectAll();
    table.select( new int[]{ 0 } );
    assertEquals( 1, table.getSelectionCount() );
    assertEquals( true, table.isSelected( 0 ) );

    table.deselectAll();
    table.setSelection( 2 );
    table.select( new int[]{ 0, 1 } );
    assertEquals( 1, table.getSelectionCount() );
    assertEquals( true, table.isSelected( 2 ) );

    table.deselectAll();
    table.setSelection( 2 );
    table.select( new int[]{ 777 } );
    assertEquals( 1, table.getSelectionCount() );
    assertEquals( true, table.isSelected( 2 ) );
  }

  public void testMultiSelection() {
    Display display = new Display();
    Shell shell = new Shell( display );
    Table table = new Table( shell, SWT.MULTI );
    TableItem item1 = new TableItem( table, SWT.NONE );
    new TableItem( table, SWT.NONE );
    new TableItem( table, SWT.NONE );
    new TableItem( table, SWT.NONE );

    // Test setSelection(int)
    table.deselectAll();
    table.setSelection( 0 );
    assertEquals( true, table.isSelected( 0 ) );

    table.setSelection( table.getItemCount() + 20 );
    assertEquals( 0, table.getSelectionCount() );

    // Test setSelection(int,int)
    table.deselectAll();
    table.setSelection( 0, 0 );
    assertEquals( 1, table.getSelectionCount() );
    assertEquals( true, table.isSelected( 0 ) );

    table.deselectAll();
    table.setSelection( 1 );
    table.setSelection( 0, 2 );
    assertEquals( 3, table.getSelectionCount() );
    assertEquals( true, table.isSelected( 0 ) );
    assertEquals( true, table.isSelected( 1 ) );
    assertEquals( true, table.isSelected( 2 ) );
    assertEquals( false, table.isSelected( 3 ) );

    table.deselectAll();
    table.setSelection( 0 );
    table.setSelection( 1, 777 );
    assertEquals( false, table.isSelected( 0 ) );
    assertEquals( true, table.isSelected( 1 ) );
    assertEquals( true, table.isSelected( 2 ) );
    assertEquals( true, table.isSelected( 3 ) );

    // Test setSelection(int[])
    table.deselectAll();
    table.setSelection( new int[]{ 0 } );
    assertEquals( 1, table.getSelectionCount() );
    assertEquals( true, table.isSelected( 0 ) );

    table.deselectAll();
    table.setSelection( 2 );
    table.setSelection( new int[]{ 0, 1 } );
    assertEquals( 2, table.getSelectionCount() );
    assertEquals( true, table.isSelected( 0 ) );
    assertEquals( true, table.isSelected( 1 ) );
    assertEquals( false, table.isSelected( 2 ) );
    assertEquals( false, table.isSelected( 3 ) );

    table.deselectAll();
    table.setSelection( 2 );
    table.setSelection( new int[]{ 777 } );
    assertEquals( 0, table.getSelectionCount() );

    // Test setSelection(TableItem)
    table.deselectAll();
    table.setSelection( item1 );
    assertEquals( 1, table.getSelectionCount() );
    assertEquals( item1, table.getSelection()[ 0 ] );

    try {
      table.deselectAll();
      table.setSelection( item1 );
      table.setSelection( ( TableItem )null );
    } catch( IllegalArgumentException e ) {
      // expected
      assertEquals( 1, table.getSelectionCount() );
      assertEquals( item1, table.getSelection()[ 0 ] );
    }

    table.deselectAll();
    table.setSelection( item1 );
    Table anotherTable = new Table( shell, SWT.NONE );
    TableItem anotherItem = new TableItem( anotherTable, SWT.NONE );
    table.setSelection( anotherItem );
    assertEquals( 0, table.getSelectionCount() );

    // Test select(int)
    table.deselectAll();
    table.select( 0 );
    assertEquals( 1, table.getSelectionCount() );
    assertEquals( true, table.isSelected( 0 ) );
    table.select( 1 );
    assertEquals( 2, table.getSelectionCount() );
    assertEquals( true, table.isSelected( 0 ) );
    assertEquals( true, table.isSelected( 1 ) );

    table.deselectAll();
    table.select( 0 );
    table.select( table.getItemCount() + 20 );
    assertEquals( true, table.isSelected( 0 ) );

    // Test select(int,int)
    table.deselectAll();
    table.select( 0, 0 );
    assertEquals( 1, table.getSelectionCount() );
    assertEquals( true, table.isSelected( 0 ) );

    table.deselectAll();
    table.setSelection( 2 );
    table.select( 0, 1 );
    assertEquals( 3, table.getSelectionCount() );
    assertEquals( true, table.isSelected( 0 ) );
    assertEquals( true, table.isSelected( 1 ) );
    assertEquals( true, table.isSelected( 2 ) );

    // Test select(int[])
    table.deselectAll();
    table.select( new int[]{ 0 } );
    assertEquals( 1, table.getSelectionCount() );
    assertEquals( true, table.isSelected( 0 ) );

    table.deselectAll();
    table.setSelection( 2 );
    table.select( new int[]{ 0, 1, 777 } );
    assertEquals( 3, table.getSelectionCount() );
    assertEquals( true, table.isSelected( 0 ) );
    assertEquals( true, table.isSelected( 1 ) );
    assertEquals( true, table.isSelected( 2 ) );

    table.deselectAll();
    table.setSelection( 2 );
    table.select( new int[]{ 777 } );
    assertEquals( 1, table.getSelectionCount() );
    assertEquals( true, table.isSelected( 2 ) );
  }

  public void testSelectionReveals() {
    Display display = new Display();
    Shell shell = new Shell( display );
    Table table = new Table( shell, SWT.BORDER | SWT.MULTI );
    table.setSize( 200, 200 );
    for( int i = 0; i < 128; i++ ) {
      TableItem item = new TableItem( table, SWT.NONE );
      item.setText( "Item " + i );
    }

    // test case precondition: table offers space for max. 30 visible items
    assertTrue( table.getVisibleItemCount( false ) < 30 );

    // calling setSelection makes the selected item visible
    table.setSelection( 95 );
    ITableAdapter tableAdapter
      = ( ITableAdapter )table.getAdapter( ITableAdapter.class );
    assertTrue( tableAdapter.isItemVisible( table.getItem( 95 ) ) );

    // calling select does *not* make the selected item visible
    table.select( 0 );
    assertFalse( tableAdapter.isItemVisible( table.getItem( 0 ) ) );
    assertTrue( tableAdapter.isItemVisible( table.getItem( 95 ) ) );

    // selecting a range will make the lower end of the range visible
    table.setSelection( 0, 95 );
    assertTrue( tableAdapter.isItemVisible( table.getItem( 0 ) ) );
    assertFalse( tableAdapter.isItemVisible( table.getItem( 95 ) ) );
  }

  public void testGetSelectionIndex() {
    Display display = new Display();
    Shell shell = new Shell( display );

    // SWT.SINGLE
    Table singleTable = new Table( shell, SWT.SINGLE );
    new TableItem( singleTable, SWT.NONE );
    new TableItem( singleTable, SWT.NONE );

    singleTable.setSelection( 1 );
    assertEquals( 1, singleTable.getSelectionIndex() );

    // SWT.MULTI
    Table multiTable = new Table( shell, SWT.MULTI );
    new TableItem( multiTable, SWT.NONE );
    new TableItem( multiTable, SWT.NONE );
    new TableItem( multiTable, SWT.NONE );

    multiTable.setSelection( 1 );
    assertEquals( 1, multiTable.getSelectionIndex() );
    multiTable.select( 2 );
    assertEquals( 1, multiTable.getSelectionIndex() );
  }

  public void testSelectAll() {
    Display display = new Display();
    Shell shell = new Shell( display );

    // SWT.SINGLE
    Table singleTable = new Table( shell, SWT.SINGLE );
    new TableColumn( singleTable, SWT.NONE );
    new TableItem( singleTable, SWT.NONE );
    new TableItem( singleTable, SWT.NONE );
    singleTable.deselectAll();
    singleTable.selectAll();
    assertEquals( 0, singleTable.getSelectionCount() );
    singleTable.setSelection( 1 );
    assertEquals( 1, singleTable.getSelectionCount() );

    // SWT.MULTI
    Table multiTable = new Table( shell, SWT.MULTI );
    new TableColumn( multiTable, SWT.NONE );
    new TableItem( multiTable, SWT.NONE );
    new TableItem( multiTable, SWT.NONE );
    multiTable.selectAll();
    assertEquals( multiTable.getItemCount(), multiTable.getSelectionCount() );
  }

  public void testDeselect() {
    Display display = new Display();
    Shell shell = new Shell( display );
    Table table = new Table( shell, SWT.SINGLE );
    new TableColumn( table, SWT.NONE );
    new TableItem( table, SWT.NONE );
    new TableItem( table, SWT.NONE );

    // deselect(int)
    table.setSelection( 0 );
    table.deselect( 0 );
    assertEquals( 0, table.getSelectionCount() );

    // deselect(int,int)
    table.setSelection( 1 );
    table.deselect( 0, 777 );
    assertEquals( 0, table.getSelectionCount() );
    table.setSelection( 1 );
    table.deselect( 4, 777 );
    assertEquals( 1, table.getSelectionCount() );
    assertEquals( true, table.isSelected( 1 ) );

    // deselect(int[])
    table.setSelection( 1 );
    table.deselect( new int[ 0 ] );
    assertEquals( 1, table.getSelectionCount() );
    assertEquals( true, table.isSelected( 1 ) );

    table.setSelection( 1 );
    table.deselect( new int[] { 1, 777 } );
    assertEquals( 0, table.getSelectionCount() );

    table = new Table( shell, SWT.MULTI );
    new TableColumn( table, SWT.NONE );
    new TableItem( table, SWT.NONE );
    new TableItem( table, SWT.NONE );

    table.selectAll();
    table.deselect( new int[] { 0, 2 } );
    assertEquals( 1, table.getSelectionCount() );
    assertEquals( true, table.isSelected( 1 ) );
  }

  public void testDeselectAll() {
    Display display = new Display();
    Shell shell = new Shell( display );
    Table table = new Table( shell, SWT.NONE );
    new TableColumn( table, SWT.NONE );
    new TableItem( table, SWT.NONE );
    new TableItem( table, SWT.NONE );

    table.setSelection( 1 );
    table.deselectAll();
    assertEquals( -1, table.getSelectionIndex() );
  }

  public void testIsSelectedNonVirtual() {
    Display display = new Display();
    Shell shell = new Shell( display );
    Table table = new Table( shell, SWT.NONE );
    new TableColumn( table, SWT.NONE );
    new TableItem( table, SWT.NONE );
    new TableItem( table, SWT.NONE );

    // initial state: no selection, isSelected returns alway false
    assertFalse( table.isSelected( 0 ) );
    assertFalse( table.isSelected( 1 ) );
    // test with indices that are out of range, must always return false
    assertFalse( table.isSelected( -3 ) );
    assertFalse( table.isSelected( table.getItemCount() + 100 ) );
    // select and verify that isSelected returns true
    table.setSelection( 0 );
    assertTrue( table.isSelected( 0 ) );
  }

  public void testIsSelectedVirtual() {
    Fixture.fakePhase( PhaseId.PROCESS_ACTION );
    Display display = new Display();
    Shell shell = new Shell( display );
    shell.setLayout( new FillLayout() );
    Table table = new Table( shell, SWT.VIRTUAL );
    new TableColumn( table, SWT.NONE );
    table.setItemCount( 1000 );
    shell.open();

    // initial state: no selection, isSelected returns alway false
    assertFalse( table.isSelected( 0 ) );
    assertFalse( table.isSelected( 1 ) );
    // test with indices that are out of range, must always return false
    assertFalse( table.isSelected( -3 ) );
    assertFalse( table.isSelected( table.getItemCount() + 100 ) );
    // select and verify that isSelected returns true
    table.setSelection( 0 );
    assertTrue( table.isSelected( 0 ) );
    // ensure that calling isSelected does not resolve a virtual item
    ITableAdapter tableAdapter
      = ( ITableAdapter )table.getAdapter( ITableAdapter.class );
    boolean selected = table.isSelected( 900 );
    assertFalse( selected );
    assertTrue( tableAdapter.isItemVirtual( 900 ) );
  }

  public void testClearNonVirtual() {
    Fixture.fakePhase( PhaseId.PROCESS_ACTION );
    Display display = new Display();
    Shell shell = new Shell( display );
    Table table = new Table( shell, SWT.CHECK );
    new TableColumn( table, SWT.NONE );
    TableItem item = new TableItem( table, SWT.NONE );
    ITableAdapter tableAdapter
      = ( ITableAdapter )table.getAdapter( ITableAdapter.class );

    table.setSelection( item );
    item.setText( "abc" );
    item.setImage( Graphics.getImage( Fixture.IMAGE1 ) );
    item.setChecked( true );
    item.setGrayed( true );
    table.clear( table.indexOf( item ) );
    assertEquals( "", item.getText() );
    assertEquals( null, item.getImage() );
    assertEquals( false, item.getChecked() );
    assertEquals( false, item.getGrayed() );
    assertFalse( tableAdapter.isItemVirtual( table.indexOf( item ) ) );
    assertSame( item, table.getSelection()[ 0 ] );

    // Test clear with illegal arguments
    try {
      table.clear( 2 );
      fail( "Must throw exception when attempting to clear non-existing item" );
    } catch( IllegalArgumentException e ) {
      // expected
    }
  }

  public void testClearVirtual() {
    Fixture.fakePhase( PhaseId.PROCESS_ACTION );
    Display display = new Display();
    Shell shell = new Shell( display );
    shell.setLayout( new FillLayout() );
    Table table = new Table( shell, SWT.VIRTUAL | SWT.CHECK );
    new TableColumn( table, SWT.NONE );
    table.setItemCount( 100 );
    shell.layout();
    shell.open();
    ITableAdapter tableAdapter
      = ( ITableAdapter )table.getAdapter( ITableAdapter.class );

    table.getItem( 0 ).getText();
    table.select( 0 );
    table.clear( 0 );
    assertTrue( tableAdapter.isItemVirtual( 0 ) );
    assertEquals( 0, table.getSelectionIndex() );
  }

  public void testClearRange() {
    Display display = new Display();
    Shell shell = new Shell( display );
    Table table = new Table( shell, SWT.NONE );
    new TableColumn( table, SWT.NONE );
    TableItem[] items = new TableItem[ 10 ];
    for( int i = 0; i < 10; i++ ) {
      items[ i ] = new TableItem( table, SWT.NONE );
      items[ i ].setText( "abc" );
      items[ i ].setImage( Graphics.getImage( Fixture.IMAGE1 ) );
    }
    table.clear( 2, 5 );
    Image img = Graphics.getImage( Fixture.IMAGE1 );
    for( int i = 0; i < 10; i++ ) {
      if( i >= 2 && i <= 5 ) {
        assertEquals( "", items[ i ].getText() );
        assertEquals( null, items[ i ].getImage() );
      } else {
        assertEquals( "abc", items[ i ].getText() );
        assertEquals( img, items[ i ].getImage() );
      }
    }
    // Test clear with illegal arguments (end > items)
    try {
      table.clear( 1, 11 );
      fail( "Must throw exception when attempting to clear non-existing items" );
    } catch( IllegalArgumentException e ) {
      // expected
    }
  }

  public void testClearIndices() {
	  Display display = new Display();
	  Shell shell = new Shell( display );
	  Table table = new Table( shell, SWT.NONE );
	  new TableColumn( table, SWT.NONE );

	  TableItem[] items = new TableItem[10];
	  for (int i = 0; i < 10; i++) {
		  items[i] = new TableItem( table, SWT.NONE );
		  items[i].setText( "abc" );
		  items[i].setImage( Graphics.getImage( Fixture.IMAGE1 ) );
	  }

	  table.clear( new int[]{ 1, 3, 5 } );
    Image img = Graphics.getImage( Fixture.IMAGE1 );
    for( int i = 0; i < 10; i++ ) {
      if( i == 1 || i == 3 || i == 5 ) {
        assertEquals( "", items[ i ].getText() );
        assertEquals( null, items[ i ].getImage() );
      } else {
        assertEquals( "abc", items[ i ].getText() );
        assertEquals( img, items[ i ].getImage() );
      }
    }
    // Test clear with illegal arguments
    try {
      table.clear( new int[]{ 2, 4, 15 } );
      fail( "Must throw exception when attempting to clear non-existing items" );
    } catch( IllegalArgumentException e ) {
      // expected
    }
  }

  public void testShowItem() {
    Fixture.fakePhase( PhaseId.PROCESS_ACTION );
    Display display = new Display();
    Shell shell = new Shell( display );
    Table table = new Table( shell, SWT.NONE );
    table.setLinesVisible( false );
    table.setHeaderVisible( false );
    new TableColumn( table, SWT.NONE );
    int itemCount = 300;
    for( int i = 0; i < itemCount; i++ ) {
      new TableItem( table, SWT.NONE );
    }
    int itemHeight = table.getItemHeight();
    int visibleLines = 100;
    table.setSize( 100, visibleLines * itemHeight );
    assertEquals( visibleLines, table.getVisibleItemCount( false ) );

    table.showItem( table.getItem( 99 ) );
    assertEquals( 0, table.getTopIndex() );

    table.showItem( table.getItem( 100 ) );
    assertEquals( 1, table.getTopIndex() );

    table.showItem( table.getItem( 199 ) );
    assertEquals( 100, table.getTopIndex() );

    table.showItem( table.getItem( itemCount - 1 ) );
    assertEquals( 200, table.getTopIndex() );

    table.showItem( table.getItem( 42 ) );
    assertEquals( 42, table.getTopIndex() );

    table.showItem( table.getItem( 0 ) );
    assertEquals( 0, table.getTopIndex() );
  }

  public void testSetSelectionBeforeSetSize() {
    // Calling setSelection() before setSize() should not change top index
    // See bug 272714, https://bugs.eclipse.org/bugs/show_bug.cgi?id=272714
    Fixture.fakePhase( PhaseId.PROCESS_ACTION );
    Display display = new Display();
    Shell shell = new Shell( display );
    Table table = new Table( shell, SWT.NONE );
    for( int i = 0; i < 10; i++ ) {
      new TableItem( table, SWT.NONE );
    }
    table.setSelection( 0 );
    table.setSize( 100, 100 );
    assertEquals( 0, table.getTopIndex() );
    table.setSize( 0, 0 );
    table.setSelection( 3 );
    table.setSize( 100, 100 );
    assertEquals( 0, table.getTopIndex() );
    // table with one item and item height == 1
    table = new Table( shell, SWT.NONE );
    new TableItem( table, SWT.NONE );
    table.setSelection( 0 );
    table.setSize( 100, table.getItemHeight() + 4 );
    assertEquals( 0, table.getTopIndex() );
    table.setSize( 0, 0 );
    table.setSelection( 1 );
    table.setSize( 100, table.getItemHeight() + 4 );
    assertEquals( 0, table.getTopIndex() );
  }

  public void testSortColumnAndDirection() {
    Display display = new Display();
    Shell shell = new Shell( display );
    Table table = new Table( shell, SWT.NONE );
    TableColumn column = new TableColumn( table, SWT.NONE );

    // Simple case set == get
    table.setSortColumn( column );
    assertSame( column, table.getSortColumn() );
    table.setSortColumn( null );
    assertNull( table.getSortColumn() );

    // Dispose sortColumn
    table.setSortColumn( column );
    table.setSortDirection( SWT.UP );
    column.dispose();
    assertNull( table.getSortColumn() );
    assertEquals( SWT.UP, table.getSortDirection() );

    // Try to set a disposed of column
    TableColumn disposedColumn = new TableColumn( table, SWT.NONE );
    disposedColumn.dispose();
    try {
      table.setSortColumn( disposedColumn );
      fail( "Must not allow to set disposed of sort column" );
    } catch( IllegalArgumentException e ) {
      // expected
    }

    // Test sortDirection
    table.setSortDirection( SWT.NONE );
    assertEquals( SWT.NONE, table.getSortDirection() );
    table.setSortDirection( SWT.UP );
    assertEquals( SWT.UP, table.getSortDirection() );
    table.setSortDirection( SWT.DOWN );
    assertEquals( SWT.DOWN, table.getSortDirection() );
    table.setSortDirection( SWT.NONE );
    table.setSortDirection( 4711 );
    assertEquals( SWT.NONE, table.getSortDirection() );
  }

  public void testGetColumnOrder() {
    Display display = new Display();
    Shell shell = new Shell( display );
    Table table = new Table( shell, SWT.NONE );

    // Test column order for table without columns
    assertEquals( 0, table.getColumnOrder().length );

    // Test column order for the first, newly created column
    TableColumn column = new TableColumn( table, SWT.NONE );
    assertEquals( 1, table.getColumnOrder().length );
    assertEquals( 0, table.getColumnOrder()[ 0 ] );
    table.getColumnOrder()[ 0 ] = 12345;
    assertEquals( 0, table.getColumnOrder()[ 0 ] );

    // Test column order when disposing of the one and only column
    column.dispose();
    assertEquals( 0, table.getColumnOrder().length );

    // Test creating a column for the now column-less table
    column = new TableColumn( table, SWT.NONE );
    assertEquals( 1, table.getColumnOrder().length );
    assertEquals( 0, table.getColumnOrder()[ 0 ] );

    // Test creating another column: must be added at the end
    TableColumn anotherColumn = new TableColumn( table, SWT.NONE );
    assertEquals( 2, table.getColumnOrder().length );
    assertEquals( 0, table.getColumnOrder()[ table.indexOf( column ) ] );
    assertEquals( 1, table.getColumnOrder()[ table.indexOf( anotherColumn ) ] );

    // Insert column1 between the already existing column0 and column2
    table = new Table( shell, SWT.NONE );
    TableColumn column0 = new TableColumn( table, SWT.NONE );
    TableColumn column2 = new TableColumn( table, SWT.NONE );
    TableColumn column1 = new TableColumn( table, SWT.NONE, 1 );
    assertEquals( column0, table.getColumn( 0 ) );
    assertEquals( column1, table.getColumn( 1 ) );
    assertEquals( column2, table.getColumn( 2 ) );
    assertEquals( 3, table.getColumnOrder().length );
    assertEquals( 0, table.getColumnOrder()[ table.indexOf( column0 ) ] );
    assertEquals( 1, table.getColumnOrder()[ table.indexOf( column1 ) ] );
    assertEquals( 2, table.getColumnOrder()[ table.indexOf( column2 ) ] );
  }

  public void testSetColumnOrder() {
    Fixture.fakePhase( PhaseId.PROCESS_ACTION );
    final StringBuffer log = new StringBuffer();
    ControlAdapter controlAdapter = new ControlAdapter() {
      public void controlMoved( final ControlEvent event ) {
        TableColumn column = ( TableColumn )event.widget;
        log.append( column.getText() + " moved|" );
      }
    };
    Display display = new Display();
    Shell shell = new Shell( display );
    Table table = new Table( shell, SWT.NONE );
    TableColumn column0 = new TableColumn( table, SWT.NONE );
    column0.setText( "Col0" );
    column0.addControlListener( controlAdapter );
    int column0Index = table.indexOf( column0 );
    TableColumn column1 = new TableColumn( table, SWT.NONE );
    column1.setText( "Col1" );
    column1.addControlListener( controlAdapter );
    int column1Index = table.indexOf( column1 );

    // Precondition: changing the column order programmatically is allowed
    // even if the moveable property is false
    assertEquals( false, column0.getMoveable() );
    assertEquals( false, column1.getMoveable() );

    // Ensure that changing the column order fire controlMoved events
    table.setColumnOrder( new int[] { column1Index, column0Index } );
    assertEquals( 2, table.getColumnOrder().length );
    assertEquals( 1, table.getColumnOrder()[ column0Index ] );
    assertEquals( 0, table.getColumnOrder()[ column1Index ] );
    assertEquals( "Col1 moved|Col0 moved|", log.toString() );

    // Ensure that calling setColumnOrder with the same order as already is
    // does not fire controlModevd events
    log.setLength( 0 );
    table.setColumnOrder( table.getColumnOrder() );
    assertEquals( "", log.toString() );
  }

  public void testDisposeOfOrderedColumn() {
    Display display = new Display();
    Shell shell = new Shell( display );
    Table table = new Table( shell, SWT.NONE );

    new TableColumn( table, SWT.NONE );
    new TableColumn( table, SWT.NONE );
    new TableColumn( table, SWT.NONE );
    table.setColumnOrder( new int[] { 1, 0, 2 } );
    table.getColumn( 0 ).dispose();
    assertEquals( 2, table.getColumnOrder().length );
    assertEquals( 0, table.getColumnOrder()[ 0 ] );
    assertEquals( 1, table.getColumnOrder()[ 1 ] );

    clearColumns( table );
    new TableColumn( table, SWT.NONE );
    new TableColumn( table, SWT.NONE );
    table.setColumnOrder( new int[] { 0, 1 } );
    table.getColumn( 0 ).dispose();
    assertEquals( 1, table.getColumnOrder().length );
    assertEquals( 0, table.getColumnOrder()[ 0 ] );

    clearColumns( table );
    new TableColumn( table, SWT.NONE );
    new TableColumn( table, SWT.NONE );
    new TableColumn( table, SWT.NONE );
    table.setColumnOrder( new int[] { 0, 1, 2 } );
    table.getColumn( 2 ).dispose();
    assertEquals( 2, table.getColumnOrder().length );
    assertEquals( 0, table.getColumnOrder()[ 0 ] );
    assertEquals( 1, table.getColumnOrder()[ 1 ] );
  }

  public void testRedrawAfterDisposeVirtual() {
    Fixture.fakePhase( PhaseId.PROCESS_ACTION );
    Display display = new Display();
    Shell shell = new Shell( display );
    Table table = new Table( shell, SWT.VIRTUAL );
    table.setSize( 100, 100 );
    table.setItemCount( 150 );
    // dispose the first item, this must cause a "redraw" which in turn triggers
    // a SetData event to populate the newly appeared item at the bottom of the
    // table
    table.getItem( 0 ).dispose();
    assertTrue( RWTLifeCycle.needsFakeRedraw( table ) );
  }

  public void testSetColumnOrderWithInvalidArguments() {
    Display display = new Display();
    Shell shell = new Shell( display );
    Table table = new Table( shell, SWT.NONE );
    // Passing null is not allowed
    try {
      table.setColumnOrder( null );
      fail( "setColumnOrder must not accept null argument" );
    } catch( IllegalArgumentException iae ) {
      // expected
    }
    // Passing in an array with more elements than columns is not allowed
    new TableColumn( table, SWT.NONE );
    try {
      table.setColumnOrder( new int[] { 0, 0 } );
      fail( "setColumnOrder must not accept more elements than columns" );
    } catch( IllegalArgumentException e ) {
      // expected
    }
    // Passing in an array with more elements than columns is not allowed
    new TableColumn( table, SWT.NONE );
    try {
      table.setColumnOrder( new int[] { 0, 0 } );
      fail( "setColumnOrder must not accept duplicate elements" );
    } catch( IllegalArgumentException e ) {
      // expected
    }
    try {
      table.setColumnOrder( new int[] { 0, 77 } );
      fail( "setColumnOrder must not accept elements out of range" );
    } catch( IllegalArgumentException e ) {
      // expected
    }
  }

  public void testSetItemCountNonVirtual() {
    Display display = new Display();
    Shell shell = new Shell( display );
    Table table = new Table( shell, SWT.NONE );

    // Setting itemCount to a higher value than getItemCount() creates the
    // missing items
    table.setItemCount( 2 );
    assertEquals( 2, table.getItemCount() );
    assertNotNull( table.getItem( 0 ) );
    assertNotNull( table.getItem( 1 ) );

    // Passing in the same value as already set is ignored
    table.setItemCount( 2 );
    table.setItemCount( table.getItemCount() );
    assertEquals( 2, table.getItemCount() );

    // Passing in a negative value is the same as passing in zero
    table.setItemCount( 2 );
    table.setItemCount( -2 );
    assertEquals( 0, table.getItemCount() );

    // Setting itemCount to a lower value than getItemCount() disposes of
    // the superfluous items
    table.setItemCount( 2 );
    table.setItemCount( 1 );
    assertEquals( 1, table.getItemCount() );
    assertNotNull( table.getItem( 0 ) );
  }

  public void testSetItemCountVirtual() {
    Display display = new Display();
    Shell shell = new Shell( display );
    Table table = new Table( shell, SWT.VIRTUAL );

    table.setItemCount( 1 );
    assertEquals( 1, table.getItemCount() );
    Item[] items = ItemHolder.getItems( table );
    assertEquals( 0, items.length );

    new TableItem( table, SWT.NONE );
    assertEquals( 2, table.getItemCount() );
    items = ItemHolder.getItems( table );
    assertEquals( 1, items.length );
  }

  public void testClearAllAndSetItemCountWithSelection() {
    Fixture.fakePhase( PhaseId.PROCESS_ACTION );
    Display display = new Display();
    Shell shell = new Shell( display );
    shell.setSize( 100, 100 );
    shell.open();
    Table table = new Table( shell, SWT.VIRTUAL | SWT.SINGLE );
    table.setSize( 90, 90 );
    table.setItemCount( 10 );
    // force items to be resolved
    for( int i = 0; i < table.getItemCount(); i++ ) {
      table.getItem( i ).getText();
    }

    table.setSelection( 0 );
    TableItem selectedItem = table.getSelection()[ 0 ];
    table.clearAll();
    table.setItemCount( table.getItemCount() - 1 );

    assertEquals( 0, table.getSelectionIndex() );
    assertEquals( selectedItem, table.getSelection()[ 0 ] );
  }
  
  // bug 303473
  public void testItemImageSizeAfterClear() {
    Display display = new Display();
    Shell shell = new Shell( display );
    Table table = new Table( shell, SWT.NONE );
    TableItem item = new TableItem( table, SWT.NONE );
    item.setImage( Graphics.getImage( Fixture.IMAGE_50x100 ) );
    table.clearAll();
    assertEquals( new Point( 0, 0 ), table.getItemImageSize() );
  }
  
  public void testItemImageSizeAfterRemovingAllItems() {
    Display display = new Display();
    Shell shell = new Shell( display );
    Table table = new Table( shell, SWT.NONE );
    TableItem item = new TableItem( table, SWT.NONE );
    item.setImage( Graphics.getImage( Fixture.IMAGE_50x100 ) );
    item.dispose();
    assertEquals( new Point( 0, 0 ), table.getItemImageSize() );
  }

  public void testSetItemCountWithSetDataListener() {
    Fixture.fakePhase( PhaseId.PROCESS_ACTION );
    Display display = new Display();
    Shell shell = new Shell( display );
    shell.setSize( 100, 100 );
    shell.open();
    Table table = new Table( shell, SWT.VIRTUAL );
    table.setSize( 90, 90 );
    table.addListener( SWT.SetData, new Listener() {
      public void handleEvent( final Event event ) {
        fail( "SetItemCount must not fire SetData events" );
      }
    } );
    // Ensure that setItemCount itself does not fire setData events
    table.setItemCount( 200 );
    assertEquals( 200, table.getItemCount() );
  }

  public void testResizeWithVirtualItems() {
    Fixture.fakePhase( PhaseId.PROCESS_ACTION );
    Display display = new Display();
    Shell shell = new Shell( display );
    Table table = new Table( shell, SWT.VIRTUAL );
    table.setSize( 0, 0 );
    table.setItemCount( 1 );
    shell.open();

    // Ensure that a virtual item is not "realized" as long as it is invisible
    assertEquals( 0, ItemHolder.getItems( table ).length );

    // Enlarge the table so that the item will become visible
    table.setSize( 200, 200 );
    assertEquals( 1, ItemHolder.getItems( table ).length );
  }

  public void testItemImageSize() {
    Display display = new Display();
    Shell shell = new Shell( display );
    Table table = new Table( shell, SWT.NONE );

    // Test initial itemImageSize
    assertEquals( new Point( 0, 0 ), table.getItemImageSize() );

    // Setting a null-image shouldn't change anything
    TableItem item = new TableItem( table, SWT.NONE );
    item.setImage( ( Image )null );
    assertEquals( new Point( 0, 0 ), table.getItemImageSize() );

    // Setting the first image also sets the itemImageSize for always and ever
    item.setImage( Graphics.getImage( Fixture.IMAGE_50x100 ) );
    assertEquals( new Point( 50, 100 ), table.getItemImageSize() );

    // Ensure that the itemImageSize - once detemined - does not change anymore
    item.setImage( Graphics.getImage( Fixture.IMAGE_100x50 ) );
    assertEquals( new Point( 50, 100 ), table.getItemImageSize() );

    // Ensure that the method returns the actual image size, not clipped by the
    // available width given by the column
    TableColumn column = new TableColumn( table, SWT.NONE );
    column.setWidth( 20 ); // image width is 50
    assertEquals( new Point( 50, 100 ), table.getItemImageSize() );
  }

  public void testHasColumnImages() {
    Display display = new Display();
    Shell shell = new Shell( display );
    Table table = new Table( shell, SWT.NONE );
    TableItem item0 = new TableItem( table, SWT.NONE );

    // Test without columns
    assertFalse( table.hasColumnImages( 0 ) );
    item0.setImage( ( Image )null );
    assertFalse( table.hasColumnImages( 0 ) );
    item0.setImage( Graphics.getImage( Fixture.IMAGE_50x100 ) );
    assertTrue( table.hasColumnImages( 0 ) );
    item0.setImage( ( Image )null );
    assertFalse( table.hasColumnImages( 0 ) );
    item0.setImage( Graphics.getImage( Fixture.IMAGE_50x100 ) );
    item0.dispose();
    assertFalse( table.hasColumnImages( 0 ) );
    item0 = new TableItem( table, SWT.NONE );
    item0.setImage( Graphics.getImage( Fixture.IMAGE_50x100 ) );
    TableItem item1 = new TableItem( table, SWT.NONE );
    item1.setImage( Graphics.getImage( Fixture.IMAGE_50x100 ) );
    assertTrue( table.hasColumnImages( 0 ) );
    item1.setImage( ( Image )null );
    assertTrue( table.hasColumnImages( 0 ) );

    // Dispose column that 'holds' images
    table.removeAll();
    TableColumn column0 = new TableColumn( table, SWT.NONE );
    new TableColumn( table, SWT.NONE );
    item0 = new TableItem( table, SWT.NONE );
    item0.setImage( 1, Graphics.getImage( Fixture.IMAGE_50x100 ) );
    assertFalse( table.hasColumnImages( 0 ) );
    assertTrue( table.hasColumnImages( 1 ) );
    column0.dispose();
    assertTrue( table.hasColumnImages( 0 ) );
    item0.setImage( 0, Graphics.getImage( Fixture.IMAGE_50x100 ) );
    assertTrue( table.hasColumnImages( 0 ) );
    item0.setImage( 0, null );
    assertFalse( table.hasColumnImages( 0 ) );
  }

  public void testHasColumnImagesAfterColumnDispose() {
    Display display = new Display();
    Shell shell = new Shell( display, SWT.NONE );
    Table table = new Table( shell, SWT.NONE );
    TableColumn column = new TableColumn( table, SWT.LEFT );
    column.dispose();
    // No assertion here,
    // call hasColumnImages() to make sure the internal structures are OK
    table.hasColumnImages( 0 );
  }

  public void testGetItem() {
    Display display = new Display();
    Shell shell = new Shell( display );
    Table table = new Table( shell, SWT.NONE );
    TableItem item;
    // Illegal argument
    try {
      table.getItem( null );
      fail( "Must not alow null-argument" );
    } catch( IllegalArgumentException e ) {
      // expected
    }
    // test with empty table
    table.setSize( 100, 100 );
    item = table.getItem( new Point( 200, 200 ) );
    assertNull( item );
    item = table.getItem( new Point( 50, 50 ) );
    assertNull( item );
    item = table.getItem( new Point( -10, -20 ) );
    assertNull( item );
    // test with populated table
    table.setSize( 100, 100 );
    for( int i = 0; i < 100; i++ ) {
      new TableItem( table, SWT.NONE );
    }
    assertNotNull( table.getItem( new Point( 5, 5 ) ) );
    item = table.getItem( new Point( 50, 50 ) );
    assertNotNull( item );
    item = table.getItem( new Point( 200, 200 ) );
    assertNull( item );
    item = table.getItem( new Point( -10, -20 ) );
    assertNull( item );
    item = table.getItem( new Point( 0, 0 ) );
    assertNotNull( item );
    assertEquals( 0, table.indexOf( item ) );
    item = table.getItem( new Point( 0, table.getItemHeight() ) );
    assertNotNull( item );
    assertEquals( 0, table.indexOf( item ) );
    // test with headers
    table.setHeaderVisible( true );
    item = table.getItem( new Point( 0, 0 ) );
    assertNull( item );
    item = table.getItem( new Point( 2, table.getHeaderHeight() + 3 ) );
    assertEquals( 0, item.getParent().indexOf( item ) );
  }

  /*
   * 283263: ArrayIndexOutOfBoundsException when clicking on the Pixel Row just
   *         below the Table Header.
   * https://bugs.eclipse.org/bugs/show_bug.cgi?id=283263
   */
  public void testGetItemBelowHeader() {
    Display display = new Display();
    Shell shell = new Shell( display );
    Table table = new Table( shell, SWT.NONE );
    table.setHeaderVisible( true );
    table.setSize( 100, 100 );
    new TableItem( table, SWT.NONE );
    TableItem item = table.getItem( new Point( 10, table.getHeaderHeight() ) );
    assertNotNull( item );
    assertEquals( 0, table.indexOf( item ) );
  }

  /*
   * Ensures that checkData calls with an invalid index are silently ignored.
   * This may happen, when the itemCount is reduced during a SetData event.
   * Queued SetData events may then have stale (out-of-bounds) indices.
   * See 235368: [table] [table] ArrayIndexOutOfBoundsException in virtual
   *     TableViewer
   *     https://bugs.eclipse.org/bugs/show_bug.cgi?id=235368
   */
  public void testCheckDataWithInvalidIndex() {
    Fixture.fakePhase( PhaseId.PROCESS_ACTION );
    Display display = new Display();
    Shell shell = new Shell( display );
    Table table = new Table( shell, SWT.VIRTUAL );
    table.setItemCount( 10 );
    ITableAdapter adapter
      = ( ITableAdapter )table.getAdapter( ITableAdapter.class );
    adapter.checkData( 99 );
    // No assert - the purpose of this test is to ensure that no
    // ArrayIndexOutOfBoundsException is thrown
  }

  public void testComputeSizeNonVirtual() {
    Fixture.fakePhase( PhaseId.PROCESS_ACTION );
    Display display = new Display();
    Shell shell = new Shell( display );

    // Test non virtual table
    Table table = new Table( shell, SWT.NONE );
    Point expected = new Point( 28, 80 );
    assertEquals( expected, table.computeSize( SWT.DEFAULT, SWT.DEFAULT ) );

    for( int i = 0; i < 10; i++ ) {
      new TableItem( table, SWT.NONE ).setText( "Item " + i );
    }
    new TableItem( table, SWT.NONE ).setText( "Long long item 100" );
    expected = new Point( 115, 181 );
    assertEquals( expected, table.computeSize( SWT.DEFAULT, SWT.DEFAULT ) );

    table = new Table( shell, SWT.BORDER );
    for( int i = 0; i < 10; i++ ) {
      new TableItem( table, SWT.NONE ).setText( "Item " + i );
    }
    new TableItem( table, SWT.NONE ).setText( "Long long item 10" );
    expected = new Point( 114, 185 );
    assertEquals( expected, table.computeSize( SWT.DEFAULT, SWT.DEFAULT ) );

    table.setHeaderVisible( true );
    assertEquals( 15, table.getHeaderHeight() );
    expected = new Point( 114, 200 );
    assertEquals( expected, table.computeSize( SWT.DEFAULT, SWT.DEFAULT ) );

    TableColumn col1 = new TableColumn( table, SWT.NONE );
    col1.setText( "Col 1" );
    TableColumn col2 = new TableColumn( table, SWT.NONE );
    col2.setText( "Column 2" );
    TableColumn col3 = new TableColumn( table, SWT.NONE );
    col3.setText( "Wider Column" );
    expected = new Point( 84, 200 );
    assertEquals( expected, table.computeSize( SWT.DEFAULT, SWT.DEFAULT ) );

    col1.pack();
    col2.pack();
    col3.pack();
    expected = new Point( 227, 200 );
    assertEquals( expected, table.computeSize( SWT.DEFAULT, SWT.DEFAULT ) );

    col1.setWidth( 10 );
    col2.setWidth( 10 );
    assertEquals( 67, col3.getWidth() );
    expected = new Point( 107, 200 );
    assertEquals( expected, table.computeSize( SWT.DEFAULT, SWT.DEFAULT ) );

    table = new Table( shell, SWT.CHECK );
    for( int i = 0; i < 10; i++ ) {
      new TableItem( table, SWT.NONE ).setText( "Item " + i );
    }
    new TableItem( table, SWT.NONE ).setText( "Long long item 10" );
    expected = new Point( 131, 181 );
    assertEquals( expected, table.computeSize( SWT.DEFAULT, SWT.DEFAULT ) );

    expected = new Point( 316, 316 );
    assertEquals( expected, table.computeSize( 300, 300 ) );
  }

  public void testComputeSizeVirtual() {
    Fixture.fakePhase( PhaseId.PROCESS_ACTION );
    Display display = new Display();
    Shell shell = new Shell( display );

    Table table = new Table( shell, SWT.BORDER | SWT.VIRTUAL );
    table.setItemCount( 10 );
    table.addListener( SWT.SetData, new Listener() {
      public void handleEvent( final Event event ) {
        TableItem item = ( TableItem )event.item;
        int tableIndex = item.getParent().indexOf( item );
        item.setText( "Item " + tableIndex );
      }
    } );
    // 12 + srollbar (16) + 2 * border (2)
    assertEquals( 150, table.getItemCount() * table.getItemHeight() );
    Point expected = new Point( 32, 170 );
    assertEquals( expected, table.computeSize( SWT.DEFAULT, SWT.DEFAULT ) );

    table.setHeaderVisible( true );
    assertEquals( 15, table.getHeaderHeight() );
    expected = new Point( 32, 185 );
    assertEquals( expected, table.computeSize( SWT.DEFAULT, SWT.DEFAULT ) );

    TableColumn col1 = new TableColumn( table, SWT.NONE );
    col1.setText( "Col 1" );
    TableColumn col2 = new TableColumn( table, SWT.NONE );
    col2.setText( "Column 2" );
    TableColumn col3 = new TableColumn( table, SWT.NONE );
    col3.setText( "Wider Column" );
    expected = new Point( 84, 185 );
    assertEquals( expected, table.computeSize( SWT.DEFAULT, SWT.DEFAULT ) );

    // first table item is auto resolved
    col1.pack();
    col2.pack();
    col3.pack();
    expected = new Point( 169, 185 );
    assertEquals( expected, table.computeSize( SWT.DEFAULT, SWT.DEFAULT ) );

    col1.setWidth( 10 );
    col2.setWidth( 10 );
    assertEquals( 67, col3.getWidth() );
    expected = new Point( 107, 185 );
    assertEquals( expected, table.computeSize( SWT.DEFAULT, SWT.DEFAULT ) );

    expected = new Point( 320, 320 );
    assertEquals( expected, table.computeSize( 300, 300 ) );
  }

  public void testComputeSizeNoScroll() {
    Display display = new Display();
    Shell shell = new Shell( display );
    Table table = new Table( shell, SWT.NO_SCROLL );
    Point actual = table.computeSize( 20, 20 );
    Point expected = new Point( 20, 20 );
    assertEquals( expected, actual );
  }

  public void testGetVisibleItemCount() {
    Display display = new Display();
    Shell shell = new Shell( display );
    Table table = new Table( shell, SWT.NONE );
    for( int i = 0; i < 10; i++ ) {
      new TableItem( table, SWT.NONE ).setText( "Item " + i );
    }
    int itemHeight = table.getItemHeight();
    int scrollBarHeight = 16;
    table.setSize( 100, 5 * itemHeight + scrollBarHeight );
    assertEquals( 5, table.getVisibleItemCount( true ) );
    assertEquals( 5, table.getVisibleItemCount( false ) );
    // check that partially visible item is included in visible item count
    table.setSize( 100, 5 * itemHeight + scrollBarHeight + itemHeight / 2 );
    assertEquals( 5, table.getVisibleItemCount( false ) );
    assertEquals( 6, table.getVisibleItemCount( true ) );
  }

  public void testGetItemHeight() {
    Display display = new Display();
    Shell shell = new Shell( display );
    Table table = new Table( shell, SWT.NONE );
    TableItem item1 = new TableItem( table, SWT.NONE );
    item1.setText( "Item 1" );
    // default font size (11) + hardcoded minimal vertical padding (4)
    assertEquals( 15, table.getItemHeight() );
    TableItem item2 = new TableItem( table, SWT.NONE );
    item2.setImage( Graphics.getImage( Fixture.IMAGE_100x50 ) );
    // vertical padding defaults to 0
    assertEquals( 50, table.getItemHeight() );
  }

  public void testNeedsScrollBarWithoutColumn() {
    Display display = new Display();
    Shell shell = new Shell( display );
    Table table = new Table( shell, SWT.NONE );
    table.setSize( 200, 200 );
    assertFalse( table.needsHScrollBar() );
    assertFalse( table.needsVScrollBar() );
    TableItem item = new TableItem( table, SWT.NONE );
    item.setText( "Item" );
    assertFalse( table.needsHScrollBar() );
    assertFalse( table.needsVScrollBar() );
    item.setText( "Very very very very very long item text " );
    assertTrue( table.needsHScrollBar() );
    assertFalse( table.needsVScrollBar() );
    for( int i = 0; i < 100; i++ ) {
      new TableItem( table, SWT.NONE );
    }
    assertTrue( table.needsHScrollBar() );
    assertTrue( table.needsVScrollBar() );
    item.setText( "Item" );
    assertFalse( table.needsHScrollBar() );
    assertTrue( table.needsVScrollBar() );
  }

  public void testNeedsScrollBarWithColumn() {
    Display display = new Display();
    Shell shell = new Shell( display );
    Table table = new Table( shell, SWT.NONE );
    table.setSize( 200, 200 );
    TableColumn column = new TableColumn( table, SWT.LEFT );
    column.setWidth( 10 );
    assertFalse( table.needsHScrollBar() );
    assertFalse( table.needsVScrollBar() );
    column.setWidth( 220 );
    assertTrue( table.needsHScrollBar() );
    assertFalse( table.needsVScrollBar() );
    column.setWidth( 5 );
    TableItem item = new TableItem( table, SWT.NONE );
    item.setText( "Very very very very very long item text " );
    assertFalse( table.needsHScrollBar() );
    assertFalse( table.needsVScrollBar() );
  }

  public void testHasScrollBar_NO_SCROLL() {
    Display display = new Display();
    Shell shell = new Shell( display );
    Table table = new Table( shell, SWT.NO_SCROLL );
    table.setSize( 200, 200 );
    assertFalse( table.hasVScrollBar() );
    assertFalse( table.hasHScrollBar() );
    TableColumn column = new TableColumn( table, SWT.LEFT );
    column.setWidth( 220 );
    assertFalse( table.hasVScrollBar() );
    assertFalse( table.hasHScrollBar() );
  }

  public void testGetScrollBarWidth() {
    Display display = new Display();
    Shell shell = new Shell( display );
    Table table = new Table( shell, SWT.NONE );
    table.setSize( 10, 10 );
    TableColumn column = new TableColumn( table, SWT.LEFT );
    column.setWidth( 20 );
    for( int i = 0; i < 10; i++ ) {
      new TableItem( table, SWT.NONE );
    }
    assertTrue( table.getVScrollBarWidth() > 0 );
    assertTrue( table.getHScrollBarHeight() > 0 );
    Table noScrollTable = new Table( shell, SWT.NO_SCROLL );
    noScrollTable.setSize( 200, 200 );
    assertEquals( 0, noScrollTable.getVScrollBarWidth() );
    assertEquals( 0, noScrollTable.getHScrollBarHeight() );
  }

  public void testUpdateScrollBarOnColumnChange() {
    Display display = new Display();
    Shell shell = new Shell( display );
    Table table = new Table( shell, SWT.NONE );
    table.setSize( 20, 20 );
    assertFalse( table.hasHScrollBar() );
    TableColumn column = new TableColumn( table, SWT.LEFT );
    column.setWidth( 25 );
    assertTrue( table.hasHScrollBar() );
    column.pack();
    assertFalse( table.hasHScrollBar() );
    column.setWidth( 25 );
    assertTrue( table.hasHScrollBar() );
    column.dispose();
    assertFalse( table.hasHScrollBar() );
  }

  public void testUpdateScrollBarOnItemsChange() {
    Display display = new Display();
    Shell shell = new Shell( display );
    Table table = new Table( shell, SWT.NONE );
    table.setSize( 20, 20 );
    assertFalse( table.hasVScrollBar() );
    for( int i = 0; i < 20; i++ ) {
      new TableItem( table, SWT.NONE );
    }
    assertTrue( table.hasVScrollBar() );
    table.removeAll();
    assertFalse( table.hasVScrollBar() );
  }

  public void testUpdateScrollBarOnResize() {
    Fixture.fakePhase( PhaseId.PROCESS_ACTION );
    Display display = new Display();
    Shell shell = new Shell( display );
    Table table = new Table( shell, SWT.NONE );
    table.setSize( 20, 20 );
    TableColumn column = new TableColumn( table, SWT.LEFT );
    column.setWidth( 25 );
    assertTrue( table.hasHScrollBar() );
    table.setSize( 30, 30 );
    assertFalse( table.hasHScrollBar() );
  }

  public void testUpdateScrollBarOnItemWidthChange() {
    Display display = new Display();
    Shell shell = new Shell( display );
    Table table = new Table( shell, SWT.NONE );
    table.setSize( 60, 60 );
    TableItem item = new TableItem( table, SWT.NONE );
    assertFalse( table.hasHScrollBar() );
    item.setText( "Very long long long long long long long long text" );
    assertTrue( table.hasHScrollBar() );
    item.setText( "" );
    assertFalse( table.hasHScrollBar() );
    Image image = Graphics.getImage( Fixture.IMAGE_100x50 );
    item.setImage( image );
    assertTrue( table.hasHScrollBar() );
    item.setImage( ( Image )null );
    assertFalse( table.hasHScrollBar() );
    item.setText( "Very long long long long long long long long text" );
    item.setImage( image );
    table.clearAll();
    assertFalse( table.hasHScrollBar() );
    // change font
    item.setText( "short" );
    assertFalse( table.hasHScrollBar() );
    Font bigFont = Graphics.getFont( "Helvetica", 50, SWT.BOLD );
    item.setFont( bigFont );
    assertTrue( table.hasHScrollBar() );
    item.setFont( null );
    assertFalse( table.hasHScrollBar() );
    table.setFont( bigFont );
    assertTrue( table.hasHScrollBar() );
  }

  public void testUpdateScrollBarOnHeaderVisibleChange() {
    Fixture.fakePhase( PhaseId.PROCESS_ACTION );
    Display display = new Display();
    Shell shell = new Shell( display );
    Table table = new Table( shell, SWT.NONE );
    int itemCount = 5;
    for( int i = 0; i < itemCount ; i++ ) {
      TableItem item = new TableItem( table, SWT.NONE );
      item.setText( "Item" );
    }
    table.setSize( 100, itemCount * table.getItemHeight() + 4 );
    assertFalse( table.hasVScrollBar() );
    table.setHeaderVisible( true );
    assertTrue( table.hasVScrollBar() );
  }

  public void testUpdateScrollBarOnVirtualItemCountChange() {
    Fixture.fakePhase( PhaseId.PROCESS_ACTION );
    Display display = new Display();
    Shell shell = new Shell( display );
    Table table = new Table( shell, SWT.VIRTUAL );
    int itemCount = 5;
    table.setSize( 100, itemCount * table.getItemHeight() + 4 );
    table.setItemCount( itemCount );
    assertFalse( table.hasVScrollBar() );
    table.setItemCount( itemCount * 2 );
    assertTrue( table.hasVScrollBar() );
  }

  public void testUpdateScrollBarItemWidthChangeWithColumn() {
    Display display = new Display();
    Shell shell = new Shell( display );
    Table table = new Table( shell, SWT.NONE );
    table.setSize( 20, 20 );
    TableColumn column = new TableColumn( table, SWT.LEFT );
    column.setWidth( 10 );
    TableItem item = new TableItem( table, SWT.NONE );
    assertFalse( table.hasHScrollBar() );
    item.setText( "Very long long long long long long long long text" );
    assertFalse( table.hasHScrollBar() );
    Image image = Graphics.getImage( Fixture.IMAGE_100x50 );
    item.setImage( image );
    assertFalse( table.hasHScrollBar() );
  }

  public void testUpdateScrollBarWithInterDependencyHFirst() {
    Fixture.fakePhase( PhaseId.PROCESS_ACTION );
    Display display = new Display();
    Shell shell = new Shell( display );
    Table table = new Table( shell, SWT.NONE );
    TableColumn column = new TableColumn( table, SWT.LEFT );
    column.setWidth( 20 );
    new TableItem( table, SWT.NONE );
    table.setSize( 30, table.getItemHeight() + 4 );
    assertFalse( table.needsVScrollBar() );
    assertFalse( table.needsHScrollBar() );
    assertFalse( table.hasHScrollBar() );
    assertFalse( table.hasVScrollBar() );
    column.setWidth( 40 );
    assertTrue( table.hasHScrollBar() );
    assertTrue( table.hasVScrollBar() );
  }

  public void testUpdateScrollBarWithInterDependencyVFirst() {
    Fixture.fakePhase( PhaseId.PROCESS_ACTION );
    Display display = new Display();
    Shell shell = new Shell( display );
    Table table = new Table( shell, SWT.NONE );
    TableColumn column = new TableColumn( table, SWT.LEFT );
    column.setWidth( 26 );
    table.setSize( 30, 30 );
    assertFalse( table.hasHScrollBar() );
    assertFalse( table.hasVScrollBar() );
    for( int i = 0; i < 10; i++ ) {
      new TableItem( table, SWT.NONE );
    }
    assertTrue( table.hasHScrollBar() );
    assertTrue( table.hasVScrollBar() );
  }

  public void testGetMeasureItemWithoutColumnsVirtual() {
    Fixture.fakePhase( PhaseId.PROCESS_ACTION );
    final String[] data = new String[ 1000 ];
    for( int i = 0; i < data.length; i++ ) {
      data[ i ] = "";
    }
    Listener setDataListener = new Listener() {
      public void handleEvent( final Event event ) {
        TableItem item = ( TableItem )event.item;
        int index = item.getParent().indexOf( item );
        item.setText( data[ index ] );
      }
    };
    Display display = new Display();
    Shell shell = new Shell( display );
    shell.setSize( 100, 100 );
    Table table = new Table( shell, SWT.VIRTUAL );
    table.addListener( SWT.SetData, setDataListener );
    table.setItemCount( data.length );
    table.setSize( 90, 90 );
    shell.open();
    int resolvedItemCount;
    TableItem measureItem;
    // Test with items that all have the same width
    resolvedItemCount = countResolvedItems( table );
    measureItem = table.getMeasureItem();
    assertNotNull( measureItem );
    assertEquals( resolvedItemCount, countResolvedItems( table ) );
    // Test with items that have ascending length
    data[ 0 ] = "a";
    for( int i = 1; i < data.length; i++ ) {
      data[ i ] = data[ i - 1 ] + "a";
    }
    table.getItem( 100 ).getText(); // resolves item
    resolvedItemCount = countResolvedItems( table );
    measureItem = table.getMeasureItem();
    int measureItemIndex = measureItem.getParent().indexOf( measureItem );
    assertEquals( 100, measureItemIndex );
    assertEquals( resolvedItemCount, countResolvedItems( table ) );
  }

  public void testIndexOf() {
    Fixture.fakePhase( PhaseId.PROCESS_ACTION );
    Display display = new Display();
    Shell shell = new Shell( display );
    shell.setSize( 100, 100 );
    Table table = new Table( shell, SWT.NONE );
    TableItem item1 = new TableItem( table, SWT.NONE );
    assertEquals( 0, table.indexOf( item1 ) );
    TableItem item0 = new TableItem( table, SWT.NONE, 0 );
    assertEquals( 0, table.indexOf( item0 ) );
    assertEquals( 1, table.indexOf( item1 ) );
    table.removeAll();
    new TableItem( table, SWT.NONE );
    new TableItem( table, SWT.NONE );
    TableItem itemBefore = new TableItem( table, SWT.NONE );
    // -> this is the place for 'newItem'
    TableItem itemAfter = new TableItem( table, SWT.NONE );
    new TableItem( table, SWT.NONE );
    TableItem newItem = new TableItem( table, SWT.NONE, 3 );
    assertEquals( 3, table.indexOf( newItem ) );
    assertEquals( 2, table.indexOf( itemBefore ) );
    assertEquals( 4, table.indexOf( itemAfter ) );
    newItem.dispose();
    assertEquals( -1, table.indexOf( newItem ) );
    assertEquals( 2, table.indexOf( itemBefore ) );
    assertEquals( 3, table.indexOf( itemAfter ) );
    table.remove( table.getItemCount() - 1 );
    assertEquals( 3, table.indexOf( itemAfter ) );
  }

  public void testIndexOfVirtual() {
    Fixture.fakePhase( PhaseId.PROCESS_ACTION );
    Display display = new Display();
    Shell shell = new Shell( display );
    shell.setSize( 100, 100 );
    Table table = new Table( shell, SWT.VIRTUAL );
    table.setItemCount( 10 );
    TableItem item = table.getItem( 0 );
    assertEquals( 0, table.indexOf( item ) );
    item = table.getItem( 5 );
    assertEquals( 5, table.indexOf( item ) );
    item = table.getItem( 9 );
    assertEquals( 9, table.indexOf( item ) );
    // ensure that updating null-items does not throw NPE
    table.remove( 5 );
  }

  public void testShowColumn() {
    Fixture.fakePhase( PhaseId.PROCESS_ACTION );
    Display display = new Display();
    Shell shell = new Shell( display );
    shell.setSize( 800, 600 );
    Table table = new Table( shell, SWT.NONE );
    table.setSize( 300, 100 );
    for( int i = 0; i < 10; i++ ) {
      TableColumn column = new TableColumn( table, SWT.NONE );
      column.setWidth( 50 );
    }
    for( int i = 0; i < 10; i++ ) {
      new TableItem( table, SWT.NONE );
    }
    ITableAdapter adapter
      = ( ITableAdapter )table.getAdapter( ITableAdapter.class );
    assertEquals( 0, adapter.getLeftOffset() );
    table.showColumn( table.getColumn( 8 ) );
    assertEquals( 166, adapter.getLeftOffset() );
    table.showColumn( table.getColumn( 1 ) );
    assertEquals( 50, adapter.getLeftOffset() );
    table.showColumn( table.getColumn( 3 ) );
    assertEquals( 50, adapter.getLeftOffset() );
    try {
      table.showColumn( null );
      fail( "Null argument not allowed" );
    } catch( IllegalArgumentException e ) {
    }
    TableColumn column = table.getColumn( 3 );
    column.dispose();
    try {
      table.showColumn( column );
      fail( "Disposed column not allowed as argument" );
    } catch( IllegalArgumentException e ) {
    }
    Table table1 = new Table( shell, SWT.NONE );
    column = new TableColumn( table1, SWT.NONE );
    table.showColumn( column );
    assertEquals( 50, adapter.getLeftOffset() );
    table.setColumnOrder( new int[] { 8, 7, 0, 1, 2, 3, 6, 5, 4 } );
    table.showColumn( table.getColumn( 8 ) );
    assertEquals( 0, adapter.getLeftOffset() );
    table.showColumn( table.getColumn( 5 ) );
    assertEquals( 116, adapter.getLeftOffset() );
  }

  public void testScrollBars() {
    Display display = new Display();
    Shell shell = new Shell( display );
    Table table = new Table( shell, SWT.NONE );
    assertNotNull( table.getHorizontalBar() );
    assertNotNull( table.getVerticalBar() );
    table = new Table( shell, SWT.NO_SCROLL );
    assertNull( table.getHorizontalBar() );
    assertNull( table.getVerticalBar() );
  }

  // 288634: [Table] TableItem images are not displayed if columns are created
  // after setInput
  // https://bugs.eclipse.org/bugs/show_bug.cgi?id=288634
  public void testUpdateColumnImageCount() {
    Display display = new Display();
    Shell shell = new Shell( display );
    shell.setSize( 100, 100 );
    Table table = new Table( shell, SWT.NONE );
    TableItem item = new TableItem( table, SWT.NONE );
    item.setText( new String[] { "col 1", "col 2", "col 3" } );
    Image image = Graphics.getImage( Fixture.IMAGE1 );
    item.setImage( new Image[] { image, null, image } );
    assertTrue( table.hasColumnImages( 0 ) );
    TableColumn col1 = new TableColumn( table, SWT.NONE );
    col1.setText( "header 1" );
    col1.setWidth( 30 );
    TableColumn col2 = new TableColumn( table, SWT.NONE );
    col2.setText( "header 2" );
    col2.setWidth( 30 );
    TableColumn col3 = new TableColumn( table, SWT.NONE );
    col3.setText( "header 3" );
    col3.setWidth( 30 );
    assertTrue( table.hasColumnImages( 0 ) );
    assertFalse( table.hasColumnImages( 1 ) );
    assertFalse( table.hasColumnImages( 2 ) );
  }

  // 239024: {TableViewer] Missing text due to TableViewerColumn
  // https://bugs.eclipse.org/bugs/show_bug.cgi?id=239024
  public void testGetItemsPreferredWidth() {
    Display display = new Display();
    Shell shell = new Shell( display );
    shell.setSize( 100, 100 );
    Table table = new Table( shell, SWT.NONE );
    new TableColumn( table, SWT.NONE );
    new TableColumn( table, SWT.NONE );
    assertEquals( 12, table.getItemsPreferredWidth( 0 ) );
    assertEquals( 12, table.getItemsPreferredWidth( 1 ) );

    table = new Table( shell, SWT.CHECK );
    new TableColumn( table, SWT.NONE );
    new TableColumn( table, SWT.NONE );
    // 33 = 21 ( check width ) + 12
    assertEquals( 33, table.getItemsPreferredWidth( 0 ) );
    assertEquals( 12, table.getItemsPreferredWidth( 1 ) );
  }
  
  public void testRemoveArrayDuplicates() {
    Display display = new Display();
    Shell shell = new Shell( display );
    Table table = new Table( shell, SWT.NONE );
    int number = 5;
    TableItem[] items = new TableItem[ number ];
    for( int i = 0; i < number; i++ ) {
      items[ i ] = new TableItem( table, 0 );
    }
    assertEquals( 5, table.getItemCount() );
    table.remove( new int[]{
      1, 1
    } );
    assertEquals( 4, table.getItemCount() );
    table.remove( new int[]{
      0, 2, 1, 1
    } );
    assertEquals( 1, table.getItemCount() );
  }
  
  private static boolean find( final int element, final int[] array ) {
    boolean result = false;
    for( int i = 0; i < array.length; i++ ) {
      if( element == array[ i ] ) {
        result = true;
      }
    }
    return result;
  }

  private static int countResolvedItems( final Table table ) {
    Object adapter = table.getAdapter( ITableAdapter.class );
    ITableAdapter tableAdapter = ( ITableAdapter )adapter;
    int result = 0;
    TableItem[] createdItems = tableAdapter.getCreatedItems();
    for( int i = 0; i < createdItems.length; i++ ) {
      int index = table.indexOf( createdItems[ i ] );
      if( !tableAdapter.isItemVirtual( index ) ) {
        result++;
      }
    }
    return result;
  }

  private static void clearColumns( final Table table ) {
    while( table.getColumnCount() > 0 ) {
      table.getColumn( 0 ).dispose();
    }
  }
}
