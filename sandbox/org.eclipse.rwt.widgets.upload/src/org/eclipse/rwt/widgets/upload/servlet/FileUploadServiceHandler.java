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

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import org.apache.commons.fileupload.*;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.eclipse.rwt.RWT;
import org.eclipse.rwt.internal.util.URLHelper;
import org.eclipse.rwt.service.IServiceHandler;


/**
 * Handles file uploads and upload progress updates. 
 * <p> 
 * Implementation note: uploaded files are currently stored in the  
 * java.io.tmpdir. See 
 * {@link #handleFileUpload(HttpServletRequest, FileUploadStorageItem)} on
 * how to change this.
 * 
 * @author stefan.roeck 
 */
public class FileUploadServiceHandler implements IServiceHandler {

  private static final String REQUEST_WIDGET_ID = "widgetId";
  private static final String XML_HEAD = "<?xml version=\"1.0\" encoding=\"utf-8\"?><response>";

  /**
   * Requests to this service handler without a valid session id are ignored for
   * security reasons. The same applies to request with widgetIds which haven't been
   * registered at the session singleton {@link FileUploadStorage}.
   */
  public void service() throws IOException, ServletException {
    
    final HttpServletRequest request = RWT.getRequest();
    final String widgetId = request.getParameter( REQUEST_WIDGET_ID );
    final HttpSession session = request.getSession( false );
    
    if( session != null && widgetId != null && !"".equals( widgetId ) )
    {
      final FileUploadStorage fileUploadStorage = FileUploadStorage.getInstance();
      final FileUploadStorageItem fileUploadStorageItem = fileUploadStorage.getUploadStorageItem( widgetId );
      
      if (fileUploadStorageItem != null) {
        if (ServletFileUpload.isMultipartContent(request)) {
          // Handle post-request which contains the file to upload
          handleFileUpload( request, fileUploadStorageItem );
        } else {
          // This is probably a request for updating the progress bar
          handleUpdateProgress( fileUploadStorageItem );
        }
      }
      
    }
  }

  private void handleFileUpload( final HttpServletRequest request,
                                 final FileUploadStorageItem fileUploadStorageitem )
  {
    // Ignore upload requests which have no valid widgetId
    if (fileUploadStorageitem != null) {
      
      // Create file upload factory and upload servlet
      // You could use new DiskFileItemFactory(threshold, location)
      // to configure a custom in-memory threshold and storage location.
      // By default the upload files are stored in the java.io.tmpdir
      FileItemFactory factory = new DiskFileItemFactory();
      ServletFileUpload upload = new ServletFileUpload( factory );
      
      // Set file upload progress listener
      final FileUploadListener listener = new FileUploadListener();
      // Upload servlet allows to set upload listener
      upload.setProgressListener( listener );
      fileUploadStorageitem.setProgressListener( listener );
      
      FileItem fileItem = null;
      try {
        final List uploadedItems = upload.parseRequest( request );
        // Only one file upload at once is supported. If there are multiple files, take
        // the first one and ignore other
        if ( uploadedItems.size() > 0 ) {
          fileItem = ( FileItem )uploadedItems.get( 0 );
          // Don't check for file size 0 because this prevents uploading new empty office xp documents
          // which have a file size of 0.
          if( !fileItem.isFormField() ) {
            fileUploadStorageitem.setFileInputStream( fileItem.getInputStream() );
            fileUploadStorageitem.setContentType(fileItem.getContentType());
          }
        }
      } catch( FileUploadException e ) {
        e.printStackTrace();
      } catch( final Exception e ) {
        e.printStackTrace();
      }
    }
    
  }

  private void handleUpdateProgress( final FileUploadStorageItem fileUploadStorageitem )
    throws IOException
  {
    final HttpServletResponse response = RWT.getResponse();
    final PrintWriter out = response.getWriter();
    
    final StringBuffer buffy = new StringBuffer( XML_HEAD );
    long bytesRead = 0;
    long contentLength = 0;
    // Check to see if we've created the listener object yet
    response.setContentType( "text/xml" );
    response.setHeader( "Cache-Control", "no-cache" );
    
    final FileUploadListener listener = fileUploadStorageitem.getProgressListener();
    if( listener != null ) {
      // Get the meta information
      bytesRead = listener.getBytesRead();
      contentLength = listener.getContentLength();
      /*
       * XML Response Code
       */
      buffy.append( "<bytes_read>" );
      buffy.append( bytesRead );
      buffy.append( "</bytes_read><content_length>" );
      buffy.append( contentLength );
      buffy.append( "</content_length>" );
      // Check to see if we're done
      if( contentLength != 0 ) {
        if( bytesRead == contentLength ) {
          buffy.append( "<finished />" );
          // No reason to keep listener in session since we're done
          fileUploadStorageitem.setProgressListener( null );
        } else {
          // Calculate the percent complete
          buffy.append( "<percent_complete>" );
          buffy.append( ( 100 * bytesRead / contentLength ) );
          buffy.append( "</percent_complete>" );
        }
      }
    }
    buffy.append( "</response>" );
    out.println( buffy.toString() );
    out.flush();
    out.close();
  }

  /**
   * Registers this service handler. This method should be called only once.
   */
  public static void register() {
    FileUploadServiceHandler instance = new FileUploadServiceHandler();
    final String serviceHandlerId = getServiceHandlerId();
    RWT.getServiceManager().registerServiceHandler(serviceHandlerId, instance);
  }

  /**
   * Returns a unique id for this service handler class.
   */
  private static String getServiceHandlerId() {
    final String serviceHandlerId = FileUploadServiceHandler.class.getName();
    return serviceHandlerId;
  }

  /**
   * Builds a url which points to the service handler and encodes the given parameters
   * as url parameters. 
   */
  public static String getUrl(final String widgetId) {
    StringBuffer url = new StringBuffer();
    url.append(URLHelper.getURLString(false));

    URLHelper.appendFirstParam(url, REQUEST_PARAM, getServiceHandlerId());
    URLHelper.appendParam(url, REQUEST_WIDGET_ID, widgetId);

    return RWT.getResponse().encodeURL(url.toString());
  }
}
