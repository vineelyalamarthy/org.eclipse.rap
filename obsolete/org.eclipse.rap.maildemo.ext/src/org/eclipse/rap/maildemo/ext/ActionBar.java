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
import org.eclipse.rwt.lifecycle.WidgetUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;


/**
 * Provides a button bar that allows toggling between the buttons of the bar.
 */
public class ActionBar extends Composite {

  private static class ActionHolder {
    IAction action;
    Button button;
  }
  
  private ActionHolder[] actionHolders = {};

  public ActionBar( final Composite parent,
                    final int style,
                    final IAction[] actions )
  {
    super( parent, style );
    this.setLayout( new RowLayout() );
    if( actions != null ) {
      actionHolders = new ActionHolder[ actions.length ];
      for( int i = 0; i < actions.length; i++ ) {
        actionHolders[ i ] = new ActionHolder();
        actionHolders[ i ].action = actions[ i ];
      }
      for( int i = 0; i < actionHolders.length; i++ ) {
        createActionBarButton( i );
      }
    }
  }

  private void createActionBarButton( final int i ) {
    final Button button = new Button( this, SWT.TOGGLE | SWT.FLAT );
    button.setText( actionHolders[ i ].action.getText() );
    button.setData( WidgetUtil.CUSTOM_VARIANT, "actionbar" );
    actionHolders[ i ].button = button;
    if( i == 0 ) {
      actionHolders[ i ].button.setSelection( true );
    }
    button.addSelectionListener( new SelectionAdapter() {
      public void widgetSelected( final SelectionEvent evt ) {
        for( int j = 0; j < actionHolders.length; j++ ) {
          actionHolders[ j ].button.setSelection( false );
        }
        button.setSelection( true );
        actionHolders[ i ].action.run();
      }
    } );
  }
}
