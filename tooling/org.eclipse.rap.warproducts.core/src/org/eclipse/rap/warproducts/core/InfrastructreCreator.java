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

import java.io.InputStream;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;


public class InfrastructreCreator {

  private static final String WEB_INF_PATH = "WEB-INF"; //$NON-NLS-1$
  private static final String WEB_XML_TEMPLATE_PATH 
    = "/web.xml"; //$NON-NLS-1$
  private static final String LAUNCH_INI_TEMPLATE_PATH 
    = "/launch.ini"; //$NON-NLS-1$
  private static final String WEB_XML_NAME = "web.xml"; //$NON-NLS-1$
  private static final String LAUNCH_INI_NAME = "launch.ini"; //$NON-NLS-1$
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
          refreshWebInf();
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
    refreshWebInf();
  }
  
  public void createLaunchIni() {
    if( webInfDir == null ) {
      createWebInf();
    }
    internalCopyFile( LAUNCH_INI_TEMPLATE_PATH, webInfDir, LAUNCH_INI_NAME );
    refreshWebInf();
  }

  private void internalCopyFile( final String from, 
                                 final IContainer container,
                                 final String fileName ) 
  {
    copyFile( from, container, fileName );
  }

  private void copyFile( final String from, 
                         final IContainer container, 
                         final String fileName ) 
  {        
      IPath path = new Path( fileName );
      IFile file = container.getFile( path );
      if( !file.exists() ) {
        try {
          file.create( getFileStream( from ), true, null );
        } catch( final CoreException e ) {
          System.err.println(Messages.creatorCouldntCopy + from );
          e.printStackTrace();
        }
      }
  }

  private InputStream getFileStream( final String from ) {
    InputStream fileInStream = getClass().getResourceAsStream( from );
    return fileInStream;
  }

  public IPath getWebXmlPath() {
    IPath webInfPath = webInfDir.getFullPath();
    return webInfPath.append( WEB_XML_NAME );
  }

  public IPath getLaunchIniPath() {
    IPath webInfPath = webInfDir.getFullPath();
    return webInfPath.append( LAUNCH_INI_NAME );
  }
  
  private void refreshWebInf() {
    try {
      webInfDir.refreshLocal( IResource.DEPTH_ONE, null );
    } catch( final CoreException e ) {
      e.printStackTrace();
    }
  }
}
