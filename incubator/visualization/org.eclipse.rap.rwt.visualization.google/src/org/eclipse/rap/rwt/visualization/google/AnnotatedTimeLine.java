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
 * Renders a Google Visualization Annotated Timeline.
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
 * dataTable.addColumn("Date", "Date", "date", null);
 * dataTable.addColumn("AverageGPA", "Average GPA", "number", null);
 * dataTable.addRow(new Object[] {new Date(1210000000), 3});
 * dataTable.addRow(new Object[] {new Date(1230809560), 3.5});
 * dataTable.addRow(new Object[] {new Date(), 2.85});
 * widgetData = dataTable.toString();
 *   
 * AnnotatedTimeLine timeLine = new AnnotatedTimeLine( composite, SWT.NONE );
 * timeLine.setWidgetOptions("{width: 500, height: 300, displayAnnotations: true}");
 * timeLine.setWidgetData(widgetData);
 * timeLine.addListener(SWT.Selection, this);
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
 *  @see <a href="http://code.google.com/apis/visualization/documentation/gallery/annotatedtimeline.html">Annotated Timeline Example</a>
 *
 */
public class AnnotatedTimeLine extends VisualizationWidget {

  public AnnotatedTimeLine( Composite parent, int style ) {
    super( parent, style );
  }
  
}
