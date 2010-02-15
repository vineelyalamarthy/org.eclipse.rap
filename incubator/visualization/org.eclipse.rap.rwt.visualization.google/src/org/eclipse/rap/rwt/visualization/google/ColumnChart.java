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
 * Renders a Google Visualization Column Chart.
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
 * dataTable.addColumn("theyear", "Date", "string", null);
 * dataTable.addColumn("CO2", "CO2", "number", null);
 * dataTable.addColumn("Temperature", "Temperature", "number", null);
 * dataTable.addRow(new Object[] {"1970", 325, 14.1});
 * dataTable.addRow(new Object[] {"2009", 389, 14.7});
 * widgetData = dataTable.toString();
 *  
 * ColumnChart chart = new ColumnChart( composite, SWT.NONE );
 * chart.setWidgetOptions("{width: 300, height: 300}");
 * chart.setWidgetData(serializedData);
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
 * @see <a href="http://code.google.com/apis/visualization/documentation/gallery/columnchart.html">Column Chart Example</a>
 */
public class ColumnChart extends VisualizationWidget {

  public ColumnChart( final Composite parent, final int style ) {
    super( parent, style );
  }

}
