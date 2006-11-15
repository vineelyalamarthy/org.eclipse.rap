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

import junit.framework.TestCase;
import com.w4t.Fixture;
import com.w4t.W4TContext;
import com.w4t.engine.W4TModelUtil;
import com.w4t.engine.requests.RequestParams;
import com.w4t.engine.service.ContextProvider;
import com.w4t.engine.service.IServiceStateInfo;
import com.w4t.util.browser.Default;
import com.w4t.util.browser.Ie5_5up;


public class LifeCycleStartupRequest_Test extends TestCase {
  
  protected void setUp() throws Exception {
    Fixture.setUp();
    Fixture.createContext( false );
  }
  
  protected void tearDown() throws Exception {
    Fixture.tearDown();
    Fixture.removeContext();
  }
  
  public void testStartupFormNoScript() throws Exception {
    Fixture.fakeResponseWriter();
    Fixture.fakeRequestParam( RequestParams.STARTUP, "true" );
    Fixture.fakeBrowser( new Default( false, false ) );
    W4TModelUtil.initModel();
    LifeCycle lifeCycle = ( LifeCycle )W4TContext.getLifeCycle();
    lifeCycle.execute();
    
    IServiceStateInfo stateInfo = ContextProvider.getStateInfo();
    String allMarkup = Fixture.getAllMarkup( stateInfo.getResponseWriter() );
    String expected
      =   "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\">"
        + "<html><head><title>W4 Toolkit</title><style type=\"text/css\">"
        + ".w4tCsscd1f6403 { font-family:arial,verdana;font-size:8pt; } "
        + "</style> <meta http-equiv=\"content-type\" content=\"text/html; "
        + "charset=UTF-8\" /><meta http-equiv=\"cache-control\" "
        + "content=\"no-cache\" /> <link rel=\"SHORTCUT ICON\" "
        + "href=\"http://fooserver:8080/fooapp/resources/images/favicon.ico\" />"
        + "<link rel=\"icon\" "
        + "href=\"http://fooserver:8080/fooapp/resources/images/favicon.ico\" "
        + "type=\"image/ico\" /></head>"
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
        + "name=\"requestCounter\" value=\"0\" /><table id=\"p1\" "
        + "width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\">"
        + "<tr><td align=\"center\" valign=\"middle\" colspan=\"3\">"
        + "<span class=\"w4tCsscd1f6403\">This is the W4Toolkit Fixture Form!" 
        + "</span></td></tr></table></form></body></html>";
    assertEquals( expected, allMarkup );
  }
  
  public void testStartupFormScript() throws Exception {
    Fixture.fakeResponseWriter();
    Fixture.fakeRequestParam( RequestParams.STARTUP, "true" );
    Fixture.fakeBrowser( new Default( true, false ) );
    W4TModelUtil.initModel();
    LifeCycle lifeCycle = ( LifeCycle )W4TContext.getLifeCycle();
    lifeCycle.execute();
    
    IServiceStateInfo stateInfo = ContextProvider.getStateInfo();
    String allMarkup = Fixture.getAllMarkup( stateInfo.getResponseWriter() );
    String expected
      = "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\">" 
      + "<html><head><title>W4 Toolkit</title>" 
      + "<style type=\"text/css\">" 
      + ".w4tCsscd1f6403 { font-family:arial,verdana;font-size:8pt; } </style>" 
      + "<meta http-equiv=\"content-type\" content=\"text/html; " 
      + "charset=UTF-8\" />" 
      + "<meta http-equiv=\"cache-control\" content=\"no-cache\" />" 
      + "<script charset=\"UTF-8\" " 
      + "src=\"http://fooserver:8080/fooapp/" 
      + "resources/js/eventhandler/eventhandler_ie.js\" " 
      + "type=\"text/javascript\"></script>" 
      + "<script charset=\"UTF-8\" " 
      + "src=\"http://fooserver:8080/fooapp/" 
      + "resources/js/windowmanager/windowmanager.js\" " 
      + "type=\"text/javascript\"></script>" 
      + "<script type=\"text/javascript\">" 
      + "windowManager.posX = -1;windowManager.posY = -1;</script>" 
      + "<link rel=\"SHORTCUT ICON\" "
      + "href=\"http://fooserver:8080/fooapp/resources/images/favicon.ico\" />" 
      + "<link rel=\"icon\" href=\"http://fooserver:8080/fooapp/"
      + "resources/images/favicon.ico\" "
      + "type=\"image/ico\" /></head>" 
      + "<body  bgcolor=\"#ffffff\" text=\"#000000\" leftmargin=\"0\" " 
      + "topmargin=\"0\" marginheight=\"0\" marginwidth=\"0\"   " 
      + "onload=\"windowManager.initWebForm( \'false\' );"
      + "eventHandler.disableFocusEvent();" 
      + "setTimeout( \'eventHandler.enableFocusEvent()\', 50 );" 
      + "windowManager.scrollWindow( 0, 0 ); windowManager.setName( \'w1\' ); " 
      + "active = window.setInterval( \'windowManager.triggerTimeStamp()\', " 
      + "1800000);\" " 
      + "onunload=\"w4tClearKeepAlive();\" >" 
      + "<form id=\"p1\" "
      + "action=\"http://fooserver:8080/fooapp/W4TDelegate?w4t_enc=no\" "
      + "name=\"W4TForm\" method=\"post\" accept-charset=\"UTF-8\" " 
      + "enctype=\"application/x-www-form-urlencoded\">" 
      + "<div id=\"w4t_userdefined_scripts\"></div>" 
      + "<input type=\"hidden\" id=\"webActionEvent\" name=\"webActionEvent\" " 
      + "value=\"not_occured\" />" 
      + "<input type=\"hidden\" id=\"webItemEvent\" name=\"webItemEvent\" " 
      + "value=\"not_occured\" /><input type=\"hidden\" " 
      + "id=\"webFocusGainedEvent\" name=\"webFocusGainedEvent\" " 
      + "value=\"not_occured\" />" 
      + "<input type=\"hidden\" id=\"webTreeNodeExpandedEvent\" " 
      + "name=\"webTreeNodeExpandedEvent\" value=\"not_occured\" />" 
      + "<input type=\"hidden\" id=\"webTreeNodeCollapsedEvent\" " 
      + "name=\"webTreeNodeCollapsedEvent\" value=\"not_occured\" />" 
      + "<input type=\"hidden\" id=\"changeImage\" name=\"changeImage\" " 
      + "value=\"\" />" 
      + "<input type=\"hidden\" id=\"dragSource\" name=\"dragSource\" " 
      + "value=\"\" />" 
      + "<input type=\"hidden\" id=\"dragDestination\" " 
      + "name=\"dragDestination\" value=\"\" />"
      + "<input type=\"hidden\" id=\"w4tDoubleClickEvent\" " 
      + "name=\"w4tDoubleClickEvent\" value=\"not_occured\" />"        
      + "<input type=\"hidden\" " 
      + "id=\"focusElement\" name=\"focusElement\" value=\"\" />" 
      + "<input type=\"hidden\" id=\"scrollX\" name=\"scrollX\" value=\"\" />" 
      + "<input type=\"hidden\" id=\"scrollY\" name=\"scrollY\" value=\"\" />" 
      + "<input type=\"hidden\" id=\"availWidth\" name=\"availWidth\" " 
      + "value=\"0\" />" 
      + "<input type=\"hidden\" id=\"availHeight\" name=\"availHeight\" " 
      + "value=\"0\" />"
      + "<input type=\"hidden\" id=\"uiRoot\" " 
      + "name=\"uiRoot\" value=\"w1;p1\" />" 
      + "<input type=\"hidden\" id=\"w4t_ajaxEnabled\" " 
      + "name=\"w4t_ajaxEnabled\" value=\"false\" />" 
      + "<input type=\"hidden\" id=\"requestCounter\" " 
      + "name=\"requestCounter\" value=\"0\" />" 
      + "<table id=\"p1\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" " 
      + "border=\"0\"><tr><td align=\"center\" valign=\"middle\" colspan=\"3\">" 
      + "<span id=\"p2\" class=\"w4tCsscd1f6403\">" 
      + "This is the W4Toolkit Fixture Form!</span></td></tr></table>" 
      + "</form></body></html>";
    assertEquals( expected, allMarkup );
  }
  
