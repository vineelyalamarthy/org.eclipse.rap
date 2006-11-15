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
import javax.servlet.ServletException;
import junit.framework.TestCase;
import com.w4t.Fixture;


public class ServiceHandler_Test extends TestCase {
  
  private final static String id 
    = "com.w4t.engine.service.ServiceHandler_Test.CustomHandler";
  private static final String SERVICE_DONE = "service done";
  private static String log = "";
  
  public static class CustomHandler implements IServiceHandler {
    public void service() throws ServletException, IOException {
      log = SERVICE_DONE;
    }    
  }
  
  protected void setUp() throws Exception {
    Fixture.setUp();
    Fixture.createContext( false );
  }
  
  protected void tearDown() throws Exception {
    Fixture.tearDown();
    Fixture.removeContext();
    log = "";
  }
  
  public void testCustomServiceHandler() throws Exception {
    Fixture.fakeRequestParam( IServiceHandler.REQUEST_PARAM, id );
    ServiceManager.getHandler().service();
    assertEquals( log, SERVICE_DONE );
  }
}
