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
package com.w4t.engine.service;

import java.io.IOException;
import java.io.OutputStream;
import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;

import org.eclipse.rwt.internal.service.AbstractServiceHandler;
import org.eclipse.rwt.internal.service.ContextProvider;
import org.eclipse.rwt.internal.util.HTML;

import com.w4t.*;
import com.w4t.IWindowManager.IWindow;
import com.w4t.internal.adaptable.IFormAdapter;

// TODO [rh] needs testing: made this class a real IServiceHandler
public final class TimestampRequestServiceHandler extends AbstractServiceHandler {

  // TODO [rh] Should we have a separate class that contains all logger names?
  //      e.g. org.eclipse.rap.util.LogerNames?
  public static final String LOG 
    = TimestampRequestServiceHandler.class.getName();
  
  private static final Logger logger = Logger.getLogger( LOG ); 
  
  static final int[] PLACE_HOLDER = new int[] {
    71, 73, 70, 56, 57, 97, 1, 0, 1, 0, 128, 0, 0,
    0, 0, 0, 255, 255, 255, 33, 249, 4 ,9, 0, 0, 1, 0,
    44, 0, 0, 0, 0, 1, 0, 1, 0, 0, 2, 2, 76, 1, 0, 59
  };

  public void service() throws IOException, ServletException {
    synchronized( ContextProvider.getSession() ) {
      log();
      WebForm form = getForm();
      if( form != null ) {
        refreshTimestamp( form );
        writeResponse();
      }
    }
  }

  //////////////////
  // Helping methods
  
  private static void writeResponse() throws IOException {
    ContextProvider.getResponse().setContentType( HTML.CONTENT_IMAGE_GIF );
    OutputStream out = ContextProvider.getResponse().getOutputStream();
    try {
      for( int i = 0; i < PLACE_HOLDER.length; i++ ) {
        out.write( PLACE_HOLDER[ i ] );
      }
      out.flush();
    } finally {
      out.close();
    }
  }

  private static WebForm getForm() {
    WebForm result = null;
    String windowId = LifeCycleHelper.getRequestWindowId();
    if( windowId != null ) {
      IWindow window = W4TContext.getWindowManager().findById( windowId );
      if( window != null ) {
        result = W4TContext.getWindowManager().findForm( window );
      }
    }
    return result;
  }

  private static void refreshTimestamp( final WebForm form ) {
    IFormAdapter adapter 
      = ( IFormAdapter )form.getAdapter( IFormAdapter.class );
    adapter.refreshTimeStamp();
  }

  //////////////////
  // Logging methods
  
  private static void log() {
    if( logger.isLoggable( Level.FINE ) ) {
      WebForm form = getForm();
      String outcome;
      if( form == null ) {
        outcome = "form not found";
      } else {
        outcome = MessageFormat.format( "form ''{0}'' updated", 
                                        new Object[] { form.getUniqueID() } );
      }
      String text = "Timestamp request for window-id ''{0}'': {1}";
      Object[] args = new Object[] { 
        LifeCycleHelper.getRequestWindowId(), 
        outcome 
      };
      logger.log( Level.FINE, MessageFormat.format( text, args ) );
    }
  }
}
