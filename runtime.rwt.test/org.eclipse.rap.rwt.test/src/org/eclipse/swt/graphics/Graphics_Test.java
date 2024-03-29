/*******************************************************************************
 * Copyright (c) 2002, 2010 Innoopract Informationssysteme GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Innoopract Informationssysteme GmbH - initial API and implementation
 *     EclipseSource - ongoing development
 ******************************************************************************/
package org.eclipse.swt.graphics;

import java.io.*;
import java.net.URL;
import java.net.URLClassLoader;

import junit.framework.TestCase;

import org.eclipse.rwt.Fixture;
import org.eclipse.rwt.graphics.Graphics;
import org.eclipse.rwt.internal.resources.*;
import org.eclipse.rwt.internal.theme.ThemeManager;
import org.eclipse.rwt.resources.IResourceManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.internal.graphics.ResourceFactory;
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

  public void testGetImage() {
    IResourceManager manager = ResourceManager.getInstance();
    // only if you comment initial registration in
    // org.eclipse.swt.internal.widgets.displaykit.QooxdooResourcesUtil
    assertFalse( manager.isRegistered( Fixture.IMAGE1 ) );
    Image image1 = Graphics.getImage( Fixture.IMAGE1 );
    String registerPath = getRegisterPath( image1 );
    assertTrue( manager.isRegistered( registerPath ) );
    File contextDir = new File( Fixture.CONTEXT_DIR,
                                ResourceManagerImpl.RESOURCES );
    assertTrue( new File( contextDir, registerPath ).exists() );
    Image image1a = Graphics.getImage( Fixture.IMAGE1 );
    assertSame( image1, image1a );
    // another picture
    Image image2 = Graphics.getImage( Fixture.IMAGE2 );
    String image2Path = getRegisterPath( image2 );
    assertTrue( manager.isRegistered( image2Path ) );
    assertTrue( new File( contextDir, image2Path ).exists() );
    // ... and do it again...
    Graphics.getImage( Fixture.IMAGE1 );
    assertTrue( manager.isRegistered( registerPath ) );
  }

  public void testGetImageWithClassLoader() throws IOException {
    File testGif = new File( Fixture.CONTEXT_DIR, "test.gif" );
    Fixture.copyTestResource( Fixture.IMAGE3, testGif );
    URL[] urls = new URL[] { Fixture.CONTEXT_DIR.toURI().toURL() };
    URLClassLoader classLoader = new URLClassLoader( urls, null );
    IResourceManager manager = ResourceManager.getInstance();
    assertFalse( manager.isRegistered( Fixture.IMAGE3 ) );
    try {
      Graphics.getImage( "test.gif" );
      fail( "Image not available on the classpath." );
    } catch( final SWTException e ) {
      // expected
    }
    Image image = Graphics.getImage( "test.gif", classLoader );
    assertNotNull( image );
  }

  public void testGetImageWithInputStream() throws IOException {
    String imageName = "testIS.gif";
    File testGif = new File( Fixture.CONTEXT_DIR, imageName );
    Fixture.copyTestResource( Fixture.IMAGE3, testGif );
    URL[] urls = new URL[] { Fixture.CONTEXT_DIR.toURI().toURL() };
    URLClassLoader classLoader = new URLClassLoader( urls, null );
    IResourceManager manager = ResourceManager.getInstance();
    assertFalse( manager.isRegistered( Fixture.IMAGE3 ) );
    try {
      Graphics.getImage( imageName );
      fail( "Image not available on the classpath." );
    } catch( final SWTException e ) {
      // expected
    }
    InputStream is = classLoader.getResourceAsStream( imageName );
    Image image = Graphics.getImage( "test.gif", is );
    assertNotNull( image );
  }

  public void testGetImageWithIllegalArguments() {
    try {
      Graphics.getImage( null );
      fail( "Image#find must not allow null-argument" );
    } catch( IllegalArgumentException e ) {
      // expected
    }
    try {
      Graphics.getImage( "" );
      fail( "Image#find must not allow empty string argument" );
    } catch( IllegalArgumentException e ) {
      // expected
    }
    try {
      Graphics.getImage( "", new ByteArrayInputStream( new byte[ 1 ] ) );
      fail( "Image#find must not allow empty string argument" );
    } catch( IllegalArgumentException e ) {
      // expected
    }
  }

  public void testGetFont() {
    Font font = Graphics.getFont( "roman", 1, SWT.NORMAL );
    assertEquals( "roman", font.getFontData()[ 0 ].getName() );
    assertEquals( 1, font.getFontData()[ 0 ].getHeight() );
    assertEquals( SWT.NORMAL, font.getFontData()[ 0 ].getStyle() );
    Font sameFont = Graphics.getFont( "roman", 1, SWT.NORMAL );
    assertSame( font, sameFont );
    Font otherFont = Graphics.getFont( "arial", 2, SWT.NORMAL );
    assertNotSame( otherFont, font );
    Font boldFont = Graphics.getFont( "arial", 11, SWT.BOLD );
    assertEquals( SWT.BOLD, boldFont.getFontData()[ 0 ].getStyle() );
    Font italicFont = Graphics.getFont( "arial", 11, SWT.ITALIC );
    assertEquals( SWT.ITALIC, italicFont.getFontData()[ 0 ].getStyle() );
    sameFont = Graphics.getFont( new FontData( "roman", 1, SWT.NORMAL ) );
    assertSame( font, sameFont );
    Font arial13Normal = Graphics.getFont( "arial", 13, SWT.NORMAL );
    Font arial12Bold = Graphics.getFont( "arial", 12, SWT.BOLD );
    assertNotSame( arial13Normal, arial12Bold );
  }

  public void testGetFontReturnsCurrentDisplay() {
    new Display();
    Font font = Graphics.getFont( "roman", 1, SWT.NORMAL );
    assertSame( Display.getCurrent(), font.getDevice() );
  }

  public void testGetFontWithIllegalArguments() {
    try {
      Graphics.getFont( null, 1, SWT.NONE );
      fail( "The font name must not be null" );
    } catch( IllegalArgumentException e ) {
      // Expected
    }
    try {
      Graphics.getFont( "abc", -1, SWT.NONE );
      fail( "The font size must not be negative" );
    } catch( IllegalArgumentException e ) {
      // Expected
    }
    Font font = Graphics.getFont( "roman", 1, 1 << 3 );
    assertEquals( SWT.NORMAL, font.getFontData()[ 0 ].getStyle() );
  }

  public void testDisposeFactoryCreated() {
    Font font = Graphics.getFont( "roman", 1, SWT.NORMAL );
    try {
      font.dispose();
      fail( "It is not allowed to dispose of a factory-created color" );
    } catch( IllegalStateException e ) {
      assertFalse( font.isDisposed() );
    }
  }

  private static String getRegisterPath( final Image image ) {
    String imagePath = ResourceFactory.getImagePath( image );
    int prefixLength = ResourceManagerImpl.RESOURCES.length() + 1;
    return imagePath.substring( prefixLength );
  }

  protected void setUp() throws Exception {
    // we do need the resource manager for this test
    Fixture.setUpWithoutResourceManager();
    Fixture.registerAdapterFactories();
    Fixture.createContext( false );
    ThemeManager.getInstance().initialize();
    // registration of real resource manager
    ResourceManager.register( new DefaultResourceManagerFactory() );
  }

  protected void tearDown() throws Exception {
    Fixture.tearDown();
  }
}
