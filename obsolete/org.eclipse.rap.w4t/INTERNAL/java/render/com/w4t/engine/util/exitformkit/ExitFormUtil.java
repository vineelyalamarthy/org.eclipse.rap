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
package com.w4t.engine.util.exitformkit;

import java.io.*;
import java.text.MessageFormat;

import org.eclipse.rwt.internal.resources.ResourceManagerImpl;
import org.eclipse.rwt.internal.util.HTML;
import org.eclipse.rwt.resources.IResourceManager;

import com.w4t.types.LocalPath;


final class ExitFormUtil {

  static final String EXTERNAL_FILE = "exit.html";
  static final String INTERNAL_FILE = "resources/html/exit.html";
  
  static void registerInternalFile() {
    IResourceManager manager = ResourceManagerImpl.getInstance();
    manager.register( INTERNAL_FILE, HTML.CHARSET_NAME_ISO_8859_1 );
  }
  
  static boolean externalFileExists() {
    File file = new LocalPath( EXTERNAL_FILE ).toFile();
    return file.exists();
  }
  
  static InputStream open() throws IOException {
    InputStream result;
    File file = new LocalPath( EXTERNAL_FILE ).toFile();
    if( file.exists() ) {
      result = new FileInputStream( file );
    } else {
      result 
        = ResourceManagerImpl.getInstance().getResourceAsStream( INTERNAL_FILE );
    }
    return result;
  }
  
  static String load() 
    throws IOException 
  {
    StringBuffer buffer = new StringBuffer();
    InputStream is = open();
    if( is == null ) {
      String text = "The resource ''{0}'' could not be found.";
      String msg = MessageFormat.format( text, new Object[] { INTERNAL_FILE } );
      throw new IllegalArgumentException( msg );
    }
    try {
      InputStreamReader reader 
        = new InputStreamReader( is, HTML.CHARSET_NAME_ISO_8859_1 );
      BufferedReader br = new BufferedReader( reader );
      try {
        int character = br.read();    
        while( character != -1 ) {
          buffer.append( ( char )character );
          character = br.read();
        }
      } finally {
        br.close();
      }
    } finally {
      is.close();
    }
    return buffer.toString();
  }

  private ExitFormUtil() {
    // prevent instantiation
  }
}
