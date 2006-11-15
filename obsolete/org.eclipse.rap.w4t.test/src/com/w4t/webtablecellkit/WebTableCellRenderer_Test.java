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
package com.w4t.webtablecellkit;

import junit.framework.TestCase;
import com.w4t.*;
import com.w4t.util.browser.Default;
import com.w4t.util.browser.Ie5up;

/** <p>Unit tests for WebTableCellRenderer.</p> */
public class WebTableCellRenderer_Test extends TestCase {
  
  public void testAjaxRenderer() throws Exception {
    WebTableCell cell = new WebTableCell();
    cell.setAlign( "center" );
    cell.setBackground( "black" );
    cell.setColspan( "5" );
    cell.setCssClass( "myClass" );
    cell.setDir( "myDir" );
    cell.setHeight( "5" );
    cell.setIgnoreLocalStyle( false );
    cell.setLang( "myLang" );
    cell.setNowrap( true );
    cell.setPadding( "5" );
    cell.setRowspan( "6" );
    cell.setSpacing( "2" );
    cell.setTitle( "myTitle" );
    cell.setVAlign( "vAlign" );
    cell.setValue( "myValue" );
    cell.setWidth( "10" );
    HtmlResponseWriter writer = new HtmlResponseWriter();
    Fixture.fakeBrowser( new Default( true, true ) );
    Fixture.setResponseWriter( writer );
    cell.render();
    String markup = Fixture.getBodyMarkup( writer );
    String expected 
      = "<td><table border=\"0\" cellpadding=\"5\" cellspacing=\"2\">"
      + "<tr><td align=\"center\" valign=\"vAlign\" nowrap "
      + "colspan=\"5\" rowspan=\"6\" width=\"10\" height=\"5\" "
      + "background=\"black\" class=\"myClass\" dir=\"myDir\" lang=\"myLang\" " 
      + "title=\"myTitle\">myValue</td></tr></table></td>";
    assertEquals( expected, markup );
  }

  public void testScriptRenderer() throws Exception {
    WebTableCell cell = new WebTableCell();
    cell.setAlign( "center" );
    cell.setBackground( "black" );
    cell.setColspan( "5" );
    cell.setCssClass( "myClass" );
    cell.setDir( "myDir" );
    cell.setHeight( "5" );
    cell.setIgnoreLocalStyle( false );
    cell.setLang( "myLang" );
    cell.setNowrap( true );
    cell.setPadding( "5" );
    cell.setRowspan( "6" );
    cell.setSpacing( "2" );
    cell.setTitle( "myTitle" );
    cell.setVAlign( "vAlign" );
    cell.setValue( "myValue" );
    cell.setWidth( "10" );
    cell.setStyle ( cell.getStyle() );
    HtmlResponseWriter writer = new HtmlResponseWriter();
    Fixture.fakeBrowser( new Ie5up( true, false ) );
    Fixture.setResponseWriter( writer );
    cell.render();
    String markup = Fixture.getBodyMarkup( writer );
    String expected 
      = "<td><table border=\"0\" cellpadding=\"5\" cellspacing=\"2\">"
      + "<tr><td align=\"center\" valign=\"vAlign\" nowrap=\"nowrap\" "
      + "colspan=\"5\" rowspan=\"6\" width=\"10\" height=\"5\" "
      + "background=\"black\" class=\"myClass\" dir=\"myDir\" lang=\"myLang\" " 
      + "title=\"myTitle\">myValue</td></tr></table></td>";
    assertEquals( expected, markup );
  }

  public void testNo_ScriptRenderer() throws Exception {
    WebTableCell cell = new WebTableCell();
    cell.setAlign( "center" );
    cell.setBackground( "black" );
    cell.setColspan( "5" );
    cell.setCssClass( "myClass" );
    cell.setDir( "myDir" );
    cell.setHeight( "5" );
    cell.setIgnoreLocalStyle( false );
    cell.setLang( "myLang" );
    cell.setNowrap( true );
    cell.setPadding( "5" );
    cell.setRowspan( "6" );
    cell.setSpacing( "2" );
    cell.setTitle( "myTitle" );
    cell.setVAlign( "vAlign" );
    cell.setValue( "myValue" );
    cell.setWidth( "10" );
    cell.setStyle ( cell.getStyle() );
    cell.setValue( "myValue" );
    HtmlResponseWriter writer = new HtmlResponseWriter();
    Fixture.fakeBrowser( new Default( false, false ) );
    Fixture.setResponseWriter( writer );
    cell.render();
    String markup = Fixture.getBodyMarkup( writer );
    String expected 
      = "<td><table border=\"0\" cellpadding=\"5\" cellspacing=\"2\">"
      + "<tr><td align=\"center\" valign=\"vAlign\" nowrap "
      + "colspan=\"5\" rowspan=\"6\" width=\"10\" height=\"5\" "
      + "background=\"black\" class=\"myClass\" dir=\"myDir\" lang=\"myLang\" " 
      + "title=\"myTitle\">myValue</td></tr></table></td>";
    assertEquals( expected, markup );
  }
  
  public void testWithSingleNonBreakingSpace() throws Exception {
    WebTableCell cell = new WebTableCell();
    cell.setSpacing( "0" );
    cell.setPadding( "3" );
    cell.setValue( HTML.NBSP );
    cell.setValueEncoded( true );
    HtmlResponseWriter writer = new HtmlResponseWriter();
    Fixture.fakeBrowser( new Default( false, false ) );
    Fixture.setResponseWriter( writer );
    cell.render();
    String markup = Fixture.getBodyMarkup( writer );
    String expected 
      = "<td><table border=\"0\" cellpadding=\"3\" cellspacing=\"0\">" 
      + "<tr><td>&nbsp;</td></tr></table></td>";
    assertEquals( expected, markup );
  }
  
  protected void setUp() throws Exception {
    Fixture.setUp();
    Fixture.createContext();
  }

  protected void tearDown() throws Exception {
    Fixture.tearDown();
    Fixture.removeContext();
  }
}
