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
import org.eclipse.rwt.internal.util.HTML;

import com.w4t.*;


final class RenderUtilPrintScript {
  
  private RenderUtilPrintScript() {
  }

  static void render( final WebButton wbt ) throws IOException {
    HtmlResponseWriter out = ContextProvider.getStateInfo().getResponseWriter();
    out.startElement( HTML.INPUT, null );
    out.writeAttribute( HTML.ID, wbt.getUniqueID(), null );
    out.writeAttribute( HTML.TYPE, HTML.BUTTON, null );
    out.writeAttribute( HTML.NAME, wbt.getUniqueID(), null );
    out.writeAttribute( HTML.VALUE, WebButtonRenderer.getLabel( wbt ), null );
    RenderUtil.writeDisabled( wbt );
    RenderUtil.writeUniversalAttributes( wbt );
    out.writeAttribute( HTML.ON_CLICK, JavaScriptUtil.JS_PRINT_PAGE, null );
    out.writeAttribute( HTML.ON_MOUSE_DOWN, 
                        JavaScriptUtil.JS_SUSPEND_SUBMIT, 
                        null );
    out.endElement( HTML.INPUT );
  }
}
