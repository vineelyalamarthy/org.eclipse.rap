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

import java.io.IOException;

/** <p>Defines the interface for classes that know how to layout 
  * {@link WebContainer WebContainers}.</p>
  */
public interface WebLayout {

  /** gets the region specified in the constraints object. Used e.g.
    * for setting the format of the region.
    * @param constraints specifies the region
    * @deprecated replaced by {@link #getArea(Object)}
    */
  WebTableCell getRegion( Object constraints );

  /** <p>returns that part of the WebContainer which has set this
    * WebLayout manager for which the specified constraint is set.</p> */
  Area getArea( Object constraint );

  /** returns a deep copy of this WebLayout (including all regions). */
  Object clone() throws CloneNotSupportedException;

  /** creates the layout for the WebContainer.
    * @param parent the WebContainer to layout.
    */
  void layoutWebContainer( WebContainer parent ) throws IOException;
  
  /** checks if the constraint parameter in the add method of a WebContainer
    * has the correct type.
    * @param constraint specifies the constraint to check
    */
  boolean checkConstraint( Object constraint );
}