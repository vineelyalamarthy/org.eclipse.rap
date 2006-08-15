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

import com.w4t.Decorator;
import com.w4t.WebLabel;

/** <p>A Decorator class which implements automatic horizontal and
 *  vertical scrolling for its content component.</p>
 *  <p>To avoid display troubles try to set an absolute or relative width to the
 *  area containing this ScrollPane. Possible trouble could be TreeViews moving
 *  through your browser window on expand or collapse.</p>
 */
public class WebScrollPane extends Decorator {
 
  /** the WebScrollpanes height in pixel */
  private int height = 100;
  /** the WebScrollpanes width in pixel */
  private int width = 100;
  /** the horizontal position (in pixels) of the scrollpanes content */
  private int scrollX = 0;
  /** the vertical position (in pixels) of the scrollpanes content */
  private int scrollY = 0;


  /** Creates a new instance of WebScrollPane */
  public WebScrollPane() {
    setContent( new WebLabel( "put your content here..." ) );
  }  
  
  /** <p>returns a path to an image that represents this WebComponent
   * (widget icon).</p> */
  public static String retrieveIconName() {
    return "resources/images/icons/scrollpane.gif";
  } 
  
  /** sets the WebScrollpanes height in pixel */
  // FIXME [rh] throw IllegalArgumentException instad of silently ignoring height?
  public void setHeight( final int height ) {
    if( height > 0 ) {
      this.height = height;
    }
  }
  
  /** returns the WebScrollpanes height in pixel */
  public int getHeight() {
    return height;
  }
  
  /** sets the WebScrollpanes width in pixel */
  // FIXME [rh] throw IllegalArgumentException instad of silently ignoring width?
  public void setWidth( final int width ) {
    if( width > 0 ) {
      this.width = width;
    }
  }
  
  /** returns the WebScrollpanes width in pixel */
  public int getWidth() {
    return width;
  }
  
  /** sets the horizontal position (in pixels) of the scrollpanes content */
  public void setScrollX( final int scrollX ) {
    this.scrollX = scrollX;
  }
  
  /** returns the horizontal position (in pixels) of the scrollpanes content */
  public int getScrollX() {
    return scrollX;
  }
  
  /** sets the vertical position (in pixels) of the scrollpanes content */  
  public void setScrollY( final int scrollY ) {
    this.scrollY = scrollY;
  }
  
  /** returns the vertical position (in pixels) of the scrollpanes content */
  public int getScrollY() {
    return scrollY;
  }
}