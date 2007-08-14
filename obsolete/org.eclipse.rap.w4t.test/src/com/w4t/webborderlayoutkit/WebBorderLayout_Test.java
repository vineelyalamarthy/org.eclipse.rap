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
package com.w4t.webborderlayoutkit;

import junit.framework.TestCase;

import org.eclipse.rwt.internal.browser.Default;
import org.eclipse.rwt.internal.lifecycle.HtmlResponseWriter;
import org.eclipse.rwt.internal.service.ContextProvider;

import com.w4t.*;

/** <p>Unit tests for WebBorderLayoutRenderer.</p>
 */

public class WebBorderLayout_Test extends TestCase {
  
  
  // single 
  ///////////////////////
  public void test_Center() throws Exception {
    WebPanel panel = new WebPanel();
    WebBorderLayout layout = new WebBorderLayout();
    panel.setWebLayout( layout );
    WebLabel lblCenter = new WebLabel( "CENTER" );
    panel.add( lblCenter, WebBorderLayout.CENTER );
    layout.setAlign( "10" );
    layout.setBorder( "1" );
    layout.setCellpadding( "10" );
    layout.setCellspacing( "5" );
    layout.setHeight( "15" );
    layout.setWidth( "7" );
    W4TFixture.fakeBrowser( new Default ( true, false ) );
    HtmlResponseWriter writer = new HtmlResponseWriter();
    setResponseWriter( writer );
    W4TFixture.renderComponent( panel );
    String markup = W4TFixture.getBodyMarkup( writer );
    String expected 
      = "<table id=\"p1\" width=\"7\" height=\"15\" cellspacing=\"5\" "
      + "cellpadding=\"10\" border=\"1\" align=\"10\">"
      + "<tr><td align=\"center\" valign=\"middle\" colspan=\"3\">"
      + "<span id=\"p2\" class=\"w4tCsscd1f6403\">CENTER</span></td></tr>"
      + "</table>";
    assertEquals( expected, markup );
  }

  public void test_East() throws Exception {
    WebPanel panel = new WebPanel();
    WebBorderLayout layout = new WebBorderLayout();
    panel.setWebLayout( layout );
    WebLabel lblEast = new WebLabel( "EAST" );
    panel.add( lblEast, WebBorderLayout.EAST );
    layout.setAlign( "10" );
    layout.setBorder( "1" );
    layout.setCellpadding( "10" );
    layout.setCellspacing( "5" );
    layout.setHeight( "15" );
    layout.setWidth( "7" );
    W4TFixture.fakeBrowser( new Default ( true, false ) );
    HtmlResponseWriter writer = new HtmlResponseWriter();
    setResponseWriter( writer );
    W4TFixture.renderComponent( panel );
    String markup = W4TFixture.getBodyMarkup( writer );
    String expected 
    = "<table id=\"p1\" width=\"7\" height=\"15\" cellspacing=\"5\" "
      + "cellpadding=\"10\" border=\"1\" align=\"10\">"
      + "<tr><td align=\"center\" valign=\"middle\" colspan=\"3\">"
      + "<span id=\"p2\" class=\"w4tCsscd1f6403\">EAST</span></td></tr>"
      + "</table>";
    assertEquals( expected, markup );
  }

  public void test_North() throws Exception {
    WebPanel panel = new WebPanel();
    WebBorderLayout layout = new WebBorderLayout();
    panel.setWebLayout( layout );
    WebLabel lblNorth = new WebLabel( "NORTH" );
    panel.add( lblNorth, WebBorderLayout.NORTH );
    layout.setAlign( "10" );
    layout.setBorder( "1" );
    layout.setCellpadding( "10" );
    layout.setCellspacing( "5" );
    layout.setHeight( "15" );
    layout.setWidth( "7" );
    W4TFixture.fakeBrowser( new Default ( true, false ) );
    HtmlResponseWriter writer = new HtmlResponseWriter();
    setResponseWriter( writer );
    W4TFixture.renderComponent( panel );
    String markup = W4TFixture.getBodyMarkup( writer );
    String expected 
    = "<table id=\"p1\" width=\"7\" height=\"15\" cellspacing=\"5\" "
      + "cellpadding=\"10\" border=\"1\" align=\"10\">"
      + "<tr><td align=\"center\" valign=\"middle\" colspan=\"3\">"
      + "<span id=\"p2\" class=\"w4tCsscd1f6403\">NORTH</span></td></tr>"
      + "</table>";
    assertEquals( expected, markup );
  }
  
  public void test_South() throws Exception {
    WebPanel panel = new WebPanel();
    WebBorderLayout layout = new WebBorderLayout();
    panel.setWebLayout( layout );
    WebLabel lblSouth = new WebLabel( "SOUTH" );
    panel.add( lblSouth, WebBorderLayout.SOUTH );
    layout.setAlign( "10" );
    layout.setBorder( "1" );
    layout.setCellpadding( "10" );
    layout.setCellspacing( "5" );
    layout.setHeight( "15" );
    layout.setWidth( "7" );
    W4TFixture.fakeBrowser( new Default ( true, false ) );
    HtmlResponseWriter writer = new HtmlResponseWriter();
    setResponseWriter( writer );
    W4TFixture.renderComponent( panel );
    String markup = W4TFixture.getBodyMarkup( writer );
    String expected 
    = "<table id=\"p1\" width=\"7\" height=\"15\" cellspacing=\"5\" "
      + "cellpadding=\"10\" border=\"1\" align=\"10\">"
      + "<tr><td align=\"center\" valign=\"middle\" colspan=\"3\">"
      + "<span id=\"p2\" class=\"w4tCsscd1f6403\">SOUTH</span></td></tr>"
      + "</table>";
    assertEquals( expected, markup );
  }

  public void test_West() throws Exception {
    WebPanel panel = new WebPanel();
    WebBorderLayout layout = new WebBorderLayout();
    panel.setWebLayout( layout );
    WebLabel lblWest = new WebLabel( "WEST" );
    panel.add( lblWest, WebBorderLayout.WEST );
    layout.setAlign( "10" );
    layout.setBorder( "1" );
    layout.setCellpadding( "10" );
    layout.setCellspacing( "5" );
    layout.setHeight( "15" );
    layout.setWidth( "7" );
    W4TFixture.fakeBrowser( new Default ( true, false ) );
    HtmlResponseWriter writer = new HtmlResponseWriter();
    setResponseWriter( writer );
    W4TFixture.renderComponent( panel );
    String markup = W4TFixture.getBodyMarkup( writer );
    String expected 
    = "<table id=\"p1\" width=\"7\" height=\"15\" cellspacing=\"5\" "
      + "cellpadding=\"10\" border=\"1\" align=\"10\">"
      + "<tr><td align=\"center\" valign=\"middle\" colspan=\"3\">"
      + "<span id=\"p2\" class=\"w4tCsscd1f6403\">WEST</span></td></tr>"
      + "</table>";
    assertEquals( expected, markup );
  }
  
