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

import org.eclipse.rwt.internal.browser.*;
import org.eclipse.rwt.internal.lifecycle.HtmlResponseWriter;
import org.eclipse.rwt.internal.service.ContextProvider;

import com.w4t.*;
import com.w4t.ajax.AjaxStatus;


public class WebFileUploadRenderer_Test extends TestCase {
  
  public void testAjaxRenderer() throws Exception {
    WebFileUpload file = new WebFileUpload();
    WebForm form = W4TFixture.getEmptyWebFormInstance();
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
    W4TFixture.fakeBrowser( new Ie6( true, true ) );
    setResponseWriter( writer );
    W4TFixture.renderComponent( file );
    String markup = W4TFixture.getBodyMarkup( writer );
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
    WebForm form = W4TFixture.getEmptyWebFormInstance();
    WebFileUpload file = new WebFileUpload();
    form.add( file, WebBorderLayout.CENTER );
    file.setDir( "myDir" );
    file.setEnabled( true );
    file.setLang( "myLang" );
    file.setName( "myName" );
    file.setTitle( "myTitle" );
    file.setDesignTime( false );
    HtmlResponseWriter writer = new HtmlResponseWriter();
    W4TFixture.fakeBrowser( new Ie5up( true, false ) );
    setResponseWriter( writer );
    W4TFixture.renderComponent( file );
    String markup = W4TFixture.getBodyMarkup( writer );
    String expected 
      = "<input type=\"file\" name=\"p2\" class=\"w4tCsscd1f6403\" "
      + "dir=\"myDir\" lang=\"myLang\" title=\"myTitle\" "
      + "onfocus=\"eventHandler.setFocusID(this)\" />";
    assertEquals( expected, markup );
  }

  public void testNo_ScriptRenderer() throws Exception {
    WebForm form = W4TFixture.getEmptyWebFormInstance();
    WebFileUpload file = new WebFileUpload();
    form.add( file, WebBorderLayout.CENTER );
    file.setDir( "myDir" );
    file.setEnabled( true );
    file.setLang( "myLang" );
    file.setName( "myName" );
    file.setTitle( "myTitle" );
    file.setDesignTime( true );
    HtmlResponseWriter writer = new HtmlResponseWriter();
    W4TFixture.fakeBrowser( new Default( false, false ) );
    setResponseWriter( writer );
    W4TFixture.renderComponent( file );
    String markup = W4TFixture.getBodyMarkup( writer );
    String expected 
      = "<input type=\"file\" name=\"p2\" class=\"w4tCsscd1f6403\" "
      + "dir=\"myDir\" lang=\"myLang\" title=\"myTitle\">";
    assertEquals( expected, markup );
  }
  
  protected void setUp() throws Exception {
    W4TFixture.setUp();
    W4TFixture.createContext();
  }

  protected void tearDown() throws Exception {
    W4TFixture.tearDown();
    W4TFixture.removeContext();
  }

  private void setResponseWriter( final HtmlResponseWriter writer ) {
    ContextProvider.getStateInfo().setResponseWriter( writer );
  }
}
