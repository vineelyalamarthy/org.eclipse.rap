/*******************************************************************************
 * Copyright (c) 2010 EclipseSource and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html Contributors:
 * EclipseSource - initial API and implementation
 *******************************************************************************/
package org.eclipse.rap.warproducts.ui.editor;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.pde.internal.ui.editor.ISortableContentOutlinePage;
import org.eclipse.pde.internal.ui.editor.PDEFormEditor;
import org.eclipse.pde.internal.ui.editor.XMLSourcePage;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;

public class WebXMLSourcePage extends XMLSourcePage {

  public WebXMLSourcePage( final PDEFormEditor editor, 
                           final String id, 
                           final String title ) {
    super( editor, id, title );
  }

  public boolean isQuickOutlineEnabled() {
    return false;
  }

  public ILabelProvider createOutlineLabelProvider() {
    return null;
  }

  public ITreeContentProvider createOutlineContentProvider() {
    return null;
  }

  public ViewerComparator createOutlineComparator() {
    return null;
  }

  public void updateSelection( final Object object ) {
  }

  public IContentOutlinePage createContentOutlinePage() {
    return null;
  }

  protected ISortableContentOutlinePage createOutlinePage() {
    return new WARProductOutlinePage( ( PDEFormEditor )getEditor() );
  }
  
}
