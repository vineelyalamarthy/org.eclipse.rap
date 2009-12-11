// Created on 07.08.2009
package org.eclipse.swt.custom;

import java.math.BigDecimal;

import junit.framework.TestCase;

import org.eclipse.rwt.Fixture;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.*;


/*
 * TODO [fappel]: implement flushCache functionality of SpreadSheetLayout#layout 
 * TODO [fappel]: check that only correct layout data types are used
 */
public class SpreadSheetLayout_Test extends TestCase {
  private static final int DEFAULT_SHELL_HEIGHT = 400;
  private static final int DEFAULT_SHELL_WIDTH = 400;
  private static final int DEFAULT_COLUMN_HEADER_HEIGHT
    = HeaderController.DEFAULT_COLUMN_HEADER_HEIGHT;
  private static final int DEFAULT_ROW_HEADER_WIDTH 
    = HeaderController.DEFAULT_ROW_HEADER_WIDTH;
  private static final int DEFAULT_GRID_LINE_BREADTH
    = CellController.DEFAULT_GRID_LINE_BREADTH;
  private static final int DEFAULT_ROW_HEIGHT
   = SpreadSheetModel.DEFAULT_ROW_HEIGHT;
  private static final int DEFAULT_COLUMN_WIDTH
    = SpreadSheetModel.DEFAULT_COLUMN_WIDTH;
  private static final int DEFAULT_CELL_HEIGHT
    =   SpreadSheetModel.DEFAULT_ROW_HEIGHT 
      - DEFAULT_GRID_LINE_BREADTH;
  private static final int DEFAULT_CELL_WIDTH
    =   SpreadSheetModel.DEFAULT_COLUMN_WIDTH
      - DEFAULT_GRID_LINE_BREADTH;
  private static final int SASH_FACTOR = 2;
  private static final int EXPECTED_SLIDER_COUNT = 2;
  private static final int EXPECTED_CONTAINER_COUNT = 1;
  private static final int EXPECTED_HEADER_COUNT = 2;
  private static final int ROW_TO_TEST = 4;
  private static final int ROW_TO_TEST_HEIGHT = 40;
  private static final int COLUMN_TO_TEST = 5;
  private static final int COLUMN_TO_TEST_WIDTH = 150;
  private Display display;
  private Shell shell;
  private SpreadSheetModel model;
  private CellController cellController;
  private HeaderController headerController;
  private SliderController sliderController;
  private SpreadSheetLayout layout;
  
  public void testEmptyGrid() {
    layout.layout( shell, false );
    Composite cellContainer = cellController.getCellContainer();
    Rectangle clientArea = shell.getClientArea();
    int expectedColCount = calculateColumnCount( clientArea );
    int expectedRowCount = calculateRowCount( clientArea );
    Control[] children = cellContainer.getChildren();
    assertEquals( expectedColCount + expectedRowCount - 2, children.length );

    Label[] gridLines = cellController.getGridLines();
    Label line = gridLines[ 0 ];
    assertEquals( clientArea.x, line.getBounds().x );
    assertEquals( DEFAULT_CELL_HEIGHT, line.getBounds().y );
    assertEquals( clientArea.width, line.getBounds().width );
    assertEquals( DEFAULT_GRID_LINE_BREADTH, line.getBounds().height );
    
    // change row and column offset
    model.setRowOffset( 1 );
    model.setColumnOffset( 1 );
    layout.layout( shell, false );

    // number of children must not change
    children = cellContainer.getChildren();
    assertEquals( expectedColCount + expectedRowCount - 2, children.length );
    
    gridLines = cellController.getGridLines();
    line = gridLines[ 0 ];
    assertEquals( clientArea.x + DEFAULT_COLUMN_WIDTH, line.getBounds().x );
    assertEquals( DEFAULT_CELL_HEIGHT * 2 + DEFAULT_GRID_LINE_BREADTH,
                  line.getBounds().y );
    assertEquals( cellContainer.getBounds().width,
                  line.getBounds().width );
    assertEquals( DEFAULT_GRID_LINE_BREADTH, line.getBounds().height );

  }
  
