/*******************************************************************************
 * Copyright (c) 2008 Innoopract Informationssysteme GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Innoopract Informationssysteme GmbH - initial API and implementation
 ******************************************************************************/
package org.eclipse.ui.forms.examples.internal;

import org.eclipse.rwt.lifecycle.IEntryPoint;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.WorkbenchAdvisor;


public final class DefaultEntryPoint implements IEntryPoint {

  public int createUI() {
    Display display = PlatformUI.createDisplay();
    WorkbenchAdvisor worbenchAdvisor = new WorkbenchAdvisor() {
      public String getInitialWindowPerspectiveId() {
        return "org.eclipse.rap.ui.forms.examples.defaultPerspective";
      }
    };
    return PlatformUI.createAndRunWorkbench( display, worbenchAdvisor );
  }
}
