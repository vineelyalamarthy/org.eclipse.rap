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
package com.w4t.webflowlayoutkit;

import junit.framework.TestCase;
import com.w4t.*;
import com.w4t.ajax.AjaxStatus;
import com.w4t.engine.service.ContextProvider;
import com.w4t.util.browser.Default;
import com.w4t.util.browser.Ie5up;

/**
 * <p>
 * Tests the rendering of com.w4t.WebFlowLayout.
 * </p>
 */
public class WebFlowLayout_Test extends TestCase {
  
  public void test_Ajax() throws Exception {
    WebPanel panel = new WebPanel();
    WebFlowLayout layout = new WebFlowLayout();
    panel.setWebLayout( layout );
    WebLabel lbl1 = new WebLabel( "lbl1" );
    WebLabel lbl2 = new WebLabel( "lbl2" );
    WebLabel lbl3 = new WebLabel( "lbl3" );
    WebLabel lbl4 = new WebLabel( "lbl4" );
    WebLabel lbl5 = new WebLabel( "lbl5" );
    panel.add( lbl1 );
    panel.add( lbl2 );
    panel.add( lbl3 );
    panel.add( lbl4 );
    panel.add( lbl5 );
    layout.setAlign( "10" );
    layout.setBorder( "1" );
    layout.setCellpadding( "10" );
    layout.setCellspacing( "5" );
    layout.setHeight( "15" );
    layout.setWidth( "7" );
    Fixture.fakeBrowser( new Default ( true, true ) );
    AjaxStatus ajaxStatus = ( AjaxStatus )panel.getAdapter( AjaxStatus.class );
    ajaxStatus.updateStatus( true );
    HtmlResponseWriter writer = new HtmlResponseWriter();
    setResponseWriter( writer );
    Fixture.renderComponent( panel );
    String markup = Fixture.getBodyMarkup( writer );
    String expected 
      = "<table width=\"7\" height=\"15\" cellspacing=\"5\" "
      + "cellpadding=\"10\" border=\"1\" align=\"10\" id=\"p1\">"
      + "<tr><td>"
      + "<span id=\"p2\" class=\"w4tCsscd1f6403\">lbl1</span>"
      + "<span id=\"p3\" class=\"w4tCsscd1f6403\">lbl2</span>"
      + "<span id=\"p4\" class=\"w4tCsscd1f6403\">lbl3</span>"
      + "<span id=\"p5\" class=\"w4tCsscd1f6403\">lbl4</span>"
      + "<span id=\"p6\" class=\"w4tCsscd1f6403\">lbl5</span>"
      + "</td></tr></table>";
    assertEquals( expected, markup );
  }

  public void test_Script() throws Exception {
    WebPanel panel = new WebPanel();
    WebFlowLayout layout = new WebFlowLayout();
    panel.setWebLayout( layout );
    WebLabel lbl1 = new WebLabel( "lbl1" );
    WebLabel lbl2 = new WebLabel( "lbl2" );
    WebLabel lbl3 = new WebLabel( "lbl3" );
    WebLabel lbl4 = new WebLabel( "lbl4" );
    WebLabel lbl5 = new WebLabel( "lbl5" );
    panel.add( lbl1 );
    panel.add( lbl2 );
    panel.add( lbl3 );
    panel.add( lbl4 );
    panel.add( lbl5 );
    panel.setDesignTime( false );
    layout.setAlign( "10" );
    layout.setBorder( "1" );
    layout.setCellpadding( "10" );
    layout.setCellspacing( "5" );
    layout.setHeight( "15" );
    layout.setWidth( "7" );
    Fixture.fakeBrowser( new Ie5up ( true, false ) );
    HtmlResponseWriter writer = new HtmlResponseWriter();
    setResponseWriter( writer );
    Fixture.renderComponent( panel );
    String markup = Fixture.getBodyMarkup( writer );
    String expected 
    = "<table width=\"7\" height=\"15\" cellspacing=\"5\" "
      + "cellpadding=\"10\" border=\"1\" align=\"10\" id=\"p1\">"
      + "<tr><td>"
      + "<span id=\"p2\" class=\"w4tCsscd1f6403\">lbl1</span>"
      + "<span id=\"p3\" class=\"w4tCsscd1f6403\">lbl2</span>"
      + "<span id=\"p4\" class=\"w4tCsscd1f6403\">lbl3</span>"
      + "<span id=\"p5\" class=\"w4tCsscd1f6403\">lbl4</span>"
      + "<span id=\"p6\" class=\"w4tCsscd1f6403\">lbl5</span>"
      + "</td></tr></table>";
    assertEquals( expected, markup );
  }

  public void test_No_Script() throws Exception {
    WebPanel panel = new WebPanel();
    WebFlowLayout layout = new WebFlowLayout();
    panel.setWebLayout( layout );
    WebLabel lbl1 = new WebLabel( "lbl1" );
    WebLabel lbl2 = new WebLabel( "lbl2" );
    WebLabel lbl3 = new WebLabel( "lbl3" );
    WebLabel lbl4 = new WebLabel( "lbl4" );
    WebLabel lbl5 = new WebLabel( "lbl5" );
    panel.add( lbl1 );
    panel.add( lbl2 );
    panel.add( lbl3 );
    panel.add( lbl4 );
    panel.add( lbl5 );
    panel.setDesignTime( false );
    layout.setAlign( "10" );
    layout.setBorder( "1" );
    layout.setCellpadding( "10" );
    layout.setCellspacing( "5" );
    layout.setHeight( "15" );
    layout.setWidth( "7" );
    Fixture.fakeBrowser( new Default ( false, false ) );
    HtmlResponseWriter writer = new HtmlResponseWriter();
    setResponseWriter( writer );
    Fixture.renderComponent( panel );
    String markup = Fixture.getBodyMarkup( writer );
    String expected 
    = "<table width=\"7\" height=\"15\" cellspacing=\"5\" "
      + "cellpadding=\"10\" border=\"1\" align=\"10\" id=\"p1\">"
      + "<tr><td>"
      + "<span class=\"w4tCsscd1f6403\">lbl1</span>"
      + "<span class=\"w4tCsscd1f6403\">lbl2</span>"
      + "<span class=\"w4tCsscd1f6403\">lbl3</span>"
      + "<span class=\"w4tCsscd1f6403\">lbl4</span>"
      + "<span class=\"w4tCsscd1f6403\">lbl5</span>"
      + "</td></tr></table>";
    assertEquals( expected, markup );
  }
  
  protected void setUp() throws Exception {
    Fixture.setUp();
    Fixture.createContext();
  }

  protected void tearDown() throws Exception {
    Fixture.removeContext();
    Fixture.tearDown();
  }

  private void setResponseWriter( final HtmlResponseWriter writer ) {
    ContextProvider.getStateInfo().setResponseWriter( writer );
  }
}

