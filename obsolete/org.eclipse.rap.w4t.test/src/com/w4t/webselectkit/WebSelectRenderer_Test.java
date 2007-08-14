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
package com.w4t.webselectkit;

import junit.framework.TestCase;

import org.eclipse.rwt.internal.browser.*;
import org.eclipse.rwt.internal.lifecycle.HtmlResponseWriter;
import org.eclipse.rwt.internal.service.ContextProvider;

import com.w4t.*;
import com.w4t.ajax.AjaxStatus;
import com.w4t.ajax.AjaxStatusUtil;
import com.w4t.event.*;
import com.w4t.util.RendererCache;


/** <p>Unit tests for WebSelectRenderer.</p> */
public class WebSelectRenderer_Test extends TestCase {
  
  public void testAjaxRendererOpera() throws Exception {
    WebSelect select = createSelect();
    select.setValue( "Hello World" );
    select.setDir( "myDir" );
    select.setLang("myLang" );
    select.setName( "myName" );
    select.setSize( 10 );
    select.setTitle( "myTitle" );
    AjaxStatus ajaxStatus = ( AjaxStatus )select.getAdapter( AjaxStatus.class );
    ajaxStatus.updateStatus( true );
    W4TFixture.fakeBrowser( new Opera9( true, true ) );
    HtmlResponseWriter writer = new HtmlResponseWriter();
    setResponseWriter( writer );
    W4TFixture.renderComponent( select );
    String markup = W4TFixture.getBodyMarkup( writer );
    String expected;
    expected 
      = "<select id=\"sel1\" name=\"sel1\" "
      + "onfocus=\"eventHandler.setFocusID(this)\" "
      + "size=\"10\" class=\"w4tCsscd1f6403\" "
      + "dir=\"myDir\" "
      + "lang=\"myLang\" "
      + "title=\"myTitle\">"
      + "<option value=\"a\">a</option>"
      + "<option value=\"b\">b</option>"
      + "<option value=\"c\">c</option>"
      + "</select>";
    assertEquals( expected, markup );
    // add WebListener
    select = createSelect();
    select.setValue( "Hello World" );
    select.addWebItemListener( new WebItemListener() {
      public void webItemStateChanged( final WebItemEvent e ) {
      } 
    } );
    select.addWebFocusGainedListener( new WebFocusGainedListener() {
      public void webFocusGained( final WebFocusGainedEvent e ) {
      } 
    } ); 
    ajaxStatus = ( AjaxStatus )select.getAdapter( AjaxStatus.class );
    ajaxStatus.updateStatus( true );
    writer = new HtmlResponseWriter();
    setResponseWriter( writer );
    W4TFixture.renderComponent( select );
    markup = W4TFixture.getBodyMarkup( writer );
    expected 
      = "<select id=\"sel1\" name=\"sel1\" "
      + "onchange=\"eventHandler.webItemStateChanged(this)\" "
      + "onfocus=\"eventHandler.setFocusID(this);"
      + "eventHandler.webFocusGained(this)\" "
      + "onmousedown=\"eventHandler.suspendSubmit()\" "
      + "onmouseup=\"eventHandler.resumeSubmit()\" "
      + "size=\"1\" class=\"w4tCsscd1f6403\">"
      + "<option value=\"a\">a</option>"
      + "<option value=\"b\">b</option>"
      + "<option value=\"c\">c</option>"
      + "</select>";
    assertEquals( expected, markup );

    select.setEnabled( true );
    select.setUpdatable( false );
    writer = new HtmlResponseWriter();
    setResponseWriter( writer );
    W4TFixture.renderComponent( select );
    markup = W4TFixture.getBodyMarkup( writer );
    expected 
      = "<select id=\"sel1\" name=\"sel1\" "
      + "onfocus=\"eventHandler.setFocusID(this);"
      + "eventHandler.webFocusGained(this)\" "
      + "onmousedown=\"eventHandler.suspendSubmit()\" "
      + "onmouseup=\"eventHandler.resumeSubmit()\" "
      + "size=\"1\" class=\"w4tCsscd1f6403\">"
      + "<option value=\"a\">a</option>"
      + "<option value=\"b\">b</option>"
      + "<option value=\"c\">c</option>"
      + "</select>";
    assertEquals( expected, markup );

    select.setEnabled( false );
    select.setUpdatable( true );
    writer = new HtmlResponseWriter();
    setResponseWriter( writer );
    W4TFixture.renderComponent( select );
    markup = W4TFixture.getBodyMarkup( writer );
    expected 
      = "<select id=\"sel1\" name=\"sel1\" "
      + "disabled=\"disabled\" "
      + "size=\"1\" class=\"w4tCsscd1f6403\">"
      + "<option value=\"a\">a</option>"
      + "<option value=\"b\">b</option>"
      + "<option value=\"c\">c</option>"
      + "</select>";
    assertEquals( expected, markup );

    select.setEnabled( false );
    select.setUpdatable( false );
    writer = new HtmlResponseWriter();
    setResponseWriter( writer );
    W4TFixture.renderComponent( select );
    markup = W4TFixture.getBodyMarkup( writer );
    expected 
      = "<select id=\"sel1\" name=\"sel1\" "
      + "disabled=\"disabled\" "
      + "size=\"1\" class=\"w4tCsscd1f6403\">"
      + "<option value=\"a\">a</option>"
      + "<option value=\"b\">b</option>"
      + "<option value=\"c\">c</option>"
      + "</select>";
    assertEquals( expected, markup );
  }

