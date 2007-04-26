/*******************************************************************************
 * Copyright (c) 2002-2006 Innoopract Informationssysteme GmbH. All rights
 * reserved. This program and the accompanying materials are made available
 * under the terms of the Eclipse Public License v1.0 which accompanies this
 * distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 * Contributors: Innoopract Informationssysteme GmbH - initial API and
 * implementation
 ******************************************************************************/
package org.eclipse.ui.internal;

import org.eclipse.jface.action.*;
import org.eclipse.jface.internal.provisional.action.IToolBarContributionItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.entrypoint.IActionBarConfigurer;
import org.eclipse.ui.entrypoint.IWorkbenchWindowConfigurer;
import org.eclipse.ui.internal.provisional.entrypoint.IActionBarConfigurer2;
import org.eclipse.ui.presentations.AbstractPresentationFactory;
import org.eclipse.ui.presentations.WorkbenchPresentationFactory;

public class WorkbenchWindowConfigurer implements IWorkbenchWindowConfigurer {

  private final WorkbenchWindow window;
  private boolean showMenuBar = true;
  private boolean showToolBar = true;
  private boolean showPerspectiveBar = false;
  private AbstractPresentationFactory presentationFactory;
  private Point initialSize;
  private WindowActionBarConfigurer actionBarConfigurer = null;
  private String windowTitle;
  private int shellStyle = SWT.SHELL_TRIM;
  
  class WindowActionBarConfigurer implements IActionBarConfigurer2 {

    private IActionBarConfigurer2 proxy;

    /**
     * Sets the proxy to use, or <code>null</code> for none.
     * 
     * @param proxy the proxy
     */
    public void setProxy( IActionBarConfigurer2 proxy ) {
      this.proxy = proxy;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.application.IActionBarConfigurer#getWindowConfigurer()
     */
    public IWorkbenchWindowConfigurer getWindowConfigurer() {
      return window.getWindowConfigurer();
    }

    /**
     * Returns whether the given id is for a cool item.
     * 
     * @param the item id
     * @return <code>true</code> if it is a cool item, and <code>false</code>
     *         otherwise
     */
    /* package */boolean containsCoolItem( String id ) {
      ICoolBarManager cbManager = getCoolBarManager();
      if( cbManager == null ) {
        return false;
      }
      IContributionItem cbItem = cbManager.find( id );
      if( cbItem == null ) {
        return false;
      }
      // @ issue: maybe we should check if cbItem is visible?
      return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.application.IActionBarConfigurer
     */
//    public IStatusLineManager getStatusLineManager() {
//      if( proxy != null ) {
//        return proxy.getStatusLineManager();
//      }
//      return window.getStatusLineManager();
//    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.application.IActionBarConfigurer
     */
    public IMenuManager getMenuManager() {
      if( proxy != null ) {
        return proxy.getMenuManager();
      }
      return window.getMenuManager();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.internal.AbstractActionBarConfigurer
     */
    public ICoolBarManager getCoolBarManager() {
      if( proxy != null ) {
        return proxy.getCoolBarManager();
      }
      return window.getCoolBarManager2();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.application.IActionBarConfigurer
     */
    public void registerGlobalAction( IAction action ) {
      if( proxy != null ) {
        proxy.registerGlobalAction( action );
      }
      window.registerGlobalAction( action );
    }

//    private IActionBarPresentationFactory getActionBarPresentationFactory() {
//      WorkbenchWindow window = ( WorkbenchWindow )getWindowConfigurer().getWindow();
//      return window.getActionBarPresentationFactory();
//    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.application.IActionBarConfigurer#createToolBarManager()
     */
    public IToolBarManager createToolBarManager() {
//      if( proxy != null ) {
        return proxy.createToolBarManager();
//      }
//      return getActionBarPresentationFactory().createToolBarManager();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.application.IActionBarConfigurer#createToolBarContributionItem(org.eclipse.jface.action.IToolBarManager,
     *      java.lang.String)
     */
    public IToolBarContributionItem createToolBarContributionItem( IToolBarManager toolBarManager,
                                                                   String id )
    {
//      if( proxy != null ) {
        return proxy.createToolBarContributionItem( toolBarManager, id );
//      }
//      return getActionBarPresentationFactory().createToolBarContributionItem( toolBarManager,
//                                                                              id );
    }
  }

  
  public WorkbenchWindowConfigurer( final WorkbenchWindow window ) {
    this.window = window;
  }

  public void createDefaultContents( final Shell shell ) {
    window.createDefaultContents( shell );
  }
  
  String basicGetTitle() {
    return windowTitle;
  }

  public String getTitle() {
      Shell shell = window.getShell();
      if (shell != null) {
          // update the cached title
          windowTitle = shell.getText();
      }
      return windowTitle;
  }

  public void setTitle(String title) {
      if (title == null) {
          throw new IllegalArgumentException();
      }
      windowTitle = title;
      Shell shell = window.getShell();
      if (shell != null && !shell.isDisposed()) {
//          shell.setText(TextProcessor.process(title, "-"));  //$NON-NLS-1$
        shell.setText( title );
      }
  }

  public boolean getShowMenuBar() {
    return showMenuBar;
  }
  
  public void setShowMenuBar( final boolean show ) {
    showMenuBar = show;
    WorkbenchWindow win = (WorkbenchWindow) getWindow();
    Shell shell = win.getShell();
    if (shell != null) {
      boolean showing = shell.getMenuBar() != null;
      if (show != showing) {
        if (show) {
          shell.setMenuBar(win.getMenuBarManager().getMenu());
        } else {
          shell.setMenuBar(null);
        }
      }
    }
  }

  public boolean getShowCoolBar() {
    return showToolBar;
  }

  public void setShowCoolBar( final boolean show ) {
    showToolBar = show;
    window.setCoolBarVisible( show );
    // @issue need to be able to reconfigure after window's controls created
  }

  public boolean getShowPerspectiveBar() {
    return showPerspectiveBar;
  }

  public void setShowPerspectiveBar( final boolean show ) {
      showPerspectiveBar = show;
      window.setPerspectiveBarVisible( show );
      // @issue need to be able to reconfigure after window's controls created
  }

  public Point getInitialSize() {
    if( initialSize == null ) {
      Rectangle bounds = Display.getCurrent().getBounds();
      initialSize = new Point( bounds.width, bounds.height );
    }
    return initialSize;
  }

  public void setInitialSize( final Point size ) {
    initialSize = size;
  }

  public AbstractPresentationFactory getPresentationFactory() {
    if( presentationFactory == null ) {
      presentationFactory = createDefaultPresentationFactory();
    }
    return presentationFactory;
  }
  
  public IWorkbenchWindow getWindow() {
    return window;
  }

  public IActionBarConfigurer getActionBarConfigurer() {
    if( actionBarConfigurer == null ) {
      // lazily initialize
      actionBarConfigurer = new WindowActionBarConfigurer();
    }
    return actionBarConfigurer;
  }

  public int getShellStyle() {
    return shellStyle;
  }

  public void setShellStyle( final int shellStyle ) {
    this.shellStyle = shellStyle;
  }
  
  //////////////////
  // helping methods
  
  private AbstractPresentationFactory createDefaultPresentationFactory() {
//    String factoryId = Workbench.getInstance().getPresentationId();
//    if( factoryId != null && factoryId.length() > 0 ) {
//      AbstractPresentationFactory factory = WorkbenchPlugin.getDefault()
//        .getPresentationFactory( factoryId );
//      if( factory != null ) {
//        return factory;
//      }
//    }
    // TODO [fappel] support of custom presentation factory
    return new WorkbenchPresentationFactory();
  }
}
