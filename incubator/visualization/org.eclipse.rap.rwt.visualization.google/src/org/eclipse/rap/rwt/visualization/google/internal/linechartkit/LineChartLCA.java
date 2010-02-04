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
package org.eclipse.rap.rwt.visualization.google.internal.linechartkit;

import java.io.IOException;

import org.eclipse.rap.rwt.visualization.google.LineChart;
import org.eclipse.rap.rwt.visualization.google.internal.VisualizationWidgetLCA;
import org.eclipse.rwt.lifecycle.ControlLCAUtil;
import org.eclipse.rwt.lifecycle.JSWriter;
import org.eclipse.swt.widgets.Widget;

public class LineChartLCA extends VisualizationWidgetLCA {
  protected static final String REDRAW_FUNC = "drawChart";
  
  public Class getWidgetType () {
    return LineChart.class;
  }
  
  public void renderChanges( final Widget widget ) throws IOException {
    super.renderChanges(widget);
    LineChart vWidget = ( LineChart )widget;
    ControlLCAUtil.writeChanges( vWidget );
    JSWriter writer = JSWriter.getWriterFor( vWidget );
    //HACK to force redraw when requested
    if (vWidget.isDirty()) {
      writer.call( REDRAW_FUNC, null );
      vWidget.setDirty(false);
    }
  }
}
