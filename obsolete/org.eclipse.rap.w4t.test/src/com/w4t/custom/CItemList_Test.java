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
package com.w4t.custom;

import junit.framework.TestCase;
import com.w4t.Fixture;
import com.w4t.WebLabel;
import com.w4t.engine.service.ContextProvider;
import com.w4t.engine.service.IServiceStateInfo;
import com.w4t.types.WebColor;
import com.w4t.util.browser.Ie5;


public class CItemList_Test extends TestCase {
  
  protected void setUp() throws Exception {
    Fixture.setUp();
    Fixture.createContext();
  }
  
  protected void tearDown() throws Exception {
    Fixture.removeContext();
    Fixture.tearDown();
  }
  
  public void testIe5() throws Exception {
    Fixture.fakeBrowser( new Ie5( true, false ) );
    CItemList list = new CItemList();
    String expected;
    // Test rendering for IE 5 (DOM & XHTMLCapable)
    Fixture.fakeResponseWriter();
    Fixture.renderComponent( list );
    expected = "<ul id=\"p1\"></ul>";
    assertEquals( expected, getAllMarkup() );
    // change type
    list.setType( CItemList.BULLET_TYPE_CIRCLE );
    Fixture.fakeResponseWriter();
    Fixture.renderComponent( list );
    expected = "<ul id=\"p1\" type=\"circle\"></ul>";
    assertEquals( expected, getAllMarkup() );
    // add an 'item'
    list.add( new WebLabel( "äöüß" ) );
    Fixture.fakeResponseWriter();
    Fixture.renderComponent( list );
    expected 
      = "<ul id=\"p1\" type=\"circle\"><li>" 
      + "<span id=\"p3\" class=\"w4tCsscd1f6403\">" 
      + "&auml;&ouml;&uuml;&szlig;</span></li></ul>";
    assertEquals( expected, getAllMarkup() );
    // with style... and title...
    list.setTitle( "äöüß" );
    list.getStyle().setColor( new WebColor( "green" ) );
    Fixture.fakeResponseWriter();
    Fixture.renderComponent( list );
    expected 
      = "<ul id=\"p1\" class=\"w4tCss65ee22f7\" " 
      + "title=\"&auml;&ouml;&uuml;&szlig;\" type=\"circle\"><li>" 
      + "<span id=\"p3\" class=\"w4tCsscd1f6403\">" 
      + "&auml;&ouml;&uuml;&szlig;</span></li></ul>"; 
    assertEquals( expected, getAllMarkup() );
  }
  
  private static String getAllMarkup() {
    IServiceStateInfo stateInfo = ContextProvider.getStateInfo();
    return Fixture.getAllMarkup( stateInfo.getResponseWriter() );
  }
}
