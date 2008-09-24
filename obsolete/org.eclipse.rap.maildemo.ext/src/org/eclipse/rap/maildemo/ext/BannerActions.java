package org.eclipse.rap.maildemo.ext;

import org.eclipse.jface.action.IAction;
import org.eclipse.rwt.graphics.Graphics;
import org.eclipse.rwt.lifecycle.WidgetUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;


public class BannerActions extends Composite {

  BannerActions( Composite parent, IAction[] commands ) {
    super( parent, SWT.NONE );
    this.setLayout( new RowLayout() );
    FormData fdComposite = new FormData();
    this.setLayoutData( fdComposite );
    fdComposite.top = new FormAttachment( 0, 8 );
    fdComposite.left = new FormAttachment( 0, 200 );
    fdComposite.bottom = new FormAttachment( 0, 36 );
       
    for( int i = 0; i < commands.length; i++ ) {
      final IAction command = commands[ i ];
        Button button = new Button( this, SWT.PUSH | SWT.FLAT );
        button.setText( command.getText()  );
        button.setToolTipText( command.getToolTipText() );
        button.setData( WidgetUtil.CUSTOM_VARIANT, "bannerButton" );
        button.pack();
        button.addSelectionListener( new SelectionAdapter(){

        	public void widgetSelected( SelectionEvent e ) {
              command.run();
            }
        });
        
        if( i < commands.length -1 ) {
          Label label = new Label( this, SWT.SEPARATOR | SWT.VERTICAL );
          label.setForeground( Graphics.getColor( 255, 255, 255 ) );
        }
      }

  }
}
