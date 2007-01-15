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

package org.eclipse.rap.ui.internal;

import org.eclipse.rap.rwt.widgets.Composite;
import org.eclipse.rap.rwt.widgets.Control;


public class ViewSashContainer extends PartSashContainer {

  public ViewSashContainer( final WorkbenchPage page, final Composite parent ) {
    super( "root layout container", page, parent );
  }
  
  protected Composite createParent( final Composite parentWidget ) {
    return parentWidget;
  }
  
  public Control getControl() {
    return parent;
  }

}
