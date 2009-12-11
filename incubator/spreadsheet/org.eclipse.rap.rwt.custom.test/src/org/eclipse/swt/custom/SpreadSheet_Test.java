// Created on 08.08.2009
package org.eclipse.swt.custom;


import junit.framework.TestCase;

import org.eclipse.rwt.Fixture;
import org.eclipse.rwt.lifecycle.PhaseId;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.*;

/*
 * TODO [fappel]: complete MouseDown + MouseUp handling
 * TODO [fappel]: check Display#map
 * TODO [fappel]: visibility of trimmings (header, slider)
 */
public class SpreadSheet_Test extends TestCase {
  private static final int DEFAULT_COLUMN_HEADER_HEIGHT
    = HeaderController.DEFAULT_COLUMN_HEADER_HEIGHT;
  private static final int DEFAULT_ROW_HEADER_WIDTH 
    = HeaderController.DEFAULT_ROW_HEADER_WIDTH;
  private static final int SLIDER_BREADTH = SliderController.SLIDER_BREADTH;
  private static final String TEST_TEXT = "TextToSet";
  private static final int ROW_TO_SELECT = 2;
  private static final int COLUMN_TO_SELECT = 4;

  private Display display;
  private Shell shell;

  public void testTextSetterAndGetter() {
    SpreadSheet sheet = new SpreadSheet( shell, SWT.NONE );

    try {
      sheet.setText( null, ROW_TO_SELECT, COLUMN_TO_SELECT );
      fail( "Parameter text must not be null." );
    } catch( final IllegalArgumentException iae ) {
      // expected
    }
    try {
      sheet.setText( TEST_TEXT, -1, COLUMN_TO_SELECT );
      fail( "Parameter rowIndex must not be negative." );
    } catch( final IllegalArgumentException iae ) {
      // expected
    }
    try {
      sheet.setText( TEST_TEXT, ROW_TO_SELECT, -1 );
      fail( "Parameter columnIndex must not be negative." );
    } catch( final IllegalArgumentException iae ) {
      // expected
    }
    try {
      sheet.getText( -1, COLUMN_TO_SELECT );
      fail( "Parameter columnIndex must not be negative." );
    } catch( final IllegalArgumentException iae ) {
      // expected
    }
    try {
      sheet.getText( ROW_TO_SELECT, -1 );
      fail( "Parameter rowIndex must not be negative." );
    } catch( final IllegalArgumentException iae ) {
      // expected
    }

    assertEquals( "", sheet.getText( ROW_TO_SELECT, COLUMN_TO_SELECT ) );
    sheet.setText( TEST_TEXT, ROW_TO_SELECT, COLUMN_TO_SELECT );
    assertEquals( TEST_TEXT,
                  sheet.getText( ROW_TO_SELECT, COLUMN_TO_SELECT ) );
    assertEquals( "", sheet.getText( ROW_TO_SELECT, COLUMN_TO_SELECT + 1 ) );
    sheet.setText( "", ROW_TO_SELECT, COLUMN_TO_SELECT );
    assertEquals( "", sheet.getText( ROW_TO_SELECT, COLUMN_TO_SELECT ) );

    sheet.setText( TEST_TEXT, ROW_TO_SELECT, COLUMN_TO_SELECT );
    Fixture.fakePhase( PhaseId.PREPARE_UI_ROOT );
    shell.layout( true, true );
    CellPosition position
      = new CellPosition( ROW_TO_SELECT, COLUMN_TO_SELECT );
    Label control = findCellControl( sheet, position );
    String text = control.getText();
    assertEquals( TEST_TEXT, text );
    
  }
  
