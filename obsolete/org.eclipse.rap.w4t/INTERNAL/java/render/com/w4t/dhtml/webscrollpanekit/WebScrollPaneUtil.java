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
package com.w4t.dhtml.webscrollpanekit;

import java.text.MessageFormat;
import com.w4t.ReadDataUtil;
import com.w4t.ajax.AjaxStatusUtil;
import com.w4t.dhtml.WebScrollPane;


final class WebScrollPaneUtil {
  
  static final String JS = "resources/js/scrollpane/scrollpane.js";

  private static final String GET_SCROLL_POS = "getScrollPosition(''{0}'');";

  static final String PREFIX_SCROLL_X = "scrollPaneX_";
  static final String PREFIX_SCROLL_Y = "scrollPaneY_";
  static final String POST_FIX_SCROLL_DIV = "_scrollDiv";
  
  static String createStyle( final WebScrollPane scrollPane ) {
    StringBuffer style = new StringBuffer();
    style.append( "overflow:auto;" );
    style.append( "width:" );
    style.append( scrollPane.getWidth() );
    style.append( "px;" );
    style.append( "height:" );
    style.append( scrollPane.getHeight() );
    style.append( "px;" );
    return style.toString();
  }

  static String createGetScrollPositionCode( final String id ) {
    return MessageFormat.format( GET_SCROLL_POS, new Object[] { id } );
  }

  static void readData( final WebScrollPane scrollPane ) {
    String scrollDivId = getSrollDivId( scrollPane );
    // read current x position
    String scrollXParam = PREFIX_SCROLL_X + scrollDivId;
    String scrollXValue = ReadDataUtil.findValue( scrollXParam );
    if( scrollXValue != null ) {
      scrollPane.setScrollX( Integer.parseInt( scrollXValue ) );
    }
    // read current y position
    String scrollYParam = PREFIX_SCROLL_Y + scrollDivId;
    String scrollYValue = ReadDataUtil.findValue( scrollYParam );
    if( scrollYValue != null ) {
      scrollPane.setScrollY( Integer.parseInt( scrollYValue ) );
    }
  }

  static String getSrollDivId( final WebScrollPane wsp ) {
    StringBuffer idBuffer = new StringBuffer( wsp.getUniqueID() );
    return idBuffer.append( POST_FIX_SCROLL_DIV ).toString();
  }
  
  static void readDataInAjaxMode( WebScrollPane pane ) {
    int oldScrollX = pane.getScrollX();
    int oldScrollY = pane.getScrollY();
    WebScrollPaneUtil.readData( pane );
    if( oldScrollX != pane.getScrollX() || oldScrollY != pane.getScrollY() ) {
      AjaxStatusUtil.updateHashCode( pane );
    }
  }

  private WebScrollPaneUtil() {
    // prevent instantiation
  }
}
