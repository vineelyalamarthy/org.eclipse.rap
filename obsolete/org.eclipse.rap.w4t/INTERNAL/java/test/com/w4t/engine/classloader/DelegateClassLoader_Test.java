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

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.net.*;
import java.util.List;
import junit.framework.TestCase;
import com.w4t.Fixture;
import com.w4t.engine.util.IEngineConfig;
import com.w4t.engine.util.WebAppURLs;
import com.w4t.util.ConfigurationReader;


public class DelegateClassLoader_Test extends TestCase {

  private final static String TEST_CLASS_FIRST_LEVEL 
    = "com.w4t.WebComponentStatistics";
  private final static String TEST_CLASS_SECOND_LEVEL
    = "com.w4t.WebComponent";
  private final static String TEST_JAR_GLOBAL_LOADING 
    =   Fixture.getWebAppBase().toString() 
      + "/INTERNAL/lib/classloader_globalloading_test.jar";
  private final static String TEST_JAR_NAME_SPACE 
    =   Fixture.getWebAppBase().toString() 
      + "/INTERNAL/lib/classloader_namespace_test.jar";

  private static URLClassLoader contextLoader;
  private static URLClassLoader firstLevelLoader;
  private static URLClassLoader secondLevelLoader;
  private static ClassLoader mainLoader 
    = DelegateClassLoader_Test.class.getClassLoader(); 

  public DelegateClassLoader_Test( final String name ) {
    super( name );
  }
  
  
  public void testDefaultClassesExclusions() throws Exception {
    String[] list = DelegateClassLoader.EXCLUDE_LOADING_CLASSES_LIST;
    for( int i = 0; i < list.length; i++ ) {
      doExclusionCheck( list[ i ] );
    }
  }
  
  public void testLoadingClassesExclusions() throws Exception {
    doExclusionCheck( "classloadertest.GlobalLoadingDemoClass" );
    String name = "classloadertest.globalnamespace.GlobalNameSpaceDemoClass";
    doExclusionCheck( name );
    Class clazz 
      = secondLevelLoader.loadClass( "classloadertest.LocalLoadingDemoClass" );
    String msg = "Local loading of " + clazz.getName() + " failed";
    assertTrue( msg, clazz.getClassLoader() == secondLevelLoader );
  }
  
  public void testLoadingNameSpaceExclusions() throws Exception {
    // java and javax
    doExclusionCheck( "java.applet.Applet" );
    // org.xml
    doExclusionCheck( "org.xml.sax.XMLReader" );
    // org.w3c
    doExclusionCheck( "org.w3c.dom.Document" );
  }
  
  public void testFirstLevelLoading() throws Exception {
    Class clazz = firstLevelLoader.loadClass( TEST_CLASS_FIRST_LEVEL );
    String msg = "Class not loaded with firstLevelLoader!";
    assertTrue( msg, clazz.getClassLoader() == firstLevelLoader );
    clazz = secondLevelLoader.loadClass( TEST_CLASS_FIRST_LEVEL );
    assertTrue( msg, clazz.getClassLoader() == firstLevelLoader );
  }
  
  public void testSecondLevelLoading() throws Exception {
    Class class2Level1 = secondLevelLoader.loadClass( TEST_CLASS_SECOND_LEVEL );
    String msg = "Class not loaded with secondLevelLoader";
    assertTrue( msg, class2Level1.getClassLoader() == secondLevelLoader );
    URLClassLoader newLoader = createSecondLevelLoader( firstLevelLoader );
    Class class2Level2 = newLoader.loadClass( TEST_CLASS_SECOND_LEVEL );
    msg = "Class objects must be different!";
    assertTrue( msg, class2Level1 != class2Level2 );
    Class class1Level = firstLevelLoader.loadClass( TEST_CLASS_SECOND_LEVEL );
    msg = "Class objects must be different!";
    assertTrue( msg, class2Level1 != class1Level );
  }
  
  public void testExcludeManager() throws Exception {
    String[] list1 
      = ExcludeManager.getExclusionList( getWebAppURLs(), contextLoader );
    String[] list2 
      = ExcludeManager.getExclusionList( getWebAppURLs(), firstLevelLoader );
    String[] list3 
      = ExcludeManager.getExclusionList( getWebAppURLs(), secondLevelLoader );
    assertTrue( "different list instances as result", list1 == list2 );
    assertTrue( "different list instances as result", list1 == list3 );
  }
  
