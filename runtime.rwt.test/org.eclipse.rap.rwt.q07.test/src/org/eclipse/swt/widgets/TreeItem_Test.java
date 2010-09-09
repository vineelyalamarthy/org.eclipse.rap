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
package org.eclipse.swt.widgets;

import java.io.IOException;
import java.io.InputStream;

import junit.framework.TestCase;

import org.eclipse.rwt.Fixture;
import org.eclipse.rwt.graphics.Graphics;
import org.eclipse.rwt.lifecycle.WidgetUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.graphics.TextSizeDetermination;
import org.eclipse.swt.internal.widgets.ITreeItemAdapter;

public class TreeItem_Test extends TestCase {

  public void testConstructor() {
    Display display = new Display();
    Shell shell = new Shell( display, SWT.NONE );
    Tree tree = new Tree( shell, SWT.NONE );
    TreeItem item = new TreeItem( tree, SWT.NONE );
    assertSame( display, item.getDisplay() );
    assertEquals( "", item.getText() );
    assertSame( item, tree.getItem( tree.getItemCount() - 1 ) );
    try {
      new TreeItem( ( TreeItem )null, SWT.NONE );
      fail( "Must not allow null-parent" );
    } catch( IllegalArgumentException iae ) {
      // expected
    }
    try {
      new TreeItem( ( Tree )null, SWT.NONE );
      fail( "Must not allow null-parent" );
    } catch( IllegalArgumentException iae ) {
      // expected
    }
    try {
      new TreeItem( tree, SWT.NONE, 5 );
      fail( "No exception thrown for illegal index argument" );
    } catch( IllegalArgumentException e ) {
   // expected
    }

    try {
      new TreeItem( item, SWT.NONE, 5 );
      fail( "No exception thrown for illegal index argument" );
    } catch( IllegalArgumentException e ) {
   // expected
    }
    try {
      new TreeItem( item, SWT.NONE, -1 );
      fail( "No exception thrown for illegal index argument" );
    } catch( IllegalArgumentException e ) {
   // expected
    }
  }

  public void testRemoveAll() {
    Display display = new Display();
    Shell shell = new Shell( display, SWT.NONE );
    Tree tree = new Tree( shell, SWT.NONE );
    TreeItem item1 = new TreeItem( tree, SWT.NONE );
    TreeItem item11 = new TreeItem( item1, SWT.NONE );
    TreeItem item111 = new TreeItem( item11, SWT.NONE );
    TreeItem item2 = new TreeItem( tree, SWT.NONE );
    item1.removeAll();
    assertEquals( false, item1.isDisposed() );
    assertEquals( true, item11.isDisposed() );
    assertEquals( true, item111.isDisposed() );
    assertEquals( 0, item1.getItemCount() );
    assertEquals( false, item2.isDisposed() );
  }

  public void testFont() {
    Display display = new Display();
    Shell shell = new Shell( display, SWT.NONE );
    Tree tree = new Tree( shell, SWT.NONE );
    Font treeFont = Graphics.getFont( "BeautifullyCraftedTreeFont",
                                      15,
                                      SWT.BOLD );
    tree.setFont( treeFont );
    TreeItem item = new TreeItem( tree, SWT.NONE );
    assertSame( treeFont, item.getFont() );
    Font itemFont = Graphics.getFont( "ItemFont", 40, SWT.NORMAL );
    item.setFont( itemFont );
    assertSame( itemFont, item.getFont() );
    item.setFont( null );
    assertSame( treeFont, item.getFont() );
  }

  public void testChecked() {
    Display display = new Display();
    Shell shell = new Shell( display, SWT.NONE );
    Tree tree = new Tree( shell, SWT.NONE );
    TreeItem item = new TreeItem( tree, SWT.NONE );
    Tree checkedTree = new Tree( shell, SWT.CHECK );
    TreeItem checkedItem = new TreeItem( checkedTree, SWT.NONE );
    // Ensure that checked-property on a treeItem cannot be changed when tree
    // is missing CHECK style
    assertEquals( false, item.getChecked() );
    item.setChecked( true );
    assertEquals( false, item.getChecked() );
    // The check-property for a treeItem on a tree with style CHECK may be
    // changed
    assertEquals( false, checkedItem.getChecked() );
    checkedItem.setChecked( true );
    assertEquals( true, checkedItem.getChecked() );
  }

  public void testGetExpanded() {
    Display display = new Display();
    Shell shell = new Shell( display, SWT.NONE );
    Tree tree = new Tree( shell, SWT.SINGLE );
    TreeItem treeItem = new TreeItem( tree, SWT.NONE );
    assertEquals( false, treeItem.getExpanded() );
    // there must be at least one subitem before you can set the treeitem
    // expanded
    new TreeItem( treeItem, 0 );
    treeItem.setExpanded( true );
    assertTrue( treeItem.getExpanded() );
    treeItem.setExpanded( false );
    assertEquals( false, treeItem.getExpanded() );
  }

  public void testBackgroundColor() {
    Display display = new Display();
    Shell shell = new Shell( display, SWT.NONE );
    Tree tree = new Tree( shell, SWT.SINGLE );
    TreeItem item = new TreeItem( tree, SWT.NONE );
    // initial background color should match the parents one
    assertEquals( tree.getBackground(), item.getBackground() );
    // change the colors
    Color green = display.getSystemColor( SWT.COLOR_GREEN );
    item.setBackground( green );
    assertEquals( green, item.getBackground() );
  }

  public void testForegroundColor() {
    Display display = new Display();
    Shell shell = new Shell( display, SWT.NONE );
    Tree tree = new Tree( shell, SWT.SINGLE );
    TreeItem item = new TreeItem( tree, SWT.NONE );
    // initial foreground color should match the parents one
    assertEquals( tree.getForeground(), item.getForeground() );
    // change the colors
    Color green = display.getSystemColor( SWT.COLOR_GREEN );
    item.setForeground( green );
    assertEquals( green, item.getForeground() );
  }

