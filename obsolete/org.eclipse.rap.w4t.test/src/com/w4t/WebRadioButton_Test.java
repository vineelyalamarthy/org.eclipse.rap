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


/** unit tests for WebRadioButton. */
public class WebRadioButton_Test extends TestCase {

  public WebRadioButton_Test( final String name ) {
    super( name );
  }

  protected void setUp() throws Exception {
    Fixture.setUp();
    Fixture.createContext();
  }
  
  protected void tearDown() throws Exception {
    Fixture.tearDown();
    Fixture.removeContext();
  }
  
  // test code
  ////////////
  
  public void testInitialValue() {
    WebRadioButton button = new WebRadioButton();
    assertEquals( button.getValue(), button.getUniqueID() );
  }
  
  public void testFindRadioButtonGroup() {
    WebPanel panel = new WebPanel();
    WebRadioButtonGroup group = new WebRadioButtonGroup();
    panel.add( group );
    WebRadioButton button = new WebRadioButton();
    // check param: null not allowed
    try {
      WebRadioButtonUtil.findGroup( null );
      fail( "Expected NullPointerException" );
    } catch( NullPointerException e ) {
      // expected
    }
    // radioButton not yet added to component tree: no group 
    WebRadioButtonGroup groupForButton = WebRadioButtonUtil.findGroup( button );
    assertEquals( null, groupForButton );
    // add button to panel -> no group in parent branch
    panel.add( button );
    groupForButton = WebRadioButtonUtil.findGroup( button );
    assertEquals( null, groupForButton );
    // add button 'properly' (to group): must find group 
    button.remove();
    group.add( button );
    groupForButton = WebRadioButtonUtil.findGroup( button );
    assertEquals( group, groupForButton );
  }
  
  public void testEnabled() {
    // Scenario 1: button on group on panel 
    WebPanel panel1 = new WebPanel();
    WebRadioButtonGroup group1 = new WebRadioButtonGroup();
    panel1.add( group1 );
    WebRadioButton button1 = new WebRadioButton();
    group1.add( button1 );
    // group disabled & button enabled -> not enabled
    group1.setEnabled( false );
    button1.setEnabled( true );
    assertEquals( false, button1.isEnabled() );
    // both enabled -> enabled
    group1.setEnabled( true );
    button1.setEnabled( true );
    assertEquals( true, button1.isEnabled() );
    // group enabled & button disabled -> not enabled
    group1.setEnabled( true );
    button1.setEnabled( false );
    assertEquals( false, button1.isEnabled() );
    // Scenario 2: button on panel on group
    WebRadioButtonGroup group2 = new WebRadioButtonGroup();
    WebPanel panel2 = new WebPanel();
    group2.add( panel2 );
    WebRadioButton button2 = new WebRadioButton();
    panel2.add( button2 );
    // group disabled & button enabled -> not enabled
    group2.setEnabled( false );
    button2.setEnabled( true );
    assertEquals( false, button2.isEnabled() );
    // both enabled -> enabled
    group2.setEnabled( true );
    button2.setEnabled( true );
    assertEquals( true, button2.isEnabled() );
    // group enabled & button disabled -> not enabled
    group2.setEnabled( true );
    button2.setEnabled( false );
    assertEquals( false, button2.isEnabled() );
  }
  
  public void testVisible() {
    //
    // Scenario 1: button on group on panel 
    WebPanel panel1 = new WebPanel();
    WebRadioButtonGroup group1 = new WebRadioButtonGroup();
    panel1.add( group1 );
    WebRadioButton button1 = new WebRadioButton();
    group1.add( button1 );
    // group invisible & button visible -> not visible
    group1.setVisible( false );
    button1.setVisible( true );
    assertEquals( false, button1.isVisible() );
    // both visible -> visible
    group1.setVisible( true );
    button1.setVisible( true );
    assertEquals( true, button1.isVisible() );
    // group visible & button invisible -> not visible
    group1.setVisible( true );
    button1.setVisible( false );
    assertEquals( false, button1.isVisible() );
    //
    // Scenario 2: button on panel on group
    WebRadioButtonGroup group2 = new WebRadioButtonGroup();
    WebPanel panel2 = new WebPanel();
    group2.add( panel2 );
    WebRadioButton button2 = new WebRadioButton();
    panel2.add( button2 );
    // group invisible & button visible -> not visible
    group2.setVisible( false );
    button2.setVisible( true );
    assertEquals( false, button2.isVisible() );
    // both visible -> visible
    group2.setVisible( true );
    button2.setVisible( true );
    assertEquals( true, button2.isVisible() );
    // group visible & button invisible -> not visible
    group2.setVisible( true );
    button2.setVisible( false );
    assertEquals( false, button2.isVisible() );
    //
    // Scenario 3: radioButton without group
    WebRadioButton button3 = new WebRadioButton();
    assertEquals( true, button3.isVisible() );
    WebPanel panel3 = new WebPanel();
    panel3.add( button3 );
    assertEquals( true, button3.isVisible() );
  }
  
  public void testValue() {
    WebRadioButtonGroup group = new WebRadioButtonGroup();
    WebRadioButton button1 = new WebRadioButton();
    group.add( button1 );
    //
    group.setValue( "1" );
    assertEquals( false, button1.isSelected() );
    button1.setValue( "1" );
    assertEquals( true, button1.isSelected() );
    group.setValue( "xyz" );
    assertEquals( false, button1.isSelected() );
    // call isSelected on a 'free-floating' button
    WebRadioButton freeButton = new WebRadioButton();
    assertEquals( false, freeButton.isSelected() );
  }
  
  public void testFindButtonsForGroup() {
    // test with invalid parameter
    try {
      WebRadioButtonUtil.findButtons( null );
      fail( "Expected NullPointerException" );
    } catch( NullPointerException e ) {
      // expected
    }
    // group without buttons must return empty array
    WebRadioButtonGroup outerGroup = new WebRadioButtonGroup();
    assertEquals( new WebRadioButton[ 0 ], 
                  WebRadioButtonUtil.findButtons( outerGroup ) );
    // Simplest case: button is direct child of group
    WebRadioButton button1 = new WebRadioButton();
    outerGroup.add( button1 );
    assertEquals( new WebRadioButton[] { button1 }, 
                  WebRadioButtonUtil.findButtons( outerGroup ) );
    // Indirect child button
    WebPanel panel = new WebPanel();
    outerGroup.add( panel );
    WebRadioButton button2 = new WebRadioButton();
    panel.add( button2 );
    assertEquals( new WebRadioButton[] { button1, button2 }, 
                  WebRadioButtonUtil.findButtons( outerGroup ) );
    // Separate group inside outerGroup
    WebRadioButtonGroup innerGroup = new WebRadioButtonGroup();
    WebRadioButton subButton1 = new WebRadioButton();
    innerGroup.add( subButton1 );
    outerGroup.add( innerGroup );
    assertEquals( new WebRadioButton[] { button1, button2 }, 
                  WebRadioButtonUtil.findButtons( outerGroup ) );
    // check nested group 
    assertEquals( new WebRadioButton[] { subButton1 }, 
                  WebRadioButtonUtil.findButtons( innerGroup ) );
  }
  
  private static void assertEquals( final WebRadioButton[] array1, 
                                    final WebRadioButton[] array2 ) 
  {
    assertEquals( "Array size differs", array1.length, array2.length );
    for( int i = 0; i < array1.length; i++ ) {
      assertEquals( array1[ i ], array2[ i ] );
    }
  }
}
