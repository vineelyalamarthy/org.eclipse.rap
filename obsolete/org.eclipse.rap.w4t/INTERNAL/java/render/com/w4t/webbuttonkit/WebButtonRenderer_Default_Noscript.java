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

import org.eclipse.rwt.internal.lifecycle.HtmlResponseWriter;
import org.eclipse.rwt.internal.util.HTML;

import com.w4t.*;
import com.w4t.event.WebActionEvent;
import com.w4t.util.image.*;


/** <p>The default noscript renderer for org.eclipse.rap.WebButton.</p>
  *
  * <p>The default noscript renderer is non-browser-specific and implements 
  * functionality in a way that runs on browsers that do not implement or 
  * permit javascript execution.</p>
  */
public class WebButtonRenderer_Default_Noscript extends WebButtonRenderer {
  
  public void processAction( final WebComponent component ) {
    ProcessActionUtil.processActionPerformedNoScript( component );
  }
  
  public void render( final WebComponent component ) throws IOException {
    WebButton wbt = ( WebButton )component;
    bufferFontColor( wbt );
    doRender( wbt );
    restoreFontColor( wbt );
  }

  private void doRender( final WebButton wbt ) throws IOException {
    if( wbt.isLink() ) {
      renderAsLink( wbt );
    } else {
      renderAsButton( wbt );
    }
  }

  private void renderAsLink( final WebButton wbt ) throws IOException {
    if( isActive( wbt ) ) {
      renderActiveLink( wbt );
    } else {
      createDisabledLink( wbt );
    }
  }

  private void renderActiveLink( final WebButton wbt ) throws IOException {
    String imageName = "";
    if( !isEmptyImage( wbt ) ) {
      RenderUtil.writeActionSubmitter( wbt.getImage(), 
                                       wbt.getUniqueID(), 
                                       "", 
                                       "" );
    } else {
      try {
        ImageDescriptor imgDesc 
          = ImageDescriptorFactory.create( getLabel( wbt ), 
                                           wbt.getStyle().getColor(),
                                           wbt.getStyle().getBgColor(),
                                           wbt.getStyle().getFontFamily(),
                                           wbt.getStyle().getFontSize(),
                                           wbt.getStyle().getFontWeight(),
                                           wbt.getStyle().getFontStyle() );
        ImageCache cache = ImageCache.getInstance();
        imageName = cache.getImageName( imgDesc );
        if( cache.isStandardSubmitterImage( imageName ) ) {
          createDisabledLink( wbt );
          RenderUtil.writeActionSubmitter( wbt.getUniqueID() );    
        } else {
          RenderUtil.writeActionSubmitter( imageName, 
                                           wbt.getUniqueID(), 
                                           "", 
                                           "" );
        }
      } catch( Exception e ) {
        System.out.println( "\nException creating submitter image:\n" + e );
        e.printStackTrace();
        createDisabledLink( wbt );
        RenderUtil.writeActionSubmitter( wbt.getUniqueID() );
      }
    }
  }

  private void renderAsButton( final WebButton wbt ) throws IOException {
    createButton( wbt );
  }
  
  
  // helping methods
  //////////////////

  private void createImageOrLabelSpan( final WebButton wbt ) 
    throws IOException 
  {
    HtmlResponseWriter out = getResponseWriter();
    out.startElement( HTML.SPAN, null );
    RenderUtil.writeUniversalAttributes( wbt );
    createImageOrLabel( wbt );
    out.endElement( HTML.SPAN );
  }

  private void createDisabledLink( final WebButton wbt ) throws IOException {
    createNonBreakingSpace( wbt );
    createImageOrLabelSpan( wbt );
    createNonBreakingSpace( wbt );
  }

  private void createButton( final WebButton wbt ) throws IOException {
    HtmlResponseWriter out = getResponseWriter();
    out.startElement( HTML.INPUT, null );
    out.writeAttribute( HTML.TYPE, HTML.SUBMIT, null );
    String name_value = appendPrefix( wbt, wbt.getUniqueID() );
    out.writeAttribute( HTML.NAME, name_value, null );
    out.writeAttribute( HTML.VALUE, wbt.getLabel(), null );
    RenderUtil.writeDisabled( wbt );
    RenderUtil.writeUniversalAttributes( wbt );
    out.endElement( HTML.INPUT );
  }
  
  private String appendPrefix(  final WebButton wbt, final String id ) {
    String result = id;
    if( isActive( wbt ) ) {
      StringBuffer buffer = new StringBuffer( WebActionEvent.PREFIX );
      result = buffer.append( id ).toString();
    }
    return result;
  }
}