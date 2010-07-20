/*******************************************************************************
 * Copyright (c) 2009 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     EclipseSource - initial API and implementation
 ******************************************************************************/

package org.eclipse.swt.custom;

import java.util.ArrayList;

import junit.framework.TestCase;

import org.eclipse.rwt.Fixture;
import org.eclipse.rwt.graphics.Graphics;
import org.eclipse.rwt.lifecycle.PhaseId;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.*;

/*
 * Note:
 * As long as CCombo uses a ListModel to maintain its items and selection,
 * most of the add/remove/getItem.../selection test cases can be omitted.
 * They are covered in List_Test
 */
public class CCombo_Test extends TestCase {

  private boolean listenerCalled;

  public void testDeselect() {
    Display display = new Display();
    Composite shell = new Shell( display, SWT.NONE );
    CCombo combo = new CCombo( shell, SWT.NONE );
    combo.add( "item1" );
    combo.add( "item2" );
    combo.add( "item3" );
    // deselect the currently selected index/item
    combo.select( 1 );
    combo.deselect( 1 );
    assertEquals( -1, combo.getSelectionIndex() );
    // deselect works only if the argument matches the currently selected index
    // (as far as I understand SWT doc/implementation...)
    combo.select( 1 );
    combo.deselect( 0 );
    assertEquals( 1, combo.getSelectionIndex() );
  }

  public void testGetText() {
    Display display = new Display();
    Shell shell = new Shell( display, SWT.NONE );
    CCombo combo = new CCombo( shell, SWT.READ_ONLY );
    combo.add( "item1" );
    combo.add( "item2" );
    combo.add( "item3" );
    // test get text without setting an explicit selection
    assertEquals( combo.getText(), "" );
    // test after selection
    combo.select( 2 );
    assertEquals( combo.getText(), "item3" );
    // test after deselection
    combo.deselectAll();
    assertEquals( combo.getText(), "" );
    // testing editable CCombo
    combo = new CCombo( shell, SWT.NONE );
    String[] cases = { "", "fred", "fredfred" };
    for( int i = 0; i < cases.length; i++ ) {
      combo.setText( cases[ i ] );
      assertTrue( ":a:" + String.valueOf( i ),
                  cases[ i ].equals( combo.getText() ) );
    }
  }

  public void testTextLimit() {
    Display display = new Display();
    Shell shell = new Shell( display, SWT.NONE );
    CCombo combo = new CCombo( shell, SWT.NONE );
    combo.setTextLimit( -1 );
    assertEquals( Combo.LIMIT, combo.getTextLimit() );
    combo.setTextLimit( -20 );
    assertEquals( Combo.LIMIT, combo.getTextLimit() );
    combo.setTextLimit( -12345 );
    assertEquals( Combo.LIMIT, combo.getTextLimit() );
    combo.setTextLimit( 20 );
    assertEquals( 20, combo.getTextLimit() );
    try {
      combo.setTextLimit( 0 );
      fail( "Must not allow to set textLimit to zero" );
    } catch( IllegalArgumentException e ) {
      // as expected
    }
    combo.setText( "Sample_text" );
    combo.setTextLimit( 6 );
    assertEquals( "Sample_text", combo.getText() );
    combo.setText( "Other_text" );
    assertEquals( "Other_", combo.getText() );
  }

  public void testListVisible() {
    Display display = new Display();
    Composite shell = new Shell( display );
    CCombo combo = new CCombo( shell, SWT.NONE );
    combo.setListVisible( true );
    assertTrue( combo.getListVisible() );
    combo.setListVisible( false );
    assertFalse( combo.getListVisible() );
  }

  public void testSelection() {
    Display display = new Display();
    Shell shell = new Shell( display, SWT.NONE );
    CCombo combo = new CCombo( shell, SWT.NONE );
    // test clearSelection
    combo.setText( "abc" );
    combo.setSelection( new Point( 1, 3 ) );
    combo.clearSelection();
    assertEquals( new Point( 0, 0 ), combo.getSelection() );
    // test setSelection( a, b ), a < b
    combo.clearSelection();
    combo.setText( "test text" );
    combo.setSelection( new Point( 3, 6 ) );
    assertEquals( new Point( 3, 6 ), combo.getSelection() );
    // test setSelection( a, b ), a > b
    combo.clearSelection();
    combo.setSelection( new Point( 5, 2 ) );
    assertEquals( new Point( 2, 5 ), combo.getSelection() );

  }

