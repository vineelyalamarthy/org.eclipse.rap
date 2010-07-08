/******************************************************************************* 
* Copyright (c) 2010 EclipseSource and others. All rights reserved. This
* program and the accompanying materials are made available under the terms of
* the Eclipse Public License v1.0 which accompanies this distribution, and is
* available at http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*   EclipseSource - initial API and implementation
*******************************************************************************/ 
package org.eclipse.rap.warproducts.ui.newwizard;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.pde.core.plugin.TargetPlatform;
import org.eclipse.pde.internal.core.iproduct.IArgumentsInfo;
import org.eclipse.pde.internal.core.iproduct.IConfigurationFileInfo;
import org.eclipse.pde.internal.core.iproduct.IJREInfo;
import org.eclipse.pde.internal.core.iproduct.IProduct;
import org.eclipse.pde.internal.core.iproduct.IProductModelFactory;
import org.eclipse.pde.internal.launching.launcher.BundleLauncherHelper;
import org.eclipse.pde.internal.ui.PDEPlugin;
import org.eclipse.pde.launching.IPDELauncherConstants;
import org.eclipse.rap.warproducts.core.InfrastructreCreator;


public class WARProductFromConfigOperation extends BaseWARProductCreationOperation {

  private IContainer productParent;
  private ILaunchConfiguration launchConfig;

  public WARProductFromConfigOperation( final IFile file, 
                                        final ILaunchConfiguration config )
  {
    super( file );  
    productParent = file.getParent();
    this.launchConfig = config;
  }
  
  protected void initializeProduct( final IProduct product ) {
    internalInitializeProduct( product );
    InfrastructreCreator creator = new InfrastructreCreator( productParent );
    creator.createWebInf();
    creator.createLaunchIni();
    creator.createWebXml();
  }
  
  /*
   * (non-Javadoc)
   * @see
   * org.eclipse.pde.internal.ui.wizards.product.BaseProductCreationOperation
   * #initializeProduct(org.eclipse.pde.internal.core.iproduct.IProduct)
   */
  protected void internalInitializeProduct( final IProduct product ) {
    if( launchConfig == null )
      return;
    try {
      IProductModelFactory factory = product.getModel().getFactory();
      boolean useProduct = launchConfig.getAttribute( IPDELauncherConstants.USE_PRODUCT,
                                                      false );
      if( useProduct ) {
        String id = launchConfig.getAttribute( IPDELauncherConstants.PRODUCT,
                                               ( String )null );
        if( id != null ) {
          initializeProductInfo( factory, product, id );
        }
      } else {
        String appName = launchConfig.getAttribute( IPDELauncherConstants.APPLICATION,
                                                    TargetPlatform.getDefaultApplication() );
        product.setApplication( appName );
      }
      // Set JRE info from information from the launch configuration
      String jreString = launchConfig.getAttribute( IJavaLaunchConfigurationConstants.ATTR_JRE_CONTAINER_PATH,
                                                    ( String )null );
      if( jreString != null ) {
        IPath jreContainerPath = new Path( jreString );
        IJREInfo jreInfo = product.getJREInfo();
        if( jreInfo == null ) {
          jreInfo = product.getModel().getFactory().createJVMInfo();
        }
        jreInfo.setJREContainerPath( TargetPlatform.getOS(), jreContainerPath );
        product.setJREInfo( jreInfo );
      }
      // fetch the plug-ins models
      String workspaceId = IPDELauncherConstants.SELECTED_WORKSPACE_PLUGINS;
      String targetId = IPDELauncherConstants.SELECTED_TARGET_PLUGINS;
      if( launchConfig.getType()
        .getIdentifier()
        .equals( IPDELauncherConstants.OSGI_CONFIGURATION_TYPE ) )
      {
        workspaceId = IPDELauncherConstants.WORKSPACE_BUNDLES;
        targetId = IPDELauncherConstants.TARGET_BUNDLES;
      }
      Set set = new HashSet();
      Map map = BundleLauncherHelper.getWorkspaceBundleMap( launchConfig,
                                                            set,
                                                            workspaceId );
      map.putAll( BundleLauncherHelper.getTargetBundleMap( launchConfig,
                                                           set,
                                                           targetId ) );
      addPlugins( factory, product, map );
      if( launchConfig.getAttribute( IPDELauncherConstants.CONFIG_GENERATE_DEFAULT,
                                     true ) )
      {
        super.initializeProduct( product );
      } else {
        String path = launchConfig.getAttribute( IPDELauncherConstants.CONFIG_TEMPLATE_LOCATION,
                                                 "/" ); //$NON-NLS-1$
        IContainer container = PDEPlugin.getWorkspace()
          .getRoot()
          .getContainerForLocation( new Path( path ) );
        if( container != null ) {
          IConfigurationFileInfo info = factory.createConfigFileInfo();
          info.setUse( null, "custom" ); //$NON-NLS-1$
          info.setPath( null, container.getFullPath().toString() );
          product.setConfigurationFileInfo( info );
        } else {
          super.initializeProduct( product );
        }
      }
      // set vm and program arguments from the launch configuration
      String vmargs = launchConfig.getAttribute( IJavaLaunchConfigurationConstants.ATTR_VM_ARGUMENTS,
                                                 ( String )null );
      String programArgs = launchConfig.getAttribute( IJavaLaunchConfigurationConstants.ATTR_PROGRAM_ARGUMENTS,
                                                      ( String )null );
      if( vmargs != null || programArgs != null ) {
        IArgumentsInfo arguments = product.getLauncherArguments();
        if( arguments == null )
          arguments = factory.createLauncherArguments();
        if( vmargs != null )
          arguments.setVMArguments( vmargs, IArgumentsInfo.L_ARGS_ALL );
        if( programArgs != null )
          arguments.setProgramArguments( programArgs, IArgumentsInfo.L_ARGS_ALL );
        product.setLauncherArguments( arguments );
      }
    } catch( final CoreException e ) {
      PDEPlugin.logException( e );
    }
  }
}
