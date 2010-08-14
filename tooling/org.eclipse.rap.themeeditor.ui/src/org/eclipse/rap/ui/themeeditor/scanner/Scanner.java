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

import java.util.LinkedList;
import java.util.List;

import org.eclipse.rap.ui.themeeditor.scanner.region.AbstractRegion;
import org.eclipse.rap.ui.themeeditor.scanner.region.IHasParentRegion;
import org.eclipse.rap.ui.themeeditor.scanner.region.IRegionExt;
import org.eclipse.rap.ui.themeeditor.scanner.region.SelectorRegion;

public class Scanner {

  private List regionList;
  private int currentToken = 0;
  private String fullContent;
  
  public Scanner() {
    regionList = new LinkedList();
  }
  
  public AbstractRegion nextRegion() {
    if( currentToken == regionList.size() - 1 ) {
      return null;
    }
    AbstractRegion region = currentToken();
    currentToken++;
    return region;
  }

  public AbstractRegion currentToken() {
    AbstractRegion region = ( AbstractRegion )regionList.get( currentToken );
    int start = region.getOffset();
    int end = start + region.getLength();
    region.setContent( fullContent.substring( start, end  ) );
    return region;
  }

  public void scanSheet( String content ) {
    this.fullContent = content;
    IRegionExt currentState = new SelectorRegion( -1 );
    SelectorRegion lastSelector = ( SelectorRegion )currentState;
    for( int i = 0; i < content.length(); i++ ) {
      char character = content.charAt( i );
      IRegionExt newState = currentState.getNextState( character );
      if( newState != currentState ) {
        regionList.add( currentState );
        if( currentState instanceof SelectorRegion ) {
          SelectorRegion sr = ( SelectorRegion )currentState;
          if( sr.getContent().trim().length() > 0 ) {
            lastSelector = ( SelectorRegion )currentState;
          }
        } else if( currentState instanceof IHasParentRegion ) {
          ( ( IHasParentRegion )currentState ).setParentRegion( lastSelector );
        }
        if( ( currentState.getOffset() + currentState.getLength() ) < 0 ) {
        }
        currentState = newState;
      }
    }
    regionList.add( currentState );
    if( currentState instanceof SelectorRegion ) {
      lastSelector = ( SelectorRegion )currentState;
    } else if( currentState instanceof IHasParentRegion ) {
      ( ( IHasParentRegion )currentState ).setParentRegion( lastSelector );
    }
  }

  public String toString() {
    String result = "";
    for( int i = 0; i < regionList.size() - 1; i++ ) {
      result += regionList.get( i ) + "\n";
    }
    result += "-------------";
    return result;
  }
}
