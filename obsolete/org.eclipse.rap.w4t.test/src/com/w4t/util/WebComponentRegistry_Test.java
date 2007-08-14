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
package com.w4t.util;

import junit.framework.TestCase;

import com.w4t.*;
import com.w4t.engine.W4TModelUtil;
import com.w4t.engine.util.FormManager;
import com.w4t.event.WebFormAdapter;
import com.w4t.event.WebFormEvent;


public class WebComponentRegistry_Test extends TestCase {
  
  protected void setUp() throws Exception {
    W4TFixture.setUp();
    W4TFixture.createContext();
  }
  
  protected void tearDown() throws Exception {
    W4TFixture.tearDown();
    W4TFixture.removeContext();
  }

  public void testRegistry() throws Exception {
    W4TModelUtil.initModel();
    WebForm form = W4TFixture.getEmptyWebFormInstance();
    FormManager.add( form );
    form.add( new WebButton(), WebBorderLayout.NORTH );
    form.add( new WebText(), WebBorderLayout.NORTH );
    form.setClosingTimeout( -2 );
    WebComponentControl.setActive( form, true );
    final WebFormEvent[] evt = new WebFormEvent[ 1 ];
    form.addWebFormListener( new WebFormAdapter() {
      public void webFormClosing( final WebFormEvent e ) {
        evt[ 0 ] = e;
      }
    } );

    final WebComponentRegistry registry = WebComponentRegistry.getInstance();
    WebComponentStatistics statistics = registry.getStatistics();
    assertEquals( 3, statistics.getComponentCountAltogether() );
    String[] componentCountsText = statistics.getComponentCountsText();
    assertEquals( 3, componentCountsText.length );
    StringBuffer buffer = new StringBuffer();
    for( int i = 0; i < componentCountsText.length; i++ ) {
      buffer.append( componentCountsText[ i ] );
      buffer.append( ";" );
    }
    String expected 
      = "1 : com.w4t.W4TFixture$EmptyWebForm;" 
      + "1 : com.w4t.WebButton;" 
      + "1 : com.w4t.WebText;";
    assertEquals( expected, buffer.toString() );
    
    Thread thread = new Thread( new Runnable() {
      public void run() {
        registry.cleanup();
      }
    } );
    thread.setDaemon( true );
    thread.start();
    thread.join();
    
    assertNotNull( evt[ 0 ] );
    assertSame( form, evt[ 0 ].getSource() );
  }
}
