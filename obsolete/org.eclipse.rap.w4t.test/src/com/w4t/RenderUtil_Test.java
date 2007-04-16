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
package com.w4t;

import java.io.IOException;
import com.w4t.IWindowManager.IWindow;
import com.w4t.engine.service.ContextProvider;
import com.w4t.engine.service.IServiceStateInfo;
import com.w4t.engine.util.FormManager;
import com.w4t.engine.util.WindowManager;
import com.w4t.types.WebColor;
import com.w4t.util.browser.*;

/**
 * <p>Tests the static helping functionality in com.w4t.ReenderUtil.</p>
 */
public class RenderUtil_Test extends RenderingTestCase {

  public RenderUtil_Test( final String name ) {
    super( name );
    setGenerateResources( false );
  }

  protected void setUp() throws Exception {
    Fixture.setUp();
    Fixture.createContext();
  }

  public void tearDown() throws Exception {
    Fixture.tearDown();
    Fixture.removeContext();
  }
  
  public void testSplitLinebreak() throws Exception {
    String text1 = "test";
    String[] result1 = RenderUtil.splitLineBreak( text1 );
    assertEquals( 1, result1.length );
    assertEquals( text1, result1[ 0 ] );
    
    String text2 = "\ntest";
    String[] result2 = RenderUtil.splitLineBreak( text2 );
    assertEquals( 2, result2.length );
    assertEquals( "\n", result2[ 0 ] );
    assertEquals( "test", result2[ 1 ] );
    
    String text3 = "te\nst";
    String[] result3 = RenderUtil.splitLineBreak( text3 );
    assertEquals( 3, result3.length );
    assertEquals( "te", result3[ 0 ] );
    assertEquals( "\n", result3[ 1 ] );
    assertEquals( "st", result3[ 2 ] );
    
    String text4 = "test\n";
    String[] result4 = RenderUtil.splitLineBreak( text4 );
    assertEquals( 2, result4.length );
    assertEquals( "test", result4[ 0 ] );
    assertEquals( "\n", result4[ 1 ] );
    
    String text5 = "te\n\nst";
    String[] result5 = RenderUtil.splitLineBreak( text5 );
    assertEquals( 4, result5.length );
    assertEquals( "te", result5[ 0 ] );
    assertEquals( "\n", result5[ 1 ] );
    assertEquals( "\n", result5[ 2 ] );
    assertEquals( "st", result5[ 3 ] );
    
    String text6 = "te\ns\nt";
    String[] result6 = RenderUtil.splitLineBreak( text6 );
    assertEquals( 5, result6.length );
    assertEquals( "te", result6[ 0 ] );
    assertEquals( "\n", result6[ 1 ] );
    assertEquals( "s", result6[ 2 ] );
    assertEquals( "\n", result6[ 3 ] );
    assertEquals( "t", result6[ 4 ] );
    
  }

  public void testUniversalAttributeCreation() throws Exception {
    WebLabel label = new WebLabel();
    label.setValue( "xyz" );
    Fixture.setWebComponentUniqueId( label, "p1" );
    Fixture.fakeBrowser( new Default( true, false ) );
    HtmlResponseWriter writer = new HtmlResponseWriter();
    Fixture.setResponseWriter( writer );
    Fixture.renderComponent( label );
    String markup = Fixture.getBodyMarkup( writer );
    String expected = "<span id=\"p1\" class=\"w4tCsscd1f6403\">xyz</span>";
    assertEquals( expected, markup );
    label.setCssClass( "myClass" );
    label.setIgnoreLocalStyle( false );
    writer = new HtmlResponseWriter();
    Fixture.setResponseWriter( writer );
    Fixture.renderComponent( label );
    markup = Fixture.getBodyMarkup( writer );
    expected = "<span id=\"p1\" "
               + "style=\"font-family:arial,verdana;font-size:8pt;\" "
               + "class=\"myClass\">xyz</span>";
    assertEquals( expected, markup );
    Style style = new Style();
    style.setFontFamily( "" );
    style.setFontSize( -1 );
    label.setStyle( style );
    writer = new HtmlResponseWriter();
    Fixture.setResponseWriter( writer );
    Fixture.renderComponent( label );
    markup = Fixture.getBodyMarkup( writer );
    expected = "<span id=\"p1\" class=\"myClass\">xyz</span>";
    assertEquals( expected, markup );
    label.setDir( "myDirectory" );
    writer = new HtmlResponseWriter();
    Fixture.setResponseWriter( writer );
    Fixture.renderComponent( label );
    markup = Fixture.getBodyMarkup( writer );
    expected = "<span id=\"p1\" class=\"myClass\" "
               + "dir=\"myDirectory\">xyz</span>";
    assertEquals( expected, markup );
    label.setLang( "myLang" );
    writer = new HtmlResponseWriter();
    Fixture.setResponseWriter( writer );
    Fixture.renderComponent( label );
    markup = Fixture.getBodyMarkup( writer );
    expected = "<span id=\"p1\" class=\"myClass\" dir=\"myDirectory\" "
               + "lang=\"myLang\">xyz</span>";
    assertEquals( expected, markup );
    label.setTitle( "myTitle" );
    writer = new HtmlResponseWriter();
    Fixture.setResponseWriter( writer );
    Fixture.renderComponent( label );
    markup = Fixture.getBodyMarkup( writer );
    expected = "<span id=\"p1\" class=\"myClass\" dir=\"myDirectory\" "
               + "lang=\"myLang\" title=\"myTitle\">xyz</span>";
    assertEquals( expected, markup );
  }

