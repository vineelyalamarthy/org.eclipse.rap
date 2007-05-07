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

import java.text.MessageFormat;
import java.util.*;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.jface.util.SafeRunnable;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.*;
import org.eclipse.ui.internal.registry.PerspectiveDescriptor;
import org.eclipse.ui.internal.registry.ViewRegistry;
import org.eclipse.ui.presentations.IStackPresentationSite;

public class WorkbenchPage implements IWorkbenchPage {
  
  private final PerspectiveList perspectiveList = new PerspectiveList();
  private final WorkbenchWindow window;
  private final IAdaptable input;
  private final EditorAreaHelper editorPresentation;
  private final EditorManager editorMgr;
  private final Composite composite;
  private ViewFactory viewFactory;
  private PageSelectionService selectionService
    = new PageSelectionService( this );
  private WorkbenchPagePartList partList
    = new WorkbenchPagePartList( selectionService );
  private IWorkbenchPartReference partBeingActivated = null;
  private ActivationList activationList = new ActivationList();
  private IActionBars actionBars;
  
  private class ActivationList {

    // List of parts in the activation order (oldest first)
    List parts = new ArrayList();

    /*
     * Add/Move the active part to end of the list;
     */
    void setActive( IWorkbenchPart part ) {
      if( parts.size() <= 0 ) {
        return;
      }
      IWorkbenchPartReference ref = getReference( part );
      if( ref != null ) {
        if( ref == parts.get( parts.size() - 1 ) ) {
          return;
        }
        parts.remove( ref );
        parts.add( ref );
      }
    }

    /*
     * Ensures that the given part appears AFTER any other part in the same
     * container.
     */
    void bringToTop( IWorkbenchPartReference ref ) {
      ILayoutContainer targetContainer = getContainer( ref );
      int newIndex = lastIndexOfContainer( targetContainer );
      // New index can be -1 if there is no last index
      if( newIndex >= 0 && ref == parts.get( newIndex ) )
        return;
      parts.remove( ref );
      if( newIndex >= 0 )
        parts.add( newIndex, ref );
      else
        parts.add( ref );
    }

    /*
     * Returns the last (most recent) index of the given container in the
     * activation list, or returns -1 if the given container does not appear in
     * the activation list.
     */
    int lastIndexOfContainer( ILayoutContainer container ) {
      for( int i = parts.size() - 1; i >= 0; i-- ) {
        IWorkbenchPartReference ref = ( IWorkbenchPartReference )parts.get( i );
        ILayoutContainer cnt = getContainer( ref );
        if( cnt == container ) {
          return i;
        }
      }
      return -1;
    }

    /*
     * Add/Move the active part to end of the list;
     */
    void setActive( IWorkbenchPartReference ref ) {
      setActive( ref.getPart( true ) );
    }

    /*
     * Add the active part to the beginning of the list.
     */
    void add( IWorkbenchPartReference ref ) {
      if( parts.indexOf( ref ) >= 0 ) {
        return;
      }
      IWorkbenchPart part = ref.getPart( false );
      if( part != null ) {
        PartPane pane = ( ( PartSite )part.getSite() ).getPane();
//        if( pane instanceof MultiEditorInnerPane ) {
//          MultiEditorInnerPane innerPane = ( MultiEditorInnerPane )pane;
//          add( innerPane.getParentPane().getPartReference() );
//          return;
//        }
      }
      parts.add( 0, ref );
    }

    /*
     * Return the active part. Filter fast views.
     */
    IWorkbenchPart getActive() {
      if( parts.isEmpty() ) {
        return null;
      }
      return getActive( parts.size() - 1 );
    }

    /*
     * Return the previously active part. Filter fast views.
     */
    IWorkbenchPart getPreviouslyActive() {
      if( parts.size() < 2 ) {
        return null;
      }
      return getActive( parts.size() - 2 );
    }

    private IWorkbenchPart getActive( int start ) {
      IWorkbenchPartReference ref = getActiveReference( start, false );
      if( ref == null ) {
        return null;
      }
      return ref.getPart( true );
    }

    public IWorkbenchPartReference getActiveReference( boolean editorsOnly ) {
      return getActiveReference( parts.size() - 1, editorsOnly );
    }

    private IWorkbenchPartReference getActiveReference( int start,
                                                        boolean editorsOnly )
    {
      // First look for parts that aren't obscured by the current zoom state
      IWorkbenchPartReference nonObscured = getActiveReference( start,
                                                                editorsOnly,
                                                                true );
      if( nonObscured != null ) {
        return nonObscured;
      }
      // Now try all the rest of the parts
      return getActiveReference( start, editorsOnly, false );
    }

