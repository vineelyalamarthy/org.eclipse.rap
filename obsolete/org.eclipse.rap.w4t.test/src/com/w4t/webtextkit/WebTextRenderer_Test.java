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
package com.w4t.webtextkit;

import junit.framework.TestCase;

import org.eclipse.rwt.internal.browser.*;
import org.eclipse.rwt.internal.lifecycle.HtmlResponseWriter;
import org.eclipse.rwt.internal.service.ContextProvider;

import com.w4t.*;
import com.w4t.ajax.AjaxStatus;
import com.w4t.ajax.AjaxStatusUtil;
import com.w4t.event.*;
import com.w4t.util.RendererCache;


/** <p>Unit tests for WebTextRenderer.</p> */
public class WebTextRenderer_Test extends TestCase {
  
  public void testAjaxRenderer() throws Exception {
    WebText text = new WebText();
    text.setValue( "Hello World" );
    W4TFixture.setWebComponentUniqueId( text, "text1" );
    AjaxStatus ajaxStatus = ( AjaxStatus )text.getAdapter( AjaxStatus.class );
    ajaxStatus.updateStatus( true );
    W4TFixture.fakeBrowser( new Ie5up( true, true ) );
    HtmlResponseWriter writer = new HtmlResponseWriter();
    setResponseWriter( writer );
    W4TFixture.renderComponent( text );
    String markup = W4TFixture.getBodyMarkup(writer);
    String expected;
    expected = "<input type=\"text\" "
             + "name=\"text1\" value=\"Hello World\" "
             + "id=\"text1\" "
             + "class=\"w4tCssfae83ad\" "
             + "onfocus=\"eventHandler.setFocusID(this)\" />";
    assertEquals( expected, markup );    
    
    // render with item listener
    text.addWebItemListener( new WebItemListener() {
      public void webItemStateChanged( final WebItemEvent e ) {
      } 
    } );
    writer = new HtmlResponseWriter();
    setResponseWriter( writer );
    W4TFixture.renderComponent( text );
    markup = W4TFixture.getBodyMarkup(writer);
    expected =   "<input type=\"text\" "
               + "name=\"text1\" value=\"Hello World\" "
               + "id=\"text1\" "
               + "class=\"w4tCssfae83ad\" " 
               + "onchange=\"eventHandler.webItemStateChanged(this)\" " 
               + "onfocus=\"eventHandler.setFocusID(this)\" />";
    assertEquals( expected, markup );    

    // render with focus listener ( enabled and writeable)
    text.addWebFocusGainedListener( new WebFocusGainedListener() {
      public void webFocusGained( final WebFocusGainedEvent e ) {
      } 
    } );
    writer = new HtmlResponseWriter();
    setResponseWriter( writer );
    W4TFixture.renderComponent( text );
    markup = W4TFixture.getBodyMarkup(writer);
    expected =  "<input type=\"text\" "
              + "name=\"text1\" value=\"Hello World\" "
              + "id=\"text1\" "
              + "class=\"w4tCssfae83ad\" " 
              + "onchange=\"eventHandler.webItemStateChanged(this)\" " 
              + "onfocus=\"eventHandler.setFocusID(this);" 
              + "eventHandler.webFocusGained(this)\" />";
    assertEquals( expected, markup );    
    
    // render with max length and size
    text.setMaxLength( 10 );
    text.setSize( 100 );
    writer = new HtmlResponseWriter();
    setResponseWriter( writer );
    W4TFixture.renderComponent( text );
    markup = W4TFixture.getBodyMarkup(writer);
    expected =   "<input type=\"text\" "
               + "name=\"text1\" value=\"Hello World\" "
               + "id=\"text1\" "
               + "size=\"100\" "
               + "maxlength=\"10\" "
               + "class=\"w4tCssfae83ad\" " 
               + "onchange=\"eventHandler.webItemStateChanged(this)\" " 
               + "onfocus=\"eventHandler.setFocusID(this);"
               + "eventHandler.webFocusGained(this)\" />";
    assertEquals( expected, markup ); 

    // render (enabled and not writeable)
    text.setUpdatable( false );
    writer = new HtmlResponseWriter();
    setResponseWriter( writer );
    W4TFixture.renderComponent( text );
    markup = W4TFixture.getBodyMarkup(writer);
    expected =  "<input type=\"text\" "
              + "name=\"text1\" value=\"Hello World\" "
              + "id=\"text1\" "
              + "size=\"100\" "
              + "maxlength=\"10\" "
              + "class=\"w4tCssfae83ad\" "
              + "readonly=\"readonly\" "
              + "onfocus=\"eventHandler.setFocusID(this);"
              + "eventHandler.webFocusGained(this)\" />";
    assertEquals( expected, markup ); 

    // render (disabled and not writeable)
    text.setUpdatable( false );
    text.setEnabled( false );
    writer = new HtmlResponseWriter();
    setResponseWriter( writer );
    W4TFixture.renderComponent( text );
    markup = W4TFixture.getBodyMarkup(writer);
    expected =  "<input type=\"text\" "
      + "name=\"text1\" value=\"Hello World\" "
      + "id=\"text1\" "
      + "size=\"100\" "
      + "maxlength=\"10\" "
      + "class=\"w4tCssfae83ad\" "
      + "readonly=\"readonly\" "
      + "disabled=\"disabled\" "
      + "/>";
    assertEquals( expected, markup ); 

    // render (disabled and writeable)
    text.setUpdatable( true );
    text.setEnabled( false );
    writer = new HtmlResponseWriter();
    setResponseWriter( writer );
    W4TFixture.renderComponent( text );
    markup = W4TFixture.getBodyMarkup(writer);
    expected =  "<input type=\"text\" "
      + "name=\"text1\" value=\"Hello World\" "
      + "id=\"text1\" "
      + "size=\"100\" "
      + "maxlength=\"10\" "
      + "class=\"w4tCssfae83ad\" "
      + "disabled=\"disabled\" "
      + "/>";
    assertEquals( expected, markup ); 
  }
  
