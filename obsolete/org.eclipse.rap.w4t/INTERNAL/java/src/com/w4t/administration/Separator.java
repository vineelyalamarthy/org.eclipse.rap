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

import org.eclipse.rwt.internal.util.HTML;

import com.w4t.*;
import com.w4t.types.WebColor;
import com.w4t.util.DefaultColorScheme;

/**
 * <p>Helper class that creates vertical separators. Can be used to visually
 * arrange top-level-menu-like links.</p>
 * <p>Looks like</p>
 * <pre>File <strong>|</strong> Edit <strong>|</strong> Help</pre>
 */
final class Separator {
  
  private Separator() {
    // prevent instantiation
  }
  
  public static WebComponent create() {
    MarkupEmbedder result 
      = new MarkupEmbedder( HTML.NBSP + "|" + HTML.NBSP );
    Style style = result.getStyle();
    // alignment
    style.setDisplay( "inline" );
    style.setVerticalAlign( "center" );
    // font
    style.setFontFamily( DefaultColorScheme.ADMIN_HEADER_FONT );
    style.setFontSize( 13 );
    style.setTextDecoration( "none" );
    // fg- and bg-color
    String color = DefaultColorScheme.get( DefaultColorScheme.ADMIN_MENU );
    style.setBgColor( new WebColor( color ) );
    color = DefaultColorScheme.get( DefaultColorScheme.ADMIN_LINK );
    style.setColor( new WebColor( color ) );
    return result;
  }
}
