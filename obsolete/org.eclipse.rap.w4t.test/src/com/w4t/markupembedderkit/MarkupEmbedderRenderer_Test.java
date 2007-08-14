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
package com.w4t.markupembedderkit;

import org.eclipse.rwt.internal.browser.*;
import org.eclipse.rwt.internal.service.ContextProvider;
import org.eclipse.rwt.internal.service.IServiceStateInfo;

import junit.framework.TestCase;

import com.w4t.*;
import com.w4t.ajax.AjaxStatus;
import com.w4t.types.WebColor;


public class MarkupEmbedderRenderer_Test extends TestCase {
  
  protected void setUp() throws Exception {
    W4TFixture.setUp();
    W4TFixture.createContext();
  }

  protected void tearDown() throws Exception {
    W4TFixture.tearDown();
    W4TFixture.removeContext();
  }

  public void testSetContent() {
    MarkupEmbedder markupEmbedder = new MarkupEmbedder();
    markupEmbedder.setContent( null );
    assertEquals( "", markupEmbedder.getContent() );
  }
  
  public void testStyle() {
    MarkupEmbedder markupEmbedder = new MarkupEmbedder();
    // Style property of newly created MarkupEmbedder *must* be empty
    assertEquals( "", markupEmbedder.getStyle().toString() );
  }

  public void testNoscript() throws Exception {
    // with default browser(noScript)
    W4TFixture.fakeBrowser( new Default( false, false ) );
    MarkupEmbedder passthrough = new MarkupEmbedder();
    W4TFixture.fakeResponseWriter();
    W4TFixture.renderComponent( passthrough );
    assertEquals( "<div id=\"p1\"></div>", getMarkup() );
    passthrough.setContent( "my markup дцья" );
    W4TFixture.fakeResponseWriter();
    W4TFixture.renderComponent( passthrough );
    assertEquals( "<div id=\"p1\">my markup дцья</div>", getMarkup() );
    // with Ie5(noScript)
    W4TFixture.fakeBrowser( new Ie5( false, false ) );
    passthrough = new MarkupEmbedder();
    W4TFixture.fakeResponseWriter();
    W4TFixture.renderComponent( passthrough );
    assertEquals( "<div id=\"p2\"></div>", getMarkup() );
    passthrough.setContent( "my markup дцья" );
    W4TFixture.fakeResponseWriter();
    W4TFixture.renderComponent( passthrough );
    assertEquals( "<div id=\"p2\">my markup дцья</div>", getMarkup() );
  }
  
  public void testScript() throws Exception {
    // with default browser(noScript)
    W4TFixture.fakeBrowser( new Default( true, false ) );
    MarkupEmbedder markupEmbedder = new MarkupEmbedder();
    W4TFixture.fakeResponseWriter();
    W4TFixture.renderComponent( markupEmbedder );
    assertEquals( "<div id=\"p1\"></div>", getMarkup() );
    markupEmbedder.setContent( "my markup дцья" );
    W4TFixture.fakeResponseWriter();
    W4TFixture.renderComponent( markupEmbedder );
    assertEquals( "<div id=\"p1\">my markup дцья</div>", getMarkup() );
    // with Ie5(noScript)
    W4TFixture.fakeBrowser( new Ie5( true, false ) );
    markupEmbedder = new MarkupEmbedder();
    W4TFixture.fakeResponseWriter();
    W4TFixture.renderComponent( markupEmbedder );
    assertEquals( "<div id=\"p2\"></div>", getMarkup() );
    markupEmbedder.setContent( "my markup дцья" );
    W4TFixture.fakeResponseWriter();
    W4TFixture.renderComponent( markupEmbedder );
    assertEquals( "<div id=\"p2\">my markup дцья</div>", getMarkup() );
  }
  
  public void testAjax() throws Exception {
    W4TFixture.fakeBrowser( new Ie6( true, true ) );
    MarkupEmbedder markupEmbedder1 = new MarkupEmbedder();
    W4TFixture.fakeResponseWriter();
    W4TFixture.renderComponent( markupEmbedder1 );
    assertEquals( "", getMarkup() );
    markupEmbedder1.setContent( "my markup дцья" );
    W4TFixture.fakeResponseWriter();
    W4TFixture.renderComponent( markupEmbedder1 );
    assertEquals( "", getMarkup() );
    setMustRender( markupEmbedder1 );
    markupEmbedder1.setContent( "my markup дцья" );
    W4TFixture.fakeResponseWriter();
    W4TFixture.renderComponent( markupEmbedder1 );
    assertEquals( "<div id=\"p1\">my markup дцья</div>", getMarkup() );
    MarkupEmbedder markupEmbedder2 = new MarkupEmbedder();
    W4TFixture.fakeResponseWriter();
    W4TFixture.renderComponent( markupEmbedder2 );
    assertEquals( "", getMarkup() );
    markupEmbedder2.setContent( "my markup дцья" );
    setMustRender( markupEmbedder2 );
    W4TFixture.fakeResponseWriter();
    W4TFixture.renderComponent( markupEmbedder2 );
    assertEquals( "<div id=\"p2\">my markup дцья</div>", getMarkup() );
  }
  
  public void testWithStyle() throws Exception {
    W4TFixture.fakeBrowser( new Default( true, false ) );
    MarkupEmbedder markupEmbedder = new MarkupEmbedder();
    markupEmbedder.getStyle().setBgColor( new WebColor( "red" ) );
    markupEmbedder.setContent( "abc" );
    W4TFixture.fakeResponseWriter();
    W4TFixture.renderComponent( markupEmbedder );
    assertEquals( "<div id=\"p1\" class=\"w4tCsse5abb2ae\">abc</div>", 
                  getMarkup() );
  }
  
  private static void setMustRender( final WebComponent component ) {
    AjaxStatus ajaxStatus;
    ajaxStatus = ( AjaxStatus )component.getAdapter( AjaxStatus.class );
    ajaxStatus.setMustRender( true );
  }
  
  private static String getMarkup() {
    IServiceStateInfo stateInfo = ContextProvider.getStateInfo();
    return W4TFixture.getAllMarkup( stateInfo.getResponseWriter() );
  }
}
