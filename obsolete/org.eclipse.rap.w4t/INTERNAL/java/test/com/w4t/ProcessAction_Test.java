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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import junit.framework.TestCase;
import com.w4t.dhtml.*;
import com.w4t.dhtml.event.*;
import com.w4t.engine.service.ContextProvider;
import com.w4t.engine.service.IServiceStateInfo;
import com.w4t.event.*;
import com.w4t.internal.adaptable.IRenderInfoAdapter;
import com.w4t.util.RendererCache;
import com.w4t.util.browser.Default;
import com.w4t.util.browser.Opera8;
import com.w4t.util.image.ImageCache;


public class ProcessAction_Test extends TestCase {
  
//  private final static String webAppRoot = Fixture.getWebAppBase().toString();
  private final static String SUFFIX = ".x";
  
  protected void setUp() throws Exception {
    Fixture.setUp();
    Fixture.createContext();
  }
  
  protected void tearDown() throws Exception {
    Fixture.tearDown();
    Fixture.removeContext();
  }
  
  public void testDragDropScript() throws Exception {
    WebForm form = Fixture.getEmptyWebFormInstance();
    Fixture.fakeBrowser( new Default( true ) );
    final Object[] dragSource = new Object[ 1 ];
    final Object[] dragDestination = new Object[ 1 ];

    TreeView treeView = new TreeView();
    TreeNode node1 = new TreeNode();
    treeView.addItem( node1 );
    TreeNode node2 = new TreeNode();
    treeView.addItem( node2 );
    TreeLeaf leaf1 = new TreeLeaf();
    node1.addItem( leaf1 );
    form.add( treeView, WebBorderLayout.NORTH );
    treeView.addDragDropListener( new DragDropListener() {
      public void receivedDragDrop( final DragDropEvent evt ) {
        dragSource[ 0 ] = evt.getDragSource();
        dragDestination[ 0 ] = evt.getDragDestination();
      }
    } );
    Fixture.fakeRequestParam( "dragSource", leaf1.getUniqueID() );
    Fixture.fakeRequestParam( "dragDestination", node2.getUniqueID() );
    Fixture.getLifeCycleAdapter( form ).processAction();
    assertNotNull( dragSource[ 0 ] );
    assertNotNull( dragDestination[ 0 ] );
    assertSame( leaf1, dragSource[ 0 ] );
    assertSame( node2, dragDestination[ 0 ] );
  }
  
  public void testDragDropNoScript() throws Exception {
    WebForm form = Fixture.getEmptyWebFormInstance();
    Fixture.fakeBrowser( new Default( false ) );
    final Object[] dragSource = new Object[ 1 ];
    final Object[] dragDestination = new Object[ 1 ];

    TreeView treeView = new TreeView();
    TreeNode node1 = new TreeNode();
    treeView.addItem( node1 );
    TreeNode node2 = new TreeNode();
    treeView.addItem( node2 );
    TreeLeaf leaf1 = new TreeLeaf();
    node1.addItem( leaf1 );
    form.add( treeView, WebBorderLayout.NORTH );
    treeView.addDragDropListener( new DragDropListener() {
      public void receivedDragDrop( final DragDropEvent evt ) {
        dragSource[ 0 ] = evt.getDragSource();
        dragDestination[ 0 ] = evt.getDragDestination();
      }
    } );
    String id = DragDropEvent.PREFIX + leaf1.getUniqueID() + SUFFIX;
    Fixture.fakeRequestParam( id, id );
    Fixture.getLifeCycleAdapter( form ).processAction();
    Fixture.fakeRequestParam( id, null );
    assertNull( dragSource[ 0 ] );
    assertNull( dragDestination[ 0 ] );
    id = DragDropEvent.PREFIX + node2.getUniqueID() + SUFFIX;
    Fixture.fakeRequestParam( id, id );
    Fixture.getLifeCycleAdapter( form ).processAction();
    Fixture.fakeRequestParam( id, null );
    assertNotNull( dragSource[ 0 ] );
    assertNotNull( dragDestination[ 0 ] );
    assertSame( leaf1, dragSource[ 0 ] );
    assertSame( node2, dragDestination[ 0 ] );    
  }
  
