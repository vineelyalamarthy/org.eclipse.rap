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

import org.eclipse.equinox.internal.app.ProductExtensionBranding;
import org.eclipse.rwt.branding.AbstractBranding;

/*
 * TODO [bm] needs to be extended to provide all ideas of the RAP branding
 */
final class RAPProductBranding extends AbstractBranding {

  private final String id;
  private final ProductExtensionBranding productBranding;
  private final String simpleIdentifier;

  // private final String windowImages;
  RAPProductBranding( final String id,
                      final ProductExtensionBranding productBranding,
                      final String simpleIdentifier )
  {
    this.id = id;
    this.productBranding = productBranding;
    this.simpleIdentifier = simpleIdentifier;
  }

  public String getDefaultEntryPoint() {
    return productBranding.getApplication();
  }

  public String getFavIcon() {
    // return ResourceManager.getInstance().getLocation( windowImages );
    return "";
  }

  public String getId() {
    return id + ".product";
  }

  public String getServletName() {
    return simpleIdentifier;
  }

  public String getTitle() {
    return productBranding.getName();
  }
}