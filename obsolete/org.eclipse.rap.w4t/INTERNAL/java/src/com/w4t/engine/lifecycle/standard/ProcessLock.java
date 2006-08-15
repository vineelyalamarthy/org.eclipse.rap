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

import java.text.MessageFormat;
import com.w4t.WebComponentControl;
import com.w4t.WebForm;
import com.w4t.engine.service.ContextProvider;

/**
 * <p>
 * 'Static' class o obtain and release locks in order to prevent forms from
 * being rendered twice upon requests received while rendering.
 * </p>
 */
final class ProcessLock {
  
  static void markAsInProcess( final WebForm form ) {
    synchronized( form ) {
      if( !isAlreadyInProcess( form ) ) {
        WebComponentControl.setAlreadyInProcess( form, true );
      } else {
        ContextProvider.getStateInfo().setAlreadyInProcess( true );
      }
    }
  }

  static void obtain( final WebForm form ) {
    /*
     * if the current form is already in process, wait till the thread
     * processing the form has finished ...
     */
    if( ContextProvider.getStateInfo().isAlreadyInProcess() ) {
      while( isAlreadyInProcess( form ) ) {
        try {
          Thread.sleep( 100 );
        } catch( InterruptedException e ) {
          String text = "Failed to obtain lock for WebForm with id ''{0}''.";
          String msg 
            = MessageFormat.format( text, new Object[] { form.getUniqueID() } );
          throw new IllegalStateException( msg ) ;
        }
      }
      /*
       * ... and then take its output to avoid losing the tcp endpoint, because
       * the browser is only waiting for the response of the last request.
       * Therefore set the expired mark.
       */
      ContextProvider.getStateInfo().setExpired( true );
    }
  }

  private static boolean isAlreadyInProcess( final WebForm form ) {
    synchronized( form ) {
      return WebComponentControl.isAlreadyInProcess( form );
    }
  }

  static void release( final WebForm form, final WebForm substitute ) {
    synchronized( form ) {
      WebComponentControl.setAlreadyInProcess( substitute, false );
      WebComponentControl.setAlreadyInProcess( form, false );
    }
  }

  private ProcessLock() {
    // prevent instantiation
  }
}
