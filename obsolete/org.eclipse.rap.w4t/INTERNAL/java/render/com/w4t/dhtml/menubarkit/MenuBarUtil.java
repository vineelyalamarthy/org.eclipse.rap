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
package com.w4t.dhtml.menubarkit;

import java.io.IOException;

import org.eclipse.rwt.Adaptable;
import org.eclipse.rwt.internal.browser.Browser;
import org.eclipse.rwt.internal.lifecycle.HtmlResponseWriter;
import org.eclipse.rwt.internal.resources.ResourceManagerImpl;
import org.eclipse.rwt.internal.service.ContextProvider;
import org.eclipse.rwt.internal.service.IServiceStateInfo;
import org.eclipse.rwt.internal.util.HTML;
import org.eclipse.rwt.resources.IResourceManager;
import org.eclipse.rwt.resources.IResourceManager.RegisterOptions;

import com.w4t.LifeCycleHelper;
import com.w4t.W4TContext;
import com.w4t.dhtml.MenuBar;
import com.w4t.dhtml.Node;
import com.w4t.dhtml.renderinfo.MenuBarInfo;
import com.w4t.internal.adaptable.IRenderInfoAdapter;


final class MenuBarUtil {
  
  static final String JS_DEFAULT = "resources/js/menubar/menubar_default.js";
  static final String JS_IE = "resources/js/menubar/menubar_ie.js";
  static final String JS_NAV6 = "resources/js/menubar/menubar_nav6.js";
  
  private MenuBarUtil() {
    // prevent instantiation
  }

  static void useJSLibrary( final String name ) {
    IResourceManager manager = ResourceManagerImpl.getInstance();
    manager.register( name, 
                      HTML.CHARSET_NAME_ISO_8859_1, 
                      RegisterOptions.VERSION_AND_COMPRESS );
    IServiceStateInfo stateInfo = ContextProvider.getStateInfo();
    HtmlResponseWriter writer = stateInfo.getResponseWriter();
    writer.useJSLibrary( name );
  }
  
  static MenuBarInfo getInfo( final MenuBar menuBar ) {
    Adaptable adaptable = menuBar;
    IRenderInfoAdapter adapter 
      = ( IRenderInfoAdapter )adaptable.getAdapter( IRenderInfoAdapter.class );
    return ( MenuBarInfo )adapter.getInfo();
  }

  static boolean isScriptEnabled() {
    boolean result = false;
    Browser browser = W4TContext.getBrowser();
    if( browser != null ) {
      result = browser.isScriptEnabled();
    }
    return result;
  }

  static void renderPopupMenu( final MenuBar menuBar ) throws IOException {
    Node[] nodes = menuBar.getNodes();
    for( int i = 0; i < nodes.length; i++ ) {
      LifeCycleHelper.render( nodes[ i ] );
    }    
  }
}
