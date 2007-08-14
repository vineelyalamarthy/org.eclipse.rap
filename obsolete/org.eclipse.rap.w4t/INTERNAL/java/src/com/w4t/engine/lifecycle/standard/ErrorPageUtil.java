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

import org.eclipse.rwt.internal.*;
import org.eclipse.rwt.internal.lifecycle.LifeCycle;
import org.eclipse.rwt.internal.service.ContextProvider;
import org.eclipse.rwt.internal.service.IServiceStateInfo;

import com.w4t.*;
import com.w4t.IWindowManager.IWindow;
import com.w4t.engine.util.FormManager;
import com.w4t.engine.util.WindowManager;
import com.w4t.internal.adaptable.IFormAdapter;


/** <p>A utility class that creates error pages for specific requests.</p>
  */
class ErrorPageUtil {
    
  private static final String STANDARD_FORM 
    = "com.w4t.administration.DefaultErrorForm";
  
  
  /** creates a new instance of the errorpage specified in the
    * configuration file W4T.xml and supplies it with the passed Exception.
    * The error page is set to the current request and sent out with it. */
  static void create( final Exception ex ) {    
    IServiceStateInfo stateInfo = ContextProvider.getStateInfo();
    WebForm errorForm = loadErrorForm( ex );

    WebForm activeForm = FormManager.getActive();
    int requestCounter = 0;
    if( activeForm != null ) {
      IFormAdapter adapter
        = ( IFormAdapter )activeForm.getAdapter( IFormAdapter.class );
      requestCounter = adapter.getRequestCounter();
    }
    if( requestCounter == 0 || !LifeCycle.showExceptionInPopUp ) {
      IWindow window = WindowManager.getActive();
      if( window == null ) {
        window = WindowManager.getInstance().create( errorForm );
        WindowManager.setActive( window );
      } else {
        window.setFormToDispatch( errorForm );
      }
      if( activeForm == null ) {
        FormManager.setActive( errorForm );
      }
      stateInfo.setIgnoreStartup( true );
      stateInfo.setExceptionOccured( false );
      stateInfo.setInvalidated( false );
    } else {
      W4TContext.showInNewWindow( errorForm );
    }
  }

  private static WebForm loadErrorForm( final Exception ex ) {
    WebForm result = null;
    try {
      IConfiguration configuration = ConfigurationReader.getConfiguration();
      IInitialization initialization = configuration.getInitialization();
      String errorFormName = initialization.getErrorPage();
      result = FormManager.load( errorFormName );  
      ( ( WebErrorForm )result ).setException( ex );
    } catch ( final Exception noValidUserDefinedErrorForm ) {
      // this means that the specified error form was not found or it
      // could not be casted in WebErrorForm, so we proceed to plan B
      ex.printStackTrace();
      result = loadStandardErrorForm( ex );
    }
    return result;
  }  

  private static WebForm loadStandardErrorForm( final Exception ex ) {
    WebForm result = FormManager.load( STANDARD_FORM );
    ( ( WebErrorForm )result ).setException( ex );
    return result;
  }
}