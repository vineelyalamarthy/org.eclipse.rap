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

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.eclipse.rwt.internal.lifecycle.HtmlResponseWriter;
import org.eclipse.rwt.internal.resources.ResourceManagerImpl;
import org.eclipse.rwt.internal.service.ContextProvider;
import org.eclipse.rwt.internal.service.IServiceStateInfo;
import org.eclipse.rwt.internal.util.*;

import com.w4t.*;
import com.w4t.engine.lifecycle.standard.IFormRenderer;
import com.w4t.engine.util.FormManager;
import com.w4t.internal.adaptable.IFormAdapter;
import com.w4t.webcontainerkit.WebContainerRenderer;


abstract class WebFormRenderer 
  extends WebContainerRenderer
  implements IFormRenderer
{
  
  public void render( final WebComponent component ) throws IOException {
    HtmlResponseWriter out = getResponseWriter();
    WebForm form = ( WebForm )component;
    StringBuffer head = new StringBuffer();
    IServiceStateInfo stateInfo = ContextProvider.getStateInfo();
    boolean isExceptionOccured = true;
    try {
      head.append( WebFormUtil.createFormIdentifiers() );
      // This hidden field must be rendered by *every* form since client-side
      // JavaScript relies on this information
      head.append( WebFormUtil.createAjaxEnablement() );

      /* use the render buffer if the posted form has expired
         or if an Exception occured during the eventhandling ... */
      if( useBufferedMarkup() ) {
        head.append( getCounterForBufferedMarkup( form ) );
        out.equalize( WebFormUtil.getFormAdapter( form ).getRenderBuffer() );

      /* ... otherwise render a new output */
      } else {
        IFormAdapter formAdapter = WebFormUtil.getFormAdapter( form );
        int requestCounter = formAdapter.getRequestCounter();
        head.append( WebFormUtil.createRequestCounter( requestCounter ) );
        // note: if an Exception occurs during rendering, a WebErrorForm
        // will be created and the render process will started again -
        // this will use the render buffer because the ExceptionOccured
        // attribute of the stateInfo is set to true
        super.render( form );
        WebFormUtil.getFormAdapter( form ).postRender();
      }
      MessagePageUtil.checkMessages();

      checkFormStack();
      
      // put the pieces of the html content together
      out.clearHead();
      out.appendHead( createPageHeader() );
      out.appendHead( head );
      out.concatLayers();
      out.clearFoot();
      out.appendFoot( WebFormUtil.createCloseForm() );
      out.appendFoot( createPageFooter() );

      /* a form which is handed out to the browser is marked as
         active as long as the cleanup method of the WebComponentRegistry
         doesn't recognize that the form has been closed in the browser */
      WebComponentControl.setActive( form, true );
      // TODO [rh] revise this
      HttpServletResponse response = ContextProvider.getResponse();
      response.setContentType( HTML.CONTENT_TEXT_HTML_UTF_8 );
      isExceptionOccured = false;
    } finally {
      if( isExceptionOccured ) {
        stateInfo.setExceptionOccured( true );
      }
    } 
  }
  
  
  ////////////////////////////////
  // IFormRenderer implementations
  
  public boolean fireRenderEvents() {
    IServiceStateInfo stateInfo = ContextProvider.getStateInfo();
    return !( stateInfo.isExpired() || stateInfo.isExceptionOccured() );
  }


  // Abstract methods
  /////////////////////////////
  
  /** renders the basic html structure of the head of a page */
  abstract StringBuffer createPageHeader();

  abstract void checkFormStack();
  
  static String getCounterForBufferedMarkup( final WebForm form ) {
    int counter = WebFormUtil.getFormAdapter( form ).getRequestCounter() - 1;
    return WebFormUtil.createRequestCounter( counter );
  }
  
  String createCssClasses() {
    HtmlResponseWriter writer = getResponseWriter();
    StringBuffer result = new StringBuffer();
    result.append( "<style type=\"text/css\">" );
    CssClass[] classes = writer.getCssClasses();
    for( int i = 0; i < classes.length; i++ ) {
      CssClass cssClass = classes[ i ];
      result.append( cssClass.getClassName() );
      result.append( " { " );
      result.append( cssClass.getContent() );
      result.append( " } " ); 
    }
    result.append( "</style>" );
    return result.toString();
  }

  void writeCssClasses() throws IOException {
    HtmlResponseWriter writer = getResponseWriter();
    writer.startElement( HTML.STYLE, null );
    writer.writeAttribute( HTML.TYPE, HTML.CONTENT_TEXT_CSS, null );
    CssClass[] classes = writer.getCssClasses();
    for( int i = 0; i < classes.length; i++ ) {
      CssClass cssClass = classes[ i ];
      writer.writeText( cssClass.getClassName(), null );
      writer.writeText( " { ", null );
      writer.writeText( cssClass.getContent(), null );
      writer.writeText( " } ", null ); 
    }
    writer.endElement( HTML.STYLE );
  }
  
  static void createCssReferences( final StringBuffer html ) {
    WebForm wf = FormManager.getActive();
    String[] cssFiles = wf.getCssFile();
    for( int i = 0; i < cssFiles.length; i++ ) {
      html.append( "<link rel=\"stylesheet\" type=\"text/css\"" );
      HTMLUtil.attribute( html, "href", ResourceManagerImpl.load( cssFiles[ i ] ) );
      html.append( " />" );      
    }
  }

  /** closes all html tags opened in the setPageHeader method */
  String createPageFooter() {
    return "</body></html>";
  }
  
  static boolean parameterExists( final String name ) {
    return    ContextProvider.getRequest().getParameter( name ) != null
           && !ContextProvider.getRequest().getParameter( name ).equals( "" );
  }

  private static boolean useBufferedMarkup() {
    IServiceStateInfo stateInfo = ContextProvider.getStateInfo();
    return    ( stateInfo.isExpired() || stateInfo.isExceptionOccured() ) 
           && !stateInfo.getDetectedBrowser().isAjaxEnabled();
  }

}
