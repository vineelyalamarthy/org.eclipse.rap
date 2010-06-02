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

import junit.framework.TestCase;

import org.eclipse.rwt.Fixture;
import org.eclipse.rwt.internal.lifecycle.DisplayUtil;
import org.eclipse.rwt.internal.protocol.IProtocolConstants;
import org.eclipse.rwt.internal.protocol.JsonMessageWriter;
import org.eclipse.rwt.internal.protocol.ProtocolMessageWriter;
import org.eclipse.rwt.internal.service.ContextProvider;
import org.eclipse.rwt.internal.service.IServiceStateInfo;
import org.eclipse.rwt.lifecycle.WidgetUtil;
import org.eclipse.rwt.protocol.IWidgetSynchronizer;
import org.eclipse.rwt.protocol.WidgetSynchronizerFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class WidgetSynchronizer_Test extends TestCase {
  
  private StringWriter stringWriter;

  protected void setUp() throws Exception {
    Fixture.setUp();
    setUpProtocolMessageWriter();
  }
  
  protected void tearDown() throws Exception {
    Fixture.tearDown();
  }
  
  public void testInstantiation() {
    Display display = new Display();
    Shell shell = new Shell( display );
    Button button = new Button( shell, SWT.PUSH );
    IWidgetSynchronizer broker 
      = WidgetSynchronizerFactory.getSynchronizerForWidget( shell );
    assertNotNull( broker );
    IWidgetSynchronizer broker2 
      = WidgetSynchronizerFactory.getSynchronizerForWidget( button );
    assertNotSame( broker, broker2 );
    broker2 = WidgetSynchronizerFactory.getSynchronizerForWidget( shell );
    assertSame( broker, broker2 );
  }
  
  public void testNewWidgetWithNullParams() {    
    Display display = new Display();
    Shell shell = new Shell( display );
    IWidgetSynchronizer broker 
      = WidgetSynchronizerFactory.getSynchronizerForWidget( shell );
    broker.newWidget( new String[] { "SHELL_TRIM" } );
    closeProtocolWriter();
    String messageString = stringWriter.getBuffer().toString();
    try {
      JSONObject message = new JSONObject( messageString );
      JSONArray widgetArray 
        = message.getJSONArray( IProtocolConstants.MESSAGE_WIDGETS );
      JSONObject widgetObject = widgetArray.getJSONObject( 0 );
      String actualWidgetId 
        = widgetObject.getString( IProtocolConstants.WIDGETS_ID );
      assertEquals( WidgetUtil.getId( shell ), actualWidgetId );
      String actualPayloadType 
        = widgetObject.getString( IProtocolConstants.WIDGETS_TYPE );
      assertEquals( IProtocolConstants.PAYLOAD_CONSTRUCT, actualPayloadType );
      JSONObject payload 
        = widgetObject.getJSONObject( IProtocolConstants.WIDGETS_PAYLOAD );
      String type = payload.getString( IProtocolConstants.KEY_WIDGET_TYPE );
      assertEquals( shell.getClass().getName(), type );
    } catch( final JSONException e ) {
      fail( e.getMessage() );
    }
  }

  private String getDisplayId( final Display display ) {
    return DisplayUtil.getId( display );
  }
  
  public void testNewWidgetWithParams() {    
    Display display = new Display();
    Shell shell = new Shell( display );
    IWidgetSynchronizer broker 
      = WidgetSynchronizerFactory.getSynchronizerForWidget( shell );
    broker.newWidget( new String[] { "SHELL_TRIM" }, 
                      new Object[] { new Integer( 1 ), new Boolean( true ) } );
    closeProtocolWriter();
    String messageString = stringWriter.getBuffer().toString();
    try {
      JSONObject message = new JSONObject( messageString );
      JSONArray widgetArray 
        = message.getJSONArray( IProtocolConstants.MESSAGE_WIDGETS );
      JSONObject widgetObject = widgetArray.getJSONObject( 0 );
      String actualWidgetId 
        = widgetObject.getString( IProtocolConstants.WIDGETS_ID );
      assertEquals( WidgetUtil.getId( shell ), actualWidgetId );
      String actualPayloadType 
        = widgetObject.getString( IProtocolConstants.WIDGETS_TYPE );
      assertEquals( IProtocolConstants.PAYLOAD_CONSTRUCT, actualPayloadType );
      JSONObject payload 
        = widgetObject.getJSONObject( IProtocolConstants.WIDGETS_PAYLOAD );
      String type = payload.getString( IProtocolConstants.KEY_WIDGET_TYPE );
      assertEquals( shell.getClass().getName(), type );
      JSONArray params 
        = payload.getJSONArray( IProtocolConstants.KEY_PARAMETER_LIST );
      assertEquals( 1, params.getInt( 0 ) );
      assertEquals( true, params.getBoolean( 1 ) );
    } catch( final JSONException e ) {
      fail( e.getMessage() );
    }
  }
  
  public void testNewWidgetStyles() {
    Display display = new Display();
    Shell shell = new Shell( display );
    Button button = new Button( shell, SWT.PUSH | SWT.BORDER );
    IWidgetSynchronizer broker 
      = WidgetSynchronizerFactory.getSynchronizerForWidget( button );
    broker.newWidget( new String[] { "PUSH", "BORDER" } );
    closeProtocolWriter();
    String messageString = stringWriter.getBuffer().toString();
    try {
      JSONObject message = new JSONObject( messageString );
      JSONArray widgetArray 
        = message.getJSONArray( IProtocolConstants.MESSAGE_WIDGETS );
      JSONObject widgetObject = widgetArray.getJSONObject( 0 );
      String actualWidgetId 
        = widgetObject.getString( IProtocolConstants.WIDGETS_ID );
      assertEquals( WidgetUtil.getId( button ), actualWidgetId );
      String actualPayloadType 
        = widgetObject.getString( IProtocolConstants.WIDGETS_TYPE );
      assertEquals( IProtocolConstants.PAYLOAD_CONSTRUCT, actualPayloadType );
      JSONObject payload 
        = widgetObject.getJSONObject( IProtocolConstants.WIDGETS_PAYLOAD );
      String parentId = payload.getString( IProtocolConstants.KEY_PARENT_ID );
      assertEquals( WidgetUtil.getId( shell ), parentId );
      String type = payload.getString( IProtocolConstants.KEY_WIDGET_TYPE );
      assertEquals( button.getClass().getName(), type );
      JSONArray styles 
        = payload.getJSONArray( IProtocolConstants.KEY_WIDGET_STYLE );
      assertEquals( "PUSH", styles.getString( 0 ) );
      assertEquals( "BORDER", styles.getString( 1 ) );
    } catch( final JSONException e ) {
      fail( e.getMessage() );
    }
  }
  
  public void testSetProperty() {
    Display display = new Display();
    Shell shell = new Shell( display );
    IWidgetSynchronizer broker 
      = WidgetSynchronizerFactory.getSynchronizerForWidget( shell );
    broker.setWidgetProperty( "key", ( Object )"value" );
    broker.setWidgetProperty( "key2", 2 );
    broker.setWidgetProperty( "key3", 3.5 );
    broker.setWidgetProperty( "key4", true );
    broker.setWidgetProperty( "key5", "aString" );    
    closeProtocolWriter();
    String messageString = stringWriter.getBuffer().toString();
    try {
      JSONObject message = new JSONObject( messageString );
      JSONArray widgetArray 
        = message.getJSONArray( IProtocolConstants.MESSAGE_WIDGETS );
      JSONObject widgetObject = widgetArray.getJSONObject( 0 );
      String actualWidgetId 
        = widgetObject.getString( IProtocolConstants.WIDGETS_ID );
      assertEquals( WidgetUtil.getId( shell ), actualWidgetId );
      String actualPayloadType 
        = widgetObject.getString( IProtocolConstants.WIDGETS_TYPE );
      assertEquals( IProtocolConstants.PAYLOAD_SYNCHRONIZE, actualPayloadType );
      JSONObject payload 
        = widgetObject.getJSONObject( IProtocolConstants.WIDGETS_PAYLOAD );
      assertEquals( "value", payload.getString( "key" ) );
      assertEquals( 2, payload.getInt( "key2" ) );
      assertEquals( 3.5, payload.getDouble( "key3" ), 0.0 );
      assertEquals( true, payload.getBoolean( "key4" ) );
      assertEquals( "aString", payload.getString( "key5" ) );
    } catch( final JSONException e ) {
      fail( e.getMessage() );
    }
  }
  
  public void testDispose() {
    Display display = new Display();
    Shell shell = new Shell( display );
    IWidgetSynchronizer broker 
      = WidgetSynchronizerFactory.getSynchronizerForWidget( shell );    
    broker.disposeWidget();
    closeProtocolWriter();
    String messageString = stringWriter.getBuffer().toString();
    try {
      JSONObject message = new JSONObject( messageString );
      JSONArray widgetArray 
        = message.getJSONArray( IProtocolConstants.MESSAGE_WIDGETS );
      JSONObject widgetObject = widgetArray.getJSONObject( 0 );
      String actualWidgetId 
        = widgetObject.getString( IProtocolConstants.WIDGETS_ID );
      assertEquals( WidgetUtil.getId( shell ), actualWidgetId );
      String actualPayloadType 
        = widgetObject.getString( IProtocolConstants.WIDGETS_TYPE );
      assertEquals( IProtocolConstants.PAYLOAD_DESTROY, actualPayloadType );
      Object payload 
        = widgetObject.get( IProtocolConstants.WIDGETS_PAYLOAD );
      assertSame( JSONObject.NULL, payload );
    } catch( final JSONException e ) {
      fail( e.getMessage() );
    }
  }
  
  public void testAddListener() {
    Display display = new Display();
    Shell shell = new Shell( display );
    IWidgetSynchronizer broker 
      = WidgetSynchronizerFactory.getSynchronizerForWidget( shell );    
    broker.addListener( "selection" );
    broker.addListener( "fake" );
    closeProtocolWriter();
    String messageString = stringWriter.getBuffer().toString();
    try {
      JSONObject message = new JSONObject( messageString );
      JSONArray widgetArray 
        = message.getJSONArray( IProtocolConstants.MESSAGE_WIDGETS );
      JSONObject widgetObject = widgetArray.getJSONObject( 0 );
      String actualWidgetId 
        = widgetObject.getString( IProtocolConstants.WIDGETS_ID );
      assertEquals( WidgetUtil.getId( shell ), actualWidgetId );
      String actualPayloadType 
        = widgetObject.getString( IProtocolConstants.WIDGETS_TYPE );
      assertEquals( IProtocolConstants.PAYLOAD_LISTEN, actualPayloadType );
      JSONObject payload 
        = widgetObject.getJSONObject( IProtocolConstants.WIDGETS_PAYLOAD );
      assertTrue( payload.getBoolean( "selection" ) );
      assertTrue( payload.getBoolean( "fake" ) );
    } catch( final JSONException e ) {
      fail( e.getMessage() );
    }
  }
  
  public void testRemoveListener() {
    Display display = new Display();
    Shell shell = new Shell( display );
    IWidgetSynchronizer broker 
      = WidgetSynchronizerFactory.getSynchronizerForWidget( shell );   
    broker.removeListener( "selection" );
    broker.removeListener( "fake" );
    broker.addListener( "fake2" );
    closeProtocolWriter();
    String messageString = stringWriter.getBuffer().toString();
    try {
      JSONObject message = new JSONObject( messageString );
      JSONArray widgetArray 
        = message.getJSONArray( IProtocolConstants.MESSAGE_WIDGETS );
      JSONObject widgetObject = widgetArray.getJSONObject( 0 );
      String actualWidgetId 
        = widgetObject.getString( IProtocolConstants.WIDGETS_ID );
      assertEquals( WidgetUtil.getId( shell ), actualWidgetId );
      String actualPayloadType 
        = widgetObject.getString( IProtocolConstants.WIDGETS_TYPE );
      assertEquals( IProtocolConstants.PAYLOAD_LISTEN, actualPayloadType );
      JSONObject payload 
        = widgetObject.getJSONObject( IProtocolConstants.WIDGETS_PAYLOAD );
      assertFalse( payload.getBoolean( "selection" ) );
      assertFalse( payload.getBoolean( "fake" ) );
      assertTrue( payload.getBoolean( "fake2" ) );
    } catch( final JSONException e ) {
      fail( e.getMessage() );
    }
  }
  
  public void testCall() {
    Display display = new Display();
    Shell shell = new Shell( display );
    IWidgetSynchronizer broker 
      = WidgetSynchronizerFactory.getSynchronizerForWidget( shell );    
    broker.call( "method" );
    broker.call( "method2", new Object[] { "a", new Integer( 3 ) } );
    closeProtocolWriter();
    String messageString = stringWriter.getBuffer().toString();
    try {
      JSONObject message = new JSONObject( messageString );
      JSONArray widgetArray 
        = message.getJSONArray( IProtocolConstants.MESSAGE_WIDGETS );
      JSONObject widgetObject = widgetArray.getJSONObject( 0 );
      String actualWidgetId 
        = widgetObject.getString( IProtocolConstants.WIDGETS_ID );
      assertEquals( WidgetUtil.getId( shell ), actualWidgetId );
      String actualPayloadType 
        = widgetObject.getString( IProtocolConstants.WIDGETS_TYPE );
      assertEquals( IProtocolConstants.PAYLOAD_EXECUTE, actualPayloadType );
      JSONObject payload 
        = widgetObject.getJSONObject( IProtocolConstants.WIDGETS_PAYLOAD );
      String method = payload.getString( IProtocolConstants.KEY_METHODNAME );
      assertEquals( "method", method );
      Object params = payload.get( IProtocolConstants.KEY_PARAMETER_LIST );
      assertSame( JSONObject.NULL, params );
      widgetObject = widgetArray.getJSONObject( 1 );
      actualWidgetId 
        = widgetObject.getString( IProtocolConstants.WIDGETS_ID );
      assertEquals( WidgetUtil.getId( shell ), actualWidgetId );
      actualPayloadType 
        = widgetObject.getString( IProtocolConstants.WIDGETS_TYPE );
      assertEquals( IProtocolConstants.PAYLOAD_EXECUTE, actualPayloadType );
      payload 
        = widgetObject.getJSONObject( IProtocolConstants.WIDGETS_PAYLOAD );
      method = payload.getString( IProtocolConstants.KEY_METHODNAME );
      assertEquals( "method2", method );
      JSONArray list 
        = payload.getJSONArray( IProtocolConstants.KEY_PARAMETER_LIST );
      assertEquals( "a", list.getString( 0 ) );
      assertEquals( 3, list.getInt( 1 ) );
    } catch( final JSONException e ) {
      fail( e.getMessage() );
    }
  }
  
  public void testExecuteScript() {
    Display display = new Display();
    Shell shell = new Shell( display );
    String expected = "var x = 5;";
    IWidgetSynchronizer broker 
      = WidgetSynchronizerFactory.getSynchronizerForWidget( shell );    
    broker.executeScript( "text/javascript", expected );
    closeProtocolWriter();
    String messageString = stringWriter.getBuffer().toString();
    try {
      JSONObject message = new JSONObject( messageString );
      JSONArray widgetArray 
        = message.getJSONArray( IProtocolConstants.MESSAGE_WIDGETS );
      JSONObject widgetObject = widgetArray.getJSONObject( 0 );
      String actualWidgetId 
        = widgetObject.getString( IProtocolConstants.WIDGETS_ID );
      assertEquals( WidgetUtil.getId( shell ), actualWidgetId );
      String actualPayloadType 
        = widgetObject.getString( IProtocolConstants.WIDGETS_TYPE );
      assertEquals( IProtocolConstants.PAYLOAD_EXECUTE_SCRIPT, 
                    actualPayloadType );
      JSONObject payload 
        = widgetObject.getJSONObject( IProtocolConstants.WIDGETS_PAYLOAD );
      String type = payload.getString( IProtocolConstants.KEY_SCRIPT_TYPE );
      assertEquals( type, "text/javascript" );
      String script = payload.getString( IProtocolConstants.KEY_SCRIPT );
      assertEquals( expected, script );
    } catch( final JSONException e ) {
      fail( e.getMessage() );
    }
  }

  private void closeProtocolWriter() {
    IServiceStateInfo stateInfo = ContextProvider.getStateInfo();
    ProtocolMessageWriter writer = stateInfo.getProtocolMessageWriter();
    writer.finish();
  }

  private void setUpProtocolMessageWriter() {
    stringWriter = new StringWriter();
    PrintWriter printWriter = new PrintWriter( stringWriter );    
    ProtocolMessageWriter writer = new JsonMessageWriter( printWriter );
    ContextProvider.getStateInfo().setProtocolMessageWriter( writer );
    
  }
}
