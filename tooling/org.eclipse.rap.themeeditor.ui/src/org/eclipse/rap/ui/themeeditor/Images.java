/*******************************************************************************
 * Copyright (c) 2010 EclipseSource.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     EclipseSource - initial API and implementation
 ******************************************************************************/
package org.eclipse.rap.ui.themeeditor;

import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.osgi.framework.Bundle;


public final class Images {
  
  private static final String ICONS_PATH = "$nl$/icons/"; //$NON-NLS-1$
  private static final String PATH_OBJ = ICONS_PATH + "obj16/"; //$NON-NLS-1$
  
//  public static final ImageDescriptor WARNING 
//    = create( PATH_OBJ + "warn.gif" ); //$NON-NLS-1$
  
  public static final ImageDescriptor RULE 
    = create( PATH_OBJ + "rule.gif" ); //$NON-NLS-1$
  
  private static ImageDescriptor create( final String name ) { 
    Bundle bundle = Activator.getDefault().getBundle();
    IPath path = new Path( name );
    URL imageUrl = FileLocator.find( bundle, path, null );
    return ImageDescriptor.createFromURL( imageUrl );
  }

  private Images() {
    // prevent instantiation
  }
}
