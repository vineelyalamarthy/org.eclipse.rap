/*******************************************************************************
 * Copyright (c) 2010 EclipseSource.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     EclipseSource - initial API and implementation
 ******************************************************************************/

package org.eclipse.rap.ui.themeeditor;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.rap.ui.themeeditor.editor.Scanner_Test;
import org.eclipse.rap.ui.themeeditor.editor.TokenProvider_Test;


public class AllTests {

  public static Test suite() {
    TestSuite suite = new TestSuite( AllTests.class.getName() );
    //$JUnit-BEGIN$
    suite.addTestSuite( TokenProvider_Test.class );
    suite.addTestSuite( Scanner_Test.class );
    //$JUnit-END$
    return suite;
  }
}
