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

import junit.framework.TestCase;

import org.eclipse.rwt.Fixture;
import org.eclipse.rwt.lifecycle.PhaseId;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.internal.widgets.IDisplayAdapter;
import org.eclipse.swt.internal.widgets.IShellAdapter;
import org.eclipse.swt.layout.FillLayout;


public class Shell_Test extends TestCase {

  public void testMenuBar() {
    Display display = new Display();
    Shell shell1 = new Shell( display, SWT.NONE );
    Shell shell2 = new Shell( display, SWT.NONE );
    Menu menu = new Menu( shell1, SWT.BAR );
    shell1.setMenuBar( menu );
    // Ensure that getMenuBar returns the very same shell that was set
    assertSame( menu, shell1.getMenuBar() );
    // Allow to 'reset' the menuBar
    shell1.setMenuBar( null );
    assertEquals( null, shell1.getMenuBar() );
    // Ensure that shell does not return a disposed of menuBar
    shell1.setMenuBar( menu );
    menu.dispose();
    assertNull( shell1.getMenuBar() );
    // Ensure that the shell does not 'react' when disposing of a formerly
    // owned menuBar
    Menu shortTimeMenu = new Menu( shell1, SWT.BAR );
    shell1.setMenuBar( shortTimeMenu );
    Menu replacementMenu = new Menu( shell1, SWT.BAR );
    shell1.setMenuBar( replacementMenu );
    shortTimeMenu.dispose();
    assertSame( replacementMenu, shell1.getMenuBar() );
    // Shell must initially have no menu bar
    assertEquals( null, shell2.getMenuBar() );
    // setMenuBar allows only a menu that whose parent is *this* shell
    try {
      Menu shell1Menu = new Menu( shell1, SWT.BAR );
      shell2.setMenuBar( shell1Menu );
      fail( "Must not allow to set menu from different shell" );
    } catch( IllegalArgumentException e ) {
      // expected
    }
    // Ensure that setMenuBar does not accept disposed of Menus
    try {
      Menu disposedMenu = new Menu( shell2, SWT.BAR );
      disposedMenu.dispose();
      shell2.setMenuBar( disposedMenu );
      fail( "Must not allow to set disposed of menu." );
    } catch( IllegalArgumentException e ) {
      // expected
    }
    // Ensure that setMenuBar does not accept menus other than those constructed
    // with SWT.BAR
    try {
      Shell shell3 = new Shell( display, SWT.NONE );
      Menu popupMenu = new Menu( shell3, SWT.POP_UP );
      shell3.setMenuBar( popupMenu );
      fail( "Must only accept menus with style SWT.BAR" );
    } catch( IllegalArgumentException e ) {
      // expected
    }
  }

  public void testClientArea() {
    Display display = new Display();
    Shell shell = new Shell( display, SWT.NONE );
    Rectangle clientAreaWithoutMenuBar = shell.getClientArea();
    Menu menuBar = new Menu( shell, SWT.BAR );
    shell.setMenuBar( menuBar );
    Rectangle clientAreaWithMenuBar = shell.getClientArea();
    assertTrue( clientAreaWithoutMenuBar.y < clientAreaWithMenuBar.y );
  }

  public void testConstructor() throws Exception {
    Shell shell;
    Display display = new Display();
    shell = new Shell();
    assertSame( display, shell.getDisplay() );
    shell = new Shell( ( Display )null, SWT.NONE );
    assertSame( display, shell.getDisplay() );
    shell = new Shell( ( Display )null );
    assertEquals( SWT.SHELL_TRIM | SWT.LEFT_TO_RIGHT, shell.getStyle() );
    shell = new Shell( display, SWT.NO_TRIM | SWT.CLOSE );
    assertTrue( ( shell.getStyle() & SWT.CLOSE ) == 0 );
    shell = new Shell( ( Shell )null );
    assertEquals( SWT.DIALOG_TRIM | SWT.LEFT_TO_RIGHT, shell.getStyle() );
    shell = new Shell( display, SWT.MIN );
    assertTrue( ( shell.getStyle() & SWT.CLOSE ) != 0 );
    try {
      Shell disposedShell = new Shell( display, SWT.NONE );
      disposedShell.dispose();
      shell = new Shell( disposedShell );
      fail( "The constructor mut not accept a disposed shell" );
    } catch( IllegalArgumentException e ) {
      // expected
    }
    final Shell[] backgroundShell = { null };
    final boolean[] failed = { false };
    Thread thread = new Thread( new Runnable() {
      public void run() {
        try {
          backgroundShell[ 0 ] = new Shell();
        } catch( Exception e ) {
          failed[ 0 ] = true;
        }
      }
    } );
    thread.setDaemon( true );
    thread.start();
    thread.join();
    assertNull( backgroundShell[ 0 ] );
    assertTrue( failed[ 0 ] );
  }

