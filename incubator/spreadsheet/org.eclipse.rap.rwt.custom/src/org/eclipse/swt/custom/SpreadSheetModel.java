// Created on 22.08.2009
package org.eclipse.swt.custom;

import java.text.MessageFormat;
import java.util.*;

import org.eclipse.swt.graphics.Rectangle;


class SpreadSheetModel {
  static final int DEFAULT_ROW_HEIGHT = 20;
  static final int DEFAULT_COLUMN_WIDTH = 100;

  private final Map rows;
  private final Map columns;
  private final Map texts;
  private final Set listeners;
  private int rowOffset;
  private int columnOffset;
  private int visibleColumns;
  private int visibleRows;


  static class Event {
    private final static int ROW_HEIGHT_CHANGED = 0;
    private final static int COLUMN_WIDTH_CHANGED = 1;
    private final static int TEXT_CHANGED = 2;
    private final static int ROW_OFFSET_CHANGED = 3;
    private final static int COLUMN_OFFSET_CHANGED = 4;

    int type;
    int rowIndex;
    int columnIndex;
    int rowHeight;
    int columnWidth;
    String text;
    int rowOffset;
    int columnOffset;

    public void fire( final Listener listener ) {
      switch( type ) {
        case ROW_HEIGHT_CHANGED:
          listener.rowHeightChanged( this );
        break;

        case COLUMN_WIDTH_CHANGED:
          listener.columnWidthChanged( this );
        break;

        case TEXT_CHANGED:
          listener.textChanged( this );
        break;
        
        case ROW_OFFSET_CHANGED:
          listener.rowOffsetChanged( this );
        break;
        
        case COLUMN_OFFSET_CHANGED:
          listener.columnOffsetChanged( this );
        break;

        default:
          String txt = "Event Type ''{0}''not supported";
          Object[] param = new Object[] { new Integer( type ) };
          String msg = MessageFormat.format( txt, param );
          throw new IllegalStateException( msg );
      }
    }
  }

  static interface Listener {
    void rowHeightChanged( Event evt );
    void columnOffsetChanged( Event evt );
    void rowOffsetChanged( Event evt );
    void columnWidthChanged( Event evt );
    void textChanged( Event evt );
  }

  static abstract class Adapter implements Listener {
    public void rowHeightChanged( final Event evt ) {};
    public void columnWidthChanged( final Event evt ) {};
    public void textChanged( final Event evt ) {};
    public void columnOffsetChanged( Event evt ) {};
    public void rowOffsetChanged( Event evt ) {};
  }


  public SpreadSheetModel() {
    rows = new HashMap();
    columns = new HashMap();
    texts = new HashMap();
    listeners = new HashSet();
    visibleColumns = 0;
    visibleRows = 0;
  }

  int getRowHeight( final int rowIndex ) {
    int result = DEFAULT_ROW_HEIGHT;
    Integer key = new Integer( rowIndex );
    Integer height = ( Integer )rows.get( key );
    if( height != null ) {
      result = height.intValue();
    }
    return result;
  }

  void setRowHeight( final int rowIndex, final int rowHeight ) {
    SpreadSheetUtils.checkNotNegative( rowIndex, "rowIndex" );
    SpreadSheetUtils.checkNotNegative( rowHeight, "rowHeight" );

    Integer key = new Integer( rowIndex );
    Integer oldRowHeight = ( Integer )rows.get( key );
    Integer newRowHeight = new Integer( rowHeight );

    if( rowHeight == SpreadSheetModel.DEFAULT_ROW_HEIGHT ) {
      rows.remove( key );
    } else {
      rows.put( key, newRowHeight );
    }

    if( !newRowHeight.equals( oldRowHeight ) ) {
      Event event = new Event();
      event.type = Event.ROW_HEIGHT_CHANGED;
      event.rowIndex = rowIndex;
      event.rowHeight = rowHeight;
      fireEvent( event );
    }
  }

  int getColumnWidth( final int columnIndex ) {
    int result = DEFAULT_COLUMN_WIDTH;
    Integer key = new Integer( columnIndex );
    Integer width = ( Integer )columns.get( key );
    if( width != null ) {
      result = width.intValue();
    }
    return result;
  }