  public void testTreeNodeExpandedScript() throws Exception {
    WebForm form = Fixture.getEmptyWebFormInstance();
    Fixture.fakeBrowser( new Default( true ) );
    final Object[] evtSource = new Object[ 1 ];
    
    TreeView treeView = new TreeView();
    handleTreeNodeExpandedScript( form, evtSource, treeView );
    assertNotNull( evtSource[ 0 ] );
    assertSame( treeView, evtSource[ 0 ] );
    
    TreeNode treeNode = new TreeNode();
    handleTreeNodeExpandedScript( form, evtSource, treeNode );
    assertNotNull( evtSource[ 0 ] );
    assertSame( treeNode, evtSource[ 0 ] );
  }
  
  public void testTreeNodeExpandedNoScript() throws Exception {
    WebForm form = Fixture.getEmptyWebFormInstance();
    Fixture.fakeBrowser( new Default( false ) );
    final Object[] evtSource = new Object[ 1 ];
    
    TreeView treeView = new TreeView();
    handleTreeNodeExpandedNoScript( form, evtSource, treeView );
    assertNotNull( evtSource[ 0 ] );
    assertSame( treeView, evtSource[ 0 ] );
    
    TreeNode treeNode = new TreeNode();
    handleTreeNodeExpandedNoScript( form, evtSource, treeNode );
    assertNotNull( evtSource[ 0 ] );
    assertSame( treeNode, evtSource[ 0 ] );
  }
  
  public void testTreeNodeCollapsedScript() throws Exception {
    WebForm form = Fixture.getEmptyWebFormInstance();
    Fixture.fakeBrowser( new Default( true ) );
    final Object[] evtSource = new Object[ 1 ];
    
    TreeView treeView = new TreeView();
    handleTreeNodeCollapsedScript( form, evtSource, treeView );
    assertNotNull( evtSource[ 0 ] );
    assertSame( treeView, evtSource[ 0 ] );
    
    TreeNode treeNode = new TreeNode();
    handleTreeNodeCollapsedScript( form, evtSource, treeNode );
    assertNotNull( evtSource[ 0 ] );
    assertSame( treeNode, evtSource[ 0 ] );
  }

  public void testTreeNodeCollapsedNoScript() throws Exception {
    WebForm form = Fixture.getEmptyWebFormInstance();
    Fixture.fakeBrowser( new Default( false ) );
    final Object[] evtSource = new Object[ 1 ];
    
    TreeView treeView = new TreeView();
    handleTreeNodeCollapsedNoScript( form, evtSource, treeView );
    assertNotNull( evtSource[ 0 ] );
    assertSame( treeView, evtSource[ 0 ] );
    
    TreeNode treeNode = new TreeNode();
    handleTreeNodeCollapsedNoScript( form, evtSource, treeNode );
    assertNotNull( evtSource[ 0 ] );
    assertSame( treeNode, evtSource[ 0 ] );
  }

  public void testFocusGained() throws Exception {
    WebForm form = Fixture.getEmptyWebFormInstance();
    Fixture.fakeBrowser( new Default( true ) );
    final Object[] evtSource = new Object[ 1 ];

    WebText text = new WebText();
    handleFocusGainedListener( form, evtSource, text );
    assertNotNull( evtSource[ 0 ] );
    assertSame( text, evtSource[ 0 ] );
    
    WebTextArea area = new WebTextArea();
    handleFocusGainedListener( form, evtSource, area );
    assertNotNull( evtSource[ 0 ] );
    assertSame( area, evtSource[ 0 ] );
    
    WebSelect select = new WebSelect();
    handleFocusGainedListener( form, evtSource, select );
    assertNotNull( evtSource[ 0 ] );
    assertSame( select, evtSource[ 0 ] );
    
    WebRadioButton radioButton = new WebRadioButton();
    handleFocusGainedListener( form, evtSource, radioButton );
    assertNotNull( evtSource[ 0 ] );
    assertSame( radioButton, evtSource[ 0 ] );
    
    WebCheckBox checkBox = new WebCheckBox();
    handleFocusGainedListener( form, evtSource, checkBox );
    assertNotNull( evtSource[ 0 ] );
    assertSame( checkBox, evtSource[ 0 ] );
    
    WebButton button = new WebButton();
    handleFocusGainedListener( form, evtSource, button );
    assertNotNull( evtSource[ 0 ] );
    assertSame( button, evtSource[ 0 ] );
  }
  