  public void testInitialValues() {
    Display display = new Display();
    Shell shell = new Shell( display, SWT.NONE );
    // Must return the display it was created with
    assertSame( display, shell.getDisplay() );
    // Active control must be null
    Object adapter = shell.getAdapter( IShellAdapter.class );
    IShellAdapter shellAdapter = ( IShellAdapter )adapter;
    assertEquals( null, shellAdapter.getActiveControl() );
    // Shell initially has no layout
    assertEquals( null, shell.getLayout() );
    // Text must be an empty string
    assertEquals( "", shell.getText() );
    // Enabled
    assertEquals( true, shell.getEnabled() );
    // Shell is visible after open(), but not directly after creation
    assertEquals( false, shell.getVisible() );
    assertEquals( false, shell.isVisible() );
    // The Shell(Display) constructor must use style SHELL_TRIM
    Shell trimShell = new Shell( display );
    assertEquals( SWT.SHELL_TRIM | SWT.LEFT_TO_RIGHT, trimShell.getStyle() );
  }

  public void testInitialSize() {
    Display display = new Display();
    Shell shell = new Shell( display, SWT.NONE );
    Point empty = new Point( 0, 0 );
    assertFalse( empty.equals( shell.getSize() ) );
  }

  public void testAlpha() {
    Display display = new Display();
    Shell shell = new Shell( display, SWT.NONE );
    assertEquals( 255, shell.getAlpha() );
    shell.setAlpha( 23 );
    assertEquals( 23, shell.getAlpha() );
    shell.setAlpha( 0 );
    assertEquals( 0, shell.getAlpha() );
  }

  public void testOpen() {
    Display display = new Display();
    Shell shell = new Shell( display, SWT.NONE );
    shell.open();
    assertEquals( true, shell.getVisible() );
    assertEquals( true, shell.isVisible() );
  }

  public void testLayoutOnSetVisible() {
    // ensure that layout is trigered while opening a shell, more specifically
    // during setVisible( true )
    Fixture.fakePhase( PhaseId.PROCESS_ACTION );
    final StringBuffer log = new StringBuffer();
    Display display = new Display();
    Shell shell = new Shell( display, SWT.NONE );
    shell.setLayout( new Layout() {
      protected Point computeSize( final Composite composite,
                                   final int hint,
                                   final int hint2,
                                   final boolean flushCache )
      {
        return null;
      }
      protected void layout( final Composite composite, final boolean flushCache ) {
        log.append( "layout" );
      }
    } );
    shell.setVisible( true );
    assertEquals( "layout", log.toString() );
    // don't re-layout when shell is laready visible
    log.setLength( 0 );
    shell.setVisible( true );
    assertEquals( "", log.toString() );
    // make sure, layout is not triggered when shell gets hidden
    log.setLength( 0 );
    shell.setVisible( false );
    assertEquals( "", log.toString() );
  }

  public void testCloseChildShells() {
    Display display = new Display();
    Shell shell = new Shell( display, SWT.NONE );
    shell.open();
    Shell childShell = new Shell( shell );
    shell.close();
    assertTrue( childShell.isDisposed() );
  }

  public void testDisposeChildShell() {
    Display display = new Display();
    Shell shell = new Shell( display, SWT.NONE );
    shell.open();
    Shell childShell = new Shell( shell );
    childShell.dispose();
    assertTrue( childShell.isDisposed() );
    childShell = new Shell( shell );
    shell.dispose();
    assertTrue( childShell.isDisposed() );
  }

  public void testDisposeMenu() {
    Display display = new Display();
    Shell shell = new Shell( display , SWT.NONE );
    Menu menu = new Menu( shell, SWT.BAR );
    shell.dispose();
    assertTrue( menu.isDisposed() );
  }

  public void testCreateDescendantShell() {
    Display display = new Display();
    Shell shell = new Shell( display, SWT.NONE );
    Shell descendantShell = new Shell( shell );
    assertEquals( 0, shell.getChildren().length );
    assertSame( shell, descendantShell.getParent() );
  }