  public void testAjaxRendererMozilla1_6() throws Exception {
    WebSelect select = createSelect();
    select.setValue( "Hello World" );
    select.setDir( "myDir" );
    select.setLang("myLang" );
    select.setName( "myName" );
    select.setSize( 10 );
    select.setTitle( "myTitle" );
    AjaxStatus ajaxStatus = ( AjaxStatus )select.getAdapter( AjaxStatus.class );
    ajaxStatus.updateStatus( true );
    W4TFixture.fakeBrowser( new Mozilla1_6( true, true ) );
    HtmlResponseWriter writer = new HtmlResponseWriter();
    setResponseWriter( writer );
    W4TFixture.renderComponent( select );
    String markup = W4TFixture.getBodyMarkup( writer );
    String expected;
    expected 
      = "<select id=\"sel1\" name=\"sel1\" "
      + "onfocus=\"eventHandler.setFocusID(this)\" "
      + "size=\"10\" class=\"w4tCsscd1f6403\" "
      + "dir=\"myDir\" "
      + "lang=\"myLang\" "
      + "title=\"myTitle\">"
      + "<option value=\"a\">a</option>"
      + "<option value=\"b\">b</option>"
      + "<option value=\"c\">c</option>"
      + "</select>";
    assertEquals( expected, markup );
    // add WebListener
    select = createSelect();
    select.setValue( "Hello World" );
    select.addWebItemListener( new WebItemListener() {
      public void webItemStateChanged( final WebItemEvent e ) {
      } 
    } );
    select.addWebFocusGainedListener( new WebFocusGainedListener() {
      public void webFocusGained( final WebFocusGainedEvent e ) {
      } 
    } ); 
    ajaxStatus = ( AjaxStatus )select.getAdapter( AjaxStatus.class );
    ajaxStatus.updateStatus( true );
    writer = new HtmlResponseWriter();
    setResponseWriter( writer );
    W4TFixture.renderComponent( select );
    markup = W4TFixture.getBodyMarkup( writer );
    expected 
      = "<select id=\"sel1\" name=\"sel1\" " 
      + "onchange=\"eventHandler.webItemStateChanged(this)\" "
      + "onfocus=\"eventHandler.setFocusID(this);" 
      + "eventHandler.webFocusGained(this)\" " 
      + "onmousedown=\"eventHandler.suspendSubmit()\" " 
      + "onmouseup=\"eventHandler.resumeSubmit()\" " 
      + "size=\"1\" class=\"w4tCsscd1f6403\">" 
      + "<option value=\"a\">a</option>" 
      + "<option value=\"b\">b</option>" 
      + "<option value=\"c\">c</option>" 
      + "</select>";
    assertEquals( expected, markup );

    select.setEnabled( true );
    select.setUpdatable( false );
    writer = new HtmlResponseWriter();
    setResponseWriter( writer );
    W4TFixture.renderComponent( select );
    markup = W4TFixture.getBodyMarkup( writer );
    expected 
      = "<select id=\"sel1\" name=\"sel1\" "
      + "onfocus=\"eventHandler.setFocusID(this);"
      + "eventHandler.webFocusGained(this)\" "
      + "onmousedown=\"eventHandler.suspendSubmit()\" "
      + "onmouseup=\"eventHandler.resumeSubmit()\" "
      + "size=\"1\" class=\"w4tCsscd1f6403\">"
      + "<option value=\"a\">a</option>"
      + "<option value=\"b\">b</option>"
      + "<option value=\"c\">c</option>"
      + "</select>";
    assertEquals( expected, markup );

    select.setEnabled( false );
    select.setUpdatable( true );
    writer = new HtmlResponseWriter();
    setResponseWriter( writer );
    W4TFixture.renderComponent( select );
    markup = W4TFixture.getBodyMarkup( writer );
    expected 
      = "<select id=\"sel1\" name=\"sel1\" "
      + "disabled=\"disabled\" "
      + "size=\"1\" class=\"w4tCsscd1f6403\">"
      + "<option value=\"a\">a</option>"
      + "<option value=\"b\">b</option>"
      + "<option value=\"c\">c</option>"
      + "</select>";
    assertEquals( expected, markup );

    select.setEnabled( false );
    select.setUpdatable( false );
    writer = new HtmlResponseWriter();
    setResponseWriter( writer );
    W4TFixture.renderComponent( select );
    markup = W4TFixture.getBodyMarkup( writer );
    expected 
      = "<select id=\"sel1\" name=\"sel1\" "
      + "disabled=\"disabled\" "
      + "size=\"1\" class=\"w4tCsscd1f6403\">"
      + "<option value=\"a\">a</option>"
      + "<option value=\"b\">b</option>"
      + "<option value=\"c\">c</option>"
      + "</select>";
    assertEquals( expected, markup );
    
  }

