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
package com.w4t.engine.lifecycle.standard;

import java.security.AccessControlException;
import java.text.MessageFormat;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import com.w4t.WebComponentControl;
import com.w4t.WebForm;
import com.w4t.IWindowManager.IWindow;
import com.w4t.engine.lifecycle.PhaseId;
import com.w4t.engine.requests.RequestCancelledException;
import com.w4t.engine.requests.RequestParams;
import com.w4t.engine.service.ContextProvider;
import com.w4t.engine.service.IServiceStateInfo;
import com.w4t.engine.util.FormManager;
import com.w4t.engine.util.WindowManager;
import com.w4t.internal.adaptable.IFormAdapter;
import com.w4t.util.ConfigurationReader;
import com.w4t.util.IInitialization;


/** <p>the implementation of the 'AccessForm' phase 
  * in the standard w4t request lifecycle.</p>
  *
  * <p>In the 'AccessForm' phase preparations in the model are done and
  * the WebForm to process is retreived. Checks for expiration etc. are 
  * done.</p>
  */
final class AccessForm extends Phase {
  
  
  private final static String DEFAULT_ADMIN_FORM
    = "com.w4t.administration.Startup";

  /** 
   * <p>takes the necessary preparations for the processing of the 
   * active WebForm.</p>
   */
  public PhaseId execute() throws ServletException, RequestCancelledException {
    PhaseId result = null;
    try {
      if( isTriggerTimeStampRequest() ) {
        result = triggerTimeStamp();
      } else if( isStartupRequest() ) {
        result = executeStartup();
      } else if( isAdminRequest() ) {
        result = executeAdmin();
      } else {
        result = executeStandard();
      }
    } catch( Exception e ) {
      try {
        ErrorPageUtil.create( e );
        result = PhaseId.RENDER;
      } catch( final Exception ex ) {
        throw new PhaseException( ex );
      }
    }
    return result;
  }

  PhaseId getPhaseID() {
    return PhaseId.ACCESS_FORM;
  }
  
  
  ///////////////////////////////////////////
  // helper methods to detect type of request 
  
  private static boolean isStartupRequest() {
    IServiceStateInfo stateInfo = ContextProvider.getStateInfo();
    return    !stateInfo.isIgnoreStartup()
           && AccessForm.parameterExists( RequestParams.STARTUP );
  }

  private static boolean isAdminRequest() {
    IServiceStateInfo stateInfo = ContextProvider.getStateInfo();
    return    !stateInfo.isIgnoreStartup()
           && AccessForm.parameterExists( RequestParams.ADMIN );
  }

  private boolean isTriggerTimeStampRequest() {
    String param = RequestParams.REQUEST_TIMESTAMP_NAME;
    return ContextProvider.getRequest().getParameter( param ) != null;
  }
  
  //////////////////
  // helping methods
  
  private PhaseId triggerTimeStamp() throws PhaseException {
    try {
      WebForm wf = retrieveFormToProcess();
      WebComponentControl.isAlreadyInProcess( wf );
    } catch( Exception e ) {
      try {
        ErrorPageUtil.create( e );
      } catch( final Exception ex ) {
        throw new PhaseException( ex );
      }
    }
    return null;
  }
  
  private static PhaseId executeAdmin() {
    IInitialization initialization = AccessForm.getInitProps();
    boolean access = initialization.isDirectMonitoringAccess();
    if( access ) {
      String adminFormName = initialization.getAdministrationStartupForm();
      WebForm wfAdmin;
      try {
        wfAdmin = FormManager.load( adminFormName );
      } catch( Exception e ) {
        adminFormName = AccessForm.DEFAULT_ADMIN_FORM;
        wfAdmin = FormManager.load( adminFormName );
      }
      IWindow window = WindowManager.getInstance().create( wfAdmin );
      WindowManager.setActive( window );
      FormManager.setActive( wfAdmin );
    } else {
      FormManager.setActive( null );
      throw new AccessControlException( "Monitoring access denied." );
    }
    return PhaseId.RENDER;
  }

  private static PhaseId executeStartup() {
    IInitialization initialization = AccessForm.getInitProps();
    String startFormName = initialization.getStartUpForm();
    WebForm result = null;
    if( FormManager.isPreloaded() ) {
      WebForm[] allForms = FormManager.getAll();
      for( int i = 0; i < allForms.length; i++ ) {
        // there can be only one instance in the FormList if we 
        // are at startup - the preloaded one
        if( allForms[ i ].getClass().getName().equals( startFormName ) ) {
          result = allForms[ i ];
          FormManager.setPreloaded( false );
        }
      }
    } else {
      FormManager.clear();
      result = FormManager.load( startFormName );        
    }
    IWindow window = WindowManager.getInstance().create( result );
    WindowManager.setActive( window );
    FormManager.setActive( result );
    return PhaseId.RENDER;
  }

  private PhaseId executeStandard() {
    PhaseId result = PhaseId.RENDER;
    HttpServletRequest request = ContextProvider.getRequest();
    String windowId = request.getParameter( RequestParams.ACTIVE_WINDOW );
    // FIXME [rh] windowId is null on first request with Firefox - there
    //       is coming in a second request with no parameters at all.
    IWindow window = WindowManager.getInstance().findById( windowId );
    if( window == null ) {
      String text = "Could not find window for id ''{0}''.";
      String msg = MessageFormat.format( text, new Object[] { windowId } );
      throw new IllegalStateException( msg );
    }
    WindowManager.setActive( window );
    WebForm form = WindowManager.getLastForm( window );
    FormManager.setActive( form );
    ProcessLock.markAsInProcess( form );
    if( !ContextProvider.getStateInfo().isAlreadyInProcess() ) {
      // check the @see WebForm#requestCounter
      if( isExpired( form ) ) {
        ContextProvider.getStateInfo().setExpired( true );
      } else {
        result = PhaseId.READ_DATA;
      }
    } 
    return result;
  }
  
  private boolean isExpired( final WebForm form ) {
    return    getExpectedRequestCounter() != getFormRequestCounter( form ) 
           || !form.getUniqueID().equals( getExpectedFormId() );
  }
  
  private String getExpectedFormId() {
    HttpServletRequest request = ContextProvider.getRequest();
    return request.getParameter( RequestParams.ACTIVE_FORM_ID );
  }
  
  private int getFormRequestCounter( final WebForm form ) {
    IFormAdapter adapt = ( IFormAdapter )form.getAdapter( IFormAdapter.class );
    return adapt.getRequestCounter();
  }
  
  private int getExpectedRequestCounter() {
    HttpServletRequest request = ContextProvider.getRequest();
    String rcParam = request.getParameter( RequestParams.REQUEST_COUNTER );
    return Integer.parseInt( rcParam ) + 1;
  }
  
  private static WebForm retrieveFormToProcess() {
    HttpServletRequest request = ContextProvider.getRequest();
    String windowId = request.getParameter( RequestParams.ACTIVE_WINDOW );
    IWindow window = WindowManager.getInstance().findById( windowId );
    return WindowManager.getLastForm( window );
  }

  private static IInitialization getInitProps() {
    return ConfigurationReader.getConfiguration().getInitialization();
  }

  private static boolean parameterExists( final String name ) {
    return    ContextProvider.getRequest().getParameter( name ) != null
           && !"".equals( ContextProvider.getRequest().getParameter( name ) );
  }
}