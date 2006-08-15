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
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import com.w4t.IResourceManager;
import com.w4t.engine.util.ResourceManager;

/** Helper class for DelegateClassLoader instanciation. Functionality belongs
 *  to the DelegateClass (see TestCase) but it is rolled out into this class
 *  since its static content must span first and second level W4Toolkit
 *  namespaces. Therefore it is loaded by the webapps context classloader. */
public class ExcludeManager {
  
  /** <p>Name of the file located on the applications classpath which
   *  contains the library list to exclude.</p> */
  public final static String EXCLUSION_LIST = "exclude.list";
  
  private static String[] excludeLoadingClassList;
  
  public static String[] getExclusionList( final URL[] classPath,
                                           final ClassLoader contextLoader )
  {
    if( excludeLoadingClassList == null ) {
      try {
        HashSet excluded = new HashSet();
        URL resource = contextLoader.getResource( EXCLUSION_LIST );
        if( resource == null ) {
          IResourceManager manager = ResourceManager.getInstance();
          resource = manager.getResource( EXCLUSION_LIST );
        }
        URLConnection connection = resource.openConnection();
        connection.setUseCaches( false );
        InputStream is = connection.getInputStream();
        InputStreamReader isr = new InputStreamReader( is );
        BufferedReader br = new BufferedReader( isr );
        while( br.ready() ) {
          String line = br.readLine().trim();
          if( isNameSpaceExclusion( line ) ) {
            excluded.add( line );
          } else {
            File library = getLibrary( classPath, line );
            if( library != null ) {
              load( excluded, library );
            }
          }
        }
        excludeLoadingClassList = new String[ excluded.size() ];
        excluded.toArray( excludeLoadingClassList );
      } catch( Exception e ) {
        excludeLoadingClassList = new String[ 0 ];
      }
    }
    return excludeLoadingClassList;
  }
  
  private static boolean isNameSpaceExclusion( final String line ) {
    return !line.toLowerCase().endsWith( ".jar" );
  }

  private static File getLibrary( final URL[] classPath, final String token ) {
    File result = null;
    for( int i = 0; result == null && i < classPath.length; i++ ) {
      if( classPath[ i ].toString().endsWith( token ) ) {
        File file = new File( classPath[ i ].getPath() );
        if( file.exists() ) {
          result = file;
        }
      }
    }
    return result;
  }
  
  private static void load( final HashSet excluded, final File library ) 
    throws IOException 
  {
    JarFile jarFile = new JarFile( library );
    Enumeration entries = jarFile.entries();
    while( entries. hasMoreElements() ) {
      JarEntry entry = ( JarEntry )entries.nextElement();
      if( entry.getName().endsWith( ".class" ) ) {
        String className = entry.getName();
        className = className.replace( '/', '.' );
        className = className.replace( '\\', '.' );
        className = className.substring( 0 , className.indexOf( ".class" ) );
        excluded.add( className );
      }
    }
  }
}