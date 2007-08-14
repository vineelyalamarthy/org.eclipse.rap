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
package com.w4t.webbordercomponentkit;

import junit.framework.TestCase;

import org.eclipse.rwt.internal.browser.*;
import org.eclipse.rwt.internal.lifecycle.HtmlResponseWriter;
import org.eclipse.rwt.internal.service.ContextProvider;

import com.w4t.*;
import com.w4t.ajax.AjaxStatus;

// TODO: [fappel] rewrite Tests with different borderType settings!
public class WebBorderComponentRenderer_Test extends TestCase {
  
  public void test_Ajax() throws Exception {
    WebPanel panel = new WebPanel();
    WebBorderComponent comp = new WebBorderComponent();
    comp.setAlign( "10" );
    comp.setHeight( "15" );
    comp.setWidth( "7" );
    comp.setBorderType( 1 );
    comp.setName( "myBorderComp" );
    comp.setPadding( 10 );
    comp.setVAlign( "10" );
    comp.setVisible( true );
    panel.add( comp );
    W4TFixture.fakeBrowser( new Ie6( true, true ) );
    AjaxStatus ajaxStatus = ( AjaxStatus )panel.getAdapter( AjaxStatus.class );
    ajaxStatus.updateStatus( true );
    HtmlResponseWriter writer = new HtmlResponseWriter();
    setResponseWriter( writer );
    W4TFixture.renderComponent( panel );
    String markup = W4TFixture.getBodyMarkup( writer );
    String expected 
      = "<div id=\"p1\">"
      + "<table width=\"7\" cellspacing=\"0\" cellpadding=\"0\""
      + " border=\"0\">"
      + "<tr><td align=\"center\" valign=\"middle\" bgcolor=\"#ffffff\" "
      + "colspan=\"3\" height=\"1\"><span class=\"w4tCsscd1f6403\">"
      + "<div><img src=\"resources/images/transparent.gif\" "
      + "border=\"0\" width=\"1\" "
      + "height=\"1\" align=\"top\" /></div></span></td></tr>"
      + "<tr><td align=\"center\" valign=\"middle\" bgcolor=\"#ffffff\">"
      + "<span class=\"w4tCsscd1f6403\">"
      + "<div><img src=\"resources/images/transparent.gif\" "
      + "border=\"0\" width=\"1\" "
      + "height=\"13\" align=\"top\" /></div></span></td>"
      + "<td align=\"10\" valign=\"10\" bgcolor=\"\" "
      + "class=\"w4tCssa67ce713\" width=\"100%\">"
      + "<table cellspacing=\"0\" cellpadding=\"0\" border=\"0\" id=\"p3\">"
      + "<tr><td>&nbsp;</td></tr></table></td>"
      + "<td align=\"center\" valign=\"middle\" bgcolor=\"#ffffff\">"
      + "<span class=\"w4tCsscd1f6403\"><div>"
      + "<img src=\"resources/images/transparent.gif\" border=\"0\" "
      + "width=\"1\" "
      + "height=\"1\" align=\"top\" /></div></span></td></tr>"
      + "<tr><td align=\"center\" valign=\"middle\" bgcolor=\"#ffffff\" "
      + "colspan=\"3\" height=\"1\"><span class=\"w4tCsscd1f6403\">"
      + "<div><img src=\"resources/images/transparent.gif\" border=\"0\" "
      + "width=\"1\" "
      + "height=\"1\" align=\"top\" /></div></span></td></tr></table></div>";
    assertEquals( expected, markup );
  }

