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

import org.eclipse.rwt.internal.browser.Browser;
import org.eclipse.rwt.internal.lifecycle.HtmlResponseWriter;
import org.eclipse.rwt.internal.resources.ResourceManagerImpl;
import org.eclipse.rwt.internal.service.ContextProvider;
import org.eclipse.rwt.internal.service.IServiceStateInfo;
import org.eclipse.rwt.internal.util.HTML;
import org.eclipse.rwt.resources.IResourceManager;



final class TreeViewUtil {
  
  static final String TREEVIEW_DEFAULT 
    = "resources/js/treeview/treeview_default.js"; 
  static final String TREEVIEW_IE_GECKO 
    = "resources/js/treeview/treeview_ie_gecko.js";
  private static final String DOUBLE_CLICK_AJAX 
    = "resources/js/treeview/doubleclick_ajax.js";
  private static final String DOUBLE_CLICK_SCRIPT 
    = "resources/js/treeview/doubleclick_script.js";
  
  private TreeViewUtil() {
    // prevent instantiation
  }

  static void userDoubleClickLibrary() {
    Browser browser = ContextProvider.getStateInfo().getDetectedBrowser();
    if( browser.isAjaxEnabled() ) {
      useJSLibrary( DOUBLE_CLICK_AJAX );
    } else {
      useJSLibrary( DOUBLE_CLICK_SCRIPT );
    }
  }
  
  static void useJSLibrary( final String name ) {
    IResourceManager manager = ResourceManagerImpl.getInstance();
    manager.register( name, 
                      HTML.CHARSET_NAME_ISO_8859_1, 
                      IResourceManager.RegisterOptions.VERSION_AND_COMPRESS );
    IServiceStateInfo stateInfo = ContextProvider.getStateInfo();
    HtmlResponseWriter writer = stateInfo.getResponseWriter();
    writer.useJSLibrary( name );
  }
}
