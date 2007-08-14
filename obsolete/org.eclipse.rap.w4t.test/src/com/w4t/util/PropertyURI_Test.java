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
package com.w4t.util;

import junit.framework.TestCase;

/** <p>Tests the parsing functionality of org.eclipse.rap.util.PropertyURI.</p>
  */
public class PropertyURI_Test extends TestCase {

  public PropertyURI_Test( final String name ) {
    super( name );
  }

  // testing methods
  //////////////////
  
  public void testParsing() throws Exception {
    String uriString_1 = "";
    String uriString_2 = "://";
    String uriString_3 = "invalidProtocol://aaa@aaa";
    String uriString_4 = "property://There is no separator here.";
    String uriString_5 = "property://correct@valid";
    
    checkInvalidString( uriString_1 );
    checkInvalidString( uriString_2 );
    checkInvalidString( uriString_3 );
    checkInvalidString( uriString_4 );
    assertEquals( uriString_5, new PropertyURI( uriString_5 ).toString() );
  }

  private void checkInvalidString( final String invalidUri ) {  
    try {
      new PropertyURI( invalidUri );
      fail( "No exception with '" + invalidUri + "'." );
    } catch( InvalidPropertyURIException ipuex ) {
    }
  }
}