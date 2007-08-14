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
package com.w4t.webformkit;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.eclipse.rwt.internal.ConfigurationReader;
import org.eclipse.rwt.internal.browser.Ie6;
import org.eclipse.rwt.internal.lifecycle.HtmlResponseWriter;
import org.eclipse.rwt.internal.resources.ResourceManagerImpl;
import org.eclipse.rwt.internal.service.ContextProvider;
import org.eclipse.rwt.internal.service.RequestParams;
import org.eclipse.rwt.internal.util.CssClass;
import org.eclipse.rwt.internal.util.HTML;

import com.w4t.*;
import com.w4t.W4TFixture.TestResponse;
import com.w4t.engine.W4TModelUtil;
import com.w4t.engine.adapter.TestEngineConfig;
import com.w4t.engine.util.FormManager;
import com.w4t.mockup.TestForm;
import com.w4t.util.RendererCache;


/** <p>Unit tests for WebFormRenderer.</p>
 */
public class WebFormRenderer_Test extends TestCase {
  
  private final static File TEST_CONFIG 
    = new File( W4TFixture.TEMP_DIR, "w4t.xml" );

  public void testGetBodyAttributes() {
    WebFormRenderer_Default_Script renderer 
      = new WebFormRenderer_Default_Script();
    WebForm form = new TestForm();
    setActiveForm( form );
    StringBuffer bodyAttributes = renderer.getBodyAttributes();
    String expected 
      = " bgcolor=\"#ffffff\" " 
      + "text=\"#000000\" " 
      + "leftmargin=\"0\" topmargin=\"0\" " 
      + "marginheight=\"0\" marginwidth=\"0\"  "; 
    assertEquals( expected, bodyAttributes.toString() );
  }
  
  public void testNoScript() throws Exception {
    WebForm form = new TestForm();
    W4TFixture.fakeEngineForRender( form );
    W4TFixture.fakeBrowser( new Ie6( false ) );
    setActiveForm( form );
    HtmlResponseWriter writer = new HtmlResponseWriter();
    W4TFixture.setResponseWriter( writer );
    W4TFixture.renderComponent( form );
    String markup = W4TFixture.getAllMarkup( writer );
    String expected 
      =   "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\">"
        + "<html>" 
        + "<head>"
        + "<title>W4 Toolkit</title>" 
        + "<style type=\"text/css\"></style> " 
        + "<meta http-equiv=\"content-type\" content=\"text/html; " 
        + "charset=UTF-8\" />"
        + "<meta http-equiv=\"cache-control\" content=\"no-cache\" /> "
        + "<!-- InternetExplorer special -->" 
        + "<script></script>" 
        + "<!-- End InternetExplorer special -->"
        + "<link rel=\"SHORTCUT ICON\" "
        + "href=\"http://fooserver:8080/fooapp/"
        + "resources/images/favicon.ico\" />" 
        + "<link rel=\"icon\" href=\"http://fooserver:8080/fooapp/"
        + "resources/images/favicon.ico\" "
        + "type=\"image/ico\" />"
        + "</head>" 
        + "<body  bgcolor=\"#ffffff\" text=\"#000000\" leftmargin=\"0\" " 
        + "topmargin=\"0\" marginheight=\"0\" marginwidth=\"0\"  >" 
        + "<form id=\"p1\" "
        + "action=\"http://fooserver:8080/fooapp/W4TDelegate?w4t_enc=no\" "
        + "name=\"W4TForm\" method=\"post\" accept-charset=\"UTF-8\" "
        + "enctype=\"application/x-www-form-urlencoded\">" 
        + "<input type=\"hidden\" id=\"uiRoot\" " 
        + "name=\"uiRoot\" value=\"w1;p1\" />" 
        + "<input type=\"hidden\" id=\"w4t_ajaxEnabled\" " 
        + "name=\"w4t_ajaxEnabled\" value=\"false\" />" 
        + "<input type=\"hidden\" id=\"requestCounter\" " 
        + "name=\"requestCounter\" value=\"0\" />" 
        + "<table id=\"p1\" width=\"100%\" " 
        + "cellspacing=\"0\" cellpadding=\"0\" border=\"0\">" 
        + "</table>" 
        + "</form>" 
        + "</body>" 
        + "</html>";
    assertEquals( expected, markup );
  }
  