  public void testLayoutCompleteClientArea() {
    Composite cellContainer = cellController.getCellContainer();
    Composite rowHeaderContainer = headerController.getRowContainer();
    Composite columnHeaderContainer = headerController.getColumnContainer();

    int expectedControlCount =   EXPECTED_CONTAINER_COUNT
                               + EXPECTED_SLIDER_COUNT
                               + EXPECTED_HEADER_COUNT;
    int sheetChildCount = shell.getChildren().length;
    assertEquals( "Sliders or container are missing", 
                  expectedControlCount, 
                  sheetChildCount );
    
    for( int repetitions = 0; repetitions < 4; repetitions++ ) {
      switch( repetitions ) {
        case 1:
          shell.setSize( 800, 800 );
        break;
        case 2:
          shell.setSize( DEFAULT_SHELL_WIDTH, DEFAULT_SHELL_HEIGHT );
        break;
        case 3:
          SpreadSheetFixture.populateModel( model );
        break;
      }
      layout.layout( shell, false );
      
      Rectangle clientArea = shell.getClientArea();
      assertEquals( clientArea, cellContainer.getBounds() );
      int expectedColCount = calculateColumnCount( clientArea );
      int expectedRowCount = calculateRowCount( clientArea );
      int expectedCellCount = 0;
      if( repetitions == 3 ) {
        expectedCellCount = expectedColCount * expectedRowCount;
      }
      int expectedChildCount =   EXPECTED_SLIDER_COUNT
                               + EXPECTED_CONTAINER_COUNT
                               + EXPECTED_HEADER_COUNT;
      assertEquals( expectedChildCount, shell.getChildren().length );

      Control[] children = cellContainer.getChildren();
      int cellCount = 0;
      int headerCount = 0;
      for( int i = 0; i < children.length; i++ ) {
        if( CellController.isCellControl( children[ i ] ) ) {
          cellCount++;
        }
      }
      Control[] rowHeaderControls = rowHeaderContainer.getChildren();
      Control[] columnHeaderControls = columnHeaderContainer.getChildren();
      headerCount = rowHeaderControls.length + columnHeaderControls.length;
      assertEquals( expectedCellCount, cellCount );
      
      int expectedHeaderCount
        = ( expectedRowCount + expectedColCount ) * SASH_FACTOR;
      assertEquals( expectedHeaderCount, headerCount );
    }
  }

  private int calculateRowCount( final Rectangle clientArea ) {
    BigDecimal height = new BigDecimal( clientArea.height );
    BigDecimal rowHeight = new BigDecimal( DEFAULT_ROW_HEIGHT );
    return height.divide( rowHeight, BigDecimal.ROUND_UP ).intValue();
  }

  private int calculateColumnCount( final Rectangle clientArea ) {
    BigDecimal width = new BigDecimal( clientArea.width );
    BigDecimal columnWidth = new BigDecimal( DEFAULT_COLUMN_WIDTH );
    return width.divide( columnWidth, BigDecimal.ROUND_UP ).intValue();
  }
  
