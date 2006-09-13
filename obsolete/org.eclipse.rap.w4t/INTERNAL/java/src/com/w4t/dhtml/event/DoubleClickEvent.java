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
package com.w4t.dhtml.event;

import com.w4t.Adaptable;
import com.w4t.event.WebEvent;


/** TODO [rh] JavaDoc */
public class DoubleClickEvent extends WebEvent {

  private static final Class LISTENER = DoubleClickListener.class;
  
  public static final int DOUBLE_CLICK_PERFORMED = 10001;

  public static final String PREFIX = "dce";
  public static final String FIELD_NAME = "w4tDoubleClickEvent";
  
  public DoubleClickEvent( final Object source, final int id ) {
    super( source, id );
  }

  protected void dispatchToObserver( final Object listener ) {
    ( ( DoubleClickListener )listener ).doubleClickPerformed( this );
  }

  protected Class getListenerType() {
    return LISTENER;
  }

  public static boolean hasListener( final Adaptable adaptable ) {
    return hasListener( adaptable, LISTENER );
  }

  public static void addListener( final Adaptable adaptable, 
                                  final DoubleClickListener listener ) 
  {
    addListener( adaptable, LISTENER, listener );
  }

  public static void removeListener( final Adaptable adaptable, 
                                     final DoubleClickListener listener )
  {
    removeListener( adaptable, LISTENER, listener );
  }

  public static Object[] getListeners( final Adaptable adaptable ) {
    return getListener( adaptable, LISTENER );
  }
}
