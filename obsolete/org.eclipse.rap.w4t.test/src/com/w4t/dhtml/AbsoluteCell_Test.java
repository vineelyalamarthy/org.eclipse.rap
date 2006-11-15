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

import java.lang.reflect.Method;
import junit.framework.TestCase;
import com.w4t.Fixture;
import com.w4t.HtmlResponseWriter;
import com.w4t.engine.service.ContextProvider;
import com.w4t.engine.service.IServiceStateInfo;
import com.w4t.types.WebColor;
import com.w4t.util.browser.Ie5_5;


public class AbsoluteCell_Test extends TestCase {
  
  protected void setUp() throws Exception {
    Fixture.setUp();
    Fixture.createContext();
  }
  
  protected void tearDown() throws Exception {
    Fixture.tearDown();
    Fixture.removeContext();
  }
  
  public void testRender() throws Exception {
    Fixture.fakeBrowser( new Ie5_5( true ) );
    AbsoluteCell cell = new AbsoluteCell();
    Fixture.fakeResponseWriter();
    callCreateRenderContent( cell );
    String allMarkup = Fixture.getAllMarkup();
    String expected 
      = "<table cellspacing=\"0\" cellpadding=\"0\" bgcolor=\"\">" 
      + "<tr><td><img src=\"resources/images/transparent.gif\" width=\"0\" " 
      + "height=\"0\" /></td></tr></table>";
    assertEquals( expected, allMarkup );
    cell.setColor( new WebColor( "green" ) );
    Fixture.fakeResponseWriter();
    callCreateRenderContent( cell );
    allMarkup = Fixture.getAllMarkup();
    expected 
      = "<table cellspacing=\"0\" cellpadding=\"0\" bgcolor=\"#008000\">" 
      + "<tr><td><img src=\"resources/images/transparent.gif\" width=\"0\" " 
      + "height=\"0\" /></td></tr></table>"; 
    assertEquals( expected, allMarkup );
    cell.setBorder( "3" );
    Fixture.fakeResponseWriter();
    callCreateRenderContent( cell );
    allMarkup = Fixture.getAllMarkup();
    expected 
      = "<table cellspacing=\"0\" cellpadding=\"0\" border=\"3\" " 
      + "bgcolor=\"#008000\">" 
      + "<tr><td><img src=\"resources/images/transparent.gif\" width=\"0\" " 
      + "height=\"0\" /></td></tr></table>"; 
    assertEquals( expected, allMarkup );
  }
  
  private void callCreateRenderContent( final AbsoluteCell cell ) 
    throws Exception 
  {
    Class[] args = new Class[] { HtmlResponseWriter.class };
    Method method = cell.getClass().getDeclaredMethod( "createRenderContent", 
                                                       args );
    method.setAccessible( true );
    IServiceStateInfo stateInfo = ContextProvider.getStateInfo();
    HtmlResponseWriter writer = stateInfo.getResponseWriter();
    method.invoke( cell, new Object[] { writer } );
  }
}
