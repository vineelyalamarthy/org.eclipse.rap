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
package com.w4t.administration;

import com.w4t.WebButton;
import com.w4t.WebPanel;
import com.w4t.types.WebColor;
import com.w4t.util.DefaultColorScheme;

/**
  * The default link layout for the menu of the administration.
  * <P>
  */
public class LinkButton extends WebButton {

  public LinkButton() {
    super();
    init();
  }

  public LinkButton( final String label ) {
    super( label );
    init();
  }

  public LinkButton( final String label, final WebPanel pnl ) {
    super( label, pnl );
    init();
  }

  private void init() {
    this.setLink( true );
    String color = DefaultColorScheme.get( DefaultColorScheme.ADMIN_LINK );
    this.getStyle().setColor( new WebColor( color ) );
    this.getStyle().setFontSize( 13 );
    this.getStyle().setTextDecoration( "none" );
    this.setUseTrim( true );
  }
}