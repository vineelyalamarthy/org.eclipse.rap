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
 * <p>Instances of this class are sent as a result of a
 * <code>WebComponent</code> being rendered.</p>
 * <p>This class is not intended to be instantiated or extended by clients.</p>
 * @see org.eclipse.rwt.event.WebRenderListener
 * @see org.eclipse.rwt.event.WebRenderAdapter
 */
public class WebRenderEvent extends WebEvent {

  private static final Class LISTENER = WebRenderListener.class;
  /** <p>marks the first integer id for the range of render event ids.</p> */
  public static final int RENDER_FIRST = 10701;
  /** <p>marks the last integer id for the range of render event ids.</p> */
  public static final int RENDER_LAST = 10702;

  /** <p>the render performed event type for events before rendering.</p> */
  public static final int BEFORE_RENDER = RENDER_FIRST;
  /** <p>the render performed event type for events before rendering.</p> */
  public static final int AFTER_RENDER = RENDER_LAST;


  /** 
   * <p>Creates a new instance of this class.</p>
   * @param source the source WebComponent at which the event occured, must
   * not be <code>null</code>
   * @param id the type of the event
   */
  public WebRenderEvent( final Object source, 
                         final int id )
  {
    super( source, id );
  }


  protected void dispatchToObserver( final Object listener ) {
    switch( getID() ) {
      case BEFORE_RENDER:
        ( ( WebRenderListener )listener ).beforeRender( this );
      break;
      case AFTER_RENDER:
        ( ( WebRenderListener )listener ).afterRender( this );
      break;
      default:
        throw new IllegalStateException( "Invalid event handler type." );
    }
  }

  protected Class getListenerType() {
    return LISTENER;
  }
  
  /**
   * <p>Returns whether the given <code>adaptable</code> has registered any 
   * listeners of type <code>WebRenderListener</code>.</p>
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
                                  final WebRenderListener listener )
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
                                     final WebRenderListener listener )
  {
    removeListener( adaptable, LISTENER, listener );
  }

  /**
   * <p>Returns all <code>WenRenderListener</code>s for the given 
   * <code>adaptable</code>. An empty array is returned if no listeners 
   * are registered.</p> 
   * @param adaptable the adaptable for which the listneners should be returned.
   * Must not be <code>null</code>.
   */
  public static Object[] getListeners( final Adaptable adaptable ) {
    return getListener( adaptable, LISTENER );
  }
}