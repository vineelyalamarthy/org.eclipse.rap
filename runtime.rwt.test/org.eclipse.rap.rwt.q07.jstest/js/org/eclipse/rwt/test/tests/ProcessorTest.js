/*******************************************************************************
 * Copyright (c) 2010 EclipseSource and others. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   EclipseSource - initial API and implementation
 ******************************************************************************/

qx.Class.define( "org.eclipse.rwt.test.tests.ProcessorTest", {
  extend : qx.core.Object,
  
  construct : function() {
    this.base( arguments );
    this.testUtil = org.eclipse.rwt.test.fixture.TestUtil;   
  },
  
  members : {

    testInstantiation : function() {
  	  var processor 
  	  	= new org.eclipse.rwt.protocol.Processor( "{}" );
  	  assertNotNull( processor );
    },
    
    testJson2Parser : function() {
	  var objectString = "{ \"key\" : \"value\"";
	  var fails = false;
	  try {
        var object = JSON.parse( objectString );
	  } catch( error ) {
	    if( error instanceof SyntaxError ) {
		  fails = true;
	    }
	  }
	  assertTrue( fails );
	  objectString = '{ "key" : "value" }';
	  var object = JSON.parse( objectString );
	  assertEquals( "value", object.key );
    },
	
  	testGetRequestCounter : function() {
  	  var processor = this._getProcessor();
  	  var counter = processor.getRequestCounter();
  	  assertEquals( 6, counter );
  	},
  	
  	testGetSettingStore : function() {
  	  var processor = this._getProcessor();
  	  var store = processor.getSettingStore();
  	  assertEquals( 123486875, store );
  	},
  	
    testGetWidgetManager : function() {
      var manager = org.eclipse.rwt.protocol.widgetManager;
      var manager2 = org.eclipse.swt.WidgetManager.getInstance();
      assertIdentical( manager2, manager );
    },
    
    testWasConstructorCalledWhileParsingMessage : function() {
      var registry = org.eclipse.rwt.protocol.widgetStatusHandlerRegistry;
      var sampleHandler = this._sampleHandler;
      registry.addHandler( sampleHandler );
      sampleHandler._wasConstructorCalled = false;
      var processor = this._getProcessor();
      assertTrue( sampleHandler._wasConstructorCalled );
    },
    
    testWasWidgetAddedToWidgetManager : function() {
      var registry = org.eclipse.rwt.protocol.widgetStatusHandlerRegistry;
      var sampleHandler = this._sampleHandler;
      registry.addHandler( sampleHandler );
      sampleHandler._wasConstructorCalled = false;
      var processor = this._getProcessor();
      assertTrue( sampleHandler._wasConstructorCalled );
      var manager = org.eclipse.rwt.protocol.widgetManager;
      var widget = manager.findWidgetById( "w24" );
      var test = org.eclipse.rwt.test.tests.ProtocolProcessorTest;
      assertIdentical( this._sampleHandler.sampleWidget, widget );
    },
    
    testWasWidgetParentAddedToWidget : function() {
      var parentWidget = {
        'text' : 'I am the parent',
        setUserData : function() {
        
        }          
      };
      var manager = org.eclipse.rwt.protocol.widgetManager;
      manager.add( parentWidget, "w22", true );
      var registry = org.eclipse.rwt.protocol.widgetStatusHandlerRegistry;
      var sampleHandler = this._sampleHandler;
      registry.addHandler( sampleHandler );
      var processor = this._getProcessor();      
      var parent = this._sampleHandler.sampleWidget.getParent();
      var expectedParent = manager.findWidgetById( "w22" );      
      assertIdentical( expectedParent, parent );
    },
    
    testAddWidgetsToType : function() {
      var id1 = 'w2';
      var id2 = 'w3';
      var registry = org.eclipse.rwt.protocol.widgetStatusHandlerRegistry;
      registry.addWidgetToType( id1, 'org.eclipse.swt.Button' );      
      var exceptionThrown = false;
      try {
        registry.addWidgetToType( id2, 'org.eclipse.swt.Text' );
      } catch( exc ) {
        exceptionThrown = true;
        registry.addWidgetToType( id2, 'org.eclipse.swt.Button' );
      }
      assertTrue( exceptionThrown );
      var type1 = registry.getTypeForWidget( id1 );
      assertEquals( 'org.eclipse.swt.Button', type1 );
      var type2 = registry.getTypeForWidget( id2 );
      assertEquals( 'org.eclipse.swt.Button', type2 );
    },
    
    testWasSynchronizeCalledWhileParsingMessage : function() {
      var registry = org.eclipse.rwt.protocol.widgetStatusHandlerRegistry;
      var sampleHandler = this._sampleHandler;
      registry.addHandler( sampleHandler );
      sampleHandler._wasSynchronizeCalled = false;
      var processor = this._getProcessor();
      assertTrue( sampleHandler._wasSynchronizeCalled );
    },
    
    testSynchronizeContent : function() {
      var registry = org.eclipse.rwt.protocol.widgetStatusHandlerRegistry;
      var sampleHandler = this._sampleHandler;
      registry.addHandler( sampleHandler );    
      var processor = this._getProcessor();
      var widget = this._sampleHandler.sampleWidget;
      assertEquals( widget.properties.prop1, 'value1' );
      assertEquals( widget.properties.prop2, 'value2' );
    },
    
    testWasListenCalledWhileParsingMessage : function() {
      var registry = org.eclipse.rwt.protocol.widgetStatusHandlerRegistry;
      var sampleHandler = this._sampleHandler;
      registry.addHandler( sampleHandler );
      sampleHandler._wasListenCalled = false;
      var processor = this._getProcessor();
      assertTrue( sampleHandler._wasListenCalled );
    },
    
    testListenContent : function() {
      var registry = org.eclipse.rwt.protocol.widgetStatusHandlerRegistry;
      var sampleHandler = this._sampleHandler;
      registry.addHandler( sampleHandler );    
      var processor = this._getProcessor();
      var widget = this._sampleHandler.sampleWidget;
      assertTrue( widget.listeners.selection );
      assertFalse( widget.listeners.focus );
    },
    
    testWasExecuteCalledWhileParsingMessage : function() {
      var registry = org.eclipse.rwt.protocol.widgetStatusHandlerRegistry;
      var sampleHandler = this._sampleHandler;
      registry.addHandler( sampleHandler );
      var widget = this._sampleHandler.sampleWidget;
      widget._wasExecuteCalled = false;
      var processor = this._getProcessor();
      assertTrue( widget._wasExecuteCalled );
    },
    
    testExecuteContent : function() {
      var registry = org.eclipse.rwt.protocol.widgetStatusHandlerRegistry;
      var sampleHandler = this._sampleHandler;
      registry.addHandler( sampleHandler );    
      var processor = this._getProcessor();
      var widget = this._sampleHandler.sampleWidget;
      assertEquals( 2, widget.executeParams.length );
      assertEquals( 'value1', widget.executeParams[ 0 ] );
      assertEquals( 'value2', widget.executeParams[ 1 ] );
    },
    
    testWasExecuteScriptCalledWhileParsingMessage : function() {
      var registry = org.eclipse.rwt.protocol.widgetStatusHandlerRegistry;
      var sampleHandler = this._sampleHandler;
      registry.addHandler( sampleHandler );
      var widget = this._sampleHandler.sampleWidget;
      widget._wasExecuteScriptCalled = false;
      var processor = this._getProcessor();
      assertTrue( widget._wasExecuteScriptCalled );
    },
    
    testWasDestroyCalledWhileParsingMessage : function() {
      var registry = org.eclipse.rwt.protocol.widgetStatusHandlerRegistry;
      var sampleHandler = this._sampleHandler;
      registry.addHandler( sampleHandler );
      var handler = this._sampleHandler;
      handler._wasDestroyCalled = false;
      var processor = this._getProcessor();
      assertTrue( handler._wasDestroyCalled );
    },
    
    
    // helper
    
    _sampleHandler : {      
      _wasConstructorCalled : false,
      _wasSynchronizeCalled : false,
      _wasListenCalled : false,
      _wasDestroyCalled : false,
      sampleWidget : {
        _wasExecuteCalled : false,
        _wasExecuteScriptCalled : false,
        parent : null,
        
        // required
        setUserData : function( key, value ) {
        },
        
        someName : function( params ) {
          this._wasExecuteCalled = true;
          this[ 'executeParams' ] = params;
        },
        
        methodWhichWillExecutedByExecuteScript : function() {
          this._wasExecuteScriptCalled = true;
        },
        
        // required
        setParent : function( parentWidget ) {
          this.parent = parentWidget;
        },
        
        getParent : function() {
          return this.parent;
        },
        
        getUserData : function() {
          
        }
        
      },
      
      // required
      getWidgetType : function() {
        return 'org.eclipse.swt.Button';
      },
      
      // required
      createWidget : function( styleArray, parameter ) {
        this._wasConstructorCalled = true;
        return this.sampleWidget;
      },
      
      // required
      isControl : function() {
        return true;
      },
      
      // required
      synchronizeWidget : function( widget, properties ) {
        this._wasSynchronizeCalled = true;
        this.sampleWidget[ 'properties' ] = properties;
      },
      
      // required
      updateListeners : function( widget, shouldListenMap ) {
        this._wasListenCalled = true;
        this.sampleWidget[ 'listeners' ] = shouldListenMap;
      },
      
      // required
      disposeWidget : function( widget ) {
        this._wasDestroyCalled = true;
      }
      
    },
    
    _sampleMessage : '{"meta":{"settingStore":123486875,"requestCounter":6' +
      ',"tabId":4},"device":{"id":"w1","cursorLocation":' +
      '[214,544],"focusControl":"w34"},"widgets":[{"widgetId":' +
      '"w23","type":"synchronize","payload":{"prop1":"value1",' +
      '"prop2":"value2"}},{"widgetId":"w22","type":' +
      '"multiSynchonize","payload":{"widgets":["w33","w44",' +
      '"w90","w31"],"prop1":"value1","prop2":"value2"}},' +
      '{"widgetId":"w24","type":"listen","payload":{"selection"' +
      ':true,"focus":false}},{"widgetId":"w24","type":' +
      '"construct","payload":{"parent":"w22","widgetType":'+
      '"org.eclipse.swt.Button","style":["BORDER","FLAT"],' +
      '"parameter":["eins",2,true]}},{"widgetId":' +
      '"w24","type":"synchronize","payload":{"prop1":"value1",' +
      '"prop2":"value2"}},{"widgetId":"w25","type":' +
      '"fireEvent","payload":{"event":"focus-in"}},{"widgetId":' +
      '"w24","type":"execute","payload":{"method":"someName",' +
      '"parameter":["value1","value2"]}},{"widgetId":"w44",' +
      '"type":"executeScript","payload":{"scriptType":' +
      '"text/javascript","script":"var w = org.eclipse.swt.WidgetManager.' +
      'getInstance().findWidgetById( \'w24\' ); if(w!==undefined)' +
      '{w.methodWhichWillExecutedByExecuteScript();}"}},{"widgetId":' +
      '"w24","type":"destroy","payload":null}]}',
  
      
  	_getProcessor : function() {
  	  var processor 
          = new org.eclipse.rwt.protocol.Processor( this._sampleMessage );
  	  return processor;
  	}
    
  }
  
}
);