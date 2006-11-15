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
package com.w4t.engine.util;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import junit.framework.TestCase;
import com.w4t.Fixture;
import com.w4t.util.DefaultColorScheme;


public class DefaultColorScheme_Test extends TestCase {
  
  protected void setUp() throws Exception {
    Fixture.setUp();
    Fixture.createContext( false );
  }
  
  protected void tearDown() throws Exception {
    Fixture.tearDown();
    Fixture.removeContext();
  }
  
  public void testColorSchemeReadingFromResource() throws Exception {
    PrintStream outBuffer = System.out;
    ByteArrayOutputStream streamBuffer = new ByteArrayOutputStream();
    System.setOut( new PrintStream( streamBuffer ) );
    String color = DefaultColorScheme.get( DefaultColorScheme.WEB_OBJECT_BG );
    assertEquals( "#ffffff", color );
    assertEquals( 0, streamBuffer.toByteArray().length );
    System.setOut( outBuffer );
  }
}
