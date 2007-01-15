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

package org.eclipse.rap.jface.resource;

import java.net.URL;
import org.eclipse.rap.rwt.graphics.Image;

public abstract class ImageDescriptor {

  protected ImageDescriptor() {
    // do nothing
  }
  
  public static ImageDescriptor createFromURL( final URL url ) {
    ImageDescriptor result = null;
    if( url == null ) {
      result = getMissingImageDescriptor();
    } else {
      result = new URLImageDescriptor( url );
    }
    return result;
  }
  
  public abstract Image createImage();

  private static ImageDescriptor getMissingImageDescriptor() {
    return MissingImageDescriptor.getInstance();
  }
}
