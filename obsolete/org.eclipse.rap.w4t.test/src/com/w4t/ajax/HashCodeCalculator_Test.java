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
package com.w4t.ajax;

import junit.framework.TestCase;

import com.w4t.*;


public class HashCodeCalculator_Test extends TestCase {
  
  private static class TestClass {
    // empty
  }

  public void testArray() {
    HashCodeCalculator calculator = new HashCodeCalculator();
    TestClass[] array = new TestClass[]{
      new TestClass(), new TestClass()
    };
    int hashCode1 = calculator.append( array ).toHashCode();
    calculator = new HashCodeCalculator();
    array[ 0 ] = new TestClass();
    int hashCode2 = calculator.append( array ).toHashCode();
    assertTrue( hashCode1 != hashCode2 );
  }
  
  public void testContainer() throws Exception {
    // border layout
    HashCodeCalculator calculator = new HashCodeCalculator();
    WebPanel panel = new WebPanel();
    panel.setWebLayout( new WebBorderLayout() );
    WebLabel label = new WebLabel();
    panel.add( label, WebBorderLayout.NORTH );
    int hashCode1 = calculator.append( panel ).toHashCode();
    label.remove();
    panel.add( label, WebBorderLayout.CENTER );
    calculator = new HashCodeCalculator();
    int hashCode2 = calculator.append( panel ).toHashCode();
    assertTrue( hashCode1 != hashCode2 );
    
    // card layout
    label.remove();
    panel.setWebLayout( new WebCardLayout() );
    panel.add( label, "constraint1" );
    calculator = new HashCodeCalculator();
    hashCode1 = calculator.append( panel ).toHashCode();
    label.remove();
    panel.add( label, "constraint2" );
    calculator = new HashCodeCalculator();
    hashCode2 = calculator.append( panel ).toHashCode();
    assertTrue( hashCode1 != hashCode2 );
    
    // grid layout
    label.remove();
    panel.setWebLayout( new WebGridLayout( 2, 2 ) );
    panel.add( label, new Position( 1, 1 ) );
    calculator = new HashCodeCalculator();
    hashCode1 = calculator.append( panel ).toHashCode();
    label.remove();
    panel.add( label, new Position( 2, 2 ) );
    calculator = new HashCodeCalculator();
    hashCode2 = calculator.append( panel ).toHashCode();
    assertTrue( hashCode1 != hashCode2 );
  }
  
  protected void setUp() throws Exception {
    W4TFixture.setUp();
    W4TFixture.createContext();
  }

  protected void tearDown() throws Exception {
    W4TFixture.tearDown();
    W4TFixture.removeContext();
  }
}
