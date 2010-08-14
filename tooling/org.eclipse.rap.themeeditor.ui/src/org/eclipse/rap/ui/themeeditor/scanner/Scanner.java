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

  private List regions;
  private int currentToken = 0;
  private String fullContent;

  public Scanner() {
    regions = new LinkedList();
  }

  public AbstractRegion nextRegion() {
    if( currentToken == regions.size() - 1 ) {
      return null;
    }
    AbstractRegion region = ( AbstractRegion )regions.get( currentToken );
    fillContent( region );
    currentToken++;
    return region;
  }

  private void fillContent( AbstractRegion region ) {
    int start = region.getOffset();
    int end = start + region.getLength();
    if( start < end ) {
      region.setContent( fullContent.substring( start, end ) );
    }
  }

  public void scanSheet( String content ) {
    this.fullContent = content;
    IRegionExt currentState = new SelectorRegion( -1 );
    SelectorRegion lastSelector = ( SelectorRegion )currentState;
    for( int i = 0; i < content.length(); i++ ) {
      char character = content.charAt( i );
      IRegionExt newState = currentState.getNextState( character );
      if( newState != currentState ) {
        regions.add( currentState );
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
    regions.add( currentState );
    if( currentState instanceof SelectorRegion ) {
      lastSelector = ( SelectorRegion )currentState;
    } else if( currentState instanceof IHasParentRegion ) {
      ( ( IHasParentRegion )currentState ).setParentRegion( lastSelector );
    }
  }

  public IRegionExt[] getRegions() {
    return ( IRegionExt[] )regions.toArray( new IRegionExt[ regions.size() ] );
  }

  public String toString() {
    String result = "";
    for( int i = 0; i < regions.size() - 1; i++ ) {
      AbstractRegion region = ( AbstractRegion )regions.get( i );
      fillContent( region );
      result += region +  "\n";
    }
    result += "-------------";
    return result;
  }
}
