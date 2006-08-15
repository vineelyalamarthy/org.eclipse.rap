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
package com.w4t.webradiobuttonkit;

import java.io.IOException;
import junit.framework.TestCase;
import com.w4t.*;
import com.w4t.ajax.AjaxStatusUtil;
import com.w4t.event.*;
import com.w4t.util.RendererCache;
import com.w4t.util.browser.Default;
import com.w4t.util.browser.Ie6;
import com.w4t.webradiobuttongroupkit.WebRadioButtonGroupRenderer_Default_Ajax;


/** <p>Unit tests for WebRadioButtonRenderer.</p> */
public class WebRadioButtonRenderer_Test extends TestCase {

  public void testAjaxRenderer() throws Exception {
    WebRadioButton radioButton = new WebRadioButton();
    Fixture.setWebComponentUniqueId( radioButton, "radio1" );
    radioButton.setValue( "Push Me" );
    radioButton.setLabel( "The Label" );
    WebRadioButtonGroup group = new WebRadioButtonGroup();
    Fixture.setWebComponentUniqueId( group, "group1" );
    group.setValue( radioButton.getValue() );
    group.add( radioButton );
    Fixture.forceAjaxRendering( radioButton );
    Fixture.fakeBrowser( new Ie6( true, true ) );
    HtmlResponseWriter writer = new HtmlResponseWriter();
    Fixture.setResponseWriter( writer );
    Fixture.renderComponent( radioButton );
    String markup = Fixture.getBodyMarkup( writer );
    String expected
      = "<envelope id=\"radio1\"><!--" 
      + "<span id=\"radio1\">" 
      + "<input type=\"radio\" style=\"vertical-align:middle\" name=\"group1\" " 
      + "checked=\"checked\" value=\"Push Me\" />" 
      + "<span class=\"w4tCsscd1f6403\">The Label</span></span>--></envelope>";
    assertEquals( expected, markup );
    
    // with event handler
    group.addWebItemListener( new WebItemListener() {
      public void webItemStateChanged( WebItemEvent e ) {
      }
    } );
    radioButton.setDir( "myDir" );
    radioButton.setLang( "myLang" );
    radioButton.setTitle( "myTitle" );
    writer = new HtmlResponseWriter();
    Fixture.setResponseWriter( writer );
    Fixture.renderComponent( radioButton );
    markup = Fixture.getBodyMarkup( writer );
    expected 
      = "<envelope id=\"radio1\"><!--" 
      + "<span id=\"radio1\">" 
      + "<input type=\"radio\" style=\"vertical-align:middle\" name=\"group1\" " 
      + "checked=\"checked\" value=\"Push Me\" " 
      + "onclick=\"eventHandler.webItemStateChanged(this)\" />" 
      + "<span class=\"w4tCsscd1f6403\" dir=\"myDir\" lang=\"myLang\" " 
      + "title=\"myTitle\">The Label</span></span>--></envelope>";
    assertEquals( expected, markup );

    radioButton.setEnabled( false );
    writer = new HtmlResponseWriter();
    Fixture.setResponseWriter( writer );
    Fixture.renderComponent( radioButton );
    markup = Fixture.getBodyMarkup( writer );
    expected 
      = "<envelope id=\"radio1\"><!--" 
      + "<span id=\"radio1\"><input type=\"radio\" " 
      + "style=\"vertical-align:middle\" name=\"group1\" " 
      + "checked=\"checked\" disabled=\"disabled\" value=\"Push Me\" />" 
      + "<span class=\"w4tCsscd1f6403\" dir=\"myDir\" lang=\"myLang\" " 
      + "title=\"myTitle\">The Label</span></span>--></envelope>";
    assertEquals( expected, markup );
    // set the group to 'not updatable'
    group.setUpdatable( false );
    radioButton.setEnabled( true );
    writer = new HtmlResponseWriter();
    Fixture.setResponseWriter( writer );
    Fixture.renderComponent( radioButton );
    markup = Fixture.getBodyMarkup( writer );
    expected 
      = "<envelope id=\"radio1\"><!--" 
      + "<span id=\"radio1\">" 
      + "<input type=\"radio\" style=\"vertical-align:middle\" name=\"group1\" " 
      + "checked=\"checked\" disabled=\"disabled\" value=\"Push Me\" " 
      + "onclick=\"eventHandler.webItemStateChanged(this)\" />" 
      + "<span class=\"w4tCsscd1f6403\" dir=\"myDir\" lang=\"myLang\" " 
      + "title=\"myTitle\">The Label</span></span>--></envelope>";
    assertEquals( expected, markup );
  }