  public void testHeaderPositions() {
    Composite rowHeaderContainer = headerController.getRowContainer();
    Composite columnHeaderContainer = headerController.getColumnContainer();

    layout.layout( shell, false );
    Rectangle clientArea = shell.getClientArea();
 
    // Note that the headers belong to the trimmings of the surrounding
    // composite and therefore they are not rendered into the clientarea
    // of the shell. This may not feels reasonable but the spreadsheet is
    // not intended to be used in another composites besides the SpreadSheet.
    // TODO [fappel]: This construct may needs a second thought though.
    Rectangle rowBounds = rowHeaderContainer.getBounds();
    assertEquals( clientArea.x - DEFAULT_ROW_HEADER_WIDTH, rowBounds.x );
    assertEquals( clientArea.y, rowBounds.y );
    assertEquals( DEFAULT_ROW_HEADER_WIDTH - DEFAULT_GRID_LINE_BREADTH,
                  rowBounds.width );
    assertEquals( clientArea.height, rowBounds.height );

    Rectangle columnBounds = columnHeaderContainer.getBounds();
    assertEquals( clientArea.x, columnBounds.x );
    assertEquals( clientArea.y - DEFAULT_COLUMN_HEADER_HEIGHT, columnBounds.y );
    assertEquals( clientArea.width, columnBounds.width );
    assertEquals( DEFAULT_COLUMN_HEADER_HEIGHT - DEFAULT_GRID_LINE_BREADTH,
                  columnBounds.height);
    
    // test initial position of the header controls
    Control[] rowHeaders = headerController.getRowHeaderControls();
    assertEquals( model.getVisibleRows(), rowHeaders.length );
    assertEquals( 0, rowHeaders[ 0 ].getBounds().x );
    assertEquals( 0, rowHeaders[ 0 ].getBounds().y );

    Control[] columnHeaders = headerController.getColumnHeaderControls();
    assertEquals( model.getVisibleColumns(), columnHeaders.length );
    assertEquals( 0, columnHeaders[ 0 ].getBounds().x );
    assertEquals( 0, columnHeaders[ 0 ].getBounds().y );
    
    // change row and column offset
    model.setRowOffset( 1 );
    model.setColumnOffset( 1 );
    layout.layout( shell, false );
    
    // test header container bounds with row and column offset
    rowBounds = rowHeaderContainer.getBounds();
    assertEquals( clientArea.x - DEFAULT_ROW_HEADER_WIDTH, rowBounds.x );
    assertEquals( clientArea.y - DEFAULT_ROW_HEIGHT, rowBounds.y );
    assertEquals( DEFAULT_ROW_HEADER_WIDTH - DEFAULT_GRID_LINE_BREADTH,
                  rowBounds.width );
    assertEquals( clientArea.height + DEFAULT_ROW_HEIGHT, rowBounds.height );

    columnBounds = columnHeaderContainer.getBounds();
    assertEquals( clientArea.x - DEFAULT_COLUMN_WIDTH, columnBounds.x );
    assertEquals( clientArea.y - DEFAULT_COLUMN_HEADER_HEIGHT, columnBounds.y );
    assertEquals( clientArea.width + DEFAULT_COLUMN_WIDTH, columnBounds.width );
    assertEquals( DEFAULT_COLUMN_HEADER_HEIGHT - DEFAULT_GRID_LINE_BREADTH,
                  columnBounds.height);

    // test header controls positions with row and column offset
    rowHeaders = headerController.getRowHeaderControls();
    assertEquals( model.getVisibleRows(), rowHeaders.length );
    assertEquals( 0, rowHeaders[ 0 ].getBounds().x );
    assertEquals( DEFAULT_ROW_HEIGHT, rowHeaders[ 0 ].getBounds().y );
    assertEquals( DEFAULT_ROW_HEIGHT * 2, rowHeaders[ 1 ].getBounds().y );
    
    columnHeaders = headerController.getColumnHeaderControls();
    assertEquals( model.getVisibleColumns(), columnHeaders.length );
    assertEquals( DEFAULT_COLUMN_WIDTH, columnHeaders[ 0 ].getBounds().x );
    assertEquals( DEFAULT_COLUMN_WIDTH * 2, columnHeaders[ 1 ].getBounds().x );
    assertEquals( 0, columnHeaders[ 0 ].getBounds().y );
  }
    
