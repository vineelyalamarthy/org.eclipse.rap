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
 * Base type for all of the Google Visualization implementations.
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
  
  /**
   * Constructs a visualization widget in the specified parent and style. A visualization widget by
   * default will auto-resize to fill its parent.
   * @param parent the parent composite (cannot be <code>null</code>)
   * @param style the style of this widget
   */
  protected VisualizationWidget( final Composite parent, final int style ) {
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
             if (!(widgetOptions.length() == 0)) {
                widgetSize = ", "+widgetSize;
             }
          }
          redraw();
      }
    });
  }

  /**
   * Returns the data that composes/defines the actual visualization.
   * @return a string representating the data, or <code>null</code> if no data has been set.
   */
  public String getWidgetData() {
    return widgetData;
  }

  /**
   * Sets the layout of the widget. Calling this method has no effect.
   */
  public void setLayout( final Layout layout ) {
    //noop
  }

  /**
   * Sets the data that composes/defines the actual visualization.
   * @param widgetData a JSON string that represents the data for this visualization.
   */
  public void setWidgetData( String widgetData ) {
    if (widgetData==null) {
      this.widgetData = "";
    } else {
      this.widgetData = widgetData;
    }
  }

  /**
   * Sets the configurable options for this visualization widget.
   * The actual value depends on the type of Google visualization represented by this widget.
   * @param widgetOptions the configurable options of this widget. Specifying <code>null</code> 
   * will clear the options. If a width and height are specified in the options they will override
   * the default behavior, which is to auto-resize with the parent composite.
   */
  public void setWidgetOptions( String widgetOptions ) {
    if (widgetOptions==null) {
      widgetOptions = "";
    } else {
      this.widgetOptions = widgetOptions;
    }
  }

  /**
   * Returns the configurable options for this visualization widget.
   * The actual value depends on the type of Google visualization represented by this widget.
   * @return a string representing the widget options
   */
  public String getWidgetOptions() {
    return widgetOptions+widgetSize;
  }
  
  /**
   * Asks this widget to perform a redraw which will update the visualization according to the 
   * current widget data and options.
   */
  public void redraw() {
    super.redraw();
    dirty = true;
  }
  
  /**
   * Marks this widget as 'dirty' which will update the visualization according to the 
   * current widget data and options.
   */
  public void setDirty (boolean dirty) {
    this.dirty = dirty;
  }
  
  /**
   * Returns the 'dirty' state of this widget.
   * @return <code>true</code> if the widget has been marked dirty, else <code>false</code>
   */
  public boolean isDirty () {
    return dirty; 
  }
  
  /**
   * Sets the selected item in the visualization. The actual effects of this depend on the visualization.
   * @param selectedItem a string representing the item to select in the visualization.
   */
  public void setSelectedItem( String selectedItem ) {
    this.selectedItem  = selectedItem;
  }

  /**
   * Returns the selected item in the visualization.  The actual selected item returned depends on the
   * visualization.
   * @return a string representing the selected item.
   */
  public String getSelectedItem() {
    return selectedItem;
  }

  /**
   * Sets the selected row in the visualization. The actual effects of this depend on the visualization.
   * @param selectedRow a string representing the row to select in the visualization.
   */
  public void setSelectedRow( String selectedRow ) {
    this.selectedRow = selectedRow;
  }

  /**
   * Returns the selected row in the visualization.  The actual selected item returned depends on the
   * visualization.
   * @return a string representing the selected row.
   */
  public String getSelectedRow() {
    return selectedRow;
  }

  /**
   * Sets the selected column in the visualization. The actual effects of this depend on the visualization.
   * @param selectedColumn a string representing the column to select in the visualization.
   */
  public void setSelectedColumn( String selectedColumn ) {
    this.selectedColumn = selectedColumn;
  }

  /**
   * Returns the selected column in the visualization.  The actual selected column returned depends on the
   * visualization.
   * @return a string representing the selected column.
   */
  public String getSelectedColumn() {
    return selectedColumn;
  }

  /**
   * Sets the selected value in the visualization. The actual effects of this depend on the visualization.
   * @param selectedValue a string representing the value to select in the visualization.
   */
  public void setSelectedValue( String selectedValue ) {
    this.selectedValue = selectedValue;
  }

  /**
   * Returns the selected value in the visualization.  The actual selected value returned depends on the
   * visualization.
   * @return a string representing the selected item.
   */
  public String getSelectedValue() {
    return selectedValue;
  }
  
}
