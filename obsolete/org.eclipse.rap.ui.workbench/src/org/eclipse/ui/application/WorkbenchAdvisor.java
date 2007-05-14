/*******************************************************************************
 * Copyright (c) 2002-2006 Innoopract Informationssysteme GmbH. All rights
 * reserved. This program and the accompanying materials are made available
 * under the terms of the Eclipse Public License v1.0 which accompanies this
 * distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 * Contributors: Innoopract Informationssysteme GmbH - initial API and
 * implementation
 ******************************************************************************/
package org.eclipse.ui.application;

import org.eclipse.core.runtime.IAdaptable;
import com.w4t.ParamCheck;

public abstract class WorkbenchAdvisor {

  private IWorkbenchConfigurer workbenchConfigurer;

  public void openWindows() {
    getWorkbenchConfigurer().openFirstTimeWindow();
  }

  protected IWorkbenchConfigurer getWorkbenchConfigurer() {
    return workbenchConfigurer;
  }

  public final void internalBasicInitialize( 
    final IWorkbenchConfigurer configurer )
  {
    ParamCheck.notNull( configurer, "configurer" );
    this.workbenchConfigurer = configurer;
    initialize( configurer );
  }

  /**
   * Performs arbitrary initialization before the workbench starts running.
   * <p>
   * This method is called during workbench initialization prior to any windows
   * being opened. Clients must not call this method directly (although super
   * calls are okay). The default implementation does nothing. Subclasses may
   * override. Typical clients will use the configurer passed in to tweak the
   * workbench. If further tweaking is required in the future, the configurer
   * may be obtained using <code>getWorkbenchConfigurer</code>.
   * </p>
   * 
   * @param configurer an object for configuring the workbench
   */
  public void initialize( final IWorkbenchConfigurer configurer ) {
    // do nothing
  }

  public IAdaptable getDefaultPageInput() {
    // default: no input
    return null;
  }

  public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(
    final IWorkbenchWindowConfigurer windowConfigurer )
  {
    return new WorkbenchWindowAdvisor( windowConfigurer );
  }

  public abstract String getInitialWindowPerspectiveId();
}
