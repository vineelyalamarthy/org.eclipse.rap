package org.eclipse.rap.rwt.custom.demo.spreadsheet;

import org.eclipse.rwt.lifecycle.IEntryPoint;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;


public class SpreadSheetEntryPoint implements IEntryPoint {

  public int createUI() {
    Display display = PlatformUI.createDisplay();
    SpreadSheetWorkbenchAdvisor advisor = new SpreadSheetWorkbenchAdvisor();
    return PlatformUI.createAndRunWorkbench( display, advisor );
  }
}
