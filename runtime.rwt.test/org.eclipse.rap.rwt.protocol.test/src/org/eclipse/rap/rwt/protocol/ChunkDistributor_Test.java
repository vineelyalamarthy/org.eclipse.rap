/******************************************************************************* 
* Copyright (c) 2010 EclipseSource and others. All rights reserved. This
* program and the accompanying materials are made available under the terms of
* the Eclipse Public License v1.0 which accompanies this distribution, and is
* available at http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*   EclipseSource - initial API and implementation
*******************************************************************************/ 
package org.eclipse.rap.rwt.protocol;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.eclipse.rap.rwt.protocol.impl.FakeLCA;
import org.eclipse.rwt.Fixture;
import org.eclipse.rwt.internal.lifecycle.RWTRequestVersionControl;
import org.eclipse.rwt.internal.protocol.ChunkDistributor;
import org.eclipse.rwt.internal.protocol.IProtocolConstants;
import org.eclipse.rwt.internal.protocol.JsonMessageWriter;
import org.eclipse.rwt.internal.protocol.Processor;
import org.eclipse.rwt.internal.protocol.ProtocolMessageWriter;
import org.eclipse.rwt.lifecycle.ILifeCycleAdapter;
import org.eclipse.rwt.lifecycle.WidgetUtil;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Widget;
import org.json.JSONException;
import org.json.JSONObject;


public class ChunkDistributor_Test extends TestCase {
  
  protected void setUp() throws Exception {
    Fixture.setUp();
  }
  
  protected void tearDown() throws Exception {
    Fixture.tearDown();
  }
  
  public void testIsWidgetCalled() {
    StringWriter stringWriter = new StringWriter();
    PrintWriter printWriter = new PrintWriter( stringWriter );  
    ProtocolMessageWriter writer = new JsonMessageWriter( printWriter );
    Map properties = createDummyProperties();    
    writer.addSychronizePayload( "w3", properties );   
    writer.finish();
    FakeLCA lca = createFakeLCA();
    Display display = new Display();
    Shell shell = new Shell( display );
    Widget widget = createWidget( lca, shell );
    widget.setData( WidgetUtil.CUSTOM_WIDGET_ID, "w3" );
    Processor processor = new Processor( stringWriter.toString() );
    processor.addStreamListener( new ChunkDistributor( shell ) );
    processor.parse();
    assertTrue( lca.wasReadDataCalled() );
  }
  
  public void testAreTwoWidgetsCalled() {
    StringWriter stringWriter = new StringWriter();
    PrintWriter printWriter = new PrintWriter( stringWriter );  
    ProtocolMessageWriter writer = new JsonMessageWriter( printWriter );
    Map properties = createDummyProperties();    
    writer.addSychronizePayload( "w3", properties );
    writer.addSychronizePayload( "w4", properties );  
    writer.finish();
    FakeLCA lca = createFakeLCA();
    FakeLCA lca2 = createFakeLCA();
    Display display = new Display();
    Shell shell = new Shell( display );
    Widget widget = createWidget( lca, shell );
    widget.setData( WidgetUtil.CUSTOM_WIDGET_ID, "w3" );
    Widget widget2 = createWidget( lca2, shell );
    widget2.setData( WidgetUtil.CUSTOM_WIDGET_ID, "w4" );
    Processor processor = new Processor( stringWriter.toString() );
    processor.addStreamListener( new ChunkDistributor( shell ) );
    processor.parse();
    assertTrue( lca.wasReadDataCalled() );
    assertTrue( lca2.wasReadDataCalled() );
  }
  
  public void testMetaData() throws JSONException {
    JSONObject messageObject = new JSONObject();
    JSONObject metaObject = new JSONObject();
    messageObject.put( IProtocolConstants.MESSAGE_META, metaObject );
    metaObject.put( IProtocolConstants.KEY_REQUEST_COUNTER, 
                    RWTRequestVersionControl.nextRequestId() );
    Processor processor = new Processor( messageObject.toString() );
    ChunkDistributor distributor = new ChunkDistributor( null );
    processor.addStreamListener( distributor );
    processor.parse();
    RWTRequestVersionControl.beforeService();
    assertTrue( distributor.isRequestValid() );
  }
  
  public void testIsWidgetCalledWithFireEvent() {
    StringWriter stringWriter = new StringWriter();
    PrintWriter printWriter = new PrintWriter( stringWriter );  
    ProtocolMessageWriter writer = new JsonMessageWriter( printWriter );
    writer.addFireEventPayload( "w3", "org.swt.selection" );  
    writer.finish();
    FakeLCA lca = createFakeLCA();
    Display display = new Display();
    Shell shell = new Shell( display );
    Widget widget = createWidget( lca, shell );
    widget.setData( WidgetUtil.CUSTOM_WIDGET_ID, "w3" );
    Processor processor = new Processor( stringWriter.toString() );
    processor.addStreamListener( new ChunkDistributor( shell ) );
    processor.parse();
    assertTrue( lca.wasProcessEventcalled() );
  }

  private Map createDummyProperties() {
    Map properties = new HashMap();
    properties.put( "prop1", "value1" );
    properties.put( "prop2", "value2" );
    properties.put( "prop3", "value2" );
    return properties;
  }

  private Widget createWidget( final FakeLCA lca, final Shell parent ) {
    Composite widget = new Composite( parent, 0 ) {
      public Object getAdapter( Class adapter ) {
        Object result = null;
        if( adapter == ILifeCycleAdapter.class ) {
          result = lca;
        } else {
          result = super.getAdapter( adapter );
        }
        return result;
      }
    };
    return widget;
  }

  private FakeLCA createFakeLCA() {
    FakeLCA result = new FakeLCA();    
    return result;
  }


  
}
