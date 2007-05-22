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

package org.eclipse.ui.internal.presentations.util;

import java.util.ArrayList;

import org.eclipse.core.runtime.Assert;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.internal.presentations.defaultpresentation.DefaultPartList;
import org.eclipse.ui.presentations.*;


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
        case TabFolderEvent.EVENT_PANE_MENU: {
          TabbedStackPresentation.this.showPaneMenu( folder.getPartForTab( e.tab ),
                                                     new Point( e.x, e.y ) );
          break;
        }
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
	  folder.getTabFolder().getControl().dispose();
  }

  public Control getControl() {
    return folder.getTabFolder().getControl();
  }

  public Control[] getTabList( IPresentablePart part ) {
    ArrayList list = new ArrayList();
    if( folder.getTabFolder().getTabPosition() == SWT.BOTTOM ) {
      if( part.getControl() != null ) {
        list.add( part.getControl() );
      }
    }
    list.add( folder.getTabFolder().getControl() );
    if( part.getToolBar() != null ) {
      list.add( part.getToolBar() );
    }
    if( folder.getTabFolder().getTabPosition() == SWT.TOP ) {
      if( part.getControl() != null ) {
        list.add( part.getControl() );
      }
    }
    return (org.eclipse.swt.widgets.Control[] )list.toArray( new Control[ list.size() ] );

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
                                                           ? SWT.DEFAULT
                                                           : availablePerpendicular;
        minSize = folder.getTabFolder().computeSize( SWT.DEFAULT, heightHint ).x;
      } else {
        int widthHint = availablePerpendicular == INFINITE
                                                          ? SWT.DEFAULT
                                                          : availablePerpendicular;
        minSize = folder.getTabFolder().computeSize( widthHint, SWT.DEFAULT ).y;
      }
      if( getSite().getState() == IStackPresentationSite.STATE_MINIMIZED ) {
        return minSize;
      }
      return Math.max( minSize, preferredResult );
    }
    return INFINITE;
  }

  /* (non-Javadoc)
   * @see org.eclipse.ui.presentations.StackPresentation#removePart(org.eclipse.ui.presentations.IPresentablePart)
   */
  public void removePart(IPresentablePart oldPart) {
//      ignoreSelectionChanges++;
      try {
          tabs.remove(oldPart);
      } finally {
//          ignoreSelectionChanges--;
      }
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
    IPresentablePart current = getSite().getSelectedPart();
    if (current != null) {
        current.setVisible(isVisible);
    }

    folder.setVisible(isVisible);
  }

  /* (non-Javadoc)
   * @see org.eclipse.ui.presentations.StackPresentation#showPaneMenu()
   */
  public void showPaneMenu() {
      IPresentablePart part = getSite().getSelectedPart();
      
      if (part != null) {
          showPaneMenu(part, folder.getTabFolder().getPaneMenuLocation());
      }
  }

  public void showPaneMenu(IPresentablePart part, Point location) {
      Assert.isTrue(!isDisposed());
      
      IPartMenu menu = part.getMenu();

      if (menu != null) {
          menu.showMenu(location);
      }
  }
  
  /**
   * Returns true iff the presentation has been disposed
   * 
   * @return true iff the presentation has been disposed
   */
  private boolean isDisposed() {
      return folder == null; /* || folder.isDisposed(); */
  }
  
  public void showSystemMenu() {
    throw new UnsupportedOperationException();
  }
}
