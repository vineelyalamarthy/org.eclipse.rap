/*******************************************************************************
 * Copyright (c) 2010-2011 Austin Riddle.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Austin Riddle - initial API, implementation and documentation
 ******************************************************************************/
package org.eclipse.rap.rwt.visualization.jit.internal;

import org.eclipse.rwt.internal.util.HTML;
import org.eclipse.rwt.resources.IResource;
import org.eclipse.rwt.resources.IResourceManager.RegisterOptions;

public abstract class JITVisualizationResource implements IResource {

  public String getCharset() {
    return HTML.CHARSET_NAME_ISO_8859_1;
  }

  public ClassLoader getLoader() {
    return this.getClass().getClassLoader();
  }

  public RegisterOptions getOptions() {
    return RegisterOptions.VERSION;
  }

  public abstract String getLocation();

  public boolean isJSLibrary() {
    return true;
  }

  public boolean isExternal() {
    return false;
  }
}
