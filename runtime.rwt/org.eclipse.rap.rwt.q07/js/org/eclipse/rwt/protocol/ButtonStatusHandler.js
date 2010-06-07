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
    return widget;
  },
 
  
  synchronizeWidget : function( widget, properties ) {
 
  },
  
  updateListeners : function( widget, shouldListenMap ) {
 
  },
  
  disposeWidget : function( widget ) {
 
  } 
  
} );



