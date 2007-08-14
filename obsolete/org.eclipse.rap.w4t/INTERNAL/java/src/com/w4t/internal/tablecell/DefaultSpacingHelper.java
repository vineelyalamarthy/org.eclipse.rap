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
import org.eclipse.rwt.internal.service.ContextProvider;
import org.eclipse.rwt.internal.service.IServiceStateInfo;
import org.eclipse.rwt.internal.util.HTML;

import com.w4t.WebTableCell;

/** <p>Additional spacing rendering (creates a TABLE within the cell, which has
  * padding and spacing).</p>
  * 
  * <p>This is the default spacing helper used in {@link org.eclipse.rwt.WebTableCell 
  * WebTableCell}.</p>
  */
public class DefaultSpacingHelper extends SpacingHelper {

  public void getSpacingStart( final WebTableCell cell ) throws IOException {
    HtmlResponseWriter out = getResponseWriter();
    if( hasSpacingOrPadding( cell ) ) {
      createCellOpener();
      out.startElement( HTML.TABLE, null );
      out.writeAttribute( HTML.BORDER, "0", null );
      out.writeAttribute( HTML.CELLPADDING, cell.getPadding(), null );
      out.writeAttribute( HTML.CELLSPACING, cell.getSpacing(), null );
      out.closeElementIfStarted();
      out.startElement( HTML.TR, null );
    }
  }

  public void getSpacingEnd( final WebTableCell cell ) throws IOException {
    HtmlResponseWriter out = getResponseWriter();
    if( hasSpacingOrPadding( cell ) ) {
      out.endElement( HTML.TR );
      out.endElement( HTML.TABLE );
      createCellCloser();
    }
  }

  // helping methods
  //////////////////
  
  static boolean hasSpacingOrPadding( final WebTableCell cell ) {
    return !cell.getSpacing().equals( "" ) || !cell.getPadding().equals( "" );
  }

  HtmlResponseWriter getResponseWriter() {
    IServiceStateInfo stateInfo = ContextProvider.getStateInfo();
    return stateInfo.getResponseWriter();
  }

  void createCellOpener() throws IOException {
    getResponseWriter().startElement( HTML.TD, null );
  }
  
  void createCellCloser() throws IOException {
    getResponseWriter().endElement( HTML.TD );
  }
}