  public void testCreatePageHeader_Script() throws Exception {
    WebForm form = W4TFixture.getEmptyWebFormInstance();
    W4TFixture.fakeEngineForRender( form );
    W4TFixture.fakeBrowser( new Ie6( false ) );
    setActiveForm( form );
    HtmlResponseWriter writer = new HtmlResponseWriter();
    W4TFixture.setResponseWriter( writer );
    WebFormRenderer renderer = new WebFormRenderer_Default_Script();
    String markup = renderer.createPageHeader().toString();
    String expected 
      = "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\">"
        + "<html><head><title>W4 Toolkit</title><style type=\"text/css\">" 
        + "</style><meta http-equiv=\"content-type\" content=\"text/html; "
        + "charset=UTF-8\" />"
        + "<meta http-equiv=\"cache-control\" content=\"no-cache\" />" 
        + "<script type=\"text/javascript\">windowManager.posX = -1;" 
        + "windowManager.posY = -1;</script>" 
        + "<link rel=\"SHORTCUT ICON\" "
        + "href=\"http://fooserver:8080/fooapp/"
        + "resources/images/favicon.ico\" />" 
        + "<link rel=\"icon\" href=\"http://fooserver:8080/fooapp/"
        + "resources/images/favicon.ico\" "
        + "type=\"image/ico\" /></head><body  bgcolor=\"#ffffff\" "
        + "text=\"#000000\" leftmargin=\"0\" " 
        + "topmargin=\"0\" marginheight=\"0\" marginwidth=\"0\"   " 
        + "onload=\"windowManager.initWebForm( \'false\' );" 
        + "eventHandler.disableFocusEvent();" 
        + "setTimeout( \'eventHandler.enableFocusEvent()\', 50 );" 
        + "windowManager.scrollWindow( 0, 0 ); " 
        + "windowManager.setName( \'w1\' ); " 
        + "active = window.setInterval( \'triggerTimeStamp_DOM()\'," 
        + "1800000);\" onunload=\"w4tClearKeepAlive();\" >" 
        + "<form id=\"p1\" "
        + "action=\"http://fooserver:8080/fooapp/W4TDelegate?w4t_enc=no\" "
        + "name=\"W4TForm\" method=\"post\" accept-charset=\"UTF-8\" "
        + "enctype=\"application/x-www-form-urlencoded\">"
        + "<div id=\"w4t_userdefined_scripts\"></div>"
        + "<input type=\"hidden\" id=\"webActionEvent\" " 
        + "name=\"webActionEvent\" value=\"not_occured\" />" 
        + "<input type=\"hidden\" id=\"webItemEvent\" " 
        + "name=\"webItemEvent\" value=\"not_occured\" />" 
        + "<input type=\"hidden\" id=\"webFocusGainedEvent\" " 
        + "name=\"webFocusGainedEvent\" value=\"not_occured\" />" 
        + "<input type=\"hidden\" id=\"webTreeNodeExpandedEvent\" " 
        + "name=\"webTreeNodeExpandedEvent\" value=\"not_occured\" />" 
        + "<input type=\"hidden\" id=\"webTreeNodeCollapsedEvent\" " 
        + "name=\"webTreeNodeCollapsedEvent\" value=\"not_occured\" />" 
        + "<input type=\"hidden\" id=\"changeImage\" " 
        + "name=\"changeImage\" value=\"\" />" 
        + "<input type=\"hidden\" id=\"dragSource\" " 
        + "name=\"dragSource\" value=\"\" />" 
        + "<input type=\"hidden\" id=\"dragDestination\" " 
        + "name=\"dragDestination\" value=\"\" />"
        + "<input type=\"hidden\" id=\"w4tDoubleClickEvent\" " 
        + "name=\"w4tDoubleClickEvent\" value=\"not_occured\" />"        
        + "<input type=\"hidden\" id=\"focusElement\" " 
        + "name=\"focusElement\" value=\"\" />" 
        + "<input type=\"hidden\" id=\"scrollX\" " 
        + "name=\"scrollX\" value=\"\" />" 
        + "<input type=\"hidden\" id=\"scrollY\" " 
        + "name=\"scrollY\" value=\"\" />" 
        + "<input type=\"hidden\" id=\"availWidth\" " 
        + "name=\"availWidth\" value=\"0\" />" 
        + "<input type=\"hidden\" id=\"availHeight\" " 
        + "name=\"availHeight\" value=\"0\" />";
    assertEquals( expected, markup );
  }
  
