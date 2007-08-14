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
package com.w4t.engine.lifecycle;

import org.eclipse.rwt.internal.browser.Default;
import org.eclipse.rwt.internal.browser.Ie5_5up;
import org.eclipse.rwt.internal.lifecycle.LifeCycle;
import org.eclipse.rwt.internal.service.*;

import junit.framework.TestCase;

import com.w4t.*;
import com.w4t.IWindowManager.IWindow;
import com.w4t.ajax.AjaxStatusUtil;
import com.w4t.engine.W4TModelUtil;
import com.w4t.engine.util.FormManager;
import com.w4t.engine.util.WindowManager;
import com.w4t.event.WebRenderEvent;
import com.w4t.event.WebRenderListener;
import com.w4t.internal.adaptable.IFormAdapter;


public class LifeCycleStandardRequest_Test extends TestCase {
  
  private static final String AFTER_RENDER = "afterRender";
  private static final String BEFORE_RENDER = "beforeRender |";
  private String log = "";
  
  private class EventTester implements WebRenderListener {
    public void beforeRender( final WebRenderEvent e ) {
      log += BEFORE_RENDER;
    }
    public void afterRender( final WebRenderEvent e ) {
      log += AFTER_RENDER;
    }
  }
  
  protected void setUp() throws Exception {
    W4TFixture.setUp();
    W4TFixture.createContext( false );
  }
  
  protected void tearDown() throws Exception {
    W4TFixture.tearDown();
    W4TFixture.removeContext();
    log = "";
  }

  public void testStandardRequestNoScript() throws Exception {
    W4TFixture.fakeResponseWriter();
    W4TFixture.fakeBrowser( new Default( false, false ) );
    W4TModelUtil.initModel();
    prepareFormAndRequestParms();
    LifeCycle lifeCycle = ( LifeCycle )W4TContext.getLifeCycle();
    lifeCycle.execute();
    
    IServiceStateInfo stateInfo = ContextProvider.getStateInfo();
    String allMarkup = W4TFixture.getAllMarkup( stateInfo.getResponseWriter() );
    String expected
      =   "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\">"
        + "<html><head><title>W4 Toolkit</title><style type=\"text/css\">"
        + ".w4tCsscd1f6403 { font-family:arial,verdana;font-size:8pt; } "
        + "</style> <meta http-equiv=\"content-type\" content=\"text/html; "
        + "charset=UTF-8\" /><meta http-equiv=\"cache-control\" "
        + "content=\"no-cache\" /> <link rel=\"SHORTCUT ICON\" "
        + "href=\"http://fooserver:8080/fooapp/"
        + "resources/images/favicon.ico\" /><link rel=\"icon\" "
        + "href=\"http://fooserver:8080/fooapp/"
        + "resources/images/favicon.ico\" type=\"image/ico\" /></head>"
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
        + "name=\"requestCounter\" value=\"1\" /><table id=\"p1\" "
        + "width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\">"
        + "<tr><td align=\"center\" valign=\"middle\" colspan=\"3\">"
        + "<span class=\"w4tCsscd1f6403\">This is the W4Toolkit W4TFixture Form!" 
        + "</span></td></tr></table></form></body></html>";
    assertEquals( expected, allMarkup );
    assertEquals( BEFORE_RENDER + AFTER_RENDER, log );
    
    // test expired request
    W4TFixture.fakeResponseWriter();
    log = "";
    FormManager.setActive( null );
    lifeCycle.execute();
    allMarkup = W4TFixture.getAllMarkup( stateInfo.getResponseWriter() );
    assertEquals( expected, allMarkup );
    assertEquals( "", log );
  }
  