  public void testItemStateChangedScript() throws Exception {
    WebForm form = Fixture.getEmptyWebFormInstance();
    Fixture.fakeBrowser( new Default( true ) );
    final Object[] evtSource = new Object[ 1 ];
    
    WebText text = new WebText();
    handleItemListenerScript( form, evtSource, text );
    assertNotNull( evtSource[ 0 ] );
    assertSame( text, evtSource[ 0 ] );
    
    WebTextArea area = new WebTextArea();
    handleItemListenerScript( form, evtSource, area );
    assertNotNull( evtSource[ 0 ] );
    assertSame( area, evtSource[ 0 ] );
    
    WebSelect select = new WebSelect();
    handleItemListenerScript( form, evtSource, select );
    assertNotNull( evtSource[ 0 ] );
    assertSame( select, evtSource[ 0 ] );
    
    WebRadioButtonGroup radioButtonGroup = new WebRadioButtonGroup();
    handleItemListenerScript( form, evtSource, radioButtonGroup );
    assertNotNull( evtSource[ 0 ] );
    assertSame( radioButtonGroup, evtSource[ 0 ] );
    
    WebCheckBox checkBox = new WebCheckBox();
    handleItemListenerScript( form, evtSource, checkBox );
    assertNotNull( evtSource [ 0 ] );
    assertSame( checkBox, evtSource[ 0 ] );
  }
  
  public void testItemStateChangedRadioScript() throws Exception {
    WebForm form = Fixture.getEmptyWebFormInstance();
    Fixture.fakeBrowser( new Default( true ) );
    final List events = new ArrayList();

    WebRadioButton radio1 = new WebRadioButton();
    WebRadioButton radio2 = new WebRadioButton();
    WebRadioButtonGroup group = new WebRadioButtonGroup();
    group.add( radio1 );
    group.add( radio2 );
    form.add( group, WebBorderLayout.NORTH );
    WebItemListener radioListener = new WebItemListener() {
      public void webItemStateChanged( final WebItemEvent evt ) {
        events.add( evt );
      }
    };
    group.addWebItemListener( radioListener );
    
    String id = group.getUniqueID();
    Fixture.fakeRequestParam( "webItemEvent", id );
    Fixture.fakeRequestParam( id, radio2.getValue() );
    Fixture.getLifeCycleAdapter( form ).readData();
    Fixture.getLifeCycleAdapter( form ).processAction();

    assertEquals( 1, events.size() );
    WebItemEvent evt = ( WebItemEvent )events.get( 0 );
    assertSame( group, evt.getSource() );
    assertFalse( radio1.isSelected() );
    assertTrue( radio2.isSelected() );
  }
  
  public void testItemStateChangedRadioNoScript() throws Exception {
    WebForm form = Fixture.getEmptyWebFormInstance();
    Fixture.fakeBrowser( new Default( false ) );
    final List events = new ArrayList();
    
    WebRadioButton radio1 = new WebRadioButton();
    WebRadioButton radio2 = new WebRadioButton();
    WebRadioButtonGroup group = new WebRadioButtonGroup();
    group.add( radio1 );
    group.add( radio2 );
    form.add( group, WebBorderLayout.NORTH );
    WebItemListener radioListener = new WebItemListener() {
      public void webItemStateChanged( final WebItemEvent evt ) {
        events.add( evt );
      }
    };
    group.addWebItemListener( radioListener );
    
    String uniqueID = group.getUniqueID();
    String id = WebItemEvent.PREFIX + uniqueID + SUFFIX;
    Fixture.fakeRequestParam( id, id );
    Fixture.fakeRequestParam( uniqueID, radio2.getValue() );
    Fixture.getLifeCycleAdapter( form ).readData();
    Fixture.getLifeCycleAdapter( form ).processAction();
    
    assertEquals( 1, events.size() );
    WebItemEvent evt = ( WebItemEvent )events.get( 0 );
    assertSame( group, evt.getSource() );
    assertFalse( radio1.isSelected() );
    assertTrue( radio2.isSelected() );
  }
  
