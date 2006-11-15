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
import com.w4t.engine.service.ContextProvider;
import com.w4t.event.WebRenderEvent;
import com.w4t.event.WebRenderListener;
import com.w4t.util.browser.Default;


public class WebRenderListener_Test extends TestCase {
  
  protected void setUp() throws Exception {
    Fixture.setUp();
    Fixture.createContext();
  }
  
  protected void tearDown() throws Exception {
    Fixture.tearDown();
    Fixture.removeContext();
  }
  
  public void testEventTriggering() throws Exception {
    WebForm form = Fixture.getEmptyWebFormInstance();
    Fixture.fakeEngineForRender( form );
    Fixture.fakeBrowser( new Default( true ) );
    HtmlResponseWriter writer = new HtmlResponseWriter();
    ContextProvider.getStateInfo().setResponseWriter( writer );
    final String[] log = new String[ 1 ];
    WebButton button = new WebButton();
    form.add( button, WebBorderLayout.NORTH );
    button.addWebRenderListener( new WebRenderListener() {
      public void beforeRender( final WebRenderEvent evt ) {
        log[ 0 ] = "beforeRender|";
      }
      public void afterRender( final WebRenderEvent evt ) {
        log[ 0 ] += "afterRender";
      }
    } );
    Fixture.getLifeCycleAdapter( form ).render();
    assertEquals( "beforeRender|afterRender", log[ 0 ] );
  }
}
