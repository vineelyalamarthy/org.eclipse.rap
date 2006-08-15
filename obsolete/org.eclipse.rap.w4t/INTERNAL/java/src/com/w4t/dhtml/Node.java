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

import java.util.*;
import com.w4t.WebContainer;
import com.w4t.event.WebActionListener;
import com.w4t.event.WebRenderListener;


/** <p>The abstract superclass for all nodes (items which may have
  * other items added as children).</p>
  */
public abstract class Node extends Item {

  /** <p>contains all nodes which are added to this Node.</p> */
  protected List nodeList = new ArrayList();
  /** <p>contains all nodes which are added to this Node.</p> */
  protected List leafList = new ArrayList();

  
  /** <p>adds the specified item as child to this Node.</p> */
  public void addItem( final Item item ) {
    item.setParentNode( this );
    if( item instanceof Node ) {
      nodeList.add( item );
    } else {
      leafList.add( item );
    }
    addToTreeData( item );
  }

  /** <p>adds the specified item at specified position
    * as child to this Node.</p> */
  private void addItem( final int pos, final Item item ) {
    item.setParentNode( this );
    if( item instanceof Node ) {
      nodeList.add( pos, item );
    } else {
      leafList.add( pos, item );
    }
    addToTreeData( item );
  }

  /** <p>removes the specified item from this Node.</p> */
  public void removeItem( final Item item ) {
    item.setParent( null );
    item.setParentNode( null );
    if( item instanceof Node ) {
      nodeList.remove( item );
    } else {
      leafList.remove( item );
    }
    removeFromTreeData( item );
  }
  
  /** <p>removes all items from this Node.</p> */
  public void removeAllItems() {
    Node[] childNodes = this.getNodes();
    for( int i = 0; i < childNodes.length; i++ ) {
      childNodes[ i ].remove();
    }
    Leaf[] childLeaves = this.getLeaves();
    for( int i = 0; i < childLeaves.length; i++ ) {
      childLeaves[ i ].remove();
    }
  }
  
  void setParent( final WebContainer parent ) {
    super.setParent( parent );
    Item[] children = getItem();
    for( int i = 0; i < children.length; i++ ) {
      children[ i ].setParent( parent );
    }    
  }

  // helping methods (data structure management)
  //////////////////////////////////////////////

  /** <p>adds the specified Item to the TreeData structure and also all
   * further items that are added to it recursively.</p> */
  private void addToTreeData( final Item item ) {
    // get the item list of the tree, if one exists, else create one
    if( tdItems == null ) {
      tdItems = new TreeData();
    }
    item.setItemList( tdItems );
    // add item to the item list of the tree
    String id = item.getItemID();
    if( !tdItems.containsItem( id ) ) {
      tdItems.put( id, item );
    }
    // if there are some more items added to item, add them to the list
    if( item instanceof Node ) {
      Node node = ( Node )item;
      Node[] childNodes = node.getNodes();
      for( int i = 0; i < childNodes.length; i++ ) {
        addToTreeData( childNodes[ i ] );
      }
      Leaf[] childLeaves = node.getLeaves();
      for( int i = 0; i < childLeaves.length; i++ ) {
        addToTreeData( childLeaves[ i ] );
      }
    }
  }

  /** <p>removes the specified Item from the TreeData structure and also all
   * further items that are added to it recursively.</p> */
  private void removeFromTreeData( final Item item ) {
    if( tdItems != null ) {
      String id = item.getItemID();
      tdItems.remove( id );
    }
    // remove all items added to item from the TreeData list
    if( item instanceof Node ) {
      Node node = ( Node )item;
      Node[] childNodes = node.getNodes();
      for( int i = 0; i < childNodes.length; i++ ) {
        removeFromTreeData( childNodes[ i ] );
      }
      Leaf[] childLeaves = node.getLeaves();
      for( int i = 0; i < childLeaves.length; i++ ) {
        removeFromTreeData( childLeaves[ i ] );
      }
    }
  }

  
  // event handling methods
  /////////////////////////
  
  /** <p>adds the specified WebActionListener to this Node and all items that
    * are added to it.</p> */
  public void addWebActionListenerRecursively( final WebActionListener l ) {
    this.addWebActionListener( l );
    for( int i = 0; i < nodeList.size(); i++ ) {
      Node childNode = ( Node )nodeList.get ( i );
      childNode.addWebActionListenerRecursively( l );
    }
    for( int i = 0; i < leafList.size(); i++ ) {
      Leaf leaf = ( Leaf )leafList.get( i );
      leaf.addWebActionListener( l );
    }
  }

