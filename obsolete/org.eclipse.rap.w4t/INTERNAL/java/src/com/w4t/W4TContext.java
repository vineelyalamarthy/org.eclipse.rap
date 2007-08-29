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

import java.util.Locale;

import org.eclipse.rwt.internal.*;
import org.eclipse.rwt.internal.browser.Browser;
import org.eclipse.rwt.internal.lifecycle.LifeCycleFactory;
import org.eclipse.rwt.internal.resources.ResourceManagerImpl;
import org.eclipse.rwt.internal.service.ContextProvider;
import org.eclipse.rwt.internal.service.IServiceStateInfo;
import org.eclipse.rwt.internal.util.ParamCheck;
import org.eclipse.rwt.lifecycle.ILifeCycle;
import org.eclipse.rwt.resources.IResourceManager;

import com.w4t.IWindowManager.IWindow;
import com.w4t.engine.util.*;
import com.w4t.internal.adaptable.IFormAdapter;
import com.w4t.util.*;


/** <p>The W4TContext is a central place to go for functionality that applies 
  * to or retrieves information from the whole web application, like loading 
  * WebForms, getting global statistics etc. (comparable to the VM's 
  * java.lang.System class).</p>
  */
public final class W4TContext {

  private W4TContext() {
    // to prevent instantiation
  }
  
  /** <p>Returns a WebComponentStatistics with information about the 
    * components, uptime etc. on the web application.</p>
    *
    * @return  a WebComponentStatistics with information about 
    *          the components, uptime etc. on the web application.
    */
  public static WebComponentStatistics getStatistics() {
    return W4TModelList.getInstance().getStatistics();
  }

  /** <p>Returns a WebComponentStatistics with information about the 
    * components, uptime etc. on the web application.</p>
    *
    * @param sessionOnly  if true, the returned WebComponentStatistics 
    *                     contains information only about the components
    *                     in the session, else the statistics information
    *                     is global (covering all sessions).
    * @return             a WebComponentStatistics with information about 
    *                     the components, uptime etc. on the web application.
    */
  public static WebComponentStatistics getStatistics( 
    final boolean sessionOnly ) 
  {
    return sessionOnly ? getStatisticsFromSessionRegistry() 
                       : getStatistics();
  }

  /** <p>Adds the specified message to the message queue. The content of the
    * message queue is displayed in a separate window once per request, if 
    * not empty.</p>
    *
    * @param message the message to be displayed on the message box. */
  public static void addMessage( final Message message ) {
    MessageHandler handler = MessageHandler.getInstance();
    handler.enqueue( message );
  }
  
  /** 
   * <p>Returns a Browser instance that represents vendor-specific and 
   * version-specific information about the web browser that is used 
   * on the client side to display the pages from the current session.</p>
   * @see Browser
   */  
  public static Browser getBrowser() {
    return ContextProvider.getBrowser();
  }
  
  /** <p> loads a WebForm instance into the sessions scope.</p>
   *
   *  @param formName fully qualified name of the WebForm to load */
  public static WebForm loadForm( final String formName ) {
    return FormManager.load( formName );
  }
  
  /** <p>loads a WebForm instance into the sessions scope.</p>
   *
   *  @param formName fully qualified name of the WebForm to load
   *  @param classLoader the classLoader which is used to create a new Instance
   *  of the specified WebForm */
  public static WebForm loadForm( final String formName,
                                  final ClassLoader classLoader ) 
  {
    return FormManager.load( formName, classLoader );
  }
  
  /**
   * <p>
   * Displays the given <code>form</code> in the browser window which issued
   * the request and thus replaces the one currently being displayed.
   * </p>
   * @param form the form to be shown. Must not be <code>null</code>. 
   * @see #showInNewWindow(WebForm)
   */
  // TODO [rh] throw IllegalStateExcp if called in beforeRender phase or later 
  public static void dispatchTo( final WebForm form ) { 
    WindowManager.getActive().setFormToDispatch( form );
  }
  
  /**
   * <p>Returns the {@link IWindowManager <code>WindowManager</code>} for the
   * current session.</p>
   */
  public static IWindowManager getWindowManager() {
    return WindowManager.getInstance();
  }
  
