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

import com.w4t.Adaptable;
import com.w4t.WebComponent;


/** <p>The base class for all WebEvents.</p>
  * <p>The WebEvent has a source Object, which 'fires' the event.
  * Every WebEvent class has its own listener interface. A listener 
  * interface has one or more method declarations - the types of
  * the event. For each of these types an integer constants is defined. 
  * These ids are set as a parameter of the WebEvents constructor.</p>
  * <p>Example implementation of a WebEvent and its listener:</p>
  * <pre>
  * public class MyEvent extends WebEvent {
  * 
  *   public static void addListener( Adaptable adaptable, MyListener listener ) {
  *     addListener( adaptable, MyListener.class, listener );
  *   }
  *   
  *   public static void removeListener( Adaptable adaptable, MyListener listener ) {
  *     removeListener( adaptable, MyListener.class, listener );
  *   }
  *   
  *   public static Object[] getListeners( Adaptable adaptable ) {
  *     return getListener( adaptable, MyListener.class );
  *   }
  *    
  *   public MyEvent( Object source, int id ) {
  *     super( source, id );
  *   }
  *   
  *   protected void dispatchToObserver( Object listener ) {
  *     ( ( MyListener )listener ).eventOccured( this );
  *   }
  *   
  *   protected Class getListenerType() {
  *     return MyListener.class;
  *   }
  * }
  * 
  * public interface MyListener {
  *   void eventOccured( MyEvent event ); 
  * }
  * </pre>
  * <p>The static <code>add/removeListener</code> methods should be implemented
  * in order be called from the component's <code>add/removeXXXListener</code>
  * methods.</p>
  */
public abstract class WebEvent {

  /** the WebEvent type */
  private int id;
  /** the source object on which this event occured */
  private Object source;
  
  /**
    * Constructor
    * @param source the source WebComponent at which the event occured
    * @param id the type of the event
    */
  public WebEvent( final Object source, final int id ) {
    this.source = source;
    this.id     = id;
  }
  
  /** <p>returns the object on which this event occured.</p> */
  public Object getSource() {
    return source;
  }

  /** <p>returns the event type of this WebEvent.</p> */
  public int getID() {
    return id;
  }

  /** <p>returns the source (as WebComponent) on which this event occured,
    * if it is of type WebComponent, or null else.</p> */
  public WebComponent getSourceComponent() {
    WebComponent result = null;
    if( source instanceof WebComponent ) {
      result = ( WebComponent )source;
    }
    return result;
  }

  /** <p>returns the ID for the EventSemantics that was associated in an
    * application with the source of this WebEvent.</p> */
  public int getEventSemanticsID() {
    int result = 0;

    WebComponent wcSource = getSourceComponent();
    if( wcSource != null ) {
      result = wcSource.getEventSemanticsID();
    }

    return result;
  }

  /** <p>returns whether the passed ID for an EventSemantics was associated
    * in an application with the source of this <code>WebEvent</code>.</p> */
  public boolean hasSemantics( final int eventSemanticsId ) {
    boolean result = false;
    WebComponent wcSource = getSourceComponent();
    if( wcSource != null ) {
      int sourceSemanticsId = ( wcSource.getEventSemanticsID() );
      result = ( ( sourceSemanticsId & eventSemanticsId ) == eventSemanticsId );
    }
    return result;
  }
  
  /**
   * <p>Notifies all registered listeners about this event.</p> 
   */
  public void processEvent() {
    IEventAdapter eventAdapter = getEventAdapter( getEventSource() );
    if( eventAdapter.hasListener( getListenerType() ) ) {
      Object[] listener = eventAdapter.getListener( getListenerType() );
      for( int i = 0; i < listener.length; i++ ) {
        // TODO: [fappel] Exception handling ? 
        dispatchToObserver( listener[ i ] );
      }
    }
  }
  
  /**
   * <p>Returns the source for this event.</p>
   */
  protected Adaptable getEventSource() {
    return ( Adaptable )getSource();
  }
  
