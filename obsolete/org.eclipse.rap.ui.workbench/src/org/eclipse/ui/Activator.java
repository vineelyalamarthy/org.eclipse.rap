/*******************************************************************************
 * Copyright (c) 2002-2006 Innoopract Informationssysteme GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Innoopract Informationssysteme GmbH - initial API and implementation
 ******************************************************************************/

package org.eclipse.ui;

import java.text.MessageFormat;
import org.eclipse.core.runtime.*;
import org.eclipse.ui.internal.misc.StatusUtil;
import org.osgi.framework.BundleContext;


public class Activator extends Plugin {

  public static final String PLUGIN_ID = "org.eclipse.rap.ui.workbench";
  private static Activator plugin;
  private BundleContext context;
  
  public void start( final BundleContext context ) throws Exception {
    super.start( context );
    plugin = this;
    this.context = context;
  }

  public void stop( final BundleContext context ) throws Exception {
    this.context = null;
    plugin = null;
    super.stop( context );
  }

  public static Activator getDefault() {
    return plugin;
  }
  
  public BundleContext getContext() {
    return context;
  }

  public static Object createExtension( final IConfigurationElement element,
                                        final String classAttribute )
    throws CoreException
  {
//    try {
      // If plugin has been loaded create extension.
      // Otherwise, show busy cursor then create extension.
//      if( BundleUtility.isActivated( element.getDeclaringExtension()
//        .getNamespace() ) )
//      {
        return element.createExecutableExtension( classAttribute );
//      }
//      final Object[] ret = new Object[ 1 ];
//      final CoreException[] exc = new CoreException[ 1 ];
//      BusyIndicator.showWhile( null, new Runnable() {
//
//        public void run() {
//          try {
//            ret[ 0 ] = element.createExecutableExtension( classAttribute );
//          } catch( CoreException e ) {
//            exc[ 0 ] = e;
//          }
//        }
//      } );
//      if( exc[ 0 ] != null ) {
//        throw exc[ 0 ];
//      }
//      return ret[ 0 ];
//    } catch( CoreException core ) {
//      throw core;
//    } catch( Exception e ) {
//      throw new CoreException( new Status( IStatus.ERROR,
//                                           PI_WORKBENCH,
//                                           IStatus.ERROR,
//                                           WorkbenchMessages.WorkbenchPlugin_extension,
//                                           e ) );
//    }
  }  
  
  // ////////////////////
  // methods for logging
  
  public static void log( final String message ) {
    IStatus status = StatusUtil.newStatus( IStatus.ERROR, message, null );
    getDefault().getLog().log( status );    
  }

  public static void log( final String message, final Throwable throwable ) {
    IStatus status = StatusUtil.newStatus( IStatus.ERROR, message, throwable );
    log( message, status );
  }

  public static void log( final Class clazz, 
                          final String methodName, 
                          final Throwable throwable )
  {
    Object[] params = new Object[]{
      clazz.getName(),
      methodName,
      throwable
    };
    String msg = MessageFormat.format( "Exception in {0}.{1}: {2}", params );
    log( msg, throwable );
  }

  public static void log( final String message, final IStatus status ) {
    if( message != null ) {
      IStatus newStatus = StatusUtil.newStatus( IStatus.ERROR, message, null );
      getDefault().getLog().log( newStatus );
    }
    getDefault().getLog().log( status );
  }

  public static void log( final Throwable throwable ) {
    getDefault().getLog().log( getStatus( throwable ) );
  }

  public static IStatus getStatus( final Throwable throwable ) {
    String message = throwable.getMessage();
    return newError( message, throwable );
  }

  public static IStatus newError( final String message, 
                                  final Throwable throwable ) {
    String pluginId = "org.eclipse.rap.ui.workbench";
    int errorCode = IStatus.OK;
    // If this was a CoreException, keep the original plugin ID and error
    // code
    if( throwable instanceof CoreException ) {
      CoreException ce = ( CoreException )throwable;
      pluginId = ce.getStatus().getPlugin();
      errorCode = ce.getStatus().getCode();
    }
    return new Status( IStatus.ERROR,
                       pluginId,
                       errorCode,
                       message,
                       StatusUtil.getCause( throwable ) );
  }
}
