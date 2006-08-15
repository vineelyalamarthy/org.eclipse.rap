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
package com.w4t.engine.classloader;

import java.util.*;
import sun.misc.Resource;


/** <p>Resource cache, used by DelegateClassLoader and BufferFeederDaemon.</p>
  * @see com.w4t.engine.classloader.DelegateClassLoader DelegateClassLoader
  * @see com.w4t.engine.classloader.BufferFeederDaemon BufferFeederDaemon
  */
final class ResourceCache {
  
  /** <p>the singleton instance of ResourceCache.</p> */
  private static ResourceCache _instance;
  
  /** contains Resources (elements) by their paths (keys). */
  private Hashtable resourceCache;
  /** contains files as byte arrays (elements) by their file names (keys). */
  private Hashtable byteCache;
  /** <p>listeners that are notified when a new byte stream resource was 
    * added to this ResourceCache.</p> */
  private Vector resourceListeners;
  
  
  /** <p>creates a new instance of ResourceCache; private in order to ensure 
    * the singleton pattern.</p> */
  private ResourceCache() {
    resourceCache = new Hashtable();
    byteCache = new Hashtable();
    resourceListeners = new Vector();
  }
  
  /** <p>returns a reference to the singleton instance of ResourceCache.</p> */
  static synchronized ResourceCache getInstance() {
    if( _instance == null ) {
      _instance = new ResourceCache();
    }
    return _instance;
  }
  
  void addResource( final String path, final Resource resource ) {
    resourceCache.put( path, resource );    
  }
  
  Resource findResource( final String path ) {
    return ( Resource )resourceCache.get( path );
  }
  
  void addByteArray( final String fileName, final byte[] bytes ) {
    byteCache.put( fileName, bytes );
    notifyListeners();
  }
  
  byte[] findByteArray( final String fileName ) {
    return ( byte[] )byteCache.get( fileName );
  }
  
  Enumeration getFileList() {
    return byteCache.keys();
  }
  
  /** <p>adds the specified listener which is to be notified when a new 
    * byte stream rewsource is added to this ResourceCache.</p> */
  void addResourceListener( final ResourceListener rl ) {
    resourceListeners.add( rl );
  }

  /** <p>removes the specified listener which is no longer notified when 
    * a new byte stream rewsource is added to this ResourceCache.</p> */
  void removeResourceListener( final ResourceListener rl ) {
    resourceListeners.remove( rl );
  }
  
  private void notifyListeners() {
    for( int i = 0; i < resourceListeners.size(); i++ ) {
      ResourceListener rl = ( ResourceListener )resourceListeners.get( i );
      rl.resourceAdded();
    }
  }
}