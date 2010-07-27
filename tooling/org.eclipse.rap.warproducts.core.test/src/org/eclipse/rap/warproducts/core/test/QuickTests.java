/******************************************************************************* 
* Copyright (c) 2010 EclipseSource and others. All rights reserved. This
* program and the accompanying materials are made available under the terms of
* the Eclipse Public License v1.0 which accompanies this distribution, and is
* available at http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*   EclipseSource - initial API and implementation
*******************************************************************************/ 
package org.eclipse.rap.warproducts.core.test;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.rap.warproducts.core.test.tests.InfrastructureCreatorTest;
import org.eclipse.rap.warproducts.core.test.tests.WARProductModelTest;
import org.eclipse.rap.warproducts.core.test.tests.WARProductTest;


public class QuickTests {
  
  public static Test suite() {
    TestSuite suite = new TestSuite( "Quick WAR product tests" );
    
    suite.addTestSuite( InfrastructureCreatorTest.class );
    suite.addTestSuite( WARProductTest.class );
    suite.addTestSuite( WARProductModelTest.class );
   
    
    return suite;
  }
  
}
