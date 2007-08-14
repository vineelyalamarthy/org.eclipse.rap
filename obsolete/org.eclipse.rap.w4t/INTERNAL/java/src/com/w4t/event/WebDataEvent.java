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
 * <p>An instance of this class is sent if the value of an
 * <code>IInputValueHolder</code> was changed by user input.</p>
 * <p>This class is not intended to be instantiated or extended by clients.</p>
 * @see org.eclipse.rwt.event.WebDataListener
 */
public class WebDataEvent extends WebEvent {

  private static final Class LISTENER = WebDataListener.class;
  /** Marks the first integer id for the range of data event ids. */
  public static final int DATA_FIRST = 11101;
  /** Marks the last integer id for the range of data event ids. */
  public static final int DATA_LAST = 11101;
  /** A value changed event type. */
  public static final int VALUE_CHANGED = DATA_FIRST;

  /** <p>the value that the source of this WebDataEvent had before
    * the value has changed.</p> */
  protected String oldValue;
  /** <p>the value that the source of this WebDataEvent has now
    * (i.e. after the value has changed).</p> */
  protected String newValue;


  /** 
   * <p>Creates a new instance of this class.</p>
   * @param source the source WebComponent at which the event occured, must not
   * be <code>null</code>
   * @param id the type of the event
   * @param oldValue the value that source had before the value has changed
   * @param newValue the value that source has now (i.e. after the value
   * has changed)
   */
  public WebDataEvent( final Object source,
                       final int id,
                       final String oldValue,
                       final String newValue )
  {
    super( source, id );
    this.oldValue = oldValue;
    this.newValue = newValue;
  }

  /** 
   * <p>The value that the source of this <code>WebDataEvent</code> had before
   * the value was changed.</p>
   */
  public String getOldValue() {
    return oldValue;
  }

  /** 
   * <p>The value that the source of this <code>WebDataEvent</code> has now
   * (i.e. after the value was changed).</p>
   */
  public String getNewValue() {
    return newValue;
  }

  protected void dispatchToObserver( final Object listener ) {
    ( ( WebDataListener )listener ).webValueChanged( this );
  }

  protected Class getListenerType() {
    return LISTENER;
  }
  
  /**
   * <p>Returns whether the given <code>adaptable</code> has registered any 
   * listeners of type <code>WebDataListener</code>.</p>
   * @see WebEvent#hasListener(Adaptable, Class)
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
   * @see WebEvent#addListener(Adaptable, Class, Object)
   */
  public static void addListener( final Adaptable adaptable,
                                  final WebDataListener listener )
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
   * @see WebEvent#removeListener(Adaptable, Class, Object)
   */
  public static void removeListener( final Adaptable adaptable,
                                     final WebDataListener listener )
  {
    removeListener( adaptable, LISTENER, listener );
  }
  
  /**
   * <p>Returns all <code>WebDataEventListener</code>s for the given 
   * <code>adaptable</code>. An empty array is returned if no listeners 
   * are registered.</p> 
   * @param adaptable the adaptable for which the listneners should be returned.
   * Must not be <code>null</code>.
   */
  public static Object[] getListeners( final Adaptable adaptable ) {
    return getListener( adaptable, LISTENER );
  }
}