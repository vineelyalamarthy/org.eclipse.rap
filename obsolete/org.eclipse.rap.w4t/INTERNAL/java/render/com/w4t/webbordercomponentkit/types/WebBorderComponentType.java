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

/** the abstract super class for the different types of borders used
  * by the WebBorderComponent.
  */
public abstract class WebBorderComponentType {

  WebBorderComponent webBorderComponent;
  
  private String colorBuffer1 = "";
  private String colorBuffer2 = "";

  
  /** constructor */
  WebBorderComponentType( final WebBorderComponent webBorderComponent ) {
    this.webBorderComponent = webBorderComponent;
  }
  
  public static WebBorderComponentType newType( final int borderType, 
                                                final WebBorderComponent wbc )
  {
    WebBorderComponentType result = null;
    switch( borderType ) {
      case WebBorderComponent.INSET:
        result = new WebBorderComponentInset( wbc );
      break;
      case WebBorderComponent.OUTSET:
        result = new WebBorderComponentOutset( wbc );
      break;
      case WebBorderComponent.RIDGE:
        result = new WebBorderComponentRidge( wbc );
      break;
      case WebBorderComponent.GROOVE:
        result = new WebBorderComponentGroove( wbc );
      break;
      case WebBorderComponent.HOVER:
        result = new WebBorderComponentHover( wbc );
      break;
      default:
        result = new WebBorderComponentInset( wbc );
    }
    return result;
  }  
  
  void tableInitialisations( final HtmlResponseWriter out ) {
    out.append( "<table width=\"" );
    out.append( webBorderComponent.getWidth() );
    out.append( "\" " );
    out.append( getTableHeight() );
    out.append( "cellspacing=\"0\" cellpadding=\"0\" border=\"0\">" );    
  }
  
  private String getTableHeight() {
    return hasPercentageHeight() 
         ? "height=\"" + webBorderComponent.getHeight() + "\" " 
         : "";
  }
    
  void closeTable( final HtmlResponseWriter out ) {
    out.append( "</table>" );
  }
  
  void secondRow( final HtmlResponseWriter out ) {
    openRow( out );
    firstBorderColumnSecondRow( out );
    centerColumnsSecondRow( out );
    lastBorderColumnSecondRow( out );
    closeRow( out );    
  }
  
  void centerColumnsSecondRow( final HtmlResponseWriter out ) {
    getTableCellHTML( 3, getColorBuffer1(), "1", out );    
  }
  
  void secondBorderColumnThirdRow( final HtmlResponseWriter out ) {
    getTableCellHTML( 1, getColorBuffer1(), getContentHeight(), out );
  }  
  
  void startContentCell( final HtmlResponseWriter out ) {
    out.append( "<td align=\"" );
    out.append( webBorderComponent.getAlign() );
    out.append( "\" valign=\"" );
    out.append( webBorderComponent.getVAlign() );
    out.append( "\" bgcolor=\"" );
    out.append( webBorderComponent.getBgColor().toString() );
    out.append( "\" " );
    if( webBorderComponent.getPadding() > 0 ) {
      out.append( "class=\""  );
      String css = "padding:" + webBorderComponent.getPadding() + ";";
      out.append( out.registerCssClass( css ) );
      out.append( "\" " );
    }
    out.append( "width=\"100%\">" );
  }  
  
  void endContentCell( final HtmlResponseWriter out ) {
    out.append( "</td>" );    
  }
  
  void thirdBorderColumnTirdRow( final HtmlResponseWriter out ) {
    getTableCellHTML( 1, getColorBuffer1(), "1", out );
  }
  
  void centerBorderColumnForthRow( final HtmlResponseWriter out ) {    
    getTableCellHTML( 3, getColorBuffer1(), "1", out );
  }
    
  void forthRow( final HtmlResponseWriter out ) {
    openRow( out );
    firstBorderColumnForthRow( out );
    centerBorderColumnForthRow( out );    
    lastBorderColumnForthRow( out );
    closeRow( out );    
  }
    