  public void testCreateCssClasses() {
    String expected;
    HtmlResponseWriter writer;
    WebFormRenderer_Default_Script renderer;
    String markup;
    W4TFixture.fakeBrowser( new Ie6( true ) );
    writer = new HtmlResponseWriter();
    W4TFixture.setResponseWriter( writer );
    renderer = new WebFormRenderer_Default_Script();
    markup = renderer.createCssClasses();
    expected 
      = "<style type=\"text/css\"></style>";
    assertEquals( expected, markup );
    writer = new HtmlResponseWriter();
    W4TFixture.setResponseWriter( writer );
    writer.addNamedCssClass( new CssClass( "myStyle", "color:red" ) );
    renderer = new WebFormRenderer_Default_Script();
    markup = renderer.createCssClasses();
    expected 
    = "<style type=\"text/css\">.myStyle { color:red } </style>";
    assertEquals( expected, markup );
  }
  
  public void testWriteCssClasses() throws IOException {
    String expected;
    HtmlResponseWriter writer;
    WebFormRenderer_Default_Script renderer;
    String markup;
    W4TFixture.fakeBrowser( new Ie6( true ) );
    writer = new HtmlResponseWriter();
    W4TFixture.setResponseWriter( writer );
    renderer = new WebFormRenderer_Default_Script();
    renderer.writeCssClasses();
    markup = W4TFixture.getAllMarkup();
    expected 
      = "<style type=\"text/css\"></style>";
    assertEquals( expected, markup );
    writer = new HtmlResponseWriter();
    W4TFixture.setResponseWriter( writer );
    writer.addNamedCssClass( new CssClass( "myStyle", "color:red" ) );
    renderer = new WebFormRenderer_Default_Script();
    renderer.writeCssClasses();
    markup = W4TFixture.getAllMarkup();
    expected 
      = "<style type=\"text/css\">.myStyle { color:red } </style>";
    assertEquals( expected, markup );
  }
  
  public void testDuplicateNamedCssClasses() throws Exception {
    // prepare
    WebForm form = W4TFixture.getEmptyWebFormInstance();
    W4TFixture.fakeEngineForRender( form );
    W4TFixture.fakeBrowser( new Ie6( false, false ) );
    HtmlResponseWriter writer = new HtmlResponseWriter();
    W4TFixture.setResponseWriter( writer );
    // add named css class
    CssClass cssClass = new CssClass( "my-style", "appearance:extraordinary" );
    writer.addNamedCssClass( cssClass );
    // add another equal named css class
    cssClass = new CssClass( "my-style", "appearance:extraordinary" );
    writer.addNamedCssClass( cssClass );
    W4TFixture.renderComponent( form );
    String allMarkup = W4TFixture.getAllMarkup( writer );
    assertEquals( allMarkup.indexOf( ".my-style" ), 
                  allMarkup.lastIndexOf( ".my-style" ) ); 
  }
  
  public void testDuplicateRegisterCssClass() throws Exception {
    // prepare
    WebForm form = W4TFixture.getEmptyWebFormInstance();
    W4TFixture.fakeEngineForRender( form );
    W4TFixture.fakeBrowser( new Ie6( false, false ) );
    HtmlResponseWriter writer = new HtmlResponseWriter();
    W4TFixture.setResponseWriter( writer );
    // register css style two times
    writer.registerCssClass( "appearance:extraordinary" );
    writer.registerCssClass( "appearance:extraordinary" );
    W4TFixture.renderComponent( form );
    String allMarkup = W4TFixture.getAllMarkup( writer );
    assertEquals( allMarkup.indexOf( ".w4tCss8bd7045c" ), 
                  allMarkup.lastIndexOf( ".w4tCss8bd7045c" ) ); 
  }
  
