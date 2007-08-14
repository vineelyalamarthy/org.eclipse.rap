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

import java.io.IOException;

import org.eclipse.rwt.internal.lifecycle.HtmlResponseWriter;
import org.eclipse.rwt.internal.util.HTML;

import com.w4t.LifeCycleHelper;
import com.w4t.WebComponent;
import com.w4t.dhtml.Item;
import com.w4t.dhtml.Menu;

/** <p>The renderer for org.eclipse.rap.dhtml.CMenu on browsers with
  * DOM support.</p>
  */
public abstract class MenuRenderer_DOM extends MenuRenderer {

  
  public void render( final WebComponent component ) throws IOException {
    Menu menu = ( Menu )component;
    HtmlResponseWriter out = getResponseWriter();
    out.startElement( HTML.DIV, null );
    out.writeAttribute( HTML.ID, menu.getUniqueID(), null );
    out.writeAttribute( HTML.CLASS, "menu", null );
    renderItems( menu );
    out.endElement( HTML.DIV );
  }
  
  void renderItems( final Menu menu ) throws IOException {
    Item[] items = menu.getLeaves();
    for( int i = 0; i < items.length; i++ ) {
      LifeCycleHelper.render( items[ i ] );
    }
  }
}
  

