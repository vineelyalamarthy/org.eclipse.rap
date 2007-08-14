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

import org.eclipse.rwt.internal.lifecycle.HtmlResponseWriter;
import org.eclipse.rwt.internal.service.ContextProvider;
import org.eclipse.rwt.internal.service.IServiceStateInfo;
import org.eclipse.rwt.lifecycle.PhaseId;

import com.w4t.WebForm;
import com.w4t.engine.util.FormManager;


/** <p>The implementation of the 'Render' phase 
  * in the standard w4t request lifecycle.</p>
  *
  * <p>In the 'Render' phase the components of the component tree are
  * recursively asked to render their state onto a html page which is 
  * then sent to the browser. .</p>
  */
final class Render extends Phase {
  
  public PhaseId execute() throws ServletException {
    try {
      render();
    } catch( final Exception e ) {
      try {
        ErrorPageUtil.create( e );
        clearResponseWriter();
        render();
      } catch( final Exception ex ) {
        ex.printStackTrace();
        throw new PhaseException( ex );
      }
    }
    return null;
  }
  
  PhaseId getPhaseID() {
    return PhaseId.RENDER;
  }
  
  
  //////////////////
  // helping methods
  
  private static void clearResponseWriter() {
    IServiceStateInfo stateInfo = ContextProvider.getStateInfo();
    HtmlResponseWriter writer = stateInfo.getResponseWriter();
    writer.clearBody();
    writer.clearHead();
    writer.clearFoot();
  }
  
  private static void render() throws IOException {
    if( FormManager.getActive() != null ) {
      WebForm form = FormManager.getActive();
      Phase.getLifeCycleAdapter( form ).render();
    }
  }
}