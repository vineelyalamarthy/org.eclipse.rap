package org.eclipse.rap.gmaps;
/*******************************************************************************
 * Copyright (c) 2002-2007 Innoopract Informationssysteme GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Innoopract Informationssysteme GmbH - initial API and implementation
 ******************************************************************************/

import org.eclipse.rwt.internal.util.HTML;
import org.eclipse.rwt.resources.IResource;
import org.eclipse.rwt.resources.IResourceManager.RegisterOptions;


public class GMapAPIResource implements IResource {

  private static final String KEY_SYSTEM_PROPERTY = "org.eclipse.rap.gmaps.key";
  // key for localhost rap development on port 9090
  private static final String KEY_LOCALHOST
  = "ABQIAAAAjE6itH-9WA-8yJZ7sZwmpRQz5JJ2zPi3YI9JDWBFF"
	  + "6NSsxhe4BSfeni5VUSx3dQc8mIEknSiG9EwaQ";
  private String location;
  
  public String getCharset() {
    return HTML.CHARSET_NAME_ISO_8859_1;
  }

  public ClassLoader getLoader() {
    return this.getClass().getClassLoader();
  }

  public RegisterOptions getOptions() {
    return RegisterOptions.VERSION;
  }

  public String getLocation() {
    if( location == null ) {
      String key = System.getProperty( KEY_SYSTEM_PROPERTY );
      if( key == null ) {
        key = KEY_LOCALHOST;
      }
      location = "http://maps.google.com/maps?file=api&v=2&key=" + key; 
    }
    return location;
  }

  public boolean isJSLibrary() {
    return true;
  }

  public boolean isExternal() {
    return true;
  }
}
