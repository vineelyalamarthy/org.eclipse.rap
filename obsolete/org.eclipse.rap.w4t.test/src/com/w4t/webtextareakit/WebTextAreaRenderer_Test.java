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
package com.w4t.webtextareakit;

import junit.framework.TestCase;

import org.eclipse.rwt.internal.browser.*;
import org.eclipse.rwt.internal.lifecycle.HtmlResponseWriter;
import org.eclipse.rwt.internal.service.ContextProvider;

import com.w4t.*;
import com.w4t.ajax.AjaxStatus;
import com.w4t.ajax.AjaxStatusUtil;
import com.w4t.event.*;
import com.w4t.util.RendererCache;


/**
 * <p>
 * Unit tests for WebTextAreaRenderer.
 * </p>
 */
public class WebTextAreaRenderer_Test extends TestCase {
  
  public void testAjaxRenderer() throws Exception {
    WebTextArea text = new WebTextArea();
    text.addWebItemListener( new WebItemListener() {
      public void webItemStateChanged( final WebItemEvent e ) {
      } 
    } );
    text.addWebFocusGainedListener( new WebFocusGainedListener() {
      public void webFocusGained( final WebFocusGainedEvent e ) {
      } 
    } );    
    text.setValue( "Hello World" );
    text.setCols( 20 );
    text.setRows( 20 );
    text.setDir( "myDirectory" );
    text.setLang( "myLang" );
    text.setName( "myName" );
    text.setTitle( "myTitle" );
    text.setEnabled( false );
    text.setUpdatable( false );
    W4TFixture.setWebComponentUniqueId( text, "text1" );
    AjaxStatus ajaxStatus = ( AjaxStatus )text.getAdapter( AjaxStatus.class );
    ajaxStatus.updateStatus( true );
    W4TFixture.fakeBrowser( new Default( true, true ) );
    HtmlResponseWriter writer = new HtmlResponseWriter();
    setResponseWriter( writer );
    W4TFixture.renderComponent( text );
    String markup = getBodyMarkup( writer );
    String expected;
    expected = "<textarea "
             + "id=\"text1\" "
             + "name=\"text1\" "
             + "rows=\"20\" cols=\"20\" "
             + "wrap=\"virtual\" "
             + "class=\"w4tCsscd1f6403\" "
             + "dir=\"myDirectory\" "
             + "lang=\"myLang\" "
             + "title=\"myTitle\" "
             + "readonly disabled>"
             + "Hello World</textarea>";
    assertEquals( expected, markup );
  }

  public void testAjaxRendererIE5() throws Exception {
    WebTextArea text = new WebTextArea();
    text.addWebItemListener( new WebItemListener() {
      public void webItemStateChanged( final WebItemEvent e ) {
      } 
    } );
    text.addWebFocusGainedListener( new WebFocusGainedListener() {
      public void webFocusGained( final WebFocusGainedEvent e ) {
      } 
    } );    
    text.setValue( "Hello World" );
    text.setCols( 20 );
    text.setRows( 20 );
    text.setDir( "myDirectory" );
    text.setLang( "myLang" );
    text.setName( "myName" );
    text.setTitle( "myTitle" );
    text.setEnabled( false );
    text.setUpdatable( false );
    W4TFixture.setWebComponentUniqueId( text, "text1" );
    AjaxStatus ajaxStatus = ( AjaxStatus )text.getAdapter( AjaxStatus.class );
    ajaxStatus.updateStatus( true );
    W4TFixture.fakeBrowser( new Ie5up( true, true ) );
    HtmlResponseWriter writer = new HtmlResponseWriter();
    setResponseWriter( writer );
    W4TFixture.renderComponent( text );
    String markup = getBodyMarkup( writer );
    String expected;
    expected =  "<textarea "
              + "id=\"text1\" "
              + "name=\"text1\" "
              + "rows=\"20\" cols=\"20\" "
              + "wrap=\"virtual\" "
              + "class=\"w4tCsscd1f6403\" "
              + "dir=\"myDirectory\" "
              + "lang=\"myLang\" "
              + "title=\"myTitle\" "
              + "readonly=\"readonly\" "
              + "disabled=\"disabled\">"
              + "Hello World</textarea>";
    assertEquals( expected, markup );
  }
  
