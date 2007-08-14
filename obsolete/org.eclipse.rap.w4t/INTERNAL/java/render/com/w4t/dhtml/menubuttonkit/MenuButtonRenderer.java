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
package com.w4t.dhtml.menubuttonkit;

import java.io.IOException;

import com.w4t.*;
import com.w4t.dhtml.*;
import com.w4t.dhtml.renderinfo.MenuButtonInfo;
import com.w4t.internal.adaptable.IRenderInfoAdapter;


/** <p>the superclass of all Renderers that render 
  * {@link org.eclipse.rwt.dhtml.MenuButton org.eclipse.rap.dhtml.MenuButton}.</p>
  */
public abstract class MenuButtonRenderer extends Renderer {

  public void render( final WebComponent component ) throws IOException {
    MenuButton menuButton = ( MenuButton )component;
    if( getMenu( menuButton ).isEnabled() && !isMenuEmpty( menuButton ) ) {
      createActiveButton( menuButton );
    } else {
      createInactiveButton( menuButton );
    }
  }

  static MenuButtonInfo getInfo( final MenuButton menuButton ) {
    IRenderInfoAdapter adapter 
      = ( IRenderInfoAdapter )menuButton.getAdapter( IRenderInfoAdapter.class );
    return ( MenuButtonInfo )adapter.getInfo();
  }
  
  static Menu getMenu( final MenuButton menuButton ) {
    return getInfo( menuButton ).getMenu();
  }
  
  static MenuBar getMenuBar( final MenuButton menuButton ) {
    return ( MenuBar )getInfo( menuButton ).getMenu().getParentNode();
  }
  
  boolean isMenuEmpty( final MenuButton menuButton ) {
    return getMenu( menuButton ).getLeaves().length == 0;
  }

  String getLabel( final MenuButton menuButton ) {
    return RenderUtil.resolve( getMenu( menuButton ).getLabel() );
  }
    
  abstract void createActiveButton( final MenuButton menuButton ) 
    throws IOException;
  
  abstract void createInactiveButton( final MenuButton menuButton ) 
    throws IOException;
}
  

