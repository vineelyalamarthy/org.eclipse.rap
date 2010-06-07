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
    }
  
  }
    
      
}


qx.Class.define( "org.eclipse.rwt.test.tests.ButtonStatusHandlerTest", object );