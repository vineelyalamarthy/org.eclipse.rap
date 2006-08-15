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
package com.w4t.webformkit;

import junit.framework.TestCase;
import com.w4t.*;
import com.w4t.engine.service.ContextProvider;
import com.w4t.engine.service.IServiceStateInfo;
import com.w4t.util.browser.*;

/**
 * <p>Tests to ensure that the correct JavaScript libraries are rendered
 * for WebForms</p>
 */
public class JSLibrary_Test extends TestCase {
  
  private static final String EVENTHANDLER_DEFAULT 
    = "resources/js/eventhandler/eventhandler_default.js";
  private static final String EVENTHANDLER_IE 
    = "resources/js/eventhandler/eventhandler_ie.js";
  private static final String WINDOWMANAGER 
    = "resources/js/windowmanager/windowmanager.js";

  protected void setUp() throws Exception {
    Fixture.setUp();
    Fixture.createContext();
  }
  
  protected void tearDown() throws Exception {
    Fixture.tearDown();
    Fixture.removeContext();
  }
  
  public void testIe() throws Exception {
    WebForm form = Fixture.getEmptyWebFormInstance();
    Fixture.fakeBrowser( new Ie5( true ) );
    Fixture.fakeEngineForRender( form );
    Fixture.fakeResponseWriter();
    Fixture.renderComponent( form );
    assertJSLibraryCount( 2 );
    assertJSLibrary( EVENTHANDLER_IE );
    assertJSLibrary( WINDOWMANAGER );
    Fixture.fakeBrowser( new Ie5up( true ) );
    Fixture.fakeEngineForRender( form );
    Fixture.fakeResponseWriter();
    Fixture.renderComponent( form );
    assertJSLibraryCount( 2 );
    assertJSLibrary( EVENTHANDLER_IE );
    assertJSLibrary( WINDOWMANAGER );
    Fixture.fakeBrowser( new Ie5_5( true ) );
    Fixture.fakeEngineForRender( form );
    Fixture.fakeResponseWriter();
    Fixture.renderComponent( form );
    assertJSLibraryCount( 2 );
    assertJSLibrary( EVENTHANDLER_IE );
    assertJSLibrary( WINDOWMANAGER );
    Fixture.fakeBrowser( new Ie5_5up( true ) );
    Fixture.fakeEngineForRender( form );
    Fixture.fakeResponseWriter();
    Fixture.renderComponent( form );
    assertJSLibraryCount( 2 );
    assertJSLibrary( EVENTHANDLER_IE );
    assertJSLibrary( WINDOWMANAGER );
    Fixture.fakeBrowser( new Ie6( true ) );
    Fixture.fakeEngineForRender( form );
    Fixture.fakeResponseWriter();
    Fixture.renderComponent( form );
    assertJSLibraryCount( 2 );
    assertJSLibrary( EVENTHANDLER_IE );
    assertJSLibrary( WINDOWMANAGER );
    Fixture.fakeBrowser( new Ie6up( true ) );
    Fixture.fakeEngineForRender( form );
    Fixture.fakeResponseWriter();
    Fixture.renderComponent( form );
    assertJSLibraryCount( 2 );
    assertJSLibrary( EVENTHANDLER_IE );
    assertJSLibrary( WINDOWMANAGER );
    Fixture.fakeBrowser( new Ie7( true ) );
    Fixture.fakeEngineForRender( form );
    Fixture.fakeResponseWriter();
    Fixture.renderComponent( form );
    assertJSLibraryCount( 2 );
    assertJSLibrary( EVENTHANDLER_IE );
    assertJSLibrary( WINDOWMANAGER );
  }
  