  // double
  ////////////////////////
  public void test_Center_East() throws Exception {
    WebPanel panel = new WebPanel();
    WebBorderLayout layout = new WebBorderLayout();
    panel.setWebLayout( layout );
    WebLabel lblEast = new WebLabel( "EAST" );
    WebLabel lblCenter = new WebLabel( "CENTER" );
    panel.add( lblEast, WebBorderLayout.EAST);
    panel.add( lblCenter, WebBorderLayout.CENTER );
    layout.setAlign( "10" );
    layout.setBorder( "1" );
    layout.setCellpadding( "10" );
    layout.setCellspacing( "5" );
    layout.setHeight( "15" );
    layout.setWidth( "7" );
    W4TFixture.fakeBrowser( new Default ( true, false ) );
    HtmlResponseWriter writer = new HtmlResponseWriter();
    setResponseWriter( writer );
    W4TFixture.renderComponent( panel );
    String markup = W4TFixture.getBodyMarkup( writer );
    String expected 
    = "<table id=\"p1\" width=\"7\" height=\"15\" cellspacing=\"5\" "
      + "cellpadding=\"10\" border=\"1\" align=\"10\">"
      + "<tr><td align=\"center\" valign=\"middle\" colspan=\"2\">"
      + "<span id=\"p3\" class=\"w4tCsscd1f6403\">CENTER</span></td>"
      + "<td align=\"center\" valign=\"middle\">"
      + "<span id=\"p2\" class=\"w4tCsscd1f6403\">EAST</span></td></tr>"
      + "</table>";
    assertEquals( expected, markup );
  }

  public void test_Center_North() throws Exception {
    WebPanel panel = new WebPanel();
    WebBorderLayout layout = new WebBorderLayout();
    panel.setWebLayout( layout );
    WebLabel lblNorth = new WebLabel( "NORTH" );
    WebLabel lblCenter = new WebLabel( "CENTER" );
    panel.add( lblNorth, WebBorderLayout.NORTH );
    panel.add( lblCenter, WebBorderLayout.CENTER );
    layout.setAlign( "10" );
    layout.setBorder( "1" );
    layout.setCellpadding( "10" );
    layout.setCellspacing( "5" );
    layout.setHeight( "15" );
    layout.setWidth( "7" );
    W4TFixture.fakeBrowser( new Default ( true, false ) );
    HtmlResponseWriter writer = new HtmlResponseWriter();
    setResponseWriter( writer );
    W4TFixture.renderComponent( panel );
    String markup = W4TFixture.getBodyMarkup( writer );
    String expected 
      = "<table id=\"p1\" width=\"7\" height=\"15\" cellspacing=\"5\" "
      + "cellpadding=\"10\" border=\"1\" align=\"10\">"
      + "<tr><td align=\"center\" valign=\"middle\" colspan=\"3\">"
      + "<span id=\"p2\" class=\"w4tCsscd1f6403\">NORTH</span></td></tr>"
      + "<tr><td align=\"center\" valign=\"middle\" colspan=\"3\">"
      + "<span id=\"p3\" class=\"w4tCsscd1f6403\">CENTER</span></td></tr>"
      + "</table>";
    assertEquals( expected, markup );
  }
  public void test_Center_South() throws Exception {
    WebPanel panel = new WebPanel();
    WebBorderLayout layout = new WebBorderLayout();
    panel.setWebLayout( layout );
    WebLabel lblSouth = new WebLabel( "SOUTH" );
    WebLabel lblCenter = new WebLabel( "CENTER" );
    panel.add( lblSouth, WebBorderLayout.SOUTH );
    panel.add( lblCenter, WebBorderLayout.CENTER );
    layout.setAlign( "10" );
    layout.setBorder( "1" );
    layout.setCellpadding( "10" );
    layout.setCellspacing( "5" );
    layout.setHeight( "15" );
    layout.setWidth( "7" );
    W4TFixture.fakeBrowser( new Default ( true, false ) );
    HtmlResponseWriter writer = new HtmlResponseWriter();
    setResponseWriter( writer );
    W4TFixture.renderComponent( panel );
    String markup = W4TFixture.getBodyMarkup( writer );
    String expected 
    = "<table id=\"p1\" width=\"7\" height=\"15\" cellspacing=\"5\" "
      + "cellpadding=\"10\" border=\"1\" align=\"10\">"
      + "<tr><td align=\"center\" valign=\"middle\" colspan=\"3\">"
      + "<span id=\"p3\" class=\"w4tCsscd1f6403\">CENTER</span></td></tr>"
      + "<tr><td align=\"center\" valign=\"middle\" colspan=\"3\">"
      + "<span id=\"p2\" class=\"w4tCsscd1f6403\">SOUTH</span></td></tr>"
      + "</table>";
    assertEquals( expected, markup );
  }

  public void test_Center_West() throws Exception {
    WebPanel panel = new WebPanel();
    WebBorderLayout layout = new WebBorderLayout();
    panel.setWebLayout( layout );
    WebLabel lblWest = new WebLabel( "WEST" );
    WebLabel lblCenter = new WebLabel( "CENTER" );
    panel.add( lblWest, WebBorderLayout.WEST );
    panel.add( lblCenter, WebBorderLayout.CENTER );
    layout.setAlign( "10" );
    layout.setBorder( "1" );
    layout.setCellpadding( "10" );
    layout.setCellspacing( "5" );
    layout.setHeight( "15" );
    layout.setWidth( "7" );
    W4TFixture.fakeBrowser( new Default ( true, false ) );
    HtmlResponseWriter writer = new HtmlResponseWriter();
    setResponseWriter( writer );
    W4TFixture.renderComponent( panel );
    String markup = W4TFixture.getBodyMarkup( writer );
    String expected 
    = "<table id=\"p1\" width=\"7\" height=\"15\" cellspacing=\"5\" "
      + "cellpadding=\"10\" border=\"1\" align=\"10\">"
      + "<tr><td align=\"center\" valign=\"middle\">"
      + "<span id=\"p2\" class=\"w4tCsscd1f6403\">WEST</span></td>"
      + "<td align=\"center\" valign=\"middle\" colspan=\"2\">"
      + "<span id=\"p3\" class=\"w4tCsscd1f6403\">CENTER</span></td></tr>"
      + "</table>";
    assertEquals( expected, markup );
  }

