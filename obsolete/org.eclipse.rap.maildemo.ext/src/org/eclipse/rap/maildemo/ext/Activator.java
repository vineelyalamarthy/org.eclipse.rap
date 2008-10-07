/*******************************************************************************
 * Copyright (c) 2008 Innoopract Informationssysteme GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Innoopract Informationssysteme GmbH - initial API and implementation
 ******************************************************************************/

package org.eclipse.rap.maildemo.ext;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;


/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

  public static final String PLUGIN_ID = "org.eclipse.rap.maildemo.ext";

  private static Activator plugin;

  public void start( final BundleContext context ) throws Exception {
    super.start( context );
    plugin = this;
  }

  public void stop( final BundleContext context ) throws Exception {
    plugin = null;
    super.stop( context );
  }

  public static Activator getDefault() {
    return plugin;
  }

  public static ImageDescriptor getImageDescriptor( final String path ) {
    return imageDescriptorFromPlugin( PLUGIN_ID, path );
  }
}
