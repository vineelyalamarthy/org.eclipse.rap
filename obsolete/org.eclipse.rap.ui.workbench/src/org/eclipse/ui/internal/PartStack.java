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

import java.util.*;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.*;
import org.eclipse.ui.internal.presentations.PresentablePart;
import org.eclipse.ui.internal.presentations.PresentationFactoryUtil;
import org.eclipse.ui.presentations.*;


public abstract class PartStack extends LayoutPart implements ILayoutContainer {

  protected int appearance = PresentationFactoryUtil.ROLE_VIEW;
  private AbstractPresentationFactory factory;
  private boolean isActive;
  private List children = new ArrayList( 3 );
  private List presentableParts = new ArrayList();
  private LayoutPart requestedCurrent;
  private LayoutPart current;
  private PresentablePart presentationCurrent;
  private boolean isMinimized;
  
  private DefaultStackPresentationSite presentationSite 
    = new DefaultStackPresentationSite()
  {

    public void close( final IPresentablePart part ) {
      PartStack.this.close( part );
    }

    public void close( final IPresentablePart[] parts ) {
      PartStack.this.close( parts );
    }

    public void dragStart( final IPresentablePart beingDragged,
                           final Point initialLocation,
                           final boolean keyboard )
    {
//      PartStack.this.dragStart( beingDragged, initialLocation, keyboard );
    }

    public void dragStart( final Point initialLocation, 
                           final boolean keyboard )
    {
//      PartStack.this.dragStart( null, initialLocation, keyboard );
    }

    public boolean isPartMoveable( final IPresentablePart part ) {
//      return PartStack.this.isMoveable( part );
      return false;
    }

    public void selectPart( final IPresentablePart toSelect ) {
      PartStack.this.presentationSelectionChanged( toSelect );
    }

    public boolean supportsState( final int state ) {
      return PartStack.this.supportsState( state );
    }

    public void setState( final int newState ) {
      PartStack.this.setState( newState );
    }

    public IPresentablePart getSelectedPart() {
      return PartStack.this.getSelectedPart();
    }

    public void addSystemActions( final IMenuManager menuManager ) {
//      PartStack.this.addSystemActions( menuManager );
    }

    public boolean isStackMoveable() {
//      return canMoveFolder();
      return false;
    }

    public void flushLayout() {
      PartStack.this.flushLayout();
    }

    public IPresentablePart[] getPartList() {
//      List parts = getPresentableParts();
//      return ( IPresentablePart[] )parts.toArray( new IPresentablePart[ parts.size() ] );
      return null;
    }
  };

  
  public PartStack( int appearance ) {
    this( appearance, null );
  }

  public PartStack( final int appearance, 
                    final AbstractPresentationFactory factory )
  {
    super( "PartStack" );
    this.appearance = appearance;
    this.factory = factory;
  }

  public PartPane getSelection() {
    if( current instanceof PartPane ) {
      return ( PartPane )current;
    }
    return null;
  }

  /**
   * @param parts
   */
  protected void close(IPresentablePart[] parts) {
      for (int idx = 0; idx < parts.length; idx++) {
          IPresentablePart part = parts[idx];

          close(part);
      }
  }
  
  /**
   * @param part
   */
  protected void close(IPresentablePart part) {
      if (!presentationSite.isCloseable(part)) {
          return;
      }

      LayoutPart layoutPart = getPaneFor(part);

      if (layoutPart != null && layoutPart instanceof PartPane) {
          PartPane viewPane = (PartPane) layoutPart;

          viewPane.doHide();
      }
  }
  
  public void setActive( boolean isActive ) {
    this.isActive = isActive;
    // Add all visible children to the presentation
    Iterator iter = children.iterator();
    while( iter.hasNext() ) {
      LayoutPart part = ( LayoutPart )iter.next();
      part.setContainer( isActive ? this : null );
    }
    for( Iterator iterator = presentableParts.iterator(); iterator.hasNext(); )
    {
      PresentablePart next = ( PresentablePart )iterator.next();
      next.enableInputs( isActive );
      next.enableOutputs( isActive );
    }
  }

