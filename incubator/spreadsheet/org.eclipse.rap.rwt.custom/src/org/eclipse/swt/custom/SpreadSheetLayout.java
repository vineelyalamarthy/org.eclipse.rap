// Created on 07.08.2009
package org.eclipse.swt.custom;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.*;

class SpreadSheetLayout extends Layout {
  private final SpreadSheetModel model;
  private final CellController cellController;
  private final SliderController sliderController;
  private final HeaderController headerController;

  
  SpreadSheetLayout( final SpreadSheetModel model,
                     final CellController cellController,
                     final SliderController sliderController,
                     final HeaderController headerController )
  {
    this.cellController = cellController;
    this.sliderController = sliderController;
    this.headerController = headerController;
    this.model = model;
  }

  protected Point computeSize( final Composite composite,
                               final int wHint,
                               final int hHint,
                               final boolean flushCache )
  {
    return null;
  }

  protected void layout( final Composite composite, final boolean flushCache ) {
    Rectangle clientArea = composite.getClientArea();
    model.updateVisibleRowAndColumns( clientArea );
    sliderController.adjustSlider();
    headerController.adjustHeaderControls();
    cellController.adjustCellControls();
    Control[] children = composite.getChildren();
    for( int i = 0; i < children.length; i++ ) {
      SpreadSheetUtils.computeBounds( clientArea, children[ i ], model );
    }
  }
}
