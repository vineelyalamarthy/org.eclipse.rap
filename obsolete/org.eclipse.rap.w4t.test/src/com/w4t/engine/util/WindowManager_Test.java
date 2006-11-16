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
package com.w4t.engine.util;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import junit.framework.TestCase;
import com.w4t.*;
import com.w4t.Fixture.TestServletContext;
import com.w4t.Fixture.TestSession;
import com.w4t.IWindowManager.IWindow;
import com.w4t.engine.W4TModelUtil;
import com.w4t.engine.lifecycle.LifeCycle;
import com.w4t.engine.service.ContextProvider;
import com.w4t.internal.adaptable.IFormAdapter;
import com.w4t.util.browser.Default;


public class WindowManager_Test extends TestCase {
  
  public void testCreate() throws Exception {
    IWindowManager windowManager = WindowManager.getInstance();
    assertNotNull( windowManager );
    try {
      windowManager.create( null );
      fail( "WindowManager#create(Form) must not allow null argument." );
    } catch( NullPointerException e ) {
      // as expected
    }
    WebForm form = Fixture.loadStartupForm();
    IWindow window1 = windowManager.create( form );
    assertNotNull( window1 );
    
    try {
      windowManager.create( form );
      fail( "Not allowed to place a form in more than one window." ); 
    } catch( IllegalStateException e ) {
      // as expected
    }
  }
  
  public void testWindowId() {
    IWindowManager windowManager = WindowManager.getInstance();
    IWindow window;
    setServletContextName( null );
    window = windowManager.create( Fixture.getEmptyWebFormInstance() );
    assertEquals( "w1", window.getId() );
    setServletContextName( "" );
    window = windowManager.create( Fixture.getEmptyWebFormInstance() );
    assertEquals( "w2", window.getId() );
    setServletContextName( "myWebApp" );
    window = windowManager.create( Fixture.getEmptyWebFormInstance() );
    assertEquals( "myWebAppw3", window.getId() );
    setServletContextName( "my Web App  " );
    window = windowManager.create( Fixture.getEmptyWebFormInstance() );
    assertEquals( "myWebAppw4", window.getId() );
    setServletContextName( "123App" );
    window = windowManager.create( Fixture.getEmptyWebFormInstance() );
    assertEquals( "_123Appw5", window.getId() );
    setServletContextName( " 1 23Ap p" );
    window = windowManager.create( Fixture.getEmptyWebFormInstance() );
    assertEquals( "_123Appw6", window.getId() );
    setServletContextName( "мпќп" );
    window = windowManager.create( Fixture.getEmptyWebFormInstance() );
    assertEquals( "w7", window.getId() );
  }

  public void testFindByIdAndWindowAndForm() throws Exception {
    IWindowManager windowManager = WindowManager.getInstance();
    // don't allow null in findWindow
    try {
      windowManager.findWindow( null );
      fail( "Must throw NPE" );
    } catch( NullPointerException e ) {
      // as expected
    }
    // find just created window
    WebForm form = Fixture.loadStartupForm();
    IWindow window = windowManager.create( form );
    IWindow foundWindow = windowManager.findWindow( form );
    assertSame( window, foundWindow );
    // don't allow null in findForm 
    try {
      windowManager.findForm( null );
      fail( "Must throw NPE" );
    } catch( Exception e ) {
      // as expected
    }
    // find above 'form' by its associated window
    WebForm foundForm = windowManager.findForm( window );
    assertSame( foundForm, form );
    try {
      windowManager.findById( null );
      fail( "Must throw NPE" );
    } catch( Exception e ) {
      // as expected
    }
    foundWindow = windowManager.findById( window.getId() );
    assertSame( foundWindow, window );
  }
  
  public void testDispatchTo() throws Exception {
    WindowManager windowManager = WindowManager.getInstance();
    WebForm form1 = Fixture.loadStartupForm();
    IWindow window = windowManager.create( form1 );
    WindowManager.setActive( window );
    WebForm form2 = Fixture.loadStartupForm();
    window.setFormToDispatch( form2 );
    WindowManager.doDispatch();
    WebForm foundForm = windowManager.findForm( window );
    assertNull( windowManager.findWindow( form1 ) );
    assertSame( foundForm, form2 );
    assertSame( window, windowManager.findWindow( form2 ) );
  }
  
  public void testRemove() throws Exception {
    WindowManager windowManager = WindowManager.getInstance();
    WebForm form = Fixture.loadStartupForm();
    IWindow window = windowManager.create( form );
    
    windowManager.remove( window );
    IWindow foundWindow = windowManager.findWindow( form );
    assertNull( foundWindow );
  }
  
  public void testRemoveAssociatedWindow() throws Exception {
    IWindowManager windowManager = WindowManager.getInstance();
    WebForm form1 = Fixture.loadStartupForm();
    IWindow window = windowManager.create( form1 );
    form1.unload();
    assertNull( windowManager.findWindow( form1 ) );
    WebForm form2 = Fixture.loadStartupForm();
    WebForm form3 = Fixture.loadStartupForm();
    window = windowManager.create( form2 );
    window.setFormToDispatch( form3 );
    WindowManager.setActive( window );
    WindowManager.doDispatch();
    form2.unload();
    assertEquals( window, windowManager.findWindow( form3 ) );
  }
  
  public void testClose() {
    IWindowManager windowManager = WindowManager.getInstance();
    WebForm form = Fixture.loadStartupForm();
    IWindow window = windowManager.create( form );
    assertEquals( false, WindowManager.isClosing( window ) );
    window.close();
    assertEquals( true, WindowManager.isClosing( window ) );
    assertEquals( false, WindowManager.isClosed( window ) );
  }
  
  /** Ensure that the active window object is functional even though the
   * associated form was unloaded (which leads to the window being closed). */
  public void testUnloadActive() {
    IWindowManager windowManager = WindowManager.getInstance();
    WebForm form = Fixture.loadStartupForm();
    IWindow window = windowManager.create( form );
    WindowManager.setActive( window );
    assertEquals( false, WindowManager.isClosing( window ) );
    window.close();
    assertEquals( true, WindowManager.isClosing( window ) );
    assertEquals( window, WindowManager.getActive() );
    form.unload();
    assertNull( WindowManager.getInstance().findById( window.getId() ) );
    assertEquals( window, WindowManager.getActive() );
    assertEquals( true, WindowManager.isClosing( window ) );
    assertEquals( false, WindowManager.isClosed( window ) );
  }
  
  public void testRefreshWindow() {
    // prepare
    IWindowManager windowManager = WindowManager.getInstance();
    WebForm form = Fixture.loadStartupForm();
    IWindow window = windowManager.create( form );
    // 'basic' refreshWindow 
    form.refreshWindow();
    assertEquals( true, form.isRefreshing() );
    // refreshWindow must override previous closeWindow
    form.closeWindow();
    assertEquals( true, WindowManager.isClosing( window ) );
    assertEquals( false, WindowManager.isClosed( window ) );
    form.refreshWindow();
    assertEquals( true, form.isRefreshing() );
    assertEquals( false, WindowManager.isClosing( window ) );
    assertEquals( false, WindowManager.isClosed( window ) );
    // try to refresh an already closed window 
    // that is, closeWindow request after rendering
    WindowManager.removeAssociatedWindow( form );
    form.refreshWindow();
    assertNotNull( WindowManager.getInstance().findWindow( form ) );
  }
  
  public void testCloseWindow() throws Exception {
    // prepare
    IWindowManager windowManager = WindowManager.getInstance();
    WebForm form = Fixture.loadStartupForm();
    IWindow window = windowManager.create( form );
    form.closeWindow();
    assertEquals( true, WindowManager.isClosing( window ) );
    assertEquals( false, WindowManager.isClosed( window ) );
    Fixture.fakeResponseWriter();
    Fixture.fakeBrowser( new Default( true, false ) );
    W4TModelUtil.initModel();
    IFormAdapter adapter = Fixture.getFormAdapter( form );
    adapter.increase();
    String formId = form.getUniqueID();
    String requestCounter = String.valueOf( adapter.getRequestCounter() - 1 );
    Fixture.fakeFormRequestParams( requestCounter, window.getId(), formId );
    LifeCycle lifeCycle = ( LifeCycle )W4TContext.getLifeCycle();
    lifeCycle.execute();
    assertEquals( true, WindowManager.isClosed( window ) );
  }
  
  protected void setUp() throws Exception {
    Fixture.setUp();
    Fixture.createContext();
  }
  
  protected void tearDown() throws Exception {
    Fixture.tearDown();
    Fixture.removeContext();
  }
  
  private static void setServletContextName( final String name ) {
    HttpSession session = ContextProvider.getRequest().getSession();
    Fixture.TestSession testSession = ( TestSession )session;
    ServletContext servletContext = testSession.getServletContext();
    Fixture.TestServletContext testServletContext;
    testServletContext = ( TestServletContext )servletContext;
    testServletContext.setServletContextName( name );
  }
  
}
