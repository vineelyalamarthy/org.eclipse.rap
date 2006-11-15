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
package com.w4t.webfileuploadkit;

import junit.framework.TestCase;
import com.w4t.*;
import com.w4t.ajax.AjaxStatus;
import com.w4t.engine.service.ContextProvider;
import com.w4t.util.browser.*;


public class WebFileUploadRenderer_Test extends TestCase {
  
  public void testAjaxRenderer() throws Exception {
    WebFileUpload file = new WebFileUpload();
    WebForm form = Fixture.getEmptyWebFormInstance();
    form.add( file, WebBorderLayout.CENTER );
    file.setDir( "myDir" );
    file.setEnabled( true );
    file.setLang( "myLang" );
    file.setName( "myName" );
    file.setTitle( "myTitle" );
    file.setDesignTime( true );
    HtmlResponseWriter writer = new HtmlResponseWriter();
    AjaxStatus ajaxStatus = ( AjaxStatus )file.getAdapter( AjaxStatus.class );
    ajaxStatus.updateStatus( true );
    Fixture.fakeBrowser( new Ie6( true, true ) );
    setResponseWriter( writer );
    Fixture.renderComponent( file );
    String markup = Fixture.getBodyMarkup( writer );
    String expected 
      = "<envelope id=\"p1\">"
      + "<!--<input type=\"file\" name=\"p1\" class=\"w4tCsscd1f6403\" "
      + "dir=\"myDir\" lang=\"myLang\" title=\"myTitle\" "
      + "onfocus=\"eventHandler.setFocusID(this);" 
        + "eventHandler.webFocusGained(this)\" />"
      + "--></envelope>";
    assertEquals( expected, markup );
  }

  public void testScriptRenderer() throws Exception {
    WebForm form = Fixture.getEmptyWebFormInstance();
    WebFileUpload file = new WebFileUpload();
    form.add( file, WebBorderLayout.CENTER );
    file.setDir( "myDir" );
    file.setEnabled( true );
    file.setLang( "myLang" );
    file.setName( "myName" );
    file.setTitle( "myTitle" );
    file.setDesignTime( false );
    HtmlResponseWriter writer = new HtmlResponseWriter();
    Fixture.fakeBrowser( new Ie5up( true, false ) );
    setResponseWriter( writer );
    Fixture.renderComponent( file );
    String markup = Fixture.getBodyMarkup( writer );
    String expected 
      = "<input type=\"file\" name=\"p2\" class=\"w4tCsscd1f6403\" "
      + "dir=\"myDir\" lang=\"myLang\" title=\"myTitle\" "
      + "onfocus=\"eventHandler.setFocusID(this)\" />";
    assertEquals( expected, markup );
  }

  public void testNo_ScriptRenderer() throws Exception {
    WebForm form = Fixture.getEmptyWebFormInstance();
    WebFileUpload file = new WebFileUpload();
    form.add( file, WebBorderLayout.CENTER );
    file.setDir( "myDir" );
    file.setEnabled( true );
    file.setLang( "myLang" );
    file.setName( "myName" );
    file.setTitle( "myTitle" );
    file.setDesignTime( true );
    HtmlResponseWriter writer = new HtmlResponseWriter();
    Fixture.fakeBrowser( new Default( false, false ) );
    setResponseWriter( writer );
    Fixture.renderComponent( file );
    String markup = Fixture.getBodyMarkup( writer );
    String expected 
      = "<input type=\"file\" name=\"p2\" class=\"w4tCsscd1f6403\" "
      + "dir=\"myDir\" lang=\"myLang\" title=\"myTitle\">";
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

  private void setResponseWriter( final HtmlResponseWriter writer ) {
    ContextProvider.getStateInfo().setResponseWriter( writer );
  }
}
