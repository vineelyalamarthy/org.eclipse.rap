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

import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.fileupload.*;
import org.eclipse.rwt.internal.*;

import com.w4t.engine.util.ServletExceptionAdapter;

/** <p>Special HttpServletRequest wrapper for handling file 
 *  upload requests.</p> */
public class FileUploadRequest 
  extends HttpServletRequestWrapper 
  implements IFileUploadRequest 
{
  
  private static final String SYSTEM_TEMP_DIR 
    = System.getProperty( "java.io.tmpdir" );

  private Hashtable parameters = new Hashtable();

  public FileUploadRequest( final HttpServletRequest request ) 
    throws ServletException 
  {
    super( request );
    IConfiguration configuration = ConfigurationReader.getConfiguration();
    IFileUpload fileUpload = configuration.getFileUpload();
    UploadRequestFileItemFactory factory = new UploadRequestFileItemFactory();
    DiskFileUpload upload = new DiskFileUpload( factory );
    upload.setSizeThreshold( fileUpload.getMaxMemorySize() );
    upload.setSizeMax( fileUpload.getMaxMemorySize() );
    upload.setRepositoryPath( FileUploadRequest.SYSTEM_TEMP_DIR );
    try {
      List items = upload.parseRequest( request );
      Iterator iter = items.iterator();
      while( iter.hasNext() ) {
        FileItem item = ( FileItem )iter.next();
        parameters.put( item.getFieldName(), item );
      }
       
    } catch( FileUploadException e ) {
      throw new ServletExceptionAdapter( e );
    }
  }

  public String getParameter( String name ) {
    String result = null;
    FileItem item = ( FileItem )parameters.get(name);
    if( item != null && item.isFormField() ) {
      result = item.getString();
    }
    return result;
  }
  
  public FileItem getFileItem( final String name ) {
    FileItem result = null;
    FileItem item = ( FileItem )parameters.get( name );
    if( item != null && !item.isFormField() ) {
      result = item;
    }
    return result;

  }

  public Enumeration getParameterNames() {
    return parameters.keys();
  }
}