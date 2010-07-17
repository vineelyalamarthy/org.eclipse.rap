/*******************************************************************************
 * Copyright (c) 2010 EclipseSource and others. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   EclipseSource - initial API and implementation
 ******************************************************************************/

org.eclipse.rwt.protocol.widgetStatusHandlerRegistry.addHandler( {
  
  getWidgetType : function() {
    return org.eclipse.rwt.protocol.constants.TYPE_SHELL;
  },
  
  isControl : function() {
    return true;
  },
  
  createWidget : function( styleArray, parameter ) {
    var shell = new org.eclipse.swt.widgets.Shell();    
    this._handleStyles( shell, styleArray );
    if( parameter !== null 
        && parameter !== undefined 
        && parameter.length > 0 ) 
    {
      if( typeof parameter[ 0 ] === 'string' ) {
        var wm = org.eclipse.rwt.protocol.widgetManager;
        var parent = wm.findWidgetById( parameter[ 0 ] );
        if( parent !== undefined ) {
          // TODO: Write test for setting parent
          shell.setParentShell( parent );
        }
      }
    }
    shell.initialize();
    shell.addToDocument();
    return shell;
  },
  
  _handleStyles : function( shell, styleArray ) {
    var styleObject 
      = org.eclipse.rwt.protocol.util.convertStyleArrayToObject( styleArray );
    if( 'BORDER' in styleObject ) {
      shell.addState( 'rwt_BORDER' );
    }
    if( 'MODAL' in styleObject ) {
      shell.addState( 'rwt_APPLICATION_MODAL' );
    }
    if( 'ON_TOP' in styleObject ) {
      shell.addState( 'rwt_ON_TOP' );
    }
    if( 'TITLE' in styleObject ) {
      shell.addState( 'rwt_TITLE' );
    }
    if( 'TOOL' in styleObject ) {
      shell.addState( 'rwt_TOOL' );
    }
    if( 'SHEET' in styleObject ) {
      shell.addState( 'rwt_SHEET' );
    }
    var showMin = ( 'MIN' in styleObject );
    shell.setShowMinimize( showMin );
    shell.setAllowMinimize( showMin );
    var showMax = ( 'MAX' in styleObject );
    shell.setShowMaximize( showMax );
    shell.setAllowMaximize( showMax );
    var showClose = ( 'CLOSE' in styleObject );
    shell.setShowClose( showClose );
    shell.setAllowClose( showClose );
    var resizable = ( 'RESIZE' in styleObject );
    shell.setResizable( resizable, resizable, resizable, resizable );
  },
  
  synchronizeWidget : function( widget, properties ) {
    if( 'icon' in properties ) {
      widget.setIcon( properties.icon );
    }
    if( 'caption' in properties ) {
      widget.setCaption( properties.caption );
    }
    if( 'opacity'  in properties ) {
      widget.setOpacity( properties.opacity );
    }
    if( 'active' in properties ) {
      widget.setActive( properties.active );
    }
    if( 'mode' in properties ) {
      widget.setMode( properties.mode );
    }
    if( 'fullScreen' in properties ) {
      widget.setFullScreen( properties.fullScreen );
    }
    if( 'minWidth' in properties ) {
      widget.setMinWidth( properties.minWidth );
    }
    if( 'minHeight' in properties ) {
      widget.setMinHeight( properties.minHeight );
    }
    org.eclipse.rwt.protocol.controlStatusUtil.synchronize( widget, properties );
  },
  
  updateListeners : function( widget, shouldListenMap ) {
    if( 'closelistener' in shouldListenMap ) {
      widget.setHasShellListener( shouldListenMap.closelistener );
    }
  },
  
  disposeWidget : function( widget ) {
    widget.doClose();
    var widgetManager = org.eclipse.rwt.protocol.widgetManager;
    var widgetId = widgetManager.findIdByWidget( widget );
    widgetManager.dispose( widgetId );
  } 
  
} );