  public void testCellBoundsAndPositionCalculation() {
    SpreadSheetFixture.populateModel( model );
    layout.layout( shell, false );
    Composite container = cellController.getCellContainer();
    assertEquals( shell.getClientArea(), container.getBounds() );

    Control[] children = container.getChildren();
    Control cell = null;
    Control cell_0_0 = null;
    for( int i = 0; i < children.length; i++ ) {
      if( CellController.isCellControl( children[ i ] ) ) {
        SpreadSheetData data = ( SpreadSheetData )children[ i ].getLayoutData();
        if( new CellPosition( 0, 0 ).equals( data.getPosition() ) ) {
          cell_0_0 = children[ i ];
        }
        if( new CellPosition( 2, 1 ).equals( data.getPosition() ) ) {
          cell = children[ i ];
        }
      }
    }
    
    // test cell position
    Rectangle cellBounds = cell.getBounds();
    Rectangle clientArea = container.getClientArea();
    assertEquals( clientArea.x + DEFAULT_COLUMN_WIDTH, cellBounds.x );
    assertEquals( clientArea.y + DEFAULT_ROW_HEIGHT * 2, cellBounds.y );
    assertEquals( DEFAULT_CELL_WIDTH, cellBounds.width );
    assertEquals( DEFAULT_CELL_HEIGHT, cellBounds.height );
    
    // test cellcontainer bounds with row and column offset
    model.setRowOffset( 1 );
    model.setColumnOffset( 1 );
    layout.layout( shell, false );
    int columnWidth = model.getColumnWidth( 0 );
    int rowHeight = model.getRowHeight( 0 );
    Rectangle cellContainerBounds = container.getBounds();
    Rectangle shellClientArea = shell.getClientArea();
    assertEquals( shellClientArea.x - columnWidth, cellContainerBounds.x );
    assertEquals( shellClientArea.y - rowHeight, cellContainerBounds.y );
    assertEquals( shellClientArea.width + columnWidth,
                  cellContainerBounds.width );
    assertEquals( shellClientArea.height + rowHeight,
                  cellContainerBounds.height );
    
    // test movement of upper left cell in case of row and column offset
    SpreadSheetData data = ( SpreadSheetData )cell_0_0.getLayoutData();
    int visibleRows = model.getVisibleRows();
    int visibleColumns = model.getVisibleColumns();
    CellPosition expectedPosition 
      = new CellPosition( visibleRows, visibleColumns );
    assertEquals( expectedPosition, data.getPosition() );
    
    // ensure position of upper left cell after offsets have been removed
    model.setRowOffset( 0 );
    model.setColumnOffset( 0 );
    layout.layout( shell, false );
    data = ( SpreadSheetData )cell_0_0.getLayoutData();
    assertEquals( new CellPosition( 0, 0 ), data.getPosition() );
    
    // test cell bounds with offset and changed size
    int height = 150;
    model.setRowHeight( 2, height );
    int width = 300;
    model.setColumnWidth( 1, width );
    layout.layout( shell, false );
    cellBounds = cell.getBounds();
    clientArea = container.getClientArea();
    assertEquals( clientArea.x + DEFAULT_COLUMN_WIDTH, cellBounds.x );
    assertEquals( clientArea.y + DEFAULT_ROW_HEIGHT * 2, cellBounds.y );
    assertEquals( width - DEFAULT_GRID_LINE_BREADTH , cellBounds.width );
    assertEquals( height - DEFAULT_GRID_LINE_BREADTH, cellBounds.height );
    
    model.setColumnOffset( 1 );
    model.setRowOffset( 1 );
    layout.layout( shell, false );
    cellBounds = cell.getBounds();
    clientArea = container.getClientArea();
    assertEquals( clientArea.x + DEFAULT_COLUMN_WIDTH, cellBounds.x );
    assertEquals( clientArea.y + DEFAULT_ROW_HEIGHT * 2, cellBounds.y );
    assertEquals( width - DEFAULT_GRID_LINE_BREADTH , cellBounds.width );
    assertEquals( height - DEFAULT_GRID_LINE_BREADTH, cellBounds.height );
  }

