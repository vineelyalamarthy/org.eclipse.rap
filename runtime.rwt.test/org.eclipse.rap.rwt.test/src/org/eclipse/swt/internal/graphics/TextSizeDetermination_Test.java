/*******************************************************************************
 * Copyright (c) 2002, 2009 Innoopract Informationssysteme GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Innoopract Informationssysteme GmbH - initial API and implementation
 *     EclipseSource - ongoing development
 ******************************************************************************/

package org.eclipse.swt.internal.graphics;

import java.io.*;
import java.util.*;

import junit.framework.TestCase;

import org.eclipse.rwt.Fixture;
import org.eclipse.rwt.graphics.Graphics;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.graphics.TextSizeDetermination.ICalculationItem;
import org.eclipse.swt.internal.graphics.TextSizeProbeStore.IProbe;
import org.eclipse.swt.internal.graphics.TextSizeProbeStore.IProbeResult;


public class TextSizeDetermination_Test extends TestCase {

  private static final String TEST_STRING = "test";

  public void testStringExtent() {
    ICalculationItem[] items = TextSizeDetermination.getCalculationItems();
    assertEquals( 0, items.length );

    Font font = Graphics.getFont( "arial", 10, SWT.NORMAL );
    Point calculated = TextSizeDetermination.stringExtent( font, TEST_STRING );
    Point estimated = TextSizeEstimation.stringExtent( font, TEST_STRING );
    assertEquals( estimated, calculated );

    items = TextSizeDetermination.getCalculationItems();
    assertEquals( 1, items.length );
    items = TextSizeDetermination.getCalculationItems();
    assertEquals( 1, items.length );

    TextSizeDetermination.stringExtent( font, TEST_STRING );
    assertEquals( 1, items.length );
    items = TextSizeDetermination.getCalculationItems();
    assertEquals( 1, items.length );

    Point storedSize = new Point( 100, 10 );
    IProbe[] probeRequests = TextSizeProbeStore.getProbeRequests();
    assertEquals( 1, probeRequests.length );
    assertEquals( font.getFontData()[ 0 ], probeRequests[ 0 ].getFontData() );

    TextSizeProbeStore probeStore = TextSizeProbeStore.getInstance();
    probeStore.createProbeResult( probeRequests[ 0 ], new Point( 10, 10 ) );
    TextSizeDataBase.store( font.getFontData()[ 0 ], 
                            TEST_STRING, 
                            SWT.DEFAULT, 
                            storedSize );
    calculated = TextSizeDetermination.stringExtent( font, TEST_STRING );
    assertEquals( storedSize, calculated );

    Point emptyStringSize = TextSizeDetermination.stringExtent( font, "" );
    assertEquals( new Point( 0, 10 ), emptyStringSize );

    // make sure string extent does not expand line breaks
    Point singleLine = TextSizeDetermination.stringExtent( font, "First Line" );
    Point multiLine = TextSizeDetermination.stringExtent( font, "First Line\nSecond Line" );
    assertEquals( singleLine.y, multiLine.y );

    // make sure that leading and trailing space are calculated
    Point str = TextSizeDetermination.stringExtent( font, "  First Line    " );
    Point trimStr = TextSizeDetermination.stringExtent( font, "First Line" );
    assertTrue( str.x > trimStr.x );
  }

  public void testTextExtent() {
    Font font = Graphics.getFont( "Helvetica", 10, SWT.NORMAL );
    // make sure text extent does expand line breaks
    Point singleLine
      = TextSizeDetermination.textExtent( font, "First Line", 0 );
    Point multiLine
      = TextSizeDetermination.textExtent( font, "First Line\nSecond Line", 0 );
    assertTrue( singleLine.y < multiLine.y );
  }

  public void testMarkupExtent() {
    Font font = Graphics.getFont( "Helvetica", 10, SWT.NORMAL );
    String markup = "First Line<ul><li>item1</li><li>item2</li></ul>";
    TextSizeDetermination.markupExtent( font, markup, 0 );
    ICalculationItem[] items = TextSizeDetermination.getCalculationItems();
    boolean markupUnaltered = false;
    for( int i = 0; i < items.length; i++ ) {
      if( items[ i ].getString().equals( markup ) ) {
        markupUnaltered = true;
      }
    }
    assertTrue( markupUnaltered );
  }

  public void testCharHeight() {
    IProbe[] probeRequests = TextSizeProbeStore.getProbeRequests();
    assertEquals( 0, probeRequests.length );

    Font font0 = Graphics.getFont( "arial", 10, SWT.NORMAL );
    int calculated = TextSizeDetermination.getCharHeight( font0 );
    int estimated = TextSizeEstimation.getCharHeight( font0 );
    assertEquals( estimated, calculated, 0 );

    probeRequests = TextSizeProbeStore.getProbeRequests();
    assertEquals( 1, probeRequests.length );
    assertEquals( font0.getFontData()[ 0 ], probeRequests[ 0 ].getFontData() );

    TextSizeProbeStore probeStore = TextSizeProbeStore.getInstance();
    Point probeSize = new Point( 10, 13 );
    probeStore.createProbeResult( probeRequests[ 0 ], probeSize );
    calculated = TextSizeDetermination.getCharHeight( font0 );
    assertEquals( 13, calculated );
  }