    /*
     * Find a part in the list starting from the end and filter fast views and
     * views from other perspectives.
     */
    private IWorkbenchPartReference getActiveReference( int start,
                                                        boolean editorsOnly,
                                                        boolean skipPartsObscuredByZoom )
    {
      IWorkbenchPartReference[] views = getViewReferences();
      for( int i = start; i >= 0; i-- ) {
        WorkbenchPartReference ref = ( WorkbenchPartReference )parts.get( i );
        if( editorsOnly && !( ref instanceof IEditorReference ) ) {
          continue;
        }
        // Skip parts whose containers have disabled auto-focus
        PartPane pane = ref.getPane();
//        if( pane != null ) {
//          if( !pane.allowsAutoFocus() ) {
//            continue;
//          }
//          if( skipPartsObscuredByZoom ) {
//            if( pane.isObscuredByZoom() ) {
//              continue;
//            }
//          }
//        }
        // Skip fastviews
//        if( ref instanceof IViewReference ) {
//          if( !( ( IViewReference )ref ).isFastView() ) {
//            for( int j = 0; j < views.length; j++ ) {
//              if( views[ j ] == ref ) {
//                return ref;
//              }
//            }
//          }
//        } else {
          return ref;
//        }
      }
      return null;
    }

    /*
     * Retuns the index of the part within the activation list. The higher the
     * index, the more recently it was used.
     */
    int indexOf( IWorkbenchPart part ) {
      IWorkbenchPartReference ref = getReference( part );
      if( ref == null ) {
        return -1;
      }
      return parts.indexOf( ref );
    }

    /*
     * Returns the index of the part reference within the activation list. The
     * higher the index, the more recent it was used.
     */
    int indexOf( IWorkbenchPartReference ref ) {
      return parts.indexOf( ref );
    }

    /*
     * Remove a part from the list
     */
    boolean remove( IWorkbenchPartReference ref ) {
      return parts.remove( ref );
    }

    /*
     * Returns the editors in activation order (oldest first).
     */
    private IEditorReference[] getEditors() {
      ArrayList editors = new ArrayList( parts.size() );
      for( Iterator i = parts.iterator(); i.hasNext(); ) {
        IWorkbenchPartReference part = ( IWorkbenchPartReference )i.next();
        if( part instanceof IEditorReference ) {
          editors.add( part );
        }
      }
      return (org.eclipse.ui.IEditorReference[] )editors.toArray( new IEditorReference[ editors.size() ] );
    }

    /*
     * Return a list with all parts (editors and views).
     */
    private IWorkbenchPartReference[] getParts() {
      IWorkbenchPartReference[] views = getViewReferences();
      ArrayList resultList = new ArrayList( parts.size() );
      for( Iterator iterator = parts.iterator(); iterator.hasNext(); ) {
        IWorkbenchPartReference ref = ( IWorkbenchPartReference )iterator.next();
        if( ref instanceof IViewReference ) {
          // Filter views from other perspectives
          for( int i = 0; i < views.length; i++ ) {
            if( views[ i ] == ref ) {
              resultList.add( ref );
              break;
            }
          }
        } else {
          resultList.add( ref );
        }
      }
      IWorkbenchPartReference[] result = new IWorkbenchPartReference[ resultList.size() ];
      return (org.eclipse.ui.IWorkbenchPartReference[] )resultList.toArray( result );
    }

    /*
     * Returns the topmost editor on the stack, or null if none.
     */
    IEditorPart getTopEditor() {
      IEditorReference editor = ( IEditorReference )getActiveReference( parts.size() - 1,
                                                                        true );
      if( editor == null ) {
        return null;
      }
      return editor.getEditor( true );
    }
  }

  
  public WorkbenchPage( final WorkbenchWindow window, 
                        final String perspId,
                        final IAdaptable input )
    throws WorkbenchException
  {
    this.window = window;
    this.input = input;

    // Create presentation.
    Composite parent = window.getPageComposite();
    composite = new Composite( parent, SWT.NONE );
    composite.setVisible( false );
    parent.layout();

    editorPresentation = new EditorAreaHelper( this );
    editorMgr = new EditorManager( window, this, editorPresentation );
    
    if( perspId != null ) {
      Workbench workbench = Workbench.getInstance();
      IPerspectiveRegistry registry = workbench.getPerspectiveRegistry();
      PerspectiveDescriptor description
        = ( PerspectiveDescriptor )registry.findPerspectiveWithId( perspId );
      if( description == null ) {
        // TODO: [fappel] NLS.bind
        String txt =   "Unable to create perspective ''{0}''.  "
                     + "There is no corresponding perspective extension.";
        String msg = MessageFormat.format( txt, new Object[] { perspId } );
        throw new WorkbenchException( msg );
      }
      Perspective persp = findPerspective( description );
      if( persp == null ) {
        persp = createPerspective( description, true );
      }
      perspectiveList.setActive( persp );
//      window.firePerspectiveActivated( this, description );
    }
  }
  
  public Perspective findPerspective( final IPerspectiveDescriptor desc ) {
    Perspective result = null;
    Iterator iterator = perspectiveList.iterator();
    while( result == null && iterator.hasNext() ) {
      Perspective perspective = ( Perspective )iterator.next();
      if( desc.getId().equals( perspective.getDesc().getId() ) ) {
        result = perspective;
      }
    }
    return result;
  }

