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

import org.eclipse.rwt.internal.ConfigurationReader;
import org.eclipse.rwt.internal.IInitialization;
import org.eclipse.rwt.internal.service.*;
import org.eclipse.rwt.lifecycle.PhaseId;

import com.w4t.LifeCycleHelper;
import com.w4t.WebForm;
import com.w4t.IWindowManager.IWindow;
import com.w4t.engine.util.FormManager;
import com.w4t.engine.util.WindowManager;
import com.w4t.internal.adaptable.IFormAdapter;


/** <p>the implementation of the 'AccessForm' phase 
  * in the standard w4t request lifecycle.</p>
  *
  * <p>In the 'AccessForm' phase preparations in the model are done and
  * the WebForm to process is retreived. Checks for expiration etc. are 
  * done.</p>
  */
final class AccessForm extends Phase {
  
  private final static String DEFAULT_ADMIN_FORM
    = "org.eclipse.rap.administration.Startup";

  /** 
   * <p>takes the necessary preparations for the processing of the 
   * active WebForm.</p>
   */
  public PhaseId execute() throws ServletException {
    PhaseId result = null;
    try {
      if( isStartupRequest() ) {
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
    return PhaseId.PREPARE_UI_ROOT;
  }
  
  
  ///////////////////////////////////////////
  // helper methods to detect type of request 
  
  private static boolean isStartupRequest() {
    IServiceStateInfo stateInfo = ContextProvider.getStateInfo();
    return    !stateInfo.isIgnoreStartup()
           && parameterExists( RequestParams.STARTUP );
  }

  private static boolean isAdminRequest() {
    IServiceStateInfo stateInfo = ContextProvider.getStateInfo();
    return    !stateInfo.isIgnoreStartup()
           && parameterExists( RequestParams.ADMIN );
  }
  
  //////////////////
  // helping methods
  
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

  private static PhaseId executeStandard() {
    PhaseId result = PhaseId.RENDER;
    String windowId = LifeCycleHelper.getRequestWindowId(); 
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
    // check the @see WebForm#requestCounter
    if( isExpired( form ) ) {
      ContextProvider.getStateInfo().setExpired( true );
    } else {
      result = PhaseId.READ_DATA;
    }
    return result;
  }
  
  private static boolean isExpired( final WebForm form ) {
    return    getExpectedRequestCounter() != getFormRequestCounter( form ) 
           || !form.getUniqueID().equals( LifeCycleHelper.getRequestFormId() );
  }
  
  private static int getFormRequestCounter( final WebForm form ) {
    IFormAdapter adapt = ( IFormAdapter )form.getAdapter( IFormAdapter.class );
    return adapt.getRequestCounter();
  }
  
  private static int getExpectedRequestCounter() {
    int result = -1;
    HttpServletRequest request = ContextProvider.getRequest();
    String value = request.getParameter( RequestParams.REQUEST_COUNTER );
    if( value != null ) {
      try {
        result = Integer.parseInt( value ) + 1;
      } catch( NumberFormatException e ) {
        // when param is not a valid number, -1 will remain the return value
        // which marks the form as expired
      }      
    }
    return result;
  }
  
  private static IInitialization getInitProps() {
    return ConfigurationReader.getConfiguration().getInitialization();
  }

  private static boolean parameterExists( final String name ) {
    return    ContextProvider.getRequest().getParameter( name ) != null
           && !"".equals( ContextProvider.getRequest().getParameter( name ) );
  }
}