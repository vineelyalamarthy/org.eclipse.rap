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
package com.w4t.webtablerowkit;

import junit.framework.TestCase;
import com.w4t.*;
import com.w4t.util.browser.Default;
import com.w4t.util.browser.Ie5up;

/** <p>Unit tests for WebRadioButtonRenderer.</p> */
public class WebTableRowRenderer_Test extends TestCase {
  
  public void testAjaxRenderer() throws Exception {
    WebTableRow row = new WebTableRow();
    row.setCssClass( "myClass" );
    row.setDir( "myDir" );
    row.setIgnoreLocalStyle( false );
    row.setLang( "myLang" );
    row.setTitle( "myTitle" );
    HtmlResponseWriter writer = new HtmlResponseWriter();
    Fixture.fakeBrowser( new Default( true, true ) );
    Fixture.setResponseWriter( writer );
    row.render();
    String markup = Fixture.getBodyMarkup( writer );
    String expected 
      = "<tr class=\"myClass\" dir=\"myDir\" lang=\"myLang\" " 
      + "title=\"myTitle\"></tr>";
    assertEquals( expected, markup );
  }

  public void testScriptRenderer() throws Exception {
    WebTableRow row = new WebTableRow();
    row.setCssClass( "myClass" );
    row.setDir( "myDir" );
    row.setIgnoreLocalStyle( false );
    row.setLang( "myLang" );
    row.setTitle( "myTitle" );
    row.setStyle ( row.getStyle() );
    HtmlResponseWriter writer = new HtmlResponseWriter();
    Fixture.fakeBrowser( new Ie5up( true, false ) );
    Fixture.setResponseWriter( writer );
    row.render();
    String markup = Fixture.getBodyMarkup( writer );
    String expected 
    = "<tr class=\"myClass\" dir=\"myDir\" lang=\"myLang\" " 
      + "title=\"myTitle\"></tr>";
    assertEquals( expected, markup );
  }

  public void testNo_ScriptRenderer() throws Exception {
    WebTableRow row = new WebTableRow();
    row.setCssClass( "myClass" );
    row.setDir( "myDir" );
    row.setIgnoreLocalStyle( false );
    row.setLang( "myLang" );
    row.setTitle( "myTitle" );
    row.setStyle ( row.getStyle() );
    HtmlResponseWriter writer = new HtmlResponseWriter();
    Fixture.fakeBrowser( new Default( false, false ) );
    Fixture.setResponseWriter( writer );
    row.render();
    String markup = Fixture.getBodyMarkup( writer );
    String expected 
    = "<tr class=\"myClass\" dir=\"myDir\" lang=\"myLang\" " 
      + "title=\"myTitle\"></tr>";
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