  public void testAjaxRendererIE5up() throws Exception {
    WebSelect select = createSelect();
    select.setDir( "myDir" );
    select.setLang("myLang" );
    select.setName( "myName" );
    select.setSize( 10 );
    select.setTitle( "myTitle" );
    select.addItem( "ü" );
    select.setValue( "ü" );
    AjaxStatus ajaxStatus = ( AjaxStatus )select.getAdapter( AjaxStatus.class );
    ajaxStatus.updateStatus( true );
    W4TFixture.fakeBrowser( new Ie5up( true, true ) );
    HtmlResponseWriter writer = new HtmlResponseWriter();
    setResponseWriter( writer );
    W4TFixture.renderComponent( select );
    String markup = W4TFixture.getBodyMarkup( writer );
    String expected;
    expected 
      = "<select id=\"sel1\" name=\"sel1\" "
      + "onfocus=\"eventHandler.setFocusID(this)\" "
      + "size=\"10\" class=\"w4tCsscd1f6403\" "
      + "dir=\"myDir\" "
      + "lang=\"myLang\" "
      + "title=\"myTitle\">"
      + "<option value=\"a\">a</option>"
      + "<option value=\"b\">b</option>"
      + "<option value=\"c\">c</option>"
      + "<option value=\"&uuml;\" selected=\"selected\">&uuml;</option>"
      + "</select>";
    assertEquals( expected, markup );
    // add WebListener
    select = createSelect();
    select.setValue( "Hello World" );
    select.addWebItemListener( new WebItemListener() {
      public void webItemStateChanged( final WebItemEvent e ) {
      } 
    } );
    select.addWebFocusGainedListener( new WebFocusGainedListener() {
      public void webFocusGained( final WebFocusGainedEvent e ) {
      } 
    } ); 
    ajaxStatus = ( AjaxStatus )select.getAdapter( AjaxStatus.class );
    ajaxStatus.updateStatus( true );
    writer = new HtmlResponseWriter();
    setResponseWriter( writer );
    W4TFixture.renderComponent( select );
    markup = W4TFixture.getBodyMarkup( writer );
    expected 
      = "<select id=\"sel1\" name=\"sel1\" "
      + "onchange=\"eventHandler.webItemStateChanged(this)\" "
      + "onfocus=\"eventHandler.setFocusID(this);"
      + "eventHandler.webFocusGained(this)\" "
      + "onmousedown=\"eventHandler.suspendSubmit()\" "
      + "onmouseup=\"eventHandler.resumeSubmit()\" "
      + "size=\"1\" class=\"w4tCsscd1f6403\">"
      + "<option value=\"a\">a</option>"
      + "<option value=\"b\">b</option>"
      + "<option value=\"c\">c</option>"
      + "</select>";
    assertEquals( expected, markup );
    
    select.setEnabled( true );
    select.setUpdatable( false );
    writer = new HtmlResponseWriter();
    setResponseWriter( writer );
    W4TFixture.renderComponent( select );
    markup = W4TFixture.getBodyMarkup( writer );
    expected 
      = "<select id=\"sel1\" name=\"sel1\" "
//      + "onchange=\"eventHandler.webItemStateChanged(this)\" "
      + "onfocus=\"eventHandler.setFocusID(this);"
      + "eventHandler.webFocusGained(this)\" "
      + "onmousedown=\"eventHandler.suspendSubmit()\" "
      + "onmouseup=\"eventHandler.resumeSubmit()\" "
      + "size=\"1\" class=\"w4tCsscd1f6403\">"
      + "<option value=\"a\">a</option>"
      + "<option value=\"b\">b</option>"
      + "<option value=\"c\">c</option>"
      + "</select>";
    assertEquals( expected, markup );

    select.setEnabled( false );
    select.setUpdatable( true );
    writer = new HtmlResponseWriter();
    setResponseWriter( writer );
    W4TFixture.renderComponent( select );
    markup = W4TFixture.getBodyMarkup( writer );
    expected 
      = "<select id=\"sel1\" name=\"sel1\" "
      + "disabled=\"disabled\" "
      + "size=\"1\" class=\"w4tCsscd1f6403\">"
      + "<option value=\"a\">a</option>"
      + "<option value=\"b\">b</option>"
      + "<option value=\"c\">c</option>"
      + "</select>";
    assertEquals( expected, markup );
    
    select.setEnabled( false );
    select.setUpdatable( false );
    writer = new HtmlResponseWriter();
    setResponseWriter( writer );
    W4TFixture.renderComponent( select );
    markup = W4TFixture.getBodyMarkup( writer );
    expected 
      = "<select id=\"sel1\" name=\"sel1\" "
      + "disabled=\"disabled\" "
      + "size=\"1\" class=\"w4tCsscd1f6403\">"
      + "<option value=\"a\">a</option>"
      + "<option value=\"b\">b</option>"
      + "<option value=\"c\">c</option>"
      + "</select>";
    assertEquals( expected, markup );
  }
  
