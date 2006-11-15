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
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.*;
import junit.framework.TestCase;
import com.w4t.Fixture;
import com.w4t.WebForm;
import com.w4t.Fixture.*;
import com.w4t.engine.requests.RequestParams;
import com.w4t.engine.util.WindowManager;


public class Logger_Test extends TestCase {

  private static final class TestHandler extends Handler {
    
    private final List records = new ArrayList();

    public void close() throws SecurityException {
      // do nothing
    }
    
    public void flush() {
      // do nothing
    }
    
    public void reset() {
      records.clear();
    }

    public void publish( final LogRecord record ) {
      records.add( record );
    }
    
    public LogRecord[] getRecords() {
      return ( LogRecord[] )records.toArray( new LogRecord[ records.size() ] );
    }
  }
  
  protected void setUp() throws Exception {
    Fixture.setUp();
    Fixture.createContext( false );
  }
  
  protected void tearDown() throws Exception {
    Fixture.tearDown();
    Fixture.removeContext();
    removeAllTestHandlers();
  }
  
  public void testRequestHeaderLogging() throws Exception {
    configureLogManager( "log-headers.properties" );
    TestHandler handler = installTestHandler();
    WebForm form = Fixture.loadStartupForm();
    WindowManager.getInstance().create( form );
    TestRequest request = ( TestRequest )ContextProvider.getRequest();
    request.setHeader( "test-header", "test-value" );
    TestResponse response = ( TestResponse )ContextProvider.getResponse();
    response.setOutputStream( new TestServletOutputStream() );
    ServiceManager.getHandler().service();
    LogRecord[] records = handler.getRecords();
    assertEquals( 1, records.length );
    assertTrue( records[ 0 ].getMessage().indexOf( "test-header" ) > -1 );
  }
  
  public void testRequestParameterLogging() throws Exception {
    configureLogManager( "log-params.properties" );
    TestHandler handler = installTestHandler();
    WebForm form = Fixture.loadStartupForm();
    WindowManager.getInstance().create( form );
    TestRequest request = ( TestRequest )ContextProvider.getRequest();
    request.setParameter( "test-parameter", "test-value" );
    TestResponse response = ( TestResponse )ContextProvider.getResponse();
    response.setOutputStream( new TestServletOutputStream() );
    ServiceManager.getHandler().service();
    LogRecord[] records = handler.getRecords();
    assertEquals( 1, records.length );
    assertTrue( records[ 0 ].getMessage().indexOf( "test-parameter" ) > -1 );
  }
  
  public void testResponseContentLogging() throws Exception {
    configureLogManager( "log-content.properties" );
    TestHandler handler = installTestHandler();
    WebForm form = Fixture.loadStartupForm();
    WindowManager.getInstance().create( form );
    TestResponse response = ( TestResponse )ContextProvider.getResponse();
    response.setOutputStream( new TestServletOutputStream() );
    ServiceManager.getHandler().service();
    LogRecord[] records = handler.getRecords();
    assertEquals( 1, records.length );
    assertEquals( FormRequestServiceHandler.LOG_RESPONSE_CONTENT, 
                  records[ 0 ].getLoggerName() );
    assertTrue( records[ 0 ].getMessage().indexOf( "<!DOCTYPE HTML" ) > -1 );
  }
  
  public void testTimestampLogging() throws Exception {
    configureLogManager( "log-timestamp.properties" );
    TestHandler handler = installTestHandler();
    TestResponse response = ( TestResponse )ContextProvider.getResponse();
    response.setOutputStream( new TestServletOutputStream() );

    new TimestampRequestServiceHandler().service();
    LogRecord[] records = handler.getRecords();
    assertEquals( 1, records.length );
    assertEquals( TimestampRequestServiceHandler.LOG, 
                  records[ 0 ].getLoggerName() );
  
    handler.reset();
    Fixture.fakeRequestParam( RequestParams.UIROOT, "xyz;abc" );
    new TimestampRequestServiceHandler().service();
    records = handler.getRecords();
    assertEquals( 1, records.length );
    assertEquals( TimestampRequestServiceHandler.LOG, 
                  records[ 0 ].getLoggerName() );
  }
  
  private static TestHandler installTestHandler() {
    TestHandler result = new TestHandler();
    result.reset();
    result.setLevel( Level.ALL );
    Logger.getLogger( "" ).addHandler( result );
    return result;
  }

  private static void removeAllTestHandlers() {
    Logger logger = Logger.getLogger( "" );
    Handler[] handlers = logger.getHandlers();
    for( int i = 0; i < handlers.length; i++ ) {
      if( handlers[ i ] instanceof TestHandler ) {
        logger.removeHandler( handlers[ i ] );
      }
    }
  }

  private static void configureLogManager( final String resource ) 
    throws IOException 
  {
    InputStream stream = Logger_Test.class.getResourceAsStream( resource );
    try {
      LogManager.getLogManager().readConfiguration( stream );
    } finally {
      stream.close();
    }
  }
}
