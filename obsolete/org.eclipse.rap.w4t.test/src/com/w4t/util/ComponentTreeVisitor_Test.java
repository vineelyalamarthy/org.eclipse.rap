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
package com.w4t.util;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import com.w4t.*;
import com.w4t.dhtml.*;


public class ComponentTreeVisitor_Test extends TestCase {
  
  private WebForm form;
  private WebButton button;
  private WebPanel panel;
  private WebScrollPane scrollPane;
  private WebText text;
  private TreeView treeView;
  private TreeNode treeNode;
  private TreeNode treeNode1;
  private TreeNode treeNode11;
  private TreeLeaf treeLeaf111;
  private TreeNode treeNode2;
  private TreeLeaf treeLeaf2;

  protected void setUp() throws Exception {
    W4TFixture.setUp();
    W4TFixture.createContext();
    form = new WebForm() {
      protected void setWebComponents() throws Exception {
      }
    };
    form.setWebLayout( new WebFlowLayout() );
    button = new WebButton();
    form.add( button );
    panel = new WebPanel();
    form.add( panel );
    scrollPane = new WebScrollPane();
    panel.add( scrollPane );
    text = new WebText();
    panel.add( text );
    
    treeView = new TreeView();
    treeNode = new TreeNode();
    treeView.addItem( treeNode );
    treeNode1 = new TreeNode();
    treeNode.addItem( treeNode1 );
    treeNode11 = new TreeNode();
    treeNode1.addItem( treeNode11 );
    treeLeaf111 = new TreeLeaf();
    treeNode11.addItem( treeLeaf111 );
    treeNode2 = new TreeNode();
    treeNode.addItem( treeNode2 );
    treeLeaf2 = new TreeLeaf();
    treeNode2.addItem( treeLeaf2 );
    
    form.add( treeView );
  }
  
  protected void tearDown() {
    W4TFixture.tearDown();
    W4TFixture.removeContext();
  }
  
  
  public void testBreadthFirstTreeVisiting() throws Exception {
    
    final List log = new ArrayList();
    
    int strategy = ComponentTreeVisitor.STRATEGY_BREADTH_FIRST;
    ComponentTreeVisitor.accept( form, strategy, new ComponentTreeVisitor() {
      public boolean visit( final WebComponent leaf ) {
        log.add( leaf );
        return true;
      }
      public boolean visit( final WebContainer container ) {
        log.add( container );
        return true;
      }
      public boolean visit( final Decorator decorator ) {
        log.add( decorator );
        return true;
      }
      public boolean visit( final Node node ) {
        log.add( node );
        return true;
      }
    } );    
    assertEquals( 13, log.size() );
    assertSame( form, log.get( 0 ) );
    assertSame( button, log.get( 1 ) );
    assertSame( panel, log.get( 2 ) );
    assertSame( treeView, log.get( 3 ) );
    assertSame( scrollPane, log.get( 4 ) );
    assertSame( text, log.get( 5 ) );
    assertSame( treeNode, log.get( 6 ) );
    assertSame( scrollPane.getContent(), log.get( 7 ) );
    assertSame( treeNode1, log.get( 8 ) );
    assertSame( treeNode2, log.get( 9 ) );
    assertSame( treeNode11, log.get( 10 ) );
    assertSame( treeLeaf2, log.get( 11 ) );
    assertSame( treeLeaf111, log.get( 12 ) );

    
    log.clear();
    ComponentTreeVisitor.accept( form, strategy, new ComponentTreeVisitor() {
      public boolean visit( final WebComponent leaf ) {
        log.add( leaf );
        return true;
      }
      public boolean visit( final WebContainer container ) {
        log.add( container );
        return true;
      }
      public boolean visit( final Decorator decorator ) {
        log.add( decorator );
        return false;
      }
      public boolean visit( final Node node ) {
        log.add( node );
        return true;
      }
    } );
    assertEquals( 12, log.size() );
    assertSame( form, log.get( 0 ) );
    assertSame( button, log.get( 1 ) );
    assertSame( panel, log.get( 2 ) );
    assertSame( treeView, log.get( 3 ) );
    assertSame( scrollPane, log.get( 4 ) );
    assertSame( text, log.get( 5 ) );
    assertSame( treeNode, log.get( 6 ) );
    assertSame( treeNode1, log.get( 7 ) );
    assertSame( treeNode2, log.get( 8 ) );
    assertSame( treeNode11, log.get( 9 ) );
    assertSame( treeLeaf2, log.get( 10 ) );
    assertSame( treeLeaf111, log.get( 11 ) );

    log.clear();
    ComponentTreeVisitor.accept( form, strategy, new ComponentTreeVisitor() {
      public boolean visit( final WebComponent leaf ) {
        log.add( leaf );
        return true;
      }
      public boolean visit( final WebContainer container ) {
        log.add( container );
        return container instanceof WebForm;
      }
      public boolean visit( final Decorator decorator ) {
        log.add( decorator );
        return false;
      }
      public boolean visit( final Node node ) {
        log.add( node );
        return true;
      }
    } );
    assertEquals( 10, log.size() );
    assertSame( form, log.get( 0 ) );
    assertSame( button, log.get( 1 ) );
    assertSame( panel, log.get( 2 ) );
    assertSame( treeView, log.get( 3 ) );
    assertSame( treeNode, log.get( 4 ) );
    assertSame( treeNode1, log.get( 5 ) );
    assertSame( treeNode2, log.get( 6 ) );
    assertSame( treeNode11, log.get( 7 ) );
    assertSame( treeLeaf2, log.get( 8 ) );
    assertSame( treeLeaf111, log.get( 9 ) );

    log.clear();
    ComponentTreeVisitor.accept( form, strategy, new ComponentTreeVisitor() {
      public boolean visit( final WebComponent leaf ) {
        log.add( leaf );
        return true;
      }
      public boolean visit( final WebContainer container ) {
        log.add( container );
        return true;
      }
      public boolean visit( final Decorator decorator ) {
        log.add( decorator );
        return true;
      }
      public boolean visit( final Node node ) {
        log.add( node );
        return false;
      }
    } );
    assertEquals( 7, log.size() );
    assertSame( form, log.get( 0 ) );
    assertSame( button, log.get( 1 ) );
    assertSame( panel, log.get( 2 ) );
    assertSame( treeView, log.get( 3 ) );
    assertSame( scrollPane, log.get( 4 ) );
    assertSame( text, log.get( 5 ) );
    assertSame( scrollPane.getContent(), log.get( 6 ) );
  }
  
