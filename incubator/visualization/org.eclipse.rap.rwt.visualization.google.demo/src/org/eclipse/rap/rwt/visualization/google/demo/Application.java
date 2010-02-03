/******************************************************************************
 * Copyright (c) 2009-2010 Texas Center for Applied Technology
 * Texas Engineering Experiment Station
 * The Texas A&M University System
 * and David Donahue 
 * All Rights Reserved.
 * 
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Austin Riddle (Texas Center for Applied Technology) - 
 *                   initial demo implementation
 *     David Donahue - examples from API documentation
 *                     @see org.eclipse.rap.rwt.visualization.google 
 * 
 *****************************************************************************/
package org.eclipse.rap.rwt.visualization.google.demo;

import java.util.Date;

import org.eclipse.rap.rwt.visualization.google.AnnotatedTimeLine;
import org.eclipse.rap.rwt.visualization.google.AreaChart;
import org.eclipse.rap.rwt.visualization.google.BarChart;
import org.eclipse.rap.rwt.visualization.google.ColumnChart;
import org.eclipse.rap.rwt.visualization.google.Gauge;
import org.eclipse.rap.rwt.visualization.google.Geomap;
import org.eclipse.rap.rwt.visualization.google.IntensityMap;
import org.eclipse.rap.rwt.visualization.google.LineChart;
import org.eclipse.rap.rwt.visualization.google.MotionChart;
import org.eclipse.rap.rwt.visualization.google.PieChart;
import org.eclipse.rap.rwt.visualization.google.ScatterChart;
import org.eclipse.rap.rwt.visualization.google.Table;
import org.eclipse.rap.rwt.visualization.google.VisualizationWidget;
import org.eclipse.rap.rwt.visualization.google.MotionChart.StateListener;
import org.eclipse.rap.rwt.visualization.google.json.JSONGoogleDataTable;
import org.eclipse.rwt.lifecycle.IEntryPoint;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.part.ViewPart;

/**
 * This class controls all aspects of the application's execution
 * and is contributed through the plugin.xml.
 */
public class Application implements IEntryPoint {
   
  private static Listener listener = new Listener() {
     @Override
     public void handleEvent(Event event)
     {
        VisualizationWidget widget = (VisualizationWidget)event.widget;
        System.out.println( "Selected item=" + widget.getSelectedItem() + 
                          "; row=" + widget.getSelectedRow() +
                          "; column=" + widget.getSelectedColumn() +
                          "; value=" + widget.getSelectedValue());
     }
  };
   
   
  public int createUI() {
    Display display = PlatformUI.createDisplay();
    PlatformUI.createAndRunWorkbench(display, new ApplicationWorkbenchAdvisor());
    return 0;
  }
  
  public static class MotionExample extends ViewPart {
     @Override
     public void createPartControl (Composite parent) {
        JSONGoogleDataTable dataTable = new JSONGoogleDataTable();
        dataTable.addColumn("Model", "Model", "string", null);
        dataTable.addColumn("thedate", "Date", "date", null);
        dataTable.addColumn("CO2", "CO2", "number", null);
        dataTable.addColumn("Temperature", "Temperature", "number", null);
        dataTable.addRow(new Object[] {"Model1", new Date(), 389, 14.8});
        dataTable.addRow(new Object[] {"Model1", new Date(4070908800L), 450, 19});
        dataTable.addRow(new Object[] {"Model2", new Date(), 389, 14.8});
        dataTable.addRow(new Object[] {"Model2", new Date(4070908800L), 700, 23});
         
        MotionChart viz = new MotionChart( parent , SWT.NONE );
        viz.setWidgetData(dataTable.toString());
        viz.addStateListener(new StateListener()
        {
          @Override
          public void stateChanged(String state)
          {
             System.out.println("State Changed: "+state);
          }
        });
        viz.setToolTipText("Motion Chart");
     }

     @Override
     public void setFocus(){}
  }
  
