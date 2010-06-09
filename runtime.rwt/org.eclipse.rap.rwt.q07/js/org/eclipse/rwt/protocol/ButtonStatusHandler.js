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
    var styleObject 
    = org.eclipse.rwt.protocol.util.convertStyleArrayToObject( styleArray );
    var widget;
    if( 'PUSH' in styleObject || 'TOGGLE' in styleObject ) {
      widget = new org.eclipse.rwt.widgets.Button( 'push' );
    } else if( 'CHECK' in styleObject ) {
      widget = new org.eclipse.rwt.widgets.Button( 'check' );
    } else if( 'RADIO' in styleObject ) {
      widget = new org.eclipse.rwt.widgets.Button( 'radio' );
    }
    this._handleStyles( widget, styleObject );
    widget.addToDocument();
    return widget;
  },
  
  _handleStyles : function( widget, styleObject ) {
    if( 'BORDER' in styleObject ) {
      widget.addState( 'rwt_BORDER' );
    }
    if( 'PUSH' in styleObject ) {
      widget.addState( 'rwt_PUSH' );
    }
    if( 'FLAT' in styleObject ) {
      widget.addState( 'rwt_FLAT' );
    }
    if( 'TOGGLE' in styleObject ) {
      widget.addState( 'rwt_TOGGLE' );
    }
    if( 'CHECK' in styleObject ) {
      widget.addState( 'rwt_CHECK' );
    }
    if( 'RADIO' in styleObject ) {
      widget.addState( 'rwt_RADIO' );
    }
  },
 
  
  synchronizeWidget : function( widget, properties ) {
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



