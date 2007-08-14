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

import org.eclipse.rwt.internal.browser.Browser;
import org.eclipse.rwt.internal.lifecycle.HtmlResponseWriter;
import org.eclipse.rwt.internal.resources.ResourceManagerImpl;
import org.eclipse.rwt.internal.service.*;
import org.eclipse.rwt.internal.util.*;

import com.w4t.*;
import com.w4t.IWindowManager.IWindow;
import com.w4t.dhtml.event.*;
import com.w4t.engine.util.FormManager;
import com.w4t.engine.util.WindowManager;
import com.w4t.event.*;
import com.w4t.internal.adaptable.IFileUploadAdapter;
import com.w4t.internal.adaptable.IFormAdapter;


public final class WebFormUtil {
  
  private WebFormUtil() {
    // prevent instantiation
  }

  static IFormAdapter getFormAdapter( final WebForm form ) {
    return ( IFormAdapter )form.getAdapter( IFormAdapter.class );  
  }
  
  /** returns the String for the window title in the html <title> tag */
  static String getWindowTitle() {
    String result = "";
    WebForm form = FormManager.getActive();
    if( form != null ) {
      String title = RenderUtil.resolve( form.getWindowTitle() );
      result = title.equals( "" ) ? "W4 Toolkit" : title;
    }
    return result;
  }

  static void renderContentType( final StringBuffer html ) 
  {
    html.append( "<meta" );
    HTMLUtil.attribute( html, HTML.HTTP_EQUIV, "content-type" );
    HTMLUtil.attribute( html, HTML.CONTENT, HTML.CONTENT_TEXT_HTML_UTF_8 );
    html.append( " />" );
  }
  
  static void renderCacheControl( final StringBuffer html ) {
    html.append( "<meta" );
    HTMLUtil.attribute( html, HTML.HTTP_EQUIV, "cache-control" );
    HTMLUtil.attribute( html, HTML.CONTENT, "no-cache" );
    html.append( " />" );
  }
  
  static void renderTitle( final StringBuffer html, 
                           final String title ) 
  {
    html.append( "<title>" );
    html.append( RenderUtil.encodeHTMLEntities( title ) );
    html.append( "</title>" );
  }
  
  /** HTML-header tags to promote W4T as favicon */ 
  static String renderFavIcon() {
    StringBuffer result = new StringBuffer();
    String faviconUrl = URLHelper.getContextURLString() 
                      + "/resources/images/favicon.ico";
    result.append( "<link" );
    HTMLUtil.attribute( result, HTML.REL, "SHORTCUT ICON" );
    HTMLUtil.attribute( result, HTML.HREF, faviconUrl );
    result.append( " />" );
    result.append( "<link" );
    HTMLUtil.attribute( result, HTML.REL, "icon" );
    HTMLUtil.attribute( result, HTML.HREF, faviconUrl );
    HTMLUtil.attribute( result, HTML.TYPE, HTML.CONTENT_IMAGE_ICO );
    result.append( " />" );
    return result.toString();
  }
  
  /////////////////////////
  // Helper methods to render various hidden fields
  
  /** <p>append the request counter of a WebForm to its hidden fields</p> */
  static String createRequestCounter( final int requestCounter ) 
  {
    return HTMLUtil.hiddenInput( RequestParams.REQUEST_COUNTER,
                                 String.valueOf( requestCounter ) );
  }

  /** <p>Render the AJAX-enabled-flag to its hidden field</p> */
  static String createAjaxEnablement() {
    Browser browser = ContextProvider.getStateInfo().getDetectedBrowser();
    String value = browser.isAjaxEnabled() ? "true" : "false";
    return HTMLUtil.hiddenInput( RequestParams.AJAX_ENABLED, value );
  }
  
  /** returns the html code for hidden input fields, which are used to
    * register events that occured. */
  static String createEventHandlerFields() {
    StringBuffer html = new StringBuffer();
    HTMLUtil.hiddenInput( html, 
                          WebActionEvent.FIELD_NAME, 
                          RequestParams.NOT_OCCURED );
    HTMLUtil.hiddenInput( html, 
                          WebItemEvent.FIELD_NAME, 
                          RequestParams.NOT_OCCURED );
    HTMLUtil.hiddenInput( html, 
                          WebFocusGainedEvent.FIELD_NAME, 
                          RequestParams.NOT_OCCURED );
    // TODO [rh] render the following events only on demand
    HTMLUtil.hiddenInput( html, WebTreeNodeExpandedEvent.FIELD_NAME, 
                          RequestParams.NOT_OCCURED );
    HTMLUtil.hiddenInput( html, WebTreeNodeCollapsedEvent.FIELD_NAME, 
                          RequestParams.NOT_OCCURED );
    
    HTMLUtil.hiddenInput( html, "changeImage", "" );
    HTMLUtil.hiddenInput( html, DragDropEvent.FIELD_NAME_SOURCE, "" );
    HTMLUtil.hiddenInput( html, DragDropEvent.FIELD_NAME_DESTINATION, "" );
    HTMLUtil.hiddenInput( html, 
                          DoubleClickEvent.FIELD_NAME, 
                          RequestParams.NOT_OCCURED );
    return html.toString();
  }


