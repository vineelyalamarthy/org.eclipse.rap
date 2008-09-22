// Created on 09.09.2007
package org.eclipse.rap.rms.ui.internal.startup;

import org.eclipse.rap.rms.ui.Constants;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;


public class RMSWorkbenchAdvisor extends WorkbenchAdvisor {

  public String getInitialWindowPerspectiveId() {
    return Constants.PERSPECTIVE_ID;
  }
  
  public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor( 
    final IWorkbenchWindowConfigurer configurer )
  {
    return new RMSWorkbenchWindowAdvisor( configurer );
  }
}
