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
package com.w4t.util.browser;

/**
 * The Konqueror Browser Detection Class
 */
public class DetectorKonqueror extends DetectorBase {

  private final static String ID_KONQUEROR = "konqueror";
  private final static String ID_KONQUEROR31 = "konqueror/3.1";
  private final static String ID_KONQUEROR32 = "konqueror/3.2";
  private final static String ID_KONQUEROR33 = "konqueror/3.3";
  private final static String ID_KONQUEROR34 = "konqueror/3.4";
  

  public DetectorKonqueror() {
    super();
  }

  public boolean knowsBrowserString( final String userAgent ) {
    return contains( userAgent, ID_KONQUEROR );
  }

  public String getBrowserName( final String userAgent ) {
    String result = "Konqueror";
    return result;
  }

  public String getBrowserClassName( final String userAgent ) {
    String result = "com.w4t.util.browser."
                    + getBrowserName( userAgent )
                    + getBrowserVersion( userAgent );
    return result;
  }

  public String getBrowserVersion( final String userAgent ) {
    String result = "";
    if( isKonqueror3_1( userAgent ) ) {
      result = "3_1";
    } else if( isKonqueror3_2( userAgent ) ) {
      result = "3_2";
    } else if( isKonqueror3_3( userAgent ) ) {
      result = "3_3";
    } else if( isKonqueror3_4( userAgent ) ) {
      result = "3_4";
    }
    return result;
  }

  private boolean isKonqueror3_4( final String userAgent ) {
    return contains( userAgent, ID_KONQUEROR34 );
  }
  
  private boolean isKonqueror3_3( final String userAgent ) {
    return contains( userAgent, ID_KONQUEROR33 );
  }
  
  private boolean isKonqueror3_2( final String userAgent ) {
    return contains( userAgent, ID_KONQUEROR32 );
  }

  private boolean isKonqueror3_1( final String userAgent ) {
    return contains( userAgent, ID_KONQUEROR31 );
  }

}
