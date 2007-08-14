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
package com.w4t.dhtml.menubarkit;

import java.io.IOException;

import org.eclipse.rwt.internal.lifecycle.HtmlResponseWriter;
import org.eclipse.rwt.internal.util.HTML;

import com.w4t.*;
import com.w4t.dhtml.*;


/** <p>The renderer for org.eclipse.rap.dhtml.MenuBar on browsers without
  * javascript support.</p>
  */
public class MenuBarRenderer_Default_Noscript extends MenuBarRenderer {
  
  public void processAction( final WebComponent component ) {
    ProcessActionUtil.processActionPerformedNoScript( component );
  }

  public void render( final WebComponent component ) throws IOException {
    MenuBar menuBar = ( MenuBar )component;
    createBorderOpener( menuBar );
    createContent( menuBar );
    createBorderCloser( menuBar );
  }

  private void createBorderOpener( final MenuBar menuBar ) throws IOException {
    HtmlResponseWriter out = getResponseWriter();
    out.startElement( HTML.TABLE, null );
    out.writeAttribute( HTML.CELLSPACING, "0", null );
    out.writeAttribute( HTML.CELLPADDING, "0", null );
    out.writeAttribute( HTML.BORDER, "0", null );
    out.writeAttribute( HTML.WIDTH, getStyle( menuBar ).getWidth(), null );
    if( hasBorder( menuBar ) ) {
      out.startElement( HTML.TR, null );
      out.startElement( HTML.TD, null );
      out.writeAttribute( HTML.BGCOLOR, 
                          getStyle( menuBar ).getBorderTopColor().toString(), 
                          null );
      out.startElement( HTML.IMG, null );
      out.writeAttribute( HTML.SRC, ItemUtil.IMG_TRANSPARENT, null );
      out.writeAttribute( HTML.BORDER, "0", null );
      out.writeAttribute( HTML.WIDTH, "1", null );
      out.writeAttribute( HTML.HEIGHT, 
                          String.valueOf( getStyle( menuBar ).getBorder() ), 
                          null );
      out.writeAttribute( HTML.ALIGN, HTML.TOP, null );
      out.endElement( HTML.TD );
      out.endElement( HTML.TR );
    } 
    out.startElement( HTML.TR, null );
    out.startElement( HTML.TD, null );
    out.writeAttribute( HTML.NOWRAP, null, null );
    out.writeAttribute( HTML.BGCOLOR, 
                        getStyle( menuBar ).getBgColor().toString(), 
                        null );
    
  }
  
  private void createBorderCloser( final MenuBar menuBar ) throws IOException {
    HtmlResponseWriter out = getResponseWriter();
    out.endElement( HTML.TD );
    out.endElement( HTML.TR );
    if( hasBorder( menuBar ) ) {
      out.startElement( HTML.TR, null );
      out.startElement( HTML.TD, null );
      out.writeAttribute( HTML.BGCOLOR, 
                          getStyle( menuBar ).getBorderBottomColor().toString(), 
                          null );
      out.startElement( HTML.IMG, null );
      out.writeAttribute( HTML.SRC, ItemUtil.IMG_TRANSPARENT, null );
      out.writeAttribute( HTML.BORDER, "0", null );
      out.writeAttribute( HTML.WIDTH, "1", null );
      out.writeAttribute( HTML.HEIGHT, 
                          String.valueOf( getStyle( menuBar ).getBorder() ), 
                          null );
      out.writeAttribute( HTML.ALIGN, HTML.TOP, null );
      out.endElement( HTML.TD );
      out.endElement( HTML.TR );
    }
    out.endElement( HTML.TABLE );
  }

  private void createContent( final MenuBar menuBar ) throws IOException {
    HtmlResponseWriter out = getResponseWriter();
    out.startElement( HTML.TABLE, null );
    out.writeAttribute( HTML.BORDER, "0", null );
    out.writeAttribute( HTML.CELLSPACING, "0", null );
    out.writeAttribute( HTML.CELLPADDING, 
                        String.valueOf( getStyle( menuBar ).getPadding() ), 
                        null );
    out.startElement( HTML.TR, null );
    MenuButton[] menuButtons = MenuBarUtil.getInfo( menuBar ).getMenuButtons();
    for( int i = 0; i < menuButtons.length; i++ ) {
      LifeCycleHelper.render( menuButtons[ i ] );
    }
    out.endElement( HTML.TR );
    out.endElement( HTML.TABLE );

    Node[] nodes = menuBar.getNodes();
    for( int i = 0; i < nodes.length; i++ ) {
      LifeCycleHelper.render( nodes[ i ] );
    }    
  }
  
  // queries
  //////////
  
  private static boolean hasBorder( final MenuBar menuBar ) {
    return getStyle( menuBar ).getBorder() > 0;
  }

  protected void useJSLibrary() throws IOException {
    // do nothing
  }
}