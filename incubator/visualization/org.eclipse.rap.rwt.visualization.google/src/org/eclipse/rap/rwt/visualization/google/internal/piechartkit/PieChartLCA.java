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
package org.eclipse.rap.rwt.visualization.google.internal.piechartkit;

import org.eclipse.rap.rwt.visualization.google.PieChart;
import org.eclipse.rap.rwt.visualization.google.internal.VisualizationWidgetLCA;

public class PieChartLCA extends VisualizationWidgetLCA {
  
   public Class getWidgetType () {
     return PieChart.class;
   }
}
