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

import com.w4t.*;

/** 
 * <p>This event occurs if a <code>WebComponent</code> is added to or removed 
 * from a <code>WebContainer</code> or if the <code>WebLayout</code> of a 
 * <code>WebContainer</code> is changed.</p>
 * <p>This class is not intended to be instantiated or extended by clients.</p>
 * @see org.eclipse.rwt.event.WebContainerListener
 * @see org.eclipse.rwt.event.WebContainerAdapter
 */
public class WebContainerEvent extends WebEvent {

  private static final Class LISTENER = WebContainerListener.class;
  /** Marks the first integer id for the range of container event ids. */
  public static final int CONTAINER_FIRST = 10801;
  /** Marks the last integer id for the range of container event ids. */
  public static final int CONTAINER_LAST = 10803;
  /** WebComponent added  event type. */
  public static final int COMPONENT_ADDED = CONTAINER_FIRST;
  /** WebComponent removed  event type. */
  public static final int COMPONENT_REMOVED = CONTAINER_FIRST + 1;
  /** WebLayout of the WebContainer was changed. */
  public static final int LAYOUT_CHANGED = CONTAINER_FIRST + 2;


  protected WebComponent child = null;
  protected Object constraint = null;
  protected WebLayout layout = null;

  /** 
   * <p>Constructor used for component operations (add/remove).</p>
   * @param source the source WebComponent at which the event occured, must
   * not be <code>null</code>
   * @param id the type of the event
   * @param child reference to the added or removed WebComponent
   * @param constraint the constraint of the child which was added or removed
   */
  public WebContainerEvent( final Object source,
                            final int id,
                            final WebComponent child,
                            final Object constraint )
  {
    super( source, id );
    if( id != COMPONENT_ADDED && id != COMPONENT_REMOVED ) {
      String msg =   "WebContainerEvent(): cannot create "
                   + "WebContainerEvent for component operations "
                   + "with wrong type id.";
      throw new IllegalArgumentException( msg );
    }
    this.child = child;
    this.constraint = constraint;
  }

  /** 
   * <p>Constructor used for layout operations</p>
   * @param source the source WebComponent at which the event occured, must not
   * be <code>null</code>
   * @param id the type of the event
   * @param layout reference to the new WebLayout used by the source
   * WebContainer
   */
  public WebContainerEvent( final Object source, 
                            final int id, 
                            final WebLayout layout )
  {
    super( source, id );
    if( id != LAYOUT_CHANGED ) {
      String msg =   "WebContainerEvent(): cannot create "
                   + "WebContainerEvent for layout operations "
                   + "with wrong type id.";
      throw new IllegalArgumentException( msg );
    }
    this.layout = layout;
  }

  /** 
   * <p>Returns the <code>WebComponent</code> that was added or removed.</p>
   * <p>In case of a layout change (see {@link 
   * WebContainerListener#webLayoutChanged(WebContainerEvent) }) 
   * <code>null</code> is returned.</p> 
   */
  public WebComponent getChild() {
    return child;
  }

  /** 
   * <p>Returns the constraint for the <code>WebComponent</code> that was 
   * added or removed.</p> 
   * <p>In case of a layout change (see {@link 
   * WebContainerListener#webLayoutChanged(WebContainerEvent) }) 
   * <code>null</code> is returned.</p> 
   */
  public Object getConstraint() {
    return constraint;
  }

  /** 
   * <p>Returns the <code>WebLayout</code> that was set.</p>
   */
  public WebLayout getWebLayout() {
    return layout;
  }

  /**
   * <p>Returns the originator of the event.</p> 
   */
  public WebContainer getContainer() {
    return ( WebContainer )getSource();
  }

  protected void dispatchToObserver( final Object listener ) {
    switch( getID() ) {
      case COMPONENT_ADDED:
        ( ( WebContainerListener )listener ).webComponentAdded( this );
      break;
      case COMPONENT_REMOVED:
        ( ( WebContainerListener )listener ).webComponentRemoved( this );
      break;
      case LAYOUT_CHANGED:
        ( ( WebContainerListener )listener ).webLayoutChanged( this );
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
   * listeners of type <code>WebContainerListener</code>.</p>
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
                                  final WebContainerListener listener )
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
                                     final WebContainerListener listener )
  {
    removeListener( adaptable, LISTENER, listener );
  }
  
  /**
   * <p>Returns all <code>WebContainerListener</code>s for the given 
   * <code>adaptable</code>. An empty array is returned if no listeners 
   * are registered.</p> 
   * @param adaptable the adaptable for which the listneners should be returned.
   * Must not be <code>null</code>.
   */
  public static Object[] getListeners( final Adaptable adaptable ) {
    return getListener( adaptable, LISTENER );
  }
}