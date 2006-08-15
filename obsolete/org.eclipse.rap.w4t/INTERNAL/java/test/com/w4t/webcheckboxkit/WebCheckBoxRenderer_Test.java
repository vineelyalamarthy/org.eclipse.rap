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
package com.w4t.webcheckboxkit;

import junit.framework.TestCase;
import com.w4t.*;
import com.w4t.ajax.AjaxStatusUtil;
import com.w4t.engine.service.ContextProvider;
import com.w4t.event.*;
import com.w4t.util.RendererCache;
import com.w4t.util.browser.*;


/** <p>Unit tests for WebCheckBoxRenderer.</p> */
public class WebCheckBoxRenderer_Test extends TestCase {

  public void testAjaxRenderer() throws Exception {
    WebCheckBox checkbox = new WebCheckBox();
    checkbox.setLabel( "Hello World" );
    checkbox.setDir( "myDir" );
    checkbox.setLang( "myLang" );
    checkbox.setSelected( false );
    checkbox.setTitle( "myTitle" );
    checkbox.setUpdatable( true );
    checkbox.setValue( "myValue" );
    Fixture.setWebComponentUniqueId( checkbox, "page1" );
    Fixture.forceAjaxRendering( checkbox );
    Fixture.fakeBrowser( new Default( true, true ) );
    HtmlResponseWriter writer = new HtmlResponseWriter();
    setResponseWriter( writer );
    Fixture.renderComponent( checkbox );
    String markup = getBodyMarkup( writer );
    String expected;
    expected 
      =   "<span id=\"page1\"><input type=\"checkbox\" name=\"page1\" " 
        + "checked=\"checked\" "
        + "value=\"-1\" onfocus=\"eventHandler.setFocusID(this)\">" 
        + "<span class=\"w4tCsscd1f6403\" " 
        + "dir=\"myDir\" lang=\"myLang\" title=\"myTitle\">"
        + "Hello World</span>"
        + "<input type=\"hidden\" id=\"cbcpage1\" name=\"cbcpage1\" " 
          + "value=\"myValue\">"
        + "</span>";
    assertEquals( expected, markup );
    
    // add listeners
    checkbox.addWebFocusGainedListener( new WebFocusGainedListener() {
      public void webFocusGained( final WebFocusGainedEvent e ) {
      }
    });
    checkbox.addWebItemListener( new WebItemListener() {
      public void webItemStateChanged( WebItemEvent e ) {
      }
    });
    checkbox.setSelected( true );
    writer = new HtmlResponseWriter();
    setResponseWriter( writer );
    Fixture.renderComponent( checkbox );
    markup = getBodyMarkup( writer );
    expected 
      = "<span id=\"page1\"><input type=\"checkbox\" name=\"page1\" "
      + "checked=\"checked\" value=\"-1\" "
      + "onmousedown=\"eventHandler.setClickedCheckBox(this)\" "
      + "onmouseup=\"eventHandler.triggerClickedCheckBox(this)\" "
      + "onkeydown=\"eventHandler.keyOnCheckBox(this)\" "
      + "onfocus=\"eventHandler.setFocusID(this);"
      + "eventHandler.webFocusGained(this)\">"
      + "<span class=\"w4tCsscd1f6403\" " 
      + "dir=\"myDir\" lang=\"myLang\" title=\"myTitle\">"
      + "Hello World</span>"
      + "<input type=\"hidden\" id=\"cbcpage1\" name=\"cbcpage1\" value=\"-1\">"
      + "</span>";   
    assertEquals( expected, markup );
  }
  