  public void testStandardRequestScript() throws Exception {
    W4TFixture.fakeResponseWriter();
    W4TFixture.fakeBrowser( new Default( true, false ) );
    W4TModelUtil.initModel();
    prepareFormAndRequestParms();
    LifeCycle lifeCycle = ( LifeCycle )W4TContext.getLifeCycle();
    lifeCycle.execute();

    IServiceStateInfo stateInfo = ContextProvider.getStateInfo();
    String allMarkup = W4TFixture.getAllMarkup( stateInfo.getResponseWriter() );
    String expected
      =   "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\">"
        + "<html><head><title>W4 Toolkit</title><style type=\"text/css\">"
        + ".w4tCsscd1f6403 { font-family:arial,verdana;font-size:8pt; } "
        + "</style><meta http-equiv=\"content-type\" content=\"text/html; "
        + "charset=UTF-8\" /><meta http-equiv=\"cache-control\" "
        + "content=\"no-cache\" />"
        + "<script charset=\"UTF-8\" " 
        + "src=\"http://fooserver:8080/fooapp/"
        + "resources/js/eventhandler/eventhandler_ie.js\" " 
        + "type=\"text/javascript\">" 
        + "</script>" 
        + "<script charset=\"UTF-8\" " 
        + "src=\"http://fooserver:8080/fooapp/"
        + "resources/js/windowmanager/windowmanager.js\" " 
        + "type=\"text/javascript\">" 
        + "</script>" 
        + "<script type=\"text/javascript\">" 
        + "windowManager.posX = -1;windowManager.posY = -1;</script>"
        + "<link rel=\"SHORTCUT ICON\" "
        + "href=\"http://fooserver:8080/fooapp/"
        + "resources/images/favicon.ico\" />"
        + "<link rel=\"icon\" href=\"http://fooserver:8080/fooapp/"
        + "resources/images/favicon.ico\" "
        + "type=\"image/ico\" /></head><body  bgcolor=\"#ffffff\" "
        + "text=\"#000000\" leftmargin=\"0\" topmargin=\"0\" "
        + "marginheight=\"0\" marginwidth=\"0\"   "
        + "onload=\"windowManager.initWebForm( 'false' );"
        + "eventHandler.disableFocusEvent();" 
        + "setTimeout( 'eventHandler.enableFocusEvent()', 50 );" 
        + "windowManager.scrollWindow( 0, 0 ); windowManager.setName( 'w1' ); "
        + "active = window.setInterval( 'windowManager.triggerTimeStamp()', "
        + "1800000);\" onunload=\"w4tClearKeepAlive();\" >"
        + "<form id=\"p1\" "
        + "action=\"http://fooserver:8080/fooapp/W4TDelegate?w4t_enc=no\" "
        + "name=\"W4TForm\" method=\"post\" accept-charset=\"UTF-8\" "
        + "enctype=\"application/x-www-form-urlencoded\">"
        + "<div id=\"w4t_userdefined_scripts\"></div>"
        + "<input type=\"hidden\" id=\"webActionEvent\" "
        + "name=\"webActionEvent\" value=\"not_occured\" />"
        + "<input type=\"hidden\" id=\"webItemEvent\" name=\"webItemEvent\" "
        + "value=\"not_occured\" /><input type=\"hidden\" "
        + "id=\"webFocusGainedEvent\" name=\"webFocusGainedEvent\" "
        + "value=\"not_occured\" /><input type=\"hidden\" "
        + "id=\"webTreeNodeExpandedEvent\" name=\"webTreeNodeExpandedEvent\" "
        + "value=\"not_occured\" /><input type=\"hidden\" "
        + "id=\"webTreeNodeCollapsedEvent\" "
        + "name=\"webTreeNodeCollapsedEvent\" value=\"not_occured\" />"
        + "<input type=\"hidden\" id=\"changeImage\" name=\"changeImage\" "
        + "value=\"\" /><input type=\"hidden\" id=\"dragSource\" "
        + "name=\"dragSource\" value=\"\" /><input type=\"hidden\" "
        + "id=\"dragDestination\" name=\"dragDestination\" value=\"\" />"
        + "<input type=\"hidden\" id=\"w4tDoubleClickEvent\" " 
        + "name=\"w4tDoubleClickEvent\" value=\"not_occured\" />"        
        + "<input type=\"hidden\" id=\"focusElement\" name=\"focusElement\" "
        + "value=\"\" /><input type=\"hidden\" id=\"scrollX\" "
        + "name=\"scrollX\" value=\"\" /><input type=\"hidden\" "
        + "id=\"scrollY\" name=\"scrollY\" value=\"\" /><input "
        + "type=\"hidden\" id=\"availWidth\" name=\"availWidth\" "
        + "value=\"0\" /><input type=\"hidden\" id=\"availHeight\" "
        + "name=\"availHeight\" value=\"0\" />" 
        + "<input type=\"hidden\" id=\"uiRoot\" " 
        + "name=\"uiRoot\" value=\"w1;p1\" />"
        + "<input type=\"hidden\" id=\"w4t_ajaxEnabled\" " 
        + "name=\"w4t_ajaxEnabled\" value=\"false\" />" 
        + "<input type=\"hidden\" id=\"requestCounter\" "
        + "name=\"requestCounter\" value=\"1\" /><table id=\"p1\" "
        + "width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\">"
        + "<tr><td align=\"center\" valign=\"middle\" colspan=\"3\"><span "
        + "id=\"p2\" class=\"w4tCsscd1f6403\">"
        + "This is the W4Toolkit W4TFixture Form!</span></td></tr></table>"
        + "</form></body>"
        + "</html>";
    assertEquals( expected, allMarkup );
    assertEquals( BEFORE_RENDER + AFTER_RENDER, log );
    
    // test expired request
    W4TFixture.fakeResponseWriter();
    log = "";
    FormManager.setActive( null );
    lifeCycle.execute();
    allMarkup = W4TFixture.getAllMarkup( stateInfo.getResponseWriter() );
    assertEquals( expected, allMarkup );
    assertEquals( "", log );
  }
  