  public void testClear() {
    Display display = new Display();
    Shell shell = new Shell( display, SWT.NONE );
    Tree tree = new Tree( shell, SWT.SINGLE | SWT.CHECK );
    TreeItem item = new TreeItem( tree, SWT.NONE );
    assertEquals( "", item.getText() );
    assertEquals( null, item.getImage() );
    assertEquals( Boolean.FALSE, Boolean.valueOf( item.getChecked() ) );
    item.setText( "foo" );
    item.setImage( Graphics.getImage( Fixture.IMAGE1 ) );
    item.setChecked( true );
    assertEquals( "foo", item.getText() );
    assertEquals( Graphics.getImage( Fixture.IMAGE1 ), item.getImage() );
    assertEquals( Boolean.TRUE, Boolean.valueOf( item.getChecked() ) );
    item.clear();
    assertEquals( "", item.getText() );
    assertEquals( null, item.getImage() );
    assertEquals( Boolean.FALSE, Boolean.valueOf( item.getChecked() ) );
    tree = new Tree( shell, SWT.SINGLE | SWT.VIRTUAL );
  }

  public void testClearAll() {
    Display display = new Display();
    Shell shell = new Shell( display, SWT.NONE );
    Tree tree = new Tree( shell, SWT.SINGLE | SWT.CHECK );
    TreeItem root = new TreeItem( tree, SWT.NONE );
    for( int i = 0; i < 2; i++ ) {
      TreeItem item0 = new TreeItem( root, 0 );
      item0.setText( "Item " + i );
      for( int j = 0; j < 2; j++ ) {
        TreeItem item1 = new TreeItem( item0, 0 );
        item1.setText( "Item " + i + " " + j );
        for( int k = 0; k < 2; k++ ) {
          TreeItem item2 = new TreeItem( item1, 0 );
          item2.setText( "Item " + i + " " + j + " " + k );
        }
      }
    }
    for( int i = 0; i < 2; i++ ) {
      TreeItem item0 = root.getItem( i );
      assertEquals( "Item " + i, item0.getText() );
      for( int j = 0; j < 2; j++ ) {
        TreeItem item1 = item0.getItem( j );
        assertEquals( "Item " + i + " " + j, item1.getText() );
        for( int k = 0; k < 2; k++ ) {
          TreeItem item2 = item1.getItem( k );
          assertEquals( "Item " + i + " " + j + " " + k, item2.getText() );
        }
      }
    }
    root.clearAll( false );
    for( int i = 0; i < 2; i++ ) {
      TreeItem item0 = root.getItem( i );
      assertEquals( "", item0.getText() );
      for( int j = 0; j < 2; j++ ) {
        TreeItem item1 = item0.getItem( j );
        assertEquals( "Item " + i + " " + j, item1.getText() );
        for( int k = 0; k < 2; k++ ) {
          TreeItem item2 = item1.getItem( k );
          assertEquals( "Item " + i + " " + j + " " + k, item2.getText() );
        }
      }
    }
    root.clearAll( true );
    for( int i = 0; i < 2; i++ ) {
      TreeItem item0 = root.getItem( i );
      assertEquals( "", item0.getText() );
      for( int j = 0; j < 2; j++ ) {
        TreeItem item1 = item0.getItem( j );
        assertEquals( "", item1.getText() );
        for( int k = 0; k < 2; k++ ) {
          TreeItem item2 = item1.getItem( k );
          assertEquals( "", item2.getText() );
        }
      }
    }
  }

  public void testSetGrayed() {
    Display display = new Display();
    Shell shell = new Shell( display, SWT.NONE );
    Tree newTree = new Tree( shell, SWT.CHECK );
    TreeItem tItem = new TreeItem( newTree, 0 );
    assertEquals( false, tItem.getGrayed() );
    tItem.setGrayed( true );
    assertTrue( tItem.getGrayed() );
    tItem.setGrayed( false );
    assertEquals( false, tItem.getGrayed() );
    newTree.dispose();
  }

  public void testSetText() {
    Display display = new Display();
    Shell shell = new Shell( display, SWT.NONE );
    Tree tree = new Tree( shell, SWT.CHECK );
    TreeItem treeItem = new TreeItem( tree, 0 );
    final String TestString = "test";
    final String TestStrings[] = new String[]{
      TestString, TestString + "1", TestString + "2"
    };
    /*
     * Test the getText/setText API with a Tree that has only the default
     * column.
     */
    assertEquals( 0, treeItem.getText( 1 ).length() );
    treeItem.setText( 1, TestString );
    assertEquals( 0, treeItem.getText( 1 ).length() );
    assertEquals( 0, treeItem.getText( 0 ).length() );
    treeItem.setText( 0, TestString );
    assertEquals( TestString, treeItem.getText( 0 ) );
    treeItem.setText( -1, TestStrings[ 1 ] );
    assertEquals( 0, treeItem.getText( -1 ).length() );
    /*
     * Test the getText/setText API with a Tree that enough columns to fit all
     * test item texts.
     */
    tree = new Tree( shell, SWT.CHECK );
    treeItem = new TreeItem( tree, 0 );
    // tree.setText(TestStrings); // create anough columns for
    // TreeItem.setText(String[]) to work
    int columnCount = tree.getColumnCount();
    if( columnCount < 12 ) {
      for( int i = columnCount; i < 12; i++ ) {
        new TreeColumn( tree, SWT.NONE );
      }
    }
    TreeColumn[] columns = tree.getColumns();
    for( int i = 0; i < TestStrings.length; i++ ) {
      columns[ i ].setText( TestStrings[ i ] );
    }
    assertEquals( 0, treeItem.getText( 1 ).length() );
    treeItem.setText( 1, TestString );
    assertEquals( TestString, treeItem.getText( 1 ) );
    assertEquals( 0, treeItem.getText( 0 ).length() );
    treeItem.setText( 0, TestString );
    assertEquals( TestString, treeItem.getText( 0 ) );
    treeItem.setText( -1, TestStrings[ 1 ] );
    assertEquals( 0, treeItem.getText( -1 ).length() );
    try {
      treeItem.setText( -1, null );
      fail( "No exception thrown for string == null" );
    } catch( IllegalArgumentException e ) {
      // expected
    }
    try {
      treeItem.setText( 0, null );
      fail( "No exception thrown for string == null" );
    } catch( IllegalArgumentException e ) {
      // expected
    }
  }

