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
package org.eclipse.rap.ui.themeeditor.scanner.region;

import org.eclipse.rap.ui.themeeditor.scanner.TokenStyleProvider;

/**
 * A region in the text file representing a variant belonging to a selector.
 */
public class VariantRegion extends AbstractRegion {

  public VariantRegion( final int offset ) {
    super( offset );
  }

  public IRegionExt getNextState( final char character ) {
    IRegionExt result = this;
    switch( character ) {
      case '[':
        result = new StyleRegion( offset + length );
      break;
      case ';':
        result = new SelectorRegion( offset + length );
      break;
      case ',':
        result = new SelectorRegion( offset + length );
      break;
      case ':':
        result = new StateRegion( offset + length );
      break;
      case '.':
        result = new VariantRegion( offset + length );
        length++;
      break;
      case '{':
        result = new PropertyRegion( offset + length );
      break;
      case '"':
        result = new StringRegion( offset + length, character, this );
      break;
      case '\'':
        result = new StringRegion( offset + length, character, this );
      break;
      case '*':
        if( lastCharacter == '/' ) {
          result = new CommentRegion( offset + length - 1, this );
          length--;
        }
      break;
      default:
      break;
    }
    if( Character.isWhitespace( character ) ) {
      result = new SelectorRegion( offset + length );
    }
    lastCharacter = character;
    length++;
    return result;
  }

  public int getTokenType() {
    return TokenStyleProvider.VARIANT_TOKEN;
  }

  public IRegionExt getCopy( int offset ) {
    return new VariantRegion( offset );
  }

  public int getKeywordType() {
    return SupportedKeywords.UNDEFINED;
  }
}
