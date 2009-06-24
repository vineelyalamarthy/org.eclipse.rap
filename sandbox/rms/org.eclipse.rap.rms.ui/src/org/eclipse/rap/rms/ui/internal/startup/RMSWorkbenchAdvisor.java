// Created on 09.09.2007
package org.eclipse.rap.rms.ui.internal.startup;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.rap.rms.ui.Constants;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.activities.IMutableActivityManager;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;


public class RMSWorkbenchAdvisor extends WorkbenchAdvisor {

  public void preStartup() {
    IWorkbench workbench = PlatformUI.getWorkbench();
    IMutableActivityManager activitySupport
      = workbench.getActivitySupport().createWorkingCopy();
    Set<String> enabledActivityIds = new HashSet<String>();
    enabledActivityIds.add( "org.eclipse.rap.rms.ui" );
    activitySupport.setEnabledActivityIds( enabledActivityIds );
  }
  
  public String getInitialWindowPerspectiveId() {
    return Constants.PERSPECTIVE_ID;
  }
  
  public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor( 
    final IWorkbenchWindowConfigurer configurer )
  {
    return new RMSWorkbenchWindowAdvisor( configurer );
  }
}