  public static class TimelineExample extends ViewPart {
     @Override
     public void createPartControl (Composite parent) {
        JSONGoogleDataTable dataTable = new JSONGoogleDataTable();
        dataTable.addColumn("Date", "Date", "date", null);
        dataTable.addColumn("AverageGPA", "Average GPA", "number", null);
        dataTable.addRow(new Object[] {new Date(), 2.85});
        dataTable.addRow(new Object[] {new Date(1230809560), 3.5});
        dataTable.addRow(new Object[] {new Date(1210000000), 3});
        
        AnnotatedTimeLine viz = new AnnotatedTimeLine( parent, SWT.NONE );
        viz.setWidgetOptions("displayAnnotations: true");
        viz.setWidgetData(dataTable.toString());
        viz.addListener(SWT.Selection, listener);
        viz.setToolTipText("Annotated Timeline");
     }
     
     @Override
     public void setFocus(){}
  }
  
  public static class AreaChartExample extends ViewPart {
     @Override
     public void createPartControl (Composite parent) {
        JSONGoogleDataTable dataTable = new JSONGoogleDataTable();
        dataTable.addColumn("theyear", "Date", "string", null);
        dataTable.addColumn("CO2", "CO2", "number", null);
        dataTable.addColumn("Temperature", "Temperature", "number", null);
        dataTable.addRow(new Object[] {"1970", 325, 14.1});
        dataTable.addRow(new Object[] {"2009", 389, 14.7});
        
        AreaChart viz = new AreaChart(parent, SWT.NONE );
        viz.setWidgetData(dataTable.toString());
        viz.addListener(SWT.Selection, listener);
     }
     
     @Override
     public void setFocus(){}
  }
  
  public static class BarChartExample extends ViewPart {
     @Override
     public void createPartControl (Composite parent) {
        JSONGoogleDataTable dataTable = new JSONGoogleDataTable();
        dataTable.addColumn("theyear", "Date", "string", null);
        dataTable.addColumn("CO2", "CO2", "number", null);
        dataTable.addColumn("Temperature", "Temperature", "number", null);
        dataTable.addRow(new Object[] {"1970", 325, 14.1});
        dataTable.addRow(new Object[] {"2009", 389, 14.7});
        
        BarChart barChart = new BarChart( parent, SWT.NONE );
        barChart.setWidgetData(dataTable.toString());
        barChart.addListener(SWT.Selection, listener);
     }
     
     @Override
     public void setFocus(){}
  }
  
  public static class ColumnChartExample extends ViewPart {
     @Override
     public void createPartControl (Composite parent) {
        JSONGoogleDataTable dataTable = new JSONGoogleDataTable();
        dataTable.addColumn("theyear", "Date", "string", null);
        dataTable.addColumn("CO2", "CO2", "number", null);
        dataTable.addColumn("Temperature", "Temperature", "number", null);
        dataTable.addRow(new Object[] {"1970", 325, 14.1});
        dataTable.addRow(new Object[] {"2009", 389, 14.7});
        
        ColumnChart chart = new ColumnChart( parent, SWT.NONE );
        chart.setWidgetData(dataTable.toString());
     }
     
     @Override
     public void setFocus(){}
  }
  
  public static class GaugeExample extends ViewPart {
     @Override
     public void createPartControl (Composite parent) {
        JSONGoogleDataTable dataTable = new JSONGoogleDataTable();
        dataTable.addColumn("CO2", "CO2", "number", null);
        dataTable.addColumn("CH4", "CH4", "number", null);
        dataTable.addColumn("Temperature", "Temperature", "number", null);
        dataTable.addRow(new Object[] {389, 1800, 14});
        
        Gauge gauge = new Gauge( parent, SWT.NONE );
        gauge.setWidgetData(dataTable.toString());
     }
     
     @Override
     public void setFocus(){}
  }
  
  public static class GeomapExample extends ViewPart {
     @Override
     public void createPartControl (Composite parent) {
        JSONGoogleDataTable dataTable = new JSONGoogleDataTable();
        dataTable.addColumn("Country", "Country", "string", null);
        dataTable.addColumn("Happiness", "Happiness", "number", null);
        dataTable.addRow(new Object[] {"Tanzania", 25});
        dataTable.addRow(new Object[] {"US", 40});
        
        Geomap geomap = new Geomap( parent, SWT.NONE );
        geomap.setWidgetData(dataTable.toString());
        geomap.addListener(SWT.Selection, listener);
     }
     
     @Override
     public void setFocus(){}
  }
  