  public void testFocusAfterReEnable() {
    Display display = new Display();
    Shell shell = new Shell( display );
    Control focusedWhileDisabled = new Button( shell, SWT.PUSH );
    Control focusedControl = new Button( shell, SWT.PUSH );
    shell.open();
    focusedWhileDisabled.forceFocus();
    shell.setEnabled( false );
    focusedControl.forceFocus();
    shell.setEnabled( true );
    assertEquals( focusedControl, display.getFocusControl() );
  }

  public void testSavedFocus() {
    Display display = new Display();
    Shell shell = new Shell( display );
    Control control = new Button( shell, SWT.PUSH );
    shell.open();
    control.setFocus();
    assertSame( shell.getSavedFocus(), control );  // ensure precondition
    control.dispose();
    assertNotSame( control, shell.getSavedFocus() );
    assertNull( shell.getSavedFocus() );
  }

  public void testInvalidDefaultButton() {
    Display display = new Display();
    Shell shell = new Shell( display );
    Shell anotherShell = new Shell( display );
    Button anotherButton = new Button( anotherShell, SWT.PUSH );

    // Setting button that belongs to different shell causes exception
    try {
      shell.setDefaultButton( anotherButton );
      fail( "Not allowed to set default button that belongs to another shell" );
    } catch( IllegalArgumentException e ) {
      // expected
    }

    // Setting button that is disposed causes exception
    try {
      Button disposedButton = new Button( shell, SWT.PUSH );
      disposedButton.dispose();
      shell.setDefaultButton( disposedButton );
      fail( "Not allowed to set default button that is disposed" );
    } catch( IllegalArgumentException e ) {
      // expected
    }

    // Set a default button for the following tests
    Button defaultButton = new Button( shell, SWT.PUSH );
    shell.setDefaultButton( defaultButton );
    assertSame( defaultButton, shell.getDefaultButton() );

    // Try to set radio-button as default is ignored
    Button radio = new Button( shell, SWT.RADIO );
    shell.setDefaultButton( radio );
    assertSame( defaultButton, shell.getDefaultButton() );

    // Try to set check-box as default is ignored
    Button check = new Button( shell, SWT.RADIO );
    shell.setDefaultButton( check );
    assertSame( defaultButton, shell.getDefaultButton() );
  }

  public void testSaveDefaultButton() {
    Display display = new Display();
    Shell shell = new Shell( display );
    Button button1 = new Button( shell, SWT.PUSH );
    Button button2 = new Button( shell, SWT.PUSH );

    shell.setDefaultButton( button1 );
    assertSame( button1, shell.getDefaultButton() );
    shell.setDefaultButton( null );
    assertSame( button1, shell.getDefaultButton() );

    shell.setDefaultButton( button1 );
    shell.setDefaultButton( button2 );
    assertSame( button2, shell.getDefaultButton() );
    shell.setDefaultButton( null );
    assertSame( button2, shell.getDefaultButton() );

    button2.dispose();
    shell.setDefaultButton( null );
    assertEquals( null, shell.getDefaultButton() );
  }

  public void testDefaultButtonDisposed() {
    Display display = new Display();
    Shell shell = new Shell( display );
    Button defaultButton = new Button( shell, SWT.PUSH );
    shell.setDefaultButton( defaultButton );
    defaultButton.dispose();
    assertNull( shell.getDefaultButton() );
  }

  public void testForceActive() {
    Display display = new Display();
    Shell shell = new Shell( display );
    Shell secondShell = new Shell( display );
    shell.open();
    secondShell.open();
    assertSame( secondShell, display.getActiveShell() );
    shell.forceActive();
    assertSame( shell, display.getActiveShell() );
  }

  public void testActivateInvisible() {
    Display display = new Display();
    Shell shell = new Shell( display );
    shell.setSize( 50, 50 );
    shell.setVisible( false );
    shell.setActive();
    assertNull( display.getActiveShell() );
  }

  public void testSetActive() {
    final java.util.List log = new ArrayList();
    Display display = new Display();
    Shell shell = new Shell( display );
    shell.open();
    assertSame( shell, display.getActiveShell() );
    shell.addShellListener( new ShellAdapter() {
      public void shellActivated( final ShellEvent event ) {
        log .add( event );
      }
      public void shellDeactivated( final ShellEvent event ) {
        log .add( event );
      }
    } );
    shell.setActive();
    shell.setActive();
    assertEquals( 0, log.size() );
  }

