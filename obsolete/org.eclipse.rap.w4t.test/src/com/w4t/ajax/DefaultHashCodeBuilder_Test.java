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
import com.w4t.custom.CMenu;
import com.w4t.dhtml.*;
import com.w4t.internal.adaptable.IFormAdapter;
import com.w4t.mockup.*;
import com.w4t.types.WebColor;
import com.w4t.util.browser.Ie6;
import com.w4t.util.browser.Mozilla1_7;


public class DefaultHashCodeBuilder_Test extends TestCase {
  
  private static final String CARD_1 = "Card1";
  private static final String CARD_2 = "Card2";

  public void testSimpleWebComponent() {
    WebForm webForm = new TestForm();
    AjaxStatusUtil.preRender( webForm );
    int hashCode1 = getHashCode( webForm );
    assertTrue( hashCode1 != 17 );
    AjaxStatusUtil.preRender( webForm );
    int hashCode2 = getHashCode( webForm );
    assertEquals( hashCode1, hashCode2 );
    // webForm.setName( "abc" );
    webForm.setEnabled( false );
    AjaxStatusUtil.preRender( webForm );
    int hashCode3 = getHashCode( webForm );
    assertTrue( hashCode2 != hashCode3 );
  }
  
  public void testAddNewComponentToContainer() throws Exception {
    WebForm webForm = new TestForm();
    webForm.setWebLayout( new WebGridLayout() );
    webForm.add( new WebText(), new Position( 1, 1 ) );
    AjaxStatusUtil.preRender( webForm );
    int hashCode1 = getHashCode( webForm );
    webForm.add( new WebText(), new Position( 2, 1 ) );
    AjaxStatusUtil.preRender( webForm );
    int hashCode2 = getHashCode( webForm );
    assertTrue( hashCode1 != hashCode2 );
  }
  
  /** Ensure that changing property of child component must not affect hashCode
   * of parent */
  public void testPropertyChangeInChildComponent() throws Exception {
    WebForm webForm = new TestForm();
    webForm.setWebLayout( new WebGridLayout() );
    WebText webText1 = new WebText();
    webForm.add( webText1, new Position( 1, 1 ) );
    webText1.setStyle( new Style() );
    WebText webText2 = new WebText();
    webText2.setStyle( new Style() );
    webForm.add( webText2, new Position( 2, 1 ) );
    AjaxStatusUtil.preRender( webForm ); 
    int hashCode1 = getHashCode( webForm );
    webText1.getStyle().setBorder( "solid" );
    AjaxStatusUtil.preRender( webForm ); 
    int hashCode2 = getHashCode( webForm );
    assertTrue( hashCode1 == hashCode2 );
  }
  
  public void testComponentWithStyleProperty() throws Exception {
    WebForm webForm = new TestForm();
    webForm.setWebLayout( new WebGridLayout() );
    WebText component = new WebText();
    webForm.add( component, new Position( 1, 1 ) );
    AjaxStatusUtil.preRender( webForm ); 
    int hashCodeWithNullStyle = getHashCode( component );
    component.setStyle( new Style() );
    component.getStyle().setBorder( "solid" );
    AjaxStatusUtil.preRender( webForm ); 
    int hashCodeWithBorderStyle = getHashCode( component );
    assertTrue(    hashCodeWithNullStyle
                != hashCodeWithBorderStyle );
  }
  
  public void testAddAndDeleteChild() throws Exception {
    // form with two text controls
    WebForm webForm = new TestForm();
    webForm.setWebLayout( new WebGridLayout( 1, 3 ) );
    WebText webText1 = new WebText();
    webForm.add( webText1, new Position( 1, 1 ) );
    WebText webText2 = new WebText();
    webForm.add( webText2, new Position( 1, 2 )  );
    AjaxStatusUtil.preRender( webForm );
    int hashCode1 = getHashCode( webForm );
    // remove one text control and add another
    WebText webText3 = new WebText();
    webForm.add( webText3, new Position( 1, 3 )  );
    webForm.remove( webText2 );
    AjaxStatusUtil.preRender( webForm );
    int hashCode2 = getHashCode( webForm );
    assertTrue( hashCode1 != hashCode2 );
  }
  
  /** Ensure that component with a setter-only property is handled correctly */
  public void testWebColorHashCodeBuilder() throws Exception {
    // due to a bug in WebColor.hashCode a custom hashCodeBuilder for WebColor
    // is needed
    WebForm webForm = new TestForm();
    webForm.setWebLayout( new WebGridLayout() );
    WebText text = new WebText();
    webForm.add( text, new Position( 1, 1 ) );
    text.setStyle( new Style() );
    text.getStyle().setBgColor( new WebColor( "blue" ) );
    AjaxStatusUtil.preRender( webForm );
  }
  
  public void testRecursion() throws Exception {
    WebForm form = new TestForm();
    form.setWebLayout( new WebGridLayout() );
    WebContainer container = new WebPanel();
    form.add( container, new Position( 1, 1 ) );
    WebComponent custom = new WebPanel();
    container.add( custom );
    AjaxStatusUtil.preRender( form );
    assertTrue( getHashCode( form ) != 0 );
  }
  
