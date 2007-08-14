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

import com.w4t.types.WebColor;


/** <p>Defines the interface for parts of a {@link org.eclipse.rwt.WebContainer 
  * WebContainer}, when it is divided and layouted by a 
  * {@link org.eclipse.rwt.WebLayout WebLayout}.</p>
  */
public interface Area {

  /** <p>Sets the horizontal alignment of this Area.</p>
    * @param align specifies the alignment by a string. This can be for example
    *              "left", "right" or "center". See an html reference
    *              for more information about alignments.
    */
  void setAlign( String align );

  /** <p>Returns the horizontal alignment of this Area.</p> */
  String getAlign();

  /** <p>Sets the path of an image that is used as backgroud image of
    * this Area.</p> */
  void setBackground( String background );

  /** <p>Returns the path of an image that is used as backgroud image of
    * this Area.</p> */
  String getBackground();

  /** <p>Sets the background color of this Area.</p>
    * @param bgColor specifies the chosen color. bgColor can be a webColor
    *                containing either a hexadecimal RGB-value
    *                (red/green/blue-value of the color) or one of 16 color
    *                names (like "black", "white", "red" etc.) See an html
    *                reference for more information about colors. */
  void setBgColor( WebColor bgColor );

  /** <p>Returns the background color of this Area.</p> */
  WebColor getBgColor();

  /** <p>Sets the name of the cascading stylesheet class for this Area.</p> */
  void setCssClass( String cssClass );

  /** <p>Returns the name of the cascading stylesheet class for
    * this Area.</p> */
  String getCssClass();

  /** <p>Sets the height of this Area.</p>
    * @param height specifies the height either by a positive integer value or
    *               by a percentage. See an html reference for more
    *               information. */
  void setHeight( String height );

  /** <p>Returns the height of this Area.</p> */
  String getHeight();

  /** <p>Sets whether linebreaks are avoided in the text of this Area.</p> */
  void setNowrap( boolean nowrap );

  /** <p>Returns whether linebreaks are avoided in the text of this
    * Area.</p> */
  boolean isNowrap();

  /** <p>Sets a style object containing css attributes for this Area.</p> */
  void setStyle( Style style );

  /** <p>Returns a style object containing the css attributes for
    * this Area.</p> */
  Style getStyle();

  /** <p>Sets the tooltip text for this Area.</p> */
  void setTitle( String title );

  /** <p>Returns the tooltip text for this Area.</p> */
  String getTitle();

  /** <p>Sets the vertical alignment of this Area.</p>
    * @param valign specifies the alignment by a string. This can be for
    *               example "top", "bottom" or "middle". See an html reference
    *               for more information about alignments.
    */
  void setVAlign( String valign );

  /** <p>Returns the vertical alignment of this Area.</p> */
  String getVAlign();

  /** <p>Sets the width of this Area.</p>
    * @param width specifies the width either by a positive integer value or by
    *              a percentage. See an html reference for more information. */
  void setWidth( String width );

  /** <p>Returns the width of this Area.</p> */
  String getWidth();
}

