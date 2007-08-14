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
import org.eclipse.rwt.internal.service.ContextProvider;
import org.eclipse.rwt.internal.service.IServiceStateInfo;
import org.eclipse.rwt.internal.util.HTML;

import com.w4t.*;
import com.w4t.types.WebColor;
import com.w4t.util.DefaultColorScheme;


/**
  * <p>A Layout manager that knows how to position the WebComponents added to
  * the WebContainer on which it is set as layout manager absolutely.</p>
  */
public class AbsoluteLayout implements WebLayout {

  private static final String ABSOLUTE_LAYOUT_BG 
    = DefaultColorScheme.ABSOLUTE_LAYOUT_BG;
  private static final WebColor DEFAULT_BG_COLOR
    = new WebColor( DefaultColorScheme.get( ABSOLUTE_LAYOUT_BG ) );
  
  /** <p>the width for the complete panel which is layouted by
    * this AbsoluteLayout.</p> */
  private String width;
  /** <p>the height for the complete panel which is layouted by
    * this AbsoluteLayout.</p> */
  private String height;
  /** <p>the border for the complete panel which is layouted by
    * this AbsoluteLayout.</p> */
  private String border ;
  /** <p>the background color for the complete panel which is layouted by
    * this AbsoluteLayout.</p> */
  private WebColor bgColor;

  public AbsoluteLayout() {
    width = "";
    height = "";
    border = "";
    bgColor = DEFAULT_BG_COLOR;
  }

  public WebTableCell getRegion( final Object constraint ) {
    return null;
  }

  /** <p>returns a clone of this WebCardayout.</p>
    * <p>Cloning a WebLayout involves a copy of all settings and inits, but no
    * cloning or copying added components
    * ( see @link WebContainer.clone() ).</p> */
  public Object clone() throws CloneNotSupportedException {
    Object clone = super.clone();
    // TODO: do all needed inits for clone
    // (no constructors are called on cloning)
    return clone;
  }

  /** <p>returns the Positioner that was added to the parent WebContainer of
    * this AbsoluteLayout with the specified constraint.</p> */
  public Area getArea( final Object constraint ) {
    // TODO
    return new AbsolutePositioner();
  }

  /** <p>checks if the constraint parameter in the add method of a WebContainer
    * has the correct type.</p>
    * @param constraint specifies the constraint to check */
  public boolean checkConstraint( final Object constraint ) {
    return ( constraint instanceof Rectangle );
  }


  // rendering methods
  ////////////////////

  public void layoutWebContainer( final WebContainer parent ) throws IOException {
    IServiceStateInfo stateInfo = ContextProvider.getStateInfo();
    HtmlResponseWriter out = stateInfo.getResponseWriter();

    // render the outer div-tag header
    createHeader( parent, out );

    // render the components (call the render of the Positioners added)
    int componentCount = parent.getWebComponentCount();
    for( int i = 0; i < componentCount; i++ ) {
      WebComponent wc = parent.get( i );
      Rectangle recConstraint = ( Rectangle )parent.getConstraint( i );
      AbsolutePositioner pos = new AbsolutePositioner();
      pos.setContent( wc );
      pos.setPosition( recConstraint );
      pos.setID( String.valueOf( i ) );
      pos.render( out );
    }

    // render the outer div-tag footer
    createFooter( out );
  }

  /** helping method for layoutWebContainer; creates the html string for the
    * outer div-tag header and appends it to out. 
   * @throws IOException */
  private void createHeader( final WebComponent parent,
                             final HtmlResponseWriter out ) 
    throws IOException 
  {
    out.startElement( HTML.TABLE, null );
    out.writeAttribute( HTML.ID, parent.getUniqueID() + "absLyt", null );
    out.writeAttribute( HTML.CELLPADDING, "0", null );
    out.writeAttribute( HTML.CELLSPACING, "0", null );
    if( !"".equals( border ) ) {
      out.writeAttribute( HTML.BORDER, border, null );
    }
    out.closeElementIfStarted();
    // the first row o the rendered table contains a horizontal blind image
    // in order to assure the width which was set on this layout
    out.startElement( HTML.TR, null );
    out.startElement( HTML.TD, null );
    out.endElement( HTML.TD );
    out.startElement( HTML.TD, null );
    out.startElement( HTML.IMG, null );
    out.writeAttribute( HTML.SRC, "resources/images/transparent.gif", null );
    out.writeAttribute( HTML.ALIGN, "top", null );
    out.writeAttribute( HTML.BORDER, "0", null );
    out.writeAttribute( HTML.WIDTH, width, null );
    out.writeAttribute( HTML.HEIGHT, "1", null );
    out.endElement( HTML.TD );
    out.endElement( HTML.TR );
    // the second row contains in the first column a vertical blind image
    // in order to assure the height which was set on this layout
    out.startElement( HTML.TR, null );
    out.startElement( HTML.TD, null );
    out.startElement( HTML.IMG, null );
    out.writeAttribute( HTML.SRC, "resources/images/transparent.gif", null );
    out.writeAttribute( HTML.BORDER, "0", null );
    out.writeAttribute( HTML.WIDTH, "1", null );
    out.writeAttribute( HTML.HEIGHT, height, null );
    out.endElement( HTML.TD );
    // the table cell for the components contains first a div tag
    // TODO: build the id in here
    out.startElement( HTML.TD, null );
    if( !"".equals( bgColor.toString() ) ) {
      out.writeAttribute( HTML.BGCOLOR, bgColor, null );
    }
    out.closeElementIfStarted();
    out.startElement( HTML.DIV, null );
    out.writeAttribute( HTML.STYLE, "position:absolute;", null );
    out.closeElementIfStarted();
  }

  /** helping method for layoutWebContainer; creates the html string for the
    * outer div-tag footer and appends it to out. 
   * @throws IOException */
  private void createFooter( final HtmlResponseWriter out ) throws IOException {
    out.endElement( HTML.DIV );
    out.endElement( HTML.TD );
    out.endElement( HTML.TR );
    out.endElement( HTML.TABLE );
  }


  // attribute getters and setters
  ////////////////////////////////

  /** <p>sets the width for the complete panel which is layouted by
    * this AbsoluteLayout.</p> */
  public void setWidth( final String width ) {
    this.width = width;
  }

  /** <p>returns the width for the complete panel which is layouted by
    * this AbsoluteLayout.</p> */
  public String getWidth() {
    return width;
  }

  /** <p>sets the height for the complete panel which is layouted by
    * this AbsoluteLayout.</p> */
  public void setHeight( final String height ) {
    this.height = height;
  }

  /** <p>returns the height for the complete panel which is layouted by
    * this AbsoluteLayout.</p> */
  public String getHeight() {
    return height;
  }

  /** <p>sets the background color for the complete panel which is layouted by
    * this AbsoluteLayout.</p> */
  public void setBgColor( final WebColor bgColor ) {
    this.bgColor = bgColor;
  }

  /** <p>returns the background color which is set for the complete panel
    * that is layouted by this AbsoluteLayout.</p> */
  public WebColor getBgColor() {
    return bgColor;
  }

  /** <p>sets the border for the complete panel which is layouted by
    * this AbsoluteLayout.</p> */
  public void setBorder( final String border ) {
    this.border = border;
  }

  /** <p>returns the border for the complete panel which is layouted by
    * this AbsoluteLayout.</p> */
  public String getBorder() {
    return border;
  }
}