  public void testMozilla() throws Exception {
    WebForm form = Fixture.getEmptyWebFormInstance();
    Fixture.fakeBrowser( new Mozilla1_6( true ) );
    Fixture.fakeEngineForRender( form );
    Fixture.fakeResponseWriter();
    Fixture.renderComponent( form );
    assertJSLibraryCount( 2 );
    assertJSLibrary( EVENTHANDLER_DEFAULT );
    assertJSLibrary( WINDOWMANAGER );
    Fixture.fakeBrowser( new Mozilla1_6up( true ) );
    Fixture.fakeEngineForRender( form );
    Fixture.fakeResponseWriter();
    Fixture.renderComponent( form );
    assertJSLibraryCount( 2 );
    assertJSLibrary( EVENTHANDLER_DEFAULT );
    assertJSLibrary( WINDOWMANAGER );
    Fixture.fakeBrowser( new Mozilla1_7( true ) );
    Fixture.fakeEngineForRender( form );
    Fixture.fakeResponseWriter();
    Fixture.renderComponent( form );
    assertJSLibraryCount( 2 );
    assertJSLibrary( EVENTHANDLER_DEFAULT );
    assertJSLibrary( WINDOWMANAGER );
    Fixture.fakeBrowser( new Mozilla1_7up( true ) );
    Fixture.fakeEngineForRender( form );
    Fixture.fakeResponseWriter();
    Fixture.renderComponent( form );
    assertJSLibraryCount( 2 );
    assertJSLibrary( EVENTHANDLER_DEFAULT );
    assertJSLibrary( WINDOWMANAGER );
  }
  
  public void testOpera() throws Exception {
    WebForm form = Fixture.getEmptyWebFormInstance();
    Fixture.fakeBrowser( new Opera8( true ) );
    Fixture.fakeEngineForRender( form );
    Fixture.fakeResponseWriter();
    Fixture.renderComponent( form );
    assertJSLibraryCount( 2 );
    assertJSLibrary( EVENTHANDLER_DEFAULT );
    assertJSLibrary( WINDOWMANAGER );
    Fixture.fakeBrowser( new Opera8up( true ) );
    Fixture.fakeEngineForRender( form );
    Fixture.fakeResponseWriter();
    Fixture.renderComponent( form );
    assertJSLibraryCount( 2 );
    assertJSLibrary( EVENTHANDLER_DEFAULT );
    assertJSLibrary( WINDOWMANAGER );
    Fixture.fakeBrowser( new Opera9( true ) );
    Fixture.fakeEngineForRender( form );
    Fixture.fakeResponseWriter();
    Fixture.renderComponent( form );
    assertJSLibraryCount( 2 );
    assertJSLibrary( EVENTHANDLER_DEFAULT );
    assertJSLibrary( WINDOWMANAGER );
    Fixture.fakeBrowser( new Opera9up( true ) );
    Fixture.fakeEngineForRender( form );
    Fixture.fakeResponseWriter();
    Fixture.renderComponent( form );
    assertJSLibraryCount( 2 );
    assertJSLibrary( EVENTHANDLER_DEFAULT );
    assertJSLibrary( WINDOWMANAGER );
  }
  
