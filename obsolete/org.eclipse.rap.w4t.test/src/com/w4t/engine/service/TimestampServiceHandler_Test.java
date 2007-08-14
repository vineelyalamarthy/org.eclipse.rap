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

import junit.framework.TestCase;

import org.eclipse.rwt.internal.service.ContextProvider;
import org.eclipse.rwt.internal.service.RequestParams;
import org.eclipse.rwt.internal.util.HTML;
import org.eclipse.rwt.service.ServiceManager;

import com.w4t.*;
import com.w4t.W4TFixture.TestResponse;
import com.w4t.W4TFixture.TestServletOutputStream;
import com.w4t.IWindowManager.IWindow;
import com.w4t.engine.util.FormManager;
import com.w4t.engine.util.WindowManager;
import com.w4t.internal.adaptable.IFormAdapter;


public class TimestampServiceHandler_Test extends TestCase {

  protected void setUp() throws Exception {
    W4TFixture.setUp();
    W4TFixture.createContext();
  }
  
  protected void tearDown() throws Exception {
    W4TFixture.tearDown();
    W4TFixture.removeContext();
  }
  
  public void testValidRequest1() throws Exception {
    WebForm form = W4TFixture.loadStartupForm();
    FormManager.setActive( form );
    IWindow window = WindowManager.getInstance().create( form );
    WindowManager.setActive( window );
    TestResponse response = ( TestResponse )ContextProvider.getResponse();
    TestServletOutputStream outputStream = new TestServletOutputStream();
    response.setOutputStream( outputStream );
    W4TFixture.fakeRequestParam( RequestParams.REQUEST_TIMESTAMP_NAME, "true" );
    String uiRootId = LifeCycleHelper.createUIRootId();
    W4TFixture.fakeRequestParam( RequestParams.UIROOT, uiRootId );
    
    ServiceManager.getHandler().service();
    assertEquals( HTML.CONTENT_IMAGE_GIF, response.getContentType() );
    assertEquals( TimestampRequestServiceHandler.PLACE_HOLDER,
                  toIntArray( outputStream.getContent().toByteArray() ) );
    assertTrue( getTimestamp( form ) > 0 );
  }
  
  public void testValidRequest2() throws Exception {
    WebForm form = W4TFixture.loadStartupForm();
    FormManager.setActive( form );
    IWindow window = WindowManager.getInstance().create( form );
    WindowManager.setActive( window );
    TestResponse response = ( TestResponse )ContextProvider.getResponse();
    TestServletOutputStream outputStream = new TestServletOutputStream();
    response.setOutputStream( outputStream );
    W4TFixture.fakeRequestParam( RequestParams.REQUEST_TIMESTAMP_NAME, "true" );
    String uiRootId = LifeCycleHelper.createUIRootId();
    W4TFixture.fakeRequestParam( RequestParams.UIROOT, uiRootId);
    W4TFixture.fakeRequestParam( "some-other-param", "random-value" );
    
    ServiceManager.getHandler().service();
    assertEquals( HTML.CONTENT_IMAGE_GIF, response.getContentType() );
    assertEquals( TimestampRequestServiceHandler.PLACE_HOLDER,
                  toIntArray( outputStream.getContent().toByteArray() ) );
    assertTrue( getTimestamp( form ) > 0 );
  }
  
  public void testInvalidRequest1() throws Exception {
    WebForm form = W4TFixture.loadStartupForm();
    long oldTimestamp = getTimestamp( form );
    TestResponse response = ( TestResponse )ContextProvider.getResponse();
    TestServletOutputStream outputStream = new TestServletOutputStream();
    response.setOutputStream( outputStream );
    W4TFixture.fakeRequestParam( RequestParams.REQUEST_TIMESTAMP_NAME, "true" );

    ServiceManager.getHandler().service();
    assertEquals( 0, outputStream.getContent().toByteArray().length );
    assertEquals( oldTimestamp, getTimestamp( form ) );
  }
  
  public void testInvalidRequest2() throws Exception {
    WebForm form = W4TFixture.loadStartupForm();
    long oldTimestamp = getTimestamp( form );
    TestResponse response = ( TestResponse )ContextProvider.getResponse();
    TestServletOutputStream outputStream = new TestServletOutputStream();
    response.setOutputStream( outputStream );
    W4TFixture.fakeRequestParam( RequestParams.REQUEST_TIMESTAMP_NAME, "true" );
    W4TFixture.fakeRequestParam( "invalid-param", "whatever" );
    
    ServiceManager.getHandler().service();
    assertEquals( 0, outputStream.getContent().toByteArray().length );
    assertEquals( oldTimestamp, getTimestamp( form ) );
  }
  
  private static void assertEquals( final int[] expected, final int[] actual ) 
  {
    assertEquals( expected.length, actual.length );
    for( int i = 0; i < expected.length; i++ ) {
      assertEquals( expected[ i ], actual[ i ] );
    }
  }
  
  private static int[] toIntArray( final byte[] array ) {
    int[] result = new int[ array.length ];
    for( int i = 0; i < array.length; i++ ) {
      result[ i ] = array[ i ] & 0xFF;
    }
    return result;
  }
  
  private static long getTimestamp( final WebForm form ) {
    IFormAdapter adapter 
      = ( IFormAdapter )form.getAdapter( IFormAdapter.class );
    return adapter.getTimeStamp();
  }
}