  public void testStandardRequestAjaxPartial() throws Exception {
    W4TFixture.fakeResponseWriter();
    W4TFixture.fakeBrowser( new Ie5_5up( true, true ) );
    W4TModelUtil.initModel();
    prepareFormAndRequestParamsAjaxPartial();
    LifeCycle lifeCycle = ( LifeCycle )W4TContext.getLifeCycle();
    lifeCycle.execute();
    
    IServiceStateInfo stateInfo = ContextProvider.getStateInfo();
    String allMarkup = W4TFixture.getAllMarkup( stateInfo.getResponseWriter() );
    String expected
      =   "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>"
        + "<ajax-response><input type=\"hidden\" id=\"webActionEvent\" "
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
        + "<input type=\"hidden\" id=\"requestCounter\" "
        + "name=\"requestCounter\" value=\"1\" />"
        + "</ajax-response>";
    assertEquals( expected, allMarkup );
    assertEquals( BEFORE_RENDER + AFTER_RENDER, log );
  }
  
  public void testStandardRequestAjaxComplete() throws Exception {
    W4TFixture.fakeResponseWriter();
    W4TFixture.fakeRequestParam( RequestParams.STARTUP, "true" );
    W4TFixture.fakeBrowser( new Ie5_5up( true, true ) );
    W4TModelUtil.initModel();
    LifeCycle lifeCycle = ( LifeCycle )W4TContext.getLifeCycle();
    lifeCycle.execute();

    W4TFixture.fakeResponseWriter();
    prepareFormAndRequestParamsAjaxComplete();
    lifeCycle.execute();
    
    IServiceStateInfo stateInfo = ContextProvider.getStateInfo();
    String allMarkup = W4TFixture.getAllMarkup( stateInfo.getResponseWriter() );
    String expected
      =   "<?xml version=\"1.0\" encoding=\"UTF-8\" ?><ajax-response>"
        + "<envelope id=\"p1\"><!--<form id=\"p1\" "
        + "action=\"http://fooserver:8080/fooapp/W4TDelegate?w4t_enc=no\" "
        + "name=\"W4TForm\" method=\"post\" accept-charset=\"UTF-8\" "
        + "enctype=\"application/x-www-form-urlencoded\">"
        + "<input type=\"hidden\" id=\"scrollX\" name=\"scrollX\" "
        + "value=\"\" /><input type=\"hidden\" id=\"scrollY\" "
        + "name=\"scrollY\" value=\"\" /><input type=\"hidden\" "
        + "id=\"availWidth\" name=\"availWidth\" value=\"0\" /><input "
        + "type=\"hidden\" id=\"availHeight\" name=\"availHeight\" "
        + "value=\"0\" />" 
        + "<input type=\"hidden\" id=\"uiRoot\" " 
        + "name=\"uiRoot\" value=\"w1;p1\" />"
        + "<input type=\"hidden\" id=\"w4t_ajaxEnabled\" " 
        + "name=\"w4t_ajaxEnabled\" value=\"true\" />" 
        + "<input type=\"hidden\" id=\"webActionEvent\" "
        + "name=\"webActionEvent\" value=\"not_occured\" /><input "
        + "type=\"hidden\" id=\"webItemEvent\" name=\"webItemEvent\" "
        + "value=\"not_occured\" /><input type=\"hidden\" "
        + "id=\"webFocusGainedEvent\" name=\"webFocusGainedEvent\" "
        + "value=\"not_occured\" /><input type=\"hidden\" "
        + "id=\"webTreeNodeExpandedEvent\" name=\"webTreeNodeExpandedEvent\" "
        + "value=\"not_occured\" /><input type=\"hidden\" "
        + "id=\"webTreeNodeCollapsedEvent\" "
        + "name=\"webTreeNodeCollapsedEvent\" value=\"not_occured\" />"
        + "<input type=\"hidden\" id=\"changeImage\" name=\"changeImage\" "
        + "value=\"\" /><input type=\"hidden\" id=\"dragSource\" "
        + "name=\"dragSource\" value=\"\" /><input type=\"hidden\" "
        + "id=\"dragDestination\" name=\"dragDestination\" value=\"\" />"
        + "<input type=\"hidden\" id=\"w4tDoubleClickEvent\" " 
        + "name=\"w4tDoubleClickEvent\" value=\"not_occured\" />"        
        + "<input type=\"hidden\" id=\"focusElement\" name=\"focusElement\" "
        + "value=\"\" /><input type=\"hidden\" id=\"requestCounter\" "
        + "name=\"requestCounter\" value=\"1\" />" 
        + "<table id=\"p1\" width=\"100%\" cellspacing=\"0\" "
        + "cellpadding=\"0\" border=\"0\"><tr><td align=\"center\" "
        + "valign=\"middle\" colspan=\"3\"><span id=\"p2\" "
        + "class=\"w4tCsscd1f6403\">This is the W4Toolkit W4TFixture Form!"
        + "</span></td></tr></table></form>--></envelope></ajax-response>";
    assertEquals( expected, allMarkup );
    assertEquals( BEFORE_RENDER + AFTER_RENDER, log );
  }