  public void testScriptRenderer() throws Exception {
    WebSelect select = createSelect();
    select.setValue( "Hello World" );
    select.setDir( "myDir" );
    select.setLang("myLang" );
    select.setName( "myName" );
    select.setSize( 10 );
    select.setTitle( "myTitle" );
    W4TFixture.fakeBrowser( new Default( true, false ) );
    HtmlResponseWriter writer = new HtmlResponseWriter();
    setResponseWriter( writer );
    W4TFixture.renderComponent( select );
    String markup = W4TFixture.getBodyMarkup( writer );
    String expected;
    expected   = "<select name=\"sel1\" "
               + "onfocus=\"eventHandler.setFocusID(this)\" size=\"10\" "
               + "class=\"w4tCsscd1f6403\" "
               + "dir=\"myDir\" "
               + "lang=\"myLang\" "
               + "title=\"myTitle\">"
               + "<option value=\"a\">a</option>"
               + "<option value=\"b\">b</option>"
               + "<option value=\"c\">c</option>"
               + "</select>";
    assertEquals( expected, markup );
    // 
    select.setUseEmptyItem( true );
    writer = new HtmlResponseWriter();
    setResponseWriter( writer );
    W4TFixture.renderComponent( select );
    markup = W4TFixture.getBodyMarkup( writer );
    expected   = "<select name=\"sel1\" "
               + "onfocus=\"eventHandler.setFocusID(this)\" size=\"10\" "
               + "class=\"w4tCsscd1f6403\" " 
               + "dir=\"myDir\" "
               + "lang=\"myLang\" "
               + "title=\"myTitle\">"
               + "<option></option>"
               + "<option value=\"a\">a</option>"
               + "<option value=\"b\">b</option>"
               + "<option value=\"c\">c</option>"
               + "</select>";
    assertEquals( expected, markup );
    // 
    select.setUseEmptyItem( false );
    select.setValue( "a" );
    writer = new HtmlResponseWriter();
    setResponseWriter( writer );
    W4TFixture.renderComponent( select );
    markup = W4TFixture.getBodyMarkup( writer );
    expected
      = "<select name=\"sel1\" "
      + "onfocus=\"eventHandler.setFocusID(this)\" size=\"10\" "
      + "class=\"w4tCsscd1f6403\" "
      + "dir=\"myDir\" "
      + "lang=\"myLang\" "
      + "title=\"myTitle\">"
      + "<option value=\"a\" selected>a</option>"
      + "<option value=\"b\">b</option>"
      + "<option value=\"c\">c</option>"
      + "</select>";
    assertEquals( expected, markup );
    //
    select.setUseEmptyItem( false );
    select.addItem( "ü" );
    select.setValue( "ü");
    writer = new HtmlResponseWriter();
    setResponseWriter( writer );
    W4TFixture.renderComponent( select );
    markup = W4TFixture.getBodyMarkup( writer );
    expected
      = "<select name=\"sel1\" "
      + "onfocus=\"eventHandler.setFocusID(this)\" size=\"10\" "
      + "class=\"w4tCsscd1f6403\" "
      + "dir=\"myDir\" "
      + "lang=\"myLang\" "
      + "title=\"myTitle\">"
      + "<option value=\"a\">a</option>"
      + "<option value=\"b\">b</option>"
      + "<option value=\"c\">c</option>"
      + "<option value=\"&uuml;\" selected>&uuml;</option>" 
      + "</select>";
    assertEquals( expected, markup );
    
    select.setEnabled( true );
    select.setUpdatable( false );
    writer = new HtmlResponseWriter();
    setResponseWriter( writer );
    W4TFixture.renderComponent( select );
    markup = W4TFixture.getBodyMarkup( writer );
    expected 
      = "<select name=\"sel1\" "
      + "onfocus=\"eventHandler.setFocusID(this)\" size=\"10\" "
      + "class=\"w4tCsscd1f6403\" "
      + "dir=\"myDir\" "
      + "lang=\"myLang\" "
      + "title=\"myTitle\">"
      + "<option value=\"a\">a</option>"
      + "<option value=\"b\">b</option>"
      + "<option value=\"c\">c</option>"
      + "<option value=\"&uuml;\" selected>&uuml;</option>" 
      + "</select>";
    assertEquals( expected, markup );

    select.setEnabled( false );
    select.setUpdatable( true );
    writer = new HtmlResponseWriter();
    setResponseWriter( writer );
    W4TFixture.renderComponent( select );
    markup = W4TFixture.getBodyMarkup( writer );
    expected 
      = "<select name=\"sel1\" "
      + "disabled " + "size=\"10\" "
      + "class=\"w4tCsscd1f6403\" "
      + "dir=\"myDir\" "
      + "lang=\"myLang\" "
      + "title=\"myTitle\">"
      + "<option value=\"a\">a</option>"
      + "<option value=\"b\">b</option>"
      + "<option value=\"c\">c</option>"
      + "<option value=\"&uuml;\" selected>&uuml;</option>" 
      + "</select>";
    assertEquals( expected, markup );
    
    select.setEnabled( false );
    select.setUpdatable( false );
    writer = new HtmlResponseWriter();
    setResponseWriter( writer );
    W4TFixture.renderComponent( select );
    markup = W4TFixture.getBodyMarkup( writer );
    expected 
      = "<select name=\"sel1\" "
      + "disabled size=\"10\" "
      + "class=\"w4tCsscd1f6403\" "
      + "dir=\"myDir\" "
      + "lang=\"myLang\" "
      + "title=\"myTitle\">"
      + "<option value=\"a\">a</option>"
      + "<option value=\"b\">b</option>"
      + "<option value=\"c\">c</option>"
      + "<option value=\"&uuml;\" selected>&uuml;</option>" 
      + "</select>";
    assertEquals( expected, markup );
  }

