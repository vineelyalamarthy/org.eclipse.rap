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

import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import junit.framework.TestCase;

import org.eclipse.rwt.internal.resources.ResourceManagerImpl;
import org.eclipse.rwt.internal.service.*;
import org.eclipse.rwt.internal.util.HTML;
import org.eclipse.rwt.resources.IResourceManager;

import com.w4t.W4TFixture;
import com.w4t.W4TContext;
import com.w4t.W4TFixture.TestResponse;
import com.w4t.W4TFixture.TestServletOutputStream;
import com.w4t.engine.W4TModelUtil;

public class ResourceServiceHandler_Test extends TestCase {
  
  
  private static final String UNKNOWN_RESOURCE_XYZ 
    = "com/w4t/engine/service/unknown-resource.xyz";
  private static final String RESOURCETEST1_JS 
    = "com/w4t/engine/util/resourcetest1.js";


  protected void setUp() throws Exception {
    W4TFixture.setUp();
    W4TFixture.createContext( false );
    ResourceManagerImpl.setDeliveryMode( ResourceManagerImpl.DELIVER_BY_SERVLET );
    // place output stream to 'see' what has been written
    HttpServletResponse response = ContextProvider.getResponse();
    W4TFixture.TestResponse testResponse = ( TestResponse )response;
    ServletOutputStream outStream = new TestServletOutputStream();
    testResponse.setOutputStream( outStream );
  }
  
  protected void tearDown() throws Exception {
    W4TFixture.tearDown();
    W4TFixture.removeContext();
  }
  
  public void testUnknown() throws Exception {
    W4TModelUtil.initModel();
    IResourceManager resourceManager = W4TContext.getResourceManager();
    resourceManager.register( UNKNOWN_RESOURCE_XYZ );
    W4TFixture.fakeRequestParam( RequestParams.RESOURCE, UNKNOWN_RESOURCE_XYZ );
    W4TFixture.fakeRequestParam( RequestParams.RESOURCE_VERSION, null );
    ServiceManager.getHandler().service();
    byte[] response = getResponseBytes();
    byte[] expected = "xyz".getBytes();
    assertEquals( expected.length, response.length );
    for( int i = 0; i < response.length; i++ ) {
      assertEquals( expected[ i ], response[ i ] );
    }
    assertEquals( "content/unknown", getResponseContentType() );
  }
  
  public void testUnversionedJavaScript() throws Exception {
    W4TModelUtil.initModel();
    IResourceManager resourceManager = W4TContext.getResourceManager();
    resourceManager.register( RESOURCETEST1_JS, 
                              HTML.CHARSET_NAME_ISO_8859_1, 
                              IResourceManager.RegisterOptions.NONE );
    W4TFixture.fakeRequestParam( RequestParams.RESOURCE, RESOURCETEST1_JS );
    W4TFixture.fakeRequestParam( RequestParams.RESOURCE_VERSION, null );
    ServiceManager.getHandler().service();
    byte[] response = getResponseBytes();
    assertTrue( response.length > 0 );
    assertEquals( "text/javascript", getResponseContentType() );
  }
  
  public void testVersionedJavaScript() throws Exception {
    W4TModelUtil.initModel();
    IResourceManager resourceManager = W4TContext.getResourceManager();
    resourceManager.register( RESOURCETEST1_JS, 
                              HTML.CHARSET_NAME_ISO_8859_1, 
                              IResourceManager.RegisterOptions.VERSION );
    W4TFixture.fakeRequestParam( RequestParams.RESOURCE, RESOURCETEST1_JS );
    String version 
      = String.valueOf( ResourceManagerImpl.findVersion( RESOURCETEST1_JS ) );
    W4TFixture.fakeRequestParam( RequestParams.RESOURCE_VERSION, version );
    ServiceManager.getHandler().service();
    byte[] response = getResponseBytes();
    assertTrue( response.length > 0 );
    assertEquals( "text/javascript", getResponseContentType() );
  }
  
  private byte[] getResponseBytes() throws IOException {
    HttpServletResponse response = ContextProvider.getResponse();
    TestServletOutputStream outStream 
      = ( TestServletOutputStream )response.getOutputStream();
    return outStream.getContent().toByteArray();
  }
  
  private String getResponseContentType() {
    HttpServletResponse response = ContextProvider.getResponse();
    W4TFixture.TestResponse testResponse = ( TestResponse )response;
    return testResponse.getContentType();
  }
}