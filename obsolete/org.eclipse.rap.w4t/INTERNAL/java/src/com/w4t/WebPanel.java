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
package com.w4t;

import org.eclipse.rwt.internal.lifecycle.LifeCycle;

/** <p>This is the simplest container class.</p>
  * <p>A WebPanel provides space in which an application can attach any other
  * WebComponent, including other Webpanels.</p>
  * <p>The default WebLayout for the WebPanel is the WebFlowLayout.</p>
  */
public class WebPanel extends WebContainer {
  
  /** constructs a new WebPanel. The default Layout Manager of the WebPanel
    * is the WebFlowLayout. */
  public WebPanel() {
    super();
    setWebLayout( new WebFlowLayout() );
  }
  
  protected final void init() throws Exception {
    if( !LifeCycle.isDevelopmentMode ) {
      doInit();
    }
  }

  /** <p>this method is called by the {@link #init() init()} method 
   * after some pre-checks. It has an empty pre-implementation. To add
   * initialisation functionality, it is recommended to override doInit()
   * and call init() from the constructor.</p> */
  protected void doInit() throws Exception {
    // empty pre-implementation
  }
  
  /** <p>returns a path to an image that represents this WebComponent
   * (widget icon).</p> */
  public static String retrieveIconName() {
    return "resources/images/icons/panel.gif";
  } 
}