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

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;

import com.w4t.*;
import com.w4t.event.*;
import com.w4t.types.WebColor;
import com.w4t.types.WebTriState;
import com.w4t.util.DefaultColorScheme;

/** 
 * <p>This is the default error form implementation of W4Toolkit.</p>
 */
public class DefaultErrorForm extends AdminBase implements WebErrorForm {

  /** the Exception which caused the loading of this form */
  protected Exception exception = null;

  WebLabel wlbError;
  MarkupEmbedder mebMessage;
  WebLabel wlbTraceHeader;
  MarkupEmbedder mebTrace;
  WebButton wbtTrace;

  public DefaultErrorForm() {
    super();
    headline = "Error Page";
    setName( "W4T_StandardErrorForm" );
  }

  protected void setWebComponents() {
    super.setWebComponents();
    initWindowProps();
    // unload error Form after closing
    this.addWebFormListener( new WebFormAdapter() {
      public void webFormClosing( final WebFormEvent e ) {
        self.unload();
      }
    } );
    initCenterArea();
    initTraceButton();
  }

  private void initWindowProps() {
    WindowProperties windowProperties = getWindowProperties();
    windowProperties.setLocation( new WebTriState( WebTriState.NO ) );
    windowProperties.setMenubar( new WebTriState( WebTriState.NO ) );
    windowProperties.setToolbar( new WebTriState( WebTriState.NO ) );
    windowProperties.setDirectories( "no" );
  }
  
  protected void initTraceButton() {
    wbtTrace = new LinkButton( "show Stacktrace" );
    wbtTrace.addWebActionListener( new WebActionListener() {
      public void webActionPerformed( final WebActionEvent e ) {
        showStackTrace();
      }
    } );
    wplMenu.add( wbtTrace, posMenuLeft );
  }

  protected void initCenterArea() {
    WebPanel wplCenterArea = new WebPanel();
    WebGridLayout wgl = new WebGridLayout( 4, 1 );
    wgl.setWidth( "100%" );
    wgl.setCellpadding( "5" );
    wplCenterArea.setWebLayout( wgl );
    wplCenterBorder.add( wplCenterArea, "CENTER" );
    WebBorderLayout wbl = ( WebBorderLayout )wplCenterBorder.getWebLayout();
    String color = DefaultColorScheme.get( DefaultColorScheme.ADMIN_CENTER );
    wbl.setBgColor( new WebColor( color ) );

    wlbError = new WebLabel( "The following Exception occured in the system:" );
    wlbError.getStyle().setFontSize( 18 );
    wlbError.getStyle().setFontFamily( FONT );
    wplCenterArea.add( wlbError, new Position( 1, 1 ) );

    mebMessage = new MarkupEmbedder ();
    mebMessage.getStyle().setFontSize( 10 );
    mebMessage.getStyle().setFontFamily( FONT );
    wplCenterArea.add( mebMessage, new Position( 2, 1 ) );

    wlbTraceHeader = new WebLabel();
    wlbTraceHeader.getStyle().setFontSize( 18 );
    wlbTraceHeader.getStyle().setFontFamily( FONT );
    wplCenterArea.add( wlbTraceHeader, new Position( 3, 1 ) );

    mebTrace = new MarkupEmbedder();
    mebTrace.getStyle().setFontSize( 10 );
    mebTrace.getStyle().setFontFamily( FONT );
    wplCenterArea.add( mebTrace, new Position( 4, 1 ) );

  }

  public void setException( final Exception exception ) {
    this.exception = exception;
    String encodedMessage;
    encodedMessage = exception.getMessage() == null
                   ? "null"
                   : RenderUtil.encodeHTMLEntities( exception.getMessage() );
    mebMessage.setContent( "<pre>" + encodedMessage + "</pre>" );
  }

  protected String getStackTraceAsString( final Throwable e ) {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    PrintWriter writer = new PrintWriter( bytes, true );
    e.printStackTrace( writer );
    return bytes.toString();
  }

  protected void showStackTrace() {
    wlbTraceHeader.setValue( "Stacktrace:" );
    try {
      String stackTrace = getStackTraceAsString( exception );
      mebTrace.setContent(   "<pre>"
                           + RenderUtil.encodeHTMLEntities( stackTrace ) 
                           + "</pre>" );
    } catch( Exception e ) {
      mebTrace.setContent( "<pre>no stacktrace available</pre>" );
    }
  }

  public void showInSameWindow() {
    throw new UnsupportedOperationException( "TODO: replace implementation" );
//    setShowInNewWindow( false );
  }
}