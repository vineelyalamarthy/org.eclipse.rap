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

import com.w4t.*;
import com.w4t.dhtml.TreeNode;
import com.w4t.dhtml.TreeView;
import com.w4t.event.*;

public class TreeForm extends WebForm {

  protected TreeView treeView;
  protected WebButton webButton1;
  protected WebButton webButton2;
  protected WebButton webButton3;

  protected void setWebComponents() throws Exception {
    instanceWebComponents();
    initialiseForm();
    initialiseTreeView();
    initialiseWebButton1();
    initialiseWebButton2();
    initialiseWebButton3();
    initialiseEventListeners();
  }

  protected void initialiseForm() throws Exception {
    this.setName( "TreeForm" );
    WebBorderLayout TreeFormWebBorderLayout = new WebBorderLayout();
    this.setWebLayout( TreeFormWebBorderLayout );
  }

  protected void instanceWebComponents() throws Exception {
    treeView = new TreeView();
    webButton1 = new WebButton();
    webButton2 = new WebButton();
    webButton3 = new WebButton();
  }

  private void initialiseTreeView() throws Exception {
    this.add( treeView, WebBorderLayout.WEST );
    treeView.setName( "treeView" );
    TreeNode node1 = new TreeNode();
    node1.setLabel( "node1" );
    treeView.addItem( node1 );
  }

  private void initialiseWebButton1() throws Exception {
    this.add( webButton1, WebBorderLayout.EAST );
    webButton1.setLabel( "new tree node" );
    webButton1.setName( "webButton1" );
  }

  private void initialiseWebButton2() throws Exception {
    this.add( webButton2, WebBorderLayout.EAST );
    webButton2.setLabel( "remove and add new tree node" );
    webButton2.setName( "webButton2" );
  }
  
  private void initialiseWebButton3() throws Exception {
    this.add( webButton3, WebBorderLayout.EAST );
    webButton3.setLabel( "add sub-node" );
    webButton3.setName( "webButton3" );
  }
  
  //$userdefined_start
  private AjaxStatusAdapterFactory ajaxStatusAdapterFactory 
    = new AjaxStatusAdapterFactory();
  //$userdefined_start
  
  //$userdefinedEventListener_start
  private void doTreeFormAfterRender( WebRenderEvent e ) {
    AjaxStatus status = ( AjaxStatus )this.getAdapter( AjaxStatus.class );
    status.updateStatus( false );
  }
  private void doTreeFormBeforeRender( WebRenderEvent e ) {
    W4TContext.getAdapterManager().registerAdapters( ajaxStatusAdapterFactory, 
                                                     WebComponent.class );
    AjaxStatusUtil.preRender( this );
  }
  private void doWebButton1WebActionPerformed( WebActionEvent e ) {
    // add new
    TreeNode treeNode = new TreeNode();
    treeNode.setLabel( "new node " + System.currentTimeMillis() );
    treeView.addItem( treeNode );
  }

  private void doWebButton2WebActionPerformed( WebActionEvent e ) {
    // remove first node and add new
    treeView.removeItem( treeView.getItem( 0 ) );
    TreeNode treeNode = new TreeNode();
    treeNode.setLabel( "node replacement " + System.currentTimeMillis() );
    treeView.addItem( treeNode );
  }

  private void doWebButton3WebActionPerformed( WebActionEvent e ) {
    // remove first sub-node (if any) add sub-node to first node
    TreeNode parentNode = ( TreeNode )treeView.getItem( 0 );
    if( parentNode.getChildCount() > 0 ) {
      parentNode.removeItem( parentNode.getItem( 0 ) );
    }
    TreeNode treeNode = new TreeNode();
    treeNode.setLabel( "sub-node" + System.currentTimeMillis() );
    parentNode.addItem( treeNode );
  }
  //$userdefinedEventListener_end

  private void initialiseEventListeners() throws Exception {
    this.addWebRenderListener( new WebRenderListener() {
      public void afterRender( WebRenderEvent e ) {
        doTreeFormAfterRender( e );
      }
      public void beforeRender( WebRenderEvent e ) {
        doTreeFormBeforeRender( e );
      }
    } );

    webButton1.addWebActionListener( new WebActionListener() {
      public void webActionPerformed( WebActionEvent e ) {
        doWebButton1WebActionPerformed( e );
      }
    } );

    webButton2.addWebActionListener( new WebActionListener() {
      public void webActionPerformed( WebActionEvent e ) {
        doWebButton2WebActionPerformed( e );
      }
    } );
    webButton3.addWebActionListener( new WebActionListener() {
      public void webActionPerformed( WebActionEvent e ) {
        doWebButton3WebActionPerformed( e );
      }
    } );
  }
}