  public void testScriptRenderer() throws Exception {
    WebRadioButton radioButton = new WebRadioButton();
    Fixture.setWebComponentUniqueId( radioButton, "radio1" );
    WebRadioButtonGroup group = new WebRadioButtonGroup();
    Fixture.setWebComponentUniqueId( group, "group1" );
    group.add( radioButton );
    radioButton.setLabel( "The Label" );
    radioButton.setValue( "Push Me" );
    HtmlResponseWriter writer = new HtmlResponseWriter();
    Fixture.fakeBrowser( new Default( true ) );
    Fixture.setResponseWriter( writer );
    Fixture.renderComponent( radioButton );
    String markup = Fixture.getBodyMarkup( writer );
    String expected;
    expected 
      = "<span id=\"radio1\"><input type=\"radio\" "
      + "style=\"vertical-align:middle\" name=\"group1\" "
      + "value=\"Push Me\">"
      + "<span class=\"w4tCsscd1f6403\">" 
      + "The Label</span></span>";
    assertEquals( expected, markup );
    
    //with listeners
    group.addWebItemListener( new WebItemListener() {
      public void webItemStateChanged( WebItemEvent e ) {
      }
    } );
    group.setValue( radioButton.getValue() );
    radioButton.setEnabled( true );
    writer = new HtmlResponseWriter();
    Fixture.fakeBrowser( new Default( true, false ) );
    Fixture.setResponseWriter( writer );
    Fixture.renderComponent( radioButton );
    markup = Fixture.getBodyMarkup( writer );
    expected 
            = "<span id=\"radio1\"><input type=\"radio\" "
            + "style=\"vertical-align:middle\" name=\"group1\" "
            + "checked value=\"Push Me\" "
            + "onclick=\"eventHandler.webItemStateChanged(this)\">"
            + "<span class=\"w4tCsscd1f6403\">" 
            + "The Label</span></span>";
    assertEquals( expected, markup );
    // set the group to 'not updatable'
    group.setUpdatable( false );
    radioButton.setEnabled( true );
    writer = new HtmlResponseWriter();
    Fixture.setResponseWriter( writer );
    Fixture.renderComponent( radioButton );
    markup = Fixture.getBodyMarkup( writer );
    expected 
      = "<span id=\"radio1\">" 
      + "<input type=\"radio\" style=\"vertical-align:middle\" name=\"group1\" " 
      + "checked disabled value=\"Push Me\" " 
      + "onclick=\"eventHandler.webItemStateChanged(this)\">" 
      + "<span class=\"w4tCsscd1f6403\">The Label</span></span>";
    assertEquals( expected, markup );
  }
  
