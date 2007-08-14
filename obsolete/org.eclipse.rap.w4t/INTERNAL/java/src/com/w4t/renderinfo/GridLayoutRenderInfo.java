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

import java.util.Vector;
import com.w4t.WebContainer;
import com.w4t.WebTableCell;

/** <p>The GridLayoutRenderInfo contains information for layouting a 
  * {@link org.eclipse.rwt.WebContainer WebContainer} using the 
  * {@link org.eclipse.rwt.WebGridLayout WebGridLayout}.</p>
  */
public class GridLayoutRenderInfo extends LayoutRenderInfo {

  /** contains the rows of the WebGridLayouts, whcih are Vectors 
    * containing WebTableCells. */
  private Vector rows;
  
  
  /** Creates a new instance of LayoutRenderInfo */
  public GridLayoutRenderInfo( final WebContainer parent, 
                               final Vector rows ) {
    super( parent );
    this.rows = rows;
  }
  
  
  // attribute getters and setters
  ////////////////////////////////

  public WebTableCell[] getTableCells( int rowNumber ) {
    Vector row = ( Vector )rows.get( rowNumber );
    WebTableCell[] result = new WebTableCell[ row.size() ];
    row.toArray( result );
    return result;
  }
}