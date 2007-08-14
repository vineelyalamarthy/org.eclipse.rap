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
package com.w4t.engine.service;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;

import org.eclipse.rwt.internal.resources.ResourceManagerImpl;
import org.eclipse.rwt.internal.service.AbstractServiceHandler;
import org.eclipse.rwt.internal.service.RequestParams;



// TODO [w4t] move back to org.eclipse.rap.w4t
class ResourceRequestServiceHandler extends AbstractServiceHandler {

  /** <p>Map file extensions to MIME types.</p>*/
  private Properties mimeTypes;

  
  /** <p>creates a new instance of ResourceRequestServiceHandler.</p> */ 
  public ResourceRequestServiceHandler() {
    mimeTypes = new Properties();
    initMap();
  }
  
  public void service() throws ServletException, IOException {
    setExpirationHeader();
    String name = getRequest().getParameter( RequestParams.RESOURCE );
    Integer version = getResourceVersion();
    int[] resource = ResourceManagerImpl.findResource( name, version );
    if( resource != null ) {
      getResponse().setContentType( getMIMEType( name ) );
      ServletOutputStream out = getResponse().getOutputStream();
      BufferedOutputStream bos = new BufferedOutputStream( out );
      try {
        for( int i = 0; i < resource.length; i++ ) {
          bos.write( resource[ i ] );  
        }
        bos.flush();
      } finally {
        bos.close();
      }
    }
  }
  
  //////////////////
  // helping methods
  
