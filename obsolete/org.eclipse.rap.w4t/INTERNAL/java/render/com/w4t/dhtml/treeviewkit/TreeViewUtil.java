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

import com.w4t.*;
import com.w4t.engine.service.ContextProvider;
import com.w4t.engine.service.IServiceStateInfo;
import com.w4t.engine.util.ResourceManager;


final class TreeViewUtil {
  
  static final String TREEVIEW_DEFAULT 
    = "resources/js/treeview/treeview_default.js"; 
  static final String TREEVIEW_IE_GECKO 
    = "resources/js/treeview/treeview_ie_gecko.js";
  
  private TreeViewUtil() {
    // prevent instantiation
  }

  static void useJSLibrary( final String name ) {
    IResourceManager manager = ResourceManager.getInstance();
    manager.register( name, 
                      HTML.CHARSET_NAME_ISO_8859_1, 
                      IResourceManager.RegisterOptions.VERSION_AND_COMPRESS );
    IServiceStateInfo stateInfo = ContextProvider.getStateInfo();
    HtmlResponseWriter writer = stateInfo.getResponseWriter();
    writer.useJSLibrary( name );
  }
}
