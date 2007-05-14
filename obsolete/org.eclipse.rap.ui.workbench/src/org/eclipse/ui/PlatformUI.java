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

package org.eclipse.ui;

import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.internal.Workbench;


public final class PlatformUI {
  
  public static final String PLUGIN_ID = "org.eclipse.rap.ui.workbench"; 
  public static final String PLUGIN_EXTENSION_NAME_SPACE = "org.eclipse.ui"; 
  
  public static Display createDisplay() {
    return Workbench.createDisplay();
  }
  
  public static void createAndRunWorkbench( final Display display,
                                            final WorkbenchAdvisor advisor )
  {
    Workbench.createAndRunWorkbench( display, advisor );
  }

  public static IWorkbench getWorkbench() {
    return Workbench.getInstance();
  }
}
