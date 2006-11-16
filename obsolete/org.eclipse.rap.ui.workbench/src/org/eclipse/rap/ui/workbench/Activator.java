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

package org.eclipse.rap.ui.workbench;

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;


public class Activator extends Plugin {

  public static final String PLUGIN_ID = "org.eclipse.rap.ui.workbench";
  private static Activator plugin;
  private BundleContext context;
  
  public void start( final BundleContext context ) throws Exception {
    super.start( context );
    plugin = this;
    this.context = context;
  }

  public void stop( final BundleContext context ) throws Exception {
    this.context = null;
    plugin = null;
    super.stop( context );
  }

  public static Activator getDefault() {
    return plugin;
  }
  
  public BundleContext getContext() {
    return context;
  }
}
