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

package org.eclipse.ui.internal;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.window.WindowManager;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.*;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.internal.registry.PerspectiveRegistry;
import com.w4t.SessionSingletonBase;

// TODO: [fappel] completion of this basic Workbench implementation...
public class Workbench extends SessionSingletonBase implements IWorkbench {

  private WorkbenchAdvisor advisor;
  private WorkbenchConfigurer workbenchConfigurer;
  private Display display;
  private WindowManager windowManager;

  private Workbench() {
  }
  
  public static Workbench getInstance() {
    return ( Workbench )getInstance( Workbench.class );
  }

  public static void createAndRunWorkbench( final Display display, 
                                            final WorkbenchAdvisor advisor )
  {
    Workbench workbench = getInstance();
    workbench.display = display;
    workbench.advisor = advisor;
    workbench.runUI();
  }

  public static Display createDisplay() {
    return new Display();
  }
  
  public IAdaptable getDefaultPageInput() {
    return getAdvisor().getDefaultPageInput();
  }
  
  public String getDefaultPerspectiveId() {
    return getAdvisor().getInitialWindowPerspectiveId();
  }
  
  public String getPresentationId() {
    // TODO: [fappel] change this hardcoded mechanism
    return "org.eclipse.ui.presentations.default";
  }
  
  public void close() {
    // TODO: [fappel] reasonable implementation
    MessageDialog.openError( getActiveWorkbenchWindow().getShell(), 
                             "RAP Demo", "Workbench close not yet implemented", 
                             null );
  }

  //////////////////
  // package private
  
  void openFirstTimeWindow() {
    WorkbenchWindow newWindow = newWorkbenchWindow();
    newWindow.create();
    windowManager.add( newWindow );
    String perspectiveId = getPerspectiveRegistry().getDefaultPerspective();
    if( perspectiveId != null ) {
      try {
        newWindow.busyOpenPage( perspectiveId, getDefaultPageInput() );
      } catch( final WorkbenchException wbe ) {
        windowManager.remove( newWindow );
        // TODO: [fappel] Exception handling 
        throw new RuntimeException( wbe );
      }
    }
    newWindow.open();
  }

  WorkbenchAdvisor getAdvisor() {
    return advisor;
  }
  
  
  // /////////////////////
  // interface IWorkbench
  
  public Device getDisplay() {
    return display;
  }
  
  public IPerspectiveRegistry getPerspectiveRegistry() {
    return PerspectiveRegistry.getInstance();
  }

  public IWorkbenchWindow getActiveWorkbenchWindow() {
//    // Return null if called from a non-UI thread.
//    // This is not spec'ed behaviour and is misleading, however this is how
//    // it
//    // worked in 2.1 and we cannot change it now.
//    // For more details, see [Bug 57384] [RCP] Main window not active on
//    // startup
//    if( Display.getCurrent() == null ) {
//      return null;
//    }
//    // Look at the current shell and up its parent
//    // hierarchy for a workbench window.
//    Control shell = display.getActiveShell();
//    while( shell != null ) {
//      Object data = shell.getData();
//      if( data instanceof IWorkbenchWindow ) {
//        return ( IWorkbenchWindow )data;
//      }
//      shell = shell.getParent();
//    }
//    // Look for the window that was last known being
//    // the active one
//    WorkbenchWindow win = getActivatedWindow();
//    if( win != null ) {
//      return win;
//    }
    // Look at all the shells and pick the first one
    // that is a workbench window.
    Shell shells[] = display.getShells();
    for( int i = 0; i < shells.length; i++ ) {
      Object data = shells[ i ].getData();
      if( data instanceof IWorkbenchWindow ) {
        return ( IWorkbenchWindow )data;
      }
    }
    // Can't find anything!
    return null;
  }

  
  
  // ////////////////
  // helping methods
  
  private void runUI() {
    init();
  }

  private void init() {
    advisor.internalBasicInitialize( getWorkbenchConfigurer() );
    windowManager = new WindowManager();
    advisor.openWindows();
  }

  WorkbenchConfigurer getWorkbenchConfigurer() {
    if( workbenchConfigurer == null ) {
      workbenchConfigurer = new WorkbenchConfigurer();
    }
    return workbenchConfigurer;
  }

  
  private WorkbenchWindow newWorkbenchWindow() {
    return new WorkbenchWindow( getNewWindowNumber() );
  }

  private int getNewWindowNumber() {
    // Get window list.
    Window[] windows = windowManager.getWindows();
    int count = windows.length;
    // Create an array of booleans (size = window count).
    // Cross off every number found in the window list.
    boolean checkArray[] = new boolean[ count ];
    for( int nX = 0; nX < count; nX++ ) {
      if( windows[ nX ] instanceof WorkbenchWindow ) {
        WorkbenchWindow ww = ( WorkbenchWindow )windows[ nX ];
        int index = ww.getNumber() - 1;
        if( index >= 0 && index < count ) {
          checkArray[ index ] = true;
        }
      }
    }
    // Return first index which is not used.
    // If no empty index was found then every slot is full.
    // Return next index.
    for( int index = 0; index < count; index++ ) {
      if( !checkArray[ index ] ) {
        return index + 1;
      }
    }
    return count + 1;
  }
}