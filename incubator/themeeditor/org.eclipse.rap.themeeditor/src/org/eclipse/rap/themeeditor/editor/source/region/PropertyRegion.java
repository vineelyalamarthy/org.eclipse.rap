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
package org.eclipse.rap.themeeditor.editor.source.region;

import org.eclipse.rap.themeeditor.SupportedKeywords;
import org.eclipse.rap.themeeditor.editor.source.CSSTokenProvider;

/**
 * A region in the text file representing a property inside a rule.
 */
public class PropertyRegion extends AbstractRegion {

  private StringBuffer buffer;

  public PropertyRegion( final int offset ) {
    super( offset );
    buffer = new StringBuffer();
  }

  public IRegionExt getNextState( final char character ) {
    IRegionExt result = this;
    switch( character ) {
      case ':':
        result = new ValueRegion( offset + length );
      break;
      case '}':
        result = new SelectorRegion( offset + length );
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
          buffer.setCharAt( buffer.length() - 1, ' ' );
        } else {
          buffer.append( character );
        }
      break;
      default:
        buffer.append( character );
      break;
    }
    lastCharacter = character;
    length++;
    return result;
  }

  public int getTokenType() {
    return CSSTokenProvider.PROPERTY_TOKEN;
  }

  public IRegionExt getCopy( final int offset ) {
    return new PropertyRegion( offset );
  }

  public int getKeywordType() {
    return SupportedKeywords.PROPERTY_TYPE;
  }

  public String getContent() {
    return buffer.toString();
  }
}