  public void testSlider() {
    SpreadSheet spreadSheet = new SpreadSheet( shell, SWT.NONE );
    Fixture.fakePhase( PhaseId.PREPARE_UI_ROOT );
    shell.layout();
    
    Slider hScroll = null;
    Slider vScroll = null;
    Control[] children = spreadSheet.getChildren();
    for( int i = 0; i < children.length; i++ ) {
      if( children[ i ] instanceof Slider ) {
        Slider slider = ( Slider )children[ i ];
        if( ( slider.getStyle() & SWT.H_SCROLL )  > 0 ) {
          assertNull( hScroll );
          hScroll = slider;
        } else {
          assertNull( vScroll );
          vScroll = slider;
        }
      }
    }
    assertNotNull( hScroll );
    assertNotNull( vScroll );
    Rectangle shellClientArea = shell.getClientArea();
    
    
    // slider positions
    assertEquals( 0, hScroll.getLocation().x );
    int yPosHScroll = shellClientArea.height - SLIDER_BREADTH;
    assertEquals( yPosHScroll, hScroll.getLocation().y );
    int widthHScroll = shellClientArea.width - SLIDER_BREADTH;
    assertEquals( widthHScroll, hScroll.getSize().x );
    assertEquals( SLIDER_BREADTH, hScroll.getSize().y );

    int xPosVScroll = shellClientArea.width - SLIDER_BREADTH;
    assertEquals( xPosVScroll, vScroll.getLocation().x );
    assertEquals( 0, vScroll.getLocation().y );
    assertEquals( SLIDER_BREADTH, vScroll.getSize().x );
    int heightVScroll = shellClientArea.height - SLIDER_BREADTH;
    assertEquals( heightVScroll, vScroll.getSize().y );

    
    // initial thumb sizes
    int hMax = hScroll.getThumb() + SliderController.PAGE_INCREMENT_OFFSET;
    assertEquals( hMax, hScroll.getMaximum() );
    assertEquals( 0, hScroll.getSelection() );
    int vMax = vScroll.getThumb() + SliderController.PAGE_INCREMENT_OFFSET;
    assertEquals( vMax, vScroll.getMaximum() );
    assertEquals( 0, vScroll.getSelection() );
    
    CellPosition position
      = new CellPosition( ROW_TO_SELECT, COLUMN_TO_SELECT );
    spreadSheet.setText( TEST_TEXT, position );
    Label cell = findCellControl( spreadSheet, position );
    assertSame( TEST_TEXT, cell.getText() );
    SpreadSheetData cellData = ( SpreadSheetData )cell.getLayoutData();
    assertEquals( position, cellData.getPosition() );
    
    
    // row- and column-offset
    CellPosition position_0_0 = new CellPosition( 0, 0 );
    Label cell_0_0 = findCellControl( spreadSheet, position_0_0 );
    assertNull( cell_0_0 );
    spreadSheet.setText( TEST_TEXT, position_0_0 );
    cell_0_0 = findCellControl( spreadSheet, position_0_0 );
    assertSame( TEST_TEXT, cell_0_0.getText() );
    
    spreadSheet.setColumnOffset( 1 );
    assertSame( "", cell_0_0.getText() );
    assertSame( TEST_TEXT, cell.getText() );
    assertEquals( 1, hScroll.getSelection() );
    
    spreadSheet.setColumnOffset( 0 );
    assertSame( TEST_TEXT, cell_0_0.getText() );
    assertSame( TEST_TEXT, cell.getText() );
    assertEquals( 0, hScroll.getSelection() );
    
    spreadSheet.setRowOffset( 1 );
    assertSame( "", cell_0_0.getText() );
    assertSame( TEST_TEXT, cell.getText() );
    assertEquals( 1, vScroll.getSelection() );
    
    spreadSheet.setRowOffset( 0 );
    assertSame( TEST_TEXT, cell_0_0.getText() );
    assertSame( TEST_TEXT, cell.getText() );
    assertEquals( 0, vScroll.getSelection() );
  }

