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

import java.text.MessageFormat;
import junit.framework.TestCase;
import com.w4t.*;
import com.w4t.IWindowManager.IWindow;
import com.w4t.ajax.AjaxStatusUtil;
import com.w4t.engine.W4TModelUtil;
import com.w4t.engine.requests.RequestParams;
import com.w4t.engine.service.ContextProvider;
import com.w4t.engine.service.IServiceStateInfo;
import com.w4t.engine.util.FormManager;
import com.w4t.engine.util.WindowManager;
import com.w4t.event.WebRenderEvent;
import com.w4t.event.WebRenderListener;
import com.w4t.internal.adaptable.IFormAdapter;
import com.w4t.util.browser.Default;
import com.w4t.util.browser.Ie5_5up;


public class LifeCycleFormDispatchRequest_Test extends TestCase {
  
  private static final String AFTER_RENDER = "afterRender";
  private static final String BEFORE_RENDER = "beforeRender";
  private String log = "";
  
  private class EventTester implements WebRenderListener {
    public void beforeRender( final WebRenderEvent e ) {
      log += BEFORE_RENDER + " " + e.getSourceComponent().getUniqueID() + " | ";
    }
    public void afterRender( final WebRenderEvent e ) {
      log += AFTER_RENDER + " " + e.getSourceComponent().getUniqueID();
    }
  }
  private final class DispatchHandler implements PhaseListener {
    private static final long serialVersionUID = 1L;
    private int formToDispatchHashCode = 0;
    public void afterPhase( final PhaseEvent event ) {
    }
    public void beforePhase( final PhaseEvent event ) {
      WebForm oldActive = FormManager.getActive();
      oldActive.unload();
      WebForm toDispatch = Fixture.loadStartupForm();
      toDispatch.addWebRenderListener( new EventTester() );
      formToDispatchHashCode = toDispatch.hashCode();
      W4TContext.dispatchTo( toDispatch );        
    }
    public PhaseId getPhaseId() {
      return PhaseId.PROCESS_ACTION;
    }
    public int getFormToDispatchHashCode() {
      return formToDispatchHashCode;
    }
  }

  protected void setUp() throws Exception {
    Fixture.setUp();
    Fixture.createContext( false );
  }
  
  protected void tearDown() throws Exception {
    Fixture.tearDown();
    Fixture.removeContext();
  }
  
  public void testFormDispatchNoScript() throws Exception {
    Fixture.fakeResponseWriter();
    Fixture.fakeBrowser( new Default( false, false ) );
    W4TModelUtil.initModel();
    WebForm originatingForm = prepareFormAndRequestParms();
    
    LifeCycle lifeCycle = ( LifeCycle )W4TContext.getLifeCycle();
    lifeCycle.addPhaseListener( new DispatchHandler() );
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
        + "href=\"http://fooserver:8080/fooapp/"
        + "resources/images/favicon.ico\" /><link rel=\"icon\" "
        + "href=\"http://fooserver:8080/fooapp/"
        + "resources/images/favicon.ico\" type=\"image/ico\" /></head>"
        + "<body  bgcolor=\"#ffffff\" text=\"#000000\" leftmargin=\"0\" "
        + "topmargin=\"0\" marginheight=\"0\" marginwidth=\"0\"  >"
        + "<form id=\"p3\" "
        + "action=\"http://fooserver:8080/fooapp/W4TDelegate?w4t_enc=no\" "
        + "name=\"W4TForm\" method=\"post\" accept-charset=\"UTF-8\" "
        + "enctype=\"application/x-www-form-urlencoded\">"
        + "<input type=\"hidden\" id=\"uiRoot\" " 
        + "name=\"uiRoot\" value=\"w1;p3\" />"
        + "<input type=\"hidden\" id=\"w4t_ajaxEnabled\" " 
        + "name=\"w4t_ajaxEnabled\" value=\"false\" />" 
        + "<input type=\"hidden\" id=\"requestCounter\" "
        + "name=\"requestCounter\" value=\"0\" /><table id=\"p3\" "
        + "width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\">"
        + "<tr><td align=\"center\" valign=\"middle\" colspan=\"3\">"
        + "<span class=\"w4tCsscd1f6403\">This is the W4Toolkit Fixture Form!" 
        + "</span></td></tr></table></form></body></html>";
    assertEquals( expected, allMarkup );
    assertEquals( BEFORE_RENDER + " p3 | " + AFTER_RENDER + " p3", log );
    assertNull( FormManager.findById( originatingForm.getUniqueID() ) );
    
    // test expired request
    Fixture.fakeResponseWriter();
    log = "";
    FormManager.setActive( null );
    WindowManager.setActive( null );
    lifeCycle.execute();
    allMarkup = Fixture.getAllMarkup( stateInfo.getResponseWriter() );
    assertEquals( expected, allMarkup );
    assertEquals( "", log );
  }
  
