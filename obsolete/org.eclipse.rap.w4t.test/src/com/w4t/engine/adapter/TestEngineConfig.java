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
package com.w4t.engine.adapter;

import java.io.File;

import org.eclipse.rwt.internal.IEngineConfig;

/** <p>A testing implementation for an engine configuration.</p>
  */
public class TestEngineConfig implements IEngineConfig {

  private File classDir;
  private File configFile;
  private File libDir;
  private File serverContextDir;
  private File sourceDir;    
  private File licenseDir;    
  
  public File getClassDir() {
    return classDir;
  }

  public File getConfigFile() {
    return configFile;
  }

  public File getLibDir() {
    return libDir;
  }

  public File getServerContextDir() {
    return serverContextDir;
  }

  public File getSourceDir() {
    return sourceDir;
  }

  public File getLicenseDir() {
    return licenseDir;
  }

  public void setClassDir( File classDir ) {
    this.classDir = classDir;
  }

  public void setConfigFile( File configFile ) {
    this.configFile = configFile;
  }

  public void setLibDir( File libDir ) {
    this.libDir = libDir;
  }

  public void setServerContextDir( File serverContextDir ) {
    this.serverContextDir = serverContextDir;
  }

  public void setSourceDir( File sourceDir ) {
    this.sourceDir = sourceDir;
  }

  public void setLicenseDir( File licenseDir ) {
    this.licenseDir = licenseDir;
  }
}