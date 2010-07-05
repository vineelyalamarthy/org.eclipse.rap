/*******************************************************************************
 * Copyright (c) 2002, 2008 Innoopract Informationssysteme GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Innoopract Informationssysteme GmbH - initial API and implementation
 ******************************************************************************/
package org.eclipse.swt.graphics;

import junit.framework.TestCase;

import org.eclipse.rwt.Fixture;
import org.eclipse.rwt.graphics.Graphics;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;


public class Graphics_Test extends TestCase {

  public void testTextExtentNull() {
    Font font = Graphics.getFont( "Arial", 12, SWT.BOLD );
    try {
      Graphics.textExtent( font , null, 0 );
      fail( "Null string should throw IAE" );
    } catch( final IllegalArgumentException e ) {
      // expected
    }
    try {
      Graphics.textExtent( null, "", 0 );
      fail( "Null font should throw IAE" );
    } catch( final IllegalArgumentException e ) {
      // expected
    }
  }

  public void testStringExtentNull() {
    Font font = Graphics.getFont( "Arial", 12, SWT.BOLD );
    try {
      Graphics.stringExtent( font , null );
      fail( "Null string should throw IAE" );
    } catch( final IllegalArgumentException e ) {
      // expected
    }
    try {
      Graphics.stringExtent( null, "" );
      fail( "Null font should throw IAE" );
    } catch( final IllegalArgumentException e ) {
      // expected
    }
  }
  
  public void testGetCharHeightNull() {
    try {
      Graphics.getCharHeight( null );
      fail( "Null font should throw IAE" );
    } catch( final IllegalArgumentException e ) {
      // expected
    }
  }
  
  public void testGetAvgCharWidth() {
    Display display = new Display();
    float result = Graphics.getAvgCharWidth( display.getSystemFont() );
    assertTrue( result > 0 );
  }

  public void testGetAvgCharWidthNull() {
    try {
      Graphics.getAvgCharWidth( null );
      fail( "Null font should throw IAE" );
    } catch( final IllegalArgumentException e ) {
      // expected
    }
  }
  
  public void testGetCursor() {
    Display display = new Display();
    Cursor cursor = Graphics.getCursor( SWT.CURSOR_ARROW );
    assertSame( display.getSystemCursor( SWT.CURSOR_ARROW ), cursor );
  }

  protected void setUp() throws Exception {
    Fixture.setUp();
  }

  protected void tearDown() throws Exception {
    Fixture.tearDown();
  }
}
