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
package com.w4t.webbuttonkit;

import junit.framework.TestCase;
import com.w4t.*;
import com.w4t.engine.service.ContextProvider;
import com.w4t.event.*;
import com.w4t.util.IInitialization;
import com.w4t.util.browser.Default;
import com.w4t.util.browser.Ie5up;
import com.w4t.util.image.ImageCache;


/** <p>Unit tests for WebButtonRenderer.</p> */
public class WebButtonRenderer_Test extends TestCase {
 
  public void testLinkButton_Ajax() throws Exception {
    WebButton button = new WebButton();
    button.setEnabled( true );
    button.setPrint( false );
    button.setLink( true );
    button.setLabel( "Push Me" );
    button.setDir( "myDir" );
    button.setLang( "myLang" );
    button.setTitle( "myTitle" );
    button.setReset( true );
    Fixture.setWebComponentUniqueId( button, "button1" );
    Fixture.fakeBrowser( new Default( true, true ) );
    HtmlResponseWriter writer = new HtmlResponseWriter();
    setResponseWriter( writer );
    Fixture.scheduleForRenderInAJAX( button );
    Fixture.renderComponent( button );
    String markup = getBodyMarkup( writer );
    String expected 
          = "<span id=\"button1\" " 
          + "class=\"w4tCsscd1f6403\" "
          + "dir=\"myDir\" lang=\"myLang\" title=\"myTitle\">"
          + "Push Me</span>";
    assertEquals( expected, markup );
    
    // add action listener and set UseTrim tp false
    button.addWebActionListener( new WebActionListener() {
      public void webActionPerformed( final WebActionEvent e ) {
      }
    });
//    button.setEnabled( false );
    button.setUseTrim( false );
    writer = new HtmlResponseWriter();
    setResponseWriter( writer );
    Fixture.renderComponent( button );
    markup = getBodyMarkup( writer );
    expected 
    = "&nbsp;<a id=\"button1\" "
      + "href=\"javascript:" 
      + "eventHandler.resetForm();"
      + "eventHandler.webActionPerformed(\'button1\')\" "
      + "onfocus=\"eventHandler.setFocusID(\'button1\')\" " 
      + "onmousedown=\"eventHandler.suspendSubmit()\" "
      + "onmouseout=\"eventHandler.resumeSubmit()\" "
      + "onmouseup=\"eventHandler.resumeSubmit()\" " 
      + "class=\"w4tCsscd1f6403\""
      + " dir=\"myDir\" lang=\"myLang\" title=\"myTitle\">"
      + "Push Me</a>&nbsp;";
    assertEquals( expected, markup );
  }
  
  public void testButtonButton_Ajax() throws Exception {
    WebButton button = new WebButton();
    button.setEnabled( true );
    button.setPrint( false );
    button.setLink( false );
    button.setLabel( "Push Me" );
    button.setDir( "myDir" );
    button.setLang( "myLang" );
    button.setTitle( "myTitle" );
    Fixture.setWebComponentUniqueId( button, "button1" );
    Fixture.fakeBrowser( new Default( true, true ) );
    HtmlResponseWriter writer = new HtmlResponseWriter();
    setResponseWriter( writer );
    Fixture.scheduleForRenderInAJAX( button );
    Fixture.renderComponent( button );
    String markup = getBodyMarkup( writer );
    String expected 
    = "<input id=\"button1\" " 
      + "type=\"button\" name=\"button1\" value=\"Push Me\" "
      + "class=\"w4tCsscd1f6403\" "
      + "dir=\"myDir\" lang=\"myLang\" title=\"myTitle\" "
      + "onfocus=\"eventHandler.setFocusID(this)\">";
    assertEquals( expected, markup );
    
    // add action listener
    button.addWebActionListener( new WebActionListener() {
      public void webActionPerformed( final WebActionEvent e ) {
      }
    });
    writer = new HtmlResponseWriter();
    setResponseWriter( writer );
    Fixture.scheduleForRenderInAJAX( button );
    Fixture.renderComponent( button );
    markup = getBodyMarkup( writer );
    expected 
    = "<input id=\"button1\" " 
      + "type=\"button\" name=\"button1\" value=\"Push Me\" "
      + "class=\"w4tCsscd1f6403\"" 
      + " dir=\"myDir\" lang=\"myLang\" title=\"myTitle\" "
      + "onclick=\"eventHandler.webActionPerformed(this)\" " 
      + "onfocus=\"eventHandler.setFocusID(this)\" " 
      + "onmousedown=\"eventHandler.suspendSubmit()\" " 
      + "onmouseout=\"eventHandler.resumeSubmit()\" " 
      + "onmouseup=\"eventHandler.resumeSubmit()\">"; 
    assertEquals( expected, markup );
  }
  
