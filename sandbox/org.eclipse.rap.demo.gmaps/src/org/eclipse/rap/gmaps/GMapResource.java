package org.eclipse.rap.gmaps;
/*******************************************************************************
 * Copyright (c) 2002-2008 Innoopract Informationssysteme GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Innoopract Informationssysteme GmbH - initial API and implementation
 ******************************************************************************/
import org.eclipse.rwt.resources.IResource;
import org.eclipse.rwt.resources.IResourceManager.RegisterOptions;

public final class GMapResource implements IResource {

  public String getCharset() {
    return "ISO-8859-1";
  }

  public ClassLoader getLoader() {
    return this.getClass().getClassLoader();
  }

  public RegisterOptions getOptions() {
    return RegisterOptions.VERSION_AND_COMPRESS;
  }

  public String getLocation() {
    return "org/eclipse/rap/gmaps/GMap.js";
  }

  public boolean isJSLibrary() {
    return true;
  }

  public boolean isExternal() {
    return false;
  }
}
