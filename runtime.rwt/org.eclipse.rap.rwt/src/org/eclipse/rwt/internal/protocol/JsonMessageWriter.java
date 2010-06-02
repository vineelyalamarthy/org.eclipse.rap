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

import java.io.PrintWriter;
import java.util.Map;


public class JsonMessageWriter extends ProtocolMessageWriter {

  public JsonMessageWriter( final PrintWriter decoratedWriter ) {
    super( decoratedWriter );    
  }
  
  protected void appendStartMessage() {     
    String message = "{\n  \""+ IProtocolConstants.MESSAGE_META + "\" : {\n" +
    		         "    \"" + IProtocolConstants.META_REQUEST_COUNTER + 
    		         // TODO: include real next request id
//    		         "\" : " + RWTRequestVersionControl.nextRequestId() +
    		         "\" : " + 0 +
                     "\n  },\n" +
    		         "  \"" + IProtocolConstants.MESSAGE_WIDGETS + "\" :" +
    		         "  [\n";
    write( message );
  }

  protected void appendEndMessage() {
    write( "\n  ]\n}" );
  }
  
  protected void appendStartPayload( final String widgetId ) {
    String payloadStart = null;
    if( widgetId != null ) {
      payloadStart = "    {\n      \"" + IProtocolConstants.WIDGETS_ID 
                     + "\" : \"" 
                     + widgetId 
                     + "\",\n";
    } else {
      payloadStart = "    {\n      \"" + IProtocolConstants.WIDGETS_ID 
                     + "\":null,";
    }
    write( payloadStart );
  }
  
  protected void appendPayloadSeparator() {
    write( ",\n" );
  }

  protected void appendEndPayload() {
    write( "\n    }" );
  }

  protected void appendConstructPayload( String widgetId,
                                         final String parentId,
                                         final String type,
                                         final String[] styles, 
                                         final Object[] arguments )
  {
    StringBuffer buffer = new StringBuffer();
    buffer.append( "      \"" + IProtocolConstants.WIDGETS_TYPE );
    buffer.append( "\" : \"" + IProtocolConstants.PAYLOAD_CONSTRUCT + "\",\n" );
    buffer.append( "      \"" + IProtocolConstants.WIDGETS_PAYLOAD );
    buffer.append( "\" : { " );
    if( parentId != null ) {
      buffer.append( "\"" + IProtocolConstants.KEY_PARENT_ID + "\" : \"" );
      buffer.append( parentId + "\", " );
    }
    buffer.append( "\"" + IProtocolConstants.KEY_WIDGET_TYPE + "\" : \"" );
    buffer.append( type + "\", "  );
    buffer.append( "\"" + IProtocolConstants.KEY_WIDGET_STYLE + "\" : " );
    buffer.append( getStyleArray( styles ) + ", " );
    buffer.append( "\"" + IProtocolConstants.KEY_PARAMETER_LIST + "\" : " );
    if( arguments == null ) {
      buffer.append( "null" );
    } else {
      buffer.append( "[ " );
      for( int i = 0; i < arguments.length; i++ ) {
        addValueSeparatorIfNeeded( buffer, i );
        buffer.append( getJsonValueOfObject( arguments[ i ] ) );
      }
      buffer.append( " ]" );
    }
    buffer.append( " }" );
    write( buffer.toString() );
  }
  
  private String getStyleArray( final String[] styles ) {
    StringBuffer result = new StringBuffer();
    if( styles != null && styles.length > 0 ) {
      result.append( "[ " );
      for( int i = 0; i < styles.length; i++ ) {
        addValueSeparatorIfNeeded( result, i );
        result.append( "\"" + styles[ i ] + "\"" );
      }
      result.append( " ]" );
    } else {
      result.append( "null" );
    }
    return result.toString();
  }

  protected void appendDestroyPayload() {
    StringBuffer buffer = new StringBuffer();
    buffer.append( "      \"" + IProtocolConstants.WIDGETS_TYPE + "\" : " );
    buffer.append( "\"" + IProtocolConstants.PAYLOAD_DESTROY + "\",\n" );
    buffer.append( "      \"" + IProtocolConstants.WIDGETS_PAYLOAD + "\" : " );
    buffer.append( " null" );
    write( buffer.toString() );    
  }

