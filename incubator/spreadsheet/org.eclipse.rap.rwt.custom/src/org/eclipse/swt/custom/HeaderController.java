// Created on 22.08.2009
package org.eclipse.swt.custom;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.*;


class HeaderController {
  static final int DEFAULT_ROW_HEADER_WIDTH = 25;
  static final int DEFAULT_COLUMN_HEADER_HEIGHT
    = SpreadSheetModel.DEFAULT_ROW_HEIGHT;
  static final String ROW_HEADER_MARKER
    = SpreadSheetLayout.class.getName() + "#RowHeader";
  static final String COLUMN_HEADER_MARKER
    = SpreadSheetLayout.class.getName() + "#ColumnHeader";
  private static final String SASH
    = SpreadSheetLayout.class.getName() + "#Sash";
  private static final int SASH_BREADTH = 5;
  private static final int SASH_OFFSET = 2;

  private final Color headerColor;
  private final Composite spreadSheet;
  private final SpreadSheetModel model;
  private Control[] rowHeaders;
  private Control[] columnHeaders;
  private Composite rowContainer;
  private Composite columnContainer;
  private int bufferedRowOffset;
  private int bufferedColumnOffset;

  
  public HeaderController( final Composite spreadSheet,
                           final SpreadSheetModel model )
  {
    SpreadSheetUtils.checkNotNull( spreadSheet, "spreadSheet" );
    SpreadSheetUtils.checkNotNull( model, "model" );
    
    this.spreadSheet = spreadSheet;
    this.model = model;
    this.bufferedRowOffset = model.getRowOffset();
    this.bufferedColumnOffset = model.getColumnOffset();
    this.rowHeaders = new Control[ 0 ];
    this.columnHeaders = new Control[ 0 ];
    rowContainer = new Composite( spreadSheet, SWT.NONE );
    columnContainer = new Composite( spreadSheet, SWT.NONE );
    Display display = Display.getDefault();
    this.headerColor  = display.getSystemColor( SWT.COLOR_WIDGET_LIGHT_SHADOW );
    rowContainer.setBackground( display.getSystemColor( SWT.COLOR_RED ) );
    columnContainer.setBackground( display.getSystemColor( SWT.COLOR_RED ) );
  }

  void adjustHeaderControls() {
    adjustRowHeaderContainer();
    adjustColumnHeaderContainer();
    adjustRowHeaders();
    adjustColumnHeaders();
  }

  private void adjustRowHeaderContainer() {
    Rectangle clientArea = spreadSheet.getClientArea();
    int xPos = clientArea.x - DEFAULT_ROW_HEADER_WIDTH;
    int rowOffset = model.getRowOffset();
    int yOffset = 0;
    for( int i = 0; i < rowOffset; i++ ) {
      yOffset += model.getRowHeight( i );
    }
    int yPos = clientArea.y - yOffset;
    int width =   DEFAULT_ROW_HEADER_WIDTH
                - CellController.DEFAULT_GRID_LINE_BREADTH;
    int height = clientArea.height + yOffset;
    rowContainer.setBounds( xPos, yPos, width, height );
  }
  
  private void adjustColumnHeaderContainer() {
    Rectangle clientArea = spreadSheet.getClientArea();
    int columnOffset = model.getColumnOffset();
    int xOffset = 0;
    for( int i = 0; i < columnOffset; i++ ) {
      xOffset += model.getColumnWidth( i );
    }
    int xPos = clientArea.x - xOffset;
    int yPos = clientArea.y - DEFAULT_COLUMN_HEADER_HEIGHT;
    int width = clientArea.width + xOffset;
    int height =   DEFAULT_COLUMN_HEADER_HEIGHT
                - CellController.DEFAULT_GRID_LINE_BREADTH;
    columnContainer.setBounds( xPos, yPos, width, height );
  }

  private void adjustColumnHeaders() {
    adjustColumnHeaderNumber();
    adjustColumnHeaderBounds();
  }

  private void adjustRowHeaders() {
    adjustRowHeaderNumber();
    adjustRowHeaderBounds();
  }

  private void adjustRowHeaderBounds() {
    int yBase = 0;
    int lowerBound = 0;
    for( int i = 0; i < rowHeaders.length; i++ ) {
      int row = i + model.getRowOffset();
      int xPos = 0;
      int yPos
        = SpreadSheetUtils.calcYPosition( yBase, lowerBound, row, model );
      yBase = yPos;
      lowerBound = row;
      int width =   getRowHeaderWidth()
                  - CellController.DEFAULT_GRID_LINE_BREADTH;
      int height =   model.getRowHeight( row )
                   - CellController.DEFAULT_GRID_LINE_BREADTH;
      rowHeaders[ i ].setBounds( xPos, yPos, width, height );
      Sash sash = ( Sash )rowHeaders[ i ].getData( SASH );
      sash.setBounds( 0, yPos + height - SASH_OFFSET, width, SASH_BREADTH );
    }
  }