  public EditorAreaHelper getEditorPresentation() {
    return editorPresentation;
  }

  public ViewFactory getViewFactory() {
    if( viewFactory == null ) {
      viewFactory = new ViewFactory( this, ViewRegistry.getInstance() );
    }
    return viewFactory;
  }

  public Composite getClientComposite() {
    return composite;
  }
  
  public Perspective getActivePerspective() {
    return perspectiveList.getActive();
  }
  
  protected void onActivate() {
    composite.setVisible( true );
    Perspective persp = getActivePerspective();
    if( persp != null ) {
      persp.onActivate();
      updateVisibility( null, persp );
    }
  }

  public void requestActivation( IWorkbenchPart part ) {
    // Sanity check.
//    if( !certifyPart( part ) ) {
//      return;
//    }
//    if( part instanceof MultiEditor ) {
//      part = ( ( MultiEditor )part ).getActiveEditor();
//    }
    // Real work.
    setActivePart( part );
  }

  void partAdded( WorkbenchPartReference ref ) {
//    activationList.add( ref );
//    partList.addPart( ref );
//    updateActivePart();
  }

  public IWorkbenchPart getActivePart() {
    return partList.getActivePart();
  }

  void refreshActiveView() {
    updateActivePart();
  }

  IWorkbenchPartReference[] getOpenParts() {
    IWorkbenchPartReference[] refs = getAllParts();
    List result = new ArrayList();
    for( int i = 0; i < refs.length; i++ ) {
      IWorkbenchPartReference reference = refs[ i ];
      IWorkbenchPart part = reference.getPart( false );
      if( part != null ) {
        result.add( reference );
      }
    }
    return (org.eclipse.ui.IWorkbenchPartReference[] )result.toArray( new IWorkbenchPartReference[ result.size() ] );
  }

  IWorkbenchPartReference[] getAllParts() {
    IViewReference[] views = viewFactory.getViews();
    IEditorReference[] editors = getEditorReferences();
    IWorkbenchPartReference[] result
      = new IWorkbenchPartReference[ views.length + editors.length ];
    int resultIdx = 0;
    for( int i = 0; i < views.length; i++ ) {
      result[ resultIdx++ ] = views[ i ];
    }
    for( int i = 0; i < editors.length; i++ ) {
      result[ resultIdx++ ] = editors[ i ];
    }
    return result;
  }

  public IEditorReference[] getEditorReferences() {
//    return editorPresentation.getEditors();
    // TODO [fappel] reasonable implementation
    return new IEditorReference[ 0 ];
  }
  
  public boolean isPartVisible( IWorkbenchPartReference partReference ) {
    throw new UnsupportedOperationException();
  }

  int getState( IWorkbenchPartReference ref ) {
    Perspective persp = getActivePerspective();
    if( persp == null ) {
      return IStackPresentationSite.STATE_RESTORED;
    }
    PartPane pane = ( ( WorkbenchPartReference )ref ).getPane();
//    if( ref instanceof IViewReference
//        && persp.isFastView( ( IViewReference )ref ) )
//    {
//      return persp.getFastViewState();
//    }
    PartStack parent = ( ( PartStack )pane.getContainer() );
    if( parent != null ) {
      return parent.getState();
    }
    return IStackPresentationSite.STATE_RESTORED;
  }

  public void setState( IWorkbenchPartReference ref, int newState ) {
    Perspective persp = getActivePerspective();
    if( persp == null ) {
      return;
    }
    PartPane pane = ( ( WorkbenchPartReference )ref ).getPane();
    // If target part is detached fire the zoom event. Note this doesn't
    // actually cause any changes in size and is required to support
    // intro state changes. We may want to introduce the notion of a zoomed
    // (fullscreen) detached view at a later time.
//    if( !pane.isDocked() ) {
//      pane.setZoomed( newState == IStackPresentationSite.STATE_MAXIMIZED );
//      return;
//    }
//    if( ref instanceof IViewReference
//        && persp.isFastView( ( IViewReference )ref ) )
//    {
//      persp.setFastViewState( newState );
//      return;
//    }
    boolean wasZoomed = isZoomed();
    boolean isZoomed = newState == IStackPresentationSite.STATE_MAXIMIZED;
    // Update zoom status.
    if( wasZoomed && !isZoomed ) {
      zoomOut();
    } else if( !wasZoomed && isZoomed ) {
      persp.getPresentation().zoomIn( ref );
      activate( ref.getPart( true ) );
    }
    PartStack parent = ( ( PartStack )pane.getContainer() );
    if( parent != null ) {
      parent.setMinimized( newState == IStackPresentationSite.STATE_MINIMIZED );
    }
  }

  public boolean isZoomed() {
    Perspective persp = getActivePerspective();
    if( persp == null ) {
      return false;
    }
    if( persp.getPresentation() == null ) {
      return false;
    }
    return persp.getPresentation().isZoomed();
  }

  public void zoomOut() {
    Perspective persp = getActivePerspective();
    if( persp != null ) {
      persp.getPresentation().zoomOut();
    }
  }



