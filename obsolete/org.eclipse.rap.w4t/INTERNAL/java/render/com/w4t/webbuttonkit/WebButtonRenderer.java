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
import org.eclipse.rwt.internal.service.ContextProvider;
import org.eclipse.rwt.internal.util.HTML;

import com.w4t.*;
import com.w4t.event.WebActionEvent;
import com.w4t.internal.adaptable.IRenderInfoAdapter;
import com.w4t.types.WebColor;


/** 
 * <p>The superclass of all Renderers that render org.eclipse.rap.WebButton.</p>
 */
public abstract class WebButtonRenderer extends Renderer {
  
  private final static String FONT_BUFFER = "fontBuffer";
  
  protected String createId( final WebButton wbt ) {
    // return nothing - subclasses may override
    return "";
  }
  
  void setFontBuffer( final WebButton button, final WebColor fontBuffer ) {
    IRenderInfoAdapter adapter = getRenderInfoAdapter( button );
    adapter.addRenderState( FONT_BUFFER, fontBuffer );      
  }
  
  WebColor getFontBuffer( final WebButton button ) {
    IRenderInfoAdapter adapter = getRenderInfoAdapter( button );
    return ( WebColor )adapter.getRenderState( FONT_BUFFER  );    
  }
  
  IRenderInfoAdapter getRenderInfoAdapter( final WebButton button ) {
    Class clazz = IRenderInfoAdapter.class;
    return ( IRenderInfoAdapter )button.getAdapter( clazz );
  }
  
  protected void bufferFontColor( final WebButton wbt ) {
    setFontBuffer( wbt, wbt.getStyle().getColor() );
    if( !wbt.isEnabled() ) {
      wbt.getStyle().setColor( wbt.getDisabledColor() );
    }
  }

  protected void restoreFontColor( final WebButton wbt ) {
    wbt.getStyle().setColor( getFontBuffer( wbt ) );
  }
  
  static String createEnabledImageName( final WebButton wbt ) {
    return wbt.getImage();
  }
  
  static String createDisabledImageName( final WebButton wbt ) {
    String imagePath = "";
    StringBuffer imageName = new StringBuffer();
    if( endsWithSeparator( wbt ) ) {
      int lastSeparatorIndex = getLastSeparatorIndex( wbt );
      imagePath = wbt.getImage().substring( 0, lastSeparatorIndex );
      imageName.append( "/disabled" );
      imageName.append( wbt.getImage().substring( lastSeparatorIndex + 1 ) );
    } else {
      imageName.append( "disabled" );
      imageName.append( wbt.getImage() );
    }
    return imagePath + imageName.toString();
  }  
  
  static int getLastSeparatorIndex( final WebButton wbt ) {
    return wbt.getImage().lastIndexOf( "/" );    
  }
  
  static boolean endsWithSeparator( final WebButton wbt ) {
    return getLastSeparatorIndex( wbt ) != -1;
  }

  static void createNonBreakingSpace( final WebButton wbt ) throws IOException {
    if( !wbt.isUseTrim() ) {
      ContextProvider.getStateInfo().getResponseWriter().writeNBSP();
    }
  }

  /** returns the html Code for loading a image if used as button,
    * else the simple label for the link is returned  
   * @throws IOException */
  static void createImageOrLabel( final WebButton wbt ) throws IOException {
    if( isEmptyImage( wbt ) ) {
      createLabel( wbt );
    } else {
      createImgTag( wbt );
    }
  }

  private static void createLabel( final WebButton wbt ) throws IOException {
    HtmlResponseWriter out = ContextProvider.getStateInfo().getResponseWriter();
    String label = getLabel( wbt );
    String[] splitLineBreak = RenderUtil.splitLineBreak( label );
    for( int i = 0; i < splitLineBreak.length; i++ ) {
      if( splitLineBreak[ i ].equals( "\n" ) ) {
        out.write( "<br>" );
      } else {
        out.writeText( splitLineBreak[ i ], null );
      }      
    }
  }

  static void createImgTag( final WebButton wbt ) throws IOException {
    if( wbt.isEnabled() ) {
      createImgTag( wbt, createEnabledImageName( wbt )  );
    } else {
      createImgTag( wbt, createDisabledImageName( wbt ) );
    }
  }

  static void createImgTag( final WebButton wbt,
                            final String imageName ) 
    throws IOException
  {
    HtmlResponseWriter out = ContextProvider.getStateInfo().getResponseWriter();
    out.startElement( HTML.IMG, null );
    String location = RenderUtil.resolveLocation( imageName );
    out.writeAttribute( HTML.SRC, location, null );
    out.writeAttribute( HTML.ALT, getTitle( wbt ), null );
    out.writeAttribute( HTML.BORDER, "0", null );
    out.endElement( HTML.IMG );
  }
  
  static String getLabel( final WebButton wbt ) {
    return RenderUtil.resolve( wbt.getLabel() );
  }
  
  static String getTitle( final WebButton wbt ) {
    return RenderUtil.resolve( wbt.getTitle() );
  }
  
  // queries
  //////////
  
  /** true, if the button is enabled and has an ActionListener set. */
  static boolean isActive( final WebButton wbt ) {
    return wbt.isEnabled() && WebActionEvent.hasListener( wbt );
  }
  
  static boolean isEmptyImage( final WebButton wbt ) {
    return wbt.getImage().equals( "" );
  }
}