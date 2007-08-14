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

import org.eclipse.rwt.internal.browser.Default;
import org.eclipse.rwt.internal.browser.Ie5up;
import org.eclipse.rwt.internal.lifecycle.HtmlResponseWriter;
import org.eclipse.rwt.internal.service.ContextProvider;

import com.w4t.*;
import com.w4t.ajax.AjaxStatus;

/**
 * <p>
 * Tests the rendering of org.eclipse.rap.WebFlowLayout.
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
    W4TFixture.fakeBrowser( new Default ( true, true ) );
    AjaxStatus ajaxStatus = ( AjaxStatus )panel.getAdapter( AjaxStatus.class );
    ajaxStatus.updateStatus( true );
    HtmlResponseWriter writer = new HtmlResponseWriter();
    setResponseWriter( writer );
    W4TFixture.renderComponent( panel );
    String markup = W4TFixture.getBodyMarkup( writer );
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
    W4TFixture.fakeBrowser( new Ie5up ( true, false ) );
    HtmlResponseWriter writer = new HtmlResponseWriter();
    setResponseWriter( writer );
    W4TFixture.renderComponent( panel );
    String markup = W4TFixture.getBodyMarkup( writer );
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
    W4TFixture.fakeBrowser( new Default ( false, false ) );
    HtmlResponseWriter writer = new HtmlResponseWriter();
    setResponseWriter( writer );
    W4TFixture.renderComponent( panel );
    String markup = W4TFixture.getBodyMarkup( writer );
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
    W4TFixture.setUp();
    W4TFixture.createContext();
  }

  protected void tearDown() throws Exception {
    W4TFixture.removeContext();
    W4TFixture.tearDown();
  }

  private void setResponseWriter( final HtmlResponseWriter writer ) {
    ContextProvider.getStateInfo().setResponseWriter( writer );
  }
}

