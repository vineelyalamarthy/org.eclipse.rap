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
import org.eclipse.ui.part.ViewPart;

/**
 * This class controls all aspects of the application's execution
 * and is contributed through the plugin.xml.
 */
public class Application implements IEntryPoint 
{
 
  public static Listener createSelectionListener () {
    return new Listener() 
    {
      
      public void handleEvent(Event event) 
      {
        VisualizationWidget widget = (VisualizationWidget)event.widget;
        System.out.println( "Selected item=" + widget.getSelectedItem() + 
            "; row=" + widget.getSelectedRow() +
            "; column=" + widget.getSelectedColumn() +
            "; value=" + widget.getSelectedValue());
      }
      
    };
  }
   
  public int createUI() 
  {
    Display display = PlatformUI.createDisplay();
    PlatformUI.createAndRunWorkbench(display, new ApplicationWorkbenchAdvisor());
    return 0;
  }
  
  public static class MotionExample extends ViewPart 
  {
     
    private MotionChart viz;

    public void createPartControl (Composite parent) 
    {
      JSONGoogleDataTable dataTable = new JSONGoogleDataTable();
      dataTable.addColumn("Model", "Model", "string", null);
      dataTable.addColumn("thedate", "Date", "date", null);
      dataTable.addColumn("CO2", "CO2", "number", null);
      dataTable.addColumn("Temperature", "Temperature", "number", null);
      dataTable.addRow(new Object[] {"Model1", new Date(), new Integer(389), new Double(14.8)});
      dataTable.addRow(new Object[] {"Model1", new Date(4070908800L), new Integer(450), new Integer(19)});
      dataTable.addRow(new Object[] {"Model2", new Date(), new Integer(389), new Double(14.8)});
      dataTable.addRow(new Object[] {"Model2", new Date(4070908800L), new Integer(700), new Integer(23)});
      
      viz = new MotionChart( parent , SWT.NONE );
      viz.setWidgetData(dataTable.toString());
      viz.addStateListener(new StateListener() 
      {
        
        public void stateChanged(String state)
        {
          System.out.println("State Changed: "+state);
        }
        
      });
      viz.setToolTipText("Motion Chart");
    }

    public void setFocus() 
    {
      viz.setFocus();
    }
    
  }
  
  public static class TimelineExample extends ViewPart 
  {
     
    private AnnotatedTimeLine viz;

    public void createPartControl (Composite parent) 
    {
      JSONGoogleDataTable dataTable = new JSONGoogleDataTable();
      dataTable.addColumn("Date", "Date", "date", null);
      dataTable.addColumn("AverageGPA", "Average GPA", "number", null);
      dataTable.addRow(new Object[] {new Date(), new Double(2.85)});
      dataTable.addRow(new Object[] {new Date(1230809560), new Double(3.5)});
      dataTable.addRow(new Object[] {new Date(1210000000), new Integer(3)});
      
      viz = new AnnotatedTimeLine( parent, SWT.NONE );
      viz.setWidgetOptions("displayAnnotations: true");
      viz.setWidgetData(dataTable.toString());
      viz.addListener(SWT.Selection, createSelectionListener());
      viz.setToolTipText("Annotated Timeline");
    }
    
    public void setFocus() 
    {
      viz.setFocus();
    }
    
  }
  
  public static class AreaChartExample extends ViewPart 
  {
     
    private AreaChart viz;
    
    public void createPartControl (Composite parent) 
    {
      JSONGoogleDataTable dataTable = new JSONGoogleDataTable();
      dataTable.addColumn("theyear", "Date", "string", null);
      dataTable.addColumn("CO2", "CO2", "number", null);
      dataTable.addColumn("Temperature", "Temperature", "number", null);
      dataTable.addRow(new Object[] {"1970", new Integer(325), new Double(14.1)});
      dataTable.addRow(new Object[] {"2009", new Integer(389), new Double(14.7)});
      
      viz = new AreaChart(parent, SWT.NONE );
      viz.setWidgetData(dataTable.toString());
      viz.addListener(SWT.Selection, createSelectionListener());
    }
     