  public void setActive( int activeState ) {
    if( activeState == StackPresentation.AS_ACTIVE_FOCUS ) {
      setMinimized( false );
    }
    presentationSite.setActive( activeState );
  }

  public void setMinimized( boolean minimized ) {
    if( minimized != isMinimized ) {
      isMinimized = minimized;
      refreshPresentationState();
    }
  }
  
  protected abstract boolean supportsState( int newState );
  
  protected AbstractPresentationFactory getFactory() {
    AbstractPresentationFactory result = factory;
    if( result == null ) {
      IWorkbenchWindow window = getPage().getWorkbenchWindow();
      WorkbenchWindow workbenchWindow = ( WorkbenchWindow )window;
      result = workbenchWindow.getWindowConfigurer().getPresentationFactory();
    }
    return result;
  }

  protected WorkbenchPage getPage() {
    WorkbenchWindow window = ( WorkbenchWindow )getWorkbenchWindow();
    WorkbenchPage result = null;
    if( window != null ) {
      result = ( WorkbenchPage )window.getActivePage();
    }
    return result;
  }

  protected StackPresentation getPresentation() {
    return presentationSite.getPresentation();
  }

  public void createControl( final Composite parent, 
                             final StackPresentation presentation )
  {
//    Assert.isTrue( isDisposed() );
    if( presentationSite.getPresentation() != null ) {
      return;
    }
    presentationSite.setPresentation( presentation );
    // Add all visible children to the presentation
    // Use a copy of the current set of children to avoid a
    // ConcurrentModificationException
    // if a part is added to the same stack while iterating over the children
    // (bug 78470)
    LayoutPart[] childParts = (org.eclipse.ui.internal.LayoutPart[] )children.toArray( new LayoutPart[ children.size() ] );
    for( int i = 0; i < childParts.length; i++ ) {
      LayoutPart part = childParts[ i ];
      showPart( part, null );
    }
//    if( savedPresentationState != null ) {
//      PresentationSerializer serializer = new PresentationSerializer( getPresentableParts() );
//      presentation.restoreState( serializer, savedPresentationState );
//    }
    Control ctrl = getPresentation().getControl();
    ctrl.setData( this );
    // We should not have a placeholder selected once we've created the widgetry
    if( requestedCurrent instanceof PartPlaceholder ) {
      requestedCurrent = null;
//      updateContainerVisibleTab();
    }
//    refreshPresentationSelection();
  }

  protected void add( LayoutPart newChild, Object cookie ) {
    children.add( newChild );
    // Fix for bug 78470:
//    if( !( newChild.getContainer() instanceof ContainerPlaceholder ) ) {
//      newChild.setContainer( this );
//    }
    showPart( newChild, cookie );
  }

  public boolean isDisposed() {
    return getPresentation() == null;
  }

  public void setSelection( final LayoutPart part ) {
    if( part != requestedCurrent ) {
      requestedCurrent = part;
    refreshPresentationSelection();
    }
  }

  public Composite getParent() {
    return getControl().getParent();
  }

  /**
   * Returns a list of IPresentablePart
   * 
   * @return
   */
  public List getPresentableParts() {
      return presentableParts;
  }
  
  public int computePreferredSize( boolean width,
                                   int availableParallel,
                                   int availablePerpendicular,
                                   int preferredParallel )
  {
    return getPresentation().computePreferredSize( width,
                                                   availableParallel,
                                                   availablePerpendicular,
                                                   preferredParallel );
  }

  public void setBounds( final Rectangle bounds ) {
    if( getPresentation() != null ) {
      getPresentation().setBounds( bounds );
    }
  }