  public void testScriptRenderer() throws Exception {
    WebCheckBox checkbox = new WebCheckBox();
    checkbox.setLabel( "Hello World" );
    checkbox.setDir( "myDir" );
    checkbox.setLang( "myLang" );
    checkbox.setTitle( "myTitle" );
    checkbox.setUpdatable( true );
    checkbox.setValue( "myValue" );
    Fixture.setWebComponentUniqueId( checkbox, "page1" );
    Fixture.fakeBrowser( new Ie5up( true, false ) );
    HtmlResponseWriter writer = new HtmlResponseWriter();
    setResponseWriter( writer );
    Fixture.renderComponent( checkbox );
    String markup = getBodyMarkup( writer );
    String expected;
    expected 
        = "<span id=\"page1\"><input type=\"checkbox\" name=\"page1\" "
        + "checked=\"checked\" "
        + "value=\"-1\" onfocus=\"eventHandler.setFocusID(this)\" />"
        + "<span class=\"w4tCsscd1f6403\" " 
        + "dir=\"myDir\" lang=\"myLang\" title=\"myTitle\">"
        + "Hello World</span>"
        + "<input type=\"hidden\" id=\"cbcpage1\" name=\"cbcpage1\" " 
          + "value=\"myValue\" />"
        + "</span>";
    assertEquals( expected, markup );
    
    // add listeners
    checkbox.addWebFocusGainedListener( new WebFocusGainedListener() {
      public void webFocusGained( final WebFocusGainedEvent e ) {
      }
    });
    checkbox.addWebItemListener( new WebItemListener() {
      public void webItemStateChanged( WebItemEvent e ) {
      }
    });
    checkbox.setSelected( true );
    writer = new HtmlResponseWriter();
    setResponseWriter( writer );
    Fixture.renderComponent( checkbox );
    markup = getBodyMarkup( writer );
    expected 
    = "<span id=\"page1\"><input type=\"checkbox\" name=\"page1\" "
    + "checked=\"checked\" value=\"-1\" "
    + "onmousedown=\"eventHandler.setClickedCheckBox(this)\" "
    + "onmouseup=\"eventHandler.triggerClickedCheckBox(this)\" "
    + "onkeydown=\"eventHandler.keyOnCheckBox(this)\" "
    + "onfocus=\"eventHandler.setFocusID(this);"
    + "eventHandler.webFocusGained(this)\" />"
    + "<span class=\"w4tCsscd1f6403\" "
    + "dir=\"myDir\" lang=\"myLang\" title=\"myTitle\">"
    + "Hello World</span>"
    + "<input type=\"hidden\" id=\"cbcpage1\" name=\"cbcpage1\" value=\"-1\" />"
    + "</span>";
    assertEquals( expected, markup );
  }
  
  public void testNoScriptRenderer() throws Exception {
    WebCheckBox checkbox = new WebCheckBox();
    checkbox.setLabel( "Hello World" );
    checkbox.setDir( "myDir" );
    checkbox.setLang( "myLang" );
    checkbox.setSelected( true );
    checkbox.setTitle( "myTitle" );
    checkbox.setUpdatable( true );
    Fixture.setWebComponentUniqueId( checkbox, "page1" );
    Fixture.fakeBrowser( new Default( false, false ) );
    HtmlResponseWriter writer = new HtmlResponseWriter();
    setResponseWriter( writer );
    Fixture.renderComponent( checkbox );
    String markup = getBodyMarkup( writer );
    String expected;
    expected 
      =   "<span id=\"page1\"><input type=\"checkbox\" name=\"page1\" "
        + "checked=\"checked\" "
        + "value=\"-1\"><span class=\"w4tCsscd1f6403\" " 
        + "dir=\"myDir\" lang=\"myLang\" title=\"myTitle\">"
        + "Hello World</span>"
        + "<input type=\"hidden\" id=\"cbcpage1\" name=\"cbcpage1\" " 
          + "value=\"-1\">"
        + "</span>";
    assertEquals( expected, markup );
    
    // add listeners
    checkbox.addWebFocusGainedListener( new WebFocusGainedListener() {
      public void webFocusGained( final WebFocusGainedEvent e ) {
      }
    });
    checkbox.addWebItemListener( new WebItemListener() {
      public void webItemStateChanged( WebItemEvent e ) {
      }
    } );
    checkbox.setSelected( true );
    writer = new HtmlResponseWriter();
    setResponseWriter( writer );
    Fixture.renderComponent( checkbox );
    markup = getBodyMarkup( writer );
    expected 
      =   "<span id=\"page1\"><input type=\"checkbox\" name=\"page1\" "
        + "checked=\"checked\" value=\"-1\">"
        + "<span class=\"w4tCsscd1f6403\" "
        + "dir=\"myDir\" lang=\"myLang\" title=\"myTitle\">"
        + "Hello World</span>"
        + "<input type=\"image\" "
        +"src=\"resources/images/submitter.gif\" name=\"wiepage1\""
        + " border=\"0\">"
        + "<input type=\"hidden\" id=\"cbcpage1\" name=\"cbcpage1\" " 
        + "value=\"-1\">"
        + "</span>";
    assertEquals( expected, markup );
  }
  