  public static class IntensityMapExample extends ViewPart {
     @Override
     public void createPartControl (Composite parent) {
        JSONGoogleDataTable dataTable = new JSONGoogleDataTable();
        dataTable.addColumn("Country", "Country", "string", null);
        dataTable.addColumn("Happiness", "Happiness", "number", null);
        dataTable.addColumn("Income", "Income", "number", null);
        dataTable.addRow(new Object[] {"TZ", 25, 3});
        dataTable.addRow(new Object[] {"US", 40, 40});
        dataTable.addRow(new Object[] {"UK", 38, 35});
        
        IntensityMap intensityMap = new IntensityMap( parent, SWT.NONE );
        intensityMap.setWidgetData(dataTable.toString());
        intensityMap.addListener(SWT.Selection, listener);
     }
     
     @Override
     public void setFocus(){}
  }
  
  public static class LineChartExample extends ViewPart {
     @Override
     public void createPartControl (Composite parent) {
        JSONGoogleDataTable dataTable = new JSONGoogleDataTable();
        dataTable.addColumn("Month", "Month", "string", null);
        dataTable.addColumn("Provider1", "Provider 1", "number", null);
        dataTable.addColumn("Provider2", "Provider 2", "number", null);
        dataTable.addColumn("Provider3", "Provider 3", "number", null);
        dataTable.addRow(new Object[] {"May", 10, 15, 20});
        dataTable.addRow(new Object[] {"June", 12, 23, 33});
        dataTable.addRow(new Object[] {"July", 11, 25, 50});
        
        LineChart lineChart = new LineChart( parent, SWT.NONE );
        lineChart.setWidgetData(dataTable.toString());
        lineChart.addListener(SWT.Selection, listener);
     }
     
     @Override
     public void setFocus(){}
  }
  
  public static class PieChartExample extends ViewPart {
     @Override
     public void createPartControl (Composite parent) {
        JSONGoogleDataTable dataTable = new JSONGoogleDataTable();
        dataTable.addColumn("Activity", "Activity", "string", null);
        dataTable.addColumn("Hours", "Hours per Week", "number", null);
        dataTable.addRow(new Object[] {"software architect", 40});
        dataTable.addRow(new Object[] {"primary care medicine", 9});
        dataTable.addRow(new Object[] {"open source development", 10});
        
        PieChart pieChart = new PieChart( parent, SWT.NONE );
        pieChart.setWidgetData(dataTable.toString());
        pieChart.addListener(SWT.Selection, listener);
     }
     
     @Override
     public void setFocus(){}
  }
  
  public static class ScatterChartExample extends ViewPart {
     @Override
     public void createPartControl (Composite parent) {
        JSONGoogleDataTable dataTable = new JSONGoogleDataTable();
        dataTable.addColumn("CO2", "CO2", "number", null);
        dataTable.addColumn("CH4", "CH4", "number", null);
        dataTable.addColumn("Temperature", "Temperature", "number", null);
        dataTable.addRow(new Object[] {350, 10, 22});
        dataTable.addRow(new Object[] {375, 12, 23});
        dataTable.addRow(new Object[] {400, 16, 25});
        
        ScatterChart scatterChart = new ScatterChart( parent, SWT.NONE );
        scatterChart.setWidgetData(dataTable.toString());
        scatterChart.addListener(SWT.Selection, listener);
     }
     
     @Override
     public void setFocus(){}
  }
  
  public static class TableExample extends ViewPart {
     @Override
     public void createPartControl (Composite parent) {
        JSONGoogleDataTable dataTable = new JSONGoogleDataTable();
        dataTable.addColumn("theyear", "Date", "string", null);
        dataTable.addColumn("CO2", "CO2", "number", null);
        dataTable.addColumn("Temperature", "Temperature (C)", "number", null);
        dataTable.addRow(new Object[] {"1970", 325, 14.1});
        dataTable.addRow(new Object[] {"2009", 389, 14.8});
        String serializedData = dataTable.toString();
        
        Table table = new Table( parent, SWT.NONE );
        table.setWidgetData(serializedData);
        table.addListener(SWT.Selection, listener);
     }
     
     @Override
     public void setFocus(){}
  }
  
}
