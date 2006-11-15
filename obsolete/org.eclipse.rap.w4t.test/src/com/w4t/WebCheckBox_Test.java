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


public class WebCheckBox_Test extends TestCase {
  
  protected void setUp() throws Exception {
    Fixture.setUp();
  }
  
  protected void tearDown() throws Exception {
    Fixture.tearDown();
  }
  
  public void testValue() {
    WebCheckBox checkBox = new WebCheckBox();
    // ensure initial state 
    assertEquals( false, checkBox.isSelected() );
    assertEquals( checkBox.getValUnCheck(), checkBox.getValue() );
    // ensure, that null is 'converted' to enpty string
    checkBox.setValue( null );
    assertEquals( "", checkBox.getValue() );
    // ensure that setting a value other than 'valChecked' or 'valUnchecked'
    // causes the checkBox to be in 'selected' state
    checkBox.setValCheck( "checked" );
    checkBox.setValUnCheck( "unchecked" );
    checkBox.setValue( "xyz" );
    assertEquals( true, checkBox.isSelected() );
    // assert behaviour of setValUnChecked 
    try {
      checkBox.setValUnCheck( null );
    } catch( NullPointerException e ) {
      // expected
    }
    // assert behaviour of setValChecked 
    try {
      checkBox.setValCheck( null );
    } catch( NullPointerException e ) {
      // expected
    }
  }
  
  public void testSetSelected() {
    WebCheckBox checkBox = new WebCheckBox();
    checkBox.setSelected( true );
    assertEquals( checkBox.getValue(), checkBox.getValCheck() );
    checkBox.setSelected( false );
    assertEquals( checkBox.getValue(), checkBox.getValUnCheck() );
  }
  
}
