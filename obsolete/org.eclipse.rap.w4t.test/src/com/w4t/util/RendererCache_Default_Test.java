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
import com.w4t.*;
import com.w4t.WebFormListener_Test.Form;
import com.w4t.administration.LinkButton;
import com.w4t.administration.PoolLabel;
import com.w4t.custom.*;
import com.w4t.dhtml.*;
import com.w4t.dhtml.menubarkit.MenuBarRenderer_Default_Noscript;
import com.w4t.dhtml.menubarkit.MenuBarRenderer_Default_Script;
import com.w4t.dhtml.menubuttonkit.MenuButtonRenderer_Default_Noscript;
import com.w4t.dhtml.menubuttonkit.MenuButtonRenderer_Default_Script;
import com.w4t.dhtml.menuitemkit.MenuItemRenderer_Default_Noscript;
import com.w4t.dhtml.menuitemkit.MenuItemRenderer_Default_Script;
import com.w4t.dhtml.menuitemseparatorkit.MenuItemSeparatorRenderer_Default_Noscript;
import com.w4t.dhtml.menuitemseparatorkit.MenuItemSeparatorRenderer_Default_Script;
import com.w4t.dhtml.menukit.MenuRenderer_Default_Noscript;
import com.w4t.dhtml.menukit.MenuRenderer_Default_Script;
import com.w4t.dhtml.treeleafkit.TreeLeafRenderer_Default_Noscript;
import com.w4t.dhtml.treeleafkit.TreeLeafRenderer_Default_Script;
import com.w4t.dhtml.treenodekit.TreeNodeRenderer_Default_Noscript;
import com.w4t.dhtml.treenodekit.TreeNodeRenderer_Default_Script;
import com.w4t.dhtml.treeviewkit.TreeViewRenderer_Default_Noscript;
import com.w4t.dhtml.treeviewkit.TreeViewRenderer_Default_Script;
import com.w4t.dhtml.webscrollpanekit.WebScrollPaneRenderer_Default_Noscript;
import com.w4t.dhtml.webscrollpanekit.WebScrollPaneRenderer_Default_Script;
import com.w4t.engine.lifecycle.standard.LoginForm;
import com.w4t.engine.util.ExitForm;
import com.w4t.engine.util.exitformkit.ExitFormRenderer_Default_Noscript;
import com.w4t.engine.util.exitformkit.ExitFormRenderer_Default_Script;
import com.w4t.mockup.*;
import com.w4t.mockup.nonajaxcomponentkit.NonAjaxComponentRenderer_Default_Script;
import com.w4t.util.browser.Default;
import com.w4t.webanchorkit.WebAnchorRenderer_Default_Noscript;
import com.w4t.webanchorkit.WebAnchorRenderer_Default_Script;
import com.w4t.webbordercomponentkit.WebBorderComponentRenderer_Default_Noscript;
import com.w4t.webbordercomponentkit.WebBorderComponentRenderer_Default_Script;
import com.w4t.webbuttonkit.WebButtonRenderer_Default_Noscript;
import com.w4t.webbuttonkit.WebButtonRenderer_Default_Script;
import com.w4t.webcardlayoutkit.WebCardLayoutRenderer_Default_Noscript;
import com.w4t.webcardlayoutkit.WebCardLayoutRenderer_Default_Script;
import com.w4t.webcheckboxkit.WebCheckBoxRenderer_Default_Noscript;
import com.w4t.webcheckboxkit.WebCheckBoxRenderer_Default_Script;
import com.w4t.webcontainerkit.WebContainerRenderer_Default_Noscript;
import com.w4t.webcontainerkit.WebContainerRenderer_Default_Script;
import com.w4t.webfileuploadkit.WebFileUploadRenderer_Default_Noscript;
import com.w4t.webfileuploadkit.WebFileUploadRenderer_Default_Script;
import com.w4t.webformkit.WebFormRenderer_Default_Noscript;
import com.w4t.webformkit.WebFormRenderer_Default_Script;
import com.w4t.webimagekit.WebImageRenderer_Default_Noscript;
import com.w4t.webimagekit.WebImageRenderer_Default_Script;
import com.w4t.weblabelkit.WebLabelRenderer_Default_Noscript;
import com.w4t.weblabelkit.WebLabelRenderer_Default_Script;
import com.w4t.webradiobuttongroupkit.WebRadioButtonGroupRenderer_Default_Noscript;
import com.w4t.webradiobuttongroupkit.WebRadioButtonGroupRenderer_Default_Script;
import com.w4t.webradiobuttonkit.WebRadioButtonRenderer_Default_Noscript;
import com.w4t.webradiobuttonkit.WebRadioButtonRenderer_Default_Script;
import com.w4t.webselectkit.WebSelectRenderer_Default_Noscript;
import com.w4t.webselectkit.WebSelectRenderer_Default_Script;
import com.w4t.webtextareakit.WebTextAreaRenderer_Default_Noscript;
import com.w4t.webtextareakit.WebTextAreaRenderer_Default_Script;
import com.w4t.webtextkit.WebTextRenderer_Default_Noscript;
import com.w4t.webtextkit.WebTextRenderer_Default_Script;

