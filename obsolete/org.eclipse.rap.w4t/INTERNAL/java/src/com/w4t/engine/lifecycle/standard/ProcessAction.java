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

import javax.servlet.ServletException;

import org.eclipse.rwt.internal.service.ContextProvider;
import org.eclipse.rwt.lifecycle.PhaseId;

import com.w4t.WebForm;
import com.w4t.engine.util.FormManager;


/** <p>The implementation of the 'Process Events' phase 
  * in the standard w4t request lifecycle.</p>
  *
  * <p>In the 'Process Event' phase all information about server-side events
  * is read out the request and executed (event handlers on the corresponding 
  * components are called).</p>
  */
final class ProcessAction extends Phase {
 
  public PhaseId execute() throws ServletException {
    PhaseId result = null;
    try {
      processEvent();
      result = PhaseId.RENDER;
    } catch( final Exception e ) {
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
    return PhaseId.PROCESS_ACTION;
  }
  
  private void processEvent() {
    WebForm form = FormManager.getActive();
    Phase.getLifeCycleAdapter( form ).processAction();
  }
}