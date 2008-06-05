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

package org.eclipse.rwt.widgets.upload.servlet;

import java.io.InputStream;


/**
 * This Pojo is used to store a file reference and a progress listener.
 * It is used for communication between service handler and rap application.
 * 
 * @author stefan.roeck
 */
public class FileUploadStorageItem {
  private InputStream fileInputStream;
  private FileUploadListener progressListener;
  private String contentType;
  
  public InputStream getFileInputStream() {
    return fileInputStream;
  }
  
  public void setFileInputStream( InputStream fileInputStream ) {
    this.fileInputStream = fileInputStream;
  }
  
  public FileUploadListener getProgressListener() {
    return progressListener;
  }
  
  public void setProgressListener( FileUploadListener progressListener ) {
    this.progressListener = progressListener;
  }

  public void setContentType( String contentType ) {
    this.contentType = contentType;
  }
  
  public String getContentType() {
    return contentType;
  }
}
