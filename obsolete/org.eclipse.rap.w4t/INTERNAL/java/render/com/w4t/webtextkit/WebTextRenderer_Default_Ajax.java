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
import com.w4t.ajax.AjaxStatusUtil;


/** <p>Renders <code>org.eclipse.rap.WebText</code> when used in AJAX-enabled browsers
 * </p>
  */
public class WebTextRenderer_Default_Ajax extends Renderer {
  
  public void readData( final WebComponent component ) {
    ReadDataUtil.applyValue( component );
  }
  
  public void processAction( final WebComponent component ) {
    ProcessActionUtil.processFocusGained( component );
    ProcessActionUtil.processItemStateChangedScript( component );
  }
  
  public void render( final WebComponent component ) throws IOException {
    if( AjaxStatusUtil.mustRender( component ) ) {
      WebText text = ( WebText )component;
      WebTextUtil.renderStart( text );
      EventUtil.createItemAndFocusHandler( text );
      WebTextUtil.renderEnd();
    }
  }
}