    public void setFocus() 
    {
      viz.setFocus();
    }
    
  }
  
  public static class BarChartExample extends ViewPart 
  {
     
    private BarChart viz;
    
    public void createPartControl (Composite parent) 
    {
      JSONGoogleDataTable dataTable = new JSONGoogleDataTable();
      dataTable.addColumn("theyear", "Date", "string", null);
      dataTable.addColumn("CO2", "CO2", "number", null);
      dataTable.addColumn("Temperature", "Temperature", "number", null);
      dataTable.addRow(new Object[] {"1970", new Integer(325), new Double(14.1)});
      dataTable.addRow(new Object[] {"2009", new Integer(389), new Double(14.7)});
      
      viz = new BarChart( parent, SWT.NONE );
      viz.setWidgetData(dataTable.toString());
      viz.addListener(SWT.Selection, createSelectionListener());
    }
    
    public void setFocus() 
    {
      viz.setFocus();
    }
    
  }
  
  public static class ColumnChartExample extends ViewPart 
  {
     
    private ColumnChart viz;
    
    public void createPartControl (Composite parent) 
    {
      JSONGoogleDataTable dataTable = new JSONGoogleDataTable();
      dataTable.addColumn("theyear", "Date", "string", null);
      dataTable.addColumn("CO2", "CO2", "number", null);
      dataTable.addColumn("Temperature", "Temperature", "number", null);
      dataTable.addRow(new Object[] {"1970", new Integer(325), new Double(14.1)});
      dataTable.addRow(new Object[] {"2009", new Integer(389), new Double(14.7)});
      
      viz = new ColumnChart( parent, SWT.NONE );
      viz.setWidgetData(dataTable.toString());
    }
    
    public void setFocus() 
    {
      viz.setFocus();
    }
    
  }
  
  public static class GaugeExample extends ViewPart 
  {
     
    private Gauge viz;
    
    public void createPartControl (Composite parent) 
    {
      JSONGoogleDataTable dataTable = new JSONGoogleDataTable();
      dataTable.addColumn("CO2", "CO2", "number", null);
      dataTable.addColumn("CH4", "CH4", "number", null);
      dataTable.addColumn("Temperature", "Temperature", "number", null);
      dataTable.addRow(new Object[] {new Integer(389), new Integer(1800), new Integer(14)});
      
      viz = new Gauge( parent, SWT.NONE );
      viz.setWidgetData(dataTable.toString());
    }
    
    public void setFocus() 
    {
      viz.setFocus();
    }
     
  }
  
  public static class GeomapExample extends ViewPart 
  {
     
    private Geomap viz;
    
    public void createPartControl (Composite parent) 
    {
      JSONGoogleDataTable dataTable = new JSONGoogleDataTable();
      dataTable.addColumn("Country", "Country", "string", null);
      dataTable.addColumn("Happiness", "Happiness", "number", null);
      dataTable.addRow(new Object[] {"Tanzania", new Integer(25)});
      dataTable.addRow(new Object[] {"US", new Integer(40)});
      
      viz = new Geomap( parent, SWT.NONE );
      viz.setWidgetData(dataTable.toString());
      viz.addListener(SWT.Selection, createSelectionListener());
    }
    
    public void setFocus() 
    {
      viz.setFocus();
    }
    
  }
  
  public static class IntensityMapExample extends ViewPart 
  {
     
    private IntensityMap viz;
    
    public void createPartControl (Composite parent) 
    {
      JSONGoogleDataTable dataTable = new JSONGoogleDataTable();
      dataTable.addColumn("Country", "Country", "string", null);
      dataTable.addColumn("Happiness", "Happiness", "number", null);
      dataTable.addColumn("Income", "Income", "number", null);
      dataTable.addRow(new Object[] {"TZ", new Integer(25), new Integer(3)});
      dataTable.addRow(new Object[] {"US", new Integer(40), new Integer(40)});
      dataTable.addRow(new Object[] {"UK", new Integer(38), new Integer(35)});
      
      viz = new IntensityMap( parent, SWT.NONE );
      viz.setWidgetData(dataTable.toString());
      viz.addListener(SWT.Selection, createSelectionListener());
    }
    
