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
package com.w4t;



/**
 * <p>Even though it might appear silly to test a test-class, due to the 
 * weird escaping of String#replaceAll() (used by RenderingTestCase#escapeAll)
 * this test case was handy to achieve the desired behaviour. 
 * </p>
 */
public class RenderingTestCase_Test extends RenderingTestCase {

  public RenderingTestCase_Test( final String name ) {
    super( name );
  }

  public void testEscapeAll() {
//  TODO: check whether there are any problems removing this 
//    String[] tokens = new String[] { "<div id=\"", };
//    escapeAll( tokens );
//    assertEquals( "<div id=\\\"", tokens[0] );
//    
//    tokens = new String[] { "quote the end\"", };
//    escapeAll( tokens );
//    assertEquals( "quote the end\\\"", tokens[0] );
//    
//    tokens = new String[] { "ab\rc", };
//    escapeAll( tokens );
//    assertEquals( "ab\\rc", tokens[0] );
//    
//    tokens = new String[] { "ab\rc", };
//    escapeAll( tokens );
//    assertEquals( "ab\\rc", tokens[0] );
//    
//    tokens = new String[] { "ab\nc", };
//    escapeAll( tokens );
//    assertEquals( "ab\\nc", tokens[0] );
  }
  
}
