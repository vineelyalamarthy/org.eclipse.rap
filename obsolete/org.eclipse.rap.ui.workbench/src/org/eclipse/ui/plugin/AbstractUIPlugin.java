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

package org.eclipse.ui.plugin;

import java.net.MalformedURLException;
import java.net.URL;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.internal.util.BundleUtility;
import org.osgi.framework.Bundle;


public abstract class AbstractUIPlugin extends Plugin {

  public static ImageDescriptor imageDescriptorFromPlugin( 
    final String pluginId,
    final String imageFilePath )
  {
    if( pluginId == null || imageFilePath == null ) {
      throw new IllegalArgumentException();
    }
    // if the bundle is not ready then there is no image
    Bundle bundle = Platform.getBundle( pluginId );
    if( !BundleUtility.isReady( bundle ) ) {
      return null;
    }
    // look for the image (this will check both the plugin and fragment folders
    URL fullPathString = BundleUtility.find( bundle, imageFilePath );
    if( fullPathString == null ) {
      try {
        fullPathString = new URL( imageFilePath );
      } catch( MalformedURLException e ) {
        return null;
      }
    }
    if( fullPathString == null ) {
      return null;
    }
    return ImageDescriptor.createFromURL( fullPathString );
  }
}
