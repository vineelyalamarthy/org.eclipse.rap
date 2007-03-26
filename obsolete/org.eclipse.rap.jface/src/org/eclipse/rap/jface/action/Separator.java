/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html Contributors:
 * IBM Corporation - initial API and implementation
 ******************************************************************************/
package org.eclipse.rap.jface.action;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.SWT;
import org.eclipse.rap.rwt.widgets.*;

/**
 * A separator is a special kind of contribution item which acts as a visual
 * separator and, optionally, acts as a group marker. Unlike group markers,
 * separators do have a visual representation for menus and toolbars.
 * <p>
 * This class may be instantiated; it is not intended to be subclassed outside
 * the framework.
 * </p>
 */
public class Separator extends AbstractGroupMarker {

  /**
   * Creates a separator which does not start a new group.
   */
  public Separator() {
    super();
  }

  /**
   * Creates a new separator which also defines a new group having the given
   * group name. The group name must not be <code>null</code> or the empty
   * string. The group name is also used as the item id.
   * 
   * @param groupName the group name of the separator
   */
  public Separator( String groupName ) {
    super( groupName );
  }

  /*
   * (non-Javadoc) Method declared on IContributionItem. Fills the given menu
   * with a SWT separator MenuItem.
   */
  public void fill( Menu menu, int index ) {
    if( index >= 0 ) {
      new MenuItem( menu, RWT.SEPARATOR, index );
    } else {
      new MenuItem( menu, RWT.SEPARATOR );
    }
  }

  /*
   * (non-Javadoc) Method declared on IContributionItem. Fills the given tool
   * bar with a SWT separator ToolItem.
   */
  public void fill( ToolBar toolbar, int index ) {
    if( index >= 0 ) {
      new ToolItem( toolbar, SWT.SEPARATOR, index );
    } else {
      new ToolItem( toolbar, SWT.SEPARATOR );
    }
  }

  /**
   * The <code>Separator</code> implementation of this
   * <code>IContributionItem</code> method returns <code>true</code>
   */
  public boolean isSeparator() {
    return true;
  }
}
