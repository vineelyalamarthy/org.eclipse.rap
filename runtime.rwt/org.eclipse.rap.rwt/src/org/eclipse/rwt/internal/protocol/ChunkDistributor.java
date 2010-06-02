/******************************************************************************* 
* Copyright (c) 2010 EclipseSource and others. All rights reserved. This
* program and the accompanying materials are made available under the terms of
* the Eclipse Public License v1.0 which accompanies this distribution, and is
* available at http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*   EclipseSource - initial API and implementation
*******************************************************************************/ 
package org.eclipse.rwt.internal.protocol;

import org.eclipse.rwt.internal.lifecycle.RWTRequestVersionControl;
import org.eclipse.rwt.lifecycle.AbstractWidgetLCA;
import org.eclipse.rwt.lifecycle.WidgetUtil;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Widget;



public class ChunkDistributor implements IStreamListener {  
  
  private Composite root;
  private int requestCount;

  public ChunkDistributor( final Composite root ) {
    this.root = root;
  }

  public void objectFinished( final String key, final Object object ) {
    if( key.equals( IProtocolConstants.MESSAGE_WIDGETS ) ) {
      handleWidgetPayloads( object );
    } else if( key.equals( IProtocolConstants.MESSAGE_META ) ) {
      handleMetaPayload( object );
    }
  }

  private void handleWidgetPayloads( final Object object ) {
    if( object instanceof Chunk ) {
      Chunk chunk = ( Chunk )object;
      String widgetId 
        = ( String )chunk.getValue( IProtocolConstants.WIDGETS_ID );
      String payloadType 
        = ( String )chunk.getValue( IProtocolConstants.WIDGETS_TYPE );
      Chunk payload 
        = ( Chunk )chunk.getValue( IProtocolConstants.WIDGETS_PAYLOAD );
      handlePayload( widgetId, payloadType, payload );
    }
  }

  private void handlePayload( final String widgetId, 
                              final String payloadType, 
                              final Chunk payload )
  {
    Widget widget = WidgetUtil.find( root, widgetId );
    if( widget != null ) {
      if( payloadType.equals( IProtocolConstants.PAYLOAD_SYNCHRONIZE ) ) {
        handleSynchronizePayload( payload, widget );
      } else if( payloadType.equals( IProtocolConstants.PAYLOAD_FIRE_EVENT ) ) {   
        String eventName 
          = ( String )payload.getValue( IProtocolConstants.KEY_EVENT );
        handleFireEventPayload( eventName, widget );
      }
    }
  }

  private void handleSynchronizePayload( final Chunk payload, 
                                         final Widget widget ) 
  {
    AbstractWidgetLCA lca = WidgetUtil.getLCA( widget );
    if( lca instanceof IChunkAdapter ) {
      ( ( IChunkAdapter)lca ).readData( widget, payload );
    }
  }

  private void handleFireEventPayload( final String eventName, 
                                       final Widget widget ) 
  {
    AbstractWidgetLCA lca = WidgetUtil.getLCA( widget );
    if( lca instanceof IChunkAdapter ) {
      ( ( IChunkAdapter)lca ).processEvent( widget, eventName );
    }
  }
  
  private void handleMetaPayload( final Object object ) {
    if( object instanceof Chunk ) {
      Chunk chunk = ( Chunk )object;
      Integer value 
        = ( Integer )chunk.getValue( IProtocolConstants.KEY_REQUEST_COUNTER );
      requestCount = value.intValue();
    }
  }

  public boolean isRequestValid() {
    return RWTRequestVersionControl.isValid( requestCount );    
  }
  
  
  
}
