/*******************************************************************************
 * Copyright (c) 2007, 2010 Innoopract Informationssysteme GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Innoopract Informationssysteme GmbH - initial API and implementation
 *     EclipseSource - ongoing development
 ******************************************************************************/
package org.eclipse.swt.internal.widgets.tableitemkit;

import java.io.IOException;

import junit.framework.TestCase;

import org.eclipse.rwt.Fixture;
import org.eclipse.rwt.graphics.Graphics;
import org.eclipse.rwt.internal.lifecycle.DisplayUtil;
import org.eclipse.rwt.internal.lifecycle.JSConst;
import org.eclipse.rwt.internal.service.RequestParams;
import org.eclipse.rwt.lifecycle.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.widgets.ITableAdapter;
import org.eclipse.swt.internal.widgets.Props;
import org.eclipse.swt.widgets.*;

public class TableItemLCA_Test extends TestCase {

  protected void setUp() throws Exception {
    Fixture.setUp();
    Fixture.fakePhase( PhaseId.RENDER );
  }

  protected void tearDown() throws Exception {
    Fixture.tearDown();
  }

  public void testPreserveValues() {
    Display display = new Display();
    Shell shell = new Shell( display );
    Table table = new Table( shell, SWT.BORDER );
    new TableColumn( table, SWT.CENTER );
    new TableColumn( table, SWT.CENTER );
    new TableColumn( table, SWT.CENTER );
    TableItem item1 = new TableItem( table, SWT.NONE );
    TableItem item2 = new TableItem( table, SWT.NONE );
    Fixture.markInitialized( display );
    Fixture.preserveWidgets();
    IWidgetAdapter adapter = WidgetUtil.getAdapter( item1 );
//    Object top = adapter.getPreserved( TableItemLCA.PROP_TOP );
//    assertEquals( new Integer( item1.getBounds().y ), top );
    Object checked = adapter.getPreserved( TableItemLCA.PROP_CHECKED );
    assertNull( checked );
    Object grayed = adapter.getPreserved( TableItemLCA.PROP_GRAYED );
    assertNull( grayed );
    Object index = adapter.getPreserved( TableItemLCA.PROP_INDEX );
    assertEquals( new Integer( 0 ), index );
    Object selected = adapter.getPreserved( TableItemLCA.PROP_SELECTED );
    assertEquals( Boolean.FALSE, selected );
    String[] texts1 = TableItemLCA.getTexts( item1 );
    String[] texts2
      = ( String[] )adapter.getPreserved( TableItemLCA.PROP_TEXTS );
    assertEquals( texts1[ 0 ], texts2[ 0 ] );
    assertEquals( texts1[ 1 ], texts2[ 1 ] );
    assertEquals( texts1[ 2 ], texts2[ 2 ] );
    Image[] images1 = TableItemLCA.getImages( item1 );
    Image[] images2
      = ( Image[] )adapter.getPreserved( TableItemLCA.PROP_IMAGES );
    assertEquals( images1[ 0 ], images2[ 0 ] );
    assertEquals( images1[ 1 ], images2[ 1 ] );
    assertEquals( images1[ 2 ], images2[ 2 ] );
    assertNull( adapter.getPreserved( TableItemLCA.PROP_BACKGROUND ) );
    assertNull( adapter.getPreserved( TableItemLCA.PROP_FOREGROUND ) );
    assertNull( adapter.getPreserved( TableItemLCA.PROP_FONT ) );
    Color[] preservedCellBackgrounds
      = ( Color[] )adapter.getPreserved( TableItemLCA.PROP_CELL_BACKGROUNDS );
    assertNull( preservedCellBackgrounds[ 0 ] );
    assertNull( preservedCellBackgrounds[ 1 ] );
    assertNull( preservedCellBackgrounds[ 2 ] );
    Color[] preservedCellForegrounds
      = ( Color[] )adapter.getPreserved( TableItemLCA.PROP_CELL_FOREGROUNDS );
    assertNull( preservedCellForegrounds[ 0 ] );
    assertNull( preservedCellForegrounds[ 1 ] );
    assertNull( preservedCellForegrounds[ 2 ] );
    Font[] preservedCellFonts =
      ( Font[] )adapter.getPreserved( TableItemLCA.PROP_CELL_FONTS );
    assertNull( preservedCellFonts[ 0 ] );
    assertNull( preservedCellFonts[ 1 ] );
    assertNull( preservedCellFonts[ 2 ] );
    Fixture.clearPreserved();
    item1.setText( 0, "item11" );
    item1.setText( 1, "item12" );
    item1.setText( 2, "item13" );
    Font font1 = Graphics.getFont( "font1", 10, 1 );
    item1.setFont( 0, font1 );
    Font font2 = Graphics.getFont( "font2", 8, 1 );
    item1.setFont( 1, font2 );
    Font font3 = Graphics.getFont( "font3", 6, 1 );
    item1.setFont( 2, font3 );
    Image image1 = Graphics.getImage( Fixture.IMAGE1 );
    Image image2 = Graphics.getImage( Fixture.IMAGE2 );
    Image image3 = Graphics.getImage( Fixture.IMAGE3 );
    item1.setImage( new Image[]{
      image1, image2, image3
    } );
    Color background1 = Graphics.getColor( 234, 230, 54 );
    item1.setBackground( 0, background1 );
    Color background2 = Graphics.getColor( 145, 222, 134 );
    item1.setBackground( 1, background2 );
    Color background3 = Graphics.getColor( 143, 134, 34 );
    item1.setBackground( 2, background3 );
    Color foreground1 = Graphics.getColor( 77, 77, 54 );
    item1.setForeground( 0, foreground1 );
    Color foreground2 = Graphics.getColor( 156, 45, 134 );
    item1.setForeground( 1, foreground2 );
    Color foreground3 = Graphics.getColor( 88, 134, 34 );
    item1.setForeground( 2, foreground3 );
    table.setSelection( 0 );
    ITableAdapter tableAdapter
      = ( ITableAdapter )table.getAdapter( ITableAdapter.class );
    tableAdapter.setFocusIndex( 0 );
    Fixture.preserveWidgets();
    adapter = WidgetUtil.getAdapter( item1 );
//    top = adapter.getPreserved( TableItemLCA.PROP_TOP );
    checked = adapter.getPreserved( TableItemLCA.PROP_CHECKED );
    grayed = adapter.getPreserved( TableItemLCA.PROP_GRAYED );
    index = adapter.getPreserved( TableItemLCA.PROP_INDEX );
    selected = adapter.getPreserved( TableItemLCA.PROP_SELECTED );
    assertNull( checked );
    assertNull( grayed );
    assertEquals( Boolean.TRUE, selected );
    assertEquals( new Integer( 0 ), index );
    texts2 = ( String[] )adapter.getPreserved( TableItemLCA.PROP_TEXTS );
    assertEquals( "item11", texts2[ 0 ] );
    assertEquals( "item12", texts2[ 1 ] );
    assertEquals( "item13", texts2[ 2 ] );
    images2 = ( Image[] )adapter.getPreserved( TableItemLCA.PROP_IMAGES );
    assertEquals( image1, images2[ 0 ] );
    assertEquals( image2, images2[ 1 ] );
    assertEquals( image3, images2[ 2 ] );
    preservedCellFonts
      = ( Font[] )adapter.getPreserved( TableItemLCA.PROP_CELL_FONTS );
    assertEquals( font1, preservedCellFonts[ 0 ] );
    assertEquals( font2, preservedCellFonts[ 1 ] );
    assertEquals( font3, preservedCellFonts[ 2 ] );
    preservedCellBackgrounds
      = ( Color[] )adapter.getPreserved( TableItemLCA.PROP_CELL_BACKGROUNDS );
    assertEquals( background1, preservedCellBackgrounds[ 0 ] );
    assertEquals( background2, preservedCellBackgrounds[ 1 ] );
    assertEquals( background3, preservedCellBackgrounds[ 2 ] );
    preservedCellForegrounds
      = ( Color[] )adapter.getPreserved( TableItemLCA.PROP_CELL_FOREGROUNDS );
    assertEquals( foreground1, preservedCellForegrounds[ 0 ] );
    assertEquals( foreground2, preservedCellForegrounds[ 1 ] );
    assertEquals( foreground3, preservedCellForegrounds[ 2 ] );
    Fixture.clearPreserved();
    // text
    Fixture.preserveWidgets();
    adapter = WidgetUtil.getAdapter( item2 );
    assertEquals( "", adapter.getPreserved( Props.TEXT ) );
    Fixture.clearPreserved();
    item2.setText( "some text" );
    Fixture.preserveWidgets();
    adapter = WidgetUtil.getAdapter( item2 );
    assertEquals( "some text", adapter.getPreserved( Props.TEXT ) );
    Fixture.clearPreserved();
    // image
    Fixture.preserveWidgets();
    adapter = WidgetUtil.getAdapter( item2 );
    assertEquals( null, adapter.getPreserved( Props.IMAGE ) );
    Fixture.clearPreserved();
    Image image = Graphics.getImage( Fixture.IMAGE1 );
    item2.setImage( image );
    Fixture.preserveWidgets();
    adapter = WidgetUtil.getAdapter( item2 );
    assertSame( image, adapter.getPreserved( Props.IMAGE ) );
    Fixture.clearPreserved();
    display.dispose();
  }

