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
package com.w4t.engine.lifecycle.standard;

import junit.framework.TestCase;
import com.w4t.Fixture;
import com.w4t.IWindowManager.IWindow;
import com.w4t.engine.lifecycle.standard.lifecycletestformkit.LifeCycleTestFormRenderer;
import com.w4t.engine.util.FormManager;
import com.w4t.engine.util.WindowManager;
import com.w4t.util.browser.Default;


public class LifeCycleAdapter_Test extends TestCase {
  
  private String log;
  
  protected void setUp() throws Exception {
    Fixture.setUp();
    Fixture.createContext();
    Fixture.fakeBrowser( new Default( false ) );
    log = "";
  }
  
  protected void tearDown() throws Exception {
    Fixture.tearDown();
    Fixture.removeContext();
    log = "";
  }

  public void testLifeCycleAdapter() throws Exception {
    final ILog logHandler = new ILog() {
      public void log( final String message ) {
        log += message;
      }
    };
    LifeCycleTestForm form = new LifeCycleTestForm( logHandler );
    ILifeCycleAdapter adapter
      = ( ILifeCycleAdapter )form.getAdapter( ILifeCycleAdapter.class );
    assertNotNull( adapter );
    
    adapter.readData();
    String readData = LifeCycleTestFormRenderer.READ_DATA_NOSCRIPT;
    assertEquals( readData, log );
    
    log = "";
    adapter.processAction();
    String process = LifeCycleTestFormRenderer.PROCESS_EVENT_NOSCRIPT;
    assertEquals( process, log );
    
    log = "";
    FormManager.add( form );
    FormManager.setActive( form );
    IWindow window = WindowManager.getInstance().create( form );
    WindowManager.setActive( window );
    
    adapter.render();
    String render = LifeCycleTestFormRenderer.RENDER_NOSCRIPT;
    assertEquals( render, log );
  }
}
