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
      = registry.getHandler( org.eclipse.rwt.protocol.constants.TYPE_SHELL );
    this._shellStatusHandler = handler; 
  },
  
  members : {   
    
    testRegisteringAsHandler : function() {
      var registry = org.eclipse.rwt.protocol.widgetStatusHandlerRegistry;
      var handler = registry.getHandler( org.eclipse.rwt.protocol.constants.TYPE_SHELL );
      registry.addHandler( handler );
    },
    
    testGetType : function() {
      var handler = this._shellStatusHandler;
      var actualType = handler.getWidgetType();
      var expectedType = org.eclipse.rwt.protocol.constants.TYPE_SHELL;
      assertEquals( expectedType, actualType );
    },
    
    testIsControl : function() {
      var handler = this._shellStatusHandler;   
      assertTrue( handler.isControl() );
    },
    
    testCreateWidget : function() {
      var handler = this._shellStatusHandler;
      var styles = [ 'SHELL_TRIM' ];
      var widget = handler.createWidget( styles );
      assertTrue( typeof widget === 'object' );
      var className = widget.classname;
      assertEquals( org.eclipse.rwt.protocol.constants.TYPE_SHELL, className );
    },
    
    testModaleState : function() {
      var handler = this._shellStatusHandler;
      var styles = [ 'MODAL' ];
      var widget = handler.createWidget( styles );
      assertTrue( widget.hasState( 'rwt_APPLICATION_MODAL' ) );
    },
    
    testOnTopState : function() {
      var handler = this._shellStatusHandler;
      var styles = [ 'ON_TOP' ];
      var widget = handler.createWidget( styles );
      assertTrue( widget.hasState( 'rwt_ON_TOP' ) );
    },
    
    testTitleState : function() {
      var handler = this._shellStatusHandler;
      var styles = [ 'TITLE' ];
      var widget = handler.createWidget( styles );
      assertTrue( widget.hasState( 'rwt_TITLE' ) );
    },
    
    testToolState : function() {
      var handler = this._shellStatusHandler;
      var styles = [ 'TOOL' ];
      var widget = handler.createWidget( styles );
      assertTrue( widget.hasState( 'rwt_TOOL' ) );
    },
    
    testSheetState : function() {
      var handler = this._shellStatusHandler;
      var styles = [ 'SHEET' ];
      var widget = handler.createWidget( styles );
      assertTrue( widget.hasState( 'rwt_SHEET' ) );
    },
    
    testBorderState : function() {
      var handler = this._shellStatusHandler;
      var styles = [ 'BORDER' ];
      var widget = handler.createWidget( styles );
      assertTrue( widget.hasState( 'rwt_BORDER' ) );
    },
    
    testMinimizeStyle : function() {
      var handler = this._shellStatusHandler;
      var styles = [ 'MIN' ];
      var widget = handler.createWidget( styles );
      assertTrue( widget.getShowMinimize() );
      assertTrue( widget.getAllowMinimize() );
      var styles2 = [ 'MAX' ];
      var widget2 = handler.createWidget( styles2 );
      assertFalse( widget2.getShowMinimize() );
      assertFalse( widget2.getAllowMinimize() );
    },
    
    testMaximizeStyle : function() {
      var handler = this._shellStatusHandler;
      var styles = [ 'MAX' ];
      var widget = handler.createWidget( styles );
      assertTrue( widget.getShowMaximize() );
      assertTrue( widget.getAllowMaximize() );
      var styles2 = [ 'MIN' ];
      var widget2 = handler.createWidget( styles2 );
      assertFalse( widget2.getShowMaximize() );
      assertFalse( widget2.getAllowMaximize() );
    },
    
    testCloseStyle : function() {
      var handler = this._shellStatusHandler;
      var styles = [ 'CLOSE' ];
      var widget = handler.createWidget( styles );
      assertTrue( widget.getShowClose() );
      assertTrue( widget.getAllowClose() );
      var styles2 = [ 'MAX' ];
      var widget2 = handler.createWidget( styles2 );
      assertFalse( widget2.getShowClose() );
      assertFalse( widget2.getAllowClose() );
    },
    
    testResizeStyle : function() {
      var handler = this._shellStatusHandler;
      var styles = [ 'CLOSE' ];
      var widget = handler.createWidget( styles );
      assertFalse( widget.getResizable() );
      var styles2 = [ 'RESIZE' ];
      var widget2 = handler.createWidget( styles2 );
      assertTrue( widget2.getResizable() );      
    },   
    
    testSetImage : function() {
      var handler = this._shellStatusHandler;
      var styles = [ 'CLOSE' ];
      var widget = handler.createWidget( styles );
      var syncObj = {
        icon : 'http://aImage.path'  
      };
      handler.synchronizeWidget( widget, syncObj );
      assertIdentical( syncObj.icon, widget.getIcon() );
    },
    
    testSetCaption : function() {
      var handler = this._shellStatusHandler;
      var styles = [ 'CLOSE' ];
      var widget = handler.createWidget( styles );
      var syncObj = {
        caption : 'a caption'  
      };
      handler.synchronizeWidget( widget, syncObj );
      assertIdentical( syncObj.caption, widget.getCaption() );
    },
    
    testSetOpacity : function() {
      var handler = this._shellStatusHandler;
      var styles = [ 'CLOSE' ];
      var widget = handler.createWidget( styles );
      var syncObj = {
        opacity : 4.5  
      };
      handler.synchronizeWidget( widget, syncObj );
      assertIdentical( syncObj.opacity, widget.getOpacity() );
    },
    
    testSetActive : function() {
      var handler = this._shellStatusHandler;
      var styles = [ 'CLOSE' ];
      var widget = handler.createWidget( styles );
      var syncObj = {
        active : true  
      };
      handler.synchronizeWidget( widget, syncObj );
      assertIdentical( syncObj.active, widget.getActive() );
    },
    
    testSetMode : function() {
      var handler = this._shellStatusHandler;
      var styles = [ 'CLOSE' ];
      var widget = handler.createWidget( styles );
      var syncObj = {
        mode : 'maximized'  
      };
      handler.synchronizeWidget( widget, syncObj );
      assertIdentical( syncObj.mode, widget.getMode() );
    },
    
    testSetFullScreen : function() {
      var handler = this._shellStatusHandler;
      var styles = [ 'CLOSE' ];
      var widget = handler.createWidget( styles );
      var syncObj = {
        fullScreen : true
      };
      handler.synchronizeWidget( widget, syncObj );
    },
    
    testSetMinWidth : function() {
      var handler = this._shellStatusHandler;
      var styles = [ 'CLOSE' ];
      var widget = handler.createWidget( styles );
      var syncObj = {
        minWidth : 100  
      };
      handler.synchronizeWidget( widget, syncObj );
      assertIdentical( syncObj.minWidth, widget.getMinWidth() );
    },
    
    testControlStatusUtil : function() {
      var handler = this._shellStatusHandler;
      var styles = [ 'CLOSE' ];
      var widget = handler.createWidget( styles );
      var syncObj = {
          zIndex : 3  
        };
      handler.synchronizeWidget( widget, syncObj );
      assertEquals( 3, widget.getZIndex() );             
    },
    
    testSetMinHeight : function() {
      var handler = this._shellStatusHandler;
      var styles = [ 'CLOSE' ];
      var widget = handler.createWidget( styles );
      var syncObj = {
        minHeight : 100  
      };
      handler.synchronizeWidget( widget, syncObj );
      assertIdentical( syncObj.minHeight, widget.getMinHeight() );
    },
    
    testDispose : function() {
      var handler = this._shellStatusHandler;
      var styles = [ 'CLOSE' ];
      var widget = handler.createWidget( styles );
      handler.disposeWidget( widget );
    },
    
    testAddCloseListener : function() {
      var handler = this._shellStatusHandler;
      var styles = [ 'CLOSE' ];
      var widget = handler.createWidget( styles );
      var listen = {
        'closelistener' : true 
      };
      handler.updateListeners( widget, listen );
      assertTrue( widget._hasShellListener );
      listen[ 'closelistener' ] = false;
      handler.updateListeners( widget, listen );
      assertFalse( widget._hasShellListener );
    }
  
  }
  
}


qx.Class.define( "org.eclipse.rwt.test.tests.ShellStatusHandlerTest", object );