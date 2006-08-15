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
package com.w4t.webpanelkit;

import junit.framework.TestCase;
import com.w4t.*;
import com.w4t.ajax.AjaxStatus;
import com.w4t.engine.service.ContextProvider;
import com.w4t.util.browser.Mozilla1_7;


/** <p>Unit tests for WebPanelRenderer.</p> */
public class WebPanelRenderer_Test extends TestCase {
  
  public void testAjaxRenderer() throws Exception {
    Fixture.fakeBrowser( new Mozilla1_7( true, true ) );
    WebPanel panel = new WebPanel();
    Fixture.setWebComponentUniqueId( panel, "a" );
    AjaxStatus ajaxStatus = ( AjaxStatus )panel.getAdapter( AjaxStatus.class );
    ajaxStatus.updateStatus( true );
    HtmlResponseWriter writer = new HtmlResponseWriter();
    setResponseWriter( writer );
    Fixture.renderComponent( panel );
    String markup = getBodyMarkup( writer );
    assertEquals( "<div id=\"a\"></div>", markup );
  }
  
  private void setResponseWriter( final HtmlResponseWriter writer ) {
    ContextProvider.getStateInfo().setResponseWriter( writer );
  }
  
  private String getBodyMarkup( final HtmlResponseWriter writer ) {
    StringBuffer buffer = new StringBuffer();
    for( int i = 0; i < writer.getBodySize(); i++ ) {
      buffer.append( writer.getBodyToken( i ) );
    }
    return buffer.toString();
  }
  
  protected void setUp() throws Exception {
    Fixture.setUp();
    Fixture.createContext();
  }
  
  protected void tearDown() throws Exception {
    Fixture.removeContext();
    Fixture.tearDown();
  }
  
}
