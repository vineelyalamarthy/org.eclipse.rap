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
package com.w4t.dhtml.renderinfo;

import com.w4t.RenderUtil;
import com.w4t.util.ImageRegistry;


/** <p>An ImageSet represents the various icon and other images for a
  * {@link org.eclipse.rwt.dhtml.TreeNode TreeNode}.</p>
  */
public class ImageSet {

  /** <p>separates the folder with the shared images from the subfolder 
    * with the special images in the name for this ImageSet.</p> */
  public static final char SHARED_FOLDER_DELIMITER = '#';
  
  private final static String EXTENSION = ".gif";
  private static final char SLASH = '/';

  private String imageSetName;

  /** <p>creates a new ImageSet with the specified image set name.</p> */
  public ImageSet( final String imageSetName ) {
    this.imageSetName = imageSetName;
  }
  
  public String getEmpty() {
    return createImage( "_Empty" );
  }
  
  public String getLine() {
    return createImage( "_Line" );
  }

  public String getInner() {
    return createImage( "_Inner" );
  }

  public String getLast() {
    return createImage( "_Last" );
  }
  
  public String getPlusInner() {
    return createImage( "_PlusInner" );
  }

  public String getPlusLast() {
    return createImage( "_PlusLast" );
  }

  public String getMinusInner() {
    return createImage( "_MinusInner" );
  }
  
  public String getMinusLast() {
    return createImage( "_MinusLast" );
  }
  
  public String getExpandedWithChildrenIcon() {
    return createImage( "_IconExpWithChildren" );
  }
  
  public String getExpandedWithoutChildrenIcon() {
    return createImage( "_IconExpWithoutChildren" );
  }
  
  public String getCollapsedIcon() {
    return createImage( "_IconCol" );
  }
  
  public String getLeafIcon() {
    return createImage( "_LeafIcon" );
  }

  private String createImage( final String name ) {
    String path = imageSetName;
    if( isDivided() ) {
      path = checkFile( name ) ? getSpecialPath() : getSharedPath();
    }
    return RenderUtil.resolveLocation( path + name + EXTENSION );
  }
  
  
  // helping methods
  //////////////////
  
  private boolean isDivided() {
    return imageSetName.indexOf( SHARED_FOLDER_DELIMITER ) != -1;
  }
  
  private String getSharedPath() {
    int delimIndex = imageSetName.indexOf( SHARED_FOLDER_DELIMITER );
    return imageSetName.substring( 0, delimIndex ) + SLASH;
  }
  
  private String getSpecialPath() {
    return imageSetName.replace( SHARED_FOLDER_DELIMITER, SLASH );
  }
  
  private boolean checkFile( final String name ) {
    return ImageRegistry.check( getSpecialPath() + name + EXTENSION );
  }
}