  public void testAvgCharWidth() {
    IProbe[] probeRequests = TextSizeProbeStore.getProbeRequests();
    assertEquals( 0, probeRequests.length );

    Font font0 = Graphics.getFont( "arial", 10, SWT.NORMAL );
    float calculated = TextSizeDetermination.getAvgCharWidth( font0 );
    float estimated = TextSizeEstimation.getAvgCharWidth( font0 );
    assertEquals( estimated, calculated, 0 );

    probeRequests = TextSizeProbeStore.getProbeRequests();
    assertEquals( 1, probeRequests.length );
    assertEquals( font0.getFontData()[ 0 ], probeRequests[ 0 ].getFontData() );

    TextSizeProbeStore probeStore = TextSizeProbeStore.getInstance();
    Point probeSize = new Point( TextSizeProbeStore.DEFAULT_PROBE.length() * 4, 10 );
    probeStore.createProbeResult( probeRequests[ 0 ], probeSize );
    calculated = TextSizeDetermination.getAvgCharWidth( font0 );
    assertEquals( 4, calculated, 0 );
  }

  public void testFontSizeDataBase() {
    Font font0 = Graphics.getFont( "arial", 10, SWT.NORMAL );
    FontData fontData0 = font0.getFontData()[ 0 ];
    Font font1 = Graphics.getFont( "helvetia", 12, SWT.NORMAL );
    FontData fontData1 = font1.getFontData()[ 0 ];

    Point textSize;
    textSize = TextSizeDataBase.lookup( fontData0, TEST_STRING, SWT.DEFAULT );
    assertNull( textSize );
    textSize = TextSizeDataBase.lookup( fontData1, TEST_STRING, SWT.DEFAULT );
    assertNull( textSize );

    try {
      Point point = new Point( 1, 1 );
      TextSizeDataBase.store( fontData1, TEST_STRING, SWT.DEFAULT, point );
      fail( "No probe available." );
    } catch( final IllegalStateException ise ) {
    }

    // simulate clientside probing...
    TextSizeProbeStore probeStore = TextSizeProbeStore.getInstance();
    Point probeSize0 = new Point( 10, 10 );
    probeStore.createProbeResult( findRequestedProbe( 0 ), probeSize0 );
    Point probeSize1 = new Point( 12, 12 );
    probeStore.createProbeResult( findRequestedProbe( 1 ), probeSize1 );

    Point calculatedTextSize0 = new Point( 100, 10 );
    TextSizeDataBase.store( fontData0,
                            TEST_STRING,
                            SWT.DEFAULT,
                            calculatedTextSize0 );
    Point calculatedTextSize1 = new Point( 100, 12 );
    TextSizeDataBase.store( fontData1,
                            TEST_STRING,
                            SWT.DEFAULT,
                            calculatedTextSize1 );
    textSize = TextSizeDataBase.lookup( fontData0, TEST_STRING, SWT.DEFAULT );
    assertEquals( calculatedTextSize0, textSize );
    textSize = TextSizeDataBase.lookup( fontData1, TEST_STRING, SWT.DEFAULT );
    assertEquals( calculatedTextSize1, textSize );
  }

  private IProbe findRequestedProbe( final int i ) {
    IProbe[] probeRequests = TextSizeProbeStore.getProbeRequests();
    return TextSizeProbeStore.getProbe( probeRequests[ i ].getFontData() );
  }

  public void testProbeStorage() {
    Font font0 = Graphics.getFont( "arial", 10, SWT.NORMAL );
    FontData fontData0 = font0.getFontData()[ 0 ];
    IProbe[] probeList = TextSizeProbeStore.getProbeList();
    assertEquals( 0, probeList.length );
    IProbe probe0 = TextSizeProbeStore.getProbe( fontData0 );
    assertNull( probe0 );

    String probeText0 = "ProbeText0";
    probe0 = TextSizeProbeStore.createProbe( fontData0, probeText0 );
    probeList = TextSizeProbeStore.getProbeList();
    assertEquals( 1, probeList.length );
    assertSame( probe0, probeList[ 0 ] );
    assertSame( probe0, TextSizeProbeStore.getProbe( fontData0 ) );
    assertTrue( TextSizeProbeStore.containsProbe( fontData0 ) );
    assertSame( probe0.getFontData(), fontData0 );
    assertSame( probe0.getString(), probeText0 );

    Font font1 = Graphics.getFont( "arial", 12, SWT.NORMAL );
    FontData fontData1 = font1.getFontData()[ 0 ];
    assertFalse( TextSizeProbeStore.containsProbe( fontData1 ) );

    TextSizeProbeStore probeStore = TextSizeProbeStore.getInstance();
    IProbeResult probeResult0 = probeStore.getProbeResult( fontData0 );
    assertNull( probeResult0 );

    Point probeSize0 = new Point( 10, 10 );
    probeResult0 = probeStore.createProbeResult( probe0, probeSize0 );
    assertSame( probeResult0.getProbe(), probe0 );
    assertSame( probeResult0.getSize(), probeSize0 );
    assertTrue( probeStore.containsProbeResult( fontData0 ) );
    assertFalse( probeStore.containsProbeResult( fontData1 ) );
  }

