/*******************************************************************************
 * Copyright (c) 2002-2006 Innoopract Informationssysteme GmbH. All rights
 * reserved. This program and the accompanying materials are made available
 * under the terms of the Eclipse Public License v1.0 which accompanies this
 * distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 * Contributors: Innoopract Informationssysteme GmbH - initial API and
 * implementation
 ******************************************************************************/
package org.eclipse.ui.internal;

import org.eclipse.ui.internal.presentations.PresentationFactoryUtil;
import org.eclipse.ui.presentations.AbstractPresentationFactory;

public class ViewStack extends PartStack {

  private WorkbenchPage page;
  private boolean allowStateChanges;

  public ViewStack( final WorkbenchPage page ) {
    this( page, true );
  }

  public ViewStack( final WorkbenchPage page, 
                    final boolean allowsStateChanges )
  {
    this( page, allowsStateChanges, PresentationFactoryUtil.ROLE_VIEW, null );
  }

  public ViewStack( final WorkbenchPage page,
                    final boolean allowsStateChanges,
                    final int appearance,
                    final AbstractPresentationFactory factory )
  {
    super( appearance, factory );
    this.page = page;
    setID( this.toString() );
    // Each folder has a unique ID so relative positioning is unambiguous.
    this.allowStateChanges = allowsStateChanges;
//    fastViewAction = new SystemMenuFastView( getPresentationSite() );
//    detachViewAction = new SystemMenuDetach( getPresentationSite() );
  }
  
  protected WorkbenchPage getPage() {
    return page;
  }
  
  
  ////////////////////////////
  // implementations PartStack
  
  protected boolean supportsState( int newState ) {
//    if( page.isFixedLayout() ) {
//      return false;
//    }
    return allowStateChanges;
  }
}
