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
package com.w4t.renderinfo;

import com.w4t.*;


/** <p>The BorderLayoutRenderInfo contains information for layouting a 
  * {@link org.eclipse.rwt.WebContainer WebContainer} using the 
  * {@link org.eclipse.rwt.WebBorderLayout WebBorderLayout}.</p>
  */
public class BorderLayoutRenderInfo extends LayoutRenderInfo {

  private WebTableCell northCell;
  private WebTableCell westCell;
  private WebTableCell centerCell;
  private WebTableCell eastCell;
  private WebTableCell southCell;
  private WebTableRow  topRow;
  private WebTableRow  middleRow;
  private WebTableRow  bottomRow;
  
  
  
  /** Creates a new instance of LayoutRenderInfo */
  public BorderLayoutRenderInfo( final WebContainer parent, 
                                 final WebTableCell northCell,
                                 final WebTableCell westCell,
                                 final WebTableCell centerCell,
                                 final WebTableCell eastCell,
                                 final WebTableCell southCell,
                                 final WebTableRow topRow,
                                 final WebTableRow middleRow,
                                 final WebTableRow bottomRow ) {
    super( parent );
    this.northCell = northCell;
    this.westCell = westCell;
    this.centerCell = centerCell;
    this.eastCell = eastCell;
    this.southCell = southCell;
    this.topRow = topRow;
    this.middleRow = middleRow;
    this.bottomRow = bottomRow;
  }
  
  
  // attribute getters and setters
  ////////////////////////////////

  public WebTableCell getNorthCell() {
    return northCell;
  }

  public WebTableCell getWestCell() {
    return westCell;
  }

  public WebTableCell getCenterCell() {
    return centerCell;
  }
  
  public WebTableCell getEastCell() {
    return eastCell;
  }

  public WebTableCell getSouthCell() {
    return southCell;
  }
  
  public WebTableRow getTopRow() {
    return topRow;
  }

  public WebTableRow getMiddleRow() {
    return middleRow;
  }

  public WebTableRow getBottomRow() {
    return bottomRow;
  }  
}