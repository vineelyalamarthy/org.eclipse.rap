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
import com.w4t.event.WebItemEvent;


/** <p>The default noscript renderer for org.eclipse.rap.WebText.</p>
  *
  * <p>The default noscript renderer is non-browser-specific and implements 
  * functionality in a way that runs on browsers that do not implement or 
  * permit javascript execution.</p>
  */
public class WebTextRenderer_Default_Noscript extends Renderer {
  
  public void readData( final WebComponent component ) {
    ReadDataUtil.applyValue( component );
  }
  
  public void processAction( final WebComponent component ) {
    ProcessActionUtil.processItemStateChangedNoScript( component );
  }
  
  public void render( final WebComponent component ) throws IOException {
    WebText wtx = ( WebText )component;
    WebTextUtil.renderStart( wtx );
    WebTextUtil.renderEnd();
    renderSubmitter( wtx );
  }


  // helping methods
  //////////////////
  
  private static String renderSubmitter( final WebText wtx ) throws IOException 
  {
    String result = "";
    if( isActive( wtx ) ) {
      RenderUtil.writeItemSubmitter( wtx.getUniqueID() );
    }
    return result;
  }
  
  private static boolean isActive( final WebText wtx ) {
    return  WebItemEvent.hasListener( wtx ) && wtx.isEnabled();
  }
}