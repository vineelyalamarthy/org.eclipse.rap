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

import org.eclipse.rwt.internal.*;
import org.eclipse.rwt.internal.browser.*;
import org.eclipse.rwt.internal.util.HTML;
import org.eclipse.rwt.internal.util.HTMLUtil;

import com.w4t.W4TContext;
import com.w4t.WebForm;
import com.w4t.engine.util.FormManager;


/**
 *  <p>This class generates the Javascript-and HTML-Code for triggering 
 *  (refresh) the form-timestamp.</p>
 * 
 *  <p>The two methods are open a new window after a given intervall and for DOM
 *   browsers just reload an image that triggers the timestamp-refresh.</p>
 */
// TODO [rh] TimeStamp trigger does not work for Opera 8
final class TriggerTimeStamp {
  
  private final static String ON_UNLOAD_CODE = "w4tClearKeepAlive();";
  
  private final static String JS_PREFIX = "triggerTimeStamp";
  private final static String JS_DOM_POSTFIX = "_DOM";
  private final static String JS_DEFAULT_POSTFIX = "";
  
  static String getOnLoadCode() {
    String result;
    if( isDOMBrowser() ) {
      result = getDOMOnLoadCode();
    } else {
      result = getDefaultOnLoadCode();
    }
    return result;
  }

  static String getOnUnloadCode() {
    return ON_UNLOAD_CODE;
  }

  static String getHTMLCode() {
    StringBuffer result = new StringBuffer();
    if( isDOMBrowser() ) {
      result.append( "<img" );
      String transparentGIF = "resources/images/transparent.gif";
      HTMLUtil.attribute( result, HTML.SRC, transparentGIF );
      HTMLUtil.attribute( result, HTML.NAME, "w4tTriggerTimeStampImg" );
      HTMLUtil.attribute( result, HTML.ID, "w4tTriggerTimeStampImg" );
      HTMLUtil.attribute( result, HTML.BORDER, "0" );
      HTMLUtil.attribute( result, HTML.HEIGHT, "1" );
      HTMLUtil.attribute( result, HTML.WIDTH, "1" );
      HTMLUtil.attribute( result, HTML.ALT, "keepAlive" );
      result.append( " />" );
    }
    return result.toString();
  }

  static String getClosingTimeout() {
    IConfiguration configuration = ConfigurationReader.getConfiguration();
    IInitialization initialization = configuration.getInitialization();
    long closingTimeOut = initialization.getClosingTimeout();
    WebForm activeForm = FormManager.getActive();
    if( activeForm != null && activeForm.getClosingTimeout() != -1 ) {
      closingTimeOut = activeForm.getClosingTimeout();
    }
    return String.valueOf( closingTimeOut / 2 );
  }

  private static String getDefaultOnLoadCode() {
    StringBuffer result = new StringBuffer();
    result.append( " active = window.setInterval( '" );
    result.append( "windowManager." );
    result.append( JS_PREFIX );
    result.append( JS_DEFAULT_POSTFIX );
    result.append( "()', " );
    result.append( getClosingTimeout() );
    result.append( ");" );
    return result.toString();
  }

  private static String getDOMOnLoadCode() {
    StringBuffer result = new StringBuffer();
    result.append( " active = window.setInterval( '" );
    result.append( JS_PREFIX );
    result.append( JS_DOM_POSTFIX );
    result.append( "()'," );
    result.append( getClosingTimeout() );
    result.append( ");" );
    return result.toString();
  }

  // FIXME [rh] move to Browser class e.g. isDOMCapable()
  private static boolean isDOMBrowser() {
    return    W4TContext.getBrowser() instanceof Mozilla
           || W4TContext.getBrowser() instanceof Konqueror
           || W4TContext.getBrowser() instanceof Ie
           || W4TContext.getBrowser() instanceof Opera
           || W4TContext.getBrowser() instanceof Safari;
  }
  
  private TriggerTimeStamp() {
    // prevent instantiation
  }
}