  /** append the instance identifiers of a WebForm to its hidden fields */
  static String createFormIdentifiers() {
    StringBuffer html = new StringBuffer();
    String uiRootId = LifeCycleHelper.createUIRootId();
    HTMLUtil.hiddenInput( html, RequestParams.UIROOT, uiRootId );
    return html.toString();
  }
  
  static String createFocusElement() {
    // TODO: [fappel] Don't set focus on the originating window if a popup
    //                window is requested, since IE brings the originating
    //                window to front. Think about a better solution...
    WebForm[] allForms = FormManager.getAll();
    boolean popUpRequest = false;
    for( int i = 0; !popUpRequest && i < allForms.length; i++ ) {
      WebForm form = allForms[ i ];
      String opBuf = getFormAdapter( form ).getWindowOpenerBuffer();
      popUpRequest = !opBuf.equals( "" ) || form.isOpeningNewWindow();
    }
    
    String result = "";
    if( popUpRequest ) {
      result = HTMLUtil.hiddenInput( RequestParams.FOCUS_ELEMENT, "" ); 
    } else {
      String focusElementId = "";
      WebForm activeForm = FormManager.getActive();
      if( activeForm != null && activeForm.retrieveFocusID() != null )
      {
        focusElementId = activeForm.retrieveFocusID();
      }
      SelectionUtil.Selection selection = SelectionUtil.retrieveSelection();
      if ( selection != null ) {
        result = HTMLUtil.hiddenInput( RequestParams.FOCUS_ELEMENT, 
                                       focusElementId + selection.toString() );
      } else {
        result = HTMLUtil.hiddenInput( RequestParams.FOCUS_ELEMENT, 
                                       focusElementId );
      }
    }
    return result;
  }

  ///////////////////
  // Helper methods to render JavaScript code for window management
  
  /** gets the javascript code for opening WebForms in new browser windows */
  static String createWindowOpener() {
    StringBuffer script = new StringBuffer();
    IServiceStateInfo stateInfo = ContextProvider.getStateInfo();
    WebForm[] allForms = FormManager.getAll();
    String opBuf = "";
    for( int i = 0; i < allForms.length; i++ ) {
      WebForm form = allForms[ i ];
      if( stateInfo.isExpired() ) {
        opBuf = getFormAdapter( form ).getWindowOpenerBuffer();
        if( !opBuf.equals( "" ) ) {
          script.append( opBuf );
        }
      } else {
        if ( WebComponentControl.openInNewWindow( form ) ) {
          String buffer = createOpenNewWindowScript( form );
          script.append( buffer );
          getFormAdapter( form ).addWindowOpenerBuffer( buffer );
        } else {
          getFormAdapter( form ).addWindowOpenerBuffer( "" );
        }
      }
    }
    return script.toString();
  }

  /** gets the javascript code for closing browser windows */
  // TODO [rh] figure out if more than one refreshWindow request can be sent
  static String createWindowCloser() {
    StringBuffer script = new StringBuffer();
    String close = "";
    WebForm activeForm = FormManager.getActive();
    boolean firstRefreshFound = false;
    IWindow[] allWindows = WindowManager.getAll();
    // Send refresh-requests to all forms but the active form, those will then
    // be the active one and actually closed
    for( int i = 0; i < allWindows.length; i++ ) {
      IWindow window = allWindows[ i ];
      if( WindowManager.getActive() != window ) {
        // refresh the first form of the forms, which are to close
        if(    !firstRefreshFound 
            && WindowManager.isClosing( window ) 
            && !WindowManager.isClosed( window ) ) 
        {
          firstRefreshFound = true;
          WebForm form = WindowManager.getLastForm( window );
          script.append( createWindowRefresher( form, window.getId() ) );
        }
      }
    }
    // close the active form directly
    IServiceStateInfo stateInfo = ContextProvider.getStateInfo();
    IWindow activeWindow = WindowManager.getActive();
    if( WindowManager.isClosing( activeWindow ) ) {
      if( stateInfo.isExpired() ) {
        close = getFormAdapter( activeForm ).getWindowCloserBuffer();
      } else {
        close = "windowManager.closeWindow();";
        getFormAdapter( activeForm ).addWindowCloserBuffer( close );
        WindowManager.setClosed( activeWindow, true );
      }
    }
    script.append( close );
    return script.toString();
  }
  
