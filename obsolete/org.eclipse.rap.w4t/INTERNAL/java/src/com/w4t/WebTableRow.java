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
package com.w4t;

import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.rwt.internal.lifecycle.HtmlResponseWriter;
import org.eclipse.rwt.internal.service.ContextProvider;
import org.eclipse.rwt.internal.service.IServiceStateInfo;
import org.eclipse.rwt.internal.util.HTML;

import com.w4t.internal.simplecomponent.UniversalAttributes;


/** <p>This is a helping class for positioning the WebComponents. It
  * builds a html table row.</p>
  */
public class WebTableRow
  extends WebObject
  implements SimpleComponent
{
  private final static UniversalAttributes DEFAULT_UNIVERSAL_ATTRIBUTES
    = createUniversalAttributes();


  /** contains the WebTableCells in that row */
  protected ArrayList row = new ArrayList();
  /** count of columns of that row */
  protected int col = 0;
  /** the universal html attributes encapsulation class */
  private UniversalAttributes universalAttributes;
  
  
  /** returns a clone of this WebText. */
  public Object clone() throws CloneNotSupportedException {
    WebTableRow result = ( WebTableRow )super.clone();
    if( universalAttributes != null ) {
      result.universalAttributes 
        = ( UniversalAttributes )universalAttributes.clone();
    }
    return result;
  }  

  /** builds the html table cell with the specified properties. */
  public void render() throws IOException {
    IServiceStateInfo stateInfo = ContextProvider.getStateInfo();
    HtmlResponseWriter out = stateInfo.getResponseWriter();
    out.startElement( HTML.TR, null );
    if( universalAttributes != null ) {
      universalAttributes.writeUniversalAttributes();
    } else {
      DEFAULT_UNIVERSAL_ATTRIBUTES.writeUniversalAttributes();
    }
    for( int i = 0; i < col; i++ ) {
      WebTableCell cell = ( WebTableCell )row.get( i );
      if( cell != null ) {
        cell.render();
      }
    }
    out.endElement( HTML.TR );
  }

  /** adds a WebTableCell to the WebTableRow.
    * @param webTableCell cell to add to this row */
  public void addCell( final WebTableCell webTableCell ) {
    row.add( col, webTableCell );
    col++;
  }

  /** adds a WebTableCell to the WebTableRow at the specified index.
    * @param webTableCell cell to add to the current Row
    * @param index position at which the row should be added */
  public void addCell( final WebTableCell webTableCell, final int index ) {
    if( index >= col ) {
      row.add( col, webTableCell );
    } else if( index < 0 ) {
      row.add( 0 , webTableCell );
    } else {
      row.add( index, webTableCell );
    }
    col++;
  }

  /** removes a WebTableCell from the WebTableRow.
    * @param index index of Cell to remove */
  public void removeCell( final int index ) {
    row.remove( index );
    col--;
  }
  
  /** retrieves the table cell at the specified index */
  public WebTableCell retrieveCell( final int index ) {
    return ( WebTableCell )row.get( index );
  }

  /** returns the count of table cells in this row. */
  public int getCellCount() {
    return row.size();
  }
  
  
  // interface methods of org.eclipse.rap.SimpleComponent
  // (no javadoc comments, so they are copied from the interface)
  ///////////////////////////////////////////////////////////////
  
  public String getCssClass() {
    return getUniversalAttributes().getCssClass();
  }
  
  public String getDir() {
    return getUniversalAttributes().getDir();
  }
  
  public String getLang() {
    return getUniversalAttributes().getLang();
  }
  
  public Style getStyle() {
    return getUniversalAttributes().getStyle();
  }
  
  public String getTitle() {
    return getUniversalAttributes().getTitle();
  }
  
  public void setCssClass( final String cssClass ) {
    getUniversalAttributes().setCssClass( cssClass );
  }
  
  public void setDir( final String dir ) {
    getUniversalAttributes().setDir( dir );
  }
  
  public void setLang( final String lang ) {
    getUniversalAttributes().setLang( lang );
  }
  
  public void setStyle( final Style style ) {
    getUniversalAttributes().setStyle( style );
  }
  
  public void setTitle( final String title ) {
    getUniversalAttributes().setTitle( title );
  }

  public void setIgnoreLocalStyle( final boolean ignoreLocalStyle ) {
    getUniversalAttributes().setIgnoreLocalStyle( ignoreLocalStyle );
  }
  
  public boolean isIgnoreLocalStyle() {
    return getUniversalAttributes().isIgnoreLocalStyle();
  }

  private UniversalAttributes getUniversalAttributes() {
    if( universalAttributes == null ) {
      universalAttributes = createUniversalAttributes();
    }
    return universalAttributes;
  }
  
  private static UniversalAttributes createUniversalAttributes() {
    UniversalAttributes result = new UniversalAttributes();
    result.getStyle().setFontFamily( "" );
    result.getStyle().setFontSize( Style.NOT_USED );
    return result;
  }
}