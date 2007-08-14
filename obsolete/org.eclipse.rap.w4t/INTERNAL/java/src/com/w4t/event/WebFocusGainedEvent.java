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
import org.eclipse.rwt.internal.util.HTML;

/** 
 * <p>This event occurs if a focusable (see {@link org.eclipse.rwt.IFocusable
 * <code>IFocusable</code>}) component gained focus.</p> 
 * @see org.eclipse.rwt.event.WebFocusGainedListener
 */
public class WebFocusGainedEvent extends WebEvent {

  private static final Class LISTENER = WebFocusGainedListener.class;

  /** JavaScript Event for the webFocusGainedEvent */
  public final static String WEB_FOCUS_GAINED_HANDLER =
      "onFocus=\"eventHandler.webFocusGained(this)\"";
  public final static String JS_HANDLER = HTML.ON_FOCUS;
  public final static String JS_HANDLER_CALL 
    = "eventHandler.webFocusGained(this)";

  /** Marks the first integer id for the range of focus gained event ids. */
  public static final int FOCUS_GAINED_FIRST = 10301;
  /** Marks the last integer id for the range of focus gained event ids. */
  public static final int FOCUS_GAINED_LAST  = 10301;
  /** An focus gained event type. */
  public static final int FOCUS_GAINED = FOCUS_GAINED_FIRST;
  public static final String FIELD_NAME = "webFocusGainedEvent";

  /** Constructor
    * @param source the source WebComponent at which the event occured
    * @param id the type of the event
    */
  public WebFocusGainedEvent( final Object source, final int id ) {
    super( source, id );
  }

  protected void dispatchToObserver( final Object listener ) {
    ( ( WebFocusGainedListener )listener ).webFocusGained( this );
  }

  protected Class getListenerType() {
    return LISTENER;
  }

  /**
   * <p>Returns whether the given <code>adaptable</code> has registered any 
   * listeners of type <code>WebFocusGainedListener</code>.</p>
   * @see WebEvent#hasListener(Adaptable, Class)
   */
  public static boolean hasListener( final Adaptable adaptable ) {
    return hasListener( adaptable, LISTENER );
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
   * @see WebEvent#removeListener(Adaptable, Class, Object)
   */
  public static void removeListener( final Adaptable adaptable,
                                     final WebFocusGainedListener listener )
  {
    removeListener( adaptable, LISTENER, listener );
  }

  /**
   * <p>Adds the given <code>listener</code> to the <code>adatable</code> that
   * will be notified when events of the class that defines this method occur.
   * </p>
   * <p>This method is only intended to be called by the components' event 
   * registration code.</p>
   * @see WebEvent#addListener(Adaptable, Class, Object)
   */
  public static void addListener( final Adaptable adaptable, 
                                  final WebFocusGainedListener listener )
  {
    addListener( adaptable, LISTENER, listener );
  }
  
  /**
   * <p>Returns all <code>WebFocusGainerListener</code>s for the given 
   * <code>adaptable</code>. An empty array is returned if no listeners 
   * are registered.</p> 
   * @param adaptable the adaptable for which the listneners should be returned.
   * Must not be <code>null</code>.
   */
  public static Object[] getListeners( final Adaptable adaptable ) {
    return getListener( adaptable, LISTENER );
  }
}