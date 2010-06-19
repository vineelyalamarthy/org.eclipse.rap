package org.eclipse.rap.rwt.protocol.playground;

import java.util.Random;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.rwt.lifecycle.IEntryPoint;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.plugin.AbstractUIPlugin;


public class PerformanceEntryPoint implements IEntryPoint {

  public PerformanceEntryPoint() {
  }

  public int createUI() {
    Display display = new Display();
    final Shell shell = new Shell( display );
    shell.setLayout( new GridLayout( 100, true ) );
    final String shellText = "A Shell with 1000 buttons";
    shell.setText( shellText );
    String pluginId = "org.eclipse.rap.rwt.protocol.playground";
    String imagePath = "img/bell_error.png";
    ImageDescriptor imageDescriptor 
      = AbstractUIPlugin.imageDescriptorFromPlugin( pluginId, imagePath );
    Image image = imageDescriptor.createImage();
    for( int i = 0; i < 1000; i++ ) {
      Button button = new Button( shell, SWT.PUSH );
      Random random = new Random();
      String text = "";
      for( int j = 0; j < 10; j++ ) {
        int nextInt = random.nextInt( Character.MAX_RADIX );
        text += ( char ) nextInt;
      }
      button.setText( text + i );
      button.setImage( image );
    }
      
    
    
    shell.open();
    while (!shell.isDisposed ()) {
        if (!display.readAndDispatch ()) display.sleep ();
    }
    display.dispose ();
    return 0;
  }
}
