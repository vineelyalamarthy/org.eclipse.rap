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

import org.eclipse.core.runtime.IContributor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.equinox.http.registry.HttpContextExtensionService;
import org.eclipse.rap.internal.product.ProductProvider;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;

public class Activator implements BundleActivator {

  /*
   * (non-Javadoc)
   * @see
   * org.eclipse.core.runtime.Plugins#start(org.osgi.framework.BundleContext)
   */
  public void start( final BundleContext context ) throws Exception {
    
    // RAP [bm]: ensure that the rap http context was loaded before
    //           the mapping of servlets takes place
    
    // TODO [bm]: can be removed once we live in the workbench bundle,
    // code should be moved to WorkbenchPlugin#start into ServiceTracker
    String serviceName = HttpContextExtensionService.class.getName();
    ServiceTracker httpContextExtensionServiceTracker
      = new ServiceTracker( context, serviceName, null )
    {
      public Object addingService( final ServiceReference reference ) {
        ApplicationRegistry.registerApplicationEntryPoints();
        ProductProvider.injectProductProvider();
        return super.addingService( reference );
      }
    };
    httpContextExtensionServiceTracker.open();
  }
  
  // [bm] verbatim copy of org.eclipse.equinox.internal.app.getBundle
  public static Bundle getBundle(IContributor contributor) {
//    if (contributor instanceof RegistryContributor) {
//      try {
//        long id = Long.parseLong(((RegistryContributor) contributor).getActualId());
//        if (bundleContext != null)
//          return bundleContext.getBundle(id);
//      } catch (NumberFormatException e) {
//        // try using the name of the contributor below
//      }
//    }
//    PackageAdmin packageAdmin = _packageAdmin;
//    if (packageAdmin == null)
//      return null;
//    Bundle[] bundles = packageAdmin.getBundles(contributor.getName(), null);
//    if (bundles == null)
//      return null;
//    //Return the first bundle that is not installed or uninstalled
//    for (int i = 0; i < bundles.length; i++)
//      if ((bundles[i].getState() & (Bundle.INSTALLED | Bundle.UNINSTALLED)) == 0)
//        return bundles[i];
//    return null;
    return Platform.getBundle( contributor.getName() );
  }

  public void stop( BundleContext context ) throws Exception {
    // do nothing
  }
}
