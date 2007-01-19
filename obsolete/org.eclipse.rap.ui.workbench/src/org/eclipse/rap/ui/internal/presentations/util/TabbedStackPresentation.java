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

package org.eclipse.rap.ui.internal.presentations.util;

import java.util.ArrayList;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.graphics.Rectangle;
import org.eclipse.rap.rwt.widgets.Control;
import org.eclipse.rap.ui.internal.presentations.defaultpresentation.DefaultPartList;
import org.eclipse.rap.ui.presentations.*;


public class TabbedStackPresentation extends StackPresentation {
  
  private PresentablePartFolder folder;
  private TabOrder tabs;
  private ISystemMenu partList;
  private TabFolderListener tabFolderListener = new TabFolderListener() {

    public void handleEvent( TabFolderEvent e ) {
      switch( e.type ) {
        case TabFolderEvent.EVENT_MINIMIZE: {
          getSite().setState( IStackPresentationSite.STATE_MINIMIZED );
          break;
        }
        case TabFolderEvent.EVENT_MAXIMIZE: {
          getSite().setState( IStackPresentationSite.STATE_MAXIMIZED );
          break;
        }
        case TabFolderEvent.EVENT_RESTORE: {
          getSite().setState( IStackPresentationSite.STATE_RESTORED );
          break;
        }
        case TabFolderEvent.EVENT_CLOSE: {
          IPresentablePart part = folder.getPartForTab( e.tab );
          if( part != null ) {
            getSite().close( new IPresentablePart[]{
              part
            } );
          }
          break;
        }
        case TabFolderEvent.EVENT_SHOW_LIST: {
          showPartList();
          break;
        }
        case TabFolderEvent.EVENT_GIVE_FOCUS_TO_PART: {
          IPresentablePart part = getSite().getSelectedPart();
          if( part != null ) {
            part.setFocus();
          }
          break;
        }
//        case TabFolderEvent.EVENT_PANE_MENU: {
//          TabbedStackPresentation.this.showPaneMenu( folder.getPartForTab( e.tab ),
//                                                     new Point( e.x, e.y ) );
//          break;
//        }
//        case TabFolderEvent.EVENT_DRAG_START: {
//          AbstractTabItem beingDragged = e.tab;
//          Point initialLocation = new Point( e.x, e.y );
//          if( beingDragged == null ) {
//            getSite().dragStart( initialLocation, false );
//          } else {
//            IPresentablePart part = folder.getPartForTab( beingDragged );
//            try {
//              dragStart = folder.indexOf( part );
//              getSite().dragStart( part, initialLocation, false );
//            } finally {
//              dragStart = -1;
//            }
//          }
//          break;
//        }
        case TabFolderEvent.EVENT_TAB_SELECTED: {
//          if( ignoreSelectionChanges > 0 ) {
//            return;
//          }
          IPresentablePart part = folder.getPartForTab( e.tab );
          if( part != null ) {
            getSite().selectPart( part );
          }
          break;
        }
        case TabFolderEvent.EVENT_SYSTEM_MENU: {
//          IPresentablePart part = folder.getPartForTab( e.tab );
//          if( part == null ) {
//            part = getSite().getSelectedPart();
//          }
//          if( part != null ) {
//            showSystemMenu( new Point( e.x, e.y ), part );
//          }
//          break;
        }
      }
    }
  };


  protected TabbedStackPresentation( final IStackPresentationSite stackSite ) {
    super( stackSite );
  }

  public TabbedStackPresentation( final IStackPresentationSite site, 
                                  final PresentablePartFolder folder, 
                                  final ISystemMenu menu )
  {
    this( site, folder,  new LeftToRightTabOrder( folder ), menu );
  }

  public TabbedStackPresentation( final IStackPresentationSite site, 
                                  final AbstractTabFolder widget, 
                                  final StandardViewSystemMenu systemMenu )
  {
    this( site, new PresentablePartFolder( widget ), systemMenu );
  }
  
  public TabbedStackPresentation( final IStackPresentationSite site,
                                  final PresentablePartFolder folder, 
                                  final TabOrder tabs,
                                  final ISystemMenu systemMenu )
  {
    super( site );
    this.folder = folder;
    this.tabs = tabs;
    
    folder.getTabFolder().addListener( tabFolderListener );
    this.partList = new DefaultPartList( site, folder );
  }

  public void addPart( IPresentablePart newPart, Object cookie ) {
//    ignoreSelectionChanges++;
//    try {
//      if( initializing ) {
//        tabs.addInitial( newPart );
//      } else {
//        if( cookie == null ) {
          tabs.add( newPart );
//        } else {
//          int insertionPoint = dragBehavior.getInsertionPosition( cookie );
//          tabs.insert( newPart, insertionPoint );
//        }
//      }
//    } finally {
//      ignoreSelectionChanges--;
//    }
  }

  public void dispose() {
    throw new UnsupportedOperationException();
  }

  public Control getControl() {
    return folder.getTabFolder().getControl();
  }

  public Control[] getTabList( IPresentablePart part ) {
    ArrayList list = new ArrayList();
    if( folder.getTabFolder().getTabPosition() == RWT.BOTTOM ) {
      if( part.getControl() != null ) {
        list.add( part.getControl() );
      }
    }
    list.add( folder.getTabFolder().getControl() );
    if( part.getToolBar() != null ) {
      list.add( part.getToolBar() );
    }
    if( folder.getTabFolder().getTabPosition() == RWT.TOP ) {
      if( part.getControl() != null ) {
        list.add( part.getControl() );
      }
    }
    return ( Control[] )list.toArray( new Control[ list.size() ] );

  }

  public int computePreferredSize( boolean width,
                                   int availableParallel,
                                   int availablePerpendicular,
                                   int preferredResult )
  {
    if( preferredResult != INFINITE
        || getSite().getState() == IStackPresentationSite.STATE_MINIMIZED )
    {
      int minSize = 0;
      if( width ) {
        int heightHint = availablePerpendicular == INFINITE
                                                           ? RWT.DEFAULT
                                                           : availablePerpendicular;
        minSize = folder.getTabFolder().computeSize( RWT.DEFAULT, heightHint ).x;
      } else {
        int widthHint = availablePerpendicular == INFINITE
                                                          ? RWT.DEFAULT
                                                          : availablePerpendicular;
        minSize = folder.getTabFolder().computeSize( widthHint, RWT.DEFAULT ).y;
      }
      if( getSite().getState() == IStackPresentationSite.STATE_MINIMIZED ) {
        return minSize;
      }
      return Math.max( minSize, preferredResult );
    }
    return INFINITE;
  }

  public void removePart( IPresentablePart oldPart ) {
    throw new UnsupportedOperationException();
  }

  public void selectPart( final IPresentablePart toSelect ) {
// initializing = false;
    tabs.select( toSelect );
  }

  public void setActive( int newState ) {
    folder.getTabFolder().setActive( newState );
  }

  public void showPartList() {
    if( partList != null ) {
      final int numberOfParts = folder.getTabFolder().getItemCount();
      if( numberOfParts > 0 ) {
        partList.show( getControl(), 
                       folder.getTabFolder().getPartListLocation(),
                       getSite().getSelectedPart() );
      }
    }
  }
  public void setBounds( Rectangle bounds ) {
    folder.setBounds( bounds );
  }

  public void setState( int state ) {
    folder.getTabFolder().setState( state );
  }

  public void setVisible( boolean isVisible ) {
    throw new UnsupportedOperationException();
  }

  public void showPaneMenu() {
    throw new UnsupportedOperationException();
  }

  public void showSystemMenu() {
    throw new UnsupportedOperationException();
  }
}
