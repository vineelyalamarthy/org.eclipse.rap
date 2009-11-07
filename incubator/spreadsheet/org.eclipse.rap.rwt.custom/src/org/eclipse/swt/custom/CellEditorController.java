// Created on 16.08.2009
package org.eclipse.swt.custom;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SpreadSheetModel.Adapter;
import org.eclipse.swt.custom.SpreadSheetModel.Event;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;


class CellEditorController {
  private final SpreadSheet spreadSheet;
  private final Text control;
  private final SpreadSheetModel model;
  

  private final class EditorKeyAdapter extends KeyAdapter {
    public void keyPressed( final KeyEvent evt ) {
      handleKeyPressed( evt );
    }
  }

  private final class ModelAdapter extends Adapter {
    public void textChanged( final Event evt ) {
      int row = evt.rowIndex;
      int column = evt.columnIndex;
      CellPosition position
        = new CellPosition( row, column );
      setText( evt.text, position );
    }
  }

  
  CellEditorController( final SpreadSheet spreadSheet,
                        final SpreadSheetModel model )
  {
    SpreadSheetUtils.checkNotNull( spreadSheet, "spreadSheet" );
    SpreadSheetUtils.checkNotNull( model, "model" );
    
    this.spreadSheet = spreadSheet;
    this.model = model;
    model.addListener( new ModelAdapter() );
    control = new Text( spreadSheet, SWT.BORDER );
    control.setLayoutData( new SpreadSheetData( 0, 0 ) );
    control.addKeyListener( new EditorKeyAdapter() );
  }

  void setText( final String text, final CellPosition position ) {
    SpreadSheetData editorData = ( SpreadSheetData )control.getLayoutData();
    if( position.equals( editorData.getPosition() ) ) {
      setText( text );
    }
  }

  void setText( final String text ) {
    control.setText( text );
  }
  
  String getText() {
    return control.getText();
  }

  boolean setFocus() {
    return control.setFocus();
  }
  
  void handleKeyPressed( final KeyEvent evt ) {
    switch( evt.keyCode ) {
      case SWT.ARROW_DOWN:
        moveCellEditor( 1, 0 );
      break;
      case SWT.ARROW_UP:
        moveCellEditor( -1, 0 );
      break;
      case SWT.ARROW_LEFT:
        moveCellEditor( 0, -1 );
      break;
      case SWT.ARROW_RIGHT:
        moveCellEditor( 0, 1 );
      break;
    }
  }

  void moveCellEditor( final int rowChange, final int columnChange )
  {
    SpreadSheetData old = ( SpreadSheetData )control.getLayoutData();
    int newRow = old.getRowIndex() + rowChange;
    int newColumn = old.getColumnIndex() + columnChange;
    if( newRow >= 0 && newColumn >= 0 ) {
      moveCellEditor( new CellPosition( newRow, newColumn ) );
    }
  }

  void moveCellEditor( final CellPosition position ) {
    SpreadSheetData oldData = ( SpreadSheetData )control.getLayoutData();
    spreadSheet.setText( control.getText(), oldData.getPosition() );
    control.setText( spreadSheet.getText( position ) );
    control.setLayoutData( new SpreadSheetData( position ) );
    Rectangle clientArea = spreadSheet.getClientArea();
    SpreadSheetUtils.computeBounds( clientArea, control, model );
  }

  Control getControl() {
    return control;
  }

  void handleMouseUp( final CellPosition position ) {
    moveCellEditor( position );
    setFocus();
  }

  void handleMouseDown( final CellPosition cell ) {
  }
}