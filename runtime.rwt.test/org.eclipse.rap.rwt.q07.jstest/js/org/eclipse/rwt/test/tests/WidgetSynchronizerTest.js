/*******************************************************************************
 * Copyright (c) 2010 EclipseSource and others. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   EclipseSource - initial API and implementation
 ******************************************************************************/

qx.Class.define( "org.eclipse.rwt.test.tests.WidgetSynchronizerTest", {
  extend : qx.core.Object,
  
  construct : function() {
    this.base( arguments );
    this.testUtil = org.eclipse.rwt.test.fixture.TestUtil;   
  },
  
  members : {
    
    testInstantiation : function() {
      var synchronizer = org.eclipse.rwt.protocol.widgetSynchronizer;
      assertTrue( synchronizer !== undefined );
    },
    
    testFireEvent : function() {
      var synchronizer = org.eclipse.rwt.protocol.widgetSynchronizer;
      var req = org.eclipse.swt.Request.getInstance();
      req._messageGenerator = new org.eclipse.rwt.protocol.MessageGenerator();
      var gen = req.getMessageGenerator();
      var wm = org.eclipse.swt.WidgetManager.getInstance();
      wm.add( this._sampleWidget, 'w33', true );
      synchronizer.fireEvent( this._sampleWidget, 'org.swt.selection' );
      var message = gen.finish();
      var obj = JSON.parse( message );
      assertEquals( obj.widgets[ 0 ].payload.event, 'org.swt.selection' );
    },
    
    testSetWidgetProperty : function() {
      var synchronizer = org.eclipse.rwt.protocol.widgetSynchronizer;
      var wm = org.eclipse.swt.WidgetManager.getInstance();
      wm.add( this._sampleWidget, 'w33', true );
      var req = org.eclipse.swt.Request.getInstance();
      req._messageGenerator = new org.eclipse.rwt.protocol.MessageGenerator();
      var gen = req.getMessageGenerator();
      synchronizer.setWidgetProperty( this._sampleWidget, 'key', 'value' );
      var message = gen.finish();
      var obj = JSON.parse( message );
      assertEquals( obj.widgets[ 0 ].payload.key, 'value' );
    },
    
    testSetDeviceProperty : function() {
      var synchronizer = org.eclipse.rwt.protocol.widgetSynchronizer;
      var req = org.eclipse.swt.Request.getInstance();
      req._messageGenerator = new org.eclipse.rwt.protocol.MessageGenerator();
      var gen = req.getMessageGenerator();
      synchronizer.setDeviceProperty( 'key', 'value' );
      var message = gen.finish();
      var obj = JSON.parse( message );
      assertEquals( obj.device.key, 'value' );
    },
    
    testSetDeviceProperty : function() {
      var synchronizer = org.eclipse.rwt.protocol.widgetSynchronizer;
      var req = org.eclipse.swt.Request.getInstance();
      req._messageGenerator = new org.eclipse.rwt.protocol.MessageGenerator();
      var gen = req.getMessageGenerator();
      synchronizer.setMetaProperty( 'key', 'value' );
      var message = gen.finish();
      var obj = JSON.parse( message );
      assertEquals( obj.meta.key, 'value' );
    },
    
    _sampleWidget : {
      id : '',
      // required
      setUserData : function( key, value ) {
        if( key === 'id' ) {
          this.is = value;
        }
      },
      
      // required
      setParent : function( parentWidget ) {
      },
      
      getUserData : function( key ) {
        if( key === 'id' ) {
          return this.id;
        }
      }
      
    }
    
  }
  
}
);