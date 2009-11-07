package org.eclipse.rap.rwt.custom.demo.spreadsheet;

import org.eclipse.ui.*;


public class SpreadSheetPerspective implements IPerspectiveFactory {

  public void createInitialLayout( final IPageLayout layout ) {
    String editorArea = layout.getEditorArea();
    layout.setEditorAreaVisible( false );
    String viewId = "org.eclipse.rap.rwt.custom.demo.views.spreadsheet";
    layout.addStandaloneView( viewId, 
                              false,
                              IPageLayout.LEFT,
                              0.75f,
                              editorArea );
  }
}
