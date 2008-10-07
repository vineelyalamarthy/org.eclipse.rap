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

package org.eclipse.rap.maildemo.ext;

import org.eclipse.rwt.lifecycle.IEntryPoint;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;


/**
 * This class controls all aspects of the application's execution.
 */
public class Application implements IEntryPoint {

  public int createUI() {
    final Display result = PlatformUI.createDisplay();
    return PlatformUI.createAndRunWorkbench( result,
                                             new ApplicationWorkbenchAdvisor() );
  }
}
