// Created on 09.09.2007
package org.eclipse.rap.rms.ui.internal.startup;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;


public class IntroPerspective implements IPerspectiveFactory {

  public void createInitialLayout( final IPageLayout layout ) {
    String editorArea = layout.getEditorArea();
    layout.setEditorAreaVisible( false );
    layout.addStandaloneView( "org.eclipse.ui.views.Intro", //$NON-NLS-1$
                              false,
                              IPageLayout.LEFT,
                              1f, 
                              editorArea );
  }
}