  protected LayoutPart getPaneFor( final IPresentablePart part ) {
    if( part == null || !( part instanceof PresentablePart ) ) {
      return null;
    }
    return ( ( PresentablePart )part ).getPane();
  }

  public Control[] getTabList( LayoutPart part ) {
    if( part != null ) {
      IPresentablePart presentablePart = getPresentablePart( part );
      StackPresentation presentation = getPresentation();
      if( presentablePart != null && presentation != null ) {
        return presentation.getTabList( presentablePart );
      }
    }
    return new Control[ 0 ];
  }

  protected IPresentablePart getSelectedPart() {
    return presentationCurrent;
  }

  protected void setState( int newState ) {
    int oldState = presentationSite.getState();
    if( !supportsState( newState ) || newState == oldState ) {
      return;
    }
    boolean minimized = ( newState == IStackPresentationSite.STATE_MINIMIZED );
    setMinimized( minimized );
    if( newState == IStackPresentationSite.STATE_MAXIMIZED ) {
      requestZoomIn();
    } else if( oldState == IStackPresentationSite.STATE_MAXIMIZED ) {
      requestZoomOut();
    }
  }

  public int getSizeFlags( boolean horizontal ) {
    StackPresentation presentation = getPresentation();
    if( presentation != null ) {
      return presentation.getSizeFlags( horizontal );
    }
    return 0;
  }

  public int getState() {
    return presentationSite.getState();
  }

  public void setZoomed( boolean isZoomed ) {
    super.setZoomed( isZoomed );
    LayoutPart[] children = getChildren();
    for( int i = 0; i < children.length; i++ ) {
      LayoutPart next = children[ i ];
      next.setZoomed( isZoomed );
    }
    refreshPresentationState();
  }

  public boolean isZoomed() {
    ILayoutContainer container = getContainer();
    if( container != null ) {
      return container.childIsZoomed( this );
    }
    return false;
  }

  public void setVisible( boolean makeVisible ) {
    Control ctrl = getControl();
    boolean useShortcut = makeVisible || !isActive;
    if( !( ctrl == null || ctrl.isDisposed() )&& useShortcut ) {
      if( makeVisible == ctrl.getVisible() ) {
        return;
      }
    }
    if( makeVisible ) {
      for( Iterator iterator = presentableParts.iterator(); iterator.hasNext(); )
      {
        PresentablePart next = ( PresentablePart )iterator.next();
        next.enableInputs( isActive );
        next.enableOutputs( isActive );
      }
    }
    super.setVisible( makeVisible );
    StackPresentation presentation = getPresentation();
    if( presentation != null ) {
      presentation.setVisible( makeVisible );
    }
    if( !makeVisible ) {
      for( Iterator iterator = presentableParts.iterator(); iterator.hasNext(); )
      {
        PresentablePart next = ( PresentablePart )iterator.next();
        next.enableInputs( false );
      }
    }
  }


  // ///////////////////////////
  // interface ILayoutContainer
  
  public void createControl( final Composite parent ) {
    
    AbstractPresentationFactory factory = getFactory();
    StackPresentation presentation 
      = PresentationFactoryUtil.createPresentation( factory,
                                                    appearance,
                                                    parent,
                                                    presentationSite );
    createControl( parent, presentation );
    getControl().moveBelow( null );
  }

  public Control getControl() {
    StackPresentation presentation = getPresentation();
    if( presentation == null ) {
      return null;
    }
    return presentation.getControl();
  }

  public void add( final LayoutPart child ) {
    add( child, null );
  }

  public boolean allowsAdd( LayoutPart toAdd ) {
	return !isStandalone();
  }

  protected final boolean isStandalone() {
    return (    appearance == PresentationFactoryUtil.ROLE_STANDALONE 
             || appearance == PresentationFactoryUtil.ROLE_STANDALONE_NOTITLE );
  }

  public boolean allowsAutoFocus() {
    throw new UnsupportedOperationException();
  }

