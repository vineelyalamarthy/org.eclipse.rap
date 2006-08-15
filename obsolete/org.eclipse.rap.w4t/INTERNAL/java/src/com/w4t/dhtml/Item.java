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

import com.w4t.WebComponent;
import com.w4t.WebContainer;
import com.w4t.event.WebActionEvent;
import com.w4t.event.WebActionListener;

/** <p>The abstract superclass of all items (nodes, leaves etc.) in dhtml
  * components.</p>
  * <p>This also provides some functionality for managing items in a central
  * data structure.</p>
  */
public abstract class Item extends WebComponent {

  /** <p>the central data structure for a number of items which are in some
    * way connected (e.g. by adding them to each other).</p> */
  protected TreeData tdItems  = null;
  /** the ID for this Item (which better should be unique). */
  protected String itemID = "";
  /** the parent node for this Item, i.e. the Node  to which this
    * Item is added. */
  protected Node parentNode = null;
  /** the caption of every item */
  protected String label = "";
  
  /** constructor */
  public Item() {
    super();
    itemID = getUniqueID();
  }

  /** sets a reference to the central item list data structure on this Item. */
  protected void setItemList( final TreeData tdItems ) {
    this.tdItems = tdItems;
  }

  /** returns a reference to the Item with the specified itemID. */
  public Item retrieveItem( final String itemID ) {
    Item foundItem = null;
    if( itemID.equals( this.itemID ) ) {
      foundItem = this;
    } else if( tdItems != null ) {
      foundItem = tdItems.get( itemID );
    }
    return foundItem;
  }

  /** <p>causes this Item to remove itself from a node to which it is added
    * (if any).</p> */
  public void remove() {
    super.remove();
    if( parentNode != null ) {
      parentNode.removeItem( this );
    }
  }

  
  /** <p>sets an ID for this Item (which better should be unique).</p> */
  protected void setItemID( final String itemID ) {
    this.itemID = itemID;
  }

  /** <p>returns the ID of this Item.</p> */
  protected String getItemID() {
    return itemID;
  }

  /** <p>sets the parent node for this Item, i.e. the Node  to which this
    * Item is added.</p> */
  void setParentNode( final Node parentNode ) {
    if( parentNode != null ) {
      setParent( parentNode.getParent() );
    }
    this.parentNode = parentNode;
  }
  
  void setParent( final WebContainer parent ) {
    this.parent = parent;
  }

  /** <p>returns the parent node of this Item, i.e. the Node to which this
    * Item is added.</p> */
  public Node getParentNode() {
    return this.parentNode;
  }
  
  public boolean isVisible() {
    boolean result = super.isVisible();
    if( this.parentNode != null ) {
      result = result && parentNode.isVisible();
    }
    return result;
  }

  /** Adds the specified WebActionListener to receive action events from
    * this Item. Action events occur when a user presses and releases
    * the mouse over the items label.
    * @param listener the WebActionListener
    */
  public void addWebActionListener( final WebActionListener listener ) {
    WebActionEvent.addListener( this, listener );
  }

  /** Removes the specified WebActionListener so that it no longer
    * receives action events from this Item. Action events occur
    * when a user presses and releases the mouse over this Item.
    * @param listener the WebActionListener
    */
  public void removeWebActionListener( final WebActionListener listener ) {
    WebActionEvent.removeListener( this, listener );
  }

  
  // attribute getters and setters
  ////////////////////////////////
  
  /** sets the display text of this Item */
  public void setLabel( final String label ) {
    this.label = label;
  }

  /** gets the display text of this Item */
  public String getLabel() {
    return label;
  }

  /** marks the current Item as the selected Item */
  public void mark() {
    tdItems.setMarked( itemID );
  }
  
  public boolean isMarked() {
    return tdItems.isMarked( this );
  }
}