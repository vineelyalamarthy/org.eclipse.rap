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


/** <p>This is the central data structure for a number of items which are
  * in some way connected (e.g. by adding them to each other).</p>
  */
public class TreeData {

  /** <p>the datastructure containing the item information. Key is the itemID
    * of the items contained in this TreeData, which should be unique; values
    * are the items which have the specified key.</p> */
  private Hashtable htItems  = new Hashtable();
  /** the Item which is marked on the tree. */
  // TODO: currently, there is only one item possibly marked; should be a list,
  // to make multiselection possible (also make configurable, whether
  // multiselection or not
  private Item markedItem = null;

  /** <p>returns the Item with the specified itemID, if there is one
    * conatained in this TreeData, or null else.</p> */
  public Item get( final String itemID ) {
    return ( Item )htItems.get( itemID );
  }

  /** <p>returns, whether an Item with the specified itemID is contained in
    * this TreeData.</p> */
  public boolean containsItem( final String itemID ) {
    return htItems.containsKey( itemID );
  }

  /** <p>adds the specified item with the specified itemID as key to this
    * TreeData. If there is already an Item with this ID, it is replaced by
    * item.</p> */
  public void put( final String itemID, final Item item ) {
    htItems.put( itemID, item );
  }

  /** <p>removes the specified item with the specified itemID from
    * TreeData.</p> */
  public void remove( final String itemID ) {
    htItems.remove( itemID );
  }

  /** <p>returns, whether the Item with the specified itemID is marked on
    * the tree.</p> */
  public boolean isMarked( final String itemID ) {
    boolean result = false;
    if(    this.markedItem != null
        && itemID.equals( markedItem.getItemID() ) ) {
      result = true;
    }
    return result;
  }

  /** <p>returns, whether the specified Item is marked on the tree.</p> */
  public boolean isMarked( final Item item ) {
    return isMarked( item.getItemID() );
  }

  /** <p>sets the Item with the specified itemID as marked on the tree.</p> */
  public void setMarked( final String itemID ) {
    Item item = ( Item )htItems.get( itemID );
    if( item != null ) {
      this.markedItem = item;
    } else {
      this.markedItem = null;
    }
  }

  /** <p>sets item as marked on the tree.</p> */
  public void setMarked( final Item item ) {
    if( item != null ) {
      this.markedItem = item;
    }
  }

  /** <p>returns the Item which is marked on the tree.</p> */
  public Item getMarkedItem() {
    return markedItem;
  }
  
  /** returns the number of items currently contained in this TreeData.</p> */
  int getItemCount() {
    return htItems.keySet().size();
  }
}