  public void testScriptRenderer() throws Exception {
    // disabled and readonly 
    WebTextArea text = new WebTextArea();
    W4TFixture.setWebComponentUniqueId( text, "text1" );
    text.setValue( "Hello World" );
      text.addWebItemListener( new WebItemListener() {
      public void webItemStateChanged( final WebItemEvent e ) {
      } 
    } );
    text.addWebFocusGainedListener( new WebFocusGainedListener() {
      public void webFocusGained( final WebFocusGainedEvent e ) {
      } 
    } );    
    text.setCols( 20 );
    text.setRows( 20 );
    text.setDir( "myDirectory" );
    text.setLang( "myLang" );
    text.setName( "myName" );
    text.setTitle( "myTitle" );   
    text.setEnabled( false );
    text.setUpdatable( false );    
    W4TFixture.fakeBrowser( new Default( true, false ) );
    HtmlResponseWriter writer = new HtmlResponseWriter();
    setResponseWriter( writer );
    W4TFixture.renderComponent( text );
    String markup = getBodyMarkup( writer );
    String expected = "<textarea "
                    + "id=\"text1\" "
                    + "name=\"text1\" "
                    + "rows=\"20\" cols=\"20\" "
                    + "wrap=\"virtual\" class=\"w4tCsscd1f6403\" "
                    + "dir=\"myDirectory\" "
                    + "lang=\"myLang\" "
                    + "title=\"myTitle\" "
                    + "readonly disabled>"
                    + "Hello World</textarea>";
    assertEquals( expected, markup );
    // render with empty value and enabled and readonly
    text.setValue( "" );
    text.setEnabled( true );
    writer = new HtmlResponseWriter();
    setResponseWriter( writer );
    W4TFixture.renderComponent( text );
    markup = getBodyMarkup( writer );
    expected =  "<textarea "
              + "id=\"text1\" "
              + "name=\"text1\" "
              + "rows=\"20\" cols=\"20\" "
              + "wrap=\"virtual\" class=\"w4tCsscd1f6403\" "
              + "dir=\"myDirectory\" "
              + "lang=\"myLang\" "
              + "title=\"myTitle\" "
              + "readonly "
              + "onfocus=\"eventHandler.setFocusID(this);"
              + "eventHandler.webFocusGained(this)\">"
              + "</textarea>";
    assertEquals( expected, markup );

    // render with empty value and enabled and writeable
    text.setValue( "" );
    text.setEnabled( true );
    text.setUpdatable( true );
    writer = new HtmlResponseWriter();
    setResponseWriter( writer );
    W4TFixture.renderComponent( text );
    markup = getBodyMarkup( writer );
    expected =  "<textarea "
              + "id=\"text1\" "
              + "name=\"text1\" "
              + "rows=\"20\" cols=\"20\" "
              + "wrap=\"virtual\" class=\"w4tCsscd1f6403\" "
              + "dir=\"myDirectory\" "
              + "lang=\"myLang\" "
              + "title=\"myTitle\" "
              + "onchange=\"eventHandler.webItemStateChanged(this)\" "
              + "onfocus=\"eventHandler.setFocusID(this);"
              + "eventHandler.webFocusGained(this)\">"
              + "</textarea>";
    assertEquals( expected, markup );

    // render with empty value and disabled and writeable
    text.setValue( "" );
    text.setEnabled( false );
    text.setUpdatable( true );
    writer = new HtmlResponseWriter();
    setResponseWriter( writer );
    W4TFixture.renderComponent( text );
    markup = getBodyMarkup( writer );
    expected =  "<textarea "
              + "id=\"text1\" "
              + "name=\"text1\" "
              + "rows=\"20\" cols=\"20\" "
              + "wrap=\"virtual\" class=\"w4tCsscd1f6403\" "
              + "dir=\"myDirectory\" "
              + "lang=\"myLang\" "
              + "title=\"myTitle\" "
              + "disabled>"
              + "</textarea>";
    assertEquals( expected, markup );
  }
  
