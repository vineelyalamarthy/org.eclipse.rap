/*******************************************************************************
 * Copyright (c) 2008 Innoopract Informationssysteme GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Innoopract Informationssysteme GmbH - initial API and implementation
 ******************************************************************************/
package org.eclipse.rwt.internal.theme.css;

import java.util.*;

import org.eclipse.rwt.internal.theme.QxType;
import org.w3c.css.sac.LexicalUnit;

public class StylePropertyMap {

  private final Map properties = new HashMap();

  /* BEGIN Modification for Theme Editor */
  private final Map lineNumbers = new HashMap();
  private final Map propertyStrings = new HashMap();
  /* END Modification for Theme Editor */

  public LexicalUnit getProperty( final String key ) {
    return ( LexicalUnit )properties.get( key );
  }

  public void setProperty( final String key, final LexicalUnit value ) {
    if( key == null || value == null ) {
      throw new NullPointerException( "null argument" );
    }
    properties.put( key, value );
    /* BEGIN Modification for Theme Editor */
    QxType qxType = PropertyResolver.getQxType( key, value );
    if( qxType != null ) {
      setPropertyString( key, qxType.toDefaultString() );
    }
    /* END Modification for Theme Editor */
  }

  public String[] getKeys() {
    Set keySet = properties.keySet();
    String[] result = new String[ keySet.size() ];
    keySet.toArray( result );
    return result;
  }

  public void merge( final StylePropertyMap styles ) {
    String[] keys = styles.getKeys();
    for( int i = 0; i < keys.length; i++ ) {
      String key = keys[ i ];
      properties.put( key, styles.getProperty( key ) );
      propertyStrings.put( key, styles.getPropertyString( key ) );
    }
  }

  /*
   * BEGIN Modification for Theme Editor
   */
  public void removeProperty( final String key ) {
    if( key != null ) {
      properties.remove( key );
      propertyStrings.remove( key );
    }
  }

  public int size() {
    return properties.size();
  }

  public int getLineNumber( final String key ) {
    int result = -1;
    Integer value = ( Integer )lineNumbers.get( key );
    if( value != null ) {
      result = value.intValue();
    }
    return result;
  }

  public void setLineNumber( final String key, final int lineNumber ) {
    if( key != null ) {
      lineNumbers.put( key, new Integer( lineNumber ) );
    }
  }

  public String getPropertyString( final String key ) {
    return ( String )propertyStrings.get( key );
  }

  public void setPropertyString( final String key, final String value ) {
    if( key == null || value == null ) {
      throw new NullPointerException( "null argument" );
    }
    if ( properties.containsKey( key ) ) {
      propertyStrings.put( key, value );
    }
  }
  /*
   * END Modification for Theme Editor
   */
}
