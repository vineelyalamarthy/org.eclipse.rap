package org.eclipse.rap.rwt.custom.demo.spreadsheet;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;


public class SpreadSheetWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {

  public SpreadSheetWorkbenchWindowAdvisor(
    final IWorkbenchWindowConfigurer configurer )
  {
    super( configurer );
  }
  
  
  public void preWindowOpen() {
    IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
    Rectangle bounds = Display.getCurrent().getBounds();
    configurer.setInitialSize( new Point( bounds.width, bounds.height ) );
    configurer.setShowCoolBar( false );
    configurer.setShowPerspectiveBar( false );
    configurer.setShowProgressIndicator( false );
    configurer.setShowStatusLine( false );
    configurer.setShowMenuBar( false );
    configurer.setTitle( "Spreadsheet Demo" );
    configurer.setShellStyle( SWT.TITLE | SWT.RESIZE );
  }
}
