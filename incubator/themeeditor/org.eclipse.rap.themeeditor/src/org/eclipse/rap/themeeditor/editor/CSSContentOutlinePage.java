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

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.rap.themeeditor.editor.rule.DefaultContentProvider;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.ui.views.contentoutline.ContentOutlinePage;

/**
 * Implementation of an Outline page showing StyleRules as items if the
 * Rules-Tab is activated, or showing OutlineRegions as items if the Source-Tab
 * is activated.
 */
public class CSSContentOutlinePage extends ContentOutlinePage {

  protected Object[] input;
  protected IDocumentProvider documentProvider;
  protected ITextEditor textEditor;
  private boolean isSetSelectionEnabled = true;
  private int tabIndex = -1;
  private Map listenerMap;

  public CSSContentOutlinePage() {
    super();
    listenerMap = new HashMap();
  }

  public void createControl( final Composite parent ) {
    super.createControl( parent );
    TreeViewer viewer = getTreeViewer();
    viewer.setContentProvider( new DefaultContentProvider() );
    viewer.setLabelProvider( new ContentOutlineLabelProvider() );
    viewer.addSelectionChangedListener( this );
    if( input != null )
      viewer.setInput( input );
  }

  /**
   * Handles the SelectionChangedEvent if the Outline selection has been changed
   * by the user, and so it calls a possibly registered listener.
   */
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
    IOutlineSelectionChangedListener listener = ( IOutlineSelectionChangedListener )listenerMap.get( new Integer( tabIndex ) );
    if( listener != null ) {
      listener.outlineSelectionChanged( newIndex, item );
    }
    isSetSelectionEnabled = true;
  }

  /**
   * Forces a SelectionChangedEvent, and so it calls a possibly registered
   * listener.
   */
  public void forceSelectionChanged( final int newIndex ) {
    if( getTreeViewer() != null ) {
      isSetSelectionEnabled = false;
      Object item = null;
      ISelection selection = getTreeViewer().getSelection();
      if( !selection.isEmpty() ) {
        item = ( ( IStructuredSelection )selection ).getFirstElement();
      }
      IOutlineSelectionChangedListener listener = ( IOutlineSelectionChangedListener )listenerMap.get( new Integer( tabIndex ) );
      if( listener != null ) {
        listener.outlineSelectionChanged( newIndex, item );
      }
      isSetSelectionEnabled = true;
    }
  }

  /**
   * Sets a new input array for the Outline.
   */
  public void setInput( final Object[] input, final int tabIndex ) {
    this.input = input;
    this.tabIndex = tabIndex;
    update();
  }

  /**
   * Updates the Outline. Called after the Outline input has changed.
   */
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

  /**
   * Sets the selected item in the Outline programmatically to the given index.
   */
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

  /**
   * Sets a listener that will be notified of the selected item in the Outline
   * changes.
   */
  public void setSelectionChangedListener( final IOutlineSelectionChangedListener listener,
                                           final int tabIndex )
  {
    listenerMap.put( new Integer( tabIndex ), listener );
  }
}