  public void testNoScriptRenderer() throws Exception {
    WebSelect select = createSelect();
    select.setDir( "myDir" );
    select.setLang("myLang" );
    select.setName( "myName" );
    select.setSize( 10 );
    select.setTitle( "myTitle" );
    select.addWebItemListener( new WebItemListener() {
      public void webItemStateChanged( final WebItemEvent e ) {
      } 
    } );
    select.addWebFocusGainedListener( new WebFocusGainedListener() {
      public void webFocusGained( final WebFocusGainedEvent e ) {
      } 
    } ); 
    HtmlResponseWriter writer = new HtmlResponseWriter();
    W4TFixture.fakeBrowser( new Default( false, false ) );
    setResponseWriter( writer );
    W4TFixture.renderComponent( select );
    String markup = W4TFixture.getBodyMarkup( writer );
    String expected 
      = "<select name=\"sel1\" size=\"10\" class=\"w4tCsscd1f6403\" "
      + "dir=\"myDir\" "
      + "lang=\"myLang\" "
      + "title=\"myTitle\">"
      + "<option value=\"a\">a</option>"
      + "<option value=\"b\">b</option>"
      + "<option value=\"c\">c</option>"
      + "</select>"
      + "<input type=\"image\" src=\"resources/images/submitter.gif\" "
      + "name=\"wiesel1\" border=\"0\">";
    assertEquals( expected, markup );
  }

