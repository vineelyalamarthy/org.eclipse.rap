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


public class MainForm extends WebForm {

  public static final String LABEL_VALUE = "This is the MainForm";
  
  private WebLabel webLabel;

  protected void setWebComponents() throws Exception {
    webLabel = new WebLabel();
    webLabel.setValue( LABEL_VALUE );
    add( webLabel, WebBorderLayout.CENTER );
  }
}
