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

import org.eclipse.rwt.internal.lifecycle.HtmlResponseWriter;

import com.w4t.WebBorderComponent;

/** implementation of the RIDGE state of the WebBorderComponent.
 */
class WebBorderComponentRidge extends WebBorderComponentType {
  
  WebBorderComponentRidge( final WebBorderComponent webBorderComponent ) {
    super( webBorderComponent );
  }
    
  public int getBorderType() {
    return WebBorderComponent.RIDGE;
  }  
  
  void setColorsForHeader() {
    setColorBuffer1( webBorderComponent.getDarkColor().toString() );
    setColorBuffer2( webBorderComponent.getLightColor().toString() );      
  }  
  
  void firstRow( final HtmlResponseWriter out ) {
    openRow( out );
    getTableCellHTML( 5, getColorBuffer2(), "1", out );
    closeRow( out );
  }
  
  void firstBorderColumnSecondRow( final HtmlResponseWriter out ) {
    getTableCellHTML( 1, getColorBuffer2(), "1", out );    
  }  

  void lastBorderColumnSecondRow( final HtmlResponseWriter out ) {
    getTableCellHTML( 1, getColorBuffer1(), "1", out );    
  }
  
  void firstBorderColumnThirdRow( final HtmlResponseWriter out ) {
    getTableCellHTML( 1, getColorBuffer2(), "1", out );
  }    
  
  void setColorsForFooter() {
    setColorBuffer1( webBorderComponent.getLightColor().toString() );
    setColorBuffer2( webBorderComponent.getDarkColor().toString() );
  }
  
  void lastBorderColumnThirdRow( final HtmlResponseWriter out ) {
    getTableCellHTML( 1, getColorBuffer2(), "1", out );
  }
  
  void firstBorderColumnForthRow( final HtmlResponseWriter out ) {
    getTableCellHTML( 1, getColorBuffer1(), "1", out );
  }
    
  void lastBorderColumnForthRow( final HtmlResponseWriter out ) {
    getTableCellHTML( 1, getColorBuffer2(), "1", out );
  }
  
  void fifthRow( final HtmlResponseWriter out ) {
    openRow( out );
    getTableCellHTML( 5, getColorBuffer2(), "1", out );
    closeRow( out );
  }  
}