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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.ITokenScanner;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.rap.themeeditor.editor.source.region.IHasParentRegion;
import org.eclipse.rap.themeeditor.editor.source.region.IRegionExt;
import org.eclipse.rap.themeeditor.editor.source.region.SelectorRegion;

/**
 * Scans a document and splits its content into tokens, which are implementors
 * of IRegionExt.
 */
public class CSSTokenScanner implements ITokenScanner {

  private CSSTokenProvider provider;
  private CSSSourceEditor editor;
  private List outlineRegions;
  private List tokenList;
  private IRegionExt lastState;
  private int currentListPosition;
  private int lastListPosition;
  private ITokenChangedListener listener = null;
  private String oldContent;
  private int oldListPosition;
  private SelectorRegion lastSelector;

  public CSSTokenScanner( final CSSSourceEditor editor ) {
    this.editor = editor;
    provider = new CSSTokenProvider();
    tokenList = new ArrayList();
    outlineRegions = new ArrayList();
  }

  public int getTokenLength() {
    return lastState.getLength();
  }

  public int getTokenOffset() {
    return lastState.getOffset();
  }

  public IToken nextToken() {
    currentListPosition++;
    if( currentListPosition >= tokenList.size()
        || ( currentListPosition > lastListPosition ) )
    {
      return Token.EOF;
    }
    lastState = ( IRegionExt )tokenList.get( currentListPosition );
    IToken result = provider.createToken( lastState.getTokenType() );
    return result;
  }

  public void setRange( final IDocument document,
                        final int offset,
                        final int length )
  {
    if( document.get().equals( oldContent ) ) {
      currentListPosition = oldListPosition;
    } else {
      oldContent = document.get();
      tokenList.clear();
      currentListPosition = -1;
      lastListPosition = -1;
      IRegionExt currentState = new SelectorRegion( -1 );
      lastSelector = ( SelectorRegion )currentState;
      for( int i = 0; i < document.getLength(); i++ ) {
        try {
          char character = document.getChar( i );
          IRegionExt newState = currentState.getNextState( character );
          if( newState != currentState ) {
            tokenList.add( currentState );
            if( currentState instanceof SelectorRegion ) {
              SelectorRegion sr = ( SelectorRegion )currentState;
              if( sr.getContent().trim().length() > 0 ) {
                lastSelector = ( SelectorRegion )currentState;
              }
            } else if( currentState instanceof IHasParentRegion ) {
              ( ( IHasParentRegion )currentState ).setParentRegion( lastSelector );
            }
            if( ( currentState.getOffset() + currentState.getLength() ) < offset )
            {
              currentListPosition++;
            }
            if( currentState.getOffset() < ( offset + length ) ) {
              lastListPosition++;
            }
            currentState = newState;
          }
        } catch( BadLocationException e ) {
          e.printStackTrace();
        }
      }
      tokenList.add( currentState );
      if( currentState instanceof SelectorRegion ) {
        lastSelector = ( SelectorRegion )currentState;
      } else if( currentState instanceof IHasParentRegion ) {
        ( ( IHasParentRegion )currentState ).setParentRegion( lastSelector );
      }
      if( ( currentState.getOffset() + currentState.getLength() ) < offset ) {
        currentListPosition++;
      }
      if( currentState.getOffset() < ( offset + length ) ) {
        lastListPosition++;
      }
      oldListPosition = currentListPosition;
      // notify token changed listener if one is registered
      if( listener != null ) {
        listener.tokensChanged();
      }
    }
  }

  /**
   * Returns the token at a given offset position in the document.
   */
  public IRegionExt getRegionExt( final int offset ) {
    IRegionExt result = null;
    Iterator it = tokenList.iterator();
    while( it.hasNext() ) {
      IRegionExt regionExt = ( IRegionExt )it.next();
      if( regionExt.getOffset() <= offset ) {
        result = regionExt;
      }
    }
    return result;
  }

  public List getTokenList() {
    return tokenList;
  }

  public void setTokenChangedListener( final ITokenChangedListener listener ) {
    this.listener = listener;
  }

  /**
   * Returns the array of Outline regions that are directly used as the input
   * items for the Outline.
   */
  public synchronized OutlineRegion[] getOutlineRegionsArray() {
    outlineRegions.clear();
    boolean bufferEnabled = false;
    int offset = 0;
    List outlineElements = new ArrayList();
    Iterator it = getTokenList().iterator();
    while( it.hasNext() ) {
      IRegionExt regionExt = ( IRegionExt )it.next();
      if( regionExt.getTokenType() == CSSTokenProvider.SELECTOR_TOKEN ) {
        outlineElements.add( regionExt );
      }
      if( regionExt.getTokenType() == CSSTokenProvider.SELECTOR_TOKEN
          && !bufferEnabled )
      {
        String temp = "";
        try {
          temp = editor.getDocument().get( regionExt.getOffset(),
                                           regionExt.getLength() ).trim();
        } catch( BadLocationException e ) {
          temp = "";
        }
        if( temp.length() > 0 ) {
          offset = regionExt.getOffset();
          bufferEnabled = true;
        }
      } else if( regionExt.getTokenType() == CSSTokenProvider.PROPERTY_TOKEN
                 && bufferEnabled )
      {
        bufferEnabled = false;
        int length = regionExt.getOffset() - offset - 1;
        String content = "";
        try {
          content = editor.getDocument().get( offset, length ).trim();
        } catch( BadLocationException e ) {
          e.printStackTrace();
        }
        if( content.length() > 0 ) {
          // ignore comments and white spaces
          content = content.replaceAll( "/\\*.*\\*/", " " );
          content = content.replaceAll( "\\s+", " " );
          OutlineRegion outlineRegion = new OutlineRegion( offset,
                                                           length,
                                                           content,
                                                           outlineElements );
          outlineRegions.add( outlineRegion );
          outlineElements = new ArrayList();
        }
      }
    }
    OutlineRegion[] result = new OutlineRegion[ outlineRegions.size() ];
    return ( org.eclipse.rap.themeeditor.editor.source.OutlineRegion[] )outlineRegions.toArray( result );
  }

  public List getOutlineRegionsList() {
    return outlineRegions;
  }
}
