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
package com.w4t;

/** <p>Messages are added to the request's message queue by means of the 
  * W4TContext and  displayed in a message box (a special pop up 
  * window).</p>
  *
  * @see org.eclipse.rwt.W4TContext#addMessage(Message) W4TContext.addMessage(Message)
  */
public class Message {

  private String message = "";
  private int priority = 0;
    
  /** <p>constructs a new Message with the passed text as message.</p> */
  public Message( final String message ) {
    this.message = message;
  }
  
  /** <p>constructs a new message with the specified priority and the passed
    * text as message.</p> */
  public Message( final int priority, final String message ) {
    this( message );
    this.priority = priority;
  }
  
  /** <p>returns the text of this message, possibly resolved, if the message
    * has been constructed with an internationalized text.</p> */
  public String getText() {
    return RenderUtil.resolve( message );
  }
  
  public int getPriority() {
    return priority;
  }
  
  /** <p>returns a String representation of this Message. This will be the 
    * message text encapsulated by this Message, even if it is a property
    * URI used for internationalization. Use {@link #getText()} in order to
    * get the resolved text.</p> */ 
  public String toString() {
    return message;
  }
}
