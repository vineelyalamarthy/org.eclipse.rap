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

import junit.framework.TestCase;


public class WebForm_Test extends TestCase {
  
  private static final String MYSTYLE1_CSS = "mystyle1.css";
  private static final String MYSTYLE2_CSS = "mystyle2.css";

  public void testCssFiles() {
    WebForm form = W4TFixture.getEmptyWebFormInstance();
    // addCssFile
    form.addCssFile( MYSTYLE1_CSS );
    form.addCssFile( MYSTYLE1_CSS );
    assertEquals( 1, form.getCssFile().length );
    // setCssFile(int,String)
    form.setCssFile( 0, MYSTYLE1_CSS );
    assertEquals( 1, form.getCssFile().length );
    form.addCssFile( MYSTYLE2_CSS );
    try {
      form.setCssFile( 1, MYSTYLE1_CSS );
      fail( "Duplicate cssFiles not allowed" );
    } catch ( IllegalArgumentException e ) {
      // expected
    }
    //setCssFile(String[])
    form.setCssFile( new String[] { MYSTYLE1_CSS, MYSTYLE2_CSS } );
    assertEquals( 2, form.getCssFile().length );
    try {
      form.setCssFile( new String[] { MYSTYLE1_CSS, MYSTYLE1_CSS } );
      fail( "Duplicate cssFiles not allowed" );
    } catch( IllegalArgumentException e ) {
      // expected
    }
  }
  
  protected void setUp() throws Exception {
    W4TFixture.setUp();
  }
  
  protected void tearDown() throws Exception {
    W4TFixture.tearDown();
  }
}
