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
import com.w4t.ajax.AjaxStatusUtil;

public class WebFlowLayoutRenderer_Default_Ajax
  extends WebFlowLayoutRenderer_Default_Script
{

  public void render( final WebComponent component ) throws IOException {
    WebContainer parent = ( WebContainer )component;
    // needs to be rendered?
    if( AjaxStatusUtil.mustRender( parent ) ) {
      super.render( parent );
    } else {
      // give the child components a chance to render themselves
      WebComponent child;
      for( int i = 0; i < parent.getWebComponentCount(); i++ ) {
        child = parent.get( i );
        LifeCycleHelper.render( child, child );
      }
    }
  }
}
