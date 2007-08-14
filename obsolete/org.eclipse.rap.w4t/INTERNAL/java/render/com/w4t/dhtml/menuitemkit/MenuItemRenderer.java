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
import com.w4t.Renderer;
import com.w4t.WebComponent;
import com.w4t.dhtml.MenuItem;



/** <p>The renderer for org.eclipse.rap.dhtml.MenuItem on browsers with
  * DOM support.</p>
  */
public abstract class MenuItemRenderer extends Renderer {
  
  public void render( final WebComponent component ) throws IOException {
    MenuItem menuItem = ( MenuItem )component;
    if( MenuItemUtil.isActive( menuItem ) ) {
      renderActiveItem( menuItem );
    } else {
      renderInactiveItem( menuItem );
    }
  }
  
  abstract void renderActiveItem( final MenuItem menuItem ) throws IOException;
  
  abstract void renderInactiveItem( final MenuItem menuItem ) throws IOException;
}