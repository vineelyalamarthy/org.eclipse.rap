package org.eclipse.rap.rwt.custom.demo.spreadsheet;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SpreadSheet;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;


public class SpreadSheetView extends ViewPart {

  private SpreadSheet spreadSheet;

  public void createPartControl( final Composite parent ) {
    spreadSheet = new SpreadSheet( parent, SWT.BORDER );
//    for( int i = 0; i < 100; i++ ) {
//      for( int j = 0; j < 100; j++ ) {
//        spreadSheet.setText( i + "_" + j, i, j );
//      }
//    }
  }

  public void setFocus() {
    spreadSheet.setFocus();
  }
}
