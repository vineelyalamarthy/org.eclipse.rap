package com.w4t.event;

import com.w4t.Adaptable;

public abstract class Event {
  
  private final Object source;
  private final int id;

  
  public Event( final Object source, int id ) {
    this.source = source;
    this.id = id;
  }
  
  /** <p>returns the event type of this WebEvent.</p> */
  public int getID() {
    return id;
  }
  
  public Object getSource() {
    return source;
  }

  protected static IEventAdapter getEventAdapter( final Adaptable adaptable ) {
    return ( IEventAdapter )adaptable.getAdapter( IEventAdapter.class );
  }

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

  private Adaptable getEventSource() {
    return ( Adaptable )source;
  }

  protected abstract void dispatchToObserver( final Object listener );

  protected abstract Class getListenerType();

  protected static boolean hasListener( final Adaptable adaptable,
                                        final Class listenerType )
  {
    return getEventAdapter( adaptable ).hasListener( listenerType );
  }

  protected static Object[] getListener( final Adaptable adaptable, 
                                         final Class listenerType )
  {
    return getEventAdapter( adaptable ).getListener( listenerType );
  }

  protected static void addListener( final Adaptable adaptable, 
                                     final Class listenerType, 
                                     final Object listener )
  {
    getEventAdapter( adaptable ).addListener( listenerType, listener );
  }

  protected static void removeListener( final Adaptable adaptable, 
                                        final Class listenerType,
                                        final Object listener )
  {
    getEventAdapter( adaptable ).removeListener( listenerType, listener );
  }
}