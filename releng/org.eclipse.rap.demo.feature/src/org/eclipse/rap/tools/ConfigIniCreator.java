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

package org.eclipse.rap.tools;

import java.io.File;


/**
 * <p>This tool creates the content of a simple config.ini file.
 * Run this after the ANT build scripts (webappBuilder.xml and the 
 * pde.exportFeatures) have finished. After that replace the content
 * of the config.ini file in the build.</p> 
 * 
 * <p>Note: this is not meant to be a high end deployment tool or the only
 * possibility of how the content of your config.ini should look like if you
 * are creating a RAP WAR. This should simplify the task to get
 * a minimalistic runtime configuration that works...</p>
 */
public class ConfigIniCreator {
  
  public static void main( final String[] arx ) {
    ////////////////////////////////////////////////////////////////////////////
    // replace this with the absolute path to the plugin directory of
    // the deployment build for example
    // File file = new File( "C:\\projects\\org.eclipse.rap\\org.eclipse.rap.demo.feature\\build\\rapdemo\\WEB-INF\\eclipse\\plugins" );
    File file = new File( "Path to the plugin directory of your build" );
    ////////////////////////////////////////////////////////////////////////////
    
    String[] list = file.list();
    StringBuffer buffer = new StringBuffer();
    buffer.append( "#Eclipse Runtime Configuration File\n" );
    
    buffer.append( "osgi.bundles=" );
    for( int i = 0; i < list.length; i++ ) {
      if(    list[ i ].endsWith( ".jar" )
          && !list[ i ].startsWith( "org.eclipse.osgi_" ) )
      {
        buffer.append( list[ i ] );
        if( list[ i ].startsWith( "org.eclipse.equinox.common_" ) ) {
          buffer.append( "@2:start" );
        } else {
          buffer.append( "@start" );
        }
        if( i + 1 < list.length ) {
          buffer.append( "," );
        }
      }
    }
    buffer.append( "\n" );
    buffer.append( "osgi.bundles.defaultStartLevel=4\n" );
    
    // write the content to the console
    System.out.print( buffer );
  }
}
