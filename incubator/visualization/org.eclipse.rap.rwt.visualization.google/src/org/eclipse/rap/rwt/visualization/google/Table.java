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

import java.util.logging.Logger;

import org.eclipse.rap.rwt.visualization.google.internal.tablekit.TableLCA;
import org.eclipse.swt.widgets.Composite;

/**
 * Renders a Google Visualization Table widget.
 * @See http://code.google.com/apis/visualization/documentation/gallery/table.html
 * 
 * Note that this widget is rendered upon calling the setWidgetData method.  
 * So if you wish to set options like width, height, colors, etc., you must do this 
 * using method setWidgetOptions, before calling setWidgetData.
 * 
 * Usage:
 * <code>
 * JSONGoogleDataTable dataTable = new JSONGoogleDataTable();
    dataTable.addColumn("theyear", "Date", "string", null);
    dataTable.addColumn("CO2", "CO2", "number", null);
    dataTable.addColumn("Temperature", "Temperature (C)", "number", null);
    dataTable.addRow(new Object[] {"1970", 325, 14.1});
    dataTable.addRow(new Object[] {"2009", 389, 14.8});
    String serializedData = dataTable.toString();
    
    Table table = new Table( composite, SWT.NONE );
    table.setWidgetOptions("{width: 300, height: 300}");
    table.setWidgetData(serializedData);
    gridData = new GridData(300, 300);
    table.setLayoutData(gridData);
    table.addListener(SWT.Selection, this);
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
public class Table extends VisualizationWidget {

//  private static final Logger log = Logger.getLogger(TableLCA.class);
  
  private String selectedItem;
//  private List<SelectionListener> listeners = new ArrayList<SelectionListener>();
  
  public Table( final Composite parent, final int style ) {
    super( parent, style );
  }

//  public void addSelectionListener(SelectionListener listener) {
//    listeners.add(listener);
//  }
  
//  public void addSelectionListener (SelectionListener listener) {
//    checkWidget ();
//    TypedListener typedListener = new TypedListener (listener);
//    addListener (SWT.Selection,typedListener);
//    addListener (SWT.DefaultSelection,typedListener);
//  }
  
//  public void removeSelectionListener(SelectionListener listener) {
//    listeners.remove(listener);
//  }
  
//  public void removeSelectionListener (SelectionListener listener) {
//    checkWidget();
//    TypedListener typedListener = new TypedListener(listener);
//    removeListener(SWT.Selection,typedListener);
//    removeListener(SWT.DefaultSelection,typedListener);
//  }
  
//  private void selectionChanged() {
//    Event event = new Event();
//    event.data = selectedItem;
//    event.text = selectedItem;
//    event.widget = this;
//    VisualizationSelectionEvent selectionEvent = new VisualizationSelectionEvent(event);
//    selectionEvent.setWidget(this);
//    selectionEvent.setSource(this);
//    selectionEvent.data = selectedItem;
//    selectionEvent.text = selectedItem;
//    for (SelectionListener listener: listeners) {
//      listener.widgetSelected( selectionEvent );
//    }
//  }
  
  
}