  public void test_Ajax_Percentage() throws Exception {
    WebPanel panel = new WebPanel();
    WebBorderComponent comp = new WebBorderComponent();
    comp.setAlign( "10" );
    comp.setHeight( "15%" );
    comp.setWidth( "7" );
    comp.setBorderType( 1 );
    comp.setName( "myBorderComp" );
    comp.setPadding( 10 );
    comp.setVAlign( "10" );
    comp.setVisible( true );
    panel.add( comp );
    W4TFixture.fakeBrowser( new Ie6( true, true ) );
    AjaxStatus ajaxStatus = ( AjaxStatus )panel.getAdapter( AjaxStatus.class );
    ajaxStatus.updateStatus( true );
    HtmlResponseWriter writer = new HtmlResponseWriter();
    setResponseWriter( writer );
    W4TFixture.renderComponent( panel );
    String markup = W4TFixture.getBodyMarkup( writer );
    String expected 
    = "<div id=\"p1\">"
      + "<table width=\"7\" height=\"15%\" cellspacing=\"0\" cellpadding=\"0\""
      + " border=\"0\">"
      + "<tr><td align=\"center\" valign=\"middle\" bgcolor=\"#ffffff\" "
      + "colspan=\"3\" height=\"1\"><span class=\"w4tCsscd1f6403\">"
      + "<div><img src=\"resources/images/transparent.gif\" border=\"0\" "
      + "width=\"1\" "
      + "height=\"1\" align=\"top\" /></div></span></td></tr>"
      + "<tr><td align=\"center\" valign=\"middle\" bgcolor=\"#ffffff\">"
      + "<span class=\"w4tCsscd1f6403\">"
      + "<div><img src=\"resources/images/transparent.gif\" border=\"0\" "
      + "width=\"1\" "
      + "height=\"1\" align=\"top\" /></div></span></td>"
      + "<td align=\"10\" valign=\"10\" bgcolor=\"\" "
      + "class=\"w4tCssa67ce713\" width=\"100%\">"
      + "<table cellspacing=\"0\" cellpadding=\"0\" border=\"0\" id=\"p3\">"
      + "<tr><td>&nbsp;</td></tr></table></td>"
      + "<td align=\"center\" valign=\"middle\" bgcolor=\"#ffffff\">"
      + "<span class=\"w4tCsscd1f6403\"><div>"
      + "<img src=\"resources/images/transparent.gif\" border=\"0\" "
      + "width=\"1\" "
      + "height=\"1\" align=\"top\" /></div></span></td></tr>"
      + "<tr><td align=\"center\" valign=\"middle\" bgcolor=\"#ffffff\" "
      + "colspan=\"3\" height=\"1\"><span class=\"w4tCsscd1f6403\">"
      + "<div><img src=\"resources/images/transparent.gif\" border=\"0\" "
      + "width=\"1\" "
      + "height=\"1\" align=\"top\" /></div></span></td></tr></table></div>";
    assertEquals( expected, markup );
  }

  public void test_Script() throws Exception {
    WebPanel panel = new WebPanel();
    WebBorderComponent comp = new WebBorderComponent();
    comp.setAlign( "10" );
    comp.setHeight( "15" );
    comp.setWidth( "7" );
    comp.setBorderType( 1 );
    comp.setName( "myBorderComp" );
    comp.setPadding( 10 );
    comp.setVAlign( "10" );
    comp.setVisible( true );
    panel.add( comp );
    W4TFixture.fakeBrowser( new Ie5up ( true, false ) );
    AjaxStatus ajaxStatus = ( AjaxStatus )panel.getAdapter( AjaxStatus.class );
    ajaxStatus.updateStatus( true );
    HtmlResponseWriter writer = new HtmlResponseWriter();
    setResponseWriter( writer );
    W4TFixture.renderComponent( panel );
    String markup = W4TFixture.getBodyMarkup( writer );
    String expected 
    = "<table width=\"7\" cellspacing=\"0\" cellpadding=\"0\""
      + " border=\"0\">"
      + "<tr><td align=\"center\" valign=\"middle\" bgcolor=\"#ffffff\" "
      + "colspan=\"3\" height=\"1\"><span class=\"w4tCsscd1f6403\">"
      + "<div><img src=\"resources/images/transparent.gif\" border=\"0\" "
      + "width=\"1\" "
      + "height=\"1\" align=\"top\" /></div></span></td></tr>"
      + "<tr><td align=\"center\" valign=\"middle\" bgcolor=\"#ffffff\">"
      + "<span class=\"w4tCsscd1f6403\">"
      + "<div><img src=\"resources/images/transparent.gif\" border=\"0\" "
      + "width=\"1\" "
      + "height=\"13\" align=\"top\" /></div></span></td>"
      + "<td align=\"10\" valign=\"10\" bgcolor=\"\" "
      + "class=\"w4tCssa67ce713\" width=\"100%\">"
      + "<table cellspacing=\"0\" cellpadding=\"0\" border=\"0\" id=\"p3\">"
      + "<tr><td>&nbsp;</td></tr></table></td>"
      + "<td align=\"center\" valign=\"middle\" bgcolor=\"#ffffff\">"
      + "<span class=\"w4tCsscd1f6403\"><div>"
      + "<img src=\"resources/images/transparent.gif\" border=\"0\" "
      + "width=\"1\" "
      + "height=\"1\" align=\"top\" /></div></span></td></tr>"
      + "<tr><td align=\"center\" valign=\"middle\" bgcolor=\"#ffffff\" "
      + "colspan=\"3\" height=\"1\"><span class=\"w4tCsscd1f6403\">"
      + "<div><img src=\"resources/images/transparent.gif\" border=\"0\" "
      + "width=\"1\" "
      + "height=\"1\" align=\"top\" /></div></span></td></tr></table>";
    assertEquals( expected, markup );
  }

  public void test_NoScript() throws Exception {
    WebPanel panel = new WebPanel();
    WebBorderComponent comp = new WebBorderComponent();
    comp.setAlign( "10" );
    comp.setHeight( "15" );
    comp.setWidth( "7" );
    comp.setBorderType( 1 );
    comp.setName( "myBorderComp" );
    comp.setPadding( 10 );
    comp.setVAlign( "10" );
    comp.setVisible( true );
    panel.add( comp );
    W4TFixture.fakeBrowser( new Default( false, false ) );
    AjaxStatus ajaxStatus = ( AjaxStatus )panel.getAdapter( AjaxStatus.class );
    ajaxStatus.updateStatus( true );
    HtmlResponseWriter writer = new HtmlResponseWriter();
    setResponseWriter( writer );
    W4TFixture.renderComponent( panel );
    String markup = W4TFixture.getBodyMarkup( writer );
    String expected 
    = "<table width=\"7\" cellspacing=\"0\" cellpadding=\"0\""
        + " border=\"0\">"
        + "<tr><td align=\"center\" valign=\"middle\" bgcolor=\"#ffffff\" "
        + "colspan=\"3\" height=\"1\"><span class=\"w4tCsscd1f6403\">"
        + "<div><img src=\"resources/images/transparent.gif\" border=\"0\" "
        + "width=\"1\" "
        + "height=\"1\" align=\"top\" /></div></span></td></tr>"
        + "<tr><td align=\"center\" valign=\"middle\" bgcolor=\"#ffffff\">"
        + "<span class=\"w4tCsscd1f6403\">"
        + "<div><img src=\"resources/images/transparent.gif\" border=\"0\" "
        + "width=\"1\" "
        + "height=\"13\" align=\"top\" /></div></span></td>"
        + "<td align=\"10\" valign=\"10\" bgcolor=\"\" "
        + "class=\"w4tCssa67ce713\" width=\"100%\">"
        + "<table cellspacing=\"0\" cellpadding=\"0\" border=\"0\" id=\"p3\">"
        + "<tr><td>&nbsp;</td></tr></table></td>"
        + "<td align=\"center\" valign=\"middle\" bgcolor=\"#ffffff\">"
        + "<span class=\"w4tCsscd1f6403\"><div>"
        + "<img src=\"resources/images/transparent.gif\" border=\"0\" "
        + "width=\"1\" "
        + "height=\"1\" align=\"top\" /></div></span></td></tr>"
        + "<tr><td align=\"center\" valign=\"middle\" bgcolor=\"#ffffff\" "
        + "colspan=\"3\" height=\"1\"><span class=\"w4tCsscd1f6403\">"
        + "<div><img src=\"resources/images/transparent.gif\" border=\"0\" "
        + "width=\"1\" "
        + "height=\"1\" align=\"top\" /></div></span></td></tr></table>";
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
