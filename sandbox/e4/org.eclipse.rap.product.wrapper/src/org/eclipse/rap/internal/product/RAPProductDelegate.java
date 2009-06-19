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

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IProduct;
import org.eclipse.core.runtime.Platform;
import org.eclipse.equinox.internal.app.ProductExtensionBranding;
import org.eclipse.rap.ui.internal.branding.BrandingExtension;
import org.eclipse.rwt.internal.branding.BrandingManager;
import org.eclipse.rwt.internal.service.BrowserSurvey;
import org.eclipse.rwt.internal.service.ContextProvider;
import org.osgi.framework.Bundle;

public class RAPProductDelegate implements IProduct {

  private static final String PI_RUNTIME = "org.eclipse.core.runtime"; //$NON-NLS-1$
  private static final String PT_PRODUCTS = "products"; //$NON-NLS-1$
  private final Map products = new HashMap();

  public RAPProductDelegate() {
    registerProducts();
  }

  public synchronized String getApplication() {
    return getCurrentBranding().getApplication();
  }

  private ProductExtensionBranding getCurrentBranding() {
    ProductExtensionBranding result;
    String servletName = null;
    if( ContextProvider.hasContext() ) {
      servletName = BrowserSurvey.getSerlvetName();
    } else {
      // no no no :)
      // TODO [bm] equinox calls getApplication during startup, need to see if
      // we can force equinox not to ask during startup
    }
    result = ( ProductExtensionBranding )products.get( servletName );
    if( result == null ) {
      result = new ProductExtensionBranding( servletName, null );
    }
    return result;
  }

  public synchronized Bundle getDefiningBundle() {
    return getCurrentBranding().getDefiningBundle();
  }

  public synchronized String getDescription() {
    return getCurrentBranding().getDescription();
  }

  /*
   * TODO [bm]: depending if we have a context we return the real product or the
   * fake delegate product
   */
  public synchronized String getId() {
    String result;
    if( ContextProvider.hasContext() ) {
      result = getCurrentBranding().getId();
    } else {
      result = "org.eclipse.rap.product";
    }
    return result;
  }

  public synchronized String getName() {
    return getCurrentBranding().getName();
  }

  private IConfigurationElement[] getProductExtensions() {
    IConfigurationElement[] elements;
    final IExtensionRegistry extensionRegistry = Platform.getExtensionRegistry();
    elements = extensionRegistry.getConfigurationElementsFor( PI_RUNTIME,
                                                              PT_PRODUCTS );
    return elements;
  }

  public synchronized String getProperty( final String key ) {
    return getCurrentBranding().getProperty( key );
  }

  private void registerProducts() {
    final IConfigurationElement[] elements = getProductExtensions();
    for( int i = 0; i < elements.length; i++ ) {
      final IConfigurationElement element = elements[ i ];
      if( element.getName().equalsIgnoreCase( "product" ) ) { //$NON-NLS-1$
        final IExtension extension = ( IExtension )element.getParent();
        final String id = extension.getUniqueIdentifier();
        final String simpleIdentifier = extension.getSimpleIdentifier();
        final ProductExtensionBranding productBranding = new ProductExtensionBranding( id,
                                                                                       element );
        final String fullIdentifier = extension.getContributor().getName()
                                      + "."
                                      + simpleIdentifier;
        products.put( fullIdentifier, productBranding );
        BrandingExtension.registerServletName( fullIdentifier );
        BrandingManager.register( new RAPProductBranding( id,
                                                          productBranding,
                                                          fullIdentifier ) );
      }
    }
  }
}
