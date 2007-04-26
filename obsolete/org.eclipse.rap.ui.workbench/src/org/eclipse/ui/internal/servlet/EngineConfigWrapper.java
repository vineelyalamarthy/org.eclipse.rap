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

package org.eclipse.ui.internal.servlet;

import java.io.*;
import java.lang.reflect.Field;
import java.text.MessageFormat;
import org.eclipse.core.runtime.*;
import org.eclipse.swt.internal.engine.*;
import org.eclipse.swt.internal.lifecycle.EntryPointManager;
import org.eclipse.swt.internal.lifecycle.RWTLifeCycle;
import org.eclipse.swt.resources.IResource;
import org.eclipse.swt.resources.ResourceManager;
import org.eclipse.ui.Activator;
import org.osgi.framework.Bundle;
import com.w4t.engine.lifecycle.LifeCycleFactory;
import com.w4t.engine.lifecycle.PhaseListener;
import com.w4t.engine.service.BrowserSurvey;
import com.w4t.engine.util.EngineConfig;
import com.w4t.engine.util.IEngineConfig;

/**
 * The underlying W4Toolkit runtime engine expects some configuration 
 * infos read by the IEngineConfig implementation. We abuse the 
 * <code>EngineConfigWrapper</code> to fake an appropriate environment
 * for the library.
 */
// TODO: [fappel] clean replacement mechanism that is anchored in W4Toolkit core
final class EngineConfigWrapper implements IEngineConfig {
  
  private final static String FOLDER
    = EngineConfigWrapper.class.getPackage().getName().replace( '.', '/' );
  // path to a w4toolkit configuration file on the classpath
  private final static String CONFIG = FOLDER +"/config.xml";
  //  extension point id for adapter factory registration
  private static final String ID_ADAPTER_FACTORY 
    = "org.eclipse.rap.ui.workbench.adapterfactory";
  //  extension point id for entry point registration
  private static final String ID_ENTRY_POINT
    = "org.eclipse.rap.ui.workbench.entrypoint";
  //  extension point id for phase listener registration
  private static final String ID_PHASE_LISTENER
    = "org.eclipse.rap.ui.workbench.phaselistener";
  //  extension point id for registration of resources (i.e. javascript)
  //  which needed to be loaded at page startup
  private static final String ID_RESOURCES
    = "org.eclipse.rap.ui.workbench.resources";

  private final EngineConfig engineConfig;

  EngineConfigWrapper() {
    engineConfig = new EngineConfig( findContextPath().toString() );    
    registerPhaseListener();
    registerRWTLifeCycle();
    registerResourceManagerFactory();
    registerWorkbenchEntryPoint();
    registerFactories();
    registerResources();
    registerIndexTemplate();
  }

  public File getClassDir() {
    return engineConfig.getClassDir();
  }

  public File getConfigFile() {
    File result = engineConfig.getConfigFile();
    if( !result.exists() ) {
      result.getParentFile().mkdirs();
      try {
        result.createNewFile();
        createConfiguration( result );
      } catch( final IOException shouldNotHappen ) {
        throw new RuntimeException( shouldNotHappen );
      }
    }
    return result;
  }

  public File getLibDir() {
    return engineConfig.getLibDir();
  }

  public File getServerContextDir() {
    return engineConfig.getServerContextDir();
  }

  public File getSourceDir() {
    return engineConfig.getSourceDir();
  }
  
  
  //////////////////
  // helping methods
  
  private static void registerPhaseListener() {
    IExtensionRegistry registry = Platform.getExtensionRegistry();
    IExtensionPoint point = registry.getExtensionPoint( ID_PHASE_LISTENER );
    IConfigurationElement[] elements = point.getConfigurationElements();
    for( int i = 0; i < elements.length; i++ ) {
      try {
        PhaseListener listener 
          = ( PhaseListener )elements[ i ].createExecutableExtension( "class" );
        PhaseListenerRegistry.add( listener );
      } catch( final CoreException ce ) {
        Activator.getDefault().getLog().log( ce.getStatus() );
      }
    }
  }
  
  private static void registerResourceManagerFactory() {
    ResourceManager.register( new ResourceManagerFactory() );
  }

