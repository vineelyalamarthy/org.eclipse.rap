package org.eclipse.rap.rwt.visualization.jit;

import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Composite;

public abstract class JITGraphWidget extends JITVisualizationWidget {
  
  protected RGB nodeColor;
  protected RGB edgeColor;
  
  public JITGraphWidget(Composite parent, int style) {
    super(parent, style);
  }
  
  /**
   * Sets the color for nodes in this visualization.
   * A <code>null</code> value is the same as black. 
   * @param nodeColor - an RGB representing the node color
   */
  public void setNodeColor(RGB nodeColor) {
    this.nodeColor = nodeColor;
  }

  /**
   * Sets the color for edges in this visualization.
   * A <code>null</code> value is the same as black. 
   * @param nodeColor - an RGB representing the edge color
   */
  public void setEdgeColor(RGB edgeColor) {
    this.edgeColor = edgeColor;
  }
  
  /**
   * Gets the color of the nodes in this visualization.
   * @return and rgb representing the color of the nodes.
   */
  public RGB getNodeColor() {
    return nodeColor;
  }
  
  /**
   * Gets the color of the edges in this visualization.
   * @return and rgb representing the color of the edges.
   */
  public RGB getEdgeColor() {
    return edgeColor;
  }
  

}
