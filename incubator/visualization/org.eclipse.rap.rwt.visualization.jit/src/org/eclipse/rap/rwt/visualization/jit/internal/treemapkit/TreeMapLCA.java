/*******************************************************************************
 * Copyright (c) 2010-2011 Austin Riddle, and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Austin Riddle - initial API, implementation and documentation
 *     Cole Markham, Texas Center for Applied Technology - widget data passing to client side
 ******************************************************************************/
package org.eclipse.rap.rwt.visualization.jit.internal.treemapkit;

import java.util.Collection;

import org.eclipse.rap.rwt.visualization.jit.JITVisualizationWidget;
import org.eclipse.rap.rwt.visualization.jit.TreeMap;
import org.eclipse.rap.rwt.visualization.jit.internal.JITWidgetLCA;

public class TreeMapLCA extends JITWidgetLCA {

  public Class getWidgetType () {
    return TreeMap.class;
  }
  
  protected Collection getInitializationParameters(JITVisualizationWidget vWidget) {
    Collection params = super.getInitializationParameters(vWidget);
    params.add(new Integer(((TreeMap)vWidget).getType()));
    return params;
  }
  
}
