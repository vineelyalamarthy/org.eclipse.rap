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

import java.text.MessageFormat;
import java.util.*;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.rap.rwt.widgets.Composite;
import org.eclipse.rap.ui.*;
import org.eclipse.rap.ui.internal.registry.PerspectiveDescriptor;


public class Perspective {
  
  private PerspectiveDescriptor descriptor;
  private WorkbenchPage page;
  private LayoutPart editorArea;
  private ViewFactory viewFactory;
  private PerspectiveHelper presentation;
  private Map mapIDtoViewLayoutRec;
  private PartPlaceholder editorHolder;

  public Perspective( final PerspectiveDescriptor descriptor, 
                      final WorkbenchPage page )
    throws WorkbenchException
  {
    this( page );
    this.descriptor = descriptor;
    if( descriptor != null ) {
      createPresentation( descriptor );
    }
  }
  
  protected Perspective( final WorkbenchPage page ) throws WorkbenchException {
    this.page = page;
    this.editorArea = page.getEditorPresentation().getLayoutPart();
    this.viewFactory = page.getViewFactory();
    this.mapIDtoViewLayoutRec = new HashMap();
  }

  public IPerspectiveDescriptor getDesc() {
    throw new UnsupportedOperationException();
  }
  
  public boolean isCloseable( final IViewReference reference ) {
    ViewLayoutRec rec = getViewLayoutRec( reference, false );
    boolean result = true;
    if( rec != null ) {
      result = rec.isCloseable;
    }
    return result;
  }

  public PerspectiveHelper getPresentation() {
    return presentation;
  }

  public IViewReference[] getViewReferences() {
    // Get normal views.
    if( presentation == null ) {
      return new IViewReference[ 0 ];
    }
    List panes = new ArrayList( 5 );
    presentation.collectViewPanes( panes );
    IViewReference[] resultArray = new IViewReference[ panes.size() ];
//    IViewReference[] resultArray = new IViewReference[ panes.size()
//                                                       + fastViews.size() ];
//    // Copy fast views.
//    int nView = 0;
//    for( int i = 0; i < fastViews.size(); i++ ) {
//      resultArray[ nView ] = ( IViewReference )fastViews.get( i );
//      ++nView;
//    }
    // Copy normal views.
    for( int i = 0; i < panes.size(); i++ ) {
      ViewPane pane = ( ViewPane )panes.get( i );
      resultArray[ i ] = pane.getViewReference();
//      resultArray[ nView ] = pane.getViewReference();
//      ++nView;
    }
    return resultArray;
  }

  public void partActivated( IWorkbenchPart activePart ) {
    // If a fastview is open close it.
//    if( activeFastView != null && activeFastView.getPart( false ) != activePart )
//    {
//      setActiveFastView( null );
//    }
  }

  public boolean bringToTop( IViewReference ref ) {
//    if( isFastView( ref ) ) {
//      setActiveFastView( ref );
//      return true;
//    } else {
      return presentation.bringPartToTop( getPane( ref ) );
//    }
}

  protected void onActivate() {
    // Update editor area state.
//    if( editorArea.getControl() != null ) {
//      editorArea.setVisible( isEditorAreaVisible() );
//    }
    // Update fast views.
    // Make sure the control for the fastviews are create so they can
    // be activated.
//    for( int i = 0; i < fastViews.size(); i++ ) {
//      ViewPane pane = getPane( ( IViewReference )fastViews.get( i ) );
//      if( pane != null ) {
//        Control ctrl = pane.getControl();
//        if( ctrl == null ) {
//          pane.createControl( getClientComposite() );
//          ctrl = pane.getControl();
//        }
//        ctrl.setEnabled( false ); // Remove focus support.
//      }
//    }
//    setAllPinsVisible( true );
    presentation.activate( getClientComposite() );
//    if( shouldHideEditorsOnActivate ) {
//      // We do this here to ensure that createPartControl is called on the top
//      // editor
//      // before it is hidden. See bug 20166.
//      hideEditorArea();
//      shouldHideEditorsOnActivate = false;
//    }
  }

  public IViewReference findView( String viewId ) {
    return findView( viewId, null );
  }

  public IViewReference findView( String id, String secondaryId ) {
    IViewReference refs[] = getViewReferences();
    for( int i = 0; i < refs.length; i++ ) {
      IViewReference ref = refs[ i ];
      if(    id.equals( ref.getId() )
          && (   secondaryId == null
               ? ref.getSecondaryId() == null
               : secondaryId.equals( ref.getSecondaryId() ) ) )
      {
        return ref;
      }
    }
    return null;
  }

  protected void hideEditorArea() {
    if( !isEditorAreaVisible() ) {
      return;
    }
    // Replace the editor area with a placeholder so we
    // know where to put it back on show editor area request.
    editorHolder = new PartPlaceholder( editorArea.getID() );
    presentation.getLayout().replace( editorArea, editorHolder );
  }

  protected boolean isEditorAreaVisible() {
    return editorHolder == null;
  }

  //////////////////
  // helping methods
  
  private Composite getClientComposite() {
    return page.getClientComposite();
  }
  
  private ViewFactory getViewFactory() {
    return viewFactory;
  }

  
  private void createPresentation( final PerspectiveDescriptor descriptor )
    throws WorkbenchException
  {
//    if( descriptor.hasCustomDefinition() ) {
//      loadCustomPersp( descriptor );
//    } else {
      loadPredefinedPersp( descriptor );
//    }
  }
  
