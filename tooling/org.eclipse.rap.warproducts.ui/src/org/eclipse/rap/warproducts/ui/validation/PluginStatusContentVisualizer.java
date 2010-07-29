/******************************************************************************* 
* Copyright (c) 2010 EclipseSource and others. All rights reserved. This
* program and the accompanying materials are made available under the terms of
* the Eclipse Public License v1.0 which accompanies this distribution, and is
* available at http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*   EclipseSource - initial API and implementation
*******************************************************************************/ 
package org.eclipse.rap.warproducts.ui.validation;

import java.util.Map;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.pde.internal.ui.PDEUIMessages;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;


public class PluginStatusContentVisualizer {
  
  public TreeViewer treeViewer;
  private Map input;
  
  public void createControls(final Composite parent ) {
    GridData gd = new GridData( GridData.FILL_BOTH );
    gd.widthHint = 400;
    gd.heightHint = 300;
    parent.setLayoutData( gd );
    Label label = new Label( parent, SWT.NONE );
    label.setText( PDEUIMessages.PluginStatusDialog_label );
    treeViewer = new TreeViewer( parent );
    PluginStatusDialogContentProvider contentProcider 
      = new PluginStatusDialogContentProvider( input );
    treeViewer.setContentProvider( contentProcider );
    treeViewer.setLabelProvider( new PluginStatusDialogLableProvider() );
    treeViewer.setComparator( new ViewerComparator() );
    treeViewer.setInput( input );
    treeViewer.getControl().setLayoutData( new GridData( GridData.FILL_BOTH ) );
  }
  
  public TreeViewer getViewer() {
    return treeViewer;
  }
  
  public void setInput( final Map input ) {
    this.input = input;
  }
  
  
}
