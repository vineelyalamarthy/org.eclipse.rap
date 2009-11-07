// Created on 16.08.2009
package org.eclipse.swt.custom;

import java.util.*;

import org.eclipse.rwt.graphics.Graphics;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SpreadSheetModel.Adapter;
import org.eclipse.swt.custom.SpreadSheetModel.Event;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.*;


class CellController {
  static final int DEFAULT_GRID_LINE_BREADTH = 1;
  private static final String CELL_CONTROL_MARKER
    = SpreadSheetLayout.class.getName() + "#Cell";

  private final Composite spreadSheet;
  private final Composite cellContainer;
  private final SpreadSheetModel model;
  private Label[] gridLines;


  private final class ModelAdapter extends Adapter {
    public void textChanged( final Event evt ) {
      int row = evt.rowIndex;
      int column = evt.columnIndex;
      CellPosition position
        = new CellPosition( row, column );
      setText( evt.text, position );
    }
  }


  CellController( final Composite spreadSheet,
                  final SpreadSheetModel model )
  {
    SpreadSheetUtils.checkNotNull( spreadSheet, "spreadSheet" );
    SpreadSheetUtils.checkNotNull( model, "model" );

    this.spreadSheet = spreadSheet;
    cellContainer = new Composite( spreadSheet, SWT.NONE );
    cellContainer.setBackground( spreadSheet.getBackground() );
    this.model = model;
    this.gridLines = new Label[ 0 ];
    model.addListener( new ModelAdapter() );
  }

  void adjustCellControls( ) {
    // TODO [fappel]: check the algorithm in relation to memory consumption
    //                and performance
    cellContainer.moveBelow( null );
    computeCellContainerBounds();
    adjustGrid();
    adjustCellLayoutData();
    Map buffer = bufferExistingCells();
    createMissingCells( buffer );
    disposeOfSpareCells( buffer );
    Rectangle clientArea = cellContainer.getClientArea();
    Control[] children = cellContainer.getChildren();
    for( int i = 0; i < children.length; i++ ) {
      SpreadSheetUtils.computeBounds( clientArea, children[ i ], model );
    }
    updateText();
  }

  private void adjustGrid() {
    adjustGridControlNumber();

    int columnOffset = model.getColumnOffset();
    int rowOffset = model.getRowOffset();
    int xBase = SpreadSheetUtils.calcXPosition( 0, 0, columnOffset, model );
    int yBase = SpreadSheetUtils.calcYPosition( 0, 0, rowOffset, model );
    int gridLineIndex = 0;
    
    for( int i = 1; i < model.getVisibleRows(); i++ ) {
      int index = rowOffset + i;
      int yPos = yBase;
      yPos =   SpreadSheetUtils.calcYPosition( yPos, rowOffset, index, model )
             - DEFAULT_GRID_LINE_BREADTH;
      int width = cellContainer.getClientArea().width;
      Label line = gridLines[ gridLineIndex ];
      line.setBounds( xBase, yPos, width, DEFAULT_GRID_LINE_BREADTH );
      gridLineIndex++;
    }
    
    for( int i = 1; i < model.getVisibleColumns(); i++ ) {
      int index = columnOffset + i;
      int xPos = xBase;
      xPos
        =   SpreadSheetUtils.calcXPosition( xBase, columnOffset, index, model )
          - DEFAULT_GRID_LINE_BREADTH;
      int height = cellContainer.getClientArea().height;
      Label line = gridLines[ gridLineIndex ];
      line.setBounds( xPos, yBase, DEFAULT_GRID_LINE_BREADTH, height );
      gridLineIndex++;
    }
  }

  private void adjustGridControlNumber() {
    int visibleRows = model.getVisibleRows();
    int visibleColumns = model.getVisibleColumns();
    int newGridLineCount = Math.max( 0, visibleColumns + visibleRows - 2 );
    Label[] newGridLines = new Label[ newGridLineCount ];
    int linesToAdjust = Math.max( newGridLineCount, gridLines.length );
    for( int i = 0; i < linesToAdjust; i++ ) {
      if( i < gridLines.length && i < newGridLineCount ) {
        // copy existing lines to new array for reuse
        newGridLines[ i ] = gridLines[ i ];
      } else if( i >= gridLines.length ) {
        // the client area has been enlarged, so more lines are needed
        newGridLines[ i ] = new Label( cellContainer, SWT.NONE );
        newGridLines[ i ].setBackground( Graphics.getColor( 0, 0, 255 ) );
      } else if( i >= newGridLineCount ) {
        // the client area has been shrunken, so dispose of the spare ones.
        gridLines[ i ].dispose();
      }
    }
    gridLines = newGridLines;
  }

