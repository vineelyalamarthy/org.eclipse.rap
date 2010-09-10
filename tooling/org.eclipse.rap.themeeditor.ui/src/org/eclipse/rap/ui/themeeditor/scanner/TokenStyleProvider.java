/*******************************************************************************
 * Copyright (c) 2010 EclipseSource.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     EclipseSource - initial API and implementation
 ******************************************************************************/
package org.eclipse.rap.ui.themeeditor.scanner;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

/**
 * Holds information about the appearance (color and style) of all token types.
 * This will be used for syntax coloring in source editor.
 */
public class TokenStyleProvider {

  private static class TokenStyle {

    public RGB rgb;
    public int style;

    public TokenStyle( final int r, final int g, final int b, final int style )
    {
      this.rgb = new RGB( r, g, b );
      this.style = style;
    }
  }
  
  public static final int DEFAULT_TOKEN = 0;
  public static final int PROPERTY_TOKEN = 1;
  public static final int SELECTOR_TOKEN = 2;
  public static final int STYLE_TOKEN = 3;
  public static final int STATE_TOKEN = 4;
  public static final int VARIANT_TOKEN = 5;
  public static final int STRING_TOKEN = 6;
  public static final int COMMENT_TOKEN = 7;
  
  private TokenStyle tokenMap[];

  public TokenStyleProvider() {
    tokenMap = new TokenStyle[ 8 ];
    tokenMap[ PROPERTY_TOKEN ] = new TokenStyle( 127, 0, 85, SWT.BOLD );
    tokenMap[ SELECTOR_TOKEN ] = new TokenStyle( 0, 0, 0, SWT.BOLD );
    tokenMap[ STYLE_TOKEN ] = new TokenStyle( 42, 0, 255, SWT.NORMAL );
    tokenMap[ STATE_TOKEN ] = new TokenStyle( 42, 0, 255, SWT.ITALIC );
    tokenMap[ VARIANT_TOKEN ] = new TokenStyle( 255, 0, 0, SWT.NORMAL );
    tokenMap[ STRING_TOKEN ] = new TokenStyle( 42, 0, 255, SWT.NORMAL );
    tokenMap[ COMMENT_TOKEN ] = new TokenStyle( 63, 127, 95, SWT.NORMAL );
    tokenMap[ DEFAULT_TOKEN ] = new TokenStyle( 0, 0, 0, SWT.NORMAL );
  }

  /**
   * Returns a new token that can be used to style a found region appropriately
   * to its type.
   */
  public IToken createToken( final int type ) {
    TokenStyle tokenStyle = tokenMap[ type ];
    // TODO [bm] need to dispose theses colors somehwere
    Display display = Display.getDefault();
    Color foreground = new Color( display, tokenStyle.rgb );
    return new Token( new TextAttribute( foreground, null, tokenStyle.style ) );
  }
}
