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
package com.w4t.webanchorkit;

import junit.framework.TestCase;

import org.eclipse.rwt.internal.browser.Default;
import org.eclipse.rwt.internal.browser.Ie5up;
import org.eclipse.rwt.internal.lifecycle.HtmlResponseWriter;
import org.eclipse.rwt.internal.service.ContextProvider;

import com.w4t.W4TFixture;
import com.w4t.WebAnchor;
import com.w4t.ajax.AjaxStatus;

public class WebAnchorRenderer_Test extends TestCase {
  
  public void testAjaxRenderer() throws Exception {
    WebAnchor anchor = new WebAnchor();
    anchor.setAnchorName( "myName" );
    anchor.setDir( "myDir" );
    anchor.setEnabled( true );
    anchor.setHRef( "10" );
    anchor.setLang( "myLang" );
    anchor.setName( "myName" );
    anchor.setTarget( "myTarget" );
    anchor.setTitle( "myTitle" );
    HtmlResponseWriter writer = new HtmlResponseWriter();
    AjaxStatus ajaxStatus = ( AjaxStatus )anchor.getAdapter( AjaxStatus.class );
    ajaxStatus.updateStatus( true );
    W4TFixture.fakeBrowser( new Default( true, true ) );
    setResponseWriter( writer );
    W4TFixture.renderComponent( anchor );
    String markup = W4TFixture.getBodyMarkup( writer );
    String expected 
      = "<a id=\"p1\" class=\"w4tCsscd1f6403\" "
      +"dir=\"myDir\" lang=\"myLang\" title=\"myTitle\" "
      +"href=\"10\" target=\"myTarget\">"
      +"<span id=\"p2\" class=\"w4tCsscd1f6403\">linkTo...</span></a>";
    assertEquals( expected, markup );
    // test encoding
    anchor.setTitle( "Tüddelpip & \"Lörz\"" );
    anchor.setLang( "" );
    anchor.setDir( "" );
    ajaxStatus = ( AjaxStatus )anchor.getAdapter( AjaxStatus.class );
    ajaxStatus.updateStatus( true );
    writer = new HtmlResponseWriter();
    W4TFixture.fakeBrowser( new Default( false, false ) );
    setResponseWriter( writer );
    W4TFixture.renderComponent( anchor );
    markup = W4TFixture.getBodyMarkup( writer );
    expected 
      = "<a id=\"p1\" class=\"w4tCsscd1f6403\" "
      + "title=\"T&uuml;ddelpip &amp; &quot;L&ouml;rz&quot;\" "
      + "href=\"10\" target=\"myTarget\">"
      + "<span class=\"w4tCsscd1f6403\">linkTo...</span></a>";
    assertEquals( expected, markup );
  }

  public void testScriptRenderer() throws Exception {
    WebAnchor anchor = new WebAnchor();
    anchor.setAnchorName( "myName" );
    anchor.setDir( "myDir" );
    anchor.setEnabled( true );
    anchor.setHRef( "10" );
    anchor.setLang( "myLang" );
    anchor.setName( "myName" );
    anchor.setTarget( "myTarget" );
    anchor.setTitle( "myTitle" );
    HtmlResponseWriter writer = new HtmlResponseWriter();
    W4TFixture.fakeBrowser( new Ie5up( true, false ) );
    setResponseWriter( writer );
    W4TFixture.renderComponent( anchor );
    String markup = W4TFixture.getBodyMarkup( writer );
    String expected 
    = "<a id=\"p1\" class=\"w4tCsscd1f6403\" "
      +"dir=\"myDir\" lang=\"myLang\" title=\"myTitle\" "
      +"href=\"10\" target=\"myTarget\">"
      +"<span id=\"p2\" class=\"w4tCsscd1f6403\">linkTo...</span></a>";
    assertEquals( expected, markup );
    // test encoding
    anchor.setTitle( "Tüddelpip & \"Lörz\"" );
    anchor.setLang( "" );
    anchor.setDir( "" );
    writer = new HtmlResponseWriter();
    W4TFixture.fakeBrowser( new Default( false, false ) );
    setResponseWriter( writer );
    W4TFixture.renderComponent( anchor );
    markup = W4TFixture.getBodyMarkup( writer );
    expected 
      = "<a id=\"p1\" class=\"w4tCsscd1f6403\" "
      + "title=\"T&uuml;ddelpip &amp; &quot;L&ouml;rz&quot;\" "
      + "href=\"10\" target=\"myTarget\">"
      + "<span class=\"w4tCsscd1f6403\">linkTo...</span></a>";
    assertEquals( expected, markup );
  }

  public void testNo_ScriptRenderer() throws Exception {
    WebAnchor anchor = new WebAnchor();
    anchor.setAnchorName( "myName" );
    anchor.setDir( "myDir" );
    anchor.setEnabled( true );
    anchor.setHRef( "10" );
    anchor.setLang( "myLang" );
    anchor.setName( "myName" );
    anchor.setTarget( "myTarget" );
    anchor.setTitle( "myTitle" );
    HtmlResponseWriter writer = new HtmlResponseWriter();
    W4TFixture.fakeBrowser( new Default( false, false ) );
    setResponseWriter( writer );
    W4TFixture.renderComponent( anchor );
    String markup = W4TFixture.getBodyMarkup( writer );
    String expected 
      = "<a id=\"p1\" class=\"w4tCsscd1f6403\" "
      + "dir=\"myDir\" lang=\"myLang\" title=\"myTitle\" "
      + "href=\"10\" target=\"myTarget\">"
      + "<span class=\"w4tCsscd1f6403\">linkTo...</span></a>";
    assertEquals( expected, markup );
    // test encoding
    anchor.setTitle( "Tüddelpip & \"Lörz\"" );
    anchor.setLang( "" );
    anchor.setDir( "" );
    writer = new HtmlResponseWriter();
    W4TFixture.fakeBrowser( new Default( false, false ) );
    setResponseWriter( writer );
    W4TFixture.renderComponent( anchor );
    markup = W4TFixture.getBodyMarkup( writer );
    expected 
      = "<a id=\"p1\" class=\"w4tCsscd1f6403\" "
      + "title=\"T&uuml;ddelpip &amp; &quot;L&ouml;rz&quot;\" "
      + "href=\"10\" target=\"myTarget\">"
      + "<span class=\"w4tCsscd1f6403\">linkTo...</span></a>";
    assertEquals( expected, markup );
  }
  
  protected void setUp() throws Exception {
    W4TFixture.setUp();
    W4TFixture.createContext();
  }

  protected void tearDown() throws Exception {
    W4TFixture.tearDown();
    W4TFixture.removeContext();
  }

  private void setResponseWriter( final HtmlResponseWriter writer ) {
    ContextProvider.getStateInfo().setResponseWriter( writer );
  }
}
