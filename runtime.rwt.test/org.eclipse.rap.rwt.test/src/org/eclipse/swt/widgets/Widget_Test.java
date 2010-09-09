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

import java.util.ArrayList;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

import org.eclipse.rwt.Fixture;
import org.eclipse.rwt.internal.lifecycle.DisposedWidgets;
import org.eclipse.rwt.lifecycle.IWidgetAdapter;
import org.eclipse.rwt.lifecycle.PhaseId;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.*;


public class Widget_Test extends TestCase {

  protected void setUp() throws Exception {
    Fixture.setUp();
  }

  protected void tearDown() throws Exception {
    Fixture.tearDown();
  }

  public void testGetAdapter() {
    Fixture.fakePhase( PhaseId.PROCESS_ACTION );
    Display display = new Display();
    Widget shell = new Shell( display );
    // ensure that Widget#getAdapter can be called after widget was disposed of
    shell.dispose();
    assertNotNull( shell.getAdapter( IWidgetAdapter.class ) );
  }

  public void testCheckWidget() throws InterruptedException {
    Display display = new Display();
    Shell shell = new Shell( display, SWT.NONE );
    final Widget widget = new Text( shell, SWT.NONE );

    final Throwable[] throwable = new Throwable[ 1 ];
    final String[] message = new String[ 1 ];
    Thread thread = new Thread( new Runnable() {
      public void run() {
        try {
          widget.checkWidget();
          fail( "Illegal thread access expected." );
        } catch( final SWTException swte ) {
          message[ 0 ] = swte.getMessage();
        } catch( final Throwable thr ) {
          throwable[ 0 ] = thr;
        }
      }
    });
    thread.start();
    thread.join();
    assertEquals( message[ 0 ], "Invalid thread access" );
    assertNull( throwable[ 0 ] );
  }

  public void testData() {
    Display display = new Display();
    Shell shell = new Shell( display );
    Widget widget = new Text( shell, SWT.NONE );

    // Test initial state
    assertEquals( null, widget.getData() );

    Object singleData = new Object();
    // Set/get some single data
    widget.setData( singleData );
    assertSame( singleData, widget.getData() );

    // Set/get some keyed data, ensure that single data remains unchanged
    Object keyedData = new Object();
    widget.setData( "key", keyedData );
    widget.setData( "null-key", null );
    assertSame( singleData, widget.getData() );
    assertSame( keyedData, widget.getData( "key" ) );
    assertSame( null, widget.getData( "null-key" ) );

    // Test 'deleting' a key
    widget.setData( "key", null );
    assertNull( widget.getData( "key" ) );

    // Test keyed data with non-existing key
    assertNull( widget.getData( "non-existing-key" ) );

    // Test keyed data with illegal arguments
    try {
      widget.setData( null, new Object() );
      fail( "Must not allow to set data with null key" );
    } catch( IllegalArgumentException e ) {
      // expected
    }
    try {
      widget.getData( null );
      fail( "Must not allow to get data for null key" );
    } catch( IllegalArgumentException e ) {
      // expected
    }
  }

  public void testDisposeParentWhileInDispose() {
    // This test leads to a stack overflow or, if line "item[ 0 ].dispose();"
    // is activated to a NPE
    Fixture.fakePhase( PhaseId.PROCESS_ACTION );
    Display display = new Display();
    Shell shell = new Shell( display, SWT.NONE );
    final Composite composite = new Composite( shell, SWT.NONE );
    ToolBar toolbar = new ToolBar( composite, SWT.NONE );
    final ToolItem[] item = { null };
    toolbar.addDisposeListener( new DisposeListener() {
      public void widgetDisposed( final DisposeEvent event ) {
        item[ 0 ].dispose();
      }
    } );
    toolbar.addDisposeListener( new DisposeListener() {
      public void widgetDisposed( final DisposeEvent event ) {
        composite.dispose();
      }
    } );
    item[ 0 ] = new ToolItem( toolbar, SWT.PUSH );
    shell.dispose();
    // no assert: this test ensures that no StackOverflowError occurs
  }

