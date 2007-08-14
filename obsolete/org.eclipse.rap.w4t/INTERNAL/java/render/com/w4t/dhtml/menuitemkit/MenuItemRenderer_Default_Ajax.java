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
package com.w4t.dhtml.menuitemkit;

import java.io.IOException;
import com.w4t.ProcessActionUtil;
import com.w4t.WebComponent;
import com.w4t.ajax.AjaxStatusUtil;
import com.w4t.dhtml.MenuItem;


/** 
 * <p>The renderer for <code>org.eclipse.rap.dhtml.MenuItem</code> on browsers with 
 * ajax support.</p>
 */
public class MenuItemRenderer_Default_Ajax
  extends MenuItemRenderer
{
  
  public void processAction( final WebComponent component ) {
    ProcessActionUtil.processActionPerformedScript( component );
  }
  
  public void render( final WebComponent component ) throws IOException {
    if( AjaxStatusUtil.mustRender( component ) ) {
      super.render( component );
    }
  }
  
  void renderActiveItem( final MenuItem menuItem ) throws IOException {
    MenuItemUtil.renderActiveItem( getResponseWriter(), menuItem );
  }
  
  void renderInactiveItem( final MenuItem menuItem ) throws IOException {
    MenuItemUtil.renderInactiveItem( getResponseWriter(), menuItem );
  }
}