  public void test_North_South() throws Exception {
    WebPanel panel = new WebPanel();
    WebBorderLayout layout = new WebBorderLayout();
    panel.setWebLayout( layout );
    WebLabel lblNorth = new WebLabel( "NORTH" );
    WebLabel lblSouth = new WebLabel( "SOUTH" );
    panel.add( lblNorth, WebBorderLayout.NORTH );
    panel.add( lblSouth, WebBorderLayout.SOUTH );
    layout.setAlign( "10" );
    layout.setBorder( "1" );
    layout.setCellpadding( "10" );
    layout.setCellspacing( "5" );
    layout.setHeight( "15" );
    layout.setWidth( "7" );
    W4TFixture.fakeBrowser( new Default ( true, false ) );
    HtmlResponseWriter writer = new HtmlResponseWriter();
    setResponseWriter( writer );
    W4TFixture.renderComponent( panel );
    String markup = W4TFixture.getBodyMarkup( writer );
    String expected 
    = "<table id=\"p1\" width=\"7\" height=\"15\" cellspacing=\"5\" "
      + "cellpadding=\"10\" border=\"1\" align=\"10\">"
      + "<tr><td align=\"center\" valign=\"middle\" colspan=\"3\">"
      + "<span id=\"p2\" class=\"w4tCsscd1f6403\">NORTH</span></td></tr>"
      + "<tr><td align=\"center\" valign=\"middle\" colspan=\"3\">"
      + "<span id=\"p3\" class=\"w4tCsscd1f6403\">SOUTH</span></td></tr>"
      + "</table>";
    assertEquals( expected, markup );
  }

  public void test_North_East() throws Exception {
    WebPanel panel = new WebPanel();
    WebBorderLayout layout = new WebBorderLayout();
    panel.setWebLayout( layout );
    WebLabel lblNorth = new WebLabel( "NORTH" );
    WebLabel lblEast = new WebLabel( "EAST" );
    panel.add( lblNorth, WebBorderLayout.NORTH );
    panel.add( lblEast, WebBorderLayout.EAST );
    layout.setAlign( "10" );
    layout.setBorder( "1" );
    layout.setCellpadding( "10" );
    layout.setCellspacing( "5" );
    layout.setHeight( "15" );
    layout.setWidth( "7" );
    W4TFixture.fakeBrowser( new Default ( true, false ) );
    HtmlResponseWriter writer = new HtmlResponseWriter();
    setResponseWriter( writer );
    W4TFixture.renderComponent( panel );
    String markup = W4TFixture.getBodyMarkup( writer );
    String expected 
    = "<table id=\"p1\" width=\"7\" height=\"15\" cellspacing=\"5\" "
      + "cellpadding=\"10\" border=\"1\" align=\"10\">"
      + "<tr><td align=\"center\" valign=\"middle\" colspan=\"3\">"
      + "<span id=\"p2\" class=\"w4tCsscd1f6403\">NORTH</span></td></tr>"
      + "<tr><td align=\"center\" valign=\"middle\" colspan=\"3\">"
      + "<span id=\"p3\" class=\"w4tCsscd1f6403\">EAST</span></td></tr>"
      + "</table>";
    assertEquals( expected, markup );
  }

  public void test_North_West() throws Exception {
    WebPanel panel = new WebPanel();
    WebBorderLayout layout = new WebBorderLayout();
    panel.setWebLayout( layout );
    WebLabel lblNorth = new WebLabel( "NORTH" );
    WebLabel lblWest = new WebLabel( "WEST" );
    panel.add( lblNorth, WebBorderLayout.NORTH );
    panel.add( lblWest, WebBorderLayout.WEST );
    layout.setAlign( "10" );
    layout.setBorder( "1" );
    layout.setCellpadding( "10" );
    layout.setCellspacing( "5" );
    layout.setHeight( "15" );
    layout.setWidth( "7" );
    W4TFixture.fakeBrowser( new Default ( true, false ) );
    HtmlResponseWriter writer = new HtmlResponseWriter();
    setResponseWriter( writer );
    W4TFixture.renderComponent( panel );
    String markup = W4TFixture.getBodyMarkup( writer );
    String expected 
    = "<table id=\"p1\" width=\"7\" height=\"15\" cellspacing=\"5\" "
      + "cellpadding=\"10\" border=\"1\" align=\"10\">"
      + "<tr><td align=\"center\" valign=\"middle\" colspan=\"3\">"
      + "<span id=\"p2\" class=\"w4tCsscd1f6403\">NORTH</span></td></tr>"
      + "<tr><td align=\"center\" valign=\"middle\" colspan=\"3\">"
      + "<span id=\"p3\" class=\"w4tCsscd1f6403\">WEST</span></td></tr>"
      + "</table>";
    assertEquals( expected, markup );
  }

  public void test_East_West() throws Exception {
    WebPanel panel = new WebPanel();
    WebBorderLayout layout = new WebBorderLayout();
    panel.setWebLayout( layout );
    WebLabel lblEast = new WebLabel( "EAST" );
    WebLabel lblWest = new WebLabel( "WEST" );
    panel.add( lblEast, WebBorderLayout.EAST );
    panel.add( lblWest, WebBorderLayout.WEST );
    layout.setAlign( "10" );
    layout.setBorder( "1" );
    layout.setCellpadding( "10" );
    layout.setCellspacing( "5" );
    layout.setHeight( "15" );
    layout.setWidth( "7" );
    W4TFixture.fakeBrowser( new Default ( true, false ) );
    HtmlResponseWriter writer = new HtmlResponseWriter();
    setResponseWriter( writer );
    W4TFixture.renderComponent( panel );
    String markup = W4TFixture.getBodyMarkup( writer );
    String expected 
    = "<table id=\"p1\" width=\"7\" height=\"15\" cellspacing=\"5\" "
      + "cellpadding=\"10\" border=\"1\" align=\"10\">"
      + "<tr><td align=\"center\" valign=\"middle\" colspan=\"2\">"
      + "<span id=\"p3\" class=\"w4tCsscd1f6403\">WEST</span></td>"
      + "<td align=\"center\" valign=\"middle\">"
      + "<span id=\"p2\" class=\"w4tCsscd1f6403\">EAST</span></td></tr>"
      + "</table>";
    assertEquals( expected, markup );
  }