  public void testNoScriptRenderer() throws Exception {
    WebTextArea text = new WebTextArea();
    W4TFixture.setWebComponentUniqueId( text, "text1" );
    text.setValue( "Hello World" );
    text.setCols( 20 );
    text.setRows( 20 );
    text.setDir( "myDirectory" );
    text.setLang( "myLang" );
    text.setName( "myName" );
    text.setTitle( "myTitle" );
    text.setUpdatable( false );    
    text.addWebItemListener( new WebItemListener() {

    public void webItemStateChanged( WebItemEvent e ) {
    }
      
    } );    
    HtmlResponseWriter writer = new HtmlResponseWriter();
    W4TFixture.fakeBrowser( new Default( false, false ) );
    setResponseWriter( writer );
    W4TFixture.renderComponent( text );
    String markup = getBodyMarkup( writer );
    String expected =   "<textarea "
                      + "name=\"text1\" "
                      + "rows=\"20\" cols=\"20\" "
                      + "wrap=\"virtual\" class=\"w4tCsscd1f6403\" "
                      + "dir=\"myDirectory\" "
                      + "lang=\"myLang\" "
                      + "title=\"myTitle\" "
                      + "readonly>"
                      + "Hello World</textarea>"
                      + "<input type=\"image\" "
                      + "src=\"resources/images/submitter.gif\" "
                      + "name=\"wietext1\" "
                      + "border=\"0\">";
    assertEquals( expected, markup );
    
    writer = new HtmlResponseWriter();
    text.setEnabled( false );
    setResponseWriter( writer );
    W4TFixture.renderComponent( text );
    markup = getBodyMarkup( writer );
    expected =  "<textarea "
              + "name=\"text1\" "
              + "rows=\"20\" cols=\"20\" "
              + "wrap=\"virtual\" class=\"w4tCsscd1f6403\" "
              + "dir=\"myDirectory\" "
              + "lang=\"myLang\" "
              + "title=\"myTitle\" "
              + "readonly disabled>"
              + "Hello World</textarea>";
    assertEquals( expected, markup );
  }
  
  public void testReadData_Default_Script() throws Exception {
    W4TFixture.fakeBrowser( new Default( true, false ) );
    WebTextArea text = new WebTextArea();
    W4TFixture.setWebComponentUniqueId( text, "text1" );
    fakeRequestParameter( text.getUniqueID(), "newText" );
    RendererCache rendererCache = RendererCache.getInstance();
    Renderer renderer = rendererCache.retrieveRenderer( text.getClass() );
    renderer.readData( text );
    assertEquals( "newText", text.getValue() );
  }
  
  public void testReadData_Default_Ajax() throws Exception {
    W4TFixture.fakeBrowser( new Ie6( true, true ) );
    WebTextArea text = new WebTextArea();
    W4TFixture.setWebComponentUniqueId( text, "text1" );
    fakeRequestParameter( text.getUniqueID(), "newText" );
    RendererCache rendererCache = RendererCache.getInstance();
    Renderer renderer = rendererCache.retrieveRenderer( text.getClass() );
    renderer.readData( text );
    assertEquals( "newText", text.getValue() );
  }
  
  public void testAjaxStatusAfterReadData() throws Exception {
    // case 1: value changed on client-side -> don't render WebText
    WebForm form = W4TFixture.getEmptyWebFormInstance();
    WebTextArea textArea = new WebTextArea();
    form.add( textArea, WebBorderLayout.CENTER );
    W4TFixture.fakeRequestParam( textArea.getUniqueID(), "newText" );
    W4TFixture.fakeBrowser( new Ie6( true, true ) );
    RendererCache rendererCache = RendererCache.getInstance();
    AjaxStatusUtil.preRender( form );
    AjaxStatusUtil.postRender( form );
    Renderer renderer = rendererCache.retrieveRenderer( textArea.getClass() );
    assertEquals( WebTextAreaRenderer_Default_Ajax.class, renderer.getClass() );
    renderer.readData( textArea );
    assertEquals( "newText", textArea.getValue() );
    AjaxStatusUtil.preRender( form );
    assertEquals( false, W4TFixture.getAjaxStatus( textArea ).mustRender() );
    // case 2: value changed on client-side and programatically after readData
    //         -> must render WebText
    form = W4TFixture.getEmptyWebFormInstance();
    textArea = new WebTextArea();
    form.add( textArea, WebBorderLayout.CENTER );
    W4TFixture.fakeRequestParam( textArea.getUniqueID(), "newText" );
    W4TFixture.fakeBrowser( new Ie6( true, true ) );
    rendererCache = RendererCache.getInstance();
    AjaxStatusUtil.preRender( form );
    AjaxStatusUtil.postRender( form );
    renderer = rendererCache.retrieveRenderer( textArea.getClass() );
    renderer.readData( textArea );
    textArea.setValue( "anotherText" );
    AjaxStatusUtil.preRender( form );
    assertEquals( true, W4TFixture.getAjaxStatus( textArea ).mustRender() );
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
  
  private String getBodyMarkup( final HtmlResponseWriter writer ) {
    StringBuffer buffer = new StringBuffer();
    for( int i = 0; i < writer.getBodySize(); i++ ) {
      buffer.append( writer.getBodyToken( i ) );
    }
    return buffer.toString();
  }

  private static void fakeRequestParameter( final String key, final String value ) {
    W4TFixture.fakeRequestParam( key, value );
  }
}
