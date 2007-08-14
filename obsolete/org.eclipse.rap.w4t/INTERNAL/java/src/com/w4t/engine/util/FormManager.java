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
import java.util.*;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.rwt.SessionSingletonBase;
import org.eclipse.rwt.internal.lifecycle.LifeCycle;
import org.eclipse.rwt.internal.service.ContextProvider;
import org.eclipse.rwt.internal.service.IServiceStateInfo;
import org.eclipse.rwt.internal.util.ParamCheck;

import com.w4t.WebComponentControl;
import com.w4t.WebForm;
import com.w4t.event.WebFormEvent;

/**
 * <p>Maintains a list of <code>WebForm</code>s for the current session.</p>
 * <p>This class is not intended to be used by clients.</p>
 */
public class FormManager extends SessionSingletonBase {

  private static final String ACTIVE_FORM = "COM_W4T_ACTIVE_FORM";
  
  /**
   * <p>
   * the internal data structure for the WebForm list.
   * </p>
   */
  private final Map formList;

  /**
   * <p>
   * whether the startup form for the W4TModel is already loaded.
   * </p>
   */
  private boolean preloaded;

  /**
   * <p>
   * private constructor, in order to ensure singleton instance of FormList.
   * </p>
   */
  private FormManager() {
    formList = new Hashtable();
    preloaded = false;
  }

  /**
   * <p>
   * Returns a reference to the singleton instance of <code>FormManager</code>.
   * </p>
   */
  private static FormManager getInstance() {
    return ( FormManager )getInstance( FormManager.class );
  }

  /**
   * <p>Returns the currently active <code>WebForm</code>.</p>
   */
  public static WebForm getActive() {
    IServiceStateInfo stateInfo = ContextProvider.getStateInfo();
    return ( WebForm )stateInfo.getAttribute( ACTIVE_FORM );
  }

  /**
   * <p>Sets the given <code>form</code> as the currently active form.</p>
   * @param form the form to be set as the active form. May be
   * <code>null</code>.
   */
  public static void setActive( final WebForm form ) {
    IServiceStateInfo stateInfo = ContextProvider.getStateInfo();
    stateInfo.setAttribute( ACTIVE_FORM, form );
  }
  
  /**
   * <p>Returns a list of all <code>WebForm</code>s known by the current 
   * session.</p>
   */
  public static WebForm[] getAll() {
    List buffer = new ArrayList();
    Iterator instanceLists = getInstance().getInstanceLists();
    while( instanceLists.hasNext() ) {
      Map instanceList = ( Map )instanceLists.next();
      Iterator instances = instanceList.values().iterator();
      while( instances.hasNext() ) {
        buffer.add( instances.next() );
      }
    }
    WebForm[] result = new WebForm[ buffer.size() ];
    buffer.toArray( result );
    return result;
  }

  /**
   * <p>Creates an instance of the <code>formClassName</code>.</p>
   * <p>Invoking this method is equivalent to: 
   * {@link #load(String, ClassLoader)
   * <pre>load( formClassName, currentLoader )</pre>}
   * where <code>currentLoader</code> denotes the defining class loader of the
   * current class.</p>
   * @param formClassName the class name to create an instance of. Must not 
   * be <code>null</code>.
   */
  public static WebForm load( final String formClassName ) {
    return load( formClassName, null );
  }

