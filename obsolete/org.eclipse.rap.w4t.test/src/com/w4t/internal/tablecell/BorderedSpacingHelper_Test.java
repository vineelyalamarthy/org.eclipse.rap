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
import junit.framework.TestCase;
import com.w4t.*;
import com.w4t.engine.service.ContextProvider;
import com.w4t.types.WebColor;
import com.w4t.util.browser.Mozilla1_6;

public class BorderedSpacingHelper_Test extends TestCase {

  public void testMarkup() throws IOException {
    Fixture.fakeBrowser( new Mozilla1_6( true, true ) );
    BorderedSpacingHelper spacingHelper 
      = new BorderedSpacingHelper( "bottom", 1, new WebColor( "black" ) );
    WebTableCell cell = new WebTableCell();
    // render with neither spacing nor padding
    HtmlResponseWriter out = new HtmlResponseWriter();
    ContextProvider.getStateInfo().setResponseWriter( out );
    spacingHelper.getSpacingStart( cell );
    spacingHelper.getSpacingEnd( cell );
    String markup = Fixture.getAllMarkup( out  );
    assertEquals( "", markup );
    // render with spacing
    out = new HtmlResponseWriter();
    ContextProvider.getStateInfo().setResponseWriter( out );
    cell.setSpacing( "11" );
    spacingHelper.getSpacingStart( cell );
    spacingHelper.getSpacingEnd( cell );
    markup = Fixture.getAllMarkup( out  );
    String expected 
      = "<td style=\"border-bottom:1px solid #000000\">"
      + "<table border=\"0\" cellpadding=\"\" cellspacing=\"11\">" 
      + "<tr></tr></table></td>";
    assertEquals( expected, markup );
    // render with padding
    out = new HtmlResponseWriter();
    ContextProvider.getStateInfo().setResponseWriter( out );
    cell.setSpacing( "" );
    cell.setPadding( "12" );
    spacingHelper.getSpacingStart( cell );
    spacingHelper.getSpacingEnd( cell );
    markup = Fixture.getAllMarkup( out  );
    expected 
      = "<td style=\"border-bottom:1px solid #000000\">" 
      + "<table border=\"0\" cellpadding=\"12\" cellspacing=\"\">"
      + "<tr></tr></table></td>";
    assertEquals( expected, markup );
  }

  protected void setUp() throws Exception {
    Fixture.setUp();
    Fixture.createContext();
  }

  protected void tearDown() throws Exception {
    Fixture.tearDown();
    Fixture.removeContext();
  }
}
