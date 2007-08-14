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
package com.w4t.dhtml;

import java.util.Hashtable;


/** <p>A helping class that encapsulates a list of Position objects for 
  * positioning menus absolutely. Absolute positions are specified for 
  * each browser type separately (as most browsers handle them differently).
  * </p>
  */
class MenuPopupPosition {
  
  /** <p>the internal data structure for this MenuPopupPosition.</p> */
  private Hashtable positionList;
  
  
  /** <p>creates a new instance of MenuPopupPositio.</p>n */
  MenuPopupPosition() {
    positionList = new Hashtable();
  }
  

  // list handling
  ////////////////
  
  /** <p>adds the passed position to this MenuPopupPosition for the 
    * specified browser.</p>
    *
    * @param   browserName   the name of the browser for which the absolute
    *                        position is to be set. Must be one of the 
    *                        browser name constants of org.eclipse.rap.Browser.
    * @param   position      the absolute position to be set for the 
    *                        specified browser.
    */
  void add( final String browserName, final Point position ) {
    positionList.put( browserName, position );
  }

  /** <p>removes the absolute position contained in this MenuPopupPosition
    * for the specified browser.</p>
    *
    * @param   browserName   the name of the browser for which the absolute
    *                        position is removed. Must be one of the 
    *                        browser name constants of org.eclipse.rap.Browser.
    */
  void remove( final String browserName ) {
    positionList.remove( browserName );
  }
  
  /** <p>returns the absolute position contained in this MenuPopupPosition
    * for the specified browser.</p>
    *
    * @param   browserName   the name of the browser for which the absolute
    *                        position is retrieved. Must be one of the 
    *                        browser name constants of org.eclipse.rap.Browser.
    * @return                a Point object which contains the absolute 
    *                        position for the specified browser, or null if
    *                        no position for that browser was set.
    */
  Point get( final String browserName ) {
    return ( Point )positionList.get( browserName );
  }
}