  private void loadPredefinedPersp( final PerspectiveDescriptor desc )
    throws WorkbenchException
  {
    // Create layout engine.
    IPerspectiveFactory factory = null;
    try {
      factory = desc.createFactory();
    } catch( final CoreException ce ) {
      // TODO: [fappel] Exception handling
      String txt = "Unable to load perspective: {0}";
      String msg = MessageFormat.format( txt, new Object[] { desc.getId() } );
      throw new WorkbenchException( msg );
    }
    
    // IPerspectiveFactory#createFactory() can return null
    if( factory == null ) {
      // TODO: [fappel] Exception handling
      String txt = "Unable to load perspective: {0}";
      String msg = MessageFormat.format( txt, new Object[] { desc.getId() } );
      throw new WorkbenchException( msg );
    }
    
    // Create layout factory.
    ViewSashContainer container = new ViewSashContainer( page,
                                                         getClientComposite() );
    PageLayout layout = new PageLayout( container,
                                        getViewFactory(),
                                        editorArea,
                                        descriptor );
//    layout.setFixed( descriptor.getFixed() );
//    // add the placeholders for the sticky folders and their contents
//    IPlaceholderFolderLayout stickyFolderRight = null, stickyFolderLeft = null, stickyFolderTop = null, stickyFolderBottom = null;
//    IStickyViewDescriptor[] descs = WorkbenchPlugin.getDefault()
//      .getViewRegistry()
//      .getStickyViews();
//    for( int i = 0; i < descs.length; i++ ) {
//      IStickyViewDescriptor stickyViewDescriptor = descs[ i ];
//      String id = stickyViewDescriptor.getId();
//      switch( stickyViewDescriptor.getLocation() ) {
//        case IPageLayout.RIGHT:
//          if( stickyFolderRight == null ) {
//            stickyFolderRight = layout.createPlaceholderFolder( StickyViewDescriptor.STICKY_FOLDER_RIGHT,
//                                                                IPageLayout.RIGHT,
//                                                                .75f,
//                                                                IPageLayout.ID_EDITOR_AREA );
//          }
//          stickyFolderRight.addPlaceholder( id );
//        break;
//        case IPageLayout.LEFT:
//          if( stickyFolderLeft == null ) {
//            stickyFolderLeft = layout.createPlaceholderFolder( StickyViewDescriptor.STICKY_FOLDER_LEFT,
//                                                               IPageLayout.LEFT,
//                                                               .25f,
//                                                               IPageLayout.ID_EDITOR_AREA );
//          }
//          stickyFolderLeft.addPlaceholder( id );
//        break;
//        case IPageLayout.TOP:
//          if( stickyFolderTop == null ) {
//            stickyFolderTop = layout.createPlaceholderFolder( StickyViewDescriptor.STICKY_FOLDER_TOP,
//                                                              IPageLayout.TOP,
//                                                              .25f,
//                                                              IPageLayout.ID_EDITOR_AREA );
//          }
//          stickyFolderTop.addPlaceholder( id );
//        break;
//        case IPageLayout.BOTTOM:
//          if( stickyFolderBottom == null ) {
//            stickyFolderBottom = layout.createPlaceholderFolder( StickyViewDescriptor.STICKY_FOLDER_BOTTOM,
//                                                                 IPageLayout.BOTTOM,
//                                                                 .75f,
//                                                                 IPageLayout.ID_EDITOR_AREA );
//          }
//          stickyFolderBottom.addPlaceholder( id );
//        break;
//      }
//      // should never be null as we've just added the view above
//      IViewLayout viewLayout = layout.getViewLayout( id );
//      viewLayout.setCloseable( stickyViewDescriptor.isCloseable() );
//      viewLayout.setMoveable( stickyViewDescriptor.isMoveable() );
//    }
    
    // Run layout engine.
    factory.createInitialLayout( layout );
    
//    PerspectiveExtensionReader extender = new PerspectiveExtensionReader();
//    extender.extendLayout( page.getExtensionTracker(),
//                           descriptor.getId(),
//                           layout );
//    // Retrieve view layout info stored in the page layout.
//    mapIDtoViewLayoutRec.putAll( layout.getIDtoViewLayoutRecMap() );
//    // Create action sets.
//    List temp = new ArrayList();
//    createInitialActionSets( temp, layout.getActionSets() );
//    for( Iterator iter = temp.iterator(); iter.hasNext(); ) {
//      IActionSetDescriptor descriptor = ( IActionSetDescriptor )iter.next();
//      addAlwaysOn( descriptor );
//    }
//    newWizardShortcuts = layout.getNewWizardShortcuts();
//    showViewShortcuts = layout.getShowViewShortcuts();
//    perspectiveShortcuts = layout.getPerspectiveShortcuts();
//    showInPartIds = layout.getShowInPartIds();
//    // Retrieve fast views
//    fastViews = layout.getFastViews();
//    // Is the layout fixed
//    fixed = layout.isFixed();
    
    // Create presentation. 
    presentation = new PerspectiveHelper( page, container );
    // Hide editor area if requested by factory
    if( !layout.isEditorAreaVisible() ) {
      hideEditorArea();
    }
  }
  
  private ViewLayoutRec getViewLayoutRec( final IViewReference viewRefernce, 
                                          final boolean create )
  {
    return getViewLayoutRec( ViewFactory.getKey( viewRefernce ), create );
  }

  private ViewLayoutRec getViewLayoutRec( final String viewId, 
                                          final boolean create )
  {
    ViewLayoutRec result = ( ViewLayoutRec )mapIDtoViewLayoutRec.get( viewId );
    if( result == null && create ) {
      result = new ViewLayoutRec();
      mapIDtoViewLayoutRec.put( viewId, result );
    }
    return result;
  }

  private ViewPane getPane( IViewReference ref ) {
    return ( ViewPane )( ( WorkbenchPartReference )ref ).getPane();
  }
}