  public void testActiveShellOnFocusControl() {
    Fixture.fakePhase( PhaseId.PROCESS_ACTION );
    final java.util.List log = new ArrayList();
    Display display = new Display();
    Shell shell = new Shell( display );
    Shell secondShell = new Shell( display );
    shell.open();
    secondShell.open();
    assertSame( secondShell, display.getActiveShell() );
    shell.addShellListener( new ShellAdapter() {
      public void shellActivated( final ShellEvent event ) {
        log.add( "shellActivated" );
      }
    } );
    Button button = new Button( shell, SWT.PUSH );
    button.addFocusListener( new FocusAdapter() {
      public void focusGained( final FocusEvent event ) {
        log.add( "buttonFocusGained" );
      }
    } );
    button.addListener( SWT.Activate, new Listener() {
      public void handleEvent( final Event event ) {
        log.add( "buttonActivated" );
      }
    } );
    button.setFocus();
    assertSame( shell, display.getActiveShell() );
    assertEquals( 3, log.size() );
    assertEquals( "shellActivated", ( String )log.get( 0 ) );
    assertEquals( "buttonFocusGained", ( String )log.get( 1 ) );
    assertEquals( "buttonActivated", ( String )log.get( 2 ) );
  }

  /* test case to simulate the scenario reported in this bug:
   * 278996: [Shell] Stackoverflow when closing child shell
   * https://bugs.eclipse.org/bugs/show_bug.cgi?id=278996
   */
  public void testCloseOnDeactivateWithSingleShell() {
    Fixture.fakePhase( PhaseId.PROCESS_ACTION );
    Display display = new Display();
    final Shell shell = new Shell( display );
    shell.addShellListener( new ShellAdapter() {
      public void shellDeactivated( final ShellEvent event ) {
        shell.close();
      }
    } );
    shell.open();
    shell.setActive();
    // no assert: test case is to ensure that no stack overflow occurs
  }

  /*
   * Bug 282506: [Shell] StackOverflow when calling Shell#close in Deactivate
   *             listener
   */
  public void testCloseOnDeactivateWithMultipleShells() {
    Fixture.fakePhase( PhaseId.PROCESS_ACTION );
    Display display = new Display();
    Shell shell = new Shell( display );
    shell.open();
    final Shell dialog = new Shell( shell );
    dialog.setLayout( new FillLayout() );
    dialog.addShellListener( new ShellAdapter() {
      public void shellDeactivated( final ShellEvent event ) {
        dialog.close();
      }
    } );
    dialog.open();
    dialog.close();
    // no assert: test case is to ensure that no stack overflow occurs
  }

  public void testNoDeactivateEventOnDispose() {
    Fixture.fakePhase( PhaseId.PROCESS_ACTION );
    final StringBuffer log = new StringBuffer();
    Display display = new Display();
    Shell shell = new Shell( display );
    shell.addShellListener( new ShellAdapter() {
      public void shellActivated( final ShellEvent event ) {
        log.append( "shell activated" );
      }
      public void shellDeactivated( final ShellEvent event ) {
        log.append( "shell deactivated" );
      }
    } );
    shell.open();
    shell.dispose();
    assertEquals( "shell activated", log.toString() );
  }

  public void testMaximized() {
    Display display = new Display();
    Shell shell = new Shell( display );
    shell.setBounds( 1, 2, 3, 4 );
    shell.setMaximized( true );
    assertTrue( shell.getMaximized() );
    assertEquals( shell.getBounds(), display.getBounds() );
  }

  public void testSetBoundsResetMaximized() {
    Display display = new Display();
    Shell shell = new Shell( display );
    shell.setBounds( 1, 2, 3, 4 );
    shell.setMaximized( true );
    Rectangle bounds = new Rectangle( 10, 10, 100, 100 );
    shell.setBounds( bounds );
    assertFalse( shell.getMaximized() );
    assertEquals( bounds, shell.getBounds() );
  }

  public void testSetLocationResetMaximized() {
    Display display = new Display();
    Shell shell = new Shell( display );
    shell.setBounds( 1, 2, 3, 4 );
    shell.setMaximized( true );
    shell.setLocation( 10, 10 );
    assertFalse( shell.getMaximized() );
  }

  public void testSetSizeResetMaximized() {
    Display display = new Display();
    Shell shell = new Shell( display );
    shell.setBounds( 1, 2, 3, 4 );
    shell.setMaximized( true );
    shell.setSize( 6, 6 );
    assertFalse( shell.getMaximized() );
  }