  public void testClientArea() {
    SpreadSheet spreadSheet = new SpreadSheet( shell, SWT.NONE );
    Fixture.fakePhase( PhaseId.PREPARE_UI_ROOT );
    shell.layout();

    Rectangle shellClientArea = shell.getClientArea();
    Rectangle clientArea = spreadSheet.getClientArea();
    assertEquals( DEFAULT_ROW_HEADER_WIDTH, clientArea.x );
    assertEquals( DEFAULT_COLUMN_HEADER_HEIGHT, clientArea.y );
    int width =   shellClientArea.width 
                - SLIDER_BREADTH
                - DEFAULT_ROW_HEADER_WIDTH;
    assertEquals( width, clientArea.width );
    int height =    shellClientArea.height
                  - SLIDER_BREADTH
                  - DEFAULT_COLUMN_HEADER_HEIGHT;
    assertEquals( height, clientArea.height );
  }

  public void testMouseHandler() {
    SpreadSheet sheet = new SpreadSheet( shell, SWT.NONE );
    CellPosition position
      = new CellPosition( ROW_TO_SELECT, COLUMN_TO_SELECT );
    sheet.getCellEditorController().handleMouseDown( position );
    sheet.getCellEditorController().handleMouseUp( position );
    CellEditorController controller = sheet.getCellEditorController();
    Control control = controller.getControl();
    SpreadSheetData data = ( SpreadSheetData )control.getLayoutData();
    assertEquals( ROW_TO_SELECT, data.getRowIndex() );
    assertEquals( COLUMN_TO_SELECT, data.getColumnIndex() );
  }

  public void testCellEditorTextInAndOutput() {
    SpreadSheet sheet = new SpreadSheet( shell, SWT.NONE );
    shell.layout( true, true );

    CellEditorController controller = sheet.getCellEditorController();
    assertEquals( "", controller.getText() );

    sheet.setText( TEST_TEXT, ROW_TO_SELECT, COLUMN_TO_SELECT );
    CellPosition position
      = new CellPosition( ROW_TO_SELECT, COLUMN_TO_SELECT );
    Label cellControl = findCellControl( sheet, position );
    assertEquals( TEST_TEXT, cellControl.getText() );
    assertEquals( "", controller.getText() );
    sheet.getCellEditorController().handleMouseDown( position );
    sheet.getCellEditorController().handleMouseUp( position );
    assertEquals( TEST_TEXT, controller.getText() );

    controller.setText( "" );
    assertEquals( TEST_TEXT, sheet.getText( ROW_TO_SELECT, COLUMN_TO_SELECT ) );
    assertEquals( TEST_TEXT, cellControl.getText() );
    KeyEvent evt = new KeyEvent( controller.getControl(),
                                 KeyEvent.KEY_PRESSED );
    evt.keyCode = SWT.ARROW_DOWN;
    controller.handleKeyPressed( evt );
    assertEquals( "", sheet.getText( ROW_TO_SELECT, COLUMN_TO_SELECT ) );
    assertTrue( "", cellControl.isDisposed() );
    assertEquals( "", controller.getText() );

    sheet.setText( TEST_TEXT, ROW_TO_SELECT + 1, COLUMN_TO_SELECT );
    assertEquals( TEST_TEXT, controller.getText() );
  }

  private Label findCellControl( final SpreadSheet sheet,
                                 final CellPosition position )
  {
    Label result = null;
    Control[] children = sheet.getChildren();
    for( int i = 0; result == null && i < children.length; i++ ) {
      if( children[ i ] instanceof Composite ) {
        Composite cellContainer = ( Composite )children[ i ];
        Control[] cellControls = cellContainer.getChildren();
        for( int j = 0; j < cellControls.length; j++ ) {
          SpreadSheetData data
          = ( SpreadSheetData )cellControls[ j ].getLayoutData();
          Control cellEditorControl
          = sheet.getCellEditorController().getControl();
          if(    data != null
              && position.equals( data.getPosition() )
              && cellControls[ j ] != cellEditorControl )
          {
            result = ( Label )cellControls[ j ];
          }
        }
      }
    }
    return result;
  }

