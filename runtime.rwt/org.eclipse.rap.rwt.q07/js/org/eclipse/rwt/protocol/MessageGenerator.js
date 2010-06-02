/*******************************************************************************
 * Copyright (c) 2010 EclipseSource and others. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   EclipseSource - initial API and implementation
 ******************************************************************************/

org.eclipse.rwt.protocol.MessageGenerator = function() {
  this._message = {};
};

org.eclipse.rwt.protocol.MessageGenerator.prototype.constants 
  = org.eclipse.rwt.protocol.constants;

org.eclipse.rwt.protocol.MessageGenerator.prototype.appendMeta 
  = function( key, value ) 
{
  if( !( this.constants.MESSAGE_META in this._message ) ) {
    this._message[ this.constants.MESSAGE_META ] = {};
  }
  this._message[ this.constants.MESSAGE_META ][ key ] = value;
};

org.eclipse.rwt.protocol.MessageGenerator.prototype.appendDevice 
  = function( key, value ) 
{
  if( !( this.constants.MESSAGE_DEVICE in this._message ) ) {
    this._message[ this.constants.MESSAGE_DEVICE ] = {};
  }
  this._message[ this.constants.MESSAGE_DEVICE ][ key ] = value;
};


org.eclipse.rwt.protocol.MessageGenerator.prototype.appendSynchronize 
  = function( widgetId, key, value ) 
{
  this._initializeWidgetArray();
  var syncObject 
    = this._findObjectInWidgets( widgetId, this.constants.PAYLOAD_SYNCHRONIZE );
  syncObject[ this.constants.WIDGETS_PAYLOAD ][ key ] = value;
}

org.eclipse.rwt.protocol.MessageGenerator.prototype._findObjectInWidgets 
  = function( widgetId, payloadType ) 
{
  var widgetArray = this._message[ this.constants.MESSAGE_WIDGETS ];
  var result;
  if( widgetArray ) {
    for( var i = 0; i < widgetArray.length && !result; i++ ) {
      var obj = widgetArray[ i ];
      if(    obj[ this.constants.WIDGETS_ID ] === widgetId 
          && obj[ this.constants.WIDGETS_TYPE ] === payloadType ) 
      {
        result = obj;
      }
    }
  }
  if( !result ) {
    result = {};
    result[ this.constants.WIDGETS_ID ] = widgetId;
    result[ this.constants.WIDGETS_TYPE ] = payloadType;
    result[ this.constants.WIDGETS_PAYLOAD ] = {};
    widgetArray.push( result );
  }
  return result;
}

org.eclipse.rwt.protocol.MessageGenerator.prototype.appendEvent 
  = function( widgetId, eventName ) 
{
  this._initializeWidgetArray();
  var widgetArray = this._message[ this.constants.MESSAGE_WIDGETS ];
  var eventObject = {};
  eventObject[ this.constants.WIDGETS_ID ] = widgetId;
  eventObject[ this.constants.WIDGETS_TYPE ] 
    = this.constants.PAYLOAD_FIRE_EVENT;
  eventObject[ this.constants.WIDGETS_PAYLOAD ] = {};
  eventObject[ this.constants.WIDGETS_PAYLOAD ][ this.constants.KEY_EVENT ] 
    = eventName;
  widgetArray.push( eventObject );
}

org.eclipse.rwt.protocol.MessageGenerator.prototype._initializeWidgetArray 
  = function() 
{
  if( !( this.constants.MESSAGE_WIDGETS in this._message ) ) {
    this._message[ this.constants.MESSAGE_WIDGETS ] = new Array();
  }  
}

org.eclipse.rwt.protocol.MessageGenerator.prototype.finish = function() {
  var jsonString = JSON.stringify( this._message, 
                                   null, 
                                   this.constants.MESSAGE_PART_INDENT );
  return jsonString;
};
