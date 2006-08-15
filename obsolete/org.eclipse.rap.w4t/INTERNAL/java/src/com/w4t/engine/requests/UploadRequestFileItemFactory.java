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

import org.apache.commons.fileupload.DefaultFileItemFactory;
import org.apache.commons.fileupload.FileItem;

/**
 * <p>
 * Factory for creation of the FileItem implementation of W4Toolkit 
 * which provides some optimizations regarding the memory footprint of 
 * formfields items.
 * </p>
 */
class UploadRequestFileItemFactory extends DefaultFileItemFactory {

  public FileItem createItem( final String fieldName,
                              final String contentType,
                              final boolean isFormField,
                              final String fileName )
  {
    return new UploadRequestFileItem( fieldName, 
                                      contentType,
                                      isFormField, 
                                      fileName, 
                                      getSizeThreshold(), 
                                      getRepository() );
  }
}