  public void testWebGridLayout() throws Exception {
    WebForm form = new TestForm();
    WebGridLayout webGridLayout = new WebGridLayout( 2, 2 );
    form.setWebLayout( webGridLayout );
    form.add( new WebButton(), new Position( 1, 1 ) );
    AjaxStatusUtil.preRender( form );
    int hashCode1 = getHashCode( form );
    Area area1 = webGridLayout.getArea( new Position( 1, 1 ) );
    area1.setBgColor( new WebColor( "#ff0000" ) );
    AjaxStatusUtil.preRender( form );
    int hashCode2 = getHashCode( form );
    assertTrue( hashCode1 != hashCode2 );
    
    Area area2 = webGridLayout.getArea( new Position( 1, 2 ) );
    area2.setBgColor( new WebColor( "#ff0000") );
    AjaxStatusUtil.preRender( form );
    int hashCode3 = getHashCode( form );
    assertTrue( hashCode2 != hashCode3 );
  }
  
  public void testWebCardLayout() throws Exception {
    // Create form with card layout to test
    WebForm form = new TestForm();
    form.setWebLayout( new WebFlowLayout() );
    WebPanel cardPanel = new WebPanel();
    form.add( cardPanel );
    WebCardLayout cardLayout = new WebCardLayout();
    cardPanel.setWebLayout( cardLayout );
    cardPanel.add( new WebPanel(), CARD_1 );
    cardPanel.add( new WebPanel(), CARD_2 );
    // Change display card and assert that its hashCode changes
    // Simulate first request, displayCard is empty
    AjaxStatusUtil.preRender( form );
    AjaxStatusUtil.postRender( form );
    // Simulate second request within which the displayCard is changed
    cardLayout.setDisplayCard( CARD_2 );
    AjaxStatusUtil.preRender( form );
    String msg 
      = "Changing displayCard must lead to layout container being rendered";
    assertEquals( msg, true, mustRender( cardPanel ) );
    AjaxStatusUtil.postRender( form );
    assertEquals( false, mustRender( cardPanel ) );
  }
  
  public void testMustRenderWithNonAjaxComponent() throws Exception {
    // set ajax-enabled browser
    Fixture.fakeBrowser( new Mozilla1_7( true, true ) );
    // construct form with
    // - one panel containing 
    //   - one (ajax-enabled) label and 
    //   - one (non-ajax) component 
    TestForm testForm = new TestForm();
    testForm.setName( "testForm" );
    WebPanel firstPanel = new WebPanel();
    firstPanel.setName( "firstPanel" );
    testForm.add( firstPanel, WebBorderLayout.CENTER );
    WebLabel label = new WebLabel();
    label.setName( "label" );
    firstPanel.add( label );
    NonAjaxComponent nonAjaxComponent = new NonAjaxComponent();
    nonAjaxComponent.setName( "nonAjaxComponent" );
    firstPanel.add( nonAjaxComponent );
    // first pass - all components must be rendered
    AjaxStatusUtil.preRender( testForm );
    assertTrue( mustRender( testForm ) );
    assertTrue( mustRender( firstPanel ) );
    assertTrue( mustRender( label ) );
    assertTrue( mustRender( nonAjaxComponent ) );
    AjaxStatusUtil.postRender( testForm );
    // change nonAjaxComponent -> must lead to parent being rendered
    nonAjaxComponent.setEnabled( !nonAjaxComponent.isEnabled() );
    AjaxStatusUtil.preRender( testForm );
    assertFalse( mustRender( testForm ) );
    assertTrue( mustRender( firstPanel ) );
    assertTrue( mustRender( label ) );
    assertTrue( mustRender( nonAjaxComponent ) );
    AjaxStatusUtil.postRender( testForm );
    // change label (which is ajax-enabled) 
    //  -> must render label and firstPanel with children (due to nonAjaxComponent)  
    label.setValue( "other label value" );
    AjaxStatusUtil.preRender( testForm );
    assertFalse( mustRender( testForm ) );
    assertTrue( mustRender( firstPanel ) );
    assertTrue( mustRender( label ) );
    assertTrue( mustRender( nonAjaxComponent ) );
  }
  