  /** <p>removes the specified WebActionListener from this Node and
    * all items that are added to it.</p> */
  public void removeWebActionListenerRecursively( final WebActionListener l ) {
    this.removeWebActionListener( l );
    for( int i = 0; i < nodeList.size(); i++ ) {
      Node childNode = ( Node )nodeList.get ( i );
      childNode.removeWebActionListenerRecursively( l );
    }
    for( int i = 0; i < leafList.size(); i++ ) {
      Leaf leaf = ( Leaf )leafList.get( i );
      leaf.removeWebActionListener( l );
    }
  }

  /** <p>adds the specified WebRenderListener to this Node and all items that
    * are added to it.</p> */
  public void addWebRenderListenerRecursively( final WebRenderListener l ) {
    this.addWebRenderListener( l );
    for( int i = 0; i < nodeList.size(); i++ ) {
      Node childNode = ( Node )nodeList.get ( i );
      childNode.addWebRenderListenerRecursively( l );
    }
    for( int i = 0; i < leafList.size(); i++ ) {
      Leaf leaf = ( Leaf )leafList.get( i );
      leaf.addWebRenderListener( l );
    }
  }

  /** <p>removes the specified WebRenderListener from this Node and
    * all items that are added to it.</p> */
  public void removeWebRenderListenerRecursively( final WebRenderListener l ) {
    this.removeWebRenderListener( l );
    for( int i = 0; i < nodeList.size(); i++ ) {
      Node childNode = ( Node )nodeList.get ( i );
      childNode.removeWebRenderListenerRecursively( l );
    }
    for( int i = 0; i < leafList.size(); i++ ) {
      Leaf leaf = ( Leaf )leafList.get( i );
      leaf.removeWebRenderListener( l );
    }
  }

  
  // attribute getters and setters
  ////////////////////////////////
  
  /** <p>returns an array of Node objects containing all nodes that were added
    * to this Node using the Node.addItem() method.</p> */
  public Node[] getNodes() {
    int size = nodeList.size();
    Node nodes[] = new Node[ size ];
    for( int i = 0; i < size; i++ ) {
      nodes[ i ] = ( Node )nodeList.get( i );
    }
    return nodes;
  }

  /** <p>returns an array of Leaf objects containing all leaves that were added
    * to this Node using the Node.addItem() method.</p> */
  public Leaf[] getLeaves() {
    int size = leafList.size();
    Leaf leaves[] = new Leaf[ size ];
    for( int i = 0; i < size; i++ ) {
      leaves[ i ] = ( Leaf )leafList.get( i );
    }
    return leaves;
  }

  /** <p> sets specified items as child to this Node. </p> */
  public void setItem( final Item[] items ) {
    this.removeAllItems();
    for ( int i=0; i<items.length; i++ ) {
      addItem( items[i] );
    }
  }

  public int getItemCount() {
    return nodeList.size() + leafList.size();
  }
  
  /** <p> gets child items of this Node. </p> */
  public Item[] getItem() {
    int nodeListSize = nodeList.size();
    int leafListSize = leafList.size();
    Item[] items = new Item[ nodeListSize + leafListSize ];
    for ( int i=0; i<nodeListSize; i++ ) {
      items[ i ] = ( Node ) nodeList.get( i );
    }
    for ( int i=0; i<leafListSize; i++ ) {
      items[ nodeListSize + i ] = ( Leaf ) leafList.get( i );
    }
    return items;
  }

  /** <p> sets specified item at specified position
    * as child to this Node. </p> */
  public void setItem( final int pos, final Item item ) {
    addItem( pos, item );
  }

  /** <p> gets child item at specified position of this Node. </p> */
  public Item getItem( final int pos ) {
    Item item = null;
    int nodeListSize = nodeList.size();
    int leafListSize = leafList.size();
    if ( pos < nodeListSize ) {
      item = ( Item )nodeList.get( pos );
    } else if ( pos < nodeListSize + leafListSize ) {
      item = ( Item )leafList.get( pos - nodeListSize );
    }
    return item;  
  }
  
  /** <p>returns a copy of Node.</p> */
  public Object clone() throws CloneNotSupportedException {
    Node result = ( Node )super.clone();

    // copy the node list    
    result.nodeList = new Vector();
    for( int i = 0; i < nodeList.size(); i++ ) {
      Object nodeClone = ( ( Node )nodeList.get( i ) ).clone();
      try {
        result.addItem( ( Item )nodeClone );
      } catch ( Exception shouldNotHappen ) {
        System.out.println( "Exception cloning Node: " + shouldNotHappen );
      }
    }    
    
    // copy the leaf list
    result.leafList = new Vector();
    for( int i = 0; i < leafList.size(); i++ ) {
      Object leafClone = ( ( Leaf )leafList.get( i ) ).clone();
      try {
        result.addItem( ( Leaf )leafClone );
      } catch( Exception shouldNotHappen ) {
        System.err.println( "Exception cloning Node: " + shouldNotHappen );
      }
    }    
    return result;    
  }    
}