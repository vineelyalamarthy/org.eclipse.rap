/*******************************************************************************
 * Copyright (c) 2010 EclipseSource and others. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   EclipseSource - initial API and implementation
 ******************************************************************************/

qx.Class.define( "org.eclipse.rwt.test.tests.WidgetStatusHandlerRegistryTest", {
  extend : qx.core.Object,
  
  construct : function() {
    this.base( arguments );
  },
  
  members : {

    testInstantiation : function() {
  	  var registry = org.eclipse.rwt.protocol.widgetStatusHandlerRegistry;
  	  assertNotNull( registry );
  	  var registry2 = org.eclipse.rwt.protocol.widgetStatusHandlerRegistry;
  	  assertIdentical( registry, registry2 );
    },
    
    testAddHandler : function() {
      var registry = org.eclipse.rwt.protocol.widgetStatusHandlerRegistry;
      var sampleHandler = this._sampleHandler;
      var registry2 = org.eclipse.rwt.protocol.widgetStatusHandlerRegistry;
      registry.addHandler( sampleHandler );
      var handler = registry2.getHandler( "org.eclipse.swt.Button" );
      assertIdentical( sampleHandler, handler );
    },
    
    _sampleHandler : {      
      _wasConstructorCalled : false,
      _wasSynchronizeCalled : false,
      _wasListenCalled : false,
      sampleWidget : {
        setUserData : function( key, value ) {
        }
      },
      
      getWidgetType : function() {
        return 'org.eclipse.swt.Button';
      },
      
      createWidget : function( styleArray, parameter ) {
        this._wasConstructorCalled = true;
        return this.sampleWidget;
      },
      
      isControl : function() {
        return true;
      },
      
      synchronizeWidget : function( widget, properties ) {
        this._wasSynchronizeCalled = true;
        this.sampleWidget[ 'properties' ] = properties;
      },
      
      updateListeners : function( widget, shouldListenMap ) {
        this._wasListenCalled = true;
      },
      
      disposeWidget : function( widget ) {
        
      }
      
    },
    
    _sampleWidget : {
      
      // required
      setUserData : function( key, value ) {
      },
      
      // required
      setParent : function( parentWidget ) {
      },
      
      getUserData : function() {
        
      }
      
    }
    
  }
  
}
);