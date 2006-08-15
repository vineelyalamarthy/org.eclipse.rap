/*******************************************************************************
 * Copyright (c) 2002-2006 Innoopract Informationssysteme GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Innoopract Informationssysteme GmbH - initial API and implementation
 ******************************************************************************/
package com.w4t.dhtml.treeleafkit;

import com.w4t.dhtml.TreeLeaf;
import com.w4t.dhtml.event.DragDropEvent;
import com.w4t.event.WebActionEvent;


final class TreeLeafUtil {

  static boolean isDragDropActive( final TreeLeaf treeLeaf ) {
    return    treeLeaf.isEnabled() 
           && DragDropEvent.hasListener( treeLeaf.getParentNode() );
  }

  static boolean isActionActive( final TreeLeaf treeLeaf ) {
    return treeLeaf.isEnabled() && hasActionListener( treeLeaf );
  }

  static boolean hasActionListener( final TreeLeaf treeLeaf ) {
    return WebActionEvent.hasListener( treeLeaf );
  }
  
}
