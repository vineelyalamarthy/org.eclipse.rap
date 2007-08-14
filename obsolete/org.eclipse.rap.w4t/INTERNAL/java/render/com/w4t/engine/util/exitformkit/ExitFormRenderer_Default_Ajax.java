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
package com.w4t.engine.util.exitformkit;

import java.io.IOException;
import java.text.MessageFormat;

import org.eclipse.rwt.internal.lifecycle.HtmlResponseWriter;
import org.eclipse.rwt.internal.service.ContextProvider;
import org.eclipse.rwt.internal.util.HTML;

import com.w4t.*;
import com.w4t.engine.lifecycle.standard.IFormRenderer;
import com.w4t.engine.util.WindowManager;


public class ExitFormRenderer_Default_Ajax 
  extends Renderer
  implements IFormRenderer
{
  
  private static final String WINDOW_OPEN 
    = "window.open( ''{0}'', ''{1}'', '''' );";

  public boolean fireRenderEvents() {
    return true;
  }

  public void prepare() {
    // do nothing
  }
  
  public void render( final WebComponent component ) throws IOException {
    ExitFormUtil.registerInternalFile();
    HtmlResponseWriter writer = getResponseWriter();
    writer.append( RenderUtil.createXmlProcessingInstruction() );
    writer.startElement( HTML.AJAX_RESPONSE, null );
    writer.closeElementIfStarted();
    writer.append( createWindowOpenScript() );
    writer.endElement( HTML.AJAX_RESPONSE );
    ContextProvider.getResponse().setContentType( HTML.CONTENT_TEXT_XML );
  }

  private static String createWindowOpenScript() {
    String url;
    if( ExitFormUtil.externalFileExists() ) {
      url = ExitFormUtil.EXTERNAL_FILE;
    } else {
      url = ExitFormUtil.INTERNAL_FILE;
    }
    Object[] args = new Object[] { url, WindowManager.getActive().getId() };
    String scriptCode = MessageFormat.format( WINDOW_OPEN, args );
    return RenderUtil.createJavaScriptInline( scriptCode );
  }
}
