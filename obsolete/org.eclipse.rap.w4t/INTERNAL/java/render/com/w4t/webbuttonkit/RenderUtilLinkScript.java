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

import org.eclipse.rwt.internal.lifecycle.HtmlResponseWriter;
import org.eclipse.rwt.internal.service.ContextProvider;

import com.w4t.WebButton;
import com.w4t.event.WebActionEvent;


final class RenderUtilLinkScript {
  
  private RenderUtilLinkScript() {
  }
  
  static HtmlResponseWriter getResponseWriter() {
    return ContextProvider.getStateInfo().getResponseWriter();
  }

  static void render( final WebButton wbt ) throws IOException {
    if( wbt.isEnabled() && WebActionEvent.hasListener( wbt ) ) {
      WebButtonRendererUtil.createAnchor( wbt );
    } else {
      WebButtonRendererUtil.createDisabledLink( wbt );
    }
  }
}
