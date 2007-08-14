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
package com.w4t.util;

import java.util.Hashtable;
import com.w4t.types.LocalPath;

/** <p>A utility class to check the existence of files.</p>
  * <p>This class is not intended to be used by clients.</p> 
  */
// TODO [rh] move to org.eclipse.rap.dhtml.renderinfo and make it package-private?
public class ImageRegistry {

  /** the singleton instance of ImageRegistry. */ 
  private static ImageRegistry _instance;
  /** contains Booleans (elements) that indicate of the files named by
    * imageNames (keys) exist. */
  private Hashtable images;
  
  /** constructs the singleton instance of ImageRegistry (lazily). */
  private ImageRegistry() {
    images = new Hashtable();
  }

  /** returns a reference to the singleton instance of ImageRegistry. */
  private static synchronized ImageRegistry getInstance() {
    if( _instance == null ) {
      _instance = new ImageRegistry();
    }
    return _instance; 
  }

  /** <p>returns whether the image file named by imageName exists on the
   * server file system. Results are globally cached, so this is fast.</p> */
  public static boolean check( final String imageName ) {
    return getInstance().checkInternal( imageName );
  }


  // helping methods
  //////////////////

  private boolean checkInternal( final String imageName ) {
    Boolean result = ( Boolean )images.get( imageName );
    if( result == null ) {
      result = new Boolean( new LocalPath( imageName ).toFile().exists() );
      images.put( imageName, result );
    }
    return result.booleanValue();
  } 
}