  public void testItemStateChangedNoScript() throws Exception {
    WebForm form = Fixture.getEmptyWebFormInstance();
    Fixture.fakeBrowser( new Default( false ) );
    final Object[] evtSource = new Object[ 1 ];

    WebText text = new WebText();
    handleItemListenerNoScript( form, evtSource, text );
    assertNotNull( evtSource[ 0 ] );
    assertSame( text, evtSource[ 0 ] );
    
    WebTextArea area = new WebTextArea();
    handleItemListenerNoScript( form, evtSource, area );
    assertNotNull( evtSource[ 0 ] );
    assertSame( area, evtSource[ 0 ] );
    
    WebSelect select = new WebSelect();
    handleItemListenerNoScript( form, evtSource, select );
    assertNotNull( evtSource[ 0 ] );
    assertSame( select, evtSource[ 0 ] );
    
    WebRadioButtonGroup radioButtonGroup = new WebRadioButtonGroup();
    handleItemListenerNoScript( form, evtSource, radioButtonGroup );
    assertNotNull( evtSource[ 0 ] );
    assertSame( radioButtonGroup, evtSource[ 0 ] );
    
    WebCheckBox checkBox = new WebCheckBox();
    handleItemListenerNoScript( form, evtSource, checkBox );
    assertNotNull( evtSource [ 0 ] );
    assertSame( checkBox, evtSource[ 0 ] );
  }
  
  public void testActionPerformedScript() throws Exception  {
    WebForm form = Fixture.getEmptyWebFormInstance();
    Fixture.fakeBrowser( new Default( true ) );
    final Object[] evtSource = new Object[ 1 ];
    
    WebButton button = new WebButton();
    handleActionListenerScript( form, evtSource, button );
    assertNotNull( evtSource[ 0 ] );
    assertSame( button, evtSource[ 0 ] );

    TreeLeaf treeLeaf = new TreeLeaf();
    handleActionListenerScript( form, evtSource, treeLeaf );
    assertNotNull( evtSource[ 0 ] );
    assertSame( treeLeaf, evtSource[ 0 ] );
    
    TreeNode treeNode = new TreeNode();
    handleActionListenerScript( form, evtSource, treeNode );
    assertNotNull( evtSource[ 0 ] );
    assertSame( treeNode, evtSource[ 0 ] );
    
    TreeView tree = new TreeView();
    handleActionListenerScript( form, evtSource, tree );
    assertNotNull( evtSource[ 0 ] );
    assertSame( tree, evtSource[ 0 ] );
    
    MenuBar menuBar = new MenuBar();
    handleActionListenerScript( form, evtSource, menuBar );
    assertNotNull( evtSource[ 0 ] );
    assertSame( menuBar, evtSource[ 0 ] );
    
    Menu menu = new Menu();
    handleActionListenerScript( form, evtSource, menu );
    assertNotNull( evtSource[ 0 ] );
    assertSame( menu, evtSource[ 0 ] );
    
    MenuItem menuItem = new MenuItem();
    handleActionListenerScript( form, evtSource, menuItem );
    assertNotNull( evtSource[ 0 ] );
    assertSame( menuItem, evtSource[ 0 ] );
    
    form.removeAll();
    WebCardLayout cardLayout = new WebCardLayout();
    cardLayout.addWebActionListener( new WebActionListener() {
      public void webActionPerformed( final WebActionEvent evt ) {
        evtSource[ 0 ] = evt.getSource();
      }
    } );
    form.setWebLayout( cardLayout );
    WebPanel cardContent = new WebPanel();
    form.add( cardContent, "card1" );
    IRenderInfoAdapter adapter = getRenderInfoAdapter( cardLayout );
    adapter.createInfo();
    IServiceStateInfo stateInfo = ContextProvider.getStateInfo();
    stateInfo.setResponseWriter( new HtmlResponseWriter() );
    RendererCache instance = RendererCache.getInstance();
    Renderer renderer = instance.retrieveRenderer( WebCardLayout.class );
    renderer.render( form );
    WebButton[] cards = ( WebButton[] )adapter.getRenderState( "cardList" );
    Fixture.fakeRequestParam( "webActionEvent", cards[ 0 ].getUniqueID() );
    Fixture.getLifeCycleAdapter( form ).processAction();
    assertNotNull( evtSource[ 0 ] );
    assertSame( cardContent, evtSource[ 0 ] );    
  }

