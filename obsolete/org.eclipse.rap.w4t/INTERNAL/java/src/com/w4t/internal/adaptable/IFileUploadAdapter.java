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
package com.w4t.internal.adaptable;

import org.apache.commons.fileupload.FileItem;

/**
 * <p>
 * Interface for internal use. It defines the methods for the encapsulation of
 * the FileUpload machanism.</p>
 * <p>
 * Classes which are involved in this machanism should provide their 
 * implementation via the {@link org.eclipse.rwt.Adaptable#getAdapter(Class) getAdapter} 
 * method of {@link org.eclipse.rwt.Adaptable Adaptable}.
 * </p>
 */
public interface IFileUploadAdapter {
  
  /**
   * <p>
   * sets the internel data structure of this FileUpload Handler.
   * </p> 
   */
  void setFileItem( FileItem fileItem );
  
  /**
   * <p>
   * returns whether the current Adapter uses multipart encoding on 
   * HTML-forms.
   * </p> 
   */
  boolean isMultipartFormEncoding();

  /**
   * <p>
   * setes whether the current Adapter should use multipart encoding on 
   * HTML-forms.
   */
  void setMultipartFormEncoding( boolean multipartFormEncoding );
}
