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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.internal.misc.StringMatcher;


public class PerspectiveHelper {

  private WorkbenchPage page;
  private ViewSashContainer mainLayout;
  private boolean active = false;
  private Composite parentWidget;
  
  
  private class MatchingPart implements Comparable {
    String pid;
    String sid;
    LayoutPart part;
    boolean hasWildcard;
    int len;

    MatchingPart(String pid, String sid, LayoutPart part) {
        this.pid = pid;
        this.sid = sid;
        this.part = part;
        this.len = (pid == null ? 0 : pid.length())
                + (sid == null ? 0 : sid.length());
        this.hasWildcard = (pid != null && pid
                .indexOf(PartPlaceholder.WILD_CARD) != -1)
                || (sid != null && sid.indexOf(PartPlaceholder.WILD_CARD) != -1);
    }

    public int compareTo(Object a) {
        // specific ids always outweigh ids with wildcards
        MatchingPart ma = (MatchingPart) a;
        if (this.hasWildcard && !ma.hasWildcard) {
            return -1;
        }
        if (!this.hasWildcard && ma.hasWildcard) {
            return 1;
        }
        // if both are specific or both have wildcards, simply compare based on length
        return ma.len - this.len;
    }
  }
  
  
  public PerspectiveHelper( final WorkbenchPage page, 
                            final ViewSashContainer mainLayout )
  {
    this.page = page;
    this.mainLayout = mainLayout;
  }

  public ViewSashContainer getLayout() {
    return mainLayout;
  }

  
  public void activate( final Composite parent ) {
    if( active ) {
      return;
    }
    parentWidget = parent;
    // Activate main layout
    // make sure all the views have been properly parented
    Vector children = new Vector();
    collectViewPanes( children, mainLayout.getChildren() );
    Enumeration itr = children.elements();
    while( itr.hasMoreElements() ) {
      LayoutPart part = ( LayoutPart )itr.nextElement();
//      part.reparent( parent );
    }
    mainLayout.createControl( parent );
    mainLayout.setActive( true );
    // Open the detached windows.
//    for( int i = 0, length = detachedWindowList.size(); i < length; i++ ) {
//      DetachedWindow dwindow = ( DetachedWindow )detachedWindowList.get( i );
//      dwindow.open();
//    }
//    enableAllDrag();
    active = true;
  }
  
  public void collectViewPanes( List result ) {
    // Scan the main window.
    collectViewPanes( result, mainLayout.getChildren() );
    // Scan each detached window.
//    if( detachable ) {
//      for( int i = 0, length = detachedWindowList.size(); i < length; i++ ) {
//        DetachedWindow win = ( DetachedWindow )detachedWindowList.get( i );
//        collectViewPanes( result, win.getChildren() );
//      }
//    }
  }

  private void collectViewPanes( List result, LayoutPart[] parts ) {
    for( int i = 0, length = parts.length; i < length; i++ ) {
      LayoutPart part = parts[ i ];
      if( part instanceof ViewPane ) {
        result.add( part );
      } else if( part instanceof ILayoutContainer ) {
        collectViewPanes( result, ( ( ILayoutContainer )part ).getChildren() );
      }
    }
  }

  public boolean bringPartToTop( LayoutPart part ) {
    ILayoutContainer container = part.getContainer();
    if( container != null && container instanceof PartStack ) {
      PartStack folder = ( PartStack )container;
      if( folder.getSelection() != part ) {
        folder.setSelection( part );
        return true;
      }
    }
    return false;
  }

  public boolean isPartVisible( IWorkbenchPartReference partRef ) {
    LayoutPart foundPart;
    if( partRef instanceof IViewReference ) {
      foundPart = findPart( partRef.getId(),
                            ( ( IViewReference )partRef ).getSecondaryId() );
    } else {
      foundPart = findPart( partRef.getId() );
    }
    if( foundPart == null ) {
      return false;
    }
    if( foundPart instanceof PartPlaceholder ) {
      return false;
    }
    ILayoutContainer container = foundPart.getContainer();
//    if( container instanceof ContainerPlaceholder ) {
//      return false;
//    }
    if( container instanceof ViewStack ) {
      ViewStack folder = ( ViewStack )container;
      PartPane visiblePart = folder.getSelection();
      if( visiblePart == null ) {
        return false;
      }
      return partRef.equals( visiblePart.getPartReference() );
    }
    return true;
  }

  private LayoutPart findPart( String id ) {
    return findPart( id, null );
  }

