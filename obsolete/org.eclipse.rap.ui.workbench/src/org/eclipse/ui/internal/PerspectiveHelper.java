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
import org.eclipse.swt.widgets.Shell;
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

  /**
	 * Adds a part to the presentation. If a placeholder exists for the part
	 * then swap the part in. Otherwise, add the part in the bottom right corner
	 * of the presentation.
	 */
	public void addPart(LayoutPart part) {

		// Look for a placeholder.
		PartPlaceholder placeholder = null;
		LayoutPart testPart = null;
		String primaryId = part.getID();
		String secondaryId = null;

		if (part instanceof ViewPane) {
			ViewPane pane = (ViewPane) part;
			IViewReference ref = (IViewReference) pane.getPartReference();
			secondaryId = ref.getSecondaryId();
		}
		if (secondaryId != null) {
			testPart = findPart(primaryId, secondaryId);
		} else {
			testPart = findPart(primaryId);
		}

		// validate the testPart
		if (testPart != null && testPart instanceof PartPlaceholder) {
			placeholder = (PartPlaceholder) testPart;
		}

		// If there is no placeholder do a simple add. Otherwise, replace the
		// placeholder if its not a pattern matching placholder
		if (placeholder == null) {
//			part.reparent(mainLayout.getParent());
			LayoutPart relative = mainLayout.findBottomRight();
			if (relative != null && relative instanceof ILayoutContainer) {
				ILayoutContainer stack = (ILayoutContainer) relative;
				if (stack.allowsAdd(part)) {
					mainLayout.stack(part, stack);
				} else {
					mainLayout.add(part);
				}
			} else {
				mainLayout.add(part);
			}
		} else {
			ILayoutContainer container = placeholder.getContainer();
			if (container != null) {
//
//				if (container instanceof DetachedPlaceHolder) {
//					// Create a detached window add the part on it.
//					DetachedPlaceHolder holder = (DetachedPlaceHolder) container;
//					detachedPlaceHolderList.remove(holder);
//					container.remove(testPart);
//					DetachedWindow window = new DetachedWindow(page);
//					detachedWindowList.add(window);
//					window.create();
//					part.createControl(window.getShell());
//					// Open window.
//					window.getShell().setBounds(holder.getBounds());
//					window.open();
//					// add part to detached window.
//					ViewPane pane = (ViewPane) part;
//					window.add(pane);
//					LayoutPart otherChildren[] = holder.getChildren();
//					for (int i = 0; i < otherChildren.length; i++) {
//						part.getContainer().add(otherChildren[i]);
//					}
//				} else {

					// reconsistute parent if necessary
					if (container instanceof ContainerPlaceholder) {
						ContainerPlaceholder containerPlaceholder = (ContainerPlaceholder) container;
						ILayoutContainer parentContainer = containerPlaceholder
								.getContainer();
						container = (ILayoutContainer) containerPlaceholder
								.getRealContainer();
						if (container instanceof LayoutPart) {
							parentContainer.replace(containerPlaceholder,
									(LayoutPart) container);
						}
						containerPlaceholder.setRealContainer(null);
					}

					// reparent part.
//					if (!(container instanceof ViewStack)) {
						// We don't need to reparent children of PartTabFolders
						// since they will automatically
						// reparent their children when they become visible.
						// This if statement used to be
						// part of an else branch. Investigate if it is still
						// necessary.
//						part.reparent(mainLayout.getParent());
//					}

					// see if we should replace the placeholder
					if (placeholder.hasWildCard()) {
						if (container instanceof PartSashContainer) {
							((PartSashContainer) container)
									.addChildForPlaceholder(part, placeholder);
						} else {
							container.add(part);
						}
					} else {
						container.replace(placeholder, part);
					}
//				}
			}
		}
	}
	
    /**
     * Remove all references to a part.
     */
    public void removePart(LayoutPart part) {

        // Reparent the part back to the main window
        Composite parent = mainLayout.getParent();
//        part.reparent(parent);

        // Replace part with a placeholder
        ILayoutContainer container = part.getContainer();
        if (container != null) {
            String placeHolderId = part.getPlaceHolderId();
            container.replace(part, new PartPlaceholder(placeHolderId));

            // If the parent is root we're done. Do not try to replace
            // it with placeholder.
            if (container == mainLayout) {
				return;
			}

            // If the parent is empty replace it with a placeholder.
            LayoutPart[] children = container.getChildren();
            if (children != null) {
                boolean allInvisible = true;
                for (int i = 0, length = children.length; i < length; i++) {
                    if (!(children[i] instanceof PartPlaceholder)) {
                        allInvisible = false;
                        break;
                    }
                }
                if (allInvisible && (container instanceof LayoutPart)) {
                    // what type of window are we in?
                    LayoutPart cPart = (LayoutPart) container;
                    //Window oldWindow = cPart.getWindow();
                    boolean wasDocked = cPart.isDocked();
                    Shell oldShell = cPart.getShell();
//                    if (wasDocked) {
                        
                        // PR 1GDFVBY: ViewStack not disposed when page
                        // closed.
                        if (container instanceof ViewStack) {
							((ViewStack) container).dispose();
						}
                        
                        // replace the real container with a
                        // ContainerPlaceholder
                        ILayoutContainer parentContainer = cPart.getContainer();
                        ContainerPlaceholder placeholder = new ContainerPlaceholder(
                                cPart.getID());
                        placeholder.setRealContainer(container);
                        parentContainer.replace(cPart, placeholder);
                        
//                    } else {
//                        DetachedPlaceHolder placeholder = new DetachedPlaceHolder(
//                                "", oldShell.getBounds()); //$NON-NLS-1$
//                        for (int i = 0, length = children.length; i < length; i++) {
//                            children[i].getContainer().remove(children[i]);
//                            children[i].setContainer(placeholder);
//                            placeholder.add(children[i]);
//                        }
//                        detachedPlaceHolderList.add(placeholder);
//                        DetachedWindow w = (DetachedWindow)oldShell.getData();
//                        oldShell.close();
//                        detachedWindowList.remove(w);
//                    }
                }
            }
        }
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
// if( container instanceof ContainerPlaceholder ) {
// return false;
// }
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
  
  /**
   * Deref a given part. Deconstruct its container as required. Do not remove
   * drag listeners.
   */
  /* package */void derefPart(LayoutPart part) {

//      if (part instanceof ViewPane) {
//          page.removeFastView(((ViewPane) part).getViewReference());
//      }

      // Get vital part stats before reparenting.
      //Window oldWindow = part.getWindow();
//      boolean wasDocked = part.isDocked();
//      Shell oldShell = part.getShell();
      ILayoutContainer oldContainer = part.getContainer();

      // Reparent the part back to the main window
//      part.reparent(mainLayout.getParent());

      // Update container.
      if (oldContainer == null) {
			return;
		}

      oldContainer.remove(part);

      LayoutPart[] children = oldContainer.getChildren();
//      if (wasDocked) {
          boolean hasChildren = (children != null) && (children.length > 0);
          if (hasChildren) {
              // make sure one is at least visible
              int childVisible = 0;
              for (int i = 0; i < children.length; i++) {
					if (children[i].getControl() != null) {
						childVisible++;
					}
				}

              // none visible, then reprarent and remove container
              if (oldContainer instanceof ViewStack) {
                  ViewStack folder = (ViewStack) oldContainer;
                  if (childVisible == 0) {
                      ILayoutContainer parentContainer = folder
                              .getContainer();
                      for (int i = 0; i < children.length; i++) {
                          folder.remove(children[i]);
                          parentContainer.add(children[i]);
                      }
                      hasChildren = false;
                  } else if (childVisible == 1) {
                      LayoutTree layout = mainLayout.getLayoutTree();
                      layout = layout.find(folder);
                      layout.setBounds(layout.getBounds());
                  }
              }
          }

          if (!hasChildren) {
              // There are no more children in this container, so get rid of
              // it
              if (oldContainer instanceof LayoutPart) {
                  LayoutPart parent = (LayoutPart) oldContainer;
                  ILayoutContainer parentContainer = parent.getContainer();
                  if (parentContainer != null) {
                      parentContainer.remove(parent);
                      parent.dispose();
                  }
              }
          }
//      } else if (!wasDocked) {
//          if (children == null || children.length == 0) {
//              // There are no more children in this container, so get rid of
//              // it
//              // Turn on redraw again just in case it was off.
//              //oldShell.setRedraw(true);
//              DetachedWindow w = (DetachedWindow)oldShell.getData();
//              oldShell.close();
//              detachedWindowList.remove(w);
//          } else {
//              // There are children. If none are visible hide detached
//              // window.
//              boolean allInvisible = true;
//              for (int i = 0, length = children.length; i < length; i++) {
//                  if (!(children[i] instanceof PartPlaceholder)) {
//                      allInvisible = false;
//                      break;
//                  }
//              }
//              if (allInvisible) {
//                  DetachedPlaceHolder placeholder = new DetachedPlaceHolder(
//                          "", //$NON-NLS-1$
//                          oldShell.getBounds());
//                  for (int i = 0, length = children.length; i < length; i++) {
//                      oldContainer.remove(children[i]);
//                      children[i].setContainer(placeholder);
//                      placeholder.add(children[i]);
//                  }
//                  detachedPlaceHolderList.add(placeholder);
//                  DetachedWindow w = (DetachedWindow)oldShell.getData();
//                  oldShell.close();
//                  detachedWindowList.remove(w);
//              }
//          }
//      }

  }
}
