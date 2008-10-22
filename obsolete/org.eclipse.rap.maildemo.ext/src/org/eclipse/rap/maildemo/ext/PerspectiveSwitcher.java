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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.*;


public class PerspectiveSwitcher extends Composite {
  
  private final IPerspectiveDescriptor[] demoPerspectives
    = new IPerspectiveDescriptor[ 2 ];

  PerspectiveSwitcher( final Composite banner ) {
    super( banner, SWT.NONE );
    setLayout( new FillLayout() );
    IPerspectiveDescriptor[] perspectives
      = getPerspectives( PlatformUI.getWorkbench() );
    List toShow = new ArrayList();
    for( int i = 0; i < perspectives.length; i++ ) {
      String label = perspectives[ i ].getLabel();
      if(    label.equals( "RAP Perspective" )
          || label.equals( "Inverted Perspective" ) )
      {
        toShow.add( perspectives[ i ] );
      }
    }
    toShow.toArray( demoPerspectives );
    IAction[] actions = new IAction[ demoPerspectives.length ];
    for( int i = 0; i < demoPerspectives.length; i++ ) {
      final int p = i;
      actions[ i ] = new Action( demoPerspectives[ i ].getLabel() ) {
        public void run() {
          switchPerspective( p );
        }
      };
    }
    new ActionBar( this, SWT.NONE, actions );
  }

  private void switchPerspective( final int perspectiveIndex ) {
    IWorkbench workbench = PlatformUI.getWorkbench();
    IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
    final IWorkbenchPage page = window.getActivePage();
    page.setPerspective( demoPerspectives[ perspectiveIndex ] );
  }

  private IPerspectiveDescriptor[] getPerspectives( final IWorkbench workbench )
  {
    IPerspectiveRegistry registry = workbench.getPerspectiveRegistry();
    final IPerspectiveDescriptor[] perspectives = registry.getPerspectives();
    return perspectives;
  }
}