  /**
   * Find the first part that matches the specified primary and secondary id
   * pair. Wild cards are supported.
   */
  private LayoutPart findPart( String primaryId, String secondaryId ) {
    // check main window.
    ArrayList matchingParts = new ArrayList();
    LayoutPart part = ( secondaryId != null )
                                             ? findPart( primaryId,
                                                         secondaryId,
                                                         mainLayout.getChildren(),
                                                         matchingParts )
                                             : findPart( primaryId,
                                                         mainLayout.getChildren(),
                                                         matchingParts );
    if( part != null ) {
      return part;
    }
    // check each detached windows.
//    for( int i = 0, length = detachedWindowList.size(); i < length; i++ ) {
//      DetachedWindow window = ( DetachedWindow )detachedWindowList.get( i );
//      part = ( secondaryId != null )
//                                    ? findPart( primaryId,
//                                                secondaryId,
//                                                window.getChildren(),
//                                                matchingParts )
//                                    : findPart( primaryId,
//                                                window.getChildren(),
//                                                matchingParts );
//      if( part != null ) {
//        return part;
//      }
//    }
//    for( int i = 0; i < detachedPlaceHolderList.size(); i++ ) {
//      DetachedPlaceHolder holder = ( DetachedPlaceHolder )detachedPlaceHolderList.get( i );
//      part = ( secondaryId != null )
//                                    ? findPart( primaryId,
//                                                secondaryId,
//                                                holder.getChildren(),
//                                                matchingParts )
//                                    : findPart( primaryId,
//                                                holder.getChildren(),
//                                                matchingParts );
//      if( part != null ) {
//        return part;
//      }
//    }
    // sort the matching parts
    if( matchingParts.size() > 0 ) {
      Collections.sort( matchingParts );
      MatchingPart mostSignificantPart = ( MatchingPart )matchingParts.get( 0 );
      if( mostSignificantPart != null ) {
        return mostSignificantPart.part;
      }
    }
    // Not found.
    return null;
  }

  /**
   * Find the first part with a given ID in the presentation.
   */
  private LayoutPart findPart( String id,
                               LayoutPart[] parts,
                               ArrayList matchingParts )
  {
    for( int i = 0, length = parts.length; i < length; i++ ) {
      LayoutPart part = parts[ i ];
      // check for part equality, parts with secondary ids fail
      if( part.getID().equals( id ) ) {
        if( part instanceof ViewPane ) {
          ViewPane pane = ( ViewPane )part;
          IViewReference ref = ( IViewReference )pane.getPartReference();
          if( ref.getSecondaryId() != null ) {
            continue;
          }
        }
        return part;
      }
      // check pattern matching placeholders
      else if( part instanceof PartPlaceholder
               && ( ( PartPlaceholder )part ).hasWildCard() )
      {
        StringMatcher sm = new StringMatcher( part.getID(), true, false );
        if( sm.match( id ) ) {
          matchingParts.add( new MatchingPart( part.getID(), null, part ) );
        }
      } else if( part instanceof EditorSashContainer ) {
        // Skip.
      } else if( part instanceof ILayoutContainer ) {
        part = findPart( id,
                         ( ( ILayoutContainer )part ).getChildren(),
                         matchingParts );
        if( part != null ) {
          return part;
        }
      }
    }
    return null;
  }

  /**
   * Find the first part that matches the specified primary and secondary id
   * pair. Wild cards are supported.
   */
  private LayoutPart findPart( String primaryId,
                               String secondaryId,
                               LayoutPart[] parts,
                               ArrayList matchingParts )
  {
    for( int i = 0, length = parts.length; i < length; i++ ) {
      LayoutPart part = parts[ i ];
      // check containers first
      if( part instanceof ILayoutContainer ) {
        LayoutPart testPart = findPart( primaryId,
                                        secondaryId,
                                        ( ( ILayoutContainer )part ).getChildren(),
                                        matchingParts );
        if( testPart != null ) {
          return testPart;
        }
      }
      // check for view part equality
      if( part instanceof ViewPane ) {
        ViewPane pane = ( ViewPane )part;
        IViewReference ref = ( IViewReference )pane.getPartReference();
        if( ref.getId().equals( primaryId )
            && ref.getSecondaryId() != null
            && ref.getSecondaryId().equals( secondaryId ) )
        {
          return part;
        }
      }
      // check placeholders
      else if( ( parts[ i ] instanceof PartPlaceholder ) ) {
        String id = part.getID();
        // optimization: don't bother parsing id if it has no separator -- it
        // can't match
        String phSecondaryId = ViewFactory.extractSecondaryId( id );
        if( phSecondaryId == null ) {
          // but still need to check for wildcard case
          if( id.equals( PartPlaceholder.WILD_CARD ) ) {
            matchingParts.add( new MatchingPart( id, null, part ) );
          }
          continue;
        }
        String phPrimaryId = ViewFactory.extractPrimaryId( id );
        // perfect matching pair
        if( phPrimaryId.equals( primaryId )
            && phSecondaryId.equals( secondaryId ) )
        {
          return part;
        }
        // check for partial matching pair
        StringMatcher sm = new StringMatcher( phPrimaryId, true, false );
        if( sm.match( primaryId ) ) {
          sm = new StringMatcher( phSecondaryId, true, false );
          if( sm.match( secondaryId ) ) {
            matchingParts.add( new MatchingPart( phPrimaryId,
                                                 phSecondaryId,
                                                 part ) );
          }
        }
      } else if( part instanceof EditorSashContainer ) {
        // Skip.
      }
    }
    return null;
  }

  public boolean isZoomed() {
    return mainLayout.getZoomedPart() != null;
  }

  public void zoomIn( IWorkbenchPartReference ref ) {
    PartPane pane = ( ( WorkbenchPartReference )ref ).getPane();
//    parentWidget.setRedraw( false );
    try {
      pane.requestZoomIn();
    } finally {
//      parentWidget.setRedraw( true );
    }
  }

  public void zoomOut() {
    LayoutPart zoomPart = mainLayout.getZoomedPart();
    if( zoomPart != null ) {
      zoomPart.requestZoomOut();
    }
  }
}