  void openRow( final HtmlResponseWriter out ) {
    out.append( "<tr>" );    
  }
  
  void closeRow( final HtmlResponseWriter out ) {
    out.append( "</tr>" );
  }
  
  public void createDecorationHeader( final HtmlResponseWriter out )
    throws IOException
  {
    setColorsForHeader();
    tableInitialisations( out );
    firstRow( out );
    secondRow( out );    
    openRow( out );
    firstBorderColumnThirdRow( out );
    secondBorderColumnThirdRow( out );
    startContentCell( out );    
  }
  
  public void createDecorationFooter( final HtmlResponseWriter out ) 
    throws IOException
  {
    setColorsForFooter();    
    endContentCell( out );
    thirdBorderColumnTirdRow( out );
    lastBorderColumnThirdRow( out );
    closeRow( out );
    forthRow( out );
    fifthRow( out );
    closeTable( out );
  }

  void getTableCellHTML( final int colSpan, 
                         final String bgColor, 
                         final String height, 
                         final HtmlResponseWriter out ) {
    out.append( "<td align=\"center\" valign=\"middle\" bgcolor=\"" );
    out.append( bgColor ); 
    out.append( "\"" );
    if( colSpan > 1 ) {
      out.append( " colspan=\"" ); 
      out.append( String.valueOf( colSpan ) );
      out.append( "\" height=\"1\"" );
    }
    out.append( ">" );
    
    out.append( "<span class=\"" );
    String css = "font-family:arial,verdana;font-size:8pt;";
    out.append( out.registerCssClass( css ) );
    out.append( "\">" );
    
    out.append( "<div><img src=\"resources/images/transparent.gif\" " );
    out.append( "border=\"0\"" );
    out.append( " width=\"1\" height=\"" );
    out.append( height );
    out.append( "\" align=\"top\" /></div></span></td>" );
  }


  // helping methods
  //////////////////
  
  private String getContentHeight() {
    return hasPercentageHeight() ? "1" : computeContentHeight();
  }
  
  private String computeContentHeight() {
    int iHeight = Integer.parseInt( webBorderComponent.getHeight() );
    if( isSingleBorderType() ) {
      iHeight = iHeight - 2;
    } else {
      iHeight = iHeight - 4;
    }
    iHeight = iHeight < 0 ? 0 : iHeight;
    return String.valueOf( iHeight );
  }


  ///////////////
  // conditionals
  
  private boolean hasPercentageHeight() {
    return webBorderComponent.getHeight().indexOf( '%' ) != -1;
  }
  
  private boolean isSingleBorderType() {
    return getBorderType() == WebBorderComponent.INSET 
        || getBorderType() == WebBorderComponent.OUTSET;
  }  
    
  //////////////////////
  // setters and getters
  
  void setColorBuffer1( final String colorBuffer1 ) {
    this.colorBuffer1 = colorBuffer1;
  }
  
  String getColorBuffer1() {
    return colorBuffer1;
  }
  
  void setColorBuffer2( final String colorBuffer2 ) {
    this.colorBuffer2 = colorBuffer2;
  }
  
  String getColorBuffer2() {
    return colorBuffer2;
  }  
  
  ///////////////////////////////
  // abstract method declarations
  
  public abstract int getBorderType();
  
  abstract void setColorsForHeader();
  
  abstract void firstRow( HtmlResponseWriter out );
  
  abstract void firstBorderColumnSecondRow( HtmlResponseWriter out );
  
  abstract void lastBorderColumnSecondRow( HtmlResponseWriter out );
  
  abstract void firstBorderColumnThirdRow( HtmlResponseWriter out );

  abstract void setColorsForFooter();
  
  abstract void lastBorderColumnThirdRow( HtmlResponseWriter out );
  
  abstract void firstBorderColumnForthRow( HtmlResponseWriter out );

  abstract void lastBorderColumnForthRow( HtmlResponseWriter out );
  
  abstract void fifthRow( HtmlResponseWriter out );
}