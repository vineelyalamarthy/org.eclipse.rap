/******************************************************************************* 
* Copyright (c) 2010 EclipseSource and others. All rights reserved. This
* program and the accompanying materials are made available under the terms of
* the Eclipse Public License v1.0 which accompanies this distribution, and is
* available at http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*   EclipseSource - initial API and implementation
*******************************************************************************/ 
package org.eclipse.rap.rwt.protocol;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.eclipse.rwt.internal.protocol.Chunk;
import org.eclipse.rwt.internal.protocol.IProtocolConstants;
import org.eclipse.rwt.internal.protocol.IStreamListener;
import org.eclipse.rwt.internal.protocol.Processor;


public class Processor_Test extends TestCase {

  private List parsedKeys;
  private List parsedObjects;
  
  protected void setUp() throws Exception {
    parsedKeys = new ArrayList();
    parsedObjects = new ArrayList();
  }
  
  public void testSimpleMessage() {
    String message = "{ 'meta' : { 'key' : 24 } }";
    Processor processor = new Processor( message );
    hookStreamListener( processor );
    processor.parse();
    assertEquals( "meta", parsedKeys.get( 0 ) );
    Chunk chunk = ( Chunk )parsedObjects.get( 0 );    
    assertEquals( new Integer( 24 ), chunk.getValue( "key" ) );
  }
  
  public void testMessageWithObject() {
    String message = "{ 'meta' : { 'key' : 23, 'key2' : 'a string' } }";
    Processor processor = new Processor( message );
    hookStreamListener( processor );
    processor.parse();
    assertEquals( "meta", parsedKeys.get( 0 ) );
    Chunk chunk = ( Chunk )parsedObjects.get( 0 );    
    assertEquals( new Integer( 23 ), chunk.getValue( "key" ) );  
    assertEquals( "a string", chunk
                  .getValue( "key2" ) );
  }
  
  public void testMessageWithTwoObjects() {
    String message = "{ 'meta' : { 'key' : 23 }, 'display' : " +
    		         "{ 'key2' : 'a string' } }";
    Processor processor = new Processor( message );
    hookStreamListener( processor );
    processor.parse();
    assertEquals( "meta", parsedKeys.get( 1 ) );
    Chunk chunk = ( Chunk )parsedObjects.get( 1 );    
    assertEquals( new Integer( 23 ), chunk.getValue( "key" ) );
    chunk = ( Chunk )parsedObjects.get( 0 ); 
    assertEquals( "display", parsedKeys.get( 0 ) );
    assertEquals( "a string", chunk.getValue( "key2" ) );
  }  
  
  public void testMessageWithArray() {
    String message = "{ 'key' : [ { 'foo' : 'bar' }, { 'foo2' : 'bar' } ] }";
    Processor processor = new Processor( message );
    hookStreamListener( processor );
    processor.parse();
    String key = ( String )parsedKeys.get( 0 );
    assertEquals( "key", key );
    Chunk chunk = ( Chunk )parsedObjects.get( 0 );    
    Object value = chunk.getValue( "foo" );
    assertEquals( "bar", value );
    chunk = ( Chunk )parsedObjects.get( 1 );    
    value = chunk.getValue( "foo2" );
    assertEquals( "bar", value );
  }
  
  public void testComplexMessage() {
    String message 
      = "{'meta':{'settingStore':123486875,'requestCounter':6" +
      ",'tabId':4},'device':{'id':'w1','cursorLocation':" +
      "[214,544],'focusControl':'w34'},'widgets':[{'widgetId':" +
      "'w23','type':'synchronize','payload':{'prop1':'value1'," +
      "'prop2':'value2'}}]}";
    Processor processor = new Processor( message );
    hookStreamListener( processor );
    processor.parse();
    int indexOf = parsedKeys.indexOf( "widgets" );
    Chunk chunk = ( Chunk )parsedObjects.get( indexOf );
    String type = ( String )chunk.getValue( "type" );
    assertEquals( IProtocolConstants.PAYLOAD_SYNCHRONIZE, type );
    Chunk payload 
      = ( Chunk )chunk.getValue( IProtocolConstants.WIDGETS_PAYLOAD );
    assertEquals( "value1", payload.getValue( "prop1" ) );
    assertEquals( "value2", payload.getValue( "prop2" ) );
  }
    
  private void hookStreamListener( final Processor processor ) {
    processor.addStreamListener( new IStreamListener() {      
      public void objectFinished( final String key, final Object object ) {
        parsedKeys.add( key );
        parsedObjects.add( object );
      }
    } );
  }
    
}
