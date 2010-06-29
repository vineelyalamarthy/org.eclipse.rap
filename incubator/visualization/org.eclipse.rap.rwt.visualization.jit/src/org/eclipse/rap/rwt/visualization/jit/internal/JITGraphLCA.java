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

import org.eclipse.rap.rwt.visualization.jit.JITGraphWidget;
import org.eclipse.rwt.lifecycle.IWidgetAdapter;
import org.eclipse.rwt.lifecycle.JSWriter;
import org.eclipse.rwt.lifecycle.WidgetUtil;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Widget;

public abstract class JITGraphLCA extends JITWidgetLCA {
  
  private static final String PROP_EDGE_COLOR = "edgeColor";
  private static final String PROP_NODE_COLOR = "nodeColor";
  
  
  public void preserveValues( final Widget widget ) {
    super.preserveValues(widget);
    JITGraphWidget vWidget = (JITGraphWidget)widget;
    IWidgetAdapter adapter = WidgetUtil.getAdapter( vWidget );
    adapter.preserve( PROP_NODE_COLOR, convertRGBToCSSString(vWidget.getNodeColor()));
    adapter.preserve( PROP_EDGE_COLOR, convertRGBToCSSString(vWidget.getEdgeColor()));
  }

  public void renderChanges( final Widget widget ) throws IOException {
    super.renderChanges(widget);
    JITGraphWidget vWidget = ( JITGraphWidget )widget;
    JSWriter writer = JSWriter.getWriterFor( vWidget );
    writer.set( PROP_NODE_COLOR, PROP_NODE_COLOR, convertRGBToCSSString(vWidget.getNodeColor()));
    writer.set( PROP_EDGE_COLOR, PROP_EDGE_COLOR, convertRGBToCSSString(vWidget.getEdgeColor()));
  }

  protected String convertRGBToCSSString(RGB color) {
    if (color == null) return "";
    StringBuilder sb = new StringBuilder("rgb(");
    sb.append(color.red).append(",").
    append(color.green).append(",").
    append(color.blue).append(")").toString();
    return sb.toString();
  }
  
}