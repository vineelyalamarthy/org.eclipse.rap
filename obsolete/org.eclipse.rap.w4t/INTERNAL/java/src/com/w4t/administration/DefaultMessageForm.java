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
package com.w4t.administration;

import com.w4t.*;
import com.w4t.event.WebFormAdapter;
import com.w4t.event.WebFormEvent;
import com.w4t.types.WebColor;
import com.w4t.util.DefaultColorScheme;


/** <p>This implementation of the org.eclipse.rap.MessageForm is used as default 
  * by the w4t framework if no userdefined message form is set.</p>
  */
public class DefaultMessageForm extends    AdminBase 
                                implements MessageForm {

  String messageHeader = "The following messages have been sent: ";

  private WebPanel wplHeadLine;
  private WebPanel wplMessages;
  private MarkupEmbedder mebMessages;

  
  /** <p>constructs a new  DefaultMessageForm (initialization of the 
    * gui is done in setWebComponents).</p> */
  public DefaultMessageForm() {
    super();
    headline = "W4T Message";
    this.addWebFormListener( new WebFormAdapter() {
      public void webFormClosing( final WebFormEvent e ) {
        self.unload();
      }
    } );
  }

  public void setMessages( final Message[] messages ) {
    StringBuffer sb = new StringBuffer( "<pre>" );
    for( int i = 0; i < messages.length; i++ ) {
      sb.append( messages[ i ].getText() );
      sb.append( "\n\n" );
    }
    sb.append( "</pre>" );
    mebMessages.setContent( sb.toString() );
  }
  
  
  // component initialisation
  ///////////////////////////
  protected void setWebComponents() {
    super.setWebComponents();
    initialiseWplHeadLine();
    initialiseWplMessages();
    initialiseWlbHeadLine();
    initialiseWlbMessages();
    changeMenuColor();
  }
  
  private void initialiseWplHeadLine() {
    wplHeadLine = new WebPanel();
    WebFlowLayout wflHeadLine = ( WebFlowLayout )wplHeadLine.getWebLayout();
    wflHeadLine.setCellpadding( "5" );
    wflHeadLine.getArea().setAlign( "left" );
    wplCenterBorder.add( wplHeadLine, WebBorderLayout.CENTER );
  }
  
  private void initialiseWlbHeadLine() {
    WebLabel wlb = new WebLabel( messageHeader );
    wlb.getStyle().setFontWeight( "bold" );
    wlb.getStyle().setFontSize( 12 );
    wplHeadLine.add( wlb );    
  }

  private void initialiseWplMessages() {
    wplMessages = new WebPanel();
    WebFlowLayout wflMessages = ( WebFlowLayout )wplMessages.getWebLayout();
    wflMessages.setWidth( "90%" );
    wflMessages.getArea().setAlign( "left" );
    wplCenterBorder.add( wplMessages, WebBorderLayout.CENTER );
  }
  
  private void initialiseWlbMessages() {
    mebMessages = new MarkupEmbedder();
    wplMessages.add( mebMessages );
  }
  
  private void changeMenuColor() {
    WebGridLayout wgl = ( WebGridLayout )wplHeader.getWebLayout();
    String bgColor 
      = DefaultColorScheme.get( DefaultColorScheme.ADMIN_MESSAGE_FORM_BG );
    wgl.setBgColor( new WebColor( bgColor ) );  
    WebGridLayout wglMenu = ( WebGridLayout )wplMenu.getWebLayout();
    wglMenu.setBgColor( new WebColor( bgColor ) );
  }
}