  public void testNoScriptRenderer() throws Exception {
    WebRadioButton radioButton = new WebRadioButton();
    Fixture.setWebComponentUniqueId( radioButton, "radio1" );
    // render without group: must not be rendered, it is considered invisible
    HtmlResponseWriter writer = new HtmlResponseWriter();
    Fixture.fakeBrowser( new Default( false ) );
    Fixture.setResponseWriter( writer );
    Fixture.renderComponent( radioButton );
    String markup = Fixture.getBodyMarkup( writer );
    String expected 
      = "<input type=\"radio\" style=\"vertical-align:middle\" " 
      + "name=\"radio1\" disabled value=\"p1\">" 
      + "<span class=\"w4tCsscd1f6403\"></span>";
    assertEquals( expected, markup );
    // render 'normal' radioButton with most of its attributes set
    radioButton.setLabel( "The Label" );
    radioButton.setValue( "Push Me" );
    radioButton.setDir( "myDir" );
    radioButton.setLang( "myLang" );
    radioButton.setTitle( "myTitle" );
    WebRadioButtonGroup group = new WebRadioButtonGroup();
    Fixture.setWebComponentUniqueId( group, "group1" );
    group.setValue( "Push Me" );
    group.addWebItemListener( new WebItemListener() {
      public void webItemStateChanged( WebItemEvent e ) {
      }
    } );
    group.add( radioButton );
    writer = new HtmlResponseWriter();
    Fixture.fakeBrowser( new Default( false ) );
    Fixture.setResponseWriter( writer );
    Fixture.renderComponent( radioButton );
    markup = Fixture.getBodyMarkup( writer );
    expected 
      = "<input type=\"radio\" "
      + "style=\"vertical-align:middle\" "
      + "name=\"group1\" "
      + "checked value=\"Push Me\">"
      + "<span class=\"w4tCsscd1f6403\" " 
      + "dir=\"myDir\" "
      + "lang=\"myLang\" title=\"myTitle\">"
      + "The Label</span>"
      + "<input type=\"image\" src=\"resources/images/submitter.gif\" "
      + "name=\"wiegroup1\" border=\"0\">";
    assertEquals( expected, markup );

    Fixture.fakeBrowser( new Default( false ) );
    Fixture.setResponseWriter( writer );
    radioButton.setEnabled( false );
    Fixture.renderComponent( radioButton );
    markup = Fixture.getBodyMarkup( writer );
    expected 
      = "<input type=\"radio\" "
      + "style=\"vertical-align:middle\" "
      + "name=\"group1\" "
      + "checked "
      + "value=\"Push Me\">"
      + "<span class=\"w4tCsscd1f6403\" " 
      + "dir=\"myDir\" "
      + "lang=\"myLang\" title=\"myTitle\">"
      + "The Label</span>"
      + "<input type=\"image\" src=\"resources/images/submitter.gif\" "
      + "name=\"wiegroup1\" border=\"0\">"
      + "<input type=\"radio\" style=\"vertical-align:middle\" " 
      + "name=\"group1\" checked "
      + "disabled value=\"Push Me\"><span "
      + "class=\"w4tCsscd1f6403\" dir=\"myDir\" lang=\"myLang\" "
      + "title=\"myTitle\">The Label</span>";
    assertEquals( expected, markup );
    // set the group to 'not updatable'
    group.setUpdatable( false );
    radioButton.setEnabled( true );
    writer = new HtmlResponseWriter();
    Fixture.setResponseWriter( writer );
    Fixture.renderComponent( radioButton );
    markup = Fixture.getBodyMarkup( writer );
    expected 
      = "<input type=\"radio\" style=\"vertical-align:middle\" name=\"group1\" " 
      + "checked disabled value=\"Push Me\"><span class=\"w4tCsscd1f6403\" "
      + "dir=\"myDir\" lang=\"myLang\" title=\"myTitle\">The Label</span>"; 
    assertEquals( expected, markup );
  }
  
  public void testRenderName() throws IOException {
    HtmlResponseWriter writer;
    // render id of radioButton for the name attribute if no group  
    writer = new HtmlResponseWriter();
    writer.startElement( HTML.INPUT, null );
    WebRadioButton radioButton1 = new WebRadioButton();
    RadioButtonUtil.renderName( writer, radioButton1 );
    String expected = "<input name=\"" + radioButton1.getUniqueID() + "\"";
    assertEquals( expected, Fixture.getAllMarkup( writer ) );
    // render id of group for the name attribute
    writer = new HtmlResponseWriter();
    writer.startElement( HTML.INPUT, null );
    WebRadioButton radioButton2 = new WebRadioButton();
    WebRadioButtonGroup group = new WebRadioButtonGroup();
    group.add( radioButton2 );
    RadioButtonUtil.renderName( writer, radioButton2 );
    expected = "<input name=\"" + group.getUniqueID() + "\"";
    assertEquals( expected, Fixture.getAllMarkup( writer ) );
  }
  