  /**
   * <p>Notifies the given <code>listener</code> about the occurence of this
   * event.</p>
   * @param listener the listener to be notified. Will be of type {@link 
   * #getListenerType() <code>getListenerType()</code>}
   */
  protected abstract void dispatchToObserver( final Object listener );
  
  /**
   * <p>Returns the listener interface that can be used to observe this 
   * <code>WebEvent</code>.</p>
   * <p>For example instances of <code>WebActionEvent</code> return  
   * <code>WebActionListener</code>.</p>
   */
  protected abstract Class getListenerType();

  /**
   * <p>Returns an <code>IEventAdapter</code> for the given 
   * <code>adaptable</code>.</p>
   * @param adaptable the adaptable to obtain an IEventAdapter for, must not be 
   * <code>null</code>.
   */
  protected static IEventAdapter getEventAdapter( final Adaptable adaptable ) {
    return ( IEventAdapter )adaptable.getAdapter( IEventAdapter.class );
  }
  
  /**
   * <p>Returns <code>true</code> if the given <code>adaptable</code> has any 
   * listeners of type <code>listenerType</code> registered; <code>false</code>
   * otherwise.</p>
   * @param adaptable the adaptable to be queried, must not be
   * <code>null</code>.
   * @param listenerType the listener type to be queried, must not be
   * <code>null</code>.
   */
  protected static boolean hasListener( final Adaptable adaptable,
                                        final Class listenerType )
  {
    return getEventAdapter( adaptable ).hasListener( listenerType );
  }
  
  /**
   * <p>Returns all listeners of the given <code>listenerType</code> which
   * are registered at the given <code>adaptable</code>. In case that there
   * are no listeners registered, an empty array is returned.</p>
   * @param adaptable the adaptable whose listeners shoule be returned. Must
   * not be <code>null</code>. 
   * @param listenerType the listener type, must not be <code>null</code>.
   */
  protected static Object[] getListener( final Adaptable adaptable,
                                         final Class listenerType )
  {
    return getEventAdapter( adaptable ).getListener( listenerType );
  }
  
  /**
   * <p>Adds the given <code>listener</code> to the <code>adaptable</code> which 
   * will be notified when an event occurs for the <code>adaptable</code> that 
   * matches the given <code>listenerType</code>.</p>
   * <p>The <code>listener</code> argument must be an instance of
   * <code>listenerType</code> or a class derived from 
   * <code>listenerType</code>.</p>
   * @param adaptable the adaptable on which the listener will be added. Must
   * not be <code>null</code>.
   * @param listenerType the type of listener, must not be <code>null</code>. 
   * @param listener the listener to be added, must not be <code>null</code>.
   * @throws NullPointerException when one of the arguments is 
   * <code>null</code>.
   * @throws IllegalArgumentException when <code>listener</code> is not 
   * compatible with <code>listenerType</code>.
   * @see #removeListener(Adaptable, Class, Object)
   */
  protected static void addListener( final Adaptable adaptable,
                                     final Class listenerType,
                                     final Object listener )
  {
    getEventAdapter( adaptable ).addListener( listenerType, listener );
  }
  
  /**
   * <p>Removes the given <code>listener</code> form the listeners of the 
   * given <code>adaptable</code>.</p>
   * <p>This ist the counterpart of the 
   * {@link #addListener(Adaptable, Class, Object) 
   * <code>addListener(Adaptable, Class, Object)</code>} method and thus must 
   * be called with the exact same arguments to succeed.</p>
   * @param adaptable the adatable from which the listener is to be removed. Must
   * not be <code>null</code>.
   * @param listenerType the listener type as it was used during registration 
   * with <code>addListener(Adaptable, Class, Object)</code>. Must not be 
   * <code>null</code>.  
   * @param listener the listener as it was used during registration with
   * <code>addListener(Adaptable, Class, Object)</code>. Must not be 
   * <code>null</code>. 
   */
  protected static void removeListener( final Adaptable adaptable,
                                        final Class listenerType,
                                        final Object listener )
  {
    
    getEventAdapter( adaptable ).removeListener( listenerType, listener );
  }
}