  private void adjustCellLayoutData() {
    Set bufferedPositions = new HashSet();
    Control[] children = cellContainer.getChildren();
    for( int i = 0; i < children.length; i++ ) {
      SpreadSheetData data = ( SpreadSheetData )children[ i ].getLayoutData();
      if( data != null ) {
        CellPosition position = data.getPosition();
        bufferedPositions.add( position );
      }
    }

    for( int i = 0; i < children.length; i++ ) {
      SpreadSheetData data = ( SpreadSheetData )children[ i ].getLayoutData();
      if( data != null ) {
        CellPosition position = data.getPosition();
        int oldRowIndex = position.getRowIndex();
        int oldColumnIndex = position.getColumnIndex();
        if(    needsNewRowIndex( oldRowIndex )
            || needsNewColumnIndex( oldColumnIndex ) )
        {
          int rowIndex = calculateRowIndex( oldRowIndex );
          int columnIndex = calculateColumn( oldColumnIndex );
          CellPosition newPosition = new CellPosition( rowIndex, columnIndex );
          if( !bufferedPositions.contains( newPosition ) ) {
            SpreadSheetData newData = new SpreadSheetData( newPosition );
            children[ i ].setLayoutData( newData );
          }
        }
      }
    }
  }

  private int calculateRowIndex( final int oldRowIndex ) {
    int result = oldRowIndex;
    if( needsNewRowIndex( oldRowIndex )  ) {
      if( isInUpperOffset( oldRowIndex ) ) {
        result = oldRowIndex + model.getVisibleRows();
      } else {
        result = oldRowIndex - model.getVisibleRows();
      }
    }
    return result;
  }

  private int calculateColumn( final int oldColumnIndex ) {
    int result = oldColumnIndex;
    if( needsNewColumnIndex( oldColumnIndex ) ) {
      if( isInLeftOffset( oldColumnIndex ) ) {
        result = oldColumnIndex + model.getVisibleColumns();
      } else {
        result = oldColumnIndex - model.getVisibleColumns();
      }
    }
    return result;
  }

  private boolean needsNewRowIndex( final int oldRowIndex ) {
    return    isInUpperOffset( oldRowIndex )
           || isInLowerOffset( oldRowIndex );
  }

  private boolean isInLowerOffset( final int oldRowIndex ) {
    return oldRowIndex >= model.getVisibleRows() + model.getRowOffset();
  }

  private boolean isInUpperOffset( final int oldRowIndex ) {
    return oldRowIndex < model.getRowOffset();
  }

  private boolean needsNewColumnIndex( final int oldColumnIndex ) {
    return    isInLeftOffset( oldColumnIndex )
           || isInRightOffset( oldColumnIndex );
  }

  private boolean isInRightOffset( final int oldColumnIndex ) {
    int visibleColumns = model.getVisibleColumns();
    return oldColumnIndex >= visibleColumns + model.getColumnOffset();
  }

  private boolean isInLeftOffset( final int oldColumnIndex ) {
    return oldColumnIndex < model.getColumnOffset();
  }

  private void computeCellContainerBounds() {
    Rectangle clientArea = spreadSheet.getClientArea();
    int rowOffset = model.getRowOffset();
    int yOffset = 0;
    for( int i = 0; i < rowOffset; i++ ) {
      yOffset += model.getRowHeight( i );
    }
    int columnOffset = model.getColumnOffset();
    int xOffset = 0;
    for( int i = 0; i < columnOffset; i++ ) {
      xOffset += model.getColumnWidth( i );
    }
    cellContainer.setBounds( clientArea.x - xOffset,
                             clientArea.y - yOffset,
                             clientArea.width + xOffset,
                             clientArea.height + yOffset );
  }