  public void testDefaultFontSizeStorage() throws IOException {
    DefaultTextSizeStorage storage = new DefaultTextSizeStorage();
    Font font0 = Graphics.getFont( "arial", 10, SWT.NORMAL );
    FontData fontData0 = font0.getFontData()[ 0 ];
    Font font1 = Graphics.getFont( "helvetia", 12, SWT.NORMAL );
    FontData fontData1 = font1.getFontData()[ 0 ];
    storage.storeFont( fontData0 );
    storage.storeFont( fontData1 );

    Point point0 = new Point( 9, 10 );
    Integer key0 = new Integer( 0 );
    storage.storeTextSize( key0, point0 );
    Point point1 = new Point( 11, 12 );
    Integer key1 = new Integer( 1 );
    storage.storeTextSize( key1, point1 );

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    storage.save( out );
    String[] expected = new String[] {
      DefaultTextSizeStorage.PREFIX_FONT_KEY + "0=1|arial|10|0|\n",
      DefaultTextSizeStorage.PREFIX_FONT_KEY + "=1|helvetia|12|0|\n",
      "0=9,10\n",
      "1=11,12\n"
    };
    for( int i = 0; i < expected.length; i++ ) {
      assertTrue( out.toString().indexOf( expected[ i ]  ) != 0 );
    }

    storage = new DefaultTextSizeStorage();
    
    ByteArrayInputStream in = new ByteArrayInputStream( out.toByteArray() );
    storage.read( in );

    Point actual = storage.lookupTextSize( key0 );
    assertEquals( point0, actual );
    actual = storage.lookupTextSize( key1 );
    assertEquals( point1, actual );
    FontData[] fontDatas = storage.getFontList();
    assertEquals( 2, fontDatas.length );
    List fontDataList = Arrays.asList( fontDatas );
    assertTrue( fontDataList.contains( fontData0 ) );
    assertTrue( fontDataList.contains( fontData1 ) );
  }

  public void testStorageOverflow() {
    DefaultTextSizeStorage storage = new DefaultTextSizeStorage();
    int storeSize = DefaultTextSizeStorage.MIN_STORE_SIZE;
    storage.setStoreSize( storeSize );

    for( int i = 0; i < storeSize - 1; i++ ) {
      Integer key = new Integer( i );
      Point point = new Point( i, i );
      storage.storeTextSize( key, point );
    }
    Integer firstKey = new Integer( 0 );
    // Attention: timestamp update!
    Point firstPoint = storage.lookupTextSize( firstKey );
    assertEquals( firstPoint, new Point( 0, 0 ) );

    Point overflowPoint = new Point( -1, -1 );
    Integer overFlowKey = new Integer( Integer.MAX_VALUE );
    storage.storeTextSize( overFlowKey, overflowPoint );

    assertEquals( firstPoint, storage.lookupTextSize( firstKey ) );
    assertEquals( overflowPoint, storage.lookupTextSize( overFlowKey ) );
    assertNull( storage.lookupTextSize( new Integer( 99 ) ) );
    assertEquals( new Point( 101, 101 ),
                  storage.lookupTextSize( new Integer( 101 ) ) );
  }

  public void testTextSizeDatabaseKey() {
    Font font = Graphics.getFont( "name", 10, SWT.NORMAL );
    final FontData fontData = font.getFontData()[ 0 ];
    Set takenKeys = new HashSet();
    StringBuffer generatedText = new StringBuffer();
    for( int i = 0; i < 100; i++ ) {
      generatedText.append( "a" );
      final String text = generatedText.toString();
      IProbe probe = new IProbe() {
        public FontData getFontData() {
          return fontData;
        }
        public String getString() {
          return text;
        }
        public String getJSProbeParam() {
          return "";
        }
      };
      Point size = new Point( 1, 2 );
      TextSizeProbeStore.getInstance().createProbeResult( probe, size );
      Integer key = TextSizeDataBase.getKey( fontData, text, -1 );
      assertFalse( takenKeys.contains( key ) );
      takenKeys.add( key );
    }
  }
  
  protected void setUp() throws Exception {
    Fixture.setUp();
    TextSizeDataBase.reset();
    TextSizeProbeStore.reset();
  }

  protected void tearDown() throws Exception {
    TextSizeProbeStore.reset();
    TextSizeDataBase.reset();
    Fixture.tearDown();
  }
}