  // ////////////////////////////
  // interfact ISelectionService
  
  public void addPostSelectionListener( ISelectionListener listener ) {
    throw new UnsupportedOperationException();
  }
  
  public void addPostSelectionListener( String partId, ISelectionListener listener ) {
    throw new UnsupportedOperationException();
  }
  
  public void addSelectionListener( ISelectionListener listener ) {
    throw new UnsupportedOperationException();
  }
  
  public void addSelectionListener( String partId, ISelectionListener listener ) {
    throw new UnsupportedOperationException();
  }
  
  public ISelection getSelection() {
    throw new UnsupportedOperationException();
  }
  
  public ISelection getSelection( String partId ) {
    throw new UnsupportedOperationException();
  }
  
  public void removePostSelectionListener( ISelectionListener listener ) {
    throw new UnsupportedOperationException();
  }
  
  public void removePostSelectionListener( String partId, ISelectionListener listener ) {
    throw new UnsupportedOperationException();
  }
  
  public void removeSelectionListener( ISelectionListener listener ) {
    throw new UnsupportedOperationException();
  }
  
  public void removeSelectionListener( String partId, ISelectionListener listener ) {
    throw new UnsupportedOperationException();
  }
  
  
  /////////////////////////
  // interface IPartService
  
  public void addPartListener( final IPartListener listener ) {
    partList.getPartService().addPartListener( listener );
  }
  
  public void addPartListener( final IPartListener2 listener ) {
    partList.getPartService().addPartListener( listener );
  }
  
  public void removePartListener( final IPartListener listener ) {
    partList.getPartService().removePartListener( listener );
  }
  
  public void removePartListener( final IPartListener2 listener ) {
    partList.getPartService().removePartListener( listener );
  }
  
  public IWorkbenchPartReference getActivePartReference() {
    return partList.getActivePartReference();
  }
  
  
  ///////////////////////////
  // interface IWorkbenchPage
  
  public boolean isPartVisible( IWorkbenchPart part ) {
    PartPane pane = getPane(part);
    return pane != null && pane.getVisible();
  }

  public void toggleZoom( IWorkbenchPartReference ref ) {
    int oldState = getState( ref );
    boolean shouldZoom = oldState != IStackPresentationSite.STATE_MAXIMIZED;
    int newState = shouldZoom
                             ? IStackPresentationSite.STATE_MAXIMIZED
                             : IStackPresentationSite.STATE_RESTORED;
    setState( ref, newState );
  }

  /*
   * Returns the parts in activation order (oldest first).
   */
  public IWorkbenchPartReference[] getSortedParts() {
      return activationList.getParts();
  }
  
  public IWorkbenchPartReference getReference( IWorkbenchPart part ) {
    if( part == null ) {
      return null;
    }
    IWorkbenchPartSite site = part.getSite();
    if( !( site instanceof PartSite ) ) {
      return null;
    }
    PartSite partSite = ( ( PartSite )site );
    PartPane pane = partSite.getPane();
//    if( pane instanceof MultiEditorInnerPane ) {
//      MultiEditorInnerPane innerPane = ( MultiEditorInnerPane )pane;
//      return innerPane.getParentPane().getPartReference();
//    }
    return partSite.getPartReference();
  }

  public String getLabel() {
    return "WorkbenchPage getLabel() is not implemented yet.";
  }
  
  public IViewReference[] getViewReferences() {
    Perspective persp = getActivePerspective();
    if( persp != null ) {
      return persp.getViewReferences();
    } else {
      return new IViewReference[ 0 ];
    }
  }

  public IWorkbenchWindow getWorkbenchWindow() {
    return window;
  }

  public IViewPart findView( String id ) {
    IViewReference ref = findViewReference( id );
    if( ref == null ) {
      return null;
    }
    return ref.getView( true );
  }

  public IViewReference findViewReference( String viewId ) {
    return findViewReference( viewId, null );
  }

  public IViewReference findViewReference( String viewId, String secondaryId ) {
    Perspective persp = getActivePerspective();
    if( persp == null ) {
      return null;
    }
    return persp.findView( viewId, secondaryId );
  }

  // ////////////////
  // helping methods
  
  private ILayoutContainer getContainer( IWorkbenchPart part ) {
    PartPane pane = getPane( part );
    if( pane == null ) {
      return null;
    }
    return pane.getContainer();
  }

  private ILayoutContainer getContainer( IWorkbenchPartReference part ) {
    PartPane pane = getPane( part );
    if( pane == null ) {
      return null;
    }
    return pane.getContainer();
  }

  private PartPane getPane( IWorkbenchPart part ) {
    if( part == null ) {
      return null;
    }
    return getPane( getReference( part ) );
  }

  private PartPane getPane( IWorkbenchPartReference part ) {
    if( part == null ) {
      return null;
    }
    return ( ( WorkbenchPartReference )part ).getPane();
  }

