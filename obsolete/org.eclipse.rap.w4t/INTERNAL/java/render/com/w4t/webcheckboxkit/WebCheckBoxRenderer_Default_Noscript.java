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
package com.w4t.webcheckboxkit;

import java.io.IOException;

import com.w4t.*;
import com.w4t.event.WebItemEvent;


/** <p>The default noscript renderer for org.eclipse.rap.WebCheckBox.</p>
  *
  * <p>The default noscript renderer is non-browser-specific and implements 
  * functionality in a way that runs on browsers that do not implement or 
  * permit javascript execution.</p>
  */
public class WebCheckBoxRenderer_Default_Noscript extends WebCheckBoxRenderer {
  
 public void readData( final WebComponent component ) {
    WebCheckBoxReadDataUtil.applyValue( ( WebCheckBox )component );
  }
  
  public void processAction( final WebComponent component ) {
    ProcessActionUtil.processItemStateChangedNoScript( component );
  }

  void createEventHandler( final WebCheckBox wcb ) {
    // no eventhandler in noscript mode
  }


  protected void createSubmitter( final WebCheckBox wcb ) throws IOException {
    if( WebItemEvent.hasListener( wcb ) && wcb.isEnabled() ) {
      RenderUtil.writeItemSubmitter( wcb.getUniqueID() );
    }
  }
}