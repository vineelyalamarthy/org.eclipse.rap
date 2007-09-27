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

package org.eclipse.ui.internal.servlet;

import java.io.*;
import java.text.MessageFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.rwt.internal.browser.Default;
import org.eclipse.rwt.internal.lifecycle.*;
import org.eclipse.rwt.internal.resources.ResourceManager;
import org.eclipse.rwt.internal.service.*;
import org.eclipse.rwt.internal.service.LifeCycleServiceHandler.ILifeCycleServiceHandlerConfigurer;
import org.eclipse.rwt.internal.service.LifeCycleServiceHandler.LifeCycleServiceHandlerSync;
import org.eclipse.rwt.internal.theme.ThemeUtil;
import org.eclipse.rwt.internal.util.HTML;
import org.eclipse.rwt.resources.IResourceManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.internal.graphics.TextSizeDetermination;
import org.eclipse.ui.internal.branding.Branding;
import org.eclipse.ui.internal.branding.BrandingRegistry;

class LifeCycleServiceHandlerConfigurer
  implements ILifeCycleServiceHandlerConfigurer
{

  private static final String PACKAGE_NAME 
    = LifeCycleServiceHandlerConfigurer.class.getPackage().getName();
  private final static String FOLDER = PACKAGE_NAME.replace( '.', '/' );
  private final static String INDEX_TEMPLATE = FOLDER + "/index.html";
  
  // TODO [fappel]: think about clusters
  // cache control variables
  private static int probeCount;
  private static long lastModified = System.currentTimeMillis();

  private final static LifeCycleServiceHandlerSync syncHandler
    = new RWTLifeCycleServiceHandlerSync();
  
  public InputStream getTemplateOfStartupPage() throws IOException {
    InputStream result = loadTemplateFile();
    InputStreamReader isr = new InputStreamReader( result );
    BufferedReader reader = new BufferedReader( isr );
    String line = reader.readLine();
    StringBuffer content = new StringBuffer();
    while( line != null ) {
      content.append( line );
      content.append( "\n" );
      line = reader.readLine();
    }
    setDummyBrowser();
    try {
      String libs = getLibraries();
      BrowserSurvey.replacePlaceholder( content, "${libraries}", libs );
      String appScript = getAppScript();
      BrowserSurvey.replacePlaceholder( content, "${appscript}", appScript );
      applyBranding( content );
    } finally {
      removeDummyBrowser();
    }
    return new ByteArrayInputStream( content.toString().getBytes() );
  }

  // TODO [fappel]: extend branding mechanism to work also for RWT/JFace
  //                standalone applications
  private void applyBranding( final StringBuffer content ) {
    HttpServletRequest request = ContextProvider.getRequest();
    String servletName = request.getServletPath();
    if( servletName.startsWith( "/" ) ) {
      servletName = servletName.substring( 1 );
    }
    String entrypoint = request.getParameter( RequestParams.STARTUP );
    BrandingRegistry brandingRegistry = BrandingRegistry.getInstance();
    Branding branding 
      = brandingRegistry.getBrandingFor( servletName, entrypoint );
    if( entrypoint == null ) {
      entrypoint 
        = brandingRegistry.getEntrypoint( branding.getDefaultEntrypointId() );
    }
    if( entrypoint == null ) {
      entrypoint = EntryPointManager.DEFAULT;
    }
    if( branding.getThemeId() != null ) {
      ThemeUtil.setCurrentTheme( branding.getThemeId() );
    }
    BrowserSurvey.replacePlaceholder( content, 
                                      "${body}", 
                                      branding.getBody() );
    BrowserSurvey.replacePlaceholder( content, 
                                      "${title}", 
                                      branding.getTitle() );
    BrowserSurvey.replacePlaceholder( content,
                                      "${headers}",
                                      branding.renderHeaders() );
    BrowserSurvey.replacePlaceholder( content, "${startup}", entrypoint );
    
    String exitMessage = branding.getExitMessage();
    String confirmationCall = "";
    if( exitMessage != null && exitMessage != "" ) {
      confirmationCall = "app.setExitConfirmation(\"" + exitMessage + "\");";
    }
    BrowserSurvey.replacePlaceholder( content,
                                      "${exitConfirmation}",
                                      confirmationCall );
  }
  
  private String getAppScript() throws IOException {
    fakeWriter();
    IServiceStateInfo stateInfo = ContextProvider.getStateInfo();
    HtmlResponseWriter writer = stateInfo.getResponseWriter();
    writer.startElement( HTML.SCRIPT, null );
    writer.writeText( "safd", null );
    writer.clearBody();
    try {
      // TODO: [fappel] this works only as long as only one display per
      //                session is supported...
      DisplayUtil.writeAppScript( "w1" );
      return getContent( writer );
    } finally {
      restoreWriter();
    }
  }
  
  private void restoreWriter() {
    IServiceStateInfo stateInfo = ContextProvider.getStateInfo();
    String key = LifeCycleServiceHandlerConfigurer.class.getName();
    HtmlResponseWriter writer
      = ( HtmlResponseWriter )stateInfo.getAttribute( key );
    stateInfo.setResponseWriter( writer );
  }

  private void fakeWriter() {
    IServiceStateInfo stateInfo = ContextProvider.getStateInfo();
    HtmlResponseWriter original = stateInfo.getResponseWriter();
    String key = LifeCycleServiceHandlerConfigurer.class.getName();
    stateInfo.setAttribute( key, original );
    HtmlResponseWriter fake = new HtmlResponseWriter();
    stateInfo.setResponseWriter( fake );
  }

  private void setDummyBrowser() {
    String id = ServiceContext.DETECTED_SESSION_BROWSER;
    ContextProvider.getSession().setAttribute( id, new Default( true ) );
  }
  
  private void removeDummyBrowser() {
    String id = ServiceContext.DETECTED_SESSION_BROWSER;
    ContextProvider.getSession().setAttribute( id, null );
  }

  private String getLibraries() throws IOException {
    fakeWriter();
    try {
      DisplayUtil.writeLibraries();
      return getContent( ContextProvider.getStateInfo().getResponseWriter() );
    } finally {
      restoreWriter();
    }
  }

  private String getContent( final HtmlResponseWriter writer ) {
    StringBuffer msg = new StringBuffer();
    for( int i = 0; i < writer.getBodySize(); i ++ ) {
      msg.append( writer.getBodyToken( i ) );
    }
    return msg.toString();
  }

  private InputStream loadTemplateFile() throws IOException {
    InputStream result = null;
    IResourceManager manager = ResourceManager.getInstance();
    ClassLoader buffer = manager.getContextLoader();
    manager.setContextLoader( EngineConfigWrapper.class.getClassLoader() );
    try {        
      result = manager.getResourceAsStream( INDEX_TEMPLATE );
      if ( result == null ) {
        String text =   "Failed to load Browser Survey HTML Page. "
                      + "Resource {0} could not be found.";
        Object[] param = new Object[]{ INDEX_TEMPLATE };
        String msg = MessageFormat.format( text, param );
        throw new IOException( msg );
      }
    } finally {
      manager.setContextLoader( buffer );          
    }
    return result;
  }

  public void registerResources() throws IOException {
    IResourceManager manager = ResourceManager.getInstance();
    ClassLoader buffer = manager.getContextLoader();
    manager.setContextLoader( SWT.class.getClassLoader() );
    try {
      manager.register( "resource/widget/rap/display/bg.gif" );
    } finally {
      manager.setContextLoader( buffer );
    }
  }

  public synchronized boolean isStartupPageModifiedSince() {
    boolean result;

    int currentProbeCount = TextSizeDetermination.getProbeCount();
    if( probeCount != currentProbeCount ) {
      lastModified = System.currentTimeMillis();
      probeCount = currentProbeCount;
    }
    
    HttpServletRequest request = ContextProvider.getRequest();
    HttpServletResponse response = ContextProvider.getResponse();
    long dateHeader = request.getDateHeader( "If-Modified-Since" );
    // Because browser store the date in format with seconds as smallest unit
    // add one second to avoid rounding problems...
    if(    dateHeader + 1000 < lastModified
        || RWTRequestVersionControl.hasChanged() )
    {
      result = true;
      response.addDateHeader( "Last-Modified", lastModified );
      // TODO [fappel]: Think about "expires"-header for proxy usage.
      // TODO [fappel]: Seems as if Safari doesn't react to last-modified. 
    } else {
      result = false;
      response.setStatus( HttpServletResponse.SC_NOT_MODIFIED );
    }
    return result;
  }

  public LifeCycleServiceHandlerSync getSynchronizationHandler() {
    return syncHandler;
  }
}