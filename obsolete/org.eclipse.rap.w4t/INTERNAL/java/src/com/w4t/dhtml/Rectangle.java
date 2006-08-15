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
package com.w4t.dhtml;

import com.w4t.Position;


/**
  * <p>A representation of four integer coordinates. Coordinates are top-left
  * originated and zero-counted. The rectangle represented by this Rectangle
  * object is determined by its top-left point, width and height.</p>
  */
public class Rectangle {

  /** <p>the x coordinate of the top-left point of the rectangle represented
    * by this Rectangle object.</p> */
  private int topLeftX;
  /** <p>the y coordinate of the top-left point of the rectangle represented
    * by this Rectangle object.</p> */
  private int topLeftY;
  /** <p>the x coordinate of the bottom-right point of the rectangle
    * represented by this Rectangle object.</p> */
  private int bottomRightX;
  /** <p>the y coordinate of the bottom-right point of the rectangle
    * represented by this Rectangle object.</p> */
  private int bottomRightY;
  /** <p>the width of the rectangle represented by this Rectangle
    * object.</p> */
  private int width;
  /** <p>the height of the rectangle represented by this Rectangle
    * object.</p> */
  private int height;


  /** <p>constructs a new Rectangle.</p> */
  public Rectangle() {
    this.topLeftX = 0;
    this.topLeftY = 0;
    this.bottomRightX = 0;
    this.bottomRightY = 0;
    this.width = 0;
    this.height = 0;
  }

  /** <p>constructs a new Rectangle with the specified top-left point and
    * the specified width and height.</p> */
  public Rectangle( final int topLeftX, 
                    final int topLeftY, 
                    final int width, 
                    final int height ) 
  {
    this.topLeftX = topLeftX;
    this.topLeftY = topLeftY;
    this.bottomRightX = topLeftX + width;
    this.bottomRightY = topLeftY + height;
    this.width = width;
    this.height = width;
  }

  /** <p>constructs a new Rectangle from the specified Position and
    * the specified width and height.</p>
    * <p>Note that Position objects have no zero-counting, the topmost and
    * leftmost pointv that can be set using this method is therefore ( 1, 1).
    * Use <code>Rectangle(int, int, int, int)</code> instead.</p> */
  public Rectangle( final Position pos, final int width, final int height ) {
    this.topLeftX = pos.getColPosition();
    this.topLeftY = pos.getRowPosition();
    this.bottomRightX = this.topLeftX + width;
    this.bottomRightY = this.topLeftY + height;
    this.width = width;
    this.height = width;
  }

  /** <p>constructs a new Rectangle from the specified Position objects. The
    * position with the coordinates that are nearer the x- resp. y-axis is
    * regarded top-left.</p>
    * <p>Note that Position objects have no zero-counting, the topmost and
    * leftmost point that can be set using this method is therefore ( 1, 1).
    * Use <code>Rectangle(int, int, int, int)</code> instead.</p> */
  public Rectangle( final Position pos1, final Position pos2 ) {
    this.topLeftX = pos1.getColPosition();
    this.topLeftY = pos1.getRowPosition();
    this.bottomRightX = pos2.getColPosition();
    this.bottomRightY = pos1.getRowPosition();
    if(    ( topLeftX < bottomRightX )
        || (    ( topLeftX == bottomRightX )
             && ( topLeftY < bottomRightY )   ) ) 
    {
      int tempX = topLeftX;
      int tempY = topLeftY;
      topLeftX = bottomRightX;
      topLeftY = bottomRightY;
      bottomRightX = tempX;
      bottomRightY = tempY;
    }
    this.width = topLeftX - bottomRightX;
    this.height = topLeftY - bottomRightY;
  }


  // attribute getters and setters
  ////////////////////////////////

  /** <p>sets the x coordinate of the top-left point of the rectangle
    * represented by this Rectangle object.</p> */
  public void setTopLeftX( final int topLeftX ) {
    int diff = this.topLeftX - topLeftX;
    this.topLeftX = topLeftX;
    this.bottomRightX += diff;
  }

  /** <p>returns the x coordinate of the top-left point of the rectangle
    * represented by this Rectangle object.</p> */
  public int getTopLeftX() {
    return topLeftX;
  }

  /** <p>sets the y coordinate of the top-left point of the rectangle
    * represented by this Rectangle object.</p> */
  public void setTopLeftY( final int topLeftY ) {
    int diff = this.topLeftY - topLeftY;
    this.topLeftY = topLeftY;
    this.bottomRightY += diff;
  }

  /** <p>returns the y coordinate of the top-left point of the rectangle
    * represented by this Rectangle object.</p> */
  public int getTopLeftY() {
    return topLeftY;
  }

  /** <p>sets the x coordinate of the top-left point of the rectangle
    * represented by this Rectangle object to the passed topLeftX and
    * topLeftY values.</p> */
  public void setTopLeft( final int topLeftX, final int topLeftY ) {
    setTopLeftX( topLeftX );
    setTopLeftY( topLeftY );
  }

  /** <p>sets the x and y coordinates of the top-left point of the rectangle
    * represented by this Rectangle object to the values of the passed
    * Position.</p>
    * <p>Note that Position objects have no zero-counting, the topmost and
    * leftmost point that can be set using this method is therefore ( 1, 1).
    * Use <code>setTopLeft(int, int)</code> instead.</p> */
  public void setTopLeft( final Position pos ) {
    setTopLeft( pos.getColPosition(), pos.getRowPosition() );
  }

  /** <p>returns the x and y coordinates of the top-left point of the rectangle
    * represented by this Rectangle object.</p> */
  public int[] getTopLeft() {
    return new int[] { topLeftX, topLeftY };
  }

  /** <p>returns a Position object representing the coordinates of the top-left
    * point of the rectangle represented by this Rectangle object.</p>
    */
  public Position getTopLeftPosition() {
    if( ( topLeftX == 0  ) || ( topLeftY == 0 ) ) {
      throw new IllegalStateException( "Position with zero values." );
    }
    Position pos = new Position( topLeftX, topLeftY );
    return pos;
  }

  /** <p>sets the width of the rectangle represented by this Rectangle
    * object.</p> */
  public void setWidth( final int width ) {
    this.width = width;
    this.bottomRightX = topLeftX + width;
  }

  /** <p>returns the width of the rectangle represented by this Rectangle
    * object.</p> */
  public int getWidth() {
    return width;
  }

  /** <p>sets the height of the rectangle represented by this Rectangle
    * object.</p> */
  public void setHeight( final int height ) {
    this.height = height;
    this.bottomRightY = topLeftY + height;
  }

  /** <p>returns the height of the rectangle represented by this Rectangle
    * object.</p> */
  public int getHeight() {
    return height;
  }
}