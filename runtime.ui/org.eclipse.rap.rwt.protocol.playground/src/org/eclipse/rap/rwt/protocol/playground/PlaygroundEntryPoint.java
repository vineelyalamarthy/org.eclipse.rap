package org.eclipse.rap.rwt.protocol.playground;

import org.eclipse.rwt.lifecycle.IEntryPoint;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;


public class PlaygroundEntryPoint implements IEntryPoint {

  public PlaygroundEntryPoint() {
  }

  public int createUI() {
    Display display = new Display();
    final Shell shell = new Shell( display );
    shell.setLayout( new FormLayout() );
    final String shellText = "A Shell created via the SWT API";
    shell.setText( shellText );
    
    final Button changeButtonTextButton = new Button( shell, SWT.PUSH );
    final String buttonText = "Change my description";
    changeButtonTextButton.setText( buttonText );
    changeButtonTextButton.addSelectionListener( new SelectionAdapter() {
      public void widgetSelected( final SelectionEvent e ) {
        if( changeButtonTextButton.getText().equals( buttonText ) ) {
          changeButtonTextButton.setText( "My description was changed" );
        } else {
          changeButtonTextButton.setText( buttonText );
        }
        
      };
    } );
    FormData fdChangeButtonTextButton = new FormData();
    changeButtonTextButton.setLayoutData( fdChangeButtonTextButton );
    fdChangeButtonTextButton.left = new FormAttachment( 0, 10 );
    fdChangeButtonTextButton.top = new FormAttachment( 0, 10 );
    
    Button changeShellTitleButton = new Button( shell, SWT.PUSH );
    changeShellTitleButton.setText( "Change the Shell's title" );
    changeShellTitleButton.addSelectionListener( new SelectionAdapter() {
      public void widgetSelected( final SelectionEvent e ) {
        String text = shell.getText();
        if( text.equals( shellText ) ) {
          shell.setText( "Oh, my title was changed. Awesome..." );
        } else {
          shell.setText( shellText );
        }
      };
    } );
    FormData fdChangeShellTitleButton = new FormData();
    changeShellTitleButton.setLayoutData( fdChangeShellTitleButton );
    fdChangeShellTitleButton.left = new FormAttachment( 0, 10 );
    fdChangeShellTitleButton.top 
      = new FormAttachment( changeButtonTextButton, 10 );
    
    final Button enableMeButton = new Button( shell, SWT.PUSH );
    enableMeButton.setEnabled( false );
    enableMeButton.setText( "I'm enabled" );
    FormData fdEnableMeButton = new FormData();
    enableMeButton.setLayoutData( fdEnableMeButton );
    fdEnableMeButton.left = new FormAttachment( 0, 10 );
    fdEnableMeButton.top = new FormAttachment( changeShellTitleButton, 10 );
    
    final Button enableSwitcher = new Button( shell, SWT.CHECK );
    enableSwitcher.addSelectionListener( new SelectionAdapter() {
      public void widgetSelected( final SelectionEvent e ) {
        enableMeButton.setEnabled( enableSwitcher.getSelection() );
      };
    } );
    FormData fdEnableSwitcher = new FormData();
    enableSwitcher.setLayoutData( fdEnableSwitcher );
    fdEnableSwitcher.left = new FormAttachment( enableMeButton, 30 );
    fdEnableSwitcher.top = new FormAttachment( changeShellTitleButton, 11 );
    
//    Slider slider = new Slider( shell, SWT.NONE );
//    slider.addSelectionListener( new SelectionAdapter() {
//      public void widgetSelected( final SelectionEvent e ) {
//        System.out.println( "test");
//      };
//    } );
    
    
    shell.open();
    while (!shell.isDisposed ()) {
        if (!display.readAndDispatch ()) display.sleep ();
    }
    display.dispose ();
    return 0;
  }
}