  public void testSetImage() {
    Display display = new Display();
    Shell shell = new Shell( display, SWT.NONE );
    Tree tree = new Tree( shell, SWT.CHECK );
    TreeItem treeItem = new TreeItem( tree, 0 );
    Image[] images = new Image[]{
      Graphics.getImage( Fixture.IMAGE1 ),
      Graphics.getImage( Fixture.IMAGE2 ),
      Graphics.getImage( Fixture.IMAGE3 )
    };
    assertNull( treeItem.getImage( 1 ) );
    treeItem.setImage( -1, null );
    assertNull( treeItem.getImage( -1 ) );
    treeItem.setImage( 0, images[ 0 ] );
    assertEquals( images[ 0 ], treeItem.getImage( 0 ) );
    String texts[] = new String[ images.length ];
    for( int i = 0; i < texts.length; i++ ) {
      texts[ i ] = String.valueOf( i );
    }
    // tree.setText(texts); // create enough columns for
    // TreeItem.setImage(Image[]) to work
    int columnCount = tree.getColumnCount();
    if( columnCount < texts.length ) {
      for( int i = columnCount; i < texts.length; i++ ) {
        new TreeColumn( tree, SWT.NONE );
      }
    }
    TreeColumn[] columns = tree.getColumns();
    for( int i = 0; i < texts.length; i++ ) {
      columns[ i ].setText( texts[ i ] );
    }
    treeItem.setImage( 1, images[ 1 ] );
    assertEquals( images[ 1 ], treeItem.getImage( 1 ) );
    treeItem.setImage( images );
    for( int i = 0; i < images.length; i++ ) {
      assertEquals( images[ i ], treeItem.getImage( i ) );
    }
    try {
      treeItem.setImage( ( Image[] )null );
      fail( "No exception thrown for images == null" );
    } catch( IllegalArgumentException e ) {
      // expected
    }
    // Test for a disposed Image in the array
    ClassLoader loader = Fixture.class.getClassLoader();
    InputStream stream = loader.getResourceAsStream( Fixture.IMAGE1 );
    Image image = new Image( display, stream );
    image.dispose();
    Image[] images2 = new Image[]{
      Graphics.getImage( Fixture.IMAGE1 ),
      image,
      Graphics.getImage( Fixture.IMAGE3 )
    };
    try {
      treeItem.setImage( images2 );
      fail( "No exception thrown for a disposed image" );
    } catch( IllegalArgumentException e ) {
      // expected
    }
    finally {
      try {
        stream.close();
      }
      catch(IOException e) {
        fail("Unable to close input stream.");
      }
    }
  }

  public void testSetImageI() {
    Display display = new Display();
    Shell shell = new Shell( display, SWT.NONE );
    Tree tree = new Tree( shell, SWT.CHECK );
    TreeItem treeItem = new TreeItem( tree, 0 );
    Image[] images = new Image[]{
      Graphics.getImage( Fixture.IMAGE1 ),
      Graphics.getImage( Fixture.IMAGE2 ),
      Graphics.getImage( Fixture.IMAGE3 )
    };
    // no columns
    assertEquals( null, treeItem.getImage( 0 ) );
    treeItem.setImage( 0, images[ 0 ] );
    assertEquals( images[ 0 ], treeItem.getImage( 0 ) );
    // index beyond range - no error
    treeItem.setImage( 10, images[ 0 ] );
    assertEquals( null, treeItem.getImage( 10 ) );
    // with columns
    new TreeColumn( tree, SWT.LEFT );
    new TreeColumn( tree, SWT.LEFT );
    // index beyond range - no error
    treeItem.setImage( 10, images[ 0 ] );
    assertEquals( null, treeItem.getImage( 10 ) );
    treeItem.setImage( 0, images[ 0 ] );
    assertEquals( images[ 0 ], treeItem.getImage( 0 ) );
    treeItem.setImage( 0, null );
    assertEquals( null, treeItem.getImage( 0 ) );
    treeItem.setImage( 0, images[ 0 ] );
    treeItem.setImage( images[ 1 ] );
    assertEquals( images[ 1 ], treeItem.getImage( 0 ) );
    treeItem.setImage( images[ 1 ] );
    treeItem.setImage( 0, images[ 0 ] );
    assertEquals( images[ 0 ], treeItem.getImage( 0 ) );

    // Test for a disposed Image in the array
    ClassLoader loader = Fixture.class.getClassLoader();
    InputStream stream = loader.getResourceAsStream( Fixture.IMAGE1 );
    Image image = new Image( display, stream );
    image.dispose();
    try {
      treeItem.setImage( image );
      fail( "No exception thrown for a disposed image" );
    } catch( IllegalArgumentException e ) {
      // expected
    }
    finally {
      try {
        stream.close();
      }
      catch(IOException e) {
        fail("Unable to close input stream.");
      }
    }
  }

  public void testSetForeground() {
    Display display = new Display();
    Shell shell = new Shell( display, SWT.NONE );
    Tree tree = new Tree( shell, SWT.CHECK );
    TreeItem treeItem = new TreeItem( tree, 0 );
    Color color = display.getSystemColor( SWT.COLOR_RED );
    treeItem.setForeground( color );
    assertEquals( color, treeItem.getForeground() );
    treeItem.setForeground( null );
    assertEquals( tree.getForeground(), treeItem.getForeground() );
    Color color2 = new Color(display, 255, 0, 0);
    color2.dispose();
    try{
      treeItem.setForeground( color2 );
      fail("Disposed Image must not be set.");
    }
    catch (IllegalArgumentException e)
    {
      //Expected Exception
    }
  }

