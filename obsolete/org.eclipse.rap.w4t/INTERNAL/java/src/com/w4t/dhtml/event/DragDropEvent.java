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

import com.w4t.dhtml.Item;
import com.w4t.dhtml.Node;
import com.w4t.event.WebEvent;


/**
  * <p>An Event for "Drag and Drop" between dhtml items.</p>
  * <p>A DragDropEvent is fired when the user hold the mouse down on an
  * Item (e.g. a TreeNode), moves it over another Item (which must be a
  * Node, e.g. TreeNode) and releases the mouse. The first Item is then
  * considered to be "dragged" to the latter Item, which is notified by a
  * DragDropEvent.</p>
  */
public class DragDropEvent extends WebEvent {

  private static final Class LISTENER = DragDropListener.class;
  /** <p>marks the first integer id for the range of
    * item drag and drop event ids.</p> */
  public static final int DRAGDROP_FIRST = 10401;
  /** <p>marks the last integer id for the range of
    * item drag and drop event ids.</p> */
  public static final int DRAGDROP_LAST  = 10401;
  /** <p>an item drag and drop event type.</p> */
  public static final int DRAGDROP = DRAGDROP_FIRST;
  /** used for rendering internally. */
  public static final String PREFIX = "dde";
  
  public static final String FIELD_NAME_SOURCE = "dragSource";
  public static final String FIELD_NAME_DESTINATION = "dragDestination";

  /** <p>the source of this DragDropEvent.</p> */
  private Item dragSource;
  /** <p>the destination of this DragDropEvent.</p> */
  private Node dragDestination;


  /** <p>constructs a new DragDropEvent with the specified id, source
    * and destination.</p>
    *
    * @param dragSource      the source of this DragDropEvent, i.e. an
    *                        Item over which the mouse was moved and held
    *                        down to be later released over another Item
    *                        (which must be a Node).
    * @param dragDestination the destination of the drag and drop, i.e.
    *                        the Node to which the mouse was moved after
    *                        it was held down above an Item (which must
    *                        not be identical with the dragSource).
    * @param id              the event type id of this DragDropEvent.
    */
  public DragDropEvent( Item dragSource, Node dragDestination, int id ) {
    super( dragDestination, id );
    this.dragSource = dragSource;
    this.dragDestination = dragDestination;
  }

  /** <p>returns the source of this DragDropEvent as Item.</p>
    * <p>The source of a DragDropEvent is the Item over which the
    * mouse was moved and held down to be later released over another
    * Item (which must be a Node).</p>
    *
    * @return  the source of this DragDropEvent as Item (the same
    *          object as getSource(), only as correct type).
    */
  public Item getDragSource() {
    return dragSource;
  }

  /** <p>returns the destination of this DragDropEvent as Node.</p>
    * <p>The source of a DragDropEvent is the Node to which the
    * mouse was moved after it was held down above an Item (which is
    * considered as the source of the drag and drop).</p>
    *
    * @return  the destination of this DragDropEvent as Node.
    */
  public Node getDragDestination() {
    return dragDestination;
  }

  protected void dispatchToObserver( final Object listener ) {
    ( ( DragDropListener )listener ).receivedDragDrop( this );
  }
  
  protected Adaptable getEventSource() {
    return dragDestination;
  }

  protected Class getListenerType() {
    return LISTENER;
  }
  
  /**
   * <p>Returns whether the given <code>adaptable</code> has registered any 
   * listeners of type <code>WebActionListener</code>.</p>
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
                                  final DragDropListener listener )
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
                                     final DragDropListener listener )
  {
    removeListener( adaptable, LISTENER, listener );
  }

  /**
   * <p>Returns all <code>DragDropListener</code>s for the given 
   * <code>adaptable</code>. An empty array is returned if no listeners 
   * are registered.</p> 
   * @param adaptable the adaptable for which the listneners should be returned.
   * Must not be <code>null</code>.
   */
  public static Object[] getListeners( final Adaptable adaptable ) {
    return getListener( adaptable, LISTENER );
  }
}