  public void testFontIsEffectivelyPreserved() throws IOException {
    Display display = new Display();
    Shell shell = new Shell( display );
    Table table = new Table( shell, SWT.BORDER );
    TableItem item = new TableItem( table, SWT.NONE );
    Fixture.markInitialized( item );
    Fixture.fakeResponseWriter();
    item.setFont( new Font( display, "Times", 12, SWT.BOLD ) );
    TableItemLCA lca = new TableItemLCA();
    lca.preserveValues( item );
    lca.renderChanges( item );
    String expected = "w.setFont(";
    String markup = Fixture.getAllMarkup();
    assertFalse( markup.indexOf( expected ) != -1 );
    display.dispose();
  }

  public void testCheckPreserveValues() {
    Display display = new Display();
    Shell shell = new Shell( display );
    Table table = new Table( shell, SWT.CHECK );
    TableItem item = new TableItem( table, SWT.NONE );
    Fixture.markInitialized( display );
    Fixture.preserveWidgets();
    IWidgetAdapter adapter = WidgetUtil.getAdapter( item );
    Object checked = adapter.getPreserved( TableItemLCA.PROP_CHECKED );
    assertEquals( Boolean.FALSE, checked );
    Object grayed = adapter.getPreserved( TableItemLCA.PROP_GRAYED );
    assertEquals( Boolean.FALSE, grayed );
    Fixture.clearPreserved();
    item.setChecked( true );
    item.setGrayed( true );
    Fixture.preserveWidgets();
    adapter = WidgetUtil.getAdapter( item );
    checked = adapter.getPreserved( TableItemLCA.PROP_CHECKED );
    grayed = adapter.getPreserved( TableItemLCA.PROP_GRAYED );
    assertEquals( Boolean.TRUE, checked );
    assertEquals( Boolean.TRUE, grayed );
    Fixture.clearPreserved();
    display.dispose();
  }

