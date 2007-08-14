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

import junit.framework.TestCase;
import com.w4t.W4TFixture;


public class RendererCache_Safari2_Test extends TestCase {

  
  
  public void testNoscript() {
// assertTrue("test fails because Renderers for Safari2 are not yet implemented", false);
//  TODO implement Safari renderer and hook up this test
    
    
    //    Renderer renderer;
//    //
//    // Safari (no script) 
//    W4TFixture.fakeBrowser( new Safari2( false ) );
//
//    // WebAnchor
//    renderer = RendererCache.getInstance().retrieveRenderer( WebAnchor.class );
//    assertEquals( WebAnchorRenderer_Default_Noscript.class, 
//                  renderer.getClass() );
//    
//    // WebBorderComponent
//    renderer = RendererCache.getInstance().retrieveRenderer( WebBorderComponent.class );
//    assertEquals( WebBorderComponentRenderer_Default_Noscript.class, 
//                  renderer.getClass() );
//    
//    // WebScrollPane
//    renderer = RendererCache.getInstance().retrieveRenderer( WebScrollPane.class );
//    assertEquals( WebScrollPaneRenderer_Safari2_Noscript.class, 
//                  renderer.getClass() );
//    
//    // MenuItem
//    renderer = RendererCache.getInstance().retrieveRenderer( MenuItem.class );
//    assertEquals( MenuItemRenderer_Safari2_Noscript.class, 
//                  renderer.getClass() );
//  
//    // MenuItemSeparator
//    renderer = RendererCache.getInstance().retrieveRenderer( MenuItemSeparator.class );
//    assertEquals( MenuItemSeparatorRenderer_Safari2_Noscript.class, 
//                  renderer.getClass() );
//    
//    // TreeLeaf
//    renderer = RendererCache.getInstance().retrieveRenderer( TreeLeaf.class );
//    assertEquals( TreeLeafRenderer_Safari2_Noscript.class, 
//                  renderer.getClass() );
//    
//    //Menu
//    renderer = RendererCache.getInstance().retrieveRenderer( Menu.class );
//    assertEquals( MenuRenderer_Safari2_Noscript.class, 
//                  renderer.getClass() );
//    
//    //MenuBar
//    renderer = RendererCache.getInstance().retrieveRenderer( MenuBar.class );
//    assertEquals( MenuBarRenderer_Safari2_Noscript.class, 
//                  renderer.getClass() );
//    
//    //TreeNode
//    renderer = RendererCache.getInstance().retrieveRenderer( TreeNode.class );
//    assertEquals( TreeNodeRenderer_Safari2_Noscript.class, 
//                  renderer.getClass() );
//
//    //TreeView
//    renderer = RendererCache.getInstance().retrieveRenderer( TreeView.class );
//    assertEquals( TreeViewRenderer_Safari2_Noscript.class, 
//                  renderer.getClass() );
//    
//    //MenuButton    
//    renderer = RendererCache.getInstance().retrieveRenderer( MenuButton.class );
//    assertEquals( MenuButtonRenderer_Safari2_Noscript.class, 
//                  renderer.getClass() );
////
////    //NonAjaxComponent
////    renderer = RendererCache.getInstance().retrieveRenderer( NonAjaxComponent.class );
////    assertEquals( NonAjaxComponentRenderer_Default_Noscript.class, 
////                  renderer.getClass() );
////    
////    //TestComponent
////    renderer = RendererCache.getInstance().retrieveRenderer( TestComponent.class );
////    assertEquals( TestComponentRenderer_Default_Noscript.class, 
////                  renderer.getClass() );
////    
////    //SuperTestComponent
////    renderer = RendererCache.getInstance().retrieveRenderer( SuperTestComponent.class );
////    assertEquals( SuperTestComponentRenderer_Default_Noscript.class, 
////                  renderer.getClass() );
////    
////    //TestComponentWithoutRenderer
////    renderer = RendererCache.getInstance().retrieveRenderer( TestComponentWithoutRenderer.class );
////    assertEquals( TestComponentWithoutRendererRenderer_Default_Noscript.class, 
////                  renderer.getClass() );
////    
//    //WebButton
//    renderer = RendererCache.getInstance().retrieveRenderer( WebButton.class );
//    assertEquals( WebButtonRenderer_Default_Noscript.class, 
//                  renderer.getClass() );
//    
//    
//    //AreaSelector
//    renderer = RendererCache.getInstance().retrieveRenderer( WebButton.class );
//    assertEquals( WebButtonRenderer_Default_Noscript.class, 
//                  renderer.getClass() );   
//    
//    
//    //LinkButton
//    renderer = RendererCache.getInstance().retrieveRenderer( LinkButton.class );
//    assertEquals( WebButtonRenderer_Default_Noscript.class, 
//                  renderer.getClass() );
//    // testing for superclass
//    
//    //WebCheckBox
//    renderer = RendererCache.getInstance().retrieveRenderer( WebCheckBox.class );
//    assertEquals( WebCheckBoxRenderer_Default_Noscript.class, 
//                  renderer.getClass() );
//    
//    //WebContainer
//    renderer = RendererCache.getInstance().retrieveRenderer( WebContainer.class );
//    assertEquals( WebContainerRenderer_Default_Noscript.class, 
//                  renderer.getClass() );
//    
//    //CItemList
//    renderer = RendererCache.getInstance().retrieveRenderer( CItemList.class );
//    assertEquals( WebContainerRenderer_Default_Noscript.class, 
//                  renderer.getClass() );
//    // test for superclass
//    
//    //CTabbedPane
//    renderer = RendererCache.getInstance().retrieveRenderer( CTabbedPane.class );
//    assertEquals( WebContainerRenderer_Default_Noscript.class, 
//                  renderer.getClass() );
//    // test for superclass
//    
//    //CTable
//    renderer = RendererCache.getInstance().retrieveRenderer( CTable.class );
//    assertEquals( WebContainerRenderer_Default_Noscript.class, 
//                  renderer.getClass() );
//    // test for superclass
//    
//    //WebForm
//    renderer = RendererCache.getInstance().retrieveRenderer( WebForm.class );
//    assertEquals( WebFormRenderer_Default_Noscript.class, 
//                  renderer.getClass() );
//
//    //Form
//    renderer = RendererCache.getInstance().retrieveRenderer( Form.class );
//    assertEquals( WebFormRenderer_Default_Noscript.class, 
//                  renderer.getClass() );
//    // test for superclass
//    
//    //LoginForm
//    renderer = RendererCache.getInstance().retrieveRenderer( LoginForm.class );
//    assertEquals( WebFormRenderer_Default_Noscript.class, 
//                  renderer.getClass() );
//    // test for superclass
//    
//    //TestForm
//    renderer = RendererCache.getInstance().retrieveRenderer( TestForm.class );
//    assertEquals( WebFormRenderer_Default_Noscript.class, 
//                  renderer.getClass() );
//    // test for superclass
//    
//    //TreeForm
//    renderer = RendererCache.getInstance().retrieveRenderer( TreeForm.class );
//    assertEquals( WebFormRenderer_Default_Noscript.class, 
//                  renderer.getClass() );
//    // test for superclass
//    
//    // WebPanel 
//    renderer = RendererCache.getInstance().retrieveRenderer( WebPanel.class );
//    assertEquals( WebContainerRenderer_Default_Noscript.class, 
//                  renderer.getClass() );
//     
//    //CMenu
//    renderer = RendererCache.getInstance().retrieveRenderer( CMenu.class );
//    assertEquals( WebContainerRenderer_Default_Noscript.class, 
//                  renderer.getClass() );
//    // testing for superclass
//    
//    //CToolBar
//    renderer = RendererCache.getInstance().retrieveRenderer( CToolBar.class );
//    assertEquals( WebContainerRenderer_Default_Noscript.class, 
//                  renderer.getClass() );
//      //  testing for superclass
//    
//    //DBPoolCard
//    renderer = RendererCache.getInstance().retrieveRenderer( DBPoolCard.class );
//    assertEquals( WebContainerRenderer_Default_Noscript.class, 
//                  renderer.getClass() );
//      //  testing for superclass
//    
////    //InsertNavigationBar
////    renderer = RendererCache.getInstance().retrieveRenderer( InsertNavigationBar.class );
////    assertEquals( InsertNavigationBarRenderer_Default_Noscript.class, 
////                  renderer.getClass() );
////    
////    //NavigationBar
////    renderer = RendererCache.getInstance().retrieveRenderer( NavigationBar.class );
////    assertEquals( NavigationBarRenderer_Default_Noscript.class, 
////                  renderer.getClass() );
////    
//    //SelectViewer
////    renderer = RendererCache.getInstance().retrieveRenderer( SelectViewer.class );
////    assertEquals( SelectViewerRenderer_Default_Noscript.class, 
////                  renderer.getClass() );
////    
////    //TableViewer
////    renderer = RendererCache.getInstance().retrieveRenderer( TableViewer.class );
////    assertEquals( TableViewerRenderer_Default_Noscript.class, 
////                  renderer.getClass() );
////    
////    //TreeViewer
////    renderer = RendererCache.getInstance().retrieveRenderer( TreeViewer.class );
////    assertEquals( TreeViewerRenderer_Default_Noscript.class, 
////                  renderer.getClass() );
////    
////    //WebBorderPanel
////    renderer = RendererCache.getInstance().retrieveRenderer( WebBorderPanel.class );
////    assertEquals( WebBorderPanelRenderer_Default_Noscript.class, 
////                  renderer.getClass() );
////    
////    //WebTrafficLight
////    renderer = RendererCache.getInstance().retrieveRenderer( WebTrafficLight.class );
////    assertEquals( WebTrafficLightRenderer_Default_Noscript.class, 
////                  renderer.getClass() );
//    
//    //WebFileUpload
//    renderer = RendererCache.getInstance().retrieveRenderer( WebFileUpload.class );
//    assertEquals( WebFileUploadRenderer_Default_Noscript.class, 
//                  renderer.getClass() );
//    
//    //WebImage
//    renderer = RendererCache.getInstance().retrieveRenderer( WebImage.class );
//    assertEquals( WebImageRenderer_Default_Noscript.class, 
//                  renderer.getClass() );
//    
//    //WebLabel
//    renderer = RendererCache.getInstance().retrieveRenderer( WebLabel.class );
//    assertEquals( WebLabelRenderer_Default_Noscript.class, 
//                  renderer.getClass() );
//    
////    //MarkupLabel
////    renderer = RendererCache.getInstance().retrieveRenderer( MarkupLabel.class );
////    assertEquals( MarkupLabelRenderer_Default_Noscript.class, 
////                  renderer.getClass() );
//    
//    //PoolLabel
//    renderer = RendererCache.getInstance().retrieveRenderer( PoolLabel.class );
//    assertEquals( WebLabelRenderer_Default_Noscript.class, 
//                  renderer.getClass() );
//    //  testing for superclass
//    
//    //SuperWebLabel
//    renderer = RendererCache.getInstance().retrieveRenderer( SuperWebLabel.class );
//    assertEquals( WebLabelRenderer_Default_Noscript.class, 
//                  renderer.getClass() );
//    //  testing for superclass
//    
//    //WebRadioButton
//    renderer = RendererCache.getInstance().retrieveRenderer( WebRadioButton.class );
//    assertEquals( WebRadioButtonRenderer_Default_Noscript.class, 
//                  renderer.getClass() );
//    
////    //WebRadioButtonGroup
////    renderer = RendererCache.getInstance().retrieveRenderer( WebRadioButtonGroup.class );
////    assertEquals( WebRadioButtonGroupRenderer_Default_Noscript.class, 
////                  renderer.getClass() );
////    
//    //WebSelect
//    renderer = RendererCache.getInstance().retrieveRenderer( WebSelect.class );
//    assertEquals( WebSelectRenderer_Default_Noscript.class, 
//                  renderer.getClass() );
//    
//    //WebText
//    renderer = RendererCache.getInstance().retrieveRenderer( WebText.class );
//    assertEquals( WebTextRenderer_Default_Noscript.class, 
//                  renderer.getClass() );
//    
////    //DatePicker
////    renderer = RendererCache.getInstance().retrieveRenderer( DatePicker.class );
////    assertEquals( DatePickerRenderer_Default_Noscript.class, 
////                  renderer.getClass() );
////    
////    //WebTextWithoutTitleMock
////    renderer = RendererCache.getInstance().retrieveRenderer( WebTextWithoutTitleMock.class );
////    assertEquals( WebTextWithoutTitleMockRenderer_Default_Noscript.class, 
////                  renderer.getClass() );
////    
//    //WebTextArea
//    renderer = RendererCache.getInstance().retrieveRenderer( WebTextArea.class );
//    assertEquals( WebTextAreaRenderer_Default_Noscript.class, 
//                  renderer.getClass() );
//
//
//    // WebCardLayout
//    renderer = RendererCache.getInstance().retrieveRenderer( WebCardLayout.class );
//    assertEquals( WebCardLayoutRenderer_Safari2_Noscript.class, 
//                  renderer.getClass() );
//          
//  }
//  
//  
//  
//
//  public void testRetrieveRenderer_Safari2_Script() {
//    Renderer renderer;
//    //
//    // Safari (no script) 
//    W4TFixture.fakeBrowser( new Safari2( true ) );
//
//    // WebAnchor
//    renderer = RendererCache.getInstance().retrieveRenderer( WebAnchor.class );
//    assertEquals( WebAnchorRenderer_Default_Script.class, 
//                  renderer.getClass() );
//    
//    // WebBorderComponent
//    renderer = RendererCache.getInstance().retrieveRenderer( WebBorderComponent.class );
//    assertEquals( WebBorderComponentRenderer_Default_Script.class, 
//                  renderer.getClass() );
//    
//    // WebScrollPane
//    renderer = RendererCache.getInstance().retrieveRenderer( WebScrollPane.class );
//    assertEquals( WebScrollPaneRenderer_Safari2_Script.class, 
//                  renderer.getClass() );
//    
//    // MenuItem
//    renderer = RendererCache.getInstance().retrieveRenderer( MenuItem.class );
//    assertEquals( MenuItemRenderer_Default_Script.class, 
//                  renderer.getClass() );
//  
//    // MenuItemSeparator
//    renderer = RendererCache.getInstance().retrieveRenderer( MenuItemSeparator.class );
//    assertEquals( MenuItemSeparatorRenderer_Default_Script.class, 
//                  renderer.getClass() );
//    
//    // TreeLeaf
//    renderer = RendererCache.getInstance().retrieveRenderer( TreeLeaf.class );
//    assertEquals( TreeLeafRenderer_Safari2_Script.class, 
//                  renderer.getClass() );
//    
//    //Menu
//    renderer = RendererCache.getInstance().retrieveRenderer( Menu.class );
//    assertEquals( MenuRenderer_Default_Script.class, 
//                  renderer.getClass() );
//    
//    //MenuBar
//    renderer = RendererCache.getInstance().retrieveRenderer( MenuBar.class );
//    assertEquals( MenuBarRenderer_Default_Script.class, 
//                  renderer.getClass() );
//    
//    //TreeNode
//    renderer = RendererCache.getInstance().retrieveRenderer( TreeNode.class );
//    assertEquals( TreeNodeRenderer_Safari2_Script.class, 
//                  renderer.getClass() );
//
//    //TreeView
//    renderer = RendererCache.getInstance().retrieveRenderer( TreeView.class );
//    assertEquals( TreeViewRenderer_Safari2_Script.class, 
//                  renderer.getClass() );
//    
//    //MenuButton    
//    renderer = RendererCache.getInstance().retrieveRenderer( MenuButton.class );
//    assertEquals( MenuButtonRenderer_Default_Script.class, 
//                  renderer.getClass() );
////
////    //NonAjaxComponent
////    renderer = RendererCache.getInstance().retrieveRenderer( NonAjaxComponent.class );
////    assertEquals( NonAjaxComponentRenderer_Default_Script.class, 
////                  renderer.getClass() );
////    
////    //TestComponent
////    renderer = RendererCache.getInstance().retrieveRenderer( TestComponent.class );
////    assertEquals( TestComponentRenderer_Default_Script.class, 
////                  renderer.getClass() );
////    
////    //SuperTestComponent
////    renderer = RendererCache.getInstance().retrieveRenderer( SuperTestComponent.class );
////    assertEquals( SuperTestComponentRenderer_Default_Script.class, 
////                  renderer.getClass() );
////    
////    //TestComponentWithoutRenderer
////    renderer = RendererCache.getInstance().retrieveRenderer( TestComponentWithoutRenderer.class );
////    assertEquals( TestComponentWithoutRendererRenderer_Default_Script.class, 
////                  renderer.getClass() );
////    
//    //WebButton
//    renderer = RendererCache.getInstance().retrieveRenderer( WebButton.class );
//    assertEquals( WebButtonRenderer_Default_Script.class, 
//                  renderer.getClass() );
//    
//    
//    //AreaSelector
//    renderer = RendererCache.getInstance().retrieveRenderer( WebButton.class );
//    assertEquals( WebButtonRenderer_Default_Script.class, 
//                  renderer.getClass() );   
//    
//    
//    //LinkButton
//    renderer = RendererCache.getInstance().retrieveRenderer( LinkButton.class );
//    assertEquals( WebButtonRenderer_Default_Script.class, 
//                  renderer.getClass() );
//    // testing for superclass
//    
//    //WebCheckBox
//    renderer = RendererCache.getInstance().retrieveRenderer( WebCheckBox.class );
//    assertEquals( WebCheckBoxRenderer_Default_Script.class, 
//                  renderer.getClass() );
//    
//    //WebContainer
//    renderer = RendererCache.getInstance().retrieveRenderer( WebContainer.class );
//    assertEquals( WebContainerRenderer_Default_Script.class, 
//                  renderer.getClass() );
//    
//    //CItemList
//    renderer = RendererCache.getInstance().retrieveRenderer( CItemList.class );
//    assertEquals( WebContainerRenderer_Default_Script.class, 
//                  renderer.getClass() );
//    // test for superclass
//    
//    //CTabbedPane
//    renderer = RendererCache.getInstance().retrieveRenderer( CTabbedPane.class );
//    assertEquals( WebContainerRenderer_Default_Script.class, 
//                  renderer.getClass() );
//    // test for superclass
//    
//    //CTable
//    renderer = RendererCache.getInstance().retrieveRenderer( CTable.class );
//    assertEquals( WebContainerRenderer_Default_Script.class, 
//                  renderer.getClass() );
//    // test for superclass
//    
//    //WebForm
//    renderer = RendererCache.getInstance().retrieveRenderer( WebForm.class );
//    assertEquals( WebFormRenderer_Default_Script.class, 
//                  renderer.getClass() );
//
//    //Form
//    renderer = RendererCache.getInstance().retrieveRenderer( Form.class );
//    assertEquals( WebFormRenderer_Default_Script.class, 
//                  renderer.getClass() );
//    // test for superclass
//    
//    //LoginForm
//    renderer = RendererCache.getInstance().retrieveRenderer( LoginForm.class );
//    assertEquals( WebFormRenderer_Default_Script.class, 
//                  renderer.getClass() );
//    // test for superclass
//    
//    //TestForm
//    renderer = RendererCache.getInstance().retrieveRenderer( TestForm.class );
//    assertEquals( WebFormRenderer_Default_Script.class, 
//                  renderer.getClass() );
//    // test for superclass
//    
//    //TreeForm
//    renderer = RendererCache.getInstance().retrieveRenderer( TreeForm.class );
//    assertEquals( WebFormRenderer_Default_Script.class, 
//                  renderer.getClass() );
//    // test for superclass
//    
//    // WebPanel 
//    renderer = RendererCache.getInstance().retrieveRenderer( WebPanel.class );
//    assertEquals( WebContainerRenderer_Default_Script.class, 
//                  renderer.getClass() );
//     
//    //CMenu
//    renderer = RendererCache.getInstance().retrieveRenderer( CMenu.class );
//    assertEquals( WebContainerRenderer_Default_Script.class, 
//                  renderer.getClass() );
//    // testing for superclass
//    
//    //CToolBar
//    renderer = RendererCache.getInstance().retrieveRenderer( CToolBar.class );
//    assertEquals( WebContainerRenderer_Default_Script.class, 
//                  renderer.getClass() );
//      //  testing for superclass
//    
//    //DBPoolCard
//    renderer = RendererCache.getInstance().retrieveRenderer( DBPoolCard.class );
//    assertEquals( WebContainerRenderer_Default_Script.class, 
//                  renderer.getClass() );
//      //  testing for superclass
//    
////    //InsertNavigationBar
////    renderer = RendererCache.getInstance().retrieveRenderer( InsertNavigationBar.class );
////    assertEquals( InsertNavigationBarRenderer_Default_Script.class, 
////                  renderer.getClass() );
////    
////    //NavigationBar
////    renderer = RendererCache.getInstance().retrieveRenderer( NavigationBar.class );
////    assertEquals( NavigationBarRenderer_Default_Script.class, 
////                  renderer.getClass() );
////    
//    //SelectViewer
////    renderer = RendererCache.getInstance().retrieveRenderer( SelectViewer.class );
////    assertEquals( SelectViewerRenderer_Default_Script.class, 
////                  renderer.getClass() );
////    
////    //TableViewer
////    renderer = RendererCache.getInstance().retrieveRenderer( TableViewer.class );
////    assertEquals( TableViewerRenderer_Default_Script.class, 
////                  renderer.getClass() );
////    
////    //TreeViewer
////    renderer = RendererCache.getInstance().retrieveRenderer( TreeViewer.class );
////    assertEquals( TreeViewerRenderer_Default_Script.class, 
////                  renderer.getClass() );
////    
////    //WebBorderPanel
////    renderer = RendererCache.getInstance().retrieveRenderer( WebBorderPanel.class );
////    assertEquals( WebBorderPanelRenderer_Default_Script.class, 
////                  renderer.getClass() );
////    
////    //WebTrafficLight
////    renderer = RendererCache.getInstance().retrieveRenderer( WebTrafficLight.class );
////    assertEquals( WebTrafficLightRenderer_Default_Script.class, 
////                  renderer.getClass() );
//    
//    //WebFileUpload
//    renderer = RendererCache.getInstance().retrieveRenderer( WebFileUpload.class );
//    assertEquals( WebFileUploadRenderer_Default_Script.class, 
//                  renderer.getClass() );
//    
//    //WebImage
//    renderer = RendererCache.getInstance().retrieveRenderer( WebImage.class );
//    assertEquals( WebImageRenderer_Default_Script.class, 
//                  renderer.getClass() );
//    
//    //WebLabel
//    renderer = RendererCache.getInstance().retrieveRenderer( WebLabel.class );
//    assertEquals( WebLabelRenderer_Default_Script.class, 
//                  renderer.getClass() );
//    
////    //MarkupLabel
////    renderer = RendererCache.getInstance().retrieveRenderer( MarkupLabel.class );
////    assertEquals( MarkupLabelRenderer_Default_Script.class, 
////                  renderer.getClass() );
//    
//    //PoolLabel
//    renderer = RendererCache.getInstance().retrieveRenderer( PoolLabel.class );
//    assertEquals( WebLabelRenderer_Default_Script.class, 
//                  renderer.getClass() );
//    //  testing for superclass
//    
//    //SuperWebLabel
//    renderer = RendererCache.getInstance().retrieveRenderer( SuperWebLabel.class );
//    assertEquals( WebLabelRenderer_Default_Script.class, 
//                  renderer.getClass() );
//    //  testing for superclass
//    
//    //WebRadioButton
//    renderer = RendererCache.getInstance().retrieveRenderer( WebRadioButton.class );
//    assertEquals( WebRadioButtonRenderer_Default_Script.class, 
//                  renderer.getClass() );
//    
////    //WebRadioButtonGroup
////    renderer = RendererCache.getInstance().retrieveRenderer( WebRadioButtonGroup.class );
////    assertEquals( WebRadioButtonGroupRenderer_Default_Script.class, 
////                  renderer.getClass() );
////    
//    //WebSelect
//    renderer = RendererCache.getInstance().retrieveRenderer( WebSelect.class );
//    assertEquals( WebSelectRenderer_Default_Script.class, 
//                  renderer.getClass() );
//    
//    //WebText
//    renderer = RendererCache.getInstance().retrieveRenderer( WebText.class );
//    assertEquals( WebTextRenderer_Default_Script.class, 
//                  renderer.getClass() );
//    
////    //DatePicker
////    renderer = RendererCache.getInstance().retrieveRenderer( DatePicker.class );
////    assertEquals( DatePickerRenderer_Default_Script.class, 
////                  renderer.getClass() );
////    
////    //WebTextWithoutTitleMock
////    renderer = RendererCache.getInstance().retrieveRenderer( WebTextWithoutTitleMock.class );
////    assertEquals( WebTextWithoutTitleMockRenderer_Default_Script.class, 
////                  renderer.getClass() );
////    
//    //WebTextArea
//    renderer = RendererCache.getInstance().retrieveRenderer( WebTextArea.class );
//    assertEquals( WebTextAreaRenderer_Default_Script.class, 
//                  renderer.getClass() );
//
//
//    // WebCardLayout
//    renderer = RendererCache.getInstance().retrieveRenderer( WebCardLayout.class );
//    assertEquals( WebCardLayoutRenderer_Default_Script.class, 
//                  renderer.getClass() );
//          
//  }
//  
//  public void testRetrieveRenderer_Safari2_Ajax() {
//    Renderer renderer;
//    //
//    // Safari (ajax) 
//    W4TFixture.fakeBrowser( new Safari( true, true ) );
//
//    // WebAnchor
//    renderer = RendererCache.getInstance().retrieveRenderer( WebAnchor.class );
//    assertEquals( WebAnchorRenderer_Default_Ajax.class, 
//                  renderer.getClass() );
//    
//    // WebBorderComponent
//    renderer = RendererCache.getInstance().retrieveRenderer( WebBorderComponent.class );
//    assertEquals( WebBorderComponentRenderer_Default_Ajax.class, 
//                  renderer.getClass() );
//    
//    // WebScrollPane
//    renderer = RendererCache.getInstance().retrieveRenderer( WebScrollPane.class );
//    assertEquals( WebScrollPaneRenderer.class, 
//                  renderer.getClass() );
//    
//    // MenuItem
//    renderer = RendererCache.getInstance().retrieveRenderer( MenuItem.class );
//    assertEquals( MenuItemRenderer_Default_Ajax.class, 
//                  renderer.getClass() );
//  
//    // MenuItemSeparator
//    renderer = RendererCache.getInstance().retrieveRenderer( MenuItemSeparator.class );
//    assertEquals( MenuItemSeparatorRenderer_Default_Ajax.class, 
//                  renderer.getClass() );
//    
////    // TreeLeaf
////    renderer = RendererCache.getInstance().retrieveRenderer( TreeLeaf.class );
////    assertEquals( TreeLeafRenderer_Safari2_Ajax.class, 
////                  renderer.getClass() );
////    
//    //Menu
//    renderer = RendererCache.getInstance().retrieveRenderer( Menu.class );
//    assertEquals( MenuRenderer_Default_Ajax.class, 
//                  renderer.getClass() );
//    
//    //MenuBar
//    renderer = RendererCache.getInstance().retrieveRenderer( MenuBar.class );
//    assertEquals( MenuBarRenderer_Default_Ajax.class, 
//                  renderer.getClass() );
//    
//    //TreeNode
//    renderer = RendererCache.getInstance().retrieveRenderer( TreeNode.class );
//    assertEquals( TreeNodeRenderer.class, 
//                  renderer.getClass() );
//
////    //TreeView
////    renderer = RendererCache.getInstance().retrieveRenderer( TreeView.class );
////    assertEquals( TreeViewRenderer_Safari2_Ajax.class, 
////                  renderer.getClass() );
////    
//    //MenuButton    
//    renderer = RendererCache.getInstance().retrieveRenderer( MenuButton.class );
//    assertEquals( MenuButtonRenderer_Default_Ajax.class, 
//                  renderer.getClass() );
////
////    //NonAjaxComponent
////    renderer = RendererCache.getInstance().retrieveRenderer( NonAjaxComponent.class );
////    assertEquals( NonAjaxComponentRenderer_Default_Ajax.class, 
////                  renderer.getClass() );
////    
////    //TestComponent
////    renderer = RendererCache.getInstance().retrieveRenderer( TestComponent.class );
////    assertEquals( TestComponentRenderer_Default_Ajax.class, 
////                  renderer.getClass() );
////    
////    //SuperTestComponent
////    renderer = RendererCache.getInstance().retrieveRenderer( SuperTestComponent.class );
////    assertEquals( SuperTestComponentRenderer_Default_Ajax.class, 
////                  renderer.getClass() );
////    
////    //TestComponentWithoutRenderer
////    renderer = RendererCache.getInstance().retrieveRenderer( TestComponentWithoutRenderer.class );
////    assertEquals( TestComponentWithoutRendererRenderer_Default_Ajax.class, 
////                  renderer.getClass() );
////    
//    //WebButton
//    renderer = RendererCache.getInstance().retrieveRenderer( WebButton.class );
//    assertEquals( WebButtonRenderer_Default_Ajax.class, 
//                  renderer.getClass() );
//    
//    
//    //AreaSelector
//    renderer = RendererCache.getInstance().retrieveRenderer( WebButton.class );
//    assertEquals( WebButtonRenderer_Default_Ajax.class, 
//                  renderer.getClass() );   
//    
//    
//    //LinkButton
//    renderer = RendererCache.getInstance().retrieveRenderer( LinkButton.class );
//    assertEquals( WebButtonRenderer_Default_Ajax.class, 
//                  renderer.getClass() );
//    // testing for superclass
//    
//    //WebCheckBox
//    renderer = RendererCache.getInstance().retrieveRenderer( WebCheckBox.class );
//    assertEquals( WebCheckBoxRenderer_Default_Ajax.class, 
//                  renderer.getClass() );
//    
//    //WebContainer
//    renderer = RendererCache.getInstance().retrieveRenderer( WebContainer.class );
//    assertEquals( WebContainerRenderer_Default_Ajax.class, 
//                  renderer.getClass() );
//    
//    //CItemList
//    renderer = RendererCache.getInstance().retrieveRenderer( CItemList.class );
//    assertEquals( WebContainerRenderer_Default_Ajax.class, 
//                  renderer.getClass() );
//    // test for superclass
//    
//    //CTabbedPane
//    renderer = RendererCache.getInstance().retrieveRenderer( CTabbedPane.class );
//    assertEquals( WebContainerRenderer_Default_Ajax.class, 
//                  renderer.getClass() );
//    // test for superclass
//    
//    //CTable
//    renderer = RendererCache.getInstance().retrieveRenderer( CTable.class );
//    assertEquals( WebContainerRenderer_Default_Ajax.class, 
//                  renderer.getClass() );
//    // test for superclass
//    
//    //WebForm
//    renderer = RendererCache.getInstance().retrieveRenderer( WebForm.class );
//    assertEquals( WebFormRenderer_Default_Ajax.class, 
//                  renderer.getClass() );
//
//    //Form
//    renderer = RendererCache.getInstance().retrieveRenderer( Form.class );
//    assertEquals( WebFormRenderer_Default_Ajax.class, 
//                  renderer.getClass() );
//    // test for superclass
//    
//    //LoginForm
//    renderer = RendererCache.getInstance().retrieveRenderer( LoginForm.class );
//    assertEquals( WebFormRenderer_Default_Ajax.class, 
//                  renderer.getClass() );
//    // test for superclass
//    
//    //TestForm
//    renderer = RendererCache.getInstance().retrieveRenderer( TestForm.class );
//    assertEquals( WebFormRenderer_Default_Ajax.class, 
//                  renderer.getClass() );
//    // test for superclass
//    
//    //TreeForm
//    renderer = RendererCache.getInstance().retrieveRenderer( TreeForm.class );
//    assertEquals( WebFormRenderer_Default_Ajax.class, 
//                  renderer.getClass() );
//    // test for superclass
//    
//    // WebPanel 
//    renderer = RendererCache.getInstance().retrieveRenderer( WebPanel.class );
//    assertEquals( WebContainerRenderer_Default_Ajax.class, 
//                  renderer.getClass() );
//     
//    //CMenu
//    renderer = RendererCache.getInstance().retrieveRenderer( CMenu.class );
//    assertEquals( WebContainerRenderer_Default_Ajax.class, 
//                  renderer.getClass() );
//    // testing for superclass
//    
//    //CToolBar
//    renderer = RendererCache.getInstance().retrieveRenderer( CToolBar.class );
//    assertEquals( WebContainerRenderer_Default_Ajax.class, 
//                  renderer.getClass() );
//      //  testing for superclass
//    
//    //DBPoolCard
//    renderer = RendererCache.getInstance().retrieveRenderer( DBPoolCard.class );
//    assertEquals( WebContainerRenderer_Default_Ajax.class, 
//                  renderer.getClass() );
//      //  testing for superclass
//    
////    //InsertNavigationBar
////    renderer = RendererCache.getInstance().retrieveRenderer( InsertNavigationBar.class );
////    assertEquals( InsertNavigationBarRenderer_Default_Ajax.class, 
////                  renderer.getClass() );
////    
////    //NavigationBar
////    renderer = RendererCache.getInstance().retrieveRenderer( NavigationBar.class );
////    assertEquals( NavigationBarRenderer_Default_Ajax.class, 
////                  renderer.getClass() );
////    
//    //SelectViewer
////    renderer = RendererCache.getInstance().retrieveRenderer( SelectViewer.class );
////    assertEquals( SelectViewerRenderer_Default_Ajax.class, 
////                  renderer.getClass() );
////    
////    //TableViewer
////    renderer = RendererCache.getInstance().retrieveRenderer( TableViewer.class );
////    assertEquals( TableViewerRenderer_Default_Ajax.class, 
////                  renderer.getClass() );
////    
////    //TreeViewer
////    renderer = RendererCache.getInstance().retrieveRenderer( TreeViewer.class );
////    assertEquals( TreeViewerRenderer_Default_Ajax.class, 
////                  renderer.getClass() );
////    
////    //WebBorderPanel
////    renderer = RendererCache.getInstance().retrieveRenderer( WebBorderPanel.class );
////    assertEquals( WebBorderPanelRenderer_Default_Ajax.class, 
////                  renderer.getClass() );
////    
////    //WebTrafficLight
////    renderer = RendererCache.getInstance().retrieveRenderer( WebTrafficLight.class );
////    assertEquals( WebTrafficLightRenderer_Default_Ajax.class, 
////                  renderer.getClass() );
//    
////    //WebFileUpload
////    renderer = RendererCache.getInstance().retrieveRenderer( WebFileUpload.class );
////    assertEquals( WebFileUploadRenderer_Default_Ajax.class, 
////                  renderer.getClass() );
////    
//    //WebImage
//    renderer = RendererCache.getInstance().retrieveRenderer( WebImage.class );
//    assertEquals( WebImageRenderer_Default_Ajax.class, 
//                  renderer.getClass() );
//    
//    //WebLabel
//    renderer = RendererCache.getInstance().retrieveRenderer( WebLabel.class );
//    assertEquals( WebLabelRenderer_Default_Ajax.class, 
//                  renderer.getClass() );
//    
////    //MarkupLabel
////    renderer = RendererCache.getInstance().retrieveRenderer( MarkupLabel.class );
////    assertEquals( MarkupLabelRenderer_Default_Ajax.class, 
////                  renderer.getClass() );
//    
//    //PoolLabel
//    renderer = RendererCache.getInstance().retrieveRenderer( PoolLabel.class );
//    assertEquals( WebLabelRenderer_Default_Ajax.class, 
//                  renderer.getClass() );
//    //  testing for superclass
//    
//    //SuperWebLabel
//    renderer = RendererCache.getInstance().retrieveRenderer( SuperWebLabel.class );
//    assertEquals( WebLabelRenderer_Default_Ajax.class, 
//                  renderer.getClass() );
//    //  testing for superclass
//    
//    //WebRadioButton
//    renderer = RendererCache.getInstance().retrieveRenderer( WebRadioButton.class );
//    assertEquals( WebRadioButtonRenderer_Default_Ajax.class, 
//                  renderer.getClass() );
//    
////    //WebRadioButtonGroup
////    renderer = RendererCache.getInstance().retrieveRenderer( WebRadioButtonGroup.class );
////    assertEquals( WebRadioButtonGroupRenderer_Default_Ajax.class, 
////                  renderer.getClass() );
////    
//    //WebSelect
//    renderer = RendererCache.getInstance().retrieveRenderer( WebSelect.class );
//    assertEquals( WebSelectRenderer_Default_Ajax.class, 
//                  renderer.getClass() );
//    
//    //WebText
//    renderer = RendererCache.getInstance().retrieveRenderer( WebText.class );
//    assertEquals( WebTextRenderer_Default_Ajax.class, 
//                  renderer.getClass() );
//    
////    //DatePicker
////    renderer = RendererCache.getInstance().retrieveRenderer( DatePicker.class );
////    assertEquals( DatePickerRenderer_Default_Ajax.class, 
////                  renderer.getClass() );
////    
////    //WebTextWithoutTitleMock
////    renderer = RendererCache.getInstance().retrieveRenderer( WebTextWithoutTitleMock.class );
////    assertEquals( WebTextWithoutTitleMockRenderer_Default_Ajax.class, 
////                  renderer.getClass() );
////    
//    //WebTextArea
//    renderer = RendererCache.getInstance().retrieveRenderer( WebTextArea.class );
//    assertEquals( WebTextAreaRenderer_Default_Ajax.class, 
//                  renderer.getClass() );
//
////
////    // WebCardLayout
////    renderer = RendererCache.getInstance().retrieveRenderer( WebCardLayout.class );
////    assertEquals( WebCardLayoutRenderer_Safari2_Ajax.class, 
////                  renderer.getClass() );
////          
  }
    
  
  
  
  protected void setUp() throws Exception {
    W4TFixture.setUp();
  }
  
  protected void tearDown() throws Exception {
    W4TFixture.tearDown();
  }
}
