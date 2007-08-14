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
package com.w4t.dhtml.treeleafkit;

import java.io.IOException;
import com.w4t.WebComponent;
import com.w4t.ajax.AjaxStatusUtil;
import com.w4t.dhtml.Item;
import com.w4t.dhtml.ItemUtil;


/**
 * <p>The renderer for <code>org.eclipse.rap.dhtml.TreeLeaf</code> on Mozilla 1.6 and 
 * later with AJaX support enabled</p>
 */
//TODO [rh] unite! copied from corresponding Ie5 renderer
public class TreeLeafRenderer_Mozilla1_6up_Ajax
  extends TreeLeafRenderer_Mozilla1_6up_Script
{
  
  public void render( final WebComponent component ) throws IOException {
    ItemUtil.checkMarkState( ( Item )component );
    if( AjaxStatusUtil.mustRender( component ) ) {
      super.render( component );
    }
    ItemUtil.bufferMarkedState( ( Item )component );
  }
}
