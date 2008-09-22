// Created on 08.09.2007
package org.eclipse.rap.rms.data;

import junit.framework.Test;
import junit.framework.TestSuite;


public class RMSDataTests {

  public static Test suite() {
    TestSuite suite = new TestSuite( "Test for org.eclipse.rap.rms.data" );
    //$JUnit-BEGIN$
    suite.addTestSuite( DataModel_Test.class );
    suite.addTestSuite( DataStorage_Test.class );
    //$JUnit-END$
    return suite;
  }
}