  public void testSetBoundsResetMaximizedEventOrder() {
    final boolean[] maximized = {
      true
    };
    Display display = new Display();
    final Shell shell = new Shell( display );
    shell.setBounds( 1, 2, 3, 4 );
    shell.addControlListener( new ControlAdapter() {
      public void controlResized( final ControlEvent event ) {
        maximized[ 0 ] = shell.getMaximized();
      }
    } );
    shell.setMaximized( true );
    shell.setSize( 6, 6 );
    assertFalse( maximized[ 0 ] );
  }

  public void testShellAdapterSetBounds() {
    final java.util.List log = new ArrayList();
    Display display = new Display();
    Shell shell = new Shell( display );
    shell.setBounds( 1, 2, 3, 4 );
    shell.setMaximized( true );
    shell.addControlListener( new ControlAdapter() {
      public void controlResized( final ControlEvent event ) {
        log.add( event );
      }
    } );
    Object adapter = shell.getAdapter( IShellAdapter.class );
    IShellAdapter shellAdapter = ( IShellAdapter )adapter;
    shellAdapter.setBounds( new Rectangle( 5, 6, 7, 8 ) );
    assertEquals( new Rectangle( 5, 6, 7, 8 ), shell.getBounds() );
    assertTrue( shell.getMaximized() );
    assertEquals( 1, log.size() );
  }

  public void testSetBoundsResetMinimized() {
    Display display = new Display();
    Shell shell = new Shell( display );
    shell.setBounds( 1, 2, 3, 4 );
    shell.setMinimized( true );
    Rectangle bounds = new Rectangle( 10, 10, 100, 100 );
    shell.setBounds( bounds );
    assertFalse( shell.getMinimized() );
    assertEquals( bounds, shell.getBounds() );
  }

  public void testModified() {
    Display display = new Display();
    Shell shell = new Shell( display );
    assertFalse( shell.getModified() );
    shell.setModified( true );
    assertTrue( shell.getModified() );
    shell.setModified( false );
    assertFalse( shell.getModified() );
  }

  public void testMinimumSize() {
    final java.util.List log = new ArrayList();
    Display display = new Display();
    Shell shell = new Shell( display );
    shell.addControlListener( new ControlAdapter() {
      public void controlResized( final ControlEvent event ) {
        log.add( event );
      }
    } );
    assertEquals( new Point( 80, 23 ), shell.getMinimumSize() );
    shell.setSize( 10, 10 );
    assertEquals( new Point( 80, 23 ), shell.getSize() );
    shell.setSize( 100, 100 );
    log.clear();
    assertEquals( new Point( 100, 100 ), shell.getSize() );
    shell.setMinimumSize( 150, 150 );
    assertEquals( 1, log.size() );
    assertEquals( new Point( 150, 150 ), shell.getMinimumSize() );
    assertEquals( new Point( 150, 150 ), shell.getSize() );
    shell.setMinimumSize( 10, 10 );
    assertEquals( new Point( 80, 23 ), shell.getMinimumSize() );
    shell.setMinimumSize( new Point( 150, 150 ) );
    assertEquals( new Point( 150, 150 ), shell.getMinimumSize() );
    shell.setBounds( 10, 10, 100, 100 );
    assertEquals( new Point( 150, 150 ), shell.getSize() );
    assertEquals( new Rectangle( 10, 10, 150, 150 ), shell.getBounds() );
    try {
      shell.setMinimumSize( null );
      fail( "Must not allow null value" );
    } catch( IllegalArgumentException e ) {
      // expected
    }
  }

