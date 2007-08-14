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
package com.w4t.dhtml.treenodekit;

import java.io.IOException;

import org.eclipse.rwt.internal.util.HTMLUtil;

import com.w4t.dhtml.TreeNode;
import com.w4t.dhtml.renderinfo.TreeNodeInfo;




/** <p>The renderer for org.eclipse.rap.dhtml.TreeNode on Microsoft Internet 
  * Explorer 6 and later.</p>
  */
public class TreeNodeRenderer_Ie5up_Script extends TreeNodeRenderer_DOM_Script {
  
  String getVerticalAlign() {
    return "super";
  }

  void createStateInfoField( final TreeNode treeNode ) throws IOException {
    TreeNodeInfo info = TreeNodeRendererUtil.getInfo( treeNode );
    String name = TreeNodeRendererUtil.getStateInfoId( treeNode.getUniqueID() );
    HTMLUtil.hiddenInput( getResponseWriter(), name, info.getExpansion() );
    // need this because of bug in IExplorer (must not be newline here!)
    String absolutelyNecessaryBlank = " ";
    getResponseWriter().write( absolutelyNecessaryBlank );
  }
}