  public void testActionPerformedNoScript() throws Exception  {
    WebForm form = Fixture.getEmptyWebFormInstance();
    Fixture.fakeBrowser( new Default( false ) );
    final Object[] evtSource = new Object[ 1 ];
    
    WebButton button = new WebButton();
    handleActionListenerNoScript( form, evtSource, button );
    assertNotNull( evtSource[ 0 ] );
    assertSame( button, evtSource[ 0 ] );
    
    TreeLeaf treeLeaf = new TreeLeaf();
    handleActionListenerNoScript( form, evtSource, treeLeaf );
    assertNotNull( evtSource[ 0 ] );
    assertSame( treeLeaf, evtSource[ 0 ] );
    
    TreeNode treeNode = new TreeNode();
    handleActionListenerNoScript( form, evtSource, treeNode );
    assertNotNull( evtSource[ 0 ] );
    assertSame( treeNode, evtSource[ 0 ] );
    
    TreeView tree = new TreeView();
    handleActionListenerNoScript( form, evtSource, tree );
    assertNotNull( evtSource[ 0 ] );
    assertSame( tree, evtSource[ 0 ] );
    
    MenuBar menuBar = new MenuBar();
    handleActionListenerNoScript( form, evtSource, menuBar );
    assertNotNull( evtSource[ 0 ] );
    assertSame( menuBar, evtSource[ 0 ] );
    
    Menu menu = new Menu();
    handleActionListenerNoScript( form, evtSource, menu );
    assertNotNull( evtSource[ 0 ] );
    assertSame( menu, evtSource[ 0 ] );
    
    MenuItem menuItem = new MenuItem();
    handleActionListenerNoScript( form, evtSource, menuItem );
    assertNotNull( evtSource[ 0 ] );
    assertSame( menuItem, evtSource[ 0 ] );
    
    form.removeAll();
    WebCardLayout cardLayout = new WebCardLayout();
    cardLayout.addWebActionListener( new WebActionListener() {
      public void webActionPerformed( final WebActionEvent evt ) {
        evtSource[ 0 ] = evt.getSource();
      }
    } );
    form.setWebLayout( cardLayout );
    WebPanel cardContent = new WebPanel();
    form.add( cardContent, "card1" );
    IRenderInfoAdapter adapter = getRenderInfoAdapter( cardLayout );
    adapter.createInfo();
    IServiceStateInfo stateInfo = ContextProvider.getStateInfo();
    stateInfo.setResponseWriter( new HtmlResponseWriter() );
    RendererCache instance = RendererCache.getInstance();
    ImageCache.createInstance( Fixture.getWebAppBase().toString(), "" );
    Renderer renderer = instance.retrieveRenderer( WebCardLayout.class );
    renderer.render( form );
    WebButton[] cards = ( WebButton[] )adapter.getRenderState( "cardList" );
    String id = WebActionEvent.PREFIX + cards[ 0 ].getUniqueID() + SUFFIX;
    Fixture.fakeRequestParam( id, id );
    Fixture.getLifeCycleAdapter( form ).processAction();
    assertNotNull( evtSource[ 0 ] );
    assertSame( cardContent, evtSource[ 0 ] );    
  }
  
