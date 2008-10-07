/*******************************************************************************
 * Copyright (c) 2008 Innoopract Informationssysteme GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Innoopract Informationssysteme GmbH - initial API and implementation
 ******************************************************************************/

package org.eclipse.rap.maildemo.ext;

import org.eclipse.rwt.graphics.Graphics;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;


public class SearchBar extends Composite {
  private static final String TXT_SEARCH = "Search";

  SearchBar( final Composite banner ) {
    super( banner, SWT.NONE );
    this.setLayout( new FormLayout() );
    final Text text = new Text( this, SWT.NONE );
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
    FormData fdText = new FormData();
    fdText.top = new FormAttachment( 0, 3 );
    fdText.left = new FormAttachment( 0, 0 );
    fdText.width = 150;
    text.setLayoutData( fdText );
  }
}