  /**
   * <p>Creates an instance of the <code>formClassName</code> using the given
   * <code>classLaoder</code>.
   * @param formClassName the class name to create an instance of, must not 
   * be <code>null</code>. The formClassName must denote a class which is 
   * derived from {@link WebForm <code>WebForm</code>}.
   * @param classLoader the <code>ClassLoader</code> that should be used to
   * load the form class. The defining class loader of the current class is
   * used if <code>classLoader</code> is <code>null</code>.
   */
  public static WebForm load( final String formClassName,
                              final ClassLoader classLoader )
  {
    Class classDefinition = null;
    WebForm result = null;
    try {
      classDefinition = loadClass( formClassName, classLoader );
      result = ( WebForm )classDefinition.newInstance();
    } catch( final Exception e ) {
      String text 
        = "Could not load WebForm ''{0}'' for the following reason:\n{1}.";
      String reason = e.getClass().getName() + ": " + e.getMessage();
      Object[] args = new Object[] { formClassName, reason };
      String msg = MessageFormat.format( text, args );
      throw new IllegalArgumentException( msg );
    }
    WebComponentControl.setWebComponents( result );
    if( !LifeCycle.isDevelopmentMode ) {
      WebFormEvent evt = new WebFormEvent( result, WebFormEvent.AFTER_INIT );
      evt.processEvent();
    }
    add( result );
    return result;
  }

  /**
   * <p>Adds the given <code>form</code> to the list of forms, maiantained
   * by the <code>FormManager</code>.</p>
   * @param form the form to be added, must not be <code>null</code>.
   */
  public static void add( final WebForm form ) {
    ParamCheck.notNull( form, "form" );
    String name = form.getClass().getName();
    Map instanceList = getInstance().getInstanceList( name );
    if( instanceList == null ) {
      instanceList = new Hashtable();
      getInstance().put( name, instanceList );
    }
    instanceList.put( form.getUniqueID(), form );
  }

  /**
   * <p>Removes the given <code>form</code> from the list of
   * <code>WebForm</code>s.</p>
   * <p>Removing <code>null</code> or a form that was not {@link #add(WebForm) 
   * added} previously is silently ignored.</p>
   * @param form the form to be removed.
   */
  public static void remove( final WebForm form ) {
    String name = form.getClass().getName();
    Map instanceList = getInstance().getInstanceList( name );
    if( instanceList != null ) {
      instanceList.remove( form.getUniqueID() );
    }
  }

  /**
   * <p>Clears the list of <code>WebForm</code>s.</p>
   */
  public static void clear() {
    getInstance().formList.clear();
  }

  /**
   * <p>Sets whether the startup form  for the <code>W4TModel</code> sould be 
   * preloaded.</p>
   */
  public static void setPreloaded( final boolean preloaded ) {
    getInstance().preloaded = preloaded;
  }

  /**
   * <p>Returns whether the startup form for the <code>W4TModel</code> is 
   * already loaded.</p>
   */
  public static boolean isPreloaded() {
    return getInstance().preloaded;
  }

  /**
   * <p>Returns the <code>WebForm</code> for the given <code>formId</code> or 
   * <code>null</code> if no form with the such an id exists.</p>
   * @param formId the id to search for
   */
  public static WebForm findById( final String formId ) {
    WebForm result = null;
    WebForm[] all = getAll();
    for( int i = 0; result == null && i < all.length; i++ ) {
      if( all[ i ].getUniqueID().equals( formId ) ) {
        result = all[ i ];
      }
    }
    return result;
  }

  // ////////////////
  // helping methods
  
  private Map getInstanceList( final String formName ) {
    return ( Map )formList.get( formName );
  }

  private void put( final String formName, final Map instanceList ) {
    formList.put( formName, instanceList );
  }

  private Iterator getInstanceLists() {
    return formList.values().iterator();
  }

  private static Class loadClass( final String formName,
                                  final ClassLoader classLoader )
    throws ClassNotFoundException
  {
    Class classDefinition;
    try {
      if( classLoader == null ) {
        classDefinition = Class.forName( formName );
      } else {
        classDefinition = classLoader.loadClass( formName );
      }
    } catch( final ClassNotFoundException cnfe ) {
      HttpServletRequest request = ContextProvider.getRequest();
      ClassLoader additionalLoader
        = ( ClassLoader )request.getAttribute( "W4T_ADDITIONAL_LOADER" );
      if( additionalLoader != null ) {
        classDefinition = additionalLoader.loadClass( formName );
      } else {
        throw cnfe;
      }
    }
    return classDefinition;
  }
}