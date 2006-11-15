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
import com.w4t.event.WebContainerAdapter;
import com.w4t.event.WebContainerEvent;


public class Decorator_Test extends TestCase {
  
  protected void setUp() throws Exception {
    Fixture.setUp();
    Fixture.createContext();
  }
  
  protected void tearDown() throws Exception {
    Fixture.tearDown();
    Fixture.removeContext();
  }
  
  public void testAttributeVisibility() throws Exception {
    WebAnchor anchor = new WebAnchor();
    assertTrue( anchor.isVisible() );
    assertTrue( anchor.getContent().isVisible() );
    
    anchor.getContent().setVisible( false );
    assertFalse( anchor.isVisible() );
    assertFalse( anchor.getContent().isVisible() );
    
    anchor.getContent().setVisible( true );
    anchor.setVisible( false );
    assertFalse( anchor.isVisible() );
    assertFalse( anchor.getContent().isVisible() );
    
    anchor.setVisible( true );
    assertTrue( anchor.isVisible() );
    assertTrue( anchor.getContent().isVisible() );
    
    anchor.removeContent();
    assertFalse( anchor.isVisible() );
    
    Decorator decorator = new Decorator( anchor ) {};
    WebLabel label = new WebLabel();
    anchor.setContent( label );
    assertTrue( decorator.isVisible() );
    label.setVisible( false );
    assertFalse( decorator.isVisible() );
    label.setVisible( true );
    
    WebPanel panel = new WebPanel();
    panel.add( decorator );
    panel.setVisible( false );
    assertFalse( label.isVisible() );
    
  }
  
  public void testAttributeEnabled() throws Exception {
    WebAnchor anchor = new WebAnchor();
    assertTrue( anchor.isEnabled() );
    
    anchor.getContent().setEnabled( false );
    assertFalse( anchor.isEnabled() );
    assertFalse( anchor.getContent().isEnabled() );
    
    anchor.getContent().setEnabled( true );
    anchor.setEnabled( false );
    assertFalse( anchor.isEnabled() );
    assertFalse( anchor.getContent().isEnabled() );
    
    anchor.setEnabled( true );
    assertTrue( anchor.isEnabled() );
    assertTrue( anchor.getContent().isEnabled() );
    
    anchor.removeContent();
    assertFalse( anchor.isEnabled() );
    
    Decorator decorator = new Decorator( anchor ) {};
    WebLabel label = new WebLabel();
    anchor.setContent( label );
    assertTrue( decorator.isEnabled() );
    label.setEnabled( false );
    assertFalse( decorator.isEnabled() );
    label.setEnabled( true );
    
    WebPanel panel = new WebPanel();
    panel.add( decorator );
    panel.setEnabled( false );
    assertFalse( label.isEnabled() );

  }
  
