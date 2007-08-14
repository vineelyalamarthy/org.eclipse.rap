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
package com.w4t.dhtml.menukit;

import com.w4t.Renderer;
import com.w4t.WebComponent;
import com.w4t.dhtml.Menu;


/** <p>the superclass of all Renderers that render 
  * {@link org.eclipse.rwt.dhtml.Menu org.eclipse.rap.dhtml.Menu}.</p>
  */
public abstract class MenuRenderer extends Renderer {

  public void scheduleRendering( final WebComponent component ) {
    Menu menu = ( Menu )component;
    for( int i = 0; i < menu.getItemCount(); i++ ) {
      if( menu.getItem(i).isVisible() ) {
        getRenderingSchedule().schedule( menu.getItem( i ) );
      }
    }
  }
}