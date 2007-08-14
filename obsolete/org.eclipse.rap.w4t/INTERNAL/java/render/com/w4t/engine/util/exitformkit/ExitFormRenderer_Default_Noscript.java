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

import javax.servlet.http.HttpServletResponse;

import org.eclipse.rwt.internal.service.ContextProvider;
import org.eclipse.rwt.internal.util.HTML;

import com.w4t.Renderer;
import com.w4t.WebComponent;
import com.w4t.engine.lifecycle.standard.IFormRenderer;


public class ExitFormRenderer_Default_Noscript 
  extends Renderer 
  implements IFormRenderer
{
  
  public boolean fireRenderEvents() {
    return true;
  }

  public void prepare() {
    // do nothing
  }
  
  public void render( final WebComponent component ) throws IOException {
    String content = ExitFormUtil.load();
    getResponseWriter().append( content );
    HttpServletResponse response = ContextProvider.getResponse();
    response.setContentType( HTML.CONTENT_TEXT_HTML_UTF_8 );
  }

}
