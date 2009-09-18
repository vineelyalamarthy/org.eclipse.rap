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
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IProduct;
import org.eclipse.core.runtime.IProductProvider;
import org.eclipse.core.runtime.Platform;
import org.eclipse.osgi.service.environment.EnvironmentInfo;
import org.eclipse.rap.ui.internal.branding.BrandingExtension;
import org.eclipse.rwt.internal.branding.BrandingManager;
import org.eclipse.rwt.internal.service.BrowserSurvey;
import org.eclipse.rwt.internal.service.ContextProvider;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

public class ProductProvider implements IProductProvider {

  public static final String RAP_PRODUCT_ID = "org.eclipse.rap.product"; //$NON-NLS-1$
  
  private static final String PI_RUNTIME = "org.eclipse.core.runtime"; //$NON-NLS-1$
  private static final String PT_PRODUCTS = "products"; //$NON-NLS-1$
  private static final String PROP_PRODUCT = "eclipse.product"; //$NON-NLS-1$
  private static final String APP_BUNDLE_ID = "org.eclipse.equinox.app"; //$NON-NLS-1$
  private static final Map products = new HashMap();

  private final IProduct productDelegate = new RAPProductDelegate();

  // gets called by bundle org.eclipse.equinox.app via reflection
  // org.eclipse.equinox.internal.app.EclipseAppContainer#getBranding
  public IProduct[] getProducts() {
    return new IProduct[]{ productDelegate };
  }

  public String getName() {
    return "RAP Product Delegate"; //$NON-NLS-1$
  }

  public static void injectProductProvider() {
    final Bundle appBundle = Platform.getBundle( APP_BUNDLE_ID );
    final BundleContext bundleContext = appBundle.getBundleContext();
    if( bundleContext != null ) {
      final EnvironmentInfo envInfo = getEnvironmentInfo( bundleContext );
      envInfo.setProperty( PROP_PRODUCT, RAP_PRODUCT_ID );
      // force app admin to load our product provider
      Platform.getProduct();
    } else {
      throw new RuntimeException(   "Could not instrument " //$NON-NLS-1$
                                  + APP_BUNDLE_ID
                                  + "to load RAP product" ); //$NON-NLS-1$
    }
    registerProducts();
  }


  public static EclipseBranding getCurrentBranding() {
    EclipseBranding result;
    String servletName = null;
    if( ContextProvider.hasContext() ) {
      servletName = BrowserSurvey.getSerlvetName();
    } else {
      // no no no :)
      // TODO [bm] equinox calls getApplication during startup, need to see if
      // we can force equinox not to ask during startup
    }
    result = ( EclipseBranding )products.get( servletName );
    if( result == null ) {
      // TODO [bm] we should return no product for servlets without product
      //           but this is currently impossible as we always have our
      //           delegate product active
      result = new EclipseBranding( servletName, null );
    }
    return result;
  }
  
  private static void registerProducts() {
    IExtension[] elements = getProductExtensions();
    for( int i = 0; i < elements.length; i++ ) {
      IExtension extension = elements[ i ];
      IConfigurationElement element = extension.getConfigurationElements()[0];
      if( element.getName().equalsIgnoreCase( "product" ) ) { //$NON-NLS-1$
        registerProduct( extension );
      }
    }
  }

  private static void registerProduct( IExtension extension ) {
    String fullIdentifier = extension.getUniqueIdentifier();
    String simpleIdentifier = extension.getSimpleIdentifier();
    IConfigurationElement element = extension.getConfigurationElements()[0];
    EclipseBranding productBranding
      = new EclipseBranding( fullIdentifier, element );
    products.put( simpleIdentifier, productBranding );
    BrandingExtension.registerServletName( simpleIdentifier );
    RAPProductBranding rapProductBranding
      = new RAPProductBranding( fullIdentifier,
                                productBranding,
                                simpleIdentifier );
    BrandingManager.register( rapProductBranding );
  }

  private static IExtension[] getProductExtensions() {
    IExtensionRegistry registry = Platform.getExtensionRegistry();
    String extensionPointId = PI_RUNTIME + '.' + PT_PRODUCTS;
    IExtensionPoint extensionPoint
      = registry.getExtensionPoint( extensionPointId );
    return extensionPoint.getExtensions();
  }
  

  // copy of org.eclipse.equinox.internal.app.Activator.getEnvironmentInfo
  private static EnvironmentInfo getEnvironmentInfo( final BundleContext bc ) {
    if( bc == null ) {
      return null;
    }
    String envInfoClass = EnvironmentInfo.class.getName();
    final ServiceReference infoRef = bc.getServiceReference( envInfoClass );
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
  
  
  public ProductProvider() {
    // prevent instantiation
  }
}