  public void test_East_South() throws Exception {
    WebPanel panel = new WebPanel();
    WebBorderLayout layout = new WebBorderLayout();
    panel.setWebLayout( layout );
    WebLabel lblEast = new WebLabel( "EAST" );
    WebLabel lblSouth = new WebLabel( "SOUTH" );
    panel.add( lblEast, WebBorderLayout.EAST );
    panel.add( lblSouth, WebBorderLayout.SOUTH );
    layout.setAlign( "10" );
    layout.setBorder( "1" );
    layout.setCellpadding( "10" );
    layout.setCellspacing( "5" );
    layout.setHeight( "15" );
    layout.setWidth( "7" );
    W4TFixture.fakeBrowser( new Default ( true, false ) );
    HtmlResponseWriter writer = new HtmlResponseWriter();
    setResponseWriter( writer );
    W4TFixture.renderComponent( panel );
    String markup = W4TFixture.getBodyMarkup( writer );
    String expected 
    = "<table id=\"p1\" width=\"7\" height=\"15\" cellspacing=\"5\" "
      + "cellpadding=\"10\" border=\"1\" align=\"10\">"
      + "<tr><td align=\"center\" valign=\"middle\" colspan=\"3\">"
      + "<span id=\"p2\" class=\"w4tCsscd1f6403\">EAST</span></td></tr>"
      + "<tr><td align=\"center\" valign=\"middle\" colspan=\"3\">"
      + "<span id=\"p3\" class=\"w4tCsscd1f6403\">SOUTH</span></td></tr>"
      + "</table>";
    assertEquals( expected, markup );
  }

  public void test_West_South() throws Exception {
    WebPanel panel = new WebPanel();
    WebBorderLayout layout = new WebBorderLayout();
    panel.setWebLayout( layout );
    WebLabel lblWest = new WebLabel( "WEST" );
    WebLabel lblSouth = new WebLabel( "SOUTH" );
    panel.add( lblWest, WebBorderLayout.WEST );
    panel.add( lblSouth, WebBorderLayout.SOUTH );
    layout.setAlign( "10" );
    layout.setBorder( "1" );
    layout.setCellpadding( "10" );
    layout.setCellspacing( "5" );
    layout.setHeight( "15" );
    layout.setWidth( "7" );
    W4TFixture.fakeBrowser( new Default ( true, false ) );
    HtmlResponseWriter writer = new HtmlResponseWriter();
    setResponseWriter( writer );
    W4TFixture.renderComponent( panel );
    String markup = W4TFixture.getBodyMarkup( writer );
    String expected 
    = "<table id=\"p1\" width=\"7\" height=\"15\" cellspacing=\"5\" "
      + "cellpadding=\"10\" border=\"1\" align=\"10\">"
      + "<tr><td align=\"center\" valign=\"middle\" colspan=\"3\">"
      + "<span id=\"p2\" class=\"w4tCsscd1f6403\">WEST</span></td></tr>"
      + "<tr><td align=\"center\" valign=\"middle\" colspan=\"3\">"
      + "<span id=\"p3\" class=\"w4tCsscd1f6403\">SOUTH</span></td></tr>"
      + "</table>";
    assertEquals( expected, markup );
  }

  public void test_Center_North_South() throws Exception {
    WebPanel panel = new WebPanel();
    WebBorderLayout layout = new WebBorderLayout();
    panel.setWebLayout( layout );
    WebLabel lblNorth = new WebLabel( "NORTH" );
    WebLabel lblCenter = new WebLabel( "CENTER" );
    WebLabel lblSouth = new WebLabel( "SOUTH" );
    panel.add( lblNorth, WebBorderLayout.NORTH );
    panel.add( lblCenter, WebBorderLayout.CENTER );
    panel.add( lblSouth, WebBorderLayout.SOUTH );
    layout.setAlign( "10" );
    layout.setBorder( "1" );
    layout.setCellpadding( "10" );
    layout.setCellspacing( "5" );
    layout.setHeight( "15" );
    layout.setWidth( "7" );
    W4TFixture.fakeBrowser( new Default ( true, false ) );
    HtmlResponseWriter writer = new HtmlResponseWriter();
    setResponseWriter( writer );
    W4TFixture.renderComponent( panel );
    String markup = W4TFixture.getBodyMarkup( writer );
    String expected 
    = "<table id=\"p1\" width=\"7\" height=\"15\" cellspacing=\"5\" "
      + "cellpadding=\"10\" border=\"1\" align=\"10\">"
      + "<tr><td align=\"center\" valign=\"middle\" colspan=\"3\">"
      + "<span id=\"p2\" class=\"w4tCsscd1f6403\">NORTH</span></td></tr>"
      + "<tr><td align=\"center\" valign=\"middle\" colspan=\"3\">"
      + "<span id=\"p3\" class=\"w4tCsscd1f6403\">CENTER</span></td></tr>"
      + "<tr><td align=\"center\" valign=\"middle\" colspan=\"3\">"
      + "<span id=\"p4\" class=\"w4tCsscd1f6403\">SOUTH</span></td></tr>"
      + "</table>";
    assertEquals( expected, markup );
  }

  public void test_Center_North_West() throws Exception {
    WebPanel panel = new WebPanel();
    WebBorderLayout layout = new WebBorderLayout();
    panel.setWebLayout( layout );
    WebLabel lblNorth = new WebLabel( "NORTH" );
    WebLabel lblCenter = new WebLabel( "CENTER" );
    WebLabel lblWest = new WebLabel( "WEST" );
    panel.add( lblNorth, WebBorderLayout.NORTH );
    panel.add( lblCenter, WebBorderLayout.CENTER );
    panel.add( lblWest, WebBorderLayout.WEST );
    layout.setAlign( "10" );
    layout.setBorder( "1" );
    layout.setCellpadding( "10" );
    layout.setCellspacing( "5" );
    layout.setHeight( "15" );
    layout.setWidth( "7" );
    W4TFixture.fakeBrowser( new Default ( true, false ) );
    HtmlResponseWriter writer = new HtmlResponseWriter();
    setResponseWriter( writer );
    W4TFixture.renderComponent( panel );
    String markup = W4TFixture.getBodyMarkup( writer );
    String expected 
    = "<table id=\"p1\" width=\"7\" height=\"15\" cellspacing=\"5\" "
      + "cellpadding=\"10\" border=\"1\" align=\"10\">"
      + "<tr><td align=\"center\" valign=\"middle\" colspan=\"3\">"
      + "<span id=\"p2\" class=\"w4tCsscd1f6403\">NORTH</span></td></tr>"
      + "<tr><td align=\"center\" valign=\"middle\">"
      + "<span id=\"p4\" class=\"w4tCsscd1f6403\">WEST</span></td>"
      + "<td align=\"center\" valign=\"middle\" colspan=\"2\">"
      + "<span id=\"p3\" class=\"w4tCsscd1f6403\">CENTER</span></td></tr>"
      + "</table>";
    assertEquals( expected, markup );
  }

