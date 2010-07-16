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

import java.io.PrintWriter;
import java.util.Map;

/**
 * One {@link ProtocolMessageWriter} per request!!!
 */
public abstract class ProtocolMessageWriter {  

  private PrintWriter decoratedWriter;
  private boolean hasMessageStarted;
  private boolean isFirstPayload;
  private String currentWidgetId;
  private String currentPayloadType;
  private boolean isPayloadOpen;
  private boolean isFirstPayloadValue;

  public ProtocolMessageWriter( final PrintWriter decoratedWriter ) {
    this.decoratedWriter = decoratedWriter;
    hasMessageStarted = false;
    isFirstPayload = true;
  }
  
  protected void write( final String s ) {
    handleMessageStart();
    decoratedWriter.write( s );
  }
  
  public void finish() {
    handleMessageStart();
    handleEndPayload();
    appendEndMessage();   
    decoratedWriter.flush();
  }
  
  private void handleEndPayload() {
    if( isPayloadOpen ) {
      appendEndPayloadValue();
      appendEndPayload();
      isPayloadOpen = false;
      isFirstPayloadValue = true;
    }
  }
  
  protected abstract void appendEndMessage();  
  
  protected abstract void appendEndPayloadValue();
  
  public final void appendPayload( final String widgetId,
                                   final String payloadType,
                                   final String key,
                                   final Object value )
  {
    handleMessageStart();
    if( currentWidgetId != null && widgetId.equals( currentWidgetId ) ) {
      addValueToSameWidgetPayload( widgetId, payloadType, key, value );
    } else {      
      createNewPayloadAndAddValue( widgetId, payloadType, key, value );
    }
    currentPayloadType = payloadType;
    currentWidgetId = widgetId;
    decoratedWriter.flush();
  }
  
  private void handleMessageStart() {
    if( !hasMessageStarted ) {
      hasMessageStarted = true;
      appendStartMessage();      
    }
  }
  
  public boolean isStarted() {
    return hasMessageStarted;
  }
  
  protected abstract void appendStartMessage();
  
  private void addValueToSameWidgetPayload( final String widgetId,
                                            final String payloadType,
                                            final String key,
                                            final Object value )
  {
    if(    currentPayloadType != null 
        && payloadType.equals( currentPayloadType ) ) 
    {
      addKeyValueToCurrentPayload( widgetId, payloadType, key, value );        
    } else {
      createNewPayloadAndAddValue( widgetId, payloadType, key, value );
    }
  }
  
  private void addKeyValueToCurrentPayload( final String widgetId,
                                            final String payloadType,
                                            final String key,
                                            final Object value )
  {
    if( !isPayloadOpen ) {
      setUpPayload( widgetId );
      appendPayloadType( payloadType );
      isPayloadOpen = true;
    } 
    handlePayloadValueSeparator();
    appendPayloadValue( key, value );
  }
  
  private void createNewPayloadAndAddValue( final String widgetId,
                                            final String payloadType,
                                            final Object key,
                                            final Object value )
  {      
    setUpPayload( widgetId );
    appendPayloadType( payloadType );
    handlePayloadValueSeparator();
    appendPayloadValue( key, value );
    isPayloadOpen = true;
  }
  
  protected abstract void appendPayloadType( final String payloadType );
  
  private void handlePayloadValueSeparator() {
    if( isPayloadOpen && !isFirstPayloadValue ) {
      appendPayloadValueSeparator();      
    }
    isFirstPayloadValue = false;
  }
  
  protected abstract void appendPayloadValueSeparator();
  
  protected abstract void appendPayloadValue( final Object key, 
                                              final Object value );
  
  public final void addConstructPayload( final String widgetId,
                                         final String parentId,
                                         final String type,
                                         final String[] styles, 
                                         final Object[] arguments )
  {
    setUpPayload( widgetId );
    appendConstructPayload( widgetId, parentId, type, styles, arguments );
    appendEndPayload();
    decoratedWriter.flush();
  }
  
  protected abstract void appendConstructPayload( final String widgetId,
                                                  final String parentId,
                                                  final String type, 
                                                  final String[] styles, 
                                                  final Object[] arguments );

  public final void addDestroyPayload( final String widgetId ) {
    setUpPayload( widgetId );    
    appendDestroyPayload();
    appendEndPayload();
    decoratedWriter.flush();
  }
  
  protected abstract void appendDestroyPayload();
  
  public final void addSychronizePayload( final String widgetId, 
                                          final Map properties ) 
  {
    setUpPayload( widgetId );
    appendSynchronizePayload( properties );    
    appendEndPayload();
    decoratedWriter.flush();
  }
  
  protected abstract void appendSynchronizePayload( final Map properties );

  public final void addMultipleSychronizePayload( final String[] widgetIds, 
                                                  final Map properties ) 
  {
    setUpPayload( null );
    appendMultipleSynchronizePayload( widgetIds, properties );    
    appendEndPayload();
    decoratedWriter.flush();
  }
  
  protected abstract void appendMultipleSynchronizePayload( 
    final String[] widgetIds,
    final Map properties ); 
  
  public final  void addListenPayload( final String widgetId, 
                                       final Map listeners ) 
  {
    setUpPayload( widgetId );       
    appendListenPayload( listeners );
    appendEndPayload();
    decoratedWriter.flush();
  }
  
  protected abstract void appendListenPayload( final Map listeners );

  public final  void addFireEventPayload( final String widgetId, 
                                          final String event ) 
  {
    setUpPayload( widgetId );
    appendEventPayload( event );
    appendEndPayload();
    decoratedWriter.flush();
  }  
  
  protected abstract void appendEventPayload( final String event );
  
  public final void addExecutePayload( final String widgetId, 
                                       final String methodName, 
                                       final Object[] arguments ) 
  {
    setUpPayload( widgetId );
    appendExecutePayload( methodName, arguments );
    appendEndPayload();    
    decoratedWriter.flush();
  }
  
  protected abstract void appendExecutePayload( final String methodName,
                                                final Object[] arguments );

  public final void addExecuteScript( final String widgetId, 
                                      final String scriptType, 
                                      final String script )
  {
    setUpPayload( widgetId );
    appendExecuteScript( scriptType, script );
    appendEndPayload();
    decoratedWriter.flush();
  }
  
  protected abstract void appendExecuteScript( final String scriptType, 
                                               final String script );
  
  private void setUpPayload( final String widgetId ) {
    handleEndPayload();  
    handleFirstPayload();
    appendStartPayload( widgetId );
  }
  
  private void handleFirstPayload() {
    if( !isFirstPayload ) {
      appendPayloadSeparator();
    }
    isFirstPayload = false;
  }
  
  protected abstract void appendPayloadSeparator();

  protected abstract void appendStartPayload( final String widgetId );
  
  protected abstract void appendEndPayload();

}
