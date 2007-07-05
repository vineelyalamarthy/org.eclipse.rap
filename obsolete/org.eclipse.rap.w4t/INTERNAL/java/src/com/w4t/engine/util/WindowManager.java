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
package com.w4t.engine.util;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.Hashtable;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import com.w4t.*;
import com.w4t.engine.service.ContextProvider;
import com.w4t.engine.service.IServiceStateInfo;

/**
 * <p>This class maintains a list of (browser) windows and their associated 
 * <code>WebForm</code>s.</p>
 * <p>The class is not intended to be used by clients.</p>
 */
//TODO [fappel] check synchronizations
public class WindowManager
  extends SessionSingletonBase
  implements IWindowManager
{
  
  private static final String ACTIVE_WINDOW = "COM_W4T_ACTIVE_WINDOW";
  
  /** 
   * <p>Associates windows and their contained forms. (key: form id (String) 
   * value: instance of Window)</p>
   */
  private final Hashtable formIdToWindowMap;
  
  ////////////////
  // inner classes
  
  private final class Window implements IWindow {

    private final String id = createId();
    private WebForm formToDispatch;
    private String lastFormId;
    private boolean closing;
    private boolean closed;
    
    public String getId() {
      return id;
    }

    public void setFormToDispatch( final WebForm formToDispatch ) {
      ParamCheck.notNull( formToDispatch, "formToDispatch" );
      // TODO: [fappel] this is not safe in case of multiple requests...
      if( getInstance().findWindow( formToDispatch ) != null ) {
        String text;
        text = "The form with id ''{0}'' is already associated with a window.";
        Object[] args = new Object[] { formToDispatch.getUniqueID() };
        String msg = MessageFormat.format( text, args );
        throw new IllegalStateException( msg );
      }
      this.formToDispatch = formToDispatch;
    }

    public WebForm getFormToDispatch() {
      return formToDispatch;
    }

    public void close() {
      closing = true;
      closed = false; 
    }

    boolean isClosing() {
      return closing;
    }
    
    boolean isClosed() {
      return closed;
    }

    void setClosing( final boolean closing ) {
      this.closing = closing;
    }
    
    void setClosed( final boolean closed ) {
      this.closed = closed;
    }
  }

  /** 
   * <p>Helper class that creates session unique identifiers for window 
   * instances.</p> 
   */
  private static class IdBuilder {
    
    private int counter = 0;
    
    private synchronized String newId() {
      counter++;
      StringBuffer result = new StringBuffer( "w" );
      result.append( counter );
      return result.toString();
    }
  }
  

  /** <p>Creates a new session singleton instance.</p> */
  private WindowManager() {
    formIdToWindowMap = new Hashtable();
  }
  
  /**
   * <p>Returns the session-singleton instance of <code>WindowManager</code>.
   * </p>
   */
  public static WindowManager getInstance() {
    return ( WindowManager )getInstance( WindowManager.class );
  }
  
  /**
   * <p>Returns the active <code>IWindow</code>, that is, the window which issued 
   * the current request.</p>
   */
  public static IWindow getActive() {
    IServiceStateInfo stateInfo = ContextProvider.getStateInfo();
    return ( IWindow )stateInfo.getAttribute( ACTIVE_WINDOW );
  }

  /**
   * <p>Sets the given <code>window</code> as the active window.</p>
   * @see #getActive()
   */
  public static void setActive( final IWindow window ) {
    IServiceStateInfo stateInfo = ContextProvider.getStateInfo();
    stateInfo.setAttribute( ACTIVE_WINDOW, window );
  }
  
  /**
   * <p>Does the actual dispatching for the active window (see 
   * {@link #getActive() <code>getActive()</code>}). If there is no form to 
   * dispatch to, nothing is done.</p> 
   */
  public static WebForm doDispatch() {
    WebForm result = FormManager.getActive();
    Window window = ( Window )getActive();
    WebForm formToDispatch = window.getFormToDispatch();
    if( formToDispatch != null ) {
      WebForm lastForm = getLastForm( window );
      WindowManager manager = getInstance();
      if( lastForm != null ) {
        manager.formIdToWindowMap.remove( lastForm.getUniqueID() );
      }
      manager.formIdToWindowMap.put( formToDispatch.getUniqueID(), window );
      window.lastFormId = formToDispatch.getUniqueID();
      result = formToDispatch;
    }
    FormManager.setActive( result );
    return result;
  }

  /**
   * <p>Removes the <code>IWindow</code> which is associated with the given 
   * <code>form</code> form the list of 'known' windows. If there is no
   * associated window nothing is done.</p>
   * @param form the <code>WebForm</code> instance whose associated window
   * should be removed. Must not be <code>null</code>.
   * @throws NullPointerException when <code>form</code> is <code>null</code>.
   */
  public static void removeAssociatedWindow( final WebForm form ) {
    ParamCheck.notNull( form, "form" );
    Window window = ( Window )getInstance().findWindow( form );
    if( window != null && form.getUniqueID().equals( window.lastFormId ) ) {
      getInstance().remove( window );
    }
  }
  
  /**
   * <p>Returns the most recent <code>WebForm</code> which was displayed in
   * the given <code>window</code>.<p> 
   */
  // TODO [rh] remove this method its functionality is covered by findForm()
  public static WebForm getLastForm( final IWindow window ) {
    return FormManager.findById( ( ( Window )window ).lastFormId );
  }
  
  /** <p>Resets the current form to be dispatched.</p> */
  public static void afterRender() {
    ( ( Window )getActive() ).formToDispatch = null;
  }

  /**
   * <p>Marks the given <code>window</code> as 'in the process of being closed'.
   * </p>
   */
  public static void setClosing( final IWindow window, final boolean closing ) {
    Window internalWindow = ( Window )window;
    internalWindow.setClosing( closing );
  }
  
  /**
   * <p>Marks the given <code>window</code> as 'being closed'.</p>
   */
  public static void setClosed( final IWindow window, final boolean closed ) {
    Window internalWindow = ( Window )window;
    internalWindow.setClosed( closed );
  }

  /** 
   * <p>Returns whether the given <code>window</code> is marked as 'to be 
   * closed'.</p> 
   */
  public static boolean isClosing( final IWindow window ) {
    Window internalWindow = ( Window )window;
    return internalWindow.isClosing();
  }
  
  /** <p>Returns whether the given <code>window</code> is closed.</p> */
  public static boolean isClosed( final IWindow window ) {
    Window internalWindow = ( Window )window;
    return internalWindow.isClosed();
  }

  /**
   * <p>Returns an array of all <code>IWindow</code>s known by this 
   * <code>WindowManager</code>. An empty array is returned if no windows
   * exist.</p>
   */
  public static IWindow[] getAll() {
    Collection windows = getInstance().formIdToWindowMap.values();
    IWindow[] result = new IWindow[ windows.size() ];
    windows.toArray( result );
    return result;
  }
  
  ///////////////////////////////////
  // implementation of IWindowManager
  
  /**
   * <p>Creates an <code>IWindow</code> for the given <code>form</code> and
   * associates the two.</p>
   * @param form the <code>WebForm</code> to create an <code>IWindow</code> for.
   * Must not be <code>null</code>.
   * @throws NullPointerException when <code>form</code> is <code>null</code>.
   * @throws IllegalStateException when the given <code>form</code> is already
   * associated with an <code>IWindow</code>. 
   */
  public IWindow create( final WebForm form ) {
    ParamCheck.notNull( form, "form" );
    if( findWindow( form ) != null ) {
      String text;
      text = "The form with id ''{0}'' is already associated with a window.";
      Object[] args = new Object[] { form.getUniqueID() };
      String msg = MessageFormat.format( text, args );
      throw new IllegalStateException( msg );
    }
    Window result = new Window();
    result.lastFormId = form.getUniqueID();
    formIdToWindowMap.put( form.getUniqueID(), result );
    return result;
  }
  
  /**
   * <p><code>Returns the <code>IWindow</code> which has the given
   * <code>id</code> or <code>null</code> if no window with the given
   * <code>id</code> exists.</p>
   * @param id the id to search for, must not be <code>null</code>.
   * @throws NullPointerException when <code>id</code> is <code>null</code>. 
   */
  public IWindow findById( final String id ) {
    ParamCheck.notNull( id, "id" );
    IWindow result = null;
    Object[] windows = formIdToWindowMap.values().toArray();
    for( int i = 0; result == null && i < windows.length; i++ ) {
      IWindow window = ( IWindow )windows[ i ];
      if( id.equals( window.getId() ) ) {
        result = window;
      }
    }
    return result;
  }

  /**
   * <p>Returns the <code>window</code> which is associated with the given 
   * <code>form</code>.</p>
   * @param form the <code>WebForm</code> to find the associated window for,
   * must not be <code>null</code>.
   * @throws NullPointerException when <code>form</code> is <code>null</code>. 
   */
  public IWindow findWindow( final WebForm form ) {
    ParamCheck.notNull( form, "form" );
    return ( IWindow )formIdToWindowMap.get( form.getUniqueID() );
  }

  /**
   * <p>Returns the <code>WebForm</code> which is associated with the given 
   * <code>window</code>.</p>
   */
  public WebForm findForm( final IWindow window ) {
    ParamCheck.notNull( window, "window" );
    return FormManager.findById( ( ( Window )window ).lastFormId );
  }
  
  /**
   * <p>Removes the given <code>window</code> from the list.</p>
   * <p>Removing a window which is not maintained by this list is ignored 
   * silently.</p>  
   * @param window the window to be removed, must not be <code>null</code>.
   * @throws NullPointerException when <code>window</code> is <code>null</code>.
   */ 
  public void remove( final IWindow window ) {
    ParamCheck.notNull( window, "window" );
    formIdToWindowMap.remove( ( ( Window )window ).lastFormId );
  }
  
  
  //////////////////
  // helping methods
  
  private static String createId() {
    return   getServletContextName() 
           + ( ( IdBuilder )getInstance( IdBuilder.class ) ).newId();
  }
  
  private static String getServletContextName() {
    String result = "";
    HttpSession session = ContextProvider.getRequest().getSession();
    ServletContext servletContext = session.getServletContext();
    if( servletContext != null ) {
      String contextName = servletContext.getServletContextName();
      if( contextName != null ) {
        result = normalizeServletContextName( contextName );
      }
    }
    return result;
  }

  private static String normalizeServletContextName( final String name ) {
    StringBuffer result = new StringBuffer( name );
    int i = 0;
    while( i < result.length() ) {
      char c = result.charAt( i );
      if(    c >= '0' && c <= '9' 
          || c >= 'a' && c <= 'z'
          || c >= 'A' && c <= 'Z'
          || c == '_' ) 
      {
        i++;
      } else {
        result.delete( i, i + 1 );
      }
    }
    if(    result.length() > 0 
        && result.charAt( 0 ) >= '0' && result.charAt( 0 ) <= '9' )
    {
      result.insert( 0, '_' ); 
    }
    return result.toString();
  }

}