  public void testSetBackground() {
    Display display = new Display();
    Shell shell = new Shell( display, SWT.NONE );
    Tree tree = new Tree( shell, SWT.CHECK );
    TreeItem treeItem = new TreeItem( tree, 0 );
    Color color = display.getSystemColor( SWT.COLOR_RED );
    treeItem.setBackground( color );
    assertEquals( color, treeItem.getBackground() );
    treeItem.setBackground( null );
    assertEquals( tree.getBackground(), treeItem.getBackground() );
    // Test for the case that the argument has been disposed
    Color color2 = new Color( display, 0, 255, 0 );
    color2.dispose();
    try {
      treeItem.setBackground( color2 );
      fail( "Disposed color must not be set." );
    } catch( IllegalArgumentException e ) {
      // Expected Exception
    }
  }

  public void testSetForegroundI() {
    Display display = new Display();
    Shell shell = new Shell( display, SWT.NONE );
    Tree tree = new Tree( shell, SWT.NONE );
    TreeItem treeItem = new TreeItem( tree, 0 );
    Color red = display.getSystemColor( SWT.COLOR_RED );
    Color blue = display.getSystemColor( SWT.COLOR_BLUE );
    // no columns
    assertEquals( tree.getForeground(), treeItem.getForeground( 0 ) );
    assertEquals( treeItem.getForeground(), treeItem.getForeground( 0 ) );
    treeItem.setForeground( 0, red );
    assertEquals( red, treeItem.getForeground( 0 ) );
    // index beyond range - no error
    treeItem.setForeground( 10, red );
    assertEquals( treeItem.getForeground(), treeItem.getForeground( 10 ) );
    // with columns
    new TreeColumn( tree, SWT.LEFT );
    new TreeColumn( tree, SWT.LEFT );
    // index beyond range - no error
    treeItem.setForeground( 10, red );
    assertEquals( treeItem.getForeground(), treeItem.getForeground( 10 ) );
    treeItem.setForeground( 0, red );
    assertEquals( red, treeItem.getForeground( 0 ) );
    treeItem.setForeground( 0, null );
    assertEquals( tree.getForeground(), treeItem.getForeground( 0 ) );
    treeItem.setForeground( 0, blue );
    treeItem.setForeground( red );
    assertEquals( blue, treeItem.getForeground( 0 ) );
    treeItem.setForeground( 0, null );
    assertEquals( red, treeItem.getForeground( 0 ) );
    treeItem.setForeground( null );
    assertEquals( tree.getForeground(), treeItem.getForeground( 0 ) );
    Color color2 = new Color(display, 255, 0, 0);
    color2.dispose();
    try{
      treeItem.setForeground(0,  color2 );
      fail("Disposed Image must not be set.");
    }
    catch (IllegalArgumentException e)
    {
      //Expected Exception
    }
  }

  public void testSetFontI() {
    Display display = new Display();
    Shell shell = new Shell( display, SWT.NONE );
    Tree tree = new Tree( shell, SWT.NONE );
    TreeItem treeItem = new TreeItem( tree, 0 );
    Font font = Graphics.getFont( "Helvetica", 10, SWT.NORMAL );
    // no columns
    assertTrue( tree.getFont().equals( treeItem.getFont( 0 ) ) );
    assertTrue( treeItem.getFont().equals( treeItem.getFont( 0 ) ) );
    treeItem.setFont( 0, font );
    assertTrue( font.equals( treeItem.getFont( 0 ) ) );
    // index beyond range - no error
    treeItem.setFont( 10, font );
    assertTrue( treeItem.getFont().equals( treeItem.getFont( 10 ) ) );
    // with columns
    new TreeColumn( tree, SWT.LEFT );
    new TreeColumn( tree, SWT.LEFT );
    // index beyond range - no error
    treeItem.setFont( 10, font );
    assertTrue( treeItem.getFont().equals( treeItem.getFont( 10 ) ) );
    treeItem.setFont( 0, font );
    assertTrue( font.equals( treeItem.getFont( 0 ) ) );
    treeItem.setFont( 0, null );
    assertTrue( tree.getFont().equals( treeItem.getFont( 0 ) ) );
    Font font2 = Graphics.getFont( "Helvetica", 20, SWT.NORMAL );
    treeItem.setFont( 0, font );
    treeItem.setFont( font2 );
    assertTrue( font.equals( treeItem.getFont( 0 ) ) );
    treeItem.setFont( 0, null );
    assertTrue( font2.equals( treeItem.getFont( 0 ) ) );
    treeItem.setFont( null );
    assertTrue( tree.getFont().equals( treeItem.getFont( 0 ) ) );
    // Test with a disposed font
    Font font3 = new Font( display, "Testfont", 10, SWT.BOLD );
    font3.dispose();
    try {
      treeItem.setFont(0, font3 );
      fail( "Disposed font must not be set." );
    } catch( IllegalArgumentException e ) {
      // Expected Exception
    }
  }

  public void testSetFont() {
      Display display = new Display();
      Shell shell = new Shell( display, SWT.NONE );
      Tree tree = new Tree( shell, SWT.NONE );
      Font treeFont = Graphics.getFont( "BeautifullyCraftedTreeFont",
                                        15,
                                        SWT.BOLD );
      tree.setFont( treeFont );
      TreeItem item = new TreeItem( tree, SWT.NONE );
      assertSame( treeFont, item.getFont() );
      Font itemFont = Graphics.getFont( "ItemFont", 40, SWT.NORMAL );
      item.setFont( itemFont );
      assertSame( itemFont, item.getFont() );
      item.setFont( null );
      assertSame( treeFont, item.getFont() );
   // Test with a disposed font
      Font font = new Font( display, "Testfont", 10, SWT.BOLD );
      font.dispose();
      try {
        item.setFont( font );
        fail( "Disposed font must not be set." );
      } catch( IllegalArgumentException e ) {
        // Expected Exception
      }
  }

