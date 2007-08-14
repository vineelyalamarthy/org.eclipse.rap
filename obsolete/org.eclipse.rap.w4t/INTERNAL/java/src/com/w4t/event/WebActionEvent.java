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
package com.w4t.event;

import org.eclipse.rwt.Adaptable;


/** 
 * <p>An instance of this class is sent as a result of a user action such as
 * a <code>WebButton</code> that was clicked.</p>
 * @see org.eclipse.rwt.event.WebActionListener
 */
public class WebActionEvent extends WebEvent {

  private static final Class LISTENER = WebActionListener.class;
  
  /** JavaScript Event for the webActionPerformed Event */
  public final static String WEB_ACTION_PERFORMED_HANDLER
    = " onClick=\"eventHandler.webActionPerformed(''{0}'')\" ";
  /** Marks the first integer id for the range of action event ids. */
  public static final int ACTION_FIRST = 10001;
  /** Marks the last integer id for the range of action event ids. */
  public static final int ACTION_LAST = 10001;
  /** An action performed event type. */
  public static final int ACTION_PERFORMED = ACTION_FIRST;

  public static final String PREFIX = "wae";
  public static final String FIELD_NAME = "webActionEvent";
  
  /** 
   * <p>Creates a new instance of this class.</p>
   * @param source the source WebComponent at which the event occured, must
   * not be <code>null</code>
   * @param id the type of the event
   */
  public WebActionEvent( final Object source, final int id ) {
    super( source, id );
  }

  protected void dispatchToObserver( final Object listener ) {
    ( ( WebActionListener )listener ).webActionPerformed( this );
  }

  protected Class getListenerType() {
    return LISTENER;
  }

  /**
   * <p>Returns whether the given <code>adaptable</code> has registered any 
   * listeners of type <code>WebActionListener</code>.</p>
   * @see org.eclipse.rwt.event.WebEvent#hasListener(Adaptable, Class)
   */
  public static boolean hasListener( final Adaptable adaptable ) {
    return hasListener( adaptable, LISTENER );
  }
  
  /**
   * <p>Adds the given <code>listener</code> to the <code>adatable</code> that
   * will be notified when events of the class that defines this method occur.
   * </p>
   * <p>This method is only intended to be called by the components' event 
   * registration code.</p>
   * @see org.eclipse.rwt.event.WebEvent#addListener(Adaptable, Class, Object)
   */
  public static void addListener( final Adaptable adaptable,
                                  final WebActionListener listener )
  {
    addListener( adaptable, LISTENER, listener );
  }
  
  /**
   * <p>Removes the given <code>listener</code> from the given
   * <code>adaptable</code>.</p>
   * <p>This method is only intended to be called by the components' event 
   * deregistration code.</p>
   * @param adaptable the adaptable on which the listener is registered, must 
   * not be <code>null</code>.
   * @param listener the listener to be removed, must not be <code>null</code>.
   * @throws NullPointerException when <code>listener</code> or 
   * <code>adaptable</code> is <code>null</code> 
   * @see org.eclipse.rwt.event.WebEvent#removeListener(Adaptable, Class, Object)
   */
  public static void removeListener( final Adaptable adaptable,
                                     final WebActionListener listener )
  {
    removeListener( adaptable, LISTENER, listener );
  }

  /**
   * <p>Returns all <code>WebActionListener</code>s for the given 
   * <code>adaptable</code>. An empty array is returned if no listeners 
   * are registered.</p> 
   * @param adaptable the adaptable for which the listneners should be returned.
   * Must not be <code>null</code>.
   */
  public static Object[] getListeners( final Adaptable adaptable ) {
    return getListener( adaptable, LISTENER );
  }
}