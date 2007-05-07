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

package org.eclipse.ui.internal;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;


public class ViewSashContainer extends PartSashContainer {

public ViewSashContainer( final WorkbenchPage page, final Composite parent ) {
    super( "root layout container", page, parent );
  }
  

	public boolean isStackType(LayoutPart toTest) {
		return (toTest instanceof ViewStack);
	}
	
  protected Composite createParent( final Composite parentWidget ) {
    return parentWidget;
  }
  
  public Control getControl() {
    return parent;
  }

  public void replace( LayoutPart oldChild, LayoutPart newChild ) {
    if( !isChild( oldChild ) ) {
      return;
    }
    // Nasty hack: ensure that all views end up inside a tab folder.
    // Since the view title is provided by the tab folder, this ensures
    // that views don't get created without a title tab.
    if( newChild instanceof ViewPane ) {
      ViewStack folder = new ViewStack( page );
      folder.add( newChild );
      newChild = folder;
    }
    super.replace( oldChild, newChild );
  }

  /* (non-Javadoc)
   * @see org.eclipse.ui.internal.PartSashContainer#derefPart(org.eclipse.ui.internal.LayoutPart)
   */
  protected void derefPart(LayoutPart sourcePart) {
      page.getActivePerspective().getPresentation().derefPart(sourcePart);
  }
  
}