  public void testKeyHandler() {
    SpreadSheet sheet = new SpreadSheet( shell, SWT.NONE );
    CellEditorController controller = sheet.getCellEditorController();
    Control cellEditor = controller.getControl();
    SpreadSheetData data = ( SpreadSheetData )cellEditor.getLayoutData();
    assertEquals( 0, data.getRowIndex() );
    assertEquals( 0, data.getColumnIndex() );

    KeyEvent evt = new KeyEvent( cellEditor, KeyEvent.KEY_PRESSED );
    evt.keyCode = SWT.ARROW_DOWN;
    controller.handleKeyPressed( evt );
    data = ( SpreadSheetData )cellEditor.getLayoutData();
    assertEquals( 1, data.getRowIndex() );
    assertEquals( 0, data.getColumnIndex() );

    evt = new KeyEvent( cellEditor, KeyEvent.KEY_PRESSED );
    evt.keyCode = SWT.ARROW_RIGHT;
    controller.handleKeyPressed( evt );
    data = ( SpreadSheetData )cellEditor.getLayoutData();
    assertEquals( 1, data.getRowIndex() );
    assertEquals( 1, data.getColumnIndex() );

    evt = new KeyEvent( cellEditor, KeyEvent.KEY_PRESSED );
    evt.keyCode = SWT.ARROW_UP;
    controller.handleKeyPressed( evt );
    data = ( SpreadSheetData )cellEditor.getLayoutData();
    assertEquals( 0, data.getRowIndex() );
    assertEquals( 1, data.getColumnIndex() );

    evt = new KeyEvent( cellEditor, KeyEvent.KEY_PRESSED );
    evt.keyCode = SWT.ARROW_LEFT;
    controller.handleKeyPressed( evt );
    data = ( SpreadSheetData )cellEditor.getLayoutData();
    assertEquals( 0, data.getRowIndex() );
    assertEquals( 0, data.getColumnIndex() );

    evt = new KeyEvent( cellEditor, KeyEvent.KEY_PRESSED );
    evt.keyCode = SWT.ARROW_LEFT;
    controller.handleKeyPressed( evt );
    data = ( SpreadSheetData )cellEditor.getLayoutData();
    assertEquals( 0, data.getRowIndex() );
    assertEquals( 0, data.getColumnIndex() );

    evt = new KeyEvent( cellEditor, KeyEvent.KEY_PRESSED );
    evt.keyCode = SWT.ARROW_UP;
    controller.handleKeyPressed( evt );
    data = ( SpreadSheetData )cellEditor.getLayoutData();
    assertEquals( 0, data.getRowIndex() );
    assertEquals( 0, data.getColumnIndex() );
  }

  public void testFocusHandling() {
    shell.open();
    SpreadSheet sheet = new SpreadSheet( shell, SWT.NONE );
    CellEditorController controller = sheet.getCellEditorController();
    Control cellEditor = controller.getControl();
    assertFalse( cellEditor.isFocusControl() );
    sheet.setFocus();
    assertTrue( cellEditor.isFocusControl() );
    Text text = new Text( shell, SWT.NONE );
    text.setFocus();
    assertFalse( cellEditor.isFocusControl() );
    CellPosition position = new CellPosition( 1, 1 );
    sheet.getCellEditorController().handleMouseDown( position );
    sheet.getCellEditorController().handleMouseUp( position );
    assertTrue( cellEditor.isFocusControl() );
  }

  protected void setUp() throws Exception {
    Fixture.setUp();
    display = new Display();
    shell = new Shell( display, SWT.SHELL_TRIM );
    shell.setSize( 600, 400 );
    shell.setLayout( new FillLayout() );
  }

  protected void tearDown() throws Exception {
    if( display != null && !display.isDisposed() ) {
      display.dispose();
      shell = null;
      display = null;
    }
    Fixture.tearDown();
  }

}
