/*******************************************************************************
 * Copyright (c) 2002, 2010 Innoopract Informationssysteme GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Innoopract Informationssysteme GmbH - initial API and implementation
 *     EclipseSource - ongoing development
 ******************************************************************************/
package org.eclipse.rwt.internal.events;

import junit.framework.TestCase;

import org.eclipse.rwt.Fixture;
import org.eclipse.rwt.lifecycle.PhaseId;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.*;


public class EventAdapter_Test extends TestCase {
  
  protected void setUp() throws Exception {
    Fixture.setUp();
    Fixture.createContext( true );
  }
  
  protected void tearDown() throws Exception {
    Fixture.tearDown();
    Fixture.removeContext();
  }
  
  public void testActionPerformed()  {
    Fixture.fakePhase( PhaseId.PROCESS_ACTION );
    Display display = new Display();
    Shell shell = new Shell( display );
    Button button = new Button( shell, SWT.PUSH );
    IEventAdapter eventAdapter
      = ( IEventAdapter )button.getAdapter( IEventAdapter.class );
    assertNotNull( eventAdapter );
    assertSame( eventAdapter, button.getAdapter( IEventAdapter.class ) );
    assertFalse( eventAdapter.hasListener( SelectionListener.class ) );
    try {
      eventAdapter.hasListener( Object.class );
      fail();
    } catch( final IllegalArgumentException iae ) {
    }
    
    Object[] listener = eventAdapter.getListener( SelectionListener.class );
    assertEquals( 0, listener.length );
    SelectionListener actionListener = new SelectionAdapter() {
    }; 
    eventAdapter.addListener( SelectionListener.class, actionListener );
    assertTrue( eventAdapter.hasListener( SelectionListener.class ) );
    listener = eventAdapter.getListener( SelectionListener.class );
    assertEquals( 1, listener.length );
    assertSame( actionListener, listener[ 0 ] );
    eventAdapter.removeListener( SelectionListener.class, actionListener );
    assertFalse( eventAdapter.hasListener( SelectionListener.class ) );
  }
  
  public void testAddListenerWithIllegalArguments() {
    Fixture.fakePhase( PhaseId.PROCESS_ACTION );
    Display display = new Display();
    Widget widget = new Shell( display );
    IEventAdapter eventAdapter
      = ( IEventAdapter )widget.getAdapter( IEventAdapter.class );
    try {
      eventAdapter.addListener( SelectionListener.class, new Object() );
      fail();
    } catch( final IllegalArgumentException iae ) {
    }
    try {
      eventAdapter.addListener( SelectionListener.class, null );
      fail();
    } catch( final IllegalArgumentException iae ) {
    }
    try {
      SelectionListener validListener = new SelectionAdapter() {
      }; 
      eventAdapter.addListener( null, validListener );
      fail();
    } catch( final IllegalArgumentException iae ) {
    }
    Object[] listeners = eventAdapter.getListener( SelectionListener.class );
    assertEquals( 0, listeners.length );
    
  }
}
