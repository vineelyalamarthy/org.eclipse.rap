/*******************************************************************************
 * Copyright (c) 2009 Innoopract Informationssysteme GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Innoopract Informationssysteme GmbH - initial API and implementation
 ******************************************************************************/

package org.eclipse.swt.internal.custom.styledtextkit;

import org.eclipse.rwt.resources.IResource;
import org.eclipse.rwt.resources.IResourceManager.RegisterOptions;

public final class BlankContentResource implements IResource {

  public String getCharset() {
    return "UTF-8"; //$NON-NLS-1$
  }

  public ClassLoader getLoader() {
    return BlankContentResource.class.getClassLoader();
  }

  public String getLocation() {
    return "org/eclipse/swt/custom/blank.html"; //$NON-NLS-1$
  }

  public RegisterOptions getOptions() {
    return RegisterOptions.NONE;
  }

  public boolean isExternal() {
    return false;
  }

  public boolean isJSLibrary() {
    return false;
  }
}
