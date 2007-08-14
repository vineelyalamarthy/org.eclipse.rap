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
package com.w4t.webformkit;

import org.eclipse.rwt.internal.service.ContextProvider;
import org.eclipse.rwt.internal.service.IServiceStateInfo;

/**
 * <p>Helper class to store and retrieve selection of currently focused
 * text input field.</p>
 */
final class SelectionUtil {
  
  private static final String SELECTION = "W4T_FORM_FOCUS_SELECTION";

  public static final class Selection {
    private final String start;
    private final String end;

    public Selection( final String start, final String end ) {
      this.start = start;
      this.end = end;
    }
    
    public String getEnd() {
      return end;
    }
    
    public String getStart() {
      return start;
    }
    
    public String toString() {
      return ";" + start + ";" + end;
    }
  }
  
  private SelectionUtil() {
    // prevent instantiation
  }
  
  static Selection parseSelection( final String value ) {
    String[] parts = value.split( ";" );
    Selection result;
    if ( parts.length > 1 ) {
      result = new Selection( parts[ 1 ], parts[ 2 ] );
    } else {
      result = null;
    }
    return result;
  }
  
  static String parseFocusId( final String value ) {
    String result = null;
    if( value != null ) {
      String[] parts = value.split( ";" );
      result = parts[ 0 ];
    }
    return result;
  }
  
  static void setSelection( final Selection selection ) {
    IServiceStateInfo stateInfo = ContextProvider.getStateInfo();
    stateInfo.setAttribute( SELECTION, selection );
  }
  
  static Selection retrieveSelection() {
    IServiceStateInfo stateInfo = ContextProvider.getStateInfo();
    return ( Selection )stateInfo.getAttribute( SELECTION );
  }
}
