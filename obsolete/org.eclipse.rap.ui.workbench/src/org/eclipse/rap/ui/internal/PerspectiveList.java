/*******************************************************************************
 * Copyright (c) 2002-2006 Innoopract Informationssysteme GmbH. All rights
 * reserved. This program and the accompanying materials are made available
 * under the terms of the Eclipse Public License v1.0 which accompanies this
 * distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 * Contributors: Innoopract Informationssysteme GmbH - initial API and
 * implementation
 ******************************************************************************/
package org.eclipse.rap.ui.internal;

import java.util.*;
import org.eclipse.rap.ui.IPerspectiveDescriptor;
import org.eclipse.rap.ui.internal.Perspective;

/**
 * Helper class to keep track of all opened perspective. Both the opened and
 * used order is kept.
 */
final class PerspectiveList {

  /**
   * List of perspectives in the order they were opened;
   */
  private List openedList;
  /**
   * List of perspectives in the order they were used. Last element is the most
   * recently used, and first element is the least recently used.
   */
  private List usedList;
  /**
   * The perspective explicitly set as being the active one
   */
  private Perspective active;

  /**
   * Creates an empty instance of the perspective list
   */
  public PerspectiveList() {
    openedList = new ArrayList();
    usedList = new ArrayList();
  }

  /**
   * Update the order of the perspectives in the opened list
   */
  public void reorder( final IPerspectiveDescriptor perspective, 
                       final int newLoc )
  {
    int oldLocation = 0;
    Perspective movedPerspective = null;
    for( Iterator iterator = openedList.iterator(); iterator.hasNext(); ) {
      Perspective openPerspective = ( Perspective )iterator.next();
      if( openPerspective.getDesc().equals( perspective ) ) {
        oldLocation = openedList.indexOf( openPerspective );
        movedPerspective = openPerspective;
      }
    }
    if( oldLocation != newLoc ) {
      openedList.remove( oldLocation );
      openedList.add( newLoc, movedPerspective );
    }
  }

  /**
   * Return all perspectives in the order they were activated.
   * 
   * @return an array of perspectives sorted by activation order
   */
  public Perspective[] getSortedPerspectives() {
    Perspective[] result = new Perspective[ usedList.size() ];
    return (org.eclipse.rap.ui.internal.Perspective[] )usedList.toArray( result );
  }

  /**
   * Adds a perspective to the list. No check is done for a duplicate when
   * adding.
   * 
   * @param perspective the perspective to add
   * @return boolean <code>true</code> if the perspective was added
   */
  public boolean add( final Perspective perspective ) {
    openedList.add( perspective );
    usedList.add( 0, perspective );
    // It will be moved to top only when activated.
    return true;
  }

  /**
   * Returns an iterator on the perspective list in the order they were opened.
   */
  public Iterator iterator() {
    return openedList.iterator();
  }

  /**
   * Returns an array with all opened perspectives
   */
  public Perspective[] getOpenedPerspectives() {
    Perspective[] result = new Perspective[ openedList.size() ];
    return (org.eclipse.rap.ui.internal.Perspective[] )openedList.toArray( result );
  }

  /**
   * Removes a perspective from the list.
   */
  public boolean remove( final Perspective perspective ) {
    if( active == perspective ) {
//      updateActionSets( active, null );
      active = null;
    }
    usedList.remove( perspective );
    return openedList.remove( perspective );
  }

  /**
   * Swap the opened order of old perspective with the new perspective.
   */
  public void swap( final Perspective oldPerspective, 
                    final Perspective newPerspective )
  {
    int oldIndex = openedList.indexOf( oldPerspective );
    int newIndex = openedList.indexOf( newPerspective );
    if( oldIndex >= 0 && newIndex >= 0 ) {
      openedList.set( oldIndex, newPerspective );
      openedList.set( newIndex, oldPerspective );
    }
  }

  /**
   * Returns whether the list contains any perspectives
   */
  public boolean isEmpty() {
    return openedList.isEmpty();
  }

  /**
   * Returns the most recently used perspective in the list.
   */
  public Perspective getActive() {
    return active;
  }

  /**
   * Returns the next most recently used perspective in the list.
   */
  public Perspective getNextActive() {
    Perspective result = null;
    if( active == null ) {
      if( usedList.isEmpty() ) {
        result = null;
      } else {
        result = ( Perspective )usedList.get( usedList.size() - 1 );
      }
    } else {
      if( usedList.size() < 2 ) {
        result = null;
      } else {
        result = ( Perspective )usedList.get( usedList.size() - 2 );
      }
    }
    return result;
  }

  /**
   * Returns the number of perspectives opened
   */
  public int size() {
    return openedList.size();
  }

  /**
   * Marks the specified perspective as the most recently used one in the list.
   */
  public void setActive( Perspective perspective ) {
    if( perspective != active ) {
//    updateActionSets( active, perspective );
      active = perspective;
      if( perspective != null ) {
        usedList.remove( perspective );
        usedList.add( perspective );
      }
    }
  }
//
//  private void updateActionSets( Perspective oldPersp, Perspective newPersp ) {
//    // Update action sets
//    if( newPersp != null ) {
//      IActionSetDescriptor[] newAlwaysOn = newPersp.getAlwaysOnActionSets();
//      for( int i = 0; i < newAlwaysOn.length; i++ ) {
//        IActionSetDescriptor descriptor = newAlwaysOn[ i ];
//        actionSets.showAction( descriptor );
//      }
//      IActionSetDescriptor[] newAlwaysOff = newPersp.getAlwaysOffActionSets();
//      for( int i = 0; i < newAlwaysOff.length; i++ ) {
//        IActionSetDescriptor descriptor = newAlwaysOff[ i ];
//        actionSets.maskAction( descriptor );
//      }
//    }
//    if( oldPersp != null ) {
//      IActionSetDescriptor[] newAlwaysOn = oldPersp.getAlwaysOnActionSets();
//      for( int i = 0; i < newAlwaysOn.length; i++ ) {
//        IActionSetDescriptor descriptor = newAlwaysOn[ i ];
//        actionSets.hideAction( descriptor );
//      }
//      IActionSetDescriptor[] newAlwaysOff = oldPersp.getAlwaysOffActionSets();
//      for( int i = 0; i < newAlwaysOff.length; i++ ) {
//        IActionSetDescriptor descriptor = newAlwaysOff[ i ];
//        actionSets.unmaskAction( descriptor );
//      }
//    }
//  }
}