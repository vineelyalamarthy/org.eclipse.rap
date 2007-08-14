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

/** implementation of the OUTSET state of the WebBorderComponent
 */
class WebBorderComponentOutset extends WebBorderComponentType {
  
  WebBorderComponentOutset( final WebBorderComponent webBorderComponent ) {
    super( webBorderComponent );
  }
  
  public int getBorderType() {
    return WebBorderComponent.OUTSET;
  }
  
  void setColorsForHeader() {
    setColorBuffer1( webBorderComponent.getLightColor().toString() );       
  }  

  void setColorsForFooter() {
    setColorBuffer1( webBorderComponent.getDarkColor().toString() );
  }


  // empty implementations from here
  
  void firstRow( final HtmlResponseWriter out ) {}  

  void firstBorderColumnSecondRow( final HtmlResponseWriter out ) {}  

  void lastBorderColumnSecondRow( final HtmlResponseWriter out ) {}  
  
  void firstBorderColumnThirdRow( final HtmlResponseWriter out ) {}    
  
  void lastBorderColumnThirdRow( final HtmlResponseWriter out ) {}
  
  void firstBorderColumnForthRow( final HtmlResponseWriter out ) {}
    
  void lastBorderColumnForthRow( final HtmlResponseWriter out ) {}

  void fifthRow( final HtmlResponseWriter out ) {}
  
}