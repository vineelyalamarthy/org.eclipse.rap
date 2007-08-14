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
package com.w4t.webfileuploadkit;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.eclipse.rwt.internal.lifecycle.HtmlResponseWriter;
import org.eclipse.rwt.internal.service.ContextProvider;
import org.eclipse.rwt.internal.util.HTML;

import com.w4t.*;
import com.w4t.engine.lifecycle.standard.EventQueue;
import com.w4t.engine.requests.IFileUploadRequest;
import com.w4t.event.WebFileUploadEvent;
import com.w4t.internal.adaptable.IFileUploadAdapter;

public abstract class WebFileUploadRenderer extends Renderer {

  public void readData( final WebComponent component ) {
    HttpServletRequest request = ContextProvider.getRequest();
    String fileName = request.getParameter( component.getUniqueID() );
    // don't ask for fileName == null; in this case there is actually an upload
    if( !"".equals( fileName ) ) {
      IFileUploadRequest uploadRequest = ( IFileUploadRequest )request;
      WebFileUpload upload = ( WebFileUpload )component;
      FileItem uploadedFile = uploadRequest.getFileItem( upload.getUniqueID() );
      if( uploadedFile.getSize() != 0 ) { 
        IFileUploadAdapter adapter 
          = ( IFileUploadAdapter )upload.getAdapter( IFileUploadAdapter.class );
        adapter.setFileItem( uploadedFile );
        
        int id = WebFileUploadEvent.FILEUPLOADED;
        WebFileUploadEvent evt = new WebFileUploadEvent( upload, id );
        EventQueue.getEventQueue().addToQueue( evt );
      }
    }
  }

  public void render( final WebComponent component ) throws IOException {
    WebFileUpload fileUpload = ( WebFileUpload )component;
    WebForm webForm = fileUpload.getWebForm();
    IFileUploadAdapter adapter 
      = ( IFileUploadAdapter )webForm.getAdapter( IFileUploadAdapter.class );
    adapter.setMultipartFormEncoding( true );
    HtmlResponseWriter out = getResponseWriter();
    out.startElement( HTML.INPUT, null );
    out.writeAttribute( HTML.TYPE, HTML.FILE, null );
    out.writeAttribute( HTML.NAME, fileUpload.getUniqueID(), null );
    RenderUtil.writeUniversalAttributes( fileUpload );
    createSizeAttribute( fileUpload );
    appendFocusHandling( fileUpload );
    out.endElement( HTML.INPUT );
  }

  // Helping Methods
  //////////////////
  
  abstract void appendFocusHandling( final WebFileUpload fileUpload ) 
    throws IOException;

  private void createSizeAttribute( final WebFileUpload fileUpload )
    throws IOException
  {
    int size = fileUpload.getSize();
    if( size >= 0 ) {
      HtmlResponseWriter out = getResponseWriter();
      out.writeAttribute( HTML.SIZE, String.valueOf( size ), null );
    }
  }
}
