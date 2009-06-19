/*******************************************************************************
 * Copyright (c) 2009 EclipseSource and others. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   EclipseSource - initial API and implementation
 ******************************************************************************/
package org.eclipse.rap.internal.product;

import org.eclipse.core.runtime.IProduct;
import org.eclipse.core.runtime.IProductProvider;
import org.eclipse.core.runtime.Platform;
import org.eclipse.osgi.service.environment.EnvironmentInfo;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

public class ProductProvider implements IProductProvider {

  static final String PROP_PRODUCT = "eclipse.product"; //$NON-NLS-1$
  static final RAPProductDelegate productDelegate = new RAPProductDelegate();

  // copy
  private static EnvironmentInfo getEnvironmentInfo( final BundleContext bc ) {
    if( bc == null ) {
      return null;
    }
    final ServiceReference infoRef = bc.getServiceReference( EnvironmentInfo.class.getName() );
    if( infoRef == null ) {
      return null;
    }
    final EnvironmentInfo envInfo = ( EnvironmentInfo )bc.getService( infoRef );
    if( envInfo == null ) {
      return null;
    }
    bc.ungetService( infoRef );
    return envInfo;
  }

  public static void injectProductProvider() {
    final String appBundleId = "org.eclipse.equinox.app";
    final Bundle appBundle = Platform.getBundle( appBundleId );
    final BundleContext bundleContext = appBundle.getBundleContext();
    if( bundleContext != null ) {
      final EnvironmentInfo envInfo = getEnvironmentInfo( bundleContext );
      envInfo.setProperty( PROP_PRODUCT, "org.eclipse.rap.product" );
      // force app admin to load our product provider
      final IProduct product = Platform.getProduct();
    } else {
      throw new RuntimeException( "Could not instrument "
                                  + appBundleId
                                  + "to load RAP product" );
    }
  }

  public String getName() {
    return "RAP Product Delegate";
  }

  // gets called by bundle org.eclipse.equinox.app via reflection
  // org.eclipse.equinox.internal.app.EclipseAppContainer#getBranding
  public IProduct[] getProducts() {
    return new IProduct[]{
      productDelegate
    };
  }
}
