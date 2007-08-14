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
package com.w4t.types;

import java.util.StringTokenizer;

/** <p>RegularExpression objects encapsulate Strings which are
 *  representing regular expressions.</p>
 * 
 *  <p>Regular expressions can be used e.g. for input validation in
 *  {@link org.eclipse.rwt.Validator Validator} implementations.</p>
 */ 
public class RegularExpression extends WebPropertyBase {

  /** <p>creates a new instance of RegularExpression. </p> */
  public RegularExpression( final String value ) {
    super( escape( value, "\\", "\\\\" ) );
  }

  /** <p>returns a String representation of the regular expression without
   *  escaping the backslash of backslash characters.</p> */  
  public String toUnescapedString() {
    return escape( value, "\\\\", "\\" );
  }

  /** <p>returns a String representation of the regular expression with
   *  an escaped backslash of the regular expressions 
   *  backslash characters.</p> */  
  public String toString() {
    return super.toString();
  }
 
  /** <p>replace all occurrence of 'toReplace' 
   *  in 'target' with 'replacement'. Usefull for de/encoding 
   *  backslash characters in regular expression values.</p> */  
  private static String escape( final String target, 
                                final String toReplace, 
                                final String replacement ) {
    StringBuffer result = new StringBuffer();
    if( target.startsWith( toReplace ) ) {
      result.append( replacement );
    }
    StringTokenizer tokenizer = new StringTokenizer( target, toReplace, false );
    while( tokenizer.hasMoreTokens() ) {
      result.append( tokenizer.nextToken() );
      if( tokenizer.hasMoreTokens() ) {
        result.append( replacement );
      }
    }
    if( target.endsWith( toReplace ) ) {
      result.append( replacement );
    }
    return result.toString();
  }  
}