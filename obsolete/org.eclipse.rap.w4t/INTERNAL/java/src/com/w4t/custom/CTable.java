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
package com.w4t.custom;

import com.w4t.*;
import com.w4t.types.WebColor;

/** <p>CTable is a custom container used for positioning WebComponents
 *  in a two-dimensional table.</p>
 * 
 *  <p>CTable is based on the WebContainer component with the
 *  WebGridLayout as layout manager.</p> 
 */
public class CTable extends WebContainer implements ICustomContainer {

  ///////////////////////
  // constant definitions
  
  /** <p>align property value for center alignment.</p> */
  public final static String ALIGN_CENTER = "center";
  /** <p>align property value for left alignment.</p> */  
  public final static String ALIGN_LEFT = "left";
  /** <p>align property value for right alignment.</p> */  
  public final static String ALIGN_RIGHT = "right";
  
  
  /** <p>Creates a new instance of CTable.</p> */
  public CTable() {
    super();
    try {
      setWebLayout( new WebGridLayout() );
    } catch ( Exception ignored ) {
      /* Ignoring the Exception is safe here: the only reason for
       * throwing it may be a multiple adding of WebLayouts to containers;
       * since we have just created the WebLayout here, this can't be the case.
       */
    }    
  }
  
  /** <p>returns a path to an image that represents this WebComponent
   *  (widget icon).</p> */
  public static String retrieveIconName() {
    return "resources/images/icons/table.gif";
  }
  
  
  /** <p>returns the number of rows of this CTable</p> */
  public int getRowCount() {
    return getLayout().getRowCount();   
  }

  /** <p>sets the number of rows in this CTable.</p> */  
  public void setRowCount( final int rowCount ) {
    getLayout().setRowCount( rowCount );
  }
  
  /** <p>returns the number of columns in this CTable</p> */
  public int getColCount() {
    return getLayout().getColCount();
  }
  
  /** <p>sets the number of columns in this CTable.</p> */
  public void setColCount( final int colCount ) {
    getLayout().setColCount( colCount );
  }
  
  /** <p>sets the width of this CTable. Corresponds to the HTML 
   *  attribute width of the tag &lsaquo;table&rsaquo;.</p>
   * 
   *  @param width specifies the width either by a positive integer value or by
   *  a percentage value. */
  public void setWidth( final String width ) {
    getLayout().setWidth( width );
  }

  /** <p>returns a string which contains the width of this CTable.
   *  Corresponds to the HTML attribute width of the tag 
   *  &lsaquo;table&rsaquo;. </p>*/
  public String getWidth() {
    return getLayout().getWidth();
  }

  /** <p>sets the height of the CTable. Corresponds to the HTML attribute 
   *  height of the tag &lsaquo;table&rsaquo;.</p>
   *  <p>Note: This attribute doesn't work well with some browsers!
   *  Instead of setting this attribute you can place a blind gif into this
   *  CTable to force the required height.</p>
   *  @param height specifies the height either by a positive integer value or
   *  by a percentage value. */
  public void setHeight( final String height ) {
    getLayout().setHeight( height );
  }

  /** <p>returns a string which contains the height of the CTable.
   *  Corresponds to the HTML attribute height of the tag 
   *  &lsaquo;table&rsaquo;.</p>*/
  public String getHeight() {
    return getLayout().getHeight();
  }

  /** <p>sets the thickness of the cell borders. Refers to all cells of 
   *  this CTable. Corresponds to the HTML attribute cellspacing of the tag
   *  &lsaquo;table&rsaquo;.</p> */
  public void setCellspacing( final String cellspacing ) {
    getLayout().setCellspacing( cellspacing );
  }

  /** <p> returns a string which contains the thickness of the cell borders. 
   *  Refers to all cells of this CTable. Corresponds to the HTML 
   *  attribute cellspacing of the tag &lsaquo;table&rsaquo;.</p>
    */
  public String getCellspacing() {
    return getLayout().getCellspacing();
  }

  /** <p>sets the distance of the content of a cell to its border. 
   *  Refers to all cells of this CTable. Corresponds to the HTML 
   *  attribute cellpadding of the tag &lsaquo;table&rsaquo;.</p> */
  public void setCellpadding( final String cellpadding ) {
    getLayout().setCellpadding( cellpadding );
  }

  /** <p>returns a string which contains the distance of the content of 
   *  a cell to its border. Refers to all cells of this CTable. 
   * Corresponds to the HTML attribute cellpadding of the tag 
   * &lsaquo;table&rsaquo;.</p> */
  public String getCellpadding() {
    return getLayout().getCellpadding();
  }

  /** <p>sets the thickness of the CTable border. 
   *  Corresponds to the HTML attribute border of the 
   *  tag &lsaquo;table&rsaquo;.</p> */
  public void setBorder( final String border ) {
    getLayout().setBorder( border );
  }

  /** <p>returns a string which contains the thickness of the CTable border.
   *  Corresponds to the HTML attribute border of the tag 
   *  &lsaquo;table&rsaquo;. </p>*/
  public String getBorder() {
    return getLayout().getBorder();
  }

  /** <p>sets the background color of this CTable. Corresponds to the HTML
   *  attribute bgcolor of the tag &lsaquo;table&rsaquo;.</p>
   *  
   *  @param bgColor specifies the chosen color. bgColor can either be a
   *  hexadecimal RGB-value (red/green/blue-value of the color) or one of 16
   *  color names (like "black", "white", "red" etc.) */
  public void setBgColor( final WebColor bgColor ) {
    getLayout().setBgColor( bgColor );
  }

  /** <p>returns a string which contains the background color of this CTable.
    * Corresponds to the HTML attribute bgcolor of the tag
    * &lsaquo;table&rsaquo;.</p> */
  public WebColor getBgColor() {
    return getLayout().getBgColor();
  }

  /** <p>sets the alignment of this CTable. Corresponds to the HTML 
   *  attribute align of the tag &lsaquo;table&rsaquo;.</p>
   *  @param align specifies the alignment by a string. 
   *  Must be one of those constants: ALIGN_LEFT, ALIGN_CENTER, ALIGN_RIGHT. */
  public void setAlign( final String align ) {
    getLayout().setAlign( align );
  }

  /** <p>returns a string which contains the alignment of this CTable.
   *  Corresponds to the HTML attribute align of the tag 
   *  &lsaquo;table&rsaquo;. </p> */
  public String getAlign() {
    return getLayout().getAlign();
  }
  
  /** <p>returns the cell specified in the constraint object. Used e.g. for
   *  setting the cell format.</p> */
  public WebTableCell getCell( final Position constraint ) {
    return ( WebTableCell )getLayout().getArea( constraint );
  }
  

  /////////////////
  // helping method
  
  private WebGridLayout getLayout() {
    return ( WebGridLayout )getWebLayout();
  }
}