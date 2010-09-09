/******************************************************************************
 * Copyright © 2010-2011 Austin Riddle
 * All Rights Reserved.
 * 
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Austin Riddle - initial API and implementation
 * 
 *****************************************************************************/
package org.eclipse.rap.rwt.visualization.jit;

import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Composite;

public class HyperTree extends JITGraphWidget
{
  boolean returnToPrevious;
  
  public HyperTree( final Composite parent, final int style )
  {
    super( parent, style );
    nodeColor = new RGB(240,0,0);
    edgeColor = new RGB(8,143,255);
  }

  public void previousState() {
    if (navStack != null) {
      if (!navStack.isEmpty()) {
        navStack.pop(); //pop current state
      }
      if (!navStack.isEmpty()) {
        Object id = navStack.pop();
        addCommand("selectNode", new Object[] {id});
      }
    }
  }
  
}
