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
import com.w4t.WebObject;


public class HashCodeBuilderFactory_Test extends TestCase {
  
  public void testRegisterBuilderWithWrongArguments() {
    try {
      HashCodeBuilderFactory.registerBuilder( null, null );
      fail( "should not get here" );
    } catch( NullPointerException e ) {
      // as expected
    }
    try {
      HashCodeBuilderFactory.registerBuilder( WebObject.class, null );
      fail( "should not get here" );
    } catch( NullPointerException e ) {
      assertEquals( NullPointerException.class, e.getClass() );
    }
    try {
      HashCodeBuilderFactory.registerBuilder( null, 
                                              new HashCodeBuilderMock() );
      fail( "should not get here" );
    } catch( NullPointerException e ) {
      assertEquals( NullPointerException.class, e.getClass() );
    }
    try {
      HashCodeBuilderFactory.registerBuilder( ISomeInterface.class, 
                                              new HashCodeBuilderMock() );
      fail( "should not get here" );
    } catch( Exception e ) {
      assertEquals( IllegalArgumentException.class, e.getClass() );
    }
  }
  
  private static interface ISomeInterface {
    // empty 
  }
  
  private static class HashCodeBuilderMock implements HashCodeBuilder {

    public int compute( final HashCodeBuilderSupport support, 
                        final Object object ) 
    {
      return 0;
    }
    
  }
}
