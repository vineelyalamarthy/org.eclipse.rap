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

import org.eclipse.rwt.Adaptable;

import com.w4t.event.WebEvent;

/**
  * <p>The WebTreeNodeExpandedEvent occurs when a node of a
  * tree view was clicked and has expanded.</p>
  */
public class WebTreeNodeExpandedEvent extends WebEvent {

  private static final Class LISTENER = WebTreeNodeExpandedListener.class;
  /** <p>Marks the first integer id for the range of
    * tree node collapsed event ids.</p> */
  public static final int TREENODE_EXPANDED_FIRST = 10501;
  /** <p>Marks the last integer id for the range of
    * tree node collapsed event ids.</p> */
  public static final int TREENODE_EXPANDED_LAST  = 10501;
  /** <p>A tree node collapsed event type.</p> */
  public static final int TREENODE_EXPANDED = TREENODE_EXPANDED_FIRST;

  /** used for rendering internally. */
  public static final String PREFIX = "tne";
  public static final String FIELD_NAME = "webTreeNodeExpandedEvent";


  /** Constructor */
  public WebTreeNodeExpandedEvent( Object source, int id ) {
    super( source, id );
  }

  protected void dispatchToObserver( final Object listener ) {
    ( ( WebTreeNodeExpandedListener)listener ).webTreeNodeExpanded( this );
  }

  protected Class getListenerType() {
    return LISTENER;
  }
  
  /**
   * <p>Returns whether the given <code>adaptable</code> has registered any 
   * listeners of type <code>WebTreeNodeExpandedListener</code>.</p>
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
                                  final WebTreeNodeExpandedListener listener )
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
   * @param lsnr the listener to be removed, must not be <code>null</code>.
   * @throws NullPointerException when <code>listener</code> or 
   * <code>adaptable</code> is <code>null</code> 
   * @see WebEvent#removeListener(Adaptable, Class, Object)
   */
  public static void removeListener( final Adaptable adaptable,
                                     final WebTreeNodeExpandedListener lsnr )
  {
    removeListener( adaptable, LISTENER, lsnr );
  }

  /**
   * <p>Returns all <code>WebTreeNodeExpandedListener</code>s for the given 
   * <code>adaptable</code>. An empty array is returned if no listeners 
   * are registered.</p> 
   * @param adaptable the adaptable for which the listneners should be returned.
   * Must not be <code>null</code>.
   */
  public static Object[] getListeners( final Adaptable adaptable ) {
    return getListener( adaptable, LISTENER );
  }
}