  public void testItemTextWithoutColumn() throws IOException {
    Display display = new Display();
    Shell shell = new Shell( display );
    Table table = new Table( shell, SWT.NONE );
    TableItem item = new TableItem( table, SWT.NONE );
    // Ensure that even though there are no columns, the first text of an item
    // will be rendered
    Fixture.fakeResponseWriter();
    TableItemLCA tableItemLCA = new TableItemLCA();
    Fixture.markInitialized( item );
    tableItemLCA.preserveValues( item );
    item.setText( "newText" );
    tableItemLCA.renderChanges( item );
    String expected = "w.setTexts( [ \"newText\" ] )";
    assertTrue( Fixture.getAllMarkup().indexOf( expected ) != -1 );
  }

  public void testDisposeSelected() {
    final boolean[] executed = { false };
    Display display = new Display();
    Shell shell = new Shell( display );
    final Table table = new Table( shell, SWT.CHECK );
    new TableItem( table, SWT.NONE );
    new TableItem( table, SWT.NONE );
    new TableItem( table, SWT.NONE );
    table.setSelection( 2 );
    Button button = new Button( shell, SWT.PUSH );
    button.addSelectionListener( new SelectionAdapter() {
      public void widgetSelected( final SelectionEvent e ) {
        table.remove( 1, 2 );
        executed[ 0 ] = true;
      }
    } );
    Fixture.fakeNewRequest();
    String displayId = DisplayUtil.getId( display );
    Fixture.fakeRequestParam( RequestParams.UIROOT, displayId );
    String buttonId = WidgetUtil.getId( button );
    Fixture.fakeRequestParam( JSConst.EVENT_WIDGET_SELECTED, buttonId );
    Fixture.executeLifeCycleFromServerThread( );
    assertTrue( executed[ 0 ] );
  }

  public void testDispose() throws IOException {
    Display display = new Display();
    Shell shell = new Shell( display );
    final Table table = new Table( shell, SWT.CHECK );
    TableItem itemOnlyDisposed = new TableItem( table, SWT.NONE );
    TableItem itemWithTableDisposed = new TableItem( table, SWT.NONE );
    Fixture.markInitialized( table );
    Fixture.markInitialized( itemOnlyDisposed );
    Fixture.markInitialized( itemWithTableDisposed );
    // Test that when a single items is disposed, its JavaScript dispose
    // function is called
    itemOnlyDisposed.dispose();
    AbstractWidgetLCA lca = WidgetUtil.getLCA( itemWithTableDisposed );
    Fixture.fakeResponseWriter();
    lca.renderDispose( itemOnlyDisposed );
    String expected = "var wm = org.eclipse.swt.WidgetManager.getInstance();"
                      + "var w = wm.findWidgetById( \"w3\" );"
                      + "w.dispose();";
    assertEquals( expected, Fixture.getAllMarkup() );
    // Test that when the whole Tables is dipsosed of, the TableItems dispose
    // function is *not* called
    table.dispose();
    lca = WidgetUtil.getLCA( itemWithTableDisposed );
    Fixture.fakeResponseWriter();
    lca.renderDispose( itemWithTableDisposed );
    assertEquals( "", Fixture.getAllMarkup() );
    assertTrue( itemWithTableDisposed.isDisposed() );
  }

