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
 * A region in the text file representing a string value.
 */
public class StringRegion extends AbstractRegion {

  private char quoteCharacter;
  private IRegionExt lastState;

  public StringRegion( final int offset,
                       final char quoteCharacter,
                       final IRegionExt lastState )
  {
    super( offset );
    this.quoteCharacter = quoteCharacter;
    this.lastState = lastState;
  }

  public IRegionExt getNextState( final char character ) {
    IRegionExt result = this;
    if( quoteCharacter == character ) {
      result = lastState.getCopy( offset + length );
    }
    lastCharacter = character;
    length++;
    return result;
  }

  public int getTokenType() {
    return TokenStyleProvider.STRING_TOKEN;
  }

  /* never called */
  public IRegionExt getCopy( final int offset ) {
    return null;
  }

  public int getKeywordType() {
    return SupportedKeywords.UNDEFINED;
  }
}
