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
  * <p>A constraint type for absolute positioning. For absolute positioning,
  * the position of the positioned component as top-left point coordinates and
  * the width (either as pixel width or as percentage) can be set.</p>
  */
public class AbsoluteConstraint extends Object {

  /** <p>the top left point to which the positioned component is set.</p> */
  Point ptTopLeft;
  /** <p>the width for the positioned component.</p> */
  String width;

  /** <p>constructs a new AbsoluteConstraint.</p> */
  public AbsoluteConstraint() {
    this.ptTopLeft = new Point( 0, 0 );
    this.width = "100%";
  }

  /** <p>constructs a new AbsoluteConstraint with the specified top-left
    * point.</p> */
  public AbsoluteConstraint( final Point ptTopLeft ) {
    this.ptTopLeft = ptTopLeft;
    this.width = "100%";
  }

  /** <p>constructs a new AbsoluteConstraint with the specified width.</p> */
  public AbsoluteConstraint( final String width ) {
    this.ptTopLeft = new Point( 0, 0 );
    this.width = width;
  }

  /** <p>constructs a new AbsoluteConstraint with the specified top-left
    * point and width.</p> */
  public AbsoluteConstraint( final Point ptTopLeft, final String width ) {
    this.ptTopLeft = ptTopLeft;
    this.width = width;
  }


  // attribute getters and setters
  ////////////////////////////////

  /** <p>returns the width for the positioned component.</p> */
  public String getWidth() {
    return width;
  }

  /** <p>returns the top left point to which the positioned component 
    * is set.</p> */
  public Point getTopLeftCoordinates() {
    return ptTopLeft;
  }

  /** <p>returns the x coordinate of the top left point to which the 
    * positioned component is set.</p> */
  public int getTopLeftX() {
    return ptTopLeft.getX();
  }

  /** <p>returns the y coordinate of the top left point to which the 
    * positioned component is set.</p> */
  public int getTopLeftY() {
    return ptTopLeft.getY();
  }
}