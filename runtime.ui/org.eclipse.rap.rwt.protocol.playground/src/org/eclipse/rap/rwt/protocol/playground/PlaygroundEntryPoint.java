package org.eclipse.rap.rwt.protocol.playground;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.rwt.lifecycle.IEntryPoint;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.plugin.AbstractUIPlugin;


public class PlaygroundEntryPoint implements IEntryPoint {

  public PlaygroundEntryPoint() {
  }

  public int createUI() {
    Display display = new Display();
    final Shell shell = new Shell( display );
    shell.setLayout( new FillLayout() );
    shell.setText( "I'm a protocol shell" );
    final Button button = new Button( shell, SWT.CHECK );
    button.setText( "I'm a protocol Button" );
    ImageDescriptor imageDescriptor 
      = AbstractUIPlugin.imageDescriptorFromPlugin( "org.eclipse.rap.rwt.protocol.playground", 
                                                    "img/bell_error.png" );
    button.setImage( imageDescriptor.createImage() );
    button.addSelectionListener( new SelectionAdapter() {
      public void widgetSelected( final SelectionEvent e ) {
        if( button.getText().equals( "I'm a protocol Button" ) ) {
          button.setText( "Text changed with the protocol" );
        } else {
          button.setText( "I'm a protocol Button" );
        }
        
      };
    } );
//    GridLayout layout = new GridLayout( 100, true );
//    shell.setLayout( layout );
//    for( int i = 0; i < 1000; i++ ) {
//      Button button = new Button( shell, SWT.PUSH );
//      button.setText( "Button " + i );
//      button.setImage( imageDescriptor.createImage() );
//    }
    
    
    shell.open ();
    while (!shell.isDisposed ()) {
        if (!display.readAndDispatch ()) display.sleep ();
    }
    display.dispose ();
    return 0;
  }
}