  public void testWriteChangesForVirtualItem() throws IOException {
    Display display = new Display();
    Shell shell = new Shell( display );
    Table table = new Table( shell, SWT.VIRTUAL );
    table.setItemCount( 100 );
    // Ensure that nothing is written for an item that is virtual and whose
    // cached was false and remains unchanged while processing the life cycle
    TableItem item = table.getItem( 0 );
    table.clear( 0 );
    TableItemLCA lca = new TableItemLCA();
    Fixture.fakeResponseWriter();
    Fixture.markInitialized( item );
    // Ensure that nothing else than the 'checked' property gets preserved
    lca.preserveValues( item );
    IWidgetAdapter itemAdapter = WidgetUtil.getAdapter( item );
    assertEquals( Boolean.FALSE,
                  itemAdapter.getPreserved( TableItemLCA.PROP_CACHED ) );
    assertNull( itemAdapter.getPreserved( TableItemLCA.PROP_TEXTS ) );
    assertNull( itemAdapter.getPreserved( TableItemLCA.PROP_IMAGES ) );
    assertNull( itemAdapter.getPreserved( TableItemLCA.PROP_CHECKED ) );
    // ... and no markup is generated for a uncached item that was already
    // uncached when entering the life cycle
    lca.renderChanges( item );
    assertEquals( "", Fixture.getAllMarkup() );
  }

  public void testCheckAndGrayedAccess() throws IOException {
    final String[] lcaMethod = { "" };
    Display display = new Display();
    Shell shell = new Shell( display );
    Table table = new Table( shell, SWT.NONE );
    TableItem item = new TableItem( table, SWT.NONE ) {
      public boolean getChecked() {
        fail(   lcaMethod[ 0 ]
              + ": Must not call getChecked() from LCA when no CHECK style" );
        return false;
      }
      public boolean getGrayed() {
        fail(   lcaMethod[ 0 ]
               + ": Must not call getGrayed() from LCA when no CHECK style" );
        return false;
      }
    };
    Fixture.fakeResponseWriter();
    TableItemLCA lca = new TableItemLCA();
    lcaMethod[ 0 ] = "preserveValues";
    lca.preserveValues( item );
    lcaMethod[ 0 ] = "readData";
    lca.readData( item );
    lcaMethod[ 0 ] = "renderInitialization";
    lca.renderInitialization( item );
    lcaMethod[ 0 ] = "renderChanges";
    lca.renderChanges( item );
    lcaMethod[ 0 ] = "renderDispose";
    lca.renderDispose( item );
  }

  public void testEscape() throws IOException {
    Display display = new Display();
    Shell shell = new Shell( display );
    Table table = new Table( shell, SWT.NONE );
    TableItem item = new TableItem( table, SWT.NONE );
    Fixture.fakeResponseWriter();
    TableItemLCA tableItemLCA = new TableItemLCA();
    Fixture.markInitialized( item );
    tableItemLCA.preserveValues( item );
    item.setText( "char test: &<>.,'\"&lt;" );
    tableItemLCA.renderChanges( item );
    String expected
      = "w.setTexts( [ \"char test: &#038;&#060;&#062;.,'&#034;&#038;lt;\" ] )";
    String result = Fixture.getAllMarkup();
    assertTrue( result.indexOf( expected ) != -1 );
  }

  public void testDynamicColumns() {
    Display display = new Display();
    Shell shell = new Shell( display );
    Table table = new Table( shell, SWT.NONE );
    new TableColumn( table, SWT.NONE );
    TableItem item = new TableItem( table, SWT.NONE );
    item.setBackground( 0, display.getSystemColor( SWT.COLOR_BLACK ) );
    // Create another column after setting a cell background
    // See https://bugs.eclipse.org/bugs/show_bug.cgi?id=277089
    new TableColumn( table, SWT.NONE );
    Fixture.markInitialized( display );
    Fixture.preserveWidgets();
  }
}
