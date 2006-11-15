// Created on 18.10.2006
package com.w4t.engine;

import java.io.IOException;
import javax.servlet.ServletException;
import junit.framework.TestCase;
import com.w4t.Fixture;
import com.w4t.Fixture.*;
import com.w4t.engine.requests.RequestParams;
import com.w4t.engine.service.*;


public class FirstAccess_Test extends TestCase {
  
  protected void setUp() throws Exception {
    Fixture.setUp();
    Fixture.createContext( false );
    TestResponse response = ( TestResponse )ContextProvider.getResponse();
    response.setOutputStream( new TestServletOutputStream() );
    TestSession session = ( TestSession )ContextProvider.getSession();
    session.setNew( true );
  }
  
  protected void tearDown() throws Exception {
    Fixture.tearDown();
    Fixture.removeContext();
  }
  
  public void testStartupWithoutParams() throws IOException, ServletException {
    ServiceManager.getHandler().service();
    String allMarkup = Fixture.getAllMarkup();
    int contains = allMarkup.indexOf( "W4Toolkit Startup Page" );
    assertTrue( contains != -1 );
    assertTrue( ContextProvider.getStateInfo().isFirstAccess() );

    createNewContext();
    Fixture.fakeRequestParam( RequestParams.STARTUP, "xyz" );
    Fixture.fakeRequestParam( RequestParams.SCRIPT, "true" );
    Fixture.fakeRequestParam( RequestParams.AJAX_ENABLED, "true" );
    String userAgent = "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)";
    Fixture.fakeUserAgent( userAgent );
    ServiceManager.getHandler().service();
    allMarkup = Fixture.getAllMarkup();
    contains = allMarkup.indexOf( "This is the W4Toolkit Fixture Form!" );
    assertTrue( contains != -1 );
    assertTrue( ContextProvider.getStateInfo().isFirstAccess() );

    createNewContext();
    Fixture.fakeRequestParam( RequestParams.AJAX_ENABLED, "true" );
    Fixture.fakeRequestParam( RequestParams.UIROOT, "w1;p1" );
    Fixture.fakeRequestParam( RequestParams.REQUEST_COUNTER, "0" );
    ServiceManager.getHandler().service();
    contains = allMarkup.indexOf( "This is the W4Toolkit Fixture Form!" );
    assertTrue( contains != -1 );
    assertFalse( ContextProvider.getStateInfo().isFirstAccess() );
  }
  
  public void testStartupWithParams() throws IOException, ServletException {
    Fixture.fakeRequestParam( RequestParams.STARTUP, "xyz" );
    Fixture.fakeRequestParam( RequestParams.SCRIPT, "true" );
    Fixture.fakeRequestParam( RequestParams.AJAX_ENABLED, "true" );
    String userAgent = "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)";
    Fixture.fakeUserAgent( userAgent );
    ServiceManager.getHandler().service();
    String allMarkup = Fixture.getAllMarkup();
    int contains = allMarkup.indexOf( "This is the W4Toolkit Fixture Form!" );
    assertTrue( contains != -1 );
    assertTrue( ContextProvider.getStateInfo().isFirstAccess() );
    
     createNewContext();
     Fixture.fakeRequestParam( RequestParams.AJAX_ENABLED, "true" );
     Fixture.fakeRequestParam( RequestParams.UIROOT, "w1;p1" );
     Fixture.fakeRequestParam( RequestParams.REQUEST_COUNTER, "0" );
     ServiceManager.getHandler().service();
     contains = allMarkup.indexOf( "This is the W4Toolkit Fixture Form!" );
     assertTrue( contains != -1 );
     assertFalse( ContextProvider.getStateInfo().isFirstAccess() );
     
     createNewContext();
     Fixture.fakeRequestParam( RequestParams.STARTUP, "xyz" );
     ServiceManager.getHandler().service();
     contains = allMarkup.indexOf( "This is the W4Toolkit Fixture Form!" );
     assertTrue( contains != -1 );
     assertTrue( ContextProvider.getStateInfo().isFirstAccess() );
     
  }
  
  private void createNewContext() {
    TestSession session = ( TestSession )ContextProvider.getSession();
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