  public boolean childIsZoomed( final LayoutPart toTest ) {
    return isZoomed();
  }

  public boolean childObscuredByZoom( final LayoutPart toTest ) {
    return isObscuredByZoom();
  }

  public void childRequestZoomIn( final LayoutPart toZoom ) {
    super.childRequestZoomIn(toZoom);
    requestZoomIn();
  }

  public void childRequestZoomOut() {
    super.childRequestZoomOut();
    requestZoomOut();
  }

  public LayoutPart[] getChildren() {
    LayoutPart[] result = new LayoutPart[ children.size() ];
    children.toArray( result );
    return result;
  }

  /**
   * See IVisualContainer#remove
   */
  public void remove(LayoutPart child) {
      PresentablePart presentablePart = getPresentablePart(child);

      // Need to remove it from the list of children before notifying the presentation
      // since it may setVisible(false) on the part, leading to a partHidden notification,
      // during which findView must not find the view being removed.  See bug 60039. 
      children.remove(child);

      StackPresentation presentation = getPresentation();

      if (presentablePart != null && presentation != null) {
//          ignoreSelectionChanges = true;
          presentableParts .remove(presentablePart);
          presentation.removePart(presentablePart);
          presentablePart.dispose();
//          ignoreSelectionChanges = false;
      }

      if (!isDisposed()) {
          child.setContainer(null);
      }

      if (child == requestedCurrent) {
          updateContainerVisibleTab();
      }
  }

  /**
	 * Update the container to show the correct visible tab based on the
	 * activation list.
	 */
  private void updateContainerVisibleTab() {
      LayoutPart[] parts = getChildren();

      if (parts.length < 1) {
          setSelection(null);
          return;
      }

      PartPane selPart = null;
      int topIndex = 0;
      WorkbenchPage page = getPage();

      if (page != null) {
          IWorkbenchPartReference sortedPartsArray[] = page.getSortedParts();
          List sortedParts = Arrays.asList(sortedPartsArray);
          for (int i = 0; i < parts.length; i++) {
              if (parts[i] instanceof PartPane) {
                  IWorkbenchPartReference part = ((PartPane) parts[i])
                          .getPartReference();
                  int index = sortedParts.indexOf(part);
                  if (index >= topIndex) {
                      topIndex = index;
                      selPart = (PartPane) parts[i];
                  }
              }
          }

      }

      if (selPart == null) {
          List presentableParts = getPresentableParts();
          if (presentableParts.size() != 0) {
              IPresentablePart part = (IPresentablePart) getPresentableParts()
                      .get(0);

              selPart = (PartPane) getPaneFor(part);
          }
      }

      setSelection(selPart);
  }
  
  /**
   * See IVisualContainer#replace
   */
  public void replace(LayoutPart oldChild, LayoutPart newChild) {
      int idx = children.indexOf(oldChild);
      int numPlaceholders = 0;
      //subtract the number of placeholders still existing in the list 
      //before this one - they wont have parts.
      for (int i = 0; i < idx; i++) {
          if (children.get(i) instanceof PartPlaceholder) {
				numPlaceholders++;
			}
      }
      Integer cookie = new Integer(idx - numPlaceholders);
      children.add(idx, newChild);

      showPart(newChild, cookie);

      if (oldChild == requestedCurrent && !(newChild instanceof PartPlaceholder)) {
          setSelection(newChild);
      }

      remove(oldChild);
  }

  public void resizeChild( LayoutPart childThatChanged ) {
    throw new UnsupportedOperationException();
  }
  
  
  //////////////////
  // helping methods
  
