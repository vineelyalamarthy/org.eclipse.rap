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

import org.eclipse.rwt.internal.browser.Browser;

import com.w4t.*;


/** 
 * <p>Provides <code>Renderer</code>s for <code>WebComponents</code> using a 
 * cache.</p>
 */
public class RendererCache {

  /** the internal datastructure of the RendererCache */
  private final Map cache;
  private final Map keyCache;
  /** the singleton instance of RendererCache. */
  private static RendererCache _instance;

  
  /** <p>Creates a new instance of RendererCache; private to ensure the 
    * singleton instance.</p> */
  private RendererCache() {
    cache = new HashMap();
    keyCache = new HashMap();
  }
  
  /** <p>returns a reference to the singleton instance of
   * <code>RendererCache</code>.</p> */
  public static synchronized RendererCache getInstance() {
    if( _instance == null ) {
      _instance = new RendererCache();
    }
    return _instance;
  }
    
  public String getRendererClass( final Class componentClass ) {
    return getRendererClass( componentClass, W4TContext.getBrowser() );
  }
  
  /** <p>Returns the class name (a superclass of <code>Renderer</code>) which is
   * responsible for rendering the given <code>componentClass</code>.</p>
   * <p>The browser capabilities are determined from the current browser
   * (<code>W4TContext#getBrowser()</code>).</p>
   * @return  the class name of the components' renderer or <code>null</code>
   * if the renderer classname could not be determined. 
   */
  public synchronized String getRendererClass( final Class componentClass,
                                               final Browser browser ) 
  {
    String rendererKey = createRendererKey( componentClass, browser );    
    RendererClassCache rcc = RendererClassCache.getInstance();
    String rendererClassName = rcc.getRendererName( rendererKey );
    if ( rendererClassName == null ) {
      try {
        Class rendererClass = loadRendererClass( componentClass, browser );
        rendererClassName = rendererClass.getName();
        rcc.addRendererName( rendererKey, rendererClassName );
      } catch( final Exception e ) {
      }
    }
    return rendererClassName;
  }
  
  /**
   * <p>Returns a <code>Renderer</code> object for the given 
   * <code>componentClass</code> and the <em>current</em> (obtained by 
   * {@link org.eclipse.rwt.W4TContext#getBrowser()
   * <code>W4TContext.getBrowser()</code>}) browser.</p>
   * <p>Invoking this method is equivalent to 
   * {@link #retrieveRenderer(Class, Browser) 
   * <code>retrieveRenderer(componentClass, W4TContext.getBrowser());</code>}
   */
  public Renderer retrieveRenderer( final Class componentClass ) {
    return retrieveRenderer( componentClass, W4TContext.getBrowser() );
  }
  
  /** <p>Returns a renderer object the given browser for a WebComponent of the 
    * specified class.</p> */
  public synchronized Renderer retrieveRenderer( final Class componentClass, 
                                                 final Browser browser ) 
  {    
    Renderer result = null;
    String rendererKey = createRendererKey( componentClass, browser );
    if( cache.containsKey( rendererKey ) ) {
      result = ( Renderer )cache.get( rendererKey );
    } else {
      RendererClassCache rcc = RendererClassCache.getInstance();
      String rendererClassName = rcc.getRendererName( rendererKey );
      Class rendererClass = null;
      if( rendererClassName == null ) {
        rendererClass = loadRendererClass( componentClass, browser );
        rcc.addRendererName( rendererKey, rendererClass.getName() );
      } else {
        try {
          ClassLoader loader = componentClass.getClassLoader();
          rendererClass = loader.loadClass( rendererClassName );
        } catch( final ClassNotFoundException cnfe ) {
          // TODO: [fappel] improve Exception handling
          String msg =   "Could not find renderer class '"
                       + rendererClassName 
                       + "' for component '" 
                       + componentClass.getName() 
                       + "'. Detected Browser: " 
                       + browser.getClass().getName();
          throw new IllegalStateException( msg );
        }
      }
      try {
        result = ( Renderer )rendererClass.newInstance();
      } catch( final Exception e ) {
        // TODO: [fappel] improve Exception handling
        String msg =   "Could not load renderer class '"
                     + rendererClassName 
                     + "' for component '" 
                     + componentClass.getName() 
                     + "'. Detected Browser: " 
                     + browser.getClass().getName();
        throw new IllegalStateException( msg );
      }      
      cache.put( rendererKey, result );
    }
    return result;
  }

  
  // helping methods
  //////////////////
  
  private Class loadRendererClass( final Class clazz, final Browser browser ) {
    Class result = null;
    RendererDescriptor descriptor = new RendererDescriptor( clazz, browser );
    if( clazz.equals( WebComponent.class ) ) {
      result = Renderer.class;
    } else {
      boolean found = false;
      boolean hasPredecessor;
      do {
        hasPredecessor = false;
        try {
          String toString = descriptor.toString();
          ClassLoader loader = clazz.getClassLoader();
          result = loader.loadClass( toString );
          found = true;
        } catch( Exception e ) {
          hasPredecessor = descriptor.useBrowserPredecessor();
        }
      } while( !found && hasPredecessor );
      if( result == null ) {
        result = loadRendererClass( clazz.getSuperclass(), browser );
      }
    }
    return result;
  }  
    
  private String createRendererKey( final Class componentClass, 
                                    final Browser browser ) 
  {
    // for perfomance reasons we buffer the created keys since the
    // StringBuffer-concatenation showed up in yourkit-profiler.
    String renderMode = determineRendererMode( browser );
    Map components = ( Map )keyCache.get( componentClass.getName() );
    if( components == null ) {
      components = new HashMap();
      keyCache.put( componentClass.getName(), components );
    }
    
    String browserName = browser.getClass().getName();
    Map browsers = ( Map )components.get( browserName );
    if( browsers == null ) {
      // Since we know that there are only 3 modes (currently) we can set
      // the exact size of the Map - note that we must use '4' since adding
      // the third element causes the capacity to be increased
      browsers = new HashMap( 4, 1f );
      components.put( browserName, browsers );
    }
    
    String result = ( String )browsers.get( renderMode );
    if( result == null ) {
      StringBuffer buffer = new StringBuffer();
      buffer.append( componentClass.getName() );
      buffer.append( "_" );
      buffer.append( browser.toString() );
      buffer.append( "_" );
      buffer.append( renderMode );
      result = buffer.toString();
      browsers.put( renderMode, result );
    }          
    return result;
  }

  private String determineRendererMode( final Browser browser ) {
    String result;
    if( browser.isAjaxEnabled() ) {
      result = "ajax";
    } else if( browser.isScriptEnabled() ) {
      result = "script";
    } else {
      result = "noscript";
    }
    return result;
  }
}