  public IActionBars getActionBars() {
    if( actionBars == null ) {
      actionBars = new WWinActionBars( window );
    }
    return actionBars;
  }
  
  private void deactivatePart( IWorkbenchPart part ) {
    if( part != null ) {
      PartSite site = ( PartSite )part.getSite();
      site.getPane().showFocus( false );
    }
  }
  
  private void setActivePart( IWorkbenchPart newPart ) {
    // Optimize it.
    if( getActivePart() == newPart ) {
      return;
    }
    if( partBeingActivated != null ) {
//      if( partBeingActivated.getPart( false ) != newPart ) {
//        WorkbenchPlugin.log( new RuntimeException( NLS.bind( "WARNING: Prevented recursive attempt to activate part {0} while still in the middle of activating part {1}", //$NON-NLS-1$
//                                                             getId( newPart ),
//                                                             getId( partBeingActivated ) ) ) );
//      }
      return;
    }
    // No need to change the history if the active editor is becoming the
    // active part
//    String label = null; // debugging only
//    if( UIStats.isDebugging( UIStats.ACTIVATE_PART ) ) {
//      label = newPart != null
//                             ? newPart.getTitle()
//                             : "none"; //$NON-NLS-1$
//    }
    try {
      IWorkbenchPartReference partref = getReference( newPart );
      IWorkbenchPartReference realPartRef = null;
      if( newPart != null ) {
        IWorkbenchPartSite site = newPart.getSite();
        if( site instanceof PartSite ) {
          realPartRef = ( ( PartSite )site ).getPane().getPartReference();
        }
      }
      partBeingActivated = realPartRef;
//      UIStats.start( UIStats.ACTIVATE_PART, label );
      // Notify perspective. It may deactivate fast view.
      Perspective persp = getActivePerspective();
      if( persp != null ) {
        persp.partActivated( newPart );
      }
      // Deactivate old part
      IWorkbenchPart oldPart = getActivePart();
      if( oldPart != null ) {
        deactivatePart( oldPart );
      }
      // Set active part.
      if( newPart != null ) {
        activationList.setActive( newPart );
//        if( newPart instanceof IEditorPart ) {
//          makeActiveEditor( ( IEditorReference )realPartRef );
//        }
      }
      activatePart( newPart );
//      actionSwitcher.updateActivePart( newPart );
      partList.setActivePart( partref );
    } finally {
      partBeingActivated = null;
//      Object blame = newPart == null
//                                    ? ( Object )this
//                                    : newPart;
//      UIStats.end( UIStats.ACTIVATE_PART, blame, label );
    }
  }

  /**
   * See IPerspective
   */
  public void hideView(IViewPart view) {
      hideView((IViewReference)getReference(view));
  }
  
  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.ui.IWorkbenchPage#hideView(org.eclipse.ui.IViewReference)
   */
  public void hideView(IViewReference ref) {
      
      // Sanity check.
      if (ref == null) {
			return;
		}

      Perspective persp = getActivePerspective();
      if (persp == null) {
			return;
		}

      boolean promptedForSave = false;
      IViewPart view = ref.getView(false);
      if (view != null) {

          if (!certifyPart(view)) {
              return;
          }
          
          // Confirm.
//  		if (view instanceof ISaveablePart) {
//  			ISaveablePart saveable = (ISaveablePart)view;
//  			if (saveable.isSaveOnCloseNeeded()) {
//  				IWorkbenchWindow window = view.getSite().getWorkbenchWindow();
//  				boolean success = EditorManager.saveAll(Collections.singletonList(view), true, true, false, window);
//  				if (!success) {
//  					// the user cancelled.
//  					return;
//  				}
//  				promptedForSave = true;
//  			}
//  		}
      }
      
//      int refCount = getViewFactory().getReferenceCount(ref);
//      SaveablesList saveablesList = null;
//      Object postCloseInfo = null;
//      if (refCount == 1) {
//      	IWorkbenchPart actualPart = ref.getPart(false);
//      	if (actualPart != null) {
//				saveablesList = (SaveablesList) actualPart
//						.getSite().getService(ISaveablesLifecycleListener.class);
//				postCloseInfo = saveablesList.preCloseParts(Collections
//						.singletonList(actualPart), !promptedForSave, this
//						.getWorkbenchWindow());
//				if (postCloseInfo==null) {
//					// cancel
//					return;
//				}
//			}
//      }
      
      // Notify interested listeners before the hide
      // XXX: [bm] do we have perspective listeners? 
//      window.firePerspectiveChanged(this, persp.getDesc(), ref,
//              CHANGE_VIEW_HIDE);

      PartPane pane = getPane(ref);
      pane.setInLayout(false);
      
      updateActivePart();
      
//      if (saveablesList != null) {
//      	saveablesList.postClose(postCloseInfo);
//      }

      // Hide the part.
      persp.hideView(ref);

      // Notify interested listeners after the hide
//    XXX: [bm] do we have perspective listeners? 
//      window.firePerspectiveChanged(this, getPerspective(), CHANGE_VIEW_HIDE);
  }
  
