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

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.eclipse.rwt.internal.service.*;
import org.eclipse.rwt.lifecycle.PhaseId;

import com.w4t.WebForm;
import com.w4t.engine.util.FormManager;


/** <p>The implementation of the 'Read Data' phase 
  * in the standard w4t request lifecycle.</p>
  *
  * <p>In the 'Read Data' phase the values of html form elements are
  * read out of the request and applied to the corresponding components.</p>
  */
final class ReadData extends Phase {
  
  public PhaseId execute() throws ServletException {
    PhaseId result = null;
    try {
      readData();
      fireDataEvents();
      result = PhaseId.PROCESS_ACTION;
    } catch( Exception e ) {
      ContextProvider.getStateInfo().setExceptionOccured( true );      
      try {
        ErrorPageUtil.create( e );
        result = PhaseId.RENDER;
      } catch( final Exception ex ) {
        ex.printStackTrace();
        throw new PhaseException( ex );
      }
    }
    return result;
  }
  
  PhaseId getPhaseID() {
    return PhaseId.READ_DATA;
  }

  private void readData() throws IOException {
    HttpServletRequest request = ContextProvider.getRequest();
    if( request.getParameter( RequestParams.PARAMLESS_GET ) == null ) {
      WebForm form = FormManager.getActive();
      Phase.getLifeCycleAdapter( form ).readData();
    }
  }

  private void fireDataEvents() {
    IServiceStateInfo stateInfo = ContextProvider.getStateInfo();
    String key = EventQueueFilter.ATTRIBUTE_KEY;
    EventQueueFilter eqv = ( EventQueueFilter )stateInfo.getAttribute( key );
    if( eqv != null ) {
      eqv.filter();
      EventQueue.getEventQueue().fireEvents();
    }
  }
}