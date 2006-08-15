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

/** <p>This is a helping class for the constraints used in
  * the WebGridLayout.</p>
  * <p>It encapsulates the constraints for the WebGridLayout
  * which is a row number and a column number to access a specific
  * cell of the Grid produced by the layout manager.</p>
  */
public class Position extends Object {

  /** the row number of this WebGridLayoutConstraints */
  private int rowPosition = 1;
  /** the column number of this WebGridLayoutConstraints */
  private int colPosition = 1;

  /**
    * constructor only values greater than one are accepted
    * for the parameters. Other values are set to one.
    */
  public Position( final int row, final int col ) {
    if( row > rowPosition ) {
      rowPosition = row;
    }
    if( col > colPosition ) {
      colPosition = col;
    }
  }

  /** returns the row number of this WebGridLayoutConstraints */
  public int getRowPosition() {
    return rowPosition;
  }

  /** returns the column number of of this WebGridLayoutConstraints */
  public int getColPosition() {
    return colPosition;
  }

  /** <p>Indicates whether some other object is "equal to" this one, 
    * which means that it must be an instance of Position and has 
    * the same values for row and column.</p> */
  public boolean equals( final Object obj ) {
    boolean result = false;
    if( obj instanceof Position ) {
      Position pos = ( Position )obj;
      result =    pos.getColPosition() == this.getColPosition()
               && pos.getRowPosition() == this.getRowPosition();
    }
    return result;
  }
  
  // no javadoc, so it is inherited from java.lang.Object javadoc
  public String toString() {
    return "(" + String.valueOf( rowPosition )
         + "," + String.valueOf( colPosition )
         + ")";
  }
}

