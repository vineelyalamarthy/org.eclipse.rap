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

import org.eclipse.rwt.internal.browser.Ie5_5;
import org.eclipse.rwt.internal.lifecycle.HtmlResponseWriter;
import org.eclipse.rwt.internal.service.ContextProvider;
import org.eclipse.rwt.internal.service.IServiceStateInfo;

import com.w4t.W4TFixture;
import com.w4t.types.WebColor;


public class AbsoluteCell_Test extends TestCase {
  
  protected void setUp() throws Exception {
    W4TFixture.setUp();
    W4TFixture.createContext();
  }
  
  protected void tearDown() throws Exception {
    W4TFixture.tearDown();
    W4TFixture.removeContext();
  }
  
  public void testRender() throws Exception {
    W4TFixture.fakeBrowser( new Ie5_5( true ) );
    AbsoluteCell cell = new AbsoluteCell();
    W4TFixture.fakeResponseWriter();
    callCreateRenderContent( cell );
    String allMarkup = W4TFixture.getAllMarkup();
    String expected 
      = "<table cellspacing=\"0\" cellpadding=\"0\" bgcolor=\"\">" 
      + "<tr><td><img src=\"resources/images/transparent.gif\" width=\"0\" " 
      + "height=\"0\" /></td></tr></table>";
    assertEquals( expected, allMarkup );
    cell.setColor( new WebColor( "green" ) );
    W4TFixture.fakeResponseWriter();
    callCreateRenderContent( cell );
    allMarkup = W4TFixture.getAllMarkup();
    expected 
      = "<table cellspacing=\"0\" cellpadding=\"0\" bgcolor=\"#008000\">" 
      + "<tr><td><img src=\"resources/images/transparent.gif\" width=\"0\" " 
      + "height=\"0\" /></td></tr></table>"; 
    assertEquals( expected, allMarkup );
    cell.setBorder( "3" );
    W4TFixture.fakeResponseWriter();
    callCreateRenderContent( cell );
    allMarkup = W4TFixture.getAllMarkup();
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
