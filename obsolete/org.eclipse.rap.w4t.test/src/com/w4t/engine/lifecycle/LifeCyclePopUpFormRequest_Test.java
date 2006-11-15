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
import com.w4t.*;
import com.w4t.IWindowManager.IWindow;
import com.w4t.ajax.AjaxStatusUtil;
import com.w4t.engine.W4TModelUtil;
import com.w4t.engine.requests.RequestParams;
import com.w4t.engine.service.ContextProvider;
import com.w4t.engine.service.IServiceStateInfo;
import com.w4t.engine.util.FormManager;
import com.w4t.engine.util.WindowManager;
import com.w4t.internal.adaptable.IFormAdapter;
import com.w4t.util.browser.Default;
import com.w4t.util.browser.Ie5_5up;


public class LifeCyclePopUpFormRequest_Test extends TestCase {
  
  private final class PopUpHandler implements PhaseListener {
    private static final long serialVersionUID = 1L;
    public void afterPhase( final PhaseEvent event ) {
    }
    public void beforePhase( final PhaseEvent event ) {
      WebForm popUp = Fixture.loadStartupForm();
      IWindow window = W4TContext.showInNewWindow( popUp );
      assertSame( window, W4TContext.showInNewWindow( popUp ) );
    }
    public PhaseId getPhaseId() {
      return PhaseId.PROCESS_ACTION;
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

  public void testOpenPopUpNoScript() throws Exception {
    // actually no popups in noscript -> use of FormCache
    Fixture.fakeResponseWriter();
    Fixture.fakeBrowser( new Default( false, false ) );
    W4TModelUtil.initModel();
    prepareFormAndRequestParms();
    LifeCycle lifeCycle = ( LifeCycle )W4TContext.getLifeCycle();
    PopUpHandler popUpHandler = new PopUpHandler();
    lifeCycle.addPhaseListener( popUpHandler );
    lifeCycle.execute();
    
    IServiceStateInfo stateInfo = ContextProvider.getStateInfo();
    String allMarkup = Fixture.getAllMarkup( stateInfo.getResponseWriter() );
    String expected
      =   "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\">"
        + "<html><head><title>W4 Toolkit</title><style type=\"text/css\">"
        + ".w4tCsscd1f6403 { font-family:arial,verdana;font-size:8pt; } "
        + "</style><meta http-equiv=\"refresh\" content=\"0; "
        + "url=http://fooserver:8080/fooapp/W4TDelegate?uiRoot="
        + "w2;p3&requestCounter=-1&w4t_paramlessGET=true\"> "
        + "<meta http-equiv=\"content-type\" "
        + "content=\"text/html; charset=UTF-8\" />"
        + "<meta http-equiv=\"cache-control\" content=\"no-cache\" /> "
        + "<link rel=\"SHORTCUT ICON\" "
        + "href=\"http://fooserver:8080/fooapp/"
        + "resources/images/favicon.ico\" />"
        + "<link rel=\"icon\" href=\"http://fooserver:8080/fooapp/"
        + "resources/images/favicon.ico\" "
        + "type=\"image/ico\" /></head><body  bgcolor=\"#ffffff\" "
        + "text=\"#000000\" leftmargin=\"0\" topmargin=\"0\" "
        + "marginheight=\"0\" marginwidth=\"0\"  >"
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
        + "<span class=\"w4tCsscd1f6403\">This is the W4Toolkit Fixture Form!" 
        + "</span></td></tr></table></form></body></html>";
    assertEquals( expected, allMarkup );
  }
  
  public void testOpenCloseOpenNoscript() throws Exception {
    // Simulates an open Form1 -> open Form2 -> close Form2 -> open Form2 cycle
    //
    // set up environment 
    Fixture.fakeResponseWriter();
    Fixture.fakeBrowser( new Default( false, false ) );
    W4TModelUtil.initModel();
    prepareFormAndRequestParms();
    LifeCycle lifeCycle = ( LifeCycle )W4TContext.getLifeCycle();
    //
    // Simulate first request (opening form)
    PopUpHandler popUpHandler = new PopUpHandler();
    lifeCycle.addPhaseListener( popUpHandler );
    FormManager.setActive( null );
    WindowManager.setActive( null );
    lifeCycle.execute();
    assertEquals( false, ContextProvider.getStateInfo().isExpired() );
    IServiceStateInfo stateInfo = ContextProvider.getStateInfo();
    String allMarkup = Fixture.getAllMarkup( stateInfo.getResponseWriter() );
    String expected
      =   "<meta http-equiv=\"refresh\" content=\"0; "
        + "url=http://fooserver:8080/fooapp/W4TDelegate?"
        + "uiRoot=w2;p3&requestCounter=-1&w4t_paramlessGET=true\">";
    assertTrue( allMarkup.indexOf( expected ) != -1 );
    expected = "form id=\"p1\"";
    assertTrue( allMarkup.indexOf( expected ) != -1 );
    WebForm form = FormManager.findById( "p3" );
    assertEquals( false, form.isOpeningNewWindow() );
    assertEquals( false, form.isRefreshing() );
    //
    // Simulate request issued by 'meta refresh' tag:
    Fixture.fakeRequestParam( RequestParams.UIROOT, "w2;p3" );
    Fixture.fakeRequestParam( "requestCounter", "-1" );
    Fixture.fakeRequestParam( "w4t_paramlessGET", "true" );
    Fixture.fakeResponseWriter();
    lifeCycle.removePhaseListener( popUpHandler );
    FormManager.setActive( null );
    WindowManager.setActive( null );
    lifeCycle.execute();
    assertEquals( false, ContextProvider.getStateInfo().isExpired() );
    allMarkup = Fixture.getAllMarkup();
    assertTrue( allMarkup.indexOf( "http-equiv=\"refresh\"" ) == -1 );
    assertTrue( allMarkup.indexOf( "form id=\"p3\"" ) != -1 );
    //
    // Simulate close request of p3
    PhaseListener closeHandler = new PhaseListener() {
      private static final long serialVersionUID = 1L;
      public void afterPhase( final PhaseEvent event ) {
      }
      public void beforePhase( final PhaseEvent event ) {
        FormManager.getActive().closeWindow();
      }
      public PhaseId getPhaseId() {
        return PhaseId.PROCESS_ACTION;
      }
      
    };
    lifeCycle.addPhaseListener( closeHandler );
    Fixture.fakeRequestParam( RequestParams.UIROOT, "w2;p3" );
    Fixture.fakeRequestParam( "requestCounter", "0" );
    Fixture.fakeRequestParam( "w4t_paramlessGET", null );
    Fixture.fakeResponseWriter();
    FormManager.setActive( null );
    WindowManager.setActive( null );
    lifeCycle.execute();
    assertEquals( false, ContextProvider.getStateInfo().isExpired() );
    allMarkup = Fixture.getAllMarkup();
    expected 
      =   "<meta http-equiv=\"refresh\" content=\"0; " 
        + "url=http://fooserver:8080/fooapp/W4TDelegate?"
        + "uiRoot=w1;p1&requestCounter=1&w4t_paramlessGET=true\">";
    assertTrue( allMarkup.indexOf( expected ) != -1 );
    expected = "form id=\"p3\"";
    assertTrue( allMarkup.indexOf( expected ) != -1 );
    //
    // Simulate request issued by 'meta refresh' tag in response to 'close p3'
    lifeCycle.removePhaseListener( closeHandler );
    Fixture.fakeRequestParam( RequestParams.UIROOT, "w1;p1" );
    Fixture.fakeRequestParam( "requestCounter", "1" );
    Fixture.fakeRequestParam( "w4t_paramlessGET", "true" );
    Fixture.fakeResponseWriter();
    FormManager.setActive( null );
    WindowManager.setActive( null );
    lifeCycle.execute();
    assertEquals( false, ContextProvider.getStateInfo().isExpired() );
    allMarkup = Fixture.getAllMarkup();
    assertTrue( allMarkup.indexOf( "http-equiv=\"refresh\"" ) == -1 );
    expected = "form id=\"p1\"";
    assertTrue( allMarkup.indexOf( expected ) != -1 );
    // 
    // Simulate request that opens the p3-form a second time
    PhaseListener reOpenHandler = new PhaseListener() {
      private static final long serialVersionUID = 1L;
      public void afterPhase( final PhaseEvent event ) {
      }
      public void beforePhase( final PhaseEvent event ) {
        WebForm form = FormManager.findById( "p3" );
        W4TContext.showInNewWindow( form );
      }
      public PhaseId getPhaseId() {
        return PhaseId.PROCESS_ACTION;
      }
    };
    lifeCycle.addPhaseListener( reOpenHandler );
    Fixture.fakeRequestParam( RequestParams.UIROOT, "w1;p1" );
    Fixture.fakeRequestParam( "requestCounter", "2" );
    Fixture.fakeRequestParam( "w4t_paramlessGET", null );
    Fixture.fakeResponseWriter();
    FormManager.setActive( null );
    WindowManager.setActive( null );
    lifeCycle.execute();
    allMarkup = Fixture.getAllMarkup();
    assertEquals( false, ContextProvider.getStateInfo().isExpired() );
    expected
      =   "<meta http-equiv=\"refresh\" content=\"0; "
        + "url=http://fooserver:8080/fooapp/W4TDelegate?"
        + "uiRoot=w3;p3&requestCounter=1&w4t_paramlessGET=true\">";
    assertTrue( allMarkup.indexOf( expected ) != -1 );
    expected = "form id=\"p1\"";
    assertTrue( allMarkup.indexOf( expected ) != -1 );
  }
  
  public void testOpenPopUpScript() throws Exception {
    Fixture.fakeResponseWriter();
    Fixture.fakeBrowser( new Default( true, false ) );
    W4TModelUtil.initModel();
    prepareFormAndRequestParms();
    LifeCycle lifeCycle = ( LifeCycle )W4TContext.getLifeCycle();
    lifeCycle.addPhaseListener( new PopUpHandler() );
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
        + "windowManager.scrollWindow( 0, 0 ); windowManager.setName( 'w1' );"
        + "openNewWindow(\'http://fooserver:8080/fooapp/W4TDelegate?"
        + "uiRoot=w2;p3&requestCounter=-1&w4t_paramlessGET=true\', " 
        + "\'w2\', \'dependent=no,directories=no,height=600,location=no,"
        + "menubar=no,resizable=yes,status=yes,scrollbars=yes,toolbar=no," 
        + "width=600,fullscreen=no,\');"
        + " active = window.setInterval( 'windowManager.triggerTimeStamp()', "
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
        + "This is the W4Toolkit Fixture Form!</span></td></tr></table>"
        + "</form></body>"
        + "</html>";
    assertEquals( expected, allMarkup );
  }
  
  public void testOpenPopUpAjax() throws Exception {
    Fixture.fakeResponseWriter();
    Fixture.fakeBrowser( new Ie5_5up( true, true ) );
    W4TModelUtil.initModel();
    prepareFormAndRequestParamsAjax();
    LifeCycle lifeCycle = ( LifeCycle )W4TContext.getLifeCycle();
    PopUpHandler popUpHandler = new PopUpHandler();
    lifeCycle.addPhaseListener( popUpHandler );
    lifeCycle.execute();
    
    IServiceStateInfo stateInfo = ContextProvider.getStateInfo();
    String allMarkup = Fixture.getAllMarkup( stateInfo.getResponseWriter() );
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
        + "<script type=\"text/javascript\">openNewWindow(\'"
        + "http://fooserver:8080/fooapp/W4TDelegate?"
        + "uiRoot=w2;p3&amp;requestCounter=-1&amp;w4t_paramlessGET=true\', "
        + "\'w2\', \'dependent=no,directories=no,height=600,location=no,"
        + "menubar=no,resizable=yes,status=yes,scrollbars=yes,toolbar=no,"
        + "width=600,fullscreen=no,\');</script></ajax-response>";
    assertEquals( expected, allMarkup );
    
    // test refresh on popup
    WebForm form = FormManager.getAll()[ 0 ];
    form.refreshWindow();
    Fixture.fakeResponseWriter();
    Fixture.fakeRequestParam( RequestParams.REQUEST_COUNTER, "1" );
    lifeCycle.removePhaseListener( popUpHandler );
    lifeCycle.execute();
    allMarkup = Fixture.getAllMarkup( stateInfo.getResponseWriter() );
    expected
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
        + "name=\"requestCounter\" value=\"2\" />"
        + "<script type=\"text/javascript\">refreshWindow( \'"
        + "http://fooserver:8080/fooapp/W4TDelegate?"
        + "uiRoot=w2;p3&amp;requestCounter=-1&amp;w4t_paramlessGET=true\', "
        + "\'w2\', \'dependent=no,directories=no,height=600,location=no,"
        + "menubar=no,resizable=yes,status=yes,scrollbars=yes,toolbar=no,"
        + "width=600,fullscreen=no,\');</script></ajax-response>";
    assertEquals( expected, allMarkup );

  }
  
  private void prepareFormAndRequestParms() throws Exception {
    WebForm form = Fixture.loadStartupForm();
    IWindow window = WindowManager.getInstance().create( form );
    IFormAdapter adapter = Fixture.getFormAdapter( form );
    adapter.increase();
    String formId = form.getUniqueID();
    String requestCounter = String.valueOf( adapter.getRequestCounter() - 1 );
    Fixture.fakeFormRequestParams( requestCounter, window.getId(), formId );
  }

  private void prepareFormAndRequestParamsAjax() throws Exception {
    prepareFormAndRequestParms();
    Fixture.fakeRequestParam( RequestParams.IS_AJAX_REQUEST, "true" );
    WebForm form = FormManager.getAll()[ 0 ];
    AjaxStatusUtil.preRender( form );
    AjaxStatusUtil.postRender( form );
  }

}