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

import org.eclipse.swt.widgets.Composite;

public class TreeMap extends JITVisualizationWidget
{
  public static final int SLICE_AND_DICE = 0;
  public static final int SQUARIFY = 1;
  public static final int STRIP = 2;
  
  private final int type;
  
  public TreeMap( final Composite parent, final int type, final int style )
  {
    super( parent, style );
    if (type > 2 || type < 0) throw new IllegalArgumentException("Invalid value for type argument.");
    this.type = type;
  }
  
  public int getType() {
    return type;
  }
    
}