  public void testSetBackgroundI() {
    Display display = new Display();
    Shell shell = new Shell( display, SWT.NONE );
    Tree tree = new Tree( shell, SWT.NONE );
    TreeItem treeItem = new TreeItem( tree, 0 );
    Color red = display.getSystemColor( SWT.COLOR_RED );
    Color blue = display.getSystemColor( SWT.COLOR_BLUE );
    // no columns
    assertEquals( tree.getBackground(), treeItem.getBackground( 0 ) );
    assertEquals( treeItem.getBackground(), treeItem.getBackground( 0 ) );
    treeItem.setBackground( 0, red );
    assertEquals( red, treeItem.getBackground( 0 ) );
    // index beyond range - no error
    treeItem.setBackground( 10, red );
    assertEquals( treeItem.getBackground(), treeItem.getBackground( 10 ) );
    // with columns
    new TreeColumn( tree, SWT.LEFT );
    new TreeColumn( tree, SWT.LEFT );
    // index beyond range - no error
    treeItem.setBackground( 10, red );
    assertEquals( treeItem.getBackground(), treeItem.getBackground( 10 ) );
    treeItem.setBackground( 0, red );
    assertEquals( red, treeItem.getBackground( 0 ) );
    treeItem.setBackground( 0, null );
    assertEquals( tree.getBackground(), treeItem.getBackground( 0 ) );
    treeItem.setBackground( 0, blue );
    treeItem.setBackground( red );
    assertEquals( blue, treeItem.getBackground( 0 ) );
    treeItem.setBackground( 0, null );
    assertEquals( red, treeItem.getBackground( 0 ) );
    treeItem.setBackground( null );
    assertEquals( tree.getBackground(), treeItem.getBackground( 0 ) );
    // Test for the case that the argument has been disposed
    Color color = new Color( display, 0, 255, 0 );
    color.dispose();
    try {
      treeItem.setBackground( 0, color );
      fail( "Disposed color must not be set." );
    } catch( IllegalArgumentException e ) {
      // Expected Exception
    }
  }

  public void testGetBoundsEmptyItem() {
    final Rectangle NULL_RECT = new Rectangle( 0, 0, 0, 0 );
    Display display = new Display();
    Shell shell = new Shell( display, SWT.NONE );
    Tree tree = new Tree( shell, SWT.NONE );
    TreeItem treeItem = new TreeItem( tree, 0 );
    TreeItem subItem = new TreeItem( treeItem, 0 );
    assertEquals( NULL_RECT, subItem.getBounds() );
    treeItem.setText( "foo" );
    assertTrue( treeItem.getBounds().height > 0 );
    assertTrue( treeItem.getBounds().width > 0 );
  }

  public void testGetBoundsWithoutColumns() {
    Display display = new Display();
    Shell shell = new Shell( display, SWT.NONE );
    Tree tree = new Tree( shell, SWT.NONE );
    TreeItem treeItem = new TreeItem( tree, 0 );
    // no columns - plain style
    Image image = Graphics.getImage( Fixture.IMAGE1 );
    Rectangle imageBounds = image.getBounds();
    String string = "hello";
    Point stringExtent = TextSizeDetermination.stringExtent( treeItem.getFont(),
                                                             string );
    Rectangle bounds;
    Rectangle bounds2;
    tree = new Tree( shell, 0 );
    treeItem = new TreeItem( tree, 0 );
    bounds = treeItem.getBounds( 0 );
    assertTrue( ":1a:", bounds.x > 0 && bounds.height > 0 );
    bounds = treeItem.getBounds( -1 );
    assertTrue( ":1b:", bounds.equals( new Rectangle( 0, 0, 0, 0 ) ) );
    bounds = treeItem.getBounds( 1 );
    assertTrue( ":1c:", bounds.equals( new Rectangle( 0, 0, 0, 0 ) ) );
    // unexpanded item
    TreeItem subItem = new TreeItem( treeItem, SWT.NONE );
    bounds = subItem.getBounds( 0 );
    assertTrue( ":1d:", bounds.equals( new Rectangle( 0, 0, 0, 0 ) ) );
    treeItem.setExpanded( true );
    bounds = subItem.getBounds( 0 );
    assertTrue( ":1e:", bounds.x > 0 && bounds.height > 0 );
    treeItem.setExpanded( false );
    bounds = subItem.getBounds( 0 );
    assertTrue( ":1f:", bounds.equals( new Rectangle( 0, 0, 0, 0 ) ) );
    treeItem.setExpanded( true );
    subItem.setText( string );
    bounds = subItem.getBounds( 0 );
    bounds2 = treeItem.getBounds( 0 );
    assertTrue( ":1g:", bounds.x > bounds2.x
                        && bounds.y >= bounds2.y + bounds2.height
                        && bounds.height > stringExtent.y
                        && bounds.width > stringExtent.x );
    treeItem.setText( string );
    bounds = treeItem.getBounds( 0 );
    assertTrue( ":1h:", bounds.x > 0
                        && bounds.height > stringExtent.y
                        && bounds.width > stringExtent.x );
    bounds2 = treeItem.getBounds();
    treeItem.setText( "" );
    bounds2 = treeItem.getBounds( 0 );
    assertTrue( ":1i:", bounds2.x > 0 && bounds2.height > 0 );
    assertTrue( ":1j:", bounds2.width < bounds.width );
    tree = new Tree( shell, 0 );
    treeItem = new TreeItem( tree, 0 );
    treeItem.setImage( image );
    bounds = treeItem.getBounds( 0 );
    assertTrue( ":1k:", bounds.x > 0
                        && bounds.height >= imageBounds.height
                        && bounds.width >= imageBounds.width );
    treeItem.setImage( ( Image )null );
    bounds2 = treeItem.getBounds( 0 );
    assertTrue( ":1l:", bounds2.x > 0 && bounds2.height > 0 );
    tree = new Tree( shell, 0 );
    treeItem = new TreeItem( tree, 0 );
    treeItem.setText( string );
    bounds = treeItem.getBounds( 0 );
    treeItem.setImage( image );
    bounds2 = treeItem.getBounds( 0 );
    assertTrue( ":1n:", bounds2.x > 0 && bounds2.height > 0 );
    assertTrue( ":1o:", bounds2.width > bounds.width );
    assertTrue( ":1p", bounds2.width >= stringExtent.x + imageBounds.width
                       && bounds2.height >= Math.max( stringExtent.y,
                                                      imageBounds.height ) );
  }

