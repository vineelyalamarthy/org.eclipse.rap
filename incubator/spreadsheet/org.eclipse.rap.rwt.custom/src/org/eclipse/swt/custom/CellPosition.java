// Created on 15.08.2009
package org.eclipse.swt.custom;


public class CellPosition {
  private final int columnIndex;
  private final int rowIndex;

  public CellPosition( final int rowIndex, final int columnIndex ) {
    SpreadSheetUtils.checkNotNegative( rowIndex, "rowIndex" );
    SpreadSheetUtils.checkNotNegative( columnIndex, "columnIndex" );
    
    this.rowIndex = rowIndex;
    this.columnIndex = columnIndex;
  }

  public int getColumnIndex() {
    return columnIndex;
  }

  public int getRowIndex() {
    return rowIndex;
  }

  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + columnIndex;
    result = prime * result + rowIndex;
    return result;
  }


  public boolean equals( final Object obj ) {
    // genereated code
    if( this == obj ) {
      return true;
    }
    if( obj == null ) {
      return false;
    }
    if( getClass() != obj.getClass() ) {
      return false;
    }
    CellPosition other = ( CellPosition )obj;
    if( columnIndex != other.columnIndex ) {
      return false;
    }
    if( rowIndex != other.rowIndex ) {
      return false;
    }
    return true;
  }
  
  public String toString() {
    return   "CellPosition [columnIndex="
           + columnIndex
           + ", rowIndex="
           + rowIndex
           + "]";
  }
}


