package org.eclipse.rap.maildemo.ext;

import org.eclipse.rwt.graphics.Graphics;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;


public class Search extends Composite {
  private static final String TXT_SEARCH = "Search";

  Search( Composite banner ) {
    super( banner, SWT.NONE );
    this.setLayout( new FormLayout() );
    final Text text = new Text( this, SWT.NONE );
    FormData fdText = new FormData();
    text.setLayoutData( fdText );
    text.setText( TXT_SEARCH );
    text.setForeground( Graphics.getColor( 128, 128, 128 ) );
    text.addFocusListener( new FocusListener() {
      public void focusGained( final FocusEvent event ) {
        if( TXT_SEARCH.equals( ( text.getText() ) ) ) {
          text.setText( "" );
        }
      }
      public void focusLost( final FocusEvent event ) {
        if( "".equals( ( text.getText() ) ) ) {
          text.setText( TXT_SEARCH );
        }
      }
    } );
    
    fdText.top = new FormAttachment( 0, 3 );
    fdText.left = new FormAttachment( 0, 0 );
    fdText.width = 150;
    
    FormData fdSearch = new FormData();
    this.setLayoutData( fdSearch );
    fdSearch.top = new FormAttachment( 0, 10 );
    fdSearch.left = new FormAttachment( 100, -175 );

  }
}
