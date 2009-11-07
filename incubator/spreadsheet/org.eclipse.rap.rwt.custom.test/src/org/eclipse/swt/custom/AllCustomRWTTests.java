// Created on 07.08.2009
package org.eclipse.swt.custom;

import junit.framework.Test;
import junit.framework.TestSuite;


public class AllCustomRWTTests {

  public static Test suite() {
    TestSuite suite = new TestSuite( "Test for org.eclipse.swt.custom" );
    //$JUnit-BEGIN$
    suite.addTestSuite( SpreadSheetLayout_Test.class );
    suite.addTestSuite( SpreadSheet_Test.class );
    //$JUnit-END$
    return suite;
  }
}
