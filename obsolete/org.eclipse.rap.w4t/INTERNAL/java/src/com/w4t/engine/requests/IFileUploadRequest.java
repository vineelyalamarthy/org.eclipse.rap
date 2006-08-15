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
package com.w4t.engine.requests;

import org.apache.commons.fileupload.FileItem;

/**
 * <p>
 * Interface for internal use. It defines the methods for the encapsulation of
 * the FileUpload machanism regarding to the request specifics.
 * Usually this is used for requerst implementations handling
 * multipart/form-data requests. 
 * </p>
 */
public interface IFileUploadRequest {

  /**
   * <p>
   * reads the handler of the underlying multipart/form-data requerst for the 
   * given parameter name.
   * </p> 
   */
  public FileItem getFileItem( String name );
  
}
