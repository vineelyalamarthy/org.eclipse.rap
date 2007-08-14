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
package com.w4t;

import org.eclipse.rwt.internal.browser.Default;
import org.eclipse.rwt.internal.service.ContextProvider;
import org.eclipse.rwt.internal.service.IServiceStateInfo;

import junit.framework.TestCase;
import com.w4t.engine.lifecycle.standard.*;
import com.w4t.event.WebDataEvent;
import com.w4t.event.WebDataListener;
import com.w4t.webcheckboxkit.WebCheckBoxRenderer;


public class WebDataEvent_Test extends TestCase {
  
  private static final String CHECKED = "-1";
  private static final String UNCHECKED = "0";
  private static final String OLD_VALUE = "mark1";
  private static final String NEW_VALUE = "mark2";

  protected void setUp() throws Exception {
    W4TFixture.setUp();
    W4TFixture.createContext();
  }
  
  protected void tearDown() throws Exception {
    W4TFixture.tearDown();
    W4TFixture.removeContext();
  }
  
  public void testDataEventTriggering() throws Exception {
    WebForm form = W4TFixture.getEmptyWebFormInstance();
    W4TFixture.fakeBrowser( new Default( true ) );
    final WebDataEvent[] dataEvent = new WebDataEvent[ 1 ];
    WebDataListener dataListener = new WebDataListener() {
      public void webValueChanged( final WebDataEvent evt ) {
        dataEvent[ 0 ] = evt;
      } 
    };
    WebText text = new WebText();
    form.add( text, WebBorderLayout.NORTH );
    
    text.setValue( OLD_VALUE );
    assertEquals( OLD_VALUE, text.getValue() );
    text.addWebDataListener( dataListener );
    text.setValue( NEW_VALUE );
    assertNull( dataEvent[ 0 ] );

    text.removeWebDataListener( dataListener );
    text.setValue( OLD_VALUE );
    dataEvent[ 0 ] = null;
    text.addWebDataListener( dataListener );
    W4TFixture.fakeRequestParam( text.getUniqueID(), NEW_VALUE );
    getLifeCycleAdapter( form ).readData();
    IServiceStateInfo stateInfo = ContextProvider.getStateInfo();
    String key = EventQueueFilter.ATTRIBUTE_KEY;
    EventQueueFilter eqv = ( EventQueueFilter )stateInfo.getAttribute( key );
    eqv.filter();
    EventQueue.getEventQueue().fireEvents();

    assertNotNull( dataEvent[ 0 ] );
    assertSame( text, dataEvent[ 0 ].getSource() );
    assertSame( OLD_VALUE, dataEvent[ 0 ].getOldValue() );
    assertSame( NEW_VALUE, dataEvent[ 0 ].getNewValue() );
    assertEquals( NEW_VALUE, text.getValue() );
  }
  
  public void testDataEventCheckBox() throws Exception {
    WebForm form = W4TFixture.getEmptyWebFormInstance();
    W4TFixture.fakeBrowser( new Default( true ) );
    final WebDataEvent[] dataEvent = new WebDataEvent[ 1 ];
    WebDataListener dataListener = new WebDataListener() {
      public void webValueChanged( final WebDataEvent evt ) {
        dataEvent[ 0 ] = evt;
      } 
    };
    WebCheckBox checkBox = new WebCheckBox();
    form.add( checkBox, WebBorderLayout.NORTH );
    
    assertEquals( UNCHECKED, checkBox.getValue() );
    checkBox.addWebDataListener( dataListener );
    
    checkBox.setValue( CHECKED );
    assertNull( dataEvent[ 0 ] );

    checkBox.removeWebDataListener( dataListener );
    checkBox.setValue( UNCHECKED );
    dataEvent[ 0 ] = null;
    checkBox.addWebDataListener( dataListener );
    String id = checkBox.getUniqueID();
    W4TFixture.fakeRequestParam( id, CHECKED );
    W4TFixture.fakeRequestParam( WebCheckBoxRenderer.PREFIX + id, UNCHECKED );
    getLifeCycleAdapter( form ).readData();
    IServiceStateInfo stateInfo = ContextProvider.getStateInfo();
    String key = EventQueueFilter.ATTRIBUTE_KEY;
    EventQueueFilter eqv = ( EventQueueFilter )stateInfo.getAttribute( key );
    eqv.filter();
    EventQueue.getEventQueue().fireEvents();
    assertNotNull( dataEvent[ 0 ] );
    assertSame( checkBox, dataEvent[ 0 ].getSource() );
    assertSame( UNCHECKED, dataEvent[ 0 ].getOldValue() );
    assertSame( CHECKED, dataEvent[ 0 ].getNewValue() );
    assertEquals( CHECKED, checkBox.getValue() );

    dataEvent[ 0 ] = null;
    W4TFixture.fakeRequestParam( checkBox.getUniqueID(), null );
    W4TFixture.fakeRequestParam( WebCheckBoxRenderer.PREFIX + id, CHECKED );
    getLifeCycleAdapter( form ).readData();
    eqv.filter();
    EventQueue.getEventQueue().fireEvents();
    assertNotNull( dataEvent[ 0 ] );
    assertSame( checkBox, dataEvent[ 0 ].getSource() );
    assertSame( CHECKED, dataEvent[ 0 ].getOldValue() );
    assertSame( UNCHECKED, dataEvent[ 0 ].getNewValue() );
    assertEquals( UNCHECKED, checkBox.getValue() );
    
    W4TFixture.fakeRequestParam( WebCheckBoxRenderer.PREFIX + id, UNCHECKED );
    dataEvent[ 0 ] = null;
    getLifeCycleAdapter( form ).readData();
    eqv.filter();
    EventQueue.getEventQueue().fireEvents();
    assertNull( dataEvent[ 0 ] );
    assertEquals( UNCHECKED, checkBox.getValue() );
    
  }
  
  private ILifeCycleAdapter getLifeCycleAdapter( final WebForm form ) {
    return ( ILifeCycleAdapter )form.getAdapter( ILifeCycleAdapter.class );
  }

}
