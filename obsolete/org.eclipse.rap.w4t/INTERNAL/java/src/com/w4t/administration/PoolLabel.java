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

import com.w4t.WebLabel;
import com.w4t.types.WebColor;
import com.w4t.util.DefaultColorScheme;

/**
  * a label for the entries in the tables of the cards.
  * <P>
  */
public class PoolLabel extends WebLabel {

  public PoolLabel() {
    super();
    init();
  }

  public PoolLabel( final String value ) {
    super( value );
    init();
  }

  private void init() {
    String color = DefaultColorScheme.get( DefaultColorScheme.ADMIN_LABEL_BG );
    this.getStyle().setColor( new WebColor( color ) );
    this.getStyle().setFontSize( 13 );
    this.getStyle().setTextDecoration( "none" );
    this.getStyle().setFontFamily( AdminBase.FONT );
  }
}

 