  public void testImageButton_Ajax() throws Exception {
    WebButton button = new WebButton();
    button.setEnabled( true );
    button.setPrint( false );
    button.setLink( true );
    button.setLabel( "Push Me" );
    button.setImage( "myImage" );
    button.setDir( "myDir" );
    button.setLang( "myLang" );
    button.setTitle( "myTitle" );
    Fixture.setWebComponentUniqueId( button, "button1" );
    Fixture.fakeBrowser( new Default( true, true ) );
    HtmlResponseWriter writer = new HtmlResponseWriter();
    Fixture.scheduleForRenderInAJAX( button );
    setResponseWriter( writer );
    Fixture.renderComponent( button );
    String markup = getBodyMarkup( writer );
    String expected 
    = "<span id=\"button1\" class=\"w4tCsscd1f6403\""     
      + " dir=\"myDir\" lang=\"myLang\" title=\"myTitle\">"
      + "<img src=\"myImage\" alt=\"myTitle\" border=\"0\"></span>";
    assertEquals( expected, markup );
    
    // add action listener
    button.addWebActionListener( new WebActionListener() {
      public void webActionPerformed( final WebActionEvent e ) {
      }
    });
    writer = new HtmlResponseWriter();
    setResponseWriter( writer );
    Fixture.scheduleForRenderInAJAX( button );
    Fixture.renderComponent( button );
    markup = getBodyMarkup( writer );
    expected 
    = "<a id=\"button1\" "
      + "href=\"javascript:eventHandler.webActionPerformed(\'button1\')\" "
      + "onfocus=\"eventHandler.setFocusID(\'button1\')\" " 
      + "onmousedown=\"eventHandler.suspendSubmit()\" "
      + "onmouseout=\"eventHandler.resumeSubmit()\" "
      + "onmouseup=\"eventHandler.resumeSubmit()\" " 
      + "class=\"w4tCsscd1f6403\""
      + " dir=\"myDir\" lang=\"myLang\" title=\"myTitle\">"
      + "<img src=\"myImage\" alt=\"myTitle\" border=\"0\">"
      + "</a>";
    assertEquals( expected, markup );
  }
  
  public void testLinkButton_Script() throws Exception {
    WebButton button = new WebButton();
    button.setEnabled( true );
    button.setPrint( false );
    button.setLink( true );
    button.setLabel( "Push Me" );
    button.setDir( "myDir" );
    button.setLang( "myLang" );
    button.setTitle( "myTitle" );
    Fixture.setWebComponentUniqueId( button, "button1" );
    Fixture.fakeBrowser( new Ie5up( true, false ) );
    HtmlResponseWriter writer = new HtmlResponseWriter();
    setResponseWriter( writer );
    Fixture.scheduleForRenderInAJAX( button );
    Fixture.renderComponent( button );
    String markup = getBodyMarkup( writer );
    String expected 
        = "<span id=\"button1\" class=\"w4tCsscd1f6403\"" 
        + " dir=\"myDir\" lang=\"myLang\" title=\"myTitle\">"
        + "Push Me</span>";
    assertEquals( expected, markup );
    
    // add action listener
    button.addWebActionListener( new WebActionListener() {
      public void webActionPerformed( final WebActionEvent e ) {
      }
    });
    button.addWebFocusGainedListener( new WebFocusGainedListener() {
      public void webFocusGained( final WebFocusGainedEvent e )
      {
      }
    });
    writer = new HtmlResponseWriter();
    setResponseWriter( writer );
    Fixture.scheduleForRenderInAJAX( button );
    Fixture.renderComponent( button );
    markup = getBodyMarkup( writer );
    expected 
      =   "<a id=\"button1\" " 
        + "href=\"javascript:eventHandler.webActionPerformed(\'button1\')\" "
        + "onfocus=\"eventHandler.setFocusID(\'button1\');"
        + "eventHandler.webFocusGained('button1')\" "
        + "onmousedown=\"eventHandler.suspendSubmit()\" "
        + "onmouseout=\"eventHandler.resumeSubmit()\" "
        + "onmouseup=\"eventHandler.resumeSubmit()\" " 
        + "class=\"w4tCsscd1f6403\""
        + " dir=\"myDir\" lang=\"myLang\" title=\"myTitle\">"
        + "Push Me</a>";

    assertEquals( expected, markup );
  }
  
