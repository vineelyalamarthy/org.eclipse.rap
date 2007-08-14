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

import com.w4t.ProcessActionUtil;
import com.w4t.WebComponent;
import com.w4t.dhtml.DHTMLProcessActionUtil;
import com.w4t.dhtml.TreeLeaf;

/** <p>The superclass for all renderers for org.eclipse.rap.dhtml.TreeLeaf on 
  * more recent browsers that support javascript.</p>
  *
  * <p>Note that this is not an actual renderer but encapsulates common 
  * functionality for browser-specific renderers.</p>
  */
public abstract class TreeLeafRenderer_Base_Script extends TreeLeafRenderer {
  
  public void processAction( final WebComponent component ) {
    TreeLeaf treeLeaf = ( TreeLeaf )component;
    DHTMLProcessActionUtil.processDragDropScript( treeLeaf );
    DHTMLProcessActionUtil.processDoubleClickScript( treeLeaf );
    ProcessActionUtil.processActionPerformedScript( treeLeaf );
  }
}