  public void testGetBoundsSubsequentRootItems() {
    final Rectangle NULL_RECT = new Rectangle( 0, 0, 0, 0 );
    Display display = new Display();
    Shell shell = new Shell( display, SWT.NONE );
    Tree tree = new Tree( shell, SWT.NONE );
    TreeItem rootItem = new TreeItem( tree, 0 );
    TreeItem subItem = new TreeItem( rootItem, 0 );
    TreeItem rootItem2 = new TreeItem( tree, 0 );
    Rectangle b1 = rootItem.getBounds();
    Rectangle b2 = rootItem2.getBounds();
    assertTrue( b2.y > b1.y );
    assertEquals( NULL_RECT, subItem.getBounds() );
    rootItem.setExpanded( true );
    assertTrue( subItem.getBounds().y >= rootItem.getBounds().y
                                         + rootItem.getBounds().height );
    assertTrue( rootItem2.getBounds().y >= subItem.getBounds().y
                                           + subItem.getBounds().height );
  }

  public void testGetBoundsWithScrolling() throws Exception {
    Display display = new Display();
    Shell shell = new Shell( display, SWT.NONE );
    Tree tree = new Tree( shell, SWT.NONE );
    TreeItem rootItem = new TreeItem( tree, 0 );
    TreeItem rootItem2 = new TreeItem( tree, 0 );
    TreeItem rootItem3 = new TreeItem( tree, 0 );
    Tree.checkAllData( tree );
    assertEquals( 0, rootItem.getBounds().y );
    assertEquals( 16, rootItem2.getBounds().y );
    assertEquals( 32, rootItem3.getBounds().y );
    Fixture.fakeNewRequest();
    String treeId = WidgetUtil.getId( tree );
    Fixture.fakeRequestParam( treeId + ".scrollLeft", "0" );
    Fixture.fakeRequestParam( treeId + ".scrollTop", "32" );
    Fixture.executeLifeCycleFromServerThread();
    assertEquals( -32, rootItem.getBounds().y );
    assertEquals( -16, rootItem2.getBounds().y );
    assertEquals( 0, rootItem3.getBounds().y );
  }

  public void testGetBoundsWithColumns() {
    Display display = new Display();
    Shell shell = new Shell( display, SWT.NONE );
    Tree tree = new Tree( shell, SWT.NONE );
    TreeColumn column1 = new TreeColumn( tree, SWT.NONE );
    column1.setText( "foo" );
    column1.setWidth( 100 );
    TreeColumn column2 = new TreeColumn( tree, SWT.NONE );
    column2.setText( "foo" );
    column2.setWidth( 100 );
    TreeColumn column3 = new TreeColumn( tree, SWT.NONE );
    column3.setText( "foo" );
    column3.setWidth( 100 );
    TreeItem rootItem = new TreeItem( tree, 0 );
    TreeItem rootItem2 = new TreeItem( tree, 0 );
    TreeItem rootItem3 = new TreeItem( tree, 0 );
    TreeItem subItem = new TreeItem( rootItem, 0 );
    final int rootItemWidth = rootItem.getBounds( 0 ).width;
    assertTrue( rootItemWidth < 100 ); // swt substracts indent
    assertEquals( 100, rootItem2.getBounds( 1 ).width );
    assertEquals( 100, rootItem3.getBounds( 2 ).width );
    assertEquals( 0, subItem.getBounds( 0 ).width );
    rootItem.setExpanded( true );
    assertTrue( subItem.getBounds( 0 ).width < rootItemWidth );
    assertTrue( rootItem.getBounds( 0 ).x > 0 );
    assertEquals( 100, rootItem.getBounds( 1 ).x );
    assertEquals( 200, rootItem.getBounds( 2 ).x );
  }

  public void testGetBoundsWithVisibleHeader() {
    Display display = new Display();
    Shell shell = new Shell( display, SWT.NONE );
    Tree tree = new Tree( shell, SWT.NONE );
    tree.setHeaderVisible( true );
    TreeItem item = new TreeItem( tree, 0 );
    Rectangle bounds = item.getBounds();
    assertTrue( bounds.y >= tree.getHeaderHeight() );
  }

  // TODO [bm]: hardcoded values - possible due to qooxdoo limitations
  public void testBoundsSubItemBug219374() {
    Display display = new Display();
    Shell shell = new Shell( display, SWT.NONE );
    Tree tree = new Tree( shell, SWT.NONE );
    TreeItem root = new TreeItem( tree, SWT.NONE );
    TreeItem root2 = new TreeItem( tree, SWT.NONE );
    TreeItem sub1 = new TreeItem( root, SWT.NONE );
    TreeItem sub2 = new TreeItem( root2, SWT.NONE );
    root2.setExpanded( true );
    // height is always 16
    assertEquals( 0, root.getBounds().y );
    assertEquals( 0, sub1.getBounds().y ); // not expanded
    assertEquals( 16, root2.getBounds().y );
    assertEquals( 32, sub2.getBounds().y );
    // indent for each level needs 19
    assertEquals( 19, root.getBounds().x );
    assertEquals( 0, sub1.getBounds().x ); // not expanded
    assertEquals( 19, root2.getBounds().x );
    assertEquals( 38, sub2.getBounds().x );
  }

