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
package com.w4t.engine.util;

/**
 * <p>Utility class that allow reading of the respectice corresponding 
 * system property.</p>
 */
public final class SystemProps {
  
  public static final String USE_VERSIONED_JAVA_SCRIPT 
    = "com.w4t.useVersionedJavaScript";
  
  public static final String USE_COMPRESSED_JAVA_SCRIPT 
    = "com.w4t.useCompressedJavaScript";
    

  private SystemProps() {
    // prevent instantiation
  }
  
  public static boolean useVersionedJavaScript() {
    return getBooleanProperty( USE_VERSIONED_JAVA_SCRIPT, true );
  }
  
  public static boolean useCompressedJavaScript() {
    return getBooleanProperty( USE_COMPRESSED_JAVA_SCRIPT, true );
  }

  private static boolean getBooleanProperty( final String key, 
                                             final boolean defaultValue ) 
  {
    boolean result = defaultValue;
    String propertyValue = System.getProperty( key );
    if( propertyValue != null ) {
      result = propertyValue.equalsIgnoreCase( "true" );
    }
    return result;
  }
  
}