  public void testButtonButton_Script() throws Exception {
    WebButton button = new WebButton();
    button.setEnabled( true );
    button.setPrint( false );
    button.setLink( false );
    button.setLabel( "Push Me" );
    button.setDir( "myDir" );
    button.setLang( "myLang" );
    button.setTitle( "myTitle" );
    Fixture.setWebComponentUniqueId( button, "button1" );
    Fixture.fakeBrowser( new Ie5up( true, false ) );
    HtmlResponseWriter writer = new HtmlResponseWriter();
    setResponseWriter( writer );
    Fixture.scheduleForRenderInAJAX( button );
    Fixture.renderComponent( button );
    String markup = getBodyMarkup( writer );
    String expected 
      =   "<input id=\"button1\" type=\"button\" " 
        + "name=\"button1\" value=\"Push Me\" "
        + "class=\"w4tCsscd1f6403\""
        + " dir=\"myDir\" lang=\"myLang\" title=\"myTitle\" "
        + "onfocus=\"eventHandler.setFocusID(this)\" />";
    assertEquals( expected, markup );
    
    // add action listener
    button.addWebActionListener( new WebActionListener() {
      public void webActionPerformed( final WebActionEvent e ) {
      }
    });
    writer = new HtmlResponseWriter();
    setResponseWriter( writer );
    Fixture.renderComponent( button );
    markup = getBodyMarkup( writer );
    expected 
      =   "<input id=\"button1\" type=\"button\" " 
        + "name=\"button1\" value=\"Push Me\" "
        + "class=\"w4tCsscd1f6403\"" 
        + " dir=\"myDir\" lang=\"myLang\" title=\"myTitle\" "
        + "onclick=\"eventHandler.webActionPerformed(this)\" " 
        + "onfocus=\"eventHandler.setFocusID(this)\" " 
        + "onmousedown=\"eventHandler.suspendSubmit()\" " 
        + "onmouseout=\"eventHandler.resumeSubmit()\" " 
        + "onmouseup=\"eventHandler.resumeSubmit()\" />"; 
    assertEquals( expected, markup );
  }
  
  public void testImageButton_Script() throws Exception {
    WebButton button = new WebButton();
    button.setEnabled( true );
    button.setPrint( false );
    button.setLink( true );
    button.setLabel( "Push Me" );
    button.setImage( "myImage" );
    button.setDir( "myDir" );
    button.setLang( "myLang" );
    button.setTitle( "myTitle" );
    Fixture.setWebComponentUniqueId( button, "button1" );
    Fixture.fakeBrowser( new Ie5up( true, false ) );
    HtmlResponseWriter writer = new HtmlResponseWriter();
    setResponseWriter( writer );
    Fixture.scheduleForRenderInAJAX( button );
    Fixture.renderComponent( button );
    String markup = getBodyMarkup( writer );
    String expected 
        = "<span id=\"button1\" class=\"w4tCsscd1f6403\""
        + " dir=\"myDir\" lang=\"myLang\" title=\"myTitle\">"
        + "<img src=\"myImage\" alt=\"myTitle\" border=\"0\" /></span>";
    assertEquals( expected, markup );
    
    // add action listener
    button.addWebActionListener( new WebActionListener() {
      public void webActionPerformed( final WebActionEvent e ) {
      }
    });
    writer = new HtmlResponseWriter();
    setResponseWriter( writer );
    Fixture.renderComponent( button );
    markup = getBodyMarkup( writer );
    expected 
        =  "<a id=\"button1\" "
        + "href=\"javascript:eventHandler.webActionPerformed('button1')\" " 
        + "onfocus=\"eventHandler.setFocusID('button1')\" "
        + "onmousedown=\"eventHandler.suspendSubmit()\" "
        + "onmouseout=\"eventHandler.resumeSubmit()\" " 
        + "onmouseup=\"eventHandler.resumeSubmit()\" class=\"w4tCsscd1f6403\""
        + " dir=\"myDir\" lang=\"myLang\" title=\"myTitle\">"
        + "<img src=\"myImage\" alt=\"myTitle\" border=\"0\" /></a>";
    assertEquals( expected, markup );
  }