  public void testScriptRenderer() throws Exception {
    WebText text = new WebText();
    W4TFixture.setWebComponentUniqueId( text, "text1" );
    text.setValue( "Hello World" );
    W4TFixture.fakeBrowser( new Ie5up( true, false ) );
    HtmlResponseWriter writer = new HtmlResponseWriter();
    setResponseWriter( writer );
    W4TFixture.renderComponent( text );
    String markup = W4TFixture.getBodyMarkup(writer);
    String expected = "<input type=\"text\" "
                    + "name=\"text1\" "
                    + "value=\"Hello World\" " 
                    + "id=\"text1\" "
                    + "class=\"w4tCssfae83ad\" "
                    + "onfocus=\"eventHandler.setFocusID(this)\" />";
    assertEquals( expected, markup );
    
    // render with empty value
    text.setValue( "" );
    writer = new HtmlResponseWriter();
    setResponseWriter( writer );
    W4TFixture.renderComponent( text );
    markup = W4TFixture.getBodyMarkup(writer);
    expected = "<input type=\"text\" "
             + "name=\"text1\" "
             + "value=\"\" " 
             + "id=\"text1\" "
             + "class=\"w4tCssfae83ad\" "
             + "onfocus=\"eventHandler.setFocusID(this)\" />";
    assertEquals( expected, markup );
    
    // render with item listener
    text.addWebItemListener( new WebItemListener() {
      public void webItemStateChanged( final WebItemEvent e ) {
      } 
    } );
    writer = new HtmlResponseWriter();
    setResponseWriter( writer );
    W4TFixture.renderComponent( text );
    markup = W4TFixture.getBodyMarkup(writer);
    expected =   "<input type=\"text\" " 
               + "name=\"text1\" " 
               + "value=\"\" " 
               + "id=\"text1\" "
               + "class=\"w4tCssfae83ad\" " 
               + "onchange=\"eventHandler.webItemStateChanged(this)\" "
               + "onfocus=\"eventHandler.setFocusID(this)\" />";
    assertEquals( expected, markup );

    // render with size, max legth
    text.setSize( 100 );
    text.setMaxLength( 10 );
    writer = new HtmlResponseWriter();
    setResponseWriter( writer );
    W4TFixture.renderComponent( text );
    markup = W4TFixture.getBodyMarkup(writer);
    expected  = "<input type=\"text\" "
              + "name=\"text1\" "
              + "value=\"\" "
              + "id=\"text1\" "
              + "size=\"100\" "
              + "maxlength=\"10\" "
              + "class=\"w4tCssfae83ad\" "
              + "onchange=\"eventHandler.webItemStateChanged(this)\" "
              + "onfocus=\"eventHandler.setFocusID(this)\" />";
    assertEquals( expected, markup );
    
    // render with focus listener
    text.addWebFocusGainedListener( new WebFocusGainedListener() {
      public void webFocusGained( final WebFocusGainedEvent e ) {
      } 
    } );
    writer = new HtmlResponseWriter();
    setResponseWriter( writer );
    W4TFixture.renderComponent( text );
    markup = W4TFixture.getBodyMarkup(writer);
    expected  = "<input type=\"text\" "
              + "name=\"text1\" "
              + "value=\"\" "
              + "id=\"text1\" "
              + "size=\"100\" "
              + "maxlength=\"10\" "
              + "class=\"w4tCssfae83ad\" "
              + "onchange=\"eventHandler.webItemStateChanged(this)\" "
              + "onfocus=\"eventHandler.setFocusID(this);"
              + "eventHandler.webFocusGained(this)\" />";
    assertEquals( expected, markup );
    
    writer = new HtmlResponseWriter();
    setResponseWriter( writer );
    text.setEnabled( false );
    text.setUpdatable( false );
    W4TFixture.renderComponent( text );
    markup = W4TFixture.getBodyMarkup(writer);
    expected =   "<input type=\"text\" "
               + "name=\"text1\" "
               + "value=\"\" "
               + "id=\"text1\" "
               + "size=\"100\" "
               + "maxlength=\"10\" "
               + "class=\"w4tCssfae83ad\" "
               + "readonly=\"readonly\" "
               + "disabled=\"disabled\" />";
    assertEquals( expected, markup );
    
    
  }
  