  public void testResponseContentType_Ajax() throws Exception {
    WebForm form = W4TFixture.getEmptyWebFormInstance();
    W4TFixture.forceAjaxRendering( form );
    W4TFixture.fakeEngineForRender( form );
    W4TFixture.fakeBrowser( new Ie6( true, true ) );
    assertEquals( WebFormRenderer_Default_Ajax.class, 
                  RendererCache.getInstance().retrieveRenderer( WebForm.class ).getClass() );
    assertEquals( true, W4TContext.getBrowser().isAjaxEnabled() );
    HtmlResponseWriter writer = new HtmlResponseWriter();
    W4TFixture.setResponseWriter( writer );
    W4TFixture.fakeRequestParam( RequestParams.IS_AJAX_REQUEST, "true" );
    W4TFixture.renderComponent( form );
    HttpServletResponse response = ContextProvider.getResponse();
    W4TFixture.TestResponse testResponse = ( TestResponse )response;
    assertEquals( HTML.CONTENT_TEXT_XML, testResponse.getContentType() );    
  }
  
  public void testResponseContentType_Script() throws Exception {
    WebForm form = W4TFixture.getEmptyWebFormInstance();
    W4TFixture.fakeEngineForRender( form );
    W4TFixture.fakeBrowser( new Ie6( true, false ) );
    HtmlResponseWriter writer = new HtmlResponseWriter();
    W4TFixture.setResponseWriter( writer );
    W4TFixture.renderComponent( form );
    HttpServletResponse response = ContextProvider.getResponse();
    W4TFixture.TestResponse testResponse = ( TestResponse )response;
    assertEquals( HTML.CONTENT_TEXT_HTML_UTF_8, testResponse.getContentType() );    
  }
  
  public void testResponseContentType_Noscript() throws Exception {
    WebForm form = W4TFixture.getEmptyWebFormInstance();
    W4TFixture.fakeEngineForRender( form );
    W4TFixture.fakeBrowser( new Ie6( false, false ) );
    HtmlResponseWriter writer = new HtmlResponseWriter();
    W4TFixture.setResponseWriter( writer );
    W4TFixture.renderComponent( form );
    HttpServletResponse response = ContextProvider.getResponse();
    W4TFixture.TestResponse testResponse = ( TestResponse )response;
    assertEquals( HTML.CONTENT_TEXT_HTML_UTF_8, testResponse.getContentType() );    
  }
  
  protected void setUp() throws Exception {
    W4TFixture.setUp();
    try {
      W4TModelUtil.initModel();
    } catch( Exception e ) {
      e.printStackTrace();
      Assert.fail( "Failed to create W4TModelCore. Reason: " + e );
    }
    ResourceManagerImpl.createInstance( W4TFixture.getWebAppBase().toString(), 
                                    ResourceManagerImpl.DELIVER_FROM_DISK );
    W4TFixture.copyTestResource( "resources/w4t_default.xml", TEST_CONFIG );
    TestEngineConfig engineConfig = new TestEngineConfig();
    engineConfig.setConfigFile( TEST_CONFIG );
    ConfigurationReader.setEngineConfig( engineConfig );
    engineConfig.setServerContextDir( W4TFixture.getWebAppBase() );
  }
  
  protected void tearDown() throws Exception {
    W4TFixture.setPrivateField( ResourceManagerImpl.class, null, "_instance", null );
    W4TFixture.tearDown();
    if( TEST_CONFIG.exists() ) {
      TEST_CONFIG.delete();
    }
    ConfigurationReader.setConfigurationFile( null );
  }
  
  private void setActiveForm( final WebForm form ) {
    FormManager.add( form );
    FormManager.setActive( form );
  }
}
