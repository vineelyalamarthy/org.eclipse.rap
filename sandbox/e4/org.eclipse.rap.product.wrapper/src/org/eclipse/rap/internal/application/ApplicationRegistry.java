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
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.rap.ui.internal.servlet.EntryPointExtension;
import org.eclipse.rwt.internal.lifecycle.EntryPointManager;
import org.eclipse.rwt.internal.service.ContextProvider;
import org.eclipse.rwt.service.ISessionStore;
import org.eclipse.ui.internal.WorkbenchPlugin;
import org.osgi.framework.Bundle;

/*
 * Registers all available applications as entrypoints.
 */
public class ApplicationRegistry {

  private static final String RUN = "run";
  private static final String PI_RUNTIME = "org.eclipse.core.runtime"; //$NON-NLS-1$
  private static final String PT_APPLICATIONS = "applications"; //$NON-NLS-1$
  private static Map applicationEntrypointMapping = new HashMap();
  private static final String CURRENT_ENTRY_POINT = EntryPointManager.class.getName()
                                                    + ".CurrentEntryPointName";

  public static synchronized IApplication getApplication() {
    IApplication application = null;
    final String currentEntryPoint = getCurrentEntryPoint();
    final Class clazz = ( Class )applicationEntrypointMapping.get( currentEntryPoint );
    try {
      application = ( IApplication )clazz.newInstance();
    } catch( final InstantiationException e ) {
      e.printStackTrace();
    } catch( final IllegalAccessException e ) {
      e.printStackTrace();
    }
    return application;
  }

  public static String getCurrentEntryPoint() {
    final ISessionStore session = ContextProvider.getSession();
    return ( String )session.getAttribute( CURRENT_ENTRY_POINT );
  }

  private static void registerApplication( final IConfigurationElement configurationElement )
  {
    final String contributorName = configurationElement.getContributor()
      .getName();
    final IConfigurationElement[] runElement = configurationElement.getChildren( RUN );
    final String className = runElement[ 0 ].getAttribute( "class" );
    String applicationId = null;
    try {
      // ignore error application of equinox
      if( !contributorName.equals( "org.eclipse.equinox.app" ) ) {
        final Bundle bundle = Platform.getBundle( contributorName );
        final Class clazz = bundle.loadClass( className );
        applicationId = contributorName + ".application";
        EntryPointManager.register( applicationId,
                                    EntrypointApplicationWrapper.class );
        EntryPointExtension.bind( applicationId, applicationId );
        applicationEntrypointMapping.put( applicationId, clazz );
      }
    } catch( final Exception e ) {
      final String text = "Could not register entry point ''{0}'' "
                          + "with request startup parameter ''{1}''.";
      final Object[] params = new Object[]{
        className, applicationId
      };
      final String msg = MessageFormat.format( text, params );
      final IStatus status = new Status( IStatus.ERROR,
                                         contributorName,
                                         IStatus.OK,
                                         msg,
                                         e );
      WorkbenchPlugin.getDefault().getLog().log( status );
    }
  }

  public static void registerApplicationEntryPoints() {
    final IExtensionRegistry registry = Platform.getExtensionRegistry();
    final IExtensionPoint point = registry.getExtensionPoint( PI_RUNTIME
                                                              + '.'
                                                              + PT_APPLICATIONS );
    final IConfigurationElement[] elements = point.getConfigurationElements();
    for( int i = 0; i < elements.length; i++ ) {
      final IConfigurationElement configurationElement = elements[ i ];
      registerApplication( configurationElement );
    }
  }
}
