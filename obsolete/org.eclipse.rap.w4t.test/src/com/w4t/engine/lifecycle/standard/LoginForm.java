/*******************************************************************************
 * Copyright (c) 2002-2006 Innoopract Informationssysteme GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Innoopract Informationssysteme GmbH - initial API and implementation
 ******************************************************************************/
package com.w4t.engine.lifecycle.standard;

import com.w4t.*;
import com.w4t.event.WebActionEvent;
import com.w4t.event.WebActionListener;


public class LoginForm extends WebForm {

  public static final String LABEL_VALUE = "This is the LoginForm";
  
  public WebButton webButton;
  public WebLabel webLabel;

  WebForm mainForm;

  protected void setWebComponents() throws Exception {
    webLabel = new WebLabel();
    webLabel.setValue( LABEL_VALUE );
    add( webLabel, WebBorderLayout.CENTER );
    webButton = new WebButton();
    webButton.setLabel( "switch to Form_SwitchTo" );
    webButton.addWebActionListener( new WebActionListener() {
      public void webActionPerformed( final WebActionEvent e ) {
        try {
          if( LoginForm.this.mainForm == null ) {
            LoginForm.this.mainForm 
              = W4TContext.loadForm( MainForm.class.getName() );
          }
          W4TContext.dispatchTo( LoginForm.this.mainForm );
        } catch( Exception e1 ) {
          String msg = "Failed to load form " + MainForm.class.getName();
          throw new RuntimeException( msg, e1 );
        }
      }
    });
    add( webButton, WebBorderLayout.CENTER );
  }

  public WebForm getMainForm() {
    return mainForm;
  }

  public void setMainForm( final WebForm mainForm ) {
    this.mainForm = mainForm;
  }
}
