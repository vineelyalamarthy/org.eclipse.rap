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

import java.io.*;
import java.net.URLClassLoader;
import java.util.Enumeration;
import com.w4t.engine.W4TModel;
import com.w4t.engine.service.ContextProvider;
import com.w4t.engine.service.ServiceContext;
import com.w4t.engine.util.FormManager;
import com.w4t.util.*;


/** <p>A BufferFeederDaemon fills a {@link Buffer Buffer} with 
  * pre-initialised W4TModels.</p>
  */
class BufferFeederDaemon extends Thread implements ResourceListener {

  private static final String FILE_NAME = "w4t_lcf.tmp";
  
  /** the preloading of the classes byte streams should only be done
   *  once per a webapplications lifecycle.*/
  private static boolean classesPreloaded = false;  
  
  /** <p>the Buffer which is filled by this BufferFeederDaemon.</p> */
  private Buffer buffer;
  /** <p>the ClassLoader which loaded this class is also the parent 
    * classloader for all newly created classloaders.</p> */
  private URLClassLoader parentLoader;

  private boolean upToDate = true;
  
  
  /** <p>creates a new instance of BufferFeederThread that is going to 
    * fill the specified buffer.</p> */
  BufferFeederDaemon( final Buffer buffer ) {
    super( "BufferFeederDaemon" );
    this.buffer = buffer;
    parentLoader = ( URLClassLoader )this.getClass().getClassLoader();
    ResourceCache cache = ResourceCache.getInstance();
    cache.addResourceListener( this );
    this.setDaemon( true );
    synchronized( this ) {
      if( !classesPreloaded ) {
        preloadClassesFromList();
      }    
    }
  }
  
  public void run() {        
    try{ 
      if( isOnlyDeamon() ) {
        while( buffer.size() < buffer.getMaxSize() ) {
          buffer.add( createFilledModel() );
          if( !upToDate ) {
            updateBuffer();
          }
        }
        writeClassListToFile();
      }
    } catch( Throwable thr ) {
      System.err.println(   "BufferFeederDaemon terminated unexpectedly.\n"
                          + thr.toString() );
    } finally {
      buffer.setDaemonRunning( false );
      ResourceCache cache = ResourceCache.getInstance();
      cache.removeResourceListener( this );
    }
  }

  /** checks and enshures that only one BufferFeederDeamon is 
   *  running at a time. */
  private boolean isOnlyDeamon() {
    boolean result = false;
    synchronized( buffer ) {
      result = !buffer.isDaemonRunning();
      if( result ) {
        buffer.setDaemonRunning( true );
      }
    }
    return result;
  }
  
  
  // interface methods of ResourceListener
  ////////////////////////////////////////
  
  public void resourceAdded() {
    upToDate = false;
  }

  
  // helping methods
  //////////////////

  private W4TModel createFilledModel() {
    W4TModel result = null;
    HttpSessionImpl session = new HttpSessionImpl();
    Request request = new Request( session );
    Response response = new Response();
    ContextProvider.setContext( new ServiceContext( request, response ) );
    try {
      result = doCreateFilledModel();
    } finally {
      ContextProvider.disposeContext();
    }
    return result;
  }

  private W4TModel doCreateFilledModel() {
    W4TModel result = null;
    DelegateClassLoader dcl 
      = new DelegateClassLoader( parentLoader.getURLs(), parentLoader, true );
    Thread.currentThread().setContextClassLoader( dcl );
    preloadClasses( dcl );
    try {
      Class w4tModelCoreClass = dcl.loadClass( W4TModel.CORE_CLASS );
      result = ( W4TModel )w4tModelCoreClass.newInstance();
    } catch( final Exception ex ) {
      System.out.println( "Could not load or instantiate W4TModel.\n" );
      ex.printStackTrace();
    }
    try {
      loadStartupForm();
    } catch( Exception ex ) {
      System.out.println( ex );
      ex.printStackTrace();
    }
    Thread.currentThread().setContextClassLoader( null );
    return result;
  }  
  
  
  /** <p>loads the startup form into the form list of this W4TModel.</p> */
  private void loadStartupForm() {
    if( usePreloadBuffer() ) {
      String startup = getInitialisationProps().getStartUpForm();
      FormManager.load( startup );
      FormManager.setPreloaded( true );
    }
  }
  
