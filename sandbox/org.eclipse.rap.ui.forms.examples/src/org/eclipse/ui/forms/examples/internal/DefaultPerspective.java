/*******************************************************************************
 * Copyright (c) 2008 Innoopract Informationssysteme GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Innoopract Informationssysteme GmbH - initial API and implementation
 ******************************************************************************/
package org.eclipse.ui.forms.examples.internal;

import org.eclipse.ui.*;

public class DefaultPerspective implements IPerspectiveFactory {

  public void createInitialLayout( final IPageLayout layout ) {
    String editorArea = layout.getEditorArea();
    layout.setEditorAreaVisible( true );
    IFolderLayout topLeft = layout.createFolder( "topLeft",
                                                 IPageLayout.LEFT,
                                                 0.25f,
                                                 editorArea );
    topLeft.addView( "org.eclipse.ui.forms.examples.views.FormView" );
  }
}
