/*******************************************************************************
 * Copyright (c) 2008 Mathias Schaeffner and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Mathias Schaeffner - initial API and implementation
 *******************************************************************************/
package org.eclipse.rap.themeeditor.editor.source;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.rap.themeeditor.ThemeEditorPlugin;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.RGB;

/**
 * Holds information about the appearance (color and style) of all token types.
 * This will be used for syntax coloring in source editor.
 */
public class CSSTokenProvider {

  public static final int PROPERTY_TOKEN = 1;
  public static final int SELECTOR_TOKEN = 2;
  public static final int STYLE_TOKEN = 3;
  public static final int STATE_TOKEN = 4;
  public static final int VARIANT_TOKEN = 5;
  public static final int STRING_TOKEN = 6;
  public static final int COMMENT_TOKEN = 7;
  public static final int DEFAULT_TOKEN = 8;
  private Map tokenMap;

  public CSSTokenProvider() {
    tokenMap = new HashMap();
    tokenMap.put( new Integer( PROPERTY_TOKEN ),
                  new CSSTokenStyle( new RGB( 127, 0, 85 ), SWT.BOLD ) );
    tokenMap.put( new Integer( SELECTOR_TOKEN ),
                  new CSSTokenStyle( new RGB( 0, 0, 0 ), SWT.BOLD ) );
    tokenMap.put( new Integer( STYLE_TOKEN ),
                  new CSSTokenStyle( new RGB( 42, 0, 255 ), SWT.NORMAL ) );
    tokenMap.put( new Integer( STATE_TOKEN ),
                  new CSSTokenStyle( new RGB( 42, 0, 255 ), SWT.ITALIC ) );
    tokenMap.put( new Integer( VARIANT_TOKEN ),
                  new CSSTokenStyle( new RGB( 255, 0, 0 ), SWT.NORMAL ) );
    tokenMap.put( new Integer( STRING_TOKEN ),
                  new CSSTokenStyle( new RGB( 42, 0, 255 ), SWT.NORMAL ) );
    tokenMap.put( new Integer( COMMENT_TOKEN ),
                  new CSSTokenStyle( new RGB( 63, 127, 95 ), SWT.NORMAL ) );
    tokenMap.put( new Integer( DEFAULT_TOKEN ),
                  new CSSTokenStyle( new RGB( 0, 0, 0 ), SWT.NORMAL ) );
  }

  /**
   * Returns a new token that can be used to style a found region appropriately
   * to its type.
   */
  public IToken createToken( final int type ) {
    CSSTokenStyle tokenStyle = ( CSSTokenStyle )tokenMap.get( new Integer( type ) );
    if( tokenStyle == null ) {
      tokenStyle = ( CSSTokenStyle )tokenMap.get( new Integer( DEFAULT_TOKEN ) );
    }
    return new Token( new TextAttribute( ThemeEditorPlugin.getDefault()
      .getColorRegistry()
      .getColor( tokenStyle.rgb ), null, tokenStyle.style ) );
  }
}