  public void test_Center_North_East() throws Exception {
    WebPanel panel = new WebPanel();
    WebBorderLayout layout = new WebBorderLayout();
    panel.setWebLayout( layout );
    WebLabel lblNorth = new WebLabel( "NORTH" );
    WebLabel lblCenter = new WebLabel( "CENTER" );
    WebLabel lblEast = new WebLabel( "EAST" );
    panel.add( lblNorth, WebBorderLayout.NORTH );
    panel.add( lblCenter, WebBorderLayout.CENTER );
    panel.add( lblEast, WebBorderLayout.EAST );
    layout.setAlign( "10" );
    layout.setBorder( "1" );
    layout.setCellpadding( "10" );
    layout.setCellspacing( "5" );
    layout.setHeight( "15" );
    layout.setWidth( "7" );
    W4TFixture.fakeBrowser( new Default ( true, false ) );
    HtmlResponseWriter writer = new HtmlResponseWriter();
    setResponseWriter( writer );
    W4TFixture.renderComponent( panel );
    String markup = W4TFixture.getBodyMarkup( writer );
    String expected 
    = "<table id=\"p1\" width=\"7\" height=\"15\" cellspacing=\"5\" "
      + "cellpadding=\"10\" border=\"1\" align=\"10\">"
      + "<tr><td align=\"center\" valign=\"middle\" colspan=\"3\">"
      + "<span id=\"p2\" class=\"w4tCsscd1f6403\">NORTH</span></td></tr>"
      + "<tr><td align=\"center\" valign=\"middle\" colspan=\"2\">"
      + "<span id=\"p3\" class=\"w4tCsscd1f6403\">CENTER</span></td>"
      + "<td align=\"center\" valign=\"middle\">"
      + "<span id=\"p4\" class=\"w4tCsscd1f6403\">EAST</span></td></tr>"
      + "</table>";
    assertEquals( expected, markup );
  }

  public void test_Center_South_West() throws Exception {
    WebPanel panel = new WebPanel();
    WebBorderLayout layout = new WebBorderLayout();
    panel.setWebLayout( layout );
    WebLabel lblSouth = new WebLabel( "SOUTH" );
    WebLabel lblCenter = new WebLabel( "CENTER" );
    WebLabel lblWest = new WebLabel( "WEST" );
    panel.add( lblSouth, WebBorderLayout.SOUTH );
    panel.add( lblCenter, WebBorderLayout.CENTER );
    panel.add( lblWest, WebBorderLayout.WEST );
    layout.setAlign( "10" );
    layout.setBorder( "1" );
    layout.setCellpadding( "10" );
    layout.setCellspacing( "5" );
    layout.setHeight( "15" );
    layout.setWidth( "7" );
    W4TFixture.fakeBrowser( new Default ( true, false ) );
    HtmlResponseWriter writer = new HtmlResponseWriter();
    setResponseWriter( writer );
    W4TFixture.renderComponent( panel );
    String markup = W4TFixture.getBodyMarkup( writer );
    String expected 
    = "<table id=\"p1\" width=\"7\" height=\"15\" cellspacing=\"5\" "
      + "cellpadding=\"10\" border=\"1\" align=\"10\">"
      + "<tr><td align=\"center\" valign=\"middle\">"
      + "<span id=\"p4\" class=\"w4tCsscd1f6403\">WEST</span></td>"
      + "<td align=\"center\" valign=\"middle\" colspan=\"2\">"
      + "<span id=\"p3\" class=\"w4tCsscd1f6403\">CENTER</span></td></tr>"
      + "<tr><td align=\"center\" valign=\"middle\" colspan=\"3\">"
      + "<span id=\"p2\" class=\"w4tCsscd1f6403\">SOUTH</span></td></tr>"
      + "</table>";
    assertEquals( expected, markup );
  }
  
  public void test_Center_South_East() throws Exception {
    WebPanel panel = new WebPanel();
    WebBorderLayout layout = new WebBorderLayout();
    panel.setWebLayout( layout );
    WebLabel lblSouth = new WebLabel( "SOUTH" );
    WebLabel lblCenter = new WebLabel( "CENTER" );
    WebLabel lblEast = new WebLabel( "EAST" );
    panel.add( lblSouth, WebBorderLayout.SOUTH );
    panel.add( lblCenter, WebBorderLayout.CENTER );
    panel.add( lblEast, WebBorderLayout.EAST );
    layout.setAlign( "10" );
    layout.setBorder( "1" );
    layout.setCellpadding( "10" );
    layout.setCellspacing( "5" );
    layout.setHeight( "15" );
    layout.setWidth( "7" );
    W4TFixture.fakeBrowser( new Default ( true, false ) );
    HtmlResponseWriter writer = new HtmlResponseWriter();
    setResponseWriter( writer );
    W4TFixture.renderComponent( panel );
    String markup = W4TFixture.getBodyMarkup( writer );
    String expected 
    = "<table id=\"p1\" width=\"7\" height=\"15\" cellspacing=\"5\" "
      + "cellpadding=\"10\" border=\"1\" align=\"10\">"
      + "<tr><td align=\"center\" valign=\"middle\" colspan=\"2\">"
      + "<span id=\"p3\" class=\"w4tCsscd1f6403\">CENTER</span></td>"
      + "<td align=\"center\" valign=\"middle\">"
      + "<span id=\"p4\" class=\"w4tCsscd1f6403\">EAST</span></td></tr>"
      + "<tr><td align=\"center\" valign=\"middle\" colspan=\"3\">"
      + "<span id=\"p2\" class=\"w4tCsscd1f6403\">SOUTH</span></td></tr>"
      + "</table>";
    assertEquals( expected, markup );
  }

  public void test_North_South_East() throws Exception {
    WebPanel panel = new WebPanel();
    WebBorderLayout layout = new WebBorderLayout();
    panel.setWebLayout( layout );
    WebLabel lblNorth = new WebLabel( "NORTH" );
    WebLabel lblSouth = new WebLabel( "SOUTH" );
    WebLabel lblEast = new WebLabel( "EAST" );
    panel.add( lblNorth, WebBorderLayout.NORTH );
    panel.add( lblSouth, WebBorderLayout.SOUTH );
    panel.add( lblEast, WebBorderLayout.EAST );
    layout.setAlign( "10" );
    layout.setBorder( "1" );
    layout.setCellpadding( "10" );
    layout.setCellspacing( "5" );
    layout.setHeight( "15" );
    layout.setWidth( "7" );
    W4TFixture.fakeBrowser( new Default ( true, false ) );
    HtmlResponseWriter writer = new HtmlResponseWriter();
    setResponseWriter( writer );
    W4TFixture.renderComponent( panel );
    String markup = W4TFixture.getBodyMarkup( writer );
    String expected 
    = "<table id=\"p1\" width=\"7\" height=\"15\" cellspacing=\"5\" "
      + "cellpadding=\"10\" border=\"1\" align=\"10\">"
      + "<tr><td align=\"center\" valign=\"middle\" colspan=\"3\">"
      + "<span id=\"p2\" class=\"w4tCsscd1f6403\">NORTH</span></td></tr>"
      + "<tr><td align=\"center\" valign=\"middle\" colspan=\"3\">"
      + "<span id=\"p4\" class=\"w4tCsscd1f6403\">EAST</span></td></tr>"
      + "<tr><td align=\"center\" valign=\"middle\" colspan=\"3\">"
      + "<span id=\"p3\" class=\"w4tCsscd1f6403\">SOUTH</span></td></tr>"
      + "</table>";
    assertEquals( expected, markup );
  }

