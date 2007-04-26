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

import java.io.*;
import org.apache.commons.fileupload.DeferredFileOutputStream;
import org.apache.commons.fileupload.FileItem;

/**
 * <p>
 * The FileItem implementation of W4Toolkit provides some optimizations
 * regarding the memory footprint of formfields items.
 * </p>
 */
class UploadRequestFileItem implements FileItem {

  private static final long serialVersionUID = 1L;

  private static final int DEFAULT_OUTPUTSTREAM_SIZE = 32;

  private static int counter = 0;
  private String fieldName;
  private String contentType;
  private boolean isFormField;
  private String fileName;
  private int sizeThreshold;
  private File repository;
  private byte[] cachedContent;
  private DeferredFileOutputStream dfos;

  /**
   * <p>creates a new <code>UploadRequestFileItem</code> instance.</p>
   * 
   * @param fieldName The name of the form field.
   * @param contentType The content type passed by the browser or
   *        <code>null</code> if not specified.
   * @param isFormField Whether or not this item is a plain form field, as
   *        opposed to a file upload.
   * @param fileName The original filename in the user's filesystem, or
   *        <code>null</code> if not specified.
   * @param sizeThreshold The threshold, in bytes, below which items will be
   *        retained in memory and above which they will be stored as a file.
   * @param repository The data repository, which is the directory in which
   *        files will be created, should the item size exceed the threshold.
   */
  UploadRequestFileItem( final String fieldName,
                         final String contentType,
                         final boolean isFormField,
                         final String fileName,
                         final int sizeThreshold,
                         final File repository )
  {
    this.fieldName = fieldName;
    this.contentType = contentType;
    this.isFormField = isFormField;
    this.fileName = fileName;
    this.sizeThreshold = sizeThreshold;
    this.repository = repository;
  }

  /**
   * <p>returns an {@link java.io.InputStream InputStream}that can be used to
   * retrieve the contents of the file.</p>
   * 
   * @return An {@link java.io.InputStream InputStream}that can be used to
   *         retrieve the contents of the file.
   * @exception IOException if an error occurs.
   */
  public InputStream getInputStream() throws IOException {
    InputStream result = null;
    if( !dfos.isInMemory() ) {
      result = new FileInputStream( dfos.getFile() );
    } else { 
      if( cachedContent == null ) {
        cachedContent = dfos.getData();
      }
      result = new ByteArrayInputStream( cachedContent );
    }
    return result;
  }

  /**
   * <p>returns the content type passed by the browser or <code>null</code> if
   * not defined.</p>
   * 
   * @return The content type passed by the browser or <code>null</code> if
   *         not defined.
   */
  public String getContentType() {
    return contentType;
  }

  /**
   * <p>returns the original filename in the client's filesystem.</p>
   * 
   * @return The original filename in the client's filesystem.
   */
  public String getName() {
    return fileName;
  }

  /**
   * <p>provides a hint as to whether or not the file contents will be read from
   * memory.</p>
   * 
   * @return <code>true</code> if the file contents will be read from memory;
   *         <code>false</code> otherwise.
   */
  public boolean isInMemory() {
    return dfos.isInMemory();
  }

  /**
   * <p>returns the size of the file.</p>
   * 
   * @return The size of the file, in bytes.
   */
  public long getSize() {
    long result = 0;
    if( cachedContent != null ) {
      result = cachedContent.length;
    } else if( dfos.isInMemory() ) {
      result = dfos.getData().length;
    } else {
      result = dfos.getFile().length();
    }
    return result;
  }

  /**
   * <p>returns the contents of the file as an array of bytes. If the contents 
   * of the file were not yet cached in memory, they will be loaded from the 
   * disk storage and cached.</p>
   * 
   * @return The contents of the file as an array of bytes.
   */
  public byte[] get() {
    byte[] result = null;
    if( dfos.isInMemory() ) {
      if( cachedContent == null ) {
        cachedContent = dfos.getData();
      }
      result = cachedContent;
    } else {
      byte[] fileData = new byte[ ( int )getSize() ];
      FileInputStream fis = null;
      try {
        fis = new FileInputStream( dfos.getFile() );
        fis.read( fileData );
      } catch( IOException e ) {
        fileData = null;
      } finally {
        if( fis != null ) {
          try {
            fis.close();
          } catch( IOException e ) {
            // ignore
          }
        }
      }
      result = fileData;
    }
    return result;
  }

  /**
   * <p>returns the contents of the file as a String, using the specified 
   * encoding. This method uses {@link #get()}to retrieve the contents of the 
   * file.</p>
   * 
   * @param encoding The character encoding to use.
   * @return The contents of the file, as a string.
   * @exception UnsupportedEncodingException if the requested character encoding
   *            is not available.
   */
  public String getString( final String encoding )
    throws UnsupportedEncodingException
  {
    return new String( get(), encoding );
  }

  /**
   * <p>returns the contents of the file as a String, using the default 
   * character encoding. This method uses {@link #get()}to retrieve the contents
   * of the file.</p>
   * 
   * @return The contents of the file, as a string.
   */
  public String getString() {
    return new String( get() );
  }

