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

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.eclipse.rwt.Fixture;
import org.eclipse.rwt.RWT;
import org.eclipse.rwt.internal.lifecycle.DisplayUtil;
import org.eclipse.rwt.internal.lifecycle.RWTRequestVersionControl;
import org.eclipse.rwt.internal.protocol.IProtocolConstants;
import org.eclipse.rwt.internal.protocol.JsonMessageWriter;
import org.eclipse.rwt.internal.protocol.ProtocolMessageWriter;
import org.eclipse.rwt.lifecycle.WidgetUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class JsonMessageWriter_Test extends TestCase {
  
  protected void setUp() throws Exception {
    Fixture.setUp();
  }
  
  protected void tearDown() throws Exception {
    Fixture.tearDown();
  } 
  
  public void testEmptyMessage() throws IOException {
    StringWriter stringWriter = new StringWriter();
    PrintWriter printWriter = new PrintWriter( stringWriter );
    ProtocolMessageWriter writer = new JsonMessageWriter( printWriter );
    writer.finish();
    String actual = stringWriter.getBuffer().toString();
    try {
      JSONObject jsonObject = new JSONObject( actual );
      JSONObject meta 
        = jsonObject.getJSONObject( IProtocolConstants.MESSAGE_META );
      int counter = meta.getInt( IProtocolConstants.META_REQUEST_COUNTER );
      assertEquals( getRequestCounter(), counter );
      JSONArray widgets 
        = jsonObject.getJSONArray( IProtocolConstants.MESSAGE_WIDGETS );
      assertEquals( 0, widgets.length() );
    } catch( JSONException e ) {
      fail( e.getMessage() );
    }
  }
  
  public void testMessageWithExecute() throws IOException, JSONException {
    StringWriter stringWriter = new StringWriter();
    PrintWriter printWriter = new PrintWriter( stringWriter );
    ProtocolMessageWriter writer = new JsonMessageWriter( printWriter );
    String methodName = "methodName";
    Display display = new Display();
    Shell shell = new Shell( display );
    writer.addExecutePayload( WidgetUtil.getId( shell ),
                              methodName, 
                              new Object[] { "a", "b" } );
    String widgetId = WidgetUtil.getId( shell );
    String actual = stringWriter.getBuffer().toString();
    JSONObject message = new JSONObject( actual + "]}" );
    JSONArray widgets 
      = message.getJSONArray( IProtocolConstants.MESSAGE_WIDGETS );
    JSONObject widgetObject = widgets.getJSONObject( 0 );
    String actualId = widgetObject.getString( IProtocolConstants.WIDGETS_ID );
    assertEquals( widgetId, actualId );
    String type = widgetObject.getString( IProtocolConstants.WIDGETS_TYPE );
    assertEquals( IProtocolConstants.PAYLOAD_EXECUTE, type );
    JSONObject payload 
      = widgetObject.getJSONObject( IProtocolConstants.WIDGETS_PAYLOAD );
    String actualMethod 
      = payload.getString( IProtocolConstants.KEY_METHODNAME );
    assertEquals( methodName, actualMethod );
    JSONArray actualParams 
      = payload.getJSONArray( IProtocolConstants.KEY_PARAMETER_LIST );
    assertEquals( "a", actualParams.getString( 0 ) );
    assertEquals( "b", actualParams.getString( 1 ) );
    Object[] array 
      = new Object[] { new Integer( 5 ), "b", new Boolean( false ) }; 
    writer.addExecutePayload( WidgetUtil.getId( shell ), 
                              methodName, 
                              array );    
    writer.finish();
    actual = stringWriter.getBuffer().toString();
    message = new JSONObject( actual );
    JSONArray widgetArray 
      = message.getJSONArray( IProtocolConstants.MESSAGE_WIDGETS );
    widgetObject = widgetArray.getJSONObject( 1 );
    type = widgetObject.getString( IProtocolConstants.WIDGETS_TYPE );
    assertEquals( IProtocolConstants.PAYLOAD_EXECUTE, type );
    actualId = widgetObject.getString( IProtocolConstants.WIDGETS_ID );
    assertEquals( widgetId, actualId );
    payload = widgetObject.getJSONObject( IProtocolConstants.WIDGETS_PAYLOAD );
    actualMethod = payload.getString( IProtocolConstants.KEY_METHODNAME );
    assertEquals( methodName, actualMethod );
    JSONArray params 
      = payload.getJSONArray( IProtocolConstants.KEY_PARAMETER_LIST );
    assertEquals( 5, params.getInt( 0 ) );
    assertEquals( "b", params.getString( 1 ) );
    assertFalse( params.getBoolean( 2 ) );
  }
  
  public void testMessageWithConstruct() throws IOException, JSONException {
    StringWriter stringWriter = new StringWriter();
    PrintWriter printWriter = new PrintWriter( stringWriter );
    ProtocolMessageWriter writer = new JsonMessageWriter( printWriter );
    Display display = new Display();
    Shell shell = new Shell( display );
    Button button = new Button( shell, SWT.PUSH );
    writer.addConstructPayload( WidgetUtil.getId( shell ),
                                DisplayUtil.getId( display ),
                                "org.Text",
                                new String[] { "TRIM" }, 
                                new Object[] { "a", "b" } );
    String parentId = "";
    if( shell.getParent() != null ) {
      parentId = WidgetUtil.getId( shell.getParent() );
    } else {
      parentId = DisplayUtil.getId( display );
    }
    String widgetId = WidgetUtil.getId( shell );    
    String actual = stringWriter.getBuffer().toString();
    JSONObject message = new JSONObject( actual + "]}" );
    JSONArray widgetArray 
      = message.getJSONArray( IProtocolConstants.MESSAGE_WIDGETS );
    JSONObject widgetObject = widgetArray.getJSONObject( 0 );
    String type = widgetObject.getString( IProtocolConstants.WIDGETS_TYPE );
    assertEquals( IProtocolConstants.PAYLOAD_CONSTRUCT, type );
    String actualId = widgetObject.getString( IProtocolConstants.WIDGETS_ID );
    assertEquals( widgetId, actualId );
    JSONObject payload 
      = widgetObject.getJSONObject( IProtocolConstants.WIDGETS_PAYLOAD );
    String actualParentId 
      = payload.getString( IProtocolConstants.KEY_PARENT_ID );
    assertEquals( parentId, actualParentId );
    String actualType 
      = payload.getString( IProtocolConstants.KEY_WIDGET_TYPE );
    assertEquals( "org.Text", actualType );
    JSONArray actualStyle 
      = payload.getJSONArray( IProtocolConstants.KEY_WIDGET_STYLE );
    assertEquals( "TRIM", actualStyle.getString( 0 ) );
    JSONArray actualParams 
      = payload.getJSONArray( IProtocolConstants.KEY_PARAMETER_LIST );
    assertEquals( "a", actualParams.getString( 0 ) );
    assertEquals( "b", actualParams.getString( 1 ) );
    writer.addConstructPayload( WidgetUtil.getId( button ), 
                                WidgetUtil.getId( shell ), 
                                "org.Shell",
                                new String[] { "PUSH", "BORDER" }, 
                                new Object[] { "a", new Boolean( true ) } );
    writer.finish();
    actual = stringWriter.getBuffer().toString();
    message = new JSONObject( actual );
    widgetArray = message.getJSONArray( IProtocolConstants.MESSAGE_WIDGETS );
    widgetObject = widgetArray.getJSONObject( 1 );
    type = widgetObject.getString( IProtocolConstants.WIDGETS_TYPE );
    assertEquals( IProtocolConstants.PAYLOAD_CONSTRUCT, type );
    actualId = widgetObject.getString( IProtocolConstants.WIDGETS_ID );
    assertEquals( WidgetUtil.getId( button ), actualId );
    payload = widgetObject.getJSONObject( IProtocolConstants.WIDGETS_PAYLOAD );
    JSONArray styles 
      = payload.getJSONArray( IProtocolConstants.KEY_WIDGET_STYLE );
    assertEquals( "PUSH", styles.getString( 0 ) );
    assertEquals( "BORDER", styles.getString( 1 ) );
    String actualParent 
      = payload.getString( IProtocolConstants.KEY_PARENT_ID );
    assertEquals( WidgetUtil.getId( shell ), actualParent );
    JSONArray params 
      = payload.getJSONArray( IProtocolConstants.KEY_PARAMETER_LIST );
    assertTrue( params.getBoolean( 1 ) ); 
  }
  
  public void testMessageWithWrongArguments() throws IOException {
    StringWriter stringWriter = new StringWriter();
    PrintWriter printWriter = new PrintWriter( stringWriter );
    ProtocolMessageWriter writer = new JsonMessageWriter( printWriter );
    Display display = new Display();
    Shell shell = new Shell( display );
    Button button = new Button( shell, SWT.PUSH );
    boolean isExceptionThrown = false;
    try {
      writer.addConstructPayload( DisplayUtil.getId( display ), 
                                  WidgetUtil.getId( shell ), 
                                  "org.Text",
                                  new String[] { "TRIM" }, 
                                  new Object[] { "a", button } );
      
    } catch ( final IllegalArgumentException e ) {
      isExceptionThrown = true;
    }
    assertTrue( isExceptionThrown );
  } 
    
  
  public void testMessageWithDestroy() throws IOException, JSONException {
    StringWriter stringWriter = new StringWriter();
    PrintWriter printWriter = new PrintWriter( stringWriter );
    ProtocolMessageWriter writer = new JsonMessageWriter( printWriter );
    Display display = new Display();
    Shell shell = new Shell( display );
    Button button = new Button( shell, SWT.PUSH );
    writer.addDestroyPayload( WidgetUtil.getId( button ) );
    String widgetId = WidgetUtil.getId( button );    
    String actual = stringWriter.getBuffer().toString();
    JSONObject message = new JSONObject( actual + "]}" );
    JSONArray widgetArray 
      = message.getJSONArray( IProtocolConstants.MESSAGE_WIDGETS );
    JSONObject widgetObject = widgetArray.getJSONObject( 0 );
    String type = widgetObject.getString( IProtocolConstants.WIDGETS_TYPE );
    assertEquals( IProtocolConstants.PAYLOAD_DESTROY, type );
    String actualId = widgetObject.getString( IProtocolConstants.WIDGETS_ID );
    assertEquals( widgetId, actualId );
    Object payload = widgetObject.get( IProtocolConstants.WIDGETS_PAYLOAD );
    assertEquals( JSONObject.NULL, payload );
    writer.addDestroyPayload( WidgetUtil.getId( shell ) );
    writer.finish();
    String shellId = WidgetUtil.getId( shell );    
    actual = stringWriter.getBuffer().toString();
    message = new JSONObject( actual );
    widgetArray = message.getJSONArray( IProtocolConstants.MESSAGE_WIDGETS );
    widgetObject = widgetArray.getJSONObject( 1 );
    type = widgetObject.getString( IProtocolConstants.WIDGETS_TYPE );
    assertEquals( IProtocolConstants.PAYLOAD_DESTROY, type );
    actualId = widgetObject.getString( IProtocolConstants.WIDGETS_ID );
    assertEquals( shellId, actualId );
    payload = widgetObject.get( IProtocolConstants.WIDGETS_PAYLOAD );
    assertEquals( JSONObject.NULL, payload );
  }
  
  public void testMessageWithListen() throws IOException, JSONException {
    StringWriter stringWriter = new StringWriter();
    PrintWriter printWriter = new PrintWriter( stringWriter );
    ProtocolMessageWriter writer = new JsonMessageWriter( printWriter );
    Display display = new Display();
    Shell shell = new Shell( display );
    Button button = new Button( shell, SWT.PUSH );
    Map listeners = new HashMap();
    listeners.put( "selection", new Boolean( false ) );
    listeners.put( "focus", new Boolean( true ) );
    listeners.put( "fake", new Boolean( true ) );
    writer.addListenPayload( WidgetUtil.getId( button ), listeners );
    String widgetId = WidgetUtil.getId( button );    
    String actual = stringWriter.getBuffer().toString();
    JSONObject message = new JSONObject( actual + "]}" );
    JSONArray widgetArray 
      = message.getJSONArray( IProtocolConstants.MESSAGE_WIDGETS );
    JSONObject widgetObject = widgetArray.getJSONObject( 0 );
    String actualId = widgetObject.getString( IProtocolConstants.WIDGETS_ID );
    assertEquals( widgetId, actualId );
    String type = widgetObject.getString( IProtocolConstants.WIDGETS_TYPE );
    assertEquals( IProtocolConstants.PAYLOAD_LISTEN, type );
    JSONObject payload 
      = widgetObject.getJSONObject( IProtocolConstants.WIDGETS_PAYLOAD );
    assertFalse( payload.getBoolean( "selection" ) );
    assertTrue( payload.getBoolean( "focus" ) );
    assertTrue( payload.getBoolean( "fake" ) );
  }
  
  public void testMessageWithFireEvent() throws IOException, JSONException {
    StringWriter stringWriter = new StringWriter();
    PrintWriter printWriter = new PrintWriter( stringWriter );
    ProtocolMessageWriter writer = new JsonMessageWriter( printWriter );
    Display display = new Display();
    Shell shell = new Shell( display );
    Button button = new Button( shell, SWT.PUSH );
    writer.addFireEventPayload( WidgetUtil.getId( button ), "selection" );
    String widgetId = WidgetUtil.getId( button );    
    String actual = stringWriter.getBuffer().toString();
    JSONObject message = new JSONObject( actual + "]}" );
    JSONArray widgetArray 
      = message.getJSONArray( IProtocolConstants.MESSAGE_WIDGETS );
    JSONObject widgetObject = widgetArray.getJSONObject( 0 );
    String type = widgetObject.getString( IProtocolConstants.WIDGETS_TYPE );
    assertEquals( IProtocolConstants.PAYLOAD_FIRE_EVENT, type );
    String actualId = widgetObject.getString( IProtocolConstants.WIDGETS_ID );
    assertEquals( widgetId, actualId );
    JSONObject payload 
      = widgetObject.getJSONObject( IProtocolConstants.WIDGETS_PAYLOAD );
    String actualEvent = payload.getString( IProtocolConstants.KEY_EVENT );
    assertEquals( "selection", actualEvent );
    writer.addFireEventPayload( WidgetUtil.getId( button ), "focus" );
    writer.finish();
    actual = stringWriter.getBuffer().toString();
    message = new JSONObject( actual );
    widgetArray = message.getJSONArray( IProtocolConstants.MESSAGE_WIDGETS );
    widgetObject = widgetArray.getJSONObject( 1 );
    type = widgetObject.getString( IProtocolConstants.WIDGETS_TYPE );
    assertEquals( IProtocolConstants.PAYLOAD_FIRE_EVENT, type );
    actualId = widgetObject.getString( IProtocolConstants.WIDGETS_ID );
    assertEquals( widgetId, actualId );
    payload = widgetObject.getJSONObject( IProtocolConstants.WIDGETS_PAYLOAD );
    actualEvent = payload.getString( IProtocolConstants.KEY_EVENT );
    assertEquals( "focus", actualEvent );
  }
  
  public void testMessageWithExecuteScript() throws IOException, JSONException {
    StringWriter stringWriter = new StringWriter();
    PrintWriter printWriter = new PrintWriter( stringWriter );
    ProtocolMessageWriter writer = new JsonMessageWriter( printWriter );
    Display display = new Display();
    Shell shell = new Shell( display );
    Button button = new Button( shell, SWT.PUSH );
    String script = "var c = 4; c++;";
    String scriptType = "text/javascript";
    writer.addExecuteScript( WidgetUtil.getId( button ), scriptType, script );
    String widgetId = WidgetUtil.getId( button );    
    String actual = stringWriter.getBuffer().toString();
    JSONObject message = new JSONObject( actual + "]}" );
    JSONArray widgetArray 
      = message.getJSONArray( IProtocolConstants.MESSAGE_WIDGETS );
    JSONObject widgetObject = widgetArray.getJSONObject( 0 );
    String type = widgetObject.getString( IProtocolConstants.WIDGETS_TYPE );
    assertEquals( IProtocolConstants.PAYLOAD_EXECUTE_SCRIPT, type );
    String actualId = widgetObject.getString( IProtocolConstants.WIDGETS_ID );
    assertEquals( widgetId, actualId );
    JSONObject payload 
      = widgetObject.getJSONObject( IProtocolConstants.WIDGETS_PAYLOAD );
    String actualType 
      = payload.getString( IProtocolConstants.KEY_SCRIPT_TYPE );
    assertEquals( scriptType, actualType );
    String actualScript = payload.getString( IProtocolConstants.KEY_SCRIPT );
    assertEquals( script, actualScript );
    scriptType = "text/vb";
    script = "really bad VB;";
    writer.addExecuteScript( WidgetUtil.getId( shell ), scriptType, script );
    writer.finish();
    widgetId = WidgetUtil.getId( shell );    
    actual = stringWriter.getBuffer().toString();
    message = new JSONObject( actual );
    widgetArray = message.getJSONArray( IProtocolConstants.MESSAGE_WIDGETS );
    widgetObject = widgetArray.getJSONObject( 1 );
    type = widgetObject.getString( IProtocolConstants.WIDGETS_TYPE );
    assertEquals( IProtocolConstants.PAYLOAD_EXECUTE_SCRIPT, type );
    actualId = widgetObject.getString( IProtocolConstants.WIDGETS_ID );
    assertEquals( widgetId, actualId );
    payload = widgetObject.getJSONObject( IProtocolConstants.WIDGETS_PAYLOAD );
    actualType 
      = payload.getString( IProtocolConstants.KEY_SCRIPT_TYPE );
    assertEquals( scriptType, actualType );
    actualScript = payload.getString( IProtocolConstants.KEY_SCRIPT );
    assertEquals( script, actualScript );
  }
  
  public void testMessageWithSynchronize() throws IOException, JSONException {
    StringWriter stringWriter = new StringWriter();
    PrintWriter printWriter = new PrintWriter( stringWriter );
    ProtocolMessageWriter writer = new JsonMessageWriter( printWriter );
    Display display = new Display();
    Shell shell = new Shell( display );
    Button button = new Button( shell, SWT.PUSH );
    Map properties = new HashMap();
    properties.put( "text", "newText" );
    properties.put( "image", "aUrl" );
    properties.put( "fake", new Integer( 1 ) );
    writer.addSychronizePayload( WidgetUtil.getId( button ), properties );
    String widgetId = WidgetUtil.getId( button );    
    String actual = stringWriter.getBuffer().toString();
    JSONObject message = new JSONObject( actual + "]}" );
    JSONArray widgetArray 
      = message.getJSONArray( IProtocolConstants.MESSAGE_WIDGETS );
    JSONObject widgetObject = widgetArray.getJSONObject( 0 );
    String type = widgetObject.getString( IProtocolConstants.WIDGETS_TYPE );
    assertEquals( IProtocolConstants.PAYLOAD_SYNCHRONIZE, type );
    String actualId = widgetObject.getString( IProtocolConstants.WIDGETS_ID );
    assertEquals( widgetId, actualId );
    JSONObject payload 
      = widgetObject.getJSONObject( IProtocolConstants.WIDGETS_PAYLOAD );
    assertEquals( "newText", payload.getString( "text" ) );
    assertEquals( "aUrl", payload.getString( "image" ) );
    assertEquals( 1, payload.getInt( "fake" ) );
    properties.remove( "image" );
    properties.put( "state", new Boolean( true ) );
    writer.addSychronizePayload( WidgetUtil.getId( button ), properties );
    writer.finish();
    actual = stringWriter.getBuffer().toString();
    message = new JSONObject( actual );
    widgetArray = message.getJSONArray( IProtocolConstants.MESSAGE_WIDGETS );
    widgetObject = widgetArray.getJSONObject( 1 );
    type = widgetObject.getString( IProtocolConstants.WIDGETS_TYPE );
    assertEquals( IProtocolConstants.PAYLOAD_SYNCHRONIZE, type );
    actualId = widgetObject.getString( IProtocolConstants.WIDGETS_ID );
    assertEquals( widgetId, actualId );
    payload = widgetObject.getJSONObject( IProtocolConstants.WIDGETS_PAYLOAD );
    assertEquals( "newText", payload.getString( "text" ) );
    assertEquals( 1, payload.getInt( "fake" ) );
    assertTrue( payload.getBoolean( "state" ) );
  }
  
  public void testMessageWithMultiSynchronize() throws IOException, JSONException {
    StringWriter stringWriter = new StringWriter();
    PrintWriter printWriter = new PrintWriter( stringWriter );
    ProtocolMessageWriter writer = new JsonMessageWriter( printWriter );
    Display display = new Display();
    Shell shell = new Shell( display );
    int widgetCount = 5;
    String[] widgetIds = new String[ widgetCount ];  
    for( int i = 0; i < widgetCount; i++ ) {
      Label label = new Label( shell, SWT.NONE );
      widgetIds[ i ] = WidgetUtil.getId( label );
    }
    Map properties = new HashMap();
    properties.put( "text", "newText" );
    properties.put( "image", "aUrl" );
    properties.put( "fake", new Integer( 1 ) );    
    writer.addMultipleSychronizePayload( widgetIds, properties );  
    String actual = stringWriter.getBuffer().toString();
    JSONObject message = new JSONObject( actual + "]}" );
    JSONArray widgetArray 
      = message.getJSONArray( IProtocolConstants.MESSAGE_WIDGETS );
    JSONObject widgetObject = widgetArray.getJSONObject( 0 );
    String type = widgetObject.getString( IProtocolConstants.WIDGETS_TYPE );
    assertEquals( IProtocolConstants.PAYLOAD_MULTI_SYNCHRONIZE, type );
    String actualId = widgetObject.getString( IProtocolConstants.WIDGETS_ID );
    assertEquals( "null", actualId );
    JSONObject payload 
      = widgetObject.getJSONObject( IProtocolConstants.WIDGETS_PAYLOAD );
    assertEquals( "newText", payload.getString( "text" ) );
    assertEquals( "aUrl", payload.getString( "image" ) );
    assertEquals( 1, payload.getInt( "fake" ) );
    JSONArray multiWidgets 
      = payload.getJSONArray( IProtocolConstants.KEY_WIDGETS );
    for( int i = 0; i < widgetCount; i++ ) {
      String actualWidgetId = multiWidgets.getString( i );
      String expectedWidgetId = widgetIds[ i ];
      assertEquals( expectedWidgetId, actualWidgetId );
    }
  }
  
  public void testMessageWithMixedPayloads() throws IOException, JSONException {
    StringWriter stringWriter = new StringWriter();
    PrintWriter printWriter = new PrintWriter( stringWriter );
    ProtocolMessageWriter writer = new JsonMessageWriter( printWriter );
    Display display = new Display();
    Shell shell = new Shell( display );
    Button button = new Button( shell, SWT.PUSH );
    createShellPayload( shell, writer );
    createButtonPayload( button, writer );
    writer.finish();
    String shellId = WidgetUtil.getId( shell );    
    String buttonId = WidgetUtil.getId( button );
    String actual = stringWriter.getBuffer().toString();
    JSONObject message = new JSONObject( actual );
    checkShellConstruct( shell, message );
    checkShellSynchronize( shellId, message );      
    checkShellListen( shellId, message );      
    checkButtonConstruct( button, shellId, buttonId, message );      
    checkButtonExecute( buttonId, message );    
  }

  private void checkButtonExecute( final String buttonId, 
                                   final JSONObject message )
    throws JSONException
  {
    JSONArray widgetArray 
      = message.getJSONArray( IProtocolConstants.MESSAGE_WIDGETS );
    JSONObject widgetObject = widgetArray.getJSONObject( 4 );
    String actualButtonId 
      = widgetObject.getString( IProtocolConstants.WIDGETS_ID );
    assertEquals( buttonId, actualButtonId );
    String type = widgetObject.getString( IProtocolConstants.WIDGETS_TYPE );
    assertEquals( IProtocolConstants.PAYLOAD_EXECUTE, type );
    JSONObject payload 
      = widgetObject.getJSONObject( IProtocolConstants.WIDGETS_PAYLOAD );
    String method = payload.getString( IProtocolConstants.KEY_METHODNAME );
    assertEquals( "select", method );
    JSONArray params 
      = payload.getJSONArray( IProtocolConstants.KEY_PARAMETER_LIST );
    assertEquals( "a1", params.getString( 0 ) );
  }

  private void checkButtonConstruct( final Button button,
                                     final String shellId,
                                     final String buttonId,
                                     final JSONObject message ) 
    throws JSONException
  {
    JSONArray widgetArray 
      = message.getJSONArray( IProtocolConstants.MESSAGE_WIDGETS );
    JSONObject widgetObject = widgetArray.getJSONObject( 3 );
    String actualButtonId 
      = widgetObject.getString( IProtocolConstants.WIDGETS_ID );
    assertEquals( buttonId, actualButtonId );
    String type = widgetObject.getString( IProtocolConstants.WIDGETS_TYPE );
    assertEquals( IProtocolConstants.PAYLOAD_CONSTRUCT, type );
    JSONObject payload 
      = widgetObject.getJSONObject( IProtocolConstants.WIDGETS_PAYLOAD );    
    String actualShellId 
      = payload.getString( IProtocolConstants.KEY_PARENT_ID );
    assertEquals( shellId, actualShellId );
    JSONArray params 
      = payload.getJSONArray( IProtocolConstants.KEY_PARAMETER_LIST );
    assertEquals( 4, params.getInt( 0 ) );
    assertTrue( params.getBoolean( 1 ) );
    type = payload.getString( IProtocolConstants.KEY_WIDGET_TYPE );
    assertEquals( button.getClass().getName(), type );
    JSONArray styles 
      = payload.getJSONArray( IProtocolConstants.KEY_WIDGET_STYLE );
    assertEquals( "PUSH", styles.getString( 0 ) );
    assertEquals( "BORDER", styles.getString( 1 ) );
  }

  private void checkShellListen( final String shellId, 
                                 final JSONObject message )
    throws JSONException
  {
    JSONArray widgetArray 
      = message.getJSONArray( IProtocolConstants.MESSAGE_WIDGETS );
    JSONObject widgetObject = widgetArray.getJSONObject( 2 );
    String actualShellId 
      = widgetObject.getString( IProtocolConstants.WIDGETS_ID );
    assertEquals( shellId, actualShellId );
    String type = widgetObject.getString( IProtocolConstants.WIDGETS_TYPE );
    assertEquals( IProtocolConstants.PAYLOAD_LISTEN, type );
    JSONObject payload 
      = widgetObject.getJSONObject( IProtocolConstants.WIDGETS_PAYLOAD );
    for( int i = 0; i < 5; i++ ) {
      boolean listen = i % 2 == 0 ? true : false; 
      boolean actualListen = payload.getBoolean( "listener" + i );
      assertEquals( listen, actualListen );
    }
  }

  private void checkShellSynchronize( final String shellId, 
                                      final JSONObject message )
    throws JSONException
  {
    JSONArray widgetArray 
      = message.getJSONArray( IProtocolConstants.MESSAGE_WIDGETS );
    JSONObject widgetObject = widgetArray.getJSONObject( 1 );
    String actualShellId 
      = widgetObject.getString( IProtocolConstants.WIDGETS_ID );
    assertEquals( shellId, actualShellId );
    String type = widgetObject.getString( IProtocolConstants.WIDGETS_TYPE );
    assertEquals( IProtocolConstants.PAYLOAD_SYNCHRONIZE, type );
    JSONObject payload 
      = widgetObject.getJSONObject( IProtocolConstants.WIDGETS_PAYLOAD );
    for( int i = 0; i < 5; i++ ) {
      String value = payload.getString( "key" + i );
      assertEquals( "value" + i, value );
    }
  }
  
  private void checkShellConstruct( final Shell shell, 
                                    final JSONObject message )
    throws JSONException
  {
    JSONArray widgetArray 
      = message.getJSONArray( IProtocolConstants.MESSAGE_WIDGETS );
    JSONObject widgetObject = widgetArray.getJSONObject( 0 );
    String actualShellId 
      = widgetObject.getString( IProtocolConstants.WIDGETS_ID );
    assertEquals( WidgetUtil.getId( shell ), actualShellId );
    String type = widgetObject.getString( IProtocolConstants.WIDGETS_TYPE );
    assertEquals( IProtocolConstants.PAYLOAD_CONSTRUCT, type );
    JSONObject payload 
      = widgetObject.getJSONObject( IProtocolConstants.WIDGETS_PAYLOAD );
    String actualDisplayId 
      = payload.getString( IProtocolConstants.KEY_PARENT_ID );
    assertEquals( DisplayUtil.getId( shell.getDisplay() ), actualDisplayId );
    JSONArray styles 
      = payload.getJSONArray( IProtocolConstants.KEY_WIDGET_STYLE );
    assertEquals( "SHELL_TRIM", styles.getString( 0 ) );
    Object params = payload.get( IProtocolConstants.KEY_PARAMETER_LIST );
    assertSame( JSONObject.NULL, params );
  }
  
  private void createButtonPayload( final Button button, 
                                    final ProtocolMessageWriter writer )
  {
    addButtonConstruct( button, writer );
    addButtonExecute( button, writer );
  }

  private void addButtonExecute( final Button button,
                                 final ProtocolMessageWriter writer )
  {
    Object[] arguments = new Object[] { "a1" };
    writer.addExecutePayload( WidgetUtil.getId( button ), "select", arguments );
  }

  private void addButtonConstruct( final Button button,
                                   final ProtocolMessageWriter writer )
  {
    String[] styles = new String[] { "PUSH", "BORDER" };
    Object[] arguments = new Object[] { new Integer( 4 ), new Boolean( true ) };
    writer.addConstructPayload( WidgetUtil.getId( button ),
                                WidgetUtil.getId( button.getParent() ),
                                button.getClass().getName(), 
                                styles, 
                                arguments );
  }

  private void createShellPayload( final Shell shell, 
                                   final ProtocolMessageWriter writer ) 
  {
    addShellConstruct( shell, writer );
    addShellSynchronize( shell, writer );
    addShellListeners( shell, writer );
  }

  private void addShellListeners( final Shell shell,
                                  final ProtocolMessageWriter writer )
  {
    Map listeners = new HashMap();
    for( int i = 0; i < 5; i++ ) {
      boolean listen = i % 2 == 0 ? true : false; 
      listeners.put( "listener" + i,  new Boolean( listen ) );
    }
    writer.addListenPayload( WidgetUtil.getId( shell ), listeners );
  }

  private void addShellSynchronize( final Shell shell,
                                    final ProtocolMessageWriter writer )
  {
    Map properties = new HashMap();
    for( int i = 0; i < 5; i++ ) {
      properties.put( "key" + i, "value" + i );
    }
    writer.addSychronizePayload( WidgetUtil.getId( shell ), properties );
  }

  private void addShellConstruct( final Shell shell,
                                  final ProtocolMessageWriter writer )
  {
    String[] styles = new String[]{ "SHELL_TRIM" };    
    writer.addConstructPayload( WidgetUtil.getId( shell ),
                                DisplayUtil.getId( shell.getDisplay() ),
                                shell.getClass().getName(), 
                                styles, 
                                null );
  }
  
  public void testStream() throws IOException, JSONException {
    StringWriter stringWriter = new StringWriter();
    PrintWriter printWriter = new PrintWriter( stringWriter );
    ProtocolMessageWriter writer = new JsonMessageWriter( printWriter );
    Display display = new Display();
    Shell shell = new Shell( display );
    Button button = new Button( shell, SWT.PUSH );
    writer.appendPayload( WidgetUtil.getId( shell ), 
                       IProtocolConstants.PAYLOAD_CONSTRUCT, 
                       "key", 
                       "value" );
    writer.appendPayload( WidgetUtil.getId( shell ), 
                          IProtocolConstants.PAYLOAD_CONSTRUCT, 
                          "key2", 
                          "value2" );
    writer.appendPayload( WidgetUtil.getId( shell ), 
                          IProtocolConstants.PAYLOAD_SYNCHRONIZE, 
                          "key", 
                          "value" );
    writer.appendPayload( WidgetUtil.getId( shell ), 
                          IProtocolConstants.PAYLOAD_SYNCHRONIZE, 
                          "key2", 
                          "value2" );
    writer.appendPayload( WidgetUtil.getId( shell ), 
                          IProtocolConstants.PAYLOAD_SYNCHRONIZE, 
                          "key3", 
                          "value3" );
    writer.appendPayload( WidgetUtil.getId( button ), 
                          IProtocolConstants.PAYLOAD_SYNCHRONIZE, 
                          "key", 
                          "value" );
    
    writer.finish();
    String actual = stringWriter.getBuffer().toString();
    JSONObject message = new JSONObject( actual );
    JSONArray widgetArray 
      = message.getJSONArray( IProtocolConstants.MESSAGE_WIDGETS );
    JSONObject widgetObject = widgetArray.getJSONObject( 0 );
    JSONObject payload 
      = widgetObject.getJSONObject( IProtocolConstants.WIDGETS_PAYLOAD );
    assertEquals( "value", payload.getString( "key" ) );
    assertEquals( "value2", payload.getString( "key2" ) );
    widgetObject = widgetArray.getJSONObject( 1 );
    payload 
      = widgetObject.getJSONObject( IProtocolConstants.WIDGETS_PAYLOAD );
    assertEquals( "value", payload.getString( "key" ) );
    assertEquals( "value2", payload.getString( "key2" ) );
    assertEquals( "value3", payload.getString( "key3" ) );
    widgetObject = widgetArray.getJSONObject( 2 );
    payload 
      = widgetObject.getJSONObject( IProtocolConstants.WIDGETS_PAYLOAD );
    String actualId = widgetObject.getString( IProtocolConstants.WIDGETS_ID );
    assertEquals( WidgetUtil.getId( button ), actualId );
    assertEquals( "value", payload.getString( "key" ) );         
  }
  
  public void testBlockWithStream() throws IOException, JSONException {
    StringWriter stringWriter = new StringWriter();
    PrintWriter printWriter = new PrintWriter( stringWriter );
    ProtocolMessageWriter writer = new JsonMessageWriter( printWriter );
    Display display = new Display();
    Shell shell = new Shell( display );
    Button button = new Button( shell, SWT.PUSH );
    writer.addConstructPayload( WidgetUtil.getId( button ), 
                                WidgetUtil.getId( button.getParent() ), 
                                button.getClass().getName(), 
                                new String[] { "PUSH" }, null );
    writer.appendPayload( WidgetUtil.getId( shell ), 
                          IProtocolConstants.PAYLOAD_CONSTRUCT, 
                          "key", 
                         "value" );
    
    writer.finish();
    String actual = stringWriter.getBuffer().toString();
    JSONObject message = new JSONObject( actual );
    JSONArray widgetArray 
      = message.getJSONArray( IProtocolConstants.MESSAGE_WIDGETS );
    JSONObject widgetObject = widgetArray.getJSONObject( 0 );
    JSONObject payload 
      = widgetObject.getJSONObject( IProtocolConstants.WIDGETS_PAYLOAD );
    String parentId = payload.getString( IProtocolConstants.KEY_PARENT_ID );
    assertEquals( WidgetUtil.getId( shell ), parentId );         
  }
  
  public void testStreamWithBlock() throws IOException, JSONException {
    StringWriter stringWriter = new StringWriter();
    PrintWriter printWriter = new PrintWriter( stringWriter );
    ProtocolMessageWriter writer = new JsonMessageWriter( printWriter );
    Display display = new Display();
    Shell shell = new Shell( display );
    Button button = new Button( shell, SWT.PUSH );
    writer.appendPayload( WidgetUtil.getId( shell ), 
                          IProtocolConstants.PAYLOAD_CONSTRUCT, 
                          "key", 
                          "value" );
    writer.addConstructPayload( WidgetUtil.getId( button ), 
                                WidgetUtil.getId( button.getParent() ), 
                                button.getClass().getName(), 
                                new String[] { "PUSH" }, null );
    writer.appendPayload( WidgetUtil.getId( button ), 
                          IProtocolConstants.PAYLOAD_SYNCHRONIZE, 
                          "key", 
                          "value" );
    writer.appendPayload( WidgetUtil.getId( button ), 
                          IProtocolConstants.PAYLOAD_SYNCHRONIZE, 
                          "key2", 
                          "value" );
    
    writer.finish();
    String actual = stringWriter.getBuffer().toString();
    JSONObject message = new JSONObject( actual );
    JSONArray widgetArray 
      = message.getJSONArray( IProtocolConstants.MESSAGE_WIDGETS );
    JSONObject widgetObject = widgetArray.getJSONObject( 1 );
    JSONObject payload 
      = widgetObject.getJSONObject( IProtocolConstants.WIDGETS_PAYLOAD );
    String parentId = payload.getString( IProtocolConstants.KEY_PARENT_ID );
    assertEquals( WidgetUtil.getId( shell ), parentId );
    widgetObject = widgetArray.getJSONObject( 2 );
    payload 
      = widgetObject.getJSONObject( IProtocolConstants.WIDGETS_PAYLOAD );
    assertEquals( "value", payload.getString( "key" ) );
    assertEquals( "value", payload.getString( "key2" ) );         
  }
  
  public void testStreamArray() throws IOException, JSONException {
    StringWriter stringWriter = new StringWriter();
    PrintWriter printWriter = new PrintWriter( stringWriter );
    ProtocolMessageWriter writer = new JsonMessageWriter( printWriter );
    Display display = new Display();
    Shell shell = new Shell( display );
    writer.appendPayload( WidgetUtil.getId( shell ), 
                          IProtocolConstants.PAYLOAD_CONSTRUCT, 
                          "key", 
                          new Integer[] { new Integer( 1 ), 
                                          new Integer( 2 ) } 
                        );
    writer.appendPayload( WidgetUtil.getId( shell ), 
                          IProtocolConstants.PAYLOAD_CONSTRUCT, 
                          "key2", 
                          new Boolean( true ) );
    
    writer.finish();
    String actual = stringWriter.getBuffer().toString();
    JSONObject message = new JSONObject( actual );
    JSONArray widgetArray 
      = message.getJSONArray( IProtocolConstants.MESSAGE_WIDGETS );
    JSONObject widgetObject = widgetArray.getJSONObject( 0 );
    JSONObject payload 
      = widgetObject.getJSONObject( IProtocolConstants.WIDGETS_PAYLOAD );
    JSONArray array = payload.getJSONArray( "key" );
    assertEquals( 1, array.getInt( 0 ) );
    assertEquals( 2, array.getInt( 1 ) );
    assertTrue( payload.getBoolean( "key2" ) );  
  }

  private int getRequestCounter() {
    String version = RWTRequestVersionControl.class + ".Version";
    Integer result = ( Integer )RWT.getServiceStore().getAttribute( version );
    if( result == null ) {
      result = new Integer( 0 );
    } else {
      result = new Integer( result.intValue() + 1 );
    }
    return result.intValue();
  }
  
}
