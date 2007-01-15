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

package org.eclipse.rap.ui.internal;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.graphics.Rectangle;
import org.eclipse.rap.rwt.widgets.*;
import org.eclipse.rap.ui.ISizeProvider;
import org.eclipse.rap.ui.IWorkbenchWindow;


public abstract class LayoutPart implements ISizeProvider {
  
  protected ILayoutContainer container;
  protected String id;

  
  public LayoutPart( final String id ) {
    this.id = id;
  }
  
  public void setID( final String id ) {
    this.id = id;
  }
  
  public String getID() {
    return id;
  }
  
  public boolean isCompressible() {
    return false;
  }
  
  public void setBounds( final Rectangle bounds ) {
    Control ctrl = getControl();
    if( !ctrl.isDisposed() ) {
//    if( !SwtUtil.isDisposed( ctrl ) ) {
      ctrl.setBounds( bounds );
    }
  }

  public void setVisible( final boolean visible ) {
    // TODO: [fappel] replace with original implementation
    Control ctrl = getControl();
    if( !ctrl.isDisposed() ) {
      ctrl.setVisible( visible );
    }
  }

  public boolean getVisible() {
    // TODO: [fappel] replace with original implementation
    Control ctrl = getControl();
    boolean result = false;
    if( !ctrl.isDisposed() ) {
      result = ctrl.isVisible();
    }
    return result;
  }

  public void setContainer( final ILayoutContainer container ) {
    this.container = container;
//    if( container != null ) {
//      setZoomed( container.childIsZoomed( this ) );
//    }
  }
  
  public ILayoutContainer getContainer() {
    return container;
  }
  
  public IWorkbenchWindow getWorkbenchWindow() {
    Shell shell = getShell();
    IWorkbenchWindow result = null;
    if( shell != null ) {
      Object data = shell.getData();
      if( data instanceof IWorkbenchWindow ) {
        result = ( IWorkbenchWindow )data;
//    } else if( data instanceof DetachedWindow ) {
//      result
//        = ( ( DetachedWindow )data ).getWorkbenchPage().getWorkbenchWindow();
      }
    }
    return result;
  }

  public Shell getShell() {
    Control ctrl = getControl();
    Shell result = null;
    if( !ctrl.isDisposed() ) {
//    if( !SwtUtil.isDisposed( ctrl ) ) {
      result = ctrl.getShell();
    }
    return result;
  }

  public void flushLayout() {
    ILayoutContainer container = getContainer();
    if( getContainer() != null ) {
      container.resizeChild( this );
    }
  }

  abstract public void createControl(Composite parent);
  abstract public Control getControl();

  
  //////////////////////////
  // Interface ISizeProvider
  
  public int getSizeFlags( final boolean horizontal ) {
    return RWT.MIN;
  }

  public int computePreferredSize( final boolean width,
                                   final int availableParallel,
                                   final int availablePerpendicular,
                                   final int preferredParallel )
  {
    return preferredParallel;
  }
}
