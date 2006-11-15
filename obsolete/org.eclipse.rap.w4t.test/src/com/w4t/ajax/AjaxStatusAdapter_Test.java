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
package com.w4t.ajax;

import junit.framework.TestCase;
import com.w4t.*;

public class AjaxStatusAdapter_Test extends TestCase {
  
  private final AdapterFactory adapterFactory = new AjaxStatusAdapterFactory();
  
  public void testFactory() throws Exception {
    MyButton button = new MyButton();
    Object adapter = adapterFactory.getAdapter( button, AjaxStatus.class );
    assertTrue( adapter instanceof AjaxStatus );
    assertSame( adapter, adapterFactory.getAdapter( button, AjaxStatus.class ) );
    
    Object adapter2 = adapterFactory.getAdapter( button, Runnable.class );
    assertNull( adapter2 );
  }
  
  public void testAdaptable() throws Exception {
    MyButton button = new MyButton();
    Object adapter = button.getAdapter( AjaxStatus.class );
    assertTrue( adapter instanceof AjaxStatus  );
    
    Object adapter2 = button.getAdapter( Runnable.class );
    assertNull( adapter2 );
  }
  
  protected void setUp() throws Exception {
    Fixture.setUp();
    Fixture.createContext();
  }
  
  protected void tearDown() throws Exception {
    Fixture.tearDown();
    Fixture.removeContext();
  }
  
  private static class MyButton extends WebButton {
  }
}