  public void testReadData_Default_Ajax() throws Exception {
    W4TFixture.fakeBrowser( new Ie6( true, true ) );
    WebText text = new WebText();
    W4TFixture.setWebComponentUniqueId( text, "select1" );
    W4TFixture.fakeRequestParam( text.getUniqueID(), "selection1" );
    RendererCache rendererCache = RendererCache.getInstance();
    Renderer renderer = rendererCache.retrieveRenderer( text.getClass() );
    renderer.readData( text );
    assertEquals( "selection1", text.getValue() );
  }
  
  public void testEncoding() throws Exception {
    WebSelect select = createSelect();
    select.addItem( "Lüßter \"&\" Möller" );
    select.setTitle( "Lüßter \"&\" Möller" );
    HtmlResponseWriter writer = new HtmlResponseWriter();
    W4TFixture.fakeBrowser( new Default( true, false ) );
    setResponseWriter( writer );
    W4TFixture.renderComponent( select );
    String markup = W4TFixture.getBodyMarkup( writer );
    String expected 
      = "<select name=\"sel1\" onfocus=\"eventHandler.setFocusID(this)\" " 
      + "size=\"1\" class=\"w4tCsscd1f6403\" " 
      + "title=\"L&uuml;&szlig;ter &quot;&amp;&quot; M&ouml;ller\">" 
      + "<option value=\"a\">a</option>" 
      + "<option value=\"b\">b</option>" 
      + "<option value=\"c\">c</option>" 
      + "<option value=\"L&uuml;&szlig;ter &quot;&amp;&quot; M&ouml;ller\">" 
      + "L&uuml;&szlig;ter &quot;&amp;&quot; M&ouml;ller</option>" 
      + "</select>"; 
    assertEquals( expected, markup );
  }
  
  public void testAjaxStatusAfterReadData_Ie() {
    WebForm form = W4TFixture.getEmptyWebFormInstance();
    WebSelect select = new WebSelect();
    select.addItem( "1" );
    select.addItem( "2" );
    form.add( select, WebBorderLayout.CENTER );
    select.setValue( "1" );
    W4TFixture.fakeRequestParam( select.getUniqueID(), "2" );
    W4TFixture.fakeBrowser( new Ie6( true, true ) );
    RendererCache rendererCache = RendererCache.getInstance();
    AjaxStatusUtil.preRender( form );
    AjaxStatusUtil.postRender( form );
    Renderer renderer = rendererCache.retrieveRenderer( select.getClass() );
    assertEquals( WebSelectRenderer_Default_Ajax.class, renderer.getClass() );
    renderer.readData( select );
    assertEquals( "2", select.getValue() );
    AjaxStatusUtil.preRender( form );
    assertEquals( false, W4TFixture.getAjaxStatus( select ).mustRender() );
  }
  
  public void testAjaxStatusAfterReadData_Mozilla() {
    WebForm form = W4TFixture.getEmptyWebFormInstance();
    WebSelect select = new WebSelect();
    select.addItem( "1" );
    select.addItem( "2" );
    form.add( select, WebBorderLayout.CENTER );
    select.setValue( "1" );
    W4TFixture.fakeRequestParam( select.getUniqueID(), "2" );
    W4TFixture.fakeBrowser( new Mozilla1_6( true, true ) );
    RendererCache rendererCache = RendererCache.getInstance();
    AjaxStatusUtil.preRender( form );
    AjaxStatusUtil.postRender( form );
    Renderer renderer = rendererCache.retrieveRenderer( select.getClass() );
    assertEquals( WebSelectRenderer_Mozilla1_6up_Ajax.class, renderer.getClass() );
    renderer.readData( select );
    assertEquals( "2", select.getValue() );
    AjaxStatusUtil.preRender( form );
    assertEquals( false, W4TFixture.getAjaxStatus( select ).mustRender() );
  }
  
  private static WebSelect createSelect() throws Exception {
    WebSelect select = new WebSelect();
    select.addItem( "a" );
    select.addItem( "b" );
    select.addItem( "c" );
    select.setValue( "Hello World" );
    W4TFixture.setWebComponentUniqueId( select, "sel1" );
    return select;
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
