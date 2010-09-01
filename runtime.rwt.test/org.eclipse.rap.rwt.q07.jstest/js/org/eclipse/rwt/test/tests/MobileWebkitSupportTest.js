/*******************************************************************************
 * Copyright (c) 2010 EclipseSource and others. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   EclipseSource - initial API and implementation
 ******************************************************************************/

qx.Class.define( "org.eclipse.rwt.test.tests.MobileWebkitSupportTest", {

  extend : qx.core.Object,

  construct : function() {
    // Eventlistener are detached by TestRunner to prevent user-interference,
    // but we need them here...
    qx.event.handler.EventHandler.getInstance().attachEvents();
  },
  
  destruct : function() {
    qx.event.handler.EventHandler.getInstance().detachEvents();
  },

  members : {

    TARGETENGINE : [ "webkit" ],
    TARGETPLATFORM : [ "iphone", "ipad" ],
    
    ///////////////
    // Test helpers
    
    testFakeTouchEvents : function() {
      var div = document.createElement( "div" );
      document.body.appendChild( div );
      var log = [];
      var logger = function( event ) {
        log.push( event.type );
      }
      div.ontouchstart = logger;  
      div.ontouchmove = logger;
      div.ontouchend = logger;
      div.ontouchcancel = logger;
      this.touch( div, "touchstart" );
      this.touch( div, "touchmove" );
      this.touch( div, "touchend" );
      this.touch( div, "touchcancel" );
      var expected = [ "touchstart", "touchmove", "touchend", "touchcancel" ];
      assertEquals( expected, log );
      document.body.removeChild( div );
    },
    
    testFakeTouchEventsTargets : function() {
      var div = document.createElement( "div" );
      document.body.appendChild( div );
      var log = [];
      var logger = function( event ) {
        log.push( event.target );
      }
      div.ontouchstart = logger;  
      div.ontouchmove = logger;
      div.ontouchend = logger;
      div.ontouchcancel = logger;
      this.touch( div, "touchstart" );
      this.touch( div, "touchmove" );
      this.touch( div, "touchend" );
      this.touch( div, "touchcancel" );
      var expected = [ div, div, div, div ];
      assertEquals( expected, log );
      document.body.removeChild( div );
    },

    testCreateTouch : function() {
      var div = document.createElement( "div" );
      var touch = this.createTouch( div, 3, 6 );
      assertTrue( touch instanceof Touch );
      // Does not work due to browser:
      //assertEquals( 3, touch.screenX );
      //assertEquals( 6, touch.screenY );
      //assertIdentical( div, touch.target );
    },

    testCreateTouchList : function() {
      var touches = [ new Touch(), new Touch() ];
      var list = this.createTouchList( touches );
      assertTrue( list instanceof TouchList );
      assertEquals( 2, list.length );
      assertIdentical( touches[ 0 ], list.item( 0 ) );
      assertIdentical( touches[ 1 ], list.item( 1 ) );
    },

    testCreateTouchEvent : function() {
      var list = this.createTouchList( [] );
      var event = this.createTouchEvent( "touchstart", list );
      assertEquals( "touchstart", event.type );
      assertIdentical( list, event.touches );
    },

    testFakeTouchEventsTouchNumber : function() {
      var div = document.createElement( "div" );
      document.body.appendChild( div );
      var log = [];
      var logger = function( event ) {
        log.push( event.touches.length );
      }
      div.ontouchstart = logger;  
      div.ontouchmove = logger;
      div.ontouchend = logger;
      div.ontouchcancel = logger;
      this.touch( div, "touchstart", 1 );
      this.touch( div, "touchmove", 1 );
      this.touch( div, "touchend", 3 );
      this.touch( div, "touchcancel",4  );
      var expected = [ 1, 1, 3, 4 ];
      assertEquals( expected, log );
      document.body.removeChild( div );
    },

    testFakeTouchEventsTouchList : function() {
      var div = document.createElement( "div" );
      var touches = [ 
        this.createTouch( div, 1, 1 ), 
        this.createTouch( div, 1, 1 )
      ]; 
      document.body.appendChild( div );
      var log = [];
      var logger = function( event ) {
        log.push( event.touches );
      }
      div.ontouchstart = logger;  
      div.ontouchmove = logger;
      div.ontouchend = logger;
      div.ontouchcancel = logger;
      this.touch( div, "touchstart", touches );
      assertEquals( 2, log[ 0 ].length );
      assertIdentical( touches[ 0 ], log[ 0 ].item( 0 ) );
      assertIdentical( touches[ 1 ], log[ 0 ].item( 1 ) );
      document.body.removeChild( div );
    },

    testFakeGestureEvent : function() {
      var div = document.createElement( "div" );
      var touches = [ new Touch(), new Touch() ];
      document.body.appendChild( div );
      var log = [];
      var logger = function( event ) {
        log.push( event.type );
      }
      div.ontouchstart = logger;  
      div.ontouchmove = logger;
      div.ontouchend = logger;
      div.ontouchcancel = logger;
      div.ongesturestart = logger;
      div.ongesturechange = logger;
      div.ongestureend = logger;
      this.gesture( div, "gesturestart" );
      this.gesture( div, "gesturechange" );
      this.gesture( div, "gestureend" );
      var expected = [
        "touchstart",
        "gesturestart",
        "touchstart",
        "gesturechange",
        "touchmove",
        "gestureend",
        "touchend",
      ];
      assertEquals( expected, log );
      document.body.removeChild( div );
    },

    testFakeGestureEventTouche : function() {
      var div = document.createElement( "div" );
      var touches = [ new Touch(), new Touch() ];
      document.body.appendChild( div );
      var log = [];
      var logger = function( event ) {
        log.push( event.touches.length );
      }
      div.ontouchstart = logger;  
      div.ontouchmove = logger;
      div.ontouchend = logger;
      this.gesture( div, "gesturestart" );
      this.gesture( div, "gesturechange" );
      this.gesture( div, "gestureend" );
      var expected = [ 1, 2, 2, 2 ];
      assertEquals( expected, log );
      document.body.removeChild( div );
    },

    //////////
    // Visuals

    testTabHighlightHidden : function() {
      var head = document.childNodes[ 0 ].childNodes[ 0 ];
      var headertext = head.innerHTML;
      var expected = "* { -webkit-tap-highlight-color: rgba(0,0,0,0); }";
      assertTrue( headertext.indexOf( expected ) != -1 );
    },
    
    //////////
    // Widgets
    
    // See Bug 323803 -  [ipad] Browser-widget/iframe broken  
    testIFrameDimensionBug : function() {
      var testUtil = org.eclipse.rwt.test.fixture.TestUtil;
      var iframe = new qx.ui.embed.Iframe();
      iframe.addToDocument();
      iframe.setWidth( 300 );
      iframe.setHeight( 400 );
      testUtil.flush();
      var node = iframe.getIframeNode();
      var widgetNode = iframe.getElement();
      assertEquals( 300, parseInt( widgetNode.style.width ) );
      assertEquals( 400, parseInt( widgetNode.style.height ) );
      assertEquals( "", node.width );
      assertEquals( "", node.height );
      assertEquals( "", node.style.width );
      assertEquals( "", node.style.height );
      assertEquals( "300px", node.style.minWidth );
      assertEquals( "400px", node.style.minHeight );
      assertEquals( "300px", node.style.maxWidth );
      assertEquals( "400px", node.style.maxHeight );
    },
    
    testTextFocus : function() {
      var testUtil = org.eclipse.rwt.test.fixture.TestUtil;
      var text = new org.eclipse.rwt.widgets.Text( false );
      org.eclipse.swt.TextUtil.initialize( text );
      text.addToDocument();
      testUtil.flush();
      assertFalse( text.isFocused() );
      var node = text._inputElement;
      this.touch( node, "touchstart" );
      var over = false;
      text.addEventListener( "mouseover", function(){
        over = true;
      } );
      testUtil.fakeMouseEventDOM( node, "mouseover", 1, 0, 0, 0, true );
      if( !over ) {
        // the ipad will only send a mousedown if mouseover is not processed
        testUtil.fakeMouseEventDOM( node, "mousedown", 1, 0, 0, 0, true );
      }
      assertTrue( text.isFocused() );
    },
    

    /////////
    // Events
    
    testPreventNativeMouseEvents : function() {
      var testUtil = org.eclipse.rwt.test.fixture.TestUtil;
      var widget = new qx.ui.basic.Terminator();
      widget.addToDocument();
      testUtil.flush();
      var counter = 0;
      widget.addEventListener( "mousedown", function(){ counter++; } );
      var node = widget._getTargetNode();
      testUtil.fakeMouseEventDOM( node, "mousedown", 1, 0, 0, 0, true );
      assertEquals( 0, counter );
      widget.destroy();
      this.resetMobileWebkitSupport();
    },
    
    testAllowNativeMouseWheelEvents : function() {
      var testUtil = org.eclipse.rwt.test.fixture.TestUtil;
      var widget = new qx.ui.basic.Terminator();
      widget.addToDocument();
      testUtil.flush();
      var counter = 0;
      widget.addEventListener( "mousewheel", function(){ counter++; } );
      var node = widget._getTargetNode();
      testUtil.fakeMouseEventDOM( node, "mousewheel", 1, 0, 0, 0, true );
      assertEquals( 1, counter );
      widget.destroy();
      this.resetMobileWebkitSupport();
    },
    
    testTouchStartCreatesMouseDown : function() {
      var testUtil = org.eclipse.rwt.test.fixture.TestUtil;
      var widget = new qx.ui.basic.Terminator();
      widget.addToDocument();
      testUtil.flush();      
      var log = [];
      var logger = function( event ){ 
        log.push( event.getType() ); 
      };
      widget.addEventListener( "mousedown", logger );
      var node = widget._getTargetNode();
      this.touch( node, "touchstart" );
      assertEquals( [ "mousedown" ], log );
      widget.destroy();
      this.resetMobileWebkitSupport();
    },
    
    testTouchEndCreatesMouseUp : function() {
      var testUtil = org.eclipse.rwt.test.fixture.TestUtil;
      var widget = new qx.ui.basic.Terminator();
      widget.addToDocument();
      testUtil.flush();      
      var log = [];
      var logger = function( event ){ 
        log.push( event.getType() ); 
      };
      widget.addEventListener( "mouseup", logger );
      var node = widget._getTargetNode();
      this.touch( node, "touchstart" );
      this.touch( node, "touchend" );
      assertEquals( [ "mouseup" ], log );
      widget.destroy();
      this.resetMobileWebkitSupport();
    },
    
    testMouseOver : function() {
      var testUtil = org.eclipse.rwt.test.fixture.TestUtil;
      var widget = new qx.ui.basic.Terminator();
      widget.addToDocument();
      testUtil.flush();      
      var log = [];
      var logger = function( event ){ 
        log.push( event.getType() ); 
      };
      widget.addEventListener( "mouseover", logger );
      widget.addEventListener( "mouseout", logger );
      var node = widget._getTargetNode();
      this.touch( node, "touchstart" );
      this.touch( node, "touchend" );
      this.touch( node, "touchstart" );
      this.touch( node, "touchend" );
      assertEquals( [ "mouseover" ], log );
      widget.destroy();
      this.resetMobileWebkitSupport();
    },
    
    testMouseOut : function() {
      this.resetMobileWebkitSupport();
      var testUtil = org.eclipse.rwt.test.fixture.TestUtil;
      var widget = new qx.ui.basic.Terminator();
      widget.addToDocument();
      testUtil.flush();      
      var log = [];
      var logger = function( event ){ 
        log.push( event.getType() ); 
      };
      widget.addEventListener( "mouseout", logger );
      var node = widget._getTargetNode();
      this.touch( node, "touchstart" );
      this.touch( node, "touchend" );
      assertEquals( [], log );
      this.touch( document.body, "touchstart" );
      assertEquals( [ "mouseout" ], log );
      widget.destroy();
      this.resetMobileWebkitSupport();
    },
    
    testCoordinatesMouseDownUp : function() {
      var testUtil = org.eclipse.rwt.test.fixture.TestUtil;
      var widget = new qx.ui.basic.Terminator();
      widget.addToDocument();
      testUtil.flush();      
      var log = [];
      var logger = function( event ){ 
        log.push( event.getPageX(), event.getPageY() ); 
      };
      widget.addEventListener( "mousedown", logger );
      widget.addEventListener( "mouseup", logger );
      var node = widget._getTargetNode(); 
      this.touchAt( node, "touchstart", 1, 2 );
      this.touchAt( node, "touchend", 3, 4 );
      assertEquals( [ 1, 2, 3, 4 ], log );
      widget.destroy();
      this.resetMobileWebkitSupport();
    },
    
    testPreventTapZoom : function() {
      var testUtil = org.eclipse.rwt.test.fixture.TestUtil;
      var widget = new qx.ui.basic.Terminator();
      widget.addToDocument();
      testUtil.flush();
      var prevented = false; 
      widget.addEventListener( "mouseup", function( event ) {
        prevented = event.getDomEvent().originalEvent.prevented === true;
      } );
      var node = widget._getTargetNode();
      this.resetMobileWebkitSupport();
      this.touch( node, "touchstart" );
      this.touch( node, "touchend" );
      assertTrue( prevented );
      widget.destroy();
      this.resetMobileWebkitSupport();
    },

    testClick : function() {
      var testUtil = org.eclipse.rwt.test.fixture.TestUtil;
      var widget = new qx.ui.basic.Terminator();
      widget.addToDocument();
      testUtil.flush();      
      var log = [];
      var logger = function( event ){ 
        log.push( event.getType() ); 
      };
      widget.addEventListener( "mousedown", logger );
      widget.addEventListener( "mouseup", logger );
      widget.addEventListener( "click", logger );
      var node = widget._getTargetNode();
      this.touch( node, "touchstart" );
      this.touch( node, "touchend" );
      assertEquals( [ "mousedown", "mouseup", "click"], log );
      widget.destroy();          
      this.resetMobileWebkitSupport();
    },
    
    testNoClickOnDifferentTargets : function() {
      var testUtil = org.eclipse.rwt.test.fixture.TestUtil;
      var widget = new qx.ui.basic.Terminator();
      widget.addToDocument();
      testUtil.flush();      
      var log = [];
      var logger = function( event ){ 
        log.push( event.getType() ); 
      };
      widget.addEventListener( "mousedown", logger );
      widget.addEventListener( "mouseup", logger );
      widget.addEventListener( "click", logger );
      var node = widget._getTargetNode();
      this.touch( document.body, "touchstart" );
      this.touch( node, "touchend" );
      assertEquals( [ "mouseup" ], log );
      widget.destroy();      
      this.resetMobileWebkitSupport();
    },

    testCancelOnGesture : function() {
      var testUtil = org.eclipse.rwt.test.fixture.TestUtil;
      var doc = qx.ui.core.ClientDocument.getInstance();
      var widget = new qx.ui.basic.Terminator();
      widget.addToDocument();
      testUtil.flush();      
      var widgetLog = [];
      var widgetLogger = function( event ){ 
        widgetLog.push( event.getType() );
        event.stopPropagation(); 
      };
      var docLog = [];
      var docLogger = function( event ){ 
        docLog.push( event.getType() ); 
      };
      widget.addEventListener( "mouseover", widgetLogger );
      widget.addEventListener( "mouseout", widgetLogger );
      widget.addEventListener( "mousedown", widgetLogger );
      widget.addEventListener( "mouseup", widgetLogger );
      widget.addEventListener( "click", widgetLogger );
      doc.addEventListener( "mouseover", docLogger );
      doc.addEventListener( "mouseout", docLogger );
      doc.addEventListener( "mousedown", docLogger );
      doc.addEventListener( "mouseup", docLogger );
      doc.addEventListener( "click", docLogger );
      var node = widget._getTargetNode();
      this.gesture( node, "gesturestart" );
      this.touch( node, "touchstart", 3 );
      this.touch( node, "touchend", 2 );
      this.gesture( node, "gestureend" );
      var widgetExpected = [ "mouseover", "mousedown", "mouseout" ];
      var docExpected = [ "mouseover", "mouseup" ];      
      assertEquals( widgetExpected, widgetLog );
      assertEquals( docExpected, docLog );
      widget.destroy();
      this.resetMobileWebkitSupport();
    },
    
    testCancelOnSwipe : function() {
      var testUtil = org.eclipse.rwt.test.fixture.TestUtil;
      var doc = qx.ui.core.ClientDocument.getInstance();
      var widget = new qx.ui.basic.Terminator();
      widget.addToDocument();
      testUtil.flush();      
      var widgetLog = [];
      var widgetLogger = function( event ){ 
        widgetLog.push( event.getType() );
        event.stopPropagation(); 
      };
      var docLog = [];
      var docLogger = function( event ){ 
        docLog.push( event.getType() ); 
      };
      widget.addEventListener( "mouseover", widgetLogger );
      widget.addEventListener( "mouseout", widgetLogger );
      widget.addEventListener( "mousedown", widgetLogger );
      widget.addEventListener( "mouseup", widgetLogger );
      widget.addEventListener( "click", widgetLogger );
      doc.addEventListener( "mouseover", docLogger );
      doc.addEventListener( "mouseout", docLogger );
      doc.addEventListener( "mousedown", docLogger );
      doc.addEventListener( "mouseup", docLogger );
      doc.addEventListener( "click", docLogger );
      var node = widget._getTargetNode();
      this.touchAt( node, "touchstart", 10, 10 );
      assertEquals( 2, widgetLog.length );
      this.touchAt( node, "touchmove", 18, 18 );
      assertEquals( 2, widgetLog.length );
      this.touchAt( node, "touchmove", 19, 19 );
      this.touch( node, "touchend" );
      var widgetExpected = [ "mouseover", "mousedown", "mouseout" ];
      var docExpected = [ "mouseover", "mouseup" ];      
      assertEquals( widgetExpected, widgetLog );
      assertEquals( docExpected, docLog );
      var widgetLog = [];
      var docLog = [];
      this.touch( node, "touchstart" );
      var widgetExpected = [ "mouseover", "mousedown" ];
      var docExpected = [ "mouseout" ];      
      assertEquals( widgetExpected, widgetLog );
      assertEquals( docExpected, docLog );
      widget.destroy();
      this.resetMobileWebkitSupport();
    },
    
    testDoubleClick : function() {
      var testUtil = org.eclipse.rwt.test.fixture.TestUtil;
      var widget = new qx.ui.basic.Terminator();
      widget.addToDocument();
      testUtil.flush();      
      var log = [];
      var logger = function( event ){ 
        log.push( event.getType() ); 
      };
      widget.addEventListener( "mousedown", logger );
      widget.addEventListener( "mouseup", logger );
      widget.addEventListener( "click", logger );
      widget.addEventListener( "dblclick", logger );
      var node = widget._getTargetNode();
      this.touch( node, "touchstart" );
      this.touch( node, "touchend" );
      this.touch( node, "touchstart" );
      this.touch( node, "touchend" );
      var expected = [
        "mousedown", 
        "mouseup", 
        "click",
        "mousedown", 
        "mouseup", 
        "click",
        "dblclick"
      ];
      assertEquals( expected, log );
      widget.destroy();                
      this.resetMobileWebkitSupport();
    },
    
    testNoDoubeClickDifferentTarget : function() {
      var testUtil = org.eclipse.rwt.test.fixture.TestUtil;
      var widget = new qx.ui.basic.Terminator();
      widget.addToDocument();
      testUtil.flush();      
      var log = [];
      var logger = function( event ){ 
        log.push( event.getType() ); 
      };
      widget.addEventListener( "mousedown", logger );
      widget.addEventListener( "mouseup", logger );
      widget.addEventListener( "click", logger );
      widget.addEventListener( "dblclick", logger );
      var node = widget._getTargetNode();
      this.touch( document.body, "touchstart" );
      this.touch( document.body, "touchend" );
      this.touch( node, "touchstart" );
      this.touch( node, "touchend" );
      var expected = [
        "mousedown", 
        "mouseup", 
        "click",
      ];
      assertEquals( expected, log );
      widget.destroy();      
      this.resetMobileWebkitSupport();
    },

    testNoDoubeClickTooSlow : function() {
      var testUtil = org.eclipse.rwt.test.fixture.TestUtil;
      var widget = new qx.ui.basic.Terminator();
      widget.addToDocument();
      testUtil.flush();      
      var log = [];
      var logger = function( event ){ 
        log.push( event.getType() ); 
      };
      widget.addEventListener( "mousedown", logger );
      widget.addEventListener( "mouseup", logger );
      widget.addEventListener( "click", logger );
      widget.addEventListener( "dblclick", logger );
      var node = widget._getTargetNode();
      this.touch( node, "touchstart" );
      this.touch( node, "touchend" );
      org.eclipse.rwt.MobileWebkitSupport._lastMouseClickTime -= 1000;
      this.touch( node, "touchstart" );
      this.touch( node, "touchend" );
      var expected = [
        "mousedown", 
        "mouseup", 
        "click",
        "mousedown", 
        "mouseup", 
        "click"
      ];
      assertEquals( expected, log );
      widget.destroy();      
      this.resetMobileWebkitSupport();
    },
    
    testPreventDefaultAtFullscreen : function() {
      this.fakeFullscreen();
      var testUtil = org.eclipse.rwt.test.fixture.TestUtil;
      var widget = new qx.ui.basic.Terminator();
      widget.addToDocument();
      testUtil.flush();
      var log = [];
      var logger = function( event ) {
        log.push( event.getDomEvent().originalEvent.prevented );
      }
      widget.addEventListener( "mousedown", logger );
      widget.addEventListener( "mouseup", logger );
      widget.addEventListener( "mouseout", logger );
      var node = widget._getTargetNode();
      this.touch( node, "touchstart" );
      this.touch( node, "touchend" );
      this.touchAt( node, "touchstart", 10, 10 );
      this.touchAt( node, "touchmove", 19, 19 );
      var expected = [ true, true, true, true ];
      assertEquals( expected, log );
      widget.destroy();
      this.resetMobileWebkitSupport();
    },
    
    testNotZoomedPreventDefaultOnSwipe : function() {
      this.fakeZoom( false );
      var testUtil = org.eclipse.rwt.test.fixture.TestUtil;
      var widget = new qx.ui.basic.Terminator();
      widget.addToDocument();
      testUtil.flush();
      var log = [];
      var logger = function( event ) {
        log.push( event.getDomEvent().originalEvent );
      }
      widget.addEventListener( "mouseout", logger );
      var node = widget._getTargetNode();
      this.touchAt( node, "touchstart", 0, 0  );
      this.touchAt( node, "touchmove", 19, 19 );
      assertTrue( log[ 0 ].prevented );
      widget.destroy();
      this.resetMobileWebkitSupport();
    },
    
    testZoomedNotPreventDefaultOnSwipe : function() {
      this.fakeZoom( true );
      var testUtil = org.eclipse.rwt.test.fixture.TestUtil;
      var widget = new qx.ui.basic.Terminator();
      widget.addToDocument();
      testUtil.flush();
      var log = [];
      var logger = function( event ) {
        log.push( event.getDomEvent().originalEvent );
      }
      widget.addEventListener( "mouseout", logger );
      var node = widget._getTargetNode();
      this.touchAt( node, "touchstart", 0, 0  );
      this.touchAt( node, "touchmove", 19, 19 );
      assertFalse( log[ 0 ].prevented );
      widget.destroy();
      this.resetMobileWebkitSupport();
    },
    
    /////////
    // Helper
    
    createTouch : function( target, x, y ) {
      // Can not overwrite fields of a touch-instance:
      return new Touch();
    },
    
    createTouchList : function( touches ) {
      // Note: "TouchList.prototype.constructor.apply" does NOT work. 
      var args = [];
      for( var i = 0; i < touches.length; i++ ) {
        args.push( "touches[ " + i + "]" );
      }
      return eval( "new TouchList(" + args.join() + ")" );
    },
    
    createTouchEvent : function( type, touchList ) {
      // Note: the screen/client values are not used in real touch-events.
      var result = document.createEvent( "TouchEvent" );
      result.initTouchEvent(
        type,
        true, //canBubble
        true, //cancelable
        window, //view
        0, //detail
        0, //screenX
        0, //screenY
        0, //clientX
        0, //clientY
        false, //ctrlKey
        false, //altKey
        false, //shiftKey
        false, //metaKey
        touchList, //touches
        touchList, //targetTouches
        touchList, //changedTouches
        0, //scale
        0 //rotation
      );
      result.touches = touchList;
      // So we can test if preventDefault has been called:
      result.preventDefault = function() {
        this.prevented = true;
      };
      result.prevented = false;      
      return result;
    },

	  createGestureEvent : function( type, target ) {
	    // Note: the screen/client values are not used in real touch-events.
      var result = document.createEvent( "GestureEvent" );
      result.initGestureEvent(
        type,
        true, // canBubble,
        true, // cancelable
        window, // view
        0, // detail
        0, // screenX
        0, // screenY
        0, // clientX
        0, // clientY
        false, // ctrlKey
        false, // boolean altKey
        false, // shiftKey
        false, // metaKey
        target,
        0, // scale,
        0 // rotation
      );
      return result;
    },
    
    // Some nodes "swallow" (non-fake) touch-events;
    _isValidTouchTarget : function( node ) {
      var tag = node.tagName;
      return tag != "INPUT" && tag != "TEXTAREA";
    },
    
    touch : function( node, type, touchesNumberOrArray ) {
      var touches;
      if( touchesNumberOrArray instanceof Array ) {
        touches = touchesNumberOrArray;
      } else {
        touches = []; 
        var number = 1;
        if( typeof touchesNumberOrArray === "number" ) {
          number = touchesNumberOrArray;
        }
        while( touches.length < number ) { 
          touches.push( new Touch() );
        }
      }
      var touchList = this.createTouchList( touches );
      var event = this.createTouchEvent( type, touchList );
      if( this._isValidTouchTarget( node ) ) {
        node.dispatchEvent( event );
      }
    },
    
    gesture : function( node, type ) {
      var event = this.createGestureEvent( type, node )
      var touchType = "";
      switch( type ) {
        case "gesturestart":
          touchType = "touchstart";
        break;
        case "gesturechange":
          touchType = "touchmove";
        break;
        case "gestureend":
          touchType = "touchend";
        break;
      }
      if( type === "gesturestart" ) {
        this.touch( node, touchType, 1 );
      }
      node.dispatchEvent( event );
      this.touch( node, touchType, 2 );
      // NOTE: there should actually be two "touchend" (unless the user
      // raises both finger exactly at once), but due to a bug webkit always
      // reports all touches to have ended, even if only one of several ended.
    },
    
    // TODO [tb] : use "real" touch-objects as ducmented in the 
    // Safari DOM Additions Reference 
    touchAt : function( node, type, x, y ) {
      var touch = {
        "target" : node,
        "clientX" : x,
        "clientY" : y
      };
      var touchList = {
        "item" : function( i ){
          return i === 0 ? touch : null;
        },
        "length" : 1
      };
      var event = {
        "preventDefault" : function(){ this.prevented = true; },
        "prevented" : false,
        "touches" : touchList,
        "target" : node,
        "type" : type
      };
      if( this._isValidTouchTarget( node ) ) {
        org.eclipse.rwt.MobileWebkitSupport.__onTouchEvent( event );
      }
    },
    
    fakeFullscreen : function() {
      org.eclipse.rwt.MobileWebkitSupport._fullscreen = true;
    },
    
    fakeZoom : function( value ) {
      org.eclipse.rwt.MobileWebkitSupport._isZoomed = function(){
        return value;
      };
    },
    
    resetMobileWebkitSupport : function() {
      var mobile = org.eclipse.rwt.MobileWebkitSupport;
      mobile._lastMouseOverTarget = null;
      mobile._lastMouseDownTarget = null;
      mobile._lastMouseDownPosition = null;
      mobile._lastMouseClickTarget = null;
      mobile._lastMouseClickTime = null;
      mobile._mouseEnabled = true;
      mobile._fullscreen = window.navigator.standalone;
    }

  }

} );