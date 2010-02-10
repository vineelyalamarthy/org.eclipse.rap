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
 * @See http://code.google.com/apis/visualization/documentation/gallery/annotatedtimeline.html
 * 
 * Note that although the documentation on this widget suggest you
 * can add columns to contain annotations of the points on the timeline,
 * I have received an error when i try to add such string columns of data.
 * 
 * Note that this widget is rendered upon calling the setWidgetData method.  
 * So if you wish to set options like width, height, colors, etc., you must do this 
 * using method setWidgetOptions, before calling setWidgetData.
 * 
 * Usage:
 * <code>
 * JSONGoogleDataTable dataTable = new JSONGoogleDataTable();
    dataTable.addColumn("Date", "Date", "date", null);
    dataTable.addColumn("AverageGPA", "Average GPA", "number", null);
    dataTable.addRow(new Object[] {new Date(1210000000), 3});
    dataTable.addRow(new Object[] {new Date(1230809560), 3.5});
    dataTable.addRow(new Object[] {new Date(), 2.85});
    widgetData = dataTable.toString();
    
    AnnotatedTimeLine timeLine = new AnnotatedTimeLine( composite, SWT.NONE );
    timeLine.setWidgetOptions("{width: 500, height: 300, displayAnnotations: true}");
    timeLine.setWidgetData(widgetData);
    gridData = new GridData(500, 300);
    timeLine.setLayoutData(gridData);
    timeLine.addListener(SWT.Selection, this);
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
  }
 *
 */
public class AnnotatedTimeLine extends VisualizationWidget {

  public AnnotatedTimeLine( Composite parent, int style ) {
    super( parent, style );
  }
  
}