  private void showPart( LayoutPart part, Object cookie ) {
    if( isDisposed() ) {
      return;
    }
    if( ( part instanceof PartPlaceholder ) ) {
      part.setContainer( this );
      return;
    }
    if( !( part instanceof PartPane ) ) {
//      WorkbenchPlugin.log( NLS.bind( WorkbenchMessages.PartStack_incorrectPartInFolder,
//                                     part.getID() ) );
    	Activator.log("incorrect part in folder:" + part.getID());
      return;
    }
    PartPane pane = ( PartPane )part;
    PresentablePart presentablePart
      = new PresentablePart( pane, getControl().getParent() );
    presentableParts.add( presentablePart );
    if( isActive ) {
      part.setContainer( this );
    }
    presentationSite.getPresentation().addPart( presentablePart, cookie );
    if( requestedCurrent == null ) {
      setSelection( part );
    }
    if( childObscuredByZoom( part ) ) {
      presentablePart.enableInputs( false );
    }
  }

  private void refreshPresentationSelection() {
    // If deferring UI updates, exit.
//    if( isDeferred() ) {
//      return;
//    }
    // If the presentation is already displaying the desired part, then there's nothing
    // to do.
    if( current == requestedCurrent ) {
      return;
    }
    StackPresentation presentation = getPresentation();
    if( presentation != null ) {
      presentationCurrent = getPresentablePart( requestedCurrent );
      if( !isDisposed() ) {
//        updateActions( presentationCurrent );
      }
      if( presentationCurrent != null && presentation != null ) {
        requestedCurrent.createControl( getParent() );
//        if( requestedCurrent.getControl().getParent() != getControl().getParent() )
//        {
//          requestedCurrent.reparent( getControl().getParent() );
//        }
        presentation.selectPart( presentationCurrent );
      }
      // Update the return value of getVisiblePart
      current = requestedCurrent;
//      fireInternalPropertyChange( PROP_SELECTION );
    }
  }

  private PresentablePart getPresentablePart( final LayoutPart pane ) {
    for( Iterator iter = presentableParts.iterator(); iter.hasNext(); ) {
      PresentablePart part = ( PresentablePart )iter.next();
      if( part.getPane() == pane ) {
        return part;
      }
    }
    return null;
  }

  private void presentationSelectionChanged( IPresentablePart newSelection ) {
    // Ignore selection changes that occur as a result of removing a part
//    if( ignoreSelectionChanges ) {
//      return;
//    }
    LayoutPart newPart = getPaneFor( newSelection );
    // This method should only be called on objects that are already in the layout
    Assert.isNotNull( newPart );
    if( newPart == requestedCurrent ) {
      return;
    }
    setSelection( newPart );
    if( newPart != null ) {
//      newPart.setFocus();
    }
  }

  private void refreshPresentationState() {
    if( isZoomed() ) {
      presentationSite.setPresentationState( IStackPresentationSite.STATE_MAXIMIZED );
    } else {
      boolean wasMinimized = ( presentationSite.getState() == IStackPresentationSite.STATE_MINIMIZED );
      if( isMinimized ) {
        presentationSite.setPresentationState( IStackPresentationSite.STATE_MINIMIZED );
      } else {
        presentationSite.setPresentationState( IStackPresentationSite.STATE_RESTORED );
      }
      if( isMinimized != wasMinimized ) {
        flushLayout();
        if( isMinimized ) {
          WorkbenchPage page = getPage();
          if( page != null ) {
            page.refreshActiveView();
          }
        }
      }
    }
  }
  
  /**
   * See LayoutPart#dispose
   */
  public void dispose() {

      if (isDisposed()) {
			return;
		}

//      savePresentationState();

      presentationSite.dispose();
      
      for (Iterator iter = presentableParts.iterator(); iter.hasNext();) {
          PresentablePart part = (PresentablePart) iter.next();
          
          part.dispose();
      }
      presentableParts.clear();
      
      presentationCurrent = null;
      current = null;
//      fireInternalPropertyChange(PROP_SELECTION);
  }
  
  public void findSashes(LayoutPart part, PartPane.Sashes sashes) {
      ILayoutContainer container = getContainer();

      if (container != null) {
          container.findSashes(this, sashes);
      }
  }
}
