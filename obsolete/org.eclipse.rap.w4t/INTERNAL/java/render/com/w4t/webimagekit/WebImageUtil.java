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
package com.w4t.webimagekit;

import java.io.IOException;

import org.eclipse.rwt.internal.lifecycle.HtmlResponseWriter;
import org.eclipse.rwt.internal.service.ContextProvider;
import org.eclipse.rwt.internal.service.IServiceStateInfo;
import org.eclipse.rwt.internal.util.HTML;

import com.w4t.RenderUtil;
import com.w4t.WebImage;


/** <p>the superclass of all Renderers that render org.eclipse.rap.WebImage.</p>
  */
final class WebImageUtil {
  
  public static String createJSChangeImagePath( final String id ) {
    StringBuffer buffer = new StringBuffer();
    buffer.append( "javascript:eventHandler.changeImagePath(\'" );
    buffer.append( id );
    buffer.append( "')" );
    return buffer.toString();
  }
  
  /** determines the correct name and path for the img tag 
    * (depending on enabled attribute). */
  public static String createImagePath( final WebImage wim ) {
    String result = wim.getValue();
    String val = result;

    if( !wim.isEnabled() ) {
      int lastSeparatorIndex = val.lastIndexOf( "/" );
      String imagePath = "";
      String imageName = "";
      if( lastSeparatorIndex != -1 ) {
        imagePath = val.substring( 0, lastSeparatorIndex );
        imageName = "/disabled" + val.substring( lastSeparatorIndex + 1 );
      } else {
        imagePath = "";
        imageName = "disabled" + val;
      }
      result = imagePath + imageName;
    }
    result = RenderUtil.resolveLocation( result );
    return result;
  }
      
  public static String getAlt( final WebImage wim ) {
    return RenderUtil.resolve( wim.getAlt() );
  }

// TODO: [fappel] move this to a special WebImageRenderer in wdbc project 
//  /** returns whether a WebDataSource and a non-empty datafield are set. */
//  public static boolean isDataBound( final WebImage wim ) {
//    return     wim.getWebDataSource() != null 
//            && !wim.getDataField().toString().equals( "" )
//            && ( wim.isInsertable() || wim.isUpdatable() );
//  }
//  
//  public static void renderSize( final HtmlResponseWriter out, final WebImage wim ) {
//    if ( wim.getSize() > 0 ) {
//      HTMLUtil.attribute( out, "size", wim.getSize() );
//    }
//  }
//
//  public static void renderMaxLength( final HtmlResponseWriter out, 
//                                      final WebImage wim ) 
//  {
//    if ( wim.getMaxLength() > 0 ) {
//      HTMLUtil.attribute( out, "maxlength", wim.getMaxLength() );
//    }
//  }
  
  public static void renderHeight( final WebImage wim ) throws IOException {
    if ( !"".equals( wim.getHeight() ) ) {
      getResponseWriter().writeAttribute( HTML.HEIGHT , wim.getHeight(), null );
    }
  }
  
  public static void renderWidth( final WebImage wim ) throws IOException {
    if ( !"".equals( wim.getWidth() ) ) {
      getResponseWriter().writeAttribute( HTML.WIDTH, wim.getWidth(), null );
    }
  }

  private static HtmlResponseWriter getResponseWriter() {
    IServiceStateInfo stateInfo = ContextProvider.getStateInfo();
    return stateInfo.getResponseWriter();
  }
  
  private WebImageUtil() {
    // prevent instantiation
  }
  
}
