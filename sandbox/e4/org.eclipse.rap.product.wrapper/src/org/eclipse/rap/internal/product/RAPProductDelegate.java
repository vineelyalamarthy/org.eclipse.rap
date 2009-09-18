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
import org.eclipse.rwt.internal.service.ContextProvider;
import org.osgi.framework.Bundle;

public class RAPProductDelegate implements IProduct {

  // [bm]: depending on if we have a session context,
  // we need look like the real product or the fake delegate product
  // * if we have a session, the application itself asks for the real product
  //
  // * without context, it's likely that equinox asks us for the product during
  //   startup and we need to "play" the rap product in order to get cached
  public String getId() {
    String productId;
    if( ContextProvider.hasContext() ) {
      productId = ProductProvider.getCurrentBranding().getId();
    } else {
      productId = ProductProvider.RAP_PRODUCT_ID;
    }
    return productId;
  }

  public synchronized String getApplication() {
    return ProductProvider.getCurrentBranding().getApplication();
  }

  public synchronized Bundle getDefiningBundle() {
    return ProductProvider.getCurrentBranding().getDefiningBundle();
  }

  public synchronized String getDescription() {
    return ProductProvider.getCurrentBranding().getDescription();
  }

  public synchronized String getName() {
    return ProductProvider.getCurrentBranding().getName();
  }

  public synchronized String getProperty( final String key ) {
    return ProductProvider.getCurrentBranding().getProperty( key );
  }

}
