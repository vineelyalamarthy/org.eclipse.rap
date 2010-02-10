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

import org.eclipse.swt.widgets.Composite;

/**
 * Renders a Google Visualization Line Chart.
 * @see http://code.google.com/apis/visualization/documentation/gallery/linechart.html
 * 
 * Note that this widget is rendered upon calling the setWidgetData method.  
 * So if you wish to set options like width, height, colors, etc., you must do this 
 * using method setWidgetOptions, before calling setWidgetData.
 * 
 * Usage:
 * <code>
 * JSONGoogleDataTable dataTable = new JSONGoogleDataTable();
    dataTable.addColumn("Month", "Month", "string", null);
    dataTable.addColumn("Provider1", "Provider 1", "number", null);
    dataTable.addColumn("Provider2", "Provider 2", "number", null);
    dataTable.addColumn("Provider3", "Provider 3", "number", null);
    dataTable.addRow(new Object[] {"May", 10, 15, 20});
    dataTable.addRow(new Object[] {"June", 12, 23, 33});
    dataTable.addRow(new Object[] {"July", 11, 25, 50});
    widgetData = dataTable.toString();
    
    LineChart lineChart = new LineChart( composite, SWT.NONE );
    lineChart.setWidgetOptions("{width: 300, height: 300}");
    lineChart.setWidgetData(widgetData);
    gridData = new GridData(300, 300);
    lineChart.setLayoutData(gridData);
    lineChart.addListener(SWT.Selection, this);
    </code>
    
    <code>
    public void handleEvent(Event event) {
    log.info("Event: " + event);
    VisualizationWidget widget = (VisualizationWidget)event.widget;
    log.info( "Selected item=" + widget.getSelectedItem() + 
        "; row=" + widget.getSelectedRow() +
        "; column=" + widget.getSelectedColumn() +
        "; value=" + widget.getSelectedValue());
    </code>
 *
 */
public class LineChart extends VisualizationWidget {

  protected String prevWidgetData;
  protected String prevWidgetOptions;
  protected boolean dirty;
  
  public LineChart( Composite parent, int style ) {
    super( parent, style );
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
}
