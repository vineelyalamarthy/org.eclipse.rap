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

import javax.servlet.http.HttpServletRequest;
import junit.framework.TestCase;
import com.w4t.*;
import com.w4t.IWindowManager.IWindow;
import com.w4t.engine.W4TModelUtil;
import com.w4t.engine.lifecycle.LifeCycle;
import com.w4t.engine.requests.RequestParams;
import com.w4t.engine.service.ContextProvider;
import com.w4t.engine.util.WindowManager;
import com.w4t.event.WebItemEvent;
import com.w4t.event.WebItemListener;
import com.w4t.internal.adaptable.IFormAdapter;
import com.w4t.util.browser.Default;


public class WebRadioButtonBehaviour_Test extends TestCase {
  
  public static class RadioButtonForm extends WebForm {

    public WebRadioButtonGroup group;
    public WebRadioButton radio1;
    public WebRadioButton radio2;
    public String eventLog = "";
    
    protected void setWebComponents() throws Exception {
      setWebLayout( new WebFlowLayout() );
      setWindowTitle( "RadioButtonTestForm" );
      group = new WebRadioButtonGroup();
      group.addWebItemListener( new WebItemListener() {
        public void webItemStateChanged( final WebItemEvent e ) {
          eventLog += e.getSourceComponent().getUniqueID();
        }
      } );
      add( group );
      radio1 = new WebRadioButton();
      radio1.setName( "radio1" );
      radio1.setLabel( "radio1" );
      radio1.setValue( "radioValue1" );
      radio2 = new WebRadioButton();
      radio1.setName( "radio2" );
      radio2.setLabel( "radio2" );
      radio1.setValue( "radioValue2" );
      group.add( radio1 );
      group.add( radio2 );
      group.setValue( radio1.getValue() );
    }
  }
  
  public void testNoscript() throws Exception {
    // prepare
    Fixture.fakeResponseWriter();
    W4TModelUtil.initModel();
    RadioButtonForm form = new RadioButtonForm();
    form.setWebComponents();
    Fixture.fakeEngineForRequestLifeCycle( form );
    Fixture.fakeBrowser( new Default( false, false ) );
    // initial form rendering: radio1 is selected
    LifeCycle lifeCycle = ( LifeCycle )W4TContext.getLifeCycle();
    lifeCycle.execute();
    String markup = Fixture.getAllMarkup();
    assertTrue( markup.indexOf( "radio1" ) != -1 );
    assertTrue( markup.indexOf( "radio2" ) != -1 );
    assertTrue( markup.indexOf( "checked" ) != -1 );
    assertTrue( markup.indexOf( "checked" ) < markup.indexOf( "radio2" ) );
    // fake request as if radio1 was clicked
    clearRequestParams();
    Fixture.fakeResponseWriter();
    fakeFormRequestParams( form );
    fakeNoscriptClick( form.radio1 );
    lifeCycle.execute();
    assertEquals( form.group.getValue(), form.radio1.getValue() );
    assertEquals( true, form.radio1.isSelected() );
    markup = Fixture.getAllMarkup();
    assertTrue( markup.indexOf( "radio1" ) != -1 );
    assertTrue( markup.indexOf( "radio2" ) != -1 );
    // fake second request as if radio1 was clicked
    clearRequestParams();
    Fixture.fakeResponseWriter();
    fakeFormRequestParams( form );
    fakeNoscriptClick( form.radio1 );
    lifeCycle.execute();
    assertEquals( form.group.getValue(), form.radio1.getValue() );
    assertEquals( true, form.radio1.isSelected() );
    markup = Fixture.getAllMarkup();
    assertTrue( markup.indexOf( "radio1" ) != -1 );
    assertTrue( markup.indexOf( "radio2" ) != -1 );
  }
  
  private static void fakeNoscriptClick( final WebRadioButton radioButton ) {
    int index = -1;
    WebRadioButtonGroup group = WebRadioButtonUtil.findGroup( radioButton );
    WebRadioButton[] buttons = WebRadioButtonUtil.findButtons( group );
    for( int i = 0; index == -1 && i < buttons.length; i++ ) {
      if( buttons[ i ] == radioButton ) {
        index = i;
      }
    }
    Fixture.fakeRequestParam( "wie" + radioButton.getUniqueID() + ".x", "8" );
    Fixture.fakeRequestParam( "wie" + radioButton.getUniqueID() + ".y", "8" );
    String groupId = group.getUniqueID();
    Fixture.fakeRequestParam( groupId, buttons[ index ].getValue() );
  }
  
  private static void fakeFormRequestParams( final WebForm form ) {
    Browser browser = W4TContext.getBrowser();
    String isAjax = Boolean.toString( browser.isAjaxEnabled() );
    String isScript = Boolean.toString( browser.isScriptEnabled() );
    Fixture.fakeRequestParam( RequestParams.AJAX_ENABLED, isAjax );
    Fixture.fakeRequestParam( RequestParams.SCRIPT, isScript );
    IFormAdapter adapter = Fixture.getFormAdapter( form );
    String formId = form.getUniqueID();
    String requestCounter = String.valueOf( adapter.getRequestCounter() - 1 );
    IWindow window = WindowManager.getInstance().findWindow( form );
    Fixture.fakeFormRequestParams( requestCounter, window.getId(), formId );
  }
  
  private static void clearRequestParams() {
    HttpServletRequest request = ContextProvider.getRequest();
    request.getParameterMap().clear();
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