  public void testTreeItemAdapter() throws Exception {
    Display display = new Display();
    Shell shell = new Shell( display, SWT.NONE );
    Tree tree = new Tree( shell, SWT.SINGLE );
    int columnCount = 3;
    createColumns( tree, columnCount );
    TreeItem item = new TreeItem( tree, SWT.NONE );
    ITreeItemAdapter adapter = ( ITreeItemAdapter )item.getAdapter( ITreeItemAdapter.class );
    assertNull( adapter.getCellBackgrounds() );
    assertNull( adapter.getCellForegrounds() );
    assertNull( adapter.getCellFonts() );
    Color bgColor = display.getSystemColor( SWT.COLOR_YELLOW );
    Color fgColor = display.getSystemColor( SWT.COLOR_BLUE );
    Font font = Graphics.getFont( "Helvetica", 12, SWT.NORMAL );
    item.setBackground( 0, bgColor );
    item.setForeground( 0, fgColor );
    item.setFont( 1, font );
    assertEquals( columnCount, adapter.getCellBackgrounds().length );
    assertEquals( columnCount, adapter.getCellForegrounds().length );
    assertEquals( columnCount, adapter.getCellFonts().length );
    assertEquals( bgColor, adapter.getCellBackgrounds()[ 0 ] );
    assertEquals( fgColor, adapter.getCellForegrounds()[ 0 ] );
    assertEquals( font, adapter.getCellFonts()[ 1 ] );
    assertNull( adapter.getCellBackgrounds()[ 1 ] );
    assertNull( adapter.getCellForegrounds()[ 1 ] );
    assertNull( adapter.getCellFonts()[ 0 ] );
  }

  public void testGetImageBoundsInvalidIndex() {
    Display display = new Display();
    Shell shell = new Shell( display, SWT.NONE );
    Tree tree = new Tree( shell, SWT.SINGLE );
    TreeItem item = new TreeItem( tree, SWT.NONE );
    assertEquals( new Rectangle( 0, 0, 0, 0 ), item.getImageBounds( 1 ) );
    assertEquals( new Rectangle( 0, 0, 0, 0 ), item.getImageBounds( -1 ) );
  }

  public void testGetImageBoundsColumns() {
    Display display = new Display();
    Shell shell = new Shell( display, SWT.NONE );
    Tree tree = new Tree( shell, SWT.SINGLE );
    TreeItem item = new TreeItem( tree, SWT.NONE );
    TreeColumn c1 = new TreeColumn( tree, SWT.NONE );
    c1.setWidth( 100 );
    TreeColumn c2 = new TreeColumn( tree, SWT.NONE );
    c2.setWidth( 100 );
    TreeColumn c3 = new TreeColumn( tree, SWT.NONE );
    c3.setWidth( 100 );
    item.setText( new String[]{
      "foo", "bar", "baz"
    } );
    Rectangle col0Bounds = item.getImageBounds( 0 );
    Rectangle col1Bounds = item.getImageBounds( 1 );
    Rectangle col2Bounds = item.getImageBounds( 2 );
    // without images, width and height should be 0
    assertEquals( 0, col0Bounds.height );
    assertEquals( 0, col0Bounds.width );
    assertEquals( 0, col1Bounds.height );
    assertEquals( 0, col1Bounds.width );
    assertEquals( 0, col2Bounds.height );
    assertEquals( 0, col2Bounds.width );
    // but x and y have to be set correctly
    assertTrue( col0Bounds.x > 0 ); // > 0 as we have an indent
    assertEquals( 100, col1Bounds.x );
    assertEquals( 200, col2Bounds.x );
    Image image = Graphics.getImage( Fixture.IMAGE1 );
    item.setImage( 0, image );
    item.setImage( 1, image );
    item.setImage( 2, image );
    Rectangle imageBounds = image.getBounds();
    col0Bounds = item.getImageBounds( 0 );
    col1Bounds = item.getImageBounds( 1 );
    col2Bounds = item.getImageBounds( 2 );
    assertEquals( imageBounds.height, col0Bounds.height );
    assertEquals( imageBounds.width, col0Bounds.width );
    assertEquals( imageBounds.height, col1Bounds.height );
    assertEquals( imageBounds.width, col1Bounds.width );
    assertEquals( imageBounds.height, col2Bounds.height );
    assertEquals( imageBounds.width, col2Bounds.width );
    assertTrue( col1Bounds.x > col0Bounds.x );
    assertTrue( col2Bounds.x > col1Bounds.x );
    TreeItem item2 = new TreeItem( tree, SWT.NONE );
    item2.setImage( 0, image );
    assertTrue( col0Bounds.y < item2.getImageBounds( 0 ).y );
  }

  public void testDynamicColumnCountAttributes() {
    Display display = new Display();
    Shell shell = new Shell( display, SWT.NONE );
    Tree tree = new Tree( shell, SWT.SINGLE );
    createColumns( tree, 1 );
    TreeItem treeItem = new TreeItem( tree, SWT.NONE );
    treeItem.setFont( 0, display.getSystemFont() );
    treeItem.setForeground( 0, display.getSystemColor( SWT.COLOR_BLACK ) );
    treeItem.setBackground( 0, display.getSystemColor( SWT.COLOR_BLACK ) );
    treeItem.setImage( 0, Graphics.getImage( Fixture.IMAGE1 ) );
    createColumns( tree, 1 );
    treeItem.setFont( 1, display.getSystemFont() );
    treeItem.setForeground( 1, display.getSystemColor( SWT.COLOR_BLACK ) );
    treeItem.setBackground( 1, display.getSystemColor( SWT.COLOR_BLACK ) );
    treeItem.setImage( 1, Graphics.getImage( Fixture.IMAGE1 ) );
  }

  public void testTextBounds() {
    // Test setup
    Display display = new Display();
    Shell shell = new Shell( display );
    Tree tree = new Tree( shell, SWT.NONE );
    TreeItem item = new TreeItem( tree, SWT.NONE );
    TreeColumn column1 = new TreeColumn( tree, SWT.NONE );
    column1.setWidth( 50 );
    TreeColumn column2 = new TreeColumn( tree, SWT.NONE );
    column2.setWidth( 50 );
    item.setText( 0, "col1" );
    item.setText( 1, "col2" );

    Rectangle textBounds1 = item.getTextBounds( 0 );
    Rectangle textBounds2 = item.getTextBounds( 1 );
    assertTrue( textBounds1.x + textBounds1.width <= textBounds2.x );
  }

