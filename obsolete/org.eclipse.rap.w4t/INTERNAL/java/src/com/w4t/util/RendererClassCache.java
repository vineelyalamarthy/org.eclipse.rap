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

import java.util.HashMap;
import java.util.Map;

/**
 * <p>Actual cache that maps 'known' renderer classes for their respective
 * component and browser.</p>
 */
public class RendererClassCache {
  
  /** <p>the singleton instance of <code>RendererClassCache</code>.</p> */
  private static RendererClassCache _instance;
  
  /** <p>returns a reference to the singleton instance of 
   * RendererClassCache.</p> */
 public static synchronized RendererClassCache getInstance() {
   if( _instance == null ) {
     _instance = new RendererClassCache();
   }
   return _instance;
 }

  /** <p>contains information about the proper renderers (classnames 
   * as elements) for components / browsers (keys).</p> */
  private Map renderers;
 
  /** <p>creates a new instance of RendererInstanceCache. Private in order 
   * to ensure singleton pattern.</p> */
  private RendererClassCache() {
    renderers = new HashMap();
  }

  /** <p>Adds information about the proper renderer class for a 
   * specified component and browser to this global cache.</p>
   *
   * @param key          consists of component name and name of the browser, 
   *                     for which the renderer works
   * @param rendererName the class name of the renderer
   */
  public synchronized void addRendererName( final String key, 
                                            final String rendererName )
  {
    renderers.put( key, rendererName );
  }

  /** <p>returns information about the proper renderer class for a 
   * specified component and browser.</p>
   *
   * @param key          consists of component name and name of the browser, 
   *                     for which the renderer works
   */
  public String getRendererName( final String key ) {
    return ( String )renderers.get( key );
  }
}
