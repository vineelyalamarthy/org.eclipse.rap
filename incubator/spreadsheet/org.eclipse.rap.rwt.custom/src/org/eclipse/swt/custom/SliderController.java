// Created on 22.08.2009
package org.eclipse.swt.custom;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Slider;


public class SliderController {
  static final int PAGE_INCREMENT_OFFSET = 2;
  static final int SLIDER_BREADTH = 20;

  private final Composite spreadSheet;
  private final HeaderController headerController;
  private final Slider horizontalSlider;
  private final Slider verticalSlider;
  private final SpreadSheetModel model;


  protected SliderController( final Composite spreadSheet,
                              final HeaderController headerController,
                              final SpreadSheetModel model )
  {
    SpreadSheetUtils.checkNotNull( spreadSheet, "spreadSheet" );
    SpreadSheetUtils.checkNotNull( headerController, "headerController" );

    this.spreadSheet = spreadSheet;
    this.headerController = headerController;
    this.model = model;
    horizontalSlider = new Slider( spreadSheet, SWT.H_SCROLL );
    horizontalSlider.addSelectionListener( new SelectionAdapter() {
      public void widgetSelected( final SelectionEvent evt ) {
        int selection = SliderController.this.horizontalSlider.getSelection();
        SliderController.this.model.setColumnOffset( selection );
      }
    } );

    verticalSlider = new Slider( spreadSheet, SWT.V_SCROLL );
    verticalSlider.addSelectionListener( new SelectionAdapter() {
      public void widgetSelected( final SelectionEvent evt ) {
        int selection = SliderController.this.verticalSlider.getSelection();
        SliderController.this.model.setRowOffset( selection );
      };
    } );
  }

  int getBreadth() {
    return SliderController.SLIDER_BREADTH;
  }

  void adjustSlider() {
    Rectangle clientArea = spreadSheet.getClientArea();
    adjustHorizontalSlider( clientArea );
    adjustVerticalSlider( clientArea );
  }

  private void adjustVerticalSlider( final Rectangle clientArea ) {
    int vX = clientArea.width + headerController.getRowHeaderWidth();
    int vHeight = clientArea.height + getBreadth();
    verticalSlider.setBounds( vX, 0, getBreadth(), vHeight );
    int rows = model.getVisibleRows();
    verticalSlider.setMinimum( 0 );
    verticalSlider.setMaximum( rows + PAGE_INCREMENT_OFFSET );
    verticalSlider.setThumb( rows );
    verticalSlider.setSelection( model.getRowOffset() );
  }

  private void adjustHorizontalSlider( final Rectangle clientArea ) {
    int hY = clientArea.height + getBreadth();
    int hWidth = clientArea.width + headerController.getRowHeaderWidth();
    horizontalSlider.setBounds( 0, hY, hWidth, getBreadth() );
    int columns = model.getVisibleColumns();
    horizontalSlider.setMinimum( 0 );
    horizontalSlider.setMaximum( columns + PAGE_INCREMENT_OFFSET );
    horizontalSlider.setThumb( columns );
    horizontalSlider.setSelection( model.getColumnOffset() );
  }
}
