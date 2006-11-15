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
package com.w4t.engine.lifecycle.standard;

import junit.framework.TestCase;
import com.w4t.Fixture;
import com.w4t.WebButton;
import com.w4t.event.*;


public class EventAdapter_Test extends TestCase {
  
  protected void setUp() throws Exception {
    Fixture.setUp();
    Fixture.createContext();
  }
  
  protected void tearDown() throws Exception {
    Fixture.tearDown();
    Fixture.removeContext();
  }
  
  public void testActionPerformed() throws Exception  {
    WebButton button = new WebButton();
    IEventAdapter eventAdapter
      = ( IEventAdapter )button.getAdapter( IEventAdapter.class );
    assertNotNull( eventAdapter );
    assertSame( eventAdapter, button.getAdapter( IEventAdapter.class ) );
    assertFalse( eventAdapter.hasListener( WebActionListener.class ) );
    try {
      eventAdapter.hasListener( Object.class );
      fail();
    } catch( final IllegalArgumentException iae ) {
    }
    
    Object[] listener = eventAdapter.getListener( WebActionListener.class );
    assertEquals( 0, listener.length );
    WebActionListener actionListener = new WebActionListener() {
      public void webActionPerformed( final WebActionEvent evt ) {
        // nothing to do here...
      }
    };
    eventAdapter.addListener( WebActionListener.class, actionListener );
    assertTrue( eventAdapter.hasListener( WebActionListener.class ) );
    listener = eventAdapter.getListener( WebActionListener.class );
    assertEquals( 1, listener.length );
    assertSame( actionListener, listener[ 0 ] );
    eventAdapter.removeListener( WebActionListener.class, actionListener );
    assertFalse( eventAdapter.hasListener( WebActionListener.class ) );
    try {
      eventAdapter.addListener( WebActionListener.class, new Object() );
      fail();
    } catch( final IllegalArgumentException iae ) {
    }
    listener = eventAdapter.getListener( WebActionListener.class );
    assertEquals( 0, listener.length );
  }
}