  private static void registerFactories() {
    IExtensionRegistry registry = Platform.getExtensionRegistry();
    IExtensionPoint point = registry.getExtensionPoint( ID_ADAPTER_FACTORY );
    IConfigurationElement[] elements = point.getConfigurationElements();
    for( int i = 0; i < elements.length; i++ ) {
      String contributorName = elements[ i ].getContributor().getName();
      String factoryName = elements[ i ].getAttribute( "factoryClass" );
      String adaptableName = elements[ i ].getAttribute( "adaptableClass" );
      try {
        Bundle bundle = Platform.getBundle( contributorName );
        Class factoryClass = bundle.loadClass( factoryName );
        Class adaptableClass = bundle.loadClass( adaptableName );
        AdapterFactoryRegistry.add( factoryClass, adaptableClass );
      } catch( final Throwable thr ) {
        String text =   "Could not register adapter factory ''{0}'' "
                      + "for the adapter type ''{1}''.";
        Object[] param = new Object[] { factoryName, adaptableName};
        String msg = MessageFormat.format( text, param );
        Status status = new Status( IStatus.ERROR, 
                                    contributorName, 
                                    IStatus.OK, 
                                    msg, 
                                    thr );
        Activator.getDefault().getLog().log( status );  
      }
    }
  }
  
  private static void registerWorkbenchEntryPoint() {
    IExtensionRegistry registry = Platform.getExtensionRegistry();
    IExtensionPoint point = registry.getExtensionPoint( ID_ENTRY_POINT );
    IConfigurationElement[] elements = point.getConfigurationElements();
    for( int i = 0; i < elements.length; i++ ) {
      String contributorName = elements[ i ].getContributor().getName();
      String className = elements[ i ].getAttribute( "class" );
      String parameter = elements[ i ].getAttribute( "parameter" );
      try {
        Bundle bundle = Platform.getBundle( contributorName );
        Class clazz = bundle.loadClass( className );
        EntryPointManager.register( parameter, clazz );
      } catch( final Throwable thr ) {
        String text =   "Could not register entry point ''{0}'' "
                      + "with request startup parameter ''{1}''.";
        Object[] param = new Object[] { className, parameter };
        String msg = MessageFormat.format( text, param );
        Status status = new Status( IStatus.ERROR, 
                                    contributorName, 
                                    IStatus.OK, 
                                    msg, 
                                    thr );
        Activator.getDefault().getLog().log( status );  
      }
    }
  }
  
  private static void registerRWTLifeCycle() {
    // TODO: [fappel] ugly, ugly, ugly - replace this.
    //                Create the only valid lifecycle for RAP
    try {
      Class clazz = LifeCycleFactory.class;
      Field field = clazz.getDeclaredField( "globalLifeCycle" );
      field.setAccessible( true );
      field.set( null, new RWTLifeCycle() );
    } catch( final Throwable shouldNotHappen ) {
      throw new RuntimeException( shouldNotHappen );
    }
  }
  
  // determine a faked context directory
  private static IPath findContextPath() {
    Bundle bundle = Platform.getBundle( Activator.PLUGIN_ID );
    IPath stateLocation = Platform.getStateLocation( bundle );
    return stateLocation.append( "context" );
  }
  
  private static void createConfiguration( final File destination )
    throws FileNotFoundException, IOException
  {
    ClassLoader loader = EngineConfigWrapper.class.getClassLoader();
    InputStream is = loader.getResourceAsStream( CONFIG );
    try {
      OutputStream out = new FileOutputStream( destination );
      try {
        int character = is.read();
        while( character != -1 ) {
          out.write( character );
          character = is.read();
        }
      } finally {
        out.close();
      }
    } finally {
      is.close();
    }
  }
  
  private void registerIndexTemplate() {
    BrowserSurvey.indexTemplate = new IndexTemplate();
  }
  
  private static void registerResources() {
    IExtensionRegistry registry = Platform.getExtensionRegistry();
    IExtensionPoint point = registry.getExtensionPoint( ID_RESOURCES );
    IConfigurationElement[] elements = point.getConfigurationElements();
    for( int i = 0; i < elements.length; i++ ) {
      try {
        IResource resource 
          = ( IResource )elements[ i ].createExecutableExtension( "class" );
        ResourceRegistry.add( resource );
      } catch( final CoreException ce ) {
        Activator.getDefault().getLog().log( ce.getStatus() );
      }
    }
  }

}