    public void setFocus() 
    {
      viz.setFocus();
    }
     
  }
  
  public static class LineChartExample extends ViewPart 
  {
    
    private LineChart viz;
    
    public void createPartControl (Composite parent) 
    {
      JSONGoogleDataTable dataTable = new JSONGoogleDataTable();
      dataTable.addColumn("Month", "Month", "string", null);
      dataTable.addColumn("Provider1", "Provider 1", "number", null);
      dataTable.addColumn("Provider2", "Provider 2", "number", null);
      dataTable.addColumn("Provider3", "Provider 3", "number", null);
      dataTable.addRow(new Object[] {"May", new Integer(10), new Integer(15), new Integer(20)});
      dataTable.addRow(new Object[] {"June", new Integer(12), new Integer(23), new Integer(33)});
      dataTable.addRow(new Object[] {"July", new Integer(11), new Integer(25), new Integer(50)});
      
      viz = new LineChart( parent, SWT.NONE );
      viz.setWidgetData(dataTable.toString());
      viz.addListener(SWT.Selection, createSelectionListener());
    }
    
    public void setFocus() 
    {
      viz.setFocus();
    }
    
  }
  
  public static class PieChartExample extends ViewPart 
  {
     
    private PieChart viz;
    
    public void createPartControl (Composite parent) 
    {
      JSONGoogleDataTable dataTable = new JSONGoogleDataTable();
      dataTable.addColumn("Activity", "Activity", "string", null);
      dataTable.addColumn("Hours", "Hours per Week", "number", null);
      dataTable.addRow(new Object[] {"software architect", new Integer(40)});
      dataTable.addRow(new Object[] {"primary care medicine", new Integer(9)});
      dataTable.addRow(new Object[] {"open source development", new Integer(10)});
      
      viz = new PieChart( parent, SWT.NONE );
      viz.setWidgetData(dataTable.toString());
      viz.addListener(SWT.Selection, createSelectionListener());
    }
    
    public void setFocus() 
    {
      viz.setFocus();
    }
    
  }
  
  public static class ScatterChartExample extends ViewPart 
  {
     
    private ScatterChart viz;
    
    public void createPartControl (Composite parent) 
    {
      JSONGoogleDataTable dataTable = new JSONGoogleDataTable();
      dataTable.addColumn("CO2", "CO2", "number", null);
      dataTable.addColumn("CH4", "CH4", "number", null);
      dataTable.addColumn("Temperature", "Temperature", "number", null);
      dataTable.addRow(new Object[] {new Integer(350), new Integer(10), new Integer(22)});
      dataTable.addRow(new Object[] {new Integer(375), new Integer(12), new Integer(23)});
      dataTable.addRow(new Object[] {new Integer(400), new Integer(16), new Integer(25)});
      
      viz = new ScatterChart( parent, SWT.NONE );
      viz.setWidgetData(dataTable.toString());
      viz.addListener(SWT.Selection, createSelectionListener());
    }
    
    public void setFocus() 
    {
      viz.setFocus();
    }
    
  }
  
  public static class TableExample extends ViewPart 
  {
    
    private Table viz;
    
    public void createPartControl (Composite parent) 
    {
      JSONGoogleDataTable dataTable = new JSONGoogleDataTable();
      dataTable.addColumn("theyear", "Date", "string", null);
      dataTable.addColumn("CO2", "CO2", "number", null);
      dataTable.addColumn("Temperature", "Temperature (C)", "number", null);
      dataTable.addRow(new Object[] {"1970", new Integer(325), new Double(14.1)});
      dataTable.addRow(new Object[] {"2009", new Integer(389), new Double(14.8)});
      viz = new Table( parent, SWT.NONE );
      viz.setWidgetData(dataTable.toString());
      viz.addListener(SWT.Selection, createSelectionListener());
    }
    
    public void setFocus() 
    {
      viz.setFocus();
    }
    
  }
  
}
