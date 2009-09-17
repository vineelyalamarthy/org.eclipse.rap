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

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.rap.ui.internal.servlet.EntryPointExtension;
import org.eclipse.rwt.internal.lifecycle.EntryPointManager;
import org.eclipse.ui.internal.WorkbenchPlugin;
import org.osgi.framework.Bundle;

/*
 * Registers all available applications as entrypoints.
 */
public class ApplicationRegistry {

  private static Map appEntrypointMapping = new HashMap();
  private static final String RUN = "run";
  private static final String PI_RUNTIME = "org.eclipse.core.runtime";
  private static final String PT_APPLICATIONS = "applications";
  private static final String PT_APP_VISIBLE = "visible"; //$NON-NLS-1$

  public static IApplication getApplication() {
    IApplication application = null;
    String currentEntryPoint = EntryPointManager.getCurrentEntryPoint();
    Class clazz = ( Class )appEntrypointMapping.get( currentEntryPoint );
    try {
      application = ( IApplication )clazz.newInstance();
    } catch( final InstantiationException e ) {
      e.printStackTrace();
    } catch( final IllegalAccessException e ) {
      e.printStackTrace();
    }
    return application;
  }

  private static void registerApplication( final IExtension extension ) {
    IConfigurationElement configElement
      = extension.getConfigurationElements()[0];
    String contributorName = configElement.getContributor().getName();
    IConfigurationElement[] runElement = configElement.getChildren( RUN );
    String className = runElement[ 0 ].getAttribute( "class" );
    String applicationId = extension.getUniqueIdentifier();
    String isVisible = configElement.getAttribute( PT_APP_VISIBLE );

    try {
      // ignore invisible applications
      if( isVisible == null || Boolean.valueOf( isVisible ).booleanValue() ) {
        Bundle bundle = Platform.getBundle( contributorName );
        Class clazz = bundle.loadClass( className );
        appEntrypointMapping.put( applicationId, clazz );
        EntryPointManager.register( applicationId,
                                    EntrypointApplicationWrapper.class );
        EntryPointExtension.bind( applicationId, applicationId );
      }
    } catch( final Exception e ) {
      String text =   "Could not register entry point ''{0}'' "
                    + "with request startup parameter ''{1}''.";
      Object[] params = new Object[]{ className, applicationId };
      String msg = MessageFormat.format( text, params );
      IStatus status = new Status( IStatus.ERROR, contributorName,
                                         IStatus.OK, msg, e );
      WorkbenchPlugin.getDefault().getLog().log( status );
    }
  }

  public static void registerApplicationEntryPoints() {
    IExtension[] elements = getApplicationExtensions();
    for( int i = 0; i < elements.length; i++ ) {
      IExtension extension = elements[ i ];
      registerApplication( extension );
    }
  }

  private static IExtension[] getApplicationExtensions() {
    IExtensionRegistry registry = Platform.getExtensionRegistry();
    String extensionPointId = PI_RUNTIME + '.' + PT_APPLICATIONS;
    IExtensionPoint extensionPoint
      = registry.getExtensionPoint( extensionPointId );
    return extensionPoint.getExtensions();
  }
}
