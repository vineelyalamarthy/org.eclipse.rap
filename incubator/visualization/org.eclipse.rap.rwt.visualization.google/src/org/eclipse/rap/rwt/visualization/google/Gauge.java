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
 * Renders a Google Visualization Gauge.
 * @See http://code.google.com/apis/visualization/documentation/gallery/gauge.html
 * 
 * Note that this widget is rendered upon calling the setWidgetData method.  
 * So if you wish to set options like width, height, colors, etc., you must do this 
 * using method setWidgetOptions, before calling setWidgetData.
 * 
 * Usage:<code>
    JSONGoogleDataTable dataTable = new JSONGoogleDataTable();
    dataTable.addColumn("CO2", "CO2", "number", null);
    dataTable.addColumn("CH4", "CH4", "number", null);
    dataTable.addColumn("Temperature", "Temperature", "number", null);
    dataTable.addRow(new Object[] {389, 1800, 14});
    widgetData = dataTable.toString();
    
    Gauge gauge = new Gauge( composite, SWT.NONE );
    gauge.setWidgetOptions("{width: 300, height: 300}");
    gauge.setWidgetData(widgetData);
    gridData = new GridData(300, 300);
    gauge.setLayoutData(gridData);
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
public class Gauge extends VisualizationWidget {

  public Gauge( final Composite parent, final int style ) {
    super( parent, style );
  }

}