  private void updateText() {
    Control[] children = cellContainer.getChildren();
    for( int i = 0; i < children.length; i++ ) {
      if( children[ i ].getData( CELL_CONTROL_MARKER ) != null ) {
        SpreadSheetData data = ( SpreadSheetData )children[ i ].getLayoutData();
        CellPosition position = data.getPosition();
        Label control = ( Label )children[ i ];
        String text = model.getText( position );
        control.setText( text );
      }
    }
  }

  Map bufferExistingCells() {
    Control[] children = cellContainer.getChildren();
    Map result = new HashMap();
    for( int i = 0; i < children.length; i++ ) {
      Control control = children[ i ];
      if( isCellControl( control ) ) {
        SpreadSheetData data = ( SpreadSheetData )control.getLayoutData();
        result.put( data.getPosition(), control );
      }
    }
    return result;
  }

  private void createMissingCells( final Map buffer )
  {
    int rowOffset = model.getRowOffset();
    int rows = model.getVisibleRows() + rowOffset;
    int columnOffset = model.getColumnOffset();
    int columns = model.getVisibleColumns() + columnOffset;
    for( int i = rowOffset; i < rows; i++ ) {
      for( int j = columnOffset; j < columns; j++ ) {
        CellPosition position = new CellPosition( i, j );
        Control control = ( Control )buffer.get( position );
        if( control == null ) {
          if( model.getText( position ) != "" ) {
            createCellControl( position );
          }
        }
      }
    }
  }

  private Control createCellControl( final CellPosition position )
  {
    Label result = new Label( cellContainer, SWT.NONE );
    result.setLayoutData( new SpreadSheetData( position ) );
    result.setData( CELL_CONTROL_MARKER, CELL_CONTROL_MARKER );
    Display current = Display.getCurrent();
    Color bgColor = current.getSystemColor( SWT.COLOR_WHITE );
    result.setBackground( bgColor );
    result.addMouseListener( new MouseAdapter()  {
      public void mouseUp( final MouseEvent evt ) {
        SpreadSheet sheet = ( SpreadSheet )spreadSheet;
        sheet.getCellEditorController().handleMouseUp( position );
      }
      public void mouseDown( final MouseEvent evt ) {
        SpreadSheet sheet = ( SpreadSheet )spreadSheet;
        sheet.getCellEditorController().handleMouseDown( position );
      }
    } );
    return result;
  }

  static boolean isCellControl( final Control control ) {
    Object data = control.getData( CELL_CONTROL_MARKER );
    return    control instanceof Label
           && control.getLayoutData() != null
           && CELL_CONTROL_MARKER.equals( data );
  }

  void setText( final String text,
                final CellPosition position )
  {
    Label cell = findCellControl( position );
    if( cell != null && "".equals( text ) ) {
      cell.dispose();
    } else if( cell != null && !"".equals( text ) ) {
      cell.setText( text );
    } else if( cell == null && !"".equals( text ) ) {
      cell = ( Label )createCellControl( position );
      Rectangle clientArea = cellContainer.getClientArea();
      SpreadSheetUtils.computeBounds( clientArea, cell, model );
      cell.setText( text );
    }
  }

  private Label findCellControl( final CellPosition position )
  {
    Label result = null;
    Control[] children = cellContainer.getChildren();
    for( int i = 0; result == null && i < children.length; i++ ) {
      SpreadSheetData data = ( SpreadSheetData )children[ i ].getLayoutData();
      // TODO [fappel]: cellContainer should contain cell controls only
      //                -> check for cell control can be removed
      if( data != null
          && position.equals( data.getPosition() )
          && isCellControl( children[ i ] ) )
      {
        result = ( Label )children[ i ];
      }
    }
    return result;
  }

  void disposeOfSpareCells( final Map buffer )
  {
    int rowOffset = model.getRowOffset();
    int rows = model.getVisibleRows() + rowOffset;
    int columnOffset = model.getColumnOffset();
    int columns = model.getVisibleColumns() + columnOffset;
    for( int i = rowOffset; i < rows; i++ ) {
      for( int j = columnOffset; j < columns; j++ ) {
        CellPosition position = new CellPosition( i, j );
        buffer.remove( position );
      }
    }
    Iterator iterator = buffer.values().iterator();
    while( iterator.hasNext() ) {
      Control control = ( Control )iterator.next();
      control.dispose();
    }
  }

  Composite getCellContainer() {
    return cellContainer;
  }

  Label[] getGridLines() {
    return gridLines;
  }
}