  public void testEncoding() throws Exception {
    WebCheckBox checkbox = new WebCheckBox();
    checkbox.setLabel( "ÜÄÖ\"?r" );
    checkbox.setTitle( "ÜÄÖ\"?" );
    checkbox.setUpdatable( true );
    Fixture.fakeBrowser( new Default( false, false ) );
    HtmlResponseWriter writer = new HtmlResponseWriter();
    setResponseWriter( writer );
    Fixture.renderComponent( checkbox );
    String markup = getBodyMarkup( writer );
    String expected 
      = "<span id=\"p1\">" 
      + "<input type=\"checkbox\" name=\"p1\" value=\"-1\">" 
      + "<span class=\"w4tCsscd1f6403\" title=\"&Uuml;&Auml;&Ouml;&quot;?\">" 
      + "&Uuml;&Auml;&Ouml;&quot;?r" 
      + "</span><input type=\"hidden\" id=\"cbcp1\" name=\"cbcp1\" value=\"0\">" 
      + "</span>"; 
    assertEquals( expected, markup );
  }
  
  public void testAjaxStatusAfterReadData() {
    WebForm form = Fixture.getEmptyWebFormInstance();
    WebCheckBox checkBox = new WebCheckBox();
    checkBox.setValCheck( "checked" );
    checkBox.setValUnCheck( "unchecked" );
    checkBox.setValue( "unchecked" );
    form.add( checkBox, WebBorderLayout.CENTER );
    
    String checkBoxId = checkBox.getUniqueID();
    String name 
      = WebCheckBoxReadDataUtil.addCheckBoxControlPrefix( checkBoxId );
    Fixture.fakeRequestParam( name, checkBox.getValue() );
    Fixture.fakeRequestParam( checkBoxId, "checked" );
    Fixture.fakeBrowser( new Ie6( true, true ) );
    RendererCache rendererCache = RendererCache.getInstance();
    AjaxStatusUtil.preRender( form );
    AjaxStatusUtil.postRender( form );
    Renderer renderer = rendererCache.retrieveRenderer( checkBox.getClass() );
    assertEquals( WebCheckBoxRenderer_Default_Ajax.class, renderer.getClass() );
    renderer.readData( checkBox );
    assertEquals( "checked", checkBox.getValue() );
    AjaxStatusUtil.preRender( form );
    assertEquals( true, Fixture.getAjaxStatus( checkBox ).mustRender() );
  }
  
  protected void setUp() throws Exception {
    Fixture.setUp();
    Fixture.createContext();
  }
  
  protected void tearDown() throws Exception {
    Fixture.tearDown();
    Fixture.removeContext();
  }
  
  private void setResponseWriter( final HtmlResponseWriter responseWriter ) {
    ContextProvider.getStateInfo().setResponseWriter( responseWriter );
  }
  
  private String getBodyMarkup( final HtmlResponseWriter writer ) {
    StringBuffer buffer = new StringBuffer();
    for( int i = 0; i < writer.getBodySize(); i++ ) {
      buffer.append( writer.getBodyToken( i ) );
    }
    return buffer.toString();
  }
}
