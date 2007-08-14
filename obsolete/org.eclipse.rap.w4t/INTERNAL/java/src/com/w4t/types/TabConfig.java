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
package com.w4t.types;

import com.w4t.WebComponentProperties;


/** <p>Encapsulates configuration settings for the 
 * {@link org.eclipse.rwt.WebCardLayout WebCardLayout}, such as the position of 
 * the tabs (top, left, right, bottom), their alignment, and the general 
 * style of the default settings.</p>
 */
public class TabConfig extends WebComponentProperties {

  public static final String ALIGN_LEFT   = "left";
  public static final String ALIGN_CENTER = "center";  
  public static final String ALIGN_RIGHT  = "right";  
  public static final String ALIGN_TOP    = "top";
  public static final String ALIGN_MIDDLE = "middle";  
  public static final String ALIGN_BOTTOM = "bottom";  
  
  public static final String POSITION_TOP    = "top";  
  public static final String POSITION_LEFT   = "left";
  public static final String POSITION_RIGHT  = "right";  
  public static final String POSITION_BOTTOM = "bottom";  
  
  public static final int TYPE_CLASSIC = 0;
  public static final int TYPE_MODERN  = 1;

  private int type;
  private String tabPosition;
  private String tabAlignment;

  /** <p>Constructs a new TabConfig with the following default settings:
   *  TYPE_CLASSIC, POSITION_TOP, ALIGN_LEFT</p> */
  public TabConfig() {
    this.type = TYPE_MODERN;
    this.tabPosition = POSITION_TOP;
    this.tabAlignment = ALIGN_LEFT;
  }

  /** <p>Constructs a new TabConfig with the passed settings.</p> */
  public TabConfig( final int type, 
                    final String tabPosition, 
                    final String tabAlignment ) {
    this.type = type;
    this.tabPosition = tabPosition;
    this.tabAlignment = tabAlignment;
  }

  // attribute getters and setter
  ///////////////////////////////

  /** <p>Returns the tabAlignment of this TabConfig.</p> */
  public String getTabAlignment() {
    return tabAlignment;
  }

  /** <p>Returns the tabPosition.</p> */
  public String getTabPosition() {
    return tabPosition;
  }

  /** <p>Returns the type  of this TabConfig.</p> */
  public int getType() {
    return type;
  }

  /** <p>Sets the tabAlignment of this TabConfig.</p> */
  public void setTabAlignment( final String tabAlignment ) {
    this.tabAlignment = tabAlignment;
  }

  /** <p>Sets the tabPosition of this TabConfig.</p> */
  public void setTabPosition( final String tabPosition ) {
    this.tabPosition = tabPosition;
  }

  /** <p>Sets the type of this TabConfig.</p> */
  public void setType( final int type ) {
    this.type = type;
  }
  
  
  // additional information getters
  /////////////////////////////////
  
  /** <p>Returns, whether the WebCardLayout is configured by this TabConfig 
   *  for horizontal tab position, that is, the tabs are in the top or 
   *  bottom row.</p> */
  public boolean isHorizontal() {
    return    tabPosition == POSITION_TOP
           || tabPosition == POSITION_BOTTOM;
  }

  /** <p>Returns, whether the WebCardLayout is configured by this TabConfig 
   *  for vertical tab position, that is, the tabs are in the left or right 
   *  column.</p> */
  public boolean isVertical() {
    return    tabPosition == POSITION_LEFT
           || tabPosition == POSITION_RIGHT;
  }
}