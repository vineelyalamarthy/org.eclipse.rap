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

import org.eclipse.jface.action.IAction;
import org.eclipse.rwt.graphics.Graphics;
import org.eclipse.rwt.lifecycle.WidgetUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.*;


public class BannerActionBar extends Composite {

  BannerActionBar( final Composite parent, final IAction[] commands ) {
    super( parent, SWT.NONE );
    this.setLayout( new RowLayout() );
    for( int i = 0; i < commands.length; i++ ) {
      final IAction command = commands[ i ];
      Button button = new Button( this, SWT.PUSH | SWT.FLAT );
      button.setText( command.getText() );
      button.setToolTipText( command.getToolTipText() );
      button.setData( WidgetUtil.CUSTOM_VARIANT, "bannerButton" );
      button.addSelectionListener( new SelectionAdapter() {

        public void widgetSelected( final SelectionEvent e ) {
          command.run();
        }
      } );
      if( i < commands.length - 1 ) {
        Label label = new Label( this, SWT.SEPARATOR | SWT.VERTICAL );
        label.setForeground( Graphics.getColor( 255, 255, 255 ) );
      }
    }
  }
}
