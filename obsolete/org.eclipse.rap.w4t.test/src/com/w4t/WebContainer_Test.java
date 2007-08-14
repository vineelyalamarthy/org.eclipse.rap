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

import java.util.Date;
import junit.framework.TestCase;
import com.w4t.event.WebContainerEvent;
import com.w4t.event.WebContainerListener;

/**
 * <p>
 * Tests functionalities in com.w4tWebContainer.
 * </p>
 */
public class WebContainer_Test extends TestCase {

  public WebContainer_Test( final String name ) {
    super( name );
  }
  
  protected void setUp() throws Exception {
    W4TFixture.setUp();
    W4TFixture.createContext();
  }
  
  protected void tearDown() throws Exception {
    W4TFixture.tearDown();
    W4TFixture.removeContext();
  }

  // testing methods
  //////////////////
  public void testClone() throws Exception {
    CloneMe cm = new CloneMe();
    CloneMe cmClone = ( CloneMe )cm.clone();
    assertTrue( cmClone.wbt != null );
    assertTrue( cm.wbt != cmClone.wbt );
  }

  public void testContainerObserver() throws Exception {
    WebPanel panel = new WebPanel();
    doTestContainerObserver( panel );
    WebForm form = new WebContainer_Test.TestForm();
    form.setWebLayout( new WebFlowLayout() );
    doTestContainerObserver( form );
  }

  private void doTestContainerObserver( final WebContainer panel )
    throws Exception
  {
    final WebLabel label = new WebLabel();
    final WebGridLayout layout = new WebGridLayout();
    final boolean[] eventTriggeredList = new boolean[ 3 ];
    panel.addWebContainerListener( new WebContainerListener() {

      public void webComponentAdded( final WebContainerEvent evt ) {
        eventTriggeredList[ 0 ] = true;
        assertTrue( evt.getChild() == label );
      }

      public void webComponentRemoved( final WebContainerEvent evt ) {
        eventTriggeredList[ 1 ] = true;
        assertTrue( evt.getChild() == label );
      }

      public void webLayoutChanged( final WebContainerEvent evt ) {
        eventTriggeredList[ 2 ] = true;
        assertTrue( evt.getWebLayout() == layout );
      }
    } );
    panel.add( label );
    assertTrue( "wrong count of components", panel.get().length == 1 );
    panel.remove( label );
    assertTrue( "wrong count of components", panel.get().length == 0 );
    panel.setWebLayout( layout );
    assertTrue( "component not added", eventTriggeredList[ 0 ] );
    assertTrue( "component not removed", eventTriggeredList[ 1 ] );
    assertTrue( "layout not changed", eventTriggeredList[ 2 ] );
  }
  
  public void testComponentAddition() throws Exception {
    WebContainer container1 = new WebPanel();
    WebButton button = new WebButton();
    container1.add( button );

    WebContainer container2 = new WebPanel();
    try {
      container2.add( button );
      fail();
    } catch( IllegalArgumentException iae ) {
    }
  }
  
  
  // inner classes
  ////////////////
  
  private class CloneMe extends WebPanel {

    private WebButton wbt = new WebButton( new Date().toString() );

    private CloneMe() throws Exception {
      add( wbt );
    }
  }
  private class TestForm extends WebForm {

    protected void setWebComponents() throws Exception {
    }
  }
}