  public void testCreateEventHandler() throws IOException {
    // prepare
    HtmlResponseWriter writer;
    WebRadioButton radioButton1;
    Fixture.fakeBrowser( new Default( true ) );
    WebRadioButtonGroup group = new WebRadioButtonGroup();
    // no event handler at all
    writer = new HtmlResponseWriter();
    Fixture.setResponseWriter( writer );
    radioButton1 = new WebRadioButton();
    group.add( radioButton1 );
    writer.startElement( HTML.INPUT, null );
    RadioButtonUtil.createEventHandler( radioButton1 );
    String expected = "<input";
    assertEquals( expected, Fixture.getAllMarkup( writer ) );
    // focus listener only
    writer = new HtmlResponseWriter();
    Fixture.setResponseWriter( writer );
    radioButton1.addWebFocusGainedListener( new WebFocusGainedListener() {
      public void webFocusGained( WebFocusGainedEvent e ) {
      }
    } );
    writer.startElement( HTML.INPUT, null );
    RadioButtonUtil.createEventHandler( radioButton1 );
    expected 
      = "<input " 
      + "onfocus=\"eventHandler.setFocusID(this);" 
      + "eventHandler.webFocusGained(this)\"";
    assertEquals( expected, Fixture.getAllMarkup( writer ) );
    // itemState- and focus-listener
    group.addWebItemListener( new WebItemListener() {
      public void webItemStateChanged( WebItemEvent e ) {
      }
    } );
    writer = new HtmlResponseWriter();
    Fixture.setResponseWriter( writer );
    writer.startElement( HTML.INPUT, null );
    RadioButtonUtil.createEventHandler( radioButton1 );
    expected 
      = "<input onclick=\"eventHandler.webItemStateChanged(this)\" " 
      + "onfocus=\"eventHandler.setFocusID(this);" 
      + "eventHandler.webFocusGained(this)\"";
    assertEquals( expected, Fixture.getAllMarkup( writer ) );
  }
  
  public void testRenderDisplay() throws IOException {
    WebRadioButton radioButton = new WebRadioButton();
    radioButton.setValue( "value1" );
    Fixture.fakeBrowser( new Default( false ) );
    HtmlResponseWriter writer;
    // render radioButton display with empty label
    writer = new HtmlResponseWriter();
    Fixture.setResponseWriter( writer );
    RadioButtonUtil.renderDisplay( radioButton );
    String expected = "<span class=\"w4tCsscd1f6403\"></span>"; 
    assertEquals( expected, Fixture.getAllMarkup( writer ) );
    // render radioButton display with some label
    radioButton.setLabel( "theLabel" );
    writer = new HtmlResponseWriter();
    Fixture.setResponseWriter( writer );
    RadioButtonUtil.renderDisplay( radioButton );
    expected = "<span class=\"w4tCsscd1f6403\">theLabel</span>"; 
    assertEquals( expected, Fixture.getAllMarkup( writer ) );
  }
  
  public void testAjaxStatusAfterReadData() {
    WebForm form = Fixture.getEmptyWebFormInstance();
    WebRadioButtonGroup group = new WebRadioButtonGroup();
    form.add( group, WebBorderLayout.CENTER );
    WebRadioButton radio1 = new WebRadioButton();
    radio1.setValue( "1" );
    group.add( radio1 );
    WebRadioButton radio2 = new WebRadioButton();
    radio2.setValue( "2" );
    group.add( radio2 );
    group.setValue( "1" );
    Fixture.fakeBrowser( new Ie6( true, true ) );
    Fixture.fakeRequestParam( group.getUniqueID(), "2" );
    RendererCache rendererCache = RendererCache.getInstance();
    AjaxStatusUtil.preRender( form );
    AjaxStatusUtil.postRender( form );
    Renderer renderer = rendererCache.retrieveRenderer( group.getClass() );
    assertEquals( WebRadioButtonGroupRenderer_Default_Ajax.class, 
                  renderer.getClass() );
    // this ensures that *only* the groups' ajax-status is updated, not radio1's
    radio1.setLabel( "anotherLabel" );  
    renderer.readData( group );
    assertEquals( "2", group.getValue() );
    AjaxStatusUtil.preRender( form );
    assertEquals( false, Fixture.getAjaxStatus( group ).mustRender() );
    assertEquals( true, Fixture.getAjaxStatus( radio1 ).mustRender() );
  }
  
  protected void setUp() throws Exception {
    Fixture.setUp();
    Fixture.createContext();
  }
  
  protected void tearDown() throws Exception {
    Fixture.tearDown();
    Fixture.removeContext();
  }
  
}
