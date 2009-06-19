/*******************************************************************************
 * Copyright (c) 2009 EclipseSource and others. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   EclipseSource - initial API and implementation
 ******************************************************************************/
package org.eclipse.rap.internal.application;

import org.eclipse.core.runtime.Plugin;
import org.eclipse.rap.internal.product.ProductProvider;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends Plugin {

  // The plug-in ID
  public static final String PLUGIN_ID = "org.eclipse.rap.productmapper";

  /*
   * (non-Javadoc)
   * @see
   * org.eclipse.core.runtime.Plugins#start(org.osgi.framework.BundleContext)
   */
  public void start( final BundleContext context ) throws Exception {
    super.start( context );
    ApplicationRegistry.registerApplicationEntryPoints();
    ProductProvider.injectProductProvider();
  }
}
