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

import com.w4t.engine.W4TModel;


/** <p>A cache for the classloaders which divide the sessions of a W4 Toolkit 
  * application into namespaces. Caching the classloaders avoids peaks in
  * runtime-consuming tasks for the first access to a session (e.g. loading
  * and instantiating big WebForm classes).</p>
  *
  * <p>The ClassLoaderCache is used by the {@link 
  * com.w4t.engine.W4TModel#W4TModel() constructor of the W4TModel}.</p>
  */
public class ClassLoaderCache {
  
  private final static int MAX_THREADS_SLEEPING = 1;
  
  /** <p>the singleton instance of ClassLoaderCache.</p> */
  private static ClassLoaderCache _instance;
  
  /** <p>the internal data structure of this ClassLoaderCache. Contains 
    * preloaded instances of DelegateClassLoader.</p> */
  private final Buffer buffer;
  /** counts the amount of threads waiting to retrieve a preloaded 
   *  W4TModel instance. */
  private int waitCount;  
  
  
  /** <p>creates a new instance of ClassLoaderCache. Private in order 
    * to ensure singleton pattern.</p> */
  private ClassLoaderCache() {
    buffer = new Buffer();
    waitCount = 0;
  }
  
  /** <p>returns a reference to the singleton instance of 
    * ClassLoaderCache.</p> */
  public static synchronized ClassLoaderCache getInstance() {
    if( _instance == null ) {
      _instance = new ClassLoaderCache();
    }
    return _instance;
  }

  public synchronized W4TModel get() {
    while( buffer.isEmpty() ) {
      waitCount++;
      try {
        wait();
      } catch( InterruptedException shouldNotHappen ) {
        System.out.println( "Exception waiting for a preloaded W4TModel: \n"
                          + shouldNotHappen );
      }   
      waitCount--;
    }
    W4TModel result = buffer.get();
    release();
    return result;
  }
    
  synchronized void release() {
    notifyAll();
  }  

  
  // attribute getters and setters
  ////////////////////////////////
  
  /** <p>returns the current size of the classloader cache, which is the 
    * number of pre-initialised W4TModels that can instantly be 
    * used by new W4 Toolkit application sessions.</p>*/
  public int getSize() {
    return buffer.size();
  }
  
  /** <p>returns the maximal size of the classloader cache, which is the 
    * number of pre-initialised W4TModels that can instantly be 
    * used by new W4 Toolkit application sessions.</p>*/
  public long getMaxSize() {
    return buffer.getMaxSize();
  }
  
  /** <p>returns the currently set minimal size of the classloader cache. 
    * When the size of the the cache reaches this value, it starts to
    * refill automatically.</p> */
  public long getMinThreshold() {
    return buffer.getMinThreshold();
  }
  
  /** returns if there are too many requests waiting to get a preloaded
   *  W4TModel instance. */
  public boolean isBusy() {
    return waitCount > MAX_THREADS_SLEEPING;
  }
}