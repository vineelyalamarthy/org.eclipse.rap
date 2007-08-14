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
package com.w4t.webanchorkit;

import java.io.IOException;

import com.w4t.*;
import com.w4t.ajax.AjaxStatusUtil;

/** 
 * <p>The default renderer for <code>org.eclipse.rap.WebAnchor</code> on AJaX-enabled
 * browsers.</p>
 */
public class WebAnchorRenderer_Default_Ajax extends WebAnchorRenderer {
  
  public void render( final WebComponent component ) throws IOException {
    if( AjaxStatusUtil.mustRender( component ) ) {
      super.render( component );
    } else {
      Decorator decorator = ( Decorator )component;
      LifeCycleHelper.render( decorator.getContent() );
    }
  }
}
