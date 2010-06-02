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

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class Processor {

  private String message;
  private List listeners;

  public Processor( final String message ) {
    this.message = message;    
  }

  public void addStreamListener( final IStreamListener listener ) {
    if( listeners == null ) {
      listeners = new ArrayList();
    }
    listeners.add( listener );
  }

  public void parse() {
    try {      
      parseJson();      
    } catch( final JSONException e ) {
      e.printStackTrace();
    }
  }

  private void parseJson() throws JSONException {
    // TODO remove JSON.org parser
    JSONObject jsonObject = new JSONObject( message );
    String[] keys = JSONObject.getNames( jsonObject );
    if( keys != null ) {
      for( int i = 0; i < keys.length; i++ ) {        
        Object child = jsonObject.get( keys[ i ] );
        if( child instanceof JSONObject ) {
          notifyListeners( keys[ i ], ( JSONObject )child );
        } else if( child instanceof JSONArray ) {
          JSONArray widgetArray = ( JSONArray )child;
          processWidgetArray( keys[ i ], widgetArray );
        }
      }
    }
  }

  private void processWidgetArray( final String key, 
                                   final JSONArray widgetArray )
    throws JSONException
  {
    for( int j = 0; j < widgetArray.length(); j++ ) {
      Object arrayObject = widgetArray.get( j );
      if( arrayObject instanceof JSONObject ) {
        notifyListeners( key, ( JSONObject )arrayObject );
      }
    }
  }

  private void notifyListeners( final String key, final JSONObject object ) {
    Chunk chunk = new Chunk( object );
    if( listeners != null ) {
      for( int i = 0; i < listeners.size(); i++ ) {
        IStreamListener listener = ( IStreamListener )listeners.get( i );
        listener.objectFinished( key, chunk );
      }
    }
  }
  
}
