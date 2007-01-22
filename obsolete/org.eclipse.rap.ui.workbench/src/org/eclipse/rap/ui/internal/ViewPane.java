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

import org.eclipse.rap.jface.internal.provisional.action.IToolBarManager2;
import org.eclipse.rap.rwt.widgets.Composite;
import org.eclipse.rap.rwt.widgets.Control;
import org.eclipse.rap.ui.IViewReference;
import org.eclipse.rap.ui.IWorkbenchPartReference;
import org.eclipse.rap.ui.internal.provisional.presentations.IActionBarPresentationFactory;
import org.eclipse.rap.ui.presentations.StackPresentation;


public class ViewPane extends PartPane {

  private boolean hasFocus;
  private IToolBarManager2 isvToolBarMgr = null;
  
  public ViewPane( final IWorkbenchPartReference partReference,
                   final WorkbenchPage workbenchPage )
  {
    super( partReference, workbenchPage );
    WorkbenchWindow workbenchWindow = ( WorkbenchWindow )page.getWorkbenchWindow();
    IActionBarPresentationFactory actionBarPresentation = workbenchWindow.getActionBarPresentationFactory();
    isvToolBarMgr = actionBarPresentation.createViewToolBarManager();
  }

  protected void createTitleBar() {
    // Only do this once.
//    updateTitles();
    // Listen to title changes.
//    getPartReference().addPropertyListener( this );
//    createToolBars();
  }

  public void createControl( Composite parent ) {
    // Only do this once.
    if( getControl() != null && !getControl().isDisposed() ) {
      return;
    }
    super.createControl( parent );
  }

  public Control[] getTabList() {
    Control control = getControl();
    if( getContainer() instanceof ViewStack ) {
      ViewStack tf = ( ViewStack )getContainer();
      return tf.getTabList( this );
    }
    return new Control[]{
      control
    };
  }

  public void doHide() {
    throw new UnsupportedOperationException();
  }

  public Control getToolBar() {
    return null;
  }

  public boolean isCloseable() {
    Perspective perspective = page.getActivePerspective();
    if( perspective == null ) {
      // Shouldn't happen -- can't have a ViewStack without a
      // perspective
      return true;
    }
    return perspective.isCloseable( getViewReference() );
  }

  void shellActivated() {
    throw new UnsupportedOperationException();
  }

  void shellDeactivated() {
    throw new UnsupportedOperationException();
  }

  void setActive( boolean active ) {
    hasFocus = active;
    if( getContainer() instanceof PartStack ) {
      ( ( PartStack )getContainer() ).setActive( active
                                                       ? StackPresentation.AS_ACTIVE_FOCUS
                                                       : StackPresentation.AS_INACTIVE );
    }
  }

  public void showFocus( boolean inFocus ) {
    setActive( inFocus );
  }
  
  public IViewReference getViewReference() {
    return ( IViewReference )getPartReference();
  }

  public void setVisible( boolean makeVisible ) {
    super.setVisible( makeVisible );
    Control toolbar = internalGetToolbar();
    if( toolbar != null ) {
      boolean visible = makeVisible && toolbarIsVisible();
      toolbar.setVisible( visible );
    }
  }

  public boolean toolbarIsVisible() {
    IToolBarManager2 toolbarManager = getToolBarManager();
    if( toolbarManager == null ) {
      return false;
    }
    Control control = toolbarManager.getControl2();
    if( control == null || control.isDisposed() ) {
      return false;
    }
    return toolbarManager.getItemCount() > 0;
  }

  private Control internalGetToolbar() {
    if( isvToolBarMgr == null ) {
      return null;
    }
    return isvToolBarMgr.getControl2();
  }

  public IToolBarManager2 getToolBarManager() {
    return isvToolBarMgr;
  }
}