  protected void appendSynchronizePayload( final Map properties ) {
    StringBuffer buffer = new StringBuffer();
    buffer.append( "      \"" + IProtocolConstants.WIDGETS_TYPE + "\" : " );
    buffer.append( "\"" + IProtocolConstants.PAYLOAD_SYNCHRONIZE + "\",\n" );
    buffer.append( "      \"" + IProtocolConstants.WIDGETS_PAYLOAD );
    buffer.append( "\" : { " );
    handleProperties( properties, buffer );
    buffer.append( " }" );
    write( buffer.toString() );
  }
  
  protected void appendMultipleSynchronizePayload( final String[] widgetIds,
                                                   final Map properties )
  {
    StringBuffer buffer = new StringBuffer();
    buffer.append( "      \"" + IProtocolConstants.WIDGETS_TYPE + "\" : " );
    buffer.append( "\"" + IProtocolConstants.PAYLOAD_MULTI_SYNCHRONIZE + "\"" );
    buffer.append( ",\n      \"" + IProtocolConstants.WIDGETS_PAYLOAD );
    buffer.append( "\" : {" );
    handleProperties( properties, buffer );
    buffer.append( ", \"" + IProtocolConstants.KEY_WIDGETS + "\" : " );
    if( widgetIds.length > 0 ) {
      buffer.append( "[ " );
      for( int i = 0; i < widgetIds.length; i++ ) {
        addValueSeparatorIfNeeded( buffer, i );
        buffer.append( "\"" + widgetIds[ i ] + "\"" );
      }
      buffer.append( " ]" );
    } else {
      throw new IllegalArgumentException( "widgets must not be null" );
    }   
    buffer.append( " }" );
    write( buffer.toString() );
  }

  protected void appendListenPayload( final Map listeners ) {
    StringBuffer buffer = new StringBuffer();
    buffer.append( "      \"" + IProtocolConstants.WIDGETS_TYPE + "\" : " );
    buffer.append( "\"" + IProtocolConstants.PAYLOAD_LISTEN + "\",\n" );
    buffer.append( "      \"" + IProtocolConstants.WIDGETS_PAYLOAD );
    buffer.append( "\" : { " );
    handleProperties( listeners, buffer );
    buffer.append( " }" );
    write( buffer.toString() );
  }
  
  private void handleProperties( final Map properties, 
                                 final StringBuffer buffer ) 
  {
    Object[] keys = properties.keySet().toArray();
    for( int i = 0; i < keys.length; i++ ) {
      addValueSeparatorIfNeeded( buffer, i );
      String key = ( String )keys[ i ];
      buffer.append( "\"" + key + "\" : " );
      buffer.append( getJsonValueOfObject( properties.get( key ) ) );
    }
  }

  protected void appendEventPayload( final String event ) {
    StringBuffer buffer = new StringBuffer();
    buffer.append( "      \"" + IProtocolConstants.WIDGETS_TYPE + "\" : " );
    buffer.append( "\"" + IProtocolConstants.PAYLOAD_FIRE_EVENT + "\",\n" );
    buffer.append( "      \"" + IProtocolConstants.WIDGETS_PAYLOAD );
    buffer.append( "\" : { " );
    buffer.append( "\"" + IProtocolConstants.KEY_EVENT + "\" : " );
    buffer.append( "\"" + event + "\" }" );
    write( buffer.toString() );
  }
  
