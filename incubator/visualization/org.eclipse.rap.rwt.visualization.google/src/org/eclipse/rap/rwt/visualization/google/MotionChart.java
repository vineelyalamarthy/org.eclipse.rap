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

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.swt.widgets.Composite;

/**
 * Renders a Google Visualization Motion Chart.  This originally appeared at gapminder.org
 * as Gapminder's Trendalyzer.
 * @see http://code.google.com/apis/visualization/documentation/gallery/scatterchart.html
 * @see http://www.ted.com/index.php/talks/hans_rosling_shows_the_best_stats_you_ve_ever_seen.html
 * 
 * Note that this widget is rendered upon calling the setWidgetData method.  
 * So if you wish to set options like width, height, colors, etc., you must do this 
 * using method setWidgetOptions, before calling setWidgetData.
 * 
 * Usage:
 * <code>
 * JSONGoogleDataTable dataTable = new JSONGoogleDataTable();
    dataTable.addColumn("Model", "Model", "string", null);
    dataTable.addColumn("thedate", "Date", "date", null);
    dataTable.addColumn("CO2", "CO2", "number", null);
    dataTable.addColumn("Temperature", "Temperature", "number", null);
    dataTable.addRow(new Object[] {"Model1", new Date(), 389, 14.8});
    dataTable.addRow(new Object[] {"Model1", new Date(4070908800), 450, 19});
    dataTable.addRow(new Object[] {"Model2", new Date(), 389, 14.8});
    dataTable.addRow(new Object[] {"Model2", new Date(4070908800), 700, 23});
    String serializedData = dataTable.toString();
     
    MotionChart motionChart = new MotionChart( composite, SWT.NONE );
    motionChart.setWidgetOptions("width: 500, height: 300");
    motionChart.setWidgetData(serializedData);
    GridData gridData = new GridData(500, 300);
    motionChart.setLayoutData(gridData);
    </code>
 *   
 */
public class MotionChart extends VisualizationWidget {

  protected Set listeners;
  
  public MotionChart( Composite parent, int style ) {
    super( parent, style );
  }
  
  public static interface StateListener {
    void stateChanged (String state);
  }
  
  public void notifyListeners (String state) {
    if (listeners != null) {
      StateListener[] listenerArray = (StateListener[])listeners.toArray(new StateListener[listeners.size()]);
      for (int i = 0; i < listenerArray.length; i++) {
         try {
           listenerArray[i].stateChanged(state);
         } catch (Exception e) {
           e.printStackTrace();
         }
       }
    }
  }

  public void addStateListener(StateListener stateListener) {
    if (listeners == null) { 
      listeners = Collections.synchronizedSet(new LinkedHashSet());
    }
    listeners.add(stateListener);
  }
  public void removeStateListener(StateListener stateListener) {
    if (listeners != null) { 
      listeners.remove(stateListener);
    }
  }
  
}