  private void initMap() {
    // TODO: read this from property file
    mimeTypes.setProperty( "",          "content/unknown" );
    mimeTypes.setProperty( ".aif",      "audio/x-aiff" );
    mimeTypes.setProperty( ".mapaifc",  "audio/x-aiff" );
    mimeTypes.setProperty( ".aiff",     "audio/x-aiff" );
    mimeTypes.setProperty( ".au",       "audio/basic" );
    mimeTypes.setProperty( ".kar",      "audio/midi" );
    mimeTypes.setProperty( ".mid",      "audio/midi" );
    mimeTypes.setProperty( ".midi",     "audio/midi" );
    mimeTypes.setProperty( ".mp2",      "audio/mpeg" ) ;
    mimeTypes.setProperty( ".mp3",      "audio/mpeg" );
    mimeTypes.setProperty( ".mpga",     "audio/mpeg" );
    mimeTypes.setProperty( ".ra",       "audio/x-realaudio" );
    mimeTypes.setProperty( ".ram",      "audio/x-pn-realaudio" );
    mimeTypes.setProperty( ".rpm",      "audio/x-pn-realaudio-plugin" );
    mimeTypes.setProperty( ".snd",      "audio/basic" );
    mimeTypes.setProperty( ".vox",      "audio/voxware" );
    mimeTypes.setProperty( ".wav",      "audio/x-wav" );
    mimeTypes.setProperty( ".wax",      "audio/x-ms-wax" );
    mimeTypes.setProperty( ".wma",      "audio/x-ms-wma" );
    mimeTypes.setProperty( ".au",       "audio/basic" );
    mimeTypes.setProperty( ".wm",       "video/x-ms-wm" );
    mimeTypes.setProperty( ".mpe",      "video/mpeg" );
    mimeTypes.setProperty( ".mpeg",     "video/mpeg" );
    mimeTypes.setProperty( ".mpg",      "video/mpeg" );
    mimeTypes.setProperty( ".asf",      "video/x-ms-asf" );
    mimeTypes.setProperty( ".asx",      "video/x-ms-asf" );
    mimeTypes.setProperty( ".avi",      "video/x-msvideo" );
    mimeTypes.setProperty( ".mov",      "video/quicktime" );
    mimeTypes.setProperty( ".movie",    "video/x-sgi-movie" );
    mimeTypes.setProperty( ".qt",       "video/quicktime" );
    mimeTypes.setProperty( ".wmv",      "video/x-ms-wmv" );
    mimeTypes.setProperty( ".wmx",      "video/x-ms-wmx" );
    mimeTypes.setProperty( ".wvx",      "video/x-ms-wvx" );
    mimeTypes.setProperty( ".bin",      "application/octet-stream" );
    mimeTypes.setProperty( ".class",    "application/octet-stream" );
    mimeTypes.setProperty( ".dll",      "application/octet-stream" );
    mimeTypes.setProperty( ".exe",      "application/octet-stream" );
    mimeTypes.setProperty( ".uu",       "application/octet-stream" );
    mimeTypes.setProperty( ".ps",       "application/postscript" );
    mimeTypes.setProperty( ".sh",       "application/x-shar" );
    mimeTypes.setProperty( ".doc",      "application/msword" );
    mimeTypes.setProperty( ".pdf",      "application/pdf" );
    mimeTypes.setProperty( ".ppt",      "application/powerpoint" );
    mimeTypes.setProperty( ".ps",       "application/postscript" );
    mimeTypes.setProperty( ".etx",      "text/x-setext" );
    mimeTypes.setProperty( ".htm",      "text/html" );
    mimeTypes.setProperty( ".html",     "text/html" );
    mimeTypes.setProperty( ".rtx",      "text/richtext" );
    mimeTypes.setProperty( ".sgm",      "text/x-sgml" );
    mimeTypes.setProperty( ".sgml",     "text/x-sgml" );
    mimeTypes.setProperty( ".text",     "text/plain" );
    mimeTypes.setProperty( ".txt",      "text/plain" );
    mimeTypes.setProperty( ".xml",      "text/xml" );
    mimeTypes.setProperty( ".xsd",      "text/xml" );
    mimeTypes.setProperty( ".dtd",      "text/xml" );
    mimeTypes.setProperty( ".css",      "text/css" );
    mimeTypes.setProperty( ".js",       "text/javascript" );
    mimeTypes.setProperty( ".c",        "text/plain" );
    mimeTypes.setProperty( ".cc",       "text/plain" );
    mimeTypes.setProperty( ".c++",      "text/plain" );
    mimeTypes.setProperty( ".h",        "text/plain" );
    mimeTypes.setProperty( ".pl",       "text/plain" );
    mimeTypes.setProperty( ".java",     "text/plain" );
    mimeTypes.setProperty( ".gtar",     "application/x-gtar" );
    mimeTypes.setProperty( ".gz",       "application/x-gzip" ) ;
    mimeTypes.setProperty( ".zip",      "application/zip" );
    mimeTypes.setProperty( ".tar",      "application/x-tar" );
    mimeTypes.setProperty( ".jar",      "application/x-compressed" );
    mimeTypes.setProperty( ".bmp",      "image/x-xbitmap" );
    mimeTypes.setProperty( ".gif",      "image/gif" );
    mimeTypes.setProperty( ".ief",      "image/ief" );
    mimeTypes.setProperty( ".jpe",      "image/jpeg" );
    mimeTypes.setProperty( ".jpeg",     "image/jpeg" );
    mimeTypes.setProperty( ".jpg",      "image/jpeg" );
    mimeTypes.setProperty( ".xpm",      "image/x-xpixmap" );
    mimeTypes.setProperty( ".xwd",      "image/x-xwindowdump" );
    mimeTypes.setProperty( ".pgm",      "image/x-portable-graymap" );
    mimeTypes.setProperty( ".png",      "image/png" );
    mimeTypes.setProperty( ".pnm",      "image/x-portable-anymap" );
    mimeTypes.setProperty( ".ppm",      "image/x-portable-pixmap" );
    mimeTypes.setProperty( ".ras",      "image/x-cmu-raster" );
    mimeTypes.setProperty( ".rgb",      "image/x-rgb" );
    mimeTypes.setProperty( ".tif",      "image/tiff" );
    mimeTypes.setProperty( ".tiff",     "image/tiff" );
    mimeTypes.setProperty( ".xbm",      "image/x-xbitmap" );
    mimeTypes.setProperty( ".vrml",     "model/vrml" );
    mimeTypes.setProperty( ".wrl",      "model/vrml" ); 
    mimeTypes.setProperty( ".iges",     "model/iges" );
    mimeTypes.setProperty( ".igs",      "model/iges" );
    mimeTypes.setProperty( ".mesh",     "model/mesh" );
    mimeTypes.setProperty( ".silo",     "model/mesh" );
  }
  
  private String getMIMEType( final String fileName ) {
    String result = mimeTypes.getProperty( "" );
    int pos = fileName.lastIndexOf( '.' );
    if( pos != -1 ) {
      String extension = fileName.substring( pos, fileName.length() );
      String mime = mimeTypes.getProperty( extension );
      if( mime != null && !mime.equals( "" ) ) {
        result = mime; 
      }
    }
    return result;
  }
  
  private static Integer getResourceVersion() {
    Integer result = null;
    String paramValue 
      = getRequest().getParameter( RequestParams.RESOURCE_VERSION );
    if( paramValue != null ) {
      try {
        result = new Integer( Integer.parseInt( paramValue ) );
      } catch( NumberFormatException e ) {
        // ignore - result remains null
      }
    }
    return result;
  }
}