  protected void appendExecutePayload( final String methodName,
                                       final Object[] arguments )
 {
   StringBuffer buffer = new StringBuffer();
   buffer.append( "      \"" + IProtocolConstants.WIDGETS_TYPE + "\" : " );
   buffer.append( "\"" + IProtocolConstants.PAYLOAD_EXECUTE + "\",\n" );
   buffer.append( "      \"" + IProtocolConstants.WIDGETS_PAYLOAD + "\" : { " );
   buffer.append( "\"" + IProtocolConstants.KEY_METHODNAME + "\" : \"" );
   buffer.append( methodName );
   buffer.append( "\", \"" + IProtocolConstants.KEY_PARAMETER_LIST + "\" : " );
   if( arguments == null || arguments.length <= 0 ) {
     buffer.append( "null" );
   } else {
     buffer.append( "[ " );
     for( int i = 0; i < arguments.length; i++ ) {
       addValueSeparatorIfNeeded( buffer, i );
       buffer.append( getJsonValueOfObject( arguments[ i ] ) );
     }
     buffer.append( " ]" );
   }
   
   buffer.append( " }" );
   write( buffer.toString() );
 }

  protected void appendExecuteScript( final String scriptType, 
                                      final String script ) 
  {
    StringBuffer buffer = new StringBuffer();
    buffer.append( "      \"" + IProtocolConstants.WIDGETS_TYPE + "\" : " );
    buffer.append( "\"" + IProtocolConstants.PAYLOAD_EXECUTE_SCRIPT + "\",\n" );
    buffer.append( "      \"" + IProtocolConstants.WIDGETS_PAYLOAD );
    buffer.append( "\" : { " );
    buffer.append( "\"" + IProtocolConstants.KEY_SCRIPT_TYPE + "\" : " );
    buffer.append( "\"" + scriptType + "\", " );
    buffer.append( "\"" + IProtocolConstants.KEY_SCRIPT + "\" : " );
    buffer.append( "\"" + script + "\" }" );
    write( buffer.toString() );
  }
  
  private String getJsonValueOfObject( final Object object ) {
    String result = null;
    if( object == null ) {
      result = "null";
    } else if( object instanceof String ) {
      result = "\"" + object.toString() + "\"";
    } else if( isObjectJsonPrimitive( object ) ){
      result = object.toString();
    } else {
      String message = "Parameter object can not be converted to JSON Value";
      throw new IllegalArgumentException( message );
    }
    return result;    
  }
  
  private boolean isObjectJsonPrimitive( final Object object ) {
    boolean result = false;
    if(    object instanceof Integer 
        || object instanceof Double 
        || object instanceof Boolean ) {
      result = true;
    }
    return result;
  }

  private void addValueSeparatorIfNeeded( final StringBuffer buffer, 
                                          final int propertyCount ) 
  {
    if( propertyCount > 0 ) {
      buffer.append( ", " );
    }
  }

  protected void appendPayloadType( final String payloadType ) {
    StringBuffer buffer = new StringBuffer();
    buffer.append( "      \"" + IProtocolConstants.WIDGETS_TYPE + "\" : " );
    buffer.append( "\"" + payloadType + "\",\n" );
    buffer.append( "      \"" + IProtocolConstants.WIDGETS_PAYLOAD );
    buffer.append( "\" : { " );
    write( buffer.toString() );
  }

  protected void appendPayloadValue( final Object key, final Object value ) {
    if( !( key instanceof String ) ) {
      throw new IllegalArgumentException( "JSON keys must be Strings" );
    }
    StringBuffer buffer = new StringBuffer();
    buffer.append( "\"" + key + "\" : " );
    if( value instanceof Object[] ) {
      handleJsonArray( value, buffer );
    } else {
      buffer.append( getJsonValueOfObject( value ) );
    }
    write( buffer.toString() );
  }

  private void handleJsonArray( final Object value, 
                                final StringBuffer buffer ) 
  {
    Object[] values = ( Object[] )value;
    if( values.length > 0 ) {
      buffer.append( "[ " );
      for( int i = 0; i < values.length; i++ ) {   
        addValueSeparatorIfNeeded( buffer, i );
        buffer.append( getJsonValueOfObject( values[ i ] ) );
      } 
      buffer.append( " ]" );
    }
  }

  protected void appendPayloadValueSeparator() {
    write( ", ");
  }

  protected void appendEndPayloadValue() {
    write( " }" );
  }

}
