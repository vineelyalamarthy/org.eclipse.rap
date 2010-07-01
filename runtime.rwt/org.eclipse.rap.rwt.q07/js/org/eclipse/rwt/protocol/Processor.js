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
org.eclipse.rwt.protocol.Processor = function( messageString ) {
//  var messageObject = eval('(' + messageString + ')');
//  var lastWidgetId, lastPayloadType;   
//  if( this.constants.MESSAGE_META in messageObject ) {
//    this.meta = messageObject[ this.constants.MESSAGE_META ];
//  } 
//  if( this.constants.MESSAGE_DEVICE in messageObject ) {
//    this.device = messageObject[ this.constants.MESSAGE_DEVICE ];
//  }
//  if( this.constants.MESSAGE_WIDGETS in messageObject ) {
//    var widgets = messageObject[ this.constants.MESSAGE_WIDGETS ];
//    for( var i = 0; i < widgets.length; i++ ) {
//      var widget = widgets[ i ];
//      var widgetId = widget[ this.constants.WIDGETS_ID ];
//      var payloadType = widget[ this.constants.WIDGETS_TYPE ];
//      var payload = widget[ this.constants.WIDGETS_PAYLOAD ];
//      this.processPayload( widgetId, payloadType, payload );
//    }
//  }

  var that = this;  
  JSON.parse( messageString, function( key, value ) {     
    switch( key ) {
    case that.constants.MESSAGE_META :
      that.meta = value;
      break;
    case that.constants.MESSAGE_DEVICE :
      that.device = value;
      break;
    case that.constants.WIDGETS_ID : 
      lastWidgetId = value;
      break;
    case that.constants.WIDGETS_TYPE :
      lastPayloadType = value;
      break;
    case that.constants.WIDGETS_PAYLOAD :
      that.processPayload( lastWidgetId, lastPayloadType, value );
      break;
    default:
      break;
    } 
    return value;
  } );
};

org.eclipse.rwt.protocol.Processor.prototype.getRequestCounter = function() {
  return this.meta.requestCounter;
};

org.eclipse.rwt.protocol.Processor.prototype.getSettingStore = function() {
  return this.meta.settingStore;
}

org.eclipse.rwt.protocol.Processor.prototype.constants 
  = org.eclipse.rwt.protocol.constants;

org.eclipse.rwt.protocol.Processor.prototype._processConstruct 
  = function( widgetId, payloadObject ) 
{  
  var that = this;
  var widgetType = payloadObject[ that.constants.KEY_WIDGET_TYPE ];
  var registry = org.eclipse.rwt.protocol.widgetStatusHandlerRegistry;
  var handler = registry.getHandler( widgetType );
  if( handler !== null && handler !== undefined ) {
    var widgetStyle = payloadObject[ that.constants.KEY_WIDGET_STYLE ];
    var constructorParams = payloadObject[ that.constants.KEY_PARAMETER_LIST ];
    var parentId = payloadObject[ that.constants.KEY_PARENT_ID ];
    var widget = handler.createWidget( widgetStyle, constructorParams );
    if( widget !== null && widget !== undefined ) {          
      registry.addWidgetToType( widgetId, widgetType );   
      var widgetManager = org.eclipse.rwt.protocol.widgetManager;
      widgetManager.add( widget, widgetId, handler.isControl() );
      if( parentId !== null &&
          parentId !== undefined && 
          typeof parentId  === 'string' ) 
      {
        var parentWidget = widgetManager.findWidgetById( parentId );
        if( parentWidget !== undefined ) {
          widget.setParent( parentWidget );
        }
      }
    } else {
      throw new Error( 'Widget was not created correctly, widget is: ' 
                       + widget );
    }
  }
};

org.eclipse.rwt.protocol.Processor.prototype._getHandlerForWidgetId 
  = function( id ) 
{
  var registry = org.eclipse.rwt.protocol.widgetStatusHandlerRegistry;
  return registry.getHandlerForWidgetId( id );
};

org.eclipse.rwt.protocol.Processor.prototype._executeMethodOnHandler 
  = function( widgetId, payloadObject, methodName ) 
{
  var widgetManager = org.eclipse.rwt.protocol.widgetManager; 
  var handler = this._getHandlerForWidgetId( widgetId );
  if( handler !== null && handler !== undefined ) {
    var widget = widgetManager.findWidgetById( widgetId );
    handler[ methodName ]( widget, payloadObject );
  }
};
 

org.eclipse.rwt.protocol.Processor.prototype._processSynchronize 
  = function( widgetId, payloadObject) 
{
  this._executeMethodOnHandler( widgetId, payloadObject, 'synchronizeWidget' );
};

org.eclipse.rwt.protocol.Processor.prototype._processListen 
  = function( widgetId, payloadObject) 
{
  this._executeMethodOnHandler( widgetId, payloadObject, 'updateListeners' );
};

org.eclipse.rwt.protocol.Processor.prototype._processDestroy 
  = function( widgetId ) 
{
  var handler = this._getHandlerForWidgetId( widgetId );
  var widgetManager = org.eclipse.rwt.protocol.widgetManager; 
  if( handler !== null && handler !== undefined ) {
    var widget = widgetManager.findWidgetById( widgetId );
    if( widget !== null && widget !== undefined ) {
      handler.disposeWidget( widget );          
    }        
  }
};

org.eclipse.rwt.protocol.Processor.prototype._processExecute 
  = function( widgetId, payloadObject ) 
{
  var widgetManager = org.eclipse.rwt.protocol.widgetManager; 
  var widget = widgetManager.findWidgetById( widgetId );
  if( widget !== null && widget !== undefined ) {
    var methodName = payloadObject[ this.constants.KEY_METHODNAME ];
    var params = payloadObject[ this.constants.KEY_PARAMETER_LIST ];
    if( widget[ methodName ] !== undefined ) {
      widget[ methodName ]( params );        
    } else {
      throw new Error( 'Method: ' + methodName 
                        + 'is not defined on widget.' );
    }
  }
};

org.eclipse.rwt.protocol.Processor.prototype._processExecuteScript 
  = function( payloadObject ) 
{
  // currently we can only execute javascript
  var scriptType = payloadObject[ this.constants.KEY_SCRIPT_TYPE ];
  var script = payloadObject[ this.constants.KEY_SCRIPT ];
  if( scriptType === this.constants.KEY_JAVA_SCRIPT ) {
    eval( script );
  }
};

org.eclipse.rwt.protocol.Processor.prototype.processPayload 
  = function( widgetId, payloadType, payloadObject ) 
{
    switch( payloadType ) {
      case this.constants.PAYLOAD_CONSTRUCT :
        this._processConstruct( widgetId, payloadObject );
        break;   
      case this.constants.PAYLOAD_SYNCHRONIZE :
        this._processSynchronize( widgetId, payloadObject );
        break;
      case this.constants.PAYLOAD_LISTEN :
        this._processListen( widgetId, payloadObject );
        break;
      case this.constants.PAYLOAD_EXECUTE :
        this._processExecute( widgetId, payloadObject );
        break;
      case this.constants.PAYLOAD_EXECUTE_SCRIPT :
        this._processExecuteScript( payloadObject );
        break;
      case this.constants.PAYLOAD_DESTROY :
        this._processDestroy( widgetId );
        break;
      default:
        break;
    }
};
