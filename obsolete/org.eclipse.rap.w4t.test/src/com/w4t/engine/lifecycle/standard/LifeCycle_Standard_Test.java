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

import org.eclipse.rwt.internal.lifecycle.LifeCycle;
import org.eclipse.rwt.lifecycle.*;

import com.w4t.W4TFixture;
import com.w4t.WebForm;
import com.w4t.engine.util.FormManager;


public class LifeCycle_Standard_Test extends TestCase {
  
  private static final String BEFORE = "before ";
  private static final String AFTER = "after ";
  
  private String log = "";
  
  protected void setUp() throws Exception {
    W4TFixture.setUp();
    W4TFixture.createContext();
  }
  
  protected void tearDown() throws Exception {
    W4TFixture.tearDown();
    W4TFixture.removeContext();
  }
  
  public void testLifeCycle() throws Exception {
    WebForm form = W4TFixture.getEmptyWebFormInstance();
    W4TFixture.fakeEngineForRequestLifeCycle( form );
    // reset active form which was set by the fake method call...
    FormManager.setActive( null ); 
    LifeCycle lifeCycle = new LifeCycle_Standard();
    PhaseListener listener = new PhaseListener() {
      private static final long serialVersionUID = 1L;

      public void afterPhase( final PhaseEvent event ) {
        log += AFTER + event.getPhaseId() + "|";
      }

      public void beforePhase( final PhaseEvent event ) {
        log += BEFORE + event.getPhaseId() + "|";
      }

      public PhaseId getPhaseId() {
        return PhaseId.ANY;
      }
      
    };
    lifeCycle.addPhaseListener( listener );
    lifeCycle.execute();
    String expected =   BEFORE + PhaseId.PREPARE_UI_ROOT + "|"
                      + AFTER + PhaseId.PREPARE_UI_ROOT + "|"
                      + BEFORE + PhaseId.READ_DATA + "|"
                      + AFTER + PhaseId.READ_DATA + "|"
                      + BEFORE + PhaseId.PROCESS_ACTION + "|"
                      + AFTER + PhaseId.PROCESS_ACTION + "|"
                      + BEFORE + PhaseId.RENDER + "|"
                      + AFTER + PhaseId.RENDER + "|";
    assertEquals( expected, log );
    
    lifeCycle.removePhaseListener( listener );
    log = "";
    lifeCycle.execute();
    assertEquals( "", log );
   
    log = "";
    lifeCycle.addPhaseListener( new PhaseListener() {
      private static final long serialVersionUID = 1L;
      
      public void afterPhase( final PhaseEvent event ) {
        log += AFTER + event.getPhaseId() + "|";
      }
      
      public void beforePhase( final PhaseEvent event ) {
        log += BEFORE + event.getPhaseId() + "|";
      }
      
      public PhaseId getPhaseId() {
        return PhaseId.PREPARE_UI_ROOT;
      }
    } );
    lifeCycle.execute();
    expected =   BEFORE + PhaseId.PREPARE_UI_ROOT + "|"
               + AFTER + PhaseId.PREPARE_UI_ROOT + "|";
    assertEquals( expected, log );
  }
}
