/*******************************************************************************
 * Copyright (c) 2010 EclipseSource and others. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   EclipseSource - initial API and implementation
 ******************************************************************************/

var object =
{
  extend : qx.core.Object,
  
  construct : function() {
    this.base( arguments );
    var registry = org.eclipse.rwt.protocol.widgetStatusHandlerRegistry;
    var handler 
      = registry.getHandler( org.eclipse.rwt.protocol.constants.TYPE_BUTTON );
    this._buttonStatusHandler = handler; 
  },
  
  members : {   
    
    testRegisteringAsHandler : function() {
      var registry = org.eclipse.rwt.protocol.widgetStatusHandlerRegistry;
      var handler = registry.getHandler( org.eclipse.rwt.protocol.constants.TYPE_BUTTON );
      registry.addHandler( handler );
    },
    
    testGetType: function() {
      var type = this._buttonStatusHandler.getWidgetType();
      var expected = org.eclipse.rwt.protocol.constants.TYPE_BUTTON;
      assertEquals( expected, type );
    },
    
    testIsControl : function() {
      assertTrue( this._buttonStatusHandler.isControl() );
    },
    
    testCreatePushButton: function() {
      var handler = this._buttonStatusHandler;
      var style = [ 'PUSH' ];
      var button = handler.createWidget( style );
      assertTrue( typeof button === 'object' );
      var className = button.classname;
      assertEquals( 'org.eclipse.rwt.widgets.Button', className );
      assertEquals( 'push-button', button.getAppearance() );
    },
    
    testCreateToggleButton: function() {
      var handler = this._buttonStatusHandler;
      var style = [ 'TOGGLE' ];
      var button = handler.createWidget( style );
      assertTrue( typeof button === 'object' );
      var className = button.classname;
      assertEquals( 'org.eclipse.rwt.widgets.Button', className );
      assertEquals( 'push-button', button.getAppearance() );
    },
    
    testCreateCheckButton: function() {
      var handler = this._buttonStatusHandler;
      var style = [ 'CHECK' ];
      var button = handler.createWidget( style );
      assertTrue( typeof button === 'object' );
      var className = button.classname;
      assertEquals( 'org.eclipse.rwt.widgets.Button', className );
      assertEquals( 'check-box', button.getAppearance() );
    },
    
    testCreateCheckButton: function() {
      var handler = this._buttonStatusHandler;
      var style = [ 'RADIO' ];
      var button = handler.createWidget( style );
      assertTrue( typeof button === 'object' );
      var className = button.classname;
      assertEquals( 'org.eclipse.rwt.widgets.Button', className );
      assertEquals( 'radio-button', button.getAppearance() );
    },
    
    testHasBorderState: function() {
      var handler = this._buttonStatusHandler;
      var style = [ 'PUSH', 'BORDER' ];
      var button = handler.createWidget( style );
      assertTrue( button.hasState( 'rwt_BORDER') );
    },
    
    testHasPushState: function() {
      var handler = this._buttonStatusHandler;
      var style = [ 'PUSH' ];
      var button = handler.createWidget( style );
      assertTrue( button.hasState( 'rwt_PUSH') );
    },
    
    testHasFlatState: function() {
      var handler = this._buttonStatusHandler;
      var style = [ 'PUSH', 'FLAT' ];
      var button = handler.createWidget( style );
      assertTrue( button.hasState( 'rwt_FLAT') );
    },
    
    testHasToggleState: function() {
      var handler = this._buttonStatusHandler;
      var style = [ 'TOGGLE' ];
      var button = handler.createWidget( style );
      assertTrue( button.hasState( 'rwt_TOGGLE') );
    },
    
    testHasCheckState: function() {
      var handler = this._buttonStatusHandler;
      var style = [ 'CHECK' ];
      var button = handler.createWidget( style );
      assertTrue( button.hasState( 'rwt_CHECK') );
    },
    
    testHasRadioState: function() {
      var handler = this._buttonStatusHandler;
      var style = [ 'RADIO' ];
      var button = handler.createWidget( style );
      assertTrue( button.hasState( 'rwt_RADIO') );
    },
    
    testSyncText : function() {
      var handler = this._buttonStatusHandler;
      var style = [ 'PUSH' ];
      var button = handler.createWidget( style );
      var syncObj = {
          text : 'a caption'  
        };
      handler.synchronizeWidget( button, syncObj );
      assertEquals( 'a caption', button.getCellContent( 2 ) );
    },
    
    testHorizontalChildrenAligment: function() {      
      var handler = this._buttonStatusHandler;
      var style = [ 'PUSH' ];
      var button = handler.createWidget( style );
      var syncObj = {
          horizontalChildrenAlign : 'left'  
        };
      handler.synchronizeWidget( button, syncObj );
      assertEquals( 'left', button.getHorizontalChildrenAlign() );
    },
    
    testSelection: function() {      
      var handler = this._buttonStatusHandler;
      var style = [ 'PUSH' ];
      var button = handler.createWidget( style );
      var syncObj = {
          selection : true  
        };
      handler.synchronizeWidget( button, syncObj );
      assertEquals( true, button._selected );
    },
    
    testGrayed: function() {      
      var handler = this._buttonStatusHandler;
      var style = [ 'CHECK' ];
      var button = handler.createWidget( style );
      var syncObj = {
          grayed : true  
        };
      handler.synchronizeWidget( button, syncObj );
      assertEquals( true, button.hasState( 'grayed' ) );      
    },
    
    testSlectionListener : function() {
      var handler = this._buttonStatusHandler;
      var style = [ 'PUSH' ];
      var button = handler.createWidget( style );
      var syncObj = {
          selectionlistener : true  
        };
      handler.updateListeners( button, syncObj );
      assertEquals( true, button._hasSelectionListener );
    },
    
    testDispose : function() {
      var handler = this._buttonStatusHandler;
      var styles = [ 'PUSH' ];
      var widget = handler.createWidget( styles );
      handler.disposeWidget( widget );
    },
    
    
    
    
  
  }
    
      
}


qx.Class.define( "org.eclipse.rwt.test.tests.ButtonStatusHandlerTest", object );