// Created on 07.11.2009
package org.eclipse.swt.custom;


public class SpreadSheetFixture {

  private static final int COLUMN_COUNT = 25;
  private static final int ROW_COUNT = 50;

  static void populateModel( final SpreadSheetModel model ) {
    for( int i = 0; i < ROW_COUNT; i++ ) {
      for( int j = 0; j < 25; j++ ) {
        model.setText( i + "_" + j, i, j );
      }
    }
  }

  static void populateModel( final SpreadSheet spreadSheet ) {
    for( int i = 0; i < ROW_COUNT; i++ ) {
      for( int j = 0; j < COLUMN_COUNT; j++ ) {
        spreadSheet.setText( i + "_" + j, i, j );
      }
    }
  }
}