  void setColumnWidth( final int columnIndex, final int columnWidth ) {
    SpreadSheetUtils.checkNotNegative( columnIndex, "columnIndex" );
    SpreadSheetUtils.checkNotNegative( columnWidth, "columnWidth" );

    Integer key = new Integer( columnIndex );
    Integer newColumnWidth = new Integer( columnWidth );
    Integer oldColumnWidth = ( Integer )columns.get( key );

    if( columnWidth == DEFAULT_COLUMN_WIDTH ) {
      columns.remove( key );
    } else {
      columns.put( key, newColumnWidth );
    }

    if( !newColumnWidth.equals( oldColumnWidth ) ) {
      Event event = new Event();
      event.type = Event.COLUMN_WIDTH_CHANGED;
      event.columnIndex = columnIndex;
      event.columnWidth = columnWidth;
      fireEvent( event );
    }
  }

  String getText( final CellPosition position ) {
    return getText( position.getRowIndex(), position.getColumnIndex() );
  }

  String getText( final int rowIndex, final int columnIndex ) {
    SpreadSheetUtils.checkNotNegative( rowIndex, "rowIndex" );
    SpreadSheetUtils.checkNotNegative( columnIndex, "columnIndex" );

    CellPosition position = new CellPosition( rowIndex, columnIndex );
    String result = ( String )texts.get( position );
    return result == null ? "" : result;
  }

  void setText( final String text, final CellPosition position ) {
    setText( text, position.getRowIndex(), position.getColumnIndex() );
  }

  void setText( final String text,
                final int rowIndex,
                final int columnIndex )
  {
    SpreadSheetUtils.checkNotNull( text, "text" );
    SpreadSheetUtils.checkNotNegative( rowIndex, "rowIndex" );
    SpreadSheetUtils.checkNotNegative( columnIndex, "columnIndex" );

    CellPosition position = new CellPosition( rowIndex, columnIndex );
    Object oldText = texts.get( position );

    if( "".equals( text ) ) {
      texts.remove( position );
    } else {
      texts.put( position, text );
    }

    if( !text.equals( oldText ) ) {
      Event event = new Event();
      event.type = Event.TEXT_CHANGED;
      event.rowIndex = rowIndex;
      event.columnIndex = columnIndex;
      event.text = text;
      fireEvent( event );
    }
  }

  void setColumnOffset( final int columnOffset ) {
    SpreadSheetUtils.checkNotNegative( columnOffset, "columnOffset" );
    int oldOffset = this.columnOffset;
    this.columnOffset = columnOffset;
    if( oldOffset != columnOffset ) {
      Event event = new Event();
      event.type = Event.COLUMN_OFFSET_CHANGED;
      event.columnOffset = columnOffset;
      fireEvent( event );
    }
  }
  
  int getColumnOffset() {
    return columnOffset;
  }

  void setRowOffset( final int rowOffset ) {
    SpreadSheetUtils.checkNotNegative( rowOffset, "rowOffset" );
    int oldOffset = this.rowOffset;
    this.rowOffset = rowOffset;
    if( oldOffset != rowOffset ) {
      Event event = new Event();
      event.type = Event.ROW_OFFSET_CHANGED;
      event.rowOffset = rowOffset;
      fireEvent( event );
    }

  }
  
  int getRowOffset() {
    return rowOffset;
  }
  
  void updateVisibleRowAndColumns( final Rectangle clientArea ) {
    visibleColumns
      = SpreadSheetUtils.calcVisibleColumns( clientArea.width, this );
    visibleRows = SpreadSheetUtils.calcVisibleRows( clientArea.height, this );
  }
  
  int getVisibleRows() {
    return visibleRows;
  }
  
  int getVisibleColumns() {
    return visibleColumns;
  }

  void addListener( final Listener listener ) {
    listeners.add( listener );
  }

  void removeListener( final Listener listener ) {
    listeners.remove( listener );
  }

  private void fireEvent( final Event event ) {
    Listener[] lsnrs = new Listener[ listeners.size() ];
    listeners.toArray( lsnrs );
    for( int i = 0; i < lsnrs.length; i++ ) {
      event.fire( lsnrs[ i ] );
    }
  }
}