  public void testIndexOf() {
    Display display = new Display();
    Shell shell = new Shell( display, SWT.NONE );
    CCombo combo = new CCombo( shell, SWT.READ_ONLY );
    combo.add( "string0" );
    try {
      combo.indexOf( null );
      fail( "No exception thrown for string == null" );
    } catch( IllegalArgumentException e ) {
    }
    combo.removeAll();
    int number = 5;
    for( int i = 0; i < number; i++ ) {
      combo.add( "fred" + i );
    }
    for( int i = 0; i < number; i++ ) {
      assertEquals( i, combo.indexOf( "fred" + i ) );
    }
    for( int i = 0; i < number; i++ ) {
      combo.add( "fred" + i );
    }
    combo.removeAll();
    for( int i = 0; i < number; i++ ) {
      assertEquals( -1, combo.indexOf( "fred" + i ) );
    }
    for( int i = 0; i < number; i++ ) {
      combo.add( "fred" + i );
    }
    combo.remove( "fred3" );
    for( int i = 0; i < 3; i++ ) {
      assertEquals( i, combo.indexOf( "fred" + i ) );
    }
    assertEquals( -1, combo.indexOf( "fred3" ) );
    for( int i = 4; i < number; i++ ) {
      assertEquals( i - 1, combo.indexOf( "fred" + i ) );
    }
    combo.removeAll();
    for( int i = 0; i < number; i++ ) {
      combo.add( "fred" + i );
    }
    combo.remove( 2 );
    for( int i = 0; i < 2; i++ ) {
      assertEquals( i, combo.indexOf( "fred" + i ) );
    }
    assertEquals( -1, combo.indexOf( "fred2" ) );
    for( int i = 3; i < number; i++ ) {
      assertEquals( i - 1, combo.indexOf( "fred" + i ) );
    }
  }

  public void testIndexOfI() {
    Display display = new Display();
    Shell shell = new Shell( display, SWT.NONE );
    CCombo combo = new CCombo( shell, SWT.READ_ONLY );
    combo.add( "string0" );
    try {
      combo.indexOf( null );
      fail( "No exception thrown for string == null" );
    } catch( IllegalArgumentException e ) {
    }
    assertEquals( -1, combo.indexOf( "string0", -1 ) );
    combo.removeAll();
    int number = 5;
    for( int i = 0; i < number; i++ ) {
      combo.add( "fred" + i );
    }
    for( int i = 0; i < number; i++ ) {
      assertTrue( ":a:" + i, combo.indexOf( "fred" + i, 0 ) == i );
    }
    for( int i = 0; i < number; i++ ) {
      assertTrue( ":b:" + i, combo.indexOf( "fred" + i, i + 1 ) == -1 );
    }
    for( int i = 0; i < number; i++ ) {
      combo.add( "fred" + i );
    }
    for( int i = 0; i < 3; i++ ) {
      assertTrue( ":a:" + i, combo.indexOf( "fred" + i, 0 ) == i );
    }
    for( int i = 3; i < number; i++ ) {
      assertTrue( ":b:" + i, combo.indexOf( "fred" + i, 3 ) == i );
    }
    for( int i = 0; i < number; i++ ) {
      assertTrue( ":b:" + i, combo.indexOf( "fred" + i, i ) == i );
    }
  }

