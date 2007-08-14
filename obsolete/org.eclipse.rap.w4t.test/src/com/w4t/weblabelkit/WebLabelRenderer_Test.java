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
package com.w4t.weblabelkit;

import junit.framework.TestCase;

import org.eclipse.rwt.internal.browser.Default;
import org.eclipse.rwt.internal.lifecycle.HtmlResponseWriter;

import com.w4t.*;
import com.w4t.ajax.AjaxStatus;


/** <p>Unit tests for WebLabelRenderer.</p> */
public class WebLabelRenderer_Test extends TestCase {
  
  public void testAjaxRenderer() throws Exception {
    WebLabel label = new WebLabel();
    label.setValue( "Hello World" );
    W4TFixture.setWebComponentUniqueId( label, "p1" );
    AjaxStatus ajaxStatus = ( AjaxStatus )label.getAdapter( AjaxStatus.class );
    ajaxStatus.updateStatus( true );
    W4TFixture.fakeBrowser( new Default( true, true ) );
    HtmlResponseWriter writer = new HtmlResponseWriter();
    W4TFixture.setResponseWriter( writer );
    W4TFixture.renderComponent( label );
    String markup = W4TFixture.getBodyMarkup( writer );
    String expected 
      = "<span id=\"p1\" class=\"w4tCsscd1f6403\">Hello World</span>";
    assertEquals( expected, markup );
    // add more arguments
    writer = new HtmlResponseWriter();
    W4TFixture.setResponseWriter( writer );
    label.setValue( "Hello World" );
    W4TFixture.renderComponent( label );
    markup = W4TFixture.getBodyMarkup( writer ); 
    expected = "<span id=\"p1\" class=\"w4tCsscd1f6403\">Hello World</span>";
    assertEquals( expected, markup );
    label.setEnabled( false );
    writer = new HtmlResponseWriter();
    W4TFixture.setResponseWriter( writer );
    label.setValue( "Hello World" );
    W4TFixture.renderComponent( label );
    markup = W4TFixture.getBodyMarkup( writer );
    expected = "<span id=\"p1\" class=\"w4tCsscd1f6403\">Hello World</span>";
    assertEquals( expected, markup );
  }
  
  public void testScriptRenderer() throws Exception {
    WebLabel label = new WebLabel();
    label.setValue( "Hello World" );
    HtmlResponseWriter writer = new HtmlResponseWriter();
    W4TFixture.fakeBrowser( new Default( true, false ) );
    W4TFixture.setWebComponentUniqueId( label, "p1" );
    W4TFixture.setResponseWriter( writer );
    W4TFixture.renderComponent( label );
    String markup = W4TFixture.getBodyMarkup( writer );
    String expected 
      = "<span id=\"p1\" class=\"w4tCsscd1f6403\">Hello World</span>";
    assertEquals( expected, markup );
  }
  
  public void testNoScriptRenderer() throws Exception {
    WebLabel label = new WebLabel();
    label.setValue( "Hello World" );
    HtmlResponseWriter writer = new HtmlResponseWriter();
    W4TFixture.fakeBrowser( new Default( false, false ) );
    W4TFixture.setResponseWriter( writer );
    W4TFixture.renderComponent( label );
    String markup = W4TFixture.getBodyMarkup( writer );
    assertEquals( "<span class=\"w4tCsscd1f6403\">Hello World</span>", 
                  markup );
  }
  
  public void testEncoding() throws Exception {
    WebLabel label = new WebLabel();
    label.setValue( "MÜä\"öüß" );
    label.setTitle( "MÜä\\\"öüß" );
    Style style = new Style();
    style.setFontFamily( "" );
    style.setFontSize( -1 );
    label.setStyle( style );
    HtmlResponseWriter writer = new HtmlResponseWriter();
    W4TFixture.fakeBrowser( new Default( false, false ) );
    W4TFixture.setResponseWriter( writer );
    W4TFixture.renderComponent( label );
    String markup = W4TFixture.getBodyMarkup( writer );
    String expected 
      = "<span title=\"M&Uuml;&auml;\\&quot;&ouml;&uuml;&szlig;\">" 
      + "M&Uuml;&auml;&quot;&ouml;&uuml;&szlig;</span>"; 
    assertEquals( expected, markup );
    writer = new HtmlResponseWriter();
    W4TFixture.fakeBrowser( new Default( true, false ) );
    W4TFixture.setResponseWriter( writer );
    W4TFixture.renderComponent( label );
    markup = W4TFixture.getBodyMarkup( writer );
    expected 
      = "<span id=\"p1\" title=\"M&Uuml;&auml;\\&quot;&ouml;&uuml;&szlig;\">"
      + "M&Uuml;&auml;&quot;&ouml;&uuml;&szlig;</span>";
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
}