  private boolean usePreloadBuffer() {
    IConfiguration configuration = ConfigurationReader.getConfiguration();
    IPreloaderBuffer preloaderBuffer = configuration.getPreloaderBuffer();
    return preloaderBuffer.isPreloadStartupForm();
  }
  
  private static IInitialization getInitialisationProps() {
    return ConfigurationReader.getConfiguration().getInitialization();
  }
  
  private void preloadClasses( final ClassLoader loader ) {
    String name = "";
    try {
      Enumeration classFileNames = ResourceCache.getInstance().getFileList();
      while( classFileNames.hasMoreElements() ) {
        name = ( String )classFileNames.nextElement();
        Class.forName( name, true, loader );
      }
    } catch( ClassNotFoundException cnfe ) {
      System.out.println(   "Class could not be loaded but is named in "
                          + "resource cache: " + name );
    }
    classesPreloaded = true;
  }
  
  /** reads in the class list file from disk and tries to load the classes
    * named there with a temporary classloader. Since this happens at startup,
    * this will with luck improve the overall performance of new session 
    * initialisations. Whatever exceptions occur will be ignored. */
  private void preloadClassesFromList() {
    DelegateClassLoader dcl
      = new DelegateClassLoader( parentLoader.getURLs(), parentLoader, true );
    File classListFile = null;
    try {
      classListFile = getClassListFile();
    } catch( Exception ex ) {
      System.out.println(   "WARNING: Could not create temporary file for "
                          + "class preload.\nThis may indicate a problem with "
                          + "the setting 'workDirectory' in your WEB-INF/conf/"
                          + "W4T.xml configuration file." );
    }
    if( exists( classListFile ) ) {
      try {
        FileReader fr = new FileReader( classListFile );
        BufferedReader br = new BufferedReader( fr );
        String line = br.readLine();
        while( line != null ) {
          try {
            Class.forName( line.trim(), true, dcl );
          } catch( ClassNotFoundException cnfex ) {
            // ignore it and try the next one
          }
          line = br.readLine();
        }
      } catch( IOException ioex ) {
        // we ignore, classes will be loaded anyway, this is just a preload 
        // for performance, so nothing critical will probably arise
      }
      updateBuffer();
    }
  }
  
  private void updateBuffer() {
    upToDate = true;
    synchronized( buffer ) {
      W4TModel[] models = buffer.getAll();
      for( int i = 0; i < models.length; i++ ) {
        ClassLoader dcl = models[ i ].getClass().getClassLoader();
        preloadClasses( dcl );
      }
    }
  }
  
  private void writeClassListToFile() throws Exception {
    if( isDynamicFileList() ) {
      try {
        File classListFile = getClassListFile();
        PrintWriter fw = new PrintWriter( new FileWriter( classListFile ) );
        Enumeration classFileNames = ResourceCache.getInstance().getFileList();
        while( classFileNames.hasMoreElements() ) {
          fw.println( ( String )classFileNames.nextElement() );
        }
        fw.close();
      } catch( IOException ioex ) {
        System.out.println(   "Could not write temporary file to " 
                            + getWorkDir() + "\n"
                            + "Note: this is probably not harmful, but may "
                            + "result in longer startup time for the "
                            + "W4 Toolkit library initialisations.\n" );
      }
    }
  }
  
  private boolean isDynamicFileList() {
    IConfiguration configuration = ConfigurationReader.getConfiguration();
    IPreloaderBuffer preloaderBuffer = configuration.getPreloaderBuffer();
    String preloadList = preloaderBuffer.getPreloadList();
    return preloadList.equals( IPreloaderBuffer.PRELOAD_LIST_DYNAMIC );
  }
  
  private File getClassListFile() throws Exception {
    return new File( getWorkDir() + FILE_NAME );
  }
  
  private String getWorkDir() throws Exception {
    IConfiguration configuration = ConfigurationReader.getConfiguration();
    return configuration.getInitialization().getWorkDirectory();
  }
  
  private boolean exists( File file ) {
    return file != null && file.exists();
  }  
}