  /**
   * <p>a convenience method to write an uploaded item to disk. The client code 
   * is not concerned with whether or not the item is stored in memory, or on 
   * disk in a temporary location. They just want to write the uploaded item to
   * a file.</p>
   * 
   * <p>This implementation first attempts to rename the uploaded item to the
   * specified destination file, if the item was originally written to disk.
   * Otherwise, the data will be copied to the specified file.</p>
   * 
   * <p>This method is only guaranteed to work <em>once</em>, the first time it
   * is invoked for a particular item. This is because, in the event that the
   * method renames a temporary file, that file will no longer be available to
   * copy or rename again at a later time.</p>
   * 
   * @param file The <code>File</code> into which the uploaded item should be
   *          stored.
   * @exception Exception if an error occurs.
   */
  public void write( final File file ) throws IOException {
    if( isInMemory() ) {
      FileOutputStream fout = null;
      try {
        fout = new FileOutputStream( file );
        fout.write( get() );
      } finally {
        if( fout != null ) {
          fout.close();
        }
      }
    } else {
      File outputFile = getStoreLocation();
      if( outputFile != null ) {
        /*
         * The uploaded file is being stored on disk in a temporary location so
         * move it to the desired file.
         */
        if( !outputFile.renameTo( file ) ) {
          BufferedInputStream in = null;
          BufferedOutputStream out = null;
          try {
            in = new BufferedInputStream( new FileInputStream( outputFile ) );
            out = new BufferedOutputStream( new FileOutputStream( file ) );
            byte[] bytes = new byte[ 2048 ];
            int s = 0;
            while( ( s = in.read( bytes ) ) != -1 ) {
              out.write( bytes, 0, s );
            }
          } finally {
            try {
              if( in != null ) {
                in.close();
              }
            } catch( IOException e ) {
              // ignore
            }
            try {
              if( out != null ) {
                out.close();
              }
            } catch( IOException e ) {
              // ignore
            }
          }
        }
      } else {
        /*
         * For whatever reason we cannot write the file to disk.
         */
        throw new IOException( "Cannot write uploaded file to disk!" );
      }
    }
  }

  /**
   * <p>deletes the underlying storage for a file item, including deleting any
   * associated temporary disk file. Although this storage will be deleted
   * automatically when the <code>FileItem</code> instance is garbage
   * collected, this method can be used to ensure that this is done at an
   * earlier time, thus preserving system resources.</p>
   */
  public void delete() {
    cachedContent = null;
    File outputFile = getStoreLocation();
    if( outputFile != null && outputFile.exists() ) {
      outputFile.delete();
    }
  }

  /**
   * <p>returns the name of the field in the multipart form corresponding to
   * this file item.</p>
   * 
   * @return The name of the form field.
   * @see #setFieldName(java.lang.String)
   */
  public String getFieldName() {
    return fieldName;
  }

  /**
   * <p>sets the field name used to reference this file item.</p>
   * 
   * @param fieldName The name of the form field.
   * @see #getFieldName()
   */
  public void setFieldName( final String fieldName ) {
    this.fieldName = fieldName;
  }

  /**
   * <p>determines whether or not a <code>FileItem</code> instance represents a
   * simple form field.</p>
   * 
   * @return <code>true</code> if the instance represents a simple form field;
   *         <code>false</code> if it represents an uploaded file.
   * @see #setFormField(boolean)
   */
  public boolean isFormField() {
    return isFormField;
  }

  /**
   * <p>specifies whether or not a <code>FileItem</code> instance represents a
   * simple form field.</p>
   * 
   * @param isFormField <code>true</code> if the instance represents a simple 
   *        form field; <code>false</code> if it represents an uploaded file.
   * @see #isFormField()
   */
  public void setFormField( final boolean isFormField ) {
    this.isFormField = isFormField;
  }

  /**
   * <p>returns an {@link java.io.OutputStream OutputStream}that can be used for
   * storing the contents of the file.</p>
   * 
   * @return An {@link java.io.OutputStream OutputStream}that can be used for
   *         storing the contensts of the file.
   * @exception IOException if an error occurs.
   */
  public OutputStream getOutputStream() throws IOException {
    if( dfos == null ) {
      File outputFile = getTempFile();
      if( isFormField() ) {
        dfos = new DeferredFileOutputStream( DEFAULT_OUTPUTSTREAM_SIZE, 
                                             outputFile );
      } else {
        dfos = new DeferredFileOutputStream( sizeThreshold, outputFile );
      }
    }
    return dfos;
  }


  /**
   * <p>returns the {@link java.io.File}object for the <code>FileItem</code>'s
   * data's temporary location on the disk. Note that for <code>FileItem</code>
   * s that have their data stored in memory, this method will return
   * <code>null</code>. When handling large files, you can use
   * {@link java.io.File#renameTo(java.io.File)}to move the file to new
   * location without copying the data, if the source and destination locations
   * reside within the same logical volume.</p>
   * 
   * @return The data file, or <code>null</code> if the data is stored in
   *         memory.
   */
  public File getStoreLocation() {
    return dfos.getFile();
  }

  /**
   * <p>removes the file contents from the temporary storage.</p>
   */
  protected void finalize() {
    File outputFile = dfos.getFile();
    if( outputFile != null && outputFile.exists() ) {
      outputFile.delete();
    }
  }

  /**
   * <p>creates and returns a {@link java.io.File File}representing a uniquely
   * named temporary file in the configured repository path.</p>
   * 
   * @return The {@link java.io.File File}to be used for temporary storage.
   */
  protected File getTempFile() {
    File tempDir = repository;
    if( tempDir == null ) {
      tempDir = new File( System.getProperty( "java.io.tmpdir" ) );
    }
    String fileName = "upload_" + getUniqueId() + ".tmp";
    File result = new File( tempDir, fileName );
    result.deleteOnExit();
    return result;
  }

  /**
   * <p>returns an identifier that is unique within the class loader used to
   * load this class, but does not have random-like apearance.</p>
   * 
   * @return A String with the non-random looking instance identifier.
   */
  private static String getUniqueId() {
    int current;
    synchronized( UploadRequestFileItem.class ) {
      current = counter++;
    }
    String result = Integer.toString( current );
    // If you manage to get more than 100 million of ids, you'll
    // start getting ids longer than 8 characters.
    if( current < 100000000 ) {
      result = ( "00000000" + result ).substring( result.length() );
    }
    return result;
  }
}