  /**
   * <p>Shows the given <code>form</code> in a popup window.</p>
   * <p><strong>Note:</strong> to actually show the form in another  window, 
   * the browser must have JavaScript enabled. If JavaScript is not available
   * the form is displayed in the same window that issued the request.</p>
   * @param form the form to be shown. Must not be <code>null</code>.
   * @see #dispatchTo(WebForm)
   */
  public static IWindow showInNewWindow( final WebForm form ) {
    ParamCheck.notNull( form, "form" );
    IWindow result;
    if( form.isOpeningNewWindow() ) {
      result = WindowManager.getInstance().findWindow( form );
    } else {
      IFormAdapter adapter;
      adapter = ( IFormAdapter )form.getAdapter( IFormAdapter.class );
      adapter.showInNewWindow( true );
      result = WindowManager.getInstance().findWindow( form );
      if( result == null ) {
        result = WindowManager.getInstance().create( form );
      }
    }
    return result;
  }
  
  /** returns the absolute path of the web applications base directory on the
   *  local file system. */
  public static String getWebAppBase() {
    IEngineConfig engineConfig = ConfigurationReader.getEngineConfig();
    return engineConfig.getServerContextDir().toString();
  }

  /** <p>resolves the passed string to its internationalized equivalent,
    * if it is a property URI that specifies a key in a resource bundle
    * property file. Else the string is returned unchanged.</p>
    *
    * <p>See the <a href="package-summary.html#i18n">package documentation</a> 
    * for more information about internationalization of W4 Toolkit 
    * applications.</p>
    *
    * <p>The behaviour of this method in case that key is a valid property URI
    * (specifying a key in a property resource file which may be locale 
    * specific) and the resource is missing (property file not found or 
    * doesn't contain the key) can be controlled via the <code>
    * handleMissingI18NResource</code> attribute in the configuration file
    * W4T.xml in the WEB-INF/conf/ directory of the web application.</p>
    *
    * @return the internationalized value for key, if key is in 
    *         valid property URI format, or key else  
    */
  public static String resolve( final String string ) {
    return RenderUtil.resolve( string );
  }
  
  /** <p>Localizes the session with the specified Locale. The 
    * internationalization functionality of the library will use this Locale
    * then instead of the {@link org.eclipse.rwt.internal.browser.Browser#getLocale() Locale of the 
    * Browser}.</p> */
  public static void setLocale( final Locale locale ) {
    SessionLocale.set( locale );
  }
  
  /** <p>returns the manager, which is responsible for registering resources
   * like images, css files etc. which are stored in libraries. The registered
   * files will be read out from their libraries and delivered if requested.
   * Usually libraries are stored in the WEB-INF/lib directory of 
   * a webapplication</p> */ 
  public static IResourceManager getResourceManager() {
    return ResourceManagerImpl.getInstance();
  }
  
  /** <p>returns the request instance of the underlying 
   *  Request/Response-API (usually HttpServletRequest) which is
   *  currently processed. </p>*/
  public static Object getRequest() {
    return ContextProvider.getRequest();
  }
  
  /** <p>returns the session implementation of the underlying 
   *  Request/Response-API (usually HttpSession) to which the
   *  context namespace belongs to. </p>*/
  public static Object getSession() {
    return ContextProvider.getSession();
  }

  /** <p>returns the response instance of the underlying 
   *  Request/Response-API (usually HttpServletResponse) which belongs
   *  to the request which is currently processed. </p>*/
  public static Object getResponse() {
    return ContextProvider.getResponse();
  }
  
  /** <p>Returns the AdapterManager implementation provided by the
   *  W4Toolkit runtime per session.</p> */
  public static AdapterManager getAdapterManager() {
    return AdapterManagerImpl.getInstance();
  }
  
  /** <p>Returns the <code>ILifeCycle</code> for the current session.</p> */
  public static ILifeCycle getLifeCycle() {
    return LifeCycleFactory.getLifeCycle();
  }
  
  /** 
   * <p>
   * Marks the current session to be invalidated. The actual invalidation 
   * of the session happens after the response was sent.
   * </p>
   * <p>If a session is invalidated, an internal form will be rendered that 
   * informs the user about the invalidation. Thus all further manipulation
   * of the component-tree is useless.
   * </p>
   * <p>
   * The above mentioned 'exit-form' can be customized. Therefore an html file 
   * called <code>exit.html</code> must be placed in the context-root. 
   * </p>
   * <strong>Note:</strong> Only call this method during event processing; 
   * otherwise its behaviour is undefined.
   * </p> 
   */
  public static void invalidate() {
    IServiceStateInfo stateInfo = ContextProvider.getStateInfo();
    stateInfo.setInvalidated( true );
  }
  
  // helping methods
  //////////////////
  
  private static WebComponentStatistics getStatisticsFromSessionRegistry() {
    return WebComponentRegistry.getInstance().getStatistics();
  }

}