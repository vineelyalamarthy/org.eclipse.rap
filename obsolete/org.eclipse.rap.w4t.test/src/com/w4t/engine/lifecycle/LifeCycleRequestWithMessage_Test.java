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
import org.eclipse.rwt.internal.lifecycle.LifeCycle;
import org.eclipse.rwt.internal.service.*;
import org.eclipse.rwt.lifecycle.*;

import junit.framework.TestCase;

import com.w4t.*;
import com.w4t.IWindowManager.IWindow;
import com.w4t.engine.W4TModelUtil;
import com.w4t.engine.util.FormManager;
import com.w4t.engine.util.WindowManager;
import com.w4t.internal.adaptable.IFormAdapter;


public class LifeCycleRequestWithMessage_Test extends TestCase {
  
  private final static String MESSAGE = "Test Message";
  
  private final static class MessageDispatcher implements PhaseListener {
    private static final long serialVersionUID = 1L;
    public void afterPhase( final PhaseEvent event ) {
      W4TContext.addMessage( new Message( MESSAGE ) );
    }
    public void beforePhase( final PhaseEvent event ) {
    }
    public PhaseId getPhaseId() {
      return PhaseId.PROCESS_ACTION;
    }
  }
  
  
  protected void setUp() throws Exception {
    W4TFixture.setUp();
    W4TFixture.createContext( false );
  }
  
  protected void tearDown() throws Exception {
    W4TFixture.tearDown();
    W4TFixture.removeContext();
  }

  public void testRequestWithMessageScript() throws Exception {
    W4TFixture.fakeResponseWriter();
    W4TFixture.fakeBrowser( new Default( true, false ) );
    W4TModelUtil.initModel();
    WebForm form = prepareFormAndRequestParms();
    LifeCycle lifeCycle = ( LifeCycle )W4TContext.getLifeCycle();
    lifeCycle.execute();
    
    W4TFixture.fakeResponseWriter();
    String requestCounter = getRequestCounter( form );
    W4TFixture.fakeRequestParam( RequestParams.REQUEST_COUNTER, requestCounter );
    FormManager.setActive( null );
    MessageDispatcher messageDispatcher = new MessageDispatcher();
    lifeCycle.addPhaseListener( messageDispatcher );
    lifeCycle.execute();
    
    IServiceStateInfo stateInfo = ContextProvider.getStateInfo();
    String allMarkup = W4TFixture.getAllMarkup( stateInfo.getResponseWriter() );
    // form markup from render buffer + open script for error form/window
    String expectedStart
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
        + "uiRoot=w2;p3&requestCounter=-1&w4t_paramlessGET=true&nocache=";
    String expectedEnd
      =   "\', \'w2\', \'dependent=no,directories=no,height=600,location=no,"
        + "menubar=no,resizable=yes,status=yes,scrollbars=yes,toolbar=no,"
        + "width=800,fullscreen=no,\');"
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
        + "name=\"requestCounter\" value=\"2\" /><table id=\"p1\" "
        + "width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\">"
        + "<tr><td align=\"center\" valign=\"middle\" colspan=\"3\"><span "
        + "id=\"p2\" class=\"w4tCsscd1f6403\">"
        + "This is the W4Toolkit W4TFixture Form!</span></td></tr></table>"
        + "</form></body>"
        + "</html>";
    assertTrue( allMarkup.startsWith( expectedStart ) );
    assertTrue( allMarkup.endsWith( expectedEnd ) );
    
    W4TFixture.fakeResponseWriter();
    W4TFixture.fakeFormRequestParams( "-1", "w2", "p3" );
    FormManager.setActive( null );
    WindowManager.setActive( null );
    lifeCycle.removePhaseListener( messageDispatcher );
    lifeCycle.execute();
    allMarkup = W4TFixture.getAllMarkup( stateInfo.getResponseWriter() );
    // ensure that the result contains a W4Toolkit WebForm
    String formMarkup
      =   "<form id=\"p3\" action=\"http://fooserver:8080/fooapp/W4TDelegate?"
        + "w4t_enc=no\" name=\"W4TForm\" "
        + "method=\"post\" accept-charset=\"UTF-8\" "
        + "enctype=\"application/x-www-form-urlencoded\">";    
    assertTrue( allMarkup.indexOf( formMarkup ) != -1 );
    // ensure that the result contains the exception message
    assertTrue( allMarkup.indexOf( MESSAGE ) != -1 );
  }
  
  // TODO: [fappel] write test for ajax...
  
  private WebForm prepareFormAndRequestParms() throws Exception {
    WebForm result = W4TFixture.loadStartupForm();
    IWindow window = WindowManager.getInstance().create( result );
    IFormAdapter adapter = W4TFixture.getFormAdapter( result );
    adapter.increase();
    String formId = result.getUniqueID();
    String requestCounter = getRequestCounter( result );
    W4TFixture.fakeFormRequestParams( requestCounter, window.getId(), formId );
    return result;
  }

  private String getRequestCounter( final WebForm form ) {
    IFormAdapter adapter = W4TFixture.getFormAdapter( form );
    return String.valueOf( adapter.getRequestCounter() - 1 );
  }
}
