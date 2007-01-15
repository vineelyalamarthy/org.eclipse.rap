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

import java.util.HashMap;
import java.util.Map;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.ui.*;
import org.eclipse.rap.ui.internal.registry.PerspectiveDescriptor;

public class PageLayout implements IPageLayout {

  private final ViewFactory viewFactory;
  private final ViewSashContainer rootLayoutContainer;
  private final LayoutPart editorFolder;
  private PerspectiveDescriptor descriptor;
  private Map mapIDtoPart = new HashMap( 10 );
  private Map mapIDtoFolder = new HashMap( 10 );

  public PageLayout( final ViewSashContainer container, 
                     final ViewFactory viewFactory, 
                     final LayoutPart editorFolder, 
                     final PerspectiveDescriptor descriptor )
  {
    this.viewFactory = viewFactory;
    this.rootLayoutContainer = container;
    this.editorFolder = editorFolder;
    this.descriptor = descriptor;
    prefill();

  }
  
  public static int swtConstantToLayoutPosition( final int swtConstant ) {
    // TODO: [fappel] copied from original PageLayout implementation
    switch( swtConstant ) {
      case RWT.TOP:
        return IPageLayout.TOP;
      case RWT.BOTTOM:
        return IPageLayout.BOTTOM;
      case RWT.RIGHT:
        return IPageLayout.RIGHT;
      case RWT.LEFT:
        return IPageLayout.LEFT;
    }
    return -1;
  }
  
  ViewFactory getViewFactory() {
    return viewFactory;
  }

  void setFolderPart( final String viewId, final ViewStack folder ) {
    mapIDtoFolder.put( viewId, folder );
  }

  //////////////////////////
  // interface IPageLayout
  
  public void addActionSet( String actionSetId ) {
    throw new UnsupportedOperationException();
  }

  public void addFastView( String viewId ) {
    throw new UnsupportedOperationException();
  }

  public void addFastView( String viewId, float ratio ) {
    throw new UnsupportedOperationException();
  }

  public void addNewWizardShortcut( String id ) {
    throw new UnsupportedOperationException();
  }

  public void addPerspectiveShortcut( String id ) {
    throw new UnsupportedOperationException();
  }

  public void addPlaceholder( String viewId,
                              int relationship,
                              float ratio,
                              String refId )
  {
    throw new UnsupportedOperationException();
  }

  public void addShowInPart( String id ) {
    throw new UnsupportedOperationException();
  }

  public void addShowViewShortcut( String id ) {
    throw new UnsupportedOperationException();
  }

  public void addStandaloneView( String viewId,
                                 boolean showTitle,
                                 int relationship,
                                 float ratio,
                                 String refId )
  {
    throw new UnsupportedOperationException();
  }

  public void addStandaloneViewPlaceholder( String viewId,
                                            int relationship,
                                            float ratio,
                                            String refId,
                                            boolean showTitle )
  {
    throw new UnsupportedOperationException();
  }

  public void addView( String viewId,
                       int relationship,
                       float ratio,
                       String refId )
  {
    throw new UnsupportedOperationException();
  }

  public IFolderLayout createFolder( String folderId,
                                     int relationship,
                                     float ratio,
                                     String refId )
  {
//    if( checkPartInLayout( folderId ) ) {
//      return new FolderLayout( this,
//                               ( ViewStack )getRefPart( folderId ),
//                               viewFactory );
//    }
    // Create the folder.
    ViewStack folder = new ViewStack( rootLayoutContainer.page );
    folder.setID( folderId );
    addPart( folder, folderId, relationship, ratio, refId );
    // Create a wrapper.
    return new FolderLayout( this, folder, viewFactory );
  }

  public IPlaceholderFolderLayout createPlaceholderFolder( String folderId,
                                                           int relationship,
                                                           float ratio,
                                                           String refId )
  {
    throw new UnsupportedOperationException();
  }

  public IPerspectiveDescriptor getDescriptor() {
    throw new UnsupportedOperationException();
  }

