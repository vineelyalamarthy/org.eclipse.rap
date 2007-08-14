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

import org.eclipse.rwt.internal.service.*;
import org.eclipse.rwt.internal.util.HTML;
import org.eclipse.rwt.internal.util.HTMLUtil;

import com.w4t.*;
import com.w4t.engine.lifecycle.standard.EventQueueFilter;
import com.w4t.engine.util.FormManager;
import com.w4t.engine.util.WindowManager;


public class WebFormRenderer_Default_Script extends WebFormRenderer {
  
  static final String ID_W4T_USERDEFINED_SCRIPTS = "w4t_userdefined_scripts";
  
  public void readData( final WebComponent component ) {
    WebForm form = ( WebForm )component;
    WebFormReadDataUtil.read( form );
    // initialize the event queue filter used in script mode
    IServiceStateInfo stateInfo = ContextProvider.getStateInfo();
    EventQueueFilter filter = new EventQueueFilter_Script();
    stateInfo.setAttribute( EventQueueFilter.ATTRIBUTE_KEY, filter );
  }
    
  void checkFormStack() {
    // we do nothing in script here
  }
  
  public void prepare() {
    // do nothing in script mode
  }
    
  /** renders the basic html structure of the head of a page, which is
    * the same in every page */
  StringBuffer createPageHeader() {
    WebForm form = FormManager.getActive();
    StringBuffer html = new StringBuffer();
    html.append( HTML.DOCTYPE_4_0_TRANSITIONAL );
    html.append( "<html><head>" );
    WebFormUtil.renderTitle( html, WebFormUtil.getWindowTitle() );
    html.append( createCssClasses() );
    WebFormUtil.renderContentType( html );
    WebFormUtil.renderCacheControl( html );
    createCssReferences( html );
    html.append( WebFormUtil.createJSLibTags( getResponseWriter() ) );
    createScreenPositioning( html );
    html.append( WebFormUtil.renderFavIcon() );
    html.append( "</head>" );
    
    html.append( "<body " );
    html.append( getBodyAttributes() );
    String resize = createBodyOnResize();
    if( !"".equals( resize ) ) {
      HTMLUtil.attribute( html, HTML.ON_RESIZE, resize );
    }
    html.append( " onload=\"windowManager.initWebForm( '" );  
    html.append( form.isEnableBrowserNavigation() );
    html.append( "' );" );
    html.append( getFocus() );
    html.append( getScrollWindow() );
    html.append( " windowManager.setName( '" );
    html.append( WindowManager.getActive().getId() );
    html.append( "' );" );      
    html.append( WebFormUtil.createWindowOpener() );
    html.append( WebFormUtil.createWindowRefresher() );
    html.append( WebFormUtil.createWindowCloser() );
    html.append( TriggerTimeStamp.getOnLoadCode() );
    html.append( "\"" );
    HTMLUtil.attribute( html, 
                        HTML.ON_UNLOAD, 
                        TriggerTimeStamp.getOnUnloadCode() );
    html.append( " >" );
    html.append( WebFormUtil.createOpenForm() );
    html.append( "<div" );
    HTMLUtil.attribute( html, HTML.ID, ID_W4T_USERDEFINED_SCRIPTS );
    html.append( "></div>");
    html.append( WebFormUtil.createEventHandlerFields() );
    html.append( WebFormUtil.createFocusElement() );
    HTMLUtil.hiddenInput( html, RequestParams.SCROLL_X, "" );
    HTMLUtil.hiddenInput( html, RequestParams.SCROLL_Y, "" );
    HTMLUtil.hiddenInput( html, RequestParams.AVAIL_WIDTH, "0" );
    HTMLUtil.hiddenInput( html, RequestParams.AVAIL_HEIGHT, "0" );
    return html;
  }
  
  String createBodyOnResize() {
    return "";
  }

  /** appends markup for the trigger timestamp and closes the &lt;body&gt; and 
   * &lt;html&gt; tag. */
  String createPageFooter() {
    StringBuffer result = new StringBuffer();
    result.append( TriggerTimeStamp.getHTMLCode() );
    result.append( "</body></html>" );
    return result.toString();
  }

  private void createScreenPositioning( final StringBuffer html ) {
    StringBuffer script = new StringBuffer();
    script.append( "windowManager.posX = " + getScreenX() + ";" );
    script.append( "windowManager.posY = " + getScreenY() + ";" );
    String scriptTag = RenderUtil.createJavaScriptInline( script.toString() );
    html.append( scriptTag );
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

  /** gets the x coordinate for the window position */
  private static String getScreenX() {
    String xPos = "-1";
    WebForm wf = FormManager.getActive();
    if( wf != null ) {
      if( !wf.getWindowProperties().getScreenX().equals( "" ) ) {
        xPos = wf.getWindowProperties().getScreenX();
      }
    }
    return xPos ;
  }

  /** gets the y coordinate for the window position */
  private static String getScreenY() {
    String yPos = "-1";
    WebForm wf = FormManager.getActive();
    if( wf != null ) {
      if( !wf.getWindowProperties().getScreenY().equals( "" ) ) {
        yPos = wf.getWindowProperties().getScreenY();
      }
    }
    return yPos ;
  }

  /** returns the scrollWindow handler javaScript function call:
    * if the WebForm is to preserve it's scrollbar positions,
    * the correct scrollbar positions are set with this call at onLoad */
  private static String getScrollWindow() {
    String result = "";
    WebForm wf = FormManager.getActive();
    if( wf != null && wf.isAutoScroll() ) {
      result = "windowManager.scrollWindow( "
             + String.valueOf( wf.getScrollX() )
             + ", "
             + String.valueOf( wf.getScrollY() )
             + " );";
    }
    return result;
  }

  /** gets the javascript code for setting focus
    * on form or a WebComponent. */
  static StringBuffer getFocus() {
    StringBuffer result = new StringBuffer();
    WebForm activeForm = FormManager.getActive();
    String focusElement = activeForm.retrieveFocusID();
    result.append( "eventHandler.disableFocusEvent();" );
    if( focusElement.equals( "" ) ) {
      // do nothing
    } else if( focusElement.equals( activeForm.getUniqueID() ) ) {
      result.append( "window.focus();" );
    } else {
      result.append( "if( document.W4TForm." + focusElement + " ) {" );
      result.append( "document.W4TForm." + focusElement + ".focus();" );
      result.append( "}" );
    }
    result.append( "setTimeout( 'eventHandler.enableFocusEvent()', 50 );" );
    return result;
  }

}
