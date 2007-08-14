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

import junit.framework.TestCase;
import com.w4t.W4TFixture;
import com.w4t.WebPanel;
import com.w4t.types.WebColor;


public class AbsoluteLayout_Test extends TestCase {
  
  protected void setUp() throws Exception {
    W4TFixture.setUp();
    W4TFixture.createContext();
  }

  protected void tearDown() throws Exception {
    W4TFixture.tearDown();
    W4TFixture.removeContext();
  }

  public void testRender() throws Exception {
    W4TFixture.fakeBrowser( new Ie5_5( true, true ) );
    String expected;
    AbsoluteLayout layout = new AbsoluteLayout();
    WebPanel panel = new WebPanel();
    panel.setWebLayout( layout );
    
    W4TFixture.fakeResponseWriter();
    layout.layoutWebContainer( panel );
    expected 
      = "<table id=\"p1absLyt\" cellpadding=\"0\" cellspacing=\"0\">" 
      + "<tr><td></td><td>" 
      + "<img src=\"resources/images/transparent.gif\" "
      + "align=\"top\" border=\"0\" " 
      + "width=\"\" height=\"1\" /></td></tr><tr><td>" 
      + "<img src=\"resources/images/transparent.gif\" border=\"0\" " 
      + "width=\"1\" height=\"\" /></td><td><div style=\"position:absolute;\">" 
      + "</div></td></tr></table>";
    assertEquals( expected, W4TFixture.getAllMarkup() );
    W4TFixture.fakeResponseWriter();
    layout.setBorder( "22px" );
    layout.layoutWebContainer( panel );
    expected 
      = "<table id=\"p1absLyt\" cellpadding=\"0\" cellspacing=\"0\" " 
      + "border=\"22px\"><tr><td></td><td><img "
      + "src=\"resources/images/transparent.gif\" " 
      + "align=\"top\" border=\"0\" width=\"\" height=\"1\" />" 
      + "</td></tr><tr><td><img "
      + "src=\"resources/images/transparent.gif\" border=\"0\" " 
      + "width=\"1\" height=\"\" /></td><td><div style=\"position:absolute;\">" 
      + "</div></td></tr></table>";
    assertEquals( expected , W4TFixture.getAllMarkup() );
    W4TFixture.fakeResponseWriter();
    layout.setBgColor( new WebColor( "green" ) );
    layout.layoutWebContainer( panel );
    expected 
      = "<table id=\"p1absLyt\" cellpadding=\"0\" cellspacing=\"0\" " 
      + "border=\"22px\"><tr><td></td><td><img "
      + "src=\"resources/images/transparent.gif\" " 
      + "align=\"top\" border=\"0\" width=\"\" height=\"1\" />" 
      + "</td></tr><tr><td><img src=\"resources/images/transparent.gif\" "
      + "border=\"0\" " 
      + "width=\"1\" height=\"\" /></td><td bgcolor=\"#008000\">" 
      + "<div style=\"position:absolute;\"></div></td></tr></table>"; 
    assertEquals( expected , W4TFixture.getAllMarkup() );
  }
}
