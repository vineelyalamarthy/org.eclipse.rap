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

import org.eclipse.core.runtime.IPath;
import org.eclipse.pde.internal.core.iproduct.IProduct;


public interface IWARProduct extends IProduct {

  public void addLibrary( final IPath absolutePath );

  public IPath[] getLibraries();

  public boolean contiansLibrary( final IPath relativeWorkspacePath );

  public void addWebXml( final IPath relativeWorkspacePath );

  public IPath getWebXml();

  public void addLaunchIni( final IPath relativeWorkspacePath );

  public IPath getLaunchIni();

  public void removeLibrary( final IPath libraryPath );

  public void removeLibraries( final IPath[] pathes );
  
}
