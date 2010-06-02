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

import java.util.HashMap;
import java.util.Map;

import org.eclipse.rwt.internal.service.ContextProvider;
import org.eclipse.rwt.internal.service.IServiceStateInfo;
import org.eclipse.rwt.lifecycle.WidgetUtil;
import org.eclipse.rwt.protocol.IWidgetSynchronizer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Widget;


public class WidgetSynchronizer implements IWidgetSynchronizer {
  
  private static final String SYNCHRONIZER_MAP_KEY = "synchronizerMapKey";
  private Widget widget;

  //prevent instantiation
  private WidgetSynchronizer( final Widget widget ) {
    this.widget = widget;
  }

  public static IWidgetSynchronizer getSynchronizerForWidget( 
    final Widget widget ) 
  {
    IServiceStateInfo stateInfo = ContextProvider.getStateInfo();
    IWidgetSynchronizer result;
    Map map = ( Map )stateInfo.getAttribute( SYNCHRONIZER_MAP_KEY );
    if( map == null ) {
      map = new HashMap();
      stateInfo.setAttribute( SYNCHRONIZER_MAP_KEY, map );
    }
    if( map.containsKey( widget ) ) {
      result = ( IWidgetSynchronizer )map.get( widget );
    } else {
      result = new WidgetSynchronizer( widget );
      map.put( widget, result );
    }
    return result;
  }
  
  private ProtocolMessageWriter getProtocolMessageWriter() {
    return ContextProvider.getStateInfo().getProtocolMessageWriter();
  }

  public void newWidget( final String[] styles ) {
    newWidget( styles, null );
  }

  public void newWidget( String[] styles, final Object[] arguments ) {
    ProtocolMessageWriter writer = getProtocolMessageWriter();
    String parentId = null;   
    if( widget instanceof Control ) {
      Composite parent = ( ( Control )widget ).getParent();
      if( parent != null ) {
        parentId = WidgetUtil.getId( parent );
      } else {
        parentId = null;
      }
    }
    writer.addConstructPayload( WidgetUtil.getId( widget ),
                                parentId,
                                widget.getClass().getName(), 
                                styles, 
                                arguments );
  }

  public void disposeWidget() {
    ProtocolMessageWriter writer = getProtocolMessageWriter();
    writer.addDestroyPaylod( WidgetUtil.getId( widget ) );
  }
  
  public void setWidgetProperty( final String name, final int value ) {
    setWidgetProperty( name, new Integer( value ) );
  }
  
  public void setWidgetProperty( final String name, final double value ) {
    setWidgetProperty( name, new Double( value ) );
  }
  
  public void setWidgetProperty( final String name, final boolean value ) {
    setWidgetProperty( name, new Boolean( value ) );
  }
  
  public void setWidgetProperty( final String name, final String value ) {
    setWidgetProperty( name, ( Object )value );    
  }

  public void setWidgetProperty( final String name, final Object value ) {
    ProtocolMessageWriter writer = getProtocolMessageWriter();
    writer.appendPayload( WidgetUtil.getId( widget ), 
                          IProtocolConstants.PAYLOAD_SYNCHRONIZE, 
                          name, 
                          value );
  }

  public void addListener( final String listenerName ) {
    appendListenPayload( listenerName, true );
  }

  public void removeListener( final String listenerName ) {
    appendListenPayload( listenerName, false );
  }

  private void appendListenPayload( final String listenerName, 
                                    final boolean shouldListen ) 
  {
    ProtocolMessageWriter writer = getProtocolMessageWriter();
    writer.appendPayload( WidgetUtil.getId( widget ), 
                          IProtocolConstants.PAYLOAD_LISTEN, 
                          listenerName, new Boolean( shouldListen ) );
  }

  public void call( final String methodName ) {
    call( methodName, null );
  }

  public void call( final String methodName, final Object[] arguments ) {
    ProtocolMessageWriter writer = getProtocolMessageWriter();
    writer.addExecutePayload( WidgetUtil.getId( widget ), 
                              methodName, 
                              arguments );
  }

  public void executeScript( final String scriptType, final String script ) {
    ProtocolMessageWriter writer = getProtocolMessageWriter();
    writer.addExecuteScript( WidgetUtil.getId( widget ), scriptType, script );
  }
   
}
