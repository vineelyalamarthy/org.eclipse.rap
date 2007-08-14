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
package com.w4t.custom;

import com.w4t.IAction;

/**
 * <p>This interface defines what will be executed as a reaction to a 
 * menu item or a toolbar item being triggered.</p>
 * @see org.eclipse.rwt.IAction
 * @see org.eclipse.rwt.custom.CToolBar
 * @see org.eclipse.rwt.custom.CMenu
 */
public interface ICustomAction extends IAction {
  
  /**
   * <p>This method is called <em>before</em> the action is 
   * <code>execute</code>ed. The <code>commandId</code> is the one that the
   * menu item or toolbar item is associated with.</p>
   */
  void init( String commandId );
}
