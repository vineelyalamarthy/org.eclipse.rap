// Created on 07.08.2009
package org.eclipse.swt.custom;



class SpreadSheetData {

  private final CellPosition position;
  
  SpreadSheetData( final CellPosition position ) {
    this.position = position;
  }

  SpreadSheetData( final int rowIndex, final int columnIndex ) {
    this( new CellPosition( rowIndex, columnIndex ) );
  }

  int getColumnIndex() {
    return position.getColumnIndex();
  }

  int getRowIndex() {
    return position.getRowIndex();
  }
  
  CellPosition getPosition() {
    return position;
  }
}