  public String getEditorArea() {
    return ID_EDITOR_AREA;
  }

  public int getEditorReuseThreshold() {
    throw new UnsupportedOperationException();
  }

  public IViewLayout getViewLayout( String id ) {
    throw new UnsupportedOperationException();
  }

  public boolean isEditorAreaVisible() {
    throw new UnsupportedOperationException();
  }

  public boolean isFixed() {
    throw new UnsupportedOperationException();
  }

  public void setEditorAreaVisible( boolean showEditorArea ) {
    throw new UnsupportedOperationException();
  }

  public void setEditorReuseThreshold( int openEditors ) {
    throw new UnsupportedOperationException();
  }

  public void setFixed( boolean isFixed ) {
    throw new UnsupportedOperationException();
  }
  
  
  //////////////////
  // helping methods
  
  private void prefill() {
    addEditorArea();
    // Add default action sets.
//    ActionSetRegistry reg = WorkbenchPlugin.getDefault().getActionSetRegistry();
//    IActionSetDescriptor[] array = reg.getActionSets();
//    int count = array.length;
//    for( int nX = 0; nX < count; nX++ ) {
//      IActionSetDescriptor desc = array[ nX ];
//      if( desc.isInitiallyVisible() ) {
//        addActionSet( desc.getId() );
//      }
//    }
  }
  
  private void addEditorArea() {
//    try {
      // Create the part.
      LayoutPart newPart = createView( ID_EDITOR_AREA );
      if( newPart == null ) {
        // this should never happen as long as newID is the editor ID.
        return;
      }
      setRefPart( ID_EDITOR_AREA, newPart );
      // Add it to the layout.
      rootLayoutContainer.add( newPart );
//    } catch( PartInitException e ) {
//      WorkbenchPlugin.log( getClass(), "addEditorArea()", e ); //$NON-NLS-1$
//    }
  }

  private LayoutPart createView( String partID ) /*throws PartInitException*/ {
    if( partID.equals( ID_EDITOR_AREA ) ) {
      return editorFolder;
    }
    return null;
//    IViewDescriptor viewDescriptor = viewFactory.getViewRegistry()
//      .find( ViewFactory.extractPrimaryId( partID ) );
//    if( WorkbenchActivityHelper.filterItem( viewDescriptor ) ) {
//      return null;
//    }
//    return LayoutHelper.createView( getViewFactory(), partID );
  }
  
  private void addPart( LayoutPart newPart,
                        String partId,
                        int relationship,
                        float ratio,
                        String refId )
  {
    setRefPart( partId, newPart );
    // If the referenced part is inside a folder,
    // then use the folder as the reference part.
    LayoutPart refPart = getFolderPart( refId );
    if( refPart == null ) {
      refPart = getRefPart( refId );
    }
    // Add it to the layout.
    if( refPart != null ) {
      ratio = normalizeRatio( ratio );
      rootLayoutContainer.add( newPart,
                               getPartSashConst( relationship ),
                               ratio,
                               refPart );
    } else {
//      WorkbenchPlugin.log( NLS.bind( WorkbenchMessages.PageLayout_missingRefPart,
//                                     refId ) );
      rootLayoutContainer.add( newPart );
    }
  }

  void setRefPart( final String partID, final LayoutPart part ) {
    mapIDtoPart.put( partID, part );
  }

  LayoutPart getRefPart( final String partID ) {
    return ( LayoutPart )mapIDtoPart.get( partID );
  }

  private ViewStack getFolderPart( final String viewId ) {
    return ( ViewStack )mapIDtoFolder.get( viewId );
  }

  private int getPartSashConst( final int nRelationship ) {
    return nRelationship;
  }

  private float normalizeRatio( final float in ) {
    float result = in;
    if( in < RATIO_MIN ) {
      result = RATIO_MIN;
    }
    if( in > RATIO_MAX ) {
      result = RATIO_MAX;
    }
    return result;
  }

}
