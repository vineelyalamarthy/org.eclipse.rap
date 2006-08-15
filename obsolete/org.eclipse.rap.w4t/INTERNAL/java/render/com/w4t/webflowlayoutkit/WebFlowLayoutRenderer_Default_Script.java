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
package com.w4t.webflowlayoutkit;

import java.io.IOException;
import com.w4t.*;
import com.w4t.engine.service.ContextProvider;
import com.w4t.engine.service.IServiceStateInfo;


/** <p>The default renderer for com.w4t.WebFlowLayout.</p>
  *
  * <p>The default renderer is non-browser-specific and implements 
  * functionality in a way that runs on the most commonly used browsers.</p>
  */
public class WebFlowLayoutRenderer_Default_Script 
  extends WebFlowLayoutRenderer
{
  
  void renderWithoutTable( final WebContainer parent ) throws IOException {
    HtmlResponseWriter out = getResponseWriter();
    IServiceStateInfo stateInfo = ContextProvider.getStateInfo();
    boolean ajaxEnabled = stateInfo.getDetectedBrowser().isAjaxEnabled();
    if( ajaxEnabled ) {
      out.startElement( HTML.DIV, null );
      out.writeAttribute( HTML.ID, parent.getUniqueID(), null );
      out.closeElementIfStarted();
    }
    super.renderWithoutTable( parent );
    if( ajaxEnabled ) {
      out.endElement( HTML.DIV );
    }
  }
}