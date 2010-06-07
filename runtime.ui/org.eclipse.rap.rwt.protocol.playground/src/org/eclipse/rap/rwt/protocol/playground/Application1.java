package org.eclipse.rap.rwt.protocol.playground;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;


public class Application1 implements IApplication {

  public Object start( IApplicationContext context ) throws Exception {
    Display display = new Display();
    final Shell shell = new Shell( display );
    shell.setLayout( new FillLayout() );
    shell.setText( "I'm a protocol shell" );
    Button button = new Button( shell, SWT.PUSH );
    button.setText( "dispose shell" );
    button.addSelectionListener( new SelectionAdapter() {
      public void widgetSelected( final SelectionEvent e ) {
        shell.dispose();
      };
    } );
    
    Button modeButton = new Button( shell, SWT.PUSH );
    modeButton.setText( "print mode" );
    modeButton.addSelectionListener( new SelectionAdapter() {
      public void widgetSelected( final SelectionEvent e ) {
        System.out.println( "Maximized:" + shell.getMaximized() );
      };
    } );
    
    shell.addShellListener( new ShellAdapter() {
      public void shellClosed( final ShellEvent e ) {
        System.out.println( "shell closed" );
      }
    } );
    
    shell.open ();
    while (!shell.isDisposed ()) {
        if (!display.readAndDispatch ()) display.sleep ();
    }
    display.dispose ();
    return EXIT_OK;
  }

  public void stop() {
  }
}
