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
package com.w4t.webformkit;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.rwt.internal.service.ContextProvider;
import org.eclipse.rwt.internal.service.RequestParams;

import com.w4t.*;
import com.w4t.util.ComponentTreeVisitor;
import com.w4t.util.ComponentTreeVisitor.AllComponentVisitor;


final class WebFormReadDataUtil {
  
  static void read( final WebForm form ) {
    Enumeration names = getRequest().getParameterNames();
    while( names.hasMoreElements() ) {
      String name = ( String )names.nextElement();
      applyFocus( name, form );
      applyScrollX( name, form );
      applyScrollY( name, form );
      applyAvailWidth( name );
      applyAvailHeight( name );
    }
  }
  
  private static void applyFocus( final String name, final WebForm form ) {
    if ( name.equals( RequestParams.FOCUS_ELEMENT ) ) {
      String value = ReadDataUtil.findValue( RequestParams.FOCUS_ELEMENT );
      final String focusId = SelectionUtil.parseFocusId( value );
      ComponentTreeVisitor visitor = new AllComponentVisitor() {
        public boolean doVisit( final WebComponent component ) {
          boolean hasFocus = focusId.equals( component.getUniqueID() );
          if( component instanceof IFocusable ) {
            ( ( IFocusable ) component ).setFocus( hasFocus );
          }
          return !hasFocus;
        }
      };
      ComponentTreeVisitor.accept( form, visitor );
      SelectionUtil.setSelection( SelectionUtil.parseSelection( value ) );
    }
  }

  private static HttpServletRequest getRequest() {
    return ContextProvider.getRequest();
  }  
  
  private static void applyScrollX( final String name, final WebForm form ) {
    if( name.equals( RequestParams.SCROLL_X ) ) {
      String scrollX 
        = getRequest().getParameter( RequestParams.SCROLL_X ).trim();
      int scrollXValue = scrollX.equals( "" ) ? 0 : Integer.parseInt( scrollX );
      form.setScrollX( scrollXValue );
    }
  }
  
  private static void applyScrollY( final String name, final WebForm form ) {  
    if( name.equals( RequestParams.SCROLL_Y ) ) {
      String scrollY
        = getRequest().getParameter( RequestParams.SCROLL_Y ).trim();
      int scrollYValue = scrollY.equals( "" ) ? 0 : Integer.parseInt( scrollY );
      form.setScrollY( scrollYValue );
    }
  }
  
  private static void applyAvailHeight( final String name ) {
    if( name.startsWith( RequestParams.AVAIL_HEIGHT ) ) {
      int height = Integer.parseInt( getRequest().getParameter( name ) );
      WindowProperties.setAvailHeight( height );
    }    
  }

  private static void applyAvailWidth( final String name ) {
    if( name.startsWith( RequestParams.AVAIL_WIDTH ) ) {
      int width = Integer.parseInt( getRequest().getParameter( name ) );
      WindowProperties.setAvailWidth( width );
    }    
  }
  
  private WebFormReadDataUtil() {
    // prevent instantiation
  }
}