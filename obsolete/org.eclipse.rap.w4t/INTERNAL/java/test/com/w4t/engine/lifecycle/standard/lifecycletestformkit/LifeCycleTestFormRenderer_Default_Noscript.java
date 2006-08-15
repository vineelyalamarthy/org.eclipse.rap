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
package com.w4t.engine.lifecycle.standard.lifecycletestformkit;

import java.io.IOException;
import com.w4t.WebComponent;
import com.w4t.engine.lifecycle.standard.ILog;
import com.w4t.engine.lifecycle.standard.LifeCycleTestForm;


public class LifeCycleTestFormRenderer_Default_Noscript
  extends LifeCycleTestFormRenderer
{
  
  public void readData( final WebComponent component ) {
    ( ( LifeCycleTestForm )component ).getLog().log( READ_DATA_NOSCRIPT );
  }
  
  public void processAction( final WebComponent component ) {
    ILog log = ( ( LifeCycleTestForm )component ).getLog();
    log.log( PROCESS_EVENT_NOSCRIPT );
  }
  
  public void render( final WebComponent component ) throws IOException {
    ( ( LifeCycleTestForm )component ).getLog().log( RENDER_NOSCRIPT );
  }

}
