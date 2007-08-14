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
import java.util.Vector;

import org.eclipse.rwt.internal.util.Assert;

import com.w4t.dhtml.renderinfo.ImageSet;


/** <p>A helping data structure for rendering TreeViews. A TreeNodeShift 
  * represents the images needed for TreeNodes and TreeLeaves added to 
  * a TreeView, arranged row by row. This includes empty placeholders, 
  * line images, and icon images for nodes and leaves. A TreeNodeShift 
  * is associated to each TreeView and filled and read out while rendering
  * that TreeView.</p>
  */
public class TreeNodeShift {
  
  /** the internal data structure of this TreeNodeShift. */
  private Vector nodeShift;
  /** whether the current state of this TreeNodeShift represents a row
    * whose node is the last child of the parent node (this results in 
    * different images for lines). */
  private boolean lastChild;
  
  private Hashtable imageSets;
  
  public TreeNodeShift( final String imageSetName ) {
    nodeShift = new Vector();
    imageSets = new Hashtable();
    imageSets.put( imageSetName, new ImageSet( imageSetName ) );
  }  

  public void addEmptyImage( final String imageSetName ) {
    nodeShift.add( findImageSet( imageSetName ).getEmpty() );
  }

  public void addLineImage( final String imageSetName ) {
    nodeShift.add( findImageSet( imageSetName ).getLine() );
  }
  
  /** <p>removes the last image from this TreeNodeShift.</p> */
  public void remove() {
    nodeShift.remove( nodeShift.size() - 1 );
  }
  
  public void clear() {
    nodeShift.clear();
  }

  public int getSize() {
    return nodeShift.size();
  }
  
  public String getImageName( final int index ) {
    return ( String )nodeShift.get( index );
  }

  public void setLastChild( final boolean lastChild ) {
    this.lastChild = lastChild;
  }
  
  public boolean isLastChild() {
    return lastChild;
  }

  public String getInner( final String imageSetName ) {
    return findImageSet( imageSetName ).getInner();    
  }

  public String getLast( final String imageSetName ) {
    return findImageSet( imageSetName ).getLast();
  }
  
  public String getPlusInner( final String imageSetName ) {
    return findImageSet( imageSetName ).getPlusInner();   
  }

  public String getPlusLast( final String imageSetName ) {
    return findImageSet( imageSetName ).getPlusLast();
  }

  public String getMinusInner( final String imageSetName ) {
    return findImageSet( imageSetName ).getMinusInner();
  }
  
  public String getMinusLast( final String imageSetName ) {
    return findImageSet( imageSetName ).getMinusLast();
  }
  
  public String getExpandedWithChildrenIcon( final String imageSetName ) {
    return findImageSet( imageSetName ).getExpandedWithChildrenIcon();
  }
  
  public String getExpandedWithoutChildrenIcon( final String imageSetName ) {
    return findImageSet( imageSetName ).getExpandedWithoutChildrenIcon();
  }
  
  public String getCollapsedIcon( final String imageSetName ) {
    return findImageSet( imageSetName ).getCollapsedIcon();
  }
  
  public String getLeafIcon( final String imageSetName ) {
    return findImageSet( imageSetName ).getLeafIcon();
  }

  
  // helping methods
  //////////////////
  
  private ImageSet findImageSet( final String name ) {
    Assert.isNotNull( name );
    ImageSet result = ( ImageSet )imageSets.get( name );
    if( result == null ) {
      result = new ImageSet( name );
      imageSets.put( name, result );
    }
    return result;
  }
}