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
import com.w4t.WebComponent;
import com.w4t.ajax.AjaxStatusUtil;


/** <p>The default AJAx-enabled renderer for org.eclipse.rap.WebButton.</p>
  */
public class WebButtonRenderer_Default_Ajax 
  extends WebButtonRenderer_Default_Script 
{

  public void render( final WebComponent component ) throws IOException {
    if( AjaxStatusUtil.mustRender( component ) ) {
      super.render( component );
    }
  }
}