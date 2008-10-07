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

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.*;


public class PerspectiveSwitcher extends Composite {

  PerspectiveSwitcher( final Composite banner ) {
    super( banner, SWT.NONE );
    setLayout( new FillLayout() );
    final IPerspectiveDescriptor[] perspectives
      = getPerspectives( PlatformUI.getWorkbench() );
    IAction[] actions = new IAction[ perspectives.length ];
    for( int i = 0; i < perspectives.length; i++ ) {
      final int p = i;
      actions[ i ] = new Action( perspectives[ i ].getLabel() ) {
        public void run() {
          switchPerspective( p );
        }
      };
    }
    new ActionBar( this, SWT.NONE, actions );
  }

  private void switchPerspective( final int perspectiveIndex ) {
    IWorkbench workbench = PlatformUI.getWorkbench();
    final IPerspectiveDescriptor[] perspectives = getPerspectives( workbench );
    IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
    final IWorkbenchPage page = window.getActivePage();
    page.setPerspective( perspectives[ perspectiveIndex ] );
  }

  private IPerspectiveDescriptor[] getPerspectives( final IWorkbench workbench )
  {
    IPerspectiveRegistry registry = workbench.getPerspectiveRegistry();
    final IPerspectiveDescriptor[] perspectives = registry.getPerspectives();
    return perspectives;
  }
}
