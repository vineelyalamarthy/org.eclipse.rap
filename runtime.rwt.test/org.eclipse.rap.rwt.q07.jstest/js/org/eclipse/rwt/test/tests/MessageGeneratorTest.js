/*******************************************************************************
 * Copyright (c) 2010 EclipseSource and others. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   EclipseSource - initial API and implementation
 ******************************************************************************/

var object = {
  extend : qx.core.Object,
  
  construct : function() {
    this.base( arguments );

  },
  
  members : {
    
    testInstantiation : function() {
      var generator = new org.eclipse.rwt.protocol.MessageGenerator();
      assertNotNull( generator );
      var message = generator.finish();
      JSON.parse( message );      
    },
    
    testAppendMeta : function() {
      var generator = new org.eclipse.rwt.protocol.MessageGenerator();
      generator.appendMeta( 'requestCounter', 23 );
      generator.appendMeta( 'settingStore', 1234567 );
      var message = generator.finish();
      var obj = JSON.parse( message );
      assertEquals( obj.meta.requestCounter, 23 );
      assertEquals( obj.meta.settingStore, 1234567 );
    },
    
    testAppendDevice : function() {
      var generator = new org.eclipse.rwt.protocol.MessageGenerator();
      generator.appendDevice( 'uiRoot', 'w1' );
      generator.appendDevice( 'focusControl', 'w23' );
      generator.appendDevice( 'mouseCoordinates', [ 2, 5 ] );
      var message = generator.finish();
      var obj = JSON.parse( message );
      assertEquals( obj.device.uiRoot, 'w1' );
      assertEquals( obj.device.focusControl, 'w23' );
      assertEquals( obj.device.mouseCoordinates[ 0 ], 2 );
      assertEquals( obj.device.mouseCoordinates[ 1 ], 5 );
    },
    
    testAppendMetaAndDevice : function() {
      var generator = new org.eclipse.rwt.protocol.MessageGenerator();
      generator.appendMeta( 'requestCounter', 23 );
      generator.appendMeta( 'settingStore', 1234567 );
      generator.appendDevice( 'uiRoot', 'w1' );
      generator.appendDevice( 'focusControl', 'w23' );
      generator.appendDevice( 'mouseCoordinates', [ 2, 5 ] );
      var message = generator.finish();
      var obj = JSON.parse( message );
      assertEquals( obj.meta.requestCounter, 23 );
      assertEquals( obj.meta.settingStore, 1234567 );
      assertEquals( obj.device.uiRoot, 'w1' );
      assertEquals( obj.device.focusControl, 'w23' );
      assertEquals( obj.device.mouseCoordinates[ 0 ], 2 );
      assertEquals( obj.device.mouseCoordinates[ 1 ], 5 );
    },
    
    testAppendSynchronize : function() {
      var generator = new org.eclipse.rwt.protocol.MessageGenerator();
      generator.appendSynchronize( 'w23', 'key', 'value' );
      generator.appendSynchronize( 'w24', 'key', [ 1, 2 ] );
      generator.appendSynchronize( 'w24', 'key2', 5 );
      var message = generator.finish();
      var obj = JSON.parse( message );
      var constants = org.eclipse.rwt.protocol.constants;
      assertEquals( obj.widgets[ 0 ].type, constants.PAYLOAD_SYNCHRONIZE );
      assertEquals( obj.widgets[ 0 ].payload.key, 'value' );
      assertEquals( obj.widgets[ 1 ].widgetId, 'w24' );
      assertEquals( obj.widgets[ 1 ].payload.key[ 0 ], 1 );
      assertEquals( obj.widgets[ 1 ].payload.key[ 1 ], 2 );
      assertEquals( obj.widgets[ 1 ].payload.key2, 5 );
    },
    
    testAppenEvent : function() {
      var generator = new org.eclipse.rwt.protocol.MessageGenerator();
      var eventType = 'org.swt.selection';
      var eventType2 = 'org.swt.focus';
      generator.appendEvent( 'w33', eventType );
      generator.appendEvent( 'w33', eventType2 );
      var message = generator.finish();
      var obj = JSON.parse( message );
      var constants = org.eclipse.rwt.protocol.constants;
      assertEquals( obj.widgets[ 0 ].type, constants.PAYLOAD_FIRE_EVENT );
      assertEquals( obj.widgets[ 0 ].payload[ constants.KEY_EVENT ], 
                    eventType );
      assertEquals( obj.widgets[ 1 ].payload[ constants.KEY_EVENT ], 
                    eventType2 );
    },
    
    testAppendMixedContent : function() {
      var constants = org.eclipse.rwt.protocol.constants;
      var generator = new org.eclipse.rwt.protocol.MessageGenerator();
      generator.appendMeta( 'requestCounter', 23 );
      generator.appendMeta( 'settingStore', 1234567 );
      generator.appendDevice( 'uiRoot', 'w1' );
      generator.appendDevice( 'focusControl', 'w23' );
      generator.appendDevice( 'mouseCoordinates', [ 2, 5 ] );
      generator.appendSynchronize( 'w23', 'key', 'value' );
      generator.appendSynchronize( 'w24', 'key', [ 1, 2 ] );
      generator.appendSynchronize( 'w24', 'key2', 5 );
      var eventType = 'org.swt.selection';
      var eventType2 = 'org.swt.focus';
      generator.appendEvent( 'w33', eventType );
      generator.appendEvent( 'w33', eventType2 );
      generator.appendSynchronize( 'w33', 'key3', 'aValue' );      
      var message = generator.finish();
      var obj = JSON.parse( message );
      assertEquals( obj.meta.requestCounter, 23 );
      assertEquals( obj.meta.settingStore, 1234567 );
      assertEquals( obj.device.uiRoot, 'w1' );
      assertEquals( obj.device.focusControl, 'w23' );
      assertEquals( obj.device.mouseCoordinates[ 0 ], 2 );
      assertEquals( obj.device.mouseCoordinates[ 1 ], 5 );
      assertEquals( obj.widgets[ 0 ].type, constants.PAYLOAD_SYNCHRONIZE );
      assertEquals( obj.widgets[ 0 ].payload.key, 'value' );
      assertEquals( obj.widgets[ 1 ].widgetId, 'w24' );
      assertEquals( obj.widgets[ 1 ].payload.key[ 0 ], 1 );
      assertEquals( obj.widgets[ 1 ].payload.key[ 1 ], 2 );
      assertEquals( obj.widgets[ 1 ].payload.key2, 5 );
      assertEquals( obj.widgets[ 2 ].type, constants.PAYLOAD_FIRE_EVENT );
      assertEquals( obj.widgets[ 2 ].payload[ constants.KEY_EVENT ], 
                    eventType );
      assertEquals( obj.widgets[ 3 ].payload[ constants.KEY_EVENT ], 
                    eventType2 );
      assertEquals( obj.widgets[ 4 ].payload.key3, 'aValue' );
    }
     
  }     
  
}

qx.Class.define( "org.eclipse.rwt.test.tests.MessageGeneratorTest", object );