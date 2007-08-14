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
import com.w4t.W4TFixture;
import com.w4t.dhtml.*;
import com.w4t.event.WebActionEvent;


public class CMenu_Test extends TestCase {
  
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
    CMenu menu = new CMenu();
    menu.setConfigResource( "com/w4t/custom/menu.xml" );
    MenuBar menuBar = ( MenuBar )menu.get()[ 0 ];
    Item[] menuBarItems = menuBar.getItem();
    assertEquals( 1, menuBarItems.length );
    Menu file = ( ( Menu ) menuBarItems[ 0 ] );
    MenuItem quit = ( MenuItem )file.getItem( 0 );
    assertEquals( quit.getLabel(), "Quit" );
    WebActionEvent event 
      = new WebActionEvent( quit, WebActionEvent.ACTION_PERFORMED );
    event.processEvent();
    assertEquals( "CALLED FILE_QUIT", FileQuitCommand.log );
  }
}