  private void adjustRowHeaderNumber() {
    final Rectangle clientArea = spreadSheet.getClientArea();
    int rows = model.getVisibleRows();
    Control[] newRowHeaders = new Control[ rows ];
    int maxRow = Math.max( rows, rowHeaders.length );
    for( int i = 0; i < maxRow; i++ ) {
      if( i < rowHeaders.length && i < newRowHeaders.length ) {
        newRowHeaders[ i ] = rowHeaders[ i ];
      } else if( i < newRowHeaders.length ) {
        Label rowHeader = new Label( rowContainer, SWT.NONE );
        rowHeader.setBackground( headerColor );
        rowHeader.setData( ROW_HEADER_MARKER, String.valueOf( i ) );
        newRowHeaders[ i ] = rowHeader;
        Sash sash = new Sash( rowContainer, SWT.HORIZONTAL );
        rowHeader.setData( SASH, sash );
        final int rowIndex = i;
        sash.addSelectionListener( new SelectionAdapter() {
          public void widgetSelected( final SelectionEvent evt ) {
            int yOffset = clientArea.y - DEFAULT_COLUMN_HEADER_HEIGHT;
            int yPos = SpreadSheetUtils.calcYPosition( yOffset,
                                                            0,
                                                            rowIndex, 
                                                            model );
            int newRowHeight = Math.max( 0, evt.y - yPos + SASH_OFFSET + 1 );
            model.setRowHeight( rowIndex, newRowHeight );
          };
        } );
      } else {
        Sash sash = ( Sash )rowHeaders[ i ].getData( SASH );
        sash.dispose();
        rowHeaders[ i ].dispose();
      }
    }
    if( bufferedRowOffset != model.getRowOffset() ) {
      int offSetChange = bufferedRowOffset - model.getRowOffset();
      newRowHeaders = reorderHeaders( offSetChange, newRowHeaders );
      bufferedRowOffset = model.getRowOffset();
    }
    rowHeaders = newRowHeaders;
  }

  private Control[] reorderHeaders( int offSetChange, Control[] headerControls )
  {
    Control[] offSetHeaders = new Control[ headerControls.length ];
    for( int i = 0; i < headerControls.length; i++ ) {
      int position = i + offSetChange;
      if( position < 0 ) {
        offSetHeaders[ headerControls.length + position ] = headerControls[ i ];
      } else if ( position >= headerControls.length ) {
        offSetHeaders[ position - headerControls.length ] = headerControls[ i ];          
      } else {
        offSetHeaders[ position ] = headerControls[ i ];
      }
    }
    return offSetHeaders;
  }

  private void adjustColumnHeaderBounds() {
    int xBase = 0;
    int lowerBound = 0;
    for( int i = 0; i < columnHeaders.length; i++ ) {
      int col = i + model.getColumnOffset();
      int xPos 
        = SpreadSheetUtils.calcXPosition( xBase, lowerBound, col, model );
      xBase = xPos;
      lowerBound = col;
      int yPos = 0;
      int width =   model.getColumnWidth( col )
                  - CellController.DEFAULT_GRID_LINE_BREADTH;
      int height =   getColumnHeaderHeight()
                   - CellController.DEFAULT_GRID_LINE_BREADTH;
      columnHeaders[ i ].setBounds( xPos, yPos, width, height );
      Sash sash = ( Sash )columnHeaders[ i ].getData( SASH );
      sash.setBounds( xPos + width - SASH_OFFSET, 0, SASH_BREADTH, height );
    }
  }
  
  private void adjustColumnHeaderNumber() {
    final Rectangle clientArea = spreadSheet.getClientArea();
    int columns = model.getVisibleColumns();
    Control[] newColumnHeaders = new Control[ columns ];
    int maxColumns = Math.max( columns, columnHeaders.length );
    for( int i = 0; i < maxColumns; i++ ) {
      if( i < columnHeaders.length && i < newColumnHeaders.length ) {
        newColumnHeaders[ i ] = columnHeaders[ i ];
      } else if( i < newColumnHeaders.length ) {
        Label columnHeader = new Label( columnContainer, SWT.NONE );
        columnHeader.setBackground( headerColor );
        columnHeader.setData( COLUMN_HEADER_MARKER, String.valueOf( i ) );
        newColumnHeaders[ i ] = columnHeader;
        Sash sash = new Sash( columnContainer, SWT.VERTICAL );
        columnHeader.setData( SASH, sash );
        final int columnIndex = i;
        sash.addSelectionListener( new SelectionAdapter() {
          public void widgetSelected( final SelectionEvent evt ) {
            int xOffset = clientArea.x - DEFAULT_ROW_HEADER_WIDTH;
            int xPos = SpreadSheetUtils.calcXPosition( xOffset,
                                                            0,
                                                            columnIndex, 
                                                            model );
            int newColumnWidth = Math.max( 0, evt.x - xPos + SASH_OFFSET + 1 );
            model.setColumnWidth( columnIndex, newColumnWidth );
          };
        } );

      } else {
        Sash sash = ( Sash )columnHeaders[ i ].getData( SASH );
        sash.dispose();
        columnHeaders[ i ].dispose();
      }
    }
    if( bufferedColumnOffset != model.getColumnOffset() ) {
      int offSetChange = bufferedColumnOffset - model.getColumnOffset();
      newColumnHeaders = reorderHeaders( offSetChange, newColumnHeaders );
      bufferedColumnOffset = model.getColumnOffset();
    }
    columnHeaders = newColumnHeaders;
  }
  
  Composite getColumnContainer() {
    return columnContainer;
  }
  
  Control[] getColumnHeaderControls() {
    return columnHeaders;
  }
  
  Composite getRowContainer() {
    return rowContainer;
  }
  
  Control[] getRowHeaderControls() {
    return rowHeaders;
  }
  
  int getRowHeaderWidth() {
    return DEFAULT_ROW_HEADER_WIDTH;
  }
  
  int getColumnHeaderHeight() {
    return DEFAULT_COLUMN_HEADER_HEIGHT;
  }

  static boolean isRowHeaderControl( final Control control ) {
    return control.getData( ROW_HEADER_MARKER ) != null;
  }
  
  static boolean isColumnHeaderControl( final Control control ) {
    return control.getData( COLUMN_HEADER_MARKER ) != null;
  }
}