  public void testDepthFirstTreeVisiting() throws Exception {
    
    final List log = new ArrayList();
    
    ComponentTreeVisitor.accept( form, new ComponentTreeVisitor() {
      public boolean visit( final WebComponent leaf ) {
        log.add( leaf );
        return true;
      }
      public boolean visit( final WebContainer container ) {
        log.add( container );
        return true;
      }
      public boolean visit( final Decorator decorator ) {
        log.add( decorator );
        return true;
      }
      public boolean visit( final Node node ) {
        log.add( node );
        return true;
      }
    } );    
    assertEquals( 13, log.size() );
    assertSame( form, log.get( 0 ) );
    assertSame( button, log.get( 1 ) );
    assertSame( panel, log.get( 2 ) );
    assertSame( scrollPane, log.get( 3 ) );
    assertSame( scrollPane.getContent(), log.get( 4 ) );
    assertSame( text, log.get( 5 ) );
    assertSame( treeView, log.get( 6 ) );
    assertSame( treeNode, log.get( 7 ) );
    assertSame( treeNode1, log.get( 8 ) );
    assertSame( treeNode11, log.get( 9 ) );
    assertSame( treeLeaf111, log.get( 10 ) );
    assertSame( treeNode2, log.get( 11 ) );    
    assertSame( treeLeaf2, log.get( 12 ) );

    
    log.clear();
    ComponentTreeVisitor.accept( form, new ComponentTreeVisitor() {
      public boolean visit( final WebComponent leaf ) {
        log.add( leaf );
        return true;
      }
      public boolean visit( final WebContainer container ) {
        log.add( container );
        return true;
      }
      public boolean visit( final Decorator decorator ) {
        log.add( decorator );
        return false;
      }
      public boolean visit( final Node node ) {
        log.add( node );
        return true;
      }
    } );
    assertEquals( 12, log.size() );
    assertSame( form, log.get( 0 ) );
    assertSame( button, log.get( 1 ) );
    assertSame( panel, log.get( 2 ) );
    assertSame( scrollPane, log.get( 3 ) );
    assertSame( text, log.get( 4 ) );
    assertSame( treeView, log.get( 5 ) );
    assertSame( treeNode, log.get( 6 ) );
    assertSame( treeNode1, log.get( 7 ) );
    assertSame( treeNode11, log.get( 8 ) );
    assertSame( treeLeaf111, log.get( 9 ) );
    assertSame( treeNode2, log.get( 10 ) );    
    assertSame( treeLeaf2, log.get( 11 ) );
    
    log.clear();
    ComponentTreeVisitor.accept( form, new ComponentTreeVisitor() {
      public boolean visit( final WebComponent leaf ) {
        log.add( leaf );
        return true;
      }
      public boolean visit( final WebContainer container ) {
        log.add( container );
        return container instanceof WebForm;
      }
      public boolean visit( final Decorator decorator ) {
        log.add( decorator );
        return false;
      }
      public boolean visit( final Node node ) {
        log.add( node );
        return true;
      }
    } );
    assertEquals( 10, log.size() );
    assertSame( form, log.get( 0 ) );
    assertSame( button, log.get( 1 ) );
    assertSame( panel, log.get( 2 ) );
    assertSame( treeView, log.get( 3 ) );
    assertSame( treeNode, log.get( 4 ) );
    assertSame( treeNode1, log.get( 5 ) );
    assertSame( treeNode11, log.get( 6 ) );
    assertSame( treeLeaf111, log.get( 7 ) );
    assertSame( treeNode2, log.get( 8 ) );    
    assertSame( treeLeaf2, log.get( 9 ) );
    
    log.clear();
    ComponentTreeVisitor.accept( form, new ComponentTreeVisitor() {
      public boolean visit( final WebComponent leaf ) {
        log.add( leaf );
        return true;
      }
      public boolean visit( final WebContainer container ) {
        log.add( container );
        return true;
      }
      public boolean visit( final Decorator decorator ) {
        log.add( decorator );
        return true;
      }
      public boolean visit( final Node node ) {
        log.add( node );
        return false;
      }
    } );
    assertEquals( 7, log.size() );
    assertSame( form, log.get( 0 ) );
    assertSame( button, log.get( 1 ) );
    assertSame( panel, log.get( 2 ) );
    assertSame( scrollPane, log.get( 3 ) );
    assertSame( scrollPane.getContent(), log.get( 4 ) );
    assertSame( text, log.get( 5 ) );
    assertSame( treeView, log.get( 6 ) );
  }
}