  public void testSetText() {
    Display display = new Display();
    Shell shell = new Shell( display, SWT.NONE );
    CCombo combo = new CCombo( shell, SWT.NONE );
    try {
      combo.setText( null );
      fail( "No exception thrown for text == null" );
    } catch( IllegalArgumentException e ) {
    }
    String[] cases = {
      "", "fred", "fred0"
    };
    for( int i = 0; i < cases.length; i++ ) {
      combo.setText( cases[ i ] );
      assertTrue( ":a:" + i, combo.getText().equals( cases[ i ] ) );
    }
    for( int i = 0; i < 5; i++ ) {
      combo.add( "fred" );
    }
    for( int i = 0; i < cases.length; i++ ) {
      combo.setText( cases[ i ] );
      assertTrue( ":b:" + i, combo.getText().equals( cases[ i ] ) );
    }
    for( int i = 0; i < 5; i++ ) {
      combo.add( "fred" + i );
    }
    for( int i = 0; i < cases.length; i++ ) {
      combo.setText( cases[ i ] );
      assertTrue( ":c:" + i, combo.getText().equals( cases[ i ] ) );
    }
  }

  public void testSelect() {
    Display display = new Display();
    Shell shell = new Shell( display, SWT.NONE );
    CCombo combo = new CCombo( shell, SWT.NONE );
    combo.add( "test" );
    combo.select( 0 );
    assertEquals( "test", combo.getText() );
    combo.removeAll();
    assertEquals( "", combo.getText() );
    combo.add( "foo" );
    combo.select( 0 );
    assertEquals( "foo", combo.getText() );
  }

  public void testSelect2() {
    Display display = new Display();
    Shell shell = new Shell( display, SWT.NONE );
    CCombo combo = new CCombo( shell, SWT.NONE );
    combo.add( "test" );
    combo.select( 0 );
    assertEquals( "test", combo.getText() );
    combo.remove( 0 );
    combo.add( "foo" );
    combo.select( 0 );
    assertEquals( "foo", combo.getText() );
    combo = new CCombo( shell, SWT.READ_ONLY );
    combo.add( "test" );
    combo.select( 0 );
    assertEquals( "test", combo.getText() );
    combo.remove( 0 );
    assertEquals( "", combo.getText() );
    combo.add( "foo" );
    combo.select( 0 );
    assertEquals( "foo", combo.getText() );
  }

  public void testSelect3() {
    Display display = new Display();
    Shell shell = new Shell( display, SWT.NONE );
    CCombo combo = new CCombo( shell, SWT.NONE );
    combo.add( "test" );
    combo.add( "test1" );
    combo.add( "test2" );
    combo.setText( "foo" );
    combo.removeAll();
    assertEquals( "", combo.getText() );
    combo = new CCombo( shell, SWT.READ_ONLY );
    combo.add( "test" );
    combo.add( "test1" );
    combo.add( "test2" );
    combo.setText( "foo" );
    combo.removeAll();
    assertEquals( "", combo.getText() );
    combo = new CCombo( shell, SWT.NONE );
    combo.add( "test" );
    combo.add( "test1" );
    combo.add( "test2" );
    combo.select( 1 );
    combo.remove( 1 );
    assertEquals( "", combo.getText() );
    combo = new CCombo( shell, SWT.NONE );
    combo.add( "test" );
    combo.add( "test1" );
    combo.add( "test2" );
    combo.select( 1 );
    combo.remove( "test1" );
    assertEquals( "", combo.getText() );
    combo = new CCombo( shell, SWT.NONE );
    combo.add( "test" );
    combo.add( "test1" );
    combo.add( "test2" );
    combo.select( 1 );
    combo.setText( "foo" );
    combo.remove( 1 );
    assertEquals( "foo", combo.getText() );
    combo.removeAll();
    combo = new CCombo( shell, SWT.NONE );
    combo.add( "test" );
    combo.add( "test1" );
    combo.add( "test2" );
    combo.add( "test3" );
    combo.select( 1 );
    combo.remove( 1, 3 );
    assertEquals( "", combo.getText() );
  }

  public void testSelectWithInvalidIndex() {
    Display display = new Display();
    Shell shell = new Shell( display, SWT.NONE );
    CCombo combo = new CCombo( shell, SWT.NONE );
    combo.add( "test" );
    combo.add( "test1" );
    combo.add( "test2" );
    combo.select( 1 );
    assertEquals( 1, combo.getSelectionIndex() );
    assertEquals( "test1", combo.getText() );
    combo.select( -2 );
    assertEquals( 1, combo.getSelectionIndex() );
    assertEquals( "test1", combo.getText() );
    combo.select( 10 );
    assertEquals( 1, combo.getSelectionIndex() );
    assertEquals( "test1", combo.getText() );
    combo.select( -1 );
    assertEquals( -1, combo.getSelectionIndex() );
    assertEquals( "", combo.getText() );
  }

