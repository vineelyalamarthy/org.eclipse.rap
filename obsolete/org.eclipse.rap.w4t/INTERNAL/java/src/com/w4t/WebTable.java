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
import org.eclipse.rwt.Adaptable;

import com.w4t.types.WebColor;
import com.w4t.util.DefaultColorScheme;

/** <p>This is a helping class for positioning the WebComponents.</p>
  * <p>It builds a html table basically with no border, no cellpadding,
  * no cellspacing, and uses the maximum space available.</p>
  */
public abstract class WebTable implements Cloneable, Adaptable {

  /** html attribute settings of the table */
  protected WebColor bgColor 
    = new WebColor( DefaultColorScheme.get( DefaultColorScheme.TABLE_BG ) );
  /** html attribute settings of the table */
  private String width = "100%";
  /** html attribute settings of the table */
  private String height = "";
  /** html attribute settings of the table */
  private String cellspacing = "0";
  /** html attribute settings of the table */
  private String cellpadding = "0";
  /** html attribute settings of the table */
  private String border = "0";
  /** html attribute settings of the table */
  private String align = "";

  /** returns a clone of this WebTableCell with a shallow copy of all
    * field values */
  public Object clone() throws CloneNotSupportedException {
    WebTable result = ( WebTable )super.clone();
    result.bgColor = ( WebColor )this.bgColor.clone();
    return result;
  }
  
  public Object getAdapter( final Class adapter ) {
    return W4TContext.getAdapterManager().getAdapter( this, adapter );
  }

  /**
    * sets the width of this table.
    * Corresponds to the HTML attribute width of the tag &lsaquo;table&rsaquo;.
    * For a short example see the beginning of
    * {@link WebFlowLayout <code>WebFlowLayount</code>}
    * @param width specifies the width either by a positive integer value or by
    * a percentage.
    */
  public void setWidth( final String width ) {
    this.width = width;
  }

  /**
    * returns a string which contains the width of this table.
    * Corresponds to the HTML attribute width of the tag &lsaquo;table&rsaquo;.
    */
    public String getWidth() {
    return width;
  }

  /**
    * sets the height of the table.
    * Attention! This attribute doesn't work well with some browsers!
    * Instead of setting this attribute you can place a blind gif into this
    * table to force the required height.
    * Corresponds to the HTML attribute height of the tag &lsaquo;table&rsaquo;.
    * @param height specifies the height either by a positive integer value or
    * by a percentage.
    */
  public void setHeight( final String height ) {
    this.height = height;
  }

  /**
    * returns a string which contains the height of the table.
    * Corresponds to the HTML attribute height of the tag &lsaquo;table&rsaquo;.
    */
  public String getHeight() {
    return height;
  }

  /**
    * sets the thickness of the cell borders. Refers to all cells of this table.
    * Corresponds to the HTML attribute cellspacing of the tag
    * &lsaquo;table&rsaquo;.
    * For a short example see the beginning of
    * {@link WebFlowLayout WebFlowLayout}.
    * @param cellspacing specifies the thickness by a positive integer value
    * (pixel).
    */
  public void setCellspacing( final String cellspacing ) {
    this.cellspacing = cellspacing;
  }

  /**
    * returns a string which contains the thickness of the cell borders. Refers
    * to all cells of this table. Corresponds to the HTML attribute cellspacing
    * of the tag &lsaquo;table&rsaquo;.
    */
  public String getCellspacing() {
    return cellspacing;
  }

  /**
    * sets the distance of the content of a cell to its border. Refers to all
    * cells of this table. Corresponds to the HTML attribute cellpadding of the
    * tag &lsaquo;table&rsaquo;.
    * For a short example see the beginning of
    * {@link WebFlowLayout WebFlowLayout}.
    * @param cellpadding specifies the distance by a positive integer value
    * (pixel).
    */
  public void setCellpadding( final String cellpadding ) {
    this.cellpadding = cellpadding;
  }

  /**
    * returns a string which contains the distance of the content of a cell to
    * its border. Refers to all cells of this table. Corresponds to the HTML
    * attribute cellpadding of the tag &lsaquo;table&rsaquo;.
    */
  public String getCellpadding() {
    return cellpadding;
  }

  /**
    * sets the thickness of the table border. Corresponds to the HTML attribute
    * border of the tag &lsaquo;table&rsaquo;.
    * For a short example see the beginning of
    * {@link WebFlowLayout WebFlowLayout}.
    * @param border specifies the border thickness by a positive integer value
    * (pixel).
    */
  public void setBorder( final String border ) {
    this.border = border;
  }

  /**
    * returns a string which contains the thickness of the table border.
    * Corresponds to the HTML attribute border of the tag &lsaquo;table&rsaquo;.
    */
  public String getBorder() {
    return border;
  }

  /**
    * sets the background color of this table. Corresponds to the HTML
    * attribute bgcolor of the tag &lsaquo;table&rsaquo;.
    * For a short example see the beginning of
    * {@link WebFlowLayout WebFlowLayout}.
    * @param bgColor specifies the chosen color. bgColor can either be a
    * hexadecimal RGB-value (red/green/blue-value of the color) or one of 16
    * color names (like "black", "white", "red" etc.)
    */
  public void setBgColor( final WebColor bgColor ) {
    this.bgColor = bgColor;
  }

  /**
    * returns a string which contains the background color of this table.
    * Corresponds to the HTML attribute bgcolor of the tag
    * &lsaquo;table&rsaquo;.
    */
  public WebColor getBgColor() {
    return bgColor;
  }

  /**
    * sets the alignment of this table. Corresponds to the HTML attribute align
    * of the tag &lsaquo;table&rsaquo;.
    * For a short example see the beginning of
    * {@link WebFlowLayout WebFlowLayout}.
    * @param align specifies the alignment by a string. This can be for example
    * "left", "right" or "center".
    */
  public void setAlign( final String align ) {
    this.align = align;
  }

  /**
    * returns a string which contains the alignment of this table.
    * Corresponds to the HTML attribute align of the tag &lsaquo;table&rsaquo;.
    */
  public String getAlign() {
    return align;
  }
}
