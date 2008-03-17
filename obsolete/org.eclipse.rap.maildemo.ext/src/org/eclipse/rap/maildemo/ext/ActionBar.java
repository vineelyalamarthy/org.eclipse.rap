// Created on 11.02.2008
package org.eclipse.rap.maildemo.ext;

import org.eclipse.jface.action.IAction;
import org.eclipse.rwt.lifecycle.WidgetUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

/** 
 * provides a button bar that allows toggling between the buttons of the bar 
 * 
 * */
public class ActionBar extends Composite {
  
  private ActionHolder[] actionHolders = {};
  
  private class ActionHolder {
    IAction action;
    Button button;
  }
  
  public ActionBar( final Composite parent,
                    final int style,
                    final IAction[] actions )
  {
    super( parent, style );
    if( actions != null ) {
      this.actionHolders = new ActionHolder[ actions.length ];
      for( int i = 0; i < actions.length; i++ ) {
        this.actionHolders[ i ] = new ActionHolder();
        this.actionHolders[ i ].action = actions[ i ];
      }
    }
    createControl();
  }

  private void createControl() {
    this.setLayout( new RowLayout() );
    for( int i = 0; i < actionHolders.length; i++ ) {
      createActionBarButton( i );
    }
  }

  private void createActionBarButton( final int i ) {

    final Button button = new Button( this, SWT.TOGGLE | SWT.FLAT );
    button.setText( actionHolders[ i ].action.getText() );
    RowData rdButton = new RowData();
    button.setLayoutData( rdButton );
    button.setData( WidgetUtil.CUSTOM_VARIANT, "actionbar" );
    actionHolders[ i ].button = button;
    if( i == 0 ){
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
