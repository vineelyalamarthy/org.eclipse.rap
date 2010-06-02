/******************************************************************************* 
* Copyright (c) 2010 EclipseSource and others. All rights reserved. This
* program and the accompanying materials are made available under the terms of
* the Eclipse Public License v1.0 which accompanies this distribution, and is
* available at http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*   EclipseSource - initial API and implementation
*******************************************************************************/ 
package org.eclipse.rap.rwt;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.rap.rwt.protocol.ChunkDistributor_Test;
import org.eclipse.rap.rwt.protocol.JsonMessageWriter_Test;
import org.eclipse.rap.rwt.protocol.Processor_Test;
import org.eclipse.rap.rwt.protocol.ProtocolMessageWriter_Test;
import org.eclipse.rap.rwt.protocol.WidgetSynchronizer_Test;


public class ProtocolTestSuite {
  
  public static Test suite() {
    TestSuite suite = new TestSuite( "RWT protocol tests" );
    suite.addTestSuite( ProtocolMessageWriter_Test.class );
    suite.addTestSuite( JsonMessageWriter_Test.class );
    suite.addTestSuite( WidgetSynchronizer_Test.class );
    suite.addTestSuite( Processor_Test.class );
    suite.addTestSuite( ChunkDistributor_Test.class );
    return suite;    
  }
  
}
