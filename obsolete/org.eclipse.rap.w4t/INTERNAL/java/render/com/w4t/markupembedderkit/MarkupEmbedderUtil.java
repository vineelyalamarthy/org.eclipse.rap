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
package com.w4t.markupembedderkit;

import java.io.IOException;

import org.eclipse.rwt.internal.lifecycle.HtmlResponseWriter;
import org.eclipse.rwt.internal.service.ContextProvider;
import org.eclipse.rwt.internal.util.HTML;

import com.w4t.MarkupEmbedder;
import com.w4t.RenderUtil;

/**
 * <p>Utility class to assist the <code>MarkupEmbedderRenderer_XXX</code>
 * classes.</p>
 */
final class MarkupEmbedderUtil {
  
  private MarkupEmbedderUtil() {
    // prevent instantiation
  }
  
  static void renderWithEmbracingDiv( final MarkupEmbedder markupEmbedder ) 
    throws IOException 
  {
    HtmlResponseWriter out = ContextProvider.getStateInfo().getResponseWriter();
    out.startElement( HTML.DIV, null );
    out.writeAttribute( HTML.ID, markupEmbedder.getUniqueID(), null );
    RenderUtil.writeUniversalAttributes( markupEmbedder );
    out.closeElementIfStarted();
    out.append( RenderUtil.resolve( markupEmbedder.getContent() ) );
    out.endElement( HTML.DIV );
  }
  
}
