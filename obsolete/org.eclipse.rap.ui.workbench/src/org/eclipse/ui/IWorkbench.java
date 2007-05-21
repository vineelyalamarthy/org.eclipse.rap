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

import org.eclipse.swt.graphics.Device;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;


public interface IWorkbench {
  IWorkbenchWindow getActiveWorkbenchWindow();
  Device getDisplay();
  IPerspectiveRegistry getPerspectiveRegistry();
  IWorkbenchBrowserSupport getBrowserSupport();
  void close();
  
  /**
   * Returns the shared images for the workbench.
   * 
   * @return the shared image manager
   */
  public ISharedImages getSharedImages();
}
