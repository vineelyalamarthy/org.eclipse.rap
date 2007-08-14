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

import org.eclipse.rwt.internal.browser.Ie5_5;
import org.eclipse.rwt.internal.service.ContextProvider;
import org.eclipse.rwt.internal.service.IServiceStateInfo;

import junit.framework.TestCase;
import com.w4t.W4TFixture;
import com.w4t.WebLabel;

public class AbsolutePositioner_Test extends TestCase {

  protected void setUp() throws Exception {
    W4TFixture.setUp();
    W4TFixture.createContext();
  }

  protected void tearDown() throws Exception {
    W4TFixture.tearDown();
    W4TFixture.removeContext();
  }

  public void testIe5_5() throws Exception {
    IServiceStateInfo stateInfo = ContextProvider.getStateInfo();
    W4TFixture.fakeBrowser( new Ie5_5( true ) );
    AbsolutePositioner pos = new AbsolutePositioner();
    pos.setID( "pid" );
    pos.setContent( new WebLabel( "CONTENT" ) );
    W4TFixture.fakeResponseWriter();
    pos.render( stateInfo.getResponseWriter() );
    String expected 
      = "<div id=\"pid\" " 
      + "style=\"position:absolute;background-color:white;\">" 
      + "<span id=\"p1\" class=\"w4tCsscd1f6403\">CONTENT</span>"
      + "</div>";
    assertEquals( expected, W4TFixture.getAllMarkup() );
    W4TFixture.fakeResponseWriter();
    AbsoluteConstraint constraint = new AbsoluteConstraint();
    pos.setPosition(constraint );
    pos.render( stateInfo.getResponseWriter() );
    expected 
      = "<div id=\"pid\" " 
      + "style=\"position:absolute;background-color:white;" 
      + "top:0px;left:0px;width:100%;\">" 
      + "<span id=\"p1\" class=\"w4tCsscd1f6403\">CONTENT</span></div>";
    assertEquals( expected, W4TFixture.getAllMarkup() );
  }
}
