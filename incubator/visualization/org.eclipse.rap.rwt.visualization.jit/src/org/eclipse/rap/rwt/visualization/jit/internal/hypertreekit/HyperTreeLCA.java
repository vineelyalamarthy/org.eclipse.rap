/*******************************************************************************
 * Copyright (c) 2010-2011 Austin Riddle, and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Austin Riddle - initial API, implementation and documentation
 *     Cole Markham (Texas Center for Applied Technology) - 
 *        widget data passing to client side
 ******************************************************************************/
package org.eclipse.rap.rwt.visualization.jit.internal.hypertreekit;

import org.eclipse.rap.rwt.visualization.jit.HyperTree;
import org.eclipse.rap.rwt.visualization.jit.internal.JITGraphLCA;

public class HyperTreeLCA extends JITGraphLCA {

  public Class getWidgetType () {
    return HyperTree.class;
  }
  
}
