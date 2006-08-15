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
package com.w4t.webfileuploadkit;

import java.io.IOException;
import com.w4t.WebComponent;
import com.w4t.ajax.AjaxStatusUtil;

/**
 * <p>Renders the <code>WebFileUpload</code> component for Ie 5 and higher
 * in AJaX mode.</p> 
 */
public class WebFileUploadRenderer_Ie5up_Ajax
  extends WebFileUploadRenderer_Default_Script
{
  public void render( final WebComponent component ) throws IOException {
    if( AjaxStatusUtil.mustRender( component ) ) {
      super.render( component );
    }
  }
}
