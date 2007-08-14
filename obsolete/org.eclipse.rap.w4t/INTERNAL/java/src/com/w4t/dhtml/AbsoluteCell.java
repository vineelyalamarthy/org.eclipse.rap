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

import java.io.IOException;

import org.eclipse.rwt.internal.lifecycle.HtmlResponseWriter;
import org.eclipse.rwt.internal.util.HTML;

import com.w4t.WebComponent;
import com.w4t.types.WebColor;
import com.w4t.util.DefaultColorScheme;


/**
  * <p>A rectangle gui component that appears as a colored rectangle and
  * which can be positioned absolutely. No more can be set on an
  * AbsoluteCell than width and height (in pixels), border and color.</p>
  */
public class AbsoluteCell extends WebComponent {

  /** </p>the width of this AbsoluteCell, in pixels.</p> */
  private int width = 0;
  /** </p>the height of this AbsoluteCell, in pixels.</p> */
  private int height = 0;
  /** </p>the border of this AbsoluteCell.</p> */
  private String border = "";
  /** <p>the color of this AbsoluteCell.</p> */
  private WebColor color;


  /** <p>constructs a new AbsoluteCell.<p> */
  public AbsoluteCell() {
    super();
    String key = DefaultColorScheme.ABSOLUTE_LAYOUT_BG;
    this.color = new WebColor( DefaultColorScheme.get( key ) );
  }

  /** <p>constructs a new AbsoluteCell with the specified
    * width and height.<p> */
  public AbsoluteCell( final int width, final int height ) {
    this();
    this.width = width;
    this.height = height;
  }

  /** <p>constructs a new AbsoluteCell with the specified
    * width and height.<p> */
  public AbsoluteCell( final int width, 
                       final int height, 
                       final WebColor color ) 
  {
    super();
    this.width = width;
    this.height = height;
    this.color = color;
  }

  /** <p>creates the actual html representation of this AbsoluteCell.</p> */
  protected void createRenderContent( final HtmlResponseWriter out ) 
    throws IOException 
  {
    out.startElement( HTML.TABLE, null );
    out.writeAttribute( HTML.CELLSPACING, "0", null );
    out.writeAttribute( HTML.CELLPADDING, "0", null );
    if( !"".equals( border )) {
      out.writeAttribute( HTML.BORDER, border, null );
    }
    out.writeAttribute( HTML.BGCOLOR, color, null );
    out.closeElementIfStarted();
    out.startElement( HTML.TR, null );
    out.startElement( HTML.TD, null );
    out.startElement( HTML.IMG, null );
    out.writeAttribute( HTML.SRC, "resources/images/transparent.gif", null );
    out.writeAttribute( HTML.WIDTH, String.valueOf( width ), null );
    out.writeAttribute( HTML.HEIGHT, String.valueOf( height ), null );
    out.closeElementIfStarted();
    out.endElement( HTML.IMG );
    out.endElement( HTML.TD );
    out.endElement( HTML.TR );
    out.endElement( HTML.TABLE );
  }

  /** <p>returns a string with the JavaScript Eventhandler used
    * for this WebComponent.</p> */
  protected String getEventHandler() {
    // This returns only an empty String for there will be no events fired
    // by an AbsoluteCell.
    return "";
  }

  
  // attribute setters and getters
  ////////////////////////////////

  /** </p>sets the width attribute of this AbsoluteCell, in pixels.</p> */
  public void setWidth( final int width ) {
    this.width = width;
  }

  /** </p>returns the currently set width of this AbsoluteCell,
    * in pixels.</p> */
  public int getWidth() {
    return width;
  }

  /** </p>sets the height attribute of this AbsoluteCell, in pixels.</p> */
  public void setHeight( final int height ) {
    this.height = height;
  }

  /** </p>returns the currently set height of this AbsoluteCell,
    * in pixels.</p> */
  public int getHeight() {
    return height;
  }

  /** </p>sets the border attribute of this AbsoluteCell.</p> */
  public void setBorder( final String border ) {
    this.border = border;
  }

  /** </p>returns the currently set border of this AbsoluteCell.</p> */
  public String getBorder() {
    return border;
  }

  /** </p>sets the color of this AbsoluteCell.</p> */
  public void setColor( final WebColor color ) {
    this.color = color;
  }

  /** </p>returns the currently set color of this AbsoluteCell.</p> */
  public WebColor getColor() {
    return color;
  }
}