/*******************************************************************************
 * Copyright (c) 2010 EclipseSource and others. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   EclipseSource - initial API and implementation
 ******************************************************************************/

'use strict';
org.eclipse.rwt.protocol.widgetStatusHandlerRegistry = ( function() {
  var internalRegistry = {};
  var widgetTypeMap = {};
  var knownTypes = [];
  
  var isHandlerValid = function( handler ) {
    var neededProperties = [ 'getWidgetType', 
                             'createWidget', 
                             'isControl', 
                             'updateListeners',
                             'disposeWidget',
                             'synchronizeWidget'
                           ];
    var result = false;
    if( handler !== null && handler !== undefined ) {
      result = true;
      for( var i = 0; i < neededProperties.length && result; i++ ) {
        var expectedProperty = neededProperties[ i ];
        if( handler[ expectedProperty ] === undefined ) {
          result = false;
        }
      }
    }
    return result;
  };
  
  var getIndexOfType = function( type ) {
    var found = false;
    var result;
    for( var i = 0; i < knownTypes.length && !found; i++ ) {
      if( knownTypes[ i ] === type ) {
        found = true;
        result = i;
      }
    }
    return result;
  };
  
  return {
    addHandler : function( handler ) {
      if( isHandlerValid( handler ) ) {
        internalRegistry[ handler.getWidgetType() ] = handler;
        knownTypes.push( handler.getWidgetType() );
      } else {
        throw new Error( "handler is not valid" );
      }
    },
    
    getHandler : function( widgetType ) {
      return internalRegistry[ widgetType ];
    },
    
    addWidgetToType : function( widgetId, widgetType ) {
      if( this.getHandler( widgetType ) !== undefined ) {        
        widgetTypeMap[ widgetId ] = getIndexOfType( widgetType );
      } else {
        throw new Error( 'no Handler registered for type: ' + widgetType );
      }
    },
    
    getTypeForWidget : function( widgetId ) {
      var result;
      var indexOfType = widgetTypeMap[ widgetId ];
      if( indexOfType !== undefined && typeof indexOfType === 'number' ) 
      {
        result = knownTypes[ indexOfType ];
      }
      return result;
    },
    
    getHandlerForWidgetId : function( widgetId ) {
      return this.getHandler( this.getTypeForWidget( widgetId ) );
    },
    
    getHandlerForWidget : function( widget ) {
      var widgetId 
        = org.eclipse.rwt.protocol.widgetManager.findIdByWidget( widget );
      return this.getHandlerForWidgetId( widgetId );
    }
    
  };
}() );