  public void testParentComponent() throws Exception {
    WebAnchor anchor = new WebAnchor();
    WebPanel parent = new WebPanel();
    parent.add( anchor );
    
    assertSame( parent, anchor.getParent() );
    assertSame( parent, anchor.getContent().getParent() );
    
    WebLabel newContent = new WebLabel();
    WebComponent oldContent = anchor.getContent();
    anchor.setContent( newContent );
    assertNull( oldContent.getParent() );
    assertSame( parent, newContent.getParent() );

    try {
      new Decorator( null ) {};
      fail();
    } catch( final NullPointerException npe ) {
      
    }
    try {
      new Decorator( newContent ) {};
      fail();
    } catch( final IllegalArgumentException iae ) {
    }
    
    Decorator newDecorator = new Decorator( oldContent ) {};
    WebPanel newPanel = new WebPanel();
    newPanel.add( newDecorator );
    assertSame( newPanel, oldContent.getParent() );
    
    newDecorator.removeContent();
    assertNull( newDecorator.getContent() );
    assertNull( oldContent.getParent() );
    assertSame( newPanel, newDecorator.getParent() );
    
    newDecorator.setContent( oldContent );
    oldContent.remove();
    assertNull( oldContent.getParent() );
    assertNull( newDecorator.getContent() );
    assertNull( newDecorator.getParent() );
    
    Decorator nestedDecorator = new Decorator( oldContent ) {};
    newDecorator.setContent( nestedDecorator );
    newPanel.add( newDecorator );
    assertSame( newPanel, nestedDecorator.getParent() );
    assertSame( newPanel, oldContent.getParent() );

    oldContent.remove();
    assertNull( oldContent.getParent() );
    assertNull( newDecorator.getContent() );
    assertNull( newDecorator.getParent() );
    assertNull( nestedDecorator.getContent() );
    assertNull( nestedDecorator.getParent() );
    assertNull( newDecorator.getContent() );
    assertNull( nestedDecorator.getContent() );
    
    newDecorator.setContent( oldContent );
    newPanel.add( newDecorator );
    newDecorator.remove();
    assertSame( oldContent, newDecorator.getContent() );
    
    newDecorator.removeContent();
    nestedDecorator.setContent( oldContent );
    newDecorator.setContent( nestedDecorator );
    newPanel.add( newDecorator );
    nestedDecorator.remove();
    assertSame( null, newDecorator.getParent() );
    assertNull( nestedDecorator.getParent() );
    assertSame( oldContent, nestedDecorator.getContent() );
  }
  
  public void testUtilMethods() throws Exception {
    try {
      assertFalse( Decorator.isDecorated( null ) );
      fail();
    } catch( NullPointerException npe ) {
    }
    WebLabel label = new WebLabel();
    Decorator nestedDecorator = new Decorator( label ) {};
    Decorator decorator = new Decorator( nestedDecorator ) {};
    try {
      assertFalse( Decorator.isDecorated( label ) );
      fail();
    } catch( IllegalArgumentException iae ) {
    }
    
    WebPanel panel = new WebPanel();
    panel.add( decorator );
    assertTrue( Decorator.isDecorated( label ) );
    assertTrue( Decorator.isDecorated( nestedDecorator ) );
    assertFalse( Decorator.isDecorated( decorator ) );
    
    assertSame( nestedDecorator, Decorator.getParentDecorator( label ) );
    assertSame( decorator, Decorator.getParentDecorator( nestedDecorator ) );
    assertNull( Decorator.getParentDecorator( decorator ) );
    assertSame( decorator, Decorator.getOuterMostDecorator( label ) );
  }
  
  public void testContainerEvents() throws Exception {
    WebLabel label = new WebLabel();
    Decorator decorator = new Decorator( label ) {};
    WebPanel panel = new WebPanel();
    final WebContainerEvent[] events = new WebContainerEvent[ 2 ];
    panel.addWebContainerListener( new WebContainerAdapter() {
      public void webComponentAdded( final WebContainerEvent evt ) {
        events[ 0 ] = evt; 
      }
      public void webComponentRemoved( final WebContainerEvent evt ) {
        events[ 1 ] = evt;
      }
    } );
    panel.add( decorator );
    label.remove();
    assertEquals( events[ 0 ].getID(), WebContainerEvent.COMPONENT_ADDED );
    assertSame( decorator, events[ 0 ].getChild() );
    assertEquals( events[ 1 ].getID(), WebContainerEvent.COMPONENT_REMOVED );
    assertSame( decorator, events[ 1 ].getChild() );
    
    events[ 0 ] = null;
    events[ 1 ] = null;
    decorator.setContent( label );
    panel.add( decorator );
    decorator.remove();
    assertEquals( events[ 0 ].getID(), WebContainerEvent.COMPONENT_ADDED );
    assertSame( decorator, events[ 0 ].getChild() );
    assertEquals( events[ 1 ].getID(), WebContainerEvent.COMPONENT_REMOVED );
    assertSame( decorator, events[ 1 ].getChild() );
  }
}