public class RendererCache_Default_Test extends TestCase {

  public void testRetrieveRenderer_Default_Noscript() {
    Renderer renderer;
    RendererCache cache = RendererCache.getInstance();
    //
    // Default up (no script)
    Fixture.fakeBrowser( new Default( false ) );
    // WebAnchor
    renderer = cache.retrieveRenderer( WebAnchor.class );
    assertEquals( WebAnchorRenderer_Default_Noscript.class, 
                  renderer.getClass() );
    // WebBorderComponent
    renderer = cache.retrieveRenderer( WebBorderComponent.class );
    assertEquals( WebBorderComponentRenderer_Default_Noscript.class,
                  renderer.getClass() );
    // WebScrollPane
    renderer = cache.retrieveRenderer( WebScrollPane.class );
    assertEquals( WebScrollPaneRenderer_Default_Noscript.class,
                  renderer.getClass() );
    // MenuItem
    renderer = cache.retrieveRenderer( MenuItem.class );
    assertEquals( MenuItemRenderer_Default_Noscript.class, 
                  renderer.getClass() );
    // MenuItemSeparator
    renderer = cache.retrieveRenderer( MenuItemSeparator.class );
    assertEquals( MenuItemSeparatorRenderer_Default_Noscript.class,
                  renderer.getClass() );
    // TreeLeaf
    renderer = cache.retrieveRenderer( TreeLeaf.class );
    assertEquals( TreeLeafRenderer_Default_Noscript.class, 
                  renderer.getClass() );
    // Menu
    renderer = cache.retrieveRenderer( Menu.class );
    assertEquals( MenuRenderer_Default_Noscript.class, 
                  renderer.getClass() );
    // MenuBar
    renderer = cache.retrieveRenderer( MenuBar.class );
    assertEquals( MenuBarRenderer_Default_Noscript.class, 
                  renderer.getClass() );
    // TreeNode
    renderer = cache.retrieveRenderer( TreeNode.class );
    assertEquals( TreeNodeRenderer_Default_Noscript.class, 
                  renderer.getClass() );
    // TreeView
    renderer = cache.retrieveRenderer( TreeView.class );
    assertEquals( TreeViewRenderer_Default_Noscript.class, 
                  renderer.getClass() );
    // MenuButton
    renderer = cache.retrieveRenderer( MenuButton.class );
    assertEquals( MenuButtonRenderer_Default_Noscript.class,
                  renderer.getClass() );
    // WebButton
    renderer = cache.retrieveRenderer( WebButton.class );
    assertEquals( WebButtonRenderer_Default_Noscript.class, 
                  renderer.getClass() );
    // AreaSelector
    renderer = cache.retrieveRenderer( WebButton.class );
    assertEquals( WebButtonRenderer_Default_Noscript.class, 
                  renderer.getClass() );
    // LinkButton
    renderer = cache.retrieveRenderer( LinkButton.class );
    assertEquals( WebButtonRenderer_Default_Noscript.class, 
                  renderer.getClass() );
    // testing for superclass
    // WebCheckBox
    renderer = cache.retrieveRenderer( WebCheckBox.class );
    assertEquals( WebCheckBoxRenderer_Default_Noscript.class,
                  renderer.getClass() );
    // WebContainer
    renderer = cache.retrieveRenderer( WebContainer.class );
    assertEquals( WebContainerRenderer_Default_Noscript.class,
                  renderer.getClass() );
    // CItemList
    renderer = cache.retrieveRenderer( CItemList.class );
    assertEquals( WebContainerRenderer_Default_Noscript.class,
                  renderer.getClass() );
    // CTabbedPane
    renderer = cache.retrieveRenderer( CTabbedPane.class );
    assertEquals( WebContainerRenderer_Default_Noscript.class,
                  renderer.getClass() );
    // test for superclass
    // CTable
    renderer = cache.retrieveRenderer( CTable.class );
    assertEquals( WebContainerRenderer_Default_Noscript.class,
                  renderer.getClass() );
    // WebForm
    renderer = cache.retrieveRenderer( WebForm.class );
    assertEquals( WebFormRenderer_Default_Noscript.class, 
                  renderer.getClass() );
    // WebPanel
    renderer = cache.retrieveRenderer( WebPanel.class );
    assertEquals( WebContainerRenderer_Default_Noscript.class,
                  renderer.getClass() );
    // CMenu
    renderer = cache.retrieveRenderer( CMenu.class );
    assertEquals( WebContainerRenderer_Default_Noscript.class,
                  renderer.getClass() );
    // CToolBar
    renderer = cache.retrieveRenderer( CToolBar.class );
    assertEquals( WebContainerRenderer_Default_Noscript.class,
                  renderer.getClass() );
    // TreeViewer
    renderer = cache.retrieveRenderer( TreeView.class );
    assertEquals( TreeViewRenderer_Default_Noscript.class, 
                  renderer.getClass() );
    // WebFileUpload
    renderer = cache.retrieveRenderer( WebFileUpload.class );
    assertEquals( WebFileUploadRenderer_Default_Noscript.class,
                  renderer.getClass() );
    // WebImage
    renderer = cache.retrieveRenderer( WebImage.class );
    assertEquals( WebImageRenderer_Default_Noscript.class, 
                  renderer.getClass() );
    // WebLabel
    renderer = cache.retrieveRenderer( WebLabel.class );
    assertEquals( WebLabelRenderer_Default_Noscript.class, 
                  renderer.getClass() );
    // WebRadioButton
    renderer = cache.retrieveRenderer( WebRadioButton.class );
    assertEquals( WebRadioButtonRenderer_Default_Noscript.class,
                  renderer.getClass() );
    // WebRadioButtonGroup
    renderer = cache.retrieveRenderer( WebRadioButtonGroup.class );
    assertEquals( WebRadioButtonGroupRenderer_Default_Noscript.class,
                  renderer.getClass() );
    // WebSelect
    renderer = cache.retrieveRenderer( WebSelect.class );
    assertEquals( WebSelectRenderer_Default_Noscript.class, 
                  renderer.getClass() );
    // WebText
    renderer = cache.retrieveRenderer( WebText.class );
    assertEquals( WebTextRenderer_Default_Noscript.class, 
                  renderer.getClass() );
    // WebTextWithoutTitleMock
    renderer = cache.retrieveRenderer( WebTextWithoutTitleMock.class );
    assertEquals( WebTextRenderer_Default_Noscript.class, 
                  renderer.getClass() );
    // WebTextArea
    renderer = cache.retrieveRenderer( WebTextArea.class );
    assertEquals( WebTextAreaRenderer_Default_Noscript.class,
                  renderer.getClass() );
    // WebCardLayout
    renderer = cache.retrieveRenderer( WebCardLayout.class );
    assertEquals( WebCardLayoutRenderer_Default_Noscript.class,
                  renderer.getClass() );
    // StaticContentForm
    renderer = cache.retrieveRenderer( ExitForm.class );
    assertEquals( ExitFormRenderer_Default_Noscript.class,
                  renderer.getClass() );
  }

