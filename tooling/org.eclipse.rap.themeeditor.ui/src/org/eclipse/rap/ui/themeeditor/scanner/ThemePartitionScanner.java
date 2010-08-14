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

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.rules.IPartitionTokenScanner;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.rap.ui.themeeditor.scanner.region.AbstractRegion;

/**
 * Scans a document and splits its content into tokens, which are implementors
 * of IRegionExt.
 */
public class ThemePartitionScanner implements IPartitionTokenScanner {

  private TokenStyleProvider styleProvider;
  private Scanner scanner;

  public ThemePartitionScanner() {
    styleProvider = new TokenStyleProvider();
  }

  public int getTokenLength() {
    return scanner.currentToken().getLength();
  }

  public int getTokenOffset() {
    return scanner.currentToken().getOffset();
  }

  public IToken nextToken() {
    IToken result;
    AbstractRegion region = scanner.nextRegion();
    if( region == null ) {
      result = Token.EOF;
    } else {
      result = styleProvider.createToken( region.getTokenType() );
    }
    return result;
  }

  public void setRange( final IDocument document,
                        final int offset,
                        final int length )
  {
    scanner = new Scanner();
    String content = document.get();
    scanner.scanSheet( content );
  }

  public void setPartialRange( final IDocument document,
                               final int offset,
                               final int length,
                               final String contentType,
                               final int partitionOffset )
  {
  }
}
