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

package org.eclipse.rap.maildemo.ext;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;


public class Perspective2 implements IPerspectiveFactory {

  private static final String ORG_ECLIPSE_RAP_MAILDEMO_VIEW = "org.eclipse.rap.maildemo.view";
  private static final String ORG_ECLIPSE_RAP_MAILDEMO_NAV_VIEW = "org.eclipse.rap.maildemo.navigationView";

  public void createInitialLayout( final IPageLayout layout ) {
    String editorArea = layout.getEditorArea();
    layout.setEditorAreaVisible( false );
    layout.addStandaloneView( ORG_ECLIPSE_RAP_MAILDEMO_NAV_VIEW,
                              false,
                              IPageLayout.RIGHT,
                              0.75f,
                              editorArea );
    layout.getViewLayout( ORG_ECLIPSE_RAP_MAILDEMO_NAV_VIEW )
      .setCloseable( false );
    IFolderLayout folder = layout.createFolder( "messages",
                                                IPageLayout.TOP,
                                                0.5f,
                                                editorArea );
    folder.addPlaceholder( ORG_ECLIPSE_RAP_MAILDEMO_VIEW + ":*" );
    folder.addView( ORG_ECLIPSE_RAP_MAILDEMO_VIEW );
  }
}
