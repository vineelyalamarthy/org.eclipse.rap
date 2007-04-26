/*******************************************************************************
 * Copyright (c) 2002-2006 Innoopract Informationssysteme GmbH. All rights
 * reserved. This program and the accompanying materials are made available
 * under the terms of the Eclipse Public License v1.0 which accompanies this
 * distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 * Contributors: Innoopract Informationssysteme GmbH - initial API and
 * implementation
 ******************************************************************************/
package org.eclipse.ui.internal;

import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.entrypoint.IWorkbenchConfigurer;

public class WorkbenchConfigurer implements IWorkbenchConfigurer {

  public void openFirstTimeWindow() {
    ( ( Workbench )getWorkbench() ).openFirstTimeWindow();
  }

  public IWorkbench getWorkbench() {
    return PlatformUI.getWorkbench();
  }
}
