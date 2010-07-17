/*******************************************************************************
 * Copyright (c) 2002, 2009 Innoopract Informationssysteme GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Innoopract Informationssysteme GmbH - initial API and implementation
 *     EclipseSource - ongoing development
 ******************************************************************************/
package org.eclipse.rwt.internal.lifecycle;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.rwt.internal.protocol.ChunkDistributor;
import org.eclipse.rwt.internal.protocol.Processor;
import org.eclipse.rwt.internal.service.ContextProvider;
import org.eclipse.rwt.lifecycle.PhaseId;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;


final class ReadData implements IPhase {

  public PhaseId getPhaseID() {
    return PhaseId.READ_DATA;
  }

  public PhaseId execute() {
    Display display = RWTLifeCycle.getSessionDisplay();
    DisplayUtil.getLCA( display ).readData( display );
    // RAP [hs] patched for protocol
    HttpServletRequest request = ContextProvider.getRequest();
    String jsonMessage = request.getParameter( "JSON" );
    if( jsonMessage != null ) {
      System.out.println( jsonMessage );
      Processor processor = new Processor( jsonMessage );
      Shell[] shells = display.getShells();
      for( int i = 0; i < shells.length; i++ ) {
        processor.addStreamListener( new ChunkDistributor( shells[ i ] ) );
      }
      processor.parse();        
    }      
    return PhaseId.PROCESS_ACTION;
  }
}
