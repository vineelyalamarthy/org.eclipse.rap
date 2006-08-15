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

/**
 * <p>The <code>IAction</code> interface represents the work that is
 * done as a reaction to a user-initiated command such as a click
 * on a menu item.</p>
 * <p>Subinterfaces may add methods that define the context the action will
 * be executed in.</p>
 */
public interface IAction {

  /**
   * <p>Executes this action. Each action implementation must define the 
   * steps needed to carry out this action.</p>
   */
  void execute();
}