  public void testSetTextSelect() {
    Display display = new Display();
    Composite shell = new Shell( display, SWT.NONE );
    CCombo combo = new CCombo( shell, SWT.NONE );
    combo.add( "test" );
    combo.add( "test2" );
    combo.add( "test3" );
    combo.setText( "foo" );
    combo.select( 1 );
    assertEquals( "test2", combo.getText() );
  }

  public void testRemoveAll() {
    Display display = new Display();
    Composite shell = new Shell( display, SWT.NONE );
    CCombo combo = new CCombo( shell, SWT.NONE );
    combo.add( "1" );
    combo.add( "2" );
    combo.removeAll();
    assertEquals( 0, combo.getItems().length );
  }

  public void testDispose() {
    Display display = new Display();
    Composite shell = new Shell( display );
    CCombo combo = new CCombo( shell, SWT.NONE );
    combo.add( "test" );
    combo.dispose();
    assertTrue( combo.isDisposed() );
  }

  public void testAddModifyListener() {
    Fixture.fakePhase( PhaseId.PROCESS_ACTION );
    Display display = new Display();
    Composite shell = new Shell( display );
    CCombo combo = new CCombo( shell, SWT.NONE );
    combo.setItems (new String [] {"A-1", "B-1", "C-1"});
    ModifyListener listener = new ModifyListener() {
      public void modifyText( final ModifyEvent event ) {
        listenerCalled = true;
      }
    };
    try {
      combo.addModifyListener( null );
      fail( "removeModifyListener must not allow null listener" );
    } catch( IllegalArgumentException e ) {
      // expected
    }
    // test whether all content modifying API methods send a Modify event
    combo.addModifyListener( listener );
    listenerCalled = false;
    combo.setText( "new text" );
    assertTrue( listenerCalled );
    listenerCalled = false;
 // select and deselect item(s) test cases
    combo.select( 1 );
    assertTrue( listenerCalled );
    listenerCalled = false;
    combo.deselect( 1 );
    assertTrue( listenerCalled );
    listenerCalled = false;
    combo.select( 0 );
    assertTrue( listenerCalled );
    listenerCalled = false;
    combo.deselectAll();
    assertTrue( listenerCalled );
    // remove item(s) test cases
    listenerCalled = false;
    combo.select(0);
    combo.remove(0);
    assertTrue( listenerCalled );
    listenerCalled = false;
    combo.setItems (new String [] {"A-1", "B-1", "C-1"});
    combo.select(0);
    combo.remove("A-1");
    assertTrue( listenerCalled );
    listenerCalled = false;
    combo.setItems (new String [] {"A-1", "B-1", "C-1"});
    combo.select(0);
    combo.remove(0,1);
    assertTrue( listenerCalled );
    listenerCalled = false;
    combo.setItems (new String [] {"A-1", "B-1", "C-1"});
    combo.select(0);
    combo.removeAll();
    assertTrue( listenerCalled );
    //
    listenerCalled = false;
    combo.removeModifyListener( listener );
    // cause to call the listener.
    combo.setText( "line" );
    assertFalse( listenerCalled );
    try {
      combo.removeModifyListener( null );
      fail( "removeModifyListener must not allow null listener" );
    } catch( IllegalArgumentException e ) {
      // expected
    }
  }