  public void testDoubleClickScript() {
    WebForm form = Fixture.getEmptyWebFormInstance();
    Fixture.fakeBrowser( new Default( true ) );
    // Create test listener
    final StringBuffer eventLog = new StringBuffer();
    DoubleClickListener listener = new DoubleClickListener() {
      public void doubleClickPerformed( final DoubleClickEvent event ) {
        eventLog.append( event.getSourceComponent().getUniqueID() );
      }
    };
    // Construct treeView with node and leaf
    TreeView treeView = new TreeView();
    TreeNode node1 = new TreeNode();
    treeView.addItem( node1 );
    TreeLeaf leaf1 = new TreeLeaf();
    node1.addItem( leaf1 );
    form.add( treeView, WebBorderLayout.NORTH );
    // Test recursion when adding/removing listener
    treeView.addDoubleClickListener( listener );
    assertEquals( true, DoubleClickEvent.hasListener( treeView ) );
    assertEquals( true, DoubleClickEvent.hasListener( node1 ) );
    assertEquals( true, DoubleClickEvent.hasListener( leaf1 ) );
    treeView.removeDoubleClickListener( listener );
    assertEquals( false, DoubleClickEvent.hasListener( treeView ) );
    assertEquals( false, DoubleClickEvent.hasListener( node1 ) );
    assertEquals( false, DoubleClickEvent.hasListener( leaf1 ) );
    
    leaf1.addDoubleClickListener( listener );
    assertEquals( true, DoubleClickEvent.hasListener( leaf1 ) );
    assertEquals( false, DoubleClickEvent.hasListener( node1 ) );
    assertEquals( false, DoubleClickEvent.hasListener( treeView ) );
    leaf1.removeDoubleClickListener( listener );
    assertEquals( false, DoubleClickEvent.hasListener( leaf1 ) );
    
    node1.addDoubleClickListener( listener );
    assertEquals( false, DoubleClickEvent.hasListener( leaf1 ) );
    assertEquals( true, DoubleClickEvent.hasListener( node1 ) );
    assertEquals( false, DoubleClickEvent.hasListener( treeView ) );
    node1.removeDoubleClickListener( listener );
    assertEquals( false, DoubleClickEvent.hasListener( node1 ) );
    
    treeView.addDoubleClickListener( listener );
    TreeNode node2 = new TreeNode();
    node1.addItem( node2 );
    assertEquals( true, DoubleClickEvent.hasListener( node2 ) );
    node2.remove();
    assertEquals( false, DoubleClickEvent.hasListener( node2 ) );
    // Test event processing - default browser
    eventLog.setLength( 0 );
    String fieldName = DoubleClickEvent.FIELD_NAME;
    Fixture.fakeRequestParam( fieldName, treeView.getUniqueID() );
    Fixture.getLifeCycleAdapter( form ).processAction();
    assertEquals( "", eventLog.toString() );
    eventLog.setLength( 0 );
    Fixture.fakeRequestParam( fieldName, node1.getUniqueID() );
    Fixture.getLifeCycleAdapter( form ).processAction();
    assertEquals( node1.getUniqueID(), eventLog.toString() );
    eventLog.setLength( 0 );
    Fixture.fakeRequestParam( fieldName, leaf1.getUniqueID() );
    Fixture.getLifeCycleAdapter( form ).processAction();
    assertEquals( leaf1.getUniqueID(), eventLog.toString() );
    // Test event processing - Opera 8
    Fixture.fakeBrowser( new Opera8( true ) );
    eventLog.setLength( 0 );
    Fixture.fakeRequestParam( fieldName, treeView.getUniqueID() );
    Fixture.getLifeCycleAdapter( form ).processAction();
    assertEquals( "", eventLog.toString() );
    eventLog.setLength( 0 );
    Fixture.fakeRequestParam( fieldName, node1.getUniqueID() );
    Fixture.getLifeCycleAdapter( form ).processAction();
    assertEquals( node1.getUniqueID(), eventLog.toString() );
    eventLog.setLength( 0 );
    Fixture.fakeRequestParam( fieldName, leaf1.getUniqueID() );
    Fixture.getLifeCycleAdapter( form ).processAction();
    assertEquals( leaf1.getUniqueID(), eventLog.toString() );
  }

  public void testDoubleClickNoscript() {
    Fixture.fakeBrowser( new Default( false ) );
    // Create test listener
    final StringBuffer eventLog = new StringBuffer();
    DoubleClickListener listener = new DoubleClickListener() {
      public void doubleClickPerformed( final DoubleClickEvent event ) {
        eventLog.append( event.getSourceComponent().getUniqueID() );
      }
    };
    // Construct treeView with node and leaf
    WebForm form = Fixture.getEmptyWebFormInstance();
    TreeView treeView = new TreeView();
    TreeNode node1 = new TreeNode();
    treeView.addItem( node1 );
    TreeLeaf leaf1 = new TreeLeaf();
    node1.addItem( leaf1 );
    form.add( treeView, WebBorderLayout.NORTH );
    treeView.addDoubleClickListener( listener );
    // Test event processing
    eventLog.setLength( 0 );
    String id = DoubleClickEvent.PREFIX + treeView.getUniqueID();
    Fixture.fakeRequestParam( id, id );
    Fixture.getLifeCycleAdapter( form ).processAction();
    assertEquals( "", eventLog.toString() );
    eventLog.setLength( 0 );
    id = DoubleClickEvent.PREFIX + node1.getUniqueID();
    Fixture.fakeRequestParam( id, id );
    Fixture.getLifeCycleAdapter( form ).processAction();
    assertEquals( node1.getUniqueID(), eventLog.toString() );
    eventLog.setLength( 0 );
    ContextProvider.getRequest().getParameterMap().clear();
    id = DoubleClickEvent.PREFIX + leaf1.getUniqueID();
    Fixture.fakeRequestParam( id, id );
    Fixture.getLifeCycleAdapter( form ).processAction();
    assertEquals( leaf1.getUniqueID(), eventLog.toString() );
  }

