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

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import junit.framework.TestCase;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.rap.warproducts.core.InfrastructreCreator;


public class InfrastructureCreatorTest extends TestCase {
  
  private static final String TEMP_PROJECT = "tempProject";
  private IFolder tempDir;

  protected void setUp() throws Exception {
    tempDir = createTempDir();
  }
  
  private IFolder createTempDir() throws CoreException {
    IWorkspace workspace = ResourcesPlugin.getWorkspace();
    IWorkspaceRoot root = workspace.getRoot();
    IProject project = root.getProject( TEMP_PROJECT );
    if( !project.exists() ) {
      project.create( null );
      project.open( null );
    }
    IFolder tempFolder = project.getFolder( "temp" );
    if( !tempFolder.exists() ) {
      tempFolder.create( true, true, null );
    }
    return tempFolder;
  }
  
  protected void tearDown() throws Exception {
    deleteTempDir();
  }
  
  private void deleteTempDir() throws CoreException {
    if( tempDir != null && tempDir.exists() ) {
      tempDir.delete( IResource.FORCE, null );
    }
  }

  public void testRootPath() {    
    IProject project 
      = ResourcesPlugin.getWorkspace().getRoot().getProject( TEMP_PROJECT );
    InfrastructreCreator icreator = new InfrastructreCreator( project );
    IContainer root = icreator.getContainer();    
    assertEquals( tempDir.getParent(), root );
  }
  
  public void testCreateWebInf() {
    InfrastructreCreator icreator = new InfrastructreCreator( tempDir );
    icreator.createWebInf();    
    IFolder webInf = tempDir.getFolder( "WEB-INF" );
    assertTrue( webInf.exists() );
  }
  
  public void testCreateWebXml() throws IOException, CoreException {
    InfrastructreCreator icreator = new InfrastructreCreator( tempDir);
    icreator.createWebXml();
    IFolder webInf = tempDir.getFolder( "WEB-INF" );
    IFile webXml = webInf.getFile( "web.xml" );
    assertTrue( webXml.exists() );
    StringBuffer webxmlContent = readFile( webXml.getContents( true ) );
    InputStream resourceStream 
      = InfrastructreCreator.class.getResourceAsStream( File.separator 
                                                        + "web.xml" );
    StringBuffer expectedContent = readFile( resourceStream );
    assertEquals( expectedContent.toString(), webxmlContent.toString() );
  }
  
  public void testGetWebXmlPath() {
    InfrastructreCreator icreator = new InfrastructreCreator( tempDir);
    icreator.createWebXml();
    IFolder webInf = tempDir.getFolder( "WEB-INF" );
    IFile webXml = webInf.getFile( "web.xml" );
    IPath webXmlPath = webXml.getFullPath();
    assertEquals( webXmlPath, icreator.getWebXmlPath() );
  }
  
  public void testCreateLaunchIni() throws IOException, CoreException {
    InfrastructreCreator icreator = new InfrastructreCreator( tempDir );
    icreator.createLaunchIni();
    IFolder webInf = tempDir.getFolder( "WEB-INF" );
    IFile launchIni = webInf.getFile( "launch.ini" );
    assertTrue( launchIni.exists() );
    StringBuffer actualLaunchIni = readFile( launchIni.getContents() );
    InputStream tempLaunchIni 
      = InfrastructreCreator.class.getResourceAsStream( File.separator 
                                                        + "launch.ini" );
    StringBuffer expectedLaunchIni = readFile( tempLaunchIni );
    assertEquals( expectedLaunchIni.toString(), actualLaunchIni.toString() );
  }
  
  public void testGetLaunchIniPath() {
    InfrastructreCreator icreator = new InfrastructreCreator( tempDir );
    icreator.createLaunchIni();
    IFolder webInf = tempDir.getFolder( "WEB-INF" );
    IFile launchIni = webInf.getFile( "launch.ini" );
    IPath launchIniPath = launchIni.getFullPath();
    assertEquals( launchIniPath, icreator.getLaunchIniPath() );
  }

  private StringBuffer readFile( final InputStream fileStream ) 
    throws IOException
  {
    InputStreamReader streamReader = new InputStreamReader( fileStream );
    BufferedReader reader = new BufferedReader( streamReader );
    StringBuffer webxmlContent = new StringBuffer();
    int c;
    while( ( c = reader.read() ) != -1 ) {
      webxmlContent.append( ( char ) c );
    }
    reader.close();
    return webxmlContent;
  }

}