  public void testKonqueror() throws Exception {
    WebForm form = Fixture.getEmptyWebFormInstance();
    Fixture.fakeBrowser( new Konqueror3_1( true ) );
    Fixture.fakeEngineForRender( form );
    Fixture.fakeResponseWriter();
    Fixture.renderComponent( form );
    assertJSLibraryCount( 2 );
    assertJSLibrary( EVENTHANDLER_IE );
    assertJSLibrary( WINDOWMANAGER );
    Fixture.fakeBrowser( new Konqueror3_1up( true ) );
    Fixture.fakeEngineForRender( form );
    Fixture.fakeResponseWriter();
    Fixture.renderComponent( form );
    assertJSLibraryCount( 2 );
    assertJSLibrary( EVENTHANDLER_IE );
    assertJSLibrary( WINDOWMANAGER );
    Fixture.fakeBrowser( new Konqueror3_2( true ) );
    Fixture.fakeEngineForRender( form );
    Fixture.fakeResponseWriter();
    Fixture.renderComponent( form );
    assertJSLibraryCount( 2 );
    assertJSLibrary( EVENTHANDLER_IE );
    assertJSLibrary( WINDOWMANAGER );
    Fixture.fakeBrowser( new Konqueror3_2up( true ) );
    Fixture.fakeEngineForRender( form );
    Fixture.fakeResponseWriter();
    Fixture.renderComponent( form );
    assertJSLibraryCount( 2 );
    assertJSLibrary( EVENTHANDLER_IE );
    assertJSLibrary( WINDOWMANAGER );
    Fixture.fakeBrowser( new Konqueror3_3( true ) );
    Fixture.fakeEngineForRender( form );
    Fixture.fakeResponseWriter();
    Fixture.renderComponent( form );
    assertJSLibraryCount( 2 );
    assertJSLibrary( EVENTHANDLER_IE );
    assertJSLibrary( WINDOWMANAGER );
    Fixture.fakeBrowser( new Konqueror3_3up( true ) );
    Fixture.fakeEngineForRender( form );
    Fixture.fakeResponseWriter();
    Fixture.renderComponent( form );
    assertJSLibraryCount( 2 );
    assertJSLibrary( EVENTHANDLER_IE );
    assertJSLibrary( WINDOWMANAGER );
    Fixture.fakeBrowser( new Konqueror3_4( true ) );
    Fixture.fakeEngineForRender( form );
    Fixture.fakeResponseWriter();
    Fixture.renderComponent( form );
    assertJSLibraryCount( 2 );
    assertJSLibrary( EVENTHANDLER_IE );
    assertJSLibrary( WINDOWMANAGER );
    Fixture.fakeBrowser( new Konqueror3_4up( true ) );
    Fixture.fakeEngineForRender( form );
    Fixture.fakeResponseWriter();
    Fixture.renderComponent( form );
    assertJSLibraryCount( 2 );
    assertJSLibrary( EVENTHANDLER_IE );
    assertJSLibrary( WINDOWMANAGER );
  }
  
  public void testSafari() throws Exception {
    WebForm form = Fixture.getEmptyWebFormInstance();
    Fixture.fakeBrowser( new Safari2( true ) );
    Fixture.fakeEngineForRender( form );
    Fixture.fakeResponseWriter();
    Fixture.renderComponent( form );
    assertJSLibraryCount( 2 );
    assertJSLibrary( EVENTHANDLER_DEFAULT );
    assertJSLibrary( WINDOWMANAGER );
    Fixture.fakeBrowser( new Safari2up( true ) );
    Fixture.fakeEngineForRender( form );
    Fixture.fakeResponseWriter();
    Fixture.renderComponent( form );
    assertJSLibraryCount( 2 );
    assertJSLibrary( EVENTHANDLER_DEFAULT );
    assertJSLibrary( WINDOWMANAGER );
  }
  
  private HtmlResponseWriter getResponseWriter() {
    IServiceStateInfo stateInfo = ContextProvider.getStateInfo();
    return stateInfo.getResponseWriter();
  }
  
  private void assertJSLibraryCount( final int expectedCount ) {
    int actual = getResponseWriter().getJSLibrariesCount();
    assertEquals( "Wrong number of JavaScript libraries", 
                  expectedCount, 
                  actual );
  }
  
  private void assertJSLibrary( final String libraryName ) {
    String[] libraries = getResponseWriter().getJSLibraries();
    boolean found = false;
    for( int i = 0; !found && i < libraries.length; i++ ) {
      found = libraryName.equals( libraries[ i ] );
    }
    assertTrue(   "Expected JS-library " + libraryName + " was not rendered. "
                + "Rendered libs: " + toString( libraries ),
                found );
  }
  
  private static String toString( final String[] array ) {
    StringBuffer result = new StringBuffer();
    for( int i = 0; i < array.length; i++ ) {
      if( i > 0 ) {
        result.append( ", " );
      }
      result.append( array[ i ] );
    }
    return result.toString();
  }
}
