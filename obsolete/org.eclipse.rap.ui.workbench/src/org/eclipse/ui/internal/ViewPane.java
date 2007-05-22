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

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.internal.provisional.action.IToolBarManager2;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.MenuListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.internal.provisional.presentations.IActionBarPresentationFactory;
import org.eclipse.ui.presentations.IPresentablePart;
import org.eclipse.ui.presentations.StackPresentation;


public class ViewPane extends PartPane {

  private boolean hasFocus;
  private boolean hadViewMenu = false;
  private IToolBarManager2 isvToolBarMgr = null;
  private MenuManager isvMenuMgr;
  
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
    updateTitles();
    // Listen to title changes.
    getPartReference().addPropertyListener( this );
    createToolBars();
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

  /**
   * @see ViewActionBars
   */
  public MenuManager getMenuManager() {
    if( isvMenuMgr == null ) {
      isvMenuMgr = new PaneMenuManager();
    }
    return isvMenuMgr;
  }

  public void doHide() {
	  getPage().hideView(getViewReference());
  }

  public Control getToolBar() {
    if( !toolbarIsVisible() ) {
      return null;
    }
    return internalGetToolbar();
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

  public void setContainer( ILayoutContainer container ) {
    ILayoutContainer oldContainer = getContainer();
    if( hasFocus ) {
      if( oldContainer != null && oldContainer instanceof PartStack ) {
        ( ( PartStack )oldContainer ).setActive( StackPresentation.AS_INACTIVE );
      }
      if( container != null && container instanceof PartStack ) {
        ( ( PartStack )container ).setActive( StackPresentation.AS_ACTIVE_FOCUS );
      }
    }
    super.setContainer( container );
  }
  
  public void moveAbove( Control refControl ) {
    super.moveAbove( refControl );
    Control toolbar = internalGetToolbar();
    if( toolbar != null ) {
      toolbar.moveAbove( control );
    }
  }
  
  /**
   * Return if there should be a view menu at all.
   * There is no view menu if there is no menu manager,
   * no pull down button or if the receiver is an
   * inactive fast view.
   */
  public boolean hasViewMenu() {
    if( isvMenuMgr != null ) {
      return !isvMenuMgr.isEmpty();
    }
    return false;
  }

  public void showViewMenu( Point location ) {
    if( !hasViewMenu() ) {
      return;
    }
    // If this is a fast view, it may have been minimized. Do nothing in this
    // case.
//    if( isFastView() && ( page.getActiveFastView() != getViewReference() ) ) {
//      return;
//    }
    Menu aMenu = isvMenuMgr.createContextMenu( getControl().getParent() );
    aMenu.setLocation( location.x, location.y );
    aMenu.setVisible( true );
  }

  public void removeContributions() {
    super.removeContributions();
    if( isvMenuMgr != null ) {
      isvMenuMgr.removeAll();
    }
    if( isvToolBarMgr != null ) {
      isvToolBarMgr.removeAll();
    }
  }
  
  public void dispose() {
    super.dispose();
    /*
     * Bug 42684. The ViewPane instance has been disposed, but an attempt is
     * then made to remove focus from it. This happens because the ViewPane is
     * still viewed as the active part. In general, when disposed, the control
     * containing the titleLabel will also disappear (disposing of the
     * titleLabel). As a result, the reference to titleLabel should be dropped.
     */
    if( isvMenuMgr != null ) {
      isvMenuMgr.dispose();
    }
    if( isvToolBarMgr != null ) {
      isvToolBarMgr.dispose();
    }
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

  public IToolBarManager2 getToolBarManager() {
    return isvToolBarMgr;
  }

  private Control internalGetToolbar() {
    if( isvToolBarMgr == null ) {
      return null;
    }
    return isvToolBarMgr.getControl2();
  }

  private void createToolBars() {
    Composite parentControl = control;
    // ISV toolbar.
    // // 1GD0ISU: ITPUI:ALL - Dbl click on view tool cause zoom
    Control isvToolBar = isvToolBarMgr.createControl2( parentControl.getParent() );
    isvToolBarMgr.addPropertyChangeListener( new ISVPropListener( isvToolBar ) );
//    isvToolBar.addMouseListener( new MouseAdapter() {
//      public void mouseDoubleClick( MouseEvent event ) {
//        if( event.widget instanceof ToolBar ) {
//          if( ( ( ToolBar )event.widget ).getItem( new Point( event.x, event.y ) ) == null )
//          {
//            doZoom();
//          }
//        }
//      }
//    } );
//    isvToolBar.addListener( SWT.Activate, this );
    isvToolBar.moveAbove( control );
  }
  
  private void toolBarResized( Control toolBar, int newSize ) {
    Control toolbar = isvToolBarMgr.getControl2();
    if( toolbar != null ) {
      Control ctrl = getControl();
      boolean visible = ctrl != null && ctrl.isVisible() && toolbarIsVisible();
      toolbar.setVisible( visible );
    }
    firePropertyChange( IPresentablePart.PROP_TOOLBAR );
  }
  
  public void updateTitles() {
    firePropertyChange( IPresentablePart.PROP_TITLE );
  }
  
  public void updateActionBars() {
    if( isvMenuMgr != null ) {
      isvMenuMgr.updateAll( false );
    }
    if( isvToolBarMgr != null ) {
      isvToolBarMgr.update( false );
    }
  }
  
  ////////////////
  // Inner classes

  /**
   * Toolbar manager for the ISV toolbar.
   */
  private class ISVPropListener implements IPropertyChangeListener {

    private Control toolBar;

    public ISVPropListener( Control toolBar ) {
      this.toolBar = toolBar;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.util.IPropertyChangeListener#propertyChange(org.eclipse.jface.util.PropertyChangeEvent)
     */
    public void propertyChange( PropertyChangeEvent event ) {
      String property = event.getProperty();
      Integer newValue = ( Integer )event.getNewValue();
      if( IToolBarManager2.PROP_LAYOUT.equals( property ) ) {
        toolBarResized( toolBar, newValue != null
                                                 ? newValue.intValue()
                                                 : 0 );
        if( toolBar instanceof Composite ) {
          ( ( Composite )toolBar ).layout();
        } else {
          toolBar.getParent().layout();
        }
      }
    }
  }
  
  /**
   * Menu manager for view local menu.
   */
  class PaneMenuManager extends MenuManager {

    public PaneMenuManager() {
      super( "View Local Menu" ); //$NON-NLS-1$
    }

    protected void update( boolean force, boolean recursive ) {
      super.update( force, recursive );
      boolean hasMenu = !isEmpty();
      if( hasMenu != hadViewMenu ) {
        hadViewMenu = hasMenu;
        firePropertyChange( IPresentablePart.PROP_PANE_MENU );
      }
    }
  }
}