  public void testRetrieveRenderer_Default_Script() {
    Renderer renderer;
    RendererCache cache = RendererCache.getInstance();
    //
    // Default (script)
    Fixture.fakeBrowser( new Default( true ) );
    // WebAnchor
    renderer = cache.retrieveRenderer( WebAnchor.class );
    assertEquals( WebAnchorRenderer_Default_Script.class,
                  renderer.getClass() );
    // WebBorderComponent
    renderer = cache.retrieveRenderer( WebBorderComponent.class );
    assertEquals( WebBorderComponentRenderer_Default_Script.class,
                  renderer.getClass() );
    // WebScrollPane
    renderer = cache.retrieveRenderer( WebScrollPane.class );
    assertEquals( WebScrollPaneRenderer_Default_Script.class,
                  renderer.getClass() );
    // MenuItem
    renderer = cache.retrieveRenderer( MenuItem.class );
    assertEquals( MenuItemRenderer_Default_Script.class, 
                  renderer.getClass() );
    // MenuItemSeparator
    renderer = cache.retrieveRenderer( MenuItemSeparator.class );
    assertEquals( MenuItemSeparatorRenderer_Default_Script.class,
                  renderer.getClass() );
    // TreeLeaf
    renderer = cache.retrieveRenderer( TreeLeaf.class );
    assertEquals( TreeLeafRenderer_Default_Script.class, 
                  renderer.getClass() );
    // Menu
    renderer = cache.retrieveRenderer( Menu.class );
    assertEquals( MenuRenderer_Default_Script.class, 
                  renderer.getClass() );
    // MenuBar
    renderer = cache.retrieveRenderer( MenuBar.class );
    assertEquals( MenuBarRenderer_Default_Script.class, 
                  renderer.getClass() );
    // TreeNode
    renderer = cache.retrieveRenderer( TreeNode.class );
    assertEquals( TreeNodeRenderer_Default_Script.class, 
                  renderer.getClass() );
    // TreeView
    renderer = cache.retrieveRenderer( TreeView.class );
    assertEquals( TreeViewRenderer_Default_Script.class, 
                  renderer.getClass() );
    // MenuButton
    renderer = cache.retrieveRenderer( MenuButton.class );
    assertEquals( MenuButtonRenderer_Default_Script.class,
                  renderer.getClass() );
    // //NonAjaxComponent
    renderer = cache.retrieveRenderer( NonAjaxComponent.class );
    assertEquals( NonAjaxComponentRenderer_Default_Script.class,
                  renderer.getClass() );
    // WebButton
    renderer = cache.retrieveRenderer( WebButton.class );
    assertEquals( WebButtonRenderer_Default_Script.class,
                  renderer.getClass() );
    // AreaSelector
    renderer = cache.retrieveRenderer( WebButton.class );
    assertEquals( WebButtonRenderer_Default_Script.class,
                  renderer.getClass() );
    // LinkButton
    renderer = cache.retrieveRenderer( LinkButton.class );
    assertEquals( WebButtonRenderer_Default_Script.class,
                  renderer.getClass() );
    // WebCheckBox
    renderer = cache.retrieveRenderer( WebCheckBox.class );
    assertEquals( WebCheckBoxRenderer_Default_Script.class,
                  renderer.getClass() );
    // WebContainer
    renderer = cache.retrieveRenderer( WebContainer.class );
    assertEquals( WebContainerRenderer_Default_Script.class,
                  renderer.getClass() );
    // CItemList
    renderer = cache.retrieveRenderer( CItemList.class );
    assertEquals( WebContainerRenderer_Default_Script.class,
                  renderer.getClass() );
    // CTabbedPane
    renderer = cache.retrieveRenderer( CTabbedPane.class );
    assertEquals( WebContainerRenderer_Default_Script.class,
                  renderer.getClass() );
    // CTable
    renderer = cache.retrieveRenderer( CTable.class );
    assertEquals( WebContainerRenderer_Default_Script.class,
                  renderer.getClass() );
    // WebForm
    renderer = cache.retrieveRenderer( WebForm.class );
    assertEquals( WebFormRenderer_Default_Script.class, renderer.getClass() );
    // WebPanel
    renderer = cache.retrieveRenderer( WebPanel.class );
    assertEquals( WebContainerRenderer_Default_Script.class,
                  renderer.getClass() );
    // CMenu
    renderer = cache.retrieveRenderer( CMenu.class );
    assertEquals( WebContainerRenderer_Default_Script.class,
                  renderer.getClass() );
    // CToolBar
    renderer = cache.retrieveRenderer( CToolBar.class );
    assertEquals( WebContainerRenderer_Default_Script.class,
                  renderer.getClass() );
    // TreeView
    renderer = cache.retrieveRenderer( TreeView.class );
    assertEquals( TreeViewRenderer_Default_Script.class, renderer.getClass() );
    // WebFileUpload
    renderer = cache.retrieveRenderer( WebFileUpload.class );
    assertEquals( WebFileUploadRenderer_Default_Script.class,
                  renderer.getClass() );
    // WebImage
    renderer = cache.retrieveRenderer( WebImage.class );
    assertEquals( WebImageRenderer_Default_Script.class, renderer.getClass() );
    // WebLabel
    renderer = cache.retrieveRenderer( WebLabel.class );
    assertEquals( WebLabelRenderer_Default_Script.class, renderer.getClass() );
    // PoolLabel
    renderer = cache.retrieveRenderer( PoolLabel.class );
    assertEquals( WebLabelRenderer_Default_Script.class, renderer.getClass() );
    // WebRadioButton
    renderer = cache.retrieveRenderer( WebRadioButton.class );
    assertEquals( WebRadioButtonRenderer_Default_Script.class,
                  renderer.getClass() );
    // WebRadioButtonGroup
    renderer = cache.retrieveRenderer( WebRadioButtonGroup.class );
    assertEquals( WebRadioButtonGroupRenderer_Default_Script.class,
                  renderer.getClass() );
    // WebSelect
    renderer = cache.retrieveRenderer( WebSelect.class );
    assertEquals( WebSelectRenderer_Default_Script.class,
                  renderer.getClass() );
    // WebText
    renderer = cache.retrieveRenderer( WebText.class );
    assertEquals( WebTextRenderer_Default_Script.class, renderer.getClass() );
    // WebTextArea
    renderer = cache.retrieveRenderer( WebTextArea.class );
    assertEquals( WebTextAreaRenderer_Default_Script.class,
                  renderer.getClass() );
    // WebCardLayout
    renderer = cache.retrieveRenderer( WebCardLayout.class );
    assertEquals( WebCardLayoutRenderer_Default_Script.class,
                  renderer.getClass() );
    // StaticContentForm
    renderer = cache.retrieveRenderer( ExitForm.class );
    assertEquals( ExitFormRenderer_Default_Script.class,
                  renderer.getClass() );
  }

