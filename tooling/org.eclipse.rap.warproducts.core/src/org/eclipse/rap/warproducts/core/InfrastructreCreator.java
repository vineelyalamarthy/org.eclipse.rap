/******************************************************************************* 
* Copyright (c) 2010 EclipseSource and others. All rights reserved. This
* program and the accompanying materials are made available under the terms of
* the Eclipse Public License v1.0 which accompanies this distribution, and is
* available at http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*   EclipseSource - initial API and implementation
*******************************************************************************/ 
package org.eclipse.rap.warproducts.core;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;


public class InfrastructreCreator {

  private static final String ECLIPSE_PATH = "eclipse";
  private static final String WEB_INF_PATH = "WEB-INF";
  private static final String WEB_XML_TEMPLATE_PATH 
    = File.separator + "web.xml";
  private static final String LAUNCH_INI_TEMPLATE_PATH 
    = File.separator + "launch.ini";
  private static final String WEB_XML_NAME = "web.xml";
  private static final String LAUNCH_INI_NAME = "launch.ini";
  private IContainer rootFolder;
  private IFolder webInfDir;

  public InfrastructreCreator( final IContainer tempDir ) {
    this.rootFolder = tempDir;
  }

  public IContainer getContainer() {
    return rootFolder;
  }

  public void createWebInf() {
    if( webInfDir == null ) {
      IPath webInfPath = new Path( WEB_INF_PATH );
      webInfDir = rootFolder.getFolder( webInfPath );
      if( !webInfDir.exists() ) {
    	  try {
          webInfDir.create( true, false, null );
        } catch( final CoreException e ) {
          e.printStackTrace();
        }
      }
    }
  }

  public void createWebXml() {
    if( webInfDir == null ) {
      createWebInf();
    }
    internalCopyFile( WEB_XML_TEMPLATE_PATH, webInfDir, WEB_XML_NAME );
  }
  
  public void createLaunchIni() {
    if( webInfDir == null ) {
    	createWebInf();
    }
    IFolder eclipseDir = createEclipseDir();
    internalCopyFile( LAUNCH_INI_TEMPLATE_PATH, eclipseDir, LAUNCH_INI_NAME );
  }

  private IFolder createEclipseDir() {
    IFolder eclipseDir = webInfDir.getFolder( ECLIPSE_PATH );
    if( !eclipseDir.exists() ) {
    	try {
        eclipseDir.create( true, true, null );
      } catch( final CoreException e ) {
        System.err.println( "Could not create eclipse directory" );
        e.printStackTrace();
      }
    }
    return eclipseDir;
  }
  
  private void internalCopyFile( final String from, 
                                 final IContainer container,
                                 final String fileName ) 
  {
    try {
      copyFile( from, container, fileName );
    } catch( final IOException e ) {
      e.printStackTrace();
    }
  }

  private void copyFile( final String from, 
                         final IContainer container, 
                         final String fileName ) throws IOException 
  {	  	  
      IPath path = new Path( fileName );
      IFile file = container.getFile( path );
      if( !file.exists() ) {
        try {
          file.create( getFileStream( from ), true, null );
        } catch( final CoreException e ) {
          System.err.println("Could not copy file: " + from );
          e.printStackTrace();
        }
      }
  }

  private InputStream getFileStream( final String from ) throws IOException {
    InputStream fileInStream = getClass().getResourceAsStream( from );
    return fileInStream;
  }

  public IPath getWebXmlPath() {
    IPath webInfPath = webInfDir.getFullPath();
    return webInfPath.append( WEB_XML_NAME );
  }

  public IPath getLaunchIniPath() {
    IPath webInfPath = webInfDir.getFullPath();
    IPath eclipsePath = webInfPath.append( ECLIPSE_PATH );
    return eclipsePath.append( LAUNCH_INI_NAME );
  }

}
