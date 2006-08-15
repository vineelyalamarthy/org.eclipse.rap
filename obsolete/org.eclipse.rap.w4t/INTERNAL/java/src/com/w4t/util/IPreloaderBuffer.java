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

/**
 * <p>To speed up the time for loading a new session W4 Toolkit provides
 * a session preloading mechanism. The <code>IPreloaderBuffer</code>
 * defines the available settings if this mechanism is activated.</p>
 * 
 * <p>Note that the <code>ClassLoader</code> architecture of W4 Toolkit
 * was originally designed to provide the possibility of session local 
 * singletons via class variables. In the meantime though W4 Toolkit provides
 * a different approach for 
 * {@link com.w4t.SessionSingletonBase session singletons} that needs less 
 * memory and causes less problems with third party libraries.</p>
 * <p>This interface is not intended to be implemented by clients.</p>
 * @see com.w4t.util.IConfiguration
 */
public interface IPreloaderBuffer {

  public final static String PRELOAD_LIST_FIX = "fix";
  public final static String PRELOAD_LIST_DYNAMIC = "dynamic";

  /** 
   * returns whether the PreloaderBuffer is used. If set to 
   * 'true' the library will preload all classes needed for starting up a
   * new session with its own classloader and buffer it. If a new session 
   * is requested, the buffered session content is handed to the new session. 
   * This reduces  the libraries startup time for a new session. If set to 
   * 'false' the session is initialised on demand.
   */
  boolean isUsePreloaderBuffer();

  /** 
   * returns the maximum amount of preloaded sessions. Note: Be careful 
   * with the size, because  preloaded sessions consume memory.
   */
  long getMaxSize();

  /**
   * returns the minimum amount of preloaded sessions in the cache. If 
   * this amount is undercut the cache will be filled up to its maximum.
   */
  long getMinThreshold();

  /**
   * returns whether an instance of the StartupFormluar 
   * {@link IInitialization#getStartUpForm()} is preloaded. Setting 
   * this to 'true' will speed up the request answering time of a newly 
   * created session.
   */
  boolean isPreloadStartupForm();

  /**
   * returns whether classes are preloaded from a fixed size list or a 
   * dynamically growing list. This list is located in the webapplications
   * {@link IInitialization#getWorkDirectory() working directory }on hard
   * disk (w4t_lcf.tmp). The classes to preload are specified with their 
   * fully qualified classname seperated by linefeeds.
   * <ul>
   * <li><code>PRELOAD_LIST_FIX</code><br>
   * the list is not changed during the lifecycle of a W4Toolkit 
   * webapplication.</li>
   * <li><code>PRELOAD_LIST_DYNAMIC</code><br>
   * the list grows dynamically with classes loaded
   * the first time during the lifecycle of a W4Toolkit
   * application. The list grows over multiple lifecycles.</li></ul>                       
   */
  String getPreloadList();
}