  public void testClassLoaderDeactivation() throws Exception {
    File w4tConf = new File( Fixture.TEMP_DIR, "w4t_deactivate.xml" );
    Fixture.copyTestResource( "resources/w4t_deactivate.xml",  w4tConf );
    try {
      ConfigurationReader.setConfigurationFile( w4tConf );
      Class class2Level1
        = secondLevelLoader.loadClass( TEST_CLASS_SECOND_LEVEL );
      String msg = "Class must not be loaded with delegate loader";
      assertTrue( msg,    class2Level1.getClassLoader() != firstLevelLoader
                       && class2Level1.getClassLoader() != secondLevelLoader );
    } finally {
      if( w4tConf.exists() ) {
        w4tConf.delete();
      }
      ConfigurationReader.setConfigurationFile( null );
    }
  }

  protected void setUp() throws Exception {
    File w4tConf = new File( Fixture.TEMP_DIR, "w4t_classloader.xml" );
    Fixture.copyTestResource( "resources/w4t_classloader.xml",  w4tConf );

    ConfigurationReader.setConfigurationFile( w4tConf );
    // reset the the global classes list
    String name = "excludeLoadingClassList";
    Field field = ExcludeManager.class.getDeclaredField( name );
    field.setAccessible( true );
    field.set( ExcludeManager.class, null );
    contextLoader = createContextLoader();
    firstLevelLoader = createFirstLevelLoader( contextLoader );
    secondLevelLoader = createSecondLevelLoader( firstLevelLoader );
  }
  
  protected void tearDown() throws Exception {
    contextLoader = null;
    firstLevelLoader = null;
    secondLevelLoader = null;
  }
  
  //////////////////
  // helping methods


  private void doExclusionCheck( final String className ) 
    throws ClassNotFoundException 
  {
    doExclusionCheck( className, secondLevelLoader );   
    doExclusionCheck( className, firstLevelLoader );
    doExclusionCheck( className, contextLoader );
  }
  
  private void doExclusionCheck( final String className, 
                                 final ClassLoader loader ) 
    throws ClassNotFoundException 
  {
    Class clazz = loader.loadClass( className );
    String classLoaderName = getClassLoaderName( clazz );
    String msg =   className 
                 + " not loaded with system, main or context loader:" 
                 + classLoaderName;
    ClassLoader realLoader = clazz.getClassLoader();
    // realLoader == null -> system classLoader
    boolean valid =    realLoader == null 
                    || realLoader == mainLoader 
                    || realLoader == contextLoader;
    assertTrue( msg, valid );
  }
  
  private String getClassLoaderName( final Class clazz ) {
    ClassLoader classLoader = clazz.getClassLoader();
    String result = "SystemLoader";
    if( classLoader != null ) {
      result = classLoader.getClass().getName();
    }
    return result;
  }

  private static URLClassLoader createContextLoader() 
    throws MalformedURLException 
  {
    return new URLClassLoader( getWebAppURLs() );
  }
  
  private static URLClassLoader createFirstLevelLoader( 
                                          final URLClassLoader contextLoader ) 
    throws ClassNotFoundException 
  {    
    URL[] urls = contextLoader.getURLs();
    DelegateClassLoader result 
      = new DelegateClassLoader( urls , contextLoader, false );
    result.loadClass( TEST_CLASS_FIRST_LEVEL );
    return result;
  }
  
  private static URLClassLoader createSecondLevelLoader( 
                                            final URLClassLoader parent ) 
    throws ClassNotFoundException
  {
    String loaderName = "com.w4t.engine.classloader.DelegateClassLoader";
    Class loaderClazz = parent.loadClass( loaderName );
    Object[] params = new Object[] { 
      parent.getURLs(), 
      parent, 
      Boolean.FALSE };
    Constructor[] constructors = loaderClazz.getConstructors();

    URLClassLoader result = null;
    for( int i = 0; result == null && i < constructors.length; i++ ) {
      try {
        result = ( URLClassLoader )constructors[ i ].newInstance( params );
      } catch( Exception ignore ) {
      }
    }
    return result; 
  }
  
  private static URL[] getWebAppURLs() throws MalformedURLException {
    List urls = WebAppURLs.getWebAppURLs( new EngineConfig() );
    urls.add( new File( TEST_JAR_GLOBAL_LOADING ).toURL() );
    urls.add( new File( TEST_JAR_NAME_SPACE ).toURL() );
    URL[] result = new URL[ urls.size() ];
    urls.toArray( result );
    return result;
  }
  
  
  ////////////////
  // inner classes
  
  private final static class EngineConfig implements IEngineConfig {

    public File getConfigFile() {
      return null;
    }

    public File getLibDir() {
      return new File( Fixture.getWebAppBase() + "/" + "WEB-INF/lib" );
    }

    public File getServerContextDir() {
      return null;
    }

    public File getClassDir() {
      return new File( Fixture.getWebAppBase() + "/" + "WEB-INF/classes" );
    }

    public File getSourceDir() {
      return null;
    }

    public File getLicenseDir() {
      return null;
    }
  }
}