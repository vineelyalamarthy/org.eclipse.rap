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
 * Renders a Google Visualization Motion Chart.
 * <p> 
 * This visualization is configured using the widget data and options 
 * set by <code>setWidgetData()</code> and <code>setWidgetOptions()</code>.  
 * Note that if the widget data or options are changed after initial rendering, 
 * the <code>redraw()</code> method should be called to render the changes.  
 * </p>
 * <p>
 * The Motion Chart 'state' parameter can be set in the widget options.
 * Changes to the state may be listened to using <code>addStateListener()</code> 
 * </p>
 * <p>
 * <b>Usage:</b>
 * <pre>
 * JSONGoogleDataTable dataTable = new JSONGoogleDataTable();
 * dataTable.addColumn("Model", "Model", "string", null);
 * dataTable.addColumn("thedate", "Date", "date", null);
 * dataTable.addColumn("CO2", "CO2", "number", null);
 * dataTable.addColumn("Temperature", "Temperature", "number", null);
 * dataTable.addRow(new Object[] {"Model1", new Date(), 389, 14.8});
 * dataTable.addRow(new Object[] {"Model1", new Date(4070908800), 450, 19});
 * dataTable.addRow(new Object[] {"Model2", new Date(), 389, 14.8});
 * dataTable.addRow(new Object[] {"Model2", new Date(4070908800), 700, 23});
 * String serializedData = dataTable.toString();
 *    
 * MotionChart motionChart = new MotionChart( composite, SWT.NONE );
 * motionChart.setWidgetOptions("width: 500, height: 300");
 * motionChart.setWidgetData(serializedData);
 * motionChart.addStateListener(this);
 * </pre>
 * </p>
 * <pre>
 * public void stateChanged (String state) {
 *   System.out.println(state);
 * }
 * </pre>
 * <p>    
 * The concept originally appeared at gapminder.org as Gapminder's Trendalyzer.
 * </p>
 * @see <a href="http://code.google.com/apis/visualization/documentation/gallery/motionchart.html">Motion Chart Example</a>
 *
 */
public class MotionChart extends VisualizationWidget {

  protected Set listeners;
  
  /**
   * Constructs a motion chart widget in the specified parent and style. 
   * A visualization widget by default will auto-resize to fill its parent.
   * <p>
   * The style value is either one of the style constants defined in
   * class <code>SWT</code> which is applicable to instances of this
   * class, or must be built by <em>bitwise OR</em>'ing together
   * (that is, using the <code>int</code> "|" operator) two or more
   * of those <code>SWT</code> style constants. The class description
   * lists the style constants that are applicable to the class, if any.
   * Style bits are also inherited from superclasses.
   * </p>
   * @param parent the parent composite (cannot be <code>null</code>)
   * @param style the style bits of the widget
   * @exception IllegalArgumentException <ul>
   *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
   * </ul>
   * @exception SWTException <ul>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
   * </ul>
   *
   */
  public MotionChart( Composite parent, int style ) {
    super( parent, style );
  }
  
  /**
   * Listener interface for widget 'state' option changes. 
   */
  public static interface StateListener {
    void stateChanged (String state);
  }
  
  /**
   * Notifies state listeners that the Motion Chart 'state' option has changed. 
   * @param state the new state of teh chart
   */
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

  /**
   * Adds a listener to be notified of changes to the Motion Chart 'state' option.
   * Multiple registrations of the same listener have no effect.
   * @param stateListener the listener to add. Cannot be <code>null</code>.
   */
  public void addStateListener(StateListener stateListener) {
    if (stateListener == null) { 
      throw new IllegalArgumentException("State listener cannot be null.");
    }
    if (listeners == null) { 
      listeners = Collections.synchronizedSet(new LinkedHashSet());
    }
    listeners.add(stateListener);
  }
  
  /**
   * Removes a listener from the list that will be notified of changes to the Motion Chart 'state' option.
   * @param stateListener the listener to remove. Cannot be <code>null</code>.
   */
  public void removeStateListener(StateListener stateListener) {
    if (stateListener == null) { 
      throw new IllegalArgumentException("State listener cannot be null.");
    }
    if (listeners != null) { 
      listeners.remove(stateListener);
    }
  }
  
}
