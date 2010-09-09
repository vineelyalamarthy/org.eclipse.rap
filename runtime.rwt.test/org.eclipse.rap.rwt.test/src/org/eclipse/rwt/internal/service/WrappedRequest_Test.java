/*******************************************************************************
 * Copyright (c) 2002, 2010 Innoopract Informationssysteme GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Innoopract Informationssysteme GmbH - initial API and implementation
 *     EclipseSource - ongoing development
 ******************************************************************************/
package org.eclipse.rwt.internal.service;

import java.io.IOException;
import java.util.*;

import junit.framework.TestCase;

import org.eclipse.rwt.*;
import org.eclipse.rwt.internal.lifecycle.EntryPointManager;
import org.eclipse.rwt.internal.lifecycle.RWTLifeCycle;
import org.eclipse.rwt.lifecycle.IEntryPoint;
import org.eclipse.swt.widgets.Display;


public class WrappedRequest_Test extends TestCase {
  
  public static final class DefaultEntryPoint implements IEntryPoint {
    public int createUI() {
      Display display = new Display();
      if( display.readAndDispatch() ) {
        display.sleep();
      }
      return 0;
    }
  }

  public void testAdditionalParameters() {
    TestRequest original = new TestRequest();
    String p0 = "p0";
    String v0 = "v0";
    original.setParameter( p0, v0 );
    String p1 = "p1";
    String v1a = "v1a";
    String v1b = "v1b";
    original.addParameter( p1, v1a );
    original.addParameter( p1, v1b );
    
    Map paramMap = new HashMap();
    String p2 = "p2";
    String v2 = "v2";
    String p3 = "p3";
    String v3a = "v3a";
    String v3b = "v3b";
    paramMap.put( p2, new String[] { v2 } );    
    paramMap.put( p3, new String[] { v3a, v3b } );    
    WrappedRequest wrapper = new WrappedRequest( original, paramMap );
    
    assertEquals( v0, wrapper.getParameter( p0 ) );
    assertEquals( v1a, wrapper.getParameter( p1 ) );
    assertEquals( v2, wrapper.getParameter( p2 ) );
    assertEquals( v3a, wrapper.getParameter( p3 ) );
    
    Enumeration parameterNames = wrapper.getParameterNames();
    Set names = new HashSet();
    while( parameterNames.hasMoreElements() ) {
      names.add(  parameterNames.nextElement() );
    }
    assertTrue( names.contains( p0 ) );
    assertTrue( names.contains( p1 ) );
    assertTrue( names.contains( p2 ) );
    assertTrue( names.contains( p3 ) );
    
    assertEquals( v0, wrapper.getParameterValues( p0 )[ 0 ] );
    assertEquals( v1a, wrapper.getParameterValues( p1 )[ 0 ] );
    assertEquals( v1b, wrapper.getParameterValues( p1 )[ 1 ] );
    assertEquals( v2, wrapper.getParameterValues( p2 )[ 0 ] );
    assertEquals( v3a, wrapper.getParameterValues( p3 )[ 0 ] );
    assertEquals( v3b, wrapper.getParameterValues( p3 )[ 1 ] );
    
    Map parameterMap = wrapper.getParameterMap();
    assertEquals( v0, ( ( String[] )parameterMap.get( p0 ) )[ 0 ] );
    assertEquals( v1a, ( ( String[] )parameterMap.get( p1 ) )[ 0 ] );
    assertEquals( v1b, ( ( String[] )parameterMap.get( p1 ) )[ 1 ] );
    assertEquals( v2, ( ( String[] )parameterMap.get( p2 ) )[ 0 ] );
    assertEquals( v3a, ( ( String[] )parameterMap.get( p3 ) )[ 0 ] );
    assertEquals( v3b, ( ( String[] )parameterMap.get( p3 ) )[ 1 ] );

    try {
      parameterMap.clear();
      fail();
    } catch( UnsupportedOperationException usoe ) {
    }
  }
  
  public void testStartupRequestWithParameter() throws Exception {
    System.setProperty( "lifecycle", RWTLifeCycle.class.getName() );
    StartupPage.IStartupPageConfigurer bufferedConfigurer 
      = StartupPage.configurer;
    StartupPage.configurer = new StartupPage.IStartupPageConfigurer() {
      public StartupPageTemplateHolder getTemplate() throws IOException {
        return new StartupPageTemplateHolder( "Startup Page" );
      }
      public boolean isModifiedSince() {
        return true;
      }
    };

    String p1 = "p1";
    String v1 = "v1";
    Fixture.fakeRequestParam( p1, v1 );
    ServiceManager.getHandler().service();
    String allMarkup = Fixture.getAllMarkup();
    assertTrue( allMarkup.indexOf( "Startup Page" ) != -1 );
    
    EntryPointManager.register( EntryPointManager.DEFAULT, 
                                DefaultEntryPoint.class );
    Fixture.fakeRequestParam( p1, null );
    Fixture.fakeRequestParam( RequestParams.STARTUP, 
                              EntryPointManager.DEFAULT );
    Fixture.fakeRequestParam( LifeCycleServiceHandler.RWT_INITIALIZE, "true" );
    TestRequest request = ( TestRequest )ContextProvider.getRequest();
    request.setHeader( "User-Agent", "myAgent" );
    ServiceManager.getHandler().service();

    assertEquals( v1, ContextProvider.getRequest().getParameter( p1 ) );
    StartupPage.configurer = bufferedConfigurer;
    System.getProperties().remove( "lifecycle" );
    EntryPointManager.deregister( EntryPointManager.DEFAULT );
  }
  
  protected void setUp() throws Exception {
    Fixture.setUp();
    Fixture.createContext( false );
    TestResponse response = ( TestResponse )ContextProvider.getResponse();
    response.setOutputStream( new TestServletOutputStream() );
  }
  
  protected void tearDown() throws Exception {
    Fixture.tearDown();
    Fixture.removeContext();
  }
}
