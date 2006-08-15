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
package com.w4t.engine.classloader;

import java.io.*;
import java.util.Vector;
import com.w4t.util.ConfigurationReader;


/** <p>encapsulates and preloades classes for the classloader namespace 
  * that is global for all sessions. Classes to preload in the global
  * namespace can be listed in a file which is named in the W4T.xml
  * configuration file as the property 'globalClassList'.</p>
  */
public class GlobalClasses {
  
  private Vector classList;

  
  public GlobalClasses() {
    this.classList = new Vector();
  }
  
  public GlobalClasses( final String configuredGlobalClassesFile ) 
    throws Exception
  {
    this();
    readConfiguredClasses( configuredGlobalClassesFile );
  }

  public void preload() throws Exception {
    preloadW4TClasses();
    preloadConfiguredClasses();
  }
  
  
  // helping methods
  //////////////////

  private void preloadW4TClasses() throws Exception {
    Class.forName( "com.w4t.WebComponentStatistics" );
    Class.forName( "com.w4t.util.image.ImageDescriptor" );
    Class.forName( "com.w4t.util.image.ImageCreator" );
    Class.forName( "com.w4t.util.DefaultColorScheme" );
    Class.forName( "com.w4t.util.ContextDirManager" );
    Class.forName( "com.w4t.util.CssClass" );
    Class.forName( "com.w4t.internal.adaptable.IServiceAdapter" );
    Class.forName( "com.w4t.engine.classloader.HttpSessionImpl" );
    Class.forName( "org.apache.commons.fileupload.FileItem" );
  }
  
  private void preloadConfiguredClasses() throws Exception {
//long start = System.currentTimeMillis();    
//System.out.print(   "Preloading " + classList.size() 
//                  + " classes into global namespace ... " );
    for( int i = 0; i < classList.size(); i++ ) {
      String className = "";
      try {
        className = ( String )classList.get( i );
        Class.forName( className );
      } catch( ClassNotFoundException clanofex ) {
        System.out.println(   "Could not preload the class "
                            + className 
                            + " in the namespace global for sessions.\n"
                            + "Please check the configuration settings for "
                            + "global classes preload.\n These settings are "
                            + "located in the file named in the "
                            + "'globalClassList' attribute of the W4T.xml "
                            + "configuration file in the WEBINF/conf/ "
                            + "directory of the web application." );
      }
    }
//System.out.print( ( System.currentTimeMillis() - start ) + " ms." );
  }
  
  private void readConfiguredClasses( final String configuredGlobalClassesFile )
                                                            throws IOException {
    File file = getConfigFile( configuredGlobalClassesFile );
    if( file != null ) {
      BufferedReader br = new BufferedReader( new FileReader( file ) );
      String line = br.readLine();
      while( line != null ) {
        String className = line.trim();
        if( !className.equals( "" ) ) {
          classList.add( className );
        }
        line = br.readLine();
      }
      br.close();
    }
  }
  
  private File getConfigFile( final String fileName ) {
    File result = null;
    if(    fileName != null
        && !fileName.equals( "" ) ) {
      File serverDir = ConfigurationReader.getEngineConfig().getServerContextDir();
      File specifiedFile = new File(   serverDir 
                                     + File.separator
                                     + fileName );
      if( !specifiedFile.exists() ) {
        System.out.println(   "Could not find the configuration file for "
                            + "loading classes in the namespace global for "
                            + "sessions.\nFile named in the W4T.xml "
                            + "configuration file is " + fileName + "." );
      } else {
        result = specifiedFile;
      }
    }
    return result;
  }
}