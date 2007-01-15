/*******************************************************************************
 * Copyright (c) 2002-2006 Innoopract Informationssysteme GmbH. All rights
 * reserved. This program and the accompanying materials are made available
 * under the terms of the Eclipse Public License v1.0 which accompanies this
 * distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 * Contributors: Innoopract Informationssysteme GmbH - initial API and
 * implementation
 ******************************************************************************/
package org.eclipse.rap.jface.window;

import org.eclipse.rap.jface.util.Assert;
import org.eclipse.rap.jface.util.Geometry;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.graphics.Point;
import org.eclipse.rap.rwt.graphics.Rectangle;
import org.eclipse.rap.rwt.layout.GridLayout;
import org.eclipse.rap.rwt.widgets.*;


// TODO: [fappel] completion of this basic Window implementation...
public abstract class Window implements IShellProvider {

  private IShellProvider parentShell;
  private WindowManager windowManager;
  private Shell shell;
  private Control contents;

  protected Window( final Shell parentShell ) {
    this( new SameShellProvider( parentShell ) );
  }

  protected Window( final IShellProvider shellProvider ) {
    Assert.isNotNull( shellProvider );
    this.parentShell = shellProvider;
  }

  public void open() {
    if( shell == null || shell.isDisposed() ) {
      shell = null;
      // create the window
      create();
    }
  }

  public void create() {
    shell = createShell();
    contents = createContents( shell );
    initializeBounds();
  }

  protected final Shell createShell() {
    Shell newParent = getParentShell();
    if( newParent != null && newParent.isDisposed() ) {
      parentShell = new SameShellProvider( null );
      newParent = getParentShell();
    }
    // Create the shell
    Shell result = new Shell( newParent, RWT.NONE );
    result.setData( this );
    configureShell( result );
    return result;
  }

  protected void configureShell( final Shell newShell ) {
    Layout layout = getLayout();
    if( layout != null ) {
      newShell.setLayout( layout );
    }
  }

  protected Layout getLayout() {
    GridLayout layout = new GridLayout();
    layout.marginHeight = 0;
    layout.marginWidth = 0;
    return layout;
  }

  protected void initializeBounds() {
    // TODO: [fappel] simple fake to get the implementation up and running
    shell.setBounds( 60, 30, 800, 600 );
  }

  protected Point getInitialLocation( final Point initialSize ) {
    Composite parent = shell.getParent();
// TODO: [fappel] since we don't have a monitor abstraction we use
//                currently the faked bounds.
//    Monitor monitor = shell.getDisplay().getPrimaryMonitor();
//    if( parent != null ) {
//      monitor = parent.getMonitor();
//    }
//    Rectangle monitorBounds = monitor.getClientArea();
    Rectangle monitorBounds = new Rectangle( 0, 0, 1024, 768 );
    Point centerPoint;
    if( parent != null ) {
      centerPoint = Geometry.centerPoint( parent.getBounds() );
    } else {
      centerPoint = Geometry.centerPoint( monitorBounds );
    }
    return new Point( centerPoint.x - ( initialSize.x / 2 ),
                      Math.max( monitorBounds.y,
                                Math.min( centerPoint.y
                                              - ( initialSize.y * 2 / 3 ),
                                          monitorBounds.y
                                              + monitorBounds.height
                                              - initialSize.y ) ) );
  }

  protected Rectangle getConstrainedShellBounds( Rectangle preferredSize ) {
    Rectangle result = new Rectangle( preferredSize.x,
                                      preferredSize.y,
                                      preferredSize.width,
                                      preferredSize.height );
// TODO: [fappel] since we don't have a monitor abstraction we use
//                currently faked bounds.
//    Monitor mon = getClosestMonitor( getShell().getDisplay(),
//                                     Geometry.centerPoint( result ) );
//    Rectangle bounds = mon.getClientArea();
    Rectangle bounds = new Rectangle( 0, 0, 1024, 768 );
    if( result.height > bounds.height ) {
      result.height = bounds.height;
    }
    if( result.width > bounds.width ) {
      result.width = bounds.width;
    }
    result.x = Math.max( bounds.x, Math.min( result.x, bounds.x
                                                       + bounds.width
                                                       - result.width ) );
    result.y = Math.max( bounds.y, Math.min( result.y, bounds.y
                                                       + bounds.height
                                                       - result.height ) );
    return result;
  }


  protected Control createContents( final Composite parent ) {
    return new Composite( parent, RWT.NONE );
  }

  protected Shell getParentShell() {
    return parentShell.getShell();
  }

  public boolean close() {
    if( windowManager != null ) {
      windowManager.remove( this );
      windowManager = null;
    }
    boolean result = shell == null || shell.isDisposed();
    if( !result ) {
      shell.dispose();
      shell = null;
      contents = null;
      result = true;
    }
    return result;
  }

  protected Control getContents() {
    return contents;
  }

  public Shell getShell() {
    return shell;
  }

  public WindowManager getWindowManager() {
    return windowManager;
  }

  public void setWindowManager( WindowManager manager ) {
    windowManager = manager;
    if( manager != null ) {
      Window[] windows = manager.getWindows();
      boolean found = false;
      for( int i = 0; !found && i < windows.length; i++ ) {
        found = windows[ i ] == this;
      }
      if( !found ) {
        manager.add( this );
      }
    }
  }
}
