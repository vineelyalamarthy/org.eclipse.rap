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
import java.util.Map;

import junit.framework.TestCase;

import org.eclipse.rwt.Fixture;
import org.eclipse.rwt.internal.lifecycle.DisplayUtil;
import org.eclipse.rwt.internal.protocol.IProtocolConstants;
import org.eclipse.rwt.internal.protocol.ProtocolMessageWriter;
import org.eclipse.rwt.lifecycle.WidgetUtil;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;


public class ProtocolMessageWriter_Test extends TestCase {
  
  private class DummyWriter extends ProtocolMessageWriter {

    public DummyWriter( final PrintWriter decoratedWriter ) {
      super( decoratedWriter );      
    }

    protected void appendEndMessage() {
      write( "*" );
    }

    protected void appendEndPayload() {
      write( "#" );
    }

    protected void appendPayloadSeparator() {
      write( "," );
    }

    protected void appendStartMessage() {
      write( "*" );
    }
    
    protected void appendExecutePayload( final String methodName,
                                         final Object[] parameter )
    {
      write( "EX" );
    }

    protected void appendStartPayload( final String widgetId ) {
      write( "#" );
    }

    protected void appendConstructPayload( String widgetId,
                                           final String parentId,
                                           final String type,
                                           final String[] styles, final Object[] arguments )
    {
      write( "C" );
    }

    protected void appendDestroyPayload() {
      write( "D" );
    }

    protected void appendListenPayload( final Map listeners ) {
      write( "L" );
    }

    protected void appendEventPayload( final String event ) {
      write( "E" );
    }

    protected void appendExecuteScript( final String scriptType, 
                                        final String script ) 
    {
      write( "EXS" );
    }

    protected void appendSynchronizePayload( final Map properties ) {
      write( "S" );
    }

    protected void appendMultipleSynchronizePayload( final String[] widgetIds,
                                                     final Map properties )
    {
      write( "MS" );
    }

    protected void appendPayloadType( final String payloadType ) {
      write( "C" );
    }

    protected void appendPayloadValueSeparator() {
    }

    protected void appendEndPayloadValue() {
    }

    protected void appendPayloadValue( Object key, Object value ) {
    }
    
  }
  
  protected void setUp() throws Exception {
    Fixture.setUp();
  }
  
  protected void tearDown() throws Exception {
    Fixture.tearDown();
  } 
  
  public void testCreateMessage() throws IOException {
    StringWriter stringWriter = new StringWriter();
    PrintWriter printWriter = new PrintWriter( stringWriter );
    ProtocolMessageWriter writer = new DummyWriter( printWriter );
    writer.finish();    
    assertEquals( "**", stringWriter.getBuffer().toString() );
  }

  public void testCreateMessageWithConstructor() throws IOException {
    StringWriter stringWriter = new StringWriter();
    PrintWriter printWriter = new PrintWriter( stringWriter );
    ProtocolMessageWriter writer = new DummyWriter( printWriter );
    Display display = new Display();
    Shell shell = new Shell( display );
    writer.addConstructPayload( DisplayUtil.getId( display ), 
                                WidgetUtil.getId( shell ), 
                                null, 
                                null, 
                                null );
    writer.finish();    
    assertEquals( "*#C#*", stringWriter.getBuffer().toString() );
  }
  
  public void testCreateMessageWithDestroyer() throws IOException {
    StringWriter stringWriter = new StringWriter();
    PrintWriter printWriter = new PrintWriter( stringWriter );
    ProtocolMessageWriter writer = new DummyWriter( printWriter );
    Display display = new Display();
    Shell shell = new Shell( display );
    writer.addDestroyPaylod( WidgetUtil.getId( shell ) );
    writer.finish();    
    assertEquals( "*#D#*", stringWriter.getBuffer().toString() );
  }
  
  public void testCreateMessageWithSynchronize() throws IOException {
    StringWriter stringWriter = new StringWriter();
    PrintWriter printWriter = new PrintWriter( stringWriter );
    ProtocolMessageWriter writer = new DummyWriter( printWriter );
    Display display = new Display();
    Shell shell = new Shell( display );
    writer.addSychronizePayload( WidgetUtil.getId( shell ), null );
    writer.finish();    
    assertEquals( "*#S#*", stringWriter.getBuffer().toString() );
  }
  
  public void testCreateMessageWithMultiSynchronize() throws IOException {
    StringWriter stringWriter = new StringWriter();
    PrintWriter printWriter = new PrintWriter( stringWriter );
    ProtocolMessageWriter writer = new DummyWriter( printWriter );   
    writer.addMultipleSychronizePayload( null, null );
    writer.finish();    
    assertEquals( "*#MS#*", stringWriter.getBuffer().toString() );
  }
  
  public void testCreateMessageWithListen() throws IOException {
    StringWriter stringWriter = new StringWriter();
    PrintWriter printWriter = new PrintWriter( stringWriter );
    ProtocolMessageWriter writer = new DummyWriter( printWriter );
    Display display = new Display();
    Shell shell = new Shell( display );
    writer.addListenPayload( WidgetUtil.getId( shell ), null );
    writer.finish();    
    assertEquals( "*#L#*", stringWriter.getBuffer().toString() );
  }
  
  public void testCreateMessageWithEvent() throws IOException {
    StringWriter stringWriter = new StringWriter();
    PrintWriter printWriter = new PrintWriter( stringWriter );
    ProtocolMessageWriter writer = new DummyWriter( printWriter );
    Display display = new Display();
    Shell shell = new Shell( display );
    writer.addFireEventPayload( WidgetUtil.getId( shell ), null );
    writer.finish();    
    assertEquals( "*#E#*", stringWriter.getBuffer().toString() );
  }
  
  public void testCreateMessageWithExecute() throws IOException {
    StringWriter stringWriter = new StringWriter();
    PrintWriter printWriter = new PrintWriter( stringWriter );
    ProtocolMessageWriter writer = new DummyWriter( printWriter );
    Display display = new Display();
    Shell shell = new Shell( display );
    writer.addExecutePayload( WidgetUtil.getId( shell ), null, null );
    writer.finish();    
    assertEquals( "*#EX#*", stringWriter.getBuffer().toString() );
  }
  
  public void testCreateMessageWithExecuteScript() throws IOException {
    StringWriter stringWriter = new StringWriter();
    PrintWriter printWriter = new PrintWriter( stringWriter );
    ProtocolMessageWriter writer = new DummyWriter( printWriter );
    Display display = new Display();
    Shell shell = new Shell( display );
    writer.addExecuteScript( WidgetUtil.getId( shell ), null, null );
    writer.finish();    
    assertEquals( "*#EXS#*", stringWriter.getBuffer().toString() );
  }
  
  public void testAppendPayload() throws IOException {
    StringWriter stringWriter = new StringWriter();
    PrintWriter printWriter = new PrintWriter( stringWriter );
    ProtocolMessageWriter writer = new DummyWriter( printWriter );
    writer.appendPayload( "id", IProtocolConstants.PAYLOAD_CONSTRUCT, "", "" );
    assertEquals( "*#C", stringWriter.getBuffer().toString() );
  }
  
  public void testWriterStarted() throws IOException {
    StringWriter stringWriter = new StringWriter();
    PrintWriter printWriter = new PrintWriter( stringWriter );
    ProtocolMessageWriter writer = new DummyWriter( printWriter );
    assertFalse( writer.isStarted() );
    writer.appendPayload( "id", IProtocolConstants.PAYLOAD_CONSTRUCT, "", "" );
    assertTrue( writer.isStarted() );
  }
  
}
