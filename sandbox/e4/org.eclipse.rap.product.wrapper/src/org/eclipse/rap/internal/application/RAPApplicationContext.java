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

import java.util.HashMap;
import java.util.Map;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.equinox.internal.app.CommandLineArgs;
import org.eclipse.rap.internal.product.ProductProvider;
import org.osgi.framework.Bundle;

/*
 * Fake context for IApplications
 */
final class RAPApplicationContext implements IApplicationContext {

  private Map arguments;

  public RAPApplicationContext( final IApplication application ) {
    super();
  }

  public void applicationRunning() {
    // do nothing
  }

  public Map getArguments() {
    if( arguments == null ) {
      arguments = new HashMap();
      final String[] context = CommandLineArgs.getApplicationArgs();
      arguments.put( IApplicationContext.APPLICATION_ARGS, context );
    }
    return arguments;
  }

  public String getBrandingApplication() {
    return ProductProvider.getCurrentBranding().getApplication();
  }

  public Bundle getBrandingBundle() {
    return ProductProvider.getCurrentBranding().getDefiningBundle();
  }

  public String getBrandingDescription() {
    return ProductProvider.getCurrentBranding().getDescription();
  }

  public String getBrandingId() {
    return ProductProvider.getCurrentBranding().getId();
  }

  public String getBrandingName() {
    return ProductProvider.getCurrentBranding().getName();
  }

  public String getBrandingProperty( final String key ) {
    return ProductProvider.getCurrentBranding().getProperty( key );
  }
}