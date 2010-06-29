/******************************************************************************
 * Copyright © 2010-2011 Austin Riddle
 * All Rights Reserved.
 * 
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Austin Riddle - initial API and implementation
 * 
 *****************************************************************************/
package org.eclipse.rap.rwt.visualization.jit.internal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ArrayBlockingQueue;

import org.eclipse.rap.rwt.visualization.jit.JITVisualizationWidget;
import org.eclipse.rwt.lifecycle.AbstractWidgetLCA;
import org.eclipse.rwt.lifecycle.ControlLCAUtil;
import org.eclipse.rwt.lifecycle.IWidgetAdapter;
import org.eclipse.rwt.lifecycle.JSVar;
import org.eclipse.rwt.lifecycle.JSWriter;
import org.eclipse.rwt.lifecycle.WidgetLCAUtil;
import org.eclipse.rwt.lifecycle.WidgetUtil;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Widget;

public abstract class JITWidgetLCA extends AbstractWidgetLCA {
  
  private static final String WIDGET_DATA = "widgetData";
  private static final String PROP_VISIBLE = "visible";

  public abstract Class getWidgetType();
  
  protected Collection getInitializationParameters (JITVisualizationWidget vWidget) {
    ArrayList params = new ArrayList();
    String id = WidgetUtil.getId( vWidget );
    params.add(id);
    return params;
  }
  
  public void renderInitialization( final Widget widget ) throws IOException {
     JITVisualizationWidget vWidget = (JITVisualizationWidget)widget;
     JSWriter writer = JSWriter.getWriterFor( vWidget );
     writer.newWidget( getWidgetType().getCanonicalName(), getInitializationParameters(vWidget).toArray() );
     writer.set( "appearance", "composite" );
     writer.set( "overflow", "hidden" );
     ControlLCAUtil.writeStyleFlags( ( Control ) widget );
   }
  
  public void preserveValues( final Widget widget ) {
    JITVisualizationWidget vWidget = (JITVisualizationWidget)widget;
    ControlLCAUtil.preserveValues( vWidget );
    IWidgetAdapter adapter = WidgetUtil.getAdapter( vWidget );
    adapter.preserve( PROP_VISIBLE, String.valueOf(vWidget.isVisible()));
    adapter.preserve( WIDGET_DATA, new JSVar(vWidget.getJSONData()));
    // only needed for custom variants (theming)
    WidgetLCAUtil.preserveCustomVariant( vWidget );
  }

  public void renderChanges( final Widget widget ) throws IOException {
    JITVisualizationWidget vWidget = ( JITVisualizationWidget )widget;
    JSWriter writer = JSWriter.getWriterFor( vWidget );
    writer.set( PROP_VISIBLE, PROP_VISIBLE, String.valueOf(vWidget.isVisible()));
    //We compare the JSON text directly because JSVar does not override Object.equals();
    IWidgetAdapter adapter = WidgetUtil.getAdapter( vWidget );
    JSVar oldValue = (JSVar)adapter.getPreserved( WIDGET_DATA );
    String jsonData = vWidget.getJSONData();
    if (jsonData != null && (oldValue == null || !jsonData.equals(oldValue.toString()))) {
      writer.set( WIDGET_DATA, WIDGET_DATA, new JSVar(jsonData));
    }
    ControlLCAUtil.writeChanges( vWidget );
    
    WidgetCommandQueue cmdQueue = (WidgetCommandQueue) vWidget.getAdapter(WidgetCommandQueue.class);
    if (cmdQueue != null) {
      while (cmdQueue.peek() != null) {
        Object[] functionCall = (Object[])cmdQueue.poll();
        writer.call((String)functionCall[0], (Object[])functionCall[1]);
      }
    }
  }

  public void renderDispose( final Widget widget ) throws IOException {
    JSWriter writer = JSWriter.getWriterFor( widget );
    writer.dispose();
  }

  public void createResetHandlerCalls( String typePoolId ) throws IOException {
    //noop
  }

  public String getTypePoolId( Widget widget ) {
    return null;
  }
  
  /**
   * Respond to selection events, set the value of selectedItem on the widget Java object,
   * and broadcast a SWT.Selection event to any listeners  
   */
  public void readData( final Widget widget ) {
    if (widget==null) return;
    JITVisualizationWidget visWidget = ( JITVisualizationWidget )widget;
    String selectedNode = WidgetLCAUtil.readPropertyValue( visWidget, "selectedNode" );
    if (selectedNode!=null) {
      visWidget.setData("selectedNode", selectedNode);
      ControlLCAUtil.processSelection( visWidget, null, true );
    }
  }
  
  public static class WidgetCommandQueue extends ArrayBlockingQueue {
    private static final long serialVersionUID = -2296255246437590610L;

    public WidgetCommandQueue() {
      super(5);
    }
  }

}