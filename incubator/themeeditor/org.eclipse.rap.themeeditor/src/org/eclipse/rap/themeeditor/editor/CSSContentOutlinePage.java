/*******************************************************************************
 * Copyright (c) 2008 Mathias Schaeffner and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Mathias Schaeffner - initial API and implementation
 *******************************************************************************/
package org.eclipse.rap.themeeditor.editor;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.rap.themeeditor.editor.source.CSSSourceEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.views.contentoutline.ContentOutlinePage;

/**
 * Implementation of an Outline page showing StyleRules as items if the
 * Rules-Tab is activated, or showing OutlineRegions as items if the Source-Tab
 * is activated.
 */
public class CSSContentOutlinePage extends ContentOutlinePage {

  protected Object[] input;
  protected IDocumentProvider documentProvider;
  protected CSSSourceEditor textEditor;
  private boolean isSetSelectionEnabled = true;

  public CSSContentOutlinePage( CSSSourceEditor cssSourceEditor ) {
    textEditor = cssSourceEditor;
  }

  public void createControl( final Composite parent ) {
    super.createControl( parent );
    TreeViewer viewer = getTreeViewer();
    viewer.setContentProvider( new DefaultContentProvider() );
    viewer.setLabelProvider( new ContentOutlineLabelProvider() );
    if( input != null ) {
      viewer.setInput( input );
    }
  }

  public void selectionChanged( final SelectionChangedEvent event ) {
    isSetSelectionEnabled = false;
    super.selectionChanged( event );
    int newIndex = -1;
    Object item = null;
    ISelection selection = event.getSelection();
    if( !selection.isEmpty() ) {
      item = ( ( IStructuredSelection )selection ).getFirstElement();
    }
    if( getTreeViewer().getTree().getSelectionCount() == 1 ) {
      TreeItem treeItem = getTreeViewer().getTree().getSelection()[ 0 ];
      newIndex = getTreeViewer().getTree().indexOf( treeItem );
    }
    textEditor.outlineSelectionChanged( newIndex, item );
    isSetSelectionEnabled = true;
  }

  public void forceSelectionChanged( final int newIndex ) {
    if( getTreeViewer() != null ) {
      isSetSelectionEnabled = false;
      Object item = null;
      ISelection selection = getTreeViewer().getSelection();
      if( !selection.isEmpty() ) {
        item = ( ( IStructuredSelection )selection ).getFirstElement();
      }
      textEditor.outlineSelectionChanged( newIndex, item );
      isSetSelectionEnabled = true;
    }
  }

  public void setInput( final Object[] input ) {
    this.input = input;
    update();
  }

  public void setSelection( final int index ) {
    if( isSetSelectionEnabled && getTreeViewer() != null ) {
      Tree tree = getTreeViewer().getTree();
      if( index >= 0 && index < tree.getItemCount() ) {
        tree.setSelection( tree.getItem( index ) );
        tree.showSelection();
      } else {
        tree.deselectAll();
      }
    }
  }

  private void update() {
    TreeViewer viewer = getTreeViewer();
    if( viewer != null ) {
      Control control = viewer.getControl();
      if( control != null && !control.isDisposed() ) {
        control.setRedraw( false );
        viewer.setInput( input );
        viewer.expandAll();
        control.setRedraw( true );
      }
    }
  }
}
