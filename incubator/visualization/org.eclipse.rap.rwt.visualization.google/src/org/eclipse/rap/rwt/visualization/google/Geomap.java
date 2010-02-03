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
 * Renders a Google Visualization Geomap.
 * @see http://code.google.com/apis/visualization/documentation/gallery/geomap.html
 * 
 * Note that this is a partial implementation of the Google GeoMap widget.
 * I have omitted the Markers capability of this widget, as this 
 * require a Google API developer key.  Support for markers could certainly
 * be added.
 * 
 * Note that this widget is rendered upon calling the setWidgetData method.  
 * So if you wish to set options like width, height, colors, etc., you must do this 
 * using method setWidgetOptions, before calling setWidgetData.
 * 
 * Usage:<code>
 * JSONGoogleDataTable dataTable = new JSONGoogleDataTable();
    dataTable.addColumn("Country", "Country", "string", null);
    dataTable.addColumn("Happiness", "Happiness", "number", null);
    dataTable.addRow(new Object[] {"Tanzania", 25});
    dataTable.addRow(new Object[] {"US", 40});
    widgetData = dataTable.toString();
    
    Geomap geomap = new Geomap( composite, SWT.NONE );
    geomap.setWidgetOptions("{width: 500, height: 500}");
    geomap.setWidgetData(widgetData);
    gridData = new GridData(500, 500);
    geomap.setLayoutData(gridData);
    geomap.addListener(SWT.Selection, this);
   </code>
    
    <code>
    public void handleEvent(Event event) {
    log.info("Event: " + event);
    VisualizationWidget widget = (VisualizationWidget)event.widget;
    log.info( "Selected item=" + widget.getSelectedItem());
    </code>
 * @See http://code.google.com/apis/visualization/documentation/gallery/columnchart.html
 *
 */
public class Geomap extends VisualizationWidget {

  public Geomap( final Composite parent, final int style ) {
    super( parent, style );
  }

}
