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
* <p> 
 * This visualization is configured using the widget data and options 
 * set by <code>setWidgetData()</code> and <code>setWidgetOptions()</code>.  
 * Note that if the widget data or options are changed after initial rendering, 
 * the <code>redraw()</code> method should be called to render the changes.  
 * </p>
 * <p>
 * <b>Usage:</b>
 * <pre>
 * JSONGoogleDataTable dataTable = new JSONGoogleDataTable();
 * dataTable.addColumn("CO2", "CO2", "number", null);
 * dataTable.addColumn("CH4", "CH4", "number", null);
 * dataTable.addColumn("Temperature", "Temperature", "number", null);
 * dataTable.addRow(new Object[] {389, 1800, 14});
 * widgetData = dataTable.toString();
 *   
 * Gauge gauge = new Gauge( composite, SWT.NONE );
 * gauge.setWidgetOptions("{width: 300, height: 300}");
 * gauge.setWidgetData(widgetData);
 * </pre>
 * </p>
 * <pre>
 * public void handleEvent(Event event) {
 *   System.out.println("Event: " + event);
 *   VisualizationWidget widget = (VisualizationWidget)event.widget;
 *   System.out.println("Selected item=" + widget.getSelectedItem() + 
 *     "; row=" + widget.getSelectedRow() +
 *     "; column=" + widget.getSelectedColumn() +
 *     "; value=" + widget.getSelectedValue());
 * }
 * </pre>
 * </p>
 * @see <a href="http://code.google.com/apis/visualization/documentation/gallery/gauge.html">Gauge Example</a>
 */
public class Gauge extends VisualizationWidget {

  public Gauge( final Composite parent, final int style ) {
    super( parent, style );
  }

}