  public void testHasUniversalAttributes() {
    WebButton button = new WebButton();
    assertTrue( RenderUtil.hasUniversalAttributes( button ) );
    button = new WebButton();
    button.setStyle( createEmptyStyle() );
    assertFalse( RenderUtil.hasUniversalAttributes( button ) );
    button = new WebButton();
    button.setStyle( createEmptyStyle() );
    button.getStyle().setBorder( "1" );
    assertTrue( RenderUtil.hasUniversalAttributes( button ) );
    button = new WebButton();
    button.setStyle( createEmptyStyle() );
    button.setCssClass( "buttonClass" );
    assertTrue( RenderUtil.hasUniversalAttributes( button ) );
    button = new WebButton();
    button.setStyle( createEmptyStyle() );
    button.setDir( "right" );
    assertTrue( RenderUtil.hasUniversalAttributes( button ) );
    button = new WebButton();
    button.setStyle( createEmptyStyle() );
    button.setLang( "en_US" );
    assertTrue( RenderUtil.hasUniversalAttributes( button ) );
    button = new WebButton();
    button.setStyle( createEmptyStyle() );
    button.setTitle( "title of button" );
    assertTrue( RenderUtil.hasUniversalAttributes( button ) );
    WebPanel panel = new WebPanel();
    panel.setWebLayout( new WebFlowLayout() );
    panel.getStyle().setBgColor( new WebColor( "#ff00aa" ) );
    assertTrue( RenderUtil.hasUniversalAttributes( panel ) );
  }
  
  public void testEncodeHTMLEntities() {
    String result;
    result = RenderUtil.encodeHTMLEntities( "-" );
    assertEquals( "-", result );
    result = RenderUtil.encodeHTMLEntities( "--" );
    assertEquals( "&#045;&#045;", result );
    result = RenderUtil.encodeHTMLEntities( "<\">" );
    assertEquals( "&lt;&quot;&gt;", result );
    result = RenderUtil.encodeHTMLEntities( "&amp;" );
    assertEquals( "&amp;amp;", result );
  }
  
  
  public void testReplaceAmpersand() {
    String result;
    result = RenderUtil.replaceAmpersand( "&" );
    assertEquals( "&amp;", result );
    result = RenderUtil.replaceAmpersand( "&#132;" );
    assertEquals( "&#132;", result );
    result = RenderUtil.replaceAmpersand( "&&" );
    assertEquals( "&amp;&amp;", result );
    result = RenderUtil.replaceAmpersand( "abc&def" );
    assertEquals( "abc&amp;def", result );
    result = RenderUtil.replaceAmpersand( "&xyz" );
    assertEquals( "&amp;xyz", result );
    result = RenderUtil.replaceAmpersand( "abc&" );
    assertEquals( "abc&amp;", result );
    result = RenderUtil.replaceAmpersand( "&#xyz;" );
    assertEquals( "&amp;#xyz;", result );
    result = RenderUtil.replaceAmpersand( "&#" );
    assertEquals( "&amp;#", result );
  }
  
  public void testEncodeXMLLEntities() {
    try {
      RenderUtil.encodeXMLEntities( null );
      fail( "Expected NullPointerException" );
    } catch( NullPointerException npe ) {
      // expected
    }
    String result;
    result = RenderUtil.encodeXMLEntities( "" );
    assertEquals( "", result );
    result = RenderUtil.encodeXMLEntities( "&nbsp;" );
    assertEquals( "&#160;", result );
    result = RenderUtil.encodeXMLEntities( "\"" );
    assertEquals( "&#034;", result );
    result = RenderUtil.encodeXMLEntities( "abc" );
    assertEquals( "abc", result );
    result = RenderUtil.encodeXMLEntities( "-" );
    assertEquals( "-", result );
    result = RenderUtil.encodeXMLEntities( "--" );
    assertEquals( "&#045;&#045;", result );
  }