  public void testDisposeSelfWhileInDispose() {
    Fixture.fakePhase( PhaseId.PROCESS_ACTION );
    Display display = new Display();
    final Shell widget = new Shell( display, SWT.NONE );
    widget.addDisposeListener( new DisposeListener() {
      public void widgetDisposed( final DisposeEvent event ) {
        widget.dispose();
      }
    } );
    widget.dispose();
    // no assert: this test ensures that no exception occurs
  }

  public void testCheckBits() {
    int style = SWT.VERTICAL | SWT.HORIZONTAL;
    int result = Widget.checkBits( style,
                                   SWT.VERTICAL,
                                   SWT.HORIZONTAL,
                                   0,
                                   0,
                                   0,
                                   0 );
    assertTrue( ( result & SWT.VERTICAL ) != 0 );
    assertFalse( ( result & SWT.HORIZONTAL ) != 0 );
  }

  public void testDispose() {
    Display display = new Display();
    Shell shell = new Shell( display );
    Widget widget = new Button( shell, SWT.NONE );

    // Ensure initial state
    assertEquals( false, widget.isDisposed() );

    // Test dispose the first time
    widget.dispose();
    assertEquals( true, widget.isDisposed() );

    // Disposing of an already disposed of widget does nothing
    widget.dispose();
    assertEquals( true, widget.isDisposed() );
  }

  public void testDisposeFromIllegalThread() throws InterruptedException {
    Display display = new Display();
    Shell shell = new Shell( display );
    final Widget widget = new Button( shell, SWT.NONE );

    final AssertionFailedError[] failure = new AssertionFailedError[ 1 ];
    Thread thread = new Thread( new Runnable() {
      public void run() {
        try {
          widget.dispose();
          fail( "Must not allow to dispose of a widget from a non-UI-thread" );
        } catch( SWTException e ) {
          // expected
        } catch( final AssertionFailedError afa ) {
          failure[ 0 ] = afa;
        }
      }
    } );
    thread.start();
    thread.join();

    if( failure[ 0 ] != null ) {
      throw failure[ 0 ];
    }
  }

  public void testDisposeWithException() {
    Fixture.fakePhase( PhaseId.PROCESS_ACTION );
    Display display = new Display();
    Widget widget = new Shell( display );
    widget.addDisposeListener( new DisposeListener() {
      public void widgetDisposed( final DisposeEvent event ) {
        throw new RuntimeException();
      }
    } );
    try {
      widget.dispose();
      fail( "Wrong test setup: dispose listener must throw exception" );
    } catch( Exception e ) {
      // expected
    }
    assertFalse( widget.isDisposed() );
    assertEquals( 0, DisposedWidgets.getAll().length );
  }

  public void testRemoveListener() {
    // Ensure that removing a listener that was never added is ignored
    // silently see https://bugs.eclipse.org/251816
    Display display = new Display();
    Widget widget = new Shell( display );
    widget.removeListener( SWT.Activate, new Listener() {
      public void handleEvent( final Event event ) {
      }
    } );
  }

  public void testNotifyListeners() {
    Fixture.fakePhase( PhaseId.PROCESS_ACTION );
    Display display = new Display();
    Widget widget = new Shell( display );
    final StringBuffer log = new StringBuffer();
    widget.addListener( SWT.Resize, new Listener() {
      public void handleEvent( final Event event ) {
        log.append( "untyped" );
      }
    } );
    widget.notifyListeners( SWT.Resize, new Event() );
    assertEquals( "untyped", log.toString() );
  }

  public void testNotifyListenersTyped() {
    Fixture.fakePhase( PhaseId.PROCESS_ACTION );
    Display display = new Display();
    Shell shell = new Shell( display );
    final StringBuffer log = new StringBuffer();
    shell.addControlListener( new ControlAdapter() {
      public void controlResized( final ControlEvent e ) {
        log.append( "typed" );
      }
    } );
    shell.notifyListeners( SWT.Resize, new Event() );
    assertEquals( "typed", log.toString() );
  }

