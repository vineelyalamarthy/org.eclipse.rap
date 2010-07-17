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
    return org.eclipse.rwt.protocol.constants.TYPE_BUTTON;
  },
  
  isControl : function() {
    return true;
  },
  
  createWidget : function( styleArray, parameter ) {
    var widget;
    for( var i = 0; i < styleArray.length; i++ ) {
      var style = styleArray[ i ];
      switch( style ) {
      case 'PUSH':
        widget = new org.eclipse.rwt.widgets.Button( 'push' );
        break;
      case 'TOGGLE':
        widget = new org.eclipse.rwt.widgets.Button( 'push' );
        break;
      case 'CHECK':
        widget = new org.eclipse.rwt.widgets.Button( 'check' );
        break;
      case 'RADIO':
        widget = new org.eclipse.rwt.widgets.Button( 'radio' );
        break;
      default:
        break;
      }
    }
    this._handleStyles( widget, styleArray );
    widget.addToDocument();
    return widget;
  },
  
  _handleStyles : function( widget, styleArray ) {
    for( var i = 0; i < styleArray.length; i++ ) {
      var style = styleArray[ i ];
      switch( style ) {
      case 'BORDER':
        widget.addState( 'rwt_BORDER' );
        break;
      case 'PUSH':
        widget.addState( 'rwt_PUSH' );
        break;
      case 'FLAT':
        widget.addState( 'rwt_FLAT' );
        break;
      case 'TOGGLE':
        widget.addState( 'rwt_TOGGLE' );
        break;
      case 'CHECK':
        widget.addState( 'rwt_CHECK' );
        break;
      case 'RADIO':
        widget.addState( 'rwt_RADIO' );
        break;
      default:
        break;
      }
    }
  },
 
  
  synchronizeWidget : function( widget, properties ) {
    org.eclipse.rwt.protocol.controlStatusUtil.synchronize( widget, properties );
    if( 'text' in properties ) {
      widget.setText( properties[ 'text' ] );
    }
    if( 'horizontalChildrenAlign' in properties ) {
      widget.setHorizontalChildrenAlign( properties[ 'horizontalChildrenAlign' ] );
    }
    if( 'selection' in properties ) {
      widget.setSelection( properties[ 'selection' ] );
    }
    if( 'grayed' in properties ) {
      widget.setGrayed( properties[ 'grayed' ] );
    }
  },
  
  updateListeners : function( widget, shouldListenMap ) {
    if( 'selectionlistener' in shouldListenMap ) {
      widget.setHasSelectionListener( shouldListenMap[ 'selectionlistener' ] );
    }
  },
  
  disposeWidget : function( widget ) {
    var widgetManager = org.eclipse.rwt.protocol.widgetManager;
    var widgetId = widgetManager.findIdByWidget( widget );
    widgetManager.dispose( widgetId );
  } 
  
} );



