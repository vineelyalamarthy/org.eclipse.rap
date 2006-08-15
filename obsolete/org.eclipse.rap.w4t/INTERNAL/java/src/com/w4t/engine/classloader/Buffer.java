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

import java.util.ArrayList;
import com.w4t.engine.W4TModel;
import com.w4t.util.ConfigurationReader;
import com.w4t.util.IConfiguration;


/** <p>Encapsulates a list of W4TModels which is used as 
  * dynamic buffer.</p>
  */
class Buffer {
    
  /** <p>the internal data structure of this Buffer.</p> */
  private ArrayList models;
  /** <p>whether a BufferFeederDaemon is running and fills this Buffer 
    * with pre-initialised W4TModels.<p> */
  private boolean daemonRunning = false;
  
  
  /** <p>creates a new instance of Buffer.</p> */
  Buffer() {
    models = new ArrayList();
  }

  /** adds the passed W4TModel to this Buffer. */
  synchronized void add( final W4TModel model ) {
    models.add( model );
    ClassLoaderCache.getInstance().release();
  }

  /** <p>returns a pre-initialised W4TModel and removes it 
    * from this Buffer. It is assumed that at least one W4TModel
    * is in fact contained in this Buffer.</p> */
  synchronized W4TModel get() {
    W4TModel result = ( W4TModel )models.remove( 0 );
    if( size() <= getMinThreshold() && !daemonRunning ) {
      new BufferFeederDaemon( this ).start();
    }
    return result;
  }
  
  /** <p>returns the number of pre-initialised W4TModels 
    * currently contained in this Buffer.</p> */
  synchronized int size() {
    return models.size();
  }

  /** <p>returns all W4TModels contained in this Buffer.</p> */
  synchronized W4TModel[] getAll() {
    W4TModel[] result = new W4TModel[ size() ];
    models.toArray( result );
    return result;
  }
  
  // attribute getters and setters
  ////////////////////////////////

  long getMinThreshold() {
    IConfiguration configuration = ConfigurationReader.getConfiguration();
    return configuration.getPreloaderBuffer().getMinThreshold();
  }
  
  long getMaxSize() {
    IConfiguration configuration = ConfigurationReader.getConfiguration();
    return configuration.getPreloaderBuffer().getMaxSize();
  }
  
  boolean isEmpty() {
    boolean result = size() == 0;
    if( result && !daemonRunning ) {
      new BufferFeederDaemon( this ).start();
    }
    return result;
  }
  
  synchronized void setDaemonRunning( final boolean daemonRunning ) {
    this.daemonRunning = daemonRunning;
  }
  
  boolean isDaemonRunning() {
    return daemonRunning;
  }
}