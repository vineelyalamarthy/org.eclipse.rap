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
 * Renders a Google Visualization Pie Chart.
 * @See http://code.google.com/apis/visualization/documentation/gallery/piechart.html
 * 
 * Note that this widget is rendered upon calling the setWidgetData method.  
 * So if you wish to set options like width, height, colors, etc., you must do this 
 * using method setWidgetOptions, before calling setWidgetData.
 * 
 * Usage:
 * <code>
 * JSONGoogleDataTable dataTable = new JSONGoogleDataTable();
    dataTable.addColumn("Activity", "Activity", "string", null);
    dataTable.addColumn("Hours", "Hours per Week", "number", null);
    dataTable.addRow(new Object[] {"software architect", 40});
    dataTable.addRow(new Object[] {"primary care medicine", 9});
    dataTable.addRow(new Object[] {"open source development", 10});
    widgetData = dataTable.toString();
    
    PieChart pieChart = new PieChart( composite, SWT.NONE );
    pieChart.setWidgetOptions("{width: 300, height: 300}");
    pieChart.setWidgetData(widgetData);
    gridData = new GridData(300, 300);
    pieChart.setLayoutData(gridData);
    pieChart.addListener(SWT.Selection, this);
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
public class PieChart extends VisualizationWidget {

  public PieChart( Composite parent, int style ) {
    super( parent, style );
  }
  
}
