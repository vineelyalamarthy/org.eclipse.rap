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
import java.text.MessageFormat;

import org.eclipse.rwt.internal.browser.Browser;
import org.eclipse.rwt.internal.lifecycle.HtmlResponseWriter;
import org.eclipse.rwt.internal.util.HTML;

import com.w4t.*;
import com.w4t.dhtml.*;

/** <p>The renderer for org.eclipse.rap.dhtml.Menu on Mozilla 1.6 and later
  * without JavaScript support.</p>
  */
public class MenuRenderer_Mozilla1_6up_Noscript extends MenuRenderer_DOM {

  public void processAction( final WebComponent component ) {
    ProcessActionUtil.processActionPerformedNoScript( component );
  }

  public void render( final WebComponent component ) throws IOException {
    Menu menu = ( Menu )component;
    HtmlResponseWriter out = getResponseWriter();
    if( menu.isExpanded() ) {
      out.startElement( HTML.DIV, null );
      out.writeAttribute( HTML.ID, menu.getUniqueID(), null );
      out.writeAttribute( HTML.CLASS, "menu", null );
      Point point = getAbsolutePosition( menu );
      if( point != null ) {        
        String text = "top:{0,number,integer};left{1,number,integer};";
        Object[] args = new Object[] { 
          new Integer( point.getY() ), 
          new Integer( point.getX() ) };
        String style = MessageFormat.format( text, args );
        out.writeAttribute( HTML.STYLE, style, null );
      }
      out.closeElementIfStarted();
      renderItems( menu );
      out.endElement( HTML.DIV );
    }
  }
   
  void renderItems( final Menu menu ) throws IOException {
    if( getAbsolutePosition( menu ) == null ) {
      renderItemHorizontal( menu );
    } else {
      renderItemVertical( menu );
    }
  }  
  
  private void renderItemHorizontal( final Menu menu ) throws IOException {
    Item[] items = menu.getLeaves();
    HtmlResponseWriter out = getResponseWriter();
    out.startElement( HTML.TABLE, null );
    out.writeAttribute( HTML.CELLSPACING, "0",  null );
    out.writeAttribute( HTML.CELLPADDING, "0",  null );
    out.writeAttribute( HTML.BORDER, "0",  null );
    out.startElement( HTML.TR, null );
    for( int i = 0; i < items.length; i++ ) {
      out.startElement( HTML.TD, null );
      LifeCycleHelper.render( items[ i ] );
      out.endElement( HTML.TD );
    }
    out.endElement( HTML.TR );
    out.endElement( HTML.TABLE );
  }
  
  private void renderItemVertical( final Menu menu ) throws IOException {
    Item[] items = menu.getLeaves();
    for( int i = 0; i < items.length; i++ ) {
      LifeCycleHelper.render( items[ i ] );
    }
  }
  
  Point getAbsolutePosition( final Menu menu ) {
    return menu.getAbsolutePosition( Browser.NAVIGATOR_6_UP );    
  }
}