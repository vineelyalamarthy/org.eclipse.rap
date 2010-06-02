/******************************************************************************* 
* Copyright (c) 2010 EclipseSource and others. All rights reserved. This
* program and the accompanying materials are made available under the terms of
* the Eclipse Public License v1.0 which accompanies this distribution, and is
* available at http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*   EclipseSource - initial API and implementation
*******************************************************************************/ 
package org.eclipse.rap.rwt;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.eclipse.rwt.Fixture;
import org.eclipse.rwt.internal.lifecycle.DisplayUtil;
import org.eclipse.rwt.internal.protocol.JsonMessageWriter;
import org.eclipse.rwt.internal.protocol.ProtocolMessageWriter;
import org.eclipse.rwt.lifecycle.WidgetUtil;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;


public class BigProtocolMessageCreator {

  public static void main( final String[] args ) {
    Fixture.setUp();
    StringWriter stringWriter = new StringWriter();
    PrintWriter printWriter = new PrintWriter( stringWriter );
    ProtocolMessageWriter writer = null;
    Display display = new Display();
    Shell shell = new Shell( display );
    try {
      writer = new JsonMessageWriter( printWriter );
      Random ran = new Random();
      // add constructors
      String[] styles = new String[] { "BORDER", "NONE", "PUSH", "FAKE" };
      Object[] arguments = new Object[] { new Integer( 4 ), "arg1" };
      for( int i = 0; i < 208; i++ ) {        
        writer.addConstructPayload( DisplayUtil.getId( display ), 
                                    WidgetUtil.getId( shell ), 
                                    shell.getClass().getName() + ran.nextInt(), 
                                    styles, arguments );
      }
      // add properties
      Map properties = new HashMap();
      for( int i = 0; i < 9; i++ ) {
        properties.put( "property" + ran.nextInt(), "sampleValue" + ran.nextInt() );
      }
      for( int i = 0; i < 100; i++ ) {
        writer.addSychronizePayload( WidgetUtil.getId( shell ), properties );
      }
      // add listeners
      Map listeners = new HashMap();
      for( int i = 0; i < 10; i++ ) {
        boolean listen = i % 2 == 0 ? true : false; 
        
        listeners.put( "sampleListener" + ran.nextInt() + ran.nextInt(),  new Boolean( listen ) );
      }
      for( int i = 0; i < 16; i++ ) {
        writer.addListenPayload( WidgetUtil.getId( shell ), listeners );
      }
      
      Fixture.tearDown();
      
      writer.finish();
      File file = new File( "message.txt" );
      FileWriter fileWrite = new FileWriter( file );
      fileWrite.write( stringWriter.getBuffer().toString() );
      fileWrite.close();
      System.out.println( stringWriter.getBuffer().toString() );
    } catch( final IOException e ) {
      e.printStackTrace();
    }
  }
}
