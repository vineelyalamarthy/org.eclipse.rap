// Created on 08.08.2009
package org.eclipse.swt.custom;


import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SpreadSheetModel.Adapter;
import org.eclipse.swt.custom.SpreadSheetModel.Event;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Layout;


public class SpreadSheet extends Composite {
  private final SpreadSheetLayout layout;
  private final SpreadSheetModel model;
  private final CellController cellController;
  private final CellEditorController cellEditorController;
  private final SliderController sliderController;
  private final HeaderController headerController;

  
  private final class ModelAdapter extends Adapter {
    public void columnWidthChanged( final Event evt ) {
      triggerLayout();
    }
    public void rowHeightChanged( final Event evt ) {
      triggerLayout();
    }
    public void columnOffsetChanged( final Event event ) {
      triggerLayout();
    }
    public void rowOffsetChanged( final Event event ) {
      triggerLayout();
    }
  }

  
  public SpreadSheet( final Composite parent, final int style ) {
    // TODO [fappel]: checkStyle( style ) implementation
    super( parent, style );
    setBackground( getDisplay().getSystemColor( SWT.COLOR_GRAY ) );

    model = new SpreadSheetModel();
    cellEditorController = new CellEditorController( this, model );
    cellController = new CellController( this, model );
    headerController = new HeaderController( this, model );
    sliderController = new SliderController( this, headerController, model );
    model.addListener( new ModelAdapter() );
    layout = new SpreadSheetLayout( model,
                                    cellController,
                                    sliderController,
                                    headerController );
    super.setLayout( layout );
  }

  private void triggerLayout() {
    layout();
    cellEditorController.setFocus();
  }

  public Rectangle getClientArea() {
    checkWidget();
    Rectangle clientArea = super.getClientArea();
    int xPos = headerController.getRowHeaderWidth();
    int yPos = headerController.getColumnHeaderHeight();
    int width =   clientArea.width
                - sliderController.getBreadth()
                - headerController.getRowHeaderWidth();
    int height =   clientArea.height
                 - sliderController.getBreadth()
                 - headerController.getColumnHeaderHeight();
    return new Rectangle( xPos, yPos, width, height );
  }

  public String getText( final int rowIndex, final int columnIndex ) {
    checkWidget();
    return model.getText( rowIndex, columnIndex );
  }

  public String getText( final CellPosition position ) {
    checkWidget();
    return getText( position.getRowIndex(), position.getColumnIndex() );
  }

  public void setText( final String text,
                       final int rowIndex,
                       final int columnIndex )
  {
    checkWidget();
    model.setText( text, rowIndex, columnIndex );
  }

  public void setText( final String text, final CellPosition position ) {
    checkWidget();
    setText( text, position.getRowIndex(), position.getColumnIndex() );
  }

  public void setColumnOffset( final int columnOffset ) {
    checkWidget();
    model.setColumnOffset( columnOffset );
  }

  public void setRowOffset( final int rowOffset ) {
    checkWidget();
    model.setRowOffset( rowOffset );
  }

  CellEditorController getCellEditorController() {
    return cellEditorController;
  }

  /////////////////
  // start override

  public boolean setFocus() {
    return cellEditorController.setFocus();
  }

  public void setLayout( final Layout layout ) {
    checkWidget();

    String msg = "Changing the layout algorithm is not supported.";
    throw new UnsupportedOperationException( msg );
  }

  // end override
  ///////////////
}