  /** gets the javascript code for refreshing browser windows */
  static String createWindowRefresher() {
    StringBuffer result = new StringBuffer();
    IServiceStateInfo stateInfoe = ContextProvider.getStateInfo();
    String refrBuf = "";
    String buffer = "";
    WebForm[] allForms = FormManager.getAll();
    for( int i = 0; i < allForms.length; i++ ) {
      WebForm form = allForms[ i ];
      if( stateInfoe.isExpired() ) {
        refrBuf = getFormAdapter( form ).getWindowRefresherBuffer();
        if( !refrBuf.equals( "" ) ) {
          result.append( refrBuf );
        }
      } else {
        if ( WebComponentControl.refreshWindow( form ) ) {
          IWindow window = WindowManager.getInstance().findWindow( form );
          buffer = createWindowRefresher( form, window.getId() );
          result.append( buffer );
          getFormAdapter( form ).addWindowRefresherBuffer( buffer );
        } else {
          getFormAdapter( form ).addWindowRefresherBuffer( "" );
        }
      }
    }
    return result.toString();
  }

  static String createWindowRefresher( final WebForm form, 
                                       final String windowId )
  {
    String url = RenderUtil.createEncodedFormGetURL( form );
    StringBuffer result = new StringBuffer( "refreshWindow( '" ); 
    result.append( url );
    result.append( "', '" );
    result.append( windowId );
    result.append( "', '" );
    result.append( form.getWindowProperties().toString() );
    result.append( "');" );
    return result.toString();
  }

  ///////////////////
  // Opening/closing <form> tag
  
  static String createOpenForm() {
    WebForm wf = FormManager.getActive();
    final StringBuffer html = new StringBuffer();
    html.append( "<form" );
    HTMLUtil.attribute( html, HTML.ID, wf.getUniqueID() );    
    HTMLUtil.attribute( html, HTML.ACTION, URLHelper.getEncodedURLString() );
    HTMLUtil.attribute( html, HTML.NAME, "W4TForm" );
    HTMLUtil.attribute( html, HTML.METHOD, HTML.POST );
    HTMLUtil.attribute( html, HTML.ACCEPT_CHARSET, HTML.CHARSET_NAME_UTF_8 );
    html.append( createFormEncoding( wf ) );
    html.append( ">" );
    return html.toString();
  }

  static String createCloseForm() {
    return "</form>";
  }
  
  static String createFormEncoding( final WebForm form ) {
    String result;
    IFileUploadAdapter adapter 
      = ( IFileUploadAdapter )form.getAdapter( IFileUploadAdapter.class );
    if( adapter.isMultipartFormEncoding() ) {
      result = HTMLUtil.attribute( HTML.ENCTYPE, HTML.ENCTYPE_FORM_DATA );
    } else {
      //Bug im Tomcat :(
      //result.append( "text/plain" );
      result = HTMLUtil.attribute( HTML.ENCTYPE, HTML.ENCTYPE_FORM_URLENCODED );
    }
    adapter.setMultipartFormEncoding( false );
    return result;
  }
  
  /** <p>Creates the &lt;script&gt; tags for all JavaScript libraries which
   * were registered at the given <code>writer</code>.</p> */
  static String createJSLibTags( final HtmlResponseWriter writer ) {
    StringBuffer result = new StringBuffer();
    String[] libraries = writer.getJSLibraries();
    for( int i = 0; i < libraries.length; i++ ) {
      String location;
      location = ResourceManagerImpl.getInstance().getLocation( libraries[ i ] );
      result.append( RenderUtil.createJavaScriptLink( location ) );
    }
    return result.toString();
  }
  
  ////////////////////
  // Helper methods
  
  private static String createOpenNewWindowScript( final WebForm form ) {
    IWindow window = WindowManager.getInstance().findWindow( form );
    StringBuffer buffer = new StringBuffer();
    buffer.append( "openNewWindow('" );
    buffer.append( RenderUtil.createEncodedFormGetURL( form ) );
    buffer.append( "', '" );
    buffer.append( window.getId() );
    buffer.append( "', '" );
    buffer.append( form.getWindowProperties().toString() );
    buffer.append( "');" );
    return buffer.toString();
  }
}
