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

/**
  * <p>A Point represents a pair of coordinates (x and y values) for
  * positioning.</p>
  */
public class Point {

  private final int x;
  private final int y;

  /** <p>constructs a new Point with zero coordinates.</p> */
  public Point() {
    this.x = 0;
    this.y = 0;
  }

  /** <p>constructs a new Point with the specified coordinates.</p> */
  public Point( final int x, final int y ) {
    this.x = x;
    this.y = y;
  }

  /** <p>returns the value for the x coordinate of the point represented
    * by this Point.</p> */
  public int getX() {
    return x;
  }

  /** <p>returns the value for the x coordinate of the point represented
    * by this Point.</p> */
  public int getY() {
    return y;
  }
}