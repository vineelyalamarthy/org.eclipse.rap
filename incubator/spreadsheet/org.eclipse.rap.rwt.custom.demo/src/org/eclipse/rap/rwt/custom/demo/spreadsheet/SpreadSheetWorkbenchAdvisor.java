package org.eclipse.rap.rwt.custom.demo.spreadsheet;

import org.eclipse.ui.application.*;


public class SpreadSheetWorkbenchAdvisor extends WorkbenchAdvisor {

  public String getInitialWindowPerspectiveId() {
    return "org.eclipse.rap.rwt.custom.demo.perspectives.spreadsheet";
  }
  
  public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(
    final IWorkbenchWindowConfigurer configurer )
  {
    return new SpreadSheetWorkbenchWindowAdvisor( configurer );
  }
}