  public void testAddModifyListenerReadOnly() {
    Fixture.fakePhase( PhaseId.PROCESS_ACTION );
    Display display = new Display();
    Composite shell = new Shell( display );
    CCombo combo = new CCombo( shell, SWT.READ_ONLY );
    combo.setItems (new String [] {"A-1", "B-1", "C-1"});
    ModifyListener listener = new ModifyListener() {
      public void modifyText( final ModifyEvent event ) {
        listenerCalled = true;
      }
    };
    try {
      combo.addModifyListener( null );
      fail( "removeModifyListener must not allow null listener" );
    } catch( IllegalArgumentException e ) {
      // expected
    }
    // test whether all content modifying API methods send a Modify event
    combo.addModifyListener( listener );
    listenerCalled = false;
 // select and deselect item(s) test cases
    combo.select( 1 );
    assertTrue( listenerCalled );
    listenerCalled = false;
    combo.deselect( 1 );
    assertTrue( listenerCalled );
    listenerCalled = false;
    combo.select( 0 );
    assertTrue( listenerCalled );
    listenerCalled = false;
    combo.deselectAll();
    assertTrue( listenerCalled );
    // remove item(s) test cases
    listenerCalled = false;
    combo.select(0);
    combo.remove(0);
    assertTrue( listenerCalled );
    listenerCalled = false;
    combo.setItems (new String [] {"A-1", "B-1", "C-1"});
    combo.select(0);
    combo.remove("A-1");
    assertTrue( listenerCalled );
    listenerCalled = false;
    combo.setItems (new String [] {"A-1", "B-1", "C-1"});
    combo.select(0);
    combo.remove(0,1);
    assertTrue( listenerCalled );
    listenerCalled = false;
    combo.setItems (new String [] {"A-1", "B-1", "C-1"});
    combo.select(0);
    combo.removeAll();
    assertTrue( listenerCalled );
    //
    listenerCalled = false;
    combo.removeModifyListener( listener );
    // cause to call the listener.
    combo.select( 2 );
    assertFalse( listenerCalled );
    try {
      combo.removeModifyListener( null );
      fail( "removeModifyListener must not allow null listener" );
    } catch( IllegalArgumentException e ) {
      // expected
    }
  }

  public void testVerifyEvent() {
    VerifyListener verifyListener;
    Fixture.fakePhase( PhaseId.PROCESS_ACTION );
    final java.util.List log = new ArrayList();
    Display display = new Display();
    Shell shell = new Shell( display );
    final CCombo combo = new CCombo( shell, SWT.NONE );
    combo.addModifyListener( new ModifyListener() {
      public void modifyText( final ModifyEvent event ) {
        log.add( event );
      }
    } );
    combo.addVerifyListener( new VerifyListener() {
      public void verifyText( final VerifyEvent event ) {
        log.add( event );
      }
    } );

    // VerifyEvent is also sent when setting text to the already set value
    log.clear();
    combo.setText( "" );
    assertEquals( 2, log.size() );
    assertEquals( VerifyEvent.class, log.get( 0 ).getClass() );
    assertEquals( ModifyEvent.class, log.get( 1 ).getClass() );

    // Test verifyListener that prevents (doit=false) change
    combo.setText( "" );
    log.clear();
    verifyListener = new VerifyListener() {
      public void verifyText( final VerifyEvent event ) {
        event.doit = false;
      }
    };
    combo.addVerifyListener( verifyListener );
    combo.setText( "other" );
    assertEquals( 1, log.size() );
    assertEquals( VerifyEvent.class, log.get( 0 ).getClass() );
    assertEquals( "", combo.getText() );
    combo.removeVerifyListener( verifyListener );

    // Test verifyListener that manipulates text
    combo.setText( "" );
    log.clear();
    verifyListener = new VerifyListener() {
      public void verifyText( final VerifyEvent event ) {
        event.text = "manipulated";
      }
    };
    combo.addVerifyListener( verifyListener );
    combo.setText( "other" );
    assertEquals( 2, log.size() );
    assertEquals( VerifyEvent.class, log.get( 0 ).getClass() );
    assertEquals( ModifyEvent.class, log.get( 1 ).getClass() );
    assertEquals( "manipulated", combo.getText() );
    combo.removeVerifyListener( verifyListener );

    // Ensure that VerifyEvent#start and #end denote the positions of the old
    // text and #text denotes the text to be set
    String oldText = "old";
    combo.setText( oldText );
    log.clear();
    String newText = oldText + "changed";
    combo.setText( newText );
    assertEquals( 2, log.size() );
    assertEquals( VerifyEvent.class, log.get( 0 ).getClass() );
    VerifyEvent verifyEvent = ( VerifyEvent )log.get( 0 );
    assertEquals( 0, verifyEvent.start );
    assertEquals( oldText.length(), verifyEvent.end );
    assertEquals( newText, verifyEvent.text );
    assertEquals( ModifyEvent.class, log.get( 1 ).getClass() );

    // Ensure that VerifyEvent#text denotes the text to be set
    // and not the cut by textLimit one
    combo.setTextLimit( 5 );
    String sampleText = "sample_text";
    log.clear();
    combo.setText( sampleText );
    assertEquals( 2, log.size() );
    assertEquals( VerifyEvent.class, log.get( 0 ).getClass() );
    verifyEvent = ( VerifyEvent )log.get( 0 );
    assertEquals( sampleText, verifyEvent.text );
  }