  public void testPrintButton_Script() throws Exception {
    WebButton button = new WebButton();
    button.setEnabled( true );
    button.setPrint( true );
    button.setLabel( "Push Me" );
    button.setImage( "myImage" );
    button.setDir( "myDir" );
    button.setLang( "myLang" );
    button.setTitle( "myTitle" );
    Fixture.setWebComponentUniqueId( button, "button1" );
    Fixture.fakeBrowser( new Ie5up( true, false ) );
    HtmlResponseWriter writer = new HtmlResponseWriter();
    setResponseWriter( writer );
    Fixture.scheduleForRenderInAJAX( button );
    Fixture.renderComponent( button );
    String markup = getBodyMarkup( writer );
    String expected 
      = "<input id=\"button1\" type=\"button\" name=\"button1\" "
        + "value=\"Push Me\" class=\"w4tCsscd1f6403\" "
        + "dir=\"myDir\" lang=\"myLang\" title=\"myTitle\" "
        + "onclick=\"windowManager.printPage()\" "
        + "onmousedown=\"eventHandler.suspendSubmit()\" />";
    assertEquals( expected, markup );
    
    // add action listener
    button.addWebActionListener( new WebActionListener() {
      public void webActionPerformed( final WebActionEvent e ) {
      }
    });
    writer = new HtmlResponseWriter();
    setResponseWriter( writer );
    Fixture.renderComponent( button );
    markup = getBodyMarkup( writer );
    expected 
      = "<input id=\"button1\" type=\"button\" "
      + "name=\"button1\" value=\"Push Me\" class=\"w4tCsscd1f6403\" "
      + "dir=\"myDir\" lang=\"myLang\" title=\"myTitle\" "
      + "onclick=\"windowManager.printPage()\" "
      + "onmousedown=\"eventHandler.suspendSubmit()\" />";
    assertEquals( expected, markup );
  }
  
  public void testLinkButton_NoScript() throws Exception {
    WebButton button = new WebButton();
    button.setEnabled( true );
    button.setPrint( false );
    button.setLink( true );
    button.setLabel( "Push Me" );
    button.setDir( "myDir" );
    button.setLang( "myLang" );
    button.setTitle( "myTitle" );
    Fixture.setWebComponentUniqueId( button, "button1" );
    Fixture.fakeBrowser( new Default( false, false ) );
    HtmlResponseWriter writer = new HtmlResponseWriter();
    setResponseWriter( writer );
    Fixture.scheduleForRenderInAJAX( button );
    Fixture.renderComponent( button );
    String markup = getBodyMarkup( writer );
    String expected 
      =   "<span class=\"w4tCsscd1f6403\"" 
      + " dir=\"myDir\" lang=\"myLang\" title=\"myTitle\">"
      + "Push Me</span>";
    assertEquals( expected, markup );
    
    // add action listener
    button.addWebActionListener( new WebActionListener() {
      public void webActionPerformed( final WebActionEvent e ) {
      }
    });
    button.setUseTrim( false );
    writer = new HtmlResponseWriter();
    setResponseWriter( writer );
    ImageCache.createInstance( Fixture.getWebAppBase().toString(), 
                               IInitialization.NOSCRIPT_SUBMITTERS_NONE );
    Fixture.scheduleForRenderInAJAX( button );
    Fixture.renderComponent( button );
    markup = getBodyMarkup( writer );
    expected 
      = "&nbsp;<span class=\"w4tCsscd1f6403\""
      + " dir=\"myDir\" lang=\"myLang\" title=\"myTitle\">"
      + "Push Me</span>&nbsp;"
      + "<input type=\"image\" " 
      + "src=\"resources/images/submitter.gif\" "
      + "name=\"waebutton1\" "
      + "border=\"0\">";
    assertEquals( expected, markup );
  }
  
