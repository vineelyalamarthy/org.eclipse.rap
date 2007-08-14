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

import java.io.*;
import junit.framework.TestCase;
import org.apache.commons.fileupload.FileItem;
import com.w4t.event.WebFileUploadEvent;
import com.w4t.event.WebFileUploadListener;
import com.w4t.internal.adaptable.IFileUploadAdapter;


public class WebFileUpload_Test extends TestCase {

  
  public WebFileUpload_Test( String arg0 ) {
    super( arg0 );
  }
  
  protected void setUp() throws Exception {
    W4TFixture.setUp();
  }
  
  protected void tearDown() throws Exception {
    W4TFixture.tearDown();
  }

  public void testProcessEvent() throws Exception {
    WebFileUpload wfu = new WebFileUpload();
    final boolean[] result = new boolean[] { false };
    wfu.addWebFileUploadListener( new WebFileUploadListener () {
      public void fileUploaded( WebFileUploadEvent e ) {
        result[ 0 ] = true;        
      } 
    } );
    IFileUploadAdapter adapter 
      = ( IFileUploadAdapter )wfu.getAdapter( IFileUploadAdapter.class );
    TestFileItem item = new TestFileItem();
    adapter.setFileItem( item );
    int id = WebFileUploadEvent.FILEUPLOADED;
    new WebFileUploadEvent( wfu, id ).processEvent();
    assertTrue( "Event was not fired!", result[ 0 ] );
  }

  public void testSetSize() {
    boolean result = false;
    WebFileUpload wfu = new WebFileUpload();
    String msg =   "Wrong default SizeAttribute! Should be -1 is " 
      + wfu.getSize();
    result = wfu.getSize() == -1;
    assertTrue( msg, result );

    wfu.setSize( -1 );
    result = wfu.getSize() == -1;
    assertTrue( msg, result );

    wfu.setSize( -10 );
    result = wfu.getSize() == -1;
    assertTrue( msg, result );

    msg = "Wrong SizeAttribute! Should be 0 is " + wfu.getSize();

    wfu.setSize( 0 );
    result = wfu.getSize() == 0;
    assertTrue( msg, result );

    msg = "Wrong SizeAttribute! Should be 10 is " + wfu.getSize();

    wfu.setSize( 10 );
    result = wfu.getSize() == 10;
    assertTrue( msg, result );

  }
  
  
  ////////////////
  // inner classes
  
  private final class TestFileItem implements FileItem {

    private static final long serialVersionUID = 1L;

    public InputStream getInputStream() throws IOException {
      return null;
    }

    public String getContentType() {
      return null;
    }

    public String getName() {
      return null;
    }

    public boolean isInMemory() {
      return false;
    }

    public long getSize() {
      return 0;
    }

    public byte[] get() {
      return null;
    }

    public String getString( String arg0 ) throws UnsupportedEncodingException {
      return null;
    }

    public String getString() {
      return null;
    }

    public void write( File arg0 ) throws Exception {
    }

    public void delete() {
    }

    public String getFieldName() {
      return null;
    }

    public void setFieldName( String arg0 ) {
    }

    public boolean isFormField() {
      return false;
    }

    public void setFormField( boolean arg0 ) {
    }

    public OutputStream getOutputStream() throws IOException {
      return null;
    }
  }
}
