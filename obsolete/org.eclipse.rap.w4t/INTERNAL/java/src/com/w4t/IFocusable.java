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

// Created on 16.12.2005
/**
 * <p>
 * Objcts implementing this interface are capable of receiving the <em>keyboard 
 * focus</em>.
 * </p>
 */
public interface IFocusable {

  /**
   * Sets whether this component should retrieve the focus during the next
   * render phase. Note that only a component of the component-tree could
   * get the focus. Setting the focus to one component means that another
   * component of the same component-tree may lose the focus. This also
   * implies that only components that are added to a component-tree
   * can actually retrieve the focus, therefore a call to setFocus on
   * a component that is not added to a component-tree will have no effect.
   */
  void setFocus( boolean focus );
  
  /** 
   * Returns whether this component has the focus on the component-tree
   * to which it is added. If it is not added to a component-tree it
   * returns false.
   */
  boolean hasFocus();
  
}