  public void test_North_West_East() throws Exception {
    WebPanel panel = new WebPanel();
    WebBorderLayout layout = new WebBorderLayout();
    panel.setWebLayout( layout );
    WebLabel lblNorth = new WebLabel( "NORTH" );
    WebLabel lblWest = new WebLabel( "WEST" );
    WebLabel lblEast = new WebLabel( "EAST" );
    panel.add( lblNorth, WebBorderLayout.NORTH );
    panel.add( lblWest, WebBorderLayout.WEST );
    panel.add( lblEast, WebBorderLayout.EAST );
    layout.setAlign( "10" );
    layout.setBorder( "1" );
    layout.setCellpadding( "10" );
    layout.setCellspacing( "5" );
    layout.setHeight( "15" );
    layout.setWidth( "7" );
    W4TFixture.fakeBrowser( new Default ( true, false ) );
    HtmlResponseWriter writer = new HtmlResponseWriter();
    setResponseWriter( writer );
    W4TFixture.renderComponent( panel );
    String markup = W4TFixture.getBodyMarkup( writer );
    String expected 
    = "<table id=\"p1\" width=\"7\" height=\"15\" cellspacing=\"5\" "
      + "cellpadding=\"10\" border=\"1\" align=\"10\">"
      + "<tr><td align=\"center\" valign=\"middle\" colspan=\"3\">"
      + "<span id=\"p2\" class=\"w4tCsscd1f6403\">NORTH</span></td></tr>"
      + "<tr><td align=\"center\" valign=\"middle\" colspan=\"2\">"
      + "<span id=\"p3\" class=\"w4tCsscd1f6403\">WEST</span></td>"
      + "<td align=\"center\" valign=\"middle\">"
      + "<span id=\"p4\" class=\"w4tCsscd1f6403\">EAST</span></td></tr>"
      + "</table>";
    assertEquals( expected, markup );
  }

  public void test_North_West_South() throws Exception {
    WebPanel panel = new WebPanel();
    WebBorderLayout layout = new WebBorderLayout();
    panel.setWebLayout( layout );
    WebLabel lblNorth = new WebLabel( "NORTH" );
    WebLabel lblWest = new WebLabel( "WEST" );
    WebLabel lblSouth = new WebLabel( "SOUTH" );
    panel.add( lblNorth, WebBorderLayout.NORTH );
    panel.add( lblWest, WebBorderLayout.WEST );
    panel.add( lblSouth, WebBorderLayout.SOUTH );
    layout.setAlign( "10" );
    layout.setBorder( "1" );
    layout.setCellpadding( "10" );
    layout.setCellspacing( "5" );
    layout.setHeight( "15" );
    layout.setWidth( "7" );
    W4TFixture.fakeBrowser( new Default ( true, false ) );
    HtmlResponseWriter writer = new HtmlResponseWriter();
    setResponseWriter( writer );
    W4TFixture.renderComponent( panel );
    String markup = W4TFixture.getBodyMarkup( writer );
    String expected 
    = "<table id=\"p1\" width=\"7\" height=\"15\" cellspacing=\"5\" "
      + "cellpadding=\"10\" border=\"1\" align=\"10\">"
      + "<tr><td align=\"center\" valign=\"middle\" colspan=\"3\">"
      + "<span id=\"p2\" class=\"w4tCsscd1f6403\">NORTH</span></td></tr>"
      + "<tr><td align=\"center\" valign=\"middle\" colspan=\"3\">"
      + "<span id=\"p3\" class=\"w4tCsscd1f6403\">WEST</span></td></tr>"
      + "<tr><td align=\"center\" valign=\"middle\" colspan=\"3\">"
      + "<span id=\"p4\" class=\"w4tCsscd1f6403\">SOUTH</span></td></tr>"
      + "</table>";
    assertEquals( expected, markup );
  }
  
  public void test_South_West_East() throws Exception {
    WebPanel panel = new WebPanel();
    WebBorderLayout layout = new WebBorderLayout();
    panel.setWebLayout( layout );
    WebLabel lblSouth = new WebLabel( "SOUTH" );
    WebLabel lblWest = new WebLabel( "WEST" );
    WebLabel lblEast = new WebLabel( "EAST" );
    panel.add( lblSouth, WebBorderLayout.SOUTH );
    panel.add( lblWest, WebBorderLayout.WEST );
    panel.add( lblEast, WebBorderLayout.EAST );
    layout.setAlign( "10" );
    layout.setBorder( "1" );
    layout.setCellpadding( "10" );
    layout.setCellspacing( "5" );
    layout.setHeight( "15" );
    layout.setWidth( "7" );
    W4TFixture.fakeBrowser( new Default ( true, false ) );
    HtmlResponseWriter writer = new HtmlResponseWriter();
    setResponseWriter( writer );
    W4TFixture.renderComponent( panel );
    String markup = W4TFixture.getBodyMarkup( writer );
    String expected 
    = "<table id=\"p1\" width=\"7\" height=\"15\" cellspacing=\"5\" "
      + "cellpadding=\"10\" border=\"1\" align=\"10\">"
      + "<tr><td align=\"center\" valign=\"middle\" colspan=\"2\">"
      + "<span id=\"p3\" class=\"w4tCsscd1f6403\">WEST</span></td>"
      + "<td align=\"center\" valign=\"middle\">"
      + "<span id=\"p4\" class=\"w4tCsscd1f6403\">EAST</span></td></tr>"
      + "<tr><td align=\"center\" valign=\"middle\" colspan=\"3\">"
      + "<span id=\"p2\" class=\"w4tCsscd1f6403\">SOUTH</span></td></tr>"
      + "</table>";
    assertEquals( expected, markup );
  } 
  
