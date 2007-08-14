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
package com.w4t.dhtml;

import junit.framework.TestCase;
import com.w4t.W4TFixture;

/** <p>Unit tests for {@link org.eclipse.rwt.custom.CMenu CMenu}.</p>
  */
public class Menu_Test extends TestCase {
  
  public Menu_Test( String name ) {
    super( name );
  }
  
  protected void setUp() throws Exception {
    W4TFixture.setUp();
    W4TFixture.createContext();
  }
  
  protected void tearDown() throws Exception {
    W4TFixture.tearDown();
    W4TFixture.removeContext();
  }
  
  // actual test code
  ///////////////////

  public void testEnabled() throws Exception {
    Menu mnu = new Menu( "Test" );
    
    // test default
    assertTrue( "menu must be enabled by default", mnu.isEnabled() );

    // test setter and getter
    mnu.setEnabled( true );
    assertTrue( "menu has been enabled", mnu.isEnabled() );
    mnu.setEnabled( false );
    assertTrue( "menu has been disabled", !mnu.isEnabled() );
    mnu.setEnabled( true );
    assertTrue( "menu has been enabled", mnu.isEnabled() );
    
    // test enabling and disableing of parent
    MenuBar mb = new MenuBar();
    mb.addItem( mnu );
    assertTrue( "menu has just been added", mnu.isEnabled() );
    mb.setEnabled( false );
    assertTrue( "menubar has been disabled", !mnu.isEnabled() );
    mb.setEnabled( true );
    assertTrue( "menubar has been anabled", mnu.isEnabled() );
  }
}
