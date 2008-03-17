package org.eclipse.rap.maildemo.ext;

import org.eclipse.rwt.lifecycle.IEntryPoint;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

/**
 * This class controls all aspects of the application's execution
 */
public class Application implements IEntryPoint {

  public int createUI() {
    final Display result = PlatformUI.createDisplay();
    return PlatformUI.createAndRunWorkbench( result, new ApplicationWorkbenchAdvisor() );
  }
}