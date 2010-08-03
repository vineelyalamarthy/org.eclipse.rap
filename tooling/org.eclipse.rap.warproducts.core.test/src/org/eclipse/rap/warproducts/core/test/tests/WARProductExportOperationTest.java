/******************************************************************************* 
* Copyright (c) 2010 EclipseSource and others. All rights reserved. This
* program and the accompanying materials are made available under the terms of
* the Eclipse Public License v1.0 which accompanies this distribution, and is
* available at http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*   EclipseSource - initial API and implementation
*******************************************************************************/ 
package org.eclipse.rap.warproducts.core.test.tests;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import junit.framework.TestCase;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.osgi.service.resolver.BundleDescription;
import org.eclipse.osgi.service.resolver.State;
import org.eclipse.pde.internal.core.TargetPlatformHelper;
import org.eclipse.pde.internal.core.exports.FeatureExportInfo;
import org.eclipse.pde.internal.core.iproduct.IConfigurationFileInfo;
import org.eclipse.pde.internal.core.iproduct.IProduct;
import org.eclipse.pde.internal.core.iproduct.IProductModelFactory;
import org.eclipse.pde.internal.core.iproduct.IProductPlugin;
import org.eclipse.rap.warproducts.core.IWARProduct;
import org.eclipse.rap.warproducts.core.InfrastructreCreator;
import org.eclipse.rap.warproducts.core.WARProductExportOperation;
import org.eclipse.rap.warproducts.core.WARProductModel;
import org.osgi.framework.Version;


public class WARProductExportOperationTest extends TestCase {
  
  private static final String WAR_FILE = "export.war";
  private static final String WAR_FILE_PATH 
    = ResourcesPlugin.getWorkspace().getRoot().getLocation().toOSString() 
      + File.separator + WAR_FILE;
  private IFolder tempDir;

  protected void setUp() throws Exception {
    tempDir = createTempDir();
  }
  
  private IFolder createTempDir() throws CoreException {
    IWorkspace workspace = ResourcesPlugin.getWorkspace();
    IWorkspaceRoot root = workspace.getRoot();
    IProject project = root.getProject( "warProject" );
    if( !project.exists() ) {
      project.create( null );
      project.open( null );
    }
    IFolder tempFolder = project.getFolder( "warFolder" );
    if( !tempFolder.exists() ) {
      tempFolder.create( true, true, null );
    }
    return tempFolder;
  }
  
  protected void tearDown() throws Exception {
    deleteTempDir();
    deleteWarFile();
  }
  

  private void deleteTempDir() throws CoreException {
    if( tempDir != null && tempDir.exists() ) {
      tempDir.delete( IResource.FORCE, null );
    }    
  }
  
  private void deleteWarFile() {
    File war = new File( WAR_FILE_PATH );
    if( war.exists() ) {
      war.delete();
    }
  }
  
  public void testWARFileContents() throws Exception {
    File war = runBlockingWARExportJob();
    assertTrue( war.exists() );
    List warEntryList = extractWarEntriesAsString( war );
    testWARFileRootIsWebInf( warEntryList );
    testWARFileContainsWebXML( warEntryList );
    testWARFileContainsLibFolder( warEntryList );
    testWebInfFolderContainsLaunchIni( warEntryList );
    testWebInfFolderContainsPlugins( warEntryList );
    testLibContainsJar( warEntryList );
  }
  
  private void testWARFileRootIsWebInf( final List warEntryList ) 
    throws Exception 
  {
    assertTrue( warEntryList.contains( "WEB-INF" + File.separator ) );
  }
  
  private void testWARFileContainsWebXML( final List warEntryList ) 
    throws Exception 
  {
    assertTrue( warEntryList.contains( getFilePath( "WEB-INF", "web.xml" ) ) );
  }
  
  private  void testWARFileContainsLibFolder( final List warEntryList) 
    throws Exception 
  {
    assertTrue( warEntryList.contains( getFilePath( "WEB-INF", "lib" ) 
                                       + File.separator ) );
  }
  
  private void testWebInfFolderContainsLaunchIni( final List warEntryList ) 
    throws Exception 
  {
    assertTrue( warEntryList.contains( getFilePath( "WEB-INF", 
                                                    "launch.ini" ) ) );
  }
  
