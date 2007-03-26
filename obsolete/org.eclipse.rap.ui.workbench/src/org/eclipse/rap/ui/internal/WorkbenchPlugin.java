/*******************************************************************************
 * Copyright (c) 2007 Innoopract Informationssysteme GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Innoopract Informationssysteme GmbH - initial API and implementation
 ******************************************************************************/

package org.eclipse.rap.ui.internal;

import org.eclipse.core.runtime.*;
import org.eclipse.rap.rwt.custom.BusyIndicator;
import org.eclipse.rap.ui.PlatformUI;
import org.eclipse.rap.ui.internal.util.BundleUtility;


public class WorkbenchPlugin {

  public static String PI_WORKBENCH = PlatformUI.PLUGIN_ID;

  /**
   * Creates an extension. If the extension plugin has not been loaded a busy
   * cursor will be activated during the duration of the load.
   * 
   * @param element the config element defining the extension
   * @param classAttribute the name of the attribute carrying the class
   * @return the extension object
   * @throws CoreException if the extension cannot be created
   */
  public static Object createExtension( final IConfigurationElement element,
                                        final String classAttribute )
    throws CoreException
  {
    try {
      // If plugin has been loaded create extension.
      // Otherwise, show busy cursor then create extension.
      if( BundleUtility.isActivated( element.getDeclaringExtension()
        .getNamespace() ) )
      {
        return element.createExecutableExtension( classAttribute );
      }
      final Object[] ret = new Object[ 1 ];
      final CoreException[] exc = new CoreException[ 1 ];
      BusyIndicator.showWhile( null, new Runnable() {

        public void run() {
          try {
            ret[ 0 ] = element.createExecutableExtension( classAttribute );
          } catch( CoreException e ) {
            exc[ 0 ] = e;
          }
        }
      } );
      if( exc[ 0 ] != null ) {
        throw exc[ 0 ];
      }
      return ret[ 0 ];
    } catch( CoreException core ) {
      throw core;
    } catch( Exception e ) {
      throw new CoreException( new Status( IStatus.ERROR,
                                           PI_WORKBENCH,
                                           IStatus.ERROR,
//                                           WorkbenchMessages.WorkbenchPlugin_extension,
                                           "Cannot create extension",
                                           e ) );
    }
  }

  public static void log( final String msg ) {
    // TODO [rh] decent implementation
System.out.println( msg );    
  }
  
  public static void log( final String msg, final IStatus status ) {
    // TODO [rh] decent implementation
System.out.println( msg );    
  }

  public static void log( final String msg, final Throwable cause ) {
    // TODO [rh] decent implementation
    System.out.println( msg );    
  }
  
  public static void log( final Throwable exception ) {
    // TODO [rh] decent implementation
System.out.println( exception );    
  }

  public static void log( final IStatus logStatus ) {
    // TODO [rh] decent implementation
System.out.println( logStatus );    
  }
}