  public void testFullScreen() {
    Fixture.fakePhase( PhaseId.PROCESS_ACTION );
    final java.util.List log = new ArrayList();
    Display display = new Display();
    Rectangle displayBounds = new Rectangle( 0, 0, 800, 600 );
    getDisplayAdapter( display ).setBounds( displayBounds );
    Shell shell = new Shell( display );
    Rectangle shellBounds = new Rectangle( 10, 10, 100, 100 );
    shell.setBounds( shellBounds );
    shell.addControlListener( new ControlListener() {
      public void controlMoved( final ControlEvent event ) {
        log.add( "controlMoved" );
      }
      public void controlResized( final ControlEvent event ) {
        log.add( "controlResized" );
      }
    } );
    shell.open();
    assertFalse( shell.getFullScreen() );
    assertFalse( shell.getMaximized() );
    assertFalse( shell.getMinimized() );

    log.clear();
    shell.setMaximized( true );
    assertFalse( shell.getFullScreen() );
    assertTrue( shell.getMaximized() );
    assertFalse( shell.getMinimized() );
    assertEquals( 2, log.size() );
    assertEquals( "controlMoved", log.get( 0 ) );
    assertEquals( "controlResized", log.get( 1 ) );
    assertEquals( displayBounds, shell.getBounds() );

    shell.setMinimized( true );
    assertFalse( shell.getFullScreen() );
    assertFalse( shell.getMaximized() );
    assertTrue( shell.getMinimized() );

    shell.setMinimized( false );
    assertFalse( shell.getFullScreen() );
    assertTrue( shell.getMaximized() );
    assertFalse( shell.getMinimized() );

    log.clear();
    shell.setFullScreen( true );
    assertTrue( shell.getFullScreen() );
    assertFalse( shell.getMaximized() );
    assertFalse( shell.getMinimized() );
    assertEquals( 0, log.size() );
    assertEquals( displayBounds, shell.getBounds() );

    log.clear();
    shell.setMaximized( true );
    assertTrue( shell.getFullScreen() );
    assertFalse( shell.getMaximized() );
    assertFalse( shell.getMinimized() );
    assertEquals( 0, log.size() );
    assertEquals( displayBounds, shell.getBounds() );

    log.clear();
    shell.setMaximized( false );
    assertTrue( shell.getFullScreen() );
    assertFalse( shell.getMaximized() );
    assertFalse( shell.getMinimized() );
    assertEquals( 0, log.size() );
    assertEquals( displayBounds, shell.getBounds() );

    log.clear();
    shell.setMinimized( true );
    assertTrue( shell.getFullScreen() );
    assertFalse( shell.getMaximized() );
    assertTrue( shell.getMinimized() );
    assertEquals( 0, log.size() );

    log.clear();
    shell.setMinimized( false );
    assertTrue( shell.getFullScreen() );
    assertFalse( shell.getMaximized() );
    assertFalse( shell.getMinimized() );
    assertEquals( 0, log.size() );

    log.clear();
    shell.setFullScreen( false );
    assertFalse( shell.getFullScreen() );
    assertTrue( shell.getMaximized() );
    assertFalse( shell.getMinimized() );
    assertEquals( 0, log.size() );
    assertEquals( displayBounds, shell.getBounds() );

    shell.setMaximized( false );
    shell.setMinimized( true );
    log.clear();
    shell.setFullScreen( true );
    assertTrue( shell.getFullScreen() );
    assertFalse( shell.getMaximized() );
    assertFalse( shell.getMinimized() );
    assertEquals( 2, log.size() );
    assertEquals( "controlMoved", log.get( 0 ) );
    assertEquals( "controlResized", log.get( 1 ) );
    assertEquals( displayBounds, shell.getBounds() );

    log.clear();
    shell.setFullScreen( false );
    assertFalse( shell.getFullScreen() );
    assertFalse( shell.getMaximized() );
    assertFalse( shell.getMinimized() );
    assertEquals( 2, log.size() );
    assertEquals( "controlMoved", log.get( 0 ) );
    assertEquals( "controlResized", log.get( 1 ) );
    assertEquals( shellBounds, shell.getBounds() );

    shell.setFullScreen( true );
    log.clear();
    shell.setBounds( 20, 20, 200, 200 );
    assertFalse( shell.getFullScreen() );
    assertFalse( shell.getMaximized() );
    assertFalse( shell.getMinimized() );
    assertEquals( 2, log.size() );
    assertEquals( "controlMoved", log.get( 0 ) );
    assertEquals( "controlResized", log.get( 1 ) );
  }

  public void testActiveShellOnFullScreen() {
    Display display = new Display();
    Shell shell1 = new Shell( display );
    shell1.setBounds( 20, 20, 200, 200 );
    shell1.open();
    Shell shell2 = new Shell( display );
    shell2.setBounds( 20, 20, 200, 200 );
    shell2.open();
    assertEquals( shell2, display.getActiveShell() );
    shell1.setFullScreen( true );
    assertEquals( shell1, display.getActiveShell() );
  }

  private static IDisplayAdapter getDisplayAdapter( final Display display ) {
    Object adapter = display.getAdapter( IDisplayAdapter.class );
    return ( IDisplayAdapter )adapter;
  }

  protected void setUp() throws Exception {
    Fixture.setUp();
    Fixture.fakePhase( PhaseId.PROCESS_ACTION );
  }

  protected void tearDown() throws Exception {
    Fixture.tearDown();
  }
}
