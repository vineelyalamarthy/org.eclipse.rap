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

/** <p> A class implements the Concealer interface to indicate that a
 *  developement tool should leave the internal structure of a instance of this 
 *  class unchanged. This marker interface is used to develope userdefined
 *  WebComponents based on composition, which are used as a whole 
 *  and should not be changed by a development tool except using the 
 *  public properties of this component. A Developement tool should also
 *  not create code of the internal component structure if its used
 *  as component in a WebPanel or WebForm.</p>
 *  <p>Note: This is only a marker interface used by Development tools,
 *  it has no effect whether to the kind the internal component 
 *  hierarchy is build, rather to the way components could be retrieve
 *  from e.g. userdefined WebPanels. <br>The main purpose of the Concealer 
 *  Interface is to break up big GUI-Forms in small, structured and
 *  reusable pieces which can be used by a development tool to put 
 *  these pieces together.</p> 
 *  <p> Example:<br>
 *  <pre>
 * public class ButtonPanel extends WebPanel implements Concealer {
 *   private WebButton button1;
 *   private WebButton button2;
 *  
 *   public ButtonPanel() {
 *     button1 = new WebButton( "button1" );
 *     button2 = new WebButton( "button2" );
 *     this.setWebLayout( new WebGridLayout( 1, 2 ) );
 *     this.add( button1, new Position( 1, 1 ) );
 *     this.add( button2, new Position( 1, 2 ) );
 *     button1.addWebActionListener( new WebActionListener() {
 *       public void webActionPerformed( WebActionEvent evt ) {
 *         W4TContext.addMessage( new Message( "Message from button1!" ) );
 *       }
 *     } );
 *     button2.addWebActionListener( new WebActionListener() {
 *       public void webActionPerformed( WebActionEvent evt ) {
 *         W4TContext.addMessage( new Message( "Message from button2!" ) );
 *       }
 *     } );
 *   }
 * }
 * </pre></p>
 * */
public interface Concealer {}