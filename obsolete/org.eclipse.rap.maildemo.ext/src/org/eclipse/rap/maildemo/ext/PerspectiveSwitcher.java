package org.eclipse.rap.maildemo.ext;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.*;

public class PerspectiveSwitcher extends Composite {

  PerspectiveSwitcher( Composite banner ) {
    super( banner, SWT.NONE );
    final IPerspectiveDescriptor[] perspectives = getPerspectives( PlatformUI.getWorkbench() );
    IAction[] actions = new IAction[ perspectives.length ];
    for( int i = 0; i < perspectives.length; i++ ) {
      final int p = i;
      actions[ i ] = new Action( perspectives[ i ].getLabel() ) {
        public void run() {
          switchPerspective( p );
        }
      };
    }
    ActionBar actionBar = new ActionBar( banner, SWT.NONE, actions );
    actionBar.pack();
    FormData fdActionBar = new FormData();
    actionBar.setLayoutData( fdActionBar );
    fdActionBar.top = new FormAttachment( 0, 45 );
    fdActionBar.left = new FormAttachment( 0, 10 );
  }

  private void switchPerspective( final int perspectiveIndex ) {
    IWorkbench workbench = PlatformUI.getWorkbench();
    final IPerspectiveDescriptor[] perspectives = getPerspectives( workbench );
    IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
    final IWorkbenchPage page = window.getActivePage();
    page.setPerspective( perspectives[ perspectiveIndex ] );
  }

  private IPerspectiveDescriptor[] getPerspectives( IWorkbench workbench ) {
    IPerspectiveRegistry registry = workbench.getPerspectiveRegistry();
    final IPerspectiveDescriptor[] perspectives = registry.getPerspectives();
    return perspectives;
  }
}
