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
 * Renders a Google Visualization Scatter Chart.
 * @see http://code.google.com/apis/visualization/documentation/gallery/scatterchart.html
 * 
 * Note that this widget is rendered upon calling the setWidgetData method.  
 * So if you wish to set options like width, height, colors, etc., you must do this 
 * using method setWidgetOptions, before calling setWidgetData.
 * 
 * Usage:
 * <code>
 * JSONGoogleDataTable dataTable = new JSONGoogleDataTable();
    dataTable.addColumn("CO2", "CO2", "number", null);
    dataTable.addColumn("CH4", "CH4", "number", null);
    dataTable.addColumn("Temperature", "Temperature", "number", null);
    dataTable.addRow(new Object[] {350, 10, 22});
    dataTable.addRow(new Object[] {375, 12, 23});
    dataTable.addRow(new Object[] {400, 16, 25});
    widgetData = dataTable.toString();
    
    ScatterChart scatterChart = new ScatterChart( composite, SWT.NONE );
    scatterChart.setWidgetOptions("{width: 300, height: 300}");
    scatterChart.setWidgetData(widgetData);
    gridData = new GridData(300, 300);
    scatterChart.setLayoutData(gridData);
    scatterChart.addListener(SWT.Selection, this);
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
public class ScatterChart extends VisualizationWidget {

  public ScatterChart( Composite parent, int style ) {
    super( parent, style );
  }
  
}
