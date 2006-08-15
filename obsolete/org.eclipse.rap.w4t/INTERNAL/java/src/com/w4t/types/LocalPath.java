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
package com.w4t.types;

import java.io.File;
import com.w4t.util.ContextDirManager;

/** <p>A LocalPath represents a file or directory on the local file system 
  * (that is, the file system of the machine where the server that runs the 
  * W4 Toolkit application resides, not the file system of the client that 
  * runs the browser).</p>
  */
public class LocalPath extends WebPropertyBase {
  
  /**
   * <p>Creates a <code>LocalPath</code> object which represents the given
   * <code>path</code>.</p>
   */
  public LocalPath( final String path ) {
    super( path );
  }

  /**
   * <p>Returns a <code>File</code> object which represents the <em>absolute</em> 
   * path to the file or directory that was passed to the constructor.</p>
   * <p>An eventually relative path is returned as an absolute path by
   * prefixing it with the context root directory.</p>
   */
  public File toFile() {
    File result = new File( value );
    if( !result.isAbsolute() ) {
      result = ContextDirManager.getInstance().getAbsolutePath( toString() );
    }
    return result;
  }
  
  /**
   * <p>Returns whether this <code>LocalPath</code> represents an absolute
   * path.</p> 
   */
  public boolean isAbsolute() {
    return new File( value ).isAbsolute();
  } 
}