  private void handleActionListenerScript( final WebForm form, 
                                           final Object[] evtSource, 
                                           final WebComponent actionCmp )
    throws Exception
  {
    form.add( actionCmp, WebBorderLayout.NORTH );
    addActionListener( evtSource, actionCmp );
    Fixture.fakeRequestParam( "webActionEvent", actionCmp.getUniqueID() );
    Fixture.getLifeCycleAdapter( form ).processAction();
  }

  private void handleActionListenerNoScript( final WebForm form, 
                                             final Object[] evtSource, 
                                             final WebComponent actionCmp )
    throws Exception
  {
    form.add( actionCmp, WebBorderLayout.NORTH );
    addActionListener( evtSource, actionCmp );
    String id = WebActionEvent.PREFIX + actionCmp.getUniqueID()+ SUFFIX;
    Fixture.fakeRequestParam( id, id );
    Fixture.getLifeCycleAdapter( form ).processAction();
    Fixture.fakeRequestParam( id, null );
  }

  private void addActionListener( final Object[] evtSource, 
                                  final WebComponent actionComponent )
    throws NoSuchMethodException, 
           IllegalAccessException,
           InvocationTargetException
  {
    Class clazz = actionComponent.getClass();
    Method method = clazz.getMethod( "addWebActionListener", new Class[] {
      WebActionListener.class
    } );
    method.invoke( actionComponent, new Object[] {
      new WebActionListener() {
        public void webActionPerformed( final WebActionEvent evt ) {
          evtSource[ 0 ] = evt.getSource();
        }
      } 
    } );
  }

  private void handleItemListenerScript( final WebForm form, 
                                         final Object[] evtSource, 
                                         final WebComponent component )
  throws Exception
  {
    form.add( component, WebBorderLayout.NORTH );
    addItemListener( evtSource, component );
    String id = component.getUniqueID();
    if( component instanceof WebRadioButton ) {
      WebRadioButton radioButton = ( WebRadioButton )component;
      WebRadioButtonGroup group = WebRadioButtonUtil.findGroup( radioButton  );
      id = group.getUniqueID();
    }
    Fixture.fakeRequestParam( "webItemEvent", id );
    Fixture.getLifeCycleAdapter( form ).processAction();
  }
  
  private void handleItemListenerNoScript( final WebForm form, 
                                           final Object[] evtSource, 
                                           final WebComponent component )
    throws Exception
  {
    form.add( component, WebBorderLayout.NORTH );
    addItemListener( evtSource, component );
    String uniqueID = component.getUniqueID();
    if( component instanceof WebRadioButton ) {
      WebRadioButton radioButton = ( WebRadioButton )component;
      WebRadioButtonGroup group = WebRadioButtonUtil.findGroup( radioButton  );
      uniqueID = group.getUniqueID();
    }
    String id = WebItemEvent.PREFIX + uniqueID + SUFFIX;
    Fixture.fakeRequestParam( id, id );
    Fixture.getLifeCycleAdapter( form ).processAction();
    Fixture.fakeRequestParam( id, null );
  }

  private void addItemListener( final Object[] evtSource, 
                                final WebComponent component )
    throws NoSuchMethodException, 
           IllegalAccessException, 
           InvocationTargetException
  {
    Class clazz = component.getClass();
    Method method = clazz.getMethod( "addWebItemListener", new Class[] {
      WebItemListener.class
    } );
    method.invoke( component, new Object[] {
      new WebItemListener() {
        public void webItemStateChanged( final WebItemEvent evt ) {
          evtSource[ 0 ] = evt.getSource();
        }
      } 
    } );
  }

