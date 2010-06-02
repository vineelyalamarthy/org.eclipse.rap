/*******************************************************************************
 * Copyright (c) 2010 EclipseSource and others. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   EclipseSource - initial API and implementation
 ******************************************************************************/

org.eclipse.rwt.protocol.widgetSynchronizer = ( function() {
  
  return {
    
    fireEvent : function( widget, eventName ) {
      var req = org.eclipse.swt.Request.getInstance();
      var generator = req.getMessageGenerator();
      var id
        = org.eclipse.rwt.protocol.widgetManager.findIdByWidget( widget );
      generator.appendEvent( id, eventName );    
    },
    
    setWidgetProperty : function( widget, propertyName, propertyValue ) {
      var req = org.eclipse.swt.Request.getInstance();
      var generator = req.getMessageGenerator();
      var id 
        = org.eclipse.rwt.protocol.widgetManager.findIdByWidget( widget );
      generator.appendSynchronize( id, propertyName, propertyValue );     
    },
    
    setDeviceProperty : function( propertyName, propertyValue ) {
      var req = org.eclipse.swt.Request.getInstance();
      var generator = req.getMessageGenerator();
      generator.appendDevice( propertyName, propertyValue ); 
    },
    
    setMetaProperty : function( propertyName, propertyValue ) {
      var req = org.eclipse.swt.Request.getInstance();
      var generator = req.getMessageGenerator();
      generator.appendMeta( propertyName, propertyValue ); 
    }
    
    
  };
  
}() );