  public void testTextBoundsWithInvalidIndex() {
    // Test setup
    Display display = new Display();
    Shell shell = new Shell( display );
    Tree tree = new Tree( shell, SWT.NONE );
    TreeItem item = new TreeItem( tree, SWT.NONE );
    item.setText( "abc" );
    // without columns
    assertEquals( new Rectangle( 0, 0, 0, 0 ), item.getTextBounds( 123 ) );
    // with column
    new TreeColumn( tree, SWT.NONE );
    assertEquals( new Rectangle( 0, 0, 0, 0 ), item.getTextBounds( 123 ) );
  }

  public void testTextBoundsWithImageAndColumns() {
    // Test setup
    Display display = new Display();
    Shell shell = new Shell( display );
    Tree tree = new Tree( shell, SWT.NONE );
    TreeItem item = new TreeItem( tree, SWT.NONE );
    TreeColumn column = new TreeColumn( tree, SWT.NONE );
    column.setWidth( 200 );

    Image image = Graphics.getImage( Fixture.IMAGE_100x50 );
    item.setImage( 0, image );
    assertTrue( item.getTextBounds( 0 ).x > image.getBounds().width );
    item.setImage( 0, null );
    assertTrue( item.getTextBounds( 0 ).x < image.getBounds().width );
  }

  public void testTextBoundsWithChangedFont() {
    Display display = new Display();
    Shell shell = new Shell( display );
    Tree tree = new Tree( shell, SWT.NONE );
    TreeItem item = new TreeItem( tree, SWT.NONE );
    item.setText( "abc" );
    Rectangle origBounds = item.getTextBounds( 0 );
    item.setFont( Graphics.getFont( "Helvetica", 50, SWT.BOLD ) );
    Rectangle actualBounds = item.getTextBounds( 0 );
    assertTrue( actualBounds.width > origBounds.width );
    item.setFont( null );
    actualBounds = item.getTextBounds( 0 );
    assertEquals( origBounds, actualBounds );
  }

  public void testTextBoundsWithCheckboxTree() {
    Display display = new Display();
    Shell shell = new Shell( display );
    Tree tree = new Tree( shell, SWT.CHECK );
    TreeColumn column = new TreeColumn( tree, SWT.LEFT );
    column.setWidth( 100 );
    TreeItem item = new TreeItem( tree, SWT.NONE );
    item.setText( "rama rama ding dong" );
    Rectangle textBounds = item.getTextBounds( 0 );
    // Item 0 must share the first column with the check box
    assertTrue( textBounds.width < 65 );
  }

  public void testTextBoundsWithCollapsedParentItem() {
    Display display = new Display();
    Shell shell = new Shell( display );
    Tree tree = new Tree( shell, SWT.NONE );
    TreeColumn column = new TreeColumn( tree, SWT.LEFT );
    column.setWidth( 100 );
    TreeItem item = new TreeItem( tree, SWT.NONE );
    item.setText( "item" );
    item.setExpanded( false );
    TreeItem subitem = new TreeItem( item, SWT.NONE );
    subitem.setText( "subitem" );
    Rectangle emptyBounds = new Rectangle( 0, 0, 0, 0 );
    assertTrue( emptyBounds.equals( subitem.getTextBounds( 0 ) ) );
    item.setExpanded( true );
    assertFalse( emptyBounds.equals( subitem.getTextBounds( 0 ) ) );
  }

  public void testNewItemWithIndex() {
    Display display = new Display();
    Shell shell = new Shell( display );
    Tree tree = new Tree( shell, SWT.NONE );
    TreeItem treeItem = new TreeItem( tree, SWT.NONE );
    treeItem.setText( "1" );
    TreeItem treeItem2 = new TreeItem( tree, SWT.NONE, 0 );
    treeItem2.setText( "2" );
    assertEquals( 1, tree.indexOf( treeItem ) );
    assertEquals( 0, tree.indexOf( treeItem2 ) );
    // Try to add an item with an index which is out of bounds
    try {
      new TreeItem( tree, SWT.NONE, tree.getItemCount() + 8 );
      String msg
        = "Index out of bounds expected when creating an item with "
        + "index > itemCount";
      fail( msg );
    } catch( IllegalArgumentException e ) {
      // expected
    }
    // Try to add an item with a negative index
    try {
      new TreeItem( tree, SWT.NONE, -1 );
      String msg
        = "Index out of bounds expected when creating an item with "
        + "index == -1";
      fail( msg );
    } catch( IllegalArgumentException e ) {
      // expected
    }
  }

  public void testNewItemWithIndexAsChild() {
    Display display = new Display();
    Shell shell = new Shell( display );
    Tree tree = new Tree( shell, SWT.NONE );
    TreeItem root = new TreeItem( tree, SWT.NONE );
    root.setText( "root" );
    TreeItem treeItem = new TreeItem( root, SWT.NONE );
    treeItem.setText( "1" );
    TreeItem treeItem2 = new TreeItem( root, SWT.NONE, 0 );
    treeItem2.setText( "2" );
    assertEquals( 0, tree.indexOf( root ) );
    assertEquals( 1, root.indexOf( treeItem ) );
    assertEquals( 0, root.indexOf( treeItem2 ) );
  }

  protected void setUp() throws Exception {
    Fixture.setUp();
  }

  protected void tearDown() throws Exception {
    Fixture.tearDown();
  }

  private static TreeColumn[] createColumns( final Tree tree, final int count )
  {
    TreeColumn[] result = new TreeColumn[ count ];
    for( int i = 0; i < count; i++ ) {
      TreeColumn column = new TreeColumn( tree, SWT.NONE );
      column.setText( i % 2 == 1 ? "foo" : "bar" );
      column.setWidth( 100 );
      result[ i ] = column;
    }
    return result;
  }
}
