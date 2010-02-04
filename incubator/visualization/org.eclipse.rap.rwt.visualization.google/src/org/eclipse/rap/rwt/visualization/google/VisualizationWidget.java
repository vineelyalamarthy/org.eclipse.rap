/*******************************************************************************
 * Copyright (c) 2009-2010 David Donahue and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     David Donahue - initial API, implementation and documentation
 *     Austin Riddle - improvements to widget hierarchy and data flow for 
 *                     consistency with SWT behavior.
 ******************************************************************************/
package org.eclipse.rap.rwt.visualization.google;

import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Layout;

/**
 * Abstract class for rendering any of the Google Visualization API UI elements.
 * 
 * @See http://code.google.com/apis/visualization/
 *
 */
public abstract class VisualizationWidget extends Composite {
  
  private String widgetData = "";
  private String widgetOptions = "";
  private String widgetSize = "";
  private String selectedItem = "";
  private String selectedRow = "";
  private String selectedColumn = "";
  private String selectedValue = "";
  private boolean dirty;
  
  public VisualizationWidget( final Composite parent, final int style ) {
    super( parent, style );
    this.addControlListener(new ControlAdapter() {
      public void controlResized(ControlEvent e)
      {
          Point psz = getSize();
          if (widgetOptions.matches(".*width:.*") &&
              widgetOptions.matches(".*height:.*")) {
             widgetSize = "";
          }
          else {
             widgetSize = "width: "+psz.x+", height: "+psz.y;
             if (!widgetOptions.isEmpty()) {
                widgetSize = ", "+widgetSize;
             }
          }
          redraw();
      }
    });
  }

  public String getWidgetData() {
    return widgetData;
  }

  /*
   * Intentionally commented out as a Motion Chart cannot have a layout
   */
  public void setLayout( final Layout layout ) {
  }

  
  public void setWidgetData( String widgetData ) {
    if (widgetData==null) {
      this.widgetData = "";
    } else {
      this.widgetData = widgetData;
    }
  }

  public void setWidgetOptions( String widgetOptions ) {
    if (widgetOptions==null) {
      widgetOptions = "";
    } else {
      this.widgetOptions = widgetOptions;
    }
  }

  public String getWidgetOptions() {
    return widgetOptions+widgetSize;
  }
  
  public void redraw() {
    super.redraw();
    dirty = true;
  }
  
  public void setDirty (boolean dirty) {
    this.dirty = dirty;
  }
  
  public boolean isDirty () {
    return dirty; 
  }
  
  public void setSelectedItem( String selectedItem ) {
    this.selectedItem  = selectedItem;
  }

  public String getSelectedItem() {
    return selectedItem;
  }

  public void setSelectedRow( String selectedRow ) {
    this.selectedRow = selectedRow;
  }

  public String getSelectedRow() {
    return selectedRow;
  }

  public void setSelectedColumn( String selectedColumn ) {
    this.selectedColumn = selectedColumn;
  }

  public String getSelectedColumn() {
    return selectedColumn;
  }

  public void setSelectedValue( String selectedValue ) {
    this.selectedValue = selectedValue;
  }

  public String getSelectedValue() {
    return selectedValue;
  }
  
}
