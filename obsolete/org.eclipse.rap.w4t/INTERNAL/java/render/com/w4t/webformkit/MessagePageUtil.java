/*******************************************************************************
 * Copyright (c) 2002-2006 Innoopract Informationssysteme GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Innoopract Informationssysteme GmbH - initial API and implementation
 ******************************************************************************/
package com.w4t.webformkit;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.rwt.internal.ConfigurationReader;
import org.eclipse.rwt.internal.IConfiguration;

import com.w4t.*;
import com.w4t.util.MessageHandler;


/** <p>a utility class that creates message pages.</p>
  */
class MessagePageUtil {
  
  private static final String STANDARD_FORM 
    = "com.w4t.administration.DefaultMessageForm";


  /** <p>loads the message form that is specified in the 'messagePage' 
    * element in the W4T.xml configuration file, or a standard form if 
    * the loading failed.</p> */
  static WebForm loadMessageForm() {
    WebForm result = null;
    try {
      IConfiguration configuration = ConfigurationReader.getConfiguration();
      String formName = configuration.getInitialization().getMessagePage();
      result = W4TContext.loadForm( formName );  
    } catch ( Exception noValidUserDefinedMessageForm ) {
      // this means that the specified message form was not found or it
      // could not be casted in MessageForm, so we proceed to plan B
      result = loadStandardForm();
    }
    return result;
  }  
  
  static void checkMessages() {
    MessageHandler handler = MessageHandler.getInstance();
    if( !handler.isEmpty() ) {
      WebForm messageForm = loadMessageForm();
      ( ( MessageForm )messageForm ).setMessages( readMessages() );
      W4TContext.showInNewWindow( messageForm );
    }
  }
  
  
  //////////////////
  // helping methods

  private static WebForm loadStandardForm() {
    return W4TContext.loadForm( STANDARD_FORM );
  }
  
  private static Message[] readMessages() {
    List buffer = new ArrayList();
    MessageHandler handler = MessageHandler.getInstance();
    while( !handler.isEmpty() ) {
      buffer.add( handler.front() );
      handler.dequeue();      
    }
    Message[] result = new Message[ buffer.size() ];
    buffer.toArray( result );
    return result; 
  }
}