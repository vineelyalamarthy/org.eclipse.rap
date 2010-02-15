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
package org.eclipse.rap.rwt.visualization.google.internal;

import java.io.IOException;

import org.eclipse.rap.rwt.visualization.google.VisualizationWidget;
import org.eclipse.rwt.lifecycle.AbstractWidgetLCA;
import org.eclipse.rwt.lifecycle.ControlLCAUtil;
import org.eclipse.rwt.lifecycle.IWidgetAdapter;
import org.eclipse.rwt.lifecycle.JSWriter;
import org.eclipse.rwt.lifecycle.WidgetLCAUtil;
import org.eclipse.rwt.lifecycle.WidgetUtil;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Widget;

/**
 * <p>
 * Base type for Google Visualizations.
 * </p>
 * 
 * <p>
 * Note that changes to the data or options of a widget obtained via <code>getWidgetData()</code> 
 * and <code>getWidgetOptions()</code> will be propagated to the client side, but the changes
 * will not be rendered until <code>isDirty()</code> returns true.  At that point the client-side widget will
 * be told to redraw.   
 * </p>
 */
public abstract class VisualizationWidgetLCA extends AbstractWidgetLCA {
  protected static final String REDRAW_FUNC = "redraw";
  protected static final String PROP_DATA = "widgetData";
  protected static final String PROP_OPTIONS = "widgetOptions";

  public abstract Class getWidgetType();
  
  public void renderInitialization( final Widget widget ) throws IOException {
     JSWriter writer = JSWriter.getWriterFor( widget );
     String id = WidgetUtil.getId( widget );
     writer.newWidget( getWidgetType().getName(), new Object[]{
       id
     } );
     writer.set( "appearance", "composite" );
     writer.set( "overflow", "hidden" );
     ControlLCAUtil.writeStyleFlags( ( Control ) widget );
   }
  
  public void preserveValues( final Widget widget ) {
    ControlLCAUtil.preserveValues( ( Control )widget );
    IWidgetAdapter adapter = WidgetUtil.getAdapter( widget );
    adapter.preserve( PROP_OPTIONS, ( ( VisualizationWidget )widget ).getWidgetOptions() );
    adapter.preserve( PROP_DATA, ( ( VisualizationWidget )widget ).getWidgetData() );
    // only needed for custom variants (theming)
//    WidgetLCAUtil.preserveCustomVariant( widget );
  }

  public void renderChanges( final Widget widget ) throws IOException {
    VisualizationWidget vWidget = ( VisualizationWidget )widget;
    ControlLCAUtil.writeChanges( vWidget );
    JSWriter writer = JSWriter.getWriterFor( vWidget );
    writer.set( PROP_OPTIONS, PROP_OPTIONS, vWidget.getWidgetOptions() );
    writer.set( PROP_DATA, PROP_DATA, vWidget.getWidgetData() );
    if (vWidget.isDirty()) {
      writer.call( REDRAW_FUNC, null );
      vWidget.setDirty(false);
    }
  }

  public void renderDispose( final Widget widget ) throws IOException {
    JSWriter writer = JSWriter.getWriterFor( widget );
    writer.dispose();
  }

  public void createResetHandlerCalls( String typePoolId ) throws IOException {
     //preserved empty here for pre RAP 1.3 compatibility
  }

  public String getTypePoolId( Widget widget ) {
   //preserved empty here for pre RAP 1.3 compatibility
    return null;
  }
  
  /**
   * Respond to selection events, set the value of selectedItem on the widget Java object,
   * and broadcast a SWT.Selection event to any listeners  
   */
  public void readData( final Widget widget ) {
    if (widget==null) return;
    VisualizationWidget visWidget = ( VisualizationWidget )widget;
    String selectedItem = WidgetLCAUtil.readPropertyValue( visWidget, "selectedItem" );
    String selectedRow = WidgetLCAUtil.readPropertyValue( visWidget, "selectedRow" );
    String selectedColumn = WidgetLCAUtil.readPropertyValue( visWidget, "selectedColumn" );
    String selectedValue = WidgetLCAUtil.readPropertyValue( visWidget, "selectedValue" );
    if (selectedItem!=null) {
      visWidget.setSelectedItem( selectedItem );
      visWidget.setSelectedRow( selectedRow );
      visWidget.setSelectedColumn( selectedColumn );
      visWidget.setSelectedValue( selectedValue );
      ControlLCAUtil.processSelection( visWidget, null, true );
    }
  }
}