  public IViewPart showView(String viewId) throws PartInitException {
		return showView(viewId, null, VIEW_ACTIVATE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchPage#showView(java.lang.String,
	 *      java.lang.String, int)
	 */
	public IViewPart showView(final String viewID, final String secondaryID,
			final int mode) throws PartInitException {

		if (secondaryID != null) {
			if (secondaryID.length() == 0
					|| secondaryID.indexOf(ViewFactory.ID_SEP) != -1) {
				throw new IllegalArgumentException("bad secondaryId");
			}
		}
		if (!certifyMode(mode)) {
			throw new IllegalArgumentException("illegal view mode");
		}

		// Run op in busy cursor.
		final Object[] result = new Object[1];
		BusyIndicator.showWhile(null, new Runnable() {
			public void run() {
				try {
					result[0] = busyShowView(viewID, secondaryID, mode);
				} catch (PartInitException e) {
					result[0] = e;
				}
			}
		});
		if (result[0] instanceof IViewPart) {
			return (IViewPart) result[0];
		} else if (result[0] instanceof PartInitException) {
			throw (PartInitException) result[0];
		} else {
			throw new PartInitException("Abnormal Workbench Condition");
		}
	}
	
    /**
	 * Shows a view.
	 * 
	 * Assumes that a busy cursor is active.
	 */
	private IViewPart busyShowView(String viewID, String secondaryID, int mode)
			throws PartInitException {
		Perspective persp = getActivePerspective();
		if (persp == null) {
			return null;
		}

		// If this view is already visible just return.
		IViewReference ref = persp.findView(viewID, secondaryID);
		IViewPart view = null;
		if (ref != null) {
			view = ref.getView(true);
		}
		if (view != null) {
			busyShowView(view, mode);
			return view;
		}

		// Show the view.
		view = persp.showView(viewID, secondaryID);
		if (view != null) {
			busyShowView(view, mode);

			IWorkbenchPartReference partReference = getReference(view);
			PartPane partPane = getPane(partReference);
			partPane.setInLayout(true);

			// XXX: [bm] do we have perspective listeners?
			// window.firePerspectiveChanged(this, getPerspective(),
			// partReference, CHANGE_VIEW_SHOW);
			// window.firePerspectiveChanged(this, getPerspective(),
			// CHANGE_VIEW_SHOW);
		}
		return view;
	}

    /**
     * This is called by child objects after a part has been added to the page.
     * The part will be queued for disposal after all listeners have been notified
     */
    /* package */ void partRemoved(WorkbenchPartReference ref) {
        activationList.remove(ref);
        disposePart(ref);
    }
    
    private void disposePart(WorkbenchPartReference ref) {
//        if (isDeferred()) {
//            pendingDisposals.add(ref);
//        } else {
            partList.removePart(ref);
//            ref.dispose();
//        }
    }
    
    /**
     * Returns the perspective.
     */
    public IPerspectiveDescriptor getPerspective() {
//        if (deferredActivePersp != null) {
//			return deferredActivePersp;
//		}
        Perspective persp = getActivePerspective();
        if (persp != null) {
			return persp.getDesc();
		} else {
			return null;
		}
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.IWorkbenchPage#getShowViewShortcuts()
     */
    public String[] getShowViewShortcuts() {
        Perspective persp = getActivePerspective();
        if (persp == null) {
            return new String[0];
        }
        return persp.getShowViewShortcuts();
    }
    
    /*
	 * Performs showing of the view in the given mode.
	 */
	private void busyShowView(IViewPart part, int mode) {
		if (mode == VIEW_ACTIVATE) {
			activate(part);
		} else if (mode == VIEW_VISIBLE) {
			IWorkbenchPartReference ref = getActivePartReference();
			// if there is no active part or it's not a view, bring to top
			if (ref == null || !(ref instanceof IViewReference)) {
				bringToTop(part);
			} else {
				// otherwise check to see if the we're in the same stack as the
				// active view
				IViewReference activeView = (IViewReference) ref;
				IViewReference[] viewStack = getViewReferenceStack(part);
				for (int i = 0; i < viewStack.length; i++) {
					if (viewStack[i].equals(activeView)) {
						return;
					}
				}
				bringToTop(part);
			}
		}
	}
    
    /**
     * Find the stack of view references stacked with this view part.
     * 
     * @param part the part
     * @return the stack of references
     * @since 3.0
     */
    private IViewReference[] getViewReferenceStack(IViewPart part) {
        // Sanity check.
        Perspective persp = getActivePerspective();
        if (persp == null || !certifyPart(part)) {
			return null;
		}

        ILayoutContainer container = ((PartSite) part.getSite()).getPane()
                .getContainer();
        if (container instanceof ViewStack) {
            ViewStack folder = (ViewStack) container;
            final ArrayList list = new ArrayList(folder.getChildren().length);
            for (int i = 0; i < folder.getChildren().length; i++) {
                LayoutPart layoutPart = folder.getChildren()[i];
                if (layoutPart instanceof ViewPane) {
                    IViewReference view = ((ViewPane) layoutPart)
                            .getViewReference();
                    if (view != null) {
						list.add(view);
					}
                }
            }

            // sort the list by activation order (most recently activated first)
            Collections.sort(list, new Comparator() {
                public int compare(Object o1, Object o2) {
                    int pos1 = (-1)
                            * activationList
                                    .indexOf((IWorkbenchPartReference) o1);
                    int pos2 = (-1)
                            * activationList
                                    .indexOf((IWorkbenchPartReference) o2);
                    return pos1 - pos2;
                }
            });

            return (IViewReference[]) list.toArray(new IViewReference[list
                    .size()]);
        }

        return new IViewReference[] { (IViewReference) getReference(part) };
    }
    
    /**
     * Returns whether a part exists in the current page.
     */
    private boolean certifyPart(IWorkbenchPart part) {
        //Workaround for bug 22325
        if (part != null && !(part.getSite() instanceof PartSite)) {
			return false;
		}

//      TODO: [bm] editorsparts? 
//        if (part instanceof IEditorPart) {
//            IEditorReference ref = (IEditorReference) getReference(part);
//            return ref != null && getEditorManager().containsEditor(ref);
//        }
        if (part instanceof IViewPart) {
            Perspective persp = getActivePerspective();
            return persp != null && persp.containsView((IViewPart) part);
        }
        return false;
    }
    
    /**
	 * Moves a part forward in the Z order of a perspective so it is visible. If
	 * the part is in the same stack as the active part, the new part is
	 * activated.
	 * 
	 * @param part
	 *            the part to bring to move forward
	 */
	public void bringToTop(IWorkbenchPart part) {
		// Sanity check.
		Perspective persp = getActivePerspective();
		if (persp == null || !certifyPart(part)) {
			return;
		}

//		String label = null; // debugging only
//		if (UIStats.isDebugging(UIStats.BRING_PART_TO_TOP)) {
//			label = part != null ? part.getTitle() : "none"; //$NON-NLS-1$
//		}

		try {
//			UIStats.start(UIStats.BRING_PART_TO_TOP, label);

			// TODO: [bm] editorparts?
			IWorkbenchPartReference ref = getReference(part);
//			ILayoutContainer activeEditorContainer = getContainer(getActiveEditor());
			ILayoutContainer activePartContainer = getContainer(getActivePart());
			ILayoutContainer newPartContainer = getContainer(part);

			if (newPartContainer == activePartContainer) {
				makeActive(ref);
//			} else if (newPartContainer == activeEditorContainer) {
//				if (ref instanceof IEditorReference) {
//					if (part != null) {
//						IWorkbenchPartSite site = part.getSite();
//						if (site instanceof PartSite) {
//							ref = ((PartSite) site).getPane()
//									.getPartReference();
//						}
//					}
//					makeActiveEditor((IEditorReference) ref);
//				} else {
//					makeActiveEditor(null);
//				}
			} else {
				internalBringToTop(ref);
				if (ref != null) {
					partList.firePartBroughtToTop(ref);
				}
			}
		} finally {
//			UIStats.end(UIStats.BRING_PART_TO_TOP, part, label);
		}
	}
    
	/**
	 * @param mode
	 *            the mode to test
	 * @return whether the mode is recognized
	 * @since 3.0
	 */
	private boolean certifyMode(int mode) {
		switch (mode) {
		case VIEW_ACTIVATE:
		case VIEW_VISIBLE:
		case VIEW_CREATE:
			return true;
		default:
			return false;
		}
	}

private void updateActivePart() {
// if( isDeferred() ) {
// return;
// }
    IWorkbenchPartReference oldActivePart = partList.getActivePartReference();
    IWorkbenchPartReference oldActiveEditor = partList.getActiveEditorReference();
    IWorkbenchPartReference newActivePart = null;
    IEditorReference newActiveEditor = null;
//    if( !window.isClosing() ) {
      // If an editor is active, try to keep an editor active
      if( oldActivePart == oldActiveEditor ) {
        newActiveEditor = ( IEditorReference )activationList.getActiveReference( true );
        newActivePart = newActiveEditor;
        if( newActivePart == null ) {
          // Only activate a non-editor if there's no editors left
          newActivePart = activationList.getActiveReference( false );
        }
      } else {
        // If a non-editor is active, activate whatever was activated most
        // recently
        newActivePart = activationList.getActiveReference( false );
        if( newActivePart instanceof IEditorReference ) {
          // If that happens to be an editor, make it the active editor as well
          newActiveEditor = ( IEditorReference )newActivePart;
        } else {
          // Otherwise, select whatever editor was most recently active
          newActiveEditor = ( IEditorReference )activationList.getActiveReference( true );
        }
      }
//    }
//    if( newActiveEditor != oldActiveEditor ) {
//      makeActiveEditor( newActiveEditor );
//    }
    if( newActivePart != oldActivePart ) {
      makeActive( newActivePart );
    }
  }

  private void makeActive( IWorkbenchPartReference ref ) {
    if( ref == null ) {
      setActivePart( null );
    } else {
      IWorkbenchPart newActive = ref.getPart( true );
      if( newActive == null ) {
        setActivePart( null );
      } else {
        activate( newActive );
      }
    }
  }

  public void activate( IWorkbenchPart part ) {
    // Sanity check.
//    if( !certifyPart( part ) ) {
//      return;
//    }
//    if( window.isClosing() ) {
//      return;
//    }
    // If zoomed, unzoom.
//    zoomOutIfNecessary( part );
//    if( part instanceof MultiEditor ) {
//      part = ( ( MultiEditor )part ).getActiveEditor();
//    }
    // Activate part.
    // if (window.getActivePage() == this) {
    IWorkbenchPartReference ref = getReference( part );
    internalBringToTop( ref );
    setActivePart( part );
  }

  private boolean internalBringToTop( IWorkbenchPartReference part ) {
    boolean broughtToTop = false;
    // Move part.
    if( part instanceof IEditorReference ) {
      ILayoutContainer container = getContainer( part );
      if( container instanceof PartStack ) {
        PartStack stack = ( PartStack )container;
        PartPane newPart = getPane( part );
        if( stack.getSelection() != newPart ) {
          stack.setSelection( newPart );
        }
        broughtToTop = true;
      }
    } else if( part instanceof IViewReference ) {
      Perspective persp = getActivePerspective();
      if( persp != null ) {
        broughtToTop = persp.bringToTop( ( IViewReference )part );
      }
    }
    // Ensure that this part is considered the most recently activated part
    // in this stack
    activationList.bringToTop( part );
    return broughtToTop;
  }

  private void activatePart( final IWorkbenchPart part ) {
// SafeRunner.run( new SafeRunnable(
// WorkbenchMessages.WorkbenchPage_ErrorActivatingView )
    SafeRunner.run( 
      new SafeRunnable( "An error has occurred when activating this view" )
    {
      public void run() {
        if( part != null ) {
          // part.setFocus();
          PartPane pane = getPane( part );
          pane.setFocus();
          PartSite site = ( PartSite )part.getSite();
          pane.showFocus( true );
          updateTabList( part );
          SubActionBars bars = ( SubActionBars )site.getActionBars();
          bars.partChanged( part );
        }
      }
    } );
  }

  private void updateTabList( IWorkbenchPart part ) {
    PartSite site = ( PartSite )part.getSite();
    PartPane pane = site.getPane();
    if( pane instanceof ViewPane ) {
      ViewPane viewPane = ( ViewPane )pane;
      Control[] tabList = viewPane.getTabList();
//      if( !pane.isDocked() ) {
//        viewPane.getControl().getShell().setTabList( tabList );
//      } else {
//        getClientComposite().setTabList( tabList );
//      }
//    } else if( pane instanceof EditorPane ) {
//      EditorSashContainer ea
//        = ( ( EditorPane )pane ).getWorkbook().getEditorArea();
//      ea.updateTabList();
//      getClientComposite().setTabList( new Control[]{
//        ea.getParent()
//      } );
    }
  }

  private Perspective createPerspective( final PerspectiveDescriptor desc, 
                                         final boolean notify )
  {
    Perspective result = null;
    try {
      result = new Perspective( desc, this );
      perspectiveList.add( result );
    } catch( final WorkbenchException wbe ) {
      // TODO Auto-generated catch block
      wbe.printStackTrace();
    }
    return result;
  }

  private void updateVisibility( Perspective oldPersp, Perspective newPersp ) {
    // Flag all parts in the old perspective
    IWorkbenchPartReference[] oldRefs = new IWorkbenchPartReference[ 0 ];
    if( oldPersp != null ) {
      oldRefs = oldPersp.getViewReferences();
      for( int i = 0; i < oldRefs.length; i++ ) {
        PartPane pane = ( ( WorkbenchPartReference )oldRefs[ i ] ).getPane();
        pane.setInLayout( false );
      }
    }
    PerspectiveHelper pres = null;
    // Make parts in the new perspective visible
    if( newPersp != null ) {
      pres = newPersp.getPresentation();
      IWorkbenchPartReference[] newRefs = newPersp.getViewReferences();
      for( int i = 0; i < newRefs.length; i++ ) {
        WorkbenchPartReference ref = ( WorkbenchPartReference )newRefs[ i ];
        PartPane pane = ref.getPane();
        if( pres.isPartVisible( ref ) ) {
          activationList.bringToTop( ref );
        }
        pane.setInLayout( true );
      }
    }
    updateActivePart();
    // Hide any parts in the old perspective that are no longer visible
    for( int i = 0; i < oldRefs.length; i++ ) {
      WorkbenchPartReference ref = ( WorkbenchPartReference )oldRefs[ i ];
      PartPane pane = ref.getPane();
      if( pres == null || !pres.isPartVisible( ref ) ) {
        pane.setVisible( false );
      }
    }
  }
}