  public void testStartupFormAjax() throws Exception {
    Fixture.fakeResponseWriter();
    Fixture.fakeRequestParam( RequestParams.STARTUP, "true" );
    Fixture.fakeBrowser( new Ie5_5up( true, true ) );
    W4TModelUtil.initModel();
    LifeCycle lifeCycle = ( LifeCycle )W4TContext.getLifeCycle();
    lifeCycle.execute();
    
    IServiceStateInfo stateInfo = ContextProvider.getStateInfo();
    String allMarkup = Fixture.getAllMarkup( stateInfo.getResponseWriter() );
    String expected
      = "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\">"
      + "<html><head><title>W4 Toolkit</title><style type=\"text/css\">"
      + ".w4tCsscd1f6403 { font-family:arial,verdana;font-size:8pt; } "
      + "</style><meta http-equiv=\"content-type\" content=\"text/html; "
      + "charset=UTF-8\" /><meta http-equiv=\"cache-control\" "
      + "content=\"no-cache\" />"
      + "<script charset=\"UTF-8\" " 
      + "src=\"http://fooserver:8080/fooapp/"
      + "resources/js/eventhandler/eventhandler_ie.js\" " 
      + "type=\"text/javascript\"></script>"
      + "<script charset=\"UTF-8\" " 
      + "src=\"http://fooserver:8080/fooapp/"
      + "resources/js/windowmanager/windowmanager.js\" " 
      + "type=\"text/javascript\"></script>" 
      + "<script type=\"text/javascript\">" 
      + "windowManager.posX = -1;windowManager.posY = -1;</script>"
      + "<link rel=\"SHORTCUT ICON\" href=\"http://fooserver:8080/fooapp/"
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
      + "active = window.setInterval( 'triggerTimeStamp_DOM()',"
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
      + "name=\"w4t_ajaxEnabled\" value=\"true\" />" 
      + "<input type=\"hidden\" id=\"requestCounter\" "
      + "name=\"requestCounter\" value=\"0\" /><table id=\"p1\" "
      + "width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\">"
      + "<tr><td align=\"center\" valign=\"middle\" colspan=\"3\"><span "
      + "id=\"p2\" class=\"w4tCsscd1f6403\">"
      + "This is the W4Toolkit Fixture Form!</span></td></tr></table>"
      + "</form>"
      + "<img src=\"resources/images/transparent.gif\" "
      + "name=\"w4tTriggerTimeStampImg\" "
      + "id=\"w4tTriggerTimeStampImg\" border=\"0\" height=\"1\" "
      + "width=\"1\" alt=\"keepAlive\" /></body></html>";
    assertEquals( expected, allMarkup );
  }
}