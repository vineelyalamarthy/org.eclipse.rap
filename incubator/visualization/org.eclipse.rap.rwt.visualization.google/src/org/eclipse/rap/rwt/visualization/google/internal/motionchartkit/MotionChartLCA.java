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
package org.eclipse.rap.rwt.visualization.google.internal.motionchartkit;

import org.eclipse.rap.rwt.visualization.google.MotionChart;
import org.eclipse.rap.rwt.visualization.google.internal.VisualizationWidgetLCA;
import org.eclipse.rwt.lifecycle.WidgetLCAUtil;
import org.eclipse.swt.widgets.Widget;

public class MotionChartLCA extends VisualizationWidgetLCA {
  
  /*
   * Initial creation procedure of the widget
   */
  @Override
  public Class getWidgetType () {
     return MotionChart.class;
  }
  
  @Override
  public void readData(Widget widget) {
    super.readData(widget);
    if (widget==null) return;
    MotionChart visWidget = ( MotionChart )widget;
    String state = WidgetLCAUtil.readPropertyValue( visWidget, "state" );
    if (state != null) {
      visWidget.notifyListeners(state.replaceAll("~", "\""));
    }
  }
}
