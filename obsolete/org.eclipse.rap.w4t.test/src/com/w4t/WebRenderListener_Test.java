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
import org.eclipse.rwt.internal.lifecycle.HtmlResponseWriter;
import org.eclipse.rwt.internal.service.ContextProvider;

import junit.framework.TestCase;
import com.w4t.event.WebRenderEvent;
import com.w4t.event.WebRenderListener;


public class WebRenderListener_Test extends TestCase {
  
  protected void setUp() throws Exception {
    W4TFixture.setUp();
    W4TFixture.createContext();
  }
  
  protected void tearDown() throws Exception {
    W4TFixture.tearDown();
    W4TFixture.removeContext();
  }
  
  public void testEventTriggering() throws Exception {
    WebForm form = W4TFixture.getEmptyWebFormInstance();
    W4TFixture.fakeEngineForRender( form );
    W4TFixture.fakeBrowser( new Default( true ) );
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
    W4TFixture.getLifeCycleAdapter( form ).render();
    assertEquals( "beforeRender|afterRender", log[ 0 ] );
  }
}
