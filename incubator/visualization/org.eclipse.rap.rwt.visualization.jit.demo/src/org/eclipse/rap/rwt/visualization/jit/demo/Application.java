/******************************************************************************
 * Copyright © 2010-2011 Texas Center for Applied Technology
 * Texas Engineering Experiment Station
 * The Texas A&M University System
 * All Rights Reserved.
 * 
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Austin Riddle (Texas Center for Applied Technology) -
 *       initial API and implementation
 * 
 *****************************************************************************/
package org.eclipse.rap.rwt.visualization.jit.demo;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.eclipse.rap.rwt.visualization.jit.HyperTree;
import org.eclipse.rap.rwt.visualization.jit.JITVisualizationWidget;
import org.eclipse.rap.rwt.visualization.jit.RGraph;
import org.eclipse.rap.rwt.visualization.jit.SpaceTree;
import org.eclipse.rap.rwt.visualization.jit.TreeMap;
import org.eclipse.rwt.lifecycle.IEntryPoint;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

/**
 * This class controls all aspects of the application's execution
 * and is contributed through the plugin.xml.
 */
public class Application implements IEntryPoint {
   
  public static Listener createSelectionListener () {
    return new Listener() 
    {
      
      public void handleEvent(Event event) 
      {
        JITVisualizationWidget widget = (JITVisualizationWidget)event.widget;
        System.out.println( "Selected node id = " + widget.getSelectedNodeId()); 
      }
      
    };
  }
   
   
  public int createUI() 
  {
    Display display = PlatformUI.createDisplay();
    PlatformUI.createAndRunWorkbench(display, new ApplicationWorkbenchAdvisor());
    return 0;
  }
  
  public static class HyperTreeExample extends ViewPart 
  {
    
    private SashForm mainSash;
    protected HyperTree topTree;
    protected HyperTree bottomTree;
    
    public void createPartControl (Composite parent) 
    {
      mainSash = new SashForm(parent, SWT.VERTICAL);
      Group group = new Group(mainSash,SWT.NONE);
      group.setText("Default");
      group.setLayout(new GridLayout(1,false));
      Button backBtn = new Button(group, SWT.PUSH);
      backBtn.setText("Previous State");
      backBtn.addSelectionListener(new SelectionAdapter() {
        public void widgetSelected(SelectionEvent e) {
          topTree.previousState();
        }
      });
      backBtn.setLayoutData(new GridData(SWT.LEFT,SWT.CENTER,false,false));
      topTree = new HyperTree(group, SWT.BORDER);
      String sampleData = loadSampleData("samples/hypertree.json");
      topTree.setJSONData(sampleData);
      topTree.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
      topTree.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
      //  leave this hyper tree with default colors
      topTree.addListener(SWT.Selection, createSelectionListener());
      topTree.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true));
      
