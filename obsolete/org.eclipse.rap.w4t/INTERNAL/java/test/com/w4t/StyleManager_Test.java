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
package com.w4t;

import junit.framework.TestCase;
import com.w4t.types.WebColor;


public class StyleManager_Test extends TestCase {
  
  private static final WebColor WEB_COLOR_RED = new WebColor( "red" );
  private static final WebColor WEB_COLOR_BLUE = new WebColor( "blue" );
  private static final WebColor WEB_COLOR_BLACK = new WebColor( "#000000" );
  private static int ATTR_0 = 0;
  private static int ATTR_1 = 1;
  private static int ATTR_2 = 2;

  public void testCreate() {
    StyleManager manager = new StyleManager( 5 );
    
    // find 'null'
    Object foundValue = manager.find( null, ATTR_0 );
    assertEquals( null, foundValue );
    
    // first entry
    Integer firstKey = manager.calculate( null, ATTR_0, null );
    assertEquals( createEmptyKey(), firstKey );
    firstKey = manager.calculate( null, ATTR_0, WEB_COLOR_RED );
    assertEquals( new Integer( 105759300 ), firstKey );
    
    assertFalse( manager.contains( firstKey ) );
    
    manager.create( null, firstKey, ATTR_0, WEB_COLOR_RED );
    assertTrue( manager.contains( firstKey ) );
    
    assertSame( WEB_COLOR_RED, manager.find( firstKey, ATTR_0 ) );
    
    // second entry
    Integer secondKey = manager.calculate( firstKey, ATTR_1, WEB_COLOR_BLUE );
    assertFalse( firstKey.equals( secondKey ) );
    assertEquals( new Integer( 1192587394 ), secondKey );
    
    assertFalse( manager.contains( secondKey ) );
    manager.create( firstKey, secondKey, ATTR_1, WEB_COLOR_BLUE );
    
    assertTrue( manager.contains( firstKey ) );
    assertTrue( manager.contains( secondKey ) );
    
    assertSame( WEB_COLOR_RED, manager.find( secondKey, ATTR_0 ) );
    assertSame( WEB_COLOR_BLUE, manager.find( secondKey, ATTR_1 ) );
  }
  
  public void testHashCode() {
    StyleManager manager = new StyleManager( 3 );
    Integer firstKey = manager.calculate( null, ATTR_0, "" );
    manager.create( null, firstKey, ATTR_0, "" );
    Integer secondKey = manager.calculate( firstKey, ATTR_1, "" );
    assertFalse( firstKey.equals( secondKey ) );
    
    manager.create( firstKey, secondKey, ATTR_1, "" );
    Integer thirdKey = manager.calculate( secondKey, ATTR_2, WEB_COLOR_BLACK );
    assertFalse( secondKey.equals( thirdKey ) );
    
  }

  private static Integer createEmptyKey() {
    return new Integer( 
      ( ( ( ( ( StyleManager.HASHCODE_INITIAL 
      * StyleManager.HASHCODE_CONSTANT + StyleManager.HASH_CODE_NULL ) 
      * StyleManager.HASHCODE_CONSTANT + StyleManager.HASH_CODE_NULL )
      * StyleManager.HASHCODE_CONSTANT  + StyleManager.HASH_CODE_NULL )
      * StyleManager.HASHCODE_CONSTANT  + StyleManager.HASH_CODE_NULL )
      * StyleManager.HASHCODE_CONSTANT  + StyleManager.HASH_CODE_NULL ) );
  }
}