  public void testTreeView() throws Exception {
    Fixture.fakeBrowser( new Ie6( true, true ) );
    // containing form
    WebForm testForm = new TestForm();
    testForm.setName( "testForm" );
    // create tree
    TreeView treeView = new TreeView();
    treeView.setName( "treeView" );
    TreeNode treeNode1 = new TreeNode();
    treeNode1.setName( "Node1" );
    treeNode1.setLabel( "Node1" );
    TreeNode treeNode2 = new TreeNode();
    treeNode2.setName( "Node2" );
    treeNode2.setLabel( "Node2" );
    treeView.addItem( treeNode1 );
    treeView.addItem( treeNode2 );
    TreeLeaf treeLeaf12 = new TreeLeaf();
    treeLeaf12.setName( "leaf12" );
    treeLeaf12.setLabel( "leaf12" );
    treeNode1.addItem( treeLeaf12 );
    TreeNode treeNode11 = new TreeNode();
    treeNode11.setLabel( "Node11" );
    treeNode11.setName( "Node11" );
    treeNode1.addItem( treeNode11 );
    TreeLeaf treeLeaf111 = new TreeLeaf();
    treeLeaf111.setLabel( "Leaf111" );
    treeLeaf111.setName( "Leaf111" );
    treeNode11.addItem( treeLeaf111 );
    treeView.setMinChildsDynLoad( 1 );
    testForm.add( treeView, WebBorderLayout.CENTER );
    
    treeView.setExpanded( true );

    //
    AjaxStatusUtil.preRender( testForm );
    assertTrue( mustRender( treeView ) );
    assertTrue( mustRender( treeNode1 ) );
    assertTrue( mustRender( treeNode2 ) );
    assertTrue( mustRender( treeNode11 ) );
    assertTrue( mustRender( treeLeaf12 ) );
    assertTrue( mustRender( treeLeaf111 ) );
    AjaxStatusUtil.postRender( testForm );
    // expand treeNode1
    treeNode1.setExpanded( true ); 
    AjaxStatusUtil.preRender( testForm );
    assertTrue( mustRender( treeNode1 ) );
    assertTrue( mustRender( treeNode11 ) );
    assertTrue( mustRender( treeLeaf12 ) );
    assertTrue( mustRender( treeLeaf111 ) );
    assertFalse( "treeView must not be rendered", mustRender( treeView ) );
    assertFalse( mustRender( treeNode2 ) );
    AjaxStatusUtil.postRender( testForm );
  }
  
  public void testMenuBar() throws Exception {
    Fixture.fakeBrowser( new Ie6( true, true ) );
    // containing form
    WebForm form = new TestForm();
    form.setName( "testForm" );

    // a new menuBar must be rendered 
    MenuBar menuBar = new MenuBar();
    form.add( menuBar, WebBorderLayout.NORTH );
    AjaxStatusUtil.preRender( form );
    assertTrue( mustRender( menuBar ) );
    AjaxStatusUtil.postRender( form );
    
    // add menu to menuBar, causes menuBar and menu to be rendered 
    Menu menu = new Menu();
    menuBar.addItem( menu );
    AjaxStatusUtil.preRender( form );
    assertTrue( mustRender( menuBar ) );
    assertTrue( mustRender( menu ) );
    AjaxStatusUtil.postRender( form );
    
    // change property of menu: causes menu to be rendered
    menu.setLabel( "abc" );
    AjaxStatusUtil.preRender( form );
    assertFalse( mustRender( menuBar ) );
    assertTrue( mustRender( menu ) );
    AjaxStatusUtil.postRender( form );
  }
  
  public void testCMenu() throws Exception {
    Fixture.fakeBrowser( new Ie6( true, true ) );
    // containing form
    WebForm form = new TestForm();
    form.setName( "testForm" );
    WebGridLayout MainFormWebGridLayout = new WebGridLayout( 5, 2 );
    form.setWebLayout( MainFormWebGridLayout );
    
    CMenu menu = new CMenu();
    form.add( menu, new Position( 1, 1 ) );
    
    AjaxStatusUtil.preRender( form );
    assertTrue( mustRender( menu ) );
    AjaxStatusUtil.postRender( form );
  }
  
  public void testForm() throws Exception {
    Fixture.fakeBrowser( new Ie6( true, true ) );
    // containing form
    WebForm form = new TestForm();
    form.setName( "testForm" );
    // initial  status: mustRender == true
    AjaxStatusUtil.preRender( form );
    assertTrue( mustRender( form ) );
    AjaxStatusUtil.postRender( form );
    
    // updating the requestCounter must not affect mustRender since there is
    // only a getRequestCounter method and no set...
    IFormAdapter adapter
      = ( IFormAdapter )form.getAdapter( IFormAdapter.class );
    adapter.updateRequestCounter( adapter.getRequestCounter() + 1 );
    AjaxStatusUtil.preRender( form );
    assertFalse( mustRender( form ) );
    AjaxStatusUtil.postRender( form );
  }
  
  private boolean mustRender( final WebComponent component ) {
    AjaxStatus ajaxStatus;
    ajaxStatus = ( AjaxStatus )component.getAdapter( AjaxStatus.class );
    return ajaxStatus.mustRender();
  }
  
  private static final int getHashCode( final WebComponent component ) {
    AjaxStatus ajaxStatus;
    ajaxStatus = ( AjaxStatus )component.getAdapter( AjaxStatus.class );
    return ajaxStatus.getComponentHashCode();
  }
  
  protected void setUp() throws Exception {
    Fixture.setUp();
    Fixture.createContext();
    Fixture.fakeBrowser( new Mozilla1_7( true, true ) );
  }
  
  protected void tearDown() throws Exception {
    Fixture.tearDown();
    Fixture.removeContext();
  }
  
}