      group = new Group(mainSash,SWT.NONE);
      group.setText("Weighted");
      group.setLayout(new GridLayout(1,false));
      backBtn = new Button(group, SWT.PUSH);
      backBtn.setText("Previous State");
      backBtn.addSelectionListener(new SelectionAdapter() {
        public void widgetSelected(SelectionEvent e) {
          bottomTree.previousState();
        }
      });
      backBtn.setLayoutData(new GridData(SWT.LEFT,SWT.CENTER,false,false));
      bottomTree = new HyperTree(group, SWT.BORDER);
      sampleData = loadSampleData("samples/hypertree-weighted.json");
      bottomTree.setJSONData(sampleData);
      bottomTree.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
      bottomTree.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_YELLOW));
      
      //Demo change of node and edge colors
      //JSON data can override colors.
      bottomTree.setNodeColor(Display.getCurrent().getSystemColor(SWT.COLOR_BLUE).getRGB());
      bottomTree.setEdgeColor(new RGB(51,34,102)); //purple
      bottomTree.addListener(SWT.Selection, createSelectionListener());
      bottomTree.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true));
    }
    
    public void setFocus()
    {
      topTree.setFocus();
    }
    
  }
  
  public static class TreeMapExample extends ViewPart 
  {
    
    private SashForm oneSash;
    private SashForm twoSash;
    private TreeMap sqViz;
    private TreeMap sliceViz;
    private TreeMap stripViz;
    private int seedId = 0;
    
    public void createPartControl (Composite parent) 
    {
      String sampleData = loadSampleData("samples/treemap.json");
      oneSash = new SashForm(parent, SWT.VERTICAL);
      twoSash = new SashForm(oneSash, SWT.HORIZONTAL);
      
      Group group = new Group(twoSash,SWT.NONE);
      group.setText("Squarified");
      group.setLayout(new FillLayout());
      sqViz = new TreeMap(group, TreeMap.SQUARIFY, SWT.BORDER);
      sqViz.setJSONData(makeUnique(sampleData));
      sqViz.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
      sqViz.addListener(SWT.Selection, createSelectionListener());
      
      group = new Group(twoSash,SWT.NONE);
      group.setText("Strip");
      group.setLayout(new FillLayout());
      stripViz = new TreeMap(group, TreeMap.STRIP, SWT.BORDER);
      stripViz.setJSONData(makeUnique(sampleData));
      stripViz.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
      stripViz.addListener(SWT.Selection, createSelectionListener());
      
      group = new Group(oneSash,SWT.NONE);
      group.setText("Slice and Dice");
      group.setLayout(new FillLayout());
      sliceViz = new TreeMap(group, TreeMap.SLICE_AND_DICE, SWT.BORDER);
      sliceViz.setJSONData(makeUnique(sampleData));
      sliceViz.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
      sliceViz.addListener(SWT.Selection, createSelectionListener());
    }
    
    public void setFocus()
    {
      oneSash.setFocus();
    }
    
    /**
     * Replaces all of the ids in the srcJSON with a unique set.
     * Widgets cannot share datasets with the same ids.
     * @param srcJSON
     * @return the JSON string with unique ids substituted for all ids
     */
    public String makeUnique (String srcJSON) {
      String[] split = srcJSON.split("\"id\" : .*,");
      StringBuffer sb = new StringBuffer();
      for (int i = 0; i < split.length; i++) {
        sb.append(split[i]);
        if ((i+1) < split.length) {
          seedId++;
          sb.append("\"id\" : ").append("\"").append(seedId).append("\"").append(",");
        }
      }
//      System.out.println(sb.toString());
      return sb.toString();
    }
    
  }
  
  public static class RGraphExample extends ViewPart 
  {
    
    private RGraph viz;
    
    public void createPartControl (Composite parent) 
    {
      viz = new RGraph(parent, SWT.BORDER);
      String sampleData = loadSampleData("samples/rgraph.json");
      viz.setJSONData(sampleData);
      viz.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
      viz.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
      //Demo change of node colors
      //JSON data can override colors.
      viz.setNodeColor(Display.getCurrent().getSystemColor(SWT.COLOR_BLUE).getRGB());
      viz.addListener(SWT.Selection, createSelectionListener());
    }
    
    public void setFocus()
    {
      viz.setFocus();
    }
    
  }
  
  public static class SpaceTreeExample extends ViewPart 
  {
    
    private SpaceTree viz;
    
    public void createPartControl (Composite parent) 
    {
      viz = new SpaceTree(parent, SWT.BORDER);
      String sampleData = loadSampleData("samples/spacetree.json");
      viz.setJSONData(sampleData);
      viz.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
      viz.addListener(SWT.Selection, createSelectionListener());
    }
    
    public void setFocus()
    {
      viz.setFocus();
    }
    
  }
  
  public static String loadSampleData (String sampleFilePath) 
  {
    StringBuffer data = new StringBuffer();
    try 
    {
      InputStream in = Activator.getDefault().getBundle().getResource(sampleFilePath).openStream();
      BufferedReader br = new BufferedReader(new InputStreamReader(in));
      try 
      {
        String line = null;
        while ((line = br.readLine()) != null) 
        {
          data.append(line).append("\n");
        }
      }
      finally 
      {
        br.close();
      }  
    } 
    catch (Exception e) 
    {
      e.printStackTrace();
    }
    return data.toString();
  }
  
}
