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
 * Renders a Google Visualization Intensity Map.
 * @See http://code.google.com/apis/visualization/documentation/gallery/intensitymap.html
 * 
 * Note that this widget does not work properly in my testing.  
 * It always renders in the upper left corner of the composite to
 * which it is added.  In addition, this widget is not fully tested.
 * 
 * Note that this widget is rendered upon calling the setWidgetData method.  
 * So if you wish to set options like width, height, colors, etc., you must do this 
 * using method setWidgetOptions, before calling setWidgetData.
 * 
 * Usage:
 * <code>
 * JSONGoogleDataTable dataTable = new JSONGoogleDataTable();
    dataTable.addColumn("Country", "Country", "string", null);
    dataTable.addColumn("Happiness", "Happiness", "number", null);
    dataTable.addColumn("Income", "Income", "number", null);
    dataTable.addRow(new Object[] {"TZ", 25, 3});
    dataTable.addRow(new Object[] {"US", 40, 40});
    dataTable.addRow(new Object[] {"UK", 38, 35});
    widgetData = dataTable.toString();
    
    IntensityMap intensityMap = new IntensityMap( composite, SWT.NONE );
    intensityMap.setWidgetOptions("{width: 440, height: 220}");
    intensityMap.setWidgetData(widgetData);
    gridData = new GridData(440, 220);
    intensityMap.setLayoutData(gridData);
    intensityMap.addListener(SWT.Selection, this);
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
public class IntensityMap extends VisualizationWidget {

  public IntensityMap( final Composite parent, final int style ) {
    super( parent, style );
  }

}
