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
package com.w4t;

import junit.framework.TestCase;
import com.w4t.event.ValidationEvent;
import com.w4t.event.ValidationListener;


public class ValidationListener_Test extends TestCase {
  
  protected void setUp() throws Exception {
    W4TFixture.setUp();
    W4TFixture.createContext();
  }
  
  protected void tearDown() throws Exception {
    W4TFixture.tearDown();
    W4TFixture.removeContext();
  }
  
  public void testValidationListenerInvocation() throws Exception {
    final ValidationEvent[] valEvt = new ValidationEvent[ 1 ];
    ValidationListener listener = new ValidationListener() {
      public void validated( final ValidationEvent evt ) {
        valEvt[ 0 ] = evt;
      }
    };
    
    WebText text = new WebText();
    text.addValidationListener( listener );
    text.validate();
    assertNotNull( valEvt[ 0 ] );
    assertSame( text, valEvt[ 0 ].getSource() );
    assertTrue( valEvt[ 0 ].getResult() );
    valEvt[ 0 ] = null;
    text.removeValidationListener( listener );
    
    WebTextArea textArea = new WebTextArea();
    textArea.addValidationListener( listener );
    textArea.validate();
    assertNotNull( valEvt[ 0 ] );
    assertSame( textArea, valEvt[ 0 ].getSource() );
    assertTrue( valEvt[ 0 ].getResult() );
    valEvt[ 0 ] = null;
    textArea.removeValidationListener( listener );
    
    WebContainer container = new WebPanel();
    container.add( text );
    final Object[] valText = new Object[ 1 ];
    text.addValidationListener( new ValidationListener() {
      public void validated( final ValidationEvent evt ) {
        valText[ 0 ] = evt.getSource();
      } 
    } );
    container.add( textArea );
    final Object[] valTextArea = new Object[ 1 ];
    textArea.addValidationListener( new ValidationListener() {
      public void validated( final ValidationEvent evt ) {
        valTextArea[ 0 ] = evt.getSource();
      }
    } );
    container.addValidationListener( listener );
    container.validate();
    assertNotNull( valEvt[ 0 ] );
    assertSame( container, valEvt[ 0 ].getSource() );
    assertTrue( valEvt[ 0 ].getResult() );
    assertNotNull( valText[ 0 ] );
    assertSame( text, valText[ 0 ] );
    assertNotNull( valTextArea[ 0 ] );
    assertSame( textArea, valTextArea[ 0 ] );
  }
}
