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

import junit.framework.TestCase;
import com.w4t.engine.util.FormManager;
import com.w4t.event.WebFormEvent;
import com.w4t.event.WebFormListener;
import com.w4t.internal.adaptable.IFormAdapter;


public class WebFormListener_Test extends TestCase {
  
  private static WebFormEvent[] evts = new WebFormEvent[ 2 ];
  
  private final static class FormListener implements WebFormListener {
    public void webFormClosing( final WebFormEvent evt ) {
      evts[ 0 ] = evt;
    }
    public void afterInit( final WebFormEvent evt ) {
      evts[ 1 ] = evt;
    }
  }

  public static class Form extends WebForm {
    protected void setWebComponents() throws Exception {
      addWebFormListener( new FormListener() );
    }    
  }
  
  protected void setUp() throws Exception {
    W4TFixture.setUp();
    W4TFixture.createContext();
  }
  
  protected void tearDown() throws Exception {
    W4TFixture.tearDown();
    W4TFixture.removeContext();
  }
  
  public void testFormListenerInvocation() throws Exception {
    String name = Form.class.getName();
    WebForm form = FormManager.load( name );
    W4TFixture.fakeEngineForRender( form );
    IFormAdapter adapter 
      = ( IFormAdapter )form.getAdapter( IFormAdapter.class );
    adapter.setActive( true );
    form.unload();

    assertNotNull( evts[ 0 ] );
    assertEquals( WebFormEvent.WEBFORM_CLOSING, evts[ 0 ].getID() );
    assertSame( form, evts[ 0 ].getSource() );
    
    assertNotNull( evts[ 1 ] );
    assertEquals( WebFormEvent.AFTER_INIT, evts[ 1 ].getID() );
    assertSame( form, evts[ 1 ].getSource() );
  }
}