  private void testWebInfFolderContainsPlugins( final List warEntryList ) 
    throws Exception 
  {
    assertTrue( warEntryList.contains( getFilePath( "WEB-INF", "plugins" ) 
                                       + File.separator ) );
  }
  
  private void testLibContainsJar( final List warEntryList ) 
  throws Exception 
{
  String path = getFilePath( "WEB-INF" + File.separator + "lib", 
                             "test.jar" );
  assertTrue( warEntryList.contains( path ) );
}

  private List extractWarEntriesAsString( File war )
    throws ZipException, IOException
  {
    ZipFile zip = new ZipFile( war );
    List warEntryList = new ArrayList();
    Enumeration entries = zip.entries();
    while( entries.hasMoreElements() ) {
      Object nextElement = entries.nextElement();
      warEntryList.add( nextElement.toString() );
    }
    return warEntryList;
  }

  private File runBlockingWARExportJob() throws Exception
  {
    WARProductExportOperation job = createWarExportOperation();
    job.setUser( true );
    job.setRule( ResourcesPlugin.getWorkspace().getRoot() );
    job.schedule();
    job.join();
    return new File( WAR_FILE_PATH );
  }

  private WARProductExportOperation createWarExportOperation()
    throws Exception
  {
    IFolder tempDir = createTempDir();
    InfrastructreCreator creator = new InfrastructreCreator( tempDir );
    WARProductModel model = new WARProductModel();
    model.load( getTestWarProduct(), false );
    IWARProduct product = ( IWARProduct )model.getProduct();
    creator.createWebInf();
    creator.createLaunchIni();
    creator.createWebXml();
    product.removeLibrary( new Path( "/test.rap/lib.jar" ) );
    IFile file = tempDir.getFile( "test.jar" );
    if( !file.exists() ) {
      File jar = File.createTempFile( "test", ".jar" );
      FileInputStream stream = new FileInputStream( jar );
      file.create( stream, true, null );
    }
    product.addLibrary( file.getFullPath(), false );
    product.addLaunchIni( creator.getLaunchIniPath() );
    IProductModelFactory factory = model.getFactory();
    IConfigurationFileInfo configInfo = factory.createConfigFileInfo();
    product.setConfigurationFileInfo( configInfo );
    configInfo.setUse(Platform.OS_MACOSX, "default" ); //$NON-NLS-1$
    configInfo.setPath( Platform.OS_MACOSX, null );
    product.addWebXml( creator.getWebXmlPath() );
    FeatureExportInfo info = configureFeatureExport( product );
    String rootDirectory = "WEB-INF";
    WARProductExportOperation job 
      = new WARProductExportOperation( info,
                                       "a job",
                                       product,
                                       rootDirectory );
    return job;
  }

  private FeatureExportInfo configureFeatureExport( final IWARProduct product ) 
  {
    FeatureExportInfo info = new FeatureExportInfo();
    info.toDirectory = false;
    info.exportSource = false;
    info.exportSourceBundle = false;
    info.allowBinaryCycles = false;
    info.exportMetadata = false;
    info.destinationDirectory 
      = ResourcesPlugin.getWorkspace().getRoot().getLocation().toOSString();
    info.zipFileName = WAR_FILE;
    BundleDescription[] pluginModels = getPluginModels( product );
    info.items = pluginModels;
    return info;
  }

  private InputStream getTestWarProduct() {
    return getClass().getResourceAsStream( "/test.warproduct" );
  }

  private BundleDescription[] getPluginModels( final IProduct product ) {
    ArrayList list = new ArrayList();
    State state = TargetPlatformHelper.getState();
    IProductPlugin[] plugins = product.getPlugins();
    for( int i = 0; i < plugins.length; i++ ) {
      BundleDescription bundle = null;
      String v = plugins[ i ].getVersion();
      if( v != null && v.length() > 0 ) {
        bundle = state.getBundle( plugins[ i ].getId(),
                                  Version.parseVersion( v ) );
      }
      // if there's no version, just grab a bundle like before
      if( bundle == null ) {
        bundle = state.getBundle( plugins[ i ].getId(), null );
      }
      if( bundle != null ) {
        list.add( bundle );
      }
    }
    Object[] bundleArray = list.toArray( new BundleDescription[ list.size() ] );
    return ( BundleDescription[] )bundleArray;
  }
  
  private String getFilePath( final String folder, final String file ) {
    return folder + File.separator + file;
  }
  
}