  public void testButtonButton_NoScript() throws Exception {
    WebButton button = new WebButton();
    button.setEnabled( true );
    button.setPrint( false );
    button.setLink( false );
    button.setLabel( "Push Me" );
    button.setDir( "myDir" );
    button.setLang( "myLang" );
    button.setTitle( "myTitle" );
    Fixture.setWebComponentUniqueId( button, "button1" );
    Fixture.fakeBrowser( new Default( false, false ) );
    HtmlResponseWriter writer = new HtmlResponseWriter();
    setResponseWriter( writer );
    Fixture.scheduleForRenderInAJAX( button );
    Fixture.renderComponent( button );
    String markup = getBodyMarkup( writer );
    String expected 
      = "<input type=\"submit\" " 
      + "name=\"button1\" value=\"Push Me\" "
      + "class=\"w4tCsscd1f6403\""
      + " dir=\"myDir\" lang=\"myLang\" title=\"myTitle\">";
    assertEquals( expected, markup );
    
    // add action listener
    button.addWebActionListener( new WebActionListener() {
      public void webActionPerformed (final WebActionEvent e ) {
      }
    });
    writer = new HtmlResponseWriter();
    setResponseWriter( writer );
    Fixture.renderComponent( button );
    markup = getBodyMarkup( writer );
    expected 
      = "<input type=\"submit\" name=\"waebutton1\" "
      + "value=\"Push Me\" class=\"w4tCsscd1f6403\""
      + " dir=\"myDir\" lang=\"myLang\" title=\"myTitle\">";
      assertEquals( expected, markup );
  }

  public void testPrintButton_NoScript() throws Exception {
    WebButton button = new WebButton();
    button.setEnabled( true );
    button.setPrint( true );
    button.setLink( false );
    button.setLabel( "Push Me" );
    button.setDir( "myDir" );
    button.setLang( "myLang" );
    button.setTitle( "myTitle" );
    Fixture.setWebComponentUniqueId( button, "button1" );
    Fixture.fakeBrowser( new Default( false, false ) );
    HtmlResponseWriter writer = new HtmlResponseWriter();
    setResponseWriter( writer );
    Fixture.scheduleForRenderInAJAX( button );
    Fixture.renderComponent( button );
    String markup = getBodyMarkup( writer );
    String expected 
    = "<input type=\"submit\" " 
      + "name=\"button1\" value=\"Push Me\" "
      + "class=\"w4tCsscd1f6403\""
      + " dir=\"myDir\" lang=\"myLang\" title=\"myTitle\">";
    assertEquals( expected, markup );
    
    // add action listener
    button.addWebActionListener( new WebActionListener() {
      public void webActionPerformed (final WebActionEvent e ) {
      }
    });
    writer = new HtmlResponseWriter();
    setResponseWriter( writer );
    Fixture.renderComponent( button );
    markup = getBodyMarkup( writer );
    expected 
    = "<input type=\"submit\" name=\"waebutton1\" "
      + "value=\"Push Me\" class=\"w4tCsscd1f6403\""
      + " dir=\"myDir\" lang=\"myLang\" title=\"myTitle\">";
    assertEquals( expected, markup );
  }
  
  public void testImageButton_NoScript() throws Exception {
    WebButton button = new WebButton();
    button.setEnabled( true );
    button.setPrint( false );
    button.setLink( true );
    button.setLabel( "Push Me" );
    button.setImage( "myImage" );
    button.setDir( "myDir" );
    button.setLang( "myLang" );
    button.setTitle( "myTitle" );
    Fixture.setWebComponentUniqueId( button, "button1" );
    Fixture.fakeBrowser( new Default( false, false ) );
    HtmlResponseWriter writer = new HtmlResponseWriter();
    setResponseWriter( writer );
    Fixture.scheduleForRenderInAJAX( button );
    Fixture.renderComponent( button );
    String markup = getBodyMarkup( writer );
    String expected 
        = "<span class=\"w4tCsscd1f6403\""
        + " dir=\"myDir\" lang=\"myLang\" title=\"myTitle\">"
        + "<img src=\"myImage\" alt=\"myTitle\" border=\"0\"></span>";
    assertEquals( expected, markup );
    
    // add action listener
    button.addWebActionListener( new WebActionListener() {
      public void webActionPerformed( final WebActionEvent e ) {
      }
    });
    writer = new HtmlResponseWriter();
    setResponseWriter( writer );
    Fixture.renderComponent( button );
    markup = getBodyMarkup( writer );
    expected 
      = "<input type=\"image\" src=\"myImage\" "
      + "name=\"waebutton1\" border=\"0\">";
    assertEquals( expected, markup );
  }
  
