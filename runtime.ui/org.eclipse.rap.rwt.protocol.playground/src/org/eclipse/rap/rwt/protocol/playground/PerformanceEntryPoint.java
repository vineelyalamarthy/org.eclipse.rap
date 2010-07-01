package org.eclipse.rap.rwt.protocol.playground;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.rwt.lifecycle.IEntryPoint;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.plugin.AbstractUIPlugin;


public class PerformanceEntryPoint implements IEntryPoint {

  private Shell shell;

  public PerformanceEntryPoint() {
  }

  public int createUI() {
    Display display = new Display();
    shell = new Shell( display );
    shell.setLayout( new GridLayout( 20, true ) );
    final String shellText = "A Shell with 1000 buttons";
    shell.setText( shellText );
    String pluginId = "org.eclipse.rap.rwt.protocol.playground";
    String imagePath = "img/bell_error.png";
    ImageDescriptor imageDescriptor 
      = AbstractUIPlugin.imageDescriptorFromPlugin( pluginId, imagePath );
    Image image = imageDescriptor.createImage();
    SelectionListener selectionListener = new SelectionAdapter() {
      public void widgetSelected( final SelectionEvent e ) {
        changeAllButtonTexts();
      }
    };
    for( int i = 0; i < 1000; i++ ) {
      Button button = new Button( shell, SWT.PUSH );
      button.addSelectionListener( selectionListener );
      button.setText( "Button" + i );
      button.setImage( image );
    }
    shell.open();
    while (!shell.isDisposed ()) {
        if (!display.readAndDispatch ()) display.sleep ();
    }
    display.dispose ();
    return 0;
  }

  protected void changeAllButtonTexts() {
    Control[] children = shell.getChildren();
    for( int i = 0; i < children.length; i++ ) {
      ( ( Button ) children[ i ] ).setText( "new Text" + i );
    }
  }
}
