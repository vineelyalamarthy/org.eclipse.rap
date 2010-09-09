/******************************************************************************
 * Copyright © 2010-2011 Austin Riddle
 * All Rights Reserved.
 * 
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Austin Riddle - initial API and implementation
 * 
 *****************************************************************************/
package org.eclipse.rap.rwt.visualization.jit.internal;

import org.eclipse.rwt.resources.IResource;
import org.eclipse.rwt.resources.IResourceManager.RegisterOptions;


public class JITAPIResource implements IResource {

  private static final String KEY_SYSTEM_PROPERTY = "org.eclipse.rap.rwt.visualization.jit.api";
  private String location;
  
  public String getCharset() {
    return "ISO-8859-1";
  }

  public ClassLoader getLoader() {
    return this.getClass().getClassLoader();
  }

  public RegisterOptions getOptions() {
    return RegisterOptions.VERSION;
  }

  public String getLocation() {
    if( location == null ) {
      location = System.getProperty( KEY_SYSTEM_PROPERTY, "org/eclipse/rap/rwt/visualization/jit/internal/jit.js"); 
    }
    return location;
  }

  public boolean isJSLibrary() {
    return true;
  }

  public boolean isExternal() {
    return false;
  }
}
