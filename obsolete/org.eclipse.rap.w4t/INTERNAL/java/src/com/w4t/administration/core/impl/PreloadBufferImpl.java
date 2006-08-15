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
package com.w4t.administration.core.impl;
import com.w4t.administration.core.IPreloadBuffer;
import com.w4t.engine.classloader.ClassLoaderCache;

class PreloadBufferImpl extends ModelElementImpl implements IPreloadBuffer {

  PreloadBufferImpl() {
  }
  
  public String getMinThreshold() {
    return String.valueOf( ClassLoaderCache.getInstance().getMinThreshold() );
  }
  
  public String getSize() {
    return String.valueOf( ClassLoaderCache.getInstance().getSize() );
  }
  
  public String getMaxSize() {
    return String.valueOf( ClassLoaderCache.getInstance().getMaxSize() );
  }
}