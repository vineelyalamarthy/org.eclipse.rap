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

package org.eclipse.ui.entrypoint;

import org.eclipse.core.runtime.Assert;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.internal.WorkbenchWindowConfigurer;


public class WorkbenchWindowAdvisor {

  private final IWorkbenchWindowConfigurer windowConfigurer;
  
  public WorkbenchWindowAdvisor( final IWorkbenchWindowConfigurer configurer ) {
    Assert.isNotNull( configurer );
    this.windowConfigurer = configurer;
  }
  
  public void preWindowOpen() {
    // do nothing
  }

  public ActionBarAdvisor createActionBarAdvisor( 
    final IActionBarConfigurer configurer )
  {
    return new ActionBarAdvisor( configurer );
  }

  public void createWindowContents( final Shell shell ) {
    WorkbenchWindowConfigurer configurer
      = ( WorkbenchWindowConfigurer )getWindowConfigurer();
    configurer.createDefaultContents( shell );
  }
  
  protected IWorkbenchWindowConfigurer getWindowConfigurer() {
    return windowConfigurer;
  }

  public void postWindowOpen() {
    // do nothing
  }
}