  public void testEncoding() throws Exception {
    Fixture.fakeBrowser( new Default( true, false ) );
    WebButton button = new WebButton();
    button.setLabel( "Ümläute & \"ß\"" );
    button.setTitle( "This title contains \"Ümläute & ß\"" );
    HtmlResponseWriter writer = new HtmlResponseWriter();
    setResponseWriter( writer );
    Fixture.renderComponent( button );
    String markup = getBodyMarkup( writer );
    String expected 
      =  "<input id=\"p1\" type=\"button\" name=\"p1\" "
      + "value=\"&Uuml;ml&auml;ute &amp; &quot;&szlig;&quot;\" " 
      + "class=\"w4tCsscd1f6403\" "
      + "title=\"This title contains &quot;&Uuml;ml&auml;ute &amp; "
        + "&szlig;&quot;\" "
      + "onfocus=\"eventHandler.setFocusID(this)\">";
    assertEquals( expected, markup );
    button.setLink( true );
    writer = new HtmlResponseWriter();
    setResponseWriter( writer );
    Fixture.renderComponent( button );
    markup = getBodyMarkup( writer );
    expected 
      =  "<span id=\"p1\" class=\"w4tCsscd1f6403\" "
      + "title=\"This title contains &quot;&Uuml;ml&auml;ute &amp; "
      + "&szlig;&quot;\">&Uuml;ml&auml;ute &amp; &quot;&szlig;&quot;</span>";
    assertEquals( expected, markup );
    button.setLink( true );
    button.setPrint( true );
    writer = new HtmlResponseWriter();
    setResponseWriter( writer );
    Fixture.renderComponent( button );
    markup = getBodyMarkup( writer );
    expected 
      =  "<input id=\"p1\" type=\"button\" name=\"p1\" " 
      + "value=\"&Uuml;ml&auml;ute &amp; &quot;&szlig;&quot;\" " 
      + "class=\"w4tCsscd1f6403\" " 
      + "title=\"This title contains &quot;&Uuml;ml&auml;ute &amp; " 
      + "&szlig;&quot;\" " 
      + "onclick=\"windowManager.printPage()\" " 
      + "onmousedown=\"eventHandler.suspendSubmit()\">";
    assertEquals( expected, markup );
  }
  
  
  public void testDisabledLinkNoscript() throws Exception {
    Fixture.fakeBrowser( new Default( false, false ) );
    WebButton button = new WebButton();
    button.setLink( true );
    button.setUseTrim( false );
    HtmlResponseWriter writer = new HtmlResponseWriter();
    setResponseWriter( writer );
    Fixture.renderComponent( button );
    String markup = Fixture.getAllMarkup( writer );
    assertEquals( "&nbsp;<span class=\"w4tCsscd1f6403\"></span>&nbsp;", 
                  markup );
  }
  
  protected void setUp() throws Exception {
    Fixture.setUp();
    Fixture.createContext();
  }
  
  protected void tearDown() throws Exception {
    Fixture.tearDown();
    ImageCache cache = ImageCache.getInstance();
    if( cache != null ) {
      Fixture.setPrivateField( ImageCache.class, cache, "_instance", null );
    }
    Fixture.removeContext();
  }
  
  private static void setResponseWriter( final HtmlResponseWriter writer ) {
    ContextProvider.getStateInfo().setResponseWriter( writer );
  }
  
  private static String getBodyMarkup( final HtmlResponseWriter writer ) {
    StringBuffer buffer = new StringBuffer();
    for( int i = 0; i < writer.getBodySize(); i++ ) {
      buffer.append( writer.getBodyToken( i ) );
    }
    return buffer.toString();
  }
}