  public void test_Center_North_South_West() throws Exception {
    WebPanel panel = new WebPanel();
    WebBorderLayout layout = new WebBorderLayout();
    panel.setWebLayout( layout );
    WebLabel lblNorth = new WebLabel( "NORTH" );
    WebLabel lblCenter = new WebLabel( "CENTER" );
    WebLabel lblSouth = new WebLabel( "SOUTH" );
    WebLabel lblWest = new WebLabel( "WEST" );
    panel.add( lblNorth, WebBorderLayout.NORTH );
    panel.add( lblCenter, WebBorderLayout.CENTER );
    panel.add( lblSouth, WebBorderLayout.SOUTH );
    panel.add( lblWest, WebBorderLayout.WEST );
    layout.setAlign( "10" );
    layout.setBorder( "1" );
    layout.setCellpadding( "10" );
    layout.setCellspacing( "5" );
    layout.setHeight( "15" );
    layout.setWidth( "7" );
    W4TFixture.fakeBrowser( new Default ( true, false ) );
    HtmlResponseWriter writer = new HtmlResponseWriter();
    setResponseWriter( writer );
    W4TFixture.renderComponent( panel );
    String markup = W4TFixture.getBodyMarkup( writer );
    String expected 
    = "<table id=\"p1\" width=\"7\" height=\"15\" cellspacing=\"5\" "
    + "cellpadding=\"10\" border=\"1\" align=\"10\">"
    + "<tr><td align=\"center\" valign=\"middle\" colspan=\"3\">"
    + "<span id=\"p2\" class=\"w4tCsscd1f6403\">NORTH</span></td></tr>"
    + "<tr><td align=\"center\" valign=\"middle\">"
    + "<span id=\"p5\" class=\"w4tCsscd1f6403\">WEST</span></td>"
    + "<td align=\"center\" valign=\"middle\" colspan=\"2\">"
    + "<span id=\"p3\" class=\"w4tCsscd1f6403\">CENTER</span></td></tr>"
    + "<tr><td align=\"center\" valign=\"middle\" colspan=\"3\">"
    + "<span id=\"p4\" class=\"w4tCsscd1f6403\">SOUTH</span></td></tr></table>";
    assertEquals( expected, markup );
  }
  public void test_East_North_South_West() throws Exception {
    WebPanel panel = new WebPanel();
    WebBorderLayout layout = new WebBorderLayout();
    panel.setWebLayout( layout );
    WebLabel lblNorth = new WebLabel( "NORTH" );
    WebLabel lblEast = new WebLabel( "EAST" );
    WebLabel lblSouth = new WebLabel( "SOUTH" );
    WebLabel lblWest = new WebLabel( "WEST" );
    panel.add( lblNorth, WebBorderLayout.NORTH );
    panel.add( lblEast, WebBorderLayout.EAST );
    panel.add( lblSouth, WebBorderLayout.SOUTH );
    panel.add( lblWest, WebBorderLayout.WEST );
    layout.setAlign( "10" );
    layout.setBorder( "1" );
    layout.setCellpadding( "10" );
    layout.setCellspacing( "5" );
    layout.setHeight( "15" );
    layout.setWidth( "7" );
    W4TFixture.fakeBrowser( new Default ( true, false ) );
    HtmlResponseWriter writer = new HtmlResponseWriter();
    setResponseWriter( writer );
    W4TFixture.renderComponent( panel );
    String markup = W4TFixture.getBodyMarkup( writer );
    String expected 
    = "<table id=\"p1\" width=\"7\" height=\"15\" cellspacing=\"5\" "
    + "cellpadding=\"10\" border=\"1\" align=\"10\">"
    + "<tr><td align=\"center\" valign=\"middle\" colspan=\"3\">"
    + "<span id=\"p2\" class=\"w4tCsscd1f6403\">NORTH</span></td></tr>"
    + "<tr><td align=\"center\" valign=\"middle\" colspan=\"2\">"
    + "<span id=\"p5\" class=\"w4tCsscd1f6403\">WEST</span></td>"
    + "<td align=\"center\" valign=\"middle\">"
    + "<span id=\"p3\" class=\"w4tCsscd1f6403\">EAST</span></td></tr>"
    + "<tr><td align=\"center\" valign=\"middle\" colspan=\"3\">"
    + "<span id=\"p4\" class=\"w4tCsscd1f6403\">SOUTH</span></td></tr></table>";
    assertEquals( expected, markup );
  }
  public void test_Center_East_South_West() throws Exception {
    WebPanel panel = new WebPanel();
    WebBorderLayout layout = new WebBorderLayout();
    panel.setWebLayout( layout );
    WebLabel lblEast = new WebLabel( "EAST" );
    WebLabel lblCenter = new WebLabel( "CENTER" );
    WebLabel lblSouth = new WebLabel( "SOUTH" );
    WebLabel lblWest = new WebLabel( "WEST" );
    panel.add( lblEast, WebBorderLayout.EAST );
    panel.add( lblCenter, WebBorderLayout.CENTER );
    panel.add( lblSouth, WebBorderLayout.SOUTH );
    panel.add( lblWest, WebBorderLayout.WEST );
    layout.setAlign( "10" );
    layout.setBorder( "1" );
    layout.setCellpadding( "10" );
    layout.setCellspacing( "5" );
    layout.setHeight( "15" );
    layout.setWidth( "7" );
    W4TFixture.fakeBrowser( new Default ( true, false ) );
    HtmlResponseWriter writer = new HtmlResponseWriter();
    setResponseWriter( writer );
    W4TFixture.renderComponent( panel );
    String markup = W4TFixture.getBodyMarkup( writer );
    String expected 
    = "<table id=\"p1\" width=\"7\" height=\"15\" cellspacing=\"5\" "
    + "cellpadding=\"10\" border=\"1\" align=\"10\">"
    + "<tr><td align=\"center\" valign=\"middle\">"
    + "<span id=\"p5\" class=\"w4tCsscd1f6403\">WEST</span></td>"
    + "<td align=\"center\" valign=\"middle\">"
    + "<span id=\"p3\" class=\"w4tCsscd1f6403\">CENTER</span></td>"
    + "<td align=\"center\" valign=\"middle\">"
    + "<span id=\"p2\" class=\"w4tCsscd1f6403\">EAST</span></td></tr>"
    + "<tr><td align=\"center\" valign=\"middle\" colspan=\"3\">"
    + "<span id=\"p4\" class=\"w4tCsscd1f6403\">SOUTH</span></td></tr></table>";
    assertEquals( expected, markup );
  }
  public void test_Center_North_East_West() throws Exception {
    WebPanel panel = new WebPanel();
    WebBorderLayout layout = new WebBorderLayout();
    panel.setWebLayout( layout );
    WebLabel lblNorth = new WebLabel( "NORTH" );
    WebLabel lblCenter = new WebLabel( "CENTER" );
    WebLabel lblEast = new WebLabel( "EAST" );
    WebLabel lblWest = new WebLabel( "WEST" );
    panel.add( lblNorth, WebBorderLayout.NORTH );
    panel.add( lblCenter, WebBorderLayout.CENTER );
    panel.add( lblEast, WebBorderLayout.EAST );
    panel.add( lblWest, WebBorderLayout.WEST );
    layout.setAlign( "10" );
    layout.setBorder( "1" );
    layout.setCellpadding( "10" );
    layout.setCellspacing( "5" );
    layout.setHeight( "15" );
    layout.setWidth( "7" );
    W4TFixture.fakeBrowser( new Default ( true, false ) );
    HtmlResponseWriter writer = new HtmlResponseWriter();
    setResponseWriter( writer );
    W4TFixture.renderComponent( panel );
    String markup = W4TFixture.getBodyMarkup( writer );
    String expected 
    = "<table id=\"p1\" width=\"7\" height=\"15\" cellspacing=\"5\" "
    + "cellpadding=\"10\" border=\"1\" align=\"10\">"
    + "<tr><td align=\"center\" valign=\"middle\" colspan=\"3\">"
    + "<span id=\"p2\" class=\"w4tCsscd1f6403\">NORTH</span></td></tr>"
    + "<tr><td align=\"center\" valign=\"middle\">"
    + "<span id=\"p5\" class=\"w4tCsscd1f6403\">WEST</span></td>"
    + "<td align=\"center\" valign=\"middle\">"
    + "<span id=\"p3\" class=\"w4tCsscd1f6403\">CENTER</span></td>"
    + "<td align=\"center\" valign=\"middle\">"
    + "<span id=\"p4\" class=\"w4tCsscd1f6403\">EAST</span></td></tr></table>";
    assertEquals( expected, markup );
  }
  public void test_Center_North_South_East() throws Exception {
    WebPanel panel = new WebPanel();
    WebBorderLayout layout = new WebBorderLayout();
    panel.setWebLayout( layout );
    WebLabel lblNorth = new WebLabel( "NORTH" );
    WebLabel lblCenter = new WebLabel( "CENTER" );
    WebLabel lblSouth = new WebLabel( "SOUTH" );
    WebLabel lblEast = new WebLabel( "EAST" );
    panel.add( lblNorth, WebBorderLayout.NORTH );
    panel.add( lblCenter, WebBorderLayout.CENTER );
    panel.add( lblSouth, WebBorderLayout.SOUTH );
    panel.add( lblEast, WebBorderLayout.EAST );
    layout.setAlign( "10" );
    layout.setBorder( "1" );
    layout.setCellpadding( "10" );
    layout.setCellspacing( "5" );
    layout.setHeight( "15" );
    layout.setWidth( "7" );
    W4TFixture.fakeBrowser( new Default ( true, false ) );
    HtmlResponseWriter writer = new HtmlResponseWriter();
    setResponseWriter( writer );
    W4TFixture.renderComponent( panel );
    String markup = W4TFixture.getBodyMarkup( writer );
    String expected 
    = "<table id=\"p1\" width=\"7\" height=\"15\" cellspacing=\"5\" "
    + "cellpadding=\"10\" border=\"1\" align=\"10\">"
    + "<tr><td align=\"center\" valign=\"middle\" colspan=\"3\">"
    + "<span id=\"p2\" class=\"w4tCsscd1f6403\">NORTH</span></td></tr>"
    + "<tr><td align=\"center\" valign=\"middle\" colspan=\"2\">"
    + "<span id=\"p3\" class=\"w4tCsscd1f6403\">CENTER</span></td>"
    + "<td align=\"center\" valign=\"middle\">"
    + "<span id=\"p5\" class=\"w4tCsscd1f6403\">EAST</span></td></tr>"
    + "<tr><td align=\"center\" valign=\"middle\" colspan=\"3\">"
    + "<span id=\"p4\" class=\"w4tCsscd1f6403\">SOUTH</span></td></tr></table>";
    assertEquals( expected, markup );
  }

