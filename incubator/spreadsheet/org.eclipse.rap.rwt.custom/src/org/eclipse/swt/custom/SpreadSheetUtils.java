// Created on 09.08.2009
package org.eclipse.swt.custom;

import java.text.MessageFormat;

import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;


class SpreadSheetUtils {
  
  static void checkNotNegative( final int parameter,
                                final String parameterName )
  {
    if( parameter < 0 ) {
      String txt = "A negative value for parameter ''{0}'' is not allowed.";
      String msg = MessageFormat.format( txt, new Object[] { parameterName } );
      throw new IllegalArgumentException( msg );
    }
  }

  static void checkNotNull( Object parameter, String parameterName ) {
    if( parameter == null ) {
      String txt = "Parameter ''{0}'' must not be null.";
      String msg = MessageFormat.format( txt, new Object[]{ parameterName } );
      throw new IllegalArgumentException( msg );
    }
  }

  static int calcVisibleRows( final int height, final SpreadSheetModel model ) {
    int rowIndex = 0;
    int rowOffset = model.getRowOffset();
    int calculatedHeight = model.getRowHeight( rowIndex + rowOffset );
    while( calculatedHeight < height ) {
      rowIndex++;
      calculatedHeight += model.getRowHeight( rowIndex + rowOffset );
    }
    // we asume that the visible area most probably will show at least 
    // one row;
    return rowIndex + 1;
  }

  static int calcVisibleColumns( final int width, 
                                 final SpreadSheetModel model )
  {
    int columnIndex = 0;
    int columnOffset = model.getColumnOffset();
    int calculatedWidth = model.getColumnWidth( columnIndex + columnOffset );
    while( calculatedWidth < width ) {
      columnIndex++;
      calculatedWidth += model.getColumnWidth( columnIndex + columnOffset );
    }
    // we asume that the visible area most probably will show at least 
    // one column;
    return columnIndex + 1;
  }

  static int calcYPosition( final int yOffset,
                            final int startIndex,
                            final int rowIndex,
                            final SpreadSheetModel model )
  {
    int result = yOffset;
    for( int i = startIndex; i < rowIndex; i++ ) {
      result += model.getRowHeight( i );
    }
    return result;
  }

  static int calcXPosition( final int xOffset,
                            final int startIndex,
                            final int columnIndex,
                            final SpreadSheetModel model )
  {
    int result = xOffset;
    for( int i = startIndex; i < columnIndex; i++ ) {
      result += model.getColumnWidth( i );
    }
    return result;
  }

  static void computeBounds( final Rectangle clientArea,
                             final Control control,
                             final SpreadSheetModel model )
  {
    SpreadSheetData data = ( SpreadSheetData )control.getLayoutData();
    if( data != null ) {
      int x = calcXPosition( clientArea.x,
                             0,
                             data.getColumnIndex(),
                             model );
      int y = calcYPosition( clientArea.y,
                             0,
                             data.getRowIndex(),
                             model );
      int width =   model.getColumnWidth( data.getColumnIndex() )
                  - CellController.DEFAULT_GRID_LINE_BREADTH;
      int height =   model.getRowHeight( data.getRowIndex() )
                   - CellController.DEFAULT_GRID_LINE_BREADTH;
      control.setBounds( x, y, width, height );
    }
  }
  
  private SpreadSheetUtils() {
    // prevent instance creation
  }
}