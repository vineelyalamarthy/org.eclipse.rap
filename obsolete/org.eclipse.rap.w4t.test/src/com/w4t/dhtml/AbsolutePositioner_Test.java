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
package com.w4t.dhtml;

import junit.framework.TestCase;
import com.w4t.Fixture;
import com.w4t.WebLabel;
import com.w4t.engine.service.ContextProvider;
import com.w4t.engine.service.IServiceStateInfo;
import com.w4t.util.browser.Ie5_5;

public class AbsolutePositioner_Test extends TestCase {

  protected void setUp() throws Exception {
    Fixture.setUp();
    Fixture.createContext();
  }

  protected void tearDown() throws Exception {
    Fixture.tearDown();
    Fixture.removeContext();
  }

  public void testIe5_5() throws Exception {
    IServiceStateInfo stateInfo = ContextProvider.getStateInfo();
    Fixture.fakeBrowser( new Ie5_5( true ) );
    AbsolutePositioner pos = new AbsolutePositioner();
    pos.setID( "pid" );
    pos.setContent( new WebLabel( "CONTENT" ) );
    Fixture.fakeResponseWriter();
    pos.render( stateInfo.getResponseWriter() );
    String expected 
      = "<div id=\"pid\" " 
      + "style=\"position:absolute;background-color:white;\">" 
      + "<span id=\"p1\" class=\"w4tCsscd1f6403\">CONTENT</span>"
      + "</div>";
    assertEquals( expected, Fixture.getAllMarkup() );
    Fixture.fakeResponseWriter();
    AbsoluteConstraint constraint = new AbsoluteConstraint();
    pos.setPosition(constraint );
    pos.render( stateInfo.getResponseWriter() );
    expected 
      = "<div id=\"pid\" " 
      + "style=\"position:absolute;background-color:white;" 
      + "top:0px;left:0px;width:100%;\">" 
      + "<span id=\"p1\" class=\"w4tCsscd1f6403\">CONTENT</span></div>";
    assertEquals( expected, Fixture.getAllMarkup() );
  }
}
