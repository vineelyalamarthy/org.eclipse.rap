/******************************************************************************* 
* Copyright (c) 2010 EclipseSource and others. All rights reserved. This
* program and the accompanying materials are made available under the terms of
* the Eclipse Public License v1.0 which accompanies this distribution, and is
* available at http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*   EclipseSource - initial API and implementation
*******************************************************************************/ 
package org.eclipse.rwt.internal.protocol;

import org.json.*;


public class Chunk {

  private JSONObject jsonObject;

  public Chunk( final JSONObject object ) {
    this.jsonObject = object;
  }     
  
  public Object getValue( final String key ) {
    Object result = null;
    try {
      Object object = jsonObject.get( key );
      if( object instanceof JSONObject ) {
        result = new Chunk( ( JSONObject )object );
      } else if( object instanceof JSONArray ) {
        JSONArray array = ( JSONArray )object;
        result = handleArray( array );
      } else {
        result = object;
      }
    } catch( final JSONException e ) {
      // do nothing atm
    }
    return result;
  }

  private Object[] handleArray( final JSONArray array ) throws JSONException {    
    Object[] arrayWrapper = new Object[ array.length() ];
    for( int i = 0; i < array.length(); i++ ) {
      Object arrayObject = array.get( i );
      if( arrayObject instanceof JSONObject ) {
        arrayWrapper[ i ] = new Chunk( ( JSONObject )arrayObject );
      } else if( arrayObject instanceof JSONArray ) {
        arrayWrapper[ i ] = handleArray( ( JSONArray )arrayObject );
      } else {
        arrayWrapper[ i ] = arrayObject;
      }     
    }
    return arrayWrapper;
  }

}
