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
package com.w4t.engine.util;

import junit.framework.TestCase;
import com.w4t.Fixture;
import com.w4t.WebForm;
import com.w4t.IWindowManager.IWindow;


public class FormManager_Test extends TestCase {
  
  protected void setUp() throws Exception {
    Fixture.setUp();
    Fixture.createContext();
  }
  
  protected void tearDown() throws Exception {
    Fixture.tearDown();
    Fixture.removeContext();
  }
  
  public void testFlat() throws Exception {
    WebForm form = Fixture.loadStartupForm();
    WebForm formToDispatch = Fixture.loadStartupForm();
    IWindow window = WindowManager.getInstance().create(form);
    WindowManager.setActive( window );
    IWindow activeWindow = WindowManager.getActive();
    activeWindow.setFormToDispatch( formToDispatch );
    assertEquals( formToDispatch, activeWindow.getFormToDispatch() );
    
    WebForm[] all = FormManager.getAll();
    assertEquals( 2, all.length );
    
    WebForm foundForm = FormManager.findById( form.getUniqueID() );
    assertSame( foundForm, form );
    
    FormManager.remove( form );
    foundForm = FormManager.findById( form.getUniqueID() );
    assertNull( foundForm );
    all = FormManager.getAll();
    assertEquals( 1, all.length );
    FormManager.remove( formToDispatch );
    all = FormManager.getAll();
    assertEquals( 0, all.length );
  }
}
