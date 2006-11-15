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

import junit.framework.TestCase;
import com.w4t.*;
import com.w4t.IWindowManager.IWindow;
import com.w4t.engine.lifecycle.LifeCycle;
import com.w4t.engine.requests.RequestParams;
import com.w4t.engine.service.ContextProvider;
import com.w4t.engine.service.IServiceStateInfo;
import com.w4t.engine.util.WindowManager;
import com.w4t.internal.adaptable.IFormAdapter;
import com.w4t.util.browser.Default;


public class Focus_Test extends TestCase {

  public static final class FocusTestForm extends WebForm {

    WebText text;
    WebButton button;
    
    protected void setWebComponents() throws Exception {
      setWebLayout( new WebFlowLayout() );
      button = new WebButton();
      add( button );
      text = new WebText();
      add( text );
    }
  }

  protected void setUp() throws Exception {
    Fixture.setUp();
    Fixture.createContext();
  }
  
  protected void tearDown() throws Exception {
    Fixture.tearDown();
    Fixture.removeContext();
  }
  
  public void testSetFocus() throws Exception {
    WebButton button = new WebButton();
    button.setFocus( true );
    assertFalse( button.hasFocus() );
    
    WebForm form = Fixture.getEmptyWebFormInstance();
    WebPanel panel = new WebPanel();
    form.add( panel, WebBorderLayout.NORTH );
    panel.add( button );
    button.setFocus( true );
    assertTrue( button.hasFocus() );
    assertEquals( button.getUniqueID(), form.retrieveFocusID() );
    button.remove();
    assertFalse( button.hasFocus() );
    assertEquals( "", form.retrieveFocusID() );
    panel.add( button );
    button.setLink( true );
    button.setFocus( true );
    assertEquals( "", form.retrieveFocusID() );
  }
  
  public void testNoFocusElementRequestParam() throws Exception {
    // prepare form with button and text
    FocusTestForm form 
      = ( FocusTestForm )W4TContext.loadForm( FocusTestForm.class.getName() );
    Fixture.fakeBrowser( new Default( true, false ) );
    Fixture.fakeEngineForRequestLifeCycle( form );
    fakeRequestCounter( form );
    // no focusElement-RequestParam -> no focus change
    form.button.setFocus( true );
    LifeCycle lifeCycle = ( LifeCycle )W4TContext.getLifeCycle();
    lifeCycle.execute();
    assertEquals(true, form.button.hasFocus() );
    String allMarkup = getAllMarkup();
    assertTrue( allMarkup.indexOf( "value=\"\"" ) != -1 );
  }
  
  public void testFocusElementRequestParamWithIdOnly() throws Exception {
    // prepare form with button and text
    FocusTestForm form 
      = ( FocusTestForm )W4TContext.loadForm( FocusTestForm.class.getName() );
    Fixture.fakeEngineForRequestLifeCycle( form );
    Fixture.fakeBrowser( new Default( true, false ) );
    fakeRequestCounter( form );
    // test request which sets focus to different component
    Fixture.fakeRequestParam( RequestParams.FOCUS_ELEMENT, 
                              form.button.getUniqueID() );
    form.text.setFocus( true );
    LifeCycle lifeCycle = ( LifeCycle )W4TContext.getLifeCycle();
    lifeCycle.execute();
    assertEquals( false, form.text.hasFocus() );
    assertEquals( true, form.button.hasFocus() );
    String allMarkup = getAllMarkup();
    String expected = "value=\"" + form.button.getUniqueID() + "\"";
    assertTrue( allMarkup.indexOf( expected ) != -1 );
  }
  
  public void testFocusElementRequestParamWithSelection() throws Exception {
    // prepare form with button and text
    FocusTestForm form 
      = ( FocusTestForm )W4TContext.loadForm( FocusTestForm.class.getName() );
    Fixture.fakeEngineForRequestLifeCycle( form );
    Fixture.fakeBrowser( new Default( true, false ) );
    fakeRequestCounter( form );
    // test request which sets focus to different component
    Fixture.fakeRequestParam( RequestParams.FOCUS_ELEMENT, 
                              form.text.getUniqueID() + ";1;4" );
    form.button.setFocus( true );
    LifeCycle lifeCycle = ( LifeCycle )W4TContext.getLifeCycle();
    lifeCycle.execute();
    assertEquals( true, form.text.hasFocus() );
    assertEquals( false, form.button.hasFocus() );
    String allMarkup = getAllMarkup();
    String expected = "value=\"" + form.text.getUniqueID() + ";1;4" + "\"";
    assertTrue( allMarkup.indexOf( expected ) != -1 );
  }

  private static String getAllMarkup() {
    IServiceStateInfo stateInfo = ContextProvider.getStateInfo();
    return Fixture.getAllMarkup( stateInfo.getResponseWriter() );
  }
  
  private static void fakeRequestCounter( WebForm form ) {
    IFormAdapter adapter = Fixture.getFormAdapter( form );
    adapter.increase();
    String formId = form.getUniqueID();
    String requestCounter = String.valueOf( adapter.getRequestCounter() - 1 );
    IWindow window = WindowManager.getInstance().findWindow( form );
    Fixture.fakeFormRequestParams( requestCounter, window.getId(), formId );
  }
  
}
