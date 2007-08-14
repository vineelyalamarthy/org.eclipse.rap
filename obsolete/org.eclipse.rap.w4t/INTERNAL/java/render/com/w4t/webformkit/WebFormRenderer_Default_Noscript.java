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

import org.eclipse.rwt.internal.browser.Ie;
import org.eclipse.rwt.internal.service.ContextProvider;
import org.eclipse.rwt.internal.service.IServiceStateInfo;
import org.eclipse.rwt.internal.util.HTML;
import org.eclipse.rwt.internal.util.HTMLUtil;

import com.w4t.*;
import com.w4t.IWindowManager.IWindow;
import com.w4t.engine.lifecycle.standard.EventQueueFilter;
import com.w4t.engine.util.*;
import com.w4t.internal.adaptable.IFormAdapter;


public class WebFormRenderer_Default_Noscript extends WebFormRenderer {
  
  public void readData( final WebComponent component ) {
    // initialize the event queue filter used in noscript mode
    IServiceStateInfo stateInfo = ContextProvider.getStateInfo();
    EventQueueFilter filter = new EventQueueFilter_Script();
    stateInfo.setAttribute( EventQueueFilter.ATTRIBUTE_KEY, filter );
  }
  
  void checkFormStack() {
    // TODO
  }
  
  public void prepare() {
    // do nothing in noscript mode
  }
  
  /** renders the basic html structure of the head of a page, which is
    * the same in every page */
  StringBuffer createPageHeader() {
    StringBuffer html = new StringBuffer();
    String ieSpecial = "";
    if( W4TContext.getBrowser() instanceof Ie ) {
      ieSpecial = "<!-- InternetExplorer special --><script>" 
                + "</script><!-- End InternetExplorer special -->";
    }
    html.append( HTML.DOCTYPE_4_0_TRANSITIONAL );
    html.append( "<html><head>" );
    WebFormUtil.renderTitle( html, WebFormUtil.getWindowTitle() );
    html.append( createCssClasses() );
    createCssReferences( html );
    html.append( getWindowOpener() );
    html.append( " " );
    html.append( getWindowCloser() );
    WebFormUtil.renderContentType( html );
    WebFormUtil.renderCacheControl( html );
    html.append( " " );
    html.append( ieSpecial );
    html.append( WebFormUtil.renderFavIcon() );
    html.append( "</head>" );
    html.append( "<body " );
    html.append( getBodyAttributes() );
    html.append( ">" );
    html.append( WebFormUtil.createOpenForm() );
    return html;
  }

  /** builds the String containing attributes for the <body> tag */
  StringBuffer getBodyAttributes() {
    StringBuffer result = new StringBuffer();
    WebForm form = FormManager.getActive();
    if( form != null ) {
      HTMLUtil.attribute( result, HTML.BGCOLOR, form.getBgColor().toString() );
      HTMLUtil.attribute( result, HTML.TEXT, form.getTextColor().toString() );
      HTMLUtil.attribute( result, HTML.LEFTMARGIN, form.getLeftmargin() );
      HTMLUtil.attribute( result, HTML.TOPMARGIN, form.getTopmargin() );
      HTMLUtil.attribute( result, HTML.MARGINHEIGHT, form.getMarginheight() );
      HTMLUtil.attribute( result, HTML.MARGINWIDTH, form.getMarginwidth() );
      result.append( " " );
      result.append( WebComponentControl.getUniversalAttributes( form ) );
      result.append( " " );
    }
    return result;
  }
  
  /** gets the redirect URL as workaround for opening 
    * a new window or refreshing a window (Noscript version) */
  private String getWindowOpener() {
    String result = "";
    IServiceStateInfo stateInfo = ContextProvider.getStateInfo();   
    WebForm activeForm = FormManager.getActive();
    
    WebForm[] allForms = FormManager.getAll();
    boolean finished = false;
    for( int i = 0; !finished && i < allForms.length; i++ ) {
      WebForm form = allForms[ i ];
      IFormAdapter formAdapter = WebFormUtil.getFormAdapter( form );
      if( stateInfo.isExpired() ) {
        if( !formAdapter.getWindowOpenerBuffer().equals( "" ) ) {
          result = formAdapter.getWindowOpenerBuffer();
          finished = true;
        }
      } else {
        if(    WebComponentControl.openInNewWindow( form )
            || WebComponentControl.refreshWindow( form ) )
        {
          result =   "<meta http-equiv=\"refresh\" content=\"0; url=" 
                   + RenderUtil.createEncodedFormGetURL( form )
                   + "\">";
          formAdapter.addWindowOpenerBuffer( result );
          NoscriptFormStack.getInstance().push( activeForm );
          finished = true;
        } else {
          formAdapter.addWindowOpenerBuffer( "" );
        }
      }
    }
    return result;
  }
  
  private String getWindowCloser() {
    String result = "";
    boolean isActiveForm = false;
    WebForm[] allForms = FormManager.getAll();
    // remove all closed windows except the active window
    for( int i = 0; i < allForms.length; i++ ) {
      WebForm form = allForms[ i ];
      if( form != FormManager.getActive() ) {
        IWindow window = W4TContext.getWindowManager().findWindow( form );
        if ( window != null && WindowManager.isClosing( window ) ) {
          NoscriptFormStack.getInstance().remove( form );
        }
      }
    }
    // 'close' active window
    IWindow activeWindow = WindowManager.getActive();
    if(     WindowManager.isClosing( activeWindow ) 
        && !WindowManager.isClosed( activeWindow ) )
    {
      isActiveForm = true;
      NoscriptFormStack.getInstance().remove( FormManager.getActive() );
      WindowManager.setClosed( activeWindow, true );
    }
    // create meta tag to redirect to previous form
    if( isActiveForm && !NoscriptFormStack.getInstance().isEmpty() ) {
      WebForm newForm = NoscriptFormStack.getInstance().pop();
      result =   "<meta http-equiv=\"refresh\" content=\"0; url="
               + RenderUtil.createEncodedFormGetURL( newForm )
               + "\">";
    }
    return result;
  }
}