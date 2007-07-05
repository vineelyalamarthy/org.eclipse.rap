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

import java.io.File;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import com.w4t.engine.service.ContextProvider;

/** <p>Manages the server context directories. This is especially important
 * when run in modes where resources from different context directories must
 * be merged.</p>
 * 
 * Singleton
 * Global classloader namespace
 */
// TODO [rh] JavaDoc: update needed?
public class ContextDirManager {
  
  /** <p>the singleton instance of ContextDirManager.</p> */
  private static ContextDirManager _instance;
  
  private File additionalRoot;
  
  /** <p>constructs the singleton instance von ContextDirManager.</p> */
  private ContextDirManager() {
    HttpServletRequest request = ContextProvider.getRequest();
    ServletContext context = request.getSession().getServletContext(); 
    additionalRoot = ( File )context.getAttribute( "w4t_add_root" );
  }
  
  /** <p>returns a reference to the singleton instance of 
   * ContextDirManager.</p> */
  public synchronized static ContextDirManager getInstance() {
    if( _instance == null ) {
      _instance = new ContextDirManager();
    }
    return _instance;
  }
  
  /** <p>returns the absolute path for the passed relative file name on the
   * actual file system on the server. This will consist of the current 
   * server context directory plus the specified path, if no additional 
   * root is set. If an additional root is set, the latter will be used 
   * instead.</p> */
  public File getAbsolutePath( final String relativePath ) {
    String context 
      = ConfigurationReader.getEngineConfig().getServerContextDir().toString();
    File result = new File( context + File.separator + relativePath );
    if( additionalRoot != null ) {
      File file = new File(   additionalRoot.toString() 
                            + File.separator 
                            + relativePath ); 
      if( file.exists() ) {
        result = file;
      }
    }
    return result;
  } 
  
  /** <p>returns the additional context root if exist, null otherwise.</p> */
  public File getAdditionalRoot() {
    return additionalRoot;
  }
}