  public void testNoScriptRenderer() throws Exception {
    WebText text = new WebText();
    W4TFixture.setWebComponentUniqueId( text, "text1" );
    text.setValue( "Hello World" );
    HtmlResponseWriter writer = new HtmlResponseWriter();
    W4TFixture.fakeBrowser( new Ie5up( false, false ) );
    setResponseWriter( writer );
    W4TFixture.renderComponent( text );
    String markup = W4TFixture.getBodyMarkup(writer);
    String expected = "<input type=\"text\" "
                    + "name=\"text1\" "
                    + "value=\"Hello World\" " 
                    + "id=\"text1\" "
                    + "class=\"w4tCssfae83ad\" />";
    assertEquals( expected, markup );

    text.setSize( 100 );
    text.setMaxLength( 20 );
    text.addWebItemListener( new WebItemListener() {

      public void webItemStateChanged( WebItemEvent e ) {
      }
      
    } );
    writer = new HtmlResponseWriter();
    setResponseWriter( writer );
    W4TFixture.renderComponent( text );
    markup = W4TFixture.getBodyMarkup(writer);
    expected  = "<input type=\"text\" "
              + "name=\"text1\" "
              + "value=\"Hello World\" "
              + "id=\"text1\" "
              + "size=\"100\" "
              + "maxlength=\"20\" "
              + "class=\"w4tCssfae83ad\" />" 
              + "<input type=\"image\""
              + " src=\"resources/images/submitter.gif\" " 
              + "name=\"wietext1\" border=\"0\" />";
    assertEquals( expected, markup );
  }
  
  public void testReadData_Default_Script() throws Exception {
    W4TFixture.fakeBrowser( new Default( true, false ) );
    WebText text = new WebText();
    W4TFixture.fakeRequestParam( text.getUniqueID(), "newText" );
    RendererCache rendererCache = RendererCache.getInstance();
    Renderer renderer = rendererCache.retrieveRenderer( text.getClass() );
    renderer.readData( text );
    assertEquals( "newText", text.getValue() );
  }
  
  public void testReadData_Default_Ajax() throws Exception {
    W4TFixture.fakeBrowser( new Ie6( true, true ) );
    WebText text = new WebText();
    W4TFixture.fakeRequestParam( text.getUniqueID(), "newText" );
    RendererCache rendererCache = RendererCache.getInstance();
    Renderer renderer = rendererCache.retrieveRenderer( text.getClass() );
    renderer.readData( text );
    assertEquals( "newText", text.getValue() );
  }
  
  
  public void testAjaxStatusAfterReadData() throws Exception {
    // case 1: value changed on client-side -> don't render WebText
    WebForm form = W4TFixture.getEmptyWebFormInstance();
    WebText text = new WebText();
    form.add( text, WebBorderLayout.CENTER );
    W4TFixture.fakeRequestParam( text.getUniqueID(), "newText" );
    W4TFixture.fakeBrowser( new Ie6( true, true ) );
    RendererCache rendererCache = RendererCache.getInstance();
    AjaxStatusUtil.preRender( form );
    AjaxStatusUtil.postRender( form );
    Renderer renderer = rendererCache.retrieveRenderer( text.getClass() );
    assertEquals( WebTextRenderer_Default_Ajax.class, renderer.getClass() );
    renderer.readData( text );
    assertEquals( "newText", text.getValue() );
    AjaxStatusUtil.preRender( form );
    assertEquals( false, W4TFixture.getAjaxStatus( text ).mustRender() );
    // case 2: value changed on client-side and programatically after readData
    //         -> must render WebText
    form = W4TFixture.getEmptyWebFormInstance();
    text = new WebText();
    form.add( text, WebBorderLayout.CENTER );
    W4TFixture.fakeRequestParam( text.getUniqueID(), "newText" );
    W4TFixture.fakeBrowser( new Ie6( true, true ) );
    rendererCache = RendererCache.getInstance();
    AjaxStatusUtil.preRender( form );
    AjaxStatusUtil.postRender( form );
    renderer = rendererCache.retrieveRenderer( text.getClass() );
    assertEquals( WebTextRenderer_Default_Ajax.class, renderer.getClass() );
    renderer.readData( text );
    text.setValue( "anotherText" );
    AjaxStatusUtil.preRender( form );
    assertEquals( true, W4TFixture.getAjaxStatus( text ).mustRender() );
  }
  
  protected void setUp() throws Exception {
    W4TFixture.setUp();
    W4TFixture.createContext();
  }
  
  protected void tearDown() throws Exception {
    W4TFixture.tearDown();
    W4TFixture.removeContext();
  }
  
  private static void setResponseWriter( final HtmlResponseWriter writer ) {
    ContextProvider.getStateInfo().setResponseWriter( writer );
  }
}
