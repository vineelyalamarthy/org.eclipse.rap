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
  },
  
  members : {   
    
    testWriteZIndex : function() {
      var util = org.eclipse.rwt.protocol.controlStatusUtil;
      var widget = this._createWidget();
      var payload = { zIndex : 3 };
      util.synchronize( widget, payload );
      assertEquals( 3, widget.getZIndex() );
    },
    
    testWriteVisible : function() {
      var util = org.eclipse.rwt.protocol.controlStatusUtil;
      var widget = this._createWidget();
      var payload = { visibility : false };
      util.synchronize( widget, payload );
      assertFalse( widget.getVisibility() );      
    },
    
    testWriteBackgroundImage : function() {
      var util = org.eclipse.rwt.protocol.controlStatusUtil;
      var widget = this._createWidget();
      var payload = { backgroundImage : "aPath" };
      util.synchronize( widget, payload );
      assertEquals( "aPath", widget.getBackgroundImage() );         
    },
    
    testWriteCursor : function() {
      assertTrue( false );
    },
    
    _createWidget : function() {
      var registry = org.eclipse.rwt.protocol.widgetStatusHandlerRegistry;
      var handler 
        = registry.getHandler( org.eclipse.rwt.protocol.constants.TYPE_SHELL );
      var style = [ 'PUSH' ];
      var shell = handler.createWidget( style );
      return shell;
    }
    
  
  }
  
}


qx.Class.define( "org.eclipse.rwt.test.tests.ControlStatusUtilTest", object );