  private void handleFocusGainedListener( final WebForm form, 
                                                final Object[] evtSource, 
                                                final WebComponent component )
    throws Exception
  {
    form.add( component, WebBorderLayout.NORTH );
    addFocusGainedListener( evtSource, component );
    Fixture.fakeRequestParam( "webFocusGainedEvent", component.getUniqueID() );
    Fixture.getLifeCycleAdapter( form ).processAction();
  }

  private void addFocusGainedListener( final Object[] evtSource, 
                                       final WebComponent component )
    throws NoSuchMethodException,
           IllegalAccessException,
           InvocationTargetException
  {
    Class clazz = component.getClass();
    Method method = clazz.getMethod( "addWebFocusGainedListener", new Class[] {
      WebFocusGainedListener.class
    } );
    method.invoke( component, new Object[]{
      new WebFocusGainedListener() {
        public void webFocusGained( final WebFocusGainedEvent evt ) {
          evtSource[ 0 ] = evt.getSource();
        }
      }
    } );
  }
    
  private void handleTreeNodeCollapsedScript( final WebForm form, 
                                              final Object[] evtSource, 
                                              final TreeNode node )
    throws Exception
  {
    form.add( node, WebBorderLayout.NORTH );
    node.addWebTreeNodeCollapsedListener( new WebTreeNodeCollapsedListener() {
      public void webTreeNodeCollapsed( final WebTreeNodeCollapsedEvent evt ) {
        evtSource[ 0 ] = evt.getSource();
      }
    } );
    Fixture.fakeRequestParam( "webTreeNodeCollapsedEvent", node.getUniqueID() );
    Fixture.getLifeCycleAdapter( form ).processAction();    
  }

  private void handleTreeNodeCollapsedNoScript( final WebForm form, 
                                                final Object[] evtSource, 
                                                final TreeNode node )
    throws Exception
  {
    form.add( node, WebBorderLayout.NORTH );
    node.addWebTreeNodeCollapsedListener( new WebTreeNodeCollapsedListener() {
      public void webTreeNodeCollapsed( final WebTreeNodeCollapsedEvent evt ) {
        evtSource[ 0 ] = evt.getSource();
      }
    } );
    String id = WebTreeNodeCollapsedEvent.PREFIX + node.getUniqueID() + SUFFIX;
    Fixture.fakeRequestParam( id, id );
    Fixture.getLifeCycleAdapter( form ).processAction();
    Fixture.fakeRequestParam( id, null );
  }
  
  private void handleTreeNodeExpandedScript( final WebForm form, 
                                             final Object[] evtSource, 
                                             final TreeNode node )
    throws Exception
  {
    form.add( node, WebBorderLayout.NORTH );
    node.addWebTreeNodeExpandedListener( new WebTreeNodeExpandedListener() {
      public void webTreeNodeExpanded( final WebTreeNodeExpandedEvent evt ) {
        evtSource[ 0 ] = evt.getSource();
      }
    } );
    Fixture.fakeRequestParam( "webTreeNodeExpandedEvent", node.getUniqueID() );
    Fixture.getLifeCycleAdapter( form ).processAction();    
  }
  
  private void handleTreeNodeExpandedNoScript( final WebForm form, 
                                               final Object[] evtSource, 
                                               final TreeNode node )
  throws Exception
  {
    form.add( node, WebBorderLayout.NORTH );
    node.addWebTreeNodeExpandedListener( new WebTreeNodeExpandedListener() {
      public void webTreeNodeExpanded( final WebTreeNodeExpandedEvent evt ) {
        evtSource[ 0 ] = evt.getSource();
      }
    } );
    String id = WebTreeNodeExpandedEvent.PREFIX + node.getUniqueID() + SUFFIX;
    Fixture.fakeRequestParam( id, id );
    Fixture.getLifeCycleAdapter( form ).processAction();
    Fixture.fakeRequestParam( id, null );
  }
  
  private IRenderInfoAdapter getRenderInfoAdapter( final WebCardLayout wcl ) {
    return ( IRenderInfoAdapter )wcl.getAdapter( IRenderInfoAdapter.class );
  }
}