  public void testCreateJavaScriptInline() {
    String html;
    html = RenderUtil.createJavaScriptInline( "alert('ho ho');" );
    String expected = "<script "
                      + "type=\"text/javascript\">"
                      + "alert(\'ho ho\');"
                      + "</script>";
    assertEquals( expected, html );
    html = RenderUtil.createJavaScriptInline( null );
    expected = "";
    assertEquals( expected, html );
    html = RenderUtil.createJavaScriptInline( "alert('Hölilälü');" );
    expected 
      = "<script type=\"text/javascript\">" 
      + "alert(\'H&ouml;lil&auml;l&uuml;\');</script>";
    assertEquals( expected, html );
    html = RenderUtil.createJavaScriptInline( "if( a && b || a > b ) &szlig;" );
    expected 
      = "<script type=\"text/javascript\">" 
      + "if( a &amp;&amp; b || a &gt; b ) &amp;szlig;</script>";
    assertEquals( expected, html );
  }

  public void testWriteJavaScriptInline() throws IOException {
    HtmlResponseWriter writer;
    String expected;
    Fixture.fakeBrowser( new Ie6( true ) );
    Fixture.fakeResponseWriter();
    writer = ContextProvider.getStateInfo().getResponseWriter();
    RenderUtil.writeJavaScriptInline( writer, "alert('ho ho');" );
    expected 
      = "<script "
      + "type=\"text/javascript\">"
      + "alert(\'ho ho\');"
      + "</script>";
    assertEquals( expected, Fixture.getAllMarkup() );
    Fixture.fakeResponseWriter();
    writer = ContextProvider.getStateInfo().getResponseWriter();
    RenderUtil.writeJavaScriptInline( writer, null );
    expected = "";
    assertEquals( expected, Fixture.getAllMarkup() );

  
    Fixture.fakeResponseWriter();
    writer = ContextProvider.getStateInfo().getResponseWriter();
    RenderUtil.writeJavaScriptInline( writer, 
                                      "alert('Ho! Räuber Hotzenplötz');" );
    expected = "<script type=\"text/javascript\">"
               + "alert('Ho! Räuber Hotzenplötz');"
               + "</script>";
    assertEquals( expected, Fixture.getAllMarkup() );
    Fixture.fakeResponseWriter();
    writer = ContextProvider.getStateInfo().getResponseWriter();
    RenderUtil.writeJavaScriptInline( writer, "if( a && b ) ... &szlig;" );
    expected = "<script type=\"text/javascript\">"
               + "if( a && b ) ... &szlig;"
               + "</script>";
    assertEquals( expected, Fixture.getAllMarkup() );
}

  public void testCreateJavaScriptLink() {
    String html;
    html = RenderUtil.createJavaScriptLink( "js/mylib/code.js" );
    String expected;
    expected = "<script charset=\"UTF-8\" src=\"js/mylib/code.js\" "
               + "type=\"text/javascript\">"
               + "</script>";
    assertEquals( expected, html );
    html = RenderUtil.createJavaScriptLink( "js/mylib/code.js" );
    expected = "<script charset=\"UTF-8\" "
               + "src=\"js/mylib/code.js\" "
               + "type=\"text/javascript\">"
               + "</script>";
    assertEquals( expected, html );
    html = RenderUtil.createJavaScriptLink( null );
    expected = "<script charset=\"UTF-8\" src=\"null\" "
               + "type=\"text/javascript\">"
               + "</script>";
    assertEquals( expected, html );
  }

  public void testAppendAjaxPlaceHolder_Mozilla() throws IOException {
    Fixture.fakeBrowser( new Mozilla1_6up( true, false ) );
    // without non-braking space
    HtmlResponseWriter out = new HtmlResponseWriter();
    WebComponent component = new WebLabel();
    Fixture.setResponseWriter( out );
    RenderUtil.appendAjaxPlaceholder( out, component, false );
    String actual = Fixture.getAllMarkup( out );
    String expected = "<span id=\"p1\"></span>";
    assertEquals( expected, actual );
    // with non-braking space
    out = new HtmlResponseWriter();
    Fixture.setResponseWriter( out );
    RenderUtil.appendAjaxPlaceholder( out, component, true );
    actual = Fixture.getAllMarkup( out );
    expected 
      = "<span id=\"p1\" style=\"display:none;visibility:hidden\">" 
      + "&nbsp;" 
      + "</span>";
    assertEquals( expected, actual );
  }