  public void testRetrieveRenderer_Default_Ajax() {
    Renderer renderer;
    //
    // Default up (ajax)
    Fixture.fakeBrowser( new Default( true, true ) );
    // WebAnchor
    RendererCache cache = RendererCache.getInstance();
    renderer = cache.retrieveRenderer( WebAnchor.class );
    assertEquals( WebAnchorRenderer_Default_Script.class, 
                  renderer.getClass() );
    // WebBorderComponent
    renderer = cache.retrieveRenderer( WebBorderComponent.class );
    assertEquals( WebBorderComponentRenderer_Default_Script.class,
                  renderer.getClass() );
    // WebScrollPane
    renderer = cache.retrieveRenderer( WebScrollPane.class );
    assertEquals( WebScrollPaneRenderer_Default_Script.class,
                  renderer.getClass() );
    // MenuItem
    renderer = cache.retrieveRenderer( MenuItem.class );
    assertEquals( MenuItemRenderer_Default_Script.class, 
                  renderer.getClass() );
    // MenuItemSeparator
    renderer = cache.retrieveRenderer( MenuItemSeparator.class );
    assertEquals( MenuItemSeparatorRenderer_Default_Script.class,
                  renderer.getClass() );
    // Menu
    renderer = cache.retrieveRenderer( Menu.class );
    assertEquals( MenuRenderer_Default_Script.class,
                  renderer.getClass() );
    // MenuBar
    renderer = cache.retrieveRenderer( MenuBar.class );
    assertEquals( MenuBarRenderer_Default_Script.class, 
                  renderer.getClass() );
    // TreeNode
    renderer = cache.retrieveRenderer( TreeNode.class );
    assertEquals( TreeNodeRenderer_Default_Script.class, 
                  renderer.getClass() );
    // TreeView
    renderer = cache.retrieveRenderer( TreeView.class );
    assertEquals( TreeViewRenderer_Default_Script.class, 
                  renderer.getClass() );
    // MenuButton
    renderer = cache.retrieveRenderer( MenuButton.class );
    assertEquals( MenuButtonRenderer_Default_Script.class,
                  renderer.getClass() );
    // NonAjaxComponent
    renderer = cache.retrieveRenderer( NonAjaxComponent.class );
    assertEquals( NonAjaxComponentRenderer_Default_Script.class,
                  renderer.getClass() );
    // WebButton
    renderer = cache.retrieveRenderer( WebButton.class );
    assertEquals( WebButtonRenderer_Default_Script.class, 
                  renderer.getClass() );
    // AreaSelector
    renderer = cache.retrieveRenderer( WebButton.class );
    assertEquals( WebButtonRenderer_Default_Script.class,
                  renderer.getClass() );
    // LinkButton
    renderer = cache.retrieveRenderer( LinkButton.class );
    assertEquals( WebButtonRenderer_Default_Script.class, 
                  renderer.getClass() );
    // testing for superclass
    // WebCheckBox
    renderer = cache.retrieveRenderer( WebCheckBox.class );
    assertEquals( WebCheckBoxRenderer_Default_Script.class, 
                  renderer.getClass() );
    // WebContainer
    renderer = cache.retrieveRenderer( WebContainer.class );
    assertEquals( WebContainerRenderer_Default_Script.class, 
                  renderer.getClass() );
    // CItemList
    renderer = cache.retrieveRenderer( CItemList.class );
    assertEquals( WebContainerRenderer_Default_Script.class, 
                  renderer.getClass() );
    // test for superclass
    // CTabbedPane
    renderer = cache.retrieveRenderer( CTabbedPane.class );
    assertEquals( WebContainerRenderer_Default_Script.class, 
                  renderer.getClass() );
    // test for superclass
    // CTable
    renderer = cache.retrieveRenderer( CTable.class );
    assertEquals( WebContainerRenderer_Default_Script.class, 
                  renderer.getClass() );
    // test for superclass
    // WebForm
    renderer = cache.retrieveRenderer( WebForm.class );
    assertEquals( WebFormRenderer_Default_Script.class, 
                  renderer.getClass() );
    // Form
    renderer = cache.retrieveRenderer( Form.class );
    assertEquals( WebFormRenderer_Default_Script.class, 
                  renderer.getClass() );
    // test for superclass
    // LoginForm
    renderer = cache.retrieveRenderer( LoginForm.class );
    assertEquals( WebFormRenderer_Default_Script.class, 
                  renderer.getClass() );
    // test for superclass
    // TestForm
    renderer = cache.retrieveRenderer( TestForm.class );
    assertEquals( WebFormRenderer_Default_Script.class, 
                  renderer.getClass() );
    // WebPanel
    renderer = cache.retrieveRenderer( WebPanel.class );
    assertEquals( WebContainerRenderer_Default_Script.class, 
                  renderer.getClass() );
    // CMenu
    renderer = cache.retrieveRenderer( CMenu.class );
    assertEquals( WebContainerRenderer_Default_Script.class, 
                  renderer.getClass() );
    // testing for superclass
    // CToolBar
    renderer = cache.retrieveRenderer( CToolBar.class );
    assertEquals( WebContainerRenderer_Default_Script.class, 
                  renderer.getClass() );
    // TreeViewer
    renderer = cache.retrieveRenderer( TreeView.class );
    assertEquals( TreeViewRenderer_Default_Script.class, 
                  renderer.getClass() );
    // WebFileUpload
    renderer = cache.retrieveRenderer( WebFileUpload.class );
    assertEquals( WebFileUploadRenderer_Default_Script.class,
                  renderer.getClass() );
    // WebImage
    renderer = cache.retrieveRenderer( WebImage.class );
    assertEquals( WebImageRenderer_Default_Script.class, 
                  renderer.getClass() );
    // WebLabel
    renderer = cache.retrieveRenderer( WebLabel.class );
    assertEquals( WebLabelRenderer_Default_Script.class, 
                  renderer.getClass() );
    // PoolLabel
    renderer = cache.retrieveRenderer( PoolLabel.class );
    assertEquals( WebLabelRenderer_Default_Script.class, 
                  renderer.getClass() );
    // testing for superclass
    // SuperWebLabel
    renderer = cache.retrieveRenderer( SuperWebLabel.class );
    assertEquals( WebLabelRenderer_Default_Script.class, 
                  renderer.getClass() );
    // WebRadioButton
    renderer = cache.retrieveRenderer( WebRadioButton.class );
    assertEquals( WebRadioButtonRenderer_Default_Script.class,
                  renderer.getClass() );
    // WebRadioButtonGroup
    renderer = cache.retrieveRenderer( WebRadioButtonGroup.class );
    assertEquals( WebRadioButtonGroupRenderer_Default_Script.class,
                  renderer.getClass() );
    // WebSelect
    renderer = cache.retrieveRenderer( WebSelect.class );
    assertEquals( WebSelectRenderer_Default_Script.class, 
                  renderer.getClass() );
    //WebText
    renderer = cache.retrieveRenderer( WebText.class );
    assertEquals( WebTextRenderer_Default_Script.class, 
                  renderer.getClass() );
    //WebTextArea
    renderer = cache.retrieveRenderer( WebTextArea.class );
    assertEquals( WebTextAreaRenderer_Default_Script.class, 
                  renderer.getClass() );
    // WebCardLayout
    renderer = cache.retrieveRenderer( WebCardLayout.class );
    assertEquals( WebCardLayoutRenderer_Default_Script.class,
                  renderer.getClass() );
  }

  protected void setUp() throws Exception {
    Fixture.setUp();
    Fixture.createContext();
  }

  protected void tearDown() throws Exception {
    Fixture.tearDown();
    Fixture.removeContext();
  }
}
