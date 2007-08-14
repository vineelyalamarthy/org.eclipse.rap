// Created on 18.10.2006
package com.w4t.engine;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import junit.framework.TestCase;

import org.eclipse.rwt.internal.service.*;
import org.eclipse.rwt.service.ServiceManager;

import com.w4t.W4TFixture;
import com.w4t.W4TFixture.*;


public class FirstAccess_Test extends TestCase {
  
  protected void setUp() throws Exception {
    W4TFixture.setUp();
    W4TFixture.createContext( false );
    TestResponse response = ( TestResponse )ContextProvider.getResponse();
    response.setOutputStream( new TestServletOutputStream() );
    HttpServletRequest request = ContextProvider.getRequest();
    TestSession session = ( TestSession )request.getSession();
    session.setNew( true );
  }
  
  protected void tearDown() throws Exception {
    W4TFixture.tearDown();
    W4TFixture.removeContext();
  }
  
  public void testStartupWithoutParams() throws IOException, ServletException {
    ServiceManager.getHandler().service();
    String allMarkup = W4TFixture.getAllMarkup();
    int contains = allMarkup.indexOf( "W4Toolkit Startup Page" );
    assertTrue( contains != -1 );
    assertTrue( ContextProvider.getStateInfo().isFirstAccess() );

    createNewContext();
    W4TFixture.fakeRequestParam( RequestParams.STARTUP, "xyz" );
    W4TFixture.fakeRequestParam( RequestParams.SCRIPT, "true" );
    W4TFixture.fakeRequestParam( RequestParams.AJAX_ENABLED, "true" );
    String userAgent = "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)";
    W4TFixture.fakeUserAgent( userAgent );
    ServiceManager.getHandler().service();
    allMarkup = W4TFixture.getAllMarkup();
    contains = allMarkup.indexOf( "This is the W4Toolkit W4TFixture Form!" );
    assertTrue( contains != -1 );
    assertTrue( ContextProvider.getStateInfo().isFirstAccess() );

    createNewContext();
    W4TFixture.fakeRequestParam( RequestParams.AJAX_ENABLED, "true" );
    W4TFixture.fakeRequestParam( RequestParams.UIROOT, "w1;p1" );
    W4TFixture.fakeRequestParam( RequestParams.REQUEST_COUNTER, "0" );
    ServiceManager.getHandler().service();
    contains = allMarkup.indexOf( "This is the W4Toolkit W4TFixture Form!" );
    assertTrue( contains != -1 );
    assertFalse( ContextProvider.getStateInfo().isFirstAccess() );
  }
  
  public void testStartupWithParams() throws IOException, ServletException {
    W4TFixture.fakeRequestParam( RequestParams.STARTUP, "xyz" );
    W4TFixture.fakeRequestParam( RequestParams.SCRIPT, "true" );
    W4TFixture.fakeRequestParam( RequestParams.AJAX_ENABLED, "true" );
    String userAgent = "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)";
    W4TFixture.fakeUserAgent( userAgent );
    ServiceManager.getHandler().service();
    String allMarkup = W4TFixture.getAllMarkup();
    int contains = allMarkup.indexOf( "This is the W4Toolkit W4TFixture Form!" );
    assertTrue( contains != -1 );
    assertTrue( ContextProvider.getStateInfo().isFirstAccess() );
    
     createNewContext();
     W4TFixture.fakeRequestParam( RequestParams.AJAX_ENABLED, "true" );
     W4TFixture.fakeRequestParam( RequestParams.UIROOT, "w1;p1" );
     W4TFixture.fakeRequestParam( RequestParams.REQUEST_COUNTER, "0" );
     ServiceManager.getHandler().service();
     contains = allMarkup.indexOf( "This is the W4Toolkit W4TFixture Form!" );
     assertTrue( contains != -1 );
     assertFalse( ContextProvider.getStateInfo().isFirstAccess() );
     
     createNewContext();
     W4TFixture.fakeRequestParam( RequestParams.STARTUP, "xyz" );
     ServiceManager.getHandler().service();
     contains = allMarkup.indexOf( "This is the W4Toolkit W4TFixture Form!" );
     assertTrue( contains != -1 );
     assertTrue( ContextProvider.getStateInfo().isFirstAccess() );
     
  }
  
  private void createNewContext() {
    HttpServletRequest request = ContextProvider.getRequest();
    TestSession session = ( TestSession )request.getSession();
    session.setNew( false );
    ContextProvider.disposeContext();
    TestRequest testRequest = new TestRequest();
    testRequest.setSession( session );
    TestResponse testResponse = new TestResponse();
    testResponse.setOutputStream( new TestServletOutputStream() );
    ServiceContext context = new ServiceContext( testRequest, testResponse );
    ContextProvider.setContext( context );
  }

}
