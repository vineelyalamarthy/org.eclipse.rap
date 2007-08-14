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
package com.w4t.webbuttonkit;

import java.io.IOException;

import com.w4t.*;


/** <p>The default renderer for org.eclipse.rap.WebButton.</p>
  *
  * <p>The default renderer is non-browser-specific and implements 
  * functionality in a way that runs on the most commonly used browsers.</p>
  */
public class WebButtonRenderer_Default_Script extends WebButtonRenderer {
  
  public void processAction( final WebComponent component ) {
    ProcessActionUtil.processFocusGained( component );
    ProcessActionUtil.processActionPerformedScript( component );
  }

  public void render( final WebComponent component ) throws IOException {
    WebButton wbt = ( WebButton )component;
    try {
      bufferFontColor( wbt );
      doRender( wbt );
      restoreFontColor( wbt );
    } finally {
      getRenderInfoAdapter( wbt ).clearRenderState();
    }
  }

  private void doRender( final WebButton wbt ) throws IOException {
    if( wbt.isPrint() ) {
      RenderUtilPrintScript.render( wbt );
    } else if( wbt.getImage() != null && !"".equals( wbt.getImage() ) ) {
      RenderUtilImageScript.render( wbt );
    } else if( wbt.isLink() ) {
      RenderUtilLinkScript.render( wbt );
    } else {
      RenderUtilButtonScript.render( wbt );
    }
  }
}