  public void testLayoutingOfAdditionalChild() {
    Label label = new Label( shell, SWT.NONE );
    
    // root coordinates
    label.setLayoutData( new SpreadSheetData( 0, 0 ) );
    layout.layout( shell, false );
    Rectangle clientArea = shell.getClientArea();
    Rectangle bounds = label.getBounds();
    assertEquals( clientArea.x, bounds.x );
    assertEquals( clientArea.y, bounds.y );
    assertEquals( DEFAULT_CELL_WIDTH, bounds.width );
    assertEquals( DEFAULT_CELL_HEIGHT, bounds.height );

    
    // arbitrary coordinates
    label.setLayoutData( new SpreadSheetData( 2, 1 ) );
    layout.layout( shell, false );
    bounds = label.getBounds();
    assertEquals( clientArea.x + DEFAULT_COLUMN_WIDTH, bounds.x );
    assertEquals( clientArea.y + DEFAULT_ROW_HEIGHT * 2, bounds.y );
    assertEquals( DEFAULT_CELL_WIDTH, bounds.width );
    assertEquals( DEFAULT_CELL_HEIGHT, bounds.height );
    
    
    // change height of a certain row
    assertEquals( DEFAULT_ROW_HEIGHT, model.getRowHeight( ROW_TO_TEST ) );
    model.setRowHeight( ROW_TO_TEST, ROW_TO_TEST_HEIGHT );
    label.setLayoutData( new SpreadSheetData( ROW_TO_TEST, 0 ) );
    layout.layout( shell, false );
    bounds = label.getBounds();
    assertEquals( clientArea.x, bounds.x );
    assertEquals( clientArea.y + DEFAULT_ROW_HEIGHT * ROW_TO_TEST, bounds.y );
    assertEquals( DEFAULT_CELL_WIDTH, bounds.width );
    assertEquals( ROW_TO_TEST_HEIGHT, model.getRowHeight( ROW_TO_TEST ) );
    
    label.setLayoutData( new SpreadSheetData( ROW_TO_TEST + 1, 0 ) );    
    layout.layout( shell, false );
    bounds = label.getBounds();
    assertEquals( clientArea.x, bounds.x );
    int yPos
      = clientArea.y + DEFAULT_ROW_HEIGHT * ROW_TO_TEST + ROW_TO_TEST_HEIGHT;
    assertEquals( yPos, bounds.y );
    assertEquals( DEFAULT_CELL_WIDTH, bounds.width );
    assertEquals( ROW_TO_TEST_HEIGHT, model.getRowHeight( ROW_TO_TEST ) );
    
    model.setRowHeight( ROW_TO_TEST, SpreadSheetModel.DEFAULT_ROW_HEIGHT );
    assertEquals( DEFAULT_ROW_HEIGHT, model.getRowHeight( ROW_TO_TEST ) );
    
    
    // change width of a certain column
    assertEquals( DEFAULT_COLUMN_WIDTH,
                  model.getColumnWidth( COLUMN_TO_TEST ) );
    model.setColumnWidth( COLUMN_TO_TEST, COLUMN_TO_TEST_WIDTH );
    label.setLayoutData( new SpreadSheetData( 0, COLUMN_TO_TEST ) );
    layout.layout( shell, false );
    bounds = label.getBounds();
    int xPos = clientArea.x + DEFAULT_COLUMN_WIDTH * COLUMN_TO_TEST;
    assertEquals( xPos, bounds.x );
    assertEquals( clientArea.y, bounds.y );
    assertEquals( COLUMN_TO_TEST_WIDTH,
                  model.getColumnWidth( COLUMN_TO_TEST ) );
    assertEquals( DEFAULT_CELL_HEIGHT, bounds.height );
    
    label.setLayoutData( new SpreadSheetData( 0, COLUMN_TO_TEST + 1 ) );
    layout.layout( shell, false );
    bounds = label.getBounds();
    xPos =    clientArea.x
            + DEFAULT_COLUMN_WIDTH * COLUMN_TO_TEST 
            + COLUMN_TO_TEST_WIDTH;
    assertEquals( xPos, bounds.x );
    assertEquals( clientArea.y, bounds.y );
    assertEquals( COLUMN_TO_TEST_WIDTH,
                  model.getColumnWidth( COLUMN_TO_TEST ) );
    assertEquals( DEFAULT_CELL_HEIGHT, bounds.height );
    
    model.setColumnWidth( COLUMN_TO_TEST, DEFAULT_COLUMN_WIDTH );
    assertEquals( DEFAULT_COLUMN_WIDTH,
                  model.getColumnWidth( COLUMN_TO_TEST ) );
  }
  
  public void testParams() {
    try {
      model.setColumnWidth( -1 , 4 );
      fail( "Negative column index isn't allowed." );
    } catch( final IllegalArgumentException iae ) {
      // expected
    }
    try {
      model.setColumnWidth( 1 , -4 );
      fail( "Negative column width isn't allowed." );
    } catch( final IllegalArgumentException iae ) {
      // expected
    }
    try {
      model.setRowHeight( -1 , 4 );
      fail( "Negative row index isn't allowed." );
    } catch( final IllegalArgumentException iae ) {
      // expected
    }
    try {
      model.setRowHeight( 1 , -4 );
      fail( "Negative row height isn't allowed." );
    } catch( final IllegalArgumentException iae ) {
      // expected
    }
  }
  
  protected void setUp() throws Exception {
    Fixture.setUp();
    display = new Display();
    shell = new Shell( display, SWT.SHELL_TRIM );
    shell.setSize( DEFAULT_SHELL_WIDTH, DEFAULT_SHELL_HEIGHT );

    model = new SpreadSheetModel();
    cellController = new CellController( shell, model );
    headerController = new HeaderController( shell, model );
    sliderController = new SliderController( shell, headerController, model );
    layout = new SpreadSheetLayout( model,
                                    cellController,
                                    sliderController,
                                    headerController );
  }

  protected void tearDown() throws Exception {
    model = null;
    cellController = null;
    headerController = null;
    sliderController = null;
    layout = null;
    
    if( display != null && !display.isDisposed() ) {
      display.dispose();
      shell = null;
      display = null;
    }
    Fixture.tearDown();
  }
}