  public void testVisibleItemCount() {
	  Display display = new Display();
    Composite shell = new Shell( display );
    CCombo combo = new CCombo( shell, SWT.NONE );
    combo.add( "1" );
    combo.add( "2" );
    combo.add( "3" );
    int visibleItemCount = combo.getVisibleItemCount();
    combo.setVisibleItemCount( -2 );
    assertEquals( visibleItemCount, combo.getVisibleItemCount() );
    combo.setVisibleItemCount( 2 );
    assertEquals( 2, combo.getVisibleItemCount() );
    combo.setVisibleItemCount( 3 );
    assertEquals( 3, combo.getVisibleItemCount() );
  }

  public void testComputeSize() {
    Fixture.fakePhase( PhaseId.PROCESS_ACTION );
    Display display = new Display();
    Composite shell = new Shell( display, SWT.NONE );
    CCombo combo = new CCombo( shell, SWT.NONE );
    Point expected = new Point( 64, 17 );
    assertEquals( expected, combo.computeSize( SWT.DEFAULT, SWT.DEFAULT ) );

    combo = new CCombo( shell, SWT.BORDER );
    expected = new Point( 66, 19 );
    assertEquals( expected, combo.computeSize( SWT.DEFAULT, SWT.DEFAULT ) );

    combo = new CCombo( shell, SWT.FLAT );
    expected = new Point( 64, 17 );
    assertEquals( expected, combo.computeSize( SWT.DEFAULT, SWT.DEFAULT ) );

    combo = new CCombo( shell, SWT.NONE );
    combo.add( "1" );
    combo.add( "22" );
    combo.add( "333" );
    expected = new Point( 47, 17 );
    assertEquals( expected, combo.computeSize( SWT.DEFAULT, SWT.DEFAULT ) );

    expected = new Point( 100, 100 );
    assertEquals( expected, combo.computeSize( 100, 100 ) );
  }

  public void testSetTextAndSelection() {
    Fixture.fakePhase( PhaseId.PROCESS_ACTION );
    Display display = new Display();
    Shell shell = new Shell( display, SWT.NONE );
    final CCombo combo = new CCombo( shell, SWT.NONE );
    combo.add( "test" );
    combo.add( "test1" );
    combo.add( "test2" );
    combo.addVerifyListener( new VerifyListener() {
      public void verifyText( VerifyEvent event ) {
        event.text = event.text + "2";
      }
    } );
    combo.setText( "test" );
    assertEquals( 2, combo.getSelectionIndex() );
  }

  public void testGetTextHeight() {
    Display display = new Display();
    Shell shell = new Shell( display );
    CCombo combo = new CCombo( shell, SWT.NONE );
    // default theme font is 11px
    assertEquals( 13, combo.getTextHeight() );
    combo.setFont( Graphics.getFont( "Helvetica", 12, SWT.NORMAL ) );
    assertEquals( 14, combo.getTextHeight() );
    combo.setFont( null );
    assertEquals( 13, combo.getTextHeight() );
  }

  protected void setUp() throws Exception {
    Fixture.setUp();
  }

  protected void tearDown() throws Exception {
    Fixture.tearDown();
  }
}
