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
import com.w4t.mockup.TestForm;
import com.w4t.util.browser.Mozilla1_7;


public class PerformanceAndMemoryTest extends TestCase {
  
  public void testMemUsageWith600WebText() throws Exception {
    WebForm webForm = new TestForm();
    webForm.setWebLayout( new WebGridLayout() );    
    for( int i = 0; i < 600; i++ ) {
      webForm.add( new WebText(), new Position( i, 1 ) );
    }
    MemSize.runGC();
    long usedMemBefore = MemSize.usedMemory();
    AjaxStatusUtil.preRender( webForm );
    MemSize.runGC();
    long usedMemAfter = MemSize.usedMemory();
    System.out.println( "Used "
                        + ( usedMemAfter - usedMemBefore )
                        + " to calculate hash-code for 600 components" );
  }
  
  public void testMemUsageWith10000WebText() throws Exception {
    WebForm webForm = new TestForm();
    webForm.setWebLayout( new WebGridLayout() );    
    for( int i = 0; i < 10000; i++ ) {
      webForm.add( new WebText(), new Position( i, 1 ) );
    }
    MemSize.runGC();
    long usedMemBefore = MemSize.usedMemory();
    AjaxStatusUtil.preRender( webForm );
    MemSize.runGC();
    long usedMemAfter = MemSize.usedMemory();
    System.out.println( "Used "
                        + ( usedMemAfter - usedMemBefore )
                        + " to calculate hash-code for 10000 components" );
  }
  
  public void testPerformance() throws Exception {
    WebForm webForm = new TestForm();
    webForm.setWebLayout( new WebGridLayout() );    
    for( int i = 0; i < 600; i++ ) {
      webForm.add( new WebText(), new Position( i, 1 ) );
    }
    long start = System.currentTimeMillis();
    AjaxStatusUtil.preRender( webForm );
    long duration = System.currentTimeMillis() - start;
    System.out.println( "Took "
                        + duration
                        + " ms to calculate hash-code for 600 components" );
  }
  
  protected void setUp() throws Exception {
    Fixture.setUp();
    Fixture.fakeBrowser( new Mozilla1_7( true, true ) );
  }

  protected void tearDown() throws Exception {
    Fixture.tearDown();
  }
}
