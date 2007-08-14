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


/** <p>This Interface has to be implemented by a WebForm which is used
  * as a message form in a w4t application.</p>
  *
  * <p>Applications may implement MessageForm in order to display 
  * messages to the user in a custom way. This is comparable to an alert box, 
  * but with the important difference that the message form does not
  * interrupt the program flow but is rendered (and sent to the browser) 
  * after the currently displayed page.</p>
  * <p>A WebForm that implements MessageForm can be named in the 
  * w4t configuration file W4T.xml in the WEB-INF/conf/ directory of the 
  * web application. When rendering the main page, the w4t system will pass
  * all messages which have been added recently via the {@link
  * org.eclipse.rwt.W4TContext#addMessage(Message) W4TContext.addMessage(Message)} 
  * method to the {@link #setMessages(String) setMessages(String)} method 
  * of this MessageForm and it is displayed to the the user.</p>
  */
public interface MessageForm {

  /** <p>called by the system to tell this MessageForm which messages 
    * to display.</p>
    * 
    * @param messages the messages which had been added within the w4t system 
    *                 via the W4TContext.setMessage( org.eclipse.rap.Message ) method.
    */
  void setMessages( Message[] messages );
}
