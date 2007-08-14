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
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.rwt.internal.browser.Browser;
import org.eclipse.rwt.internal.browser.BrowserLoader;
import org.eclipse.rwt.internal.lifecycle.HtmlResponseWriter;
import org.eclipse.rwt.internal.resources.ResourceManagerImpl;
import org.eclipse.rwt.internal.service.*;
import org.eclipse.rwt.internal.util.*;

import com.w4t.*;
import com.w4t.ajax.AjaxStatusUtil;
import com.w4t.engine.util.WindowManager;
import com.w4t.util.RendererCache;


public class WebFormRenderer_Default_Ajax 
  extends WebFormRenderer_Default_Script 
{

  private static final String WINDOW_NAME = "COM_W4T_AJAX_WINDOW_NAME";
  
  public void render( final WebComponent component ) throws IOException {
    WebForm form = ( WebForm )component;
    AjaxStatusUtil.preRender( form );
    try {
      if( allowsAjaxMarkup() ) {
        if( ContextProvider.getStateInfo().isExceptionOccured() ) {
          renderExceptionHandlingMarkup( form );
        } else if( needDispatch() ) {
          renderDispatchMarkup( form );
        } else {
          renderAjaxResponse( form );
        }
      } else {
        // There are three reasons to get here:
        // 1. An Ajax-enabled browser has sent a non-ajax request
        //    (e.g. via an javaScript document.submit)
        // 2. It is the first time that this form is renderd
        // 3. The form is expired
        renderNonAjaxResponse( form );
      }
    } finally {
      AjaxStatusUtil.postRender( form );
    }
  }

  private boolean needDispatch() {
    return    isDispatchWithAjaxRendering() 
           || ContextProvider.getStateInfo().isExpired();
  }

  private boolean isDispatchWithAjaxRendering() {
    return    WindowManager.getActive().getFormToDispatch() != null
           && !ContextProvider.getStateInfo().isFirstAccess();
  }
  
  public void prepare() {
    IServiceStateInfo stateInfo = ContextProvider.getStateInfo();
    stateInfo.setAttribute( WINDOW_NAME, WindowManager.getActive().getId() );
  }
  
  public boolean fireRenderEvents() {
    return    !isDispatchWithAjaxRendering() 
           && super.fireRenderEvents();
  }

  private boolean allowsAjaxMarkup() {
    IServiceStateInfo stateInfo = ContextProvider.getStateInfo();
    return    isAjaxRequest() 
           && !stateInfo.isFirstAccess() 
           && !stateInfo.isExpired();
  }
  
  /** <p>Renders the given form with the closest matching non-Ajax-renderer.
   * While rendering the actual (ajax-) browser is replaced by the corresponding
   * script-enabled browser. This causes the <code>LifeCycleHelper</code> to
   * retrieve script-renderer for all components that are being rendered.</p>
   */
  private static void renderNonAjaxResponse( final WebForm form ) 
    throws IOException 
  {
    Browser saveBrowser = W4TContext.getBrowser();
    String browserClassName = W4TContext.getBrowser().getClass().getName();
    Browser nonAjaxBrowser;
    nonAjaxBrowser = BrowserLoader.loadClassForName( browserClassName, 
                                                     true, 
                                                     false );
    String id = ServiceContext.DETECTED_SESSION_BROWSER;
    ContextProvider.getSession().setAttribute( id, nonAjaxBrowser );
    try {
      LifeCycleHelper.render( form, form );
    } finally {
      ContextProvider.getSession().setAttribute( id, saveBrowser );
    }
  }
  
  private void renderAjaxResponse( final WebForm form ) 
    throws IOException 
  {
    HtmlResponseWriter writer = getResponseWriter();
    String[] cssFileBuffer = form.getCssFile();
    
    // TODO [rh] revise this: using head/foot for ajax-request 
    //           tags to pass by issue with FormMarkupAppender 
    //           (e.g. in WebScrollPane)
    writer.appendHead( RenderUtil.createXmlProcessingInstruction() );
    writer.appendHead( HTML.START_AJAX_RESPONSE );
    
    if( AjaxStatusUtil.mustRender( form ) ) {
      AjaxStatusUtil.startEnvelope( form );
      writer.append( WebFormUtil.createOpenForm() );
      HTMLUtil.hiddenInput( writer, RequestParams.SCROLL_X, "" );
      HTMLUtil.hiddenInput( writer, RequestParams.SCROLL_Y, "" );
      HTMLUtil.hiddenInput( writer, RequestParams.AVAIL_WIDTH, "0" );
      HTMLUtil.hiddenInput( writer, RequestParams.AVAIL_HEIGHT, "0" );
      writer.append( WebFormUtil.createFormIdentifiers() );
      writer.append( WebFormUtil.createAjaxEnablement() );
    } 
    // TODO [rh] check if it is really necessary to render *all* 
    //           event handler fields 
    writer.append( WebFormUtil.createEventHandlerFields() );
    writer.append( WebFormUtil.createFocusElement() );
    int requestCounter = WebFormUtil.getFormAdapter( form ).getRequestCounter();
    writer.append( WebFormUtil.createRequestCounter( requestCounter ) );
    
    if( AjaxStatusUtil.mustRender( form ) ) {
      // render form-layout and its child components
      RendererCache rendererCache = RendererCache.getInstance();
      Renderer renderer = rendererCache.retrieveRenderer( WebContainer.class );
      renderer.render( form );
    } else {
      // render only child components
      WebComponent childComponent;
      for( int i = 0; i < form.getWebComponentCount(); i++ ) {
        childComponent = form.get( i );
        LifeCycleHelper.render( childComponent, childComponent );
      }
    }

    if( AjaxStatusUtil.mustRender( form ) ) {
      writer.append( WebFormUtil.createCloseForm() );
      AjaxStatusUtil.endEnvelope( form );
    }
    
    // render script library reference tags
    appendScriptTags( form );

    // render JavaScript to open and close windows as requested
    MessagePageUtil.checkMessages();
    appendWindowOpener();
    appendWindowCloser();
    appendWindowRefresher();
    
    // render css styles
    appendCssClasses( form );
    appendCssReference( form, cssFileBuffer );
    
    // process postRender the same way as script-renderer would 
    WebFormUtil.getFormAdapter( form ).postRender();
    writer.appendFoot( HTML.END_AJAX_RESPONSE );
    ContextProvider.getResponse().setContentType( HTML.CONTENT_TEXT_XML );    
  }
  
  private void renderDispatchMarkup( final WebForm form ) throws IOException {
    HtmlResponseWriter writer = getResponseWriter();
    writer.appendHead( RenderUtil.createXmlProcessingInstruction() );
    writer.appendHead( HTML.START_AJAX_RESPONSE );
    IServiceStateInfo stateInfo = ContextProvider.getStateInfo();
    String windowId = ( String )stateInfo.getAttribute( WINDOW_NAME );
    String dispatcher = WebFormUtil.createWindowRefresher( form, windowId );
    String encodedDispatcher = RenderUtil.encodeXMLEntities( dispatcher );
    RenderUtil.writeJavaScriptInline( writer, encodedDispatcher );
    writer.appendFoot( HTML.END_AJAX_RESPONSE );
    ContextProvider.getResponse().setContentType( HTML.CONTENT_TEXT_XML );
  }
  
  private void renderExceptionHandlingMarkup( final WebForm form ) 
    throws IOException 
  {
    HtmlResponseWriter writer = getResponseWriter();
    writer.appendHead( RenderUtil.createXmlProcessingInstruction() );
    writer.appendHead( HTML.START_AJAX_RESPONSE );
    appendWindowOpener();
    writer.appendFoot( HTML.END_AJAX_RESPONSE );
    ContextProvider.getResponse().setContentType( HTML.CONTENT_TEXT_XML );
  }

  private void appendCssReference( final WebForm form, 
                                   final String[] cssFileBuffer ) 
      throws IOException
  {
    Set buffer = new HashSet();
    String[] cssFile = form.getCssFile();
    for( int i = 0; i < cssFile.length; i++ ) {
      buffer.add( cssFile[ i ] );
    }
    for( int i = 0; i < cssFileBuffer.length; i++ ) {
      buffer.remove( cssFileBuffer[ i ] );
    }
    String[] toAppend = new String[ buffer.size() ];
    buffer.toArray( toAppend );
    HtmlResponseWriter out = getResponseWriter();
    for( int i = 0; i < toAppend.length; i++ ) {
      out.startElement( HTML.LINK, null );
      out.writeAttribute( HTML.REL, HTML.STYLESHEET, null );
      out.writeAttribute( HTML.TYPE, HTML.CONTENT_TEXT_CSS, null );
      String url = ResourceManagerImpl.load( toAppend[ i ] );
      out.writeAttribute( HTML.HREF, url, null );
      out.closeElementIfStarted();
    }
  }

  private void appendScriptTags( final WebForm form ) {
    HtmlResponseWriter writer = getResponseWriter();
    HtmlResponseWriter renderBuffer 
      = WebFormUtil.getFormAdapter( form ).getRenderBuffer();
    // TODO [rh] why that? remove libraries, create tags and later add them?
    String[] libraries = renderBuffer.getJSLibraries();
    for( int i = 0; i < libraries.length; i++ ) {
      writer.removeJSLibraries( libraries[ i ] );
    }
    if( writer.getJSLibrariesCount() != 0 ) {
      writer.appendHead( "<div" );
      String id = HTMLUtil.attribute( HTML.ID, ID_W4T_USERDEFINED_SCRIPTS );
      writer.appendHead( id );
      writer.appendHead( ">" );
      writer.appendHead( WebFormUtil.createJSLibTags( writer ) );
      writer.appendHead( "</div>" );
    }
    for( int i = 0; i < libraries.length; i++ ) {
      writer.useJSLibrary( libraries[ i ] );
    }
  }

  private void appendCssClasses( final WebForm form ) {
    HtmlResponseWriter writer = getResponseWriter();
    HtmlResponseWriter renderBuffer
     = WebFormUtil.getFormAdapter( form ).getRenderBuffer();
    CssClass[] cssClasses = renderBuffer.getCssClasses();
    for( int i = 0; i < cssClasses.length; i++ ) {
      writer.removeCssClass( cssClasses[ i ].getContent() );
    }
    if( writer.getCssClassCount() != 0 ) {
      writer.append( createCssClasses() );
    }
    writer.mergeRegisteredCssClasses( cssClasses );
  }

  private void appendWindowOpener() throws IOException {
    String script = WebFormUtil.createWindowOpener();
    script = RenderUtil.encodeXMLEntities( script );
    RenderUtil.writeJavaScriptInline( getResponseWriter(), script );
  }

  private void appendWindowRefresher() throws IOException {
    String script = WebFormUtil.createWindowRefresher();
    script = RenderUtil.encodeXMLEntities( script );
    RenderUtil.writeJavaScriptInline( getResponseWriter(), script );
  }

  private void appendWindowCloser() throws IOException {
    String script = WebFormUtil.createWindowCloser();
    script = RenderUtil.encodeXMLEntities( script );
    RenderUtil.writeJavaScriptInline( getResponseWriter(), script );
  }

  private static boolean isAjaxRequest() {
    HttpServletRequest request = ContextProvider.getRequest();
    String paramValue = request.getParameter( RequestParams.IS_AJAX_REQUEST );
    return "true".equals( paramValue );
  }
}
