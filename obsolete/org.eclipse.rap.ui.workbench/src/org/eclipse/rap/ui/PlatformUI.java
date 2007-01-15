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

package org.eclipse.rap.ui;

import org.eclipse.rap.rwt.widgets.Display;
import org.eclipse.rap.ui.entrypoint.WorkbenchAdvisor;
import org.eclipse.rap.ui.internal.Workbench;


public final class PlatformUI {
  
  public static final String PLUGIN_ID = "org.eclipse.rap.ui.workbench"; 
  
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