  public void testNotifyListenersDisplayFilter() {
    Fixture.fakePhase( PhaseId.PROCESS_ACTION );
    Display display = new Display();
    Shell shell = new Shell( display );
    final StringBuffer log = new StringBuffer();
    display.addFilter( SWT.Resize, new Listener() {
      public void handleEvent( final Event event ) {
        log.append( "filter" );
      }
    });
    shell.notifyListeners( SWT.Resize, new Event() );
    assertEquals( "filter", log.toString() );
  }

  // SWT always overrides e.type, e.display and e.widget
  public void testNotifyListenersEventFields() {
    Fixture.fakePhase( PhaseId.PROCESS_ACTION );
    final Display display = new Display();
    final Shell shell = new Shell( display );
    final StringBuffer log = new StringBuffer();
    display.addFilter( SWT.Resize, new Listener() {
      public void handleEvent( final Event event ) {
        assertEquals( SWT.Resize, event.type );
        assertEquals( shell, event.widget );
        log.append( "filter" );
      }
    });

    Event event = new Event();
    event.button = 2;
    event.character = 'a';
    event.count = 4;
    event.data = new Object();
    event.detail = 6;
    event.display = null;
    event.doit = false;
    event.end = 8;
    event.height = 10;
    event.index = 12;
    event.item = shell;
    event.keyCode = 14;
    event.start = 16;
    event.stateMask = 18;
    event.text = "foo";
    event.type = SWT.MouseDoubleClick;
    event.widget = shell;
    event.width = 20;
    event.x = 22;
    event.y = 24;

    shell.notifyListeners( SWT.Resize, event );
    assertEquals( "filter", log.toString() );
  }

  public void testNotifyListenersSetData() {
    Fixture.fakePhase( PhaseId.PROCESS_ACTION );
    final Display display = new Display();
    final Widget widget = new Shell( display );
    final StringBuffer log = new StringBuffer();
    widget.addListener( SWT.SetData, new Listener(){
      public void handleEvent( final Event event ) {
        assertSame( widget, event.widget );
        assertSame( widget, event.item );
        assertEquals( 3, event.index );
        assertSame( display, event.display );
        log.append( "setdata" );
      }
    });
    Event event = new Event();
    event.item = widget;
    event.index = 3;
    widget.notifyListeners( SWT.SetData, event );
    assertEquals( "setdata", log.toString() );
  }

  public void testNotifyListenersNullEvent() {
    Fixture.fakePhase( PhaseId.PROCESS_ACTION );
    final Display display = new Display();
    final Control control = new Shell( display );
    final StringBuffer log = new StringBuffer();
    control.addControlListener( new ControlAdapter() {
      public void controlResized( final ControlEvent event ) {
        assertSame( control, event.widget );
        assertSame( display, event.display );
        log.append( "typed" );
      }
    } );
    control.notifyListeners( SWT.Resize, null );
    assertEquals( "typed", log.toString() );
  }

  public void testNotifyListenersInvalidEvent() {
    Fixture.fakePhase( PhaseId.PROCESS_ACTION );
    final Display display = new Display();
    final Widget widget = new Shell( display );
    widget.notifyListeners( 4711, new Event() );
    // no assertion: this test ensures that invalid event types are silently
    // ignored
  }

  public void testGetListeners() {
    final Display display = new Display();
    final Widget widget = new Shell( display );
    Listener[] listeners = widget.getListeners( 0 );
    assertNotNull( listeners );
    assertEquals( 0, listeners.length );
    Listener dummyListener = new Listener() {
      public void handleEvent( final Event event ) {
      }
    };
    Listener dummyListener2 = new Listener() {
      public void handleEvent( final Event event ) {
      }
    };
    widget.addListener( SWT.Resize, dummyListener );
    assertEquals( 0, widget.getListeners( SWT.Move ).length );
    assertEquals( 1, widget.getListeners( SWT.Resize ).length );
    assertSame( dummyListener, widget.getListeners( SWT.Resize )[0] );
    widget.addListener( SWT.Resize, dummyListener2 );
    assertEquals( 2, widget.getListeners( SWT.Resize ).length );
  }

