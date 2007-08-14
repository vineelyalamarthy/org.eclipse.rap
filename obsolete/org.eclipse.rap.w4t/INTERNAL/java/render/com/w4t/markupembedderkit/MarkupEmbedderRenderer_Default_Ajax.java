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

import com.w4t.*;
import com.w4t.ajax.AjaxStatusUtil;


/**
 * <p>The default renderer for <code>org.eclipse.rap.MarkupEmbedder</code> in AJaX
 * mode.</p> 
 */
public class MarkupEmbedderRenderer_Default_Ajax extends Renderer {
  
  public void render( final WebComponent component ) throws IOException {
    if( AjaxStatusUtil.mustRender( component ) ) {
      MarkupEmbedder markupEmbedder = ( MarkupEmbedder )component;
      MarkupEmbedderUtil.renderWithEmbracingDiv( markupEmbedder );
    }
  }

}