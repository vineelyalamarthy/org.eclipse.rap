/*******************************************************************************
 * Copyright (c) 2002-2006 Innoopract Informationssysteme GmbH. All rights
 * reserved. This program and the accompanying materials are made available
 * under the terms of the Eclipse Public License v1.0 which accompanies this
 * distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 * Contributors: Innoopract Informationssysteme GmbH - initial API and
 * implementation
 ******************************************************************************/
package org.eclipse.rap.ui.internal;

import org.eclipse.rap.ui.IPageLayout;



public class EditorAreaHelper {

  private EditorSashContainer editorArea;
  
  public EditorAreaHelper( final WorkbenchPage page ) {
    this.editorArea = new EditorSashContainer( IPageLayout.ID_EDITOR_AREA,
                                               page,
                                               page.getClientComposite() );
    this.editorArea.createControl( page.getClientComposite() );
    this.editorArea.setActive( true );
  }

  public LayoutPart getLayoutPart() {
    return editorArea;
  }
}