  public void testAppendAjaxPlaceHolder_Ie() throws IOException {
    Fixture.fakeBrowser( new Ie5up( true, false ) );
    // without non-braking space
    HtmlResponseWriter out = new HtmlResponseWriter();
    Fixture.setResponseWriter( out );
    WebComponent component = new WebLabel();
    RenderUtil.appendAjaxPlaceholder( out, component, false );
    String actual = Fixture.getAllMarkup( out );
    String expected 
    =   "<span id=\"p1\">"
      + "<img width=\"0\" height=\"0\" "
      + "src=\"resources/images/transparent.gif\" />"
      + "</span>";
    assertEquals( expected, actual );
    // with non-braking space
    out = new HtmlResponseWriter();
    Fixture.setResponseWriter( out );
    RenderUtil.appendAjaxPlaceholder( out, component, true );
    actual = Fixture.getAllMarkup( out );
    expected 
      = "<span id=\"p1\" style=\"display:none;visibility:hidden\">" 
      + "&nbsp;" 
      + "</span>";
    assertEquals( expected, actual );
  }

  public void testWriteFontOpener() throws IOException {
    Fixture.fakeBrowser( new Default( false, false ) );
    IServiceStateInfo stateInfo = ContextProvider.getStateInfo();
    String expected;
    String markup;
    WebColor color = new WebColor( "blue" );
    Fixture.fakeResponseWriter();
    RenderUtil.writeFontOpener( "BoldFont", color, 12 );
    HtmlResponseWriter responseWriter = stateInfo.getResponseWriter();
    markup = Fixture.getAllMarkup( responseWriter );
    expected = "<font face=\"BoldFont\" color=\"#0000FF\" size=\"3\">";
    assertEquals( expected, markup );
    Fixture.fakeResponseWriter();
    RenderUtil.writeFontOpener( null, color, 0 );
    responseWriter = stateInfo.getResponseWriter();
    markup = Fixture.getAllMarkup( responseWriter );
    expected = "<font color=\"#0000FF\" size=\"1\">";
    assertEquals( expected, markup );
  }

  public void testUseEventHandler() {
    String markup;
    markup = RenderUtil.useEventHandler( null );
    assertEquals( " onClick=\"eventHandler.webActionPerformed('null')\" ",
                  markup );
    markup = RenderUtil.useEventHandler( "p1" );
    assertEquals( " onClick=\"eventHandler.webActionPerformed('p1')\" ", 
                  markup );
  }

  public void testWebActionPerformed() {
    String markup;
    markup = RenderUtil.webActionPerformed( null );
    assertEquals( "eventHandler.webActionPerformed('null')", markup );
    markup = RenderUtil.webActionPerformed( "p1" );
    assertEquals( "eventHandler.webActionPerformed('p1')", markup );
  }

  public void testJsWebActionPerformed() {
    String markup;
    markup = RenderUtil.jsWebActionPerformed( null );
    assertEquals( "javascript:eventHandler.webActionPerformed('null')", 
                  markup );
    markup = RenderUtil.jsWebActionPerformed( "p1" );
    assertEquals( "javascript:eventHandler.webActionPerformed('p1')", markup );
  }

  public void testJsDoDragDrop() {
    try {
      RenderUtil.jsDoDragDrop( null );
      fail( "Expected NullPointerException" );
    } catch( NullPointerException npe ) {
      // expected
    }
    WebComponent component = new WebButton();
    String markup;
    markup = RenderUtil.jsDoDragDrop( component );
    assertEquals( "javascript:dragDropHandler.doDragDrop( 'p1' );", markup );
  }
  
  public void testCreateFormGetURL() {
    WebForm form = Fixture.getEmptyWebFormInstance();
    FormManager.setActive( form );
    IWindow window = WindowManager.getInstance().create( form );
    WindowManager.setActive( window );
    String actual = RenderUtil.createFormGetURL( form );
    String expected 
      = "http://fooserver:8080/fooapp/W4TDelegate?uiRoot=w1;p1" 
      + "&requestCounter=-1" 
      + "&w4t_paramlessGET=true"
      + "&nocache="
      + form.hashCode();
    assertEquals( expected, actual );
  }
  
  private static Style createEmptyStyle() {
    Style result = new Style();
    WebColor noColor = new WebColor( "" );
    result.setFontFamily( "" );
    result.setFontSize( Style.NOT_USED );
    result.setColor( noColor );
    result.setBorderColor( noColor );
    result.setBorderTopColor( noColor );
    result.setBorderBottomColor( noColor );
    result.setBorderLeftColor( noColor );
    result.setBorderRightColor( noColor );
    result.setBgColor( noColor );
    return result;
  }
}

// $endOfPublicClass
class RenderUtilTest_1 {

  private static String[] res = new String[]{
    " width=\"42\"  height=\"42\"  cellspacing=\"42\"  cellpadding=\"42\"  border=\"42\"  bgColor=\"#0000FF\"  align=\"42\" "
  };

  static String[] getRes() {
    return res;
  }
}

class RenderUtilTest_0 {

  private static String[] res = new String[]{
    " width=\"100%\"  cellspacing=\"0\"  cellpadding=\"0\"  border=\"0\" "
  };

  static String[] getRes() {
    return res;
  }
}
