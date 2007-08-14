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
package com.w4t.custom;

import junit.framework.TestCase;

import com.w4t.*;
import com.w4t.event.WebActionEvent;


public class CToolbar_Test extends TestCase {
  
  protected void setUp() throws Exception {
    W4TFixture.setUp();
    W4TFixture.createContext( false );
  }
  
  protected void tearDown() throws Exception {
    W4TFixture.tearDown();
    W4TFixture.removeContext();
    W4TFixture.clearSingletons();
  }
  
  public void testActionInvokation() throws Exception {
    FileQuitCommand.log = "";
    CToolBar toolbar = new CToolBar();
    toolbar.setConfigResource( "com/w4t/custom/toolbar.xml" );
    WebBorderComponent border = ( WebBorderComponent )toolbar.get()[ 0 ];
    assertEquals( 1, toolbar.get().length );
    WebButton quit = ( WebButton )border.getContent();
    assertEquals( quit.getTitle(), "Quit" );
    WebActionEvent event 
      = new WebActionEvent( quit, WebActionEvent.ACTION_PERFORMED );
    event.processEvent();
    assertEquals( "CALLED FILE_QUIT", FileQuitCommand.log );
  }
}
