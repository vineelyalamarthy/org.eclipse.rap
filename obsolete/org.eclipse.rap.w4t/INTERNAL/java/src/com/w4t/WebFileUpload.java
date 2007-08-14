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
package com.w4t;

import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.fileupload.FileItem;
import com.w4t.event.WebFileUploadEvent;
import com.w4t.event.WebFileUploadListener;
import com.w4t.internal.adaptable.IFileUploadAdapter;
import com.w4t.internal.simplecomponent.UniversalAttributes;

/**
 *  <p>
 *  A File-Upload is a WebComponent which allows uploading files from the 
 *  client to the server. It encapsulates the HTML-form 
 *  &lt;input type="file"&gt; element. This class represents a file received.
 *  </p>
 *  <p>
 *  After retrieving an instance of this class, you may
 *  either request all contents of the file at once using {@link #get()} or
 *  request an {@link java.io.InputStream InputStream} with
 *  {@link #getInputStream()} and process the file without attempting to load
 *  it into memory, which may come handy with large files.
 *  </p>
 *  <p>
 *  The data may be stored in memory, on disk, or somewhere else and will be
 *  deleted on finalize.
 *  </p>
 */
public class WebFileUpload 
  extends WebComponent
  implements SimpleComponent, IFocusable
{
  
  private FileItem fileItem;
  private int size = -1;

  /** the universal html attributes encapsulation class */
  private UniversalAttributes universalAttributes;
  
  
  public void setFocus( final boolean focus ) {
    FocusHelper.setFocus( this, focus );
  }
  
  public boolean hasFocus() {
    return FocusHelper.hasFocus( this );
  }
  
  public void remove() {
    setFocus( false );
    super.remove();
  }

  /** 
   * <p>
   * get the width (number of chars) which are displayed in the 
   * &quot;selected file&quot; box.
   * </p>
   */
  public int getSize() {
    return size;
  }
  
  /** 
   * <p>
   * set the width (number of chars) which are displayed in the 
   * &quot;selected file&quot; box.
   * </p>
   */
  public void setSize( final int size ) {
    if( size >= -1 ) {
      this.size = size;
    }
  }

  /**
   *  <p> 
   *  Returns an {@link java.io.InputStream InputStream} that can be
   *  used to retrieve the contents of the file.
   *  </p>
   */
  public InputStream getInputStream() throws IOException {
    InputStream result = null;
    if( fileItem != null ) {
      result = fileItem.getInputStream();
    }
    return result;
  }

  /**
   * <p>
   * Deletes the underlying storage for a file item, including deleting any
   * associated temporary disk file. Although this storage will be deleted
   * automatically when the <code>FileItem</code> instance is garbage
   * collected, this method can be used to ensure that this is done at an
   * earlier time, thus preserving system resources.
   * </p>
   * <p>
   * On finalize htis method will be called. 
   * </p> 
   */
  private void delete() {
    if( fileItem != null ) {
      fileItem.delete();
    }
  }

  /**
   *  <p>
   *  Returns the contents of the file item as an array of bytes.
   *  </p> 
   */
  public byte[] get() {
    byte[] result = null;
    if( fileItem != null ) {
      result = fileItem.get();
    }
    return result;
  }

  /**
   *  <p>
   *  Returns the content type passed by the browser or <code>null</code> if
   *  not defined. The passed ContentType may differ dependingon the browser / 
   *  operating system used. 
   *  </p>
   */
  public String getContentType() {
    String result = null;
    if( fileItem != null ) {
      result = fileItem.getContentType();
    }
    return result;
  }


  /**  
   *  <p>
   *  Returns the size of the file item in bytes.
   *  </p> 
   */
  public long getFileSize() {
    long result = 0;
    if( fileItem != null ) {
      result = fileItem.getSize();
    }
    return result;
  }

  /**  
   *  <p>
   *  Returns the original filename in the client's filesystem, as provided
   *  by the browser (or other client software). In most cases, this will be 
   *  the base file name, without path information. However, some clients, such
   *  as the Opera browser, do include path information.
   *  </p>
   */
  public String getFileName() {
    String result = null;
    if( fileItem != null ) {
      result = fileItem.getName();
    }
    return result;
  }

  /** 
   * <p>
   * returns a clone of this WebFileUpload.<br>
   * Cloning will result in a deep copy, shallow copying all fields, cloning 
   * property objects (i.e. Style, WindowProperties etc.).<br>
   * Fields containing external references to Objects (i.e. FileItem) which are 
   * not added with one of those methods are set to null in the cloned object.
   * </p>
   */
  public Object clone() throws CloneNotSupportedException {
    WebFileUpload result = ( WebFileUpload )super.clone();
    result.universalAttributes = null;
    if( universalAttributes != null ) {
      result.universalAttributes 
        = ( UniversalAttributes )universalAttributes.clone();
    }
    result.fileItem = null;
    return result;
  }
  
  /** 
   *  <p>
   *  returns a path to an image that represents this WebComponent 
   *  (widget icon).
   *  </p> 
   */
  public static String retrieveIconName() {
    return "resources/images/icons/fileupload.gif";
  }    

  /**
   *  <p>
   *  This method is used to encapsulate internal methods for the programmers
   *  convenience. This helps to keep the API slim and avoids unnecessary bloat.
   *  </p>
   */
  public Object getAdapter( final Class adapter ) {
    Object result = null;
    if( adapter == IFileUploadAdapter.class ) {
      result = new IFileUploadAdapter() {
        public void setFileItem( final FileItem newFileItem ) {
          fileItem = newFileItem;
        }
        public boolean isMultipartFormEncoding() {
          return false;
        }
        public void setMultipartFormEncoding( final boolean encoding ) {
        }
      };
    } else {
      result = super.getAdapter( adapter );
    }
    return result;
  }

  protected void finalize() throws Throwable {
    delete();
    super.finalize();
  }
  
  /** 
    *  <p>
    *  Add a WebFileUploadListener to it. 
    *  </p>
    */
  public void addWebFileUploadListener( final WebFileUploadListener lsnr ) {
    WebFileUploadEvent.addListener( this, lsnr );
  }
  
  /** 
   *  <p>
   *  Remove the WebFileUploadListener. 
   *  </p>
   */
  public void removeWebFileUploadListener( final WebFileUploadListener lsnr ) {
    WebFileUploadEvent.removeListener( this, lsnr );
  }

  
  // interface methods of org.eclipse.rap.SimpleComponent
  ///////////////////////////////////////////////////////////////


  public String getCssClass() {
    return getUniversalAttributes().getCssClass();
  }
  
  public String getDir() {
    return getUniversalAttributes().getDir();
  }
  
  public String getLang() {
    return getUniversalAttributes().getLang();
  }
  
  public Style getStyle() {
    return getUniversalAttributes().getStyle();
  }
  
  public String getTitle() {
    return getUniversalAttributes().getTitle();
  }
  
  public void setCssClass( final String cssClass ) {
    getUniversalAttributes().setCssClass( cssClass );
  }
  
  public void setDir( final String dir ) {
    getUniversalAttributes().setDir( dir );
  }
  
  public void setLang( final String lang ) {
    getUniversalAttributes().setLang( lang );
  }
  
  public void setStyle( final Style style ) {
    getUniversalAttributes().setStyle( style );
  }
  
  public void setTitle( final String title ) {
    getUniversalAttributes().setTitle( title );
  }

  public void setIgnoreLocalStyle( final boolean ignoreLocalStyle ) {
    getUniversalAttributes().setIgnoreLocalStyle( ignoreLocalStyle );
  }
  
  public boolean isIgnoreLocalStyle() {
    return getUniversalAttributes().isIgnoreLocalStyle();
  }

  private UniversalAttributes getUniversalAttributes() {
    if( universalAttributes == null ) {
      universalAttributes = new UniversalAttributes();
    }
    return universalAttributes;
  }
}