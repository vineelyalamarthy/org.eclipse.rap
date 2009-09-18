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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.eclipse.rwt.branding.AbstractBranding;
import org.eclipse.rwt.internal.resources.ResourceManager;
import org.eclipse.ui.branding.IProductConstants;
import org.osgi.framework.Bundle;

// TODO [bm] needs to be extended to provide all ideas of the RAP branding
final class RAPProductBranding extends AbstractBranding {

  private final String id;
  private final EclipseBranding productBranding;
  private final String simpleIdentifier;

  // private final String windowImages;
  public RAPProductBranding( final String id,
                             final EclipseBranding productBranding,
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
    return getFavIconFromProduct();
  }

  public String getId() {
    return id;
  }

  public String getServletName() {
    return simpleIdentifier;
  }

  public String getTitle() {
    return productBranding.getName();
  }
  
  public void registerResources() throws IOException {
    String favIconPath = getFavIconFromProduct();
    if( favIconPath != null ) {
      Bundle bundle = productBranding.getDefiningBundle();
      URL url = bundle.getResource( favIconPath );
      InputStream iconStream = null;
      try {
        iconStream = url.openStream();
        ResourceManager.getInstance().register( favIconPath, iconStream );
      } finally {
        if( iconStream != null ) {
          iconStream.close();
        }
      }
    }
  }

  // TODO [bm] WINDOW_IMAGES can contain multiple pathes
  private String getFavIconFromProduct() {
    return productBranding.getProperty( IProductConstants.WINDOW_IMAGES );
  }
}