  private void prepareFormAndRequestParamsAjaxComplete() {
    W4TFixture.fakeRequestParam( RequestParams.IS_AJAX_REQUEST, "true" );
    W4TFixture.fakeRequestParam( RequestParams.STARTUP, null );
    WebForm form = FormManager.getAll()[ 0 ];
    IFormAdapter adapter = W4TFixture.getFormAdapter( form );
    String requestCounter = String.valueOf( adapter.getRequestCounter() - 1 );
    String formId = form.getUniqueID();
    IWindow window = WindowManager.getInstance().findWindow( form );
    W4TFixture.fakeFormRequestParams( requestCounter, window.getId(), formId );
    form.setMarginheight( "15" );
    form.addWebRenderListener( new EventTester() );
  }

  private void prepareFormAndRequestParamsAjaxPartial() throws Exception {
    prepareFormAndRequestParms();
    W4TFixture.fakeRequestParam( RequestParams.IS_AJAX_REQUEST, "true" );
    WebForm form = FormManager.getAll()[ 0 ];
    AjaxStatusUtil.preRender( form );
    AjaxStatusUtil.postRender( form );
  }

  private void prepareFormAndRequestParms() throws Exception {
    WebForm form = W4TFixture.loadStartupForm();
    IWindow window = WindowManager.getInstance().create( form );
    IFormAdapter adapter = W4TFixture.getFormAdapter( form );
    adapter.increase();
    String formId = form.getUniqueID();
    String requestCounter = String.valueOf( adapter.getRequestCounter() - 1 );
    W4TFixture.fakeFormRequestParams( requestCounter, window.getId(), formId );
    form.addWebRenderListener( new EventTester() );
  }
}