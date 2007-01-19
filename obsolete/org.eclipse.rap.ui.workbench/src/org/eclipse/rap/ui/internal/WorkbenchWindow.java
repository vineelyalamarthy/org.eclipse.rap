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

import java.util.HashMap;
import java.util.Map;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.rap.jface.action.IAction;
import org.eclipse.rap.jface.action.MenuManager;
import org.eclipse.rap.jface.commands.ActionHandler;
import org.eclipse.rap.jface.window.ApplicationWindow;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.custom.CBanner;
import org.eclipse.rap.rwt.custom.StackLayout;
import org.eclipse.rap.rwt.events.ControlAdapter;
import org.eclipse.rap.rwt.events.ControlEvent;
import org.eclipse.rap.rwt.graphics.Point;
import org.eclipse.rap.rwt.graphics.Rectangle;
import org.eclipse.rap.rwt.widgets.*;
import org.eclipse.rap.ui.*;
import org.eclipse.rap.ui.entrypoint.*;
import org.eclipse.rap.ui.internal.layout.*;
import org.eclipse.rap.ui.internal.misc.UIListenerLogging;


public class WorkbenchWindow
  extends ApplicationWindow
  implements IWorkbenchWindow
{
  private static final int FILL_ALL_ACTION_BARS
    =    ActionBarAdvisor.FILL_MENU_BAR
       | ActionBarAdvisor.FILL_COOL_BAR
       | ActionBarAdvisor.FILL_STATUS_LINE;
  
  private final int number;
  private WorkbenchPage page;
  private Composite pageComposite;
  private WorkbenchWindowAdvisor windowAdvisor;
  private WorkbenchWindowConfigurer windowConfigurer = null;
  private ActionBarAdvisor actionBarAdvisor;
  private TrimLayout defaultLayout;
  private CBanner topBar;
  private IWindowTrim topBarTrim;
  private boolean coolBarVisible = true;
  private boolean perspectiveBarVisible = true;
  // Previous shell size. Used to prevent the CBanner from triggering
  // redundant layouts
  private Point lastShellSize = new Point( 0, 0 );
  private Map globalActionHandlersByCommandId = new HashMap();
  private WWinPartService partService = new WWinPartService(this);
  
    
  public WorkbenchWindow( int number ) {
    super( null );
    this.number = number;
    
    addMenuBar();
    addCoolBar( RWT.NONE );
    
    fireWindowOpening();
    
    // Fill the action bars
    fillActionBars( FILL_ALL_ACTION_BARS );
  }

  public void fillActionBars( int flags ) {
    getActionBarAdvisor().fillActionBars( flags );
  }

  public int getNumber() {
    return number;
  }

  public ISelectionService getSelectionService() {
    return partService.getSelectionService();
  }

  public IWorkbenchPage getActivePage() {
    return page;
  }

  public IWorkbenchPage[] getPages() {
    return new IWorkbenchPage[] { page };
  }
  
  protected void busyOpenPage( final String perspID, final IAdaptable input )
    throws WorkbenchException
  {
    IWorkbenchPage newPage = new WorkbenchPage( this, perspID, input );
    page = ( WorkbenchPage )newPage;
    firePageOpened( newPage );
    setActivePage( newPage );
  }

  public void open() {
    super.open();
    //  It's time for a layout ... to insure that if TrimLayout
    // is in play, it updates all of the trim it's responsible
    // for. We have to do this before updating in order to get
    // the PerspectiveBar management correct...see defect 137334
    getShell().layout();
  }
  
  public void setActivePage( final IWorkbenchPage in ) {
//    if( getActiveWorkbenchPage() == in ) {
//      return;
//    }
//    // 1FVGTNR: ITPUI:WINNT - busy cursor for switching perspectives
//    BusyIndicator.showWhile( getShell().getDisplay(), new Runnable() {
//
//      public void run() {
//        // Deactivate old persp.
//        WorkbenchPage currentPage = getActiveWorkbenchPage();
//        if( currentPage != null ) {
//          currentPage.onDeactivate();
//        }
//        // Activate new persp.
//        if( in == null || pageList.contains( in ) ) {
//          pageList.setActive( in );
//        }
//        WorkbenchPage newPage = pageList.getActive();
        WorkbenchPage newPage = ( WorkbenchPage )in;
        Composite parent = getPageComposite();
        StackLayout layout = ( StackLayout )parent.getLayout();
//        if( newPage != null ) {
//          layout.topControl = newPage.getClientComposite();
          layout.topControl = newPage.getClientComposite();
          parent.layout();
//          hideEmptyWindowContents();
          newPage.onActivate();
//          firePageActivated( newPage );
//          if( newPage.getPerspective() != null ) {
//            firePerspectiveActivated( newPage, newPage.getPerspective() );
//          }
//        } else {
//          layout.topControl = null;
//          parent.layout();
//        }
//        updateFastViewBar();
//        if( isClosing() ) {
//          return;
//        }
//        updateDisabled = false;
//        // Update action bars ( implicitly calls updateActionBars() )
//        updateActionSets();
//        submitGlobalActions();
//        if( perspectiveSwitcher != null ) {
//          perspectiveSwitcher.update( false );
//        }
//        getMenuManager().update( IAction.TEXT );
//      }
//    } );
  }

  public void setCoolBarVisible( final boolean visible ) {
    boolean oldValue = coolBarVisible;
    coolBarVisible = visible;
    if( oldValue != coolBarVisible ) {
      updateLayoutDataForContents();
    }
  }
  
  protected void createDefaultContents( final Shell shell ) {
    defaultLayout = new TrimLayout();
    defaultLayout.setSpacing( 5, 5, 2, 2 );
    shell.setLayout( defaultLayout );
    
    Menu menuBar = getMenuBarManager().createMenuBar( shell );
    if( getWindowConfigurer().getShowMenuBar() ) {
      shell.setMenuBar( menuBar );
    }
    
    // TODO: [fappel] since we do not have reasonable compute size
    //                implementations...
    topBar = new CBanner( shell, RWT.NONE ) {
      public Point computeSize( int wHint, int hHint, boolean changed ) {
        return new Point( super.computeSize( wHint, hHint, changed ).x, 34 );
      }
    };
    String id = "org.eclipse.ui.internal.WorkbenchWindow.topBar";
    topBarTrim 
      = new WindowTrimProxy( topBar, id, "&Main Toolbar", RWT.NONE, true );
    
    CacheWrapper coolbarCacheWrapper = new CacheWrapper( topBar );
    final Control coolBar
      = createCoolBarControl( coolbarCacheWrapper.getControl() );
    coolBar.addControlListener( new ControlAdapter() {
      public void controlResized( ControlEvent event ) {
        // If the user is dragging the sash then we will need to force
        // a resize. However, if the coolbar was resized programatically
        // then everything is already layed out correctly. There is no
        // direct way to tell the difference between these cases,
        // however
        // we take advantage of the fact that dragging the sash does not
        // change the size of the shell, and only force another layout
        // if the shell size is unchanged.
        Rectangle clientArea = shell.getClientArea();
        if(    lastShellSize.x == clientArea.width
            && lastShellSize.y == clientArea.height )
        {
          LayoutUtil.resize( coolBar );
        }
        lastShellSize.x = clientArea.width;
        lastShellSize.y = clientArea.height;
      }
    } );
    if( getWindowConfigurer().getShowCoolBar() ) {
      topBar.setLeft( coolbarCacheWrapper.getControl() );
    }

    ToolBar perspectiveBar 
      = new ToolBar( topBar, RWT.BORDER );
    topBar.setRight( perspectiveBar );
    
    createPageComposite( shell );
    
    setLayoutDataForContents();
  }

  protected Composite createPageComposite( final Composite parent ) {
    pageComposite = new Composite( parent, RWT.NONE );
    pageComposite.setLayout( new StackLayout() );
    return pageComposite;
  }

  // override createContents from jface window
  protected Control createContents( final Composite parent ) {
    // we know from Window.create that the parent is a Shell.
    getWindowAdvisor().createWindowContents( ( Shell )parent );
    // the page composite must be set by createWindowContents
    String msg 
      = "createWindowContents must call configurer.createPageComposite";
    Assert.isNotNull( pageComposite, msg );
    return pageComposite;
  }
    
  protected Composite getPageComposite() {
    return pageComposite;
  }
  
  public boolean getCoolBarVisible() {
    return coolBarVisible;
  }
  
  public boolean getPerspectiveBarVisible() {
    return perspectiveBarVisible;
  }

  protected void initializeBounds() {
    Point size = getInitialSize();
    Point location = getInitialLocation( size );
    getShell().setBounds( getConstrainedShellBounds( new Rectangle( location.x,
                                                                    location.y,
                                                                    size.x,
                                                                    size.y ) ) );
  }

  protected Point getInitialSize() {
    return getWindowConfigurer().getInitialSize();
  }

  protected Point getInitialLocation( final Point size ) {
    Shell shell = getShell();
    if( shell != null ) {
      return shell.getLocation();
    }
    return super.getInitialLocation( size );
  }

  public MenuManager getMenuManager() {
    return getMenuBarManager();
  }

  
  // ////////
  // package
  
  WorkbenchWindowConfigurer getWindowConfigurer() {
    if( windowConfigurer == null ) {
      // lazy initialize
      windowConfigurer = new WorkbenchWindowConfigurer( this );
    }
    return windowConfigurer;
  }

  void registerGlobalAction( final IAction globalAction ) {
    String commandId = globalAction.getActionDefinitionId();
    if( commandId != null ) {
      final Object value = globalActionHandlersByCommandId.get( commandId );
      if( value instanceof ActionHandler ) {
        // This handler is about to get clobbered, so dispose it.
        final ActionHandler handler = ( ActionHandler )value;
        handler.dispose();
      }
      globalActionHandlersByCommandId.put( commandId,
                                           new ActionHandler( globalAction ) );
    }
    submitGlobalActions();
  }

  void submitGlobalActions() {
    // TODO: [fappel] implementation
  }

  
  /////////////////////////
  // interface IPageService
  
  public void addPageListener( IPageListener listener ) {
    throw new UnsupportedOperationException();
  }
  
  public void addPerspectiveListener( IPerspectiveListener listener ) {
    throw new UnsupportedOperationException();
  }
  
  public void removePageListener( IPageListener listener ) {
    throw new UnsupportedOperationException();
  }
  
  public void removePerspectiveListener( IPerspectiveListener listener ) {
    throw new UnsupportedOperationException();
  }
  
  
  //////////////////
  // helping methods
  
    private void firePageOpened( IWorkbenchPage page ) {
//    String label = null; // debugging only
//    if( UIStats.isDebugging( UIStats.NOTIFY_PAGE_LISTENERS ) ) {
//      label = "opened " + page.getLabel(); //$NON-NLS-1$
//    }
    try {
//      UIStats.start( UIStats.NOTIFY_PAGE_LISTENERS, label );
      UIListenerLogging.logPageEvent( this,
                                      page,
                                      UIListenerLogging.WPE_PAGE_OPENED );
//      pageListeners.firePageOpened( page );
      partService.pageOpened( page );
    } finally {
//      UIStats.end( UIStats.NOTIFY_PAGE_LISTENERS, page.getLabel(), label );
    }
  }

  
  private WorkbenchWindowAdvisor getWindowAdvisor() {
    if( windowAdvisor == null ) {
      WorkbenchWindowConfigurer conf = getWindowConfigurer();
      windowAdvisor = getAdvisor().createWorkbenchWindowAdvisor( conf );
      Assert.isNotNull( windowAdvisor );
    }
    return windowAdvisor;
  }

  private ActionBarAdvisor getActionBarAdvisor() {
    if( actionBarAdvisor == null ) {
      IActionBarConfigurer configurer 
        = getWindowConfigurer().getActionBarConfigurer();
      actionBarAdvisor 
        = getWindowAdvisor().createActionBarAdvisor( configurer );
      Assert.isNotNull( actionBarAdvisor );
    }
    return actionBarAdvisor;
  }

  
  private WorkbenchAdvisor getAdvisor() {
    return Workbench.getInstance().getAdvisor();
  }

  private void setLayoutDataForContents() {
    updateLayoutDataForContents();
  }
  
  private void updateLayoutDataForContents() {
    if( defaultLayout == null ) {
      return;
    }
    
    // @issue this is not ideal; coolbar and perspective shortcuts should be
    // separately configurable
    boolean showCoolBar 
      =    getCoolBarVisible() 
        && getWindowConfigurer().getShowCoolBar();
    boolean showPerspectiveBar 
      =    getPerspectiveBarVisible() 
        && getWindowConfigurer().getShowPerspectiveBar();
    if( showCoolBar || showPerspectiveBar ) {
      if( defaultLayout.getTrim( topBarTrim.getId() ) == null ) {
        defaultLayout.addTrim( RWT.TOP, topBarTrim );
      }
      topBar.setVisible( true );
    } else {
      defaultLayout.removeTrim( topBarTrim );
      topBar.setVisible( false );
    }
//    if( fastViewBar != null ) {
//      if( getFastViewBarVisible()
//          && getWindowConfigurer().getShowFastViewBars() )
//      {
//        int side = fastViewBar.getSide();
//        if( defaultLayout.getTrim( fastViewBar.getId() ) == null ) {
//          defaultLayout.addTrim( side, fastViewBar );
//        }
//        fastViewBar.getControl().setVisible( true );
//      } else {
//        defaultLayout.removeTrim( fastViewBar );
//        fastViewBar.getControl().setVisible( false );
//      }
//    }
//    if( getStatusLineVisible() && getWindowConfigurer().getShowStatusLine() ) {
//      if( defaultLayout.getTrim( getStatusLineTrim().getId() ) == null ) {
//        defaultLayout.addTrim( SWT.BOTTOM, getStatusLineTrim() );
//      }
//      getStatusLineManager().getControl().setVisible( true );
//    } else {
//      defaultLayout.removeTrim( getStatusLineTrim() );
//      getStatusLineManager().getControl().setVisible( false );
//    }
//    if( heapStatus != null ) {
//      if( getShowHeapStatus() ) {
//        if( heapStatus.getLayoutData() == null ) {
//          heapStatusTrim.setWidthHint( heapStatus.computeSize( SWT.DEFAULT,
//                                                               SWT.DEFAULT ).x );
//          heapStatusTrim.setHeightHint( getStatusLineManager().getControl()
//            .computeSize( SWT.DEFAULT, SWT.DEFAULT ).y );
//        }
//        if( defaultLayout.getTrim( heapStatusTrim.getId() ) == null ) {
//          defaultLayout.addTrim( SWT.BOTTOM, heapStatusTrim );
//        }
//        heapStatus.setVisible( true );
//      } else {
//        defaultLayout.removeTrim( heapStatusTrim );
//        heapStatus.setVisible( false );
//      }
//    }
//    if( progressRegion != null ) {
//      if( getWindowConfigurer().getShowProgressIndicator() ) {
//        if( defaultLayout.getTrim( progressRegion.getId() ) == null ) {
//          defaultLayout.addTrim( SWT.BOTTOM, progressRegion );
//        }
//        progressRegion.getControl().setVisible( true );
//      } else {
//        defaultLayout.removeTrim( progressRegion );
//        progressRegion.getControl().setVisible( false );
//      }
//    }
    defaultLayout.setCenterControl( getPageComposite() );
    // Re-populate the trim elements
//    trimMgr.update( true, false, !topBar.getVisible() );
  }
  
  private void fireWindowOpening() {
    getWindowAdvisor().preWindowOpen();
  }
}
