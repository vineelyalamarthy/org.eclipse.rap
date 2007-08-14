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
package com.w4t.webformkit;

import org.eclipse.rwt.internal.browser.*;

import junit.framework.TestCase;
import com.w4t.W4TFixture;

public class TriggerTimeStamp_Test extends TestCase {
  
  public void testGetHTMLCode() {
    // DOM Browsers: Ie6
    W4TFixture.fakeBrowser( new Ie6( true ) );
    String html = TriggerTimeStamp.getHTMLCode();
    String expected 
      =   "<img src=\"resources/images/transparent.gif\" "
        + "name=\"w4tTriggerTimeStampImg\" "
        + "id=\"w4tTriggerTimeStampImg\" border=\"0\" height=\"1\" width=\"1\" "
        + "alt=\"keepAlive\" />";
    assertEquals( expected, html );
    // DOM Browsers: Konqueror3_1
    W4TFixture.fakeBrowser( new Konqueror3_1( true ) );
    html = TriggerTimeStamp.getHTMLCode();
    assertEquals( expected, html );
    // DOM Browsers: Mozilla1_6
    W4TFixture.fakeBrowser( new Mozilla1_6( true ) );
    html = TriggerTimeStamp.getHTMLCode();
    assertEquals( expected, html );
    // DOM Browsers: Mozilla 1.7
    W4TFixture.fakeBrowser( new Mozilla1_7( true ) );
    html = TriggerTimeStamp.getHTMLCode();
    assertEquals( expected, html );
    // Non-DOM Browsers: Hotjava
    W4TFixture.fakeBrowser( new Default( true ) );
    html = TriggerTimeStamp.getHTMLCode();
    assertEquals( "", html );
  }
  
  public void testGetOnLoadCode() {
    // DOM Browsers: Ie6
    W4TFixture.fakeBrowser( new Ie6( true ) );
    String code = TriggerTimeStamp.getOnLoadCode();
    String expected 
      = " active = window.setInterval( 'triggerTimeStamp_DOM()',1800000);";
    assertEquals( expected, code );
    // DOM Browsers: Konqueror3_1
    W4TFixture.fakeBrowser( new Konqueror3_1( true ) );
    code = TriggerTimeStamp.getOnLoadCode();
    assertEquals( expected, code );
    // DOM Browsers: Mozilla1_6
    W4TFixture.fakeBrowser( new Mozilla1_6( true ) );
    code = TriggerTimeStamp.getOnLoadCode();
    assertEquals( expected, code );
    // DOM Browsers: Nav6
    W4TFixture.fakeBrowser( new Mozilla1_7( true ) );
    code = TriggerTimeStamp.getOnLoadCode();
    assertEquals( expected, code );
    // Non-DOM Browsers: Hotjava
    W4TFixture.fakeBrowser( new Default( true ) );
    code = TriggerTimeStamp.getOnLoadCode();
    expected 
      = " active = window.setInterval( \'windowManager.triggerTimeStamp()\', "
      + "1800000);";
    assertEquals( expected , code );
  }
  
  public void testDOMBrowser() {
    String  code;
    W4TFixture.fakeBrowser( new Ie5( true ) );
    code = TriggerTimeStamp.getOnLoadCode();
    assertTrue( code.indexOf( "triggerTimeStamp_DOM" ) != -1 );
    
    W4TFixture.fakeBrowser( new Ie5up( true ) );
    code = TriggerTimeStamp.getOnLoadCode();
    assertTrue( code.indexOf( "triggerTimeStamp_DOM" ) != -1 );
    
    W4TFixture.fakeBrowser( new Ie5_5( true ) );
    code = TriggerTimeStamp.getOnLoadCode();
    assertTrue( code.indexOf( "triggerTimeStamp_DOM" ) != -1 );
    
    W4TFixture.fakeBrowser( new Ie6( true ) );
    code = TriggerTimeStamp.getOnLoadCode();
    assertTrue( code.indexOf( "triggerTimeStamp_DOM" ) != -1 );
    
    W4TFixture.fakeBrowser( new Ie7( true ) );
    code = TriggerTimeStamp.getOnLoadCode();
    assertTrue( code.indexOf( "triggerTimeStamp_DOM" ) != -1 );
    
    W4TFixture.fakeBrowser( new Mozilla1_6( true ) );
    code = TriggerTimeStamp.getOnLoadCode();
    assertTrue( code.indexOf( "triggerTimeStamp_DOM" ) != -1 );
    
    W4TFixture.fakeBrowser( new Mozilla1_6up( true ) );
    code = TriggerTimeStamp.getOnLoadCode();
    assertTrue( code.indexOf( "triggerTimeStamp_DOM" ) != -1 );
    
    W4TFixture.fakeBrowser( new Mozilla1_7( true ) );
    code = TriggerTimeStamp.getOnLoadCode();
    assertTrue( code.indexOf( "triggerTimeStamp_DOM" ) != -1 );
    
    W4TFixture.fakeBrowser( new Konqueror3_1( true ) );
    code = TriggerTimeStamp.getOnLoadCode();
    assertTrue( code.indexOf( "triggerTimeStamp_DOM" ) != -1 );
    
    W4TFixture.fakeBrowser( new Konqueror3_1up( true ) );
    code = TriggerTimeStamp.getOnLoadCode();
    assertTrue( code.indexOf( "triggerTimeStamp_DOM" ) != -1 );
    
    W4TFixture.fakeBrowser( new Opera8( true ) );
    code = TriggerTimeStamp.getOnLoadCode();
    assertTrue( code.indexOf( "triggerTimeStamp_DOM" ) != -1 );
    
    W4TFixture.fakeBrowser( new Opera9( true ) );
    code = TriggerTimeStamp.getOnLoadCode();
    assertTrue( code.indexOf( "triggerTimeStamp_DOM" ) != -1 );
    
    W4TFixture.fakeBrowser( new Safari2( true ) );
    code = TriggerTimeStamp.getOnLoadCode();
    assertTrue( code.indexOf( "triggerTimeStamp_DOM" ) != -1 );
    
    W4TFixture.fakeBrowser( new Safari2up( true ) );
    code = TriggerTimeStamp.getOnLoadCode();
    assertTrue( code.indexOf( "triggerTimeStamp_DOM" ) != -1 );
  }
  
  protected void setUp() throws Exception {
    W4TFixture.setUp();
  }
  
  protected void tearDown() throws Exception {
    W4TFixture.tearDown();
  }
}
