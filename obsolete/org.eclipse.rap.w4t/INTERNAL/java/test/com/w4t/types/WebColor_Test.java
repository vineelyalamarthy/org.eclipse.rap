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
package com.w4t.types;

import junit.framework.TestCase;


/** <p>unit tests for WebColor.</p>
  */
public class WebColor_Test extends TestCase {

  private WebColor wco_1 = new WebColor( "#FF0000" );
  private WebColor wco_2 = new WebColor( "#FF0000" );
  // should be the same as "#FF0000" above
  private WebColor wco_3 = new WebColor( "red" );
  private WebColor wco_4 = new WebColor( "#abcdef" );
  private WebColor wco_5 = new WebColor( "#000000" );
  private WebColor wco_6 = new WebColor( "#0000FF" );

  public WebColor_Test( String name ) {
    super( name );
  }

  
  // test code
  ////////////
  
  public void testEquals() throws Exception {
    assertTrue( "'#FF0000' should be the same as '#FF0000'", 
                wco_1.equals( wco_2 ) );
    assertTrue( "'#FF0000' should be the same as 'red'", 
                wco_1.equals( wco_3 ) );
    assertTrue( "'#FF0000' should be different from '#abcdef'", 
                !wco_1.equals( wco_4 ) );
    assertTrue( "'red' should be different from  '#abcdef'", 
                !wco_3.equals( wco_4 ) );
  }
  
  public void testUnknownColor() {
    WebColor color = new WebColor( "abc" );
    assertEquals( "abc", color.toString() );
  }
  
  public void testHashCode() throws Exception {
    assertTrue(   "wrong hashcode for '#FF0000': " 
                + wco_1.hashCode() 
                + ", should be 16711680",
                wco_1.hashCode() == "#FF0000".hashCode() );
    assertTrue(   "wrong hashcode for 'red': " 
                + wco_3.hashCode() 
                + ", should be 16711680",
                wco_3.hashCode() == "#FF0000".hashCode() );
    assertTrue(   "wrong hashcode for '#abcdef': " 
                + wco_4.hashCode() 
                + ", should be 11259375",
                wco_4.hashCode() == "#abcdef".hashCode() );
    assertTrue(   "wrong hashcode for '#000000': " 
                + wco_5.hashCode() 
                + ", should be 0",
                wco_5.hashCode() == "#000000".hashCode() );
    assertTrue(   "wrong hashcode for '#0000FF': " 
                + wco_6.hashCode() 
                + ", should be 255",
                wco_6.hashCode() == "#0000FF".hashCode() );
  }
}
