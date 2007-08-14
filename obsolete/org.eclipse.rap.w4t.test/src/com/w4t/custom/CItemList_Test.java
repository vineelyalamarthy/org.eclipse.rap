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

import org.eclipse.rwt.internal.browser.Ie5;
import org.eclipse.rwt.internal.service.ContextProvider;
import org.eclipse.rwt.internal.service.IServiceStateInfo;

import junit.framework.TestCase;
import com.w4t.W4TFixture;
import com.w4t.WebLabel;
import com.w4t.types.WebColor;


public class CItemList_Test extends TestCase {
  
  protected void setUp() throws Exception {
    W4TFixture.setUp();
    W4TFixture.createContext();
  }
  
  protected void tearDown() throws Exception {
    W4TFixture.removeContext();
    W4TFixture.tearDown();
  }
  
  public void testIe5() throws Exception {
    W4TFixture.fakeBrowser( new Ie5( true, false ) );
    CItemList list = new CItemList();
    String expected;
    // Test rendering for IE 5 (DOM & XHTMLCapable)
    W4TFixture.fakeResponseWriter();
    W4TFixture.renderComponent( list );
    expected = "<ul id=\"p1\"></ul>";
    assertEquals( expected, getAllMarkup() );
    // change type
    list.setType( CItemList.BULLET_TYPE_CIRCLE );
    W4TFixture.fakeResponseWriter();
    W4TFixture.renderComponent( list );
    expected = "<ul id=\"p1\" type=\"circle\"></ul>";
    assertEquals( expected, getAllMarkup() );
    // add an 'item'
    list.add( new WebLabel( "äöüß" ) );
    W4TFixture.fakeResponseWriter();
    W4TFixture.renderComponent( list );
    expected 
      = "<ul id=\"p1\" type=\"circle\"><li>" 
      + "<span id=\"p3\" class=\"w4tCsscd1f6403\">" 
      + "&auml;&ouml;&uuml;&szlig;</span></li></ul>";
    assertEquals( expected, getAllMarkup() );
    // with style... and title...
    list.setTitle( "äöüß" );
    list.getStyle().setColor( new WebColor( "green" ) );
    W4TFixture.fakeResponseWriter();
    W4TFixture.renderComponent( list );
    expected 
      = "<ul id=\"p1\" class=\"w4tCss65ee22f7\" " 
      + "title=\"&auml;&ouml;&uuml;&szlig;\" type=\"circle\"><li>" 
      + "<span id=\"p3\" class=\"w4tCsscd1f6403\">" 
      + "&auml;&ouml;&uuml;&szlig;</span></li></ul>"; 
    assertEquals( expected, getAllMarkup() );
  }
  
  private static String getAllMarkup() {
    IServiceStateInfo stateInfo = ContextProvider.getStateInfo();
    return W4TFixture.getAllMarkup( stateInfo.getResponseWriter() );
  }
}
