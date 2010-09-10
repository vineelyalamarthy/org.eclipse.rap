/*******************************************************************************
 * Copyright (c) 2010 EclipseSource.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     EclipseSource - initial API and implementation
 ******************************************************************************/
package org.eclipse.rap.ui.themeeditor;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

public final class Activator extends AbstractUIPlugin {

  public static final String PLUGIN_ID = "org.eclipse.rap.themeeditor.ui"; //$NON-NLS-1$
  
  // The shared instance
  private static Activator plugin;

  public static Activator getDefault() {
    return plugin;
  }

  // ///////////////////////////
  // AbstractUIPlugin overrides
  public void start( final BundleContext context ) throws Exception {
    super.start( context );
    plugin = this;
  }

  public void stop( final BundleContext context ) throws Exception {
    plugin = null;
    super.stop( context );
  }
}
