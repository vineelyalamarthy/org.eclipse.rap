package org.eclipse.rap.rwt.visualization.jit;

import java.util.Stack;

import org.eclipse.rap.rwt.visualization.jit.internal.JITWidgetLCA.WidgetCommandQueue;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

public abstract class JITVisualizationWidget extends Composite {
  
  protected String jsonData;
  
  protected String selectedNodeId;
  
  protected WidgetCommandQueue cmdQueue;
  
  protected Stack navStack;
  
  protected Listener selListener = new Listener() {
    public void handleEvent(Event event) {
      selectedNodeId = (String) JITVisualizationWidget.this.getData("selectedNode");
      if (navStack == null) {
        navStack = new Stack();
      }
      navStack.push(selectedNodeId);
    }
  };
  
  public JITVisualizationWidget(Composite parent, int style) {
    super(parent, style);
    addListener(SWT.Selection, selListener);
  }
  
  public Object getAdapter(Class adapter) {
    if (adapter.equals(WidgetCommandQueue.class))
      return cmdQueue;
    return super.getAdapter(adapter);
  }
  
  /**
   * Sets the JSON data that this visualization uses for rendering.
   * @param data
   */
  public void setJSONData(String data) {
    jsonData = data;
  }
  
  /**
   * Gets the JSON data that this visualization uses for rendering.
   * @return the JSON data as a string
   */
  public String getJSONData() {
    return jsonData;
  }

  /**
   * Sets the selected JSON node id. 
   * @param selectedNode
   */
  public void setSelectedNodeId (String selectedNodeId) {
    this.selectedNodeId = selectedNodeId;
    addCommand("selectNode",null);
  }
  
  /**
   * Gets the JSON node that is currently selected in the widget.
   * @return the id of the currently selected JSON node.
   */
  public String getSelectedNodeId() {
    return selectedNodeId;
  }
  
  /**
   * Adds commands to a queue for execution on client side.
   * @param cmd - the name of the function to execute.
   * @param args and array of function arguments. Can be <code>null</code>.
   */
  protected synchronized void addCommand (String cmd, Object[] args) {
    if (cmdQueue == null) {
      cmdQueue = new WidgetCommandQueue(); 
    }
    cmdQueue.add(new Object[] {cmd,args});
  }
  
}
