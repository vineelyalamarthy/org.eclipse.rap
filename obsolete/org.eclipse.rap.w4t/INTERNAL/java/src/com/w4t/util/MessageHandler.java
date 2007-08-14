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
package com.w4t.util;

import java.util.Vector;

import org.eclipse.rwt.SessionSingletonBase;

import com.w4t.Message;


/** <p>This controls a queue of messages, which could be added during 
  * the processing of the current request. At the beginning and after
  * a request is processed the message queue is empty.</p>
  */
public class MessageHandler extends SessionSingletonBase {
  
  private Vector queue;
  
  /** Creates a new instance of MessageHandler */
  private MessageHandler() {
    queue = new Vector();
  }
  
  /** returns the singleton instance (per session) of MessageHandler */
  public static MessageHandler getInstance() {
    return ( MessageHandler )getInstance( MessageHandler.class );
  }
  
  /** adds a message at the end of the queue */
  public void enqueue( final Message message ) {
    queue.add( message );
  }
  
  /** returns the first message of the queue if exists */
  public Message front() {
    Message result = null;
    if( !isEmpty() ) {
      result = ( Message )queue.get( 0 );
    }
    return result;
  }
  
  /** removes the first message of the queue if exists */
  public void dequeue() {
    if( !isEmpty() ) {
      queue.remove( 0 );
    }
  }
  
  /** tells if there are any messages in the queue */
  public boolean isEmpty() {
    return queue.size() == 0;
  }
}