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
package com.w4t.internal.tablecell;

import java.io.IOException;

import org.eclipse.rwt.internal.lifecycle.HtmlResponseWriter;
import org.eclipse.rwt.internal.util.HTML;

import com.w4t.types.WebColor;

/** <p>additional spacing rendering (creates a TABLE within the cell, which has
  * padding and spacing, and border styling).</p>
  * 
  * <p>This is the spacing helper used in the renderer for 
  * {@link org.eclipse.rwt.WebCardLayout WebCardLayout}.</p>
  */
public class BorderedSpacingHelper extends DefaultSpacingHelper {

  private String borderPosition;
  private int borderWidth;
  private WebColor borderColor;
  private String width;

  public BorderedSpacingHelper( final String borderPosition,
                                final int borderWidth,
                                final WebColor borderColor ) 
  {
    this( borderPosition, borderWidth, borderColor, "" );
  }

  public BorderedSpacingHelper( final String borderPosition,
                                final int borderWidth,
                                final WebColor borderColor,
                                final String width ) 
  {
    super();
    this.borderPosition = borderPosition;
    this.borderWidth = borderWidth;
    this.borderColor = borderColor;
    this.width = width;
  }

  void createCellOpener()  throws IOException {
    HtmlResponseWriter out = getResponseWriter();
    out.startElement( HTML.TD, null );
    if( !width.equals( "" ) ) {
      out.writeAttribute( HTML.WIDTH, width, null );
    }
    StringBuffer style = new StringBuffer();
    style.append( "border-" );
    style.append( borderPosition );
    style.append( ":" );
    style.append( String.valueOf( borderWidth ) );
    style.append( "px " );
    style.append( "solid " );
    style.append( borderColor.toString() );
    out.writeAttribute( HTML.STYLE, style, null );
    out.closeElementIfStarted();
  }
}