  public void test_Center_North_South_West_East() throws Exception {
    WebPanel panel = new WebPanel();
    WebBorderLayout layout = new WebBorderLayout();
    panel.setWebLayout( layout );
    WebLabel lblNorth = new WebLabel( "NORTH" );
    WebLabel lblWest = new WebLabel( "WEST" );
    WebLabel lblCenter = new WebLabel( "CENTER" );
    WebLabel lblSouth = new WebLabel( "SOUTH" );
    WebLabel lblEast = new WebLabel( "EAST" );
    panel.add( lblNorth, WebBorderLayout.NORTH );
    panel.add( lblCenter, WebBorderLayout.CENTER );
    panel.add( lblSouth, WebBorderLayout.SOUTH );
    panel.add( lblWest, WebBorderLayout.WEST );
    panel.add( lblEast, WebBorderLayout.EAST );
    layout.setAlign( "10" );
    layout.setBorder( "1" );
    layout.setCellpadding( "10" );
    layout.setCellspacing( "5" );
    layout.setHeight( "15" );
    layout.setWidth( "7" );
    W4TFixture.fakeBrowser( new Default ( true, false ) );
    HtmlResponseWriter writer = new HtmlResponseWriter();
    setResponseWriter( writer );
    W4TFixture.renderComponent( panel );
    String markup = W4TFixture.getBodyMarkup( writer );
    String expected 
    = "<table id=\"p1\" width=\"7\" height=\"15\" cellspacing=\"5\" "
    + "cellpadding=\"10\" border=\"1\" align=\"10\">"
    + "<tr><td align=\"center\" valign=\"middle\" colspan=\"3\">"
    + "<span id=\"p2\" class=\"w4tCsscd1f6403\">NORTH</span></td></tr>"
    + "<tr><td align=\"center\" valign=\"middle\">"
    + "<span id=\"p3\" class=\"w4tCsscd1f6403\">WEST</span></td>"
    + "<td align=\"center\" valign=\"middle\">"
    + "<span id=\"p4\" class=\"w4tCsscd1f6403\">CENTER</span></td>"
    + "<td align=\"center\" valign=\"middle\">"
    + "<span id=\"p6\" class=\"w4tCsscd1f6403\">EAST</span></td></tr>"
    + "<tr><td align=\"center\" valign=\"middle\" colspan=\"3\">"
    + "<span id=\"p5\" class=\"w4tCsscd1f6403\">SOUTH</span></td></tr></table>";
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