  public void testIsListening() {
    final Display display = new Display();
    final Widget widget = new Shell( display );
    final Listener dummyListener = new Listener() {
      public void handleEvent( final Event event ) {
      }
    };
    assertFalse( widget.isListening( SWT.Resize ) );
    widget.addListener( SWT.Resize, dummyListener );
    assertTrue( widget.isListening( SWT.Resize ) );
    widget.removeListener( SWT.Resize, dummyListener );
    assertFalse( widget.isListening( SWT.Resize ) );
  }

  public void testIsListeningForTypedEvent() {
    Fixture.fakePhase( PhaseId.PROCESS_ACTION );
    Display display = new Display();
    Control control = new Shell( display );
    control.addHelpListener( new HelpListener() {
      public void helpRequested( final HelpEvent event ) {
      }
    } );
    assertTrue( control.isListening( SWT.Help ) );
  }

  public void testReskin() {
    Fixture.fakePhase( PhaseId.PROCESS_ACTION );
    final java.util.List log = new ArrayList();
    Display display = new Display();
    Listener listener = new Listener() {
      public void handleEvent( final Event event ) {
        if( event.type == SWT.Skin ) {
          log.add( event.widget );
        }
      }
    };
    display.addListener( SWT.Skin, listener );
    Shell shell = new Shell( display );
    Composite child1 = new Composite( shell, SWT.NONE );
    Label subchild1 = new Label( child1, SWT.NONE );
    Composite child2 = new Composite( shell, SWT.NONE );
    Label subchild2 = new Label( child2, SWT.NONE );
    Composite child3 = new Composite( shell, SWT.NONE );
    Label subchild3 = new Label( child3, SWT.NONE );
    shell.reskin( SWT.ALL );
    display.readAndDispatch();
    assertEquals( 7, log.size() );
    assertSame( shell, log.get( 0 ) );
    assertSame( child1, log.get( 1 ) );
    assertSame( subchild1, log.get( 2 ) );
    assertSame( child2, log.get( 3 ) );
    assertSame( subchild2, log.get( 4 ) );
    assertSame( child3, log.get( 5 ) );
    assertSame( subchild3, log.get( 6 ) );
    log.clear();
    shell.setData( SWT.SKIN_CLASS, "skin" );
    display.readAndDispatch();
    assertEquals( 7, log.size() );
    assertSame( shell, log.get( 0 ) );
    assertSame( child1, log.get( 1 ) );
    assertSame( subchild1, log.get( 2 ) );
    assertSame( child2, log.get( 3 ) );
    assertSame( subchild2, log.get( 4 ) );
    assertSame( child3, log.get( 5 ) );
    assertSame( subchild3, log.get( 6 ) );
    log.clear();
    shell.setData( SWT.SKIN_ID, "skin" );
    display.readAndDispatch();
    assertEquals( 7, log.size() );
    assertSame( shell, log.get( 0 ) );
    assertSame( child1, log.get( 1 ) );
    assertSame( subchild1, log.get( 2 ) );
    assertSame( child2, log.get( 3 ) );
    assertSame( subchild2, log.get( 4 ) );
    assertSame( child3, log.get( 5 ) );
    assertSame( subchild3, log.get( 6 ) );
    log.clear();
    child3.reskin( SWT.ALL );
    display.readAndDispatch();
    assertEquals( 2, log.size() );
    assertSame( child3, log.get( 0 ) );
    assertSame( subchild3, log.get( 1 ) );
    log.clear();
    child2.reskin( SWT.NONE );
    display.readAndDispatch();
    assertEquals( 1, log.size() );
    assertSame( child2, log.get( 0 ) );
    log.clear();
    display.removeListener( SWT.Skin, listener );
    shell.reskin( SWT.ALL );
    display.readAndDispatch();
    assertEquals( 0, log.size() );
  }
}
