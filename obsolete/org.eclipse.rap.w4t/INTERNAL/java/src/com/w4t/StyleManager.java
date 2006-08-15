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
package com.w4t;

import java.util.Hashtable;
import java.util.Map;



final class StyleManager {

  static final int HASH_CODE_NULL = 77;
  static final int HASHCODE_INITIAL = 17;
  static final int HASHCODE_CONSTANT = 37;

  private final int size;
  private final Map table;
  private final Hashtable styleTextTable; 
  
  StyleManager( final int size ) {
    this.size = size;
    this.table = new Hashtable();
    this.styleTextTable = new Hashtable();
  }

  void create( final Integer existingKey, 
               final Integer newKey, 
               final int attributeIndex, 
               final Object newValue ) 
  {
    Object[] attributes = new Object[ size ];
    table.put( newKey, attributes );
    if( contains( existingKey ) ) {
      Object[] oldAttributes
        = ( Object[] )table.get( existingKey );
      System.arraycopy( oldAttributes, 0, attributes, 0, size );
    } 
    attributes[ attributeIndex ] = newValue;
  }
  
  boolean contains( final Integer key ) {
    boolean result = false;
    if( key != null ) {
      result = table.containsKey( key );
    }
    return result;
  }
  
  Object find( final Integer key, final int attributeIndex ) {
    Object result = null;
    if( key != null ) {
      result = ( ( Object[] )table.get( key ) )[ attributeIndex ];
    }
    return result;
  }

  String getBufferedStyleText( final Integer key ) {
    return ( String )styleTextTable.get( key );
  }

  void bufferStyleText( final Integer key, final String text ) {
    styleTextTable.put( key, text );
  }
  
  Integer calculate( final Integer existingKey, 
                     final int attributeIndex, 
                     final Object newValue  ) 
  {
    int hashCode = HASHCODE_INITIAL;
    for( int i = 0; i < size; i++ ) {
      if( i == attributeIndex ) {
        hashCode = computeHashCode( hashCode, newValue );
      } else {
        if( existingKey == null ) {
          hashCode = computeHashCode( hashCode, null );        
        } else {
          hashCode = computeHashCode( hashCode, find( existingKey, i ) );
        }
      }
    }
    return new Integer( hashCode );
  }
  
  private static int computeHashCode( final int oldHashCode, 
                                      final Object object ) 
  {
    int result;
    if( object == null ) {
      result = oldHashCode * HASHCODE_CONSTANT + HASH_CODE_NULL;
    } else {
      result = oldHashCode * HASHCODE_CONSTANT + object.hashCode();
    }
    return result;
  }
}