  public void testFormDispatchScript() throws Exception {
    Fixture.fakeResponseWriter();
    Fixture.fakeBrowser( new Default( true, false ) );
    W4TModelUtil.initModel();
    WebForm originatingForm = prepareFormAndRequestParms();

    LifeCycle lifeCycle = ( LifeCycle )W4TContext.getLifeCycle();
    lifeCycle.addPhaseListener( new DispatchHandler() );
    lifeCycle.execute();

    IServiceStateInfo stateInfo = ContextProvider.getStateInfo();
    String allMarkup = Fixture.getAllMarkup( stateInfo.getResponseWriter() );
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
        + "<form id=\"p3\" "
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
        + "name=\"uiRoot\" value=\"w1;p3\" />"
        + "<input type=\"hidden\" id=\"w4t_ajaxEnabled\" " 
        + "name=\"w4t_ajaxEnabled\" value=\"false\" />" 
        + "<input type=\"hidden\" id=\"requestCounter\" "
        + "name=\"requestCounter\" value=\"0\" /><table id=\"p3\" "
        + "width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\">"
        + "<tr><td align=\"center\" valign=\"middle\" colspan=\"3\"><span "
        + "id=\"p4\" class=\"w4tCsscd1f6403\">"
        + "This is the W4Toolkit Fixture Form!</span></td></tr></table>"
        + "</form></body>"
        + "</html>";
    assertEquals( expected, allMarkup );
    assertEquals( BEFORE_RENDER + " p3 | " +  AFTER_RENDER + " p3", log );
    assertNull( FormManager.findById( originatingForm.getUniqueID() ) );
    
    
    // test expired request
    Fixture.fakeResponseWriter();
    log = "";
    FormManager.setActive( null );
    WindowManager.setActive( null );
    lifeCycle.execute();
    allMarkup = Fixture.getAllMarkup( stateInfo.getResponseWriter() );
    assertEquals( expected, allMarkup );
    assertEquals( "", log );
  }
  
  public void testFormDispatchAjax() throws Exception {
    Fixture.fakeResponseWriter();
    Fixture.fakeBrowser( new Ie5_5up( true, true ) );
    W4TModelUtil.initModel();
    WebForm originatingForm = prepareFormAndRequestParamsAjax();
    LifeCycle lifeCycle = ( LifeCycle )W4TContext.getLifeCycle();
    DispatchHandler dispatchHandler = new DispatchHandler();
    lifeCycle.addPhaseListener( dispatchHandler );
    lifeCycle.execute();
    
    IServiceStateInfo stateInfo = ContextProvider.getStateInfo();
    String allMarkup = Fixture.getAllMarkup( stateInfo.getResponseWriter() );
    String expected1
      =   "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>"
        + "<ajax-response><script type=\"text/javascript\">refreshWindow( " 
        + "'http://fooserver:8080/fooapp/W4TDelegate?uiRoot=w1;p3&amp;"
        + "requestCounter=-1&amp;w4t_paramlessGET=true&amp;nocache=" 
        + dispatchHandler.getFormToDispatchHashCode()
        + "', 'w1', 'dependent=no,directories=no,height=600,location=no,"
        + "menubar=no,resizable=yes,status=yes,scrollbars=yes,toolbar=no,"
        + "width=600,fullscreen=no,');</script></ajax-response>";
    assertEquals( expected1, allMarkup );
    assertEquals( "", log );
    String formId = originatingForm.getUniqueID();
    assertNull( FormManager.findById( formId ) );

    Fixture.fakeResponseWriter();
    log = "";
    Fixture.fakeRequestParam( RequestParams.PARAMLESS_GET, "true" );
    Fixture.fakeRequestParam( RequestParams.IS_AJAX_REQUEST, null );
    Fixture.fakeRequestParam( RequestParams.STARTUP, null );
    Fixture.fakeFormRequestParams( "-1", "w1", "p3" );
    lifeCycle.removePhaseListener( dispatchHandler );
    stateInfo.setAttribute( "COM_W4T_FORM_TO_DISPATCH", null );
    lifeCycle.execute();
    allMarkup = Fixture.getAllMarkup( stateInfo.getResponseWriter() );
    // Note: 'expected2' is suitable to be passed to MessageFormat.format
    String expected2
      =   "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\">"
        + "<html><head><title>W4 Toolkit</title><style type=\"text/css\">"
        + ".w4tCsscd1f6403 '{ font-family:arial,verdana;font-size:8pt; }' "
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
        + "onload=\"windowManager.initWebForm( ''false'' );"
        + "eventHandler.disableFocusEvent();" 
        + "setTimeout( ''eventHandler.enableFocusEvent()'', 50 );" 
        + "windowManager.scrollWindow( 0, 0 ); windowManager.setName( ''w1'' ); "
        + "active = window.setInterval( ''triggerTimeStamp_DOM()'',"
        + "1800000);\" onunload=\"w4tClearKeepAlive();\" >"
        + "<form id=\"p3\" "
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
        + "name=\"uiRoot\" value=\"w1;p3\" />"
        + "<input type=\"hidden\" id=\"w4t_ajaxEnabled\" " 
        + "name=\"w4t_ajaxEnabled\" value=\"true\" />" 
        + "<input type=\"hidden\" id=\"requestCounter\" "
        + "name=\"requestCounter\" value=\"{0}\" /><table id=\"p3\" "
        + "width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\">"
        + "<tr><td align=\"center\" valign=\"middle\" colspan=\"3\"><span "
        + "id=\"p4\" class=\"w4tCsscd1f6403\">"
        + "This is the W4Toolkit Fixture Form!</span></td></tr></table>"
        + "</form>"
        + "<img src=\"resources/images/transparent.gif\" "
        + "name=\"w4tTriggerTimeStampImg\" "
        + "id=\"w4tTriggerTimeStampImg\" border=\"0\" height=\"1\" "
        + "width=\"1\" alt=\"keepAlive\" /></body></html>";
    assertEquals( setRequestCounter( expected2, "0" ), allMarkup );
    assertEquals( BEFORE_RENDER + " p3 | " +  AFTER_RENDER + " p3", log );
    
    // test expired request
    Fixture.fakeRequestParam( RequestParams.PARAMLESS_GET, null );
    Fixture.fakeRequestParam( RequestParams.IS_AJAX_REQUEST, "true" );
    Fixture.fakeFormRequestParams( "-1", "w1", formId );
    Fixture.fakeResponseWriter();
    log = "";
    FormManager.setActive( null );
    WindowManager.setActive( null );
    lifeCycle.execute();
    allMarkup = Fixture.getAllMarkup( stateInfo.getResponseWriter() );
    assertEquals( setRequestCounter( expected2, "1" ), allMarkup );
    assertEquals( "", log );
  }

  private String setRequestCounter( final String response, 
                                    final String requestCounter )
  {
    return MessageFormat.format( response, new Object[]{ requestCounter } );
  }

  private WebForm prepareFormAndRequestParms() throws Exception {
    WebForm result = Fixture.loadStartupForm();
    IWindow window = WindowManager.getInstance().create( result );
    IFormAdapter adapter = Fixture.getFormAdapter( result );
    adapter.increase();
    String formId = result.getUniqueID();
    String requestCounter = String.valueOf( adapter.getRequestCounter() - 1 );
    Fixture.fakeFormRequestParams( requestCounter, window.getId(), formId );
    return result;
  }
  
  private WebForm prepareFormAndRequestParamsAjax() throws Exception {
    WebForm result = prepareFormAndRequestParms();
    Fixture.fakeRequestParam( RequestParams.IS_AJAX_REQUEST, "true" );
    WebForm form = FormManager.getAll()[ 0 ];
    AjaxStatusUtil.preRender( form );
    AjaxStatusUtil.postRender( form );
    return result;
  }
}
