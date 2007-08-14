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

import org.eclipse.rwt.internal.browser.Browser;
import org.eclipse.rwt.internal.browser.Default;
import org.eclipse.rwt.internal.lifecycle.LifeCycle;
import org.eclipse.rwt.internal.service.ContextProvider;
import org.eclipse.rwt.internal.service.RequestParams;

import com.w4t.*;
import com.w4t.IWindowManager.IWindow;
import com.w4t.engine.W4TModelUtil;
import com.w4t.engine.util.WindowManager;
import com.w4t.event.WebItemEvent;
import com.w4t.event.WebItemListener;
import com.w4t.internal.adaptable.IFormAdapter;


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
    W4TFixture.fakeResponseWriter();
    W4TModelUtil.initModel();
    RadioButtonForm form = new RadioButtonForm();
    form.setWebComponents();
    W4TFixture.fakeEngineForRequestLifeCycle( form );
    W4TFixture.fakeBrowser( new Default( false, false ) );
    // initial form rendering: radio1 is selected
    LifeCycle lifeCycle = ( LifeCycle )W4TContext.getLifeCycle();
    lifeCycle.execute();
    String markup = W4TFixture.getAllMarkup();
    assertTrue( markup.indexOf( "radio1" ) != -1 );
    assertTrue( markup.indexOf( "radio2" ) != -1 );
    assertTrue( markup.indexOf( "checked" ) != -1 );
    assertTrue( markup.indexOf( "checked" ) < markup.indexOf( "radio2" ) );
    // fake request as if radio1 was clicked
    clearRequestParams();
    W4TFixture.fakeResponseWriter();
    fakeFormRequestParams( form );
    fakeNoscriptClick( form.radio1 );
    lifeCycle.execute();
    assertEquals( form.group.getValue(), form.radio1.getValue() );
    assertEquals( true, form.radio1.isSelected() );
    markup = W4TFixture.getAllMarkup();
    assertTrue( markup.indexOf( "radio1" ) != -1 );
    assertTrue( markup.indexOf( "radio2" ) != -1 );
    // fake second request as if radio1 was clicked
    clearRequestParams();
    W4TFixture.fakeResponseWriter();
    fakeFormRequestParams( form );
    fakeNoscriptClick( form.radio1 );
    lifeCycle.execute();
    assertEquals( form.group.getValue(), form.radio1.getValue() );
    assertEquals( true, form.radio1.isSelected() );
    markup = W4TFixture.getAllMarkup();
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
    W4TFixture.fakeRequestParam( "wie" + radioButton.getUniqueID() + ".x", "8" );
    W4TFixture.fakeRequestParam( "wie" + radioButton.getUniqueID() + ".y", "8" );
    String groupId = group.getUniqueID();
    W4TFixture.fakeRequestParam( groupId, buttons[ index ].getValue() );
  }
  
  private static void fakeFormRequestParams( final WebForm form ) {
    Browser browser = W4TContext.getBrowser();
    String isAjax = Boolean.toString( browser.isAjaxEnabled() );
    String isScript = Boolean.toString( browser.isScriptEnabled() );
    W4TFixture.fakeRequestParam( RequestParams.AJAX_ENABLED, isAjax );
    W4TFixture.fakeRequestParam( RequestParams.SCRIPT, isScript );
    IFormAdapter adapter = W4TFixture.getFormAdapter( form );
    String formId = form.getUniqueID();
    String requestCounter = String.valueOf( adapter.getRequestCounter() - 1 );
    IWindow window = WindowManager.getInstance().findWindow( form );
    W4TFixture.fakeFormRequestParams( requestCounter, window.getId(), formId );
  }
  
  private static void clearRequestParams() {
    HttpServletRequest request = ContextProvider.getRequest();
    request.getParameterMap().clear();
  }
  
  protected void setUp() throws Exception {
    W4TFixture.setUp();
    W4TFixture.createContext();
  }
  
  protected void tearDown() throws Exception {
    W4TFixture.tearDown();
    W4TFixture.removeContext();
  }
}
