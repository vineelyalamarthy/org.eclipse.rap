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
package com.w4t.dhtml.treeviewkit;

import java.io.IOException;

/**
 * <p>
 * The renderer for org.eclipse.rap.dhtml.TreeView on Konqueror 3.1 and later.
 * </p>
 */
public class TreeViewRenderer_Konqueror3_1up_Script
  extends TreeViewRenderer_DOM_Script
{

  protected void useJSLibrary() throws IOException {
    TreeViewUtil.userDoubleClickLibrary();
    TreeViewUtil.useJSLibrary( TreeViewUtil.TREEVIEW_IE_GECKO );
  }
}