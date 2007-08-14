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
package com.w4t.webbordercomponentkit.types;

import java.io.IOException;

import org.eclipse.rwt.internal.lifecycle.HtmlResponseWriter;

import com.w4t.WebBorderComponent;

/** border which hovers up on mouse over.
  */
class WebBorderComponentHover extends WebBorderComponentType {
  
  /** Creates a new instance of WebBorderComponentHover */
  WebBorderComponentHover( final WebBorderComponent webBorderComponent ) {
    super( webBorderComponent );
  }
    
  public int getBorderType() {
    return WebBorderComponent.HOVER;
  }
  
  public void createDecorationHeader( final HtmlResponseWriter out ) 
    throws IOException
  {
    out.append( "<div align=\"" );
    out.append( webBorderComponent.getAlign() );
    out.append( "\" " );
    out.append( "id=\"" );
    out.append( webBorderComponent.getUniqueID() );
    out.append( "\" " );
    createStyle( out );
    out.append( "onmouseover=\"this.style.borderBottomColor='" );
    out.append( webBorderComponent.getDarkColor().toString() );
    out.append( "';" );
    out.append( "this.style.borderLeftColor='" );
    out.append( webBorderComponent.getLightColor().toString() );
    out.append( "';" );
    out.append( "this.style.borderRightColor='" );
    out.append( webBorderComponent.getDarkColor().toString() );
    out.append( "';" );
    out.append( "this.style.borderTopColor='" );
    out.append( webBorderComponent.getLightColor().toString() );
    out.append( "';\" " );
    out.append( "onmouseout=\"this.style.border='1px solid " );
    out.append( webBorderComponent.getBgColor().toString() );    
    out.append( "'\">" );
  }

  public void createDecorationFooter( final HtmlResponseWriter out ) 
    throws IOException
  {
    out.append( "</div>" );
  }
  
  void fifthRow(  final HtmlResponseWriter out  ) {
    // implemented empty
  }
  
  void firstBorderColumnForthRow(  final HtmlResponseWriter out  ) {
    // implemented empty
  }
  
  void firstBorderColumnSecondRow(  final HtmlResponseWriter out  ) {
    // implemented empty
  }
  
  void firstBorderColumnThirdRow(  final HtmlResponseWriter out  ) {
    // implemented empty
  }
  
  void firstRow(  final HtmlResponseWriter out  ) {
    //  implemented empty
  }
  
  void lastBorderColumnForthRow(  final HtmlResponseWriter out  ) {
    //  implemented empty
  }
  
  void lastBorderColumnSecondRow(  final HtmlResponseWriter out  ) {
    //  implemented empty    
  }
  
  void lastBorderColumnThirdRow(  final HtmlResponseWriter out  ) {
    //  implemented empty    
  }
  
  void setColorsForFooter() {
    //  implemented empty    
  }
  
  void setColorsForHeader() {
    //  implemented empty    
  }
  
  
  // helping methods
  //////////////////
  
  private void createStyle( final HtmlResponseWriter out ) {
    out.append( "class=\"" );
    out.append( out.registerCssClass( createStyleContent() ) );
    out.append( "\" " );
  }

  private String createStyleContent() {
    StringBuffer css = new StringBuffer( "border:1px solid " );
    css.append( webBorderComponent.getBgColor().toString() );
    css.append( ";padding:" );
    css.append( String.valueOf( webBorderComponent.getPadding() ) );
    css.append( "px;background-color:" );
    css.append( webBorderComponent.getBgColor().toString() );
    css.append( ";" );
    return css.toString();
  } 
}