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
package com.w4t.webtextkit;

import java.io.IOException;

import com.w4t.*;


/** <p>The default renderer for org.eclipse.rap.WebText.</p>
  *
  * <p>The default renderer is non-browser-specific and implements 
  * functionality in a way that runs on the most commonly used browsers.</p>
  */
public class WebTextRenderer_Default_Script extends Renderer {
  
  public void readData( final WebComponent component ) {
    ReadDataUtil.applyValue( component );
  }
  
  public void processAction( final WebComponent component ) {
    ProcessActionUtil.processFocusGained( component );
    ProcessActionUtil.processItemStateChangedScript( component );
  }
  
  public void render( final WebComponent component ) throws IOException {
    WebText wtx = ( WebText )component;
    WebTextUtil.renderStart( wtx );
    EventUtil.createItemAndFocusHandler( wtx );
    WebTextUtil.renderEnd();
  }
}