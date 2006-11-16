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
import com.w4t.util.browser.Ie6;


public class FormCloseAndUnload_Test extends TestCase {

  
  // opens a new form in a new window and closes and unloads the current 
  private final class DispatchHandler implements PhaseListener {
    
    private static final long serialVersionUID = 1L;

    WebForm newForm;
    
    public void afterPhase( final PhaseEvent event ) {
    }
    
    public void beforePhase( final PhaseEvent event ) {
      WebForm oldActive = FormManager.getActive();
      IWindow window = W4TContext.getWindowManager().findWindow( oldActive );
      if( window != null ) {
        window.close();
      }
      oldActive.unload();
      newForm = Fixture.loadStartupForm();
      W4TContext.showInNewWindow( newForm );
    }
    
    public PhaseId getPhaseId() {
      return PhaseId.PROCESS_ACTION;
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
  
  public void testSimpleClose_Script() throws Exception {
    // prepare 'fake' environment
    Fixture.fakeResponseWriter();
    Fixture.fakeBrowser( new Default( true, false ) );
    W4TModelUtil.initModel();
    final WebForm form = prepareFormAndRequestParms();
    //
    // run request -> closes the window of the current form
    LifeCycle lifeCycle = ( LifeCycle )W4TContext.getLifeCycle();
    lifeCycle.addPhaseListener( new PhaseListener() {
      private static final long serialVersionUID = 1L;
      public void afterPhase( final PhaseEvent event ) {
      }
      public void beforePhase( final PhaseEvent event ) {
        IWindow window = W4TContext.getWindowManager().findWindow( form );
        if( window != null ) {
          window.close();
        }
      }
      public PhaseId getPhaseId() {
        return PhaseId.PROCESS_ACTION;
      }
    } );
    lifeCycle.execute();
    // obtain generated markup
    IServiceStateInfo stateInfo = ContextProvider.getStateInfo();
    String allMarkup = Fixture.getAllMarkup( stateInfo.getResponseWriter() );
    assertTrue( allMarkup.indexOf( "windowManager.closeWindow()" ) != -1 );
  }
  
  public void testCloseTwoWindows_Script() throws Exception {
    // prepare 'fake' environment
    Fixture.fakeResponseWriter();
    Fixture.fakeBrowser( new Default( true, false ) );
    W4TModelUtil.initModel();
    final WebForm form1 = prepareFormAndRequestParms();
    final WebForm form2 = Fixture.getEmptyWebFormInstance();
    FormManager.add( form2 );
    IWindow window1 = WindowManager.getInstance().findWindow( form1 );
    IWindow window2 = WindowManager.getInstance().create( form2 );
    //
    // run request -> closes the window of the current form
    LifeCycle lifeCycle = ( LifeCycle )W4TContext.getLifeCycle();
    PhaseListener phaseListener = new PhaseListener() {
      private static final long serialVersionUID = 1L;
      public void afterPhase( final PhaseEvent event ) {
      }
      public void beforePhase( final PhaseEvent event ) {
        form1.closeWindow();
        form2.closeWindow();
      }
      public PhaseId getPhaseId() {
        return PhaseId.PROCESS_ACTION;
      }
    };
    lifeCycle.addPhaseListener( phaseListener );
    lifeCycle.execute();
    IServiceStateInfo stateInfo = ContextProvider.getStateInfo();
    String allMarkup = Fixture.getAllMarkup( stateInfo.getResponseWriter() );
    assertTrue( allMarkup.indexOf( "windowManager.closeWindow()" ) != -1 );
    String expected 
      = "refreshWindow( 'http://fooserver:8080/fooapp/W4TDelegate?"
      + "uiRoot=" 
      + window2.getId()
      + ";" 
      + form2.getUniqueID();
    assertTrue( allMarkup.indexOf( expected ) != -1 );
    assertEquals( true, WindowManager.isClosing( window1 ) );
    assertEquals( true, WindowManager.isClosed( window1 ) );
    assertEquals( true, WindowManager.isClosing( window2 ) );
    assertEquals( false, WindowManager.isClosed( window2 ) );
    // simulate 'refresh'-request of form2
    IFormAdapter adapter = Fixture.getFormAdapter( form2 );
    String requestCounter = String.valueOf( adapter.getRequestCounter() - 1 );
    Fixture.fakeFormRequestParams( requestCounter, 
                                   window2.getId(), 
                                   form2.getUniqueID() );
    FormManager.setActive( null );
    WindowManager.setActive( null );
    Fixture.fakeResponseWriter();
    lifeCycle.removePhaseListener( phaseListener );
    lifeCycle.execute();
    allMarkup = Fixture.getAllMarkup( stateInfo.getResponseWriter() );
    assertTrue( allMarkup.indexOf( "windowManager.closeWindow()" ) != -1 );
    assertTrue( allMarkup.indexOf( "refreshWindow" ) == -1 );
  }
  
  public void testSimpleClose_Ajax() throws Exception {
    // prepare 'fake' environment
    Fixture.fakeResponseWriter();
    Fixture.fakeBrowser( new Ie6( true, true ) );
    W4TModelUtil.initModel();
    final WebForm form = prepareFormAndRequestParms();
    AjaxStatusUtil.preRender( form );
    AjaxStatusUtil.postRender( form );
    Fixture.fakeRequestParam( RequestParams.IS_AJAX_REQUEST, "true" );
    //
    // run request -> closes the window of the current form
    LifeCycle lifeCycle = ( LifeCycle )W4TContext.getLifeCycle();
    lifeCycle.addPhaseListener( new PhaseListener() {
      private static final long serialVersionUID = 1L;
      public void afterPhase( final PhaseEvent event ) {
      }
      public void beforePhase( final PhaseEvent event ) {
        form.closeWindow();
      }
      public PhaseId getPhaseId() {
        return PhaseId.PROCESS_ACTION;
      }
    } );
    lifeCycle.execute();
    // obtain generated markup
    IServiceStateInfo stateInfo = ContextProvider.getStateInfo();
    String allMarkup = Fixture.getAllMarkup( stateInfo.getResponseWriter() );
    assertTrue( allMarkup.indexOf( "ajax-response" ) != -1 );
    String expected 
      = "<script type=\"text/javascript\">" 
      + "windowManager.closeWindow();" 
      + "</script>";
    assertTrue( allMarkup.indexOf( expected ) != -1 );
  }
  
  public void testCloseTwoWindows_Ajax() throws Exception {
    // prepare 'fake' environment
    Fixture.fakeResponseWriter();
    Fixture.fakeBrowser( new Ie6( true, true ) );
    W4TModelUtil.initModel();
    final WebForm form1 = prepareFormAndRequestParms();
    final WebForm form2 = Fixture.getEmptyWebFormInstance();
    FormManager.add( form2 );
    IWindow window2 = WindowManager.getInstance().create( form2 );
    AjaxStatusUtil.preRender( form1 );
    AjaxStatusUtil.postRender( form1 );
    Fixture.fakeRequestParam( RequestParams.IS_AJAX_REQUEST, "true" );
    //
    // run request -> closes the window of the current form
    LifeCycle lifeCycle = ( LifeCycle )W4TContext.getLifeCycle();
    lifeCycle.addPhaseListener( new PhaseListener() {
      private static final long serialVersionUID = 1L;
      public void afterPhase( final PhaseEvent event ) {
      }
      public void beforePhase( final PhaseEvent event ) {
        form1.closeWindow();
        form2.closeWindow();
      }
      public PhaseId getPhaseId() {
        return PhaseId.PROCESS_ACTION;
      }
    } );
    lifeCycle.execute();
    // obtain generated markup
    IServiceStateInfo stateInfo = ContextProvider.getStateInfo();
    String allMarkup = Fixture.getAllMarkup( stateInfo.getResponseWriter() );
    assertTrue( allMarkup.indexOf( "ajax-response" ) != -1 );
    String expected = "windowManager.closeWindow();"; 
    assertTrue( allMarkup.indexOf( expected ) != -1 );
    expected 
      = "refreshWindow( 'http://fooserver:8080/fooapp/W4TDelegate?"
      + "uiRoot=" 
      + window2.getId()
      + ";" 
      + form2.getUniqueID();
    assertTrue( allMarkup.indexOf( expected ) != -1 );
  }
  
  public void testShowInNewWindowAndUnload_Noscript() throws Exception {
    // prepare 'fake' environment
    Fixture.fakeResponseWriter();
    Fixture.fakeBrowser( new Default( false, false ) );
    W4TModelUtil.initModel();
    WebForm originatingForm = prepareFormAndRequestParms();
    IWindow originatingWindow 
      = WindowManager.getInstance().findWindow( originatingForm );
    // run request -> switches form with closing and unloading old one
    LifeCycle lifeCycle = ( LifeCycle )W4TContext.getLifeCycle();
    DispatchHandler dispatchHandler = new DispatchHandler();
    lifeCycle.addPhaseListener( dispatchHandler );
    lifeCycle.execute();
    // obtain generated markup
    IServiceStateInfo stateInfo = ContextProvider.getStateInfo();
    String allMarkup = Fixture.getAllMarkup( stateInfo.getResponseWriter() );

     // new form must be marked as active
    assertEquals( FormManager.getActive(), originatingForm );
    // old form must be marked as closing
    assertEquals( true, WindowManager.isClosing( originatingWindow ) );
    // old form must not be found in FormManager
    assertNull( FormManager.findById( originatingForm.getUniqueID() ) );
    assertEquals( 1, FormManager.getAll().length );
    // window of old form must not be found in WindowManager
    IWindow window 
      = WindowManager.getInstance().findById( originatingWindow.getId() );
    assertNull( window );
    // render output must contain markup for old form 
    String expected = "<form id=\"" + originatingForm.getUniqueID();
    assertTrue( allMarkup.indexOf( expected ) != -1 );
    // ... and include code to close open new form (in its new window)
    //     must look like openNewWindow( ... 'newWindowId'
    expected 
      = "<meta http-equiv=\"refresh\" content=\"0; "
      + "url=http://fooserver:8080/fooapp/W4TDelegate?uiRoot=w2;"
      + dispatchHandler.newForm.getUniqueID();
    assertTrue( allMarkup.indexOf( expected ) != -1 );
    
    // simulate 'refresh'-request of newForm
    IFormAdapter adapter = Fixture.getFormAdapter( dispatchHandler.newForm );
    String requestCounter = String.valueOf( adapter.getRequestCounter() - 1 );
    Fixture.fakeFormRequestParams( requestCounter, 
                                   "w2", 
                                   dispatchHandler.newForm.getUniqueID() );
    FormManager.setActive( null );
    WindowManager.setActive( null );
    Fixture.fakeResponseWriter();
    lifeCycle.removePhaseListener( dispatchHandler );
    lifeCycle.execute();
    allMarkup = Fixture.getAllMarkup( stateInfo.getResponseWriter() );
    assertTrue( allMarkup.indexOf( "http-equiv=\"refresh\"" ) == -1 );
  }
  
  public void testShowInNewWindowAndUnload_Script() throws Exception {
    // prepare 'fake' environment
    Fixture.fakeResponseWriter();
    Fixture.fakeBrowser( new Default( true, false ) );
    W4TModelUtil.initModel();
    WebForm originatingForm = prepareFormAndRequestParms();
    IWindow originatingWindow 
      = WindowManager.getInstance().findWindow( originatingForm );
    // run request -> switches form with closing and unloading old one
    LifeCycle lifeCycle = ( LifeCycle )W4TContext.getLifeCycle();
    DispatchHandler dispatchHandler = new DispatchHandler();
    lifeCycle.addPhaseListener( dispatchHandler );
    lifeCycle.execute();
    // obtain generated markup
    IServiceStateInfo stateInfo = ContextProvider.getStateInfo();
    String allMarkup = Fixture.getAllMarkup( stateInfo.getResponseWriter() );

     // new form must be marked as active
    assertEquals( FormManager.getActive(), originatingForm );
    // old form must be marked as closing
    assertEquals( true, WindowManager.isClosing( originatingWindow ) );
    // old form must not be found in FormManager
    assertNull( FormManager.findById( originatingForm.getUniqueID() ) );
    assertEquals( 1, FormManager.getAll().length );
    // window of old form must not be found in WindowManager
    IWindow window 
      = WindowManager.getInstance().findById( originatingWindow.getId() );
    assertNull( window );
    // render output must contain markup for old form 
    String expected = "<form id=\"" + originatingForm.getUniqueID();
    assertTrue( allMarkup.indexOf( expected ) != -1 );
    // ... and include code to close open new form (in its new window)
    //     must look like openNewWindow( ... 'newWindowId'
    int indexOfOpenNewWindow = allMarkup.indexOf( "openNewWindow(" );
    assertTrue( indexOfOpenNewWindow != -1 );
    IWindow newWindow 
       = WindowManager.getInstance().findWindow( dispatchHandler.newForm );
    assertTrue( allMarkup.indexOf( newWindow.getId() ) > indexOfOpenNewWindow );
    // ... and include code to close window of old form
    assertTrue( allMarkup.indexOf( "closeWindow()" ) != -1 );
  }
  
  public void testShowInNewWindowAndUnload_Ajax() throws Exception {
    // prepare 'fake' environment
    Fixture.fakeResponseWriter();
    Fixture.fakeBrowser( new Ie6( true, true ) );
    W4TModelUtil.initModel();
    WebForm originatingForm = prepareFormAndRequestParms();
    AjaxStatusUtil.preRender( originatingForm );
    AjaxStatusUtil.postRender( originatingForm );
    Fixture.fakeRequestParam( RequestParams.IS_AJAX_REQUEST, "true" );
    IWindow originatingWindow 
      = WindowManager.getInstance().findWindow( originatingForm );
    // run request -> switches form with closing and unloading old one
    LifeCycle lifeCycle = ( LifeCycle )W4TContext.getLifeCycle();
    DispatchHandler dispatchHandler = new DispatchHandler();
    lifeCycle.addPhaseListener( dispatchHandler );
    lifeCycle.execute();
    // obtain generated markup
    IServiceStateInfo stateInfo = ContextProvider.getStateInfo();
    String allMarkup = Fixture.getAllMarkup( stateInfo.getResponseWriter() );

     // new form must be marked as active
    assertEquals( FormManager.getActive(), originatingForm );
    // old window must be marked as closing
    assertEquals( true, WindowManager.isClosing( originatingWindow ) );
    // old form must not be found in FormManager nay more
    assertNull( FormManager.findById( originatingForm.getUniqueID() ) );
    assertEquals( 1, FormManager.getAll().length );
    // window of old form must not be found in WindowManager
    IWindow window 
      = WindowManager.getInstance().findById( originatingWindow.getId() );
    assertNull( window );
    // render output must be an ajax-response containing the javaScript to
    // close old form and open new form in new window
    assertTrue( allMarkup.indexOf( "<ajax-response>" ) != -1 );
    // ... and include code to close open new form (in its new window)
    //     must look like openNewWindow( ... 'newWindowId'
    int indexOfOpenNewWindow = allMarkup.indexOf( "openNewWindow(" );
    assertTrue( indexOfOpenNewWindow != -1 );
    IWindow newWindow 
       = WindowManager.getInstance().findWindow( dispatchHandler.newForm );
    assertTrue( allMarkup.indexOf( newWindow.getId() ) > indexOfOpenNewWindow );
    // ... and include code to close window of old form
    assertTrue( allMarkup.indexOf( "closeWindow()" ) != -1 );
  }

// TODO [rh] [WFT-29] Find a decent way to handle requests to closed windows
//      
//  public void testRequestUnloadedForm_Script() throws Exception {
//    // 1. prepare 'fake' environment
//    Fixture.fakeResponseWriter();
//    Fixture.fakeBrowser( new Default( false, true ) );
//    W4TModelUtil.getW4TModel();
//    WebForm originatingForm = prepareFormAndRequestParms();
//    // 2. run request -> switches form with closing and unloading old one
//    LifeCycle lifeCycle = ( LifeCycle )W4TContext.getLifeCycle();
//    DispatchHandler dispatchHandler = new DispatchHandler();
//    lifeCycle.addPhaseListener( dispatchHandler );
//    lifeCycle.execute();
//    // 3. Fake request that asks for the just unloaded form
//    FormManager.setActive( null );
//    WindowManager.setActive( null );
//    Fixture.fakeResponseWriter();
//    lifeCycle.removePhaseListener( dispatchHandler );
//    lifeCycle.execute();
//    IServiceStateInfo stateInfo = ContextProvider.getStateInfo();
//    String allMarkup = Fixture.getAllMarkup( stateInfo.getResponseWriter() );
//    String notExpected = "<form id=\"p5\"";
//    // assertTrue( allMarkup.indexOf( notExpected ) == -1 );
//    System.out.println( allMarkup );
//  }
//
//  public void testRequestUnloadedForm_Ajax() throws Exception {
//    // 1. prepare 'fake' environment
//    Fixture.fakeResponseWriter();
//    Fixture.fakeBrowser( new Default( true, true ) );
//    W4TModelUtil.getW4TModel();
//    WebForm originatingForm = prepareFormAndRequestParms();
//    AjaxStatusUtil.update( originatingForm );
//    AjaxStatusUtil.reset( originatingForm );
//    Fixture.fakeRequestParam( RequestParams.AJAX_REQUEST, "true" );
//    // 2. run request -> switches form with closing and unloading old one
//    LifeCycle lifeCycle = ( LifeCycle )W4TContext.getLifeCycle();
//    DispatchHandler dispatchHandler = new DispatchHandler();
//    lifeCycle.addPhaseListener( dispatchHandler );
//    lifeCycle.execute();
//    
//    // 3. Fake request that asks for the just unloaded form
//    FormManager.setActive( null );
//    WindowManager.setActive( null );
//    Fixture.fakeResponseWriter();
//    lifeCycle.removePhaseListener( dispatchHandler );
//    lifeCycle.execute();
//    
//    IServiceStateInfo stateInfo = ContextProvider.getStateInfo();
//    String allMarkup = Fixture.getAllMarkup( stateInfo.getResponseWriter() );
//    
//    String notExpected = "<form id=\"p5\"";
////    assertTrue( allMarkup.indexOf( notExpected ) == -1